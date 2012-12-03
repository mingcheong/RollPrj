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
     * DataSet发生Insert/Append之后要修改Report的数据
     */
    public void afterProc(DataSetEvent event) throws Exception {
        DataSet ds = event.getDataSet();
        String bookmark = ds.toogleBookmark();
        TreeNode node = CustomTree.createNode("","",bookmark,"",tree);
        tree.nodeCache.put(bookmark,node);
    }
}
