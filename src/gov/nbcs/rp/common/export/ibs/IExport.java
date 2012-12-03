package gov.nbcs.rp.common.export.ibs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * �����Ľӿ�
 * 
 * @author qzc
 * 
 */
public interface IExport {

	// ���ñ�ı���
	public static final String sConfigTableName = "FB_S_PRINTINFO";

	// ���ñ���Ĺؼ��ֶ�
	public static final String sKeyFiled = "REPORT_ID";

	// �����
	public static final String sTitle = "REPORT_TITLE";

	// С����1
	public static final String sChildTitle1 = "REPORT_TITLE_A1";

	// С����2
	public static final String sChildTitle2 = "REPORT_TITLE_A2";

	// ���ñ�������
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
	 * ��i�ζ�ȡblob
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
	 * ����blob�ֶ���Ϣ
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
	 * ���ñ���������¼
	 * @param report_id
	 * @param set_year
	 * @throws Exception
	 */
	public void newSetData(String report_id,String set_year,String title) throws Exception;
	
	/**
	 * ִ��SQL���
	 * @param sql
	 * @throws Exception
	 */
	public void execCute(String sql) throws Exception;
	
	public DataSet getDivDataPop(String sYear, int isSetted, String swhere) throws Exception;
	
	public void newImportSetData(String tableEName,String tableCName, String set_year)
	throws Exception;

}
