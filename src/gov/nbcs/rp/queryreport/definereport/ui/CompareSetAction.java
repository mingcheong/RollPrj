/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.EventObject;

import com.foundercy.pf.reportcy.design.actions.UndoableAction;
import com.foundercy.pf.reportcy.design.gui2.core.DesignUtils;
import com.fr.cell.ReportPane;
import com.fr.report.CellElement;

/**

 */
public class CompareSetAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	private String sCompareFlag;

	public CompareSetAction(ReportPane reportPane, boolean bEnable,
			String sShowName, String sCompareFlag) {
		// this.setEnabled(bEnable);报表组件包发生改变，不可调用
		this.setName(sShowName);
		this.setSmallIcon(DesignUtils.getIcon("images/fbudget/a12.gif"));
		this.setReportPane(reportPane);
		this.sCompareFlag = sCompareFlag;

	}

	public boolean executeAction(EventObject evt, ReportPane reportPane) {

		int col = reportPane.getCellSelection().getColumn();
		int row = reportPane.getCellSelection().getRow();
		CellElement cellElement = reportPane.getReport().getCellElement(col,
				row);

		Object value;
		if (cellElement == null) {
			FundSourceImpl fundSourceImpl = new FundSourceImpl("");
			fundSourceImpl.setCompareFlag(sCompareFlag);
			cellElement = new CellElement(col, row);
			cellElement.setValue(fundSourceImpl);
			reportPane.getReport().addCellElement(cellElement);
		} else {
			value = cellElement.getValue();
			if (value == null) {
				cellElement.setValue(new FundSourceImpl(""));
			} else if (value instanceof FundSourceImpl) {
				((FundSourceImpl) value).setCompareFlag(sCompareFlag);
			} else {
				FundSourceImpl fundSourceImpl = new FundSourceImpl(value
						.toString());
				fundSourceImpl.setCompareFlag(sCompareFlag);
				cellElement.setValue(fundSourceImpl);
			}
		}
		return false;
	}
}
