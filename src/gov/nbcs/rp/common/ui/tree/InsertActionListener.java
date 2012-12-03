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

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

import javax.swing.tree.TreeNode;

class InsertActionListener implements DataSetProcListener  {
    CustomTree tree;
    InsertActionListener(CustomTree tree) {
        this.tree = tree;
    }
    
    public void beforeProc(DataSetEvent event) throws Exception {
    }

    /**
     * DataSet����Insert/Append֮��Ҫ�޸�Report������
     */
    public void afterProc(DataSetEvent event) throws Exception {
        DataSet ds = event.getDataSet();
        String bookmark = ds.toogleBookmark();
        TreeNode node = CustomTree.createNode("","",bookmark,"",tree);
        tree.nodeCache.put(bookmark,node);
    }
}
