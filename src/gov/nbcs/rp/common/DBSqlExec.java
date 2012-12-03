/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Query;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.util.List;
import java.util.Map;


/**
 * The Class DBSqlExec. ȡ���ݿ�ֵ�����Ĺ��߼�
 * <p>
 * �ڷ���˵���ʱ��ֱ����static������DBSqlExec.server���ã��� DBSqlExec.getFloatValue(String);
 * <p>
 * �ڿͻ��˵���ʱ��ʹ��DBSqlExec.client���� DBSqlExec.client.getFloatValue(String);
 */
public class DBSqlExec {

	/** The Constant server. ����������ã� */
	private static DBSqlExecImpl server;

	/** The Constant client. �����ͻ����ã� */
	private static DBSqlExecImpl client;
	
	/**
	 * Gets the single instance of DBSqlExec.
	 * 
	 * @param isClientEnd
	 *            the value is true if it is client end
	 * @return single instance of DBSqlExec
	 */
	public static DBSqlExecImpl getInstance(boolean isClientEnd) {
		return isClientEnd ? client() : server();
	}

	/**
	 * Server DBSqlExec.
	 * 
	 * @return the dB sql exec impl
	 */
	private static DBSqlExecImpl server() {
		if (server == null) {
			server = new DBSqlExecImpl(false);
		}
		return server;
	}

	/**
	 * Client DBSqlExec.
	 * 
	 * @return the dB sql exec impl
	 */
	public static DBSqlExecImpl client() {
		if (client == null) {
			client = new DBSqlExecImpl(true);
		}
		return client;
	}	
	
	/**
	 * Gets the data set.
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet getDataSet(String sql, Object[] params) throws Exception {
		return server().getDataSet(sql, params);
	}

	/**
	 * Gets the data set.
	 * 
	 * @param sql
	 *            the sql
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet getDataSet(String sql) throws Exception {
		return server().getDataSet(sql);
	}

	/**
	 * Gets the data set. ����Sql����ʼ��DataSet����, ����λ����һ����¼����������ã�
	 * 
	 * @param sql
	 *            the sql
	 * @param dataset
	 *            the dataset
	 * @return the boolean value
	 * @throws Exception
	 *             the exception
	 */
	public static boolean getDataSet(String sql, DataSet dataset)
			throws Exception {
		return server().getDataSet(sql, dataset);
	}

	/**
	 * Gets the data set. ����Sql����ʼ��DataSet����, ����λ����һ����¼����������ã�
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @param dataset
	 *            the dataset
	 * @return the boolean value
	 * @throws Exception
	 *             the exception
	 */
	public static boolean getDataSet(String sql, Object[] params,
			DataSet dataset) throws Exception {
		return server().getDataSet(sql, params, dataset);
	}

	/**
	 * ��ȡ first column value. ��ȡ��һ�е�ֵ.����������ã�
	 * 
	 * @param sql
	 *            ���� select oneColumn from table where id=rowId �����
	 * @param params
	 *            the params
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public static Object getFirstColumnValue(String sql, Object[] params)
			throws Exception {
		return server().getFirstColumnValue(sql, params);
	}

	/**
	 * Gets the float value. ����Sql���õ�һ��double����������������ã� select adoub from table
	 * where id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the float value
	 * @throws Exception
	 *             the exception
	 */
	public static double getFloatValue(String sql, Object[] params)
			throws Exception {
		return server().getFloatValue(sql, params);
	}

	/**
	 * Gets the float value. ����Sql���õ�һ��double����������������ã� select adoub from table
	 * where id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @return the float value
	 * @throws Exception
	 *             the exception
	 */
	public static double getFloatValue(String sql) throws Exception {
		return server().getFloatValue(sql);
	}

	/**
	 * Gets the int value. ����Sql���õ�һ����������������ã� select aint from table where
	 * id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the int value
	 * @throws Exception
	 *             the exception
	 */
	public static int getIntValue(String sql, Object[] params) throws Exception {
		return server().getIntValue(sql, params);
	}

	/**
	 * Gets the int value. ����Sql���õ�һ����������������ã� select aint from table where
	 * id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @return the int value
	 * @throws Exception
	 *             the exception
	 */
	public static int getIntValue(String sql) throws Exception {
		return server().getIntValue(sql);
	}

	/**
	 * Gets the max value from field. �����ݱ��ֶ���ȡ����ǰ���ֵ(����)����������ã�
	 * 
	 * @param tableName
	 *            the table name
	 * @param fieldName
	 *            the field name
	 * @param filter
	 *            the filter
	 * @return the max value from field
	 * @throws Exception
	 *             the exception
	 */
	public static String getMaxValueFromField(String tableName,
			String fieldName, String filter) throws Exception {
		return server().getMaxValueFromField(tableName, fieldName, filter);
	}

	/**
	 * Gets the record count. �����������ر��еļ�¼������������ã�
	 * 
	 * @param tableName
	 *            the table name
	 * @param filter
	 *            the filter
	 * @return the record count
	 * @throws Exception
	 *             the exception
	 */
	public static int getRecordCount(String tableName, String filter)
			throws Exception {
		return server().getRecordCount(tableName, filter);
	}

	/**
	 * Gets the string value. ����Sql���õ�һ���ַ�������������ã� select astr from table where
	 * id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @param params
	 *            the params
	 * @return the string value
	 * @throws Exception
	 *             the exception
	 */
	public static String getStringValue(String sql, Object[] params)
			throws Exception {
		return server().getStringValue(sql, params);
	}

	/**
	 * Gets the string value. ����Sql���õ�һ���ַ�������������ã� select astr from table where
	 * id=rowId
	 * 
	 * @param sql
	 *            the sql
	 * @return the string value
	 * @throws Exception
	 *             the exception
	 */
	public static String getStringValue(String sql) throws Exception {
		return server().getStringValue(sql);
	}

	/**
	 * The Class DBSqlExecImpl.
	 */
	public static class DBSqlExecImpl {

		/** The value is true if it is client end. */
		private boolean isClientEnd;

		/**
		 * @param isClientEnd
		 */
		public DBSqlExecImpl(boolean isClientEnd) {
			super();
			this.isClientEnd = isClientEnd;
		}

		/** The query tool. */
		protected Query queryTool;

		/**
		 * Gets the local query tool.
		 * 
		 * @return the ends the query tool
		 */
		protected Query localQueryTool() {
			if (queryTool == null) {
				queryTool = isClientEnd ? QueryStub.getClientQueryTool()
						: QueryStub.getQueryTool();
			}
			return queryTool;
		}

		/**
		 * ��ȡ first column value. ��ȡ��һ�е�ֵ.
		 * 
		 * @param sql
		 *            ���� select oneColumn from table where id=rowId �����
		 * @param params
		 *            the params
		 * @return the object
		 * @throws Exception
		 *             the exception
		 */
		public Object getFirstColumnValue(String sql, Object[] params)
				throws Exception {
			List rs = localQueryTool().executeQuery2(sql, params);
			if ((rs != null) && (rs.size() > 0)) {
				Map r = (Map) rs.get(0);
				if ((r != null) && (r.size() > 0)) {
					return r.values().iterator().next();
				}
			}
			return null;
		}

		/**
		 * Gets the string value. ����Sql���õ�һ���ַ��� select astr from table where
		 * id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @return the string value
		 * @throws Exception
		 *             the exception
		 */
		public String getStringValue(String sql) throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return null;
			// return dsData.fields()[0].getString();
			// } else
			// return null;
			return getStringValue(sql, null);
		}

		/**
		 * Gets the int value. ����Sql���õ�һ������ select aint from table where
		 * id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @return the int value
		 * @throws Exception
		 *             the exception
		 */
		public int getIntValue(String sql) throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return 0;
			// return Common.isNullStr(dsData.fields()[0].getString()) ? 0
			// : dsData.fields()[0].getInteger();
			// } else
			// return 0;
			return getIntValue(sql, null);
		}

		/**
		 * Gets the float value. ����Sql���õ�һ��double������ select adoub from table
		 * where id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @return the float value
		 * @throws Exception
		 *             the exception
		 */
		public double getFloatValue(String sql) throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return 0.0;
			// return Common.isNullStr(dsData.fields()[0].getString()) ? 0.0
			// : dsData.fields()[0].getDouble();
			// } else
			// return 0.0;
			return getFloatValue(sql, null);
		}

		/**
		 * Gets the string value. ����Sql���õ�һ���ַ��� select astr from table where
		 * id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @param params
		 *            the params
		 * @return the string value
		 * @throws Exception
		 *             the exception
		 */
		public String getStringValue(String sql, Object[] params)
				throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.setQueryParams(params);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return null;
			// return dsData.fields()[0].getString();
			// } else
			// return null;

			// ֱ����Query��ȡ�����ٴ���DataSet������ by qinj at Feb 24, 2009
			Object result = getFirstColumnValue(sql, params);
			if (result != null) {
				return result.toString();
			} else {
				return null;
			}
		}

		/**
		 * Gets the int value. ����Sql���õ�һ������ select aint from table where
		 * id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @param params
		 *            the params
		 * @return the int value
		 * @throws Exception
		 *             the exception
		 */
		public int getIntValue(String sql, Object[] params) throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.setQueryParams(params);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return 0;
			// return Common.isNullStr(dsData.fields()[0].getString()) ? 0
			// : dsData.fields()[0].getInteger();
			// } else
			// return 0;

			// ֱ����Query��ȡ�����ٴ���DataSet������ by qinj at Feb 24, 2009
			Object result = getFirstColumnValue(sql, params);
			if (result instanceof Number) {
				return ((Number) result).intValue();
			} else {
				return 0;
			}
		}

		/**
		 * Gets the float value. ����Sql���õ�һ��double������ select adoub from table
		 * where id=rowId
		 * 
		 * @param sql
		 *            the sql
		 * @param params
		 *            the params
		 * @return the float value
		 * @throws Exception
		 *             the exception
		 */
		public double getFloatValue(String sql, Object[] params)
				throws Exception {
			// DataSet dsData = DataSet.create();
			// dsData.setSqlClause(sql);
			// dsData.setQueryParams(params);
			// dsData.open();
			// if (!dsData.isEmpty() && dsData.next()) {
			// if (dsData.fields()[0].getValue() == null)
			// return 0.0;
			// return Common.isNullStr(dsData.fields()[0].getString()) ? 0.0
			// : dsData.fields()[0].getDouble();
			// } else
			// return 0.0;

			// ֱ����Query��ȡ�����ٴ���DataSet������ by qinj at Feb 24, 2009
			Object result = getFirstColumnValue(sql, params);
			if (result instanceof Number) {
				return ((Number) result).doubleValue();
			} else {
				return 0.0d;
			}
		}
		
		/**
		 * Gets the data set.
		 * 
		 * @param sql
		 *            the sql
		 * @return the data set
		 * @throws Exception
		 *             the exception
		 */
		public DataSet getDataSet(String sql) throws Exception {
			return getDataSet(sql, (Object[]) null);
		}
		
		/**
		 * Gets the data set.
		 * 
		 * @param sql
		 *            the sql
		 * @param params
		 *            the params
		 * @return the data set
		 * @throws Exception
		 *             the exception
		 */
		public DataSet getDataSet(String sql, Object[] params) throws Exception {
			DataSet dataset = isClientEnd ? DataSet.createClient() : DataSet
					.create();
			dataset.setSqlClause(sql);
			dataset.setQueryParams(params);
			dataset.open();
			dataset.next();
			return dataset;
		}
		
		/**
		 * Gets the data set. ����Sql����ʼ��DataSet����, ����λ����һ����¼
		 * 
		 * @param sql
		 *            the sql
		 * @param dataset
		 *            the dataset
		 * @return the boolean value
		 * @throws Exception
		 *             the exception
		 */
		public boolean getDataSet(String sql, DataSet dataset) throws Exception {
			return getDataSet(sql, null, dataset);
		}

		/**
		 * Gets the data set. ����Sql����ʼ��DataSet����, ����λ����һ����¼
		 * 
		 * @param sql
		 *            the sql
		 * @param params
		 *            the params
		 * @param dataset
		 *            the dataset
		 * @return the boolean value
		 * @throws Exception
		 *             the exception
		 */
		public boolean getDataSet(String sql, Object[] params, DataSet dataset)
				throws Exception {
			if (dataset == null) {
				throw new Exception("The dataset should not be null!");
			}
			// boolean b = isClientEnd() ^ dataset.isClient();
			if (!isClientEnd && dataset.isClient()) {
				throw new Exception(
						"It should not run at client!");
			} else if (isClientEnd && !dataset.isClient()) {
				throw new Exception(
						"It should run at server with a server dataset!");
			}
			dataset.setSqlClause(sql);
			dataset.setQueryParams(params);
			dataset.open();
			dataset.next();
			return true;
		}

		/**
		 * Gets the record count. �����������ر��еļ�¼��
		 * 
		 * @param tableName
		 *            the table name
		 * @param filter
		 *            the filter
		 * @return the record count
		 * @throws Exception
		 *             the exception
		 */
		public int getRecordCount(String tableName, String filter)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COUNT(*) FROM ");
			sql.append(tableName);
			if (!Common.isNullStr(filter)) {
				sql.append(" WHERE ");
				sql.append(filter);
			}
			return getIntValue(sql.toString());
		}

		/**
		 * Gets the max value from field. �����ݱ��ֶ���ȡ����ǰ���ֵ(����)
		 * 
		 * @param tableName
		 *            the table name
		 * @param fieldName
		 *            the field name
		 * @param filter
		 *            the filter
		 * @return the max value from field
		 * @throws Exception
		 *             the exception
		 */
		public String getMaxValueFromField(String tableName, String fieldName,
				String filter) throws Exception {
			String sql = " SELECT MAX(" + fieldName + ") as MAXVALUE FROM "
					+ tableName;
			if (!Common.isNullStr(filter)) {
				sql = sql + " WHERE " + filter;
			}
			String Maxcode = getStringValue(sql);
			return Common.isNullStr(Maxcode) ? "0" : Maxcode;
		}

	}
}
