/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

public class ReportRowSelDataStatListener implements StateChangeListener {
    Report report;

    public ReportRowSelDataStatListener(Report report) {
        this.report = report;
    }

    public void onStateChange(DataSetEvent event) throws Exception {
        if ((report != null) && (report.getUI() != null)) {
            if ((event.getDataSet().getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
                report.getUI().setRowSelect(false);
            }
            else {
                report.getUI().setRowSelect(true);
            }
        }
    }

}
