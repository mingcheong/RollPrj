/**
 * @# RowInfo.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:行信息
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 
 */
public class RowInfo {

	private List lstListener = new ArrayList();

	// private XMLData aData;

	public static final int MEASURE_SUM = 1;// 求和

	public static final int MEASURE_COUNT = 2;// 计数

	public static final int MEASURE_SQL = 3;// SQL

	public static final String FIELD_REPORT_ID = "REPORT_ID";

	public static final String FIELD_ITEM_ID = "ITEM_ID";

	public static final String FIELD_ITEM = "ITEM";

	public static final String FIELD_VALUE = "VALUE";

	public static final String FIELD_ITEMATTACH = "ITEMATTACH";

	public static final String FIELD_FIELDS = "FIELDS";

	public static final String FIELD_TABLE_NAME = "TABLE_NAME";

	public static final String FIELD_SOURCEID = "SOURCE_ID";

	public static final String FIELD_MEASURE_TYPE = "MEASURE_TYPE";

	public static final String TABLE_NAME = "FB_U_QR_ROWSET";

	public static final String FIELD_SQL = "SEARCH_SQL";

	public static final String PARAM_DIV = "\\$div\\_code";

	public static final String PARAM_SET_YEAR = "\\$set\\_year";

	public static final String PARAM_BATCH = "\\$batch";

	public static final String PARAM_DATA_TYPE = "\\$data\\_type";

	public static final String PARAM_DATA_VER = "\\$data\\_ver";

	public static final String PARAM_REPORT_ID = "\\$report\\_id";

	public static final String PARAM_TABLE = "\\$table-";

	public static final String FIELD_PRECHAR = "Z";// 只可以有一位

	private String reportID;

	private String itemID;

	private String item;

	private String itemAttach;

	private String fields;

	private int measureType;

	private String tableName;

	private String sourceID;

	private List lstCons = new ArrayList();

	private int lineMax = 200;

	private String sql;

	private double value;// 存放值

	Condition reportCon;

	public RowInfo() {

	}

	/**
	 * 查询初始化时用到
	 * 
	 * @param aData主表信息
	 * @param lstCons条件信息
	 */
	public RowInfo(XMLData aData, List lstCons) {
		if (aData == null)
			return;
		this.reportID = Common.getAStringField(aData, FIELD_REPORT_ID);
		this.itemID = Common.getAStringField(aData, FIELD_ITEM_ID);
		this.item = Common.getAStringField(aData, FIELD_ITEM);
		this.sourceID = Common.getAStringField(aData, FIELD_SOURCEID);// 添加了数据源ID
		this.itemAttach = Common.getAStringField(aData, FIELD_ITEMATTACH);
		this.fields = Common.getAStringField(aData, FIELD_FIELDS);
		this.sql = Common.getAStringField(aData, FIELD_SQL);
		this.measureType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_MEASURE_TYPE) == null ? "1" : Common.getAStringField(
				aData, FIELD_MEASURE_TYPE));
		this.tableName = Common.getAStringField(aData, FIELD_TABLE_NAME);
		if (lstCons != null && !lstCons.isEmpty()) {
			int iCount = lstCons.size();
			for (int i = 0; i < iCount; i++) {
				Condition aCon = new Condition((XMLData) lstCons.get(i));
				aCon.setLineNum(lstCons.size() + 1);
				this.lstCons.add(aCon);
			}
		}
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
		fireValuechange(false);

	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
		fireValuechange(true);
	}

	public String getItemAttach() {
		return itemAttach;
	}

	public void setItemAttach(String itemAttach) {
		this.itemAttach = itemAttach;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
		// 如果要修改此则必须将条件的也修改了
		if (!lstCons.isEmpty()) {
			int iCount = lstCons.size();
			for (int i = 0; i < iCount; i++) {
				Condition aCon = (Condition) lstCons.get(i);
				aCon.setItemID(itemID);
			}
		}
	}

	public int getMeasureType() {
		return measureType;
	}

	public void setMeasureType(int measureType) {
		this.measureType = measureType;
		fireValuechange(false);
	}

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
		if (this.lstCons == null)
			return;
		int iCount = lstCons.size();
		for (int i = 0; i < iCount; i++) {
			Condition aCon = (Condition) lstCons.get(i);
			aCon.setReportID(reportID);

		}

	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {

		if (this.tableName != null && this.tableName.equals(tableName))
			return;
		// 修改数据源时，要将条件都清空
		this.tableName = tableName;
		this.lstCons.clear();
		setFields("");
	}

	// 必须传入字段的信息,要根据字段的信息来检查操作类型是否正确
	public String check(XMLData xmlFieldType) {
		if (Common.isNullStr(reportID))
			return "未指定报表ID";
		if (Common.isNullStr(itemID))
			return "未指定行ID";
		if (Common.isNullStr(item))
			return "标题不可以为空";
		if (measureType == MEASURE_SUM && Common.isNullStr(fields))
			return "未指定查询的字段";
		if (measureType != MEASURE_SUM && measureType != MEASURE_COUNT
				&& measureType != MEASURE_SQL)
			return "未指定计量方式";
		if (measureType != MEASURE_SQL
				&& (Common.isNullStr(tableName) || Common.isNullStr(sourceID)))
			return "未指定数据源名";
		// 只有各字段都是数字型的才可以求和
		if (measureType == MEASURE_SUM) {
			String[] arrStr = fields.split(";");
			String fieldType = "";
			for (int i = 0; i < arrStr.length; i++) {
				fieldType = (String) xmlFieldType.get(arrStr[i]);
				if (fieldType == null || fieldType.equals("字符型")
						|| fieldType.equals("日期型"))
					return "求和字段不可以是字符型或日期型!";
			}
		}
		if (!lstCons.isEmpty()) {
			int iCount = lstCons.size();
			for (int i = 0; i < iCount; i++) {
				String sErr = ((Condition) lstCons.get(i)).check();
				if (!sErr.equals(""))
					return sErr;
			}
		}

		return "";

	}

	public String getTitle(XMLData xmlTable, List lstField) {
		String cField = "";
		if (this.measureType == MEASURE_SUM)
			cField = "的字段【" + getFieldToZH(lstField) + "】求和";
		else if (this.measureType == MEASURE_COUNT)
			// cField = "的字段【" + getFieldToZH(xmlField) + "】计数";
			cField = "计数";
		else if (this.measureType == MEASURE_SQL)
			return "SQL语句";
		else
			cField = "未指定计量方式";

		return "对表【" + transTableToZH(xmlTable) + "】" + cField;
	}

	public String transTableToZH(XMLData aData) {
		if (Common.isNullStr(this.sourceID))
			return "";
		Object obj = aData.get(this.sourceID);
		if (obj != null)
			return obj.toString();
		else
			return this.tableName;
	}

	public String getFieldToZH(List lstField) {
		if (Common.isNullStr(this.fields))
			return "";
		if (lstField == null)
			return fields;
		String sTemp = fields;
		String[] arrField = fields.split(";");
		int iCount = arrField.length;
		for (int i = 0; i < iCount; i++) {
			int index = ((List) lstField.get(0)).indexOf(arrField[i]);
			Object obj = ((List) lstField.get(1)).get(index);
			if (obj != null)
				sTemp = StringUtils.replace(sTemp, arrField[i], obj.toString());
		}
		return sTemp;
	}

	public List getInsertSql(String setYear, String sRowID, XMLData xmlFieldType)
			throws Exception {
		resetParam();
		String sErr = check(xmlFieldType);
		if (!sErr.equals(""))
			throw new Exception(sErr);
		String sqlTemp = sql;
		if (sqlTemp != null)
			sqlTemp = sqlTemp.replaceAll("'", "''");// 将引号转义
		StringBuffer sb = new StringBuffer();
		sb.append("Insert into ").append(TABLE_NAME).append("(");
		sb.append("set_year,").append(FIELD_REPORT_ID + ",").append(
				FIELD_ITEM_ID + ",").append(FIELD_ITEM + ",").append(
				FIELD_ITEMATTACH + ",").append(FIELD_FIELDS + ",").append(
				FIELD_TABLE_NAME + ",").append(FIELD_SQL).append(",").append(
				FIELD_MEASURE_TYPE + "," + FIELD_SOURCEID + ")");
		sb.append("values(");
		sb.append(setYear + ",").append("'" + reportID + "',").append(
				"'" + sRowID + "',").append(getStringByQuater(item) + ",")
				.append(getStringByQuater(itemAttach) + ",").append(
						getStringByQuater(fields) + ",").append(
						getStringByQuater(tableName) + ",").append(
						getStringByQuater(sqlTemp) + ",").append(
						measureType + "," + getStringByQuater(sourceID) + ")");
		List lstString = new ArrayList();
		lstString.add(sb.toString());
		int iCount = lstCons.size();

		for (int i = 0; i < iCount; i++) {
			Condition con = (Condition) lstCons.get(i);
			lstString.add(con.getInsertSql(setYear, String.valueOf(i)));
		}
		return lstString;

	}

	/**
	 * 将不相关的字段清空，以免产生脏数据
	 * 
	 */
	private void resetParam() {
		switch (measureType) {
		case MEASURE_SUM:
			sql = null;
			break;
		case MEASURE_COUNT:
			fields = null;
			sql = null;
			break;
		case MEASURE_SQL:
			fields = null;
			lstCons = new ArrayList();
			break;
		}
	}

	public Condition addCons(Condition aCondition) {

		aCondition.setLineNum(lstCons.size() + 1);
		lstCons.add(aCondition);
		return aCondition;

	}

	public void addTitleChangeedListener(ITitleChangedListener arg1) {
		lstListener.add(arg1);
	}

	private void fireValuechange(boolean isItem) {
		if (lstListener.isEmpty())
			return;
		int iCount = lstListener.size();
		for (int i = 0; i < iCount; i++) {
			((ITitleChangedListener) lstListener.get(i)).titleChanged(this,
					isItem);
		}
	}

	/**
	 * 取得TABLE用的条件信息
	 * 
	 * @return
	 */
	public List getConList() {
		List lstData = new ArrayList();
		int iCount = lstCons.size();
		for (int i = 0; i < iCount; i++) {
			Condition aCon = (Condition) lstCons.get(i);
			lstData.add(aCon.getXML());
		}
		return lstData;

	}

	public Condition addDefaultCon() {
		Condition aCon = new Condition();
		aCon.setReportID(reportID);
		aCon.setItemID(itemID);
		aCon.setLineNum(lineMax + 1);
		lineMax = lineMax + 1;
		return this.addCons(aCon);
	}

	// 转换成XML
	public XMLData getXML() {
		XMLData aData = new XMLData();
		aData.put(FIELD_REPORT_ID, reportID);

		aData.put(FIELD_ITEM_ID, itemID);

		aData.put(FIELD_ITEM, item);

		aData.put(FIELD_ITEMATTACH, itemAttach);

		aData.put(FIELD_FIELDS, fields);

		aData.put(FIELD_TABLE_NAME, tableName);

		aData.put(FIELD_MEASURE_TYPE, String.valueOf(measureType));
		return aData;
	}

	/**
	 * 添加一查询的字段
	 * 
	 * @param field
	 * @return
	 */
	public boolean addOperField(String field) {
		if (Common.isNullStr(field))
			return false;
		if (Common.isNullStr(fields)) {
			fields = field;
			fireValuechange(false);
			return true;
		}
		String[] arrField = fields.split(";");// 比较一下，不可以重复
		for (int i = 0; i < arrField.length; i++) {
			if (field.equals(arrField[i]))
				return false;
		}
		this.fields = this.fields + ";" + field;
		fireValuechange(false);
		return true;

	}

	public boolean deleteAField(String field) {
		if (Common.isNullStr(field) || Common.isNullStr(fields))
			return false;
		String temp = ";" + fields + ";";
		temp = StringUtils.replace(temp, ";" + field, "");
		if (temp.equals(";")) {
			this.fields = "";
			fireValuechange(false);
			return true;
		}
		this.fields = temp.substring(1, temp.length() - 1);
		fireValuechange(false);
		return true;

	}

	public List getLstCons() {
		return lstCons;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	private String getStringByQuater(String str) {
		if (Common.isNullStr(str) || str.equals("null"))
			return "null";
		return "'" + str + "'";
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	// 取得查询时的语句
	public String getSearchSql() {
		// 注：原来以为表名在数据字典中是唯一的，但自字义表不符合条件，故添加了数据源ID，以标识唯一
		// 可以重复定义的表只有几个，写成了固定值，以便检查，如果是可重复定义的，查询数据时要以DICID区分
//		if (BaseUtil.isMultiTable(this.tableName) && (reportCon == null)) {// 如果是可重复定义表，加条件
			reportCon = new Condition();
			reportCon.setReportID(reportID);
			reportCon.setConField("report_id");
			reportCon.setConOper("=");
			reportCon.setConValue(sourceID);
			reportCon.setFieldType("字符型");
			reportCon.setItemID(itemID);
			if (lstCons == null)
				lstCons = new ArrayList();
			lstCons.add(reportCon);

//		}

		// 如果在有条件的情况下.用CASE when
		String sSql = "";
		if (lstCons != null && lstCons.size() > 0) {
			if (measureType == RowInfo.MEASURE_SUM) {// 如果是求和

				sSql = " sum( case when " + getConsSql() + " then "
						+ getSelectFields() + " end ) as " + FIELD_PRECHAR
						+ itemID;
			} else if (measureType == RowInfo.MEASURE_COUNT) {// 计数
				sSql = " sum( case when " + getConsSql() + " then 1 end ) as "
						+ FIELD_PRECHAR + itemID;
			}
		} else {
			// 如果没有条件
			if (measureType == RowInfo.MEASURE_SUM) {// 如果是求和
				sSql = " sum(" + getSelectFields() + "  ) as " + FIELD_PRECHAR
						+ itemID;
			} else if (measureType == RowInfo.MEASURE_COUNT) {// 计数
				sSql = " sum(1) as " + FIELD_PRECHAR + itemID;
			}
		}
		return sSql;

	}

	private String getConsSql() {
		if (lstCons == null || lstCons.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lstCons.size(); i++) {
			Condition aCon = (Condition) lstCons.get(i);// 这里没考虑到OR的情况
			sb.append(aCon.getSelectSql()).append(" and ");
		}
		return sb.substring(0, sb.length() - 4);
	}

	private String getSelectFields() {
		if (measureType == RowInfo.MEASURE_COUNT)
			return "1";
		else {
			// 要将所有的字段都加上ISNULL()的函数
			String[] strArr = fields.split(";");
			String result = " ";
			for (int i = 0; i < strArr.length; i++) {
				result = result + " isnull(" + strArr[i] + ",0)+";
			}
			// 只考虑字段之间的加
			return result.substring(0, result.length() - 1);

		}
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

}
