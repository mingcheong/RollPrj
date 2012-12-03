package gov.nbcs.rp.queryreport.szzbset.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import com.foundercy.pf.util.XMLData;

public interface ISzzbSet {
	String NODEID = "NODEID";

	// fb_u_qr_szzb表
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

	public static final String FORMAT_INT = "整数";

	public static final String FORMAT_FLOAT = "带小数的数值";

	public static final String FORMAT_STRING = "字符型";

	
	public static final String CONVERT_FIELD="C1";

	/**
	 * 得到收支总表的信息
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception;

	/**
	 * 得到收支总表信息
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getSzzb(String sReportId, String setYear) throws Exception;

	/**
	 * 保存收支总表信息
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
	 * 得到报表类型
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReportType(String setYear) throws Exception;

	/**
	 * 保存封面
	 * 
	 */
	public void saveConver(RepSetObject repSetObject, DataSet dsSzzb,
			String sReportID, List lstType) throws Exception;

	/**
	 * 得到最大编码
	 */
	public String getMaxCode(String sFieldName) throws NumberFormatException,
			Exception;

	/**
	 * 得到江苏省厅职务补贴
	 */
	public DataSet getJsPositionSub(String setYear) throws Exception;

	/**
	 * 保存江苏省厅职务补贴
	 */
	public void saveJsPositionSub(DataSet ds) throws Exception;

	public List getSzzbCons(String sReportId, String setYear) throws Exception;

	/**
	 * 取得收支表固定格信息(数据集)
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
	 * 得到报表表头
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
	 * 取得一个收支总表的数据
	 * 
	 * @param xmlForCell单元格的设置信息
	 * @param sqlWhere//查询时的条件
	 * @return
	 */
	public XMLData getSzzbValues(XMLData xmlForCell, String sqlWhere,boolean isConvert);

	public XMLData getSzzbValuesByDetail(XMLData xmlForCell, String sVerNo,
			String sReportID, String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception;

	/**
	 * 取得收支表固定格信息(数据集)不含标题
	 * 
	 * @param sReportId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */

	public DataSet getSzzbConsDsWithoutTitle(String sReportId, String setYear)
			throws Exception;

	// 取得收支表的表头起始行
	public int getHeadStartRow(String sReportId, String setYear)
			throws Exception;
	
	/**
	 * 取得格式化数据。
	 * @return 键：类型 值：DATASET
	 * @throws Exception
	 */
	public XMLData getFormatList() throws Exception ;
	/**
	 * 获得支出项目类别
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception;
	
	/**
	 * 报表是否要转换到万元
	 * @param reportID
	 * @return
	 * @throws Exception
	 */
	public boolean isSZReportNeedConvert(String reportID)throws Exception;

}
