/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
     * 绑定的DataSet发生变化的时候修改Report的数据
     */
    public void onDataChange(DataChangeEvent event) throws Exception {
        if ((event.type() == DataChangeEvent.FIELD_MODIRED) && (event.getSender()!=report)) {// 如果是字段值修改的事件
            DataSet ds = event.getDataSet();
            Field field = (Field) event.getSource();
            if (field.isLatestModified()) {
                String fieldName = field.getName();
                String bookmark = ds.toogleBookmark();
                String cellName = Report.translateToCellName(bookmark,
                        fieldName);
                CellElement cell = report.getCellElement(cellName);
                report.maskCellValueChange(true);
                if (cell != null) {// 如果单元格存在则修改单元格的值
                    cell.setValue(field.getValue());
                } /**else {// 否则添加新的单元格，考虑是否添加新的行，并且设置相关单元格的值
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
                                    && ds.getRecordState() == DataSet.FOR_INSERT) {// 如果记录不存在并且焦点位于其它记录之间就要新插入一行
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
