package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.EventObject;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.design.actions.UndoableAction;
import com.foundercy.pf.reportcy.design.drag.FormulaM;
import com.fr.cell.ReportPane;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:设置
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */

public class SetParaAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	public SetParaAction(ReportPane reportPane, boolean bEnable) {
		// this.setEnabled(bEnable); 报表组件包发生改变，不可调用
		this.setName("设置查询单位参数");
		this.setReportPane(reportPane);
	}

	public boolean executeAction(EventObject evt, ReportPane reportPane) {
		int col = reportPane.getCellSelection().getColumn();
		int row = reportPane.getCellSelection().getRow();
		FormulaM formula = new FormulaM("\"查询单位:\"+$"
				+ IDefineReport.DIVNAME_PARA);
		formula.setDisplayName("\"查询单位:\"+$" + IDefineReport.DIVNAME_PARA);
		if (reportPane.getReport().getCellElement(col, row) == null) {
			CellElement cellElement = new CellElement(col, row);
			reportPane.getReport().addCellElement(cellElement, true);
		}
		reportPane.getReport().getCellElement(col, row).setValue(formula);
		return true;
	}
}