/**
 * Copyright浙江易桥
 * 

 * 
 * @title 定制查询报表界面
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.sys.besqryreport.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.report.HeaderUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.WorkSheet;
import com.fr.report.io.ExcelImporter;

public class BesUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private FTabbedPane tabPane;

	// 基本属性列表的元素定义
	private ReportUI reportUIB;

	private Report reportB;

	private FTextField tfTitle;

	private JFileChooser fileChooser;

	private FTextField tfFilePath;

	private FTextField tfFileName;

	private FTextField tfSource;

	private FTextField tfMid1;

	private FTextField tfMid2;

	// 报表分类
	private ReportTypeList reportTypeList;

	private FComboBox cbMoneyType;

	private FRadioGroup rgUserType;

	private BesAct BAct;

	// private IBesQryReport itserv;

	private DataSet dsSource; // 主表的 dataset

	private DataSet dsSql; // sqllines的dataset

	private String sReportID;

	private String sTypeNO;

	private DataSet dsReportType; // 报表类别

	private String[] bmk;

	// 列属性元素定义
	private ReportUI reportUIC;

	private Report reportC;

	private TableHeader TTableHeaderC; // 第二个界面的reportUI的表头

	private Node nodeC; // 第二个界面reportUI的node

	private DataSet dsTitle; // 第二个界面的表头的 dataset

	private CustomTree table; // 主表信息的tree

	private DataSet dsInfo; // col的tree的dataset

	private FTextField tfColName; // 列的中文名编辑框

	private CustomComboBox tfColFormat; // 格式化

	private FCheckBox ckHide; // 是否 隐藏

	private CustomComboBox cbDataType; // 数据类型

	private DataSet dsFormat; // 显示格式的dataset

	// 数据状态类型
	private boolean isChange = true;

	private int iState = 0;

	private final static int state_Browse = 0;

	private final static int state_Modify = 1;

	// 界面元素
	private FPanel pnlBAttr;

	private FPanel pnlCAttr;

	// 数据定义
	private IBesQryReport itserv; // 接口

	private DefineReport defineReport;

	public BesUI(String sReportID, DefineReport defineReport) {
		this.sReportID = sReportID;
		this.defineReport = defineReport;
		// 初始化界面
		initize();
	}

	/**
	 * 初始化界面
	 */
	private void initize() {
		try {
			this.setSize(900, 650);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setTitle("特殊报表设计");

			BAct = new BesAct();
			itserv = BesStub.getMethod();
			tabPane = new FTabbedPane();
			tabPane.add(getBPanel(), "报表基本属性设置");
			tabPane.add(getCPanel(), "报表列属性设置");

			// this.createToolBar();
			tabPane.addChangeListener(new SelNextPaneListener());
			// getReportID();
			FPanelBtnButtom fpnlButton = new FPanelBtnButtom();

			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new RowPreferedLayout(1));
			fpnlMain
					.addControl(tabPane, new TableConstraints(1, 1, true, true));
			fpnlMain.addControl(fpnlButton, new TableConstraints(2, 1, false,
					true));
			this.getContentPane().add(fpnlMain);
			addUserTypeSelListener();
			iState = state_Browse;
			setButtonState(state_Browse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取基本属性面版
	private FPanel getBPanel() throws Exception {
		if (pnlBAttr == null) {
			FPanel pnlBlank = new FPanel();
			pnlBAttr = new FPanel();
			dsSource = DataSet.create();
			dsSql = DataSet.create();
			dsReportType = BAct.getReportTypeData();
			dsReportType.beforeFirst();
			dsReportType.next();
			FPanel pnlTop = new FPanel();
			// pnlTop.setTitle( "top" );
			RowPreferedLayout layTop = new RowPreferedLayout(1);
			pnlTop.setLayout(layTop);
			reportB = new Report();
			reportUIB = new ReportUI(reportB);
			pnlTop.add(reportUIB, new TableConstraints(13, 1, true, true));
			reportUIB.setBorder(new LineBorder(Color.BLACK));
			/*
			 * 下半部分布局
			 */
			FPanel pnlUnder = new FPanel();
			// pnlUnder.setTitle( "under" );
			RowPreferedLayout layUnder = new RowPreferedLayout(10);
			layUnder.setColumnGap(1);
			layUnder.setColumnWidth(100);
			pnlUnder.setLayout(layUnder);
			FLabel lbFilePath = new FLabel();
			lbFilePath.setText("   当前文件:");
			FButton btnOpen = new FButton("btn01", "打开");
			tfFilePath = new FTextField("");
			tfFilePath.setProportion(0.01f);
			tfFilePath.setEditable(false);

			// 标详细信息
			FPanel pnlContent = new FPanel();
			pnlContent.setTitle("表基本信息");
			RowPreferedLayout layContent = new RowPreferedLayout(10);
			layContent.setColumnGap(1);
			layContent.setColumnWidth(90);
			layContent.setRowGap(15);
			pnlContent.setLayout(layContent);
			tfFileName = new FTextField("  报表名称:");
			tfFileName.setProportion(0.1f);

			// 报表分类信息
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("报表类型：");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			cbMoneyType = new FComboBox("  货币类型:");
			cbMoneyType.setRefModel("1#元+2#万元");
			cbMoneyType.setProportion(0.33f);
			rgUserType = new FRadioGroup("   用户类型:", 0);
			rgUserType.setRefModel("0#单位使用+1#财政使用+2#单位财政共同使用");
			rgUserType.setProportion(0.2f);
			rgUserType.setValue("2");

			FLabel lbDataSource = new FLabel();
			lbDataSource.setText("  数据来源");
			FButton btnSource = new FButton("btn02", "选择");
			tfSource = new FTextField();
			tfSource.setProportion(0.001f);
			tfSource.setEditable(false);

			FLabel lbDataMid1 = new FLabel();
			lbDataMid1.setText("  中间视图");
			FButton btnMid1 = new FButton("btn03", "选择");
			tfMid1 = new FTextField();
			tfMid1.setProportion(0.001f);
			tfMid1.setEditable(false);

			FLabel lbDataMid2 = new FLabel();
			lbDataMid2.setText("  中间视图");
			FButton btnMid2 = new FButton("btn04", "选择");
			tfMid2 = new FTextField();
			tfMid2.setProportion(0.001f);
			tfMid2.setEditable(false);

			pnlContent.addControl(tfFileName, new TableConstraints(1, 10,
					false, false));
			pnlContent.addControl(cbMoneyType, new TableConstraints(1, 3,
					false, false));
			pnlContent.addControl(rgUserType, new TableConstraints(1, 7, false,
					false));

			pnlContent.addControl(lbDataSource, new TableConstraints(1, 1,
					false, false));
			pnlContent.addControl(btnSource, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfSource, new TableConstraints(1, 4, false,
					false));
			// 报表分类
			pnlContent.addControl(fpnlReportType, new TableConstraints(4, 4,
					false, false));

			pnlContent.addControl(lbDataMid1, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(btnMid1, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfMid1, new TableConstraints(1, 4, false,
					false));

			pnlContent.addControl(lbDataMid2, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(btnMid2, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfMid2, new TableConstraints(1, 4, false,
					false));

			pnlUnder.addControl(lbFilePath, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(btnOpen, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(tfFilePath, new TableConstraints(1, 7, false,
					false));

			FButton btnGetHeader = new FButton("btnGet", "获取表头");
			FButton btnGetTitle = new FButton("btnGetTitle", "获取标题");
			tfTitle = new FTextField("   标题信息");
			tfTitle.setProportion(0.11f);
			tfTitle.setEditable(false);

			pnlUnder.addControl(btnGetTitle, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(tfTitle, new TableConstraints(1, 9, false,
					false));
			pnlUnder.addControl(btnGetHeader, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(pnlContent, new TableConstraints(12, 10, true,
					true));
			/*
			 * 总体布局
			 */
			RowPreferedLayout layBase = new RowPreferedLayout(3);
			pnlBAttr.setLayout(layBase);
			layBase.setRowGap(5);
			FLabel lbTitle = new FLabel();
			lbTitle.setText("          原   始   报   表");
			lbTitle.setFontSize(20);
			lbTitle.setFont(new Font("宋体", 15, 15));
			pnlBAttr.addControl(pnlBlank, new TableConstraints(1, 1, false,
					true));
			pnlBAttr.addControl(lbTitle,
					new TableConstraints(1, 1, false, true));
			pnlBAttr.addControl(pnlBlank, new TableConstraints(1, 1, false,
					true));
			pnlBAttr.addControl(pnlTop,
					new TableConstraints(13, 3, false, true));
			pnlBAttr.addControl(pnlUnder, new TableConstraints(13, 3, true,
					true));
			this.getContentPane().add(pnlBAttr);
			// this.createToolBar();
			btnOpen.addMouseListener(new ImportRep());

			// /////////////////////////////////////////////////////////按钮监听事件/////////////////////////////////////////////////////////////////////////////

			// 源表信息导入的监听事件
			btnSource.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String sTableName = (tfSource.getValue() == null) ? ""
							: tfSource.getValue().toString();
					SelectSource ss = new SelectSource(BesUI.this, sTableName);
					Tools.centerWindow(ss);
					ss.setVisible(true);
					String sTName = (ss.getValue() == null) ? "" : ss
							.getValue().toString();
					tfSource.setValue(sTName);
					isChange = true;
					try {
						setSourceTitle(tfTitle.getValue());
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			});
			// 中间表1导入的监听事件
			btnMid1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SelectSource ss = new SelectSource(BesUI.this, (tfMid1
							.getValue() == null) ? "" : tfMid1.getValue()
							.toString());
					Tools.centerWindow(ss);
					ss.setVisible(true);
					tfMid1.setValue(ss.getValue());
				}
			});
			// 中间表二导入的监听事件
			btnMid2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SelectSource ss = new SelectSource(BesUI.this, (tfMid2
							.getValue() == null) ? "" : tfMid2.getValue()
							.toString());
					Tools.centerWindow(ss);
					ss.setVisible(true);
					tfMid2.setValue(ss.getValue());
				}
			});
			btnGetHeader.addMouseListener(new SetTableHeader());
			btnGetTitle.addMouseListener(new SetTitleListener());

		}
		return pnlBAttr;
	}

	// 获取列属性面版
	private FPanel getCPanel() throws Exception {
		if (pnlCAttr == null) {
			pnlCAttr = new FPanel();
			BAct = new BesAct();
			reportC = new Report();
			reportUIC = new ReportUI(reportC);
			reportUIC.setEditable(false);

			// 总体布局
			FPanel pnlBlank = new FPanel();
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlCAttr.setLayout(layBase);
			reportUIC.setBorder(new LineBorder(Color.BLACK));

			// 上面面版布局
			FPanel pnlTop = new FPanel();
			RowPreferedLayout layTop = new RowPreferedLayout(8);
			pnlTop.setLayout(layTop);
			pnlTop.add(reportUIC, new TableConstraints(10, 8, false, false));

			// 下面面版布局
			FPanel pnlUnder = new FPanel();
			RowPreferedLayout layUnder = new RowPreferedLayout(8);
			pnlUnder.setLayout(layUnder);

			FPanel pnlContent = new FPanel();
			pnlContent.setTitle("表列属性");
			RowPreferedLayout layContent = new RowPreferedLayout(4);
			layContent.setRowGap(20);
			pnlContent.setLayout(layContent);
			FPanel pnlInfo = new FPanel();
			RowPreferedLayout layInfo = new RowPreferedLayout(4);
			pnlInfo.setLayout(layInfo);
			layInfo.setColumnWidth(155);
			pnlInfo.setTitle("选择对应的列");

			// 表列属性的面版
			cbDataType = new CustomComboBox(BAct.getDataTypeData(),
					IBesQryReport.DataType_ID, IBesQryReport.DataType_Name);
			cbDataType.setTitle("  列属性");
			cbDataType.setProportion(0.2f);
			ckHide = new FCheckBox("  该列是否隐藏");
			ckHide.setProportion(0.7f);
			tfColName = new FTextField("  列名称: ");
			tfColName.setProportion(0.13f);
			tfColName.setEditable(false);
			String sFilter = "typeid = 'DQRDISPLAY' and "
					+ IBesQryReport.SET_YEAR + "=" + Global.loginYear;
			dsFormat = itserv.getDataSet(IBesQryReport.PUBCODETABLE, sFilter);
			tfColFormat = new CustomComboBox(dsFormat, "NAME", "NAME");
			tfColFormat.setTitle("显示格式");
			tfColFormat.setProportion(0.2f);

			// 按钮面版
			FPanel pnlButton = new FPanel();
			RowPreferedLayout layButton = new RowPreferedLayout(4);
			layButton.setColumnWidth(100);
			pnlButton.setLayout(layButton);
			FButton btnSet = new FButton("", "设置");
			FButton btnCancelSet = new FButton("", "取消设置");
			pnlButton.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));
			pnlButton.addControl(btnSet, new TableConstraints(1, 1, false,
					false));
			pnlButton.addControl(btnCancelSet, new TableConstraints(1, 1,
					false, false));
			pnlButton.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));

			// 列信息
			pnlContent.addControl(cbDataType, new TableConstraints(1, 2, false,
					false));
			pnlContent.addControl(ckHide, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfColName, new TableConstraints(1, 3, false,
					false));
			pnlContent.addControl(tfColFormat, new TableConstraints(1, 2,
					false, false));
			pnlContent.addControl(pnlButton, new TableConstraints(1, 4, false,
					false));

			// 选择对应的列的面版
			// 一个customtable,第一个源表
			dsInfo = DataSet.create();
			table = new CustomTree("列信息", dsInfo, "COLUMN_ID", "COLUMN_NAME",
					null, (SysCodeRule)null);
			FScrollPane spTable = new FScrollPane();
			spTable.addControl(table);// , new
			// TableConstraints(4,4,true,true));
			pnlInfo.addControl(spTable, new TableConstraints(13, 3, false,
					false));
			pnlUnder.addControl(pnlContent, new TableConstraints(14, 4, false,
					false));
			pnlUnder.addControl(pnlInfo, new TableConstraints(14, 3, false,
					false));

			// 标题面版
			FPanel pnlTitle = new FPanel();
			RowPreferedLayout layTitle = new RowPreferedLayout(9);
			pnlTitle.setLayout(layTitle);
			FLabel lbTitle = new FLabel();
			lbTitle.setText("  表  列  属  性  设  置");
			lbTitle.setFontSize(20);
			lbTitle.setFont(new Font("宋体", 15, 15));
			pnlTitle.addControl(pnlBlank, new TableConstraints(1, 3, false,
					false));
			pnlTitle.addControl(lbTitle, new TableConstraints(1, 3, false,
					false));
			pnlTitle.addControl(pnlBlank, new TableConstraints(1, 3, false,
					false));

			pnlCAttr.addControl(pnlTitle, new TableConstraints(1, 1, false,
					true));
			pnlCAttr.addControl(pnlTop,
					new TableConstraints(11, 1, false, true));
			// pnlCAttr.addControl( pnlButton, new TableConstraints(
			// 11,1,false,true));
			pnlCAttr.addControl(pnlUnder, new TableConstraints(15, 1, false,
					true));
			pnlCAttr.setTopInset(10);
			reportUIC.getGrid().addMouseListener(new ReportClickListener());
			ckHide.addValueChangeListener(new HideChangeListener());
			cbDataType.addValueChangeListener(new ColTypeChangeLisentener());
			tfColFormat.addValueChangeListener(new FormatChangeLisentener());
			btnSet.addActionListener(new SetListener());
		}
		return pnlCAttr;
	}

	// /////////////////////////////////////////////////////////导入操作/////////////////////////////////////////////////////////////////////////////////

	/**
	 * 创建文件选择器
	 * 
	 */
	private void CreateChooser() {
		// 创建文件选择器
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls".toLowerCase())
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "所有文件(*.xls)";
			}
		});
	}

	/**
	 * 导入报表按钮的操作
	 */
	public class ImportRep extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				CreateChooser();
				int returnval = fileChooser.showOpenDialog(BesUI.this);
				File file = fileChooser.getSelectedFile();
				if (returnval == JFileChooser.APPROVE_OPTION) {
					ExcelImporter ei = new ExcelImporter(file);
					reportUIB = BAct.initReportUI(ei, reportUIB);
					reportUIB.repaint();
					tfFilePath.setValue(file.getPath());
					iState = state_Modify;
					sReportID = getReportID();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 选取表头
	 */
	private class SetTableHeader extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				if (reportUIB == null)
					return;
				CellSelection cs = reportUIB.getCellSelection();
				if (cs == null)
					return;
				nodeC = ReportUtil.parseDocument(reportUIB.getWorkSheet(), cs);
				TTableHeaderC = new TableHeader(nodeC);
				TTableHeaderC.setFont(new Font("宋体", Font.PLAIN, 12));
				TTableHeaderC.setColor(new Color(250, 228, 184));
				// 初始化第二个界面reportUI
				BAct.createReport(TTableHeaderC, reportUIC, reportC);
				dsTitle = DataSet.create();
				dsSource.beforeFirst();
				dsSource.next();
				BAct.FillIntoDataSet(dsTitle, nodeC, reportUIC,
						BesAct.codeRule_Col, sReportID);
				TableHeader tableHeader = HeaderUtility.createHeader(dsTitle,
						IBesQryReport.FIELD_CODE, IBesQryReport.FIELD_CNAME,
						BesAct.codeRule_Col, null);
				BAct.createReport(tableHeader, reportUIC, reportC);
				List listFields = TTableHeaderC.getFields();
				DataSet dsBody = DataSet.create();
				initBodyData(dsBody, listFields, "");
				initBodyData(dsBody, listFields, "不隐藏");
				reportC.setBodyData(dsBody);
				reportC.refreshBody();
				reportUIC.repaint();
				table.addMouseListener(new DataSoureChangeListener(dsInfo,
						IBesQryReport.FIELD_ENAME, table, tfColName));
				for (int i = 0; i < TTableHeaderC.getColumns(); i++) {
					reportC.setColumnWidth(i, 100);
				}
				isChange = true;
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 选取标题
	 */
	private class SetTitleListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				if (reportUIB == null)
					return;
				CellSelection cs = reportUIB.getCellSelection();
				int iRow = cs.getRow();
				int iCol = cs.getColumn();
				WorkSheet css = reportUIB.getWorkSheet();
				CellElement cell = css.getCellElement(iCol, iRow);
				if (cell == null)
					return;
				Object oValue = cell.getValue();
				tfTitle.setValue(oValue);
				setSourceTitle(oValue);
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 设置主表信息里表头得标题
	 * 
	 * @param aTitle
	 * @throws Exception
	 */
	private void setSourceTitle(Object aTitle) throws Exception {
		if (dsSource != null && !dsSource.isEmpty()) {
			dsSource.edit();
			dsSource.beforeFirst();
			dsSource.next();
			dsSource.fieldByName(IBesQryReport.TITLE).setValue(aTitle);
		}
	}

	// ///////////////////////////////////////////////////////////操作///////////////////////////////////////////////////////////////////////////

	/**
	 * 保存进行下一步
	 */
	private class AddActionListenernext implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			try {
				int idex = tabPane.getSelectedIndex();
				if (idex != 0)
					return;
				InfoPackage info = gotoNext();
				if (info.getSuccess() == true) {
					iState = state_Modify;
					setButtonState(iState);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private InfoPackage checkIsNext() throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(String.valueOf(tfFileName.getValue()))) {
			info.setSuccess(false);
			info.setsMessage("请输入表名");
			return info;
		}
		if (Common.isNullStr(String.valueOf(tfFilePath.getValue()))) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "请选择EXCEL文件");
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfSource.getValue()))) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "请选择源表信息");
			return info;
		}

		if (nodeC == null) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "请选择表头");
			return info;
		}
		return info;
	}

	/**
	 * 进入下一个界面将进行的操作
	 * 
	 */
	private InfoPackage gotoNext() {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try {
			info = checkIsNext();
			if (info.getSuccess() == false) {
				JOptionPane.showMessageDialog(tabPane, info.getsMessage());
				tabPane.setSelectedIndex(0);
				return info;
			}
			tabPane.setSelectedIndex(1);
			if (!isChange)
				return info;
			;
			bmk = BAct.initAttrData(dsSource, dsSql);
			saveSourceInfo();
			saveSqlInfo();
			BAct.changeParentID(dsSource, sReportID);
			setColPanel();
			List listFields = TTableHeaderC.getFields();
			DataSet dsBody = DataSet.create();
			initBodyData(dsBody, listFields, "");
			initBodyData(dsBody, listFields, "不隐藏");
			reportC.setBodyData(dsBody);
			reportC.refreshBody();
			reportUIC.repaint();
			isChange = false;
			return info;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return info;
	}

	/**
	 * 进入下一个界面
	 * 
	 * @author Administrator
	 * 
	 */
	private class SelNextPaneListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			InfoPackage info = new InfoPackage();
			info.setSuccess(true);
			int idex = tabPane.getSelectedIndex();
			if (idex == 1) {
				info = gotoNext();
				if (info.getSuccess() == false) {
					JOptionPane.showMessageDialog(tabPane, info.getsMessage());
					return;
				}
				isChange = false;
			}
		}
	}

	// public void doCancel() {
	// // 取消
	// try {
	// Component aSel = tabPane.getSelectedComponent();
	// if (JOptionPane.showConfirmDialog(aSel, "是否确认要全部重新设置？", "提示",
	// JOptionPane.OK_CANCEL_OPTION) == 0) {
	// if (dsTitle != null)
	// dsTitle.cancel();
	// if (dsSource != null)
	// dsSource.cancel();
	// if (dsInfo != null) {
	// dsInfo.cancel();
	// table.setDataSet(dsInfo);
	// table.reset();
	// }
	// if (dsSql != null)
	// dsSql.cancel();
	// tfSource.setValue("");
	// tfMid1.setValue("");
	// tfMid2.setValue("");
	// tfFileName.setValue("");
	// tabPane.setSelectedIndex(0);
	// iState = state_Browse;
	// setButtonState(iState);
	// getReportID();
	// isChange = true;
	// }
	// } catch (Exception ee) {
	// ee.printStackTrace();
	// }
	// }

	/**
	 * 保存
	 * 
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// 保存
			try {
				InfoPackage infoReturn = new InfoPackage();
				infoReturn = checkInfo();
				if (infoReturn.getSuccess() == false) {
					JOptionPane.showMessageDialog(tabPane, infoReturn
							.getsMessage());
					return;
				}

				if (dsTitle == null || dsSql == null || dsSource == null) {
					JOptionPane.showMessageDialog(tabPane, "信息不完整");
					return;
				}
				if (dsTitle.isEmpty() || dsSql.isEmpty() || dsSource.isEmpty()) {
					JOptionPane.showMessageDialog(tabPane, "信息不完整");
					return;
				}
				if (!dsTitle.isEmpty()) {
					InfoPackage info = checkIsNullCol(dsTitle);
					if (info.getSuccess() == false) {
						JOptionPane.showMessageDialog(pnlCAttr, info
								.getsMessage());
						tabPane.setSelectedIndex(1);
						return;
					}
				}
				List listSql = new LinkedList();
				int iRecNum = 0;
				listSql.add(iRecNum, BAct.getBSql(dsSource));
				iRecNum++;
				sReportID = getReportID();
				dsSource.edit();
				dsSource.beforeFirst();
				while (dsSource.next()) {
					dsSource.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
				}
				dsTitle.maskDataChange(true);
				dsTitle.edit();
				dsTitle.beforeFirst();
				while (dsTitle.next()) {
					dsTitle.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
					listSql.add(iRecNum, BAct.getCSql(dsTitle));
					iRecNum++;
				}
				dsTitle.maskDataChange(false);

				List lstSqlLines = new ArrayList();
				dsSql.maskDataChange(true);
				dsSql.edit();
				dsSql.beforeFirst();
				int iLevel = 1;
				while (dsSql.next()) {
					dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
					dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue(
							String.valueOf(iLevel));
					lstSqlLines.add(BAct.getSSql(dsSql));
					iRecNum++;
					iLevel++;
				}

				List lstType = reportTypeList.getSelectData();
				itserv.execSql(listSql, lstSqlLines, sReportID, lstType);
				// 修改或增加节点时，刷新树节点
				dsSource.beforeFirst();
				dsSource.next();
				dsSource.fieldByName(IBesQryReport.IS_END).setValue("1");
				defineReport.refreshNodeEdit(dsSource, lstType, sReportID);

				iState = state_Browse;
				JOptionPane.showMessageDialog(tabPane, "定制报表成功");
				BesUI.this.dispose();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	// ///////////////////////////////////////////////////////////功能函数///////////////////////////////////////////////////////////////////////////

	/**
	 * 判断是否所有的节点都设置了field_ename(父节点除外)
	 * 
	 * @param dsPost
	 * @return
	 * @throws Exception
	 */
	private InfoPackage checkIsNullCol(DataSet dsPost) throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		// boolean IsParNull =
		DataSet ds = (DataSet) dsPost.clone();
		String[] pars = getArrayPars(dsPost);
		pars = BesAct.getNotNullArray(pars);
		boolean arrayIsNull = BesAct.checkArrayIsNull(pars);
		// int iparNum = pars.length;
		// int iparNewNum = 0;
		// for ( int i = 0 ; i < iparNum ; i++ ){
		//			
		// }
		if (pars.length != 0 && arrayIsNull)
			Arrays.sort(pars);
		ds.beforeFirst();
		while (ds.next()) {
			String sNodeID = ds.fieldByName("nodeID").getString();
			if (pars.length != 0 && arrayIsNull
					&& Arrays.binarySearch(pars, sNodeID) >= 0)
				continue;
			String sEName = ds.fieldByName(IBesQryReport.FIELD_ENAME)
					.getString();
			String sCName = ds.fieldByName(IBesQryReport.FIELD_CNAME)
					.getString();
			if (Common.isNullStr(sEName)) {
				info.setSuccess(false);
				info.setsMessage("[" + sCName + "] 的对应列没有设置");
				return info;
			}
		}
		return info;
	}

	/**
	 * 获取表头数据集里所有父节点的数组
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	private String[] getArrayPars(DataSet ds) throws Exception {
		String[] pars = new String[ds.getRecordCount()];
		int iParCirNum = 0;
		ds.beforeFirst();
		while (ds.next()) {
			String sParID = ds.fieldByName("PARID").getString();
			if (Common.isNullStr(sParID))
				continue;
			pars[iParCirNum] = sParID;
			iParCirNum++;
		}
		return pars;
	}

	/**
	 * 设置面板信息
	 * 
	 * @throws Exception
	 */
	private void setColPanel() throws Exception {
		// tableHeader = BesAct.getHeader();
		dsInfo = DataSet.create();
		dsInfo = itserv.getFieldInfo(dsSource.fieldByName(
				IBesQryReport.OBJECT_NAMES).getString());
		table.setDataSet(dsInfo);
		table.reset();
	}

	/**
	 * 保存主表信息（repset）
	 */
	private void saveSourceInfo() throws Exception {
		if (dsSource == null)
			return;
		dsSource.edit();
		dsSource.gotoBookmark(bmk[0]);
		dsSource.fieldByName(IBesQryReport.TITLE).setValue(tfTitle.getValue());
		dsSource.fieldByName(IBesQryReport.REPORT_CNAME).setValue(
				tfFileName.getValue());
		dsSource.fieldByName(IBesQryReport.OBJECT_NAMES).setValue(
				tfSource.getValue());
		String sMT = cbMoneyType.getValue().toString();
		String sMTValue = null;
		if ("1".equals(sMT))
			sMTValue = "元";
		else
			sMTValue = "万元";
		dsSource.fieldByName(IBesQryReport.CURRENCYUNIT).setValue(sMTValue);

		// 保存用户类型
		dsSource.fieldByName(IBesQryReport.DATA_USER).setValue(
				rgUserType.getValue());
		dsSource.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		dsSource.fieldByName(IBesQryReport.LVL_ID).setValue(sReportID);
		sTypeNO = dsSource.fieldByName(IBesQryReport.TYPENO).getString();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void saveSqlInfo() throws Exception {
		if (dsSql == null)
			return;

		dsSql.edit();
		dsSql.gotoBookmark(bmk[1]);
		dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(tfSource.getValue());
		// dsSql.fieldByName( IBesQryReport.SQLLINES ).setValue(
		// BesAct.getSql(tfSource.getValue().toString()) );
		dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		if (!Common.isNullStr(Common.nonNullStr(tfMid1.getValue()))) {
			dsSql.append();
			dsSql.fieldByName(IBesQryReport.SET_YEAR)
					.setValue(Global.loginYear);
			// dsSql.fieldByName( IBesQryReport.REPORT_ID ).setValue( sReportID
			// );
			dsSql.fieldByName(IBesQryReport.SQLTYPE).setValue("MIDQUERY");
			dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue("2");
			dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(
					tfMid1.getValue());
			// String sType = BAct.getTalbeType( tfMid1.getValue().toString());
			// if ( "VIEW".equals(sType))
			// dsSql.fieldByName(IBesQryReport.SQLLINES).setValue(
			// BesAct.getSql(tfMid1.getValue().toString()));
			dsSql.fieldByName(IBesQryReport.TYPENO).setValue(sTypeNO);
			dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		}
		if (!Common.isNullStr(Common.nonNullStr(tfMid2.getValue()))) {
			dsSql.append();
			dsSql.fieldByName(IBesQryReport.SET_YEAR)
					.setValue(Global.loginYear);
			// dsSql.fieldByName( IBesQryReport.REPORT_ID ).setValue( sReportID
			// );
			dsSql.fieldByName(IBesQryReport.SQLTYPE).setValue("MIDQUERY");
			dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue("3");
			dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(
					tfMid2.getValue());
			dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
			// String sType = BAct.getTalbeType( tfMid2.getValue().toString());
			// if ( "VIEW".equals(sType))
			// dsSql.fieldByName(IBesQryReport.SQLLINES).setValue(
			// BesAct.getSql(tfMid2.getValue().toString()));
			dsSql.fieldByName(IBesQryReport.TYPENO).setValue(sTypeNO);
		}
	}

	/**
	 * 表体dataset初始化数据(列属性界面的标题)
	 */
	private DataSet initBodyData(DataSet ds, List list, String fieldValue)
			throws Exception {
		ds.append();
		for (int i = 0; i < list.size(); i++) {
			String sFieldName = null;
			if (list.get(i) == null)
				sFieldName = "";
			else
				sFieldName = list.get(i).toString();
			ds.fieldByName(sFieldName).setValue(fieldValue);
		}
		return ds;
	}

	/**
	 * 列属性信息面板的源表数据发生改变时的监听事件
	 * 
	 * @param ds
	 *            列信息的dataset
	 * 
	 * @param tree
	 *            源表的dataset
	 * 
	 * @param tf
	 *            要填充的textfield
	 * 
	 * @param fieldname
	 *            现在的定位的col的字段名
	 */
	private class DataSoureChangeListener extends MouseAdapter {
		FTextField tf;

		CustomTree tree;

		DataSet ds;

		String fieldname;

		public DataSoureChangeListener(DataSet ds, String fieldname,
				CustomTree tree, FTextField tf) {
			this.tf = tf;
			this.tree = tree;
			this.ds = ds;
			this.fieldname = fieldname;
		}

		public void mouseClicked(MouseEvent e) {
			try {
				MyTreeNode node = (MyTreeNode) tree.getSelectedNode();
				if (node == null || node == tree.getRoot())
					return;
				PfTreeNode nodeNow = (PfTreeNode) node.getUserObject();
				String name = nodeNow.getShowContent();
				tf.setValue(name);
				if (e.getClickCount() == 2) {
					// 把数据填入导colinfo的dataset中
					setColEName();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 设置列信息英文名
	 */
	private void setColEName() throws Exception {
		// 设置表显示信息
		Cell cell = (Cell) reportUIC.getReportContent().getSelectedCell();
		int iCol = cell.getColumn();
		int iRows = TTableHeaderC.getRows();
		Cell cellEName = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows);
		cellEName.setValue(dsInfo.fieldByName(IBesQryReport.Column_NAME)
				.getString());
		reportUIC.repaint();
		// 编辑数据集里的数据
		Cell cellRoot = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows - 1);
		String bmk = cellRoot.getBookmark();
		dsTitle.gotoBookmark(bmk);
		dsTitle.edit();
		dsTitle.fieldByName(IBesQryReport.FIELD_ENAME).setValue(
				dsInfo.fieldByName("COLUMN_NAME").getString());
	}

	/**
	 * reportUI的鼠标监听事件
	 */
	private class ReportClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				Cell cell = (Cell) reportUIC.getReportContent() // 当前选中的单元格
						.getSelectedCell();
				String bmkNow = null;
				int iCol = cell.getColumn();
				int iRows = TTableHeaderC.getRows();
				Object oEName = reportUIC.getReportContent().getCellElement(
						iCol, iRows).getValue();
				String sEName = ((oEName == null) ? "" : oEName.toString());
				if (!Common.isNullStr(sEName)) {
					table.expandTo("COLUMN_NAME", sEName);
				}
				Cell cellRoot = (Cell) reportUIC.getReportContent()
						.getCellElement(iCol, iRows - 1);
				bmkNow = cellRoot.getBookmark();
				dsTitle.gotoBookmark(bmkNow);
				setPanelInfo(iCol);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 设置是否隐藏列的监听事件
	 */
	private class HideChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent e) {
			try {
				setColHide();
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 列格式化信息发生改变时的监听事件
	 */
	private class FormatChangeLisentener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			try {
				Object oValue = tfColFormat.getValue();
				String sValue = (oValue == null) ? "" : oValue.toString();
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT).setValue(
						sValue);
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 设置按钮的监听事件
	 */
	private class SetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// 设置显示信息
				MyTreeNode nodeTree = (MyTreeNode) table.getSelectedNode();
				if (dsInfo == null || dsInfo.isEmpty() || dsInfo.bof()
						|| dsInfo.eof() || nodeTree == null
						|| nodeTree == table.getRoot()) {
					JOptionPane.showMessageDialog(pnlCAttr, "请选择要设置的列信息");
					return;
				}
				setColEName();
				setColHide();
				int iRows = BesAct.tableHeader.getRows();
				Cell cell = (Cell) reportUIC.getReportContent()
						.getSelectedCell();
				int iCol = cell.getColumn();
				Cell cellRoot = (Cell) reportUIC.getReportContent()
						.getCellElement(iCol, iRows - 1);
				Double dWidth = new Double(reportUIC.getReportContent()
						.getColumnWidth(iCol));
				String sCName = cellRoot.getValue().toString();
				String bmk = cell.getBookmark();
				dsTitle.gotoBookmark(bmk);
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_DISPWIDTH).setValue(
						dWidth);
				dsTitle.fieldByName(IBesQryReport.FIELD_CNAME).setValue(sCName);
				dsTitle.fieldByName(IBesQryReport.IS_HIDECOL).setValue(
						("true".equals(ckHide.getValue().toString())) ? "是"
								: "否");
				dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT).setValue(
						Common.nonNullStr(tfColFormat.getValue()));
				dsTitle.fieldByName(IBesQryReport.FIELD_TYPE).setValue(
						cbDataType.getValue().toString());
				setPanelInfo(reportUIC.getReportContent().getSelectedCell()
						.getColumn());
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 列类型改变时的听事件
	 */
	private class ColTypeChangeLisentener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent e) {
			try {
				Object oValue = cbDataType.getValue();
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_TYPE).setValue(oValue);
				if (oValue == null)
					return;
				String sValue = oValue.toString();
				if (sValue.equals("浮点型")) {
					String sFilter = "CVALUE=='带小数的数值'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("整型")) {
					String sFilter = "CVALUE=='整数'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("字符型")) {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				} else if (sValue.equals("日期型")) {
					String sFilter = "CVALUE=='日期'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("货币")) {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				} else {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 把表上的信息显示导面板上
	 * 
	 * @param iCol
	 */
	private void setPanelInfo(int iCol) throws Exception {
		int iRows = TTableHeaderC.getRows();
		Cell cellEName = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows);
		tfColName.setValue(cellEName.getValue());
		Cell cellHide = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows + 1);
		Object hideValue = cellHide.getValue();
		String sHide = null;
		if (hideValue != null)
			sHide = cellHide.getValue().toString();
		if ("不隐藏".equals(sHide))
			ckHide.setValue("false");
		else if ("隐藏".equals(sHide))
			ckHide.setValue("true");
		else
			ckHide.setValue("false");

		if (dsTitle == null || dsTitle.isEmpty())
			return;
		tfColFormat.setValue(dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT)
				.getString());
		String sDataType = dsTitle.fieldByName(IBesQryReport.FIELD_TYPE)
				.getString();
		if (!Common.isNullStr(sDataType))
			cbDataType.setValue(dsTitle.fieldByName(IBesQryReport.FIELD_TYPE)
					.getString());
		String sFormatValue = dsTitle
				.fieldByName(IBesQryReport.FIELD_DISFORMAT).getString();
		if (!Common.isNullStr(sFormatValue))
			tfColFormat.setValue(sFormatValue);
	}

	/**
	 * 设置列信息是否隐藏
	 */
	private void setColHide() throws Exception {
		String sHideValue = ("true".equals(ckHide.getValue().toString())) ? "是"
				: "否";
		String sHideCellValue = ("true".equals(ckHide.getValue().toString())) ? "隐藏"
				: "不隐藏";
		Cell cell = (Cell) reportUIC.getReportContent().getSelectedCell();
		int iCol = cell.getColumn();
		int iRows = TTableHeaderC.getRows();
		Cell cellHide = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows + 1);
		// reportUIC.repaint();
		cellHide.setValue(sHideCellValue);
		reportUIC.repaint();
		// 编辑数据集中的数据
		if (dsTitle == null || dsTitle.isEmpty())
			return;
		dsTitle.edit();
		dsTitle.fieldByName(IBesQryReport.IS_HIDECOL).setValue(sHideValue);
	}

	/**
	 * 用户类型发生改变时的监听事件
	 */
	private void addUserTypeSelListener() throws Exception {
		JRadioButton[] radios = rgUserType.getRadios();
		int iRadioNum = radios.length;
		for (int i = 0; i < iRadioNum; i++) {
			radios[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						getReportID();
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 获得报表编码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getReportID() throws Exception {
		return "";
//		return BAct.getReportID();
	}

	private InfoPackage checkInfo() throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(String.valueOf(tfFileName.getValue()))) {
			info.setsMessage("请输入表名");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfFilePath.getValue()))) {
			info.setsMessage("请选择EXCEL文件");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfSource.getValue()))) {
			info.setsMessage("请选择源表信息");
			tabPane.setSelectedIndex(0);
			return info;
		}
		if (nodeC == null) {
			info.setsMessage("请选择表头");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (reportTypeList.getSelectData().size() == 0) {
			info.setsMessage("请选择报表类型!");
			tabPane.setSelectedIndex(0);
			return info;
		}

		// 检查是否已经存在
		String sFilter = IBesQryReport.SET_YEAR + "=" + Global.loginYear;
		DataSet dsExistSource = itserv.getDataSet(IBesQryReport.TABLENAME_MAIN,
				sFilter);
		dsExistSource.beforeFirst();
		while (dsExistSource.next()) {
			String sEReportID = dsExistSource.fieldByName(
					IBesQryReport.REPORT_ID).getString();
			if (Common.isEqual(sReportID, sEReportID)) {
				info.setSuccess(false);
				info.setsMessage("表里已经已经存在相同编号的报表");
				return info;
			}
		}
		return info;
	}

	/**
	 * 设置按钮状态
	 */
	private void setButtonState(int aState) throws Exception {
		List controls = null;
		// this.getToolbarPanel().getSubControls();
		boolean isEditEnable;
		boolean isSaveEnable;
		isEditEnable = true;
		isSaveEnable = true;
		if (aState == state_Browse) {
			isEditEnable = true;
			isSaveEnable = false;
		} else {
			isEditEnable = false;
			isSaveEnable = true;
		}
		// if (IWf.getCanEditStru() == false) {
		// isEditEnable = false;
		// isSaveEnable = false;
		// }
		// for (int i = 0; i < controls.size(); i++) {
		// FButton btnGet = (FButton) controls.get(i);
		// // if ("上一步".equals(btnGet.getText())) {
		// // btnGet.setEnabled(isSaveEnable);
		// // }
		// // if ("下一步".equals(btnGet.getText())) {
		// // btnGet.setEnabled(isEditEnable);
		// // }
		// if ("关闭".equals(btnGet.getText())) {
		// btnGet.setEnabled(isEditEnable);
		// }
		// if ("保存".equals(btnGet.getText())) {
		// btnGet.setEnabled(isSaveEnable);
		// }
		// if ("取消".equals(btnGet.getText())) {
		// btnGet.setEnabled(isSaveEnable);
		// }
		// }
	}

	/**
	 * 上一步
	 * 
	 */
	private class AddActionListenerpre implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			int idex = tabPane.getSelectedIndex();
			if (idex != 0)
				tabPane.setSelectedIndex(0);

		}
	}

	/**
	 * 关闭
	 * 
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			BesUI.this.dispose();
		}
	}

	/**
	 * 按钮面板
	 */
	private class FPanelBtnButtom extends FPanel {
		private FButton fbtnPrev;

		private FButton fbtnNext;

		private FButton fbtnDown;

		private FButton fbtnCancel;

		private static final long serialVersionUID = 1L;

		public FPanelBtnButtom() {
			this.setLeftInset(10);
			this.setRightInset(10);
			this.setLayout(new RowPreferedLayout(2));

			fbtnPrev = new FButton("fbtnPrev", "上一步");
			fbtnPrev.addActionListener(new AddActionListenerpre());
			fbtnNext = new FButton("fbtnNext", "下一步");
			fbtnNext.addActionListener(new AddActionListenernext());
			FFlowLayoutPanel fpnlPreNext = new FFlowLayoutPanel();
			// 设置靠右显示
			fpnlPreNext.setAlignment(FlowLayout.RIGHT);
			// 上一步
			fpnlPreNext.addControl(fbtnPrev, new TableConstraints(1, 1, false,
					false));
			// 下一步
			fpnlPreNext.addControl(fbtnNext, new TableConstraints(1, 1, false,
					false));

			fbtnDown = new FButton("fbtnDown", "完 成");
			fbtnDown.addActionListener(new DownActionListener());
			fbtnCancel = new FButton("fbtnCancel", "关 闭");
			fbtnCancel.addActionListener(new CancelActionListener());

			FFlowLayoutPanel fpnlDown = new FFlowLayoutPanel();
			fpnlDown.setAlignment(FlowLayout.RIGHT);
			// 完成
			fpnlDown.addControl(fbtnDown, new TableConstraints(1, 1, false,
					false));
			// 取消
			fpnlDown.addControl(fbtnCancel, new TableConstraints(1, 1, false,
					false));

			this
					.addControl(fpnlPreNext, new TableConstraints(1, 1, true,
							true));
			this.addControl(fpnlDown, new TableConstraints(1, 1, true, true));

		}
	}

}
