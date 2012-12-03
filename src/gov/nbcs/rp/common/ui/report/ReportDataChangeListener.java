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

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import com.fr.report.CellElement;

class ReportDataChangeListener implements DataChangeListener {
    Report report;

    ReportDataChangeListener(Report report) {
        this.report = report;
    }

    /**
     * �󶨵�DataSet�����仯��ʱ���޸�Report������
     */
    public void onDataChange(DataChangeEvent event) throws Exception {
        if ((event.type() == DataChangeEvent.FIELD_MODIRED) && (event.getSender()!=report)) {// ������ֶ�ֵ�޸ĵ��¼�
            DataSet ds = event.getDataSet();
            Field field = (Field) event.getSource();
            if (field.isLatestModified()) {
                String fieldName = field.getName();
                String bookmark = ds.toogleBookmark();
                String cellName = Report.translateToCellName(bookmark,
                        fieldName);
                CellElement cell = report.getCellElement(cellName);
                report.maskCellValueChange(true);
                if (cell != null) {// �����Ԫ��������޸ĵ�Ԫ���ֵ
                    cell.setValue(field.getValue());
                } /**else {// ��������µĵ�Ԫ�񣬿����Ƿ�����µ��У�����������ص�Ԫ���ֵ
                    TableHeader header = report.getReportHeader();
                    Object fieldId = report.getCellProperty()
                    .getFieldId(fieldName);
                    int columnIndex = header.getFields().indexOf(fieldId);
                    if (columnIndex >= 0) {
                        int rowIndex = report.bookmarkToRow(bookmark);
                        if (rowIndex >= report.getReportHeader().getRows()) {
                            if (rowIndex < report.getReportHeader().getRows()
                                    + report.getBodyRowCount()
                                    && !report.containsRecord(ds
                                            .toogleBookmark())
                                    && ds.getRecordState() == DataSet.FOR_INSERT) {// �����¼�����ڲ��ҽ���λ��������¼֮���Ҫ�²���һ��
                                report.insertRow(rowIndex);
                            }
                            
                            CellElement cellElement = report.createCell(
                                    columnIndex, rowIndex, fieldId);
                            report.addCellElement(cellElement,true);
                        }
                    }
                }*/
                report.maskCellValueChange(false);
            }
        }
    }
}
