/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.pubinterface.ibs;

import java.util.List;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

public interface IPubInterface {
	// 单位信息
	String DIV_ID = "EN_ID"; // 单位ID 生成树的主码

	String DIV_PARENT_ID = "PARENT_ID"; // 单位ID树主码的父亲

	String DIV_NAME = "DIV_NAME"; // 单位名称

	String DIV_CODE_NAME = "CODE_NAME"; // 单位显示码和名称的合并

	String DIV_CODE = "DIV_CODE"; // 单位显示码,一般为CODE

	String DIV_KIND = "DIV_KIND"; // 单位性质

	// 经济科目
	String BSI_ID = "BSI_ID"; // 经济科目ID 生成树的主码

	String BSI_PARENT_ID = "PARENT_ID"; // ID树主码的父亲

	String ACCT_CODE_JJ = "ACCT_CODE_JJ"; // 经济科目编码

	String ACCT_NAME_JJ = "ACCT_NAME_JJ"; // 经济科目名称

	String ACCT_JJ_FNAME = "FULLNAME"; // 编码和名称的合并

	// 功能科目
	String BS_ID = "BS_ID"; // 功能科目ID 生成树的主码

	String BS_PARENT_ID = "PARENT_ID"; // ID树主码的父亲

	String ACCT_CODE = "ACCT_CODE"; // 功能科目编码

	String ACCT_NAME = "ACCT_NAME"; // 功能科目名称

	String ACCT_FNAME = "FULLNAME"; // 编码和名称的合并

	// 获得单位信息数据集
	public DataSet getDivData(String sYear) throws Exception;

	public DataSet getDivDataPop(String sYear) throws Exception; // 带权限过滤

	public DataSet getDepToDivData(String sYear, int levl, boolean isRight)
			throws Exception; // 带部门分类的单位

	/**
	 * Gets the div data by my dep.
	 * 
	 * 以当前登录用户权限，获取单位信息子集（深圳模式）
	 * 
	 * @param sYear
	 *            the s year
	 * @param levl
	 *            the levl
	 * @param isRight
	 *            the value is true if it is right
	 * 
	 * @return the div data by my dep
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public DataSet getDivDataByMyRight(String sYear, int levl, boolean isRight)
			throws Exception; // 带部门分类的单位

	// 获得经济科目数据集
	public DataSet getAcctEconomy(String filter) throws Exception;

	// 获得功能科目数据集
	public DataSet getAcctFunc(String filter) throws Exception;

	// 根据规则类型得到编码规则
	public SysCodeRule getCodeRule(int nYear, String ruleType) throws Exception;

	// 根据数据表替换字符串
	public String replaceTextEx(String sValue, int intType, String aTable,
			String nowField, String endField, String aFilter) throws Exception;

	// 根据数据集替换字符串
	public String replaceTextExDs(String sValue, int intType, DataSet dsTable,
			String nowField, String endField) throws Exception;

	// 作用：从已分配的列数据集中查找出新列合适的对应字段，在系统中的多个表的数据字段都是以F1、F2、F3的形式存在的
	public String assignNewCol(String sFieldName, String sFieldPrefix,
			int iMaxFieldNum, String sTableName, String nYear, String sFilter)
			throws Exception;

	// 函数功能：根据规则在对应的数据表中获取新的节点code
	public String getNodeID(String aTableName, String aFieldName,
			String aParID, String aFilter, SysCodeRule codeRule)
			throws Exception;

	// 函数功能：根据编码长度生成表中最大长度的code
	public String getMaxCode(String aTableName, String aFieldName,
			String aFilter, int intL) throws Exception;

	/**
	 * 获取表某个字段最大值
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 */
	public int getMaxValue(String tableName, String fieldName);

	/**
	 * 获取表某个字段最小值
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 */
	public int getMinValue(String tableName, String fieldName);

	/**
	 * 根据传入的用户ID,获取对应的用户所属机构类型
	 * 
	 * @param user_id
	 * @return
	 * @author lixf
	 */
	public String getUserBelongType(String user_id) throws Exception;

	/**
	 * 根据传入的用户ID,获取对应的用户所属处室编码
	 * 
	 * @param userBeLong
	 * @return
	 * @throws Exception
	 */
	public String getBranchCode(String userBeLong) throws Exception;

	/**
	 * 根据传入的en_id返回DIV_CODE
	 * 
	 * @param en_id
	 * @return
	 * @throws Exception
	 */
	public String getEnDivCode(String en_id) throws Exception;

	/**
	 * 获取单位级次
	 * 
	 * @param en_id
	 * @return
	 * @throws Exception
	 */
	public int getEnDivLevelNum(String en_id) throws Exception;

	/**
	 * 获取单位/财政 是否可以编辑当前项目数据
	 * 
	 * @param aPrjCode
	 *            项目编码
	 * @param userCode
	 *            用户名 客户端直接传入Global.user_code
	 * @param depID
	 *            处室序号 客户端直接传入GlobalEx.getUserBeLong()
	 * @return InfoPackage getSucess()==true 可以编辑 false:不可以编辑
	 *         通过getsMessage()获取提示信息
	 * @throws Exception
	 */
	public InfoPackage getAuditCanEdit(String aPrjCode, String userCode,
			String depID, String moduleid) throws Exception;

	/**
	 * 单位送审项目
	 * 
	 * @param prjCode
	 *            项目编码
	 * @return InfoPackage getSucess()==true 送审成功 false:送审失败
	 *         通过getsMessage()获取提示信息
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes,
			String aUserCode, String rgCode, String moduleid) throws Exception;

	public InfoPackage BackAuditTZInfoByDiv(int type, String[] prjCodes,
			String aUserCode, String rgCode, String moduleid) throws Exception;

	public InfoPackage sendAuditInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public InfoPackage startflowInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public String getCurState(String setYear, String rgCode) throws Exception;

	public String getSupportNValue(String supType) throws Exception;

	public DataSet getOptDataTypeList() throws Exception;

	public String savehistory(String[] prjCodes,boolean isSave) throws Exception;

	public String saveTZhistory(String[] prjCodes) throws Exception;

	public InfoPackage sendTZAuditInfoByDiv(List xmxhs, String aUserCode,
			String rgCode, String moduleid) throws Exception;

	public int getCurBatchNO() throws Exception;

}
