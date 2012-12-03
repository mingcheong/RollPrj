package gov.nbcs.rp.queryreport.szzbset.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import com.foundercy.pf.util.XMLData;

public interface ISzzbSet {
	String NODEID = "NODEID";

	// fb_u_qr_szzb��
	String REPORT_ID = "REPORT_ID";

	String FIELD_COLUMN = "FIELD_COLUMN";

	String FIELD_COLUMNSPAN = "FIELD_COLUMNSPAN";

	String FIELD_ROW = "FIELD_ROW";

	String FIELD_ROWSPAN = "FIELD_ROWSPAN";

	String FIELD_VALUE = "FIELD_VALUE";

	String HEADERBODY_FLAG = "HEADERBODY_FLAG";

	String SET_YEAR = "SET_YEAR";

	String TYPE_NO = "TYPE_NO";

	String FIELD_COLWIDTH = "FIELD_COLWIDTH";

	String FIELD_ROWHEIGHT = "FIELD_ROWHEIGHT";

	String FIELD_FONT = "FIELD_FONT";

	String FIELD_FONTSIZE = "FIELD_FONTSIZE";

	String HOR_ALIGNMENT = "HOR_ALIGNMENT";

	String FIELD_PARA = "FIELD_PARA";

	String FIELD_FONTSTYPE = "FIELD_FONTSTYPE";

	String VER_ALIGNMENT = "VER_ALIGNMENT";

	public static final String FORMAT_INT = "����";

	public static final String FORMAT_FLOAT = "��С������ֵ";

	public static final String FORMAT_STRING = "�ַ���";

	
	public static final String CONVERT_FIELD="C1";

	/**
	 * �õ���֧�ܱ����Ϣ
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception;

	/**
	 * �õ���֧�ܱ���Ϣ
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getSzzb(String sReportId, String setYear) throws Exception;

	/**
	 * ������֧�ܱ���Ϣ
	 * 
	 * @param sReportId
	 * @param dsSzzb
	 * @param dsRepset
	 * @param dsColSet
	 * @throws Exception
	 */
	public void saveReport(DataSet dsRepset, DataSet dsColSet, DataSet dsSzzb,
			List lstType) throws Exception;

	/**
	 * �õ���������
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportType(String setYear) throws Exception;

	/**
	 * �������
	 * 
	 */
	public void saveConver(RepSetObject repSetObject, DataSet dsSzzb,
			String sReportID, List lstType) throws Exception;

	/**
	 * �õ�������
	 */
	public String getMaxCode(String sFieldName) throws NumberFormatException,
			Exception;

	/**
	 * �õ�����ʡ��ְ����
	 */
	public DataSet getJsPositionSub(String setYear) throws Exception;

	/**
	 * ���潭��ʡ��ְ����
	 */
	public void saveJsPositionSub(DataSet ds) throws Exception;

	public List getSzzbCons(String sReportId, String setYear) throws Exception;

	/**
	 * ȡ����֧��̶�����Ϣ(���ݼ�)
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getSzzbConsDs(String sReportId, String setYear)
			throws Exception;

	public List getSzzbFor(String sReportId, String setYear) throws Exception;

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

	public boolean saveFillCols(List lstSql, String reportID) throws Exception;

	/**
	 * ȡ��һ����֧�ܱ������
	 * 
	 * @param xmlForCell��Ԫ���������Ϣ
	 * @param sqlWhere//��ѯʱ������
	 * @return
	 */
	public XMLData getSzzbValues(XMLData xmlForCell, String sqlWhere,boolean isConvert);

	public XMLData getSzzbValuesByDetail(XMLData xmlForCell, String sVerNo,
			String sReportID, String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception;

	/**
	 * ȡ����֧��̶�����Ϣ(���ݼ�)��������
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */

	public DataSet getSzzbConsDsWithoutTitle(String sReportId, String setYear)
			throws Exception;

	// ȡ����֧��ı�ͷ��ʼ��
	public int getHeadStartRow(String sReportId, String setYear)
			throws Exception;
	
	/**
	 * ȡ�ø�ʽ�����ݡ�
	 * @return �������� ֵ��DATASET
	 * @throws Exception
	 */
	public XMLData getFormatList() throws Exception ;
	/**
	 * ���֧����Ŀ���
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception;
	
	/**
	 * �����Ƿ�Ҫת������Ԫ
	 * @param reportID
	 * @return
	 * @throws Exception
	 */
	public boolean isSZReportNeedConvert(String reportID)throws Exception;

}
