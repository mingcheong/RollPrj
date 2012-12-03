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
import com.fr.report.CellElement;

class DeleteActionListener implements DataSetProcListener {
    Report report;

    DeleteActionListener(Report report) {
        this.report = report;
    }

    /**
     * DataSet删除一条记录的时候相应删除FineReport对应行
     */
    public void beforeProc(DataSetEvent event) throws Exception {
        DataSet ds = event.getDataSet();
        Object fieldId = report.getReportHeader().getFields().get(0);
        String fieldName = report.getCellProperty().getFieldName(fieldId);
        CellElement cell = report.getCellElement(Report.translateToCellName(ds
                .toogleBookmark(), fieldName));
        if (cell != null) {
			report.removeRow(cell.getRow());
		}
    }

    public void afterProc(DataSetEvent event) throws Exception {
    }
}
