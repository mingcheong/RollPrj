/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.object.DefaultDictionaryImpl;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryParameterImpl;
import com.foundercy.pf.reportcy.summary.object.base.ToSource;
import com.foundercy.pf.reportcy.summary.object.enumer.SummaryEnumSourceImpl;
import com.foundercy.pf.reportcy.summary.object.enumer.SummaryEnumSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.SummarySourceCol;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Resource;
import com.foundercy.pf.util.Tools;

/**
 * <p>
 * Title:自定义查询表定义客服端界面
 * </p>
 * <p>
 * Description:自定义查询表定义客服端界面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司

 */
public class ReportGuideUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// 报表ID
	String sReportId = null;

	// 报表主表对象
	RepSetObject repSetObject = null;

	// 定义数据源显示树
	private CustomTree datasChoicTre = null;

	SubFieldSetDialog subFieldSetDialog;

	// 定义报表主属性页面
	ReportInfoSet reportInfoSet = null;

	// 定义报表排序页面
	ReportOrderSet reportOrderSet = null;

	// 定义数据源关系设置
	ReportDSRelaSet reportDSRelaSet = null;

	// 定义报表分组设置页面
	ReportGroupSet reportGroupSet = null;

	// 定义数据库接口
	IDefineReport definReportServ = null;

	// 选中的数据源
	private List lstDataSource = null;

	// 定义数据源选择类
	private DataSourceChoiceUI dataSourceChoice = null;

	// 定义数源设置对话框
	private DataSourceSet dataSourceSet = null;

	ReportQuerySource querySource = null;

	// 定义报表表格面板
	ReportPanel fpnlDefineReport = null;

	// 得到引用列主键编码
	private Map refColPriMap = null;

	// 报表主界面
	DefineReport defineReport;

	/**
	 * 构造函数
	 * 
	 */
	public ReportGuideUI(RepSetObject repSetObject, DefineReport defineReport) {
		this.setSize(900, 650);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		if (repSetObject == null) {
			this.setTitle("新增分组汇总表");
		} else {
			this.setTitle("修改分组汇总表");
		}
		if (repSetObject != null) {
			this.sReportId = repSetObject.getREPORT_ID();
		}

		this.repSetObject = repSetObject;
		this.defineReport = defineReport;

		if (this.repSetObject == null) {
			this.repSetObject = new RepSetObject();
		}

		// 定义数据库接口
		definReportServ = DefineReportI.getMethod();

		// 调用界面初始化方法
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(ReportGuideUI.this, e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
		}

		// 判断是增加报表还是修改报表
		if (sReportId != null && !"".equals(sReportId)) {// 修改
			// 根据文件内容得到数据源信息
			getLstDataSource();
			setDataSourceTreeValue(lstDataSource);
		}

	}

	/**
	 * 界面初始化方法
	 * 
	 * @throws Exception
	 * 
	 */
	private void jbInit() throws Exception {
		datasChoicTre = new CustomDragTree("数据源", null, "chr_id",
				"FIELD_FNAME", "parent_id", null, "chr_id") {

			private static final long serialVersionUID = -556491557372220658L;

			public boolean isDragAble() {
				if (this.getSelectedNode() == null
						|| !this.getSelectedNode().isLeaf())
					return false;
				if (this.getSelectionPath().getLastPathComponent() == this
						.getRoot())
					return false;
				return true;
			}

		};

		FScrollPane datasChoicScroll = new FScrollPane(datasChoicTre);

		// 定义左边报表数据源面板
		FPanel datasChoicePnl = new FPanel();
		datasChoicePnl.setTitle("报表数据源");
		datasChoicePnl.setLayout(new RowPreferedLayout(1));
		// 数据源显示树加入左边报表数据源面板
		datasChoicePnl.addControl(datasChoicScroll);

		// 定义报表表格面板
		fpnlDefineReport = new ReportPanel(this);

		// 定义querySource
		querySource = (ReportQuerySource) ReportConver
				.getReportQuerySource(fpnlDefineReport.groupReport);

		// 定义报表属性多页面面板
		FTabbedPane ftabPnlReportSet = new FTabbedPane();
		// 定义报表主属性页面
		reportInfoSet = new ReportInfoSet(this);
		ftabPnlReportSet.addControl("分组条件设置", reportInfoSet);
		// 定义数据源关系设置
		reportDSRelaSet = new ReportDSRelaSet(this);
		ftabPnlReportSet.addControl("数据源关系设置", reportDSRelaSet);
		// 定义报表分组设置页面
		reportGroupSet = new ReportGroupSet(this);
		ftabPnlReportSet.addControl("分组汇总设置", reportGroupSet);
		// 定义报表排序页面
		reportOrderSet = new ReportOrderSet(this);
		ftabPnlReportSet.addControl("排序设置", reportOrderSet);

		// 定义右边上下分面板
		FSplitPane spnlRight = new FSplitPane();
		spnlRight.setBorder(null);
		spnlRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spnlRight.setDividerLocation(Toolkit.getDefaultToolkit()
				.getScreenSize().height - 300);
		// 报表表格加入面板上部
		spnlRight.addControl(fpnlDefineReport);
		// 报表属性多页面面板加入面板下部
		spnlRight.addControl(ftabPnlReportSet);

		// 预览面板
		// PriviewPanel priviewPanel = new PriviewPanel();

		FTabbedPane ftabPnlRight = new FTabbedPane();
		ftabPnlRight.setBorder(null);
		ftabPnlRight.addTab("设计", Resource.getImage("images/fbudget/set.gif"),
				spnlRight);
		// ftabPnlRight.addTab("预览",
		// Resource.getImage("images/fbudget/prw.gif"),
		// priviewPanel);

		// 定义数据源按钮面板
		ChoiceBtnPanel choiceBtnPanel = new ChoiceBtnPanel();

		// 定义完成、取消按钮类面板
		ButtonPanel btnPanel = new ButtonPanel(this);

		// 定义左右分主面板
		FSplitPane spnlMain = new FSplitPane();
		spnlMain.setBorder(null);
		spnlMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spnlMain.setDividerLocation(300);
		// 报表数据源面板加入左右分主面板左边
		spnlMain.addControl(datasChoicePnl);
		// 右边上下分面板加入左右分主面板右边
		spnlMain.addControl(ftabPnlRight);

		// 定义主面板
		FPanel fpnlMain = new FPanel();
		RowPreferedLayout rLayRight = new RowPreferedLayout(2);
		rLayRight.setRowHeight(35);
		rLayRight.setColumnWidth(300);
		fpnlMain.setLayout(rLayRight);
		// 报表属性多页面面板加入右边面板
		fpnlMain.add(spnlMain, new TableConstraints(1, 2, true, true));
		// 完成、取消按钮类面板加入右边面板
		fpnlMain.add(choiceBtnPanel, new TableConstraints(1, 1, false, false));
		// 完成、取消按钮类面板加入右边面板
		fpnlMain.add(btnPanel, new TableConstraints(1, 1, false, true));

		this.getContentPane().add(fpnlMain);

	}

	/**
	 * 定义数据源设置按钮面板
	 */
	private class ChoiceBtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		// "数据源设置"按钮
		public FButton fbtnDataSourceSet = null;

		// "数据源选择"按钮
		public FButton fbtnDataSourceChoice = null;

		/**
		 * 构造方法
		 * 
		 */
		public ChoiceBtnPanel() {
			// 设置靠右显示
			this.setAlignment(FlowLayout.RIGHT);

			fbtnDataSourceSet = new FButton("fbtnDataSourceSet", "数据源过滤条件");
			fbtnDataSourceSet
					.addActionListener(new DataSourceSetActionListener());
			fbtnDataSourceChoice = new FButton("nextBtn", "数据源选择");
			fbtnDataSourceChoice
					.addActionListener(new DataSourceChoiceActionListener());

			// 数据源设置
			this.addControl(fbtnDataSourceSet, new TableConstraints(1, 1, true,
					false));
			// 数据源选择
			this.addControl(fbtnDataSourceChoice, new TableConstraints(1, 1,
					true, false));

		}
	}

	/**
	 * 根据文件内容得到数据源信息
	 * 
	 */
	private void getLstDataSource() {
		if (lstDataSource == null) {
			lstDataSource = new ArrayList();
		}
		// 得到数据源信息
		if (querySource.getDataSourceManager() == null)
			return;

		IDataSource[] summaryDataSourceImpl = querySource
				.getDataSourceManager().getDataSourceArray();
		Map mapDataSource = null;
		for (int i = 0; i < summaryDataSourceImpl.length; i++) {
			mapDataSource = new HashMap();
			mapDataSource.put(IDefineReport.DICID, summaryDataSourceImpl[i]
					.getSourceID());
			lstDataSource.add(mapDataSource);
		}
	}

	/**
	 * 在数据源树上显示数据源信息
	 * 
	 * @param lstDataSource
	 *            显示的列
	 */
	private void setDataSourceTreeValue(List lstDataSource) {
		if (lstDataSource == null)
			return;
		// 根据数据源列表转换成字符串
		String sDataSource = getDataSource(lstDataSource);

		// 得到选中的数据源数明细信息
		DataSet dsDataSourceDetail = null;
		try {
			if (Common.isNullStr(sDataSource))
				return;
			dsDataSourceDetail = definReportServ
					.getDataSoureDetail(sDataSource);
			// 将得到选中的数据源数明细信息设给定义数据源显示树
			datasChoicTre.setDataSet(dsDataSourceDetail);
			datasChoicTre.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据数据源列表转换成字符串
	 * 
	 * @param lstDataSource
	 *            数据源列表
	 * @return
	 */
	private String getDataSource(List lstDataSource) {
		// 得到选中数据源的表或视图名称
		String sDataSource = "";
		String sTemp = null;
		// 循环取得选中数据源的表或视图名称
		for (int i = 0; i < lstDataSource.size(); i++) {
			// 取得选中数据源的表或视图名称
			sTemp = "'" + ((Map) lstDataSource.get(i)).get(IDefineReport.DICID)
					+ "'";
			// 组织成查询条件
			if ("".equals(sDataSource)) {
				sDataSource = sDataSource + sTemp;
			} else {
				sDataSource = sDataSource + "," + sTemp;
			}
		}
		return sDataSource;

	}

	/**
	 * 数据源选中按钮点击事件
	 */
	private class DataSourceChoiceActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 定义数据源选择类
			if (dataSourceChoice == null) {
				dataSourceChoice = new DataSourceChoiceUI(ReportGuideUI.this,
						definReportServ, lstDataSource);
			}
			// 数据源选择窗体具中显示
			Tools.centerWindow(dataSourceChoice);
			// 显示数据源选择窗体
			dataSourceChoice.setVisible(true);

			// 得到选中的数据源
			List lstTmp = dataSourceChoice.getDataSource();

			// 判断选中的数据源是否发生了改变
			if (lstDataSource != null) {
				if (lstDataSource.equals(lstTmp)) {
					return;
				} else {
					lstDataSource = lstTmp;
				}
			} else { // 将选中的列信息传给lstDataSource
				lstDataSource = lstTmp;
			}
			// 在数据源树上显示数据源信息
			setDataSourceTreeValue(lstDataSource);

			// 保存数据源类信息
			saveSummaryDataSourceManagerImpl(lstDataSource);
			// 保存枚举数据源信息
			saveEnumSourceManager(lstDataSource);
			// 设置默认参数(单位div_code,批次Batch_no,类型Data_type)
			try {
				saveDefaultParameter(lstDataSource);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ReportGuideUI.this,
						"数据源选择发生错误，错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 保存数据源类信息
	 * 
	 * @param lstDataSource
	 *            数据源List
	 */
	private void saveSummaryDataSourceManagerImpl(List lstDataSource) {
		if (lstDataSource == null)
			return;

		if (refColPriMap == null) {
			refColPriMap = getRefColPriCode();
		}

		// 选中的数据数量
		int iCount = lstDataSource.size();
		SummaryDataSourceImpl[] summaryDataSourceImpl = new SummaryDataSourceImpl[iCount];
		Map mapDataSource = null;
		// 数据源名称
		String sDataSourceName = null;
		// 定义数据字典Dictionary
		DefaultDictionaryImpl defaultDictionaryImpl = null;
		String sSourceID = null;
		// 定义数据源Datasource,循环处理选中的数据源
		for (int i = 0; i < iCount; i++) {
			summaryDataSourceImpl[i] = new SummaryDataSourceImpl();
			mapDataSource = (Map) lstDataSource.get(i);
			// 数据源名称
			sDataSourceName = mapDataSource.get(IDefineReport.OBJECT_CNAME)
					.toString();
			summaryDataSourceImpl[i].setDataSourceName(sDataSourceName);
			// 数据源类型
			summaryDataSourceImpl[i]
					.setSourceType(getSourceType(mapDataSource));
			// 数据源ID
			sSourceID = mapDataSource.get(IDefineReport.DICID).toString();
			summaryDataSourceImpl[i].setSourceID(sSourceID);
			// sourceAlais数据源别名
			summaryDataSourceImpl[i].setSourceAlais(ReportUtil
					.translateToColumnName(i));
			// 数据源名称或Sql语句
			summaryDataSourceImpl[i].setSource(getSource(mapDataSource));
			// 根据数据源名称取得数据源的详细字段信息

			// 定义数据字典Dictionary
			defaultDictionaryImpl = saveDataSourceDictionary(sSourceID);
			summaryDataSourceImpl[i].setDictionary(defaultDictionaryImpl);
			// 定义数据源与枚举源列信息
			SummarySourceCol[] summarySourceCol = saveDataSourceColArray(sSourceID);
			summaryDataSourceImpl[i].setColArray(summarySourceCol);
		}
		// 定义数据源数据管理DataSourceManager
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null)
			summaryDataSourceManagerImpl = new SummaryDataSourceManagerImpl();
		summaryDataSourceManagerImpl.setDataSourceArray(summaryDataSourceImpl);
		querySource.setDataSourceManager(summaryDataSourceManagerImpl);
	}

	/**
	 * 数据源类型
	 * 
	 * @param mapDataSource
	 * @return
	 */
	private int getSourceType(Map mapDataSource) {
		String objectEname = mapDataSource.get(IDefineReport.OBJECT_ENAME)
				.toString();
		if (IDefineReport.TAB_FB_B_INFO.equals(objectEname)) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * 数据源source值
	 * 
	 * @param mapDataSource
	 * @return
	 */
	private String getSource(Map mapDataSource) {
		String objectEname = mapDataSource.get(IDefineReport.OBJECT_ENAME)
				.toString();
		if (IDefineReport.TAB_FB_B_INFO.equals(objectEname)) {
			return "select * from " + objectEname + " where report_id = '"
					+ mapDataSource.get(IDefineReport.DICID).toString() + "'";
		} else {
			return objectEname;
		}
	}

	/**
	 * 得到引用列主键编码Map
	 * 
	 * @return
	 */
	private Map getRefColPriCode() {
		Map resultMap = new HashMap();

		List lstRefColPriCode = DefineReportI.getMethod().getRefColPriCode();
		Map tempMap;
		for (int i = 0; i < lstRefColPriCode.size(); i++) {
			tempMap = (Map) lstRefColPriCode.get(i);
			resultMap.put(tempMap.get(ISysRefCol.REFCOL_ID), tempMap
					.get(ISysRefCol.LVL_FIELD));
		}
		return resultMap;
	}

	/**
	 * 定义数据字典Dictionary
	 * 
	 * @param sObjectEName
	 *            数据源EName值
	 * @return 数据源数据字典Dictionary
	 */
	private DefaultDictionaryImpl saveDataSourceDictionary(String sObjectEName) {
		String sCode = null;
		String sName = null;
		Map mapField = null;
		DefaultDictionaryImpl defaultDictionaryImpl = new DefaultDictionaryImpl();
		// 根据数据源EName值取得字段详细信息
		List lstField = definReportServ.getFieldWithEname(sObjectEName);
		for (int i = 0; i < lstField.size(); i++) {
			mapField = (Map) lstField.get(i);
			sCode = mapField.get(IDefineReport.FIELD_ENAME).toString();
			sName = mapField.get(IDefineReport.FIELD_FNAME).toString();
			defaultDictionaryImpl.put(sCode, sName);
		}
		return defaultDictionaryImpl;
	}

	/**
	 * 定义数据源与枚举源列信息
	 * 
	 * @param sObjectEName
	 *            数据源EName值
	 * @return 数据源与枚举源列对象
	 */
	private SummarySourceCol[] saveDataSourceColArray(String sObjectEName) {
		List lstEnum = definReportServ.getEnumWhere(sObjectEName);
		int iCount = lstEnum.size();
		Map mapEnum = null;
		// 定义枚举源列对象
		RefEnumSource refEnumSource = null;
		SummarySourceCol[] summarySourceCol = new SummarySourceCol[iCount];
		String sEnumCol = null;
		for (int i = 0; i < iCount; i++) {
			summarySourceCol[i] = new SummarySourceCol();
			mapEnum = (Map) lstEnum.get(i);
			// 数据源列字段名称
			summarySourceCol[i].setColID(mapEnum.get(IDefineReport.FIELD_ENAME)
					.toString());
			// 数据源列字段类型
			summarySourceCol[i].setColType(DefinePub.getFieldTypeWithCh(mapEnum
					.get(IDefineReport.FIELD_TYPE).toString()));
			// 定义枚举源列对象
			refEnumSource = new RefEnumSource();
			// 枚举源列ID
			if (mapEnum.get(IDataDictBO.REFCOL_ID) != null) {
				refEnumSource.setEnumID(IDefineReport.ENUM_
						+ mapEnum.get(IDataDictBO.REFCOL_ID).toString());
			}
			// 设置数据源与枚举联接关系默认为左联接
			refEnumSource.setJoinType(IDefineReport.LEFT_JOIN);
			// 枚举源列编码
			refEnumSource.setRefEnumColCode(mapEnum.get(
					IDataDictBO.CON_FIELDENAME).toString());
			// 标识是否是不是枚举源主键;0:主键，1:非主键
			if (refColPriMap.get(mapEnum.get(IDataDictBO.REFCOL_ID)) == null) {
				refEnumSource.setRefType(1);
			} else {
				sEnumCol = refColPriMap.get(mapEnum.get(IDataDictBO.REFCOL_ID))
						.toString();
				if (mapEnum.get(IDataDictBO.CON_FIELDENAME).toString()
						.equalsIgnoreCase(sEnumCol)) {
					refEnumSource.setRefType(0);
				} else {
					refEnumSource.setRefType(1);
				}
			}
			summarySourceCol[i].setRefEnumSource(refEnumSource);
		}
		return summarySourceCol;
	}

	/**
	 * 保存枚举数据源信息
	 * 
	 * @param lstDataSource
	 *            数据源List
	 */
	private void saveEnumSourceManager(List lstDataSource) {
		if (lstDataSource == null)
			return;
		Map mapEnumSource = null;
		String sCode = null;
		String sName = null;
		// 根据数据源列表转换成字符串
		String sDataSource = getDataSource(lstDataSource);
		List lstEnumSource = definReportServ.getEnumInfo(sDataSource);
		int iCount = lstEnumSource.size();
		SummaryEnumSourceImpl[] summaryEnumSourceImpl = new SummaryEnumSourceImpl[iCount];
		for (int i = 0; i < lstEnumSource.size(); i++) {
			summaryEnumSourceImpl[i] = new SummaryEnumSourceImpl();
			mapEnumSource = (Map) lstEnumSource.get(i);
			// enumID枚举源ID
			summaryEnumSourceImpl[i].setEnumID(IDefineReport.ENUM_
					+ mapEnumSource.get(ISysRefCol.REFCOL_ID).toString());
			// enumType,1:要素,2:sql
			summaryEnumSourceImpl[i].setEnumType("2");
			// 枚举源类型 sourcetype ,1,表名，2：sql,
			summaryEnumSourceImpl[i].setSourceType("2");
			// 别名enumSourceAlais
			summaryEnumSourceImpl[i].setEnumSourceAlais(IDefineReport.ENUM_
					+ mapEnumSource.get(ISysRefCol.REFCOL_ID).toString());
			// enumName
			summaryEnumSourceImpl[i].setEnumName(mapEnumSource.get(
					ISysRefCol.REFCOL_NAME).toString());
			// levelInfo
			summaryEnumSourceImpl[i].setLevelInfo(setLevel(mapEnumSource.get(
					ISysRefCol.LVL_STYLE).toString()));
			// levelCode
			summaryEnumSourceImpl[i].setLevelCode(mapEnumSource.get(
					ISysRefCol.LVL_FIELD).toString());
			// pk
			summaryEnumSourceImpl[i].setPk(mapEnumSource.get(
					ISysRefCol.PRIMARY_FIELD).toString());
			// Source
			summaryEnumSourceImpl[i].setSource(setSql(mapEnumSource.get(
					ISysRefCol.SQL_DET).toString()));
			// Dictionary
			DefaultDictionaryImpl defaultDictionaryImpl = new DefaultDictionaryImpl();
			sCode = mapEnumSource.get(ISysRefCol.CODE_FIELD).toString();
			sName = mapEnumSource.get(ISysRefCol.REFCOL_NAME).toString() + "编码";
			defaultDictionaryImpl.put(sCode, sName);
			sCode = mapEnumSource.get(ISysRefCol.NAME_FIELD).toString();
			sName = mapEnumSource.get(ISysRefCol.REFCOL_NAME).toString() + "名称";
			defaultDictionaryImpl.put(sCode, sName);
			summaryEnumSourceImpl[i].setDictionary(defaultDictionaryImpl);
		}

		SummaryEnumSourceManagerImpl SummaryEnumSourceManagerImpl = new SummaryEnumSourceManagerImpl();
		SummaryEnumSourceManagerImpl.setEnumSourceArray(summaryEnumSourceImpl);
		querySource.setEnumSourceManager(SummaryEnumSourceManagerImpl);
	}

	/**
	 * 去掉引用列，年份和排序定义信息
	 * 
	 * @param sSqlDet
	 * @return
	 */
	private String setSql(String sSqlDet) {
		sSqlDet = sSqlDet.toUpperCase();
		int index = sSqlDet.indexOf("ORDER");
		if (index != -1) {
			sSqlDet = sSqlDet.substring(0, index);
		}
		String sSetYear = "#SET_YEAR#";
		index = sSqlDet.indexOf(sSetYear);
		int startIndex = sSqlDet.lastIndexOf("SET_YEAR", index);
		if (index != -1 && startIndex != -1) {
			String value = sSqlDet.substring(startIndex, index
					+ sSetYear.length());
			sSqlDet = sSqlDet.replaceAll(value, "1=1");
		}

		return sSqlDet;
	}

	/**
	 * 设置默认参数(单位div_code,批次Batch_no,类型Data_type)
	 * 
	 * @param lstDataSource
	 * @throws Exception
	 */
	private void saveDefaultParameter(List lstDataSource) throws Exception {
		if (lstDataSource == null)
			return;
		// 保存默认参数(单位div_code)
		SummaryParameterImpl[] divCodeSummaryParameterImpl = saveDivCodeParameter(lstDataSource);
		// 保存默认参数(批次Batch_no,类型Data_type)
		SummaryParameterImpl[] batchNoSummaryParameterImpl = saveBatchNoParameter(lstDataSource);
		// 版本号参数
		SummaryParameterImpl verNoSummaryParameterImpl = saveVerNoParameter(lstDataSource);
		// 单位参数
		SummaryParameterImpl divNameSummaryParameterImpl = saveDivNameParameter();

		// 定义参数
		SummaryParameterImpl[] summaryParameterImpl = divCodeSummaryParameterImpl;

		// 批次batch_no参数,类型data_type参数
		if (batchNoSummaryParameterImpl != null) {
			int iCount = batchNoSummaryParameterImpl.length;

			int iStart = 0;
			// 判断是否已定义
			if (summaryParameterImpl == null) {// 未定义
				summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
			} else {// 已定义，增加数组长度
				// 保存原数组长度
				iStart = summaryParameterImpl.length;
				// 增加数据长度
				summaryParameterImpl = addArrayLength(summaryParameterImpl,
						iCount);
			}
			// 循环,将批次和类型参数对象加入总的参数数组
			for (int i = 0; i < iCount; i++) {
				summaryParameterImpl[iStart + i] = batchNoSummaryParameterImpl[i];
			}
		}

		// 保存版本参数
		int iStart = 0;
		if (verNoSummaryParameterImpl != null) {
			// 判断是否已定义
			if (summaryParameterImpl == null) {// 未定义
				summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
			} else {// 已定义，增加数组长度
				// 保存原数组长度
				iStart = summaryParameterImpl.length;
				// 增加数据长度
				summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
			}
			summaryParameterImpl[iStart] = verNoSummaryParameterImpl;
		}

		// 保存标题区单位名称参数
		iStart = 0;
		// 判断是否已定义
		if (summaryParameterImpl == null) {// 未定义
			summaryParameterImpl = new SummaryParameterImpl[batchNoSummaryParameterImpl.length];
		} else {// 已定义，增加数组长度
			// 保存原数组长度
			iStart = summaryParameterImpl.length;
			// 增加数据长度
			summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
		}
		summaryParameterImpl[iStart] = divNameSummaryParameterImpl;

		IParameter[] parameter = querySource.getParameterArray();
		if (parameter != null)
			for (int i = 0; i < parameter.length; i++) {
				if (DataSourceSet.checkDefaultPara(parameter[i].getChName())) {
					continue;
				}
				iStart = summaryParameterImpl.length;
				summaryParameterImpl = addArrayLength(summaryParameterImpl, 1);
				summaryParameterImpl[iStart] = (SummaryParameterImpl) parameter[i];
			}

		// 默认参数值对象设给报表对象
		querySource.setParameterArray(summaryParameterImpl);
	}

	/**
	 * 增加数据长度
	 * 
	 * @param summaryParameterImpl
	 *            原数组
	 * @param iNum
	 *            增加的长度
	 * @return
	 */
	public static SummaryParameterImpl[] addArrayLength(
			SummaryParameterImpl[] summaryParameterImpl, int iNum) {
		if (iNum <= 0)
			return summaryParameterImpl;
		SummaryParameterImpl[] tempSummaryParameterImpl = null;
		if (summaryParameterImpl == null) {
			tempSummaryParameterImpl = new SummaryParameterImpl[1];
		} else {
			int iOldCount = summaryParameterImpl.length;
			int iCount = iOldCount + iNum;
			tempSummaryParameterImpl = new SummaryParameterImpl[iCount];
			for (int i = 0; i < iOldCount; i++) {
				tempSummaryParameterImpl[i] = summaryParameterImpl[i];
			}
		}
		return tempSummaryParameterImpl;
	}

	/**
	 * 保存默认参数(单位div_code)
	 * 
	 * @param lstDataSource
	 *            数据源List
	 * @return 单位参数对象
	 */

	private SummaryParameterImpl[] saveDivCodeParameter(List lstDataSource) {
		if (lstDataSource == null)
			return null;

		// 数据源记录信息
		Map mapDataSource = null;
		// 数据源数量
		int iCount = lstDataSource.size();

		SummaryParameterImpl[] summaryParameterImpl = new SummaryParameterImpl[iCount];
		// 定义作用的数据源数组
		ToSource toSource[] = null;
		// 循环处理选中的数据源
		for (int i = 0; i < iCount; i++) {
			summaryParameterImpl[i] = new SummaryParameterImpl();
			// 定义作用的数据源数组
			toSource = new ToSource[1];
			toSource[0] = new ToSource();
			mapDataSource = (Map) lstDataSource.get(i);
			// 数据源ID
			toSource[0].setSourceID(mapDataSource.get(IDefineReport.DICID)
					.toString());
			// 列id
			toSource[0].setSourceColID(IDefineReport.DIV_CODE);
			// 是否作用于枚举源,设为否（不作用于枚举源)
			toSource[0].setToEnumSource(false);
			// 参数ID
			summaryParameterImpl[i].setName(DefinePub.getRandomUUID());
			// 参数名称
			summaryParameterImpl[i].setChName(IDefineReport.DIV_CODE);
			// 参数作用数据源
			summaryParameterImpl[i].setToSourceArray(toSource);
		}

		return summaryParameterImpl;
	}

	/**
	 * 保存默认参数(批次Batch_no,类型Data_type)
	 * 
	 * @param lstDataSource数据源List
	 * @return 参数数组对象(0:批次Batch_no,1:类型Data_type)
	 */
	private SummaryParameterImpl[] saveBatchNoParameter(List lstDataSource) {
		if (lstDataSource == null)
			return null;
		// 循环次数
		int iCount = 0;
		// 数据源记录信息
		Map mapDataSource = null;
		// 保存需要批次查询条件的数据源
		List lstIsBatchNoDataSource = null;
		// 批次标记
		Object tempObject = null;
		String sIsBatchNo = null;
		// 数据源数量
		iCount = lstDataSource.size();
		// 循环处理选中的数据源,是否需加批次查询条件
		for (int i = 0; i < iCount; i++) {
			mapDataSource = (Map) lstDataSource.get(i);
			// 得到批次信息
			tempObject = mapDataSource.get(IDefineReport.IS_BATCHNO);
			if (tempObject != null) {// 判断数据源是否要加批次查询条件
				sIsBatchNo = tempObject.toString();
			} else {// isBatch_no为null,默认为不需要批次查询条件
				continue;
			}
			// 批次Batch_no,类型Data_type设置
			if (IDefineReport.TRUE_FLAG.equals(sIsBatchNo)) {
				if (lstIsBatchNoDataSource == null) {
					lstIsBatchNoDataSource = new ArrayList();
				}
				lstIsBatchNoDataSource.add(mapDataSource);
			}
		}
		// 如选中数据源都不需加批次查询条件，返回null
		if (lstIsBatchNoDataSource == null)
			return null;

		// 得到需批次条件的数据源数量
		int iBatchNoNum = lstIsBatchNoDataSource.size();

		// 0:批次Batch_no,1:类型Data_type
		SummaryParameterImpl summaryParameterImpl[] = new SummaryParameterImpl[2];
		// 定义作用数据源数组
		ToSource[] toSource = null;
		iCount = summaryParameterImpl.length;
		for (int i = 0; i < iCount; i++) {
			summaryParameterImpl[i] = new SummaryParameterImpl();
			// id
			summaryParameterImpl[i].setName(DefinePub.getRandomUUID());
			// compareType,比较类型，设为"="
			summaryParameterImpl[i].setCompareType(IDefineReport.EQUAL_FLAG);
			// 定义作用的数据源数组
			toSource = new ToSource[iBatchNoNum];
			// 循环处理需批次条件的数据源
			for (int j = 0; j < iBatchNoNum; j++) {
				mapDataSource = (Map) lstIsBatchNoDataSource.get(j);
				toSource[j] = new ToSource();
				// 数据源ID
				toSource[j].setSourceID(mapDataSource.get(IDefineReport.DICID)
						.toString());
				// 是否作用于枚举源,设为否（不作用于枚举源)
				toSource[j].setToEnumSource(false);
				if (i == 0) {// 0:批次Batch_no
					toSource[j].setSourceColID(IDefineReport.BATCH_NO);
				} else if (i == 1) {// 1:类型Data_type
					toSource[j].setSourceColID(IDefineReport.DATA_TYPE);
				}
			}

			summaryParameterImpl[i].setToSourceArray(toSource);
		}
		// 批次batch_no,参数名称
		summaryParameterImpl[0].setChName(IDefineReport.BATCH_NO);
		// 类型data_type,参数名称
		summaryParameterImpl[1].setChName(IDefineReport.DATA_TYPE);

		return summaryParameterImpl;
	}

	/**
	 * 保存默认参数(批次Batch_no,类型Data_type)
	 * 
	 * @param lstDataSource数据源List
	 * @return 参数数组对象(0:批次Batch_no,1:类型Data_type)
	 * @throws Exception
	 */
	private SummaryParameterImpl saveVerNoParameter(List lstDataSource)
			throws Exception {
		if (lstDataSource == null)
			return null;
		// 数据源数量
		int iCount = lstDataSource.size();

		List lstVerNo = new ArrayList();

		String isVerNo = null;
		// 循环处理需批次条件的数据源
		for (int i = 0; i < iCount; i++) {
			isVerNo = ((Map) lstDataSource.get(i)).get(IDefineReport.SUP_VER)
					.toString();
			// 判断是否需要版本号条件
			if (Common.estimate(isVerNo)) {
				lstVerNo.add(lstDataSource.get(i));
			}
		}

		ToSource[] toSource = null;
		iCount = lstVerNo.size();
		if (iCount > 0) {
			toSource = new ToSource[iCount];
		} else {
			return null;
		}

		Map mapDataSource;
		for (int i = 0; i < iCount; i++) {
			mapDataSource = (Map) lstVerNo.get(i);
			toSource[i] = new ToSource();
			// 数据源ID
			toSource[i].setSourceID(mapDataSource.get(IDefineReport.DICID)
					.toString());
			toSource[i].setToEnumSource(false);
			toSource[i].setSourceColID(IDefineReport.VER_NO);
		}
		SummaryParameterImpl summaryParameterImpl = new SummaryParameterImpl();
		summaryParameterImpl.setToSourceArray(toSource);

		// id
		summaryParameterImpl.setName(DefinePub.getRandomUUID());
		// compareType,比较类型，设为"="
		summaryParameterImpl.setCompareType(IDefineReport.EQUAL_FLAG);
		summaryParameterImpl.setChName(IDefineReport.VER_NO);

		return summaryParameterImpl;
	}

	/**
	 * 保存单位名称参数，默认显示在标题区最后一行，最左边一个单元格
	 * 
	 */
	private SummaryParameterImpl saveDivNameParameter() {
		SummaryParameterImpl summaryParameterImpl = new SummaryParameterImpl();
		summaryParameterImpl.setName(IDefineReport.DIVNAME_PARA);
		summaryParameterImpl.setChName(IDefineReport.DIVNAME_PARA);
		summaryParameterImpl.setValue("业务处室、主管局、单位名称");
		return summaryParameterImpl;
	}

	/**
	 * 设置层次码格式
	 * 
	 * @param sLvlCode
	 * @return
	 */
	private String setLevel(String sLvlCode) {
		Pattern codeSplitter = Pattern.compile("\\s*\\|\\s*");
		String[] ruleArray = codeSplitter.split(sLvlCode);
		if (ruleArray.length == 0) {
			return "";
		}
		String sLvlCodeValue = ruleArray[0];
		int iLvlCodeValue = Integer.parseInt(ruleArray[0]);
		for (int i = 1; i < ruleArray.length; i++) {
			sLvlCodeValue = sLvlCodeValue
					+ "-"
					+ String.valueOf(Integer.parseInt(ruleArray[i])
							- iLvlCodeValue);
			iLvlCodeValue = Integer.parseInt(ruleArray[i]);
		}
		return sLvlCodeValue;
	}

	/**
	 * 数据源设置按钮点击事件
	 */
	private class DataSourceSetActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 定义数据源设置类
			dataSourceSet = new DataSourceSet(ReportGuideUI.this);
			// 数据源设置窗体具中显示
			Tools.centerWindow(dataSourceSet);
			// 显示数据源设置窗体
			try {
				dataSourceSet.setVisible(true);
			} finally {
				dataSourceSet.dispose();
			}

		}
	}

	public DataSet getDataSource() {
		return datasChoicTre.getDataSet();
	}

	public SummaryReportPane getReport() {
		return this.fpnlDefineReport.designGroupReportPane;
	}

}
