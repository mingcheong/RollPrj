/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

class InsertActionListener implements DataSetProcListener  {
    Report report;
    
    InsertActionListener(Report report) {
        this.report = report;
    }
    
    public void beforeProc(DataSetEvent event) throws Exception {
        report.getUI().setRowSelect(false);
    }

    /**
     * DataSet发生Insert/Append之后要修改Report的数据
     */
    public void afterProc(DataSetEvent event) throws Exception {
        DataSet ds = event.getDataSet();
        switch(ds.getRecordState()) {
        case DataSet.FOR_INSERT:
            int rowIndex = report.getSelectedRow();
            if(rowIndex>=0) {
				report.insertRow(rowIndex);
			}
            break;
        case DataSet.FOR_APPEND:
            report.appendRow();
            break;
        }
    }
}
