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
	// fb_u_qr_repset��
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

	String TYPE_FLAG = "TYPE_FLAG"; // �������� 0����ͨ����1����֧�ܱ� ��2���б���3:���棬9���ۺϴ��ı�

	String LVL_ID = "LVL_ID";

	String END_FLAG = "END_FLAG";

	// fb_u_qr_colset��
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

	// ��λ
	String EN_ID = "EID";

	String DIV_CODE = "DIV_CODE";

	String PARENT_ID = "PARENT_ID";

	String CODE_NAME = "CODE_NAME";

	String ID = "ID";

	String NAME = "NAME";

	// FB_U_QR_QUERYVER��(��ѯ�汾)
	String VER_NO = "VER_NO";

	String VER_REMARK = "VER_REMARK";

	String VER_DATE = "VER_DATE";

	String ADD = "ADD";

	String MODIFY = "MODIFY";

	String FULLNAME = "FULLNAME";

	// ��λ�ϱ���δ�ϱ����
	String SEND = "SEND";// ���ϱ�

	String UNSEND = "UNSEND";// δ�ϱ�

	String UUID = "UUID";

	String RG_CODE = "RG_CODE";

	public class ColDispInf {
		public String sFieldType = new String();

		public String sFieldDisformat = new String();

		public Style style = Style.getInstance();

		public String fieldEName = "";
	}

	public class DivObject {
		public String sDivCode = new String(); // ��λ����

		public boolean isLeaf; // �Ƿ�Ҷ�ڵ㣬true:Ҷ�ڵ㣬 false ������Ҷ�ڵ�
	}

	public class BodyInfo {

		public List lstBody;

		public String sSqlBody;
	}

	/**
	 * �õ�������Ϣ
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportName(int iUserType, String setYear,
			String reportType) throws Exception;

	/**
	 * �õ���λ����
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getDivName(String setYear, int iLevel, int iUserType)
			throws Exception;

	/**
	 * �õ������ͷsql
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
	 * �õ������ͷ
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportHeader(String sReportId, String setYear)
			throws Exception;

	/**
	 * �����п�
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
	 * �õ���ѯ����
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
	 * �����м���ͼ,�õ���ѯ���
	 * 
	 * @param sReportID
	 * @param sBatchNoFilter
	 *            �Ƿ������εĲ�ѯ����
	 * @param lstDept
	 *            ѡ�е�ҵ�����б�
	 * @param lstDiv
	 *            ѡ�еĵ�λ�б�
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public List getData(String sVerNo, String sReportID, String sBatchNoFilter,
			List lstDept, List lstDiv, String sFieldSelect, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag) throws Exception;

	/**
	 * ���ز�ѯ���
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
	 * ��λ��ѯ
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
	// chcx add �汾����2007.09.09
	// �õ��汾��Ϣ
	public DataSet getVerInfo() throws Exception;

	/**
	 * ���ݱ���õ��汾��Ϣ,author:zlx
	 */
	public DataSet getVerInfoWithReport(String sReportId) throws Exception;

	// ɾ���汾��Ϣ
	public InfoPackage delVerInfo(String verNo) throws Exception;

	// ����汾��Ϣ
	public InfoPackage saveVerInfo(String verNo, String verDate,
			String verRemark, String EditorType) throws Exception;

	// ɾ���汾�����
	public InfoPackage DelTableReportVer(String verNo) throws Exception;

	public DataSet getReportInfo(String verNo) throws Exception;

	// ��������汾�����
	public InfoPackage CreatTabelReportVer(String verNo, String reportId,
			String sqllines) throws Exception;

	// �ϱ���δ�ϱ���ѯ����
	public DataSet getCondition() throws Exception;

	// ��λ�����ͷ
	public DataSet getEnterpriseReportHeader() throws Exception;

	// ��λ�ϱ����
	public DataSet getSendEnterprise(String sendFlag) throws Exception;

	/**
	 * ��ѯ���ݣ����������ݺͲ�ѯ�����,ע���ص��ֶ�������Сд
	 * 
	 * @param sVerNo
	 *            �汾��
	 * @param sBatchNoFilter
	 *            ����
	 * @param lstDept
	 *            ��������
	 * @param lstDiv
	 *            ��λ����
	 * @param setYear
	 *            ���
	 * @param iUserType
	 *            �û�����
	 * @param iLoginmode
	 *            ��¼��ʽ
	 * @param iTypeFlag
	 *            ��������
	 * @param reportInfo
	 *            ������Ϣ
	 * @return List�е�һ��Ϊ����LIST���ڶ���Ϊһ��OriSearchObj���Ա�����
	 * @throws Exception
	 */

	public List getOriData(String sVerNo, String sBatchNoFilter, List lstDept,
			List lstDiv, String setYear, int iUserType, int iLoginmode,
			int iTypeFlag, ReportInfo reportInfo, ConditionObj conditionObj)
			throws Exception;

	/**
	 * ��ѯ���ݣ����������ݺͲ�ѯ�����,ע���ص��ֶ�������Сд,���ݲ�ѯ����б�
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
	 * ��ѯ���ݸ��ݲ�ѯ�����󣬲���������
	 * 
	 * @param sVerNo
	 *            �汾��
	 * @param sBatchNoFilter
	 *            ����
	 * @param lstDept
	 *            ��������
	 * @param lstDiv
	 *            ��λ����
	 * @param setYear
	 *            ���
	 * @param iUserType
	 *            �û�����
	 * @param iLoginmode
	 *            ��¼��ʽ
	 * @param iTypeFlag
	 *            ��������
	 * @param lstSqlLines
	 *            ��ѯ���
	 * @return List��ѯ����
	 * @throws Exception
	 */
	public OriSearchObj getSearchObjWhere(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			List lstSqlLines, ConditionObj conditionObj) throws Exception;

	/**
	 * ȡ�ò�ѯ��
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
	 * �õ�����ͷ(����������)
	 * 
	 * @param sReportId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportHeader_A(String sReportId) throws Exception;

	/**
	 * �õ���ִ�нű�
	 * 
	 * @param iLoginMode
	 *            ��½��ʽ
	 * @return ��ѯ����б�
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getScriptInfo(int iLoginMode) throws SQLException, IOException;

	/**
	 * ִ�нű�
	 * 
	 * @param sSql
	 *            �ű�
	 * @throws Exception
	 */
	public void exeScript(String sSql) throws Exception;

	/**
	 * �õ���λ������ֵ
	 * 
	 * @param sDivCode
	 */
	public List getDivRae(String sDivCode);

	/**
	 * ����Ԥ����±�---2009��֧��Ԥ�����´���,���ڻ���֧��
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownJBZC(String divCode, int batchNo, int dataType)
			throws Exception;

	/**
	 * ����Ԥ����±�---2009��֧��Ԥ�����´���,����ר����ϸ
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownPrjDetail(String prjType, String divCode,
			int batchNo, int dataType, boolean isPrjGovJj) throws Exception;

	/**
	 * ����Ԥ����±�---2009�������ɹ�Ԥ��֪ͨ��
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDownGov(String divCode, int batchNo, int dataType,
			boolean isPrjGovJj) throws Exception;

	/**
	 * �����п�(��֧�ܱ�ͷ���Ŀ¼��
	 * 
	 * @throws Exception
	 */
	public void saveSzzbColWidth(String sReportId, String setYear,
			List lstColWidth) throws Exception;

	// ��ѯ����0����ʾ���պͽ�����ͬ�ĵ��� ��1����ʾ����
	public int getSearchType(String sYear) throws Exception;

	/**
	 * ����Ԥ����±�---2010������Ԥ�����´���
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDown(String divCode, int batchNo, int dataType)
			throws Exception;

	public Object[] getBudgetDownBz(String divCode, int batchNo, int dataType)
			throws Exception;

}
