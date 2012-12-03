/**
 * @# OraSearchObj.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.util.ArrayList;
import java.util.List;

import com.foundercy.pf.util.UUIDRandom;

/**
 * ����˵��:��ѯ��丨����
 * <P>
 * Copyright
 * <P>

 */
public class OriSearchObj {

	private String tempTable = null;// ����

	private List lstInsertSql = new ArrayList();// �������

	private String createTableSql = null;// �������

	private String dropTableSql = null;// ɾ�������

	private String searchSql = null;// ��ѯ���

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
		// ��sSql�еı����滻,ע����λ����滻������������ڷ������
		lstInsertSql.add(sSql.replaceAll("[\\{]" + TEMP_TABLE + "[\\}]",
				tempTable));
	}

	/**
	 * ����������ʱ�����
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
			throw new Exception("����������ʱ���������" + e);
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
			return "û������ʱ��";
		if (lstInsertSql == null || lstInsertSql.size() == 0)
			return "û��ָ���������";
		if (searchSql == null || searchSql.equals(""))
			return "û��ָ����ѯ���";
		if (createTableSql == null || createTableSql.equals(""))
			return "û�д���������";
		return "";
	}

}
