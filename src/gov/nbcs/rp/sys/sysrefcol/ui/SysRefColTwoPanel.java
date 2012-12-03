package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.PaperSize;

/**
 * 引用列列内容设置(第二步)
 * 

 * 
 */
public class SysRefColTwoPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private int iOperationType;

	// 保存表头信息(字段名，字段类型)
	TableColumnInfo tableColumnInfo[];

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	// 存放第一步填写的sql查询出的数据集
	private DataSet dsQuery;

	private Report reportQuery;

	private ReportUI reportUI;

	// 当前列信息
	private FPanel fpnlFieldInfo;

	// 该列隐藏
	private FCheckBox fchkFieldHide;

	// 列英文名
	private FTextField ftxtfFieldEName;

	// 列中文名
	private FTextField ftxtfFieldCName;

	// 数据类型
	private FRadioGroup frdoFieldType;

	// 对应保留列
	private TableComboBox fcbxReservedField;

	// 显示风格
	private FComboBox fcbxDisplayFormat;

	// 列性质
	private FRadioGroup frdoFieldFlag;

	private FPanel fpnlFieldCodeTypes;

	// 代码显示风格
	private SysRefCodeRule fpnlCodeRule;

	// 显示末级层次
	private FCheckBox fchkShowLastLevel;

	private final ISysRefCol sysRefColServ = SysRefColI
			.getMethod();

	private Vector vectorInc = new Vector();

	private Vector vectorNumeric = new Vector();

	private Vector vectorDate = new Vector();

	private Object[][] objectInc;

	private Object[][] objectNumeric;

	private Object[][] objectVarchar;

	private Object[][] objectDate;

	private final String header[] = { "中文名", "字段名" };

	private final String INC = "inc";

	private final String NUMERIC = "numeric";

	private final String VARCHAR = "varchar";

	private final String DATE = "date";

	final private String FIELDCODE = "FIELDCODE";

	final private String FIELDNAME = "FIELDNAME";

	final private String LVL_ID = "LVL_ID";

	private int iCurCol;

	private int iCurRow;

	private String sCurEname;

	public SysRefColTwoPanel() {
		try {
			this.setTopInset(3);
			this.setBottomInset(3);
			this.setLeftInset(3);
			this.setRightInset(3);
			this.setLayout(new RowPreferedLayout(1));
			reportQuery = new Report();

			reportUI = new ReportUI(reportQuery);
			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			reportUI.getGrid().addMouseListener(new ReportUIMouseListener());
			reportUI.getGrid().addKeyListener(new ReportUIKeyListener());

			reportUI.getGridColumn().addMouseListener(
					new ReportUIMouseListener());
			reportUI.getGridColumn().addKeyListener(new ReportUIKeyListener());

			// 当前列信息Panel
			fpnlFieldInfo = new FPanel();
			fpnlFieldInfo.setLayout(new RowPreferedLayout(6));
			fpnlFieldInfo.setTitle("当前列信息");
			{
				fchkFieldHide = new FCheckBox("该列隐藏");
				fchkFieldHide.setTitlePosition("RIGHT");
				fchkFieldHide
						.addValueChangeListener(new FieldHideValueChangeListener());
				ftxtfFieldEName = new FTextField("列英文名：");
				ftxtfFieldEName.setProportion(0.3f);
				ftxtfFieldCName = new FTextField("列中文名：");
				ftxtfFieldCName.setProportion(0.24f);
				((JTextField) ftxtfFieldCName.getEditor()).getDocument()
						.addDocumentListener(new CNameDocumentListener());

				// 对应保留列
				FPanel fpnlReservedField = new FPanel();
				fpnlReservedField.setLayout(new RowPreferedLayout(4));
				FLabel flblReservedField = new FLabel();
				flblReservedField.setTitle("对应保留列：");
				fcbxReservedField = new TableComboBox();
				fpnlReservedField.addControl(flblReservedField,
						new TableConstraints(1, 1, true, true));
				fpnlReservedField.add(fcbxReservedField, new TableConstraints(
						1, 3, true, true));

				fcbxDisplayFormat = new FComboBox("显示风格：");
				fcbxDisplayFormat.setProportion(0.26f);

				// 数据类型Panel
				FPanel fpnlFieldType = new FPanel();
				fpnlFieldType.setLayout(new RowPreferedLayout(1));
				fpnlFieldType.setTitle("数据类型");
				frdoFieldType = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoFieldType.setTitleVisible(false);
				frdoFieldType.setRefModel(INC + "#整数     +" + NUMERIC
						+ "#浮点数     +" + VARCHAR + "#字符型     +" + DATE
						+ "#日期型     ");
				frdoFieldType
						.addValueChangeListener(new FieldTypeValueChangeListener());
				fpnlFieldType.addControl(frdoFieldType, new TableConstraints(1,
						1, true, true));

				// 列性质 Panel
				FPanel fpnlFieldFlag = new FPanel();
				fpnlFieldFlag.setLayout(new RowPreferedLayout(1));
				fpnlFieldFlag.setTitle("列性质");
				frdoFieldFlag = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoFieldFlag
						.setRefModel("1#代码列      +2#名称列      +3#其他列      ");
				frdoFieldFlag.setTitleVisible(false);
				frdoFieldFlag
						.addValueChangeListener(new FieldFlagValueChangeListener());

				fpnlFieldFlag.addControl(frdoFieldFlag, new TableConstraints(1,
						1, true, true));

				// 代码风格
				fpnlFieldCodeTypes = new FPanel();
				RowPreferedLayout rLayoutCodeType = new RowPreferedLayout(1);
				rLayoutCodeType.setColumnGap(1);
				fpnlFieldCodeTypes.setLayout(rLayoutCodeType);
				fpnlFieldCodeTypes.setTitle(" 代码风格 ");
				fpnlCodeRule = new SysRefCodeRule();
				fchkShowLastLevel = new FCheckBox("将末级编码作为汇总行");
				fchkShowLastLevel.setTitlePosition("RIGHT");
				fpnlFieldCodeTypes.addControl(fpnlCodeRule,
						new TableConstraints(2, 1, false, true));
				// 将末级编码作为汇总行
				fpnlFieldCodeTypes.addControl(fchkShowLastLevel,
						new TableConstraints(1, 1, false, true));

				// 该列隐藏
				fpnlFieldInfo.addControl(fchkFieldHide, new TableConstraints(1,
						1, false, true));
				// 列英文名
				fpnlFieldInfo.addControl(ftxtfFieldEName, new TableConstraints(
						1, 2, false, true));
				// 列性质 Panel
				fpnlFieldInfo.addControl(fpnlFieldFlag, new TableConstraints(2,
						3, false, true));
				// 列中文名
				fpnlFieldInfo.addControl(ftxtfFieldCName, new TableConstraints(
						1, 3, false, true));

				// 数据类型Panel
				fpnlFieldInfo.addControl(fpnlFieldType, new TableConstraints(2,
						3, false, true));
				// 代码风格
				fpnlFieldInfo.addControl(fpnlFieldCodeTypes,
						new TableConstraints(4, 3, false, true));

				// 对应保留列
				fpnlFieldInfo.add(fpnlReservedField, new TableConstraints(1, 3,
						false, true));

				// 显示风格
				fpnlFieldInfo.addControl(fcbxDisplayFormat,
						new TableConstraints(1, 3, false, true));

			}
			// 上部的Table
			this.add(reportUI, new TableConstraints(1, 1, true, true));
			// 当前列信息Panel
			this.addControl(fpnlFieldInfo, new TableConstraints(7, 1, false,
					true));

			((JCheckBox) fchkFieldHide.getEditor()).setSelected(true);

			displayFormat();
			dispalyReserved();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "引用列列属性信息界面初始化发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	private class FieldHideValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (Common.estimate(fchkFieldHide.getValue())) {
				setAllControlEnabledFalse(true);
			} else {
				setAllControlEnabledTrue();
			}

		}
	}

	/**
	 * 设置所有控件可用
	 * 
	 */
	private void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(fpnlFieldInfo, true);
		ftxtfFieldEName.setEditable(false);
		fcbxReservedField.setEnabled(true);
		if (frdoFieldFlag.getValue() == null)
			frdoFieldFlag.setValue("3");
		switch (Integer.parseInt(frdoFieldFlag.getValue().toString())) {
		case 1:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, true);
			fpnlCodeRule.getJspChoiceLen().setEnabled(true);
			frdoFieldType.setEditable(false);
			break;
		case 2:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
			fpnlCodeRule.getJspChoiceLen().setEnabled(false);
			frdoFieldType.setEditable(false);
			break;
		case 3:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
			fpnlCodeRule.getJspChoiceLen().setEnabled(false);
			frdoFieldType.setEditable(true);
			break;
		}
		if (frdoFieldType.getValue() == null)
			frdoFieldType.setValue(VARCHAR);
		if (INC.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		} else if (NUMERIC.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		} else if (VARCHAR.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(false);
		} else if (DATE.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		}
		((SysRefCodeRule) fpnlCodeRule).getFtxtCodeRule().setEditable(false);
		ftxtfFieldEName.setValue(sCurEname);
	}

	/**
	 * 设置所有控件不可用
	 * 
	 * @param bShowFieldHide,设置"该列隐藏"控件是否可以编辑，true:可编辑，
	 *            false：不可编辑
	 */
	private void setAllControlEnabledFalse(boolean bShowFieldHide) {
		Common.changeChildControlsEditMode(fpnlFieldInfo, false);
		fpnlCodeRule.getJspChoiceLen().setEnabled(false);
		fcbxReservedField.setEnabled(false);
		if (bShowFieldHide)
			fchkFieldHide.setEditable(true);
		else
			fchkFieldHide.setEditable(false);
	}

	private class FieldTypeValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (frdoFieldType.getValue() == null) {
				frdoFieldType.setValue(VARCHAR);
			}
			if (INC.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorInc);
				fcbxReservedField.setModel(objectInc, header);
			} else if (NUMERIC.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorNumeric);
				fcbxReservedField.setModel(objectNumeric, header);
			} else if (VARCHAR.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(false);
				fcbxReservedField.setModel(objectVarchar, header);
			} else if (DATE.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorDate);
				fcbxReservedField.setModel(objectDate, header);
			}
			fcbxDisplayFormat.setValue("");
			fcbxReservedField.setValue("");
		}
	}

	private class FieldFlagValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (frdoFieldFlag.getValue() == null)
				frdoFieldFlag.setValue("3");
			switch (Integer.parseInt(frdoFieldFlag.getValue().toString())) {
			case 1:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, true);
				fpnlCodeRule.getJspChoiceLen().setEnabled(true);
				frdoFieldType.setEditable(false);
				frdoFieldType.setValue(VARCHAR);
				((SysRefCodeRule) fpnlCodeRule).getFtxtCodeRule().setEditable(
						false);
				break;
			case 2:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
				fpnlCodeRule.getJspChoiceLen().setEnabled(false);
				fpnlCodeRule.getFtxtCodeRule().setValue("");
				frdoFieldType.setEditable(false);
				frdoFieldType.setValue(VARCHAR);
				((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
				break;
			case 3:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
				fpnlCodeRule.getJspChoiceLen().setEnabled(false);
				fpnlCodeRule.getFtxtCodeRule().setValue("");
				frdoFieldType.setEditable(true);
				((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
				break;
			}
		}
	}

	/**
	 * 显示格式
	 * 
	 * @throws Exception
	 */
	private void displayFormat() throws Exception {
		DataSet getDisplayFormat = sysRefColServ
				.getDisplayFormat(Global.loginYear);
		vectorInc.addElement("");
		vectorNumeric.addElement("");
		vectorDate.addElement("");
		getDisplayFormat.beforeFirst();
		while (getDisplayFormat.next()) {
			String sCVuale = getDisplayFormat.fieldByName("CValue").getString();
			String sName = getDisplayFormat.fieldByName("Name").getString();
			if ("整数数值".equals(sCVuale)) {
				vectorInc.addElement(sName);
			}
			if ("带小数的数值".equals(sCVuale)) {
				vectorNumeric.addElement(sName);
			}
			if ("日期".equals(sCVuale)) {
				vectorDate.addElement(sName);
			}
		}
	}

	private void dispalyReserved() throws Exception {
		DataSet getDisplayFormat = sysRefColServ
				.getReservedField(Global.loginYear);
		DataSet getDisplayFormatTemp;
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='" + INC
				+ "'");
		objectInc = setObjectValue(getDisplayFormatTemp, objectInc);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='"
				+ NUMERIC + "'");
		objectNumeric = setObjectValue(getDisplayFormatTemp, objectNumeric);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='"
				+ VARCHAR + "'");
		objectVarchar = setObjectValue(getDisplayFormatTemp, objectVarchar);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='" + DATE
				+ "'");
		objectDate = setObjectValue(getDisplayFormatTemp, objectDate);

	}

	private Object[][] setObjectValue(DataSet dataSet, Object[][] object)
			throws Exception {
		object = new Object[dataSet.getRecordCount() + 1][2];
		object[0][0] = "";
		object[0][1] = "";
		int i = 1;
		dataSet.beforeFirst();
		while (dataSet.next()) {
			object[i][0] = dataSet.fieldByName("FIELD_CNAME").getString();
			object[i][1] = dataSet.fieldByName("FIELD_ENAME").getString();
			i++;
		}
		return object;
	}

	/**
	 * 虚拟表头DataSet
	 * 
	 * @return
	 * @throws Exception
	 */
	private DataSet getHeaderDataSet() throws Exception {
		DataSet ds = DataSet.create();
		String sFieldName;
		String sSqlDet = dsRefCol.fieldByName("SQL_DET").getString();
		sSqlDet = SysRefColPub.ReplaceRefColFixFlag(sSqlDet.toUpperCase());
		tableColumnInfo = sysRefColServ.getFieldInfo(sSqlDet);
		for (int i = 0; i < tableColumnInfo.length; i++) {
			sFieldName = tableColumnInfo[i].getColumnName().toUpperCase();
			ds.append();
			ds.fieldByName(FIELDCODE).setValue(sFieldName);
			ds.fieldByName(FIELDNAME).setValue(sFieldName);
			ds.fieldByName(LVL_ID).setValue(
					Common.getStrID(new BigDecimal(i + 1), 4));
		}
		// 判断字段是否发生改变，删除明细表和关联源表中已不存在的记录，
		List lstDelBookmark = new ArrayList();
		String sBookmark = dsRefColDetail.toogleBookmark();
		dsRefColDetail.beforeFirst();
		while (dsRefColDetail.next()) {
			//modify by qzc 2009.6.5
			String sEname = dsRefColDetail.fieldByName("FIELD_ENAME")
					.getString().toUpperCase();
			if (!ds.locate(FIELDCODE, sEname)) {
				JOptionPane.showMessageDialog(this, "丢失字段:"
						+ dsRefColDetail.fieldByName("field_cname").getString()
						+ "(" + sEname + ")!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				lstDelBookmark.add(dsRefColDetail.toogleBookmark());
			} else {
				ds.fieldByName(FIELDNAME).setValue(
						dsRefColDetail.fieldByName("FIELD_CNAME").getString());
			}
		}
		for (int i = 0; i < lstDelBookmark.size(); i++) {
			dsRefColDetail.gotoBookmark(lstDelBookmark.get(i).toString());
			dsRefColDetail.delete();
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		return ds;
	}

	/**
	 * 传入第一个页填写的sql语句查询出的记录
	 * 
	 * @param dsQuery
	 */
	public void setDsQuery(DataSet dsQuery) {
		this.dsQuery = dsQuery;
	}

	/**
	 * 显示报表信息
	 * 
	 * @throws Exception
	 */
	private void setReport() throws Exception {
		DataSet dsHeader = getHeaderDataSet();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsHeader, FIELDCODE, FIELDNAME, "", LVL_ID);
		TableHeader tableHeader = new TableHeader(node);
		tableHeader.setFont(this.getFont());
		reportQuery.removeAllCellElements();
		reportQuery.setReportHeader(tableHeader);
		reportQuery.setBodyData(dsQuery);
		reportQuery.refreshBody();
		reportUI.repaint();
		// 设表头列宽
		reportQuery.getReportHeader().setColor(UntPub.HEADER_COLOR);
		for (int i = 0; i < reportQuery.getReportHeader().getColumns(); i++) {
			reportQuery.setColumnWidth(i, 100);
		}
	}

	/**
	 * 变换列,判断列信息填写是正确，不正确不允许变换列，正确：保存或删除列信息，显示新选择列的信息
	 * 
	 * @throws Exception
	 * 
	 */
	private void changeCol() throws Exception {
		// 判断选中的是否是同一列
		if (iCurCol == reportQuery.getSelectedCell().getColumn())
			return;
		InfoPackage infoPackage = checkFillInfo();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(SysRefColTwoPanel.this, infoPackage
					.getsMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
			CellSelection cs = new CellSelection(iCurCol, iCurRow);
			reportUI.setCellSelection(cs);
			return;
		}
		// 保存当前单元格信息
		operateRefColDetailToDs();
		// 保当新选择的单元格信息
		saveCurCellInfo();
		showDetailInfo(sCurEname);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class ReportUIMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				changeCol();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColTwoPanel.this,
						"显示引用列内容发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private class ReportUIKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			try {
				changeCol();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColTwoPanel.this,
						"显示引用列内容发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	};

	/**
	 * 显示记录信息,根据Dataset内容
	 * 
	 * @throws Exception
	 * 
	 */
	public void showInfo(DataSet dsRefCol, DataSet dsRefColDetail,
			int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		this.dsRefCol = dsRefCol;
		this.dsRefColDetail = dsRefColDetail;
		// 显示报表信息
		setReport();
		// 保存当前单元格信息
		saveCurCellInfo();
		// 显示当前选中列的明细信息
		showDetailInfo(sCurEname);
	}

	/**
	 * 根据ename的值定位dsRefColDetail(引用列明细信息表)显示当前记录信息
	 * 
	 * @param sFieldEname
	 * @throws Exception
	 * @return,true:定位到记录，false,明细记录不存在
	 */
	private void showDetailInfo(String sFieldEname) throws Exception {
		if (!dsRefColDetail.locate("FIELD_ENAME", sFieldEname)) {
			// 明细记录不存在,设置该列隐藏为true,清空显示信息
			((JCheckBox) fchkFieldHide.getEditor()).setSelected(true);
			// 清空显示的信息
			ftxtfFieldEName.setValue("");
			// 列中文名
			ftxtfFieldCName.setValue("");
			// 数据类型
			frdoFieldType.setValue("");
			// 对应保留列
			fcbxReservedField.setValue("");
			// 显示风格
			fcbxDisplayFormat.setValue("");
			// 列性质
			frdoFieldFlag.setValue("");
			// 代码显示风格
			fpnlCodeRule.getFtxtCodeRule().setValue("");
			// 显示末级层次
			((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
			// 显示所有控件不可用
			setAllControlEnabledFalse(true);
		} else {
			// 明细记录存在,显示明细记录
			// 该列隐藏
			((JCheckBox) fchkFieldHide.getEditor()).setSelected(false);
			// 列英文名
			ftxtfFieldEName.setValue(dsRefColDetail.fieldByName("FIELD_ENAME")
					.getString());
			// 列中文名
			ftxtfFieldCName.setValue(dsRefColDetail.fieldByName("FIELD_CNAME")
					.getString());
			// 数据类型
			frdoFieldType.setValue(dsRefColDetail.fieldByName("FIELD_TYPE")
					.getString());
			// 对应保留列
			fcbxReservedField.setValue(dsRefColDetail.fieldByName(
					"RESERVED_NAME").getString());
			// 显示风格
			fcbxDisplayFormat.setValue(dsRefColDetail.fieldByName(
					"DISPLAY_FORMAT").getString());
			// 列性质
			frdoFieldFlag.setValue(dsRefColDetail.fieldByName("FIELD_FLAG")
					.getString());
			// 代码显示风格
			fpnlCodeRule.getFtxtCodeRule().setValue(
					dsRefColDetail.fieldByName("FIELD_CODE_STYLE").getString());
			// 显示末级层次
			((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(Common
					.estimate(new Integer(dsRefColDetail.fieldByName(
							"Show_Last_Level").getInteger())));
			// 显示所有控件可用
			if (iOperationType != SysRefColOperation.rcoView)
				setAllControlEnabledTrue();
		}
		if (iOperationType == SysRefColOperation.rcoView)
			setAllControlEnabledFalse(false);
	}

	/**
	 * 点击一下按钮时判断是否信息填写完整
	 * 
	 * @throws Exception
	 * 
	 */
	public InfoPackage nextOperate() throws Exception {
		InfoPackage infoPackage = checkFillInfo();
		// 保存当前单元格信息
		if (!infoPackage.getSuccess()) {
			return infoPackage;
		}
		operateRefColDetailToDs();
		infoPackage = checkFillCodeAndName();
		if (!infoPackage.getSuccess()) {
			return infoPackage;
		}
		return infoPackage;
	}

	/**
	 * 判断填写的信息是否完整和正确
	 * 
	 */
	private InfoPackage checkFillInfo() {
		InfoPackage infoPackage = new InfoPackage();
		if (Common.estimate(fchkFieldHide.getValue())) {
			infoPackage.setSuccess(true);
			return infoPackage;
		}
		if ("".equals(ftxtfFieldCName.getValue().toString())) {
			ftxtfFieldCName.setFocus();
			infoPackage.setsMessage("请输入中文名！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if ("1".equals(frdoFieldFlag.getValue().toString())
				&& "".equals(fpnlCodeRule.getFtxtCodeRule().getValue()
						.toString())) {
			fpnlCodeRule.getFtxtCodeRule().setFocus();
			infoPackage.setsMessage("请设置代码风格！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 操作明细信息，操作dataset
	 * 
	 * @throws Exception
	 * 
	 */
	private void operateRefColDetailToDs() throws Exception {
		if (Common.estimate(fchkFieldHide.getValue())) {
			if (dsRefColDetail.locate("field_ename", sCurEname))
				dsRefColDetail.delete();
			// 设置表头显示的信息为ename值
			reportQuery.setCellValue(iCurCol, 0, sCurEname);
		} else {
			if (dsRefColDetail.locate("field_ename", sCurEname)) {
				dsRefColDetail.edit();
				saveRefColDetailtoDs("mod");
			} else {
				dsRefColDetail.append();
				saveRefColDetailtoDs("add");
			}
		}
	}

	private void saveRefColDetailtoDs(String sType) throws Exception {
		if (sType == "add") {
			String sRefColId = dsRefCol.fieldByName("refcol_id").getValue()
					.toString();
			dsRefColDetail.fieldByName("refcol_id").setValue(sRefColId);
			dsRefColDetail.fieldByName("field_id").setValue(
					sRefColId + getMaxCode());
		}
		dsRefColDetail.fieldByName("field_cname").setValue(
				ftxtfFieldCName.getValue().toString());
		dsRefColDetail.fieldByName("field_ename").setValue(
				ftxtfFieldEName.getValue().toString());
		if (fcbxReservedField.getValue() == null)
			dsRefColDetail.fieldByName("reserved_name").setValue("");
		else
			dsRefColDetail.fieldByName("reserved_name").setValue(
					fcbxReservedField.getValue().toString());

		dsRefColDetail.fieldByName("field_flag").setValue(
				frdoFieldFlag.getValue().toString());
		dsRefColDetail.fieldByName("field_type").setValue(
				frdoFieldType.getValue());
		dsRefColDetail.fieldByName("field_width").setValue(
				new Double(reportQuery.getColumnWidth(iCurCol)));
		//modify by qzc 2009.6.5
		dsRefColDetail.fieldByName("display_format").setValue(
				Common.nonNullStr(fcbxDisplayFormat.getValue()));
		if ("1".equals(frdoFieldFlag.getValue().toString())) {
			dsRefColDetail.fieldByName("field_code_style").setValue(
					fpnlCodeRule.getFtxtCodeRule().getValue());
		} else {
			dsRefColDetail.fieldByName("field_code_style").setValue("");
		}
		dsRefColDetail.fieldByName("show_last_level")
				.setValue(
						Boolean.getBoolean(fchkShowLastLevel.getValue()
								.toString()) ? "1" : "0");

		dsRefColDetail.fieldByName("set_year").setValue(Global.loginYear);
		dsRefColDetail.fieldByName(ISysRefCol.RG_CODE).setValue(
				Global.getCurrRegion());
	}

	private String getMaxCode() throws Exception {
		int iMax = 0;
		String sBookmark = dsRefColDetail.toogleBookmark();
		dsRefColDetail.beforeFirst();
		while (dsRefColDetail.next()) {
			if (dsRefColDetail.fieldByName("field_id").getValue() != null) {
				int iMaxTmp = Integer.parseInt(dsRefColDetail.fieldByName(
						"field_id").getString().substring(4));
				if (iMax < iMaxTmp)
					iMax = iMaxTmp;
			}
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		iMax++;
		return Common.getStrID(new BigDecimal(iMax), 3);
	}

	/**
	 * 保存当前选中单元格信息
	 * 
	 */
	private void saveCurCellInfo() {
		iCurCol = reportQuery.getSelectedCell().getColumn();
		iCurRow = reportQuery.getSelectedCell().getRow();
		sCurEname = tableColumnInfo[iCurCol].getColumnName().toString();
	}

	private class CNameDocumentListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			if (!"".equals(ftxtfFieldCName.getValue().toString().trim())) {
				reportQuery
						.setCellValue(iCurCol, 0, ftxtfFieldCName.getValue());
			} else {
				reportQuery.setCellValue(iCurCol, 0, sCurEname);
			}
			reportUI.repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}

	}

	public TableColumnInfo[] getTableColumnInfo() {
		return tableColumnInfo;
	}

	/**
	 * 判断是否设置代码列和名称列
	 * 
	 * @return
	 * @throws Exception
	 */
	private InfoPackage checkFillCodeAndName() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sBookmark = dsRefColDetail.toogleBookmark();
		if (!dsRefColDetail.locate("field_flag", "1")) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("请设置代码列！");
			return infoPackage;
		}
		if (!dsRefColDetail.locate("field_flag", "2")) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("请设置名称列！");
			return infoPackage;
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		infoPackage.setSuccess(true);
		return infoPackage;
	}
}
