/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
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
     * DataSet����Insert/Append֮��Ҫ�޸�Report������
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
