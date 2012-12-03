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
 * Title:ɾ���в���
 * </p>
 * <p>
 * Description:ɾ���в���
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
			// �õ���������ʹ�õ���
			for (int i = (column + colSpan) - 1; i >= column; i--) {
				if (checkUse(report, i, column, colSpan)) {
					if (lstUseCol == null) {
						lstUseCol = new ArrayList();
					}
					lstUseCol.add(String.valueOf(i));
					continue;
				}
			}

			// ��ʾ��ʾ��Ϣ
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
				sInfo = "��" + sInfo + "���ѱ�����������,������ɾ����ɾ����������";
				// ��ʾ�Ƿ�ȷ��ɾ��
				if (JOptionPane
						.showConfirmDialog(reportGuideUI, sInfo, "��ʾ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
					return false;
				}
			}

			// ɾ����
			for (int i = (column + colSpan) - 1; i >= column; i--) {
				// �ж��Ƿ��Ǳ�������������
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

		// ˢ����ʾ����
		reportGuideUI.reportOrderSet.showOrderSet(report);
		// ˢ�����л�����ʾ˳��
		reportGuideUI.reportGroupSet.showGroupSet(report);

		return true;
	}

	/**
	 * �������Ƿ�����δɾ����ʹ��
	 * 
	 * @param groupReport
	 * @param curCol
	 * @param startCol
	 * @param colSpan
	 * @return
	 */
	private boolean checkUse(GroupReport groupReport, int curCol, int startCol,
			int colSpan) {// �õ��������к�
		// �����������к�
		int rowOpe = getOpeRow(groupReport);

		String sColName = ReportUtil.translateToColumnName(curCol)
				+ String.valueOf(rowOpe + 1);
		// �õ�������
		int col = groupReport.getColumnCount();
		Object value;
		String sFormula;
		for (int i = 0; i < col; i++) {
			if (curCol == i)
				continue;
			// �жϸ����Ƿ���ɾ��������
			if (i < startCol || i > colSpan) {
				continue;
			}

			// �õ���ǰ�ж�Ӧ�Ĳ�����cell
			if (groupReport.getCellElement(i, rowOpe) == null) {
				continue;
			}
			value = groupReport.getCellElement(i, rowOpe).getValue();
			if (!(value instanceof MyCalculateValueImpl)) {
				continue;
			}
			MyCalculateValueImpl calculateValueImpl = (MyCalculateValueImpl) value;
			sFormula = calculateValueImpl.getFormula().getContent();
			// �ж��Ƿ������˸���
			if (sFormula.indexOf(sColName) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �õ��������к�
	 * 
	 * @param groupReport
	 * @return
	 */
	private int getOpeRow(GroupReport groupReport) {
		// �õ���ͷ������
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		return indexs[0];
	}
}
