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
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.cellvalue.AbstractFormula;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:���鱨���壬����������)�ͷ���ҳ��
 * </p>
 * <p>
 * Description:���鱨���壬����������)�ͷ���ҳ��
 * </p>
 * <p>

 * @version 6.2.40
 */
public class ReportOrderSet extends FTitledPanel {

	private static final long serialVersionUID = 1L;

	// ����������
	private FTable ftabOrderSet = null;

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
	public ReportOrderSet(ReportGuideUI reportGuideUI) {
		// this.querySource = reportGuideUI.querySource;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		// �����ʼ������
		jbInit();

		if (reportGuideUI.sReportId == null) {// ����
		} else {// �޸�
			showOrderSet(groupReport);
		}

	}

	/**
	 * �����ʼ������
	 * 
	 */
	private void jbInit() {
		// ����������
		ftabOrderSet = new MyFTable();
		FScrollPane fspnlOrderSet = new FScrollPane(ftabOrderSet);

		// ���ư�ť
		FButton upBtn = new FButton("upBtn", "����");
		upBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ѡ����
				int curRowIndex = ftabOrderSet.getCurrentRowIndex();
				if (curRowIndex <= 0)
					return;

				// ǰһ������Ϣ
				Map preMap = ftabOrderSet.getDataByIndex(curRowIndex - 1);
				String preOrderIndex = preMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				Object preValue = preMap.get(IDefineReport.COL_ID);

				// ��ǰ����Ϣ
				Map curMap = ftabOrderSet.getDataByIndex(curRowIndex);
				String curOrderIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				Object curValue = curMap.get(IDefineReport.COL_ID);

				// �ı�����˳��orderIndexֵ
				saveOrderIndex(preValue, curOrderIndex);
				saveOrderIndex(curValue, preOrderIndex);

				// �ı�λ��
				List lstValue = ftabOrderSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex - 1, curMap);
				ftabOrderSet.setData(lstValue);
				ftabOrderSet.setRowSelectionInterval(0, curRowIndex - 1);

			}
		});
		// ���ư�ť
		FButton downBtn = new FButton("downBtn", "����");
		downBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// ѡ����
				int curRowIndex = ftabOrderSet.getCurrentRowIndex();
				if (curRowIndex < 0
						|| curRowIndex == ftabOrderSet.getModel().getRowCount() - 1)
					return;
				// ��һ������Ϣ
				Map nextMap = ftabOrderSet
						.getDataByIndex(curRowIndex + 1);
				String nextOrderIndex = nextMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				String nextSummaryID = nextMap.get(IDefineReport.COL_ID)
						.toString();

				// ��ǰ����Ϣ
				Map curMap = ftabOrderSet.getDataByIndex(curRowIndex);
				String curOrderIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				String curSummaryID = curMap.get(IDefineReport.COL_ID)
						.toString();

				// �ı�����˳��orderIndexֵ
				saveOrderIndex(nextSummaryID, curOrderIndex);
				saveOrderIndex(curSummaryID, nextOrderIndex);

				// �ı�λ��
				List lstValue = ftabOrderSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex + 1, curMap);
				ftabOrderSet.setData(lstValue);
				ftabOrderSet.setRowSelectionInterval(0, curRowIndex + 1);
			}
		});

		// ���尴ť���
		FFlowLayoutPanel btnPnl = new FFlowLayoutPanel();
		btnPnl.addControl(upBtn, new TableConstraints(1, 1, true, false));
		btnPnl.addControl(downBtn, new TableConstraints(1, 1, true, false));

		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(70);
		rLay.setColumnGap(2);
		this.setLayout(rLay);
		this.addControl(fspnlOrderSet, new TableConstraints(1, 1, true, true));
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

			String[][] columns = new String[][] { { IDefineReport.FIELD_FNAME,
					"�ֶ�����", "200" } };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();
				if ("joinType".equals(col[0])) {
					column.setEditable(true);
				}
				column.setId(col[0]);
				column.setTitle(col[1]);
				column.setSortable(false);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(500);
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
	 * ����SummaryID�ı�OrderIndexֵ
	 * 
	 * @param sSummaryID
	 * @param orderIndex
	 */
	private void saveOrderIndex(Object value, String orderIndex) {
		if (value instanceof IGroupAble) {
			((IGroupAble) value).setOrderIndex(orderIndex);
		} else if (value instanceof MyCalculateValueImpl) {
			((MyCalculateValueImpl) value).setOrderIndex(orderIndex);
		}
	}

	/**
	 * ��ʾ������Ϣ
	 */
	void showOrderSet(GroupReport groupReport) {
		// �õ���������ͷ���Ŀ�ʼ��
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = rowIndex[rowIndex.length - 1];

		// �õ�������
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		List lstResult = new ArrayList();

		MyCalculateValueImpl calc;

		// ѭ�������õ�����ֵ
		for (int i = 0; i < colCount; i++) {
			// �õ���Ԫ��ֵ
			if (groupReport.getCellElement(i, row) == null)
				continue;
			object = groupReport.getCellElement(i, row).getValue();
			// �ж��ǲ��Ƿ�����,���Ƿ������ж��Ƿ����򣬵õ���������
			if (object instanceof MyGroupValueImpl) {
				iGroupAbleArray = ((MyGroupValueImpl) object)
						.getGroupAbleArray();
				arrayNum = iGroupAbleArray.length;
				// ѭ��ȡ�õ�Ԫ������������˳���
				for (int j = 0; j < arrayNum; j++) {
					if (!Common.isNullStr(iGroupAbleArray[j].getLevel())
							|| Common.estimate(iGroupAbleArray[j].getIsTotal())) {
						continue;
					}
					// �ж��Ƿ�������
					if (!Common.isInteger(iGroupAbleArray[j].getOrderIndex()))
						continue;
					setOrderPos(lstResult, iGroupAbleArray[j]);
				}
			} else if (object instanceof MyCalculateValueImpl) {
				calc = ((MyCalculateValueImpl) object);
				// �ж��Ƿ�������
				if (Common.isNullStr(calc.getOrderIndex())
						|| !Common.isInteger(calc.getOrderIndex()))
					continue;
				setOrderPos(lstResult, calc);
			}
		}

		// ��ʾ��Ϣ
		ftabOrderSet.setData(lstResult);
	}

	/**
	 * ���������б�
	 * 
	 * @param lstResult
	 * @param orderObj
	 */
	private void setOrderPos(List lstResult, Object value) {
		String curOrderIndex = "0";
		if (value instanceof IGroupAble) {
			curOrderIndex = ((IGroupAble) value).getOrderIndex();
		} else if (value instanceof MyCalculateValueImpl) {
			curOrderIndex = ((MyCalculateValueImpl) value).getOrderIndex();
		}
		String orderIndex = null;
		Map aMap = null;
		int count = lstResult.size();
		if (count == 0) {
			aMap = getOrderListValue(value);
			lstResult.add(aMap);
			return;
		}

		for (int i = count - 1; i >= 0; i--) {
			aMap = new HashMap();
			orderIndex = ((Map) lstResult.get(i))
					.get(IDefineReport.ORDER_INDEX).toString();
			if (!Common.isInteger(orderIndex))
				return;
			if (Integer.parseInt(curOrderIndex) < Integer.parseInt(orderIndex)) {
				aMap = getOrderListValue(value);
				lstResult.add(i, aMap);
				break;
			} else {
				if (i == count - 1) {
					aMap = getOrderListValue(value);
					lstResult.add(aMap);
					break;
				}
			}

		}
	}

	private Map getOrderListValue(Object value) {
		Map aMap = new HashMap();
		if (value instanceof IGroupAble) {
			IGroupAble iGroupAble = (IGroupAble) value;
			aMap.put(IDefineReport.FIELD_FNAME, ((AbstractFormula) iGroupAble
					.getFormula()).getDisplayValue());
			if (IDefineReport.ASC_FLAG.equals(iGroupAble.getOrderType()))
				aMap.put(IDefineReport.ORDER_TYPE, "����");
			else
				aMap.put(IDefineReport.ORDER_TYPE, "����");
			aMap.put(IDefineReport.COL_ID, value);
			aMap.put(IDefineReport.ORDER_INDEX, iGroupAble.getOrderIndex());
		} else if (value instanceof MyCalculateValueImpl) {
			MyCalculateValueImpl calc = (MyCalculateValueImpl) value;
			aMap.put(IDefineReport.FIELD_FNAME, calc.getDispContent()
					.substring(1));
			if (IDefineReport.ASC_FLAG.equals(calc.getOrderType()))
				aMap.put(IDefineReport.ORDER_TYPE, "����");
			else
				aMap.put(IDefineReport.ORDER_TYPE, "����");
			aMap.put(IDefineReport.COL_ID, value);
			aMap.put(IDefineReport.ORDER_INDEX, calc.getOrderIndex());
		}
		return aMap;
	}
}
