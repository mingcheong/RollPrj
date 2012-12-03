package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.CellElement;
import com.fr.report.WorkBook;
import com.fr.report.WorkSheet;

/**
 * 查询报表封面设置
 * 
 * @author qzc
 * 
 */
public class ConverSet extends JFrame {

	private static final long serialVersionUID = 1L;

	// 打开Excel打开文件的路径
	private FLabel flblOpenExcel;

	private JWorkBook jWorkBook;

	private FTabbedPane ftabPnlSet;

	// 表名称
	private FTextField ftxtTitle;

	// 报表用户类型
	private FRadioGroup reportUserTypeGrp = null;

	// 单元格参数
	private FComboBox fcbxFieldPara;

	// 报表分类
	private ReportTypeList reportTypeList;

	// 占用区域
	private FTextField ftxtConverArea;

	// 是否启用
	private FCheckBox fchkIsActice;

	private DataSet dsSzzb = null;

	private int iTypeNo = 1;

	private int KEY_UP = 38;

	private int KEY_DOWN = 40;

	private int KEY_LEFT = 37;

	private int KEY_RIGHT = 39;

	private DefineReport defineReport;

	private RepSetObject repSetObject;

	private boolean clearFlag = false;

	private String sReportID = null;

	public ConverSet(RepSetObject repSetObject, DefineReport defineReport) {
		this.setSize(900, 650);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		if (repSetObject == null) {
			this.setTitle("新增封面或目录");
		} else {
			this.setTitle("修改封面或目录");
		}
		if (repSetObject != null) {
			this.sReportID = repSetObject.getREPORT_ID();
		}

		this.repSetObject = repSetObject;
		this.defineReport = defineReport;

		if (this.repSetObject == null) {
			this.repSetObject = new RepSetObject();
		}

		init();
	}

	/**
	 * 初始化界面
	 * 
	 */
	private void init() {
		try {
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(FSplitPane.VERTICAL_SPLIT);
			fSplitPane.setDividerLocation(Toolkit.getDefaultToolkit()
					.getScreenSize().height - 310);
			this.getContentPane().add(fSplitPane);

			// 上面报表信息
			FPanel fpnlReport = new FPanel();
			fSplitPane.addControl(fpnlReport);
			fpnlReport.setLeftInset(10);
			fpnlReport.setRightInset(10);
			fpnlReport.setBottomInset(10);
			fpnlReport.setTopInset(10);

			WorkBook workBook = new WorkBook() {
				private static final long serialVersionUID = 1L;

			};
			WorkSheet workSheet = new WorkSheet() {

				private static final long serialVersionUID = 1L;

			};
			workBook.addReport(workSheet);
			jWorkBook = new JWorkBook() {
				private static final long serialVersionUID = 1L;

			};

			jWorkBook.setWorkBook(workBook);
			jWorkBook.getGrid().addMouseListener(new reportMouseListener());
			jWorkBook.getGrid().addKeyListener(new reportKeyListener());

			flblOpenExcel = new FLabel();
			flblOpenExcel.setForeground(Color.blue);
			FButton fbtnOpenExcel = new FButton("fbtnOpenExcel", "打开Excel模板");
			fbtnOpenExcel.addActionListener(new OpenExcelActionListener(
					jWorkBook, flblOpenExcel));

			RowPreferedLayout rLay = new RowPreferedLayout(3);
			rLay.setRowHeight(23);
			rLay.setRowGap(5);
			rLay.setColumnWidth(115);
			fpnlReport.setLayout(rLay);
			fpnlReport.add(jWorkBook, new TableConstraints(1, 3, true, true));
			fpnlReport.addControl(fbtnOpenExcel, new TableConstraints(1, 1,
					false, false));
			fpnlReport.addControl(flblOpenExcel, new TableConstraints(1, 1,
					false, true));

			// 下面设置信息
			ftabPnlSet = new FTabbedPane();
			FTitledPanel ftitPnlConverInfo = new ConverInfoTitledPanel();
			ftabPnlSet.addControl("信息设置", ftitPnlConverInfo);
			ftabPnlSet.setBorder(null);

			// 定义按钮面板
			ButtonPanel buttonPanel = new ButtonPanel();

			// 定义下方面板
			FPanel fpnlBottom = new FPanel();
			RowPreferedLayout rLayBtn = new RowPreferedLayout(1);
			rLayBtn.setRowHeight(30);
			fpnlBottom.setLayout(rLayBtn);
			fpnlBottom.addControl(ftabPnlSet, new TableConstraints(1, 1, true,
					true));
			fpnlBottom.addControl(buttonPanel, new TableConstraints(1, 1,
					false, true));
			fSplitPane.addControl(fpnlBottom);

			// 显示基本信息
			if (!Common.isNullStr(sReportID)) {
				showBaseInfo();
				// 显示表体信息
				dsSzzb = SzzbSetI.getMethod().getSzzb(sReportID,
						Global.loginYear);
				setConverInfo(jWorkBook, dsSzzb);
			} else {
				dsSzzb = DataSet.create();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "封面或目录设置显示界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 封面信息设置
	 * 
	 */
	private class ConverInfoTitledPanel extends FTitledPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ConverInfoTitledPanel() throws Exception {
			this.setLeftInset(5);
			this.setRightInset(5);
			this.setTopInset(5);
			RowPreferedLayout rLay = new RowPreferedLayout(2);
			rLay.setColumnWidth(370);
			rLay.setRowGap(8);
			this.setLayout(rLay);

			ftxtTitle = new FTextField("报表名称：");

			// 报表分类信息
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("报表类型：");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			// 用户类型
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#仅单位使用 +1#仅财政使用+2#财政单位共同使用");
			reportUserTypeGrp.setValue("2");
			reportUserTypeGrp.setTitleVisible(false);
			// 定义报表用户类型面板
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("用户类型");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// 报表用户类型单选框加入报表用户类型面板
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			FPanel fpnlConverArea = new FPanel();
			fpnlConverArea.setTitle("封面或目录区域");
			RowPreferedLayout rConverAreaLay = new RowPreferedLayout(2);
			rConverAreaLay.setColumnWidth(90);
			fpnlConverArea.setLayout(rConverAreaLay);
			ftxtConverArea = new FTextField("占用区域：");
			ftxtConverArea.setEditable(false);
			FButton fbtnConverArea = new FButton("fbtnConverArea", "获得区域");
			fbtnConverArea.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int i = jWorkBook.getCellSelection().getColumn();
					int j = jWorkBook.getCellSelection().getRow();
					int ispan = jWorkBook.getCellSelection().getColumnSpan();
					int jspan = jWorkBook.getCellSelection().getRowSpan();
					String sColumnName = ReportUtil.translateToColumnName(i)
							+ (j + 1) + ":"
							+ ReportUtil.translateToColumnName(i + ispan - 1)
							+ (j + jspan);
					ftxtConverArea.setValue(sColumnName);

					// 得到fb_u_qr_szzb表信息
					try {
						// if (dsSzzb == null) {
						// dsSzzb = DataSet.create();
						// } else {
						// dsSzzb.clearAll();
						// }
						dsSzzb = DataSet.create();
						dsSzzb.edit();
						getSzzbInfo(dsSzzb);
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ConverSet.this,
								"获得区域发生错误，错误信息：" + e.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			});
			fpnlConverArea.addControl(ftxtConverArea, new TableConstraints(1,
					1, false, true));
			fpnlConverArea.addControl(fbtnConverArea, new TableConstraints(1,
					1, false, false));

			FPanel fpnlFieldPara = new FPanel();
			fpnlFieldPara.setTitle("设置单元格参数");
			RowPreferedLayout rFieldParaLay = new RowPreferedLayout(2);
			rFieldParaLay.setColumnWidth(90);
			fpnlFieldPara.setLayout(rFieldParaLay);
			fcbxFieldPara = new FComboBox("单元格参数：");
			fcbxFieldPara.setRefModel("# +DIV_NAME#预算单位");
			FButton fbtnFieldArea = new FButton("fbtnFieldArea", "设置参数");
			fbtnFieldArea.addActionListener(new FieldAreaActionListener());
			fpnlFieldPara.addControl(fcbxFieldPara, new TableConstraints(1, 1,
					false, true));
			fpnlFieldPara.addControl(fbtnFieldArea, new TableConstraints(1, 1,
					false, false));

			// 是否启用
			fchkIsActice = new FCheckBox("是否启用");
			fchkIsActice.setTitlePosition("RIGHT");
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);

			// 报表名称
			this
					.addControl(ftxtTitle, new TableConstraints(1, 1, false,
							false));
			// 报表分类
			this.addControl(fpnlReportType, new TableConstraints(5, 1, false,
					false));
			// 设置封面区域
			this.addControl(fpnlConverArea, new TableConstraints(2, 1, false,
					false));
			// 用户类型
			this.addControl(reportUserTypePnl, new TableConstraints(2, 1,
					false, false));
			// 参数名称
			this.addControl(fpnlFieldPara, new TableConstraints(2, 1, false,
					false));
			// 是否启用
			this.addControl(fchkIsActice, new TableConstraints(1, 1, false,
					false));
		}
	}

	/**
	 * 按钮面板
	 */
	private class ButtonPanel extends FFlowLayoutPanel {
		private static final long serialVersionUID = 1L;

		// "完成"按钮
		private FButton fbtnDown = null;

		// "取消"按钮
		private FButton fbtnCancel = null;

		public ButtonPanel() {
			init();
		}

		private void init() {
			// 设置靠右显示
			this.setAlignment(FlowLayout.RIGHT);

			fbtnDown = new FButton("fbtnDown", "完 成");
			fbtnDown.addActionListener(new SaveConverActionListener());
			fbtnCancel = new FButton("fbtnDown", "关 闭");
			fbtnCancel.addActionListener(new CancelActionListener());

			// 完成
			this.addControl(fbtnDown, new TableConstraints(1, 1, true, false));
			// 取消
			this
					.addControl(fbtnCancel, new TableConstraints(1, 1, true,
							false));
		}

	}

	/**
	 * 显示基础信息
	 */
	private void showBaseInfo() throws Exception {
		ftxtTitle.setValue(repSetObject.getREPORT_CNAME());
		// 定义报表分类
		reportTypeList.setSelected(repSetObject.getREPORT_ID());
		ftxtConverArea.setValue(repSetObject.getCOLUMN_AREA());

		reportUserTypeGrp.setValue(String.valueOf(repSetObject.getDATA_USER()));
		((JCheckBox) fchkIsActice.getEditor()).setSelected(Common
				.estimate(repSetObject.getIS_ACTIVE()));
	}

	/**
	 * 显示表体信息
	 * 
	 */
	private void setConverInfo(JWorkBook jWorkBook, DataSet dsSzzb)
			throws Exception {
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;
		Object oValue;
		Cell cellElement;

		dsSzzb.beforeFirst();
		while (dsSzzb.next()) {
			iColumn = dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN).getInteger();
			iColumnSpan = dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMNSPAN)
					.getInteger();
			iRow = dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).getInteger();
			iRowSpan = dsSzzb.fieldByName(ISzzbSet.FIELD_ROWSPAN).getInteger();

			oValue = dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).getValue();
			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// 设格式
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(dsSzzb.fieldByName(
					ISzzbSet.HOR_ALIGNMENT).getInteger());
			style = style.deriveVerticalAlignment(dsSzzb.fieldByName(
					ISzzbSet.VER_ALIGNMENT).getInteger());
			FRFont aFont = FRFont.getInstance(dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONT).getString(), dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONTSTYPE).getInteger(), dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONTSIZE).getInteger());
			style = style.deriveFRFont(aFont);
			cellElement.setStyle(style);
			jWorkBook.getReport().setColumnWidth(iColumn,
					dsSzzb.fieldByName(ISzzbSet.FIELD_COLWIDTH).getInteger());
			jWorkBook.getReport().setRowHeight(iRow,
					dsSzzb.fieldByName(ISzzbSet.FIELD_ROWHEIGHT).getInteger());
			jWorkBook.getReport().addCellElement(cellElement, false);
		}
	}

	/**
	 * 保存封面
	 */
	public class SaveConverActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (Common.isNullStr(ftxtTitle.getValue().toString().trim())) {
				JOptionPane.showMessageDialog(ConverSet.this, "请填写报表名称!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				ftxtTitle.setFocus();
				return;
			}
			if ("".equals(ftxtConverArea.getValue())) {
				JOptionPane.showMessageDialog(ConverSet.this, "请选择占用区域!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			List lstType = reportTypeList.getSelectData();
			if (lstType.size() == 0) {
				JOptionPane.showMessageDialog(ConverSet.this, "	请选择报表类型!",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try {
				// 保存基础信息
				getRepsetInfo();

				// 设fb_u_qr_szzb表的report_id
				dsSzzb.edit();
				dsSzzb.beforeFirst();
				while (dsSzzb.next()) {
					dsSzzb.fieldByName(ISzzbSet.REPORT_ID).setValue(
							repSetObject.getREPORT_ID());
				}

				// 保存报表信息
				if (clearFlag) {
					SzzbSetI.getMethod().saveConver(repSetObject, dsSzzb,
							repSetObject.getREPORT_ID(),
							reportTypeList.getSelectData());
				} else {
					SzzbSetI.getMethod().saveConver(repSetObject, dsSzzb, null,
							lstType);
				}

				dsSzzb.applyUpdate();
				clearFlag = false;

				// 修改或增加节点时，刷新树节点
				defineReport.refreshNodeEdit(repSetObject, lstType, sReportID);

				// 报表类型设为修改
				sReportID = repSetObject.getREPORT_ID();

				JOptionPane.showMessageDialog(ConverSet.this, "保存成功！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this, "保存失败,错误信息:"
						+ e.getMessage(), "提示", JOptionPane.ERROR);
			}
		}
	}

	/**
	 * 关闭按钮点击按钮事件
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			ConverSet.this.dispose();

		}
	}

	/**
	 * 得到repset信息
	 */
	private void getRepsetInfo() throws Exception {

		if (Common.isNullStr(sReportID)) {
			repSetObject.setSET_YEAR(Integer.parseInt(Global.loginYear));
			repSetObject.setTYPE_NO(iTypeNo);
			repSetObject.setREPORT_SOURCE("定制");
			repSetObject.setIS_PASSVERIFY("是");
			repSetObject.setIS_HASBATCH("否");
			repSetObject.setIS_MULTICOND("是");
			repSetObject.setIS_END(1);
			repSetObject.setTYPE_FLAG(IDefineReport.REPORTTYPE_COVER);
			repSetObject.setRG_CODE(Global.getCurrRegion());
			// 报表排序码
			repSetObject.setLVL_ID(SzzbSetI.getMethod().getMaxCode(
					IQrBudget.LVL_ID));
			// 报表ID
			repSetObject.setREPORT_ID(SzzbSetI.getMethod().getMaxCode(
					IQrBudget.REPORT_ID));
		}
		// 报表名称
		repSetObject.setREPORT_CNAME(ftxtTitle.getValue().toString());
		// 占用区域
		repSetObject.setCOLUMN_AREA(ftxtConverArea.getValue().toString());
		// 用户类型
		repSetObject.setDATA_USER(Integer.parseInt(reportUserTypeGrp.getValue()
				.toString()));
		// 是否启用
		repSetObject.setIS_ACTIVE(("false".equals(fchkIsActice.getValue()
				.toString()) ? "否" : "是"));
	}

	/**
	 * 得到封面体信息
	 * 
	 */
	private void getSzzbInfo(DataSet dsSzzb) throws Exception {
		clearFlag = true;
		XMLData aData = new XMLData();// XXL 存储已放入单元格，避免重复放入
		// 重复放入时在复原展示时，会创建多个相同的单元格
		CellSelection cells = ReportUtil.translateToNumber(ftxtConverArea
				.getValue().toString());
		CellElement cell;
		for (int i = cells.getColumn(); i < cells.getColumn()
				+ cells.getColumnSpan(); i++) {
			for (int j = cells.getRow(); j < cells.getRow()
					+ cells.getRowSpan(); j++) {
				cell = jWorkBook.getReport().getCellElement(i, j);
				if (cell == null)
					continue;
				this.addSzzb(cell, aData);
			}
		}
	}

	/**
	 * 增加单元格信息
	 * 
	 * @param cell
	 * @param aData
	 * @throws Exception
	 */
	private void addSzzb(CellElement cell, XMLData aData) throws Exception {
		int iCol = cell.getColumn();
		int iRow = cell.getRow();
		if (aData.containsKey(iCol + "-" + iRow))
			return;
		aData.put(iCol + "-" + iRow, null);
		this.addSzzb(cell);
	}

	private void addSzzb(CellElement cell) throws Exception {
		int iCol = cell.getColumn();
		int iRow = cell.getRow();
		dsSzzb.append();
		dsSzzb.fieldByName(ISzzbSet.SET_YEAR).setValue(Global.loginYear);
		dsSzzb.fieldByName(ISzzbSet.TYPE_NO).setValue(new Integer(iTypeNo));

		dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN).setValue(new Integer(iCol));
		dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMNSPAN).setValue(
				new Integer(cell.getColumnSpan()));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).setValue(new Integer(iRow));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROWSPAN).setValue(
				new Integer(cell.getRowSpan()));
		dsSzzb.fieldByName(ISzzbSet.HEADERBODY_FLAG).setValue(new Integer(2));

		Object oValue = jWorkBook.getReport().getCellValue(cell.getColumn(),
				cell.getRow());
		dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).setValue(oValue);

		dsSzzb.fieldByName(ISzzbSet.FIELD_COLWIDTH).setValue(
				new Double(jWorkBook.getReport().getColumnWidth(iCol)));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROWHEIGHT).setValue(
				new Double(jWorkBook.getReport().getRowHeight(iRow)));

		dsSzzb.fieldByName(ISzzbSet.FIELD_FONT).setValue(
				cell.getStyle().getFRFont().getFontName());
		dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSIZE).setValue(
				new Integer(cell.getStyle().getFRFont().getSize()));
		dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSTYPE).setValue(
				new Integer(cell.getStyle().getFRFont().getStyle()));
		dsSzzb.fieldByName(ISzzbSet.HOR_ALIGNMENT).setValue(
				new Integer(cell.getStyle().getHorizontalAlignment()));
		dsSzzb.fieldByName(ISzzbSet.VER_ALIGNMENT).setValue(
				new Integer(cell.getStyle().getVerticalAlignment()));
		dsSzzb.fieldByName(IQrBudget.RG_CODE).setValue(Global.getCurrRegion());
	}

	/**
	 * 设置参数
	 * 
	 */
	private class FieldAreaActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if ("".equals(ftxtConverArea.getValue())) {
				JOptionPane.showMessageDialog(ConverSet.this, "请选择占用区域!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (jWorkBook.getCellSelection() == null) {
				JOptionPane.showMessageDialog(ConverSet.this, "请选择一个单元格！",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			try {
				CellSelection cell = ReportUtil
						.translateToNumber(ftxtConverArea.getValue().toString());

				int iCol = jWorkBook.getCellSelection().getColumn();
				int iRow = jWorkBook.getCellSelection().getRow();
				int iColSpan = jWorkBook.getCellSelection().getColumnSpan();
				int iRowSpan = jWorkBook.getCellSelection().getRowSpan();
				if ((iCol > (cell.getColumn() + cell.getColumnSpan()))
						|| (iRow > (cell.getRow() + cell.getRowSpan()))) {
					JOptionPane.showMessageDialog(ConverSet.this, "超出设置区域！",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (locateByFilter(dsSzzb, iCol, iRow)) {
						dsSzzb.edit();
						dsSzzb.fieldByName(ISzzbSet.FIELD_PARA).setValue(
								fcbxFieldPara.getValue());
					} else {
						CellElement cellEle = new CellElement(iCol, iRow,
								iColSpan, iRowSpan, "");
						jWorkBook.getReport().addCellElement(cellEle);
						addSzzb(cellEle);
						dsSzzb.fieldByName(ISzzbSet.FIELD_PARA).setValue(
								fcbxFieldPara.getValue());
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this, "设置参数出错,错误信息:"
						+ e.getMessage(), "提示", JOptionPane.ERROR);
			}
		}
	}

	private boolean locateByFilter(DataSet ds, int iCol, int iRow)
			throws Exception {
		if (ds == null)
			return false;
		ds.beforeFirst();
		while (ds.next()) {
			if (ds.fieldByName(ISzzbSet.FIELD_COLUMN).getInteger() == iCol
					&& ds.fieldByName(ISzzbSet.FIELD_ROW).getInteger() == iRow) {
				return true;
			}
		}
		return false;
	}

	private class reportMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int iCol = jWorkBook.getCellSelection().getColumn();
			int iRow = jWorkBook.getCellSelection().getRow();
			try {
				showFieldArea(iCol, iRow);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this,
						"显示单元格参数发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

	private class reportKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int iCol = jWorkBook.getCellSelection().getColumn();
			int iRow = jWorkBook.getCellSelection().getRow();
			if (e.getKeyCode() == KEY_UP && iRow != 0) {
				iRow = iRow - 1;
			}
			if (e.getKeyCode() == KEY_DOWN) {
				iRow = iRow + 1;
			}
			if (e.getKeyCode() == KEY_LEFT && iCol != 0) {
				iCol = iCol - 1;
			}
			if (e.getKeyCode() == KEY_RIGHT) {
				iCol = iCol + 1;
			}
			try {
				showFieldArea(iCol, iRow);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this,
						"显示单元格参数发生错误，错误信息：" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * 显示单元格参数s
	 * 
	 */
	private void showFieldArea(int iCol, int iRow) throws Exception {
		if (locateByFilter(dsSzzb, iCol, iRow)) {
			fcbxFieldPara.setValue(dsSzzb.fieldByName(ISzzbSet.FIELD_PARA)
					.getValue());
		} else {
			fcbxFieldPara.setValue("");
		}
	}

}
