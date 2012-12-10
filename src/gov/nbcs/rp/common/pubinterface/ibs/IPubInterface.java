/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.pubinterface.ibs;

import java.util.List;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

public interface IPubInterface {
	// ��λ��Ϣ
	String DIV_ID = "EN_ID"; // ��λID ������������

	String DIV_PARENT_ID = "PARENT_ID"; // ��λID������ĸ���

	String DIV_NAME = "DIV_NAME"; // ��λ����

	String DIV_CODE_NAME = "CODE_NAME"; // ��λ��ʾ������Ƶĺϲ�

	String DIV_CODE = "DIV_CODE"; // ��λ��ʾ��,һ��ΪCODE

	String DIV_KIND = "DIV_KIND"; // ��λ����

	// ���ÿ�Ŀ
	String BSI_ID = "BSI_ID"; // ���ÿ�ĿID ������������

	String BSI_PARENT_ID = "PARENT_ID"; // ID������ĸ���

	String ACCT_CODE_JJ = "ACCT_CODE_JJ"; // ���ÿ�Ŀ����

	String ACCT_NAME_JJ = "ACCT_NAME_JJ"; // ���ÿ�Ŀ����

	String ACCT_JJ_FNAME = "FULLNAME"; // ��������Ƶĺϲ�

	// ���ܿ�Ŀ
	String BS_ID = "BS_ID"; // ���ܿ�ĿID ������������

	String BS_PARENT_ID = "PARENT_ID"; // ID������ĸ���

	String ACCT_CODE = "ACCT_CODE"; // ���ܿ�Ŀ����

	String ACCT_NAME = "ACCT_NAME"; // ���ܿ�Ŀ����

	String ACCT_FNAME = "FULLNAME"; // ��������Ƶĺϲ�

	// ��õ�λ��Ϣ���ݼ�
	public DataSet getDivData(String sYear) throws Exception;

	public DataSet getDivDataPop(String sYear) throws Exception; // ��Ȩ�޹���

	public DataSet getDepToDivData(String sYear, int levl, boolean isRight)
			throws Exception; // �����ŷ���ĵ�λ

	/**
	 * Gets the div data by my dep.
	 * 
	 * �Ե�ǰ��¼�û�Ȩ�ޣ���ȡ��λ��Ϣ�Ӽ�������ģʽ��
	 * 
	 * @param sYear
	 *            the s year
	 * @param levl
	 *            the levl
	 * @param isRight
	 *            the value is true if it is right
	 * 
	 * @return the div data by my dep
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public DataSet getDivDataByMyRight(String sYear, int levl, boolean isRight)
			throws Exception; // �����ŷ���ĵ�λ

	// ��þ��ÿ�Ŀ���ݼ�
	public DataSet getAcctEconomy(String filter) throws Exception;

	// ��ù��ܿ�Ŀ���ݼ�
	public DataSet getAcctFunc(String filter) throws Exception;

	// ���ݹ������͵õ��������
	public SysCodeRule getCodeRule(int nYear, String ruleType) throws Exception;

	// �������ݱ��滻�ַ���
	public String replaceTextEx(String sValue, int intType, String aTable,
			String nowField, String endField, String aFilter) throws Exception;

	// �������ݼ��滻�ַ���
	public String replaceTextExDs(String sValue, int intType, DataSet dsTable,
			String nowField, String endField) throws Exception;

	// ���ã����ѷ���������ݼ��в��ҳ����к��ʵĶ�Ӧ�ֶΣ���ϵͳ�еĶ����������ֶζ�����F1��F2��F3����ʽ���ڵ�
	public String assignNewCol(String sFieldName, String sFieldPrefix,
			int iMaxFieldNum, String sTableName, String nYear, String sFilter)
			throws Exception;

	// �������ܣ����ݹ����ڶ�Ӧ�����ݱ��л�ȡ�µĽڵ�code
	public String getNodeID(String aTableName, String aFieldName,
			String aParID, String aFilter, SysCodeRule codeRule)
			throws Exception;

	// �������ܣ����ݱ��볤�����ɱ�����󳤶ȵ�code
	public String getMaxCode(String aTableName, String aFieldName,
			String aFilter, int intL) throws Exception;

	/**
	 * ��ȡ��ĳ���ֶ����ֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param fieldName
	 *            �ֶ���
	 */
	public int getMaxValue(String tableName, String fieldName);

	/**
	 * ��ȡ��ĳ���ֶ���Сֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param fieldName
	 *            �ֶ���
	 */
	public int getMinValue(String tableName, String fieldName);

	/**
	 * ���ݴ�����û�ID,��ȡ��Ӧ���û�������������
	 * 
	 * @param user_id
	 * @return
	 * @author lixf
	 */
	public String getUserBelongType(String user_id) throws Exception;

	/**
	 * ���ݴ�����û�ID,��ȡ��Ӧ���û��������ұ���
	 * 
	 * @param userBeLong
	 * @return
	 * @throws Exception
	 */
	public String getBranchCode(String userBeLong) throws Exception;

	/**
	 * ���ݴ����en_id����DIV_CODE
	 * 
	 * @param en_id
	 * @return
	 * @throws Exception
	 */
	public String getEnDivCode(String en_id) throws Exception;

	/**
	 * ��ȡ��λ����
	 * 
	 * @param en_id
	 * @return
	 * @throws Exception
	 */
	public int getEnDivLevelNum(String en_id) throws Exception;

	/**
	 * ��ȡ��λ/���� �Ƿ���Ա༭��ǰ��Ŀ����
	 * 
	 * @param aPrjCode
	 *            ��Ŀ����
	 * @param userCode
	 *            �û��� �ͻ���ֱ�Ӵ���Global.user_code
	 * @param depID
	 *            ������� �ͻ���ֱ�Ӵ���GlobalEx.getUserBeLong()
	 * @return InfoPackage getSucess()==true ���Ա༭ false:�����Ա༭
	 *         ͨ��getsMessage()��ȡ��ʾ��Ϣ
	 * @throws Exception
	 */
	public InfoPackage getAuditCanEdit(String aPrjCode, String userCode,
			String depID, String moduleid) throws Exception;

	/**
	 * ��λ������Ŀ
	 * 
	 * @param prjCode
	 *            ��Ŀ����
	 * @return InfoPackage getSucess()==true ����ɹ� false:����ʧ��
	 *         ͨ��getsMessage()��ȡ��ʾ��Ϣ
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes,
			String aUserCode, String rgCode, String moduleid) throws Exception;

	public InfoPackage BackAuditTZInfoByDiv(int type, String[] prjCodes,
			String aUserCode, String rgCode, String moduleid) throws Exception;

	public InfoPackage sendAuditInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public InfoPackage startflowInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public String getCurState(String setYear, String rgCode) throws Exception;

	public String getSupportNValue(String supType) throws Exception;

	public DataSet getOptDataTypeList() throws Exception;

	public String savehistory(String[] prjCodes,boolean isSave) throws Exception;

	public String saveTZhistory(String[] prjCodes) throws Exception;

	public InfoPackage sendTZAuditInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public int getCurBatchNO() throws Exception;

}
