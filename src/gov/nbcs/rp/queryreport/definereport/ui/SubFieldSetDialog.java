/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomStatisticCaliber;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title: 分栏设置客服端界面
 * </p>
 * <p>
 * Description: 分栏设置客服端界面
 * </p>

 */
public class SubFieldSetDialog extends FDialog {

	private static final long serialVersionUID = 1L;

	private ReportGuideUI reportGuideUI = null;

	// 定义SummaryReport对象
	// private SummaryReport summaryReport = null;

	private ReportQuerySource querySource = null;

	// 定义枚举Tabel
	private EnumTable ftabEnum = null;

	// 定义分栏名称文本框
	private FTextField ftxtSubField1 = null;

	private FTextField ftxtSubField2 = null;

	// 定义是否包含上次设置选择框
	private FCheckBox fchkSubField1 = null;

	private FCheckBox fchkSubField2 = null;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	// 数据源树1
	private CustomTree enumATree = null;

	// 数据源树2
	private CustomTree enumBTree = null;

	// 定义选择节次数字框
	private IntegerSpinner jspSubField1 = null;

	private IntegerSpinner jspSubField2 = null;

	// 定义列层次数字框
	private IntegerSpinner jspRow = null;

	// 存放选中的枚举源
	private List enumInfoMap = null;

	private SummaryReportPane summaryReportPane = null;

	private GroupReport groupReport = null;

	// 保存上次增加的列,用于取消设置使用
	private int lastTimeAddCol = 0;

	public SubFieldSetDialog(ReportGuideUI reportGuideUI) {
		this.setModal(true);
		this.setSize(700, 525);
		// this.setExtendedState(JFrame.NORMAL);
		this.setTitle("分栏设置");
		this.reportGuideUI = reportGuideUI;
		this.querySource = reportGuideUI.querySource;
		this.definReportServ = reportGuideUI.definReportServ;
		this.summaryReportPane = reportGuideUI.fpnlDefineReport.designGroupReportPane;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;

		// stayOnTop();
		enumInfoMap = new ArrayList();
		// 初始化界面
		init();
		// 加载界面数据
		moduleActive();
	}

	/**
	 * 初始化界面
	 * 
	 */
	private void init() {
		// 定义枚举Tabel
		ftabEnum = new EnumTable();

		// 定义选择按钮
		FButton fbtnChoice = new FButton("", "选择>");
		fbtnChoice.addActionListener(new ChoiceActionListener());
		// 定义清除按钮
		FButton fbtnClear = new FButton("", "清除<");
		fbtnClear.addActionListener(new ClearActionListener());
		// 定义取数条件按钮
		FButton fbtnSetWhere = new FButton("", "设置条件");
		fbtnSetWhere.addActionListener(new SetActionListener());
		FLabel flblEmpty1 = new FLabel();
		FLabel flblEmpty2 = new FLabel();
		FLabel flblEmpty3 = new FLabel();
		// 定义按钮面板
		FPanel btnPanel = new FPanel();
		btnPanel.setLayout(new RowPreferedLayout(1));
		btnPanel.addControl(flblEmpty1, new TableConstraints(1, 1, true, true));
		btnPanel
				.addControl(fbtnChoice, new TableConstraints(1, 1, false, true));
		btnPanel.addControl(fbtnClear, new TableConstraints(1, 1, false, true));

		btnPanel.addControl(flblEmpty2, new TableConstraints(1, 1, true, true));
		btnPanel.addControl(fbtnSetWhere, new TableConstraints(1, 1, false,
				true));
		btnPanel
				.addControl(flblEmpty3, new TableConstraints(1, 1, false, true));

		// 层次码规则
		try {
			enumATree = new CustomTree("数据1", null, "code", "name", null, null,
					"code", true);
			enumATree.setIsCheckBoxEnabled(true);
			enumBTree = new CustomTree("数据2", null, "code", "name", null, null,
					"code", true);
			enumBTree.setIsCheckBoxEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FScrollPane fspnlEnumA = new FScrollPane(enumATree);
		FScrollPane fspnlEnumB = new FScrollPane(enumBTree);

		FSplitPane fSplitPaneTre = new FSplitPane();
		fSplitPaneTre.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		fSplitPaneTre.setDividerLocation(180);
		fSplitPaneTre.addControl(fspnlEnumA);
		fSplitPaneTre.addControl(fspnlEnumB);

		FPanel fpnlEnum = new FPanel();
		RowPreferedLayout rLayEnum = new RowPreferedLayout(5);
		rLayEnum.setColumnWidth(82);
		rLayEnum.setColumnGap(1);
		fpnlEnum.setLayout(rLayEnum);
		fpnlEnum.addControl(ftabEnum, new TableConstraints(1, 3, true, false));
		fpnlEnum.addControl(btnPanel, new TableConstraints(1, 1, true, false));
		fpnlEnum.addControl(fSplitPaneTre, new TableConstraints(1, 1, true,
				true));

		// 定义分组文本框
		ftxtSubField1 = new FTextField("列:");
		ftxtSubField1.setEditable(false);
		ftxtSubField1.setProportion(0.1f);
		ftxtSubField2 = new FTextField("列:");
		ftxtSubField2.setEditable(false);
		ftxtSubField2.setProportion(0.1f);

		FLabel flblSubField1 = new FLabel();
		flblSubField1.setText("需要细化到");
		FLabel flblSubField2 = new FLabel();
		flblSubField2.setText("需要细化到");

		// 定义选择节次数字框
		SpinnerModel modelSubField1 = new SpinnerNumberModel(1, 1, 100, 1);
		jspSubField1 = new IntegerSpinner(modelSubField1);
		SpinnerModel modelSubField2 = new SpinnerNumberModel(1, 1, 100, 1);
		jspSubField2 = new IntegerSpinner(modelSubField2);

		FLabel flblSubField11 = new FLabel();
		flblSubField11.setText("级。");
		FLabel flblSubField22 = new FLabel();
		flblSubField22.setText("级。");

		FFlowLayoutPanel fpnlSubField11 = new FFlowLayoutPanel();
		fpnlSubField11.setAlignment(FlowLayout.LEFT);
		fpnlSubField11.addControl(flblSubField1);
		fpnlSubField11.add(jspSubField1);
		fpnlSubField11.addControl(flblSubField11);

		FFlowLayoutPanel fpnlSubField12 = new FFlowLayoutPanel();
		fpnlSubField12.setAlignment(FlowLayout.LEFT);
		fpnlSubField12.addControl(flblSubField2);
		fpnlSubField12.add(jspSubField2);
		fpnlSubField12.addControl(flblSubField22);

		// 定义是否包含上次设置选择框
		fchkSubField1 = new FCheckBox("包含上级");
		fchkSubField1.setTitlePosition("RIGHT");
		fchkSubField2 = new FCheckBox("包含上级");
		fchkSubField2.setTitlePosition("RIGHT");

		FLabel flblRow = new FLabel();
		flblRow.setText("分栏表头结束行位于第");
		// 定义列层次数字框
		int row[] = DefinePub.getHeaderRow(groupReport);
		int headRowLast = 3;
		if (row != null) {
			headRowLast = row[row.length - 1] + 1;
		}
		SpinnerModel modelRow = new SpinnerNumberModel(headRowLast, 1, 100, 1);
		jspRow = new IntegerSpinner(modelRow);
		FLabel flblRow1 = new FLabel();
		flblRow1.setText("行");
		FFlowLayoutPanel fpnlRow = new FFlowLayoutPanel();
		fpnlRow.setAlignment(FlowLayout.LEFT);
		fpnlRow.addControl(flblRow);
		fpnlRow.add(jspRow);
		fpnlRow.addControl(flblRow1);

		FButton fbtnAutoSet = new FButton("", "自动设置");
		fbtnAutoSet.addActionListener(new AutoSetActionListener());
		FButton fbtnCancelSet = new FButton("", "取消上次自动设置");
		fbtnCancelSet.addActionListener(new CanelSetActionListener());
		FFlowLayoutPanel fpnlBtn = new FFlowLayoutPanel();
		// 定义按钮面板
		fpnlBtn.setAlignment(FlowLayout.LEFT);
		fpnlBtn.addControl(fbtnAutoSet);
		fpnlBtn.addControl(fbtnCancelSet);

		FPanel fpnlAutoSet = new FPanel();
		fpnlAutoSet.setTitle("自动多栏设置:");
		RowPreferedLayout rLay = new RowPreferedLayout(3);
		rLay.setColumnWidth(240);
		rLay.setColumnGap(0);
		rLay.setRowHeight(23);
		fpnlAutoSet.setLayout(rLay);
		fpnlAutoSet.addControl(ftxtSubField1, new TableConstraints(1, 1, false,
				false));
		fpnlAutoSet.addControl(fpnlSubField11, new TableConstraints(1, 1,
				false, false));
		fpnlAutoSet.addControl(fchkSubField1, new TableConstraints(1, 1, false,
				true));
		fpnlAutoSet.addControl(ftxtSubField2, new TableConstraints(1, 1, false,
				false));
		fpnlAutoSet.addControl(fpnlSubField12, new TableConstraints(1, 1,
				false, true));
		fpnlAutoSet.addControl(fchkSubField2, new TableConstraints(1, 1, false,
				true));
		fpnlAutoSet
				.addControl(fpnlRow, new TableConstraints(1, 3, false, true));
		fpnlAutoSet.addControl(fpnlBtn, new TableConstraints(1, 3, true, true));

		ClosePanel closePanel = new ClosePanel();

		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(1));

		fpnlMain.addControl(fpnlEnum, new TableConstraints(1, 1, true, true));
		fpnlMain.addControl(fpnlAutoSet,
				new TableConstraints(6, 1, false, true));
		fpnlMain
				.addControl(closePanel, new TableConstraints(2, 1, false, true));

		this.getContentPane().add(fpnlMain);
	}

	/**
	 * 设置表格列
	 * 
	 * @param ftable需设置的表格
	 */
	private class EnumTable extends FTable {

		private static final long serialVersionUID = 1L;

		public EnumTable() {
			super();
			this.setShowSumRow(false);
			init();
		}

		public EnumTable(boolean arg0) {
			super(arg0);
			init();
		}

		private void init() {
			String[][] columns = new String[][] {
					{ IDefineReport.DATASOURCE_NAME, "数据源名", "100" },
					{ IDefineReport.FIELD_FNAME, "字段名", "100" } };

			for (int i = 0; i < columns.length; i++) {
				String[] col = columns[i];
				FBaseTableColumn column = new FBaseTableColumn();
				column.setId(col[0]);
				column.setTitle(col[1]);
				if (col.length == 3) {
					column.setWidth(Integer.parseInt(col[2]));
					column.setPreferredWidth(Integer.parseInt(col[2]));
					column.setMaxWidth(500);
					column.setMinWidth(0);
				}
				this.addColumn(column);
			}
		}
	}

	/**
	 * 加载界面数据
	 * 
	 */
	private void moduleActive() {
		// 检查当前单元是否允许设置列条件
		int col = summaryReportPane.getCellSelection().getColumn();
		int row = summaryReportPane.getCellSelection().getRow();
		Object value = groupReport.getCellValue(col, row);

		if (!(value instanceof MyCalculateValueImpl)) {
			return;
		}

		List lstSourceID = null;
		IMeasureAttr[] measureAttr = ((MyCalculateValueImpl) value)
				.getMeasureArray();
		int length = measureAttr.length;
		for (int i = 0; i < length; i++) {
			if (lstSourceID == null)
				lstSourceID = new ArrayList();
			lstSourceID.add(measureAttr[i].getSourceID());
		}

		List lstEnumWhereSet = DefinePub.showEnumWhereSetInfo(querySource,
				true, lstSourceID);
		ftabEnum.setData(lstEnumWhereSet);

		// 得到条件
		IStatisticCaliber[] statisticCaliberArray = ((MyCalculateValueImpl) value)
				.getStatisticCaliberArray();
		if (statisticCaliberArray == null)
			return;
		length = statisticCaliberArray.length;
		String sEnumId;
		String sNodeId;
		Map selectData;
		CustomTree curTree;
		for (int i = 0; i < length; i++) {
			// 判断是否是分栏设置的条件
			if (DefinePub.isPACaliber(statisticCaliberArray[i])) {
				if (statisticCaliberArray[i] instanceof ICustomStatisticCaliber) {
					sEnumId = ((ICustomStatisticCaliber) statisticCaliberArray[i])
							.getEnumID();
					sNodeId = ((ICustomStatisticCaliber) statisticCaliberArray[i])
							.getNodeID();
					selectData = getDataWithEnumId(sEnumId, lstEnumWhereSet);
					curTree = showEnumTree(sEnumId, selectData);
					if (curTree != null)
						setNodeSelect(sNodeId, curTree);
				}
			}
		}
	}

	/**
	 * 根据枚举ID得到Map值
	 * 
	 * @param sEnumId
	 * @param lstEnumWhereSet
	 * @return
	 */
	private Map getDataWithEnumId(String sEnumId, List lstEnumWhereSet) {
		if (sEnumId == null)
			return null;
		if (lstEnumWhereSet == null || lstEnumWhereSet.size() == 0)
			return null;
		String sEnumIdTmp;
		int size = lstEnumWhereSet.size();
		for (int i = 0; i < size; i++) {
			if (((Map) lstEnumWhereSet.get(i)).get(IDefineReport.ENUM_ID) == null) {
				continue;
			}
			sEnumIdTmp = ((Map) lstEnumWhereSet.get(i)).get(
					IDefineReport.ENUM_ID).toString();
			if (sEnumId.equals(sEnumIdTmp)) {
				return (Map) lstEnumWhereSet.get(i);
			}
		}
		return null;
	}

	/**
	 * 关闭按钮面板
	 */
	private class ClosePanel extends FFlowLayoutPanel {
		private static final long serialVersionUID = 1L;

		public ClosePanel() {
			this.setAlignment(FlowLayout.RIGHT);

			FButton fbtnClose = new FButton("fbtnClose", "关闭");
			fbtnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					SubFieldSetDialog.this.dispose();
				}
			});

			this.addControl(fbtnClose);
		}
	}

	/**
	 * 选择枚举源按钮点击事件
	 */
	private class ChoiceActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 判断是否已选中两个枚举
			if (enumInfoMap.size() == 2) {
				return;
			}

			// 得到当前选中的行
			int index = ftabEnum.getCurrentRowIndex();
			if (index < 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"请选择分栏列！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			Map selectData = ftabEnum.getDataByIndex(index);

			// 得到枚举ID
			String sEnumId = selectData.get(IDefineReport.ENUM_ID).toString();

			// 判断枚举源是否已选择
			for (int i = 0; i < enumInfoMap.size(); i++) {
				if (((Map) enumInfoMap.get(i)).get(ISysRefCol.REFCOL_ID)
						.equals(sEnumId)) {
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"您选择的分栏信息列已被选择,请选择其他列！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}

			// 显示树信息
			showEnumTree(sEnumId, selectData);
		}
	}

	/**
	 * 显示树信息,根据枚举ID
	 * 
	 * @param sEnumId
	 * @param selectData
	 */
	private CustomTree showEnumTree(String sEnumId, Map selectData) {
		if (Common.isNullStr(sEnumId))
			return null;
		String sEnumIdTmp = sEnumId.substring(IDefineReport.ENUM_.length());

		// 根据enumId得到引用列信息
		Map selectDataMap = null;
		try {
			selectDataMap = definReportServ.getEnumDataWithEnumID(sEnumIdTmp);

			if (selectDataMap == null) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"未找到对应的引用列", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			selectDataMap.put(ISysRefCol.REFCOL_ID, sEnumIdTmp);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
		if (selectDataMap == null) {
			return null;
		}

		selectDataMap.putAll(selectData);
		// 得到数据ID
		// selectDataMap.put(IDefineReport.SOURCE_ID, selectData
		// .get(IDefineReport.SOURCE_ID));
		// // 数据源名称
		// selectDataMap.put(IDefineReport.DATASOURCE_NAME, selectData
		// .get(IDefineReport.DATASOURCE_NAME));
		// // 得到列名称
		// selectDataMap.put(IDefineReport.COL_ID, selectData
		// .get(IDefineReport.COL_ID));
		// // 得到列中文名称
		// selectDataMap.put(IDefineReport.FIELD_FNAME, selectData
		// .get(IDefineReport.FIELD_FNAME));

		String sLvlField = selectDataMap.get(ISysRefCol.LVL_FIELD).toString();
		// refcol_name
		String sRefcolName = selectDataMap.get(ISysRefCol.REFCOL_NAME)
				.toString();
		// lvl_style
		String sLvlStyle = selectDataMap.get(ISysRefCol.LVL_STYLE).toString();
		// 枚举源DataSet
		DataSet dsEnum = (DataSet) selectDataMap.get(IDefineReport.ENUM_DATA);

		if (enumInfoMap.size() == 0) {
			// 设置根名称
			enumATree.setRootName(sRefcolName);
			// 设置DataSet
			enumATree.setDataSet(dsEnum);
			// 设置IdName
			enumATree.setIdName(sLvlField);
			// 设置编码规则
			enumATree.setCodeRule(SysCodeRule.createClient(sLvlStyle));
			// 设置SortKey
			enumATree.setSortKey(sLvlField);
			try {
				enumATree.reset();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			ftxtSubField1.setValue(sRefcolName);

			MyTreeNode root = (MyTreeNode) enumATree.getRoot();
			((SpinnerNumberModel) jspSubField1.getModel())
					.setMaximum(new Integer(root.getDepth()));
			enumInfoMap.add(selectDataMap);
			return enumATree;

		} else if (enumInfoMap.size() == 1) {
			// 设置根名称
			enumBTree.setRootName(sRefcolName);
			// 设置DataSet
			enumBTree.setDataSet(dsEnum);
			// 设置IdName
			enumBTree.setIdName(sLvlField);
			// 设置编码规则
			enumBTree.setCodeRule(SysCodeRule.createClient(sLvlStyle));
			// 设置SortKey
			enumBTree.setSortKey(sLvlField);
			try {
				enumBTree.reset();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			ftxtSubField2.setValue(sRefcolName);
			MyTreeNode root = (MyTreeNode) enumBTree.getRoot();
			((SpinnerNumberModel) jspSubField2.getModel())
					.setMaximum(new Integer(root.getDepth()));
			enumInfoMap.add(selectDataMap);
			return enumBTree;

		} else {
			return null;
		}

	}

	/**
	 * 清除<按钮点击事件
	 */
	private class ClearActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 判断数据源2是否有数据
			if (enumInfoMap.size() == 2) {
				// 设置根名称
				enumBTree.setRootName("数据2");
				// 设置DataSet
				enumBTree.setDataSet(DataSet.create());
				// 设置IdName
				enumBTree.setIdName(null);
				// 设置TextName
				// customTreeB.setTextName(null);
				// 设置编码规则
				enumBTree.setCodeRule(null);
				// 设置SortKey
				enumBTree.setSortKey(null);
				try {
					enumBTree.reset();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
				ftxtSubField2.setValue("");
			} else if (enumInfoMap.size() == 1) {// 判断数据源1是否有数据
				// 设置根名称
				enumATree.setRootName("数据1");
				// 设置DataSet
				enumATree.setDataSet(DataSet.create());
				// 设置IdName
				enumATree.setIdName(null);
				// 设置TextName
				// customTreeA.setTextName(null);
				// 设置编码规则
				enumATree.setCodeRule(null);
				// 设置SortKey
				enumATree.setSortKey(null);
				try {
					enumATree.reset();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SubFieldSetDialog.this,
							"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
				ftxtSubField1.setValue("");
			}
			enumInfoMap.remove(enumInfoMap.size() - 1);
		}
	}

	/**
	 * 取数按钮点击事件
	 */
	private class SetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 检查当前单元是否允许设置列条件
			int col = summaryReportPane.getCellSelection().getColumn();
			int row = summaryReportPane.getCellSelection().getRow();
			if (!checkCell(col, row)) {
				return;
			}

			// 根据枚举源A和B得组织成查询条件SQL（不需要拼上数据源别名）
			List lstAddSql = null;
			try {
				lstAddSql = getAddSqlValue();
				if (lstAddSql == null || lstAddSql.size() == 0) {
					new MessageBox(SubFieldSetDialog.this, "请选择分栏条件!",
							MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
					return;
				}

				// 保存分栏条件
				Object value = groupReport.getCellValue(col, row);

				saveCellValue(col, row, value, lstAddSql);
				// 刷新显示条件区信息
				refreshFilter(col, row);
				// 刷新显示
				reportGuideUI.fpnlDefineReport.designGroupReportPane
						.fireReportDataChanged();

				new MessageBox(SubFieldSetDialog.this, "设置成功!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(SubFieldSetDialog.this, "发生错误，错误信息："
						+ e.getMessage(), MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			}

		}
	}

	/**
	 * 刷新显示条件区信息
	 */
	private void refreshFilter(int col, int row) {
		Object obj = groupReport.getCellElement(col, row).getValue();
		if (obj == null || !(obj instanceof MyCalculateValueImpl)) {
			return;
		}
		MyCalculateValueImpl calculateValueImpl = (MyCalculateValueImpl) obj;
		String sFilter = DefinePub.getFilter(querySource, calculateValueImpl
				.getStatisticCaliberArray());

		CellElement cellFilter;
		if (groupReport.getCellElement(col, row + 1) == null) {
			cellFilter = new CellElement(col, row + 1);
			cellFilter.setValue(sFilter);
			groupReport.addCellElement(cellFilter, true);
		} else {
			cellFilter = groupReport.getCellElement(col, row + 1);
		}
		cellFilter.setValue(sFilter);
	}

	/**
	 * 检查是否已有分栏条件,如有分栏条件，删除分栏条件
	 * 
	 * @param iStatisticCaliber
	 * @param lstRow,行号列表
	 * @return
	 */
	private IStatisticCaliber[] delStatisticCaliberArray(
			IStatisticCaliber[] iStatisticCaliber) {

		// 记录分栏参数行号
		Map rowMap = new HashMap();

		if (iStatisticCaliber == null) {
			return null;
		}

		// 得到分栏条件的行号
		for (int i = 0; i < iStatisticCaliber.length; i++) {
			if (DefinePub.isPACaliber(iStatisticCaliber[i])) {
				rowMap.put(String.valueOf(i), String.valueOf(i));
			}
		}

		IStatisticCaliber[] iCurStatisticCaliber = new IStatisticCaliber[iStatisticCaliber.length
				- rowMap.size()];

		int j = 0;
		// 删除分栏条件
		for (int i = 0; i < iStatisticCaliber.length; i++) {
			if (rowMap.containsKey(String.valueOf(i))) {
				continue;
			}
			iCurStatisticCaliber[j] = iStatisticCaliber[i];
			j++;
		}

		return iCurStatisticCaliber;
	}

	/**
	 * IStatisticCaliberArray数据长度加长
	 * 
	 * @param iStatisticCaliber
	 * @param iNum
	 */
	private IStatisticCaliber[] addStatisticCaliberArrayLegth(
			IStatisticCaliber[] iStatisticCaliber, int iNum) {
		if (iNum < 1)
			return null;
		int iCount = 0;
		if (iStatisticCaliber != null) {
			iCount = iStatisticCaliber.length;
		}

		IStatisticCaliber[] iStatisticCaliberTmp = new IStatisticCaliber[iCount
				+ iNum];
		for (int i = 0; i < iCount; i++) {
			iStatisticCaliberTmp[i] = iStatisticCaliber[i];
		}
		return iStatisticCaliberTmp;
	}

	/**
	 * 检查当前单元是否允许设置列条件
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean checkCell(int col, int row) {
		Object value = reportGuideUI.fpnlDefineReport.groupReport.getCellValue(
				col, row);
		if (value == null) {
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"当前单元格未设置数据来源，列条件没法设置！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (!(value instanceof MyCalculateValueImpl)) {
			JOptionPane
					.showMessageDialog(SubFieldSetDialog.this,
							"当前单元格非计算列，列条件没法设置！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (DefinePub.checkIsCellFormula((MyCalculateValueImpl) value)) {
			JOptionPane.showMessageDialog(SubFieldSetDialog.this,
					"当前单元格是单元格计算列，列条件没法设置！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * 根据枚举源A和B得组织成查询条件SQL（不需要拼上数据源别名）
	 * 
	 * @return 列表
	 * @throws Exception
	 */
	private List getAddSqlValue() throws Exception {

		int iCount = enumInfoMap.size();
		// 判断是否选择了枚举源
		if (iCount == 0) {
			return null;
		}

		List lstResult = new ArrayList();
		// 得到第一个枚举源条件
		if (iCount >= 1) {
			Map tempMap = (Map) enumInfoMap.get(0);
			getEnumValue(tempMap, enumATree, lstResult);

		}
		// 得到第二个枚举源条件
		if (iCount == 2) {
			Map tempMap = (Map) enumInfoMap.get(1);
			getEnumValue(tempMap, enumBTree, lstResult);
		}
		return lstResult;
	}

	/**
	 * 得到一个节点信息和这个节点数据源的信息，得到这个节点查询条件，存于Map中，追上到List中
	 * 
	 * @param map
	 *            数据相关信息
	 * @param customTree树
	 * @param lstResult结果加上此List
	 * @throws Exception
	 */
	private void getEnumValue(DataSet ds, Map map, MyTreeNode myTreeNode,
			List lstResult) throws Exception {
		// 定义sql
		String sSqlWhere = "";
		String sSqlWhereName = "";

		// 数据源中文名
		String sDataSourceName = map.get(IDefineReport.DATASOURCE_NAME)
				.toString();
		// 列名
		String sFieldCode = map.get(IDefineReport.COL_ID).toString();
		// 列中文名
		String sFieldName = map.get(IDefineReport.FIELD_FNAME).toString();

		// 判断主键和层次码是否同一字段，进行不同处理
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		boolean isDiffer = isDiffer(map);

		String sSqlWhereTemp = getSqlWhere(ds, myTreeNode, isDiffer, priField,
				sFieldCode);

		if (sSqlWhereTemp != null) {
			sSqlWhere = " (" + sSqlWhereTemp + ")";
		}
		sSqlWhereTemp = getSqlWhereName(myTreeNode.getUserObject().toString(),
				sDataSourceName, sFieldName);
		if (sSqlWhereName != null) {
			sSqlWhereName = " 并且 (" + sSqlWhereTemp + ")";
		}

		Map resultMap = new HashMap();
		if ("".equals(sSqlWhere)) {
			resultMap.put(IDefineReport.CODE, null);
		} else {
			resultMap.put(IDefineReport.CODE, sSqlWhere);
		}
		resultMap.put(IDefineReport.NAME, sSqlWhereName);
		resultMap
				.put(IDefineReport.SOURCE_ID, map.get(IDefineReport.SOURCE_ID));
		resultMap.put(IDefineReport.COL_ID, map.get(IDefineReport.COL_ID));

		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		resultMap.put(IDefineReport.NODE_ID, myPfNode.getValue());
		resultMap.put(IDefineReport.ENUM_ID, map.get(IDefineReport.ENUM_ID));
		lstResult.add(resultMap);
	}

	/**
	 * 得到一个数据源值
	 * 
	 * @param map
	 *            数据相关信息
	 * @param customTree树
	 * @param lstResult结果加上此List
	 * @throws Exception
	 */
	private void getEnumValue(Map map, CustomTree customTree, List lstResult)
			throws Exception {
		// 定义sql
		String sSqlWhere = "";
		String sSqlWhereName = "";
		// 数据源中文名
		String sDataSourceName = map.get(IDefineReport.DATASOURCE_NAME)
				.toString();
		// 列名
		String sFieldCode = map.get(IDefineReport.COL_ID).toString();
		// 列中文名
		String sFieldName = map.get(IDefineReport.FIELD_FNAME).toString();

		// 判断主键和层次码是否同一字段，进行不同处理
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		boolean isDiffer = isDiffer(map);

		Map mapValue = getWhere(customTree, isDiffer, priField);
		if (mapValue == null) {
			return;
		}

		List list = (List) mapValue.get(IDefineReport.CODE);
		String sSqlWhereTemp = getSqlWhere(list, sFieldCode, isDiffer);
		if (sSqlWhereTemp != null) {
			sSqlWhere = " (" + sSqlWhereTemp + ")";
		}
		list = (List) mapValue.get(IDefineReport.NAME);
		sSqlWhereTemp = getSqlWhereName(list, sDataSourceName, sFieldName);
		if (sSqlWhereName != null) {
			sSqlWhereName = " 并且 (" + sSqlWhereTemp + ")";
		}
		Map resultMap = new HashMap();
		if ("".equals(sSqlWhere)) {
			resultMap.put(IDefineReport.CODE, null);
		} else {
			resultMap.put(IDefineReport.CODE, sSqlWhere);
		}
		resultMap.put(IDefineReport.NAME, sSqlWhereName);
		resultMap
				.put(IDefineReport.SOURCE_ID, map.get(IDefineReport.SOURCE_ID));
		resultMap.put(IDefineReport.COL_ID, map.get(IDefineReport.COL_ID));
		resultMap.put(IDefineReport.NODE_ID, this.getSelectNodeId(customTree));
		resultMap.put(IDefineReport.ENUM_ID, map.get(IDefineReport.ENUM_ID));
		lstResult.add(resultMap);
	}

	/**
	 * 判断主键和层次码是否同一字段
	 * 
	 * @param map
	 * @return相同返回false,不同返回true
	 */
	public static boolean isDiffer(Map map) {
		// 判断主键和层次码是否同一字段，进行不同处理
		String priField = map.get(ISysRefCol.PRIMARY_FIELD).toString();
		String lvlField = map.get(ISysRefCol.LVL_FIELD).toString();
		boolean isDiffer;
		if (priField.equalsIgnoreCase(lvlField)) {
			isDiffer = false;
		} else {
			isDiffer = true;
		}
		return isDiffer;
	}

	/**
	 * 得到SQL查询条件，根据List
	 * 
	 * @param list
	 * @param sFieldCode
	 * @return
	 */
	private String getSqlWhere(List list, String sFieldCode, boolean isDiffer) {
		if (list == null)
			return null;
		StringBuffer sSqlWhere = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (isDiffer) {
				if (!Common.isNullStr(sSqlWhere.toString())) {
					sSqlWhere.append(",");
				}
				sSqlWhere.append(list.get(i).toString());
			} else {
				if (!"".equals(sSqlWhere.toString())) {
					sSqlWhere.append(" or  ");
				}
				sSqlWhere.append(sFieldCode + " like '"
						+ list.get(i).toString() + "%'");
			}
		}
		if (isDiffer) {
			sSqlWhere.insert(0, sFieldCode + " in (");
			sSqlWhere.append(")");
		}
		return sSqlWhere.toString();
	}

	/**
	 * 得到SQL查询条件
	 * 
	 * @param list
	 * @param sFieldCode
	 * @return
	 * @throws Exception
	 */
	private String getSqlWhere(DataSet ds, MyTreeNode node, boolean isDiffer,
			String priField, String sFieldCode) throws Exception {
		StringBuffer sSqlWhere = new StringBuffer();
		String value;
		if (isDiffer) {
			value = getLeafValue(ds, node, priField);
			sSqlWhere.append(sFieldCode + " in (");
			sSqlWhere.append(value);
			sSqlWhere.append(")");
		} else {
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			value = myPfNode.getValue();
			if ("".equals(sSqlWhere.toString())) {
				sSqlWhere.append(sFieldCode + " like '" + value + "%'");
			} else {
				sSqlWhere.append(" or  " + sFieldCode + " like '" + value
						+ "%'");
			}
		}

		return sSqlWhere.toString();
	}

	/**
	 * 得到SQL查询条件字符串，根据List
	 * 
	 * @param list
	 * @param sDataSourceName
	 *            数据源名称
	 * @return
	 */
	private String getSqlWhereName(List list, String sDataSourceName,
			String sFieldName) {
		String sDataSourceNameTmp = sDataSourceName + ".";
		if (list == null)
			return null;
		String sSqlWhereName = "";
		for (int i = 0; i < list.size(); i++) {
			if ("".equals(sSqlWhereName)) {
				sSqlWhereName = "{" + sDataSourceNameTmp + sFieldName + "} 包含 "
						+ list.get(i).toString();
			} else {
				sSqlWhereName = sSqlWhereName + " 或者  {" + sDataSourceNameTmp
						+ sFieldName + "} 包含 " + list.get(i).toString();
			}
		}
		return sSqlWhereName;
	}

	/**
	 * 得到SQL查询条件字符串
	 * 
	 * @param value
	 *            值
	 * @param sDataSourceName数据源名称
	 * @param sFieldName名称名称
	 * @return
	 */
	private String getSqlWhereName(String value, String sDataSourceName,
			String sFieldName) {
		String sDataSourceNameTmp = sDataSourceName + ".";
		String sSqlWhereName = "";
		if ("".equals(sSqlWhereName)) {
			sSqlWhereName = "{" + sDataSourceNameTmp + sFieldName + "} 包含 "
					+ value;
		} else {
			sSqlWhereName = sSqlWhereName + " 或者  {" + sDataSourceNameTmp
					+ sFieldName + "} 包含 " + value;
		}
		return sSqlWhereName;
	}

	/**
	 * 遍历树节点，得到查询条件
	 * 
	 * @param customTree树
	 * @param listCode编码列表
	 * @param listName名称列表
	 * @param isDiffer主键和层次码是否同一字段，不同true,相同false
	 * @param priField主键字段
	 * @throws Exception
	 */
	private Map getWhere(CustomTree customTree, boolean isDiffer,
			String priField) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// 判断根节点是否选中
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		List listCode = new ArrayList();
		List listName = new ArrayList();

		getSelectDiv(customTree.getDataSet(), myTreeNode, listCode, listName,
				isDiffer, priField);
		Map resultMap = new HashMap();
		resultMap.put(IDefineReport.CODE, listCode);
		resultMap.put(IDefineReport.NAME, listName);
		return resultMap;
	}

	/**
	 * 得到选中节点编码，使用了递归
	 * 
	 * @param myTreeNode树节点
	 * @param lstCode编码列表
	 * @param lstName名称列表
	 * @param isDiffer主键和层次码是否同一字段，不同true,相同false
	 * @param priField主键字段
	 * @throws Exception
	 * @throws Exception
	 */
	private void getSelectDiv(DataSet ds, MyTreeNode myTreeNode, List listCode,
			List lstName, boolean isDiffer, String priField) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				// 主键和层次码是否同一字段
				if (isDiffer) {// 不同
					listCode.add(getLeafValue(ds, myTreeNodeTmp, priField));
				} else {// 相同
					listCode.add(myPfNode.getValue());
				}
				lstName.add(myTreeNodeTmp.getUserObject().toString());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDiv(ds, myTreeNodeTmp, listCode, lstName, isDiffer,
						priField);
			}
		}
	}

	/**
	 * 得到节点值
	 * 
	 * @param ds数据集
	 * @param node节点
	 * @param priField主键字段
	 * @return
	 * @throws Exception
	 */
	private String getLeafValue(DataSet ds, MyTreeNode node, String priField)
			throws Exception {
		String curBookmark = ds.toogleBookmark();
		// 判断是否叶节点
		if (node.isLeaf()) {
			ds.gotoBookmark(node.getBookmark());
			return "'" + ds.fieldByName(priField).getString() + "'";
		}

		StringBuffer sFileter = new StringBuffer();
		MyTreeNode myTreeNodeTmp;
		Enumeration enume = node.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			myTreeNodeTmp = (MyTreeNode) enume.nextElement();
			if (!myTreeNodeTmp.isLeaf())
				continue;
			ds.gotoBookmark(myTreeNodeTmp.getBookmark());
			if (!Common.isNullStr(sFileter.toString())) {
				sFileter.append(",");
			}
			sFileter.append("'" + ds.fieldByName(priField).getString() + "'");
		}

		ds.gotoBookmark(curBookmark);
		return sFileter.toString();
	}

	/**
	 * 自动设置分栏按钮点击事件
	 */
	private class AutoSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (enumInfoMap == null || enumInfoMap.size() == 0) {
				return;
			}

			// 自动分栏开始行
			int startRow = Integer.parseInt(jspRow.getValue().toString()) - 1;
			// 判断选择的行在不在行头区
			if (!CreateGroupReport.isInGroupRow(startRow,
					RowConstants.UIAREA_HEADER, groupReport)) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"所选择的行不在表头区域，请选择结束行！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 取得A数据源的编码节次
			int iLevelA = Integer.parseInt(jspSubField1.getValue().toString());
			// 取得B数据源的编码节次
			int iLevelB = Integer.parseInt(jspSubField2.getValue().toString());
			// 取得A数据源的节点数
			List lstNodsA = getNodes(enumATree, iLevelA);
			// 取得B数据源的节点数
			List lstNodsB = getNodes(enumBTree, iLevelB);
			int iCountA = lstNodsA.size();
			int iCountB = lstNodsB.size();
			// 判断是否设置取数条件
			if (!checkWhereSet(enumInfoMap, iCountA, iCountB))
				return;

			// 判断是否超出128列，如超出128列，提示
			int iCountCol = 0;
			if (iCountB == 0) {
				iCountCol = iCountA;
			} else {
				iCountCol = iCountA * iCountB;
			}
			if (iCountCol > 128) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"按照目前的设置,将产生超过128列的查询信息，将导致查询时间过长，请减少查询内容或分几张进行查询！",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 检查当前单元是否允许设置列条件
			int col = summaryReportPane.getCellSelection().getColumn();
			int row = summaryReportPane.getCellSelection().getRow();

			if (!checkCell(col, row)) {
				return;
			}

			// 得到当前单元格值
			Object value = groupReport.getCellValue(col, row);

			// 得到行头区的开始行
			int rowIndex[] = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_HEADER, groupReport);
			int rowFrom = rowIndex[0];

			// 判断是否选择了包括上级
			int rowFLagA = 0;
			int rowFLagB = 0;
			if ("false".equals(fchkSubField1.getValue().toString())) {
				rowFLagA = 1;
			} else {
				rowFLagA = iLevelA;
			}
			if (iCountB == 0)
				rowFLagB = 0;
			else if ("false".equals(fchkSubField2.getValue().toString())) {
				rowFLagB = 1;
			} else {
				rowFLagB = iLevelB;
			}

			// 判断要不要插入行,用需要的行数-（表开始的行-行头区的开始行+1）
			int rowNeedAdd = rowFLagA + rowFLagB - (startRow - rowFrom + 1);
			// >0说明需要插入行
			for (int i = 0; i < rowNeedAdd; i++) {
				groupReport.addRowBefore(rowFrom);
			}
			if (rowNeedAdd > 0) {
				startRow = startRow + rowNeedAdd;
			} else {
				rowNeedAdd = 0;
			}

			MyTreeNode myTreeNodeA = null;
			MyTreeNode myTreeNodeB = null;

			Map tempMapA = null;
			Map tempMapB = null;

			List lstAddSql = null;

			// 定位原单元格
			summaryReportPane.setCellSelection(new CellSelection(col, row
					+ rowNeedAdd));

			int curCol = 0;
			try {
				// 循环处理数据源A,
				for (int i = 0; i < iCountA; i++) {
					lstAddSql = new ArrayList();
					tempMapA = (Map) enumInfoMap.get(0);
					myTreeNodeA = (MyTreeNode) lstNodsA.get(i);

					// 得到一个节点信息和这个节点数据源的信息，得到这个节点查询条件，存于Map中，追加到List中
					getEnumValue(enumATree.getDataSet(), tempMapA, myTreeNodeA,
							lstAddSql);

					// 判断数据源2有没有设置条件，没有设置条件，保存数据源1条件
					if (iCountB == 0) {
						curCol = col + i;
						// 插入一列
						groupReport.addColumnAfter(curCol);
						// 保存单元格信息
						saveCellValue(curCol, row + rowNeedAdd, value,
								lstAddSql);
						// 刷新显示条件区信息
						refreshFilter(curCol, row + rowNeedAdd);

						// 设置表头信息
						setHeaderValue(curCol, startRow - rowFLagA, startRow,
								myTreeNodeA, "false".equals(fchkSubField1
										.getValue().toString()) ? false : true);

					} else {// 数据源有条件
						// 循环处理数据源B
						for (int j = 0; j < iCountB; j++) {
							myTreeNodeB = (MyTreeNode) lstNodsB.get(j);
							tempMapB = (Map) enumInfoMap.get(1);
							// 得到一个节点信息和这个节点数据源的信息，得到这个节点查询条件，存于Map中，追上到List中
							getEnumValue(enumBTree.getDataSet(), tempMapB,
									myTreeNodeB, lstAddSql);

							curCol = col + i * iCountB + j;
							// 插入一列
							groupReport.addColumnAfter(curCol);
							// 保存单元格信息
							saveCellValue(curCol, row + rowNeedAdd, value,
									lstAddSql);
							// 刷新显示条件区信息
							refreshFilter(curCol, row + rowNeedAdd);
							// 移除数据源B条件，以便循环加入新数源B条件
							lstAddSql.remove(lstAddSql.size() - 1);

							// 设置表头信息
							setHeaderValue(curCol, startRow - rowFLagA
									- rowFLagB, startRow - rowFLagB,
									myTreeNodeA, "false".equals(fchkSubField1
											.getValue().toString()) ? false
											: true);
							// 设置表头信息
							setHeaderValue(curCol, startRow - rowFLagB,
									startRow, myTreeNodeB, "false"
											.equals(fchkSubField1.getValue()
													.toString()) ? false : true);
						}
					}
				}

				// 刷新显示
				reportGuideUI.fpnlDefineReport.designGroupReportPane
						.fireReportDataChanged();

				// 保存本次增加的列数
				lastTimeAddCol = iCountCol;
				new MessageBox(SubFieldSetDialog.this, "设置成功!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox(SubFieldSetDialog.this, "发生错误，错误信息："
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}
		}
	}

	/**
	 * 设置表头信息
	 * 
	 * @param col列
	 * @param row行
	 * @param myTreeNode树节点
	 * @param bUpSet是否向上汇总
	 */
	private void setHeaderValue(int col, int startRow, int endRow,
			MyTreeNode myTreeNode, boolean bUpSet) {
		int iLevel;
		if (bUpSet)
			iLevel = myTreeNode.getLevel();
		else
			iLevel = 1;
		// 显示表头信息
		String showValue = myTreeNode.getUserObject().toString();
		showValue = showValue.substring(showValue.indexOf("]") + 1);

		CellElement cell = groupReport.getCellElement(col, startRow + iLevel);
		if (cell == null) {
			cell = new CellElement(col, startRow + iLevel);
			groupReport.addCellElement(cell);
		}
		cell.setValue(showValue);

		// 设置单元格格式
		Style style = cell.getStyle();
		style = style.deriveHorizontalAlignment(Constants.CENTER);
		cell.setStyle(style);

		if (myTreeNode.isLeaf()) {
			if (endRow != (startRow + iLevel)) {
				// 是末级点，是否上下合并单元格
				CellSelection cellSelection = new CellSelection(col, startRow
						+ iLevel, 1, endRow - (startRow + iLevel) + 1, col,
						startRow + iLevel, 1, endRow - (startRow + iLevel) + 1);
				summaryReportPane.mergeCell(cellSelection);
			}

		} else { // 不是末级点，合并单元格
			// 左右合并单元格

			// // editColumn = i;
			// // editRow = j;
			// // editColumnSpan = k;
			// // editRowSpan = l;
			// // column = i1;
			// // row = j1;
			// // columnSpan = k1;
			// // rowSpan = l1;

			int colNum = getPreNodeNum(col, startRow + iLevel, groupReport);
			if (colNum != 0) {
				colNum = colNum + 1;
				CellSelection cellSelection = new CellSelection(col - colNum
						+ 1, startRow + iLevel, colNum, 1, col - colNum + 1,
						startRow + iLevel, colNum, 1);
				summaryReportPane.mergeCell(cellSelection);
			}
		}

		// 是否向上汇总
		if (bUpSet) {
			// 得到父节点
			MyTreeNode parNode = (MyTreeNode) myTreeNode.getParent();
			if (parNode.isRoot())
				return;

			setHeaderValue(col, startRow, endRow, parNode, bUpSet);
		}
	}

	/**
	 * 得到前面的节点数
	 * 
	 * @param myTreeNode
	 * @return
	 */
	private int getPreNodeNum(int col, int row, GroupReport groupReport) {
		int iCount = 0;
		String value;
		if (groupReport.getCellElement(col, row) == null)
			value = "";
		else
			value = groupReport.getCellElement(col, row).getValue().toString();
		String valueTmp = null;
		for (int i = col - 1; i >= 0; i--) {
			if (groupReport.getCellElement(i, row) == null)
				valueTmp = "";
			else
				valueTmp = groupReport.getCellElement(i, row).getValue()
						.toString();
			if (value.equals(valueTmp)) {
				iCount++;
			} else {
				return iCount;
			}
		}
		return iCount;
	}

	/**
	 * 判断是否设置取数条件
	 * 
	 * @param enumInfoMap
	 * @param iCountA
	 * @param iCountB
	 * @return
	 */

	private boolean checkWhereSet(List enumInfoMap, int iCountA, int iCountB) {
		// 判断是否设置取数条件
		if (enumInfoMap.size() >= 1) {
			if (iCountA == 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"未设置取数条件，请设置！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		if (enumInfoMap.size() == 2) {
			if (iCountB == 0) {
				JOptionPane.showMessageDialog(SubFieldSetDialog.this,
						"未设置取数条件，请设置！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}

	/**
	 * 保存单元格的条件
	 * 
	 * @param col
	 *            列
	 * @param row
	 *            行
	 * @param value
	 *            单元格原值
	 * @param lstAddSql分栏条件列
	 */
	private void saveCellValue(int col, int row, Object value, List lstAddSql) {
		if (lstAddSql == null)
			return;

		// 得到MyCalculateValueImpl对象
		MyCalculateValueImpl myCalculateValueImpl = (MyCalculateValueImpl) ((MyCalculateValueImpl) value)
				.clone();

		IMeasureAttr[] measureAttr = myCalculateValueImpl.getMeasureArray();
		int length = measureAttr.length;
		String sFormula = myCalculateValueImpl.getFormula().getContent();
		String randomUUID;
		for (int i = 0; i < length; i++) {
			randomUUID = DefinePub.getRandomUUID();
			sFormula = sFormula.replaceAll(measureAttr[i].getMeasureID(),
					randomUUID);
			measureAttr[i].setMeasureID(randomUUID);
		}
		myCalculateValueImpl.getFormula().setContent(sFormula);

		// 得到列条件对象
		IStatisticCaliber[] iStatisticCaliber = myCalculateValueImpl
				.getStatisticCaliberArray();
		iStatisticCaliber = delStatisticCaliberArray(iStatisticCaliber);

		Map addSqlMap = null;
		String addSql;
		String addSqlStr;
		// 加入分栏条件
		int iCount = lstAddSql.size();
		for (int i = 0; i < iCount; i++) {
			addSqlMap = (Map) lstAddSql.get(i);
			if (addSqlMap.get(IDefineReport.CODE) == null)
				return;
			addSql = addSqlMap.get(IDefineReport.CODE).toString();
			addSqlStr = addSqlMap.get(IDefineReport.NAME).toString();

			iStatisticCaliber = addStatisticCaliberArrayLegth(
					iStatisticCaliber, 1);
			int index = iStatisticCaliber.length - 1;
			iStatisticCaliber[index] = new MySummaryStatisticCaliberImpl(
					addSqlStr);
			// 设置CaliberID(参数ID)值
			iStatisticCaliber[index].setCaliberID(IDefineReport.PA_
					+ DefinePub.getRandomUUID());
			iStatisticCaliber[index].setSourceID(addSqlMap.get(
					IDefineReport.SOURCE_ID).toString());
			iStatisticCaliber[index].setSourceColID(addSqlMap.get(
					IDefineReport.COL_ID).toString());
			iStatisticCaliber[index].setJoinBefore("and");
			// 设置AddSql值
			iStatisticCaliber[index].setAddSQL(addSql);
			if (iStatisticCaliber[index] instanceof ICustomStatisticCaliber) {
				// if (addSqlMap.get(IDefineReport.ENUM_ID) != null) {
				((ICustomStatisticCaliber) iStatisticCaliber[index])
						.setEnumID(addSqlMap.get(IDefineReport.ENUM_ID)
								.toString());
				// }
				// if (addSqlMap.get(IDefineReport.ENUM_ID) != null) {
				((ICustomStatisticCaliber) iStatisticCaliber[index])
						.setNodeID(addSqlMap.get(IDefineReport.NODE_ID)
								.toString());
				// }
			}
		}

		myCalculateValueImpl.setStatisticCaliberArray(iStatisticCaliber);
		groupReport.getCellElement(col, row).setValue(myCalculateValueImpl);
	}

	/**
	 * 得到树节点List,根据树的层次
	 * 
	 * @param customTree
	 * @param level层次
	 * @return
	 */
	private List getNodes(CustomTree customTree, int level) {
		List listNodes = new ArrayList();
		MyTreeNode root = (MyTreeNode) customTree.getRoot();
		Enumeration enuma = root.depthFirstEnumeration();
		// 遍历树
		while (enuma.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enuma.nextElement();
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			// 判断是不是根节点
			if (node == root) {
				continue;
			}

			// 判断节点的状态是不是全选中
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				if (node.getLevel() > level)
					continue;
				else if (node.getChildCount() == 0)
					listNodes.add(node);
				else if (node.getLevel() == level) {// 判断是不是需要的节次
					listNodes.add(node);
				}
			}
		}
		return listNodes;

	}

	/**
	 * 自动设置分栏按钮点击事件
	 */
	private class CanelSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			int col = summaryReportPane.getCellSelection().getColumn();
			for (int i = lastTimeAddCol - 1; i >= 0; i--) {
				groupReport.removeColumn(col + i);
			}
			// 刷新显示
			reportGuideUI.fpnlDefineReport.designGroupReportPane
					.fireReportDataChanged();
			new MessageBox(SubFieldSetDialog.this, "自取上次设置成功！",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
		}
	}

	// public void stayOnTop() {
	// this.addWindowListener(new WindowListener() {
	//
	// public void windowActivated(WindowEvent e) {
	// // TODO 自动生成方法存根
	//
	// }
	//
	// public void windowClosed(WindowEvent e) {
	// // TODO 自动生成方法存根
	//
	// }
	//
	// public void windowClosing(WindowEvent e) {
	// // TODO 自动生成方法存根
	//
	// }
	//
	// public void windowDeactivated(WindowEvent e) {
	// SubFieldSetDialog.this.toFront();
	//
	// }
	//
	// public void windowDeiconified(WindowEvent e) {
	// // TODO 自动生成方法存根
	// }
	//
	// public void windowIconified(WindowEvent e) {
	// // TODO 自动生成方法存根
	//
	// }
	//
	// public void windowOpened(WindowEvent e) {
	// // TODO 自动生成方法存根
	//
	// }
	//
	// });
	// }

	/**
	 * 得到选中节点ID
	 * 
	 * @param customTree
	 */
	private String getSelectNodeId(CustomTree customTree) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// 判断根节点是否选中
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}

		StringBuffer sSqlNode = new StringBuffer();
		// 得到选中节点ID
		getSelectNodeIdRecursion(customTree.getDataSet(), myTreeNode, sSqlNode);
		return sSqlNode.toString();
	}

	/**
	 * 得到选中节点ID，使用了递归
	 * 
	 * @param ds
	 *            数据集
	 * @param myTreeNode
	 *            树节点
	 * @param sSqlNode
	 *            存选中节点ID
	 * @throws Exception
	 */
	private void getSelectNodeIdRecursion(DataSet ds, MyTreeNode myTreeNode,
			StringBuffer sSqlNode) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				if (!Common.isNullStr(sSqlNode.toString())) {
					sSqlNode.append(",");
				}
				sSqlNode.append(myPfNode.getValue());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectNodeIdRecursion(ds, myTreeNodeTmp, sSqlNode);
			}
		}
	}

	/**
	 * 设置节点为选中状态
	 * 
	 * @param sNodeId
	 * @param customTree
	 */
	public static void setNodeSelect(String sNodeId, CustomTree customTree) {
		if (sNodeId == null)
			return;
		String[] sNodeIdArray = sNodeId.split(",");
		// SetSelectTree.setIsCheck(customTree, sNodeIdArray);
		MyTreeNode node;
		PfTreeNode pNode;
		int len = sNodeIdArray.length;
		for (int i = 0; i < len; i++) {
			node = (MyTreeNode) customTree.getNodeById(sNodeIdArray[i]);
			if (node != null) {
				pNode = (PfTreeNode) node.getUserObject();
				pNode.setIsSelect(true);
			}
		}
	}
}
