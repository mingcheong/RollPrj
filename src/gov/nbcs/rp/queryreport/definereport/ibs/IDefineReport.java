/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ibs;

import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.queryreport.definereport.ui.DefinePub;


public interface IDefineReport {
	String DICID = "dicid";

	// �����˱����ͼ��
	String OBJECT_ENAME = "object_ename";

	// �����ͼ����˵��
	String OBJECT_CNAME = "object_cname";

	// ����
	String OBJECT_CLASS = "object_class";

	// �Ƿ�֧������
	String IS_BATCHNO = "isbatchno";

	// ֧�ְ汾
	String SUP_VER = "sup_ver";

	String FIELD_ENAME = "field_ename";

	String FIELD_FNAME = "field_fname";

	String FIELD_TYPE = "field_type";

	// ������
	String LEFT_JOIN = "Left join";

	// ������
	String RIGHT_JOIN = "Right join";

	// ����������
	String LEFT_JOIN_NAME = "������";

	// ����������
	String RIGHT_JOIN_NAME = "������";

	// ����Դ����
	String DATASOURCE_NAME = "dataSourceName";

	// ����Դ����
	String SOURCE_ALAIS = " SourceAlais";

	// ����ԴID
	String SOURCE_ID = "sourceID";

	String LVL_ID = "lvl_id";

	String SHOW_LVL = "show_lvl";

	String PAR_ID = "par_id";

	String CODE = "code";

	String NAME = "name";

	String CODE_NAME = "code_name";

	String TRUE_FLAG = "��";

	String FLASE_FLAG = "��";

	String TRUE_NUM = "1";

	String FLASE_NUM = "0";

	String EQUAL_FLAG = "=";

	String BATCH_NO = "batch_no";

	String DATA_TYPE = "data_type";

	String DIV_CODE = "div_code";

	String DIV_NAME = "div_name";

	String VER_NO = "ver_no";

	// �ֶ�����
	String CHAR_TYPE = "�ַ���";

	String CHAR_TYPE_A = "�ַ���";

	String CURRENCY_TYPE = "����";

	String INT_TYPE = "����";

	String INTT_TYPE = "����";

	String DATE_TYPE = "������";

	String FLOAT_TYPE = "������";

	String CHAR_Val = "char";

	String NUMBER_VAL = "number";

	String ASC_ARROW = "��";

	String DESC_ARROW = "��";

	String ASC_FLAG = "ASC";

	String DESC_FLAG = "DESC";

	String COL_ID = "colID";

	String JOIN_TYPE = "joinType";

	String ENUM_ID = "enumID";

	String ENUM_NAME = "enumName";

	String ENUM_ = "enum_";

	Object ENUM_DATA = "enumData";

	String PA_ = "PA_";

	String PATH_ = DefinePub.getTempDir();

	String ORDER_TYPE = "ORDER_TYPE";

	String ORDER_INDEX = "ORDER_INDEX";

	String LEVEL = "LEVEL";

	String DIVNAME_PARA = "DIVNAME";

	String INT_FORMATE = "#,###";

	String FLOAT_FORMATE = "#,##0.00";

	public static final int REPORTTYPE_OTHER = 0;

	public static final int REPORTTYPE_SZZB = 1;

	public static final int REPORTTYPE_ROW = 2;

	public static final int REPORTTYPE_COVER = 3;

	public static final int REPORTTYPE_GROUP = 4;

	public String TEMP_TABLE = "TEMPTABLE";

	public String CREATE_QUERY = "CREATEQUERY";

	public String MID_QUERY = "MIDQUERY";

	public String LAST_QUERY = "LASTQUERY";

	String IS_GROUP = "ISGROUP";

	String SUMMARY_INDEX = "SUMMARYINDEX";

	String OPE_LIKE = "like";

	String OPE_NOTLIKE = "not like";

	String DEP_CODE = "DEP_CODE";

	String DEP_NAME = "DEP_NAME";

	// ������Ϣ����������
	String TAB_FB_B_INFO = "FB_B_INFO";

	String DIV_KIND = "divkind";

	String DIV_FMKIND = "divfmkind";

	String SHOW_INFO = "show_info";

	String NODE_ID = "nodeID";

	/**
	 * �õ����������ѡ����
	 * 
	 * @return�������������ѡ�����б�
	 */
	public List getReportSort();

	/**
	 * �õ�����������Ϣ
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 * 
	 * @return���ر�������
	 */
	public DataSet getReportType() throws Exception;

	/**
	 * �õ�����Դ��Ϣ
	 * 
	 * @return ��������Դ��Ϣ�б�
	 */
	public List getDataSource();

	/**
	 * �õ�ѡ�е�����Դ����ϸ��Ϣ
	 * 
	 * @param sDataSource
	 *            ѡ�е�����Դ��Ϣ
	 * @throws Exception
	 * @return����ѡ�е�����Դ��ϸ��Ϣ
	 */
	public DataSet getDataSoureDetail(String sDataSource) throws Exception;

	/**
	 * �õ�ѡ������Դ��ö�ٵĶ�Ӧ��ϵ
	 * 
	 * @param sDataSource
	 *            ѡ�е�����Դ��Ϣ
	 * @return����ѡ������Դ��ö����Ϣ
	 */
	public List getEnumWhere(String sDataSource);

	/**
	 * ����ѡ�е�����Դ���õ���Ҫʹ�õ�ö��Դ����
	 * 
	 * @param sDataSource
	 * @return
	 */
	public List getEnumInfo(String sDataSource);

	/**
	 * �õ�������Ϣ
	 * 
	 * @return ���ر�����Ϣ
	 * @throws Exception
	 */
	public DataSet getReport() throws Exception;

	/**
	 * ��������ԴENameֵȡ���ֶ���ϸ��Ϣ
	 * 
	 * @param sObjectEName
	 *            ����Դ�����ͼ��
	 * @return
	 */
	public List getFieldWithEname(String sObjectEName);

	/**
	 * �õ���������������
	 * 
	 * @return
	 */
	public List getRefColPriCode();

	/**
	 * ����enumId�õ���������Ϣ
	 * 
	 * @param sEnumID
	 *            ��ö��ID
	 * @return
	 * @throws Exception
	 */
	public Map getEnumDataWithEnumID(String sEnumID) throws Exception;

	public List getDataSourceRefString(String dsIDs);

	/**
	 * ���汨��
	 * 
	 * @param setYear
	 *            ���
	 * @param blobByte
	 * @param repSetObject
	 *            �����������
	 * @param sOldReportId
	 *            �����������
	 * @param dsHeader
	 *            ��ͷ
	 * @param lstSqlLines
	 *            �����ѯ���
	 * @param lstType
	 *            ��������
	 * 
	 * @return
	 * @throws Exception
	 */
	public int saveReportFile(byte[] blobByte, RepSetObject repSetObject,
			String sOldReportId, DataSet dsHeader, List lstSqlLines,
			List lstType) throws Exception;

	/**
	 * ȡ��ID�е�ͼƬ��Ϣ
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID, int loginmode)
			throws Exception;

	/**
	 * XXL ɾ������
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public String deleteReport(String reportID, String setYear)
			throws Exception;

	/**
	 * ���ݱ���ɾ����������
	 * 
	 * @param reportID
	 */
	public String deleteReportType(String sCode);

	/**
	 * ���汨������
	 * 
	 * @param sLvl
	 * @param sName
	 */
	public String saveReportType(String sCode, String sLvl, String sName,String c1)
			throws Exception;

	/**
	 * �õ����ڴ�
	 * 
	 * @param sTableName����
	 * @return ���ڴ�
	 * @throws Exception
	 */
	public int[] getMaxLevel(String[] sTableName, String[] sLevelCodeArray,
			String[] sLevelInfoArray) throws Exception;

	/**
	 * �ñ������͸��ݱ���ID
	 * 
	 * @param sReportId����ID
	 * @return���������б�
	 */
	public List getReportToType(String sReportId);

	/**
	 * �õ�������Ϣѡ���е�ֵ�����ݱ���id��enameֵ
	 * 
	 * @param sReportId
	 * @param sFieldEname
	 * @return
	 * @throws Exception 
	 */
	public List getBasSelectInputValue(String sReportId, String sFieldEname) throws Exception;

	/**
	 * ��������Դ���ƣ��õ�����Ϣ
	 * 
	 * @param sDataSourceName
	 *            ����Դ����
	 * @return ����Ϣ
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sDataSourceName)
			throws Exception;

	/**
	 * �������˳��
	 * 
	 * @param oneLvl
	 *            �����ڵ�һlvl_id
	 * @param twoLvl
	 *            �����ڵ��lvl_id
	 * @param oneId
	 *            �����ڵ�һID��
	 * @param twoId
	 *            �����ڵ��ID��
	 * @param tableName
	 *            ����
	 * @param lvlFieldName
	 *            lvl_id�ֶ���
	 * @param idFieleName
	 *            id���������ֶ��� *
	 * @param filter
	 *            ����
	 */
	public void changeLvlValue(String oneLvl, String twoLvl, String oneId,
			String twoId, String tableName, String lvlFieldName,
			String idFieleName, String filter);

	/**
	 * ���Ʋ�ѯ����
	 * 
	 * @param reportID����ID
	 * @param reportName�������ɵ��±�������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage copyReport(String reportID, String reportName, String setYear)
			throws Exception;
}
