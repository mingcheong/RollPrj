/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 代码输出入框，只是能输入数字
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */


package gov.nbcs.rp.common.ui.report.cell.editors;

import java.awt.Component;

import gov.nbcs.rp.common.ui.input.IntegerTextField;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class NumberTextCellEditor extends AbstractCellEditor {
	protected IntegerTextField integerTextField;

	public NumberTextCellEditor(int iMaxLength) {
		integerTextField = new IntegerTextField();
		integerTextField.setTitleVisible(false);
		if (iMaxLength > 0) {
			integerTextField.setMaxLength(iMaxLength);
		}
        new BoundFocusEvent().bind(this,integerTextField.getEditor());
	}

	public Object getCellEditorValue() throws Exception {
		if (integerTextField.getValue().toString().matches("\\d+")) {
			return integerTextField.getValue();
		}
		return null;
	}

	public Component getCellEditorComponent(Grid grid, CellElement cell) {
		Object value = cell.getValue();
		if (value != null) {
			integerTextField.setValue(value);
		}
		return integerTextField;
	}

}
