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
import gov.nbcs.rp.common.Common;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The Class SQLTool. 对sql语句操作的工具集
 */
public class SQLTool {

	/**
	 * 根据一条记录Collection<Field>生成更新SQL语句
	 */
	public static String updateSQL(String tableName, Field[] fields, Set colInfo)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (fields != null) {
			StringBuffer whereBuffer = null;
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if (!colInfo.contains(name)) {
					continue;
				}
				if (whereBuffer == null) {
					whereBuffer = new StringBuffer(" where ");
				}
				if (sb.length() <= 0) {
					sb.append("update ");
					sb.append(tableName);
					sb.append(" set ");
				}
				Object value = Common.convertToSQLType(field.getValue());
				if (value != null) {
					sb.append(name);
					sb.append("=");
					sb.append(value);
					sb.append(',');
				}
				whereBuffer.append(Common.convertToSQLCondition(name, field
						.getOldValue()));
				whereBuffer.append(" and ");
			}
			sb.deleteCharAt(sb.length() - 1);
			int idx = whereBuffer.lastIndexOf("and");
			if (idx >= 0) {
				whereBuffer.delete(idx, whereBuffer.length());
			}
			sb.append(whereBuffer);
		}
		return sb.toString();
	}

	public static String updateSQL(String tableName, Field[] fields,
			Set colInfo, List primaryKeyValue, String[] primarykey)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (primaryKeyValue != null) {
			sb.append("update ");
			sb.append(tableName);
			sb.append(" set ");

			StringBuffer whereBuffer = new StringBuffer(" where ");
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if (!colInfo.contains(name)) {
					continue;
				}
				Object value = Common.convertToSQLType(field.getValue());
				if (value != null) {
					sb.append(name);
					sb.append("=");
					sb.append(value);
					sb.append(',');
				}

			}
			for (int i = 0; i < primarykey.length; i++) {
				whereBuffer.append(Common.convertToSQLCondition(primarykey[i],
						primaryKeyValue.get(i)));
				whereBuffer.append(" and ");
			}
			sb.deleteCharAt(sb.length() - 1);
			int idx = whereBuffer.lastIndexOf("and");
			if (idx >= 0) {
				whereBuffer.delete(idx, whereBuffer.length());
			}
			sb.append(whereBuffer);
		}
		return sb.toString();
	}

	/**
	 * 根据一条记录Collection<Field>生成插入SQL语句
	 */
	public static String insertSQL(String tableName, Field[] fields, Set colInfo)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (fields != null) {
			sb.append("insert into ");
			sb.append(tableName);
			sb.append('(');
			StringBuffer valueBuffer = new StringBuffer(" values(");
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if ((field.getValue() == null) || !colInfo.contains(name)) {
					continue;
				}
				Object value = Common.convertToSQLType(field.getValue());
				sb.append(name);
				sb.append(',');
				valueBuffer.append(value);
				valueBuffer.append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			valueBuffer.deleteCharAt(valueBuffer.length() - 1);
			sb.append(")");
			valueBuffer.append(")");
			sb.append(valueBuffer);
		}
		return sb.toString();
	}

	/**
	 * 根据一条记录Collection<Field>生成删除SQL语句
	 */
	public static String deleteSQL(String tableName, Field[] fields, Set colInfo)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if ((field.getOldValue() == null) || !colInfo.contains(name)) {
					continue;
				}
				if (sb.length() <= 0) {
					sb.append("delete from ");
					sb.append(tableName);
					sb.append(" where ");
				}
				sb.append(Common.convertToSQLCondition(name, field
						.getOldValue()));
				sb.append(" and ");
			}
			int idx = sb.lastIndexOf("and");
			if (idx >= 0) {
				sb.delete(idx, sb.length());
			}
		}
		return sb.toString();
	}

	public static String deleteSQL(String tableName, List primaryKeyValue,
			String[] primarykey) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (primaryKeyValue != null) {
			sb.append("delete from ");
			sb.append(tableName);
			sb.append(" where ");
			for (int i = 0; i < primarykey.length; i++) {
				// Field field = primaryKeyValue[i];
				String name = primarykey[i];
				sb.append(Common.convertToSQLCondition(name, primaryKeyValue
						.get(i)));
				sb.append(" and ");
			}
			int idx = sb.lastIndexOf("and");
			if (idx >= 0) {
				sb.delete(idx, sb.length());
			}
		}
		return sb.toString();
	}

	/**
	 * 根据表名和条件生成查询SQL语句
	 */
	public static String querySQL(String tableName) throws Exception {
		return "select * from " + tableName;
	}

	/**
	 * 将一个sql语句并装成分页的sql（适用于oracle）
	 */
	public static String createPageSQL(String sql, int rowFrom, int rowTo) {
		return "SELECT * FROM  ( SELECT A.*, ROWNUM RN FROM (" + sql
				+ ") A WHERE ROWNUM <= " + rowTo + ")WHERE RN >= " + rowFrom;
	}

	/**
	 * 获取 取得一个新的ID号的SQL代码.
	 * 
	 * @return the string
	 */
	public static String sqlNewId() {
		return Global.loginmode == 0 ? "newid()" : "UUIDTOSTR(newid())";
	}
	/**
	 * Gen in-clause.
	 * 
	 * @param lstData
	 *            the lst data
	 * @param feildName
	 *            the feild name
	 * @param isInOrNotIn
	 *            the value is true if it is in or not in
	 * @param isNum
	 *            the value is true if it is num
	 * @return the string
	 */
	public static String genInClause(List lstData, String feildName,
			boolean isInOrNotIn, boolean isNum, String keyName) {
		if ((lstData == null) || lstData.isEmpty()) {
			return "";
		}
		int iCount = lstData.size();
		String inExp = " not in ";
		if (isInOrNotIn) {
			inExp = " in ";
		}
		int pageCount = (iCount - 1) / 400 + 1;
		StringBuffer sqlAll = new StringBuffer("(");
		int ser = 0;
		for (int i = 0; i < pageCount; i++) {
			sqlAll.append(feildName).append(inExp).append(" (");
			for (int j = 0; j < 400; j++) {
				ser = i * 400 + j;
				if (ser < iCount) {
					Map m = (Map) lstData.get(ser);
					String kv = (String) m.get(keyName);
					if (isNum) {
						sqlAll.append(kv).append(",");
					} else {
						sqlAll.append("'").append(kv)
								.append("',");
					}
				}
			}
			sqlAll.delete(sqlAll.length() - 1, sqlAll.length()).append(")")
					.append(isInOrNotIn ? " or  " : " and ");
		}
		return sqlAll.substring(0, sqlAll.length() - 4) + ")";
	}
	
	/**
	 * Gen in-clause.
	 * 
	 * @param lstData
	 *            the lst data
	 * @param feildName
	 *            the feild name
	 * @param isInOrNotIn
	 *            the value is true if it is in or not in
	 * @param isNum
	 *            the value is true if it is num
	 * @return the string
	 */
	public static String genInClause(List lstData, String feildName,
			boolean isInOrNotIn, boolean isNum) {
		if ((lstData == null) || lstData.isEmpty()) {
			return "";
		}
		int iCount = lstData.size();
		String inExp = " not in ";
		if (isInOrNotIn) {
			inExp = " in ";
		}
		int pageCount = (iCount - 1) / 400 + 1;
		StringBuffer sqlAll = new StringBuffer("(");
		int ser = 0;
		for (int i = 0; i < pageCount; i++) {
			sqlAll.append(feildName).append(inExp).append(" (");
			for (int j = 0; j < 400; j++) {
				ser = i * 400 + j;
				if (ser < iCount) {
					if (isNum) {
						sqlAll.append(lstData.get(ser)).append(",");
					} else {
						sqlAll.append("'").append(lstData.get(ser))
								.append("',");
					}
				}
			}
			sqlAll.delete(sqlAll.length() - 1, sqlAll.length()).append(")")
					.append(isInOrNotIn ? " or  " : " and ");
		}
		return sqlAll.substring(0, sqlAll.length() - 4) + ")";
	}
}
