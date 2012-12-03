/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 下拉列表单元格编辑器
 * 
 * @author 钱自成
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
	 * 创建下拉列表单元格编辑器
	 * @param valueList 列表值
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
