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

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.sql.OracleSQLBuilder;
import gov.nbcs.rp.common.datactrl.sql.SybaseSQLBuilder;

import java.util.List;
import java.util.Set;


/**
 * @author qj
 *
 */
public abstract class SQLBuilder {
	public static final int For_Oracle = 0;

	public static final int For_Sybase = 1;

	private static SQLBuilder oracleSQLBuilder = new OracleSQLBuilder();

	private static SQLBuilder sybaseSQLBuilder = new SybaseSQLBuilder();

	/**
	 * Gets the SQLBuilder instance.
	 *
	 * @param databaseType
	 *            example:For_Oracle
	 *
	 * @return the instance
	 */
	public static SQLBuilder getInstance(int databaseType) {
		switch (databaseType) {
		case For_Oracle:
			return oracleSQLBuilder;
		case For_Sybase:
			return sybaseSQLBuilder;
		default:
			throw new IllegalArgumentException();
		}
	}

	protected static final String select = " select ";

	protected static final String where = " where ";

	protected static final String set = " set ";

	protected static final String from = " from ";

	protected static final String update = " update ";

	/**
	 * To select sql.
	 *
	 * @param cols
	 *            the cols ���԰�����������"col as colAlias"
	 * @param tableAlias
	 *            the table alias
	 *
	 * @return the string
	 */
	protected String toSelectSql(String[] cols, String tableAlias,
			boolean beginWithSelect) {
		StringBuffer sbSelect = null;
		if (cols != null) {
			for (int i = 0; i < cols.length; i++) {
				if (cols[i] != null) {
					if (sbSelect == null) {
						sbSelect = new StringBuffer(beginWithSelect ? select
								: "");
					}
					if (tableAlias != null) {
						sbSelect.append(tableAlias).append(".");
					}
					sbSelect.append(cols[i]).append(',');
				}
			}
		}
		// ɾ��ĩβ��','
		sbSelect.deleteCharAt(sbSelect.length() - 1);
		return sbSelect == null ? "" : sbSelect.toString();
	}

	/**
	 * To where sql.
	 *
	 * @param fields
	 *            the fields
	 * @param colInfo
	 *            the col info
	 *
	 * @return the string
	 */
	protected String toWhereSql(Field[] fields, Set colInfo,
			boolean beginWithWhere) {
		StringBuffer sbWhere = null;
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if (!colInfo.contains(name)) {
					continue;
				}
				if (sbWhere == null) {
					sbWhere = new StringBuffer(beginWithWhere ? where : "");
				}
				sbWhere.append(Common.convertToSQLCondition(name, field
						.getOldValue()));
				sbWhere.append(" and ");
			}
			int idx = sbWhere.lastIndexOf("and");
			if (idx >= 0) {
				sbWhere.delete(idx, sbWhere.length());
			}
		}
		return sbWhere == null ? "" : sbWhere.toString();

	}

	/**
	 * To set sql.
	 *
	 * @param fields
	 *            the fields
	 * @param colInfo
	 *            the col info
	 *
	 * @return the string
	 */
	protected String toSetSql(Field[] fields, Set colInfo, boolean beginWithSet) {

		StringBuffer sbSet = null;
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String name = field.getName();
				if (!colInfo.contains(name)) {
					continue;
				}
				if (sbSet == null) {
					sbSet = new StringBuffer(beginWithSet ? set : "");
				}
				Object value = Common.convertToSQLType(field.getValue());
				if (value != null) {
					sbSet.append(name);
					sbSet.append("=");
					sbSet.append(value);
					sbSet.append(',');
				}
			}
			sbSet.deleteCharAt(sbSet.length() - 1);
		}
		return sbSet.toString();
	}

	/**
	 * ����һ����¼Collection<Field>���ɸ���SQL���
	 */
	public abstract String updateSQL(String tableName, Field[] fields,
			Set colInfo) throws Exception;

	public abstract String updateSQL(String tableName, Field[] fields,
			Set colInfo, List primaryKeyValue, String[] primarykey)
			throws Exception;

	/**
	 * ����һ����¼Collection<Field>���ɲ���SQL���
	 */
	public abstract String insertSQL(String tableName, Field[] fields,
			Set colInfo) throws Exception;

	/**
	 * ����һ����¼Collection<Field>����ɾ��SQL���
	 */
	public abstract String deleteSQL(String tableName, Field[] fields,
			Set colInfo) throws Exception;

	public abstract String deleteSQL(String tableName, List primaryKeyValue,
			String[] primarykey) throws Exception;

	/**
	 * ���ݱ������������ɲ�ѯSQL���
	 */
	public abstract String querySQL(String tableName, Field[] fields,
			Set colInfo) throws Exception;

	/**
	 * ���ݱ������������ɷ�ҳ��ѯSQL���
	 */
	public abstract String querySQLPage(String tableName, Field[] fields,
			Set colInfo, int begin, int count) throws Exception;
}
