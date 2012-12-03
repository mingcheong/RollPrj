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
import java.math.BigDecimal;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.report.cell.editors.BoundFocusEvent;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class NumberSpinnerCellEditor extends AbstractCellEditor {
    protected IntegerSpinner spinner;

    public NumberSpinnerCellEditor() {
        SpinnerModel model = new SpinnerNumberModel();
        spinner = new IntegerSpinner(model);
        this.spinner.setCursor(Cursor.getDefaultCursor());
    }

    public NumberSpinnerCellEditor(SpinnerModel modelCalcPRI) {
        spinner = new IntegerSpinner(modelCalcPRI);
        this.spinner.setCursor(Cursor.getDefaultCursor());
        new BoundFocusEvent().bind(this,((JSpinner.DefaultEditor) spinner
                .getEditor()).getTextField());
    }

    /**
     * 设置最大值
     * 
     * @param c
     */
    public void setMaximum(Comparable c) {
        ((SpinnerNumberModel) spinner.getModel()).setMaximum(c);
    }

    /**
     * 设置最小值
     * 
     * @param c
     */
    public void setMinimum(Comparable c) {
        ((SpinnerNumberModel) spinner.getModel()).setMinimum(c);
    }

    public Object getCellEditorValue() throws Exception {
        return SpinnerValueGetter.getEditorValue(spinner);
    }

    public Component getCellEditorComponent(Grid grid, CellElement cell) {
        Object value = cell.getValue();
        if ((value != null) && Common.isNumber(value.toString())) {
            spinner.setValue(new BigDecimal(value.toString()));
        }
        return spinner;
    }
}
