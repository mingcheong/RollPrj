/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.tree;

import com.foundercy.pf.control.PfTreeNode;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


class DataChangeActionListener implements DataChangeListener {
    CustomTree tree;

    DataChangeActionListener(CustomTree tree) {
        this.tree = tree;
    }

    /**
     * DataSet发生变化的时候改变树结点信息
     */
    public void onDataChange(DataChangeEvent event) throws Exception {
        DataSet dataSet = event.getDataSet();
        if (event.type() != DataChangeEvent.FIELD_MODIRED) {
			return;
		}
        Field field = (Field) event.getSource();
        String fieldName = field.getName();
        String id = dataSet.fieldByName(tree.getIdName()).getString();
        if (field.isLatestModified()) {
            DefaultMutableTreeNode node = getNode(field, id);
            dataSet.maskDataChange(true);
            tree.maskValueChange(true);
            if (fieldName.equalsIgnoreCase(tree.getIdName())) {
                String parentId = tree.getParentId(dataSet, id);
                if ((tree.getCodeRule() == null) && Common.isNullStr(parentId)) {
                    tree.maskValueChange(false);
                    dataSet.maskDataChange(false);
                    return;
                }
                adjustParent(node, parentId, dataSet);
                modifyChildrenParentID(dataSet, node);
            } else if (fieldName.equalsIgnoreCase(tree.getTextName())) {
                ((PfTreeNode) node.getUserObject()).setShowContent(dataSet
                        .fieldByName(tree.getTextName()).getString());
                if (node.getParent() != null) {
                    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                }
            } else if (fieldName.equalsIgnoreCase(tree.getParentIdName())) {
                adjustParent(node, tree.getParentId(dataSet, id), dataSet);
            } else if (fieldName.equalsIgnoreCase(tree.getSortKey())) {
                if (node.getParent() != null) {
                    ((MyTreeNode)node).sortKeyValue = dataSet.fieldByName(tree.getSortKey()).getString();
                    adjustParent(node, tree.getParentId(dataSet, id), dataSet);
                }
            }
            tree.maskValueChange(false);
            dataSet.maskDataChange(false);
        }
    }

    /**
     * 父结点ID修改时同时修改子结点的parentid值（如果有Parentid字段）
     * 
     * @param ds
     * @param parent
     * @throws Exception
     */
    protected void modifyChildrenParentID(DataSet ds,
            DefaultMutableTreeNode parent) throws Exception {
        String bookmark = ds.toogleBookmark();
        String parentId = ((PfTreeNode) parent.getUserObject()).getValue();
        for (int i = 0; i < parent.getChildCount(); i++) {
            MyTreeNode node = (MyTreeNode) parent.getChildAt(i);
            ds.gotoBookmark(node.bookmark);
            if (!Common.isNullStr(tree.getParentIdName())) {
                ds.fieldByName(tree.getParentIdName()).setValue(parentId);
            }
        }
        ds.gotoBookmark(bookmark);
    }

    /**
     * 获得结点，如果结点id值发生变化，用变化之前的id值搜索
     * 
     * @param event
     * @param id
     * @return
     */
    protected DefaultMutableTreeNode getNode(Field idField, String id)
            throws Exception {
        DefaultMutableTreeNode node = null;
        if (!Common.isNullStr(id)) {
            node = (DefaultMutableTreeNode) tree.nodeCache.get(id);
            if (node == null) {
                Object key = idField.getPreviousValue();
                if (key != null) {
                    node = (DefaultMutableTreeNode) tree.nodeCache.get(key);
                    if(node!=null) {
                        tree.nodeCache.put(key,tree.nodeCache.remove(id));
                    }
                }
            }
        }
        if (node == null) {
            node = (DefaultMutableTreeNode) tree.nodeCache.get(tree.getDataSet().toogleBookmark());
            if(!Common.isNullStr(id)) {
                tree.nodeCache.put(id,tree.nodeCache.remove(tree.getDataSet().toogleBookmark()));
            }
        }
        ((PfTreeNode) node.getUserObject()).setValue(id);
        return node;
    }

    /**
     * 根据一个结点ID调整它的所属父结点
     * 
     * @return
     */
    protected void adjustParent(DefaultMutableTreeNode node, String parentId,
            DataSet ds) throws Exception {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
                .getParent();
        if (parent != null) {
            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
        }
        DefaultMutableTreeNode newParent = null;
        if (!Common.isNullStr(parentId)) {
            ds.fieldByName(tree.getParentIdName()).setValue(parentId);
            newParent = (DefaultMutableTreeNode) tree.nodeCache.get(parentId);
            if (newParent == null) {
				newParent = tree.getRoot();
			}
        } else {
			newParent = tree.getRoot();
		}
        tree.addChild(newParent, node, (DefaultTreeModel) tree.getModel());
    }
}
