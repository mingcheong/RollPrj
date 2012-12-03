/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title �����б�Ԫ��༭��
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class ComboBoxCellEditor extends AbstractCellEditor {
	protected JComboBox comboBox;

	/**
	 * ���������б�Ԫ��༭��
	 * @param valueList �б�ֵ
	 */
	public ComboBoxCellEditor(Object[] valueList) {
		comboBox = new JComboBox(new DefaultComboBoxModel(valueList));
		comboBox.setCursor(Cursor.getDefaultCursor());
		new BoundFocusEvent().bind(this, comboBox);
	}

	public Object getCellEditorValue() throws Exception {
		return comboBox.getSelectedItem();
	}

	public Component getCellEditorComponent(Grid grid, CellElement cell) {
		Object value = cell.getValue();
		if ((value != null)
				&& (((DefaultComboBoxModel) comboBox.getModel())
						.getIndexOf(value) >= 0)) {
			comboBox.setSelectedItem(value);
		}
		return comboBox;
	}

	public void setModel(Object[] valueList) {
		comboBox.setModel(new DefaultComboBoxModel(valueList));
	}
	
	public JComboBox getComboBox(){
		return this.comboBox;
	}
}
