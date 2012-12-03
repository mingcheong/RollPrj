/**
 * @# IRowSet.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:�б���ӿ�
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public interface IRowSet {
	public void saveRows(List lstSql, String reportID, String setYear,
			XMLData xmlReport, List lstType) throws Exception;

	/**
	 * ȡ���б��������
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public List getReportCons(String reportID, String setYear);

	/**
	 * ȡ�ñ������
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public List getReportRows(String reportID, String setYear);

	/**
	 * �õ�ѡ�е�����Դ��
	 * 
	 * @param lstDataSource
	 *            ѡ�е�����Դ��Ϣ
	 * @throws Exception
	 * @return����ѡ�е�����Դ��ϸ��Ϣ
	 */
	public List getDataSoureDetail() throws Exception;

	public XMLData getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception;

	/**
	 * ɾ��һ�ű���
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public int deleteRowSetReport(String reportID, String setYear)
			throws Exception;

	/**
	 * ȡ��ID�е�ͼƬ��Ϣ
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID) throws Exception;

	public int saveReportFile(String setYear, byte[] blobByte, String reportID);

	/**
	 * ȡ�õ�λ��Ԥ��ı���� ��ΪԤ���ñ�ֵΪ��λ�ñ�
	 * 
	 * @param setYear
	 * @return
	 */
	public XMLData getDataSourceTables(String setYear);

	/**
	 * ȡ�ÿյ��ֶ���Ϣ��������ϢҪ�ڳ�ʼ������ʱ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getEmptyField() throws Exception;

	/**
	 * �б��������ѯ
	 * 
	 * @param rows
	 *            һ����ʾ������
	 * @param reportID����ID
	 * @param depts
	 *            ��������
	 * @param divs
	 *            ��λ����
	 * @param sVerNo
	 *            ���ݰ汾
	 * @param sBatchNoFilter
	 *            ����
	 * @param iLoginmode
	 *            ��¼��ʽ
	 * @return
	 */
	public XMLData getRowSetData(int rows, String reportID, List depts,
			List divs, String sVerNo, String sBatchNoFilter, int iLoginmode);
	
	public void executeQuery(String sSql)throws Exception;
}
