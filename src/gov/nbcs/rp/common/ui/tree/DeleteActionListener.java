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

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


class DeleteActionListener implements DataSetProcListener {
    CustomTree tree;

    DeleteActionListener(CustomTree tree) {
        this.tree = tree;
    }

    /**
     * DataSet删除一条记录后，删除相应的树结点
     */
    public void beforeProc(DataSetEvent event) throws Exception {
        DataSet ds = event.getDataSet();
        String id = ds.fieldByName(tree.getIdName()).getString();
        if (!Common.isNullStr(id)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.nodeCache
                    .get(id);
            if (node != null) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
                        .getParent();
                if (parent != null) {
                    ((DefaultTreeModel) tree.getModel())
                            .removeNodeFromParent(node);
                    deleteTree(ds, (MyTreeNode) node);
                }
            }
        }
        tree.nodeCache.remove(id);
    }

    /**
     * 删除一个树结点的时候，也会删除相应的子树数据(DataSet中)
     * 
     * @param node
     * @throws Exception
     */
    protected void deleteTree(DataSet ds, MyTreeNode parent) throws Exception {
        ds.maskDataChange(true);
        ds.maskOperationChange(true);
        Enumeration enumeration = parent.breadthFirstEnumeration();
        String bookmark = ds.toogleBookmark();
        while (enumeration.hasMoreElements()) {
            MyTreeNode node = (MyTreeNode) enumeration.nextElement();
            if (node != parent) {
                ds.gotoBookmark(node.bookmark);
                tree.nodeCache.remove(ds.fieldByName(tree.getIdName())
                        .getString());
                ds.delete();
            }
        }
        ds.gotoBookmark(bookmark);
        ds.maskOperationChange(false);
        ds.maskDataChange(false);
    }

    public void afterProc(DataSetEvent event) throws Exception {
    }
}
