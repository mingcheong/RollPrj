/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.design.actions.other.DeleteColumnAction;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.fr.cell.CellSelection;
import com.fr.cell.ReportPane;
import com.fr.cell.core.GridUtils;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:删除列操作
 * </p>
 * <p>
 * Description:删除列操作
 * </p>

 */
public class CustomDeleteColumnAction extends DeleteColumnAction {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI;

	public CustomDeleteColumnAction() {
		super();
	}

	public CustomDeleteColumnAction(ReportGuideUI reportGuideUI, ReportPane arg0) {
		super(arg0);
		this.reportGuideUI = reportGuideUI;
	}

	public boolean executeAction(EventObject arg0, ReportPane arg1) {

		SummaryReportPane reportPanel = (SummaryReportPane) arg1;

		List lstUseCol = null;
		GroupReport report = reportPanel.getGroupReport();
		CellSelection cellSelection = reportPanel.getCellSelection();
		if (cellSelection == null) {
			report.removeColumn(getIndex());
		} else {
			int column = cellSelection.getColumn();
			int colSpan = cellSelection.getColumnSpan();
			// 得到被其他列使用的列
			for (int i = (column + colSpan) - 1; i >= column; i--) {
				if (checkUse(report, i, column, colSpan)) {
					if (lstUseCol == null) {
						lstUseCol = new ArrayList();
					}
					lstUseCol.add(String.valueOf(i));
					continue;
				}
			}

			// 显示提示信息
			if (lstUseCol != null) {
				String sInfo = "";
				int count = lstUseCol.size();
				for (int i = count - 1; i >= 0; i--) {
					if (!Common.isNullStr(sInfo)) {
						sInfo = sInfo + ",";
					}
					sInfo = sInfo
							+ ReportUtil.translateToColumnName(Integer
									.parseInt(lstUseCol.get(i).toString()));
				}
				sInfo = "第" + sInfo + "列已被其他列引用,不允许删除！删除其他列吗？";
				// 提示是否确定删除
				if (JOptionPane
						.showConfirmDialog(reportGuideUI, sInfo, "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
					return false;
				}
			}

			// 删除列
			for (int i = (column + colSpan) - 1; i >= column; i--) {
				// 判断是否是被其他列引用列
				if (lstUseCol != null) {
					if (lstUseCol.contains(String.valueOf(i))) {
						continue;
					}
				}
				report.removeColumn(i);
			}

		}
		int lastColumn = report.getColumnCount();
		if (cellSelection != null
				&& cellSelection.getColumn() + cellSelection.getColumnSpan() < lastColumn)
			GridUtils.doSelectCell(reportPanel, cellSelection.getColumn(),
					cellSelection.getRow());
		else if (cellSelection != null && cellSelection.getColumn() > 0)
			GridUtils.doSelectCell(reportPanel, cellSelection.getColumn() - 1,
					cellSelection.getRow());

		// 刷新显示排序
		reportGuideUI.reportOrderSet.showOrderSet(report);
		// 刷新向中汇总显示顺序
		reportGuideUI.reportGroupSet.showGroupSet(report);

		return true;
	}

	/**
	 * 检查该列是否被其他未删除列使用
	 * 
	 * @param groupReport
	 * @param curCol
	 * @param startCol
	 * @param colSpan
	 * @return
	 */
	private boolean checkUse(GroupReport groupReport, int curCol, int startCol,
			int colSpan) {// 得到操作区行号
		// 操作运算区行号
		int rowOpe = getOpeRow(groupReport);

		String sColName = ReportUtil.translateToColumnName(curCol)
				+ String.valueOf(rowOpe + 1);
		// 得到总列数
		int col = groupReport.getColumnCount();
		Object value;
		String sFormula;
		for (int i = 0; i < col; i++) {
			if (curCol == i)
				continue;
			// 判断该列是否在删除列里面
			if (i < startCol || i > colSpan) {
				continue;
			}

			// 得到当前列对应的操作区cell
			if (groupReport.getCellElement(i, rowOpe) == null) {
				continue;
			}
			value = groupReport.getCellElement(i, rowOpe).getValue();
			if (!(value instanceof MyCalculateValueImpl)) {
				continue;
			}
			MyCalculateValueImpl calculateValueImpl = (MyCalculateValueImpl) value;
			sFormula = calculateValueImpl.getFormula().getContent();
			// 判断是否引用了该列
			if (sFormula.indexOf(sColName) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到操作区行号
	 * 
	 * @param groupReport
	 * @return
	 */
	private int getOpeRow(GroupReport groupReport) {
		// 得到表头行区域
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		return indexs[0];
	}
}
