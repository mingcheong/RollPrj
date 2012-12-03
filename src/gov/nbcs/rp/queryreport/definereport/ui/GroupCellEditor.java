/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.foundercy.pf.util.Tools;
import com.fr.cell.Grid;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-28
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class GroupCellEditor extends com.fr.cell.editor.AbstractCellEditor {

	private GroupColumnDialog dataColumnDialog = null;

	public GroupCellEditor() {
		super();
	}

	public Object getCellEditorValue() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Component getCellEditorComponent(Grid grid, CellElement cellElement) {
		Object value = cellElement.getValue();
		if (value == null || !(value instanceof MyGroupValueImpl)) {
			value = new MyGroupValueImpl("");
		}

		Window window = SwingUtilities.getWindowAncestor(grid);
		if (window instanceof JFrame) {
			this.dataColumnDialog = new GroupColumnDialog((JFrame) window);
		} else {
			this.dataColumnDialog = new GroupColumnDialog((Dialog) window);
		}
		Tools.centerWindow(dataColumnDialog);
		dataColumnDialog.populate(cellElement, value);

		return this.dataColumnDialog;
	}

}
