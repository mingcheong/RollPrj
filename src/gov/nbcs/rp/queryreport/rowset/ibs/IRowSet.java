/**
 * @# IRowSet.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:行报表接口
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
	 * 取得行报表的条件
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public List getReportCons(String reportID, String setYear);

	/**
	 * 取得报表的行
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public List getReportRows(String reportID, String setYear);

	/**
	 * 得到选中的数据源数
	 * 
	 * @param lstDataSource
	 *            选中的数据源信息
	 * @throws Exception
	 * @return返回选中的数据源详细信息
	 */
	public List getDataSoureDetail() throws Exception;

	public XMLData getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception;

	/**
	 * 删除一张报表
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public int deleteRowSetReport(String reportID, String setYear)
			throws Exception;

	/**
	 * 取得ID中的图片信息
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID) throws Exception;

	public int saveReportFile(String setYear, byte[] blobByte, String reportID);

	/**
	 * 取得单位和预算的表对照 键为预算用表，值为单位用表
	 * 
	 * @param setYear
	 * @return
	 */
	public XMLData getDataSourceTables(String setYear);

	/**
	 * 取得空的字段信息，具体信息要在初始化界面时决定
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getEmptyField() throws Exception;

	/**
	 * 行报表引擎查询
	 * 
	 * @param rows
	 *            一行显示的数量
	 * @param reportID报表ID
	 * @param depts
	 *            部门条件
	 * @param divs
	 *            单位条件
	 * @param sVerNo
	 *            数据版本
	 * @param sBatchNoFilter
	 *            批次
	 * @param iLoginmode
	 *            登录方式
	 * @return
	 */
	public XMLData getRowSetData(int rows, String reportID, List depts,
			List divs, String sVerNo, String sBatchNoFilter, int iLoginmode);
	
	public void executeQuery(String sSql)throws Exception;
}
