/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.foundercy.pf.util.Tools;
import com.fr.cell.Grid;
import com.fr.report.CellElement;


public class CaluCellEditor extends com.fr.cell.editor.AbstractCellEditor {

	private CalcColumnDialog calcColumnDialog = null;

	public CaluCellEditor() {
		super();
	}

	public Object getCellEditorValue() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Component getCellEditorComponent(Grid grid, CellElement cellElement) {
		Object value = cellElement.getValue();
		if (value == null || !(value instanceof MyCalculateValueImpl)) {
			value = new MyCalculateValueImpl("");
		}

		Window window = SwingUtilities.getWindowAncestor(grid);
		if (window instanceof Frame) {
			this.calcColumnDialog = new CalcColumnDialog((JFrame) window);
		} else {
			this.calcColumnDialog = new CalcColumnDialog((Dialog) window);
		}
		Tools.centerWindow(calcColumnDialog);
		calcColumnDialog.populate(cellElement, value);

		return this.calcColumnDialog;
	}
}
