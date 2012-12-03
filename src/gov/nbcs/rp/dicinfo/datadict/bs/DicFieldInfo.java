/**
 * @# DicFieldInfo.java    <文件名>
 */
package gov.nbcs.rp.dicinfo.datadict.bs;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:数据字典一字段的信息,只包含关键字和要添加的选项
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-5-9
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class DicFieldInfo {

	private String tableName;

	private String fieldName;

	private String refColId;

	private String refColName;

	private String conFieldCname;

	private String conFieldEname;

	private String dicID;

	private int xh;

	private String memo;

	private String sVisible;

	private String fieldCName;

	public DicFieldInfo() {

	}

	public DicFieldInfo(XMLData aData, int iXh) {
		this.tableName = getAStringField(aData, IDataDictBO.TABLE_ENAME);
		this.fieldName = getAStringField(aData, IDataDictBO.AFIELD_ENAME);
		this.refColId = getAStringField(aData, IDataDictBO.REFCOL_ID);
		this.refColName = getAStringField(aData, IDataDictBO.REFCOL_NAME);
		this.conFieldCname = getAStringField(aData, IDataDictBO.CON_FIELDCNAME);
		this.conFieldEname = getAStringField(aData, IDataDictBO.CON_FIELDENAME);
		this.xh = iXh;
		this.memo = getAStringField(aData, IDataDictBO.AMEMO);
		this.sVisible = getAStringField(aData, IDataDictBO.AIS_VISIBLE);
		this.dicID = getAStringField(aData, IDataDictBO.DICID);
		this.fieldCName = getAStringField(aData, IDataDictBO.AFIELD_CNAME);
	}

	public String getConFieldCname() {
		return conFieldCname;
	}

	public void setConFieldCname(String conFieldCname) {
		this.conFieldCname = conFieldCname;
	}

	public String getConFieldEname() {
		return conFieldEname;
	}

	public void setConFieldEname(String conFieldEname) {
		this.conFieldEname = conFieldEname;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getRefColId() {
		return refColId;
	}

	public void setRefColId(String refColId) {
		this.refColId = refColId;
	}

	public String getRefColName() {
		return refColName;
	}

	public void setRefColName(String refColName) {
		this.refColName = refColName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public static String getAStringField(XMLData aData, String sField) {
		if (aData == null)
			return null;
		if (aData.get(sField) == null)
			return null;
		return aData.get(sField).toString();
	}

	public static String getSqlStringField(String field) {
		if (field == null || field.equals(""))
			return "null";
		return "'" + field + "'";
	}

	public String getUpdateSql() throws Exception {
		if (tableName == null || tableName.equals("") || fieldName == null
				|| fieldName.equals("")) {
			throw new Exception("指定的字段数据不全，不可以生成更新语句!");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("update " + IDataDictBO.DIC_FIELD_TABLENAME + " set ");
		sb.append(IDataDictBO.REFCOL_ID).append("=").append(
				getSqlStringField(refColId)).append(",");
		sb.append(IDataDictBO.REFCOL_NAME).append("=").append(
				getSqlStringField(refColName)).append(",");
		sb.append(IDataDictBO.CON_FIELDCNAME).append("=").append(
				getSqlStringField(conFieldCname)).append(",");
		sb.append(IDataDictBO.CON_FIELDENAME).append("=").append(
				getSqlStringField(conFieldEname)).append(",");

		sb.append(IDataDictBO.AFIELD_SORT).append("=").append(xh).append(",");
		sb.append(IDataDictBO.AMEMO).append("=")
				.append(getSqlStringField(memo)).append(",");
		sb.append(IDataDictBO.AIS_VISIBLE).append("=").append(
				getSqlStringField(sVisible)).append(",");
		sb.append(IDataDictBO.AFIELD_CNAME).append("=").append(
				getSqlStringField(fieldCName));

		sb.append(" where ").append(IDataDictBO.TABLE_ENAME).append("=")
				.append(getSqlStringField(tableName));
		sb.append(" and  ").append(IDataDictBO.AFIELD_ENAME).append("=")
				.append(getSqlStringField(fieldName));
		sb.append(" and  ").append(IDataDictBO.DICID).append("=").append(
				getSqlStringField(dicID));

		return sb.toString();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public String getDicID() {
		return dicID;
	}

	public void setDicID(String dicID) {
		this.dicID = dicID;
	}

	public String check() {
		if (tableName == null || tableName.equals("") || fieldName == null
				|| fieldName.equals("")) {
			return "字段为："+fieldCName+"  指定的字段数据不全，不可以生成更新语句!";
		}
		if (!Common.isNullStr(refColId) && Common.isNullStr(conFieldEname))
			return "字段为："+fieldCName+" 填写了参考列的行，必须要填写取数条件!";
		return "";
	}
}
