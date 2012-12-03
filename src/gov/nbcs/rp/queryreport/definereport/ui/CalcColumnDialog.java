/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.FormulaTool;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.action.SearchCalcCol;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.summary.constants.ColumnConstants;
import com.foundercy.pf.reportcy.summary.iface.IDataSourceManager;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.AbstractFormula;
import com.foundercy.pf.reportcy.summary.object.cellvalue.MeasureAttr;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.cell.CellSelection;
import com.fr.dialog.BaseDialog;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;


public class CalcColumnDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	public final static String OPER_SUM = "sum";

	public final static String OPER_AVG = "avg";

	public final static String OPER_COUNT = "count";

	public final static String OPER_NULLIF = "nullif";

	private JFrame frame = null;

	private CustomTree datasChoicTre;

	private MyCalculateValueImpl curCell;

	private DataSet dsDataSource;

	private CustomTextArea ftxtaFormula;

	private XMLData xmlDsFieldToName;

	private XMLData xmlDsNameToField;

	private FComboBox fcbxCol;// 列

	private FTextField fcbxRow;// 行

	private CellElement cellElement;

	private FCheckBox fchkVisible;// 列可视性

	// 是否向上求和
	private FCheckBox fchkIsSumUp = null;

	// 定义排序单选框
	private FCheckBox fchkOrder = null;

	private ReportQuerySource querySource;

	private GroupReport groupReport;

	private ColWhereSetPnl colWhereSetPnl;

	public CalcColumnDialog(Dialog dialog) {
		super(dialog);
		this.setSize(600, 650);
		this.setTitle("计算列");
		this.setModal(true);

		init();
	}

	public CalcColumnDialog(JFrame frame) {
		super(frame);
		this.frame = frame;
		this.setSize(600, 650);
		this.setTitle("计算列");
		this.setModal(true);
		this.dsDataSource = ((ReportGuideUI) frame).getDataSource();
		if ((DataSet) this.dsDataSource != null) {
			this.dsDataSource = (DataSet) this.dsDataSource.clone();
		}
		this.querySource = ((ReportGuideUI) frame).querySource;
		this.groupReport = ((ReportGuideUI) frame).fpnlDefineReport.groupReport;
		List arrList = DefineReportI.getMethod().getDataSourceRefString(
				getDataSourceIDs(((ReportGuideUI) frame).querySource));
		if (arrList != null) {
			xmlDsFieldToName = (XMLData) arrList.get(0);
			xmlDsNameToField = (XMLData) arrList.get(1);
		}
		// TODO列行数的显示
		init();
	}

	private void init() {

		// 定义信息Label
		FLabel flblFormual = new FLabel();
		flblFormual.setText("=");
		// 定义公式多行文本框
		ftxtaFormula = new CustomTextArea();
		ftxtaFormula.setLineWrap(true);
		JScrollPane fsPaneFormula = new JScrollPane(ftxtaFormula);

		// ftxtaFormula.setTitleVisible(false);
		// 定义检查合法性按钮
		FButton fbtnCheck = new FButton("fbtnCheck", "有效性检查");
		fbtnCheck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String sErr = check();
				if (!Common.isNullStr(sErr)) {
					new MessageBox(frame, sErr, MessageBox.MESSAGE,
							MessageBox.BUTTON_OK).show();
					return;
				}
				new MessageBox(frame, "校验通过!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();

			}

		});
		FFlowLayoutPanel fpnlCheck = new FFlowLayoutPanel();
		fpnlCheck.addControl(fbtnCheck);

		// 定义公式面板
		FPanel fpnlFormula = new FPanel();
		fpnlFormula.setTitle("请在下面的文本框里面输入公式");
		RowPreferedLayout rLay = new RowPreferedLayout(6);
		rLay.setColumnWidth(10);
		fpnlFormula.setLayout(rLay);
		fpnlFormula.addControl(flblFormual, new TableConstraints(1, 1, true,
				false));
		fpnlFormula.add(fsPaneFormula, new TableConstraints(1, 1, true, true));
		fpnlFormula.addControl(fpnlCheck, new TableConstraints(1, 4, true,
				false));

		// 函数列
		FList flstFunc = new FList(new Object[] {
				new FuncListObject("sum(", "加和"),
				// new FuncListObject("avg(", "平均"),
				new FuncListObject("count(", "计数") });
		flstFunc.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					Object obj = ((FList) e.getSource()).getSelectedValue();
					if (obj == null)
						return;
					ftxtaFormula.setValue(((FuncListObject) obj).getName());
				}

			}
		});

		FScrollPane fspnlFunc = new FScrollPane(flstFunc);
		// 定义函数面板
		FPanel fpnlFunc = new FPanel();
		fpnlFunc.setTitle("函数");
		fpnlFunc.setLayout(new RowPreferedLayout(1));
		fpnlFunc.addControl(fspnlFunc, new TableConstraints(1, 1, true, true));

		// 数据源树
		datasChoicTre = null;
		try {
			datasChoicTre = new CustomDragTree("数据源", dsDataSource, "chr_id",
					"FIELD_FNAME", "parent_id", null, "chr_id");
		} catch (Exception e) {

			e.printStackTrace();
			new MessageBox(frame, "刷新数据源出错!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
		}
		datasChoicTre.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					MyTreeNode aNode = ((CustomTree) e.getSource())
							.getSelectedNode();
					if (aNode == null || !aNode.isLeaf())
						return;
					try {
						// String sDsFieldType = dsDataSource.fieldByName(
						// IDefineReport.FIELD_TYPE).getString();
						// DefinePub definePub = new DefinePub();
						// sDsFieldType = definePub
						// .getFieldTypeWithCh(sDsFieldType);
						// if (IDefineReport.CHAR_Val.equals(sDsFieldType)) {
						// new MessageBox(frame, "此字段是字符型，不可以添加!",
						// MessageBox.MESSAGE, MessageBox.BUTTON_OK)
						// .show();
						// return;
						// }

						String sDsEname = dsDataSource.fieldByName(
								IDefineReport.DICID).getString();

						String sDsName = colWhereSetPnl.dataSourceCbx
								.getRefModel().getNameByValue(sDsEname);
						ftxtaFormula.setValue("{"
								+ sDsName
								+ "."
								+ dsDataSource.fieldByName(
										IDefineReport.FIELD_FNAME).getString()
								+ "}");
					} catch (Exception e1) {

						e1.printStackTrace();
					}
				}
			}
		});
		FScrollPane fspnlDatasChoic = new FScrollPane(datasChoicTre);
		// 定义数据源面板
		FPanel fpnlDataSource = new FPanel();
		fpnlDataSource.setTitle("数据源");
		fpnlDataSource.setLayout(new RowPreferedLayout(1));
		fpnlDataSource.addControl(fspnlDatasChoic, new TableConstraints(1, 1,
				true, true));

		// 定义单元格
		fcbxCol = new FComboBox("列:");
		fcbxCol.setTitleVisible(false);
		fcbxCol.setRefModel(getColRefString());
		fcbxRow = new FTextField();
		fcbxRow.setTitleVisible(false);
		fcbxRow.setEditable(false);
		fcbxRow.setValue(String.valueOf(DefinePub.getOpeRow(groupReport) + 1));
		FButton fbtnCell = new FButton("ddd", "添加");
		fbtnCell.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addCol();
			}

		});
		FPanel fpnlCell = new FPanel();
		fpnlCell.setTitle("单元格");
		fpnlCell.setLayout(new RowPreferedLayout(7));
		fpnlCell.addControl(fcbxCol, new TableConstraints(1, 2, true, true));
		fpnlCell.addControl(fcbxRow, new TableConstraints(1, 2, true, true));
		fpnlCell.addControl(fbtnCell, new TableConstraints(1, 3, true, true));

		// 定义操作符按钮
		FButton fbtnAdd = new FButton("fbtnAdd", "+");
		fbtnAdd.addActionListener(new BtnClick());
		FButton fbtnDec = new FButton("fbtnDec", "-");
		fbtnDec.addActionListener(new BtnClick());
		FButton fbtnMul = new FButton("fbtnMul", "*");
		fbtnMul.addActionListener(new BtnClick());
		FButton fbtnDiv = new FButton("fbtnDiv", "/");
		fbtnDiv.addActionListener(new BtnClick());
		FButton fbtnLeftBrack = new FButton("fbtnLeftBrack", "(");
		fbtnLeftBrack.addActionListener(new BtnClick());
		FButton fbtnRightBrack = new FButton("fbtnLeftBrack", ")");
		fbtnRightBrack.addActionListener(new BtnClick());
		// 定义操作符面板
		FFlowLayoutPanel fpnlOpea = new FFlowLayoutPanel();
		fpnlOpea.addControl(fbtnAdd, new TableConstraints(1, 1, false, false));
		fpnlOpea.addControl(fbtnDec, new TableConstraints(1, 1, false, false));
		fpnlOpea.addControl(fbtnMul, new TableConstraints(1, 1, false, false));
		fpnlOpea.addControl(fbtnDiv, new TableConstraints(1, 1, false, false));
		fpnlOpea.addControl(fbtnLeftBrack, new TableConstraints(1, 1, false,
				false));
		fpnlOpea.addControl(fbtnRightBrack, new TableConstraints(1, 1, false,
				false));

		fchkVisible = new FCheckBox("隐藏列");
		fchkVisible.setTitlePosition("right");
		// 是否求和
		fchkIsSumUp = new FCheckBox("向上求和");
		fchkIsSumUp.setTitlePosition("RIGHT");

		// 作为排序列
		fchkOrder = new FCheckBox("作为排序列");
		fchkOrder.setTitlePosition("RIGHT");

		// fpnlOpea.add(cbxVisible);

		FPanel fpnlCellOpea = new FPanel();
		fpnlCellOpea.setLayout(new RowPreferedLayout(1));
		fpnlCellOpea.addControl(fpnlCell, new TableConstraints(2, 1, false,
				true));
		fpnlCellOpea.addControl(fpnlOpea,
				new TableConstraints(1, 1, true, true));
		fpnlCellOpea.addControl(fchkVisible, new TableConstraints(1, 1, false,
				true));
		fpnlCellOpea.addControl(fchkIsSumUp, new TableConstraints(1, 1, false,
				true));
		fpnlCellOpea.addControl(fchkOrder, new TableConstraints(1, 1, false,
				true));

		// 定义列条件面板
		colWhereSetPnl = new ColWhereSetPnl(this);
		colWhereSetPnl.setTitle("列过滤条件设置");

		// 定义按钮面板
		BtnPanel btnPanel = new BtnPanel();

		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(6));
		fpnlMain.addControl(fpnlFormula,
				new TableConstraints(5, 6, false, true));
		fpnlMain.addControl(fpnlFunc, new TableConstraints(1, 1, true, true));
		fpnlMain.addControl(fpnlDataSource, new TableConstraints(1, 3, true,
				true));
		fpnlMain.addControl(fpnlCellOpea,
				new TableConstraints(1, 2, true, true));
		fpnlMain.addControl(colWhereSetPnl, new TableConstraints(13, 6, false,
				true));
		fpnlMain.addControl(btnPanel, new TableConstraints(1, 6, false, true));

		this.getContentPane().add(fpnlMain);

	}

	/**
	 * 定义按钮类
	 */
	private class BtnPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public BtnPanel() {
			// 设置靠右显示
			// this.setAlignment(FlowLayout.RIGHT);
			RowPreferedLayout rLay = new RowPreferedLayout(3);
			rLay.setColumnWidth(70);
			rLay.setColumnGap(1);
			this.setLayout(rLay);

			// 定义“确定”按钮
			FButton okBtn = new FButton("saveBtn", "保存");
			okBtn.addActionListener(new SaveActionListener());
			// 定义”取消“按钮
			FButton cancelBtn = new FButton("cancelBtn", "取 消");
			// 实现“取消”按钮点击事件
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					CalcColumnDialog.this.dispose();
				}

			});

			this.addControl(new FLabel(),
					new TableConstraints(1, 1, true, true));
			// “确定”按钮加入按钮面板
			this.addControl(okBtn, new TableConstraints(1, 1, true, false));
			// “取消”按钮加入按钮面板
			this.addControl(cancelBtn, new TableConstraints(1, 1, true, false));
		}
	}

	private void refreshData() {
		if (this.curCell == null)
			return;
		if (!Common.isNullStr(curCell.toString()))
			this.ftxtaFormula.setText(curCell.toString().substring(1));
		this.fchkVisible.setValue(new Boolean(curCell.getIsVisual() == Integer
				.parseInt(IDefineReport.FLASE_NUM)));
		this.fchkIsSumUp.setValue(new Boolean(Common.estimate(curCell
				.getIsSumUp())));
		if (!Common.isNullStr(curCell.getOrderIndex())) {
			this.fchkOrder.setValue(new Boolean(true));
		}
	}

	void populate(CellElement cellElement, Object value) {
		this.cellElement = cellElement;
		if (value == null || !(value instanceof MyCalculateValueImpl))
			this.curCell = new MyCalculateValueImpl(null);
		else
			this.curCell = (MyCalculateValueImpl) value;

		colWhereSetPnl.refreshData();
		refreshData();
	}

	public static String getDataSourceIDs(ReportQuerySource querySource) {
		IDataSourceManager dsManager = querySource.getDataSourceManager();
		if (dsManager == null)
			return null;
		IDataSource[] ds = dsManager.getDataSourceArray();
		if (ds == null)
			return null;
		String sSource = "";
		for (int i = 0; i < ds.length; i++) {
			sSource = sSource + "'" + ds[i].getSourceID() + "',";
		}
		return sSource.substring(0, sSource.length() - 1);
	}

	// 简单检查设置信息正确
	public String check() {
		// 检查括号的对应情况
		boolean hasField = false;// 记录是否存在字段，因为有字段就不可以有单元格
		String formula = (String) ftxtaFormula.getText();

		if (Common.isNullStr(formula.trim()))
			return "公式未设置,请设置公式！";
		formula = formula.toLowerCase();

		if (formula.indexOf("count(*)") != -1) {
			return "请在count中设置一个字段！";
		}
		int iLeft = StringUtils.countMatches(formula, "{");
		int iRight = StringUtils.countMatches(formula, "}");
		if (iLeft != iRight)
			return "大括号数量不匹配";
		iLeft = StringUtils.countMatches(formula, "(");
		iRight = StringUtils.countMatches(formula, ")");
		if (iLeft != iRight)
			return "小括号数量不匹配";
		// 操作符的匹配
		// formula = StringUtils.remove(formula, " ");
		int iOper = StringUtils.countMatches(formula, "+")
				+ StringUtils.countMatches(formula, "-")
				+ StringUtils.countMatches(formula, "*")
				+ StringUtils.countMatches(formula, "/");
		String[] arrEle = formula.split("[\\+\\-\\*/]");
		Arrays.sort(arrEle);
		if (arrEle != null && arrEle.length > 0 && Common.isNullStr(arrEle[0])) {
			return "操作符数量与元素个数不匹配";
		}
		if (arrEle.length != (iOper + 1))
			return "操作符数量与元素个数不匹配";
		// 检查字段的正确性
		int iBegin = formula.indexOf("{");
		// 有条件时就必须有字段
		if (iBegin == -1 && colWhereSetPnl.getWhere() != null
				&& colWhereSetPnl.getWhere().length > 0) {
			return "已设置了条件，但没有添加统计字段!";
		}
		// if (iBegin != -1
		// && (colWhereSetPnl.getWhere() == null || colWhereSetPnl
		// .getWhere().length == 0)) {
		// return "已添加了统计字段，但没有设置条件!";
		// }
		int iEnd = formula.indexOf("}");
		String fieldType;
		while (iBegin != -1) {
			hasField = true;
			String subFormula = formula.substring(iBegin + 1, iEnd);
			String eName = (String) xmlDsNameToField.get(subFormula);
			if (Common.isNullStr(eName))
				return "没有找到字段对应的数据源信息!字段名为：" + subFormula;

			fieldType = eName.split(",")[2];
			if (IDefineReport.INT_TYPE.equals(fieldType)
					|| IDefineReport.INTT_TYPE.equals(fieldType)
					|| IDefineReport.CURRENCY_TYPE.equals(fieldType)
					|| IDefineReport.FLOAT_TYPE.equals(fieldType)) {
				formula = StringUtils.replace(formula, "{" + subFormula + "}",
						"FIELD_NUM");
			} else {
				formula = StringUtils.replace(formula, "{" + subFormula + "}",
						"FIELD_STR");
			}

			iBegin = formula.indexOf("{");
			iEnd = formula.indexOf("}");
		}
		// 检查其它
		String sErr = checkFunction(formula, hasField);
		if (!Common.isNullStr(sErr))
			return sErr;
		if (Common.estimate(fchkOrder.getValue())) {
			// 只可以整数作为排序列，浮点型不可以作为排序列
			if (!hasField) {
				List lstCells = DefinePubOther
						.getCellsWithOutClone(groupReport);
				try {
					if (this.checkFloat((String) ftxtaFormula.getText(),
							lstCells)) {
						return "目前不支持浮点数排序!";
					}
				} catch (Exception e) {
					return e.getMessage();
				}

			} else {
				if (checkFloatFormula((String) ftxtaFormula.getText())) {
					return "目前不支持浮点数排序!";
				}
			}
		}
		if (!hasField) {
			// 检查单元格公式是否死循环
			int row = DefinePub.getOpeRow(groupReport);
			if (!checkCirle(ftxtaFormula.getText(), ReportUtil
					.translateToColumnName(cellElement.getColumn())
					+ (row + 1), row, cellElement.getColumn()))
				return "公式出现死循环，请检查！";

		}

		return "";

	}

	private boolean checkFloatFormula(String formula) {
		// 检查字段的正确性
		int iBegin = formula.indexOf("{");
		int iEnd = formula.indexOf("}");
		int intNumber = 0;
		while (iBegin != -1) {
			String subFormula = formula.substring(iBegin + 1, iEnd)
					.toLowerCase();
			String eName = (String) xmlDsNameToField.get(subFormula);
			String[] arrField = eName.split(",");
			formula = StringUtils.replace(formula, "{" + subFormula + "}",
					"FIELD");
			iBegin = formula.indexOf("{");
			iEnd = formula.indexOf("}");

			if (intNumber == 0) {
				intNumber = DefinePub.checkFieldFloat(arrField[2]);
				if (intNumber == 1)
					return true;
			}

		}
		return false;
	}

	/**
	 * 检查公式中的字段是否正确
	 * <P>
	 * 思路:有字段存在，替换后要是一具正确常量表达式 计数时可以不含字段
	 */

	public static String isAvalidate(String subStr, boolean isCount) {
		if (subStr == null || subStr.trim().equals(""))
			return "没有字段";

		if (!isCount) {
			if (subStr.indexOf("FIELD_NUM") == -1)
				return "没有数值型字段";
			if (subStr.indexOf("FIELD_STR") != -1)
				return "包含字符型字段";
		} else {
			if (subStr.indexOf("FIELD_NUM") != -1)
				return "包含非字符型字段";
		}
		subStr = subStr.replaceAll("FIELD_STR", "99");
		subStr = subStr.replaceAll("FIELD_NUM", "99");

		if (!"*".equals(subStr))
			try {
				FormulaTool.getValue(subStr, null);
			} catch (Exception e) {
				return "公式有误";
			}
		return "";

	}

	/**
	 * 检查函数的正确性
	 * 
	 * @param formula
	 * @return hasField 是否存在字段，如果有字段则不可以有单元格(目前)
	 */
	private String checkFunction(String formula, boolean hasField) {
		// 如果此公式不含功能函数则为非法的
		formula = formula.replaceAll(" ", "");
		if (colWhereSetPnl.getWhere() != null
				&& colWhereSetPnl.getWhere().length > 0
				&& formula.indexOf("sum(") == -1
				&& formula.indexOf("count(") == -1
				&& formula.indexOf("avg(") == -1) {
			return "不含有操作函数";
		}
		// 每一个字段都必须有操作函数
		if (!checkPreOper(formula))
			return "字段必须有一个操作函数，且要包含在最靠近的操作函数里";

		String sErr;

		// 加和
		int iBegin = formula.indexOf("sum(");
		int iEnd;
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 4, iEnd);
			sErr = isAvalidate(innerPar, false);
			if (!Common.isNullStr(sErr))
				return "累加函数中" + sErr;

			formula = formula.substring(0, iBegin) + "99"
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("sum(");
		}
		// 计数
		iBegin = formula.indexOf("count(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 6, iEnd);
			sErr = isAvalidate(innerPar, true);
			if (!Common.isNullStr(sErr))
				return "计数函数中" + sErr;
			formula = formula.substring(0, iBegin) + "99"
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("count(");

		}
		// 平均
		iBegin = formula.indexOf("avg(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 4, iEnd);
			sErr = isAvalidate(innerPar, false);
			if (!Common.isNullStr(sErr))
				return "平均函数中" + sErr;
			formula = formula.substring(0, iBegin) + "99"
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("avg(");

		}

		// mod by ymq ceiling -->ceil
		iBegin = formula.indexOf("ceil(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);

			String innerPar = formula.substring(iBegin + 5, iEnd);
			sErr = isAvalidate_A(innerPar, false);
			if (!Common.isNullStr(sErr))
				return "Ceil函数中" + sErr;

			formula = formula.substring(0, iBegin)
					+ formula.substring(iBegin + 5, iEnd)
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("ceil(");
		}
		// round
		iBegin = formula.indexOf("round(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 6, iEnd);
			sErr = isAvalidate_A(innerPar, true);
			if (!Common.isNullStr(sErr))
				return "四舍五入函数中" + sErr;
			int iComma = formula.indexOf(",", iBegin);

			formula = formula.substring(0, iBegin)
					+ formula.substring(iBegin + 6, iComma)
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("round(");
		}
		// isnull
		iBegin = formula.indexOf("isnull(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 6, iEnd);
			sErr = isAvalidate_A(innerPar, true);
			if (!Common.isNullStr(sErr))
				return "isnull函数中" + sErr;

			int iComma = formula.indexOf(",", iBegin);
			formula = formula.substring(0, iBegin)
					+ formula.substring(iBegin + 6, iComma)
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("isnull(");
		}
		// nullif
		iBegin = formula.indexOf("nullif(");
		while (iBegin != -1) {
			iEnd = formula.indexOf(")", iBegin);
			String innerPar = formula.substring(iBegin + 7, iEnd);
			sErr = isAvalidate_A(innerPar, true);
			if (!Common.isNullStr(sErr))
				return "nullif函数中" + sErr;

			int iComma = formula.indexOf(",", iBegin);
			formula = formula.substring(0, iBegin)
					+ formula.substring(iBegin + 7, iComma)
					+ formula.substring(iEnd + 1);
			iBegin = formula.indexOf("nullif(");

		}
		// 去掉回车
		formula = formula.replaceAll("\n", "");
		// 小括号配对
		// 思路，找到最后一个左括号，和此括号后的右括号，括号内的内容不可以为空，且一定会找到
		iBegin = formula.lastIndexOf("(");
		iEnd = formula.indexOf(")", iBegin);
		while (iBegin != -1) {
			if (iEnd == -1)
				return "存在小括号不匹配";
			// 右括号前的第一个应该是个操作符
			if (iBegin != 0) {
				// mod by ymq (iBegin-1,iBegin)
				String subOper = formula.substring(iBegin, iBegin + 1);
				if (!Pattern.matches("[\\+\\-\\*/\\(]", subOper))
					return "小括号前没有操作符";
			}
			String subStr = formula.substring(iBegin + 1, iEnd);
			if (Common.isNullStr(subStr))
				return "小括号内存在空的内容";
			formula = formula.substring(0, iBegin) + subStr
					+ formula.substring(iEnd + 1);
			iBegin = formula.lastIndexOf("(");
			iEnd = formula.indexOf(")", iBegin);
		}
		// 替换后的公式中，应该只剩下列格之间的计算
		String[] arrStr = formula.split("[\\+\\-\\*/]");
		Pattern pt;
		if (!hasField)
			pt = Pattern.compile("[A-Za-z]{0,2}[0-9.]{1,9}");
		else
			pt = Pattern.compile("[0-9.]{1,9}");

		for (int i = 0; i < arrStr.length; i++) {
			if (!pt.matcher(arrStr[i]).matches())
				return "公式不正确,可能原因是：存在了字段的公式不再可以有单元格参与计算";
		}
		return "";
	}

	/**
	 * 检查单元格公式是否死循环
	 * 
	 * @param cellName
	 * @return false:
	 */
	private boolean checkCirle(String sFormula, String curFormula, int row,
			int curCol) {
		String[] arrStr = sFormula.split("[\\+\\-\\*/(),]");
		MyCalculateValueImpl calculateValueImpl;
		CellSelection cell;

		for (int i = 0; i < arrStr.length; i++) {
			if (!SearchCalcCol.check(arrStr[i]))
				continue;

			cell = ReportUtil.translateToNumber(arrStr[i].toUpperCase() + ":"
					+ arrStr[i].toUpperCase());
			// 得到列号
			int col = cell.getColumn();
			if (col == curCol) {
				return false;
			}

			Object value = groupReport.getCellValue(col, row);
			if (value instanceof MyCalculateValueImpl) {
				calculateValueImpl = (MyCalculateValueImpl) value;
				if (DefinePub.checkIsCellFormula(calculateValueImpl)) {

					if (!checkCirle(calculateValueImpl.getFormula()
							.getContent().substring(1), curFormula, row, curCol))
						return false;
				}
			}

			sFormula = SearchCalcCol.replace(arrStr[i], ReportUtil
					.translateToColumnName(col)
					+ row, sFormula);
		}
		return true;
	}

	public String save() {
		String formula = ftxtaFormula.getText().toLowerCase();
		if (Common.isNullStr(formula)) {
			cellElement.setValue(null);
			return "";
		}
		// 解析字符
		int iBegin, iEnd;
		iBegin = formula.indexOf("{");
		iEnd = formula.indexOf("}");
		List lstFunction = new ArrayList();
		MyCalculateValueImpl aFun = new MyCalculateValueImpl("="
				+ ftxtaFormula.getText());

		int intNumber = 0;
		while (iBegin != -1) {

			String subFormula = formula.substring(iBegin + 1, iEnd)
					.toLowerCase();
			String eName = (String) xmlDsNameToField.get(subFormula);
			if (Common.isNullStr(eName))
				return "没有找到字段对应的数据源信息!字段名为：" + subFormula;
			String[] arrField = eName.split(",");
			String aUUID = DefinePub.getRandomUUID();
			MeasureAttr aMeasure = new MeasureAttr();
			aMeasure.setMeasureID(aUUID);
			aMeasure.setSourceColID(arrField[1]);
			aMeasure.setSourceID(arrField[0]);
			aMeasure.setColType(DefinePub.getFieldTypeWithCh(arrField[2]));
			String mesureType = getForwardFirstMatch(formula.substring(0,
					iBegin));
			if (Common.isNullStr(mesureType))
				return "字段" + subFormula + "要有计量方式!";
			aMeasure.setGroupType(mesureType);
			formula = StringUtils.replace(formula, "{" + subFormula + "}",
					aUUID);
			lstFunction.add(aMeasure);
			iBegin = formula.indexOf("{");
			iEnd = formula.indexOf("}");

			if (intNumber == 0) {
				intNumber = DefinePub.checkFieldFloat(arrField[2]);
			}
		}

		MeasureAttr[] aMeasure = new MeasureAttr[lstFunction.size()];
		for (int i = 0; i < aMeasure.length; i++) {
			aMeasure[i] = (MeasureAttr) lstFunction.get(i);
		}
		aFun.setMeasureArray(aMeasure);

		AbstractFormula aformula = new AbstractFormula();
		aformula.setContent(formula);
		aFun.setFormula(aformula);
		// 添加过滤条件
		aFun.setStatisticCaliberArray(colWhereSetPnl.getWhere());
		aFun.setColumnType(ColumnConstants.UITYPE_1_CALCULATE);
		aFun.setIsVisual(Integer.parseInt(((Boolean) this.fchkVisible
				.getValue()).booleanValue() ? IDefineReport.FLASE_NUM
				: IDefineReport.TRUE_NUM));
		// 向上求和
		aFun
				.setIsSumUp(Common.estimate(this.fchkIsSumUp.getValue()
						.toString()) ? IDefineReport.TRUE_NUM
						: IDefineReport.FLASE_NUM);
		// 是否作为排序列
		if (Common.estimate(fchkOrder.getValue())) {
			if (Common.isNullStr(aFun.getOrderIndex())) {
				aFun.setOrderIndex(DefinePub.getOrderIndex(groupReport));
				aFun.setOrderType(IDefineReport.ASC_FLAG);
			}
		} else {
			aFun.setOrderIndex("");
			aFun.setOrderType("");
		}

		if (intNumber == 0) {
			intNumber = checkCountIntNumber(formula);
		}
		aFun.setIntNumber(intNumber);
		curCell = aFun;
		cellElement.setValue(curCell);

		return "";
	}

	/**
	 * 分组表count()/100默认应是浮点型，而不是整型
	 * 
	 * @param formula
	 * @return
	 */
	private int checkCountIntNumber(String formula) {
		if (formula.toLowerCase().indexOf(OPER_COUNT + "(") != -1
				&& formula.toLowerCase().indexOf("/") != -1) {
			return 1;
		}
		return 0;
	}

	private String getForwardFirstMatch(String strPre) {
		int iSum = getMaxMatch(strPre, OPER_SUM);
		int iCount = getMaxMatch(strPre, OPER_COUNT);
		int iAvg = getMaxMatch(strPre, OPER_AVG);

		int iMax = Math.max(iSum, Math.max(iCount, iAvg));
		if (iMax == -1)
			return "";
		if (iSum == iMax)
			return OPER_SUM;
		if (iCount == iMax)
			return OPER_COUNT;

		return OPER_AVG;

	}

	/**
	 * 添加行列格
	 * 
	 */
	public void addCol() {
		String sCol = (String) fcbxCol.getValue();
		if (Common.isNullStr(sCol)) {
			new MessageBox(frame, "请先选择列信息!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
		String sRow = (String) fcbxRow.getValue();
		if (Common.isNullStr(sRow)) {
			new MessageBox(frame, "请先选择行信息!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
		// 判断增加的是否是计算列
		int curCol = ReportUtil.translateToNumber(
				sCol + sRow + ":" + sCol + sRow).getColumn();
		Object obj = groupReport.getCellValue(curCol,
				Integer.parseInt(sRow) - 1);
		if (!(obj instanceof MyCalculateValueImpl)) {
			new MessageBox(frame, "该列不是计算列，不允许参与计算,请选择其他列!",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			return;
		}

		ftxtaFormula.setValue(sCol + sRow);
	}

	private int getMaxMatch(String strPre, String oper) {
		if (Common.isNullStr(strPre))
			return -1;
		String pre = strPre.toLowerCase();
		int iMax = (-oper.length());
		while (true) {
			int iPos = pre.indexOf(oper, iMax + oper.length());
			iMax = Math.max(iMax, iPos);
			if (iPos == -1)
				return iMax;
		}

	}

	class FuncListObject {

		private String name;

		private String sCHName;

		public FuncListObject(String name, String sCHName) {
			this.name = name;
			this.sCHName = sCHName;
		}

		public String toString() {
			return sCHName;
		}

		public String getName() {
			return name;
		}
	}

	class BtnClick implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ("/".equals(((FButton) e.getSource()).getTitle())) {
				ftxtaFormula.setValue(((FButton) e.getSource()).getTitle()
						+ "nullif(,0)", 8);
			} else {
				ftxtaFormula.setValue(((FButton) e.getSource()).getTitle());
			}
		}
	}

	class SaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String sErr = check();
			if (!sErr.equals("")) {
				new MessageBox(frame, sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			sErr = save();
			if (!sErr.equals("")) {
				new MessageBox(frame, sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			// 设置单元格格式
			cellElement.getStyle().deriveHorizontalAlignment(Constants.RIGHT);

			// 校验单位格操作列IntNumber值
			try {
				checkOutCellIntNumber();
			} catch (Exception e1) {
				new MessageBox(frame, e1.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				return;
			}

			// 刷新显示条件区信息
			refreshFilter();

			// 刷新界面显示
			((ReportGuideUI) frame).fpnlDefineReport.designGroupReportPane
					.fireReportDataChanged();

			// 定义单元格高亮条件，目的：当单元格值为零时不显示
			// HighlightGroup highlightGroup = new CustomHighlightGroup();
			// cellElement.setHighlightGroup(highlightGroup);
			CalcColumnDialog.this.dispose();
		}
	}

	/**
	 * 校验单位格操作列IntNumber值
	 * 
	 * @throws Exception
	 * 
	 */
	private void checkOutCellIntNumber() throws Exception {
		List lstCells = DefinePubOther.getCellsWithOutClone(groupReport);
		int size = lstCells.size();
		int intNumber;
		for (int i = 0; i < size; i++) {
			Object value = lstCells.get(i);
			if (!(value instanceof MyCalculateValueImpl))
				continue;
			if (DefinePub.checkIsCellFormula((MyCalculateValueImpl) value)) {
				if (checkFloat(((MyCalculateValueImpl) value).getFormula()
						.getContent().substring(1), lstCells)) {
					intNumber = 1;
				} else {
					intNumber = 0;
				}

				((MyCalculateValueImpl) value).setIntNumber(intNumber);
			}
		}
	}

	/**
	 * 检查每个字段必在操作符的括号内 目前只支持在最近的操作符的括号内
	 * 
	 * @param formula
	 * @return
	 */
	private boolean checkAPreOper(String formula, int iBegin) {
		String preStr = formula.substring(0, iBegin);
		int iSum = getMaxMatch(preStr, OPER_SUM);
		int iCount = getMaxMatch(preStr, OPER_COUNT);
		int iAvg = getMaxMatch(preStr, OPER_AVG);

		int iMax = Math.max(iSum, Math.max(iCount, iAvg));
		// String operName;
		if (iMax == -1)
			return false;
		// if (iSum == iMax)
		// operName = OPER_SUM;
		// else if (iCount == iMax)
		// operName = OPER_COUNT;
		// else
		// operName = OPER_AVG;
		if (formula.indexOf(")", iMax) < iBegin + 3) {
			return false;
		}
		return true;

		// 取得

	}

	private boolean checkPreOper(String formula) {
		int iBegin = 0;
		iBegin = formula.indexOf("FIELD", iBegin);
		while (iBegin != -1) {
			if (!checkAPreOper(formula, iBegin))
				return false;
			iBegin = formula.indexOf("FIELD", iBegin + 3);
		}
		return true;
	}

	// 生成列选择字串
	private String getColRefString() {
		int iColCount = ((ReportGuideUI) frame).getReport().getReport()
				.getColumnCount();
		String sRefString = "";
		String sColName;
		for (int i = 0; i < iColCount; i++) {
			sColName = ReportUtil.translateToColumnName(i);
			sRefString = sRefString + "+" + sColName + "#" + sColName;
		}
		return sRefString;
	}

	/**
	 * 刷新显示条件区信息
	 */
	private void refreshFilter() {
		String sFilter = DefinePub.getFilter(querySource, curCell
				.getStatisticCaliberArray());
		int col = cellElement.getColumn();
		int row = cellElement.getRow() + 1;

		CellElement cell;
		if (groupReport.getCellElement(col, row) == null) {
			cell = new CellElement(col, row);
			cell.setValue(sFilter);
			groupReport.addCellElement(cell, true);
		} else {
			cell = groupReport.getCellElement(col, row);
		}
		cell.setValue(sFilter);
	}

	/**
	 * 判断单位元格是整型还是浮点型
	 */
	private boolean checkFloat(String sFormula, List lstCells) throws Exception {

		String[] arrStr = sFormula.split("[\\+\\-\\*/(),]");
		MyCalculateValueImpl calculateValueImpl;
		for (int i = 0; i < arrStr.length; i++) {
			if (!SearchCalcCol.check(arrStr[i]))
				continue;

			CellSelection cell = ReportUtil.translateToNumber(arrStr[i]
					.toUpperCase()
					+ ":" + arrStr[i].toUpperCase());
			// 得到列号
			int col = cell.getColumn();

			Object value = null;
			try {
				value = lstCells.get(col);
			} catch (Exception e) {
				throw new Exception("\"" + arrStr[i] + "\"单元格不存在");
			}
			if (value instanceof MyCalculateValueImpl) {
				calculateValueImpl = (MyCalculateValueImpl) value;
				if (DefinePub.checkIsCellFormula(calculateValueImpl)) {
					if (checkFloat(calculateValueImpl.getFormula().getContent()
							.substring(1), lstCells))
						return true;
				} else {
					if (calculateValueImpl.getIntNumber() == 1)
						return true;
				}
			}

		}

		return false;
	}

	public ReportQuerySource getQuerySource() {
		return querySource;
	}

	public XMLData getXmlDsFieldToName() {
		return xmlDsFieldToName;
	}

	public MyCalculateValueImpl getCurCell() {
		return curCell;
	}

	/**
	 * 检查公式中函数是否设置正确
	 * 
	 * @param subStr
	 * @param isPostfix
	 *            函数是否有后缀值
	 * @return
	 */
	public static String isAvalidate_A(String subStr, boolean isPostfix) {
		if (subStr == null || subStr.trim().equals(""))
			return "没有字段";

		if (isPostfix) {
			if (StringUtils.countMatches(subStr, ",") != 1)
				return "后缀参数设置不正确";
			int index = subStr.indexOf(",");
			if (Common.isNullStr(subStr.substring(index))) {
				return "没有后缀参数值";
			}
			subStr = subStr.substring(0, index);
		}

		if (subStr.indexOf("99") == -1 && !SearchCalcCol.check(subStr)) {
			return "没有字段";
		}

		if (!"*".equals(subStr) && subStr.indexOf("99") != -1)
			try {
				FormulaTool.getValue(subStr, null);
			} catch (Exception e) {
				return "公式有误";
			}
		return "";

	}

}
