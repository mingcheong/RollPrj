/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
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
     * DataSet�����仯��ʱ��ı��������Ϣ
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
     * �����ID�޸�ʱͬʱ�޸��ӽ���parentidֵ�������Parentid�ֶΣ�
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
     * ��ý�㣬������idֵ�����仯���ñ仯֮ǰ��idֵ����
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
     * ����һ�����ID�����������������
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
