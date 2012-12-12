package gov.nbcs.rp.sync.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;




public interface SyncInterface
{

	public List getSchemaData(String type, String setYear) throws Exception;


	public DataSet getTableData(String type) throws Exception;


	public void execBatchSql(List sqlList) throws Exception;


	/**
	 * ͬ�������ݽӿ�(���ݷ�������)
	 * 
	 * @param dblinkName
	 *            ���
	 * @param typeCode
	 *            ��������
	 * @param whereSql
	 *            �������� example: wheresql = " and 1=1 ";
	 * @throws Exception
	 */
	public void syncTableByType(String setYear, int typeCode, String whereSql) throws Exception;


	/**
	 * ͬ�������ݽӿ�(���ݷ�������)
	 * 
	 * @throws Exception
	 */
	public void syncBudget1(String setYear, String batchNo, String dateType, List lBudgetData) throws Exception;


	/**
	 * ͬ�������ݽӿ�(���ݷ�������)
	 * 
	 * @throws Exception
	 */
	public void syncBudget2(String setYear, String batchNo, String dateType, List lBudgetData) throws Exception;


	/**
	 * ͬ�������ݽӿ�
	 * 
	 * @param dblink
	 * @param direction
	 * @param exeFunc1
	 * @param exeFunc2
	 * @param list
	 * @param whereSql
	 * @throws Exception
	 * @author z
	 */
	public void syncTableData(String setYear, String dblink, String direction, String exeFunc1, String exeFunc2, List list, String beforeSql, String endSql, String whereSql) throws Exception;


	// public List getSchemeList(int code) throws Exception;

	public DataSet getScronyTableData(String type) throws Exception;


	/**
	 * ��������������Ƚӿ�
	 * 
	 * @param dblink
	 * @param direction
	 * @param exeFunc1
	 * @param exeFunc2
	 * @param list
	 * @param whereSql
	 * @throws Exception
	 * @author z
	 */

	public void scronyNextyear(String setYear, String rgCode, String[] tablename) throws Exception;


	/**
	 * ͬ����
	 * 
	 * 
	 * @param setYear
	 * @param rgCode
	 * @param tablename
	 * 
	 */

	public void Sametablebyyear(String setYear, String rgCode, String[] tablename) throws Exception;

}
