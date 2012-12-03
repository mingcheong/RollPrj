/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.EventObject;

import javax.swing.JFrame;

import com.foundercy.pf.reportcy.design.actions.UndoableAction;
import com.foundercy.pf.reportcy.design.gui2.core.DesignUtils;
import com.foundercy.pf.util.Tools;
import com.fr.cell.ReportPane;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:设置分组列操作
 * </p>
 * <p>
 * Description:设置分组列操作
 * </p>

 */
public class SetGroupAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	private JFrame jframe = null;

	public SetGroupAction(ReportPane reportPane, JFrame jframe, boolean bEnable) {
		// this.setEnabled(bEnable);报表组件包发生改变，不可调用
		this.setName("分组列设置");
		this.setSmallIcon(DesignUtils.getIcon("images/fbudget/a12.gif"));
		this.setReportPane(reportPane);

		this.jframe = jframe;

	}

	public boolean executeAction(EventObject evt, ReportPane reportPane) {

		int col = reportPane.getCellSelection().getColumn();
		int row = reportPane.getCellSelection().getRow();
		CellElement cellElement = reportPane.getReport().getCellElement(col,
				row);

		Object value;
		if (cellElement == null) {
			value = new MyGroupValueImpl("");
			cellElement = new CellElement(col, row);
			cellElement.setValue(value);
			reportPane.getReport().addCellElement(cellElement);
		} else {
			value = cellElement.getValue();
			if (value == null || !(value instanceof MyGroupValueImpl)) {
				value = new MyGroupValueImpl("");
			}
		}

		GroupColumnDialog dataColumnDialog = new GroupColumnDialog(jframe);
		Tools.centerWindow(dataColumnDialog);
		dataColumnDialog.populate(cellElement, value);
		dataColumnDialog.setVisible(true);
		return false;
	}
}
