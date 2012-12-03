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
import com.fr.report.CellElement;

class DeleteActionListener implements DataSetProcListener {
    Report report;

    DeleteActionListener(Report report) {
        this.report = report;
    }

    /**
     * DataSetɾ��һ����¼��ʱ����Ӧɾ��FineReport��Ӧ��
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
