/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.table.TablePanel;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.IToSource;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSourceRelations;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:数据源选择客户端界面
 * </p>
 * <p>
 * Description:数据源选择客户端界面
 * </p>
 * <p>

 */
public class DataSourceChoiceUI extends FDialog {

	private static final long serialVersionUID = 1L;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	// 数据源表格
	private FTable dataSourceTable = null;

	// 保存选中的数据源
	private List lstDataSource = null;

	private GroupReport groupReport;

	private ReportQuerySource querySource;

	/**
	 * 构造函数
	 * 
	 * @param definReportServ
	 *            数据库接口
	 */
	public DataSourceChoiceUI(Frame frame, IDefineReport definReportServ,
			List lstDataSource) {
		super(frame);
		this.setSize(600, 400);
		this.setTitle("数据源选择");
		this.setModal(true);
		// 定义数据库接口
		this.definReportServ = definReportServ;
		// 保存选中的数据源
		this.lstDataSource = lstDataSource;

		groupReport = ((ReportGuideUI) frame).fpnlDefineReport.groupReport;
		querySource = ((ReportGuideUI) frame).querySource;
		// 调用界面初始化方法院
		jbInit();
	}

	/**
	 * 界面初始化方法
	 * 
	 */
	private void jbInit() {

		// 定义数据源表格
		dataSourceTable = new TablePanel(new String[][] {
				{ IDefineReport.OBJECT_CLASS, "类别", "100" },
				{ IDefineReport.OBJECT_CNAME, "表或视图名称", "300" } }, true);

		dataSourceTable.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent mouseevent) {
				if (mouseevent.getButton() != 1
						|| !(mouseevent.getSource() instanceof JTable))
					return;

				Boolean boolean1;
				int j = dataSourceTable.getLeftLockedTable().getSelectedRow();
				if (j < 0)
					return;
				Object obj = dataSourceTable.getValueAt(j, "isCheck");
				if (obj instanceof Boolean) {
					boolean1 = (Boolean) obj;
					if (!boolean1.equals(Boolean.TRUE)) {
						String sourceID = dataSourceTable.getDataByIndex(j)
								.get(IDefineReport.DICID).toString();
						String sInfo = null;
						try {
							sInfo = checkIsUse(sourceID);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(
									DataSourceChoiceUI.this, "选择数据源发生错误，错误信息："
											+ e.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						}
						if (!Common.isNullStr(sInfo)) {
							dataSourceTable.setValueAt(Boolean.TRUE, j,
									"isCheck");
							JOptionPane.showMessageDialog(
									DataSourceChoiceUI.this, sInfo, "提示",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
				}
				super.mouseClicked(mouseevent);
			}
		});

		// 设置数据源表格列可以拖动宽度
		for (int i = 0; i < dataSourceTable.getColumnModel().getColumnCount(); i++) {
			dataSourceTable.getColumnModel().getColumn(i).setMinWidth(0);
			dataSourceTable.getColumnModel().getColumn(i).setMaxWidth(500);
		}
		// 设置数据表格数据
		// 得到数据源信息
		List dataSource_List = definReportServ.getDataSource();
		dataSourceTable.setData(dataSource_List);
		// 显示选中状态
		showChoice();

		// 定义数源信息面板
		FPanel dataSourcePnl = new FPanel();
		dataSourcePnl.setTitle("数据源");
		dataSourcePnl.setLayout(new RowPreferedLayout(1));
		dataSourcePnl.addControl(dataSourceTable, new TableConstraints(1, 1,
				true, true));

		// 定义“确定”按钮
		FButton okBtn = new FButton("okBtn", "确定");
		okBtn.addActionListener(new OkActionListener());
		// 定义”取消“按钮
		FButton cancelBtn = new FButton("cancelBtn", "取 消");
		// 实现“取消”按钮点击事件
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				DataSourceChoiceUI.this.setVisible(false);
			}
		});

		// 定义按钮面板
		FFlowLayoutPanel btnFpnl = new FFlowLayoutPanel();
		// 设置靠右显示
		btnFpnl.setAlignment(FlowLayout.RIGHT);
		// “确定”按钮加入按钮面板
		btnFpnl.addControl(okBtn);
		// “取消”按钮加入按钮面板
		btnFpnl.addControl(cancelBtn);

		// 定义主面板及布局
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRlay = new RowPreferedLayout(1);
		mainRlay.setRowHeight(30);
		mainPnl.setLayout(mainRlay);
		// 数据源表格加入主面板
		mainPnl.add(dataSourcePnl, new TableConstraints(1, 1, true, true));
		// 按钮面板加入主面板
		mainPnl.add(btnFpnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// 增加界面激活事件
		// this.addWindowListener(new DswindowActivated());

	}

	/**
	 * 显示选中状态
	 */
	private void showChoice() {
		// 判断选中的数据源是否存在
		if (lstDataSource == null) {
			// 不存在退出
			return;
		}

		// 保存数据源表格OBJECT_ENAME值
		String dataSourceList = null;
		// 保存选中的数据源的OBJECT_ENAME值
		String dataSourceChoice = null;
		// 取得数据源表格列表
		List dataList = dataSourceTable.getData();
		// 数据源表格列表循环
		for (int i = 0; i < dataList.size(); i++) {
			// 设当前数据源列表选中状态为false
			dataSourceTable.setCheckBoxSelectedAtRow(i, false);
			// 取得当前数据源列表的ID值
			dataSourceList = ((Map) dataList.get(i)).get(IDefineReport.DICID)
					.toString();
			// 选中数据源表格列表循环
			for (int j = 0; j < lstDataSource.size(); j++) {
				// 取得当前选中数据源列表的ID值
				dataSourceChoice = ((Map) lstDataSource.get(j)).get(
						IDefineReport.DICID).toString();

				// 判断"数据源表格ID值"与"选中数据源列表的ID值"是否相等
				if (dataSourceList.equals(dataSourceChoice)) {
					// 设当前数据源列表选中状态为true
					dataSourceTable.setCheckBoxSelectedAtRow(i, true);
					continue;
				}
			}
		}
	}

	/**
	 * 确定按钮点击事件
	 */
	private class OkActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// 得到选中的数据源
			lstDataSource = dataSourceTable.getSelectedDataByCheckBox();
			// 判断是否选中的数据源
			if (lstDataSource.size() == 0) {
				JOptionPane
						.showMessageDialog(DataSourceChoiceUI.this,
								"您未选择数据，请勾选数据源！", "提示",
								JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// 关闭窗体
			DataSourceChoiceUI.this.setVisible(false);

		}
	}

	/**
	 * 得到选中的数据信息方法
	 * 
	 * @return 返回选中的数据信息
	 */
	public List getDataSource() {
		return lstDataSource;
	}

	private String checkIsUse(String sourceID) throws Exception {
		// 得到操作区域行
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = indexs[0];

		int colCount = groupReport.getColumnCount();
		MyGroupValueImpl GroupValueImpl;
		MyCalculateValueImpl calculateValueImpl;
		IGroupAble[] groupAble;
		FunctionRef[] functionRef;
		Object value;
		// 判断字段是否已被使用
		for (int i = 0; i < colCount; i++) {
			if (groupReport.getCellElement(i, row) == null)
				continue;
			value = groupReport.getCellElement(i, row).getValue();

			if (value instanceof MyGroupValueImpl) { // 判断是否是分组列
				GroupValueImpl = ((MyGroupValueImpl) groupReport
						.getCellElement(i, row).getValue());
				groupAble = GroupValueImpl.getGroupAbleArray();
				for (int j = 0; j < groupAble.length; j++) {
					functionRef = groupAble[j].getFormula()
							.getFunctionRefArray();
					for (int k = 0; k < functionRef.length; k++) {
						if (sourceID.equals(functionRef[k].getSourceID())) {
							return "已被报表使用,不允许取消勾选！";
						}
					}
				}
			} else if (value instanceof MyCalculateValueImpl) { // 判断是否是计算了列
				calculateValueImpl = ((MyCalculateValueImpl) groupReport
						.getCellElement(i, row).getValue());
				IMeasureAttr[] measureAttr = calculateValueImpl
						.getMeasureArray();
				for (int j = 0; j < measureAttr.length; j++) {
					if (sourceID.equals(measureAttr[j].getSourceID())) {
						return "已被报表使用,不允许取消勾选！";
					}
				}
			}
		}

		// 判断数据源是否已设置过滤条件
		IParameter[] parameter = querySource.getParameterArray();
		SummaryParameterImpl summaryParameterImpl;
		IToSource[] toSource;
		String chName;
		if (parameter != null)
			for (int i = 0; i < parameter.length; i++) {
				summaryParameterImpl = (SummaryParameterImpl) parameter[i];
				chName = summaryParameterImpl.getChName();
				if (DataSourceSet.checkDefaultPara(chName))
					continue;
				toSource = summaryParameterImpl.getToSourceArray();
				for (int k = 0; k < toSource.length; k++) {
					if (sourceID.equals(toSource[k].getSourceID())) {
						return "数据源已设置过滤条件，不允许取消勾选！";
					}
				}
			}
		// 判断数据源是否已设置过滤条件
		if (querySource.getDataSourceManager() != null) {
			IDataSourceRelations[] dataSourceRelations = querySource
					.getDataSourceManager().getDataSourceRelationsArray();
			if (dataSourceRelations == null)
				return "";
			SummaryDataSourceRelationsImpl dataSourceRelationsImpl;
			RefSource[] refSource;
			for (int i = 0; i < dataSourceRelations.length; i++) {
				dataSourceRelationsImpl = ((SummaryDataSourceRelationsImpl) dataSourceRelations[i]);
				refSource = dataSourceRelationsImpl.getRefSourceArray();
				for (int j = 0; j < refSource.length; j++) {
					if (sourceID.equals(refSource[j].getSourceID())) {
						return "数据源已设置关联关系，不允许取消勾选！";
					}
				}
			}
		}

		return "";
	}
}
