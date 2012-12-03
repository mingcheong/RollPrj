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

import gov.nbcs.rp.common.Common;

/**
 * The class SqlCommand.
 * 
 * @author qj
 * @version 1.0, Jan 21, 2009
 * @since 6.3.50
 */
public abstract class SqlCommand {

	/** The Constant For_Oracle. */
	public static final int For_Oracle = 0;

	/** The Constant For_Sybase. */
	public static final int For_Sybase = 1;

	/** The Constant For_Default. */
	public static final int For_Default = For_Oracle;

	/** The database type. */
	private int dbType;

	/**
	 * Instantiates a new sql command.
	 * 
	 * @param dbType
	 *            the db type
	 */
	public SqlCommand(int dbType) {
		super();
		this.dbType = dbType;
	}

	/**
	 * Gets the selects the statement.
	 * 
	 * @return the selects the statement
	 */
	public abstract String getSelectStatement();

	/**
	 * Gets the selects the statement page.
	 * 
	 * @param pageStart
	 *            the page start, this first is 1
	 * @param pageCapacity
	 *            the page capacity, it must > 0
	 * 
	 * @return the selects the statement page
	 */
	public String getSelectStatementPage(int pageStart, int pageCapacity) {
		// 是否有效分页
		if ((pageStart > 0) && (pageCapacity > 0)) {
			if (dbType == For_Sybase) {
				throw new RuntimeException("Paging-Select-Sql is NOT supported in Sybase!");
			} else {
				return Common.getPageQrySQL(getSelectStatement(), pageStart,
						pageCapacity);
			}
		} else {
			return getSelectStatement();
		}
	}

	/**
	 * Gets the selects the parameters.
	 * 
	 * @return the selects the parameters
	 */
	public abstract Object[] getSelectParameters();

	/**
	 * @return the dbType
	 */
	public int getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

}
