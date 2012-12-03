/**
 * Copyright 浙江易桥

 * 
 * @title 定制查询报表-接口
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.sys.besqryreport.ibs;

import java.util.List;

import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;

public interface IBesQryReport {

	public String TNAME = "TNAME";

	// pubcode
	public String TITLE = "TITLE";

	public String PUBCODETABLE = "FB_S_PUBCODE";

	public String DataType_TypeID = "TYPEID";

	public String DATATYPE = "DATATYPE";

	public String CODE = "CODE";

	public String NAME = "NAME";

	public String DataType_TypeIDValue = "QUERYTYPE";

	public String DataType_ID = "CODE";

	public String DataType_Name = "NAME";

	public String sSourceTableName = "TNAME"; // 源表的表名

	public String sSourceTableType = "TABTYPE"; // 源表的类型

	// 系统列信息字段
	public String Column_NAME = "Column_NAME";

	// 主表字段
	public String TABLENAME_MAIN = "fb_u_qr_repset".toUpperCase();

	public String TYPENO = "TYPE_NO"; // 业务编号

	public String REPORT_ID = "REPORT_ID";

	public String REPORT_TYPE = "REPORT_TYPE"; // 报表类型

	public String REPORT_TYPE_VALUE = String.valueOf(UntPub.IR_REPORT_QR);

	public String REPORT_CNAME = "REPORT_CNAME";

	public String CURRENCYUNIT = "CURRENCYUNIT"; // 货币类型

	public String REPORT_SOURCE = "REPORT_SOURCE";

	public String REPORT_SOURCE_VALUE = "定制";

	public String IS_PASSVERIFY = "IS_PASSVERIFY";

	public String IS_PASSVERIFY_VALUE = "是";

	public String IS_ACTIVE = "IS_ACTIVE";

	public String IS_ACTIVE_VALUE = "是";

	public String DATA_USER = "DATA_USER";

	public String OBJECT_NAMES = "OBJECT_NAMES"; // 源表名称

	public String OBJECT_ENAMES = "OBJECT_ENAMES"; // 源表中文名称

	public String IS_HASBATCH = "IS_HASBATCH";

	public String IS_HASBATCH_VALUE = "是";

	public String IS_MULTICOND = "IS_MULTICOND";

	public String IS_MULTICOND_VALUE = "是";

	public String IS_END = "IS_END";

	public String IS_END_VALUE = "1";

	public String SET_YEAR = "SET_YEAR";

	public String LVL_ID = "LVL_ID";

	// 列信息表字段
	public String TABLENAME_COL = "fb_u_qr_colset".toUpperCase();

	public String FIELD_ENAME = "FIELD_ENAME";

	public String FIELD_CNAME = "FIELD_CNAME";

	public String IS_HIDECOL = "IS_HIDECOL";

	public String FIELD_FNAME = "FIELD_FNAME";

	public String FIELD_DISFORMAT = "FIELD_DISFORMAT";

	public String FIELD_TYPE = "FIELD_TYPE";

	public String FIELD_CODE = "FIELD_CODE";

	public String FIELD_ID = "FIELD_ID";

	public String IS_LEAF = "IS_LEAF";

	public String FIELD_LEVEL = "FIELD_LEVEL";

	public String FIELD_DISPWIDTH = "FIELD_DISPWIDTH";
	
	public String FUNDSOURCE_FLAG   = "FUNDSOURCE_FLAG";
	
	public String COMPARE_FLAG   = "COMPARE_FLAG";

	// sqllines表里的列信息
	public String TABLE_SQLLINES = "fb_u_qr_sqllines";

	public String TABLE_SQLCOLS = "USER_VIEWS";

	public String VIEW_NAME = "VIEW_NAME";

	public String TEXT = "text";

	public String SQLTYPE = "SQLTYPE";

	public String VIEWLEVEL = "VIEWLEVEL";

	public String VIEWNAME = "VIEWNAME";

	public String SQLLINES = "SQLLINES";

	public String SQLTREAM = "SQLTREAM";

	public DataSet getDataSet(String aTable, String aFilter) throws Exception; // 获取dataset

	public DataSet getFieldInfo(String aTableName) throws Exception;

	public DataSet getSourceData() throws Exception;

	public String getMaxValueFromField(String aTableName, String aFieldName,
			String aFilter) throws Exception;

	/**
	 * 保存报表
	 * 
	 * @param sqls主表，表头语句
	 * @param lstSqllines
	 *            sqllines表语句
	 * @param sReportID报表ID
	 * @param lstType
	 * @throws Exception
	 */
	public void execSql(List sqls, List lstSqllines, String sReportID,
			List lstType) throws Exception;

}
