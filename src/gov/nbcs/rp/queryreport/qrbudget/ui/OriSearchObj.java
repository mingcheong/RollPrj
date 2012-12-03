/**
 * @# OraSearchObj.java    <文件名>
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.util.ArrayList;
import java.util.List;

import com.foundercy.pf.util.UUIDRandom;

/**
 * 功能说明:查询语句辅助类
 * <P>
 * Copyright
 * <P>

 */
public class OriSearchObj {

	private String tempTable = null;// 表名

	private List lstInsertSql = new ArrayList();// 插入语句

	private String createTableSql = null;// 建表语句

	private String dropTableSql = null;// 删除表语句

	private String searchSql = null;// 查询语句

	public String TEMP_TABLE = "TEMPTABLE";

	public OriSearchObj(String sTempTableName) {
		this.tempTable = sTempTableName;
		this.dropTableSql = "drop table " + tempTable;
	}

	public String getCreateTableSql() {
		return createTableSql;
	}

	public void setCreateTableSql(String createTableSql) {
		if (createTableSql != null)
			createTableSql = createTableSql.replaceAll("[\\{]" + TEMP_TABLE
					+ "[\\}]", tempTable);
		this.createTableSql = createTableSql;
	}

	public String getDropTableSql() {
		return dropTableSql;
	}

	public List getLstInsertSql() {
		return lstInsertSql;
	}

	public void setLstInsertSql(List lstInsertSql) {
		this.lstInsertSql = lstInsertSql;
	}

	public String getTempTable() {
		return tempTable;
	}

	public void addInsertSql(String sSql) {
		if (lstInsertSql == null)
			lstInsertSql = new ArrayList();
		// 将sSql中的表名替换,注：单位表的替换和条件的添加在服务端做
		lstInsertSql.add(sSql.replaceAll("[\\{]" + TEMP_TABLE + "[\\}]",
				tempTable));
	}

	/**
	 * 创建报表临时表表名
	 */
	public static String generateTempTableName() throws Exception {
		try {
			String strName = "TEMP_" + UUIDRandom.generate();
			strName = strName.replaceAll("[\\-\\{\\}]", "");

			if (strName.length() > 30)
				strName = strName.substring(0, 30).toUpperCase();

			// for (int i = 0; i < 3; i++)
			// System.out.println(UUIDRandom.generate());
			return strName;
		} catch (Exception e) {
			throw new Exception("创建报表临时表表名出错，" + e);
		}
	}

	public String getSearchSql() {
		return searchSql;
	}

	public void setSearchSql(String searchSql) {
		searchSql = searchSql.replaceAll("[\\{]" + TEMP_TABLE + "[\\}]",
				tempTable);
		this.searchSql = searchSql;
	}

	public String check() {
		if (tempTable == null || tempTable.equals(""))
			return "没生成临时表";
		if (lstInsertSql == null || lstInsertSql.size() == 0)
			return "没有指定插入语句";
		if (searchSql == null || searchSql.equals(""))
			return "没有指定查询语句";
		if (createTableSql == null || createTableSql.equals(""))
			return "没有创建表的语句";
		return "";
	}

}
