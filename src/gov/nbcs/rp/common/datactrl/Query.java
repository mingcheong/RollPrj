/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Query extends Serializable {

	/**
	 * ��ѯ������һ��List<Map>���У�����ÿ��Ԫ��<Map>���� �ֶ���:�ֶ�ֵ ��pair
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List executeQuery(String sql) throws Exception;

	/**
	 * ��ѯ������һ��List<Map>���У�����ÿ��Ԫ��<Map>���� �ֶ���:�ֶ�ֵ ��pair
	 * 
	 * @param sql
	 * @parame parameters ����
	 * @return
	 * @throws Exception
	 */
	public List executeQuery2(String sql, Object[] parameters) throws Exception;

//	public QueryResult executeQuery3(String sql, Object[] parameters)
//			throws Exception;

	/**
	 * ִ�и�����䣬ֱ�ӵ���dao.findBySql()
	 * 
	 * @param sql
	 *            ����SQL���
	 * @return ���ظ��µļ�¼��
	 * @throws Exception
	 */
	public int executeUpdate(String sql) throws Exception;

	/**
	 * ִ�и�����䣬ֱ�ӵ���dao.findBySql2()
	 * 
	 * @param sql
	 *            ����SQL���
	 * @param parameters
	 *            ����
	 * @return ���ظ��µļ�¼��
	 * @throws Exception
	 */
	public int executeUpdate2(String sql, Object[] parameters) throws Exception;

	/**
	 * ִ����������
	 * 
	 * @param sqls
	 * @return ÿ�������µļ�¼��
	 * @throws Exception
	 */
	public int[] executeBatch(List sqls) throws Exception;

	/**
	 * ִ��һ���洢����
	 * 
	 * @param procName
	 *            ��������
	 * @param parameter
	 *            ������
	 * @param outParamIndexes
	 *            ָ����Ҫ���ֵ�Ĳ������±꣨���ǲ�������±꣩
	 * @return ִ�н��
	 */
	public void executeStoreProc2(String procName, Object[] params,
			int[] outParamIndexes) throws Exception;

	/**
	 * ִ��һ���洢����
	 * 
	 * @param procName
	 *            ��������
	 * @return ִ�н��
	 */
	public void executeStoreProc(String procName) throws Exception;

	/**
	 * ���ݱ�����ȡ�ֶ���Ϣ
	 * 
	 * @param tableName
	 * @return
	 */
	public Set getColumnInfo(String tableName) throws Exception;
	public Set getColumnInfo2(String tableName) throws Exception;

	/**
	 * ����SQL��ѯ��ȡ�ֶ���Ϣ
	 * 
	 * @param tableName
	 * @return
	 */
	public TableColumnInfo[] getColumnInfoBySQL(String sql) throws Exception;

	/**
	 * ���ݱ�����ȡ�ֶ�������Ϣ
	 * 
	 * @param tableName
	 *            ����
	 * @param fieldName
	 *            �ֶ���
	 * @return
	 * @throws Exception
	 */
	public Class getColumnType(String tableName, String fieldName)
			throws Exception;

	/**
	 * ���ݱ�����ȡ�����ֶ�������Ϣ
	 * 
	 * @param tableName
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public Map getColumnTypeMap(String tableName) throws Exception;
	public Map getColumnTypeMap2(String tableName) throws Exception;

	/**
	 * 
	 * ��sybase���У���������executeUpdate�������д�select���ĸ��£������������execute���� XXL 20080704
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sSql) throws Exception;

	/**
	 * ִ����������,���д�select ���
	 * 
	 * @param sqls
	 * @return
	 * @throws Exception
	 */
	public void executeBatch2(List sqls) throws Exception;

	/**
	 * Gets the clob
	 * 
	 * @param sql
	 *            the sql
	 * @param parameters
	 *            the parameters
	 * @param clobColName
	 *            the clob col name
	 * 
	 * @return the clob
	 */
	public String getClob(String sql, Object[] parameters, String clobColName)
			throws SQLException, IOException;

	/**
	 * Updates the clob.
	 * 
	 * @param tableName
	 *            the table name
	 * @param clobColumnName
	 *            the clob column name
	 * @param whereClause
	 *            the where clause
	 * @param whereParameters
	 *            the where parameters
	 * @param clob
	 *            the clob
	 * 
	 * @return ִ�е�����
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int updateClob(String tableName, String clobColumnName,
			String whereClause, Object[] whereParameters, String clob)
			throws SQLException, IOException;

	/**
	 * Gets the blob.
	 * 
	 * @param sql
	 *            the sql
	 * @param parameters
	 *            the parameters
	 * @param blobColName
	 *            the blob col name
	 * 
	 * @return the blob
	 */
	public byte[] getBlob(String sql, Object[] parameters, String blobColName)
			throws SQLException, IOException;

	/**
	 * Updates the blob.
	 * 
	 * @param tableName
	 *            the table name
	 * @param blobColumnName
	 *            the blob column name
	 * @param whereClause
	 *            the where clause
	 * @param whereParameters
	 *            the where parameters
	 * @param blob
	 *            the blob
	 * 
	 * @return ִ�е�����
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int updateBlob(String tableName, String blobColumnName,
			String whereClause, Object[] whereParameters, byte[] blob)
			throws SQLException, IOException;
	
	/**
	 * Finds the by sql. dao��ͬ������delegate
	 * 
	 * @param sql
	 *            the sql
	 * 
	 * @return the list
	 */
	public List findBySql(String sql);

	/**
	 * Finds the by sql. dao��ͬ������delegate
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the list
	 */
	public List findBySql2(String sql, Object[] params);

	/**
	 * Finds the by sql by upper. dao��ͬ������delegate
	 * 
	 * @param sql
	 *            the sql
	 * @return the list
	 */
	public List findBySqlByUpper(String sql);
}
