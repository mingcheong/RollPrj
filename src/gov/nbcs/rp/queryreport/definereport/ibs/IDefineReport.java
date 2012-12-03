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

	// 财政端表或视图名
	String OBJECT_ENAME = "object_ename";

	// 表或视图中文说明
	String OBJECT_CNAME = "object_cname";

	// 类型
	String OBJECT_CLASS = "object_class";

	// 是否支持批次
	String IS_BATCHNO = "isbatchno";

	// 支持版本
	String SUP_VER = "sup_ver";

	String FIELD_ENAME = "field_ename";

	String FIELD_FNAME = "field_fname";

	String FIELD_TYPE = "field_type";

	// 左联接
	String LEFT_JOIN = "Left join";

	// 右联接
	String RIGHT_JOIN = "Right join";

	// 左联接名称
	String LEFT_JOIN_NAME = "左联接";

	// 右联接名称
	String RIGHT_JOIN_NAME = "右联接";

	// 数据源名称
	String DATASOURCE_NAME = "dataSourceName";

	// 数据源别名
	String SOURCE_ALAIS = " SourceAlais";

	// 数据源ID
	String SOURCE_ID = "sourceID";

	String LVL_ID = "lvl_id";

	String SHOW_LVL = "show_lvl";

	String PAR_ID = "par_id";

	String CODE = "code";

	String NAME = "name";

	String CODE_NAME = "code_name";

	String TRUE_FLAG = "是";

	String FLASE_FLAG = "否";

	String TRUE_NUM = "1";

	String FLASE_NUM = "0";

	String EQUAL_FLAG = "=";

	String BATCH_NO = "batch_no";

	String DATA_TYPE = "data_type";

	String DIV_CODE = "div_code";

	String DIV_NAME = "div_name";

	String VER_NO = "ver_no";

	// 字段类型
	String CHAR_TYPE = "字符型";

	String CHAR_TYPE_A = "字符串";

	String CURRENCY_TYPE = "货币";

	String INT_TYPE = "整数";

	String INTT_TYPE = "整型";

	String DATE_TYPE = "日期型";

	String FLOAT_TYPE = "浮点型";

	String CHAR_Val = "char";

	String NUMBER_VAL = "number";

	String ASC_ARROW = "↑";

	String DESC_ARROW = "↓";

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

	// 基础信息公共表名称
	String TAB_FB_B_INFO = "FB_B_INFO";

	String DIV_KIND = "divkind";

	String DIV_FMKIND = "divfmkind";

	String SHOW_INFO = "show_info";

	String NODE_ID = "nodeID";

	/**
	 * 得到新增报表可选种类
	 * 
	 * @return返回新增报表可选类型列表
	 */
	public List getReportSort();

	/**
	 * 得到报表类型信息
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 * 
	 * @return返回报表类型
	 */
	public DataSet getReportType() throws Exception;

	/**
	 * 得到数据源信息
	 * 
	 * @return 返回数据源信息列表
	 */
	public List getDataSource();

	/**
	 * 得到选中的数据源数明细信息
	 * 
	 * @param sDataSource
	 *            选中的数据源信息
	 * @throws Exception
	 * @return返回选中的数据源详细信息
	 */
	public DataSet getDataSoureDetail(String sDataSource) throws Exception;

	/**
	 * 得到选中数据源与枚举的对应关系
	 * 
	 * @param sDataSource
	 *            选中的数据源信息
	 * @return返回选中数据源的枚举信息
	 */
	public List getEnumWhere(String sDataSource);

	/**
	 * 根据选中的数据源，得到需要使用的枚举源程序
	 * 
	 * @param sDataSource
	 * @return
	 */
	public List getEnumInfo(String sDataSource);

	/**
	 * 得到报表信息
	 * 
	 * @return 返回报表信息
	 * @throws Exception
	 */
	public DataSet getReport() throws Exception;

	/**
	 * 根据数据源EName值取得字段详细信息
	 * 
	 * @param sObjectEName
	 *            数据源表或视图名
	 * @return
	 */
	public List getFieldWithEname(String sObjectEName);

	/**
	 * 得到引用列主键编码
	 * 
	 * @return
	 */
	public List getRefColPriCode();

	/**
	 * 根据enumId得到引用列信息
	 * 
	 * @param sEnumID
	 *            ，枚举ID
	 * @return
	 * @throws Exception
	 */
	public Map getEnumDataWithEnumID(String sEnumID) throws Exception;

	public List getDataSourceRefString(String dsIDs);

	/**
	 * 保存报表
	 * 
	 * @param setYear
	 *            年份
	 * @param blobByte
	 * @param repSetObject
	 *            报表主表对象
	 * @param sOldReportId
	 *            报表主表对象
	 * @param dsHeader
	 *            表头
	 * @param lstSqlLines
	 *            报表查询语句
	 * @param lstType
	 *            报表类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public int saveReportFile(byte[] blobByte, RepSetObject repSetObject,
			String sOldReportId, DataSet dsHeader, List lstSqlLines,
			List lstType) throws Exception;

	/**
	 * 取得ID中的图片信息
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID, int loginmode)
			throws Exception;

	/**
	 * XXL 删除报表
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public String deleteReport(String reportID, String setYear)
			throws Exception;

	/**
	 * 根据编码删除报表类型
	 * 
	 * @param reportID
	 */
	public String deleteReportType(String sCode);

	/**
	 * 保存报表类型
	 * 
	 * @param sLvl
	 * @param sName
	 */
	public String saveReportType(String sCode, String sLvl, String sName,String c1)
			throws Exception;

	/**
	 * 得到最大节次
	 * 
	 * @param sTableName表名
	 * @return 最大节次
	 * @throws Exception
	 */
	public int[] getMaxLevel(String[] sTableName, String[] sLevelCodeArray,
			String[] sLevelInfoArray) throws Exception;

	/**
	 * 得报表类型根据报表ID
	 * 
	 * @param sReportId报表ID
	 * @return报表类型列表
	 */
	public List getReportToType(String sReportId);

	/**
	 * 得到基础信息选择列的值，根据报表id和ename值
	 * 
	 * @param sReportId
	 * @param sFieldEname
	 * @return
	 * @throws Exception 
	 */
	public List getBasSelectInputValue(String sReportId, String sFieldEname) throws Exception;

	/**
	 * 根据数据源名称，得到列信息
	 * 
	 * @param sDataSourceName
	 *            数据源名称
	 * @return 列信息
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sDataSourceName)
			throws Exception;

	/**
	 * 报表调整顺序
	 * 
	 * @param oneLvl
	 *            交换节点一lvl_id
	 * @param twoLvl
	 *            交换节点二lvl_id
	 * @param oneId
	 *            交换节点一ID号
	 * @param twoId
	 *            交换节点二ID号
	 * @param tableName
	 *            表名
	 * @param lvlFieldName
	 *            lvl_id字段名
	 * @param idFieleName
	 *            id（主键）字段名 *
	 * @param filter
	 *            条件
	 */
	public void changeLvlValue(String oneLvl, String twoLvl, String oneId,
			String twoId, String tableName, String lvlFieldName,
			String idFieleName, String filter);

	/**
	 * 复制查询报表
	 * 
	 * @param reportID报表ID
	 * @param reportName复制生成的新报表名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage copyReport(String reportID, String reportName, String setYear)
			throws Exception;
}
