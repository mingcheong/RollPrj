package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.Report;

/**
 * 设置收支总表设置，主界面
 * 
 * @author qzc XXL
 * 
 */
public class SzzbSet extends FModulePanel {
	/**
	 * 
	 */
	public JFrame frame;

	public static int lastValue_source = -1;// 记录类型选择的变动

	public static int lastValue_Acc = -1;// 记录类型选择的变动

	public static final int PAGE_BASE = 0;

	public static final int PAGE_COL = 1;

	public boolean maskPageChange = false;

	public boolean maskChange = false;// 编辑控件的变动开关

	public boolean isNeedFresh = false;// 第二个页面是否要刷新

	private static final long serialVersionUID = -7862352940577468074L;

	public boolean isChanged = false;

	private boolean maskTypeChange = false;// 类型设置变动响应的开关，add by xxl 20090923

	private boolean isRowOrColChanged = false;

	private XMLData xmlFormat;

	String reportID;

	String setYear = Global.getSetYear();

	CellColPanel cellPanel;

	FTabbedPane ftabPnlSet;

	DataSet dsHeader;// 表头DataSet

	DataSet dsRepset; // 主表

	DataSet dsSzzb;// 表体

	CellSetPanel pnlCellSet;

	DefineReport defineReport;

	JWorkBook jWorkBook;

	// 扫描报表
	SzReportPanel szReportPanel;

	// 主表信息
	BaseInfoTitledPanel ftitPnlBaseInfo;

	public SzzbSet() {
		super();
		init();
		// this.setReportID("5000040035");
	}

	public SzzbSet(String sReportID) {
		super();
		init();
		setReportID(sReportID);
	}

	public SzzbSet(String sReportID, String setYear) {
		super();
		this.setYear = setYear;
		init();
		setReportID(sReportID);
	}

	public SzzbSet(JFrame frame, String sReportID, String setYear,
			DefineReport defineReport) {
		super();
		this.setYear = setYear;
		this.frame = frame;
		this.defineReport = defineReport;
		init();
		setReportID(sReportID);

		// 在此添加恢复显示的功能
		initDispReport();
		// --------------------------

	}

	public void initize() {

	}

	public void init() {
		try {
			FSplitPane spnlBase = new FSplitPane();
			FSplitPane spnlCol = new FSplitPane();

			spnlCol.setOrientation(FSplitPane.VERTICAL_SPLIT);
			spnlBase.setOrientation(FSplitPane.VERTICAL_SPLIT);
			spnlBase.setResizeWeight(1);
			spnlCol.setResizeWeight(1);

			// 上面报表信息
			szReportPanel = new SzReportPanel(this);
			this.jWorkBook = szReportPanel.getJWorkBook();
			spnlBase.addControl(szReportPanel);
			szReportPanel.setBorder(null);

			cellPanel = new CellColPanel(this);
			spnlCol.add(cellPanel, FSplitPane.TOP);

			// 下面设置信息
			ftabPnlSet = new FTabbedPane();

			ftitPnlBaseInfo = new BaseInfoTitledPanel();
			pnlCellSet = new CellSetPanel(this);
			spnlCol.add(pnlCellSet, FSplitPane.BOTTOM);
			ftabPnlSet.addControl("基础信息设置", spnlBase);
			ftabPnlSet.addControl("单元格属性设置", spnlCol);
			ftabPnlSet.addChangeListener(new TabPnlSetChangeListener(this));

			spnlBase.addControl(ftitPnlBaseInfo);
			ftitPnlBaseInfo.setBorder(null);

			addSelectionListener(pnlCellSet);

			this.add(ftabPnlSet);
			getData();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "收支总表信息设置显示界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setReportID(String reportID) {
		if (reportID != null && this.reportID != null
				&& reportID.equals(this.reportID))
			return;
		this.reportID = reportID;
		maskChange = true;
		refreshData();
		maskChange = false;
	}

	/**
	 * 基础信息设置
	 * 
	 * @author qzc
	 * 
	 */
	public class BaseInfoTitledPanel extends FTitledPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1496501128963959223L;

		FTextField ftxtEditRepName;// 报表名称

		FTextField ftxtEditReportTitle;// 标题内容

		ReportInfoPanel reportTitlePanel; // 表标题

		CustomTree ftreeColSelect;// 列信息

		FRadioGroup frdoFtype;// 数据类型

		FCheckBox fchkHideFlag;// 该列隐藏

		FComboBox fcbxSFormate;// 显示风格

		ReportTypeList reportTypeList;// 报表分类

		JComboBox cbxCurrencyUnit;// 货币类型
		FCheckBox cbxConvert;//元到万元的转换

		// 用户类型
		FRadioGroup reportUserTypeGrp;

		ReportInfoPanel reportColumnsPanel;// 表头

		public BaseInfoTitledPanel() throws Exception {
			this.setLeftInset(5);
			this.setRightInset(5);
			RowPreferedLayout rLay = new RowPreferedLayout(7);
			// rLay.setColumnWidth(430);
			rLay.setRowHeight(24);
			rLay.setColumnGap(1);
			rLay.setRowGap(1);
			this.setLayout(rLay);
			// 表标题
			FPanel fpnlTitle = new FPanel();
			fpnlTitle.setTitle("表标题");
			fpnlTitle.setLayout(new RowPreferedLayout(1));

			ftxtEditRepName = new FTextField("报表名称：");
			ftxtEditRepName.addValueChangeListener(new valueChange());
			ftxtEditRepName.setProportion(0.16f);
			ftxtEditReportTitle = new FTextField("标题内容：");
			ftxtEditReportTitle.setEditable(false);
			ftxtEditReportTitle.setProportion(0.16f);
			reportTitlePanel = new ReportInfoPanel("获取表标题");
			reportTitlePanel.fbtnGetInfo
					.addActionListener(new GetTitleActionListener(SzzbSet.this,
							reportTitlePanel));
			fpnlTitle.addControl(ftxtEditRepName, new TableConstraints(1, 1,
					false, true));
			fpnlTitle.addControl(ftxtEditReportTitle, new TableConstraints(1,
					1, false, true));
			fpnlTitle.addControl(reportTitlePanel, new TableConstraints(1, 1,
					true, true));
			// 表头
			FPanel fpnlReportColumns = new FPanel();
			fpnlReportColumns.setTitle("表头");
			fpnlReportColumns.setLayout(new RowPreferedLayout(1));
			reportColumnsPanel = new ReportInfoPanel("获取表头列");
			reportColumnsPanel.fbtnGetInfo
					.addActionListener(new GetHeaderActionListener(
							SzzbSet.this, reportColumnsPanel));
			fpnlReportColumns.addControl(reportColumnsPanel,
					new TableConstraints(1, 1, true, true));

			// 设置列信息
			FPanel fpnlSetColInfo = new FPanel();
			fpnlSetColInfo.setTitle("设置列信息");
			RowPreferedLayout rLaySetColInfo = new RowPreferedLayout(2);
			rLaySetColInfo.setRowGap(5);
			rLaySetColInfo.setColumnGap(5);
			fpnlSetColInfo.setLayout(rLaySetColInfo);
			ftreeColSelect = new CustomTree("列信息", null, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_FNAME, "", null);
			ftreeColSelect
					.addTreeSelectionListener(new ColSelectTreeSelectionListener(
							SzzbSet.this));
			FScrollPane fspnlColSelect = new FScrollPane(ftreeColSelect);

			FPanel fpnlFtype = new FPanel(); // 数据类型
			fpnlFtype.setLayout(new RowPreferedLayout(1));
			fpnlFtype.setTitle("数据类型");
			frdoFtype = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoFtype.setRefModel("整数#" + ISzzbSet.FORMAT_INT + "+浮点型#"
					+ ISzzbSet.FORMAT_FLOAT + "+无格式#" + ISzzbSet.FORMAT_STRING
					+ "");
			frdoFtype.setTitleVisible(false);
			frdoFtype.addValueChangeListener(new FtypeValueChangeListener());

			fpnlFtype.addControl(frdoFtype, new TableConstraints(1, 1, true,
					true));

			fchkHideFlag = new FCheckBox("该列隐藏");
			fchkHideFlag.setTitlePosition("RIGHT");
			fchkHideFlag.setVisible(false);
			fchkHideFlag
					.addValueChangeListener(new HideFlagValueChangeListener());

			fcbxSFormate = new FComboBox();
			fcbxSFormate.setTitle("显示格式：");
			fcbxSFormate.setProportion(0.35f);
			fcbxSFormate.setValue("");
			fcbxSFormate
					.addValueChangeListener(new FormateValueChangeListener());

			fpnlSetColInfo.addControl(fspnlColSelect, new TableConstraints(5,
					1, false, true));
			fpnlSetColInfo.addControl(fpnlFtype, new TableConstraints(2, 1,
					false, true));
			fpnlSetColInfo.addControl(fchkHideFlag, new TableConstraints(1, 1,
					false, true));
			fpnlSetColInfo.addControl(fcbxSFormate, new TableConstraints(1, 1,
					false, true));

			// 表内容
			FPanel fpnlreportContent = new FPanel();
			fpnlreportContent.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

			// 报表分类信息
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("报表类型：");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			FLabel flblCurrencyUnit = new FLabel();
			flblCurrencyUnit.setTitle("货币类型：");
			cbxCurrencyUnit = new JComboBox(new String[] { "", "元", "万元" });
			cbxCurrencyUnit.addActionListener(new valueChange());

			cbxConvert = new FCheckBox();
			cbxConvert.setTitle("元转换成万元");
			cbxConvert.addValueChangeListener(new valueChange());
			cbxConvert.setPreferredSize(new Dimension(240, 20));

			FLabel flblEmpty = new FLabel();
			flblEmpty.setText("                      ");
			// 暂时不用
			// FButton fbtnSaveDetail = new FButton("fbtnSaveDetail", "保存设置");

			// fbtnSaveDetail.addActionListener(new SaveReportActionListener(
			// SzzbSet.this));

			// 定义报表用户类型
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#仅单位使用 +1#仅财政使用+2#财政单位共同使用");
			reportUserTypeGrp.setTitleVisible(false);
			reportUserTypeGrp.setValue("2");
			// 定义报表用户类型面板
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("用户类型");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// 报表用户类型单选框加入报表用户类型面板
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			fpnlreportContent.addControl(flblCurrencyUnit);
			fpnlreportContent.add(cbxCurrencyUnit);

			fpnlreportContent.addControl(cbxConvert);

			// fpnlreportContent.addControl(fbtnSaveDetail);

			this.addControl(fpnlTitle, new TableConstraints(4, 3, false, true));
			this.addControl(fpnlReportType, new TableConstraints(6, 1, false,
					true));
			this.addControl(fpnlSetColInfo, new TableConstraints(6, 3, false,
					true));
			this.addControl(fpnlReportColumns, new TableConstraints(2, 3,
					false, true));
			this.addControl(reportUserTypePnl, new TableConstraints(2, 3,
					false, true));
			this.addControl(fpnlreportContent, new TableConstraints(2, 4,
					false, true));
		}

		/**
		 * 设置数据类型
		 * 
		 * @author qzc
		 * 
		 */
		private class FtypeValueChangeListener implements ValueChangeListener {

			public void valueChanged(ValueChangeEvent arg0) {

				if (maskTypeChange)// 如果是设置则跳出
					return;
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				String sValue = (String) arg0.getNewValue();
				// add by XXL TODO
				// 另外要清除格式 和设置选择项
				Vector sFormat = (Vector) xmlFormat.get(frdoFtype.getRefModel()
						.getNameByValue(arg0.getNewValue()));

				maskTypeChange = true;
				fcbxSFormate.setRefModel(sFormat);
				fcbxSFormate.setValue("");
				maskTypeChange = false;

				try {
					dsHeader.edit();
					dsHeader.fieldByName(IQrBudget.FIELD_TYPE).setValue(sValue);
					dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT)
							.setValue("");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"收支总表信息设置数据类型发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		/**
		 * 设置列信息是否隐藏
		 * 
		
		 * 
		 */
		private class HideFlagValueChangeListener implements
				ValueChangeListener {
			public void valueChanged(ValueChangeEvent arg0) {
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				try {
					dsHeader.edit();
					if ("true".equals(fchkHideFlag.getValue().toString())) {
						dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
								.setValue("是");
					} else {
						dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
								.setValue("否");
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"收支总表信息设置列信息隐藏发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		private class FormateValueChangeListener implements ValueChangeListener {

			public void valueChanged(ValueChangeEvent arg0) {
				if (maskTypeChange)
					return;
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				try {
					dsHeader.edit();
					dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT).setValue(
							fcbxSFormate.getValue().toString());
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"收支总表信息设置列信息显示格式发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void showRepsetInfo() throws Exception {

		if (!dsRepset.isEmpty()) {
			// 显示表头信息
			ftitPnlBaseInfo.ftxtEditRepName.setValue(dsRepset.fieldByName(
					IQrBudget.REPORT_CNAME).getValue());
			ftitPnlBaseInfo.ftxtEditReportTitle.setValue(dsRepset.fieldByName(
					IQrBudget.TITLE).getValue());

			ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_AREA)
							.getValue());
			ftitPnlBaseInfo.reportTitlePanel.ftxtEditTitleFont
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_FONT)
							.getValue());
			ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleFontSize
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_FONTSIZE)
							.getValue());

			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_AREA)
							.getValue());
			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditTitleFont
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_FONT)
							.getValue());
			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleFontSize
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_FONTSIZE)
							.getValue());
			ftitPnlBaseInfo.reportTypeList.setSelected(dsRepset.fieldByName(
					IQrBudget.REPORT_ID).getString());

//			 if (!Common.isNullStr(dsRepset.fieldByName(IQrBudget.LVL_ID)
//			 .getString()))// 如果新增时并没有报表级次
//			 cbxReportType.setCode(dsRepset.fieldByName(IQrBudget.LVL_ID)
//			 .getString().substring(0, 6));

			ftitPnlBaseInfo.cbxCurrencyUnit.setSelectedItem(dsRepset
					.fieldByName(IQrBudget.CURRENCYUNIT).getValue());
			String isConvert = dsRepset.fieldByName(ISzzbSet.CONVERT_FIELD)
					.getString();
			if ("1".equals(isConvert)) {
				ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(true));
			} else
				ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(false));
		}

	}

	/**
	 * 显示列信息
	 * 
	 * @param szzbSet
	 * @throws Exception
	 */
	public void showColInfo() throws Exception {
		// 显示列信息
		// dsHeader.cancel();
		ftitPnlBaseInfo.ftreeColSelect.setDataSet(dsHeader);
		ftitPnlBaseInfo.ftreeColSelect.reset();
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void refreshData() {
		try {
			getData();
			showRepsetInfo();
			showColInfo();
			cellPanel.dispData(this.reportID);
			isChanged = false;
			isNeedFresh = false;
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

	private void getData() throws Exception {

		dsRepset = SzzbSetI.getMethod().getRepset(setYear, 1, reportID);

		// 显示表头
		dsHeader = SzzbSetI.getMethod().getReportHeader(reportID, setYear);
		// showColInfo();
		// 显示报表信息
		// setReportBodyInfo(sReportId, jWorkBook);
		// add by xxl 用于动态显示格式化信息 20090922
		xmlFormat = SzzbSetI.getMethod().getFormatList();
	}

	public void refreshColInfo() {

	}

	public void saveBaseInfo() {
		new SaveReportActionListener(this).actionPerformed(null);

	}

	public void saveColInfo() {
		String sErr = cellPanel.saveCols();
		if (!Common.isNullStr(sErr)) {
			// new MessageBox(sErr, MessageBox.MESSAGE, MessageBox.BUTTON_OK)
			// .show();
			JOptionPane.showMessageDialog(SzzbSet.this, sErr, "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// new MessageBox("保存成功!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
		// .show();
		JOptionPane.showMessageDialog(SzzbSet.this, "保存成功!", "提示",
				JOptionPane.INFORMATION_MESSAGE);

		isChanged = false;

	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public void setActivePage(int iPage) {
		this.ftabPnlSet.setSelectedIndex(iPage);
	}

	public int getActivePage() {
		return this.ftabPnlSet.getSelectedIndex();
	}

	public String getReportID() {
		return reportID;
	}

	public void addSelectionListener(ISelectionListener l) {
		cellPanel.addSelectLisener(l);
	}

	class valueChange implements ValueChangeListener, ActionListener {

		public void valueChanged(ValueChangeEvent arg0) {
			if (maskChange)
				return;
			isChanged = true;

		}

		public void actionPerformed(ActionEvent e) {
			if (ftitPnlBaseInfo.cbxCurrencyUnit == e.getSource()) {//如果是单位选择
				if (ftitPnlBaseInfo.cbxCurrencyUnit.isEnabled()) {//只有在可编辑的状态下才生效
					if ("万元".equals(ftitPnlBaseInfo.cbxCurrencyUnit
							.getSelectedItem()))
						ftitPnlBaseInfo.cbxConvert.setEnabled(true);
					else {
						ftitPnlBaseInfo.cbxConvert.setEnabled(false);
						ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(false));
					}
				}

			}
			if (maskChange)
				return;
			isChanged = true;

		}
	}

	public void setReportIDForAdd(String sReportID) {
		try {

			this.reportID = sReportID;
			// 更新报表
			if (dsRepset != null && !dsRepset.isEmpty()) {
				dsRepset.beforeFirst();
				dsRepset.next();
				dsRepset.edit();
				dsRepset.fieldByName(IQrBudget.REPORT_ID).setValue(sReportID);
			}

			// 更新表头
			if (dsHeader != null) {
				String sBook = dsHeader.toogleBookmark();
				dsHeader.beforeFirst();
				dsHeader.next();
				dsHeader.edit();
				while (!dsHeader.eof()) {
					dsHeader.fieldByName(IQrBudget.REPORT_ID).setValue(
							sReportID);
					dsHeader.next();
				}
				dsHeader.gotoBookmark(sBook);
				// 更新CELL
				cellPanel.setNewReportID(sReportID);
			}
		} catch (Exception e) {
			// if (this.frame != null)
			// new MessageBox(this.frame, "更新数据出错!", e.getMessage(),
			// MessageBox.ERROR, MessageBox.BUTTON_OK).show();
			JOptionPane.showMessageDialog(SzzbSet.this, "更新数据出错!"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			//			
			// else
			// new MessageBox("更新数据出错!", e.getMessage(), MessageBox.ERROR,
			// MessageBox.BUTTON_OK).show();
			// JOptionPane.showMessageDialog(SzzbSet.this,
			// "更新数据出错!"+e.getMessage(), "提示",
			// JOptionPane.ERROR_MESSAGE);
		}

	}

	// 初始化显示报表
	private void initDispReport() {
		if (Common.isNullStr(reportID)) {
			szReportPanel.getFlblOpenExcel().setTitle("无报表信息");
		} else {
			try {
				List lstConsCell = SzzbSetI.getMethod().getSzzbCons(
						this.reportID, Global.getSetYear());
				dispConsCells(lstConsCell, szReportPanel.getWorkSheet());
				szReportPanel.getFlblOpenExcel().setTitle("恢复的报表");
			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame, "初始化报表失败!"
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void dispConsCells(List lstConsCell, Report report) {

		if (lstConsCell == null || lstConsCell.size() == 0)
			return;
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;

		// int colStart = thDetail.getColumns();
		Object oValue;
		Cell cellElement;
		XMLData aData;
		int iCount = lstConsCell.size();
		CellSelection cells = ReportUtil
				.translateToNumber(this.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString());
		int titleStart = cells.getRow();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstConsCell.get(i);
			iColumn = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMN));
			iColumnSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMNSPAN));
			iRow = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROW));
			iRowSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROWSPAN));
			oValue = Common.getAStringField(aData, ISzzbSet.FIELD_VALUE);

			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// 设格式
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.HOR_ALIGNMENT))).deriveVerticalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.VER_ALIGNMENT)));
			FRFont aFont = FRFont.getInstance(Common.getAStringField(aData,
					ISzzbSet.FIELD_FONT), Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_FONTSTYPE)), Integer
					.parseInt(Common.getAStringField(aData,
							ISzzbSet.FIELD_FONTSIZE)));
			style = style.deriveFRFont(aFont);

			if (iRow >= titleStart) {
				style = style.deriveBorder(1, Color.BLACK, 1, Color.BLACK, 1,
						Color.BLACK, 1, Color.BLACK);
			}

			cellElement.setStyle(style);
			report.setColumnWidth(iColumn, Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_COLWIDTH)));
			report.setRowHeight(iRow, Integer.parseInt(Common.getAStringField(
					aData, ISzzbSet.FIELD_ROWHEIGHT)));
			report.addCellElement(cellElement, false);
		}
	}

	// 添加保存给外界面调用,这样可以把按钮给隐了，好操作一些
	public void saveInfos() {
		if (ftabPnlSet.getSelectedIndex() == 0) {// 如果选中是的第一个面页
			new SaveReportActionListener(this).actionPerformed(null);
		} else {
			saveColInfo();
		}

	}

	public boolean isMaskTypeChange() {
		return maskTypeChange;
	}

	public void setMaskTypeChange(boolean maskTypeChange) {
		this.maskTypeChange = maskTypeChange;
	}

	public XMLData getXmlFormat() {
		return xmlFormat;
	}

	public boolean isRowOrColChanged() {
		return isRowOrColChanged;
	}

	public void setRowOrColChanged(boolean isRowOrColChanged) {
		this.isRowOrColChanged = isRowOrColChanged;
	}

}
