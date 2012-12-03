package gov.nbcs.rp.input.ibs;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;
import java.util.Map;




public interface IPrjInput
{

	public final static String ROW_ID = "ROW_ID";



	/**
	 * 项目填报详细信息(金额)
	 * 
	 * @return DataSet
	 * @throws Exception
	 */
	public DataSet getPrjTbDetailInfo(String sYear, String rgCode, String xmbm, String en_code, String xmxh) throws Exception;


	public DataSet getQueryProject(String cond) throws Exception;


	public DataSet getQueryProject2(String cond) throws Exception;


	public String getNodeName(String xmxh, String year) throws Exception;


	/**
	 * 获取预算单位
	 * 
	 * @return
	 * @throws Exception
	 */
	// public DataSet getEnterprise() throws Exception;
	/**
	 * 获取执行周期
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmZxzq(String setYear, String rgCode) throws Exception;


	/**
	 * 获取项目分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmfl(String setYear, String rgCode) throws Exception;


	/**
	 * 获取项目状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmzt(String setYear, String rgCode) throws Exception;


	/**
	 * 获取项目属性
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmXmsx(String setYear, String rgCode) throws Exception;


	/**
	 * 获取科目选择
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmKmxz(String setYear, String rgCode) throws Exception;


	/**
	 * 获取单位选择
	 * 
	 * @return
	 * @throws Exception
	 */
	// public List getEnlist(String setYear,String rgCode) throws Exception;
	/**
	 * 获取经济科目
	 * 
	 * @return
	 * @throws Exception
	 */

	public DataSet getjjKm(String setYear, String rgCode) throws Exception;


	/**
	 * 获取科目选择
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmKmxzByExitKm(String inKm) throws Exception;


	/**
	 * 判断项目名称是否已经保存
	 * 
	 * @param enDm预算单位代码
	 * @param xmmc项目名称
	 * @return
	 * @throws Exception
	 */
	public List isExistsXmmc(String enDm, String xmmc, String setYear, String rgCode) throws Exception;


	/**
	 * 根据预算单位ID获取CODE
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public List getEnCode(String enID) throws Exception;


	/**
	 * 获取项目编码
	 * 
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public String getXmbm(String enCode, String setYear) throws Exception;


	/**
	 * 保存项目
	 * 
	 * @param list
	 * @param listKm
	 * @return
	 * @throws Exception
	 */
	public InfoPackage saveXmjl(List list) throws Exception;


	/**
	 * 修改项目
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editXmjl(DataSet ds) throws Exception;


	/**
	 * 修改项目
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmSearch(String year, String rgCode, Map parMap, String kmxz[], String enxz[]) throws Exception;


	/**
	 * 获取所有项目
	 * 
	 * @param year
	 * @param rgCode
	 * @param en_id
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmBALL(String year, String rgCode) throws Exception;


	/**
	 * 获得项目列表
	 * 
	 * @param year
	 * @param rgCode
	 * @return 点击不是叶子节点的时候查询项目
	 * @throws Exception
	 */
	public DataSet getXmByEnId(String year, String rgCode, String en_id) throws Exception;


	/**
	 * 获取项目列表
	 * 
	 * @param year
	 * @param rgCode
	 * @return 点击不是叶子节点的时候查询项目
	 * @throws Exception
	 */

	public DataSet getXmByEnLike(String year, String rgCode, String div_code) throws Exception;


	/**
	 * 删除项目
	 * 
	 * @param listSql
	 * @throws Exception
	 */
	public void postData(List listSql) throws Exception;


	/**
	 * 根据项目序号获取项目
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmByXmxh(String xmxh, String setYear, String rgCode) throws Exception;


	// /**
	// * 根据项目序号获取项目历史
	// * @param xmxh
	// * @return
	// * @throws Exception
	// */
	// public List getXmByXmxhHs(String xmxh) throws Exception;
	/**
	 * 根据项目序号获取项目明细历史
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */

	public List getXmmxByXmxhHs(String xmxh) throws Exception;


	/**
	 * 根据预算单位获取项目列表
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */

	public DataSet getXmListByEnID(String enID, String rgCode) throws Exception;


	/**
	 * 根据项目序号获取项目
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmDataByXmxh(String xmxh) throws Exception;


	/**
	 * 根据项目序号获取项目金额信息
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmmxByXmxh(String xmxh) throws Exception;


	/**
	 * 判断项目是否被使用过
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List isExistsXm(String xmxhs) throws Exception;


	/**
	 * 根据预算单位ID获取单位控制数
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public DataSet getDwkzsByEnId(String year, String rgCode, String enId) throws Exception;


	/**
	 * 根据预算单位ID获取单位控制数ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public List getDwkzsListByEnId(String year, String rgCode, String enId) throws Exception;


	/**
	 * 根据预算单位ID获取单位控制数ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public DataSet getDwkzsByEnIdLike(String year, String rgCode, String enCode) throws Exception;


	/**
	 * 根据预算单位ID获取单位控制数ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	public List getDwkzsListByEnIdLike(String year, String rgCode, String enCode) throws Exception;


	/**
	 * 判断预算单位是否有控制数
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public List isExistsDwkzs(String enID) throws Exception;


	/**
	 * 根据ID获取单位控制数
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public List getDwkzsByDwkzsId(String dwkzsid) throws Exception;


	/**
	 * 根据ID获取单位控制数
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public DataSet getDwkzsByDwkzsIdForDs(String dwkzsid) throws Exception;


	/**
	 * 单位控制数
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editDwkzs(DataSet ds) throws Exception;


	/**
	 * 根据项目编码来获取已经选择的科目信息
	 * 
	 * @param aPrjCode
	 * @return
	 * @throws Exception
	 */
	public String getAcctInfo(String aPrjID) throws Exception;


	/**
	 * 项目排序-获取项目
	 * 
	 * @param year
	 * @param rgCode
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmSortListByEnID(String year, String rgCode, String enCode, String identity) throws Exception;


	/**
	 * 获取项目上报理由
	 * 
	 * @param year
	 * @param rgCode
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmSbLyByXmxh(String year, String rgCode, String xmxh) throws Exception;


	/**
	 * 根据项目序号获取对应的科目列表
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmKmListByXmxh(String xmxh) throws Exception;


	/**
	 * 根据功能科目code获取id
	 * 
	 * @param bsCode
	 * @return
	 * @throws Exception
	 */
	public String getBsIDByCode(String bsCode) throws Exception;


	/**
	 * 获取某表数据
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List getList(String tableName) throws Exception;


	/**
	 * 根据项目序号获取项目名称
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getKmmcByXmxh(String xmxh) throws Exception;


	/**
	 * 获取DBLINK链接
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBLink() throws Exception;


	/**
	 * 获取远程数据-项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBDataXm(String dbLinkName, String tableName, String divCode) throws Exception;


	/**
	 * 获取导入数据的单位
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDisDivCode(String dbLinkName) throws Exception;


	/**
	 * 获取导入的科目
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDisDivKm(String dbLinkName, String divCode, String prjCode) throws Exception;


	public String getlcwz(String xmbm) throws Exception;


	/**
	 * 获取导入项目明细
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDataTbxm(String dbLinkName, String divCode, String prjCode) throws Exception;


	/**
	 * 获取导入项目明细
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public boolean insertJlDatahis(String xmbm);


	/**
	 * 根据单位获得获取项目列表
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public List getxmTableInfo(String divCode, int isBack, int stepId) throws Exception;


	/**
	 * 根据单位获得获取项目列表
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public void deleteDwkzs(String kzsid) throws Exception;


	/**
	 * 根据单位获得获取项目列表
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public List getxmTableInfoAll(String divCode) throws Exception;


	public DataSet getxmTableInfoAllds(String divCode, String moduleid, String flowstatus) throws Exception;


	public boolean getHisNull(String prjCode) throws Exception;


	public DataSet getxmTableInfods(String divCode, String moduleid, String flowstatus) throws Exception;


	// ,
	public int[] getxmztNum(String divCode, boolean isTB) throws Exception;


	public DataSet getEnxmxhs(String xmxhs) throws Exception;


	public DataSet getPrjCreateInfo(String filter) throws Exception;


	public DataSet getPrjCreateHisInfo(String set_year, String filter) throws Exception;


	/**
	 * 保存项目建立信息（单位用）
	 * 
	 * @param setYear
	 * @param xmxh
	 * @param dsPrj
	 * @param kmInfo
	 * @param rgCode
	 * @param batchNo
	 * @param auditNo
	 * @throws Exception
	 */
	public void savePrjCreateInfo(String setYear, String xmxh, DataSet dsPrj, String[] kmInfo, String rgCode, String batchNo, String auditNo) throws Exception;


	/**
	 * 生成建立信息
	 * 
	 * @param setYear
	 * 
	 * @param rgCode *
	 * @param xmxhs
	 * @param batchNo
	 * @param auditNo
	 * @throws Exception
	 */
	public void doPrjCreateInfo(String setYear, String xmxhstr, String prjCode, String rgCode, String step_id, String batchNo, String auditNo) throws Exception;


	/**
	 * 获取项目明细信息
	 */
	public DataSet getPrjDetailInfo(String setYear, String xmxh, String rgCode) throws Exception;


	/**
	 * 获取项目明细信息
	 */
	public DataSet getPrjDetailInfoHis(String setYear, String xmxh, String rgCode) throws Exception;


	/**
	 * 审核单位信息
	 * 
	 * @param divCode
	 * @param setYear
	 * @param rgCode
	 * @return
	 * @throws Exception
	 */
	public DataSet doAuditDivData(String divCode, String setYear, String rgCode) throws Exception;


	/**
	 * 保存明细信息
	 * 
	 * @param enID
	 * @param xmxh
	 * @param ds
	 * @param setYear
	 * @param rgCode
	 * @param userID
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public InfoPackage savePrjDetailInfo(String enID, String xmxh, DataSet ds, String setYear, String rgCode, String userID, String date) throws Exception;


	/**
	 * 保存项目历史记录（包括建立信息和明细信息）
	 * 
	 * @param xmxh
	 * @param batchNo
	 * @param stepID
	 * @throws Exception
	 */
	public void savePrjInfoHistory(String[] xmxh, int batchNo, int stepID) throws Exception;


	public int getPrjCount(String div_code);


	/**
	 * 查申报记录
	 * 
	 */
	// 查看
	public DataSet getPrjSbCreateInfo(String filer, String flowstatus, String moduleId) throws Exception;


	/**
	 * 保申报记录
	 * 
	 */
	public DataSet getPrjSbshCreateInfo(String filer, String flowstatus, String moduleId) throws Exception;


	// 保存
	public void savePrjCreateSbInfo(DataSet ds, String setYear, String xmxh, String[] kmInfo, String rgCode) throws Exception;


	public String sendbackAudit(List xmxhs, String rgCode, String setYear, String moduleId, int type) throws Exception;


	public String doSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception;


	/**
	 * 撤销退回
	 * 
	 * @param prjCode
	 *            项目编码
	 * @return
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes, String aUserCode, String rgCode, String moduleid, String year) throws Exception;


	public String doshSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception;


	public String saveTbPrj(Map map) throws Exception;


	public String savePrjTbDetailInfo(String enID, String xmxh, DataSet ds, String aYear, String rgCode, String userID, String date);


	/**
	 * 保存项目填报功能科目数据
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String saveTbPrjSubject(String sYear, String rgCode, String[] subject, String chrId);


	/**
	 * 根据单位得到附件列表
	 * 
	 * @param enId
	 * @return
	 * @throws Exception
	 */
	public DataSet getFjFiles(String enId, String flowstatus) throws Exception;


	public DataSet getControlMoney(String divCode, String year) throws Exception;
}
