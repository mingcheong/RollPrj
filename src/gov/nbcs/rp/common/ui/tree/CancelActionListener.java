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

import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

class CancelActionListener implements DataSetProcListener {
    CustomTree tree;
    CancelActionListener(CustomTree tree) {
        this.tree = tree;
    }
    
    public void beforeProc(DataSetEvent event)
    throws Exception  {
    }

    /**
     * DataSet发生Cancel后重置树结点
     */
    public void afterProc(DataSetEvent event)
    throws Exception {
        tree.maskValueChange(true);
        tree.reset();
        tree.maskValueChange(false);
    }
}
