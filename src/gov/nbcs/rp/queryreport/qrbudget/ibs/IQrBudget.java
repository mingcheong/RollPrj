package gov.nbcs.rp.queryreport.qrbudget.ibs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OtherSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import com.fr.base.Style;

public interface IQrBudget {
	// fb_u_qr_repset表
	String SET_YEAR = "SET_YEAR";

	String TYPE_NO = "TYPE_NO";

	String REPORT_ID = "REPORT_ID";

	String REPORT_TYPE = "REPORT_TYPE";

	String REPORT_CNAME = "REPORT_CNAME";

	String TITLE = "TITLE";

	String TITLE_AREA = "TITLE_AREA";

	String TITLE_FONT = "TITLE_FONT";

	String TITLE_FONTSIZE = "TITLE_FONTSIZE";

	String COLUMN_AREA = "COLUMN_AREA";

	String COLUMN_FONT = "COLUMN_FONT";

	String COLUMN_FONTSIZE = "COLUMN_FONTSIZE";

	String CURRENCYUNIT = "CURRENCYUNIT";

	String REPORT_SOURCE = "REPORT_SOURCE";

	String IS_PASSVERIFY = "IS_PASSVERIFY";

	String IS_ACTIVE = "IS_ACTIVE";

	String DATA_USER = "DATA_USER";

	String IS_HASBATCH = "IS_HASBATCH";

	String IS_MULTICOND = "IS_MULTICOND";

	String IS_END = "IS_END";

	String TYPE_FLAG = "TYPE_FLAG"; // 报表类型 0：普通报表，1：收支总表 ，2：行报表，3:封面，9：综合处的表

	String LVL_ID = "LVL_ID";

	String END_FLAG = "END_FLAG";

	// fb_u_qr_colset表
	String FIELD_ID = "FIELD_ID";

	String FIELD_CODE = "FIELD_CODE";

	String FIELD_CNAME = "FIELD_CNAME";

	String FIELD_ENAME = "FIELD_ENAME";

	String FIELD_FNAME = "FIELD_FNAME";

	String FIELD_LEVEL = "FIELD_LEVEL";

	String FIELD_TYPE = "FIELD_TYPE";

	String FIELD_DISPWIDTH = "FIELD_DISPWIDTH";

	String IS_HIDECOL = "IS_HIDECOL";

	String FIELD_DISFORMAT = "FIELD_DISFORMAT";

	String IS_LEAF = "IS_LEAF";

	String FIELD_COLUMN = "COL_NUM";

	// 单位
	String EN_ID = "EID";

	String DIV_CODE = "DIV_CODE";

	String PARENT_ID = "PARENT_ID";

	String CODE_NAME = "CODE_NAME";

	String ID = "ID";

	String NAME = "NAME";

	// FB_U_QR_QUERYVER表(查询版本)
	String VER_NO = "VER_NO";

	String VER_REMARK = "VER_REMARK";

	String VER_DATE = "VER_DATE";

	String ADD = "ADD";

	String MODIFY = "MODIFY";

	String FULLNAME = "FULLNAME";

	// 单位上报与未上报情况
	String SEND = "SEND";// 已上报

	String UNSEND = "UNSEND";// 未上报

	String UUID = "UUID";

	String RG_CODE = "RG_CODE";

	public class ColDispInf {
		public String sFieldType = new String();

		public String sFieldDisformat = new String();

		public Style style = Style.getInstance();

		public String fieldEName = "";
	}

	public class DivObject {
		public String sDivCode = new String(); // 单位编码

		public boolean isLeaf; // 是否叶节点，true:叶节点， false ：不是叶节点
	}

	public class BodyInfo {

		public List lstBody;

		public String sSqlBody;
	}

	/**
	 * 得到报表信息
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportName(int iUserType, String setYear,
			String reportType) throws Exception;

	/**
	 * 得到单位名称
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getDivName(String setYear, int iLevel, int iUserType)
			throws Exception;

	/**
	 * 得到报表表头sql
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getReportHeaderSql(String sReportId, String setYear)
			throws Exception;

	/**
	 * 得到报表表头
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportHeader(String sReportId, String setYear)
			throws Exception;

	/**
	 * 保存列宽
	 * 
	 * @param sReportId
	 * @param setYear
	 * @param lstFields
	 * @param lstColWidth
	 * @throws Exception
	 */
	public void saveColWidth(String sReportId, String setYear, List lstFields,
			List lstColWidth) throws Exception;

	/**
	 * 得到查询条件
	 * 
	 * @param lstDept
	 * @param lstDiv
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public String getDivWhereSql(int iTypeFlag, List lstDept, List lstDiv,
			String setYear) throws Exception;

	/**
	 * 创建中间视图,得到查询结果
	 * 
	 * @param sReportID
	 * @param sBatchNoFilter
	 *            是否有批次的查询条件
	 * @param lstDept
	 *            选中的业务处室列表
	 * @param lstDiv
	 *            选中的单位列表
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public List getData(String sVerNo, String sReportID, String sBatchNoFilter,
			List lstDept, List lstDiv, String sFieldSelect, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag) throws Exception;

	/**
	 * 返回查询语句
	 * 
	 * @param sReportId
	 * @param sBatchNoFilter
	 * @param sDivWhereSql
	 * @param sFieldSelect
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @return
	 * @throws Exception
	 */
	public OtherSearchObj getOtherSearchObj(String sVerNo, String sReportId,
			String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iUserType, int iLoginmode)
			throws Exception;

	/**
	 * 单位查询
	 * 
	 * @param sReportID
	 * @param sFilter
	 * @param sFieldSelect
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	// public InfoPackage getDataDiv(String sReportID, String sFilter,
	// String sFieldSelect, String setYear) throws Exception;
	// chcx add 版本处理2007.09.09
	// 得到版本信息
	public DataSet getVerInfo() throws Exception;

	/**
	 * 根据报表得到版本信息,author:zlx
	 */
	public DataSet getVerInfoWithReport(String sReportId) throws Exception;

	// 删除版本信息
	public InfoPackage delVerInfo(String verNo) throws Exception;

	// 保存版本信息
	public InfoPackage saveVerInfo(String verNo, String verDate,
			String verRemark, String EditorType) throws Exception;

	// 删除版本物理表
	public InfoPackage DelTableReportVer(String verNo) throws Exception;

	public DataSet getReportInfo(String verNo) throws Exception;

	// 创建报表版本物理表
	public InfoPackage CreatTabelReportVer(String verNo, String reportId,
			String sqllines) throws Exception;

	// 上报、未上报查询条件
	public DataSet getCondition() throws Exception;

	// 单位报表表头
	public DataSet getEnterpriseReportHeader() throws Exception;

	// 单位上报情况
	public DataSet getSendEnterprise(String sendFlag) throws Exception;

	/**
	 * 查询数据，并返回数据和查询语句类,注返回的字段名都是小写
	 * 
	 * @param sVerNo
	 *            版本号
	 * @param sBatchNoFilter
	 *            批次
	 * @param lstDept
	 *            部门条件
	 * @param lstDiv
	 *            单位条件
	 * @param setYear
	 *            年度
	 * @param iUserType
	 *            用户类型
	 * @param iLoginmode
	 *            登录方式
	 * @param iTypeFlag
	 *            报表类型
	 * @param reportInfo
	 *            报表信息
	 * @return List中第一个为数据LIST，第二个为一个OriSearchObj，以备导出
	 * @throws Exception
	 */

	public List getOriData(String sVerNo, String sBatchNoFilter, List lstDept,
			List lstDiv, String setYear, int iUserType, int iLoginmode,
			int iTypeFlag, ReportInfo reportInfo, ConditionObj conditionObj)
			throws Exception;

	/**
	 * 查询数据，并返回数据和查询语句类,注返回的字段名都是小写,根据查询语句列表
	 * 
	 * @param sVerNo
	 * @param sBatchNoFilter
	 * @param lstDept
	 * @param lstDiv
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @param iTypeFlag
	 * @param reportInfo
	 * @param lstSqlLines
	 * @return
	 * @throws Exception
	 */
	public List getOriDataWhere(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			List lstSqlLines, ConditionObj conditionObj) throws Exception;

	/**
	 * 查询数据根据查询语句对象，并返回数据
	 * 
	 * @param sVerNo
	 *            版本号
	 * @param sBatchNoFilter
	 *            批次
	 * @param lstDept
	 *            部门条件
	 * @param lstDiv
	 *            单位条件
	 * @param setYear
	 *            年度
	 * @param iUserType
	 *            用户类型
	 * @param iLoginmode
	 *            登录方式
	 * @param iTypeFlag
	 *            报表类型
	 * @param lstSqlLines
	 *            查询语句
	 * @return List查询数据
	 * @throws Exception
	 */
	public OriSearchObj getSearchObjWhere(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			List lstSqlLines, ConditionObj conditionObj) throws Exception;

	/**
	 * 取得查询体
	 * 
	 * @param sVerNo
	 * @param sBatchNoFilter
	 * @param lstDept
	 * @param lstDiv
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @param iTypeFlag
	 * @param reportInfo
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getSearchObj(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			ConditionObj conditionObj) throws Exception;

	/**
	 * 得到报表报头(包含隐藏列)
	 * 
	 * @param sReportId
	 *            报表ID
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportHeader_A(String sReportId) throws Exception;

	/**
	 * 得到需执行脚本
	 * 
	 * @param iLoginMode
	 *            登陆方式
	 * @return 查询结果列表
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getScriptInfo(int iLoginMode) throws SQLException, IOException;

	/**
	 * 执行脚本
	 * 
	 * @param sSql
	 *            脚本
	 * @throws Exception
	 */
	public void exeScript(String sSql) throws Exception;

	/**
	 * 得到单位控制数值
	 * 
	 * @param sDivCode
	 */
	public List getDivRae(String sDivCode);

	/**
	 * 部门预算二下表---2009年支出预算数下达书,用于基本支出
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownJBZC(String divCode, int batchNo, int dataType)
			throws Exception;

	/**
	 * 部门预算二下表---2009年支出预算数下达书,用于专项明细
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownPrjDetail(String prjType, String divCode,
			int batchNo, int dataType, boolean isPrjGovJj) throws Exception;

	/**
	 * 部门预算二下表---2009年政府采购预算通知书
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDownGov(String divCode, int batchNo, int dataType,
			boolean isPrjGovJj) throws Exception;

	/**
	 * 保存列宽(收支总表和封面目录）
	 * 
	 * @throws Exception
	 */
	public void saveSzzbColWidth(String sReportId, String setYear,
			List lstColWidth) throws Exception;

	// 查询类型0：表示江苏和江苏相同的地区 ，1：表示宁波
	public int getSearchType(String sYear) throws Exception;

	/**
	 * 部门预算二下表---2010年收入预算数下达书
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDown(String divCode, int batchNo, int dataType)
			throws Exception;

	public Object[] getBudgetDownBz(String divCode, int batchNo, int dataType)
			throws Exception;

}
