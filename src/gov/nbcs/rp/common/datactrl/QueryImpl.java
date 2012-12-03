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

import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.MyHashSet;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.log4j.FbLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.hibernate.Session;

import weblogic.jdbc.vendor.oracle.OracleThinBlob;
import weblogic.jdbc.vendor.oracle.OracleThinClob;


public class QueryImpl implements Query {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private GeneralDAO dao;

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	private Map tableInfoCache = new MyMap();

	private Map columnTypeCache = new MyMap();

	/**
	 * 得到数据库中表字段的数据类型（映射到Java的类型）
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public Class getColumnType(String tableName, String fieldName)
			throws Exception {
		Map colType = (Map) columnTypeCache.get(tableName);
		if (colType == null) {
			genColInfo(tableName);
			colType = (Map) columnTypeCache.get(tableName);
		}
		return (Class) colType.get(fieldName);
	}

	/**
	 * 根据表名获取字段名
	 * 
	 * @param tableName
	 * @return
	 */
	public Set getColumnInfo(String tableName) throws Exception {
		Set colInfo = (Set) tableInfoCache.get(tableName);
		if (colInfo == null) {
			genColInfo(tableName);
			colInfo = (Set) tableInfoCache.get(tableName);
		}
		return colInfo;
	}

	public Set getColumnInfo2(String tableName) throws Exception {
		Set nameBuffer = new MyHashSet();
		String sql = SQLTool.querySQL(tableName);
		Statement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			// 防止取数过多，影响效率 by qinj at Mar 11, 2009
			rs = stmt.executeQuery(sql + " WHERE 1=2");
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				String columnName = meta.getColumnName(i + 1);
				nameBuffer.add(columnName);
			}
			this.tableInfoCache.put(tableName, nameBuffer);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}
		return nameBuffer;
	}

	/**
	 * 根据表名获取所有字段类型信息
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public Map getColumnTypeMap(String tableName) throws Exception {
		Map colType = (Map) columnTypeCache.get(tableName);
		if (colType == null) {
			genColInfo(tableName);
			colType = (Map) columnTypeCache.get(tableName);
		}
		return colType;
	}

	public Map getColumnTypeMap2(String tableName) throws Exception {
		Map typeBuffer = new MyMap();
		
		String sql = SQLTool.querySQL(tableName);
		Statement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			// 防止取数过多，影响效率 by qinj at Mar 11, 2009
			rs = stmt.executeQuery(sql + " WHERE 1=2");
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				String columnName = meta.getColumnName(i + 1);
				typeBuffer.put(columnName, Common.getJavaType(meta
						.getColumnType(i + 1)));
			}
			this.columnTypeCache.put(tableName, typeBuffer);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}
		return typeBuffer;
	}

	/**
	 * 将Table的字段信息生成到指定的Set中去
	 * 
	 * @param tableName
	 * @param buffer
	 */
	protected synchronized void genColInfo(String tableName) throws Exception {
		if ((tableName == null) || (tableName.length() == 0)) {
			return;
		}

		Set nameBuffer = (Set) this.tableInfoCache.get(tableName);
		Map typeBuffer = (Map) this.columnTypeCache.get(tableName);
		if ((nameBuffer == null) || (typeBuffer == null)) {
			nameBuffer = new MyHashSet();
			typeBuffer = new MyMap();
			this.columnTypeCache.put(tableName, typeBuffer);
			this.tableInfoCache.put(tableName, nameBuffer);

			String sql = SQLTool.querySQL(tableName);
			Statement stmt = null;
			ResultSet rs = null;
			Session session = null;
			try {
				session = dao.getSession();
				stmt = session.connection().createStatement();
				// 防止取数过多，影响效率 by qinj at Mar 11, 2009
				rs = stmt.executeQuery(sql + " WHERE 1=2");
				ResultSetMetaData meta = rs.getMetaData();
				for (int i = 0; i < meta.getColumnCount(); i++) {
					String columnName = meta.getColumnName(i + 1);
					nameBuffer.add(columnName);
					typeBuffer.put(columnName, Common.getJavaType(meta
							.getColumnType(i + 1)));
				}
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				dao.closeSession(session);
			}
		}
	}

	/**
	 * 查询，返回一个List<Map>序列，其中每个元素<Map>都是 字段名:字段值 的pair
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List executeQuery(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			logSQL(sql);
//			dao.findBySql(sql);
			rs = stmt.executeQuery(sql);
			return asList(rs);
		} catch (Exception e) {
			// System.out.println("出错sql==="+sql);
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}

			dao.closeSession(session);
		}
		return null;
	}

	/**
	 * 查询，返回一个List<Map>序列，其中每个元素<Map>都是 字段名:字段值 的pair
	 * 
	 * @param sql
	 * @parame parameters 参数
	 * @return
	 * @throws Exception
	 */
	public List executeQuery2(String sql, Object[] parameters) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().prepareStatement(sql);

			logSQL(sql);

			if ((parameters != null) && (parameters.length > 0)) {
				logSQL(parameters);

				for (int i = 0; i < parameters.length; i++) {
					stmt.setObject(i + 1, parameters[i]);
				}
			}
			rs = stmt.executeQuery();
			return asList(rs);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}
	}

	/**
	 * 执行更新语句
	 * 
	 * @param sql
	 *            更新SQL语句
	 * @return 返回更新的记录数
	 * @throws Exception
	 */
	public int executeUpdate(String sql) throws Exception {
		logSQL(sql);
		return dao.executeBySql(sql);
	}

	/**
	 * 执行更新语句
	 * 
	 * @param sql
	 *            更新SQL语句
	 * @param parameters
	 *            参数
	 * @return 返回更新的记录数
	 * @throws Exception
	 */
	public int executeUpdate2(String sql, Object[] parameters) throws Exception {
		logSQL(sql);
		return dao.executeBySql(sql, parameters);
	}

	/**
	 * 执行批量更新
	 * 
	 * @param sqls
	 * @return 每条语句更新的记录数
	 * @throws Exception
	 */
	public int[] executeBatch(List sqls) throws Exception {
		// 避免null值 by qinj at Aug 15, 2008 3:11:49 PM
		if ((sqls == null) || (sqls.size() == 0)) {
			return new int[0];
		}

		Statement stmt = null;
		if (Global.loginmode == 1) { // 离线版数据库不支持批处理，ymq 7.14 修改
			int num[] = new int[sqls.size()];
			for (int i = 0; i < sqls.size(); i++) {
				logSQL(sqls.get(i));
				num[i] = dao.executeBySql((String) sqls.get(i));
			}
			return num;
		} else {
			Session session = null;
			try {
				session = dao.getSession();
				stmt = session.connection().createStatement();
				for (int i = 0; i < sqls.size(); i++) {
					if (!Common.isNullStr((String) sqls.get(i))) {
						logSQL(sqls.get(i));
						stmt.addBatch((String) sqls.get(i));
					}
				}
				return stmt.executeBatch();
			} finally {
				if (stmt != null) {
					stmt.close();
				}
				dao.closeSession(session);
			}
		}
	}

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
			int[] outParamIndexes) throws Exception {
		CallableStatement stmt = null;
		Session session = null;
		try {
			session = dao.getSession();
			StringBuffer sb = new StringBuffer("{CALL ");
			sb.append(procName);
			Set outParam = new HashSet();
			for (int i = 0; (outParamIndexes != null)
					&& (i < outParamIndexes.length); i++) {
				outParam.add(new Integer(outParamIndexes[i]));
			}
			if (params != null) {
				sb.append("(");

				for (int i = 0; i < params.length; i++) {
					sb.append("?,");
				}
				sb.deleteCharAt(sb.length() - 1);// 删除最后一个多余的逗号
				sb.append(")");
			}
			sb.append("}");
			stmt = session.connection().prepareCall(sb.toString());
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					if (outParam.contains(new Integer(i))) {
						stmt.registerOutParameter(i, Common
								.getSQLTypeSymbol(params[i].getClass()));
					}
				}
			}
			stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}

	}

	/**
	 * 执行一个存储过程
	 * 
	 * @param procName
	 *            过程名字
	 * @return 执行结果
	 */
	public void executeStoreProc(String procName) throws Exception {
		this.executeStoreProc2(procName, null, null);
	}

	/**
	 * 将查询结果集ResultSet转换为List<Map>数据结构
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	protected List asList(ResultSet rs) throws Exception {
		// qj List result = new LinkedList();
		// 对于append操作，ArrayList比LinkedList效率高
		List result = new ArrayList();
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			Map row = new MyMap();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				row.put(meta.getColumnName(i + 1), rs.getObject(i + 1));
			}
			result.add(row);
		}
		return result;
	}

	/**
	 * 返回一个查询结果的字段信息
	 */
	public TableColumnInfo[] getColumnInfoBySQL(String sql) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			logSQL(sql);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			TableColumnInfo tableColumnInfo[] = new TableColumnInfo[meta
					.getColumnCount()];
			for (int i = 0; i < meta.getColumnCount(); i++) {
				tableColumnInfo[i] = new TableColumnInfo();
				tableColumnInfo[i].setColumnName(meta.getColumnName(i + 1));
				tableColumnInfo[i].setColumnType(meta.getColumnType(i + 1));
				tableColumnInfo[i].setColumnDisplaySize(meta
						.getColumnDisplaySize(i + 1));
			}
			return tableColumnInfo;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}
	}

//	public QueryResult executeQuery3(String sql, Object[] parameters)
//			throws Exception {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		Session session = null;
//		try {
//			session = dao.getSession();
//			stmt = session.connection().prepareStatement(sql);
//			logSQL(sql);
//			if (parameters != null && parameters.length > 0) {
//				for (int i = 0; i < parameters.length; i++) {
//					stmt.setObject(i + 1, parameters[i]);
//				}
//			}
//			rs = stmt.executeQuery();
//			return asResultObject(rs);
//		} finally {
//			if (rs != null)
//				rs.close();
//			if (stmt != null)
//				stmt.close();
//			dao.closeSession(session);
//		}
//	}
//
//	/**
//	 * 用jdbc的ResultSet生成QueryResult对象
//	 * 
//	 * @param rs
//	 * @return
//	 */
//	protected QueryResult asResultObject(ResultSet rs) throws Exception {
//		QueryResult result = new QueryResult();
//		ResultSetMetaData meta = rs.getMetaData();
//		int columnCount = meta.getColumnCount();
//		for (int i = 0; i < meta.getColumnCount(); i++) {// 生成字段列信息
//			result.addColumnIndex(i, meta.getColumnName(i + 1));
//		}
//		// qj List dataList = new LinkedList();
//		// 对于append操作，ArrayList比LinkedList效率高
//		List dataList = new ArrayList();
//		while (rs.next()) {
//			Object[] rowData = new Object[columnCount + 1];
//			int i = 0;
//			for (; i < columnCount; i++) {
//				rowData[i] = rs.getObject(i + 1);
//			}
//			rowData[i] = new Integer(DataSet.NORMAL);
//			dataList.add(rowData);
//		}
//		result.setDataBody(dataList);
//		return result;
//	}

	/**
	 * Prints the sql.
	 * 
	 * @param sql
	 *            the sql
	 */
	private void logSQL(Object sql) {
		// 不再受Common.RUNTIME_DEBUG限制，在/src/log4j_fb.properties配置日志级别
		// by qinj at 20080808
		// if (Common.RUNTIME_DEBUG) {
		// System.out.println(sql);
		FbLogger.sqlLogger().debug(sql);
		// }
	}

	/**
	 * Prints the sql.
	 * 
	 * @param sql
	 *            the sql
	 */
	private void logSQL(Object[] sqlParams) {
		// 不再受Common.RUNTIME_DEBUG限制，在/src/log4j_fb.properties配置日志级别
		// by qinj at 20080808
		// if (Common.RUNTIME_DEBUG) {
		// System.out.println(sql);
		StringBuffer sbSqlParams = new StringBuffer("Parameter(s) -");
		for (int i = 0; i < sqlParams.length; i++) {
			sbSqlParams.append(" [" + i + "]:" + sqlParams[i]);
		}
		logSQL(sbSqlParams);
		// }
	}

	/**
	 * 
	 * 在sybase库中，不允许用executeUpdate方法进行带select语句的更新，所以这里添加execute方法 XXL 20080704
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	public boolean execute(String sSql) throws Exception {
		PreparedStatement stmt = null;

		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().prepareStatement(sSql);
			return stmt.execute();
		} finally {

			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}
	}

	/**
	 * 执行批量更新,运行带select 语句
	 * 
	 * @param sqls
	 * @return
	 * @throws Exception
	 */
	public void executeBatch2(List sqls) throws Exception {
		// Parameters check by qinj at Aug 16, 2008 11:16:40 AM
		if ((sqls == null) || (sqls.size() == 0)) {
			return;
		}

		Statement stmt = null;
		// if (Global.loginmode == 1) { // 离线版数据库不支持批处理，ymq 7.14 修改
		// int num[] = new int[sqls.size()];
		// for (int i = 0; i < sqls.size(); i++) {
		// logSQL(sqls.get(i));
		// num[i] = dao.executeBySql((String) sqls.get(i));
		// }
		// return num;
		// } else {
		Session session = null;
		try {
			session = dao.getSession();
			stmt = session.connection().createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				if (!Common.isNullStr((String) sqls.get(i))) {
					logSQL(sqls.get(i));
					stmt.execute((String) sqls.get(i));
				}
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			dao.closeSession(session);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.Query#getBlob(java.lang.String,
	 *      java.lang.Object[], java.lang.String)
	 */
	public byte[] getBlob(String sql, Object[] parameters, String blobColName)
			throws SQLException, IOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			// 获取clob字段
			session = dao.getSession();
			Connection conn = session.connection();
			pstmt = conn.prepareStatement(sql);
			putParameters(pstmt, 1, parameters);
			rs = pstmt.executeQuery();
			byte[] result = null;
			if (rs.next()) {
				result = readBlob(rs, blobColName);
			}
			rs.close();
			pstmt.close();
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			dao.closeSession(session);
		}
	}

	/**
	 * Puts the parameters.
	 * 
	 * @param pstmt
	 *            the pstmt
	 * @param pos
	 *            the pos 起始位置
	 * @param parameters
	 *            the parameters
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	private void putParameters(PreparedStatement pstmt, int pos,
			Object[] parameters) throws SQLException {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i + pos, parameters[i]);
			}
		}
	}

	/**
	 * Read blob.
	 * 
	 * @param rs
	 *            the rs
	 * @param blobColName
	 *            the blob col name
	 * 
	 * @return the byte[]
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private byte[] readBlob(ResultSet rs, String blobColName)
			throws SQLException, IOException {
		byte[] result = null;
		if (Global.loginmode == 0) {
			// oracle
			Blob jBlob = rs.getBlob(blobColName);
			if (jBlob != null) {
				int iLength = (int) jBlob.length();
				result = new byte[iLength];
				InputStream inputStream = jBlob.getBinaryStream();
				if (inputStream != null) {
					try {
						inputStream.read(result);
					} catch (Exception e) {
					} finally {
						inputStream.close();
					}
				}
			}
		} else {
			// sybase
			InputStream inputStream = rs.getBinaryStream(1);
			if (inputStream != null) {
				try {
					result = new byte[inputStream.available()];
					inputStream.read(result);
				} catch (Exception e) {
				} finally {
					inputStream.close();
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.Query#getClob(java.lang.String,
	 *      java.lang.Object[], java.lang.String)
	 */
	public String getClob(String sql, Object[] parameters, String clobColName)
			throws SQLException, IOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Session session = null;
		try {
			// 获取clob字段
			session = dao.getSession();
			Connection conn = session.connection();
			pstmt = conn.prepareStatement(sql);
			putParameters(pstmt, 1, parameters);
			rs = pstmt.executeQuery();

			String sClob = null;
			if (rs.next()) {
				sClob = readClob(rs, clobColName);
			}
			rs.close();
			pstmt.close();
			return sClob;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			dao.closeSession(session);
		}
	}

	/**
	 * Read clob.
	 * 
	 * @param rs
	 *            the rs
	 * @param clobColName
	 *            the clob col name
	 * 
	 * @return the string
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String readClob(ResultSet rs, String clobColName)
			throws SQLException, IOException {
		StringBuffer sbuf = new StringBuffer();
		if (Global.loginmode == 0) {
			// oracle
			Clob jClob = rs.getClob(clobColName);
			if (jClob != null) {
				Reader clobReader = jClob.getCharacterStream();
				if (clobReader != null) {
					try {
						int r = 0;
						char[] cbuf = new char[4096];
						while ((r = clobReader.read(cbuf)) != -1) {
							sbuf.append(cbuf, 0, r);
						}
					} catch (Exception e) {
					} finally {
						clobReader.close();
					}
				}
			}
		} else {
			// sybase
			Reader clobReader = rs.getCharacterStream(clobColName);
			if (clobReader != null) {
				try {
					int r = 0;
					char[] cbuf = new char[4096];
					while ((r = clobReader.read(cbuf)) != -1) {
						sbuf.append(cbuf, 0, r);
					}
				} catch (Exception e) {
				} finally {
					clobReader.close();
				}
			}
		}
		return sbuf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.Query#updateBlob(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Object[], byte[])
	 */
	public int updateBlob(String tableName, String blobColumnName,
			String whereClause, Object[] whereParameters, byte[] blob)
			throws SQLException, IOException {
		if (Global.loginmode == 0) {
			// oracle
			String sql = new StringBuffer("select ").append(blobColumnName)
					.append(" from ").append(tableName).append(" where ")
					.append(whereClause).append(" for update").toString();
			return updateOracleBlob(sql, whereParameters, blob);
		} else {
			// sybase
			String sql = new StringBuffer("update ").append(tableName).append(
					" set ").append(blobColumnName).append("=? where ").append(
					whereClause).toString();
			return updateSybaseLongBinary(sql, whereParameters, blob);
		}
	}

	/**
	 * Updates the oracle blob.
	 * 
	 * @param sql
	 *            the sql
	 * @param whereParameters
	 *            the where parameters
	 * @param blob
	 *            the blob
	 * 
	 * @return the int
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int updateOracleBlob(String sql, Object[] whereParameters,
			byte[] blob) throws SQLException, IOException {
		if ((blob != null) && (blob.length > 0)) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Session session = null;
			try {
				// 获取clob字段
				session = dao.getSession();
				Connection conn = session.connection();
				pstmt = conn.prepareStatement(sql);
				putParameters(pstmt, 1, whereParameters);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					writeOracleBlob(rs, blob);
				}
				rs.close();
				pstmt.close();
				return 1;
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				dao.closeSession(session);
			}
		}
		return 0;
	}

	/**
	 * Updates the sybase long binary.
	 * 
	 * @param sql
	 *            the sql
	 * @param whereParameters
	 *            the where parameters
	 * @param blob
	 *            the blob
	 * 
	 * @return the int
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int updateSybaseLongBinary(String sql, Object[] whereParameters,
			byte[] blob) throws SQLException, IOException {
		int result = 0;
		if ((blob != null) && (blob.length > 0)) {
			PreparedStatement pstmt = null;
			Session session = null;
			try {
				// 获取clob字段
				session = dao.getSession();
				Connection conn = session.connection();
				pstmt = conn.prepareStatement(sql);
				ByteArrayInputStream bis = new ByteArrayInputStream(blob);
				pstmt.setBinaryStream(1, bis, blob.length);
				putParameters(pstmt, 2, whereParameters);
				result = pstmt.executeUpdate();
				pstmt.close();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
				dao.closeSession(session);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.Query#updateClob(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Object[],
	 *      java.lang.String)
	 */
	public int updateClob(String tableName, String clobColumnName,
			String whereClause, Object[] whereParameters, String clob)
			throws SQLException, IOException {
		if (Global.loginmode == 0) {
			// oracle
			String sql = new StringBuffer("select ").append(clobColumnName)
					.append(" from ").append(tableName).append(" where ")
					.append(whereClause).append(" for update").toString();
			return updateOracleClob(sql, whereParameters, clob);
		} else {
			// sybase
			String sql = new StringBuffer("update ").append(tableName).append(
					" set ").append(clobColumnName).append("=? where ").append(
					whereClause).toString();
			return updateSybaseLongVarchar(sql, whereParameters, clob);
		}

	}

	/**
	 * Updates the oracle clob.
	 * 
	 * @param sql
	 *            the sql
	 * @param whereParameters
	 *            the where parameters
	 * @param clob
	 *            the clob
	 * 
	 * @return the int
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int updateOracleClob(String sql, Object[] whereParameters,
			String clob) throws SQLException, IOException {
		if ((clob != null) && (clob.length() > 0)) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Session session = null;
			try {
				// 获取clob字段
				session = dao.getSession();
				Connection conn = session.connection();
				pstmt = conn.prepareStatement(sql);
				putParameters(pstmt, 1, whereParameters);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					writeOracleClob(rs, clob);
				}
				rs.close();
				pstmt.close();
				return 1;
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				dao.closeSession(session);
			}
		}
		return 0;
	}

	/**
	 * Updates the sybase long varchar.
	 * 
	 * @param sql
	 *            the sql
	 * @param whereParameters
	 *            the where parameters
	 * @param clob
	 *            the clob
	 * 
	 * @return the int
	 */
	private int updateSybaseLongVarchar(String sql, Object[] whereParameters,
			String clob) throws SQLException, IOException {
		int result = 0;
		if ((clob != null) && (clob.length() > 0)) {
			PreparedStatement pstmt = null;
			Session session = null;
			try {
				// 获取clob字段
				session = dao.getSession();
				Connection conn = session.connection();
				pstmt = conn.prepareStatement(sql);
				Reader reader = new StringReader(clob);
				pstmt.setCharacterStream(1, reader, clob.length());
				putParameters(pstmt, 2, whereParameters);
				result = pstmt.executeUpdate();
				pstmt.close();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
				dao.closeSession(session);
			}
		}
		return result;
	}

	/**
	 * Write blob.
	 * 
	 * @param rs
	 *            the rs
	 * @param blob
	 *            the blob
	 */
	private void writeOracleBlob(ResultSet rs, byte[] blob)
			throws SQLException, IOException {
		OutputStream outStream = null;
		try {
			Object jBlob = rs.getBlob(1);
			if (jBlob != null) {
				if (jBlob instanceof OracleThinBlob) {
					// weblogic.jdbc.vendor.oracle.OracleThinBlob
					outStream = ((OracleThinBlob) jBlob)
							.getBinaryOutputStream();
				} else if (jBlob instanceof BLOB) {
					// oracle.sql.BLOB
					outStream = ((BLOB) jBlob).getBinaryOutputStream();
				} else {
					// 其他

				}
			}
			if (outStream != null) {
				outStream.write(blob);
			}
		} catch (Exception e) {
		} finally {
			if (outStream != null) {
				outStream.close();
			}
		}
	}

	/**
	 * Write clob.
	 * 
	 * @param rs
	 *            the rs
	 * @param clob
	 *            the clob
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeOracleClob(ResultSet rs, String clob)
			throws SQLException, IOException {
		Writer outWriter = null;
		try {
			Object jClob = rs.getClob(1);
			if (jClob != null) {
				if (jClob instanceof OracleThinClob) {
					// weblogic.jdbc.vendor.oracle.OracleThinClob
					outWriter = ((OracleThinClob) jClob)
							.getCharacterOutputStream();
				} else if (jClob instanceof CLOB) {
					// oracle.sql.CLOB
					outWriter = ((CLOB) jClob).getCharacterOutputStream();
				} else {
					// 其他
				}
			}
			if (outWriter != null) {
				char[] c = clob.toCharArray();
				outWriter.write(c, 0, c.length);
			}

		} catch (Exception e) {
		} finally {
			if (outWriter != null) {
				outWriter.close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.datactrl.Query#findBySql(java.lang.String)
	 */
	public List findBySql(String sql) {
		logSQL(sql);
		return dao.findBySql(sql);
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.datactrl.Query#findBySql(java.lang.String, java.lang.Object[])
	 */
	public List findBySql2(String sql, Object[] params) {
		logSQL(sql);
		logSQL(params);
		return dao.findBySql(sql, params);
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.datactrl.Query#findBySqlByUpper(java.lang.String)
	 */
	public List findBySqlByUpper(String sql) {
		logSQL(sql);
		return dao.findBySqlByUpper(sql);
	}
	
}
