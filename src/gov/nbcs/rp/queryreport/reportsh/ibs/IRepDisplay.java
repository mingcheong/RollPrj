/**
 * @# IRepDisplay.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * ����˵��:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public interface IRepDisplay {
	/**
	 * ȡ���ʽ���Դ
	 * @return
	 * @throws Exception
	 */

	public static final String BUDGET_TOTAL = "1001";

	public static final String TEMP_TABLE = "GT_VW_FB_QR_SUMMARY";

//	public static final String VW_PROJECT = "VW_FB_QR_SUMMARY_PROJECT";

	public static final String VW_PAYOUT = "VW_FB_QR_SUMMARY_BASE";

	public static final String MONEY_FIELD = "payout";

	public static final String upateView[] = new String[] {
			"VW_FB_U_PAYOUT_BASE_SH", "VW_FB_P_PAYOUT_DETAIL",
		
			"VW_FB_BUDGET_DETAIL" };

	public static final String upateViewBase[] = new String[] {
			"VW_FB_U_PAYOUT_BASE_SH", 
			"VW_FB_BUDGET_DETAIL" };

	public static final String upateViewPrj[] = new String[] {
			"VW_FB_P_PAYOUT_DETAIL", 
			"VW_FB_BUDGET_DETAIL" };

	public static final String PRE_STRING = "GT_";

	public DataSet getFunderSource(int Level) throws Exception;

	/**
	 * ȡ������
	 * @param sumfields�����Ӻ͵��ֶα��ʽ
	 * @param tableName����ѯ�ı���
	 * @param fixCol��ǰ�����Ĺ̶���
	 * @param lstDiv��ѡ��ĵ�λ
	 * @return
	 * @throws Exception
	 */
	public List getSqlData(String sumfields, String fields, String tableName,
			String fixCol, List lstDiv, String filter) throws Exception;

	/**
	 * ȡ����Ŀ����
	 * @param lvl TODO
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjSortDs(int lvl) throws Exception;

	/**
	 * ȡ���ʽ���Դ���ֶ�,����ÿ��Ϊ�ֶΣ�����Ԥ��֧�����ڶ���ΪCODEƴ����SQL������������Ŀ
	 * @param lstSelectCode
	 * @return
	 * @throws Exception
	 */
	public List getFunfields(List lstSelectCode) throws Exception;

	/**
	 * �����ݲ��뵽��ʱ��
	 * @param filter
	 * @param fundSource
	 * @throws Exception
	 */
	public void insertToTempTable(String fundCodeFilter, String filter,
			String fundSource, String userID) throws Exception;

	/**
	 * ɾ�����е��������
	 * @throws Exception
	 */
	public void finalTreat(String userID) throws Exception;

	/**
	 * ������ʱ��
	 * @param fundCodeFilter
	 * @param filter
	 * @param fundSource
	 * @param userID
	 * @param divFilter TODO
	 * @throws Exception
	 */
	public void insertToTempTableIncFun(String fundCodeFilter, String filter,
			String fundSource, String userID, String divFilter)
			throws Exception;

	/**
	 * ˢ�µ�ǰ�û����ڲ�ѯ����ʱ����
	 * @throws Exception
	 */
	public void refreshCurUserQrData(List lstDiv) throws Exception;
	
	/**
	 * ˢ�²�������
	 * @param lstDiv ��λ�б�
	 * @param batchNo ����
	 * @param userType �û�����
	 * @param lstView Ҫˢ�µ���ͼ ��Ϊ��
	 * @param prjCode ��Ŀ��ţ���Ϊ��
	 * @throws Exception
	 */
	public void refreshCurUserQrDataBySpec(List lstDiv, int batchNo,
			int userType, String[] lstView, String prjCode) throws Exception;

//	/**
//	 * Gets the fund class. �������
//	 * 
//	 * @param parseInt
//	 *            the parse int
//	 * @return the fund class
//	 * @throws Exception
//	 *             the exception
//	 */
//	public DataSet getFundClass(int parseInt) throws Exception;
//	
//	/**
//	 * ȡ����Ŀ�������
//	 * @return
//	 * @throws Exception
//	 */
//	public DataSet getFunClassAll() throws Exception;
	/**
	 * ȡ�����ѡ����
	 * 
	 * @return
	 */
	public String getYearRefString() ;
	/**
	 * ������Ŀ���룬��ȡ��λ����
	 * @throws Exception 
	 */
	public String getDivCodeByPrjCode(String prjCode,int batchNo,int dataType,String setYear);
}
