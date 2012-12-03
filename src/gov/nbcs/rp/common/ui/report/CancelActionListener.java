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

import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

class CancelActionListener implements DataSetProcListener {
    Report report;

    CancelActionListener(Report report) {
        this.report = report;
    }

    public void beforeProc(DataSetEvent event) throws Exception {
    }

    public void afterProc(DataSetEvent event) throws Exception {
        for (int i = report.getReportHeader().getRows(); i < report
                .getReportHeader().getRows()
                + report.getBodyRowCount(); i++) {
            for (int j = 0; j < report.getReportHeader().getColumns(); j++) {
                report.removeCellElement(j, i);
            }
        }
        report.refreshBody();
    }
}
