/**
 * @# CellFieldInfo.java    <文件名>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkSheet;
import com.fr.report.CellElement;

/**
 * 功能说明:记录一个单元格的设置情况，即取数条件,提供显示和检查功能
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * 版权所有：浙江易桥
 * <P>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <P>
 * 侵权者将受到法律追究。
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-17
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class CellFieldInfo {
	public static final int SOURCETYPE_ZC = 0;// 支出

	public static final int SOURCETYPE_SR = 1;// 收入

	public static final int SOURCETYPE_FOR = 2;// 公式

	public static final int SOURCETYPE_SQL = 3;// SQL语句

	public static final int SOURCETYPE_NULL = -1;// 空值

	public static final String FIELD_ID = "REPORT_ID";

	public static final String FIELD_XH = "XH";

	public static final String FIELD_NAME = "FIELD_NAME";

	public static final String FIELD_PFS = "PFS";

	public static final String FIELD_SOURCE = "T_SOURCE";

	public static final String FIELD_JJ = "ACCT_CODE_JJ";

	public static final String FIELD_INCCODE = "INCTYPE_CODE";

	public static final String FIELD_FORMULA = "FORMULA";

	public static final String FIELD_FOR_LVL = "CAL_FG";

	public static final String FIELD_ZC_TYPE = "ZC_TYPE";

	public static final String FIELD_ACCT = "ACCT_CODE";

	public static final String FIELD_COL = "CELLCOL";

	public static final String FIELD_ROW = "CELLROW";

	public static final String TABLENAME = "FB_U_QR_SZZBCELL";

	public static final String FIELD_SQL = "SQL_STATEMENT";

	public static final String ZCTYPE_BASE = "0";// 基本支出

	public static final String ZCTYPE_BASE2 = "50";// 2基本支出

	public static final String ZCTYPE_PRO = "150";// 项目支出

	public static final String ZCTYPE_ALL = "-1";// 项目支出

	// public static final String FIELD_INCCOL = "INC_COL";

	private String reportID;// 报表 ID

	private int XH;// 序号

	private String fieldName;// 字段名

	private String payoutKind;// 支出项目类别

	private int sourceType = -2;// 数据来源

	private String acctJJ;// 经济科目

	// private String incCol;// 收入列名

	private String incType;// 收入类型

	// private String acctInc;// 收入科目

	private String acctCode;// 功能科目

	private int zcType = -2;// 支出类别

	private int cellCol;// 列

	private int cellRow;// 行

	private String formula = "";

	private int forLvl = 0;// 计算级次

	private String title;

	private JWorkSheet parent;

	private String sqlStatement;

	public static final String FIELD_PAYOUTKIND = "PAYOUT_KIND_CODE";

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
		refreshDisp();
	}

	public String getAcctCode() {
		return acctCode;
	}

	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
		refreshDisp();
	}

	public String getAcctJJ() {
		return acctJJ;

	}

	public void setAcctJJ(String acctJJ) {
		this.acctJJ = acctJJ;
		refreshDisp();
	}

	public String getFieldName() {
		return fieldName;
	}

	// 取得资金来源列以拼写SQL。因为其现在的形式为 field1;field2;field3
	// 要转换成 isnull(field1,0)+isnull(field2,0)....
	public String getFieldNameForSql() {
		String[] arrField = fieldName.split(";");
		StringBuffer sbField = new StringBuffer();
		int iCount = arrField.length;
		for (int i = 0; i < iCount; i++) {
			sbField.append(" isnull(").append(arrField[i]).append(",0)+");
		}
		return sbField.substring(0, sbField.length() - 1);
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		refreshDisp();
	}

	public String getIncType() {
		return incType;
	}

	public void setIncType(String incType) {
		this.incType = incType;
	}

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
		initTitle();
		refreshDisp();
	}

	public int getXH() {
		return XH;
	}

	public void setXH(int xh) {
		XH = xh;
	}

	public int getZcType() {
		return zcType;
	}

	public void setZcType(int zcType) {
		this.zcType = zcType;
	}

	public CellFieldInfo() {

	}

	public CellFieldInfo(JWorkSheet parent) {
		this.parent = parent;
	}

	public CellFieldInfo(XMLData aData, JWorkSheet parent) {
		if (aData == null)
			return;
		this.acctCode = Common.getAStringField(aData, FIELD_ACCT);
		this.incType = Common.getAStringField(aData, FIELD_INCCODE);
		this.acctJJ = Common.getAStringField(aData, FIELD_JJ);
		this.fieldName = Common.getAStringField(aData, FIELD_NAME);
		this.payoutKind = Common.getAStringField(aData, FIELD_PAYOUTKIND);

		this.reportID = Common.getAStringField(aData, FIELD_ID);
		this.sourceType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_SOURCE) == null ? "-2" : Common.getAStringField(aData,
				FIELD_SOURCE));
		this.XH = Integer
				.parseInt(Common.getAStringField(aData, FIELD_XH) == null ? "-2"
						: Common.getAStringField(aData, FIELD_XH));
		this.zcType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_ZC_TYPE) == null ? "-2" : Common.getAStringField(aData,
				FIELD_ZC_TYPE));
		this.cellCol = Integer.parseInt(Common
				.getAStringField(aData, FIELD_COL) == null ? "-2" : Common
				.getAStringField(aData, FIELD_COL));
		this.cellRow = Integer.parseInt(Common
				.getAStringField(aData, FIELD_ROW) == null ? "-2" : Common
				.getAStringField(aData, FIELD_ROW));
		this.formula = Common.getAStringField(aData, FIELD_FORMULA);
		this.forLvl = Integer.parseInt(Common.getAStringField(aData,
				FIELD_FOR_LVL) == null ? "0" : Common.getAStringField(aData,
				FIELD_FOR_LVL));
		this.sqlStatement = Common.getAStringField(aData, FIELD_SQL);

		this.parent = parent;
		refreshDisp();
	}

	public CellFieldInfo(XMLData aData) {
		if (aData == null)
			return;
		this.acctCode = Common.getAStringField(aData, FIELD_ACCT);
		this.incType = Common.getAStringField(aData, FIELD_INCCODE);
		this.acctJJ = Common.getAStringField(aData, FIELD_JJ);
		this.fieldName = Common.getAStringField(aData, FIELD_NAME);
		this.payoutKind = Common.getAStringField(aData, FIELD_PAYOUTKIND);

		this.reportID = Common.getAStringField(aData, FIELD_ID);
		this.sourceType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_SOURCE) == null ? "-2" : Common.getAStringField(aData,
				FIELD_SOURCE));
		this.XH = Integer
				.parseInt(Common.getAStringField(aData, FIELD_XH) == null ? "-2"
						: Common.getAStringField(aData, FIELD_XH));
		this.zcType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_ZC_TYPE) == null ? "-2" : Common.getAStringField(aData,
				FIELD_ZC_TYPE));
		this.cellCol = Integer.parseInt(Common
				.getAStringField(aData, FIELD_COL) == null ? "-2" : Common
				.getAStringField(aData, FIELD_COL));
		this.cellRow = Integer.parseInt(Common
				.getAStringField(aData, FIELD_ROW) == null ? "-2" : Common
				.getAStringField(aData, FIELD_ROW));
		this.formula = Common.getAStringField(aData, FIELD_FORMULA);
		this.forLvl = Integer.parseInt(Common.getAStringField(aData,
				FIELD_FOR_LVL) == null ? "0" : Common.getAStringField(aData,
				FIELD_FOR_LVL));
		this.sqlStatement = Common.getAStringField(aData, FIELD_SQL);
		initTitle();

	}

	public int getCellCol() {
		return cellCol;
	}

	public void setCellCol(int cellCol) {
		this.cellCol = cellCol;
	}

	public int getCellRow() {
		return cellRow;
	}

	public void setCellRow(int cellRow) {
		this.cellRow = cellRow;
	}

	public String getTitle() {
		return title;
	}

	private String initTitle() {
		switch (this.sourceType) {
		case SOURCETYPE_ZC:
			title = "支出";
			break;
		case SOURCETYPE_SR:
			title = "收入";
			break;
		case SOURCETYPE_FOR:
			title = "(公式):" + formula;
			break;
		case SOURCETYPE_SQL:
			title = "SQL语句";
			break;
		case SOURCETYPE_NULL:
			title = "空值";
			break;
		default:
			title = "";
		}

		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void refreshDisp() {
		if (parent == null)
			return;
		CellElement aCell = parent.getReport().getCellElement(
				this.getCellCol(), this.getCellRow());
		Style aStyle = aCell.getStyle();
		FRFont aFont = aStyle.getFRFont();
		aFont = aFont.applyForeground(Color.RED);
		aStyle = aStyle.deriveFRFont(aFont);
		aCell.setStyle(aStyle);
		if (aCell != null) {
			aCell.setValue(initTitle());
			parent.repaint();
		}
	}

	public int getForLvl() {
		return forLvl;
	}

	public void setForLvl(int forLvl) {
		this.forLvl = forLvl;
	}

	public String getInsertSql() throws Exception {
		String sErr = checkCell();
		if (!Common.isNullStr(sErr))
			throw new Exception(sErr);
		if (this.sourceType == SOURCETYPE_NULL)
			return null;
		StringBuffer sbField = new StringBuffer();
		StringBuffer sbValue = new StringBuffer();
		sbField.append(FIELD_ID).append(",").append(FIELD_COL).append(",")
				.append(FIELD_ROW).append(",").append("SET_YEAR").append(",")
				.append(FIELD_SOURCE).append(",");
		sbValue.append("'" + this.reportID + "'").append(",").append(
				this.cellCol).append(",").append(this.cellRow).append(",")
				.append(Global.getSetYear()).append(",").append(
						this.getSourceType()).append(",");
		switch (this.getSourceType()) {
		case SOURCETYPE_ZC: {
			sbField.append(FIELD_NAME).append(",");
			sbValue.append("'" + this.fieldName + "'").append(",");
			if (!Common.isNullStr(this.payoutKind)) {// 支出项目类别
				sbField.append(FIELD_PAYOUTKIND).append(",");
				sbValue.append("'" + this.payoutKind + "'").append(",");
			}
			if (!Common.isNullStr(this.acctJJ)) {// 经济科目
				sbField.append(FIELD_JJ).append(",").append(FIELD_ZC_TYPE)
						.append(",");
				sbValue.append("'" + this.acctJJ + "'").append(",").append(
						this.zcType).append(",");
			} else if (!Common.isNullStr(this.acctCode)) {// 功能科目
				sbField.append(FIELD_ACCT).append(",");
				sbValue.append("'" + this.acctCode + "'").append(",");
			}
			break;

		}
		case SOURCETYPE_SR: {
			sbField.append(FIELD_INCCODE).append(",");
			sbValue.append("'" + this.incType + "'").append(",");
			break;

		}
		case SOURCETYPE_FOR: {
			sbField.append(FIELD_FORMULA).append(",").append(FIELD_FOR_LVL)
					.append(",");
			sbValue.append("'" + this.formula + "'").append(",").append(
					this.forLvl).append(",");
			break;
		}
		case SOURCETYPE_SQL: {
			sbField.append(FIELD_SQL).append(",");
			String sSqlTemp = this.sqlStatement;
			if (sSqlTemp != null)
				sSqlTemp = sSqlTemp.replaceAll("'", "''");
			sbValue.append("'" + sSqlTemp + "'").append(",");
			break;
		}

		}
		StringBuffer sbMain = new StringBuffer();
		sbMain.append(" insert into ").append(TABLENAME).append("(").append(
				sbField.substring(0, sbField.length() - 1)).append(")values(")
				.append(sbValue.substring(0, sbValue.length() - 1)).append(")");
		return sbMain.toString();

	}

	public String checkCell() {
		if (sourceType != SOURCETYPE_ZC && sourceType != SOURCETYPE_SR
				&& sourceType != SOURCETYPE_FOR
				&& sourceType != SOURCETYPE_NULL
				&& sourceType != SOURCETYPE_SQL) {
			postion();
			return "未指定数据源";
		}
		switch (sourceType) {
		case SOURCETYPE_ZC: {
			if (Common.isNullStr(this.fieldName)) {
				postion();
				return "数据来源字段没指定值";
			}
			if (Common.isNullStr(this.acctJJ)
					&& Common.isNullStr(this.acctCode)
					&& Common.isNullStr(this.payoutKind)) {
				postion();
				return "未指定支出分类";
			}
			if (!Common.isNullStr(this.acctJJ)) {
				String sCurType = String.valueOf(this.zcType);
				if (!sCurType.equals(ZCTYPE_BASE)
						&& !sCurType.equals(ZCTYPE_ALL)
						&& !sCurType.equals(ZCTYPE_PRO)) {
					postion();
					return "未指定经济科目的支出类别";
				}
			}

		}
			break;
		case SOURCETYPE_SR: {
			if (Common.isNullStr(this.incType)) {
				postion();
				return "未指定收入项目类别";
			}
		}

			break;
		case SOURCETYPE_FOR: {
			if (Common.isNullStr(this.formula.trim())) {
				postion();

				return "未指定公式内容";
			}
			if (!checkFormula()) {
				postion();
				return "公式内容不合法,公式形如{D1}+{D2}";
			}
		}

			break;
		case SOURCETYPE_SQL: {
			if (Common.isNullStr(Common.nonNullStr(this.sqlStatement).trim())) {
				postion();
				return "没有指定SQL语句";
			}
			String sErr = checkSql(sqlStatement);
			if (!Common.isNullStr(sErr)) {
				postion();
				return sErr;
			}
		}
			break;
		default:
			return "";
		}
		return "";
	}

	private boolean checkFormula() {
		if (Common.isNullStr(this.formula.trim()))
			return true;
		String[] arrFormula = this.formula.split("[+-\\/\\*]");
		int iCount = arrFormula.length;
		Pattern p = Pattern.compile("[{][a-zA-Z]{1,3}[0-9]{1,3}[}]");

		for (int i = 0; i < iCount; i++) {
			Matcher m = p.matcher(arrFormula[i]);
			if (!m.matches())
				return false;
		}
		return true;

	}

	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	private String checkSql(String Sql) {
		String sTemp = Common.nonNullStr(Sql).toLowerCase();
		if (sTemp.indexOf("drop ") != -1 || sTemp.indexOf("delete ") != -1
				|| sTemp.indexOf("truncate ") != -1
				|| (sTemp.indexOf("alter ") != -1)
				|| sTemp.indexOf("update ") != -1
				|| sTemp.indexOf("insert ") != -1) {
			return "存在不合法的关键字！";
		}
		if (sTemp.indexOf("select ") == -1 || sTemp.indexOf(" from ") == -1) {
			return "查询语句缺少关键字";
		}
		return "";
	}

	public JWorkSheet getParent() {
		return parent;
	}

	public void setParent(JWorkSheet parent) {
		this.parent = parent;
	}

	private void postion() {
		if (this.parent == null)
			return;
		CellSelection cs = new CellSelection(this.cellCol, this.cellRow);
		parent.setCellSelection(cs);
	}

	public Object clone() {
		CellFieldInfo aCellFieldInfo = new CellFieldInfo();
		aCellFieldInfo.reportID = this.reportID;
		aCellFieldInfo.XH = this.XH;
		aCellFieldInfo.fieldName = this.fieldName;
		aCellFieldInfo.sourceType = this.sourceType;
		aCellFieldInfo.acctJJ = this.acctJJ;
		aCellFieldInfo.incType = this.incType;
		aCellFieldInfo.acctCode = this.acctCode;
		aCellFieldInfo.zcType = this.zcType;
		aCellFieldInfo.cellCol = this.cellCol;
		aCellFieldInfo.cellRow = this.cellRow;
		aCellFieldInfo.formula = this.formula;
		aCellFieldInfo.forLvl = this.forLvl;
		aCellFieldInfo.title = this.title;
		aCellFieldInfo.parent = this.parent;
		aCellFieldInfo.sqlStatement = this.sqlStatement;
		aCellFieldInfo.payoutKind = this.payoutKind;
		return aCellFieldInfo;

	}

	public String getPayoutKind() {
		return payoutKind;
	}

	public void setPayoutKind(String payoutKind) {
		this.payoutKind = payoutKind;
	}

	// 取得资金来源列以拼写SQL。因为其现在的形式为 field1;field2;field3
	// 要转换成 isnull(field1,0)+isnull(field2,0)....
	public String getPayoutKindForSql() {
		String[] arrField = payoutKind.split(";");
		StringBuffer sbField = new StringBuffer();
		int iCount = arrField.length;
		for (int i = 0; i < iCount; i++) {
			sbField.append("'").append(arrField[i]).append("',");
		}
		return sbField.substring(0, sbField.length() - 1);
	}
}
