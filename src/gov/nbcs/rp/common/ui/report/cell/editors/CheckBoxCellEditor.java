/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JCheckBox;

import gov.nbcs.rp.common.Common;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class CheckBoxCellEditor extends AbstractCellEditor {
    /**
     * ����ѡ/��ѡ�󣬶�CellEditorȡֵʱ���غ���ֵ�����磺true->"��"��false->"��"
     */
    private Object checkedValue;

    private Object uncheckedValue;

    private JCheckBox checkBox = new JCheckBox();

    public CheckBoxCellEditor(Object checkedValue, Object uncheckedValue,
            String title) {
        this.checkedValue = checkedValue;
        this.uncheckedValue = uncheckedValue;
        checkBox.setText(title);
        checkBox.setCursor(Cursor.getDefaultCursor());
        new BoundFocusEvent().bind(this,checkBox);
    }

    public CheckBoxCellEditor(Object checkedValue, Object uncheckedValue) {
        this(checkedValue, uncheckedValue, "");
    }

    public Object getCellEditorValue() throws Exception {
        return checkBox.isSelected() ? checkedValue : uncheckedValue;
    }

    public Component getCellEditorComponent(Grid grid, CellElement cell) {
        checkBox.setSelected(Common.estimate(cell.getValue()));
        return checkBox;
    }
}
