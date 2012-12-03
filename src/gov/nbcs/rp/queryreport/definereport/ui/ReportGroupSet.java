/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:���鱨����,���ϻ������ÿͷ���ҳ��
 * </p>
 * <p>
 * Description:���鱨����,�������ÿͷ���ҳ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class ReportGroupSet extends FTitledPanel {
	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI;

	// ����������
	private FTable ftabGroupSet = null;

	private ReportQuerySource querySource = null;

	// ���屨��
	private GroupReport groupReport = null;

	// ��������

	/**
	 * ���캯��
	 * 
	 * @param groupAreaImpl
	 *            ������
	 * @param sReportId
	 *            ����ID,���ӱ���null��"",�޸ı�������IDF
	 */
	public ReportGroupSet(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		// �����ʼ������
		jbInit();

		if (reportGuideUI.sReportId == null) {// ����
		} else {// �޸�
			showGroupSet(groupReport);
		}
	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {
		// ����������
		ftabGroupSet = new MyFTable();
		FScrollPane fspnlGroupSet = new FScrollPane(ftabGroupSet);

		// ���ư�ť
		FButton upBtn = new FButton("upBtn", "����");
		upBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ѡ����
				int curRowIndex = ftabGroupSet.getCurrentRowIndex();
				if (curRowIndex <= 0) {
					new MessageBox(reportGuideUI, "��ѡ����Ϣ��",
							MessageBox.INFOMATION, MessageBox.BUTTON_OK).show();
					return;
				}

				// ǰһ������Ϣ
				Map preMap = ftabGroupSet.getDataByIndex(curRowIndex - 1);
				String preGroupIndex = preMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				List lstPreGroupAble = (List) preMap.get(IDefineReport.COL_ID);

				// ��ǰ����Ϣ
				Map curMap = ftabGroupSet.getDataByIndex(curRowIndex);
				String curGroupIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				List lstCurGroupAble = (List) curMap.get(IDefineReport.COL_ID);

				// �ı�����˳��GroupIndexֵ
				saveGroupIndex(lstPreGroupAble, curGroupIndex);
				saveGroupIndex(lstCurGroupAble, preGroupIndex);

				// �ı�λ��
				List lstValue = ftabGroupSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex - 1, curMap);
				ftabGroupSet.setData(lstValue);
				ftabGroupSet.setRowSelectionInterval(curRowIndex - 1,
						curRowIndex - 1);

			}
		});
		// ���ư�ť
		FButton downBtn = new FButton("downBtn", "����");
		downBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ѡ����
				int curRowIndex = ftabGroupSet.getCurrentRowIndex();
				if (curRowIndex < 0) {
					new MessageBox(reportGuideUI, "��ѡ����Ϣ��",
							MessageBox.INFOMATION, MessageBox.BUTTON_OK).show();
					return;
				}
				if (curRowIndex == ftabGroupSet.getModel().getRowCount() - 1)
					return;
				// ��һ������Ϣ
				Map nextMap = ftabGroupSet
						.getDataByIndex(curRowIndex + 1);
				String nextGroupIndex = nextMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				List listNextGroupAble = (List) nextMap
						.get(IDefineReport.COL_ID);

				// ��ǰ����Ϣ
				Map curMap = ftabGroupSet.getDataByIndex(curRowIndex);
				String curGroupIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				List lstCurGroupAble = (List) curMap.get(IDefineReport.COL_ID);

				// �ı�����˳��GroupIndexֵ
				saveGroupIndex(listNextGroupAble, curGroupIndex);
				saveGroupIndex(lstCurGroupAble, nextGroupIndex);

				// �ı�λ��
				List lstValue = ftabGroupSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex + 1, curMap);
				ftabGroupSet.setData(lstValue);
				ftabGroupSet.setRowSelectionInterval(curRowIndex + 1,
						curRowIndex + 1);
			}
		});

		FButton uniteBtn = new FButton("uniteBtn", "�ϲ�");
		uniteBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				List lstData = ftabGroupSet.getSelectedDataByCheckBox();
				if (lstData.size() < 2) {
					new MessageBox(reportGuideUI, "�빴ѡ�������������Ϻϲ���Ϣ��",
							MessageBox.INFOMATION, MessageBox.BUTTON_OK).show();
					return;
				}
				String sErr = SummaryIndexSet.setSummaryIndexUnite(lstData);
				if (!Common.isNullStr(sErr)) {
					new MessageBox(reportGuideUI, sErr, MessageBox.ERROR,
							MessageBox.BUTTON_OK).show();
					return;
				}
				showGroupSet(groupReport);
			}
		});
		FButton splitBtn = new FButton("splitBtn", "���");
		splitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ѡ����
				int curRowIndex = ftabGroupSet.getCurrentRowIndex();
				if (curRowIndex < 0) {
					new MessageBox(reportGuideUI, "��ѡ����Ϣ��",
							MessageBox.INFOMATION, MessageBox.BUTTON_OK).show();
					return;
				}
				Map mapData = ftabGroupSet.getDataByIndex(curRowIndex);
				List lstGroupAble = (List) mapData.get(IDefineReport.COL_ID);

				List lstCells = DefinePubOther
						.getCellsWithOutClone(groupReport);
				// ��ִ���
				SummaryIndexSet.setSummaryIndexSplit(querySource, lstGroupAble,
						lstCells);

				// ˢ����ʾ
				showGroupSet(groupReport);
			}
		});

		// ���尴ť���
		FFlowLayoutPanel btnPnl = new FFlowLayoutPanel();
		btnPnl.addControl(upBtn, new TableConstraints(1, 1, true, false));
		btnPnl.addControl(downBtn, new TableConstraints(1, 1, true, false));
		btnPnl.addControl(uniteBtn, new TableConstraints(1, 1, true, false));
		btnPnl.addControl(splitBtn, new TableConstraints(1, 1, true, false));

		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(70);
		rLay.setColumnGap(2);
		this.setLayout(rLay);
		this.addControl(fspnlGroupSet, new TableConstraints(1, 1, true, true));
		this.addControl(btnPnl, new TableConstraints(1, 1, true, false));
	}

	/**
	 * ���ñ����
	 * 
	 * @param ftable�����õı��
	 */
	private class MyFTable extends FTable {

		private static final long serialVersionUID = 1L;

		public MyFTable() {
			super();
			init();
		}

		public MyFTable(boolean arg0) {
			super(arg0);
			init();
		}

		private void init() {

			this.setIsCheck(true);

			String[][] columns = new String[][] {
					{ IDefineReport.FIELD_FNAME, "�ֶ�����", "400" },
					{ IDefineReport.LEVEL, "���", "150" }, };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();

				column.setId(col[0]);
				column.setTitle(col[1]);
				column.setSortable(false);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(1000);
					column.setMinWidth(0);
				}
				this.addColumn(column);
			}
			// ���õ�ѡ
			this.getRightActiveTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);

		}
	}

	/**
	 * ����SummaryID�ı�GroupIndexֵ
	 * 
	 * @param sSummaryID
	 * @param GroupIndex
	 */
	private void saveGroupIndex(List lstGroupAble, String summaryIndex) {
		int size = lstGroupAble.size();
		for (int i = 0; i < size; i++) {
			((IGroupAble) lstGroupAble.get(i)).setSummaryIndex(summaryIndex);
		}
	}

	/**
	 * ��ʾ���ϻ�����Ϣ
	 */
	void showGroupSet(GroupReport groupReport) {
		List lstResult = new ArrayList();
		List lstCells = DefinePubOther.getCellsWithOutClone(groupReport);
		int maxSummaryIndex = DefinePubOther.getMaxSummaryIndex(lstCells);
		for (int i = 1; i <= maxSummaryIndex; i++) {
			List lstGroupAble = DefinePubOther
					.getGroupAbleValuelst(lstCells, i);
			if (lstGroupAble == null)
				continue;
			lstResult.add(getGroupListValue(DefinePubOther
					.convertType(lstGroupAble)));
		}

		// ��ʾ��Ϣ
		ftabGroupSet.setData(lstResult);
	}

	/**
	 * �õ�������ܽڴ���Ϣ
	 * 
	 * @param groupAbleArray
	 * @return
	 */
	private Map getGroupListValue(IGroupAble[] groupAbleArray) {
		Map aMap = new HashMap();
		List lstGroupAble = null;

		int len = groupAbleArray.length;

		String level = "";
		if (DefinePubOther.chekcIsMbSummary(groupAbleArray)) {
			level = "ҵ����";
		}

		// ���
		String levTmp = DefinePubOther.getLevel(groupAbleArray);
		if (!Common.isNullStr(levTmp)) {
			level = DefinePubOther.addComma(level);
			level = level + levTmp;
		}

		// ĩ��
		if (DefinePubOther.checkIsTotal(groupAbleArray)) {
			level = DefinePubOther.addComma(level);
			level = level + "ĩ��";
		}
		aMap.put(IDefineReport.LEVEL, level);
		for (int i = 0; i < len; i++) {
			if (lstGroupAble == null)
				lstGroupAble = new ArrayList();
			lstGroupAble.add(groupAbleArray[i]);
		}

		aMap.put(IDefineReport.FIELD_FNAME, getGroupColName(groupAbleArray));
		aMap
				.put(IDefineReport.ORDER_INDEX, groupAbleArray[0]
						.getSummaryIndex());
		aMap.put(IDefineReport.COL_ID, lstGroupAble);

		return aMap;
	}

	/**
	 * �õ����ϻ���
	 * 
	 * @param groupAbleArray
	 * @return
	 */
	private String getGroupColName(IGroupAble[] groupAbleArray) {
		String sSourceID;
		String sSourceColID;
		String sSourceName = null;
		String sSourceColName = null;
		String colName = "";

		int len = groupAbleArray.length;
		for (int i = 0; i < len; i++) {
			sSourceID = groupAbleArray[i].getSourceID();
			sSourceColID = groupAbleArray[i].getSourceColID();

			sSourceName = DefinePub.getDataSourceNameWithID(querySource,
					sSourceID);
			sSourceColName = DefinePub.getDataSourceColNameWithID(querySource,
					sSourceID, sSourceColID);
			colName = DefinePubOther.addComma(colName);
			colName = colName + sSourceName + "." + sSourceColName;
		}
		return colName;
	}

}
