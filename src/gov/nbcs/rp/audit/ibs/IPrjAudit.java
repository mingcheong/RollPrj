/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title ��Ŀ��Ч-�ӿ���
 * 
 * @author Ǯ�Գ�
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

	/** �����ϱ�����group_code */
	public final static String UPGROUPCODE = "{0FF37881-C447-11DE-B912-EBA08E614509}";

	public final static String DOWNGROUPCODE = "{61887066-C446-11DE-B912-EBA08E614509}";

	/** ָ������ */
	public int IndexRowCount = 4;

	/** ���� */
	public String PRJAPPR_BATCH_NO = "2";

	/** �������� */
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

		/** ��λ���� */
		public final static String DIV_CODE = "DIV_CODE";

		/** ��λ���� */
		public final static String DIV_NAME = "DIV_NAME";

		/** ��Ŀ���� */
		public final static String PRJ_CODE = "PRJ_CODE";

		/** ��Ŀ���� */
		public final static String PRJ_NAME = "PRJ_NAME";

		/** ��Ŀ������ */
		public final static String PRJSORT_CODE = "PRJSORT_CODE";

		/** ��Ŀ������� */
		public final static String PRJSORT_NAME = "PRJSORT_NAME";

		/** ��Ŀ���Ա��� */
		public final static String PRJATTR_CODE = "PRJATTR_CODE";

		/** ��Ŀ�������� */
		public final static String PRJATTR_NAME = "PRJATTR_NAME";

		/** ��Ŀ���ݱ��� */
		public final static String PRJACCORD_CODE = "PRJACCORD_CODE";

		/** ��Ŀ�������� */
		public final static String PRJACCORD_NAME = "PRJACCORD_NAME";

		/** ��Ŀ���ͱ��� */
		public final static String PRJ_TRANSFER = "PRJ_TRANSFER";

		/** ��Ŀ�������� */
		public final static String PRJ_TRANSFER_NAME = "PRJ_TRANSFER_NAME";

		/** ��Ŀ���� */
		public final static String PRJ_CONTENT = "PRJ_CONTENT";

		/** ��Ŀ���� */
		public final static String ACCT_CODE = "ACCT_CODE";

		/** ��Ŀ���� */
		public final static String ACCT_NAME = "ACCT_NAME";

	}

	/** ������Ϣ */
	public class MainTable
	{

		/** ���� */
		public final static String TableName = "FB_P_APPR";

		/** ��Ŀ��Ч���� */
		public final static String PraiseType = "PRJAPPRAISETYPE";



		/** ��Ŀ�ſ� */
		public final static class GeneralInfo
		{

			/** ��ˮ�� */
			public final static String ROW_ID = "ROW_ID";

			/** ��Ŀ���� */
			public final static String PrjCode = "PRJCODE";

			/** �Ƿ����Ч���� */
			public final static String IsInputPrjAppr = "ISINPUTPRJAPPR";

			/** ��Ч���ͱ��� */
			public final static String SORTCODE = "SORTCODE";

			/** ��Ŀ�걨���� */
			public final static String DECLAREDepartMent = "SBDEPARTMENT";

			/** ��Ŀ������ */
			public final static String DECLAREManager = "SBMANAGER";

			/** ��ϵ�绰 */
			public final static String DECLARETel = "SBTEL";

			/** ��Ŀʵʩ���� */
			public final static String EXECUTEDEpartMent = "EXECUTEDEPARTMENT";

			/** ��Ŀ������ */
			public final static String EXECUTEManager = "EXECUTEMANAGER";

			/** ��ϵ�绰 */
			public final static String EXECUTETel = "EXECUTETEL";

			/** ��Ŀ�ʽ��걨�ƻ�����Ԫ�� */
			/** �ʽ��ܶ� */
			public final static String TotleMoney = "TOTLEMONEY";

			/** �������ʽ� */
			public final static String FinanceMoney = "FINANCEMONEY";

			/** ��Ŀ��λ�Գ� */
			public final static String DivColMoney = "DIVCOLMONEY";

			/** �����ʽ� */
			public final static String OtherMoney = "OTHERMONEY";

			/** ��Ŀ��ֹʱ�� */
			/** Ԥ�ƿ�ʼʱ�� */
			public final static String PrjBeginDate = "PRJBEGINDATE";

			/** Ԥ�ƽ���ʱ�� */
			public final static String PrjEndDate = "PRJENDDATE";

			/** ��Ŀ�ſ����������ݡ���Ҫ��������� */
			public final static String PrjGeneralContent = "PRJGENERALCONTENT";

			/** ��ЧĿ��Ԥ��ʵ�ֽ��ȣ�����Ŀ��ʵ���ȼƻ��� */
			public final static String Speed = "SPEED";

			/** ��ǰ�����Ŀ��Ч����������������Ŀ�� */
			public final static String OldApprAbout = "OLDAPPRABOUT";

			/** ��Ŀ�ʽ�Ͷ��ʹ�ã��ƻ� */
			public final static String FUNDSPLAN = "FUNDSPLAN";
		}

		/** ��Ŀ��ЧĿ�� */
		public class Target
		{
			/** ��Ŀ�� */
			public final static String General = "TargetGeneral";

			/** �׶���Ŀ�� */
			public final static String Stage = "TargetStage";

			/** ������Ŀ */
			/** ���ڿ���Ŀ�� */
			public final static String Time = "TargetTime";

			/** �ɱ�����Ŀ�� */
			public final static String Cost = "TargetCost";

			/** ��������Ŀ�� */
			public final static String Quality = "TargetQuality";

		}

		/** ��Ŀ�ſ����������ݡ���Ҫ��������Ը��� */
		public class Affix
		{

			public final static String TABLENAME = "FB_P_AFFIX_APPR";

			/** ��ˮ�� */
			public final static String ROW_ID = "ROW_ID";

			/** ��� */
			public final static String SET_YEAR = "SET_YEAR";

			/** ��λ���� */
			public final static String DIV_CODE = "DIV_CODE";

			/** ��Ŀ���� */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** �������� */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** �������� */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** �ļ��� */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** �������� */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** ������ */
			public final static String RG_CODE = "RG_CODE";

			/** ���༭�¼� */
			public final static String LAST_VER = "LAST_VER";

		}
	}

	/** �ӱ���Ϣ */
	public class DetailInfo
	{

		/** ���� */
		public final static String TableName = "FB_P_APPR_DETAIL";



		/** ��Ŀ����ָ�� */
		public final static class Index
		{

			/** ָ����� */
			public final static String DETAILCODE = "DETAILCODE";

			/** ָ������ */
			public final static String DETAILNAME = "DETAILNAME";

			/** ������� */
			public final static String[] SortNames = new String[] { "һ��Ͷ��ָ��", "��������Ч����Ч��", "�����ɳ���Ӱ���뷢չָ��", "�ġ��������ָ��" };

			/** ������ */
			public final static String SortCode = "SortCode";

			/** ����ָ�� */
			public final static String PubIndex = "PubIndex";

			/** ���ڵ� */
			public final static String PARCODE = "PARCODE";

			/** ���ڵ� */
			public final static String ENDFLAG = "END_FLAG";

			/** ����ָ�� */
			public final static String SingleIndex = "SingleIndex";

			/** ����ָ�� */
			public final static String SETYEAR = "SET_YEAR";
		}
	}

	public class AuditNewInfo
	{

		/** �ⲿ������������� */
		public final static String WID = "WID";

		/** �ڲ�������������� */
		public final static String NID = "NID";

		public final static String SET_YEAR = "SET_YEAR";
		/** ��λ���� */
		public final static String DIV_CODE = "DIV_CODE";
		public final static String DIV_NAME = "DIV_NAME";
		/** ��Ŀ���� */
		public final static String PRJ_CODE = "PRJ_CODE";
		public final static String PRJ_NAME = "PRJ_NAME";
		/** �༭ʱ�� */
		public final static String LAST_VER = "LAST_VER";
		/** �Ƿ��˻� */
		public final static String ISBACK = "IS_BACK";



		/** ������Ϣ */
		public class OperTypeInfo
		{

			/** �޲��� */
			public final static int doNull = 0;

			/** �ύ��� */
			public final static int doNext = 1;

			/** �˻� */
			public final static int doBack = -1;

			/** �˻ص���λ */
			public final static int doBackToDiv = -2;
		}

		/** �������̱� */
		public class AudtiStep
		{
			/** �������̱� */
			public final static String TableName = "FB_P_APPR_AUDIT_WSTEP";
			/** �ⲿ������������� */
			public final static String WID = "WID";
			/** �ⲿ���̱��� */
			public final static String WNAME = "WNAME";
			/** �ýڵ㸺���� */
			public final static String PRJFZR = "OPUSERID";
			/** �ýڵ������ */
			public final static String PRJSHR = "OPID";
			/** �ýڵ�����Ƿ����û� */
			public final static String ISUSER = "ISUSER";
		}

		/** ��ǰ��Ŀ����� */
		public class AuditCurPrjState
		{
			/** ��ǰ��Ŀ����� */
			public final static String TableName = "FB_P_APPR_AUDIT_CURSTATE";
			/** ��ǰ��Ŀ�����Ŀ���� */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** ��ǰ��Ŀ����ⲿ���̱��� */
			public final static String WID = "WID";
			/** ��ǰ��Ŀ����ڲ����̱��� */
			public final static String NID = "NID";
			/** ��ǰ��Ŀ����ⲿ�����Ƿ���� */
			public final static String WISEND = "WISEND";
			/** ��ǰ��Ŀ����ⲿ���������� */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** ��ǰ��Ŀ����ⲿ���̽ڵ��Ƿ��˻� */
			public final static String ISBACK = "ISBACK";
			/** ��ǰ��Ŀ����ⲿ�������༭ʱ�� */
			public final static String last_ver = "last_ver";
			/** ��ǰ��Ŀ���Ԥ����� */
			public final static String SET_YEAR = "SET_YEAR";
		}

		/** ��Ŀ�ⲿ������������ */
		public class AuditPrjWState
		{
			/** ��Ŀ�ⲿ������������ */
			public final static String TableName = "FB_P_APPR_AUDIT_WSTATE";
			/** ��Ŀ�ⲿ���������Ŀ���� */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** ��Ŀ�ⲿ����������̱��� */
			public final static String WID = "WID";
			/** ��Ŀ�ⲿ������������� */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** ��Ŀ�ⲿ�������Ԥ����� */
			public final static String SET_YEAR = "SET_YEAR";
			/** ��Ŀ�ⲿ��������Ƿ��˻� */
			public final static String ISBACK = "ISBACK";
			/** ��Ŀ�ⲿ����������༭ʱ�� */
			public final static String last_ver = "last_ver";

		}

		/** ��Ŀ�ڲ������������� */
		public class AuditPrjNState
		{
			/** ��Ŀ�ⲿ������������ */
			public final static String TableName = "FB_P_APPR_AUDIT_NSTATE";
			/** ��Ŀ���� */
			public final static String PRJ_CODE = "PRJ_CODE";
			/** �ڲ�������̵Ĳ��� */
			public final static String STEP = "STEP";
			/** �ڲ�������������� */
			public final static String NID = "NID";
			/** �ڲ���������������� */
			public final static String NNAME = "NNAME";
			/** �ڲ�������̵������� */
			public final static String AUDITCONTEXT = "AUDITCONTEXT";
			/** �ڲ���˸ýڵ��Ƿ��Ѿ����� */
			public final static String ISEND = "ISEND";

			/** �ڲ���˸ýڵ��Ƿ��˻� */
			public final static String ISBACK = "ISBACK";
			/** �ڲ���˸ýڵ����������˭ */
			public final static String PBR = "PBR";
			/** �ڲ���˸ýڵ�������ʱ�� */
			public final static String last_ver = "last_ver";

		}

		public class EXPERT
		{

			public final static String TableName = "FB_P_APPR_EXPERT";
			/** ���� */
			public final static String ROW_ID = "ROW_ID";

			/** �༭ʱ�� */
			public final static String SET_YEAR = "SET_YEAR";

			/** �༭ʱ�� */
			public final static String DIV_CODE = "DIV_CODE";

			/** �༭ʱ�� */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** �༭ʱ�� */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** �༭ʱ�� */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** �༭ʱ�� */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** �༭ʱ�� */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** �༭ʱ�� */
			public final static String LAST_VER = "LAST_VER";

			/** �༭ʱ�� */
			public final static String BATCH_NO = "BATCH_NO";

			/** �༭ʱ�� */
			public final static String DATA_TYPE = "DATA_TYPE";

			/** �༭ʱ�� */
			public final static String EXPERT_ID = "EXPERT_ID";

			/** �༭ʱ�� */
			public final static String EXPERT_NAME = "EXPERT_NAME";

			/** �༭ʱ�� */
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



	/** ��ȡ��Ŀ���� */
	public DataSet getMainPrjData(String userCode, String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception;


	/** ��ȡ��Ŀ��ϸ���� */
	public DataSet getPrjData(String userCode, String aDivCode, String PrjCode, String setYear, String filter, String batch_no, String dataType) throws Exception;


	/** ��ȡ��Ŀ��� */
	public DataSet getPrjSort(String setYear) throws Exception;


	/** ��ȡ��Ŀ���� */
	public DataSet getPrjAttr(String compID, String setYear) throws Exception;


	/** ��ȡȫ����Ŀ���ݼ� */
	public DataSet getPrjDataAll(String userCode, String aDivCode, String setYear, String filter, String filter2, String batch_no, String data_type) throws Exception;


	/**
	 * ��ȡ����Դ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getFundsData() throws Exception;


	/**
	 * ��ȡ��Ч����
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPriaseTypeData(String setYear) throws Exception;


	/**
	 * ��ȡ��Ч����ָ��ı������ݼ�(��ϸ�����ݼ�)
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPriaseStandData(String parCode, String setYear, String batch_no, String dataType) throws Exception;


	/**
	 * ��ȡ��Ŀ��Ч������Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjApprMainData(String prjCode, String setYear, String batch_no, String dataType) throws Exception;


	/**
	 * ������Ϣ
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
	 * ��ȡ��Ч��Ŀ�����Ϣ
	 * 
	 * @param type
	 *            0:��˱� 1����ʷ��
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
	 * �����ֺϲ���Ŀ����
	 * 
	 * @param ds
	 * @param isSaveOldData
	 *            �Ƿ񱣴�ԭ����Ŀ
	 * @param oldPrjCode
	 *            Դ��Ŀ��
	 * @param setYear
	 *            Ԥ�����
	 * @throws Exception
	 */
	public void saveApprSptBtnData(boolean isSplit, DataSet ds, String oldPrjCode, String setYear, int batch_no, int data_type) throws Exception;


	public DataSet getLockInfoPrj(boolean isLock, String divCode, String setYear) throws Exception;


	public DataSet getAuditElement(boolean isUser, String dep_id) throws Exception;


	public DataSet getAuditStepInfo() throws Exception;


	public DataSet getPrjAuditStepInfo() throws Exception;


	/**
	 * �������
	 * 
	 * @param userCode
	 *            ������
	 * @param prjCodes
	 *            ��Ŀ����
	 * @param ToUserCodes
	 *            �����˱���
	 * @param toUserNames
	 *            ����������
	 * @param curTime
	 *            ��ǰʱ��
	 * @param wid
	 *            �ⲿ���̱���
	 * @param setYear
	 *            Ԥ�����
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

			/** ��ˮ�� */
			public final static String ROW_ID = "ROW_ID";

			/** ��� */
			public final static String SET_YEAR = "SET_YEAR";

			/** ��λ���� */
			public final static String DIV_CODE = "DIV_CODE";

			/** ��Ŀ���� */
			public final static String PRJ_CODE = "PRJ_CODE";

			/** �������� */
			public final static String AFFIX_TITLE = "AFFIX_TITLE";

			/** �������� */
			public final static String AFFIX_TYPE = "AFFIX_TYPE";

			/** �ļ��� */
			public final static String AFFIX_FILE = "AFFIX_FILE";

			/** �������� */
			public final static String AFFIX_CONT = "AFFIX_CONT";

			/** ������ */
			public final static String RG_CODE = "RG_CODE";

			/** ���༭�¼� */
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


	// ��������
	public String saveAffixFile(String tableName, String en_code, String filename, String setYear, byte[] bytes, String path) throws Exception;


	// ��������
	public String saveAffixFiles(String tableName, String en_code, String filename, String setYear, byte[] bytes, String path) throws Exception;


	// �����鿴
	public byte[] getFileFind(String tableName, String rowid, String setYear) throws Exception;


	public byte[] getFileFinds(String enId, String fileName, String setYear, String suptype, String rgCode) throws Exception;


	public int[] executeBatch(List sql) throws Exception;


	public boolean execute(String sql) throws Exception;


	// public boolean file_sure(int user,int sure,String name ) throws
	// Exception;

	public DataSet getAuditHis(String xmxh, String year, String rgCode, String aduitRole, String p_no) throws Exception;
}
