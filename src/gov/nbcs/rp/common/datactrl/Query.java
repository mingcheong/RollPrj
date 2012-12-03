/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
	 * 查询，返回一个List<Map>序列，其中每个元素<Map>都是 字段名:字段值 的pair
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List executeQuery(String sql) throws Exception;

	/**
	 * 查询，返回一个List<Map>序列，其中每个元素<Map>都是 字段名:字段值 的pair
	 * 
	 * @param sql
	 * @parame parameters 参数
	 * @return
	 * @throws Exception
	 */
	public List executeQuery2(String sql, Object[] parameters) throws Exception;

//	public QueryResult executeQuery3(String sql, Object[] parameters)
//			throws Exception;

	/**
	 * 执行更新语句，直接调用dao.findBySql()
	 * 
	 * @param sql
	 *            更新SQL语句
	 * @return 返回更新的记录数
	 * @throws Exception
	 */
	public int executeUpdate(String sql) throws Exception;

	/**
	 * 执行更新语句，直接调用dao.findBySql2()
	 * 
	 * @param sql
	 *            更新SQL语句
	 * @param parameters
	 *            参数
	 * @return 返回更新的记录数
	 * @throws Exception
	 */
	public int executeUpdate2(String sql, Object[] parameters) throws Exception;

	/**
	 * 执行批量更新
	 * 
	 * @param sqls
	 * @return 每条语句更新的记录数
	 * @throws Exception
	 */
	public int[] executeBatch(List sqls) throws Exception;

	/**
	 * 执行一个存储过程
	 * 
	 * @param procName
	 *            过程名字
	 * @param parameter
	 *            参数表
	 * @param outParamIndexes
	 *            指定需要输出值的参数的下标（就是参数表的下标）
	 * @return 执行结果
	 */
	public void executeStoreProc2(String procName, Object[] params,
			int[] outParamIndexes) throws Exception;

	/**
	 * 执行一个存储过程
	 * 
	 * @param procName
	 *            过程名字
	 * @return 执行结果
	 */
	public void executeStoreProc(String procName) throws Exception;

	/**
	 * 根据表名获取字段信息
	 * 
	 * @param tableName
	 * @return
	 */
	public Set getColumnInfo(String tableName) throws Exception;
	public Set getColumnInfo2(String tableName) throws Exception;

	/**
	 * 根据SQL查询获取字段信息
	 * 
	 * @param tableName
	 * @return
	 */
	public TableColumnInfo[] getColumnInfoBySQL(String sql) throws Exception;

	/**
	 * 根据表名获取字段类型信息
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 * @return
	 * @throws Exception
	 */
	public Class getColumnType(String tableName, String fieldName)
			throws Exception;

	/**
	 * 根据表名获取所有字段类型信息
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public Map getColumnTypeMap(String tableName) throws Exception;
	public Map getColumnTypeMap2(String tableName) throws Exception;

	/**
	 * 
	 * 在sybase库中，不允许用executeUpdate方法进行带select语句的更新，所以这里添加execute方法 XXL 20080704
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sSql) throws Exception;

	/**
	 * 执行批量更新,运行带select 语句
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
	 * @return 执行的行数
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
	 * @return 执行的行数
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
	 * Finds the by sql. dao的同名方法delegate
	 * 
	 * @param sql
	 *            the sql
	 * 
	 * @return the list
	 */
	public List findBySql(String sql);

	/**
	 * Finds the by sql. dao的同名方法delegate
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the list
	 */
	public List findBySql2(String sql, Object[] params);

	/**
	 * Finds the by sql by upper. dao的同名方法delegate
	 * 
	 * @param sql
	 *            the sql
	 * @return the list
	 */
	public List findBySqlByUpper(String sql);
}
