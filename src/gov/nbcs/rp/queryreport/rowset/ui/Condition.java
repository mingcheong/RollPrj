/**
 * @# Condition.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:����������,
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class Condition {

	public static final String FIELD_REPORT_ID = "REPORT_ID";

	public static final String FIELD_ITEM_ID = "ITEM_ID";

	public static final String FIELD_CON_FIELD = "CON_FIELD";

	public static final String FIELD_TYPE = "FIELD_TYPE";// �ֶ�����

	public static final String FIELD_CON_OPER = "CON_OPER";

	public static final String FIELD_CON_VALUE = "CON_VALUE";

	public static final String FIELD_LINE_NUM = "LINE_NUM";

	public static final String TABLE_NAME = "FB_U_QR_ROWSET_CON";

	private String reportID;

	private String itemID;

	private String conField;

	private String conOper;

	private String conValue;

	private String fieldType;

	private int lineNum;

	public String getConField() {
		return conField;
	}

	public void setConField(String conField) {
		this.conField = conField;
	}

	public String getConOper() {
		return conOper;
	}

	public void setConOper(String conOper) {
		this.conOper = conOper;
	}

	public String getConValue() {
		return conValue;
	}

	public void setConValue(String conValue) {
		this.conValue = conValue;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	public Condition() {

	}

	public String getInsertSql(String setYear, String order) throws Exception {
		String sErr = check();
		if (!sErr.equals("")) {
			throw new Exception(sErr);
		}

		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(TABLE_NAME).append("( Set_Year,")
				.append(FIELD_REPORT_ID).append(",").append(FIELD_ITEM_ID)
				.append(",").append(FIELD_CON_FIELD).append(",").append(
						FIELD_CON_VALUE).append(",").append(FIELD_CON_OPER)
				.append(",").append(FIELD_LINE_NUM).append(",").append(
						FIELD_TYPE).append(")");
		sb.append("values(").append(setYear).append(",").append(
				"'" + reportID + "',").append("'" + itemID + "',").append(
				"'" + conField + "',").append("'" + conValue + "',").append(
				"'" + conOper + "',").append(order).append(",'").append(
				fieldType).append("'").append(")");

		return sb.toString();
	}

	public Condition(XMLData aData) {
		if (aData == null)
			return;
		this.reportID = Common.getAStringField(aData, FIELD_REPORT_ID);

		itemID = Common.getAStringField(aData, FIELD_ITEM_ID);

		conField = Common.getAStringField(aData, FIELD_CON_FIELD);

		conOper = Common.getAStringField(aData, FIELD_CON_OPER);
		conValue = Common.getAStringField(aData, FIELD_CON_VALUE);
		fieldType = Common.getAStringField(aData, FIELD_TYPE);

		lineNum = Integer.parseInt(Common
				.getAStringField(aData, FIELD_LINE_NUM) == null ? "0" : Common
				.getAStringField(aData, FIELD_LINE_NUM));

	}

	// ����ǰ�ļ��
	public String check() {
		if (Common.isNullStr(reportID))
			return "δָ������ID";
		if (Common.isNullStr(itemID))
			return "δָ����ID";
		if (Common.isNullStr(conField)) {
			return "δָ�������ֶ�";

		}
		if (Common.isNullStr(conOper)) {
			return "δָ���Ƚ�����";

		}
		if (Common.isNullStr(conValue)) {
			return "δָ���Ƚ�ֵ";
		}
		// ����ֶ������ǿջ�
		if (Common.isNullStr(fieldType)) {
			return "�ֶ������ǿ�!";
		}
		if (fieldType.equals(IDefineReport.INTT_TYPE)
				|| fieldType.equals(IDefineReport.INT_TYPE)
				|| fieldType.equals(IDefineReport.CURRENCY_TYPE)) {
			// ��������������LIKE--��֧��
			// ֵ����������
			try {
				Double.parseDouble(conValue);
			} catch (Exception e) {
				return "�ֶ������������ͣ����Ƚ�ֵ[" + conValue + "]���ǺϷ�����������";
			}

		}

		return "";
	}

	public String getTitle(XMLData aData) {
		if (aData == null)
			return this.conField;
		else
			return transFieldToZH(aData);
	}

	private String transFieldToZH(XMLData aData) {
		if (Common.isNullStr(this.conField))
			return "";

		Object obj = aData.get(conField);
		if (obj != null)
			return obj.toString();

		return "";
	}

	public XMLData getXML() {
		XMLData aData = new XMLData();
		aData.put(FIELD_REPORT_ID, reportID);
		aData.put(FIELD_ITEM_ID, itemID);
		aData.put(FIELD_CON_FIELD, conField);
		aData.put(FIELD_CON_OPER, conOper);
		aData.put(FIELD_CON_VALUE, conValue);
		aData.put(FIELD_LINE_NUM, String.valueOf(lineNum));
		aData.put(FIELD_REPORT_ID, reportID);
		aData.put(FIELD_TYPE, fieldType);
		return aData;

	}

	public String getSelectSql() {
		if ((fieldType.equals(IDefineReport.CHAR_TYPE) || fieldType
				.equals(IDefineReport.DATA_TYPE))
				&& (conOper.indexOf("is") == -1)
				&& !conValue.trim().toLowerCase().equals("null"))
			conValue = "'" + conValue + "'";
		if (conOper != null && conOper.equalsIgnoreCase("in"))
			conValue = "(" + conValue + ")";

		return conField + conOper + conValue;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
}
