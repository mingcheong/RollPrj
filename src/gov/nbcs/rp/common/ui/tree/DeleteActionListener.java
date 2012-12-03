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
     * DataSetɾ��һ����¼��ɾ����Ӧ�������
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
     * ɾ��һ��������ʱ��Ҳ��ɾ����Ӧ����������(DataSet��)
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
