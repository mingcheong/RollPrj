/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 项目绩效-接口类
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.audit.ibs;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;




public interface IPrjAudit
{

	public final static String tranFlag = "appr";

	/** 分组上报编码group_code */
	public final static String UPGROUPCODE = "{0FF37881-C447-11DE-B912-EBA08E614509}";

	public final static String DOWNGROUPCODE = "{61887066-C446-11DE-B912-EBA08E614509}";

	/** 指标行数 */
	public int IndexRowCount = 4;

	/** 批次 */
	public String PRJAPPR_BATCH_NO = "2";

	/** 数据类型 */
	public String PRJAPPR_DATA_TYPE = "0";

	public final static String FB_S_TRANDETAIL_APPR = "FB_S_TRANDETAIL_APPR";

	public final static String FB_S_TRANDETAIL_SCHEME_APPR = "FB_S_TRANDETAIL_SCHEME_APPR";

	public final static String PROJECT_SOURCE = "VW_FB_P_BASE_APPR";

	public final static String SORT_CODE = "CL1";

	public final static String SORT_NAME = "CL2";

	public final static String SORT_FNAME = "CL3";



	public class PrjTable
	{

		public final static String TABLENAME_Main = "VW_FB_QR_PRJAPPR";

		public final static String TABLENAME_Detail = "VW_FB_QR_PRJAPPR";

		public final static String TABLENAME_APPR = "FB_P_BASE_APPR";

		public final static String TABLENAME_SORT = "FB_P_APPR_SORT";

		/** 单位编码 */
		public final static String DIV_CODE = "DIV_CODE";

		/** 单位名称 */
		public final static String DIV_NAME = "DIV_NAME";

		/** 项目编码 */
		public final static String PRJ_CODE = "PRJ_CODE";

		/** 项目名称 */
		public final static String PRJ_NAME = "PRJ_NAME";

		/** 项目类别编码 */
		public final static String PRJSORT_CODE = "PRJSORT_CODE";

		/** 项目类别名称 */
		public final static String PRJSORT_NAME = "PRJSORT_NAME";

		/** 项目属性编码 */
		public final static String PRJATTR_CODE = "PRJATTR_CODE";

		/** 项目属性名称 */
		public final static String PRJATTR_NAME = "PRJATTR_NAME";

		/** 项目依据编码 */
		public final static String PRJACCORD_CODE = "PRJACCORD_CODE";

		/** 项目依据名称 */
		public final static String PRJACCORD_NAME = "PRJACCORD_NAME";

		/** 项目类型编码 */
		public final static String PRJ_TRANSFER = "PRJ_TRANSFER";

		/** 项目类型名称 */
		public final static String PRJ_TRANSFER_NAME = "PRJ_TRANSFER_NAME";

		/** 项目内容 */
		public final static String PRJ_CONTENT = "PRJ_CONTENT";

		/** 科目编码 */
		public final static String ACCT_CODE = "ACCT_CODE";

		/** 科目名称 */
		public final static String ACCT_NAME = "ACCT_NAME";

	}

	/** 主表信息 */
	public class MainTable
	{

		/** 表名 */
		public final static String TableName = "FB_P_APPR";

		/** 项目绩效类型 */
		public final static String PraiseType = "PRJAPPRAISETYPE";



		/** 项目概况 */
		public final static class GeneralInfo
		{

			/** 流水码 */
			public final static String ROW_ID = "ROW_ID";

			/** 项目编码 */
			public final static String PrjCode = "PRJCODE";

			/** 是否已填绩效评价 */
			public final static String IsInputPrjAppr = "ISINPUTPRJAPPR";

			/** 绩效类型编码 */
			public final static String SORTCODE = "SORTCODE";

			/** 项目申报部门 */
			public final static String DECLAREDepartMent = "SBDEPARTMENT";

			/** 项目负责人 */
			public final static String DECLAREManager = "SBMANAGER";

			/** 联系电话 */
			public final static String DECLARETel = "SBTEL";

			/** 项目实施部门 */
			public final static String EXECUTEDEpartMent = "EXECUTEDEPARTMENT";

			/** 项目负责人 */
			public final static String EXECUTEManager = "EXECUTEMANAGER";

			/** 联系电话 */
			public final static String EXECUTETel = "EXECUTETEL";

			/** 项目资金申报计划（万元） */
			/** 资金总额 */
			public final static String TotleMoney = "TOTLEMONEY";

			/** 财政性资金 */
			public final static String FinanceMoney = "FINANCEMONEY";

			/** 项目单位自筹 */
			public final static String DivColMoney = "DIVCOLMONEY";

			/** 其他资金 */
			public final static String OtherMoney = "OTHERMONEY";

			/** 项目起止时间 */
			/** 预计开始时间 */
			public final static String PrjBeginDate = "PRJBEGINDATE";

			/** 预计结束时间 */
			public final static String PrjEndDate = "PRJENDDATE";

			/** 项目概况、设立依据、必要性与可行性 */
			public final static String PrjGeneralContent = "PRJGENERALCONTENT";

			/** 绩效目标预期实现进度（或项目事实进度计划） */
			public final static String Speed = "SPEED";

			/** 以前年度项目绩效评价情况（跨年度项目） */
			public final static String OldApprAbout = "OLDAPPRABOUT";

			/** 项目资金投向（使用）计划 */
			public final static String FUNDSPLAN = "FUNDSPLAN";
		}

		/** 项目绩效目标 */
		public class Target
		{
			/** 总目标 */
			public final static String General = "TargetGeneral";

			/** 阶段性目标 */
			public final static String Stage = "TargetStage";

			/** 基建项目 */
			/** 工期控制目标 */
			public final static String Time = "TargetTime";

			/** 成本控制目标 */
			public final static String Cost = "TargetCost";

			/** 质量控制目标 */
			public final static String Quality = "TargetQuality";

		}

		/** 项目概况、设立依据、必要性与可行性附件 */
		public class Affix
		{

			public final static String TABLENAME = "FB_P_AFFIX_APPR";

			/** 流水码 */
			public final static String ROW_ID = "ROW_ID";

			/** 年度 */
			public final static String SET_YEAR = "SET_YEAR";

			/** 单位编码 */
			public final static String DIV_CODE = "DIV_CODE";

			/** 项目编码 */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** 附件标题 */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** 附件类型 */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** 文件名 */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** 附件内容 */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** 区划码 */
			public final static String RG_CODE = "RG_CODE";

			/** 最后编辑事件 */
			public final static String LAST_VER = "LAST_VER";

		}
	}

	/** 从表信息 */
	public class DetailInfo
	{

		/** 表名 */
		public final static String TableName = "FB_P_APPR_DETAIL";



		/** 项目评价指标 */
		public final static class Index
		{

			/** 指标编码 */
			public final static String DETAILCODE = "DETAILCODE";

			/** 指标名称 */
			public final static String DETAILNAME = "DETAILNAME";

			/** 类别名称 */
			public final static String[] SortNames = new String[] { "一、投入指标", "二、产出效益与效果", "三、可持续影响与发展指标", "四、社会评价指标" };

			/** 类别编码 */
			public final static String SortCode = "SortCode";

			/** 共性指标 */
			public final static String PubIndex = "PubIndex";

			/** 父节点 */
			public final static String PARCODE = "PARCODE";

			/** 父节点 */
			public final static String ENDFLAG = "END_FLAG";

			/** 个性指标 */
			public final static String SingleIndex = "SingleIndex";

			/** 个性指标 */
			public final static String SETYEAR = "SET_YEAR";
		}
	}

	public class AuditNewInfo
	{

		/** 外部审核流程流程码 */
		public final static String WID = "WID";

		/** 内部审核流程流程码 */
		public final static String NID = "NID";

		public final static String SET_YEAR = "SET_YEAR";
		/** 单位编码 */
		public final static String DIV_CODE = "DIV_CODE";
		public final static String DIV_NAME = "DIV_NAME";
		/** 项目编码 */
		public final static String PRJ_CODE = "PRJ_CODE";
		public final static String PRJ_NAME = "PRJ_NAME";
		/** 编辑时间 */
		public final static String LAST_VER = "LAST_VER";
		/** 是否退回 */
		public final static String ISBACK = "IS_BACK";



		/** 操作信息 */
		public class OperTypeInfo
		{

			/** 无操作 */
			public final static int doNull = 0;

			/** 提交审核 */
			public final static int doNext = 1;

			/** 退回 */
			public final static int doBack = -1;

			/** 退回到单位 */
			public final static int doBackToDiv = -2;
		}

		/** 步骤流程表 */
		public class AudtiStep
		{
			/** 步骤流程表 */
			public final static String TableName = "FB_P_APPR_AUDIT_WSTEP";
			/** 外部审核流程流程码 */
			public final static String WID = "WID";
			/** 外部流程编码 */
			public final static String WNAME = "WNAME";
			/** 该节点负责人 */
			public final static String PRJFZR = "OPUSERID";
			/** 该节点审核人 */
			public final static String PRJSHR = "OPID";
			/** 该节点审核是否是用户 */
			public final static String ISUSER = "ISUSER";
		}

		/** 当前项目情况表 */
		public class AuditCurPrjState
		{
			/** 当前项目情况表 */
			public final static String TableName = "FB_P_APPR_AUDIT_CURSTATE";
			/** 当前项目情况项目编码 */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** 当前项目情况外部流程编码 */
			public final static String WID = "WID";
			/** 当前项目情况内部流程编码 */
			public final static String NID = "NID";
			/** 当前项目情况外部流程是否结束 */
			public final static String WISEND = "WISEND";
			/** 当前项目情况外部流程审核意见 */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** 当前项目情况外部流程节点是否退回 */
			public final static String ISBACK = "ISBACK";
			/** 当前项目情况外部流程最后编辑时间 */
			public final static String last_ver = "last_ver";
			/** 当前项目情况预算年度 */
			public final static String SET_YEAR = "SET_YEAR";
		}

		/** 项目外部审核流程情况表 */
		public class AuditPrjWState
		{
			/** 项目外部审核流程情况表 */
			public final static String TableName = "FB_P_APPR_AUDIT_WSTATE";
			/** 项目外部审核流程项目编码 */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** 项目外部审核流程流程编码 */
			public final static String WID = "WID";
			/** 项目外部审核流程审核意见 */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** 项目外部审核流程预算年度 */
			public final static String SET_YEAR = "SET_YEAR";
			/** 项目外部审核流程是否退回 */
			public final static String ISBACK = "ISBACK";
			/** 项目外部审核流程最后编辑时间 */
			public final static String last_ver = "last_ver";

		}

		/** 项目内部审核流程情况表 */
		public class AuditPrjNState
		{
			/** 项目外部审核流程情况表 */
			public final static String TableName = "FB_P_APPR_AUDIT_NSTATE";
			/** 项目编码 */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** 内部审核流程的步骤 */
			public final static String STEP = "STEP";
			/** 内部审核流程流程码 */
			public final static String NID = "NID";
			/** 内部审核流程流程名称 */
			public final static String NNAME = "NNAME";
			/** 内部审核流程的审核意见 */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** 内部审核该节点是否已经结束 */
			public final static String ISEND = "ISEND";

			/** 内部审核该节点是否被退回 */
			public final static String ISBACK = "ISBACK";
			/** 内部审核该节点的批办人是谁 */
			public final static String PBR = "PBR";
			/** 内部审核该节点审核最后时间 */
			public final static String last_ver = "last_ver";

		}

		public class EXPERT
		{

			public final static String TableName = "FB_P_APPR_EXPERT";
			/** 编码 */
			public final static String ROW_ID = "ROW_ID";

			/** 编辑时间 */
			public final static String SET_YEAR = "SET_YEAR";

			/** 编辑时间 */
			public final static String DIV_CODE = "DIV_CODE";

			/** 编辑时间 */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** 编辑时间 */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** 编辑时间 */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** 编辑时间 */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** 编辑时间 */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** 编辑时间 */
			public final static String LAST_VER = "LAST_VER";

			/** 编辑时间 */
			public final static String BATCH_NO = "BATCH_NO";

			/** 编辑时间 */
			public final static String DATA_TYPE = "DATA_TYPE";

			/** 编辑时间 */
			public final static String EXPERT_ID = "EXPERT_ID";

			/** 编辑时间 */
			public final static String EXPERT_NAME = "EXPERT_NAME";

			/** 编辑时间 */
			public final static String EXPERT_SUG = "EXPERT_SUG";

			public final static String OPT_ID = "OPT_ID";

		}
	}

	public class APPRSORT
	{

		public final static String SORT_CODE = "SORT_CODE";

		public final static String SORT_NAME = "SORT_NAME";

		public final static String END_FLAG = "END_FLAG";

		public final static String SORT_FNAME = "SORT_FNAME";

		public final static String PAR_ID = "PAR_ID";

		public final static String SET_YEAR = "SET_YEAR";

		public final static String DIVCODES = "DIVCODES";

	}

	public class SYNBACK
	{

		public static int SENDINCEPTSTATUSWAIT = 0;

		public static int SENDINCEPTSTATUSOVER = 1;

		public static int SENDINCEPTSTATUSBACK = 2;

		public static String DIV_CODE = "div_code";

		public static String DIV_NAME = "div_name";

		public static String SEND_DIV_CODE = "send_div_code";

		public static String SEND_DIV_NAME = "send_div_name";

		public static String DEST_DIV_CODE = "dest_div_code";

		public static String DEST_DIV_NAME = "dest_div_name";

		public static String ROW_ID = "row_id";

		public static String SCHEME_NAME = "scheme_name";

		public static String TRANSMIT_MODE = "transmit_mode";

		public static String STATUS = "status";

		public static String TRANSMIT_TIME = "transmit_time";

		public static String USER_CODE = "user_code";

		public static String USER_NAME = "user_name";

	}



	/** 获取项目数据 */
	public DataSet getMainPrjData(String userCode, String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception;


	/** 获取项目明细数据 */
	public DataSet getPrjData(String userCode, String aDivCode, String PrjCode, String setYear, String filter, String batch_no, String dataType) throws Exception;


	/** 获取项目类别 */
	public DataSet getPrjSort(String setYear) throws Exception;


	/** 获取项目属性 */
	public DataSet getPrjAttr(String compID, String setYear) throws Exception;


	/** 获取全部项目数据集 */
	public DataSet getPrjDataAll(String userCode, String aDivCode, String setYear, String filter, String filter2, String batch_no, String data_type) throws Exception;


	/**
	 * 获取数据源数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getFundsData() throws Exception;


	/**
	 * 获取绩效类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPriaseTypeData(String setYear) throws Exception;


	/**
	 * 获取绩效评价指标的表体数据集(明细表数据集)
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPriaseStandData(String parCode, String setYear, String batch_no, String dataType) throws Exception;


	/**
	 * 获取项目绩效主表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjApprMainData(String prjCode, String setYear, String batch_no, String dataType) throws Exception;


	/**
	 * 保存信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public InfoPackage saveData(DataSet dsMain, DataSet dsDetail);


	public void modifyDocBlobs(String tableName, String rowid, String set_year, byte[] longColData) throws Exception;


	public byte[] getDocBlob(String tableName, int i, String prjCode, String setYear) throws SQLException, IOException, ClassNotFoundException;


	public void doChoosePrj(String filter, int batch_no, int data_type) throws Exception;


	public InfoPackage doCancelChoosePrj(String filter, int batch_no, int data_type) throws Exception;


	public DataSet getSchemeGroup(String SCHEME_CODE);


	public DataSet getAffixInfo(String divCode, String prjCode, String setYear, String batch_no, String data_type) throws Exception;


	public void dsPost(DataSet ds) throws Exception;


	public DataSet getDivDataPop(String sYear, int batch_no) throws Exception;


	public DataSet getDepToDivData(String sYear, int levl, boolean isRight, int batch_no) throws Exception;


	/**
	 * 获取绩效项目审核信息
	 * 
	 * @param type
	 *            0:审核表 1：历史表
	 * @param divCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditInfo(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter, String flowstatus,
			String moduleId) throws Exception;


	public DataSet getAuditInfoforInput(String[] prjCode) throws Exception;


	public List getAuditSqlInfoForSend(int isback, int isend, boolean backtoback, String filter, String[] divCodes, String loginUserCode, String setYear) throws Exception;


	public List getStepAllInfo(String setYear);


	public DataSet getStepNowInfo(String prjCode) throws Exception;


	public DataSet getStepAllData(String setYear) throws Exception;


	public DataSet getUserToPrj(String setYear, String userCode) throws Exception;


	public DataSet getUserInfo(String setYear) throws Exception;


	public DataSet getMainPrjDataNotUserTo(String userCode, String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception;


	public void doChoosePrjToUser(String userCode, String PrjCodefilter, String setYear, int batch_no, int data_type) throws Exception;


	public InfoPackage doCancelChoosePrjToUser(String userCode, String PrjCodefilter, String setYear, int batch_no, int data_type) throws Exception;


	/**
	 * 保存拆分合并项目数据
	 * 
	 * @param ds
	 * @param isSaveOldData
	 *            是否保存原有项目
	 * @param oldPrjCode
	 *            源项目码
	 * @param setYear
	 *            预算年度
	 * @throws Exception
	 */
	public void saveApprSptBtnData(boolean isSplit, DataSet ds, String oldPrjCode, String setYear, int batch_no, int data_type) throws Exception;


	public DataSet getLockInfoPrj(boolean isLock, String divCode, String setYear) throws Exception;


	public DataSet getAuditElement(boolean isUser, String dep_id) throws Exception;


	public DataSet getAuditStepInfo() throws Exception;


	public DataSet getPrjAuditStepInfo() throws Exception;


	/**
	 * 批办操作
	 * 
	 * @param userCode
	 *            批办人
	 * @param prjCodes
	 *            项目编码
	 * @param ToUserCodes
	 *            处理人编码
	 * @param toUserNames
	 *            处理人名称
	 * @param curTime
	 *            当前时间
	 * @param wid
	 *            外部流程编码
	 * @param setYear
	 *            预算年度
	 * @return InfoPackage
	 * @throws Exception
	 */
	public int[] getxmshZT(String userCode, String SetYear) throws Exception;


	public InfoPackage sentPrjToUser(String userCode, String[] prjCodes, String[] ToUserCodes, String[] canUserCodes, String[] toUserNames, String curTime, String setYear, String wid, String batch_no)
			throws Exception;


	public DataSet getMainPrjDataNotLock(String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception;


	public DataSet getMainPrjDataLock(String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception;


	public void doPostImportData(DataSet dsAppr, DataSet dsApprDetail, String prjCode, int batchNo, int dataType) throws Exception;


	public InfoPackage isRightUserWhenSend(String[] userID, String sYear) throws Exception;


	public InfoPackage setPrjSort(String sortCode, String sortName, String sortFName, String filter) throws Exception;


	public InfoPackage setPrjDivInfo(String divCode, String filter) throws Exception;


	public DataSet getSortData(String setYear) throws Exception;



	public class PrjAuditTable
	{

		public class PrjAuditSetTable
		{

			public final static String TABLENAME_AUDIT_SET = "RP_S_AUDIT_STEP";

			public final static String STEP_ID = "STEP_ID";

			public final static String STEP_NAME = "STEP_NAME";

			public final static String AUDIT_OPER = "AUDIT_OPER";

			public final static String IS_USER = "IS_USER";

			public final static String IS_INSERTHIS = "IS_INSERTHIS";

			public final static String SET_YEAR = "SET_YEAR";

		}

		public class Affix
		{

			public final static String TABLENAME = "RP_AUDIT_AFFIX";

			/** 流水码 */
			public final static String ROW_ID = "ROW_ID";

			/** 年度 */
			public final static String SET_YEAR = "SET_YEAR";

			/** 单位编码 */
			public final static String DIV_CODE = "DIV_CODE";

			/** 项目编码 */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** 附件标题 */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** 附件类型 */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** 文件名 */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** 附件内容 */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** 区划码 */
			public final static String RG_CODE = "RG_CODE";

			/** 最后编辑事件 */
			public final static String LAST_VER = "LAST_VER";

		}



		public final static String TABLENAME_AUDIT_HIS = "RP_AUDIT_HIS";

		public final static String TABLENAME_AUDIT_CUR = "RP_AUDIT_CUR";

		public final static String row_id = "row_id";

		public final static String div_code = "div_code";

		public final static String div_name = "div_name";

		public final static String prj_code = "prj_code";

		public final static String prj_name = "prj_name";

		public final static String audit_userid = "audit_userid";

		public final static String audit_username = "audit_username";

		public final static String audit_state = "audit_state";

		public final static String is_back = "is_back";

		public final static String audit_time = "audit_time";

		public final static String audit_text = "audit_text";

		public final static String set_year = "set_year";

		public final static String rg_code = "rg_code";

		public final static String AUDIT_ISEND = "audit_isend";

		public final static String EN_ID = "rg_code";

	}



	public DataSet getFinshAuditInfo(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter)
			throws Exception;


	public InfoPackage backFinshAudit(List prjCodes, String aUserCode, String rgCode, String moduleid, int type) throws Exception;


	public InfoPackage finshAudit(List prjCodes, String aUserCode, String rgCode, String setYear, String moduleid, int type) throws Exception;


	// 附件保存
	public String saveAffixFile(String tableName, String en_code, String filename, String setYear, byte[] bytes, String path) throws Exception;


	// 附件保存
	public String saveAffixFiles(String tableName, String en_code, String filename, String setYear, byte[] bytes, String path) throws Exception;


	// 附件查看
	public byte[] getFileFind(String tableName, String rowid, String setYear) throws Exception;


	public byte[] getFileFinds(String enId, String fileName, String setYear, String suptype, String rgCode) throws Exception;


	public int[] executeBatch(List sql) throws Exception;


	public boolean execute(String sql) throws Exception;


	// public boolean file_sure(int user,int sure,String name ) throws
	// Exception;

	public DataSet getAuditHis(String xmxh, String year, String rgCode, String aduitRole, String p_no) throws Exception;
}
