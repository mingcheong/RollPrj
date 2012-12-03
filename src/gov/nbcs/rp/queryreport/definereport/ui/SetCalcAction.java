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
 * Title:设置计算列操作
 * </p>
 * <p>
 * Description:设置计算列操作
 * </p>
 * <p>

 */
public class SetCalcAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	private JFrame jframe = null;

	private CalcColumnDialog calcColumnDialog;

	public SetCalcAction(ReportPane reportPane, JFrame jframe, boolean bEnable) {
		// this.setEnabled(bEnable);报表组件包发生改变，不可调用
		this.setName("计算列设置");
		this.setSmallIcon(DesignUtils.getIcon("images/fbudget/a7.gif"));
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
			value = new MyCalculateValueImpl("");
			((MyCalculateValueImpl) value).setIsVisual(1);
			cellElement = new CellElement(col, row);
			cellElement.setValue(value);
			reportPane.getReport().addCellElement(cellElement);
		} else {
			value = cellElement.getValue();
			if (value == null || !(value instanceof MyCalculateValueImpl)) {
				value = new MyCalculateValueImpl("");
				((MyCalculateValueImpl) value).setIsVisual(1);
			}
		}

		this.calcColumnDialog = new CalcColumnDialog(jframe);
		Tools.centerWindow(calcColumnDialog);
		calcColumnDialog.populate(cellElement, value);
		calcColumnDialog.setVisible(true);
		return false;
	}
}
