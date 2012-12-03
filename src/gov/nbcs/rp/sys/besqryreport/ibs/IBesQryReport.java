/**
 * Copyright �㽭����

 * 
 * @title ���Ʋ�ѯ����-�ӿ�
 * 
 * @author Ǯ�Գ�
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

	public String sSourceTableName = "TNAME"; // Դ��ı���

	public String sSourceTableType = "TABTYPE"; // Դ�������

	// ϵͳ����Ϣ�ֶ�
	public String Column_NAME = "Column_NAME";

	// �����ֶ�
	public String TABLENAME_MAIN = "fb_u_qr_repset".toUpperCase();

	public String TYPENO = "TYPE_NO"; // ҵ����

	public String REPORT_ID = "REPORT_ID";

	public String REPORT_TYPE = "REPORT_TYPE"; // ��������

	public String REPORT_TYPE_VALUE = String.valueOf(UntPub.IR_REPORT_QR);

	public String REPORT_CNAME = "REPORT_CNAME";

	public String CURRENCYUNIT = "CURRENCYUNIT"; // ��������

	public String REPORT_SOURCE = "REPORT_SOURCE";

	public String REPORT_SOURCE_VALUE = "����";

	public String IS_PASSVERIFY = "IS_PASSVERIFY";

	public String IS_PASSVERIFY_VALUE = "��";

	public String IS_ACTIVE = "IS_ACTIVE";

	public String IS_ACTIVE_VALUE = "��";

	public String DATA_USER = "DATA_USER";

	public String OBJECT_NAMES = "OBJECT_NAMES"; // Դ������

	public String OBJECT_ENAMES = "OBJECT_ENAMES"; // Դ����������

	public String IS_HASBATCH = "IS_HASBATCH";

	public String IS_HASBATCH_VALUE = "��";

	public String IS_MULTICOND = "IS_MULTICOND";

	public String IS_MULTICOND_VALUE = "��";

	public String IS_END = "IS_END";

	public String IS_END_VALUE = "1";

	public String SET_YEAR = "SET_YEAR";

	public String LVL_ID = "LVL_ID";

	// ����Ϣ���ֶ�
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

	// sqllines���������Ϣ
	public String TABLE_SQLLINES = "fb_u_qr_sqllines";

	public String TABLE_SQLCOLS = "USER_VIEWS";

	public String VIEW_NAME = "VIEW_NAME";

	public String TEXT = "text";

	public String SQLTYPE = "SQLTYPE";

	public String VIEWLEVEL = "VIEWLEVEL";

	public String VIEWNAME = "VIEWNAME";

	public String SQLLINES = "SQLLINES";

	public String SQLTREAM = "SQLTREAM";

	public DataSet getDataSet(String aTable, String aFilter) throws Exception; // ��ȡdataset

	public DataSet getFieldInfo(String aTableName) throws Exception;

	public DataSet getSourceData() throws Exception;

	public String getMaxValueFromField(String aTableName, String aFieldName,
			String aFilter) throws Exception;

	/**
	 * ���汨��
	 * 
	 * @param sqls������ͷ���
	 * @param lstSqllines
	 *            sqllines�����
	 * @param sReportID����ID
	 * @param lstType
	 * @throws Exception
	 */
	public void execSql(List sqls, List lstSqllines, String sReportID,
			List lstType) throws Exception;

}
