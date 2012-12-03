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
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.sql.SqlTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * The Class DataAdapter.
 * 
 * @author qj
 * @version 1.0
 */
public class DataAdapter {

	/** The query tool. */
	private Query queryTool;

	/**
	 * The sql commands. 0:SqlCommand.For_Default、SqlCommand.For_Oracle,
	 * 1:SqlCommand.For_Sybase
	 */
	private SqlCommand[] sqlCommands = new SqlCommand[2];

	/**
	 * Instantiates a new data adapter. 默认为服务端bean
	 * 
	 */
	public DataAdapter() {
		init(false);
	}

	/**
	 * Instantiates a new data adapter.
	 * 
	 * @param isClient
	 *            是否客户端bean, the value is true if it is client
	 */
	public DataAdapter(boolean isClient) {
		init(isClient);
	}

	/**
	 * Inits the data adapter.
	 * 
	 * @param isClient
	 *            the value is true if it is client
	 */
	private void init(boolean isClient) {
		queryTool = isClient ? QueryStub.getClientQueryTool() : QueryStub
				.getQueryTool();
	}

	/**
	 * Puts the sql command.
	 * 
	 * @param sqlCommand
	 *            the sql command
	 */
	public void putSqlCommand(SqlCommand sqlCommand) {
		if ((sqlCommand != null) && (sqlCommand.getDbType() >= 0)
				&& (sqlCommand.getDbType() < sqlCommands.length)) {
			sqlCommands[sqlCommand.getDbType()] = sqlCommand;
		}
	}

	/**
	 * Gets the sql command.
	 * 
	 * @return the sql command
	 */
	public SqlCommand getSqlCommand() {
		int dbType;
		if (Global.loginmode == 1) { // offline, sybase
			dbType = SqlCommand.For_Sybase;
		} else { // default, online, oracle
			dbType = SqlCommand.For_Default;
		}
		// 按运行的db类型获取SqlCommand
		SqlCommand sqlCmd = sqlCommands[dbType];
		// 如果对应的sqlCmd为null，则取默认值
		if ((sqlCmd == null) && (dbType != SqlCommand.For_Default)) {
			sqlCmd = sqlCommands[SqlCommand.For_Default];
		} 

		if (sqlCmd != null) {
			// 将默认值的db类型置为运行的db类型
			sqlCmd.setDbType(dbType);
			return sqlCmd;
		} else {
			return null;
		}
	}

	/**
	 * Fills the.
	 * 
	 * @param dataset
	 *            the dataset
	 * @param pageStart
	 *            the page start, the first is 1
	 * @param pageSize
	 *            the page size
	 * 
	 * @return the int 当>=0:记录数；-1:未执行选取操作
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public int fill(DataSet dataset, int pageStart, int pageSize)
			throws Exception {
		SqlCommand sqlCmd = getSqlCommand();
		if (sqlCmd != null) {
			String sql;
			// 合成分页语句
			if ((pageStart > 0) && (pageSize > 0)) {
				sql = sqlCmd.getSelectStatementPage(pageStart, pageSize);
			} else {
				sql = sqlCmd.getSelectStatement();
			}
			Object[] params = sqlCmd.getSelectParameters();
			List datas = params == null ? queryTool.executeQuery(sql)
					: queryTool.executeQuery2(sql, params);
			dataset.fireBeforeEvent(DataSet.OPEN_EVENT);
			dataset.createCacheFrom(datas);
			dataset.beforeFirst();
			int preStat = dataset.toState(StatefulData.DS_BROWSE);
			if (preStat >= 0) {
				dataset.fireStateChange(preStat, DataChangeEvent.DATA_FILLED);
			}
			dataset.fireAfterEvent(DataSet.OPEN_EVENT);
			dataset.fireDataChange(this, this, DataChangeEvent.DATA_FILLED);
			return datas != null ? datas.size() : 0;
		} else {
			return -1;
		}
	}

	/**
	 * Fills the.
	 * 
	 * @param dataset
	 *            the dataset
	 * 
	 * @return the int 当>=0:记录数；-1:未执行选取操作
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public int fill(DataSet dataset) throws Exception {
		return fill(dataset, -1, -1);
	}

	/**
	 * 提交保存所有尚未保存的操作结果.
	 * 
	 * @param dataset
	 *            the dataset
	 * 
	 * @return the int 当>=0:执行的记录数；-1:未执行提交操作
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public int post(DataSet dataset) throws Exception {
		// 只对SqlTable有效
		if (!(getSqlCommand() instanceof SqlTable)) {
			return -1;
		}
		SqlTable sqlTable = (SqlTable) getSqlCommand();
		String tableName = sqlTable.getTableName();
		String[] primarykeys = sqlTable.getPrimarykeys();

		// 仅提交处于StatefulData.DS_EDIT状态的DataSet
		if ((dataset.getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
			// 触发before-POST_EVENT事件
			dataset.fireBeforeEvent(DataSet.POST_EVENT);
			// 记录需要执行的sql语句
			List sqls = new ArrayList();
			//			dataset.maskDataChange(true);
			//			String bookmark = dataset.toogleBookmark();
			//			Query query = dataset.isClient ? QueryStub.getClientQueryTool()
			//					: QueryStub.getQueryTool();
			//			dataset.beforeFirst();
			//			if (dataset.getColNames() == null) {
			//				dataset.setName(tableName);// 触发搜集列信息			
			//			}
			Set colNames = queryTool.getColumnInfo2(tableName);
			//			Map colTypes = queryTool.getColumnTypeMap(tableName);	

			List dataCache = dataset.getDataCache();
			// 记录需要删除的record
			Set removedRecords = new HashSet();
			int cursor = 0;
			for (Iterator itr = dataCache.iterator(); itr.hasNext(); cursor++) {
				Record record = (Record) itr.next();
				Field[] fields = DataSetUtil.pickUpFields(record);
				if (fields != null) {
//					switch (dataset.getState(record)) {
					switch (record.getState()) {
					case DataSet.FOR_DELETE:// 根据待删除record生成delete-SQL语句
						if (primarykeys == null) {
							sqls.add(SQLTool.deleteSQL(tableName, fields,
									colNames));
							removedRecords.add(record);
							//							dataCache.remove(cursor);
							dataset.adjustedBookmarkPointer(cursor, -1);
							//							cursor--;
						} else {
							// dataset是否包含所有所需pk
							boolean falg = true;
							// 本record中的pk值
							List primarykeyValues = new ArrayList();
							for (int i = 0; i < primarykeys.length; i++) {
								if (!record.containsKey(primarykeys[i])) {
									falg = false;
									break;
								} else {
									Object value = record.fieldByName(
											primarykeys[i]).getValue();
									if (value == null) {
										falg = false;
										break;
									} else {
										primarykeyValues.add(value);
									}
								}
							}
							if (falg) {
								sqls.add(SQLTool.deleteSQL(tableName,
										primarykeyValues, primarykeys));
								removedRecords.add(record);
								//								dataCache.remove(cursor);
								dataset.adjustedBookmarkPointer(cursor, -1);
								//								cursor--;
							}
						}
						break;
					case DataSet.FOR_APPEND:
					case DataSet.FOR_INSERT: // 根据待插入record生成insert-SQL语句
						sqls
								.add(SQLTool.insertSQL(tableName, fields,
										colNames));
						for (int i = 0; i < fields.length; i++) {
							fields[i].applyUpdate();
						}
						break;
					default: // DataSet.FOR_UPDATE
						if (dataset.isModified()) {// 根据待修改record生成update-sql语句
							if (primarykeys == null) {
								sqls.add(SQLTool.updateSQL(tableName, fields,
										colNames));
								for (int i = 0; i < fields.length; i++) {
									fields[i].applyUpdate();
								}

							} else {
								// dataset是否包含所有所需pk
								boolean falg = true;
								List primarykeyValue = new ArrayList();
								for (int i = 0; i < primarykeys.length; i++) {
									if (!record.containsKey(primarykeys[i])) {
										falg = false;
										break;
									} else {
										Object value = record.fieldByName(
												primarykeys[i]).getValue();
										if (value == null) {
											falg = false;
											break;
										} else {
											primarykeyValue.add(value);
										}
									}
								}
								if (falg) {
									sqls.add(SQLTool.updateSQL(tableName,
											fields, colNames, primarykeyValue,
											primarykeys));
								}
								for (int i = 0; i < fields.length; i++) {
									fields[i].applyUpdate();
								}
							}
						}
					}
				}
//				dataset.setState(record, DataSet.NORMAL);
				record.setState(DataSet.NORMAL);
			}
			// 移除FOR_DELETE的record
			dataCache.removeAll(removedRecords);
			//			dataset.recordCount = new Integer(dataCache.size());
			//			if (!dataset.gotoBookmark(bookmark))
			//				dataset.beforeFirst();// 恢复操作前游标值
			if (!sqls.isEmpty()) {
				queryTool.executeBatch(sqls);
			}
			// 触发DataChangeEvent.CHANGE_APPLIED事件
			int preStat = dataset.toState(StatefulData.DS_BROWSE);
			if (preStat >= 0) {
				dataset
						.fireStateChange(preStat,
								DataChangeEvent.CHANGE_APPLIED);
			}
			//			dataset.maskDataChange(false);
			// 触发after-POST_EVENT事件
			dataset.fireAfterEvent(DataSet.POST_EVENT);
			return sqls.size();
		} else {
			return -1;
		}
	}

}
