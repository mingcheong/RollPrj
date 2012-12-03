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
 * Title:分组报表定义，（排序设置)客服端页面
 * </p>
 * <p>
 * Description:分组报表定义，（排序设置)客服端页面
 * </p>
 * <p>

 * @version 6.2.40
 */
public class ReportOrderSet extends FTitledPanel {

	private static final long serialVersionUID = 1L;

	// 定义排序表格
	private FTable ftabOrderSet = null;

	// 定义报表
	private GroupReport groupReport = null;

	// 排序类型

	/**
	 * 构造函数
	 * 
	 * @param groupAreaImpl
	 *            分组类
	 * @param sReportId
	 *            报表ID,增加报表传null或"",修改报表传报表IDF
	 */
	public ReportOrderSet(ReportGuideUI reportGuideUI) {
		// this.querySource = reportGuideUI.querySource;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		// 界面初始化方法
		jbInit();

		if (reportGuideUI.sReportId == null) {// 增加
		} else {// 修改
			showOrderSet(groupReport);
		}

	}

	/**
	 * 界面初始化方法
	 * 
	 */
	private void jbInit() {
		// 定义排序表格
		ftabOrderSet = new MyFTable();
		FScrollPane fspnlOrderSet = new FScrollPane(ftabOrderSet);

		// 上移按钮
		FButton upBtn = new FButton("upBtn", "上移");
		upBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// 选中列
				int curRowIndex = ftabOrderSet.getCurrentRowIndex();
				if (curRowIndex <= 0)
					return;

				// 前一个列信息
				Map preMap = ftabOrderSet.getDataByIndex(curRowIndex - 1);
				String preOrderIndex = preMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				Object preValue = preMap.get(IDefineReport.COL_ID);

				// 当前列信息
				Map curMap = ftabOrderSet.getDataByIndex(curRowIndex);
				String curOrderIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				Object curValue = curMap.get(IDefineReport.COL_ID);

				// 改变排列顺序orderIndex值
				saveOrderIndex(preValue, curOrderIndex);
				saveOrderIndex(curValue, preOrderIndex);

				// 改变位置
				List lstValue = ftabOrderSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex - 1, curMap);
				ftabOrderSet.setData(lstValue);
				ftabOrderSet.setRowSelectionInterval(0, curRowIndex - 1);

			}
		});
		// 下移按钮
		FButton downBtn = new FButton("downBtn", "下移");
		downBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// 选中列
				int curRowIndex = ftabOrderSet.getCurrentRowIndex();
				if (curRowIndex < 0
						|| curRowIndex == ftabOrderSet.getModel().getRowCount() - 1)
					return;
				// 后一个列信息
				Map nextMap = ftabOrderSet
						.getDataByIndex(curRowIndex + 1);
				String nextOrderIndex = nextMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				String nextSummaryID = nextMap.get(IDefineReport.COL_ID)
						.toString();

				// 当前列信息
				Map curMap = ftabOrderSet.getDataByIndex(curRowIndex);
				String curOrderIndex = curMap.get(IDefineReport.ORDER_INDEX)
						.toString();
				String curSummaryID = curMap.get(IDefineReport.COL_ID)
						.toString();

				// 改变排列顺序orderIndex值
				saveOrderIndex(nextSummaryID, curOrderIndex);
				saveOrderIndex(curSummaryID, nextOrderIndex);

				// 改变位置
				List lstValue = ftabOrderSet.getData();
				lstValue.remove(curRowIndex);
				lstValue.add(curRowIndex + 1, curMap);
				ftabOrderSet.setData(lstValue);
				ftabOrderSet.setRowSelectionInterval(0, curRowIndex + 1);
			}
		});

		// 定义按钮面板
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
	 * 设置表格列
	 * 
	 * @param ftable需设置的表格
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
					"字段名称", "200" } };

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
			// 设置单选
			this.getRightActiveTable().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/**
	 * 根据SummaryID改变OrderIndex值
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
	 * 显示排序信息
	 */
	void showOrderSet(GroupReport groupReport) {
		// 得到操作区行头区的开始行
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = rowIndex[rowIndex.length - 1];

		// 得到总列数
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		List lstResult = new ArrayList();

		MyCalculateValueImpl calc;

		// 循环处理，得到排序值
		for (int i = 0; i < colCount; i++) {
			// 得到单元格值
			if (groupReport.getCellElement(i, row) == null)
				continue;
			object = groupReport.getCellElement(i, row).getValue();
			// 判断是不是分组列,如是分组列判断是否排序，得到最大排序号
			if (object instanceof MyGroupValueImpl) {
				iGroupAbleArray = ((MyGroupValueImpl) object)
						.getGroupAbleArray();
				arrayNum = iGroupAbleArray.length;
				// 循环取得单元格数组中排序顺序号
				for (int j = 0; j < arrayNum; j++) {
					if (!Common.isNullStr(iGroupAbleArray[j].getLevel())
							|| Common.estimate(iGroupAbleArray[j].getIsTotal())) {
						continue;
					}
					// 判断是否是数字
					if (!Common.isInteger(iGroupAbleArray[j].getOrderIndex()))
						continue;
					setOrderPos(lstResult, iGroupAbleArray[j]);
				}
			} else if (object instanceof MyCalculateValueImpl) {
				calc = ((MyCalculateValueImpl) object);
				// 判断是否是数字
				if (Common.isNullStr(calc.getOrderIndex())
						|| !Common.isInteger(calc.getOrderIndex()))
					continue;
				setOrderPos(lstResult, calc);
			}
		}

		// 显示信息
		ftabOrderSet.setData(lstResult);
	}

	/**
	 * 保存排序列表
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
				aMap.put(IDefineReport.ORDER_TYPE, "升序");
			else
				aMap.put(IDefineReport.ORDER_TYPE, "降序");
			aMap.put(IDefineReport.COL_ID, value);
			aMap.put(IDefineReport.ORDER_INDEX, iGroupAble.getOrderIndex());
		} else if (value instanceof MyCalculateValueImpl) {
			MyCalculateValueImpl calc = (MyCalculateValueImpl) value;
			aMap.put(IDefineReport.FIELD_FNAME, calc.getDispContent()
					.substring(1));
			if (IDefineReport.ASC_FLAG.equals(calc.getOrderType()))
				aMap.put(IDefineReport.ORDER_TYPE, "升序");
			else
				aMap.put(IDefineReport.ORDER_TYPE, "降序");
			aMap.put(IDefineReport.COL_ID, value);
			aMap.put(IDefineReport.ORDER_INDEX, calc.getOrderIndex());
		}
		return aMap;
	}
}
