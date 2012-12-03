package gov.nbcs.rp.input.ibs;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;
import java.util.Map;




public interface IPrjInput
{

	public final static String ROW_ID = "ROW_ID";



	/**
	 * ��Ŀ���ϸ��Ϣ(���)
	 * 
	 * @return DataSet
	 * @throws Exception
	 */
	public DataSet getPrjTbDetailInfo(String sYear, String rgCode, String xmbm, String en_code, String xmxh) throws Exception;


	public DataSet getQueryProject(String cond) throws Exception;


	public DataSet getQueryProject2(String cond) throws Exception;


	public String getNodeName(String xmxh, String year) throws Exception;


	/**
	 * ��ȡԤ�㵥λ
	 * 
	 * @return
	 * @throws Exception
	 */
	// public DataSet getEnterprise() throws Exception;
	/**
	 * ��ȡִ������
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmZxzq(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmfl(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀ״̬
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmzt(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmsx(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀѡ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmKmxz(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��λѡ��
	 * 
	 * @return
	 * @throws Exception
	 */
	// public List getEnlist(String setYear,String rgCode) throws Exception;
	/**
	 * ��ȡ���ÿ�Ŀ
	 * 
	 * @return
	 * @throws Exception
	 */

	public DataSet getjjKm(String setYear, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀѡ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmKmxzByExitKm(String inKm) throws Exception;


	/**
	 * �ж���Ŀ�����Ƿ��Ѿ�����
	 * 
	 * @param enDmԤ�㵥λ����
	 * @param xmmc��Ŀ����
	 * @return
	 * @throws Exception
	 */
	public List isExistsXmmc(String enDm, String xmmc, String setYear, String rgCode) throws Exception;


	/**
	 * ����Ԥ�㵥λID��ȡCODE
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public List getEnCode(String enID) throws Exception;


	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public String getXmbm(String enCode, String setYear) throws Exception;


	/**
	 * ������Ŀ
	 * 
	 * @param list
	 * @param listKm
	 * @return
	 * @throws Exception
	 */
	public InfoPackage saveXmjl(List list) throws Exception;


	/**
	 * �޸���Ŀ
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editXmjl(DataSet ds) throws Exception;


	/**
	 * �޸���Ŀ
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmSearch(String year, String rgCode, Map parMap, String kmxz[], String enxz[]) throws Exception;


	/**
	 * ��ȡ������Ŀ
	 * 
	 * @param year
	 * @param rgCode
	 * @param en_id
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmBALL(String year, String rgCode) throws Exception;


	/**
	 * �����Ŀ�б�
	 * 
	 * @param year
	 * @param rgCode
	 * @return �������Ҷ�ӽڵ��ʱ���ѯ��Ŀ
	 * @throws Exception
	 */
	public DataSet getXmByEnId(String year, String rgCode, String en_id) throws Exception;


	/**
	 * ��ȡ��Ŀ�б�
	 * 
	 * @param year
	 * @param rgCode
	 * @return �������Ҷ�ӽڵ��ʱ���ѯ��Ŀ
	 * @throws Exception
	 */

	public DataSet getXmByEnLike(String year, String rgCode, String div_code) throws Exception;


	/**
	 * ɾ����Ŀ
	 * 
	 * @param listSql
	 * @throws Exception
	 */
	public void postData(List listSql) throws Exception;


	/**
	 * ������Ŀ��Ż�ȡ��Ŀ
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmByXmxh(String xmxh, String setYear, String rgCode) throws Exception;


	// /**
	// * ������Ŀ��Ż�ȡ��Ŀ��ʷ
	// * @param xmxh
	// * @return
	// * @throws Exception
	// */
	// public List getXmByXmxhHs(String xmxh) throws Exception;
	/**
	 * ������Ŀ��Ż�ȡ��Ŀ��ϸ��ʷ
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */

	public List getXmmxByXmxhHs(String xmxh) throws Exception;


	/**
	 * ����Ԥ�㵥λ��ȡ��Ŀ�б�
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmListByEnID(String enID, String rgCode) throws Exception;


	/**
	 * ������Ŀ��Ż�ȡ��Ŀ
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmDataByXmxh(String xmxh) throws Exception;


	/**
	 * ������Ŀ��Ż�ȡ��Ŀ�����Ϣ
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmmxByXmxh(String xmxh) throws Exception;


	/**
	 * �ж���Ŀ�Ƿ�ʹ�ù�
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List isExistsXm(String xmxhs) throws Exception;


	/**
	 * ����Ԥ�㵥λID��ȡ��λ������
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public DataSet getDwkzsByEnId(String year, String rgCode, String enId) throws Exception;


	/**
	 * ����Ԥ�㵥λID��ȡ��λ������ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public List getDwkzsListByEnId(String year, String rgCode, String enId) throws Exception;


	/**
	 * ����Ԥ�㵥λID��ȡ��λ������ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public DataSet getDwkzsByEnIdLike(String year, String rgCode, String enCode) throws Exception;


	/**
	 * ����Ԥ�㵥λID��ȡ��λ������ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public List getDwkzsListByEnIdLike(String year, String rgCode, String enCode) throws Exception;


	/**
	 * �ж�Ԥ�㵥λ�Ƿ��п�����
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public List isExistsDwkzs(String enID) throws Exception;


	/**
	 * ����ID��ȡ��λ������
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public List getDwkzsByDwkzsId(String dwkzsid) throws Exception;


	/**
	 * ����ID��ȡ��λ������
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public DataSet getDwkzsByDwkzsIdForDs(String dwkzsid) throws Exception;


	/**
	 * ��λ������
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editDwkzs(DataSet ds) throws Exception;


	/**
	 * ������Ŀ��������ȡ�Ѿ�ѡ��Ŀ�Ŀ��Ϣ
	 * 
	 * @param aPrjCode
	 * @return
	 * @throws Exception
	 */
	public String getAcctInfo(String aPrjID) throws Exception;


	/**
	 * ��Ŀ����-��ȡ��Ŀ
	 * 
	 * @param year
	 * @param rgCode
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmSortListByEnID(String year, String rgCode, String enCode, String identity) throws Exception;


	/**
	 * ��ȡ��Ŀ�ϱ�����
	 * 
	 * @param year
	 * @param rgCode
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmSbLyByXmxh(String year, String rgCode, String xmxh) throws Exception;


	/**
	 * ������Ŀ��Ż�ȡ��Ӧ�Ŀ�Ŀ�б�
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmKmListByXmxh(String xmxh) throws Exception;


	/**
	 * ���ݹ��ܿ�Ŀcode��ȡid
	 * 
	 * @param bsCode
	 * @return
	 * @throws Exception
	 */
	public String getBsIDByCode(String bsCode) throws Exception;


	/**
	 * ��ȡĳ������
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List getList(String tableName) throws Exception;


	/**
	 * ������Ŀ��Ż�ȡ��Ŀ����
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getKmmcByXmxh(String xmxh) throws Exception;


	/**
	 * ��ȡDBLINK����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBLink() throws Exception;


	/**
	 * ��ȡԶ������-��Ŀ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBDataXm(String dbLinkName, String tableName, String divCode) throws Exception;


	/**
	 * ��ȡ�������ݵĵ�λ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDisDivCode(String dbLinkName) throws Exception;


	/**
	 * ��ȡ����Ŀ�Ŀ
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDisDivKm(String dbLinkName, String divCode, String prjCode) throws Exception;


	public String getlcwz(String xmbm) throws Exception;


	/**
	 * ��ȡ������Ŀ��ϸ
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDataTbxm(String dbLinkName, String divCode, String prjCode) throws Exception;


	/**
	 * ��ȡ������Ŀ��ϸ
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public boolean insertJlDatahis(String xmbm);


	/**
	 * ���ݵ�λ��û�ȡ��Ŀ�б�
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public List getxmTableInfo(String divCode, int isBack, int stepId) throws Exception;


	/**
	 * ���ݵ�λ��û�ȡ��Ŀ�б�
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public void deleteDwkzs(String kzsid) throws Exception;


	/**
	 * ���ݵ�λ��û�ȡ��Ŀ�б�
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public List getxmTableInfoAll(String divCode) throws Exception;


	public DataSet getxmTableInfoAllds(String divCode, String moduleid, String flowstatus) throws Exception;


	public boolean getHisNull(String prjCode) throws Exception;


	public DataSet getxmTableInfods(String divCode, String moduleid, String flowstatus) throws Exception;


	// ,
	public int[] getxmztNum(String divCode, boolean isTB) throws Exception;


	public DataSet getEnxmxhs(String xmxhs) throws Exception;


	public DataSet getPrjCreateInfo(String filter) throws Exception;


	public DataSet getPrjCreateHisInfo(String set_year, String filter) throws Exception;


	/**
	 * ������Ŀ������Ϣ����λ�ã�
	 * 
	 * @param setYear
	 * @param xmxh
	 * @param dsPrj
	 * @param kmInfo
	 * @param rgCode
	 * @param batchNo
	 * @param auditNo
	 * @throws Exception
	 */
	public void savePrjCreateInfo(String setYear, String xmxh, DataSet dsPrj, String[] kmInfo, String rgCode, String batchNo, String auditNo) throws Exception;


	/**
	 * ���ɽ�����Ϣ
	 * 
	 * @param setYear
	 * 
	 * @param rgCode *
	 * @param xmxhs
	 * @param batchNo
	 * @param auditNo
	 * @throws Exception
	 */
	public void doPrjCreateInfo(String setYear, String xmxhstr, String prjCode, String rgCode, String step_id, String batchNo, String auditNo) throws Exception;


	/**
	 * ��ȡ��Ŀ��ϸ��Ϣ
	 */
	public DataSet getPrjDetailInfo(String setYear, String xmxh, String rgCode) throws Exception;


	/**
	 * ��ȡ��Ŀ��ϸ��Ϣ
	 */
	public DataSet getPrjDetailInfoHis(String setYear, String xmxh, String rgCode) throws Exception;


	/**
	 * ��˵�λ��Ϣ
	 * 
	 * @param divCode
	 * @param setYear
	 * @param rgCode
	 * @return
	 * @throws Exception
	 */
	public DataSet doAuditDivData(String divCode, String setYear, String rgCode) throws Exception;


	/**
	 * ������ϸ��Ϣ
	 * 
	 * @param enID
	 * @param xmxh
	 * @param ds
	 * @param setYear
	 * @param rgCode
	 * @param userID
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public InfoPackage savePrjDetailInfo(String enID, String xmxh, DataSet ds, String setYear, String rgCode, String userID, String date) throws Exception;


	/**
	 * ������Ŀ��ʷ��¼������������Ϣ����ϸ��Ϣ��
	 * 
	 * @param xmxh
	 * @param batchNo
	 * @param stepID
	 * @throws Exception
	 */
	public void savePrjInfoHistory(String[] xmxh, int batchNo, int stepID) throws Exception;


	public int getPrjCount(String div_code);


	/**
	 * ���걨��¼
	 * 
	 */
	// �鿴
	public DataSet getPrjSbCreateInfo(String filer, String flowstatus, String moduleId) throws Exception;


	/**
	 * ���걨��¼
	 * 
	 */
	public DataSet getPrjSbshCreateInfo(String filer, String flowstatus, String moduleId) throws Exception;


	// ����
	public void savePrjCreateSbInfo(DataSet ds, String setYear, String xmxh, String[] kmInfo, String rgCode) throws Exception;


	public String sendbackAudit(List xmxhs, String rgCode, String setYear, String moduleId, int type) throws Exception;


	public String doSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception;


	/**
	 * �����˻�
	 * 
	 * @param prjCode
	 *            ��Ŀ����
	 * @return
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes, String aUserCode, String rgCode, String moduleid, String year) throws Exception;


	public String doshSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception;


	public String saveTbPrj(Map map) throws Exception;


	public String savePrjTbDetailInfo(String enID, String xmxh, DataSet ds, String aYear, String rgCode, String userID, String date);


	/**
	 * ������Ŀ����ܿ�Ŀ����
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String saveTbPrjSubject(String sYear, String rgCode, String[] subject, String chrId);


	/**
	 * ���ݵ�λ�õ������б�
	 * 
	 * @param enId
	 * @return
	 * @throws Exception
	 */
	public DataSet getFjFiles(String enId, String flowstatus) throws Exception;


	public DataSet getControlMoney(String divCode, String year) throws Exception;
}
