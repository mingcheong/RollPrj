package gov.nbcs.rp.sys.sysrefcol.ibs;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;

/**

 */
public interface ISysRefCol {
	// 引用列编号
	String REFCOL_ID = "refcol_id";

	// 引用列名称
	String REFCOL_NAME = "refcol_name";

	// 引用列类型
	String REFCOL_KIND = "refcol_kind";

	// 引用列语句内容
	String SQL_DET = "sql_det";

	// 引用列语句Ora内容
	String SQL_DET_O = "sql_det_o";

	// 选取方式
	String SELECT_KIND = "select_kind";

	// 主键字段
	String PRIMARY_FIELD = "primary_field";

	// 主键字段类型
	String PRIMARY_TYPE = "primary_type";

	// 显示代码代码字段
	String CODE_FIELD = "code_field";

	// 显示名称字段
	String NAME_FIELD = "name_field";

	// 层次码字段
	String LVL_FIELD = "lvl_field";

	// 层次码风格
	String LVL_STYLE = "lvl_style";

	// 数据使用者
	String DATA_OWNER = "data_owner";

	// 预算年度
	String SET_YEAR = "set_year";

	// 区划
	String RG_CODE = "rg_code";

	// 最后版本
	String LAST_VER = "last_ver";

	/**
	 * 查询引用列记录
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColRecord(String setYear) throws Exception;

	/**
	 * 执行sql语句
	 * 
	 * @param sSql
	 * @throws Exception
	 */
	public void exeSql(String sSql) throws Exception;

	/**
	 * 执行sql语句，返回DataSet
	 * 
	 * @param sSql
	 * @throws Exception
	 */
	public DataSet exeSqlDs(String sSql) throws Exception;

	/**
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getDisplayFormat(String setYear) throws Exception;

	/**
	 * 取得引用列明细
	 * 
	 * @param refColId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReservedField(String setYear) throws Exception;

	/**
	 * 取得引用列明细,根据引用列refcode_id
	 * 
	 * @param refColId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColDetailWithRefColId(String RefCol_id, String setYear)
			throws Exception;

	/**
	 * 根据SQL查询获取字段信息
	 * 
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sSql) throws Exception;

	/**
	 * 保存引用列
	 * 
	 * @param dsRefCol,引用列主表信息
	 * @param dsRefColDetail,引用列明细表信息
	 * @param dsRefColRelatable
	 *            引用列关联表信息
	 * @throws Exception
	 */
	public void saveRefCol(DataSet dsRefCol, DataSet dsRefColDetail,
			DataSet dsRefColRelatable) throws Exception;

	/**
	 * 得到关联源表
	 * 
	 * @param RefCol_Id
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColRelatable(String RefCol_Id, String setYear)
			throws Exception;

	/**
	 * 删除一条引用列管理信息
	 * 
	 * @param dsRefCol
	 * @param sRefColId
	 * @throws Exception
	 */
	public void DeleteRefCol(String sRefColId, String setYear) throws Exception;

	/**
	 * 检查引用列是否被使用
	 * 
	 * @return.true： 未被使用，false:已被使用
	 * @throws Exception
	 */
	public InfoPackage checkRefColUsed(String sRefColId, String setYear)
			throws Exception;

	/**
	 * 检查名称填写是否重复
	 * 
	 * @param sName名称
	 * @param sCode编码，增加时传null,修改时填当前记录的refcol_id
	 * @param setYear
	 * @return true:未被使用,false：已被使用
	 * @throws Exception
	 */
	public boolean checkRefNameUsed(String sName, String sCode, String setYear)
			throws Exception;

	public String replaceRefColFixFlag(String sSql) throws Exception;

}
