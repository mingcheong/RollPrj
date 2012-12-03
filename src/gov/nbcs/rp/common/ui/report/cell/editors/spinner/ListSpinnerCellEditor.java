/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors.spinner;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import gov.nbcs.rp.common.ui.report.cell.editors.BoundFocusEvent;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class ListSpinnerCellEditor extends AbstractCellEditor {
    protected JSpinner spinner;

    /**
     * �����̶���ֵ�б�༭��
     * @param valueList �̶���ֵ�б�
     */
    public ListSpinnerCellEditor(Number valueList[]) {
        SpinnerModel model = new SpinnerListModel(valueList);
        spinner = new JSpinner(model);
        this.spinner.setCursor(Cursor.getDefaultCursor());
        new BoundFocusEvent().bind(this,((JSpinner.DefaultEditor) spinner
                .getEditor()).getTextField());
    }

    public Object getCellEditorValue() throws Exception {
        return SpinnerValueGetter.getEditorValue(spinner);
    }

    public Component getCellEditorComponent(Grid grid, CellElement cell) {
        Object value = cell.getValue();
        if ((value != null) && (((SpinnerListModel)spinner.getModel()).getList().indexOf(value)>=0)) {
            spinner.setValue(value);
        }
        return spinner;
    }
}
