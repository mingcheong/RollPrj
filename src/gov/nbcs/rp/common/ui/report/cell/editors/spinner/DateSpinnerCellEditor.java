/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors.spinner;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import gov.nbcs.rp.common.ui.report.cell.editors.BoundFocusEvent;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class DateSpinnerCellEditor extends AbstractCellEditor {
    protected JSpinner spinner;

    /**
     * 创建日期单元格编辑框
     * @param dateFormat 日期显示格式
     */
    public DateSpinnerCellEditor(String dateFormat) {
        SpinnerModel model = new SpinnerDateModel();
        spinner = new JSpinner(model);
        this.spinner.setCursor(Cursor.getDefaultCursor());
        this.spinner.setEditor(new JSpinner.DateEditor(spinner, dateFormat));
        new BoundFocusEvent().bind(this,((JSpinner.DefaultEditor) spinner
                .getEditor()).getTextField());
    }

    public Object getCellEditorValue() throws Exception {
        return SpinnerValueGetter.getEditorValue(spinner);
    }

    public Component getCellEditorComponent(Grid grid, CellElement cell) {
        Object value = cell.getValue();
        if (value != null) {
            try {
                spinner.setValue(value);
            } catch (Exception ex) {

            }
        }
        return spinner;
    }
}
