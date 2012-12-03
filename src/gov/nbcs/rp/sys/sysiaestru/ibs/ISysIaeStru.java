package gov.nbcs.rp.sys.sysiaestru.ibs;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * ��֧�ṹ�ӿ�
 * 
 * @author qzc
 * 
 */
public interface ISysIaeStru {
	int iCodeLen = 3; // ���볤��

	String NAME = "name";

	String ROOT_CODE = "1001"; // ������Ŀ��֧���ʽ���Դ�������

	String Max_Lvl = "9999";

	/**
	 * �ύ����
	 * 
	 * @param dataSet
	 */
	public void postDataSet(DataSet dataSet) throws Exception;

	/**
	 * ��ʾ��ʽ
	 */
	public DataSet getSFormate(int setYear) throws Exception;

	/**
	 * �༭��ʽ
	 */
	public DataSet getEFormate(int setYear) throws Exception;

	/**
	 * �������
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getKind(String typeID, String setYear) throws Exception;

	/**
	 * ���������Ŀ��
	 * 
	 * @return��¼��
	 * @throws Exception
	 */
	public DataSet getIncColTre(int setYear) throws Exception;

	/**
	 * �õ�������Ŀ���շ���Ŀ
	 * 
	 * @param setYear
	 *            ���
	 * @param sIncColCode
	 *            ������Ŀ����
	 * @return ����������Ŀ���շ���ĿDataSet
	 * @throws Exception
	 */

	public DataSet getInccolumnToToll(String setYear, String sIncColCode)
			throws Exception;

	/**
	 * ���������Ŀ��,��ѡ��Ŀ
	 * 
	 * @return��¼��
	 * @throws Exception
	 */
	public DataSet getIncColTreCalc(int setYear, String sIncColCode)
			throws Exception;

	/**
	 * ȡ������Ԥ��������ٸ�������Ŀ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getIncColIValue(String setYear) throws Exception;

	/**
	 * �ж��ܷ�ɾ��������Ŀ��¼
	 * 
	 * @param sENameӢ������
	 * @param sIncColCode������Ŀ����
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableDel(String sEName, String sIncColCode,
			String setYear) throws Exception;

	/**
	 * �ж��ܷ��޸�������Ŀ��¼
	 * 
	 * @param sENameӢ������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableModify(String sEName, String setYear)
			throws Exception;

	/**
	 * �ж�������Ŀ��д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public InfoPackage judgeIncColCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�������Ŀ��д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public InfoPackage judgeIncColNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�������Ŀ�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgeIncColParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * ����������Ŀ
	 * 
	 * @param dsIncCol������ĿDataSet
	 * @param sIncOldColCodeԭ������Ŀ����
	 * @param sIncColCode������Ŀ����
	 * @paramb AddFirstNode�Ƿ����ӵĵ�һ���ڵ�
	 * @param lstTollCode�շ���Ŀ�б�
	 * @param setYear���
	 * @throws Exception
	 */
	public void saveIncCol(DataSet dsIncCol, String sIncOldColCode,
			String sIncColCode, boolean bAddFirstNode, ArrayList lstTollCode,
			String setYear) throws Exception;

	/**
	 * ɾ��������Ŀ
	 * 
	 * @param dsIncCol
	 *            ������ĿDataSet
	 * @param sIncColParCode
	 *            ���ڵ����
	 * @param sIncColCode
	 *            ������Ŀ����
	 * @param setYear���
	 * @param sIncColName������Ŀ����
	 * @param sIncColEname������Ŀename
	 * @throws Exception
	 */
	public void delIncCol(DataSet dsIncCol, String sIncColParCode,
			String sIncColCode, String setYear, String sIncColName,
			String sIncColEname) throws Exception;

	/**
	 * ȡ��֧���ʽ���Դ�������ٸ���Ŀ��
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public int getPayOutFsIValue(int setYear) throws Exception;

	/**
	 * ���֧���ʽ���Դ
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSTre(int setYear) throws Exception;

	/**
	 * ���֧���ʽ���Դ,��ѡ��Ŀ
	 * 
	 * @return��¼��
	 * @throws Exception
	 */
	public DataSet getPayOutFSCalc(int setYear, String sPfsCode)
			throws Exception;

	/**
	 * ����ʽ���Դ�Ƿ�ʹ�� 0δ��ʹ��1�����ã�����ṹ��2��ʹ�ã�¼�����ݣ�3�ȱ������ֱ�ʹ�á�
	 * 
	 * @param iSetYear
	 * @param sPfsCode
	 * @param sPfsField
	 * @return
	 */
	public InfoPackage chkFundSourceByRef(String setYear, String sPfsCode,
			String sPfsField) throws Exception;

	/**
	 * ɾ���ʽ���Դ������һ���������ύ����������
	 * 
	 * @param setYear
	 * @param sSrcPfsCodeԴ�ʽ���Դ����
	 * @param sTagPfsCodeĿ���ʽ���Դ���룬�����޸��ʽ���Դ����ʱ,ɾ��ʱ�ò���Ϊ��.
	 * @param sPfsField�ʽ���Դ��Ӧ���ֶΡ�
	 * @param sOptType�������ͣ�1ɾ��2�޸ġ�
	 * @param dataSet,�ύ�����ݼ�
	 * @param iClearFlag,true:������
	 *            ClearFundSourceData����������ʽ���Դ��Ϣ;false������
	 * @param sPfsName����
	 * @return
	 */
	public void delFundSource(String setYear, String sSrcPfsCode,
			String sTagPfsCode, String sPfsField, int iOptType,
			DataSet dataSet, boolean iClearFlag, String sPfsName)
			throws Exception;

	/**
	 * ���������Ŀ���
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getIncTypeTre(int setYear) throws Exception;

	/**
	 * �ж�������Ŀ����Ƿ���ɾ��
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableDel(String sCode, String setYear)
			throws Exception;

	/**
	 * �ж�������Ŀ����Ƿ����޸�
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableModify(String sCode, String setYear)
			throws Exception;

	/**
	 * �ж�������Ŀ�����д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�������Ŀ�����д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public InfoPackage judgeIncTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�������Ŀ�������Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgeIncTypeParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * ɾ��������Ŀ���
	 * 
	 * @param dataSet
	 * @param sCode,��ǰ��lvl���� *
	 * @param sParCode,��ǰ�ڵ㸸�ڵ���� *
	 * @param setYear
	 * @param sIncTypeName
	 * @param sPfsCode
	 */
	public void delIncType(DataSet dataSet, String sCode, String sParCode,
			String setYear, String sIncTypeName) throws Exception;

	/**
	 * ����������Ŀ���
	 * 
	 * @param dsIncType
	 *            ������Ŀ������ݼ�
	 * @param sOldIncTypeCode
	 *            ������Ŀ���ԭ����
	 * @param sIncTypeCode
	 *            ������Ŀ����±���
	 * @param dsInccolumnToInc
	 *            ѡ�е�������Ŀ�б�����Ԥ���Ŀ���շ���Ŀ
	 * @param setYear���
	 * @param sInctypeName
	 *            ������Ŀ������ƣ��޸����ƴ�ֵ��������null
	 * @param lstPfsCode
	 *            ֧���ʽ���Դ
	 * @throws Exception
	 */
	public void saveIncType(DataSet dsIncType, String sOldIncTypeCode,
			String sIncTypeCode, DataSet dsInccolumnToInc, String setYear,
			String sInctypeName, List lstPfsCode) throws Exception;

	/**
	 * ����������Ŀ�����룬�õ�������Ŀ�����������Ŀ�Ķ�Ӧ��ϵ
	 * 
	 * @param setYear���
	 * @param sIncTypeCode������Ŀ������
	 * @return
	 * @throws Exception
	 */
	public DataSet getInctypeToIncolumn(String setYear, String sIncTypeCode)
			throws Exception;

	/**
	 * ���֧����Ŀ���
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception;

	/**
	 * ɾ��֧����Ŀ���
	 * 
	 * @param dataSet
	 * @param sCode
	 * @param sParCode
	 * @param setYear
	 */
	public void delPayOutKind(DataSet dataSet, String sCode, String sParCode,
			String sParName, String sParParId, String setYear,
			String sPayoutKindName) throws Exception;

	/**
	 * ����֧����Ŀ���
	 * 
	 * @param dataSet
	 * @throws Exception
	 */
	public void savePayOutKind(DataSet dataSet, DataSet dsPayoutKindToJj,
			String sSaveType, String sPayoutKindCode, String sPayOutKindName,
			String setYear, String sParCode) throws Exception;

	/**
	 * ����֧�����ÿ�Ŀ��֧������Ŀ
	 * 
	 * @param setYear
	 * @param lstSubItem
	 * @param sBsiId
	 * @param sAcctCodeJj
	 * @param sSubType
	 * @throws Exception
	 */
	public void saveAcctJjToSubItem(String setYear, String sBsiId,
			String sSubType) throws Exception;

	/**
	 * ���������Ԥ���Ŀ���շ���Ŀ
	 * 
	 * @param setYear
	 * @param sSelectNodeId
	 * @param sIN_BS_ID
	 * @param sAcctCodeJj
	 * @param sSubType
	 * @param iDataSrc
	 * @throws Exception
	 */
	public void saveIncAcctToIncItem(String setYear, ArrayList sSelectNodeId,
			ArrayList sSelectNodeCode, String sIN_BS_ID, String sAcctCodeInc,
			String sSubType, int iDataSrc) throws Exception;

	/**
	 * ���ã������ʽ���Դ��Ӧ����
	 * 
	 * @param setYear���
	 * @param sDivCode
	 * @param iDataType
	 * @param sPFSCode
	 * @param dsPfsToIncitem,�ʽ���Դ��Ӧ������ĿDataSet
	 * @throws Exception
	 */
	public DataSet savePfsToItem(String setYear, String sDivCode,
			String sPFSCode, DataSet dsPayOutFS, DataSet dsPfsToIncitem)
			throws Exception;

	/**
	 * ��֧��Ŀ�ҽ���ϸ�� �õ��շ���Ŀcha_id,��������Ԥ���Ŀ����
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getIncItemWithCode(String sIN_BS_ID, String setYear)
			throws Exception;

	/**
	 * �жϾ��ÿ�Ŀ��֧������Ŀ�ܷ��޸�
	 * 
	 * @param sBSI_ID
	 * @param sName
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeJjEnableModify(String sBsiId, String sName,
			String setYear) throws Exception;

	/**
	 * �ж�����Ԥ���Ŀ���շ���Ŀ�ܷ��޸�
	 * 
	 * @param sBSI_ID
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncEnableModify(String sActtCode, String sName,
			String setYear) throws Exception;

	/**
	 * �ж�֧����Ŀ����Ƿ��ѱ�ʹ��,�����޸ĺ�ɾ��ʱ�ж�
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @return true:δ��ʹ�ã�false,�ѱ�ʹ��
	 * @throws Exception
	 */
	public InfoPackage judgePayOutTypeUse(String sPayoutKindCode, String setYear)
			throws Exception;

	/**
	 * ֧����Ŀ��𣬵õ����ÿ�Ŀid,����֧����Ŀ������
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getJjWithPayoutKindCode(String sPayoutKindCode,
			String setYear) throws Exception;

	/**
	 * ֧���ʽ���Դ�����룬�ж�Ӣ�������Ƿ��ѱ�ʹ��
	 * 
	 * @param sEname
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public boolean judgePfsEnameUse(String sEname, String setYear)
			throws Exception;

	/**
	 * ֧���ʽ���Դ�����룬�õ�����Ԥ���ĿId,����֧���ʽ���Դ����
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getIncWithPfsCode(String sPfsCode, String setYear)
			throws Exception;

	/**
	 * �ж��ܷ�ɾ��֧���ʽ���Դ
	 * 
	 * @param sENameӢ������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePfsEnableDel(String sEName, String setYear)
			throws Exception;

	/**
	 * �ж�֧����Ŀ�����д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public boolean judgePayOutTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * ֧����Ŀ���,�жϱ����Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgePayOutTypeParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * �ж�֧����Ŀ�����д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public boolean judgePayOutTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * ֧����Ŀ����У��ж�ѡ�еľ��ÿ�Ŀ�Ƿ��ѱ�ѡ��
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @param bModify
	 * @return
	 * @throws Exception
	 */
	// public InfoPackage judgeJjIsCheck(ArrayList lstId, ArrayList lstName,
	// String setYear, String sPayOutTypeCode) throws Exception;
	/**
	 * �ж�֧���ʽ���Դ�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽᣬ�����Ҷ�ӽڵ�
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return��true��������Ҳ���Ҷ�ڵ�
	 */
	public InfoPackage judgePfsParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * �ж�֧���ʽ���Դ��д�ı����Ƿ��ظ�
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :���ظ�,false �ظ�
	 * @throws Exception
	 */
	public InfoPackage judgePfsCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�֧���ʽ���Դ��д�������Ƿ��ظ�
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :���ظ�,false �ظ�
	 */
	public InfoPackage judgePfsNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * �ж�֧���ʽ���Դ��д�������Ƿ��ظ�
	 * 
	 * @param sCode
	 * @param bModify
	 * @return
	 * @throws Exception
	 */
	public boolean judgePfsStdTypeCode(String sCode, String setYear,
			boolean bModify) throws Exception;

	/**
	 * �ж��ʽ���Դ�����룬������Ŀ�Ƿ�ѡ��
	 * 
	 * @param lstId
	 * @param lstName
	 * @param setYear
	 * @param sPayOutTypeCode
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeIsCheck(ArrayList lstId, ArrayList lstName,
			String setYear, String sPfs) throws Exception;

	/**
	 * ����֧���ʽ���Դ
	 * 
	 * @param dsPayOutFS,֧���ʽ���ԴDataSet
	 * @param sPfsCode,������¼����
	 * @param sParPfsCode,������¼����¼�ı���
	 * @param lstAcctCode���ܿ�Ŀ�б�
	 * @throws Exception
	 */
	public void savePayOutFS(DataSet dsPayOutFS, String sPfsCode,
			String sParPfsCode, String setYear, List lstAcctCode,
			boolean bAddFirstNode) throws Exception;

	/**
	 * �õ��ʽ���Դ�Թ��ܿ�Ŀ
	 * 
	 * @param setYear
	 * @param sPfsCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsToAcct(String setYear, String sPfsCode)
			throws Exception;

	/**
	 * �ж��Ƿ������ù�ʽ�͹�ʽ�뵥λ��Ӧ��ϵ
	 * 
	 * @param payOutKindCode֧����Ŀ�������
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayoutTypeFormulaUse(String payOutKindCode)
			throws Exception;

	/**
	 * �жϾ��ÿ�Ŀ���֧����Ŀ����Ƿ���ڶ�Ӧ��ϵ
	 * 
	 * @param sPayoutKindCode
	 *            ֧����Ŀ������
	 * @param acctJJCode
	 *            ���ÿ�Ŀ����
	 * @return ���ڶ�Ӧ��ϵ����true,���򷵻�false
	 * @throws Exception
	 */

	public boolean JudgeAcctJJExist(String sPayoutKindCode, String acctJJCode)
			throws Exception;

	/**
	 * �ı�֧����Ŀ���
	 * 
	 * @param sNewPayoutKindCode��֧����Ŀ������
	 * @param sNewPayoutKindName��֧����Ŀ��𸸶�������
	 * @param sOldPayoutKindCodeԭ֧����Ŀ������
	 * @param acctJjCode���ÿ�Ŀ����
	 * @throws Exception
	 * 
	 */
	public void changePayOutKind(String sNewPayoutKindCode,
			String sNewPayoutKindName, String sOldPayoutKindCode,
			String acctJjCode) throws Exception;

	/**
	 * �ж�֧����Ŀ����о��ÿ�Ŀ�Ƿ�ʹ��
	 * 
	 * @param payOutKindId֧����Ŀ�������
	 * @param acctJjCode���ÿ�Ŀ����
	 * @returnʹ�������Ϣ��δʹ�÷��ؿ�ֵ
	 * @throws Exception
	 */
	public String judgePayoutTypeAcctJjUse(String payOutKindCode,
			String acctJjCode) throws Exception;

	/**
	 * �õ�֧����Ŀ���-ר��֧����¼��
	 * 
	 * @param payouKindCode
	 * @return
	 * @throws Exception
	 */
	public String getPayoutKindStandPrj(String payouKindCode) throws Exception;

	/**
	 * �����֧���ʽ���Դ���õ�֧���ʽ���Դ����,��������Ԥ���ĿId
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getPfsToIncCode(String sIncTypeCode, String setYear)
			throws Exception;

	/**
	 * ���֧���ʽ���Դ(������������ԴΪ����ļ�¼��
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSNotCalcTre(int setYear) throws Exception;

	/**
	 * �õ�������Ŀ���is_inc�ֶ�ֵ
	 * 
	 * @param incTypeCode
	 * @return
	 * @throws Exception
	 */
	public int getIncTypeIsInc(String incTypeCode) throws Exception;

	/**
	 * �ж�Ԥ�������Ƿ����
	 * 
	 * @param setYear
	 * @param sCode
	 * @return
	 * @throws Exception
	 */
	public DataSet judgeIncColYLBLExist(String setYear, String sCode)
			throws Exception;

	/**
	 * �õ�֧���ʽ���Դ�ܼƼ�¼
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsHj(String setYear) throws Exception;
}
