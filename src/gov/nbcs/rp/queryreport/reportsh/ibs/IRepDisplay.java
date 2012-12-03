/**
 * @# IRepDisplay.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public interface IRepDisplay {
	/**
	 * 取得资金来源
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
	 * 取得数据
	 * @param sumfields　带加和的字段表达式
	 * @param tableName　查询的表名
	 * @param fixCol　前面分组的固定列
	 * @param lstDiv　选择的单位
	 * @return
	 * @throws Exception
	 */
	public List getSqlData(String sumfields, String fields, String tableName,
			String fixCol, List lstDiv, String filter) throws Exception;

	/**
	 * 取得项目类型
	 * @param lvl TODO
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjSortDs(int lvl) throws Exception;

	/**
	 * 取得资金来源的字段,返回每个为字段，用于预算支出，第二个为CODE拼出的SQL条件，用于项目
	 * @param lstSelectCode
	 * @return
	 * @throws Exception
	 */
	public List getFunfields(List lstSelectCode) throws Exception;

	/**
	 * 将数据插入到临时表
	 * @param filter
	 * @param fundSource
	 * @throws Exception
	 */
	public void insertToTempTable(String fundCodeFilter, String filter,
			String fundSource, String userID) throws Exception;

	/**
	 * 删除表中的相关数据
	 * @throws Exception
	 */
	public void finalTreat(String userID) throws Exception;

	/**
	 * 插入临时表
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
	 * 刷新当前用户用于查询的临时数据
	 * @throws Exception
	 */
	public void refreshCurUserQrData(List lstDiv) throws Exception;
	
	/**
	 * 刷新部分数据
	 * @param lstDiv 单位列表
	 * @param batchNo 批次
	 * @param userType 用户类型
	 * @param lstView 要刷新的视图 可为空
	 * @param prjCode 项目编号，可为空
	 * @throws Exception
	 */
	public void refreshCurUserQrDataBySpec(List lstDiv, int batchNo,
			int userType, String[] lstView, String prjCode) throws Exception;

//	/**
//	 * Gets the fund class. 经费类别
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
//	 * 取得项目经费类别
//	 * @return
//	 * @throws Exception
//	 */
//	public DataSet getFunClassAll() throws Exception;
	/**
	 * 取得年度选择项
	 * 
	 * @return
	 */
	public String getYearRefString() ;
	/**
	 * 根据项目编码，获取单位编码
	 * @throws Exception 
	 */
	public String getDivCodeByPrjCode(String prjCode,int batchNo,int dataType,String setYear);
}
