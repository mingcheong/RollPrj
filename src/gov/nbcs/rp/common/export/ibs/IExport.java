package gov.nbcs.rp.common.export.ibs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * 导出的接口
 * 
 * @author qzc
 * 
 */
public interface IExport {

	// 配置表的表名
	public static final String sConfigTableName = "FB_S_PRINTINFO";

	// 配置表里的关键字段
	public static final String sKeyFiled = "REPORT_ID";

	// 大标题
	public static final String sTitle = "REPORT_TITLE";

	// 小标题1
	public static final String sChildTitle1 = "REPORT_TITLE_A1";

	// 小标题2
	public static final String sChildTitle2 = "REPORT_TITLE_A2";

	// 配置表里的年度
	public static final String sYear = "SET_YEAR";

	public List getData(String sql) throws Exception;

	public List getData(String aTableName, List fields, String aFilter)
			throws Exception;

	public int getRecNum(String aTableName, String aFilter) throws Exception;

	public List executeQuery(String sql) throws Exception;

	public List executeQueryWithInfo(String sql, List sqls_Before,
			List sqls_After) throws Exception;

	public DataSet getDataset(String sql) throws Exception;

	public void closeSession() throws Exception;

	public void dsPost(DataSet ds, String[] keyName) throws Exception;

	/**
	 * 第i次读取blob
	 * 
	 * @param i
	 * @param reportid
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public byte[] getDocBlob(int i, String tableName,String fieldName,String filter) throws SQLException,
			IOException, ClassNotFoundException;

	/**
	 * 保存blob字段信息
	 * 
	 * @param report_id
	 * @param longColData
	 * @throws Exception
	 */
	public void modifyDocBlobs(String tableName,String fieldID,String filter,
			byte[] longColData)
			throws Exception;
//	public void modifyDocBlobs(String tableName,String fieldID,String filter,
//			byte[] longColData) throws Exception;
	
	/**
	 * 配置表里新增记录
	 * @param report_id
	 * @param set_year
	 * @throws Exception
	 */
	public void newSetData(String report_id,String set_year,String title) throws Exception;
	
	/**
	 * 执行SQL语句
	 * @param sql
	 * @throws Exception
	 */
	public void execCute(String sql) throws Exception;
	
	public DataSet getDivDataPop(String sYear, int isSetted, String swhere) throws Exception;
	
	public void newImportSetData(String tableEName,String tableCName, String set_year)
	throws Exception;

}
