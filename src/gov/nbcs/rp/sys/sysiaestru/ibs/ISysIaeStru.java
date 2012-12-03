package gov.nbcs.rp.sys.sysiaestru.ibs;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * 收支结构接口
 * 
 * @author qzc
 * 
 */
public interface ISysIaeStru {
	int iCodeLen = 3; // 编码长度

	String NAME = "name";

	String ROOT_CODE = "1001"; // 收入栏目和支出资金来源最顶级编码

	String Max_Lvl = "9999";

	/**
	 * 提交数据
	 * 
	 * @param dataSet
	 */
	public void postDataSet(DataSet dataSet) throws Exception;

	/**
	 * 显示格式
	 */
	public DataSet getSFormate(int setYear) throws Exception;

	/**
	 * 编辑格式
	 */
	public DataSet getEFormate(int setYear) throws Exception;

	/**
	 * 获得性质
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getKind(String typeID, String setYear) throws Exception;

	/**
	 * 获得收入栏目树
	 * 
	 * @return记录集
	 * @throws Exception
	 */
	public DataSet getIncColTre(int setYear) throws Exception;

	/**
	 * 得到收入栏目对收费项目
	 * 
	 * @param setYear
	 *            年份
	 * @param sIncColCode
	 *            收入栏目编码
	 * @return 返回收入栏目对收费项目DataSet
	 * @throws Exception
	 */

	public DataSet getInccolumnToToll(String setYear, String sIncColCode)
			throws Exception;

	/**
	 * 获得收入栏目树,可选栏目
	 * 
	 * @return记录集
	 * @throws Exception
	 */
	public DataSet getIncColTreCalc(int setYear, String sIncColCode)
			throws Exception;

	/**
	 * 取得收入预算表共个多少个收入栏目列
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getIncColIValue(String setYear) throws Exception;

	/**
	 * 判断能否删除收入栏目记录
	 * 
	 * @param sEName英文名称
	 * @param sIncColCode收入栏目编码
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableDel(String sEName, String sIncColCode,
			String setYear) throws Exception;

	/**
	 * 判断能否修改收入栏目记录
	 * 
	 * @param sEName英文名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableModify(String sEName, String setYear)
			throws Exception;

	/**
	 * 判断收入栏目填写的编码是否重复
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :不重复,false 重复
	 * @throws Exception
	 */
	public InfoPackage judgeIncColCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断收入栏目填写的名称是否重复
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :不重复,false 重复
	 */
	public InfoPackage judgeIncColNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断收入栏目编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgeIncColParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * 保存收入栏目
	 * 
	 * @param dsIncCol收入栏目DataSet
	 * @param sIncOldColCode原收入栏目编码
	 * @param sIncColCode收入栏目编码
	 * @paramb AddFirstNode是否增加的第一个节点
	 * @param lstTollCode收费项目列表
	 * @param setYear年份
	 * @throws Exception
	 */
	public void saveIncCol(DataSet dsIncCol, String sIncOldColCode,
			String sIncColCode, boolean bAddFirstNode, ArrayList lstTollCode,
			String setYear) throws Exception;

	/**
	 * 删除收入栏目
	 * 
	 * @param dsIncCol
	 *            收入栏目DataSet
	 * @param sIncColParCode
	 *            父节点编码
	 * @param sIncColCode
	 *            收入栏目编码
	 * @param setYear年份
	 * @param sIncColName收入栏目名称
	 * @param sIncColEname收入栏目ename
	 * @throws Exception
	 */
	public void delIncCol(DataSet dsIncCol, String sIncColParCode,
			String sIncColCode, String setYear, String sIncColName,
			String sIncColEname) throws Exception;

	/**
	 * 取得支出资金来源共个多少个栏目列
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public int getPayOutFsIValue(int setYear) throws Exception;

	/**
	 * 获得支出资金来源
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSTre(int setYear) throws Exception;

	/**
	 * 获得支出资金来源,可选栏目
	 * 
	 * @return记录集
	 * @throws Exception
	 */
	public DataSet getPayOutFSCalc(int setYear, String sPfsCode)
			throws Exception;

	/**
	 * 检查资金来源是否被使用 0未被使用1被引用（设入结构）2被使用（录入数据）3既被引用又被使用。
	 * 
	 * @param iSetYear
	 * @param sPfsCode
	 * @param sPfsField
	 * @return
	 */
	public InfoPackage chkFundSourceByRef(String setYear, String sPfsCode,
			String sPfsField) throws Exception;

	/**
	 * 删作资金来源，做到一个方法，提交，做成事务
	 * 
	 * @param setYear
	 * @param sSrcPfsCode源资金来源编码
	 * @param sTagPfsCode目标资金来源编码，用在修改资金来源编码时,删除时该参数为空.
	 * @param sPfsField资金来源对应的字段。
	 * @param sOptType操作类型，1删除2修改。
	 * @param dataSet,提交的数据集
	 * @param iClearFlag,true:，调用
	 *            ClearFundSourceData方法，清空资金来源信息;false不调用
	 * @param sPfsName名称
	 * @return
	 */
	public void delFundSource(String setYear, String sSrcPfsCode,
			String sTagPfsCode, String sPfsField, int iOptType,
			DataSet dataSet, boolean iClearFlag, String sPfsName)
			throws Exception;

	/**
	 * 获得收入项目类别
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getIncTypeTre(int setYear) throws Exception;

	/**
	 * 判断收入项目类别是否能删除
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableDel(String sCode, String setYear)
			throws Exception;

	/**
	 * 判断收入项目类别是否能修改
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableModify(String sCode, String setYear)
			throws Exception;

	/**
	 * 判断收入项目类别填写的编码是否重复
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :不重复,false 重复
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断收入项目类别填写的名称是否重复
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :不重复,false 重复
	 */
	public InfoPackage judgeIncTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断收入项目类别编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgeIncTypeParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * 删除收入项目类别
	 * 
	 * @param dataSet
	 * @param sCode,当前树lvl编码 *
	 * @param sParCode,当前节点父节点编码 *
	 * @param setYear
	 * @param sIncTypeName
	 * @param sPfsCode
	 */
	public void delIncType(DataSet dataSet, String sCode, String sParCode,
			String setYear, String sIncTypeName) throws Exception;

	/**
	 * 保存收入项目类别
	 * 
	 * @param dsIncType
	 *            收入项目类别数据集
	 * @param sOldIncTypeCode
	 *            收入项目类别原编码
	 * @param sIncTypeCode
	 *            收入项目类别新编码
	 * @param dsInccolumnToInc
	 *            选中的收入栏目列表、收入预算科目和收费项目
	 * @param setYear年份
	 * @param sInctypeName
	 *            收入项目类别名称，修改名称存值，其他存null
	 * @param lstPfsCode
	 *            支出资金来源
	 * @throws Exception
	 */
	public void saveIncType(DataSet dsIncType, String sOldIncTypeCode,
			String sIncTypeCode, DataSet dsInccolumnToInc, String setYear,
			String sInctypeName, List lstPfsCode) throws Exception;

	/**
	 * 根据收入项目类别编码，得到收入项目类别与收入栏目的对应关系
	 * 
	 * @param setYear年份
	 * @param sIncTypeCode收入类目类别编码
	 * @return
	 * @throws Exception
	 */
	public DataSet getInctypeToIncolumn(String setYear, String sIncTypeCode)
			throws Exception;

	/**
	 * 获得支出项目类别
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception;

	/**
	 * 删除支出项目类别
	 * 
	 * @param dataSet
	 * @param sCode
	 * @param sParCode
	 * @param setYear
	 */
	public void delPayOutKind(DataSet dataSet, String sCode, String sParCode,
			String sParName, String sParParId, String setYear,
			String sPayoutKindName) throws Exception;

	/**
	 * 保存支出项目类别
	 * 
	 * @param dataSet
	 * @throws Exception
	 */
	public void savePayOutKind(DataSet dataSet, DataSet dsPayoutKindToJj,
			String sSaveType, String sPayoutKindCode, String sPayOutKindName,
			String setYear, String sParCode) throws Exception;

	/**
	 * 保存支出经济科目对支出子项目
	 * 
	 * @param setYear
	 * @param lstSubItem
	 * @param sBsiId
	 * @param sAcctCodeJj
	 * @param sSubType
	 * @throws Exception
	 */
	public void saveAcctJjToSubItem(String setYear, String sBsiId,
			String sSubType) throws Exception;

	/**
	 * 保存存收入预算科目对收费项目
	 * 
	 * @param setYear
	 * @param sSelectNodeId
	 * @param sIN_BS_ID
	 * @param sAcctCodeJj
	 * @param sSubType
	 * @param iDataSrc
	 * @throws Exception
	 */
	public void saveIncAcctToIncItem(String setYear, ArrayList sSelectNodeId,
			ArrayList sSelectNodeCode, String sIN_BS_ID, String sAcctCodeInc,
			String sSubType, int iDataSrc) throws Exception;

	/**
	 * 作用，保存资金来源对应收入
	 * 
	 * @param setYear年份
	 * @param sDivCode
	 * @param iDataType
	 * @param sPFSCode
	 * @param dsPfsToIncitem,资金来源对应收入项目DataSet
	 * @throws Exception
	 */
	public DataSet savePfsToItem(String setYear, String sDivCode,
			String sPFSCode, DataSet dsPayOutFS, DataSet dsPfsToIncitem)
			throws Exception;

	/**
	 * 收支科目挂接明细， 得到收费项目cha_id,根据收入预算科目编码
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getIncItemWithCode(String sIN_BS_ID, String setYear)
			throws Exception;

	/**
	 * 判断经济科目对支出子项目能否修改
	 * 
	 * @param sBSI_ID
	 * @param sName
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeJjEnableModify(String sBsiId, String sName,
			String setYear) throws Exception;

	/**
	 * 判断收入预算科目对收费项目能否修改
	 * 
	 * @param sBSI_ID
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncEnableModify(String sActtCode, String sName,
			String setYear) throws Exception;

	/**
	 * 判断支出项目类别是否已被使用,用于修改和删除时判断
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @return true:未被使用，false,已被使用
	 * @throws Exception
	 */
	public InfoPackage judgePayOutTypeUse(String sPayoutKindCode, String setYear)
			throws Exception;

	/**
	 * 支出项目类别，得到经济科目id,根据支出项目类别编码
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getJjWithPayoutKindCode(String sPayoutKindCode,
			String setYear) throws Exception;

	/**
	 * 支出资金来源对收入，判断英文名称是否已被使用
	 * 
	 * @param sEname
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public boolean judgePfsEnameUse(String sEname, String setYear)
			throws Exception;

	/**
	 * 支出资金来源对收入，得到收入预算科目Id,根据支出资金来源编码
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getIncWithPfsCode(String sPfsCode, String setYear)
			throws Exception;

	/**
	 * 判断能否删除支出资金来源
	 * 
	 * @param sEName英文名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePfsEnableDel(String sEName, String setYear)
			throws Exception;

	/**
	 * 判断支出项目类别填写的编码是否重复
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :不重复,false 重复
	 * @throws Exception
	 */
	public boolean judgePayOutTypeCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * 支出项目类别,判断编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgePayOutTypeParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * 判断支出项目类别填写的名称是否重复
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @throws Exception
	 * @returntrue :不重复,false 重复
	 */
	public boolean judgePayOutTypeNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * 支出项目类别中，判断选中的经济科目是否已被选中
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @param bModify
	 * @return
	 * @throws Exception
	 */
	// public InfoPackage judgeJjIsCheck(ArrayList lstId, ArrayList lstName,
	// String setYear, String sPayOutTypeCode) throws Exception;
	/**
	 * 判断支出资金来源编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgePfsParExist(String sParLvlId, String setYear)
			throws Exception;

	/**
	 * 判断支出资金来源填写的编码是否重复
	 * 
	 * @param sLvlId
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @return true :不重复,false 重复
	 * @throws Exception
	 */
	public InfoPackage judgePfsCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断支出资金来源填写的名称是否重复
	 * 
	 * @param sName
	 * @param sPar
	 * @param setYear
	 * @param sCode
	 * @param bModidy
	 * @throws Exception
	 * @returntrue :不重复,false 重复
	 */
	public InfoPackage judgePfsNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception;

	/**
	 * 判断支出资金来源填写的名称是否重复
	 * 
	 * @param sCode
	 * @param bModify
	 * @return
	 * @throws Exception
	 */
	public boolean judgePfsStdTypeCode(String sCode, String setYear,
			boolean bModify) throws Exception;

	/**
	 * 判断资金来源对收入，收入项目是否被选用
	 * 
	 * @param lstId
	 * @param lstName
	 * @param setYear
	 * @param sPayOutTypeCode
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeIsCheck(ArrayList lstId, ArrayList lstName,
			String setYear, String sPfs) throws Exception;

	/**
	 * 保存支出资金来源
	 * 
	 * @param dsPayOutFS,支出资金来源DataSet
	 * @param sPfsCode,新增记录编码
	 * @param sParPfsCode,新增记录父记录的编码
	 * @param lstAcctCode功能科目列表
	 * @throws Exception
	 */
	public void savePayOutFS(DataSet dsPayOutFS, String sPfsCode,
			String sParPfsCode, String setYear, List lstAcctCode,
			boolean bAddFirstNode) throws Exception;

	/**
	 * 得到资金来源对功能科目
	 * 
	 * @param setYear
	 * @param sPfsCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsToAcct(String setYear, String sPfsCode)
			throws Exception;

	/**
	 * 判断是否已设置公式和公式与单位对应关系
	 * 
	 * @param payOutKindCode支出项目类别内码
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayoutTypeFormulaUse(String payOutKindCode)
			throws Exception;

	/**
	 * 判断经济科目与此支出项目类别是否存在对应关系
	 * 
	 * @param sPayoutKindCode
	 *            支出项目类别编码
	 * @param acctJJCode
	 *            经济科目编码
	 * @return 存在对应关系返回true,否则返回false
	 * @throws Exception
	 */

	public boolean JudgeAcctJJExist(String sPayoutKindCode, String acctJJCode)
			throws Exception;

	/**
	 * 改变支出项目类别
	 * 
	 * @param sNewPayoutKindCode新支出项目类别编码
	 * @param sNewPayoutKindName新支出项目类别父对象名称
	 * @param sOldPayoutKindCode原支出项目类别编码
	 * @param acctJjCode经济科目编码
	 * @throws Exception
	 * 
	 */
	public void changePayOutKind(String sNewPayoutKindCode,
			String sNewPayoutKindName, String sOldPayoutKindCode,
			String acctJjCode) throws Exception;

	/**
	 * 判断支出项目类别中经济科目是否使用
	 * 
	 * @param payOutKindId支出项目类别内码
	 * @param acctJjCode经济科目编码
	 * @return使用情况信息，未使用返回空值
	 * @throws Exception
	 */
	public String judgePayoutTypeAcctJjUse(String payOutKindCode,
			String acctJjCode) throws Exception;

	/**
	 * 得到支出项目类别-专项支出记录数
	 * 
	 * @param payouKindCode
	 * @return
	 * @throws Exception
	 */
	public String getPayoutKindStandPrj(String payouKindCode) throws Exception;

	/**
	 * 收入对支出资金来源，得到支出资金来源编码,根据收入预算科目Id
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getPfsToIncCode(String sIncTypeCode, String setYear)
			throws Exception;

	/**
	 * 获得支出资金来源(不包含数据来源为计算的记录）
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSNotCalcTre(int setYear) throws Exception;

	/**
	 * 得到收入项目类别is_inc字段值
	 * 
	 * @param incTypeCode
	 * @return
	 * @throws Exception
	 */
	public int getIncTypeIsInc(String incTypeCode) throws Exception;

	/**
	 * 判断预留比例是否存在
	 * 
	 * @param setYear
	 * @param sCode
	 * @return
	 * @throws Exception
	 */
	public DataSet judgeIncColYLBLExist(String setYear, String sCode)
			throws Exception;

	/**
	 * 得到支出资金来源总计记录
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsHj(String setYear) throws Exception;
}
