package gov.nbcs.rp.sys.sysiaestru.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 收支结构接口实现
 * 

 */
public class SysIaeStruBO implements ISysIaeStru {
	/**
	 * 提交数据
	 * 
	 * @param dataSet
	 */
	public void postDataSet(DataSet dataSet) throws Exception {
		dataSet.post();
	}

	/**
	 * 获得显示格式
	 */
	public DataSet getSFormate(int setYear) throws Exception {
		String sFormateSQL = " select distinct '' as name from fb_s_pubcode"
				+ " union all"
				+ " select Name from fb_s_pubcode "
				+ " where typeID='DQRDISPLAY' and cvalue='带小数的数值' and  set_year=?"
				+ " Order By Name ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sFormateSQL);
		ds.open();
		return ds;
	}

	/**
	 * 获得编辑格式
	 */
	public DataSet getEFormate(int setYear) throws Exception {
		String eFormateSQL = "select distinct '' as name from fb_s_pubcode"
				+ " union all" + " select Name from fb_s_pubcode"
				+ " where typeID='DQREDIT' and cvalue='带小数的数值' and set_year=?"
				+ " Order By Name";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(eFormateSQL);
		ds.open();
		return ds;
	}

	/**
	 * 获得性质
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getKind(String typeID, String setYear) throws Exception {
		String sSql = "select code,name from fb_s_pubcode where typeId=?  and set_Year = ?  order by code";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new String(typeID),
				new Integer(setYear) });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * 获得收入栏目树
	 */
	public DataSet getIncColTre(int setYear) throws Exception {
		String incColSQL = "select lvl_id||' '||inccol_name as name ,a.*  from fb_iae_inccolumn a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(incColSQL);
		ds.open();
		return ds;
	}

	/**
	 * 获得收入栏目树,可选栏目
	 * 
	 * @return记录集
	 * @throws Exception
	 */
	public DataSet getIncColTreCalc(int setYear, String sIncColCode)
			throws Exception {
		String sSql = "select inccol_code,inccol_fname,'' as par_id,inccol_ename "
				+ " from fb_iae_inccolumn where set_year=? and end_flag=1 and inccol_code<>?"
				+ " order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear), sIncColCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

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
			throws Exception {
		String sSql = "select toll_code "
				+ " from fb_iae_inccolumn_to_toll where set_year=? and  inccol_code=? ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { setYear, sIncColCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * 取得收入预算表共个多少个收入栏目列
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getIncColIValue(String setYear) throws Exception {
		String sSql = "select IValue from fb_s_pubcode where TypeID = 'MAXFIELDNUM' and code='002' and set_Year="
				+ setYear;
		return DBSqlExec.getIntValue(sSql);
	}

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
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// 判断该收入栏目已作为其他栏目计算公式的内容,如果是，不允许删除
		String Epq = "%{" + sEName + "}%";
		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", "formula_Det like '"
				+ Epq + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("该收入栏目已作为其他栏目计算公式的内容,请先删除其他栏目!");
			return infoPackage;
		}
		// 判断此记录是否已被收入预算表使用，如果已被使用，不允许删除
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sEName
				+ ">0 and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入栏目已被收入预算表使用,不能删除!");
			return infoPackage;
		}
		// 判断收入项目类别与收入栏目是否建立了对应关系
		if (DBSqlExec.getRecordCount("fb_iae_inctype_to_incolumn",
				"IncCol_code ='" + sIncColCode + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入栏目已被收入项目类别设置对应关系,不能删除!");
			return infoPackage;
		}

		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 判断能否修改收入栏目记录
	 * 
	 * @param sEName英文名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncColEnableModify(String sEName, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sEName
				+ ">0 and set_Year =" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入栏目已被使用,只能修改显示格式!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 判断编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgeIncColParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_inccolumn", setYear);
	}

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
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // 增加
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// 修改
			sFilterA = "lvl_id ='" + sLvlId + "' and inccol_code <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("编码已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

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
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and INCCOL_NAME = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and INCCOL_NAME = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// 是否是修改,判断条件加上不等于自己
			sFilter = sFilter + " and inccol_code != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_inccolumn", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入栏目名称已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

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
			String setYear) throws Exception {
		// 得到optMenu值
		String optMenu = UpdateStruPub.getOptMenu(dsIncCol, sIncColCode,
				IIncColumn.INCCOL_CODE, IIncColumn.INCCOL_NAME,
				IIncColumn.INCCOL_ROOT);

		// 得到收入栏目的OptSql,需在post前执行
		String optSql = UpdateStruPub.getIncColFormulaOptSql(dsIncCol,
				sIncColCode);

		// 交提收入栏目树信息
		dsIncCol.post();

		ArrayList lstSql = new ArrayList();
		String sSql = null;
		// 删除原对应关系
		if (!"".equals(sIncOldColCode) && sIncOldColCode != null) {
			sSql = "delete fb_iae_inccolumn_to_toll where incCol_code ='"
					+ sIncOldColCode + "' and set_year=" + setYear;
		}
		lstSql.add(sSql);
		// 新对应关系
		if (lstTollCode != null) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (int i = 0; i < lstTollCode.size(); i++) {
				sSql = "insert into fb_iae_inccolumn_to_toll(inccol_code,toll_code,set_year,rg_code) values('"
						+ sIncColCode
						+ "','"
						+ lstTollCode.get(i).toString()
						+ "'," + setYear + ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
		}
		// 是否增加的第一个节点，如增加将父对象与收入项目类别的对应关系设给子节点
		if (bAddFirstNode) {
			sSql = "update fb_iae_inctype_to_incolumn  set incCol_code ='"
					+ sIncColCode + "' where incCol_code ='" + sIncOldColCode
					+ "' and set_year=" + setYear;
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

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
			String sIncColEname) throws Exception {
		dsIncCol.post();
		String OptSql = "";
		String sSql = null;
		// sIncColParCode == null说明删除节点的父节点不仅有一个子节点
		if (sIncColParCode == null) {
			// 删除原对应关系
			sSql = "delete fb_iae_inccolumn_to_toll where incCol_code ='"
					+ sIncColCode + "' and set_year=" + setYear;
			// 更新fb_u_Div_Incoming表sIncColEname字段为零值
			OptSql = UpdateStruPub.getUpdateToZeroSql("fb_u_Div_Incoming",
					sIncColEname);
		} else {// 更改收入栏目编码，将子节点与收费项目的对应关系更改为父节点与收费项目
			sSql = "update fb_iae_inccolumn_to_toll set incCol_code ='"
					+ sIncColParCode + "' where incCol_code ='" + sIncColCode
					+ "' and set_year=" + setYear;
		}
		QueryStub.getQueryTool().executeUpdate(sSql);
		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IIncColumn.INCCOL_ROOT + "-" + sIncColName, OptSql);
	}

	/**
	 * 获得支出资金来源
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSTre(int setYear) throws Exception {
		String payOutFSSQL = "select lvl_id||' '||PFS_NAME as name ,a.*  from Fb_Iae_Payout_Fundsource a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(payOutFSSQL);
		ds.open();
		return ds;
	}

	/**
	 * 获得支出资金来源,可选栏目
	 * 
	 * @return记录集
	 * @throws Exception
	 */
	public DataSet getPayOutFSCalc(int setYear, String sPfsCode)
			throws Exception {
		String sSql = "select pfs_code,pfs_fname,'' as par_id"
				+ " from Fb_Iae_Payout_Fundsource a where set_year=? and end_flag=1 and pfs_code <>?"
				+ " order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear), sPfsCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * 取得支出资金来源共个多少个栏目列
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public int getPayOutFsIValue(int setYear) throws Exception {
		String sSql = "select IValue from fb_s_pubcode where TypeID = 'MAXFIELDNUM' and code='001' and set_Year="
				+ setYear;
		return DBSqlExec.getIntValue(sSql);
	}

	/**
	 * 检查资金来源是否被使用 0未被使用1被引用（设入结构）2被使用（录入数据）3既被引用又被使用。
	 * 
	 * @param iSetYear
	 * @param sPfsCode
	 * @param sPfsField
	 * @return
	 */
	public InfoPackage chkFundSourceByRef(String setYear, String sPfsCode,
			String sPfsField) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		infoPackage.setSuccess(true);
		int iCiteFlag = 0;// 引用标志
		int iUseFlag = 0; // 使用标志
		// 判断是否在"支出项目单元格公式相关引用元素表","资金来源对收入项目表","支出项目单元格公式表"是否引用
		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula",
				"FFieldName='" + sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "已被\""
					+ getTableChinaName("FB_IAE_PAYOUT_CELL_FORMULA")
					+ "\"使用，\n");
			infoPackage.setSuccess(false);
		}

		if (DBSqlExec.getRecordCount("fb_Iae_Pfs_To_IncItem", "PFS_Code='"
				+ sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "已被\""
					+ getTableChinaName("FB_IAE_PFS_TO_INCITEM") + "\"使用，\n");
			infoPackage.setSuccess(false);
		}
		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_RefElet",
				"FFieldName='" + sPfsField + "' and set_year=" + setYear) > 0) {
			iCiteFlag = 1;
			infoPackage.setsMessage(infoPackage.getsMessage() + "已被\""
					+ getTableChinaName("FB_IAE_PAYOUT_CELL_REFELET")
					+ "\"使用，\n");
			infoPackage.setSuccess(false);
		}
		// 与资金来源有关的表
		DataSet ds = getFundSouceDataTable(setYear);
		ds.beforeFirst();
		while (ds.next()) {
			if (DBSqlExec.getRecordCount(ds.fieldByName("name").getString(),
					sPfsField + ">0 and set_year=" + setYear) > 0) {
				iUseFlag = 2;
				infoPackage.setsMessage(infoPackage.getsMessage()
						+ "已被\""
						+ getTableChinaName(ds.fieldByName("name").getString()
								.toUpperCase()) + "\"引用，\n");
				infoPackage.setSuccess(false);
				break;
			}
		}
		infoPackage.setObject(new Integer(iCiteFlag + iUseFlag));
		return infoPackage;
	}

	/**
	 * 根据表的英文名称获得表的中文名称
	 * 
	 * @param sTableName
	 * @return
	 * @throws Exception
	 */
	private String getTableChinaName(String sTableName) throws Exception {
		String sSql = " select comments from all_tab_comments where table_name = '"
				+ sTableName.toUpperCase() + "'";
		return DBSqlExec.getStringValue(sSql);
	}

	/**
	 * 获得与支出资金来源有关的表信息
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private DataSet getFundSouceDataTable(String setYear) throws Exception {
		// 与资金来源有关的表
		String sSql = "Select Name From fb_s_PubCode Where set_Year=" + setYear
				+ " And TypeID = 'PFSDATATABLE' Order By lvl_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 获得支出项目类别
	 * 
	 * @param settYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutKind(int setYear) throws Exception {
		String sSQL = "select lvl_id||' '||PAYOUT_KIND_NAME as name ,a.*  from fb_iae_payout_kind a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sSQL);
		ds.open();
		return ds;
	}

	/**
	 * 处理删除或修改资金来源相对应的引用信息
	 * 
	 * @param setYear
	 * @param ASrcPfsCode源资金来源编码
	 * @param ATagPfsCode目标资金来源编码，用在修改资金来源编码时,删除时该参数为空.
	 * @param APfsField资金来源对应的字段。
	 * @param AOptType操作类型，1删除2修改。
	 * @throws Exception
	 */
	private void ModFundSourceRefInfo(String setYear, String ASrcPfsCode,
			String ATagPfsCode, String APfsField, int AOptType)
			throws Exception {
		if (AOptType == 1) { // 删除
			// 支出项目公式对单位表
			String sSql = "Select distinct Formula_ID,Payout_Item_Code "
					+ " From fb_Iae_PayOut_Formula_To_Div Where set_Year=? and FFieldName =?";
			DataSet ds = DataSet.create();
			ds.setQueryParams(new Object[] { new Integer(setYear), APfsField });
			ds.setSqlClause(sSql);
			ds.open();

			InfoPackage infoPackage = new InfoPackage();
			ds.beforeFirst();
			while (ds.next()) {
				if (!PubUntSys.unReg_Div_Formula(setYear, "", ds.fieldByName(
						"Formula_ID").getString(), APfsField, ds.fieldByName(
						"Payout_Item_Code").getString(), -1, -1, infoPackage))
					return;
			}

			ArrayList lstSql = new ArrayList();
			// 支出项目单元格公式表
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and FFieldName ='"
					+ APfsField + "'";
			lstSql.add(sSql);
			// 资金来源对收入项目表
			sSql = "Delete From fb_Iae_Pfs_To_IncItem  Where set_Year="
					+ String.valueOf(setYear) + " and Pfs_Code='" + ASrcPfsCode
					+ "'";
			lstSql.add(sSql);
			// 支出项目单元格公式表
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and FFieldName ='"
					+ APfsField + "'";
			lstSql.add(sSql);

			QueryStub.getQueryTool().executeBatch(lstSql);
		} else if (AOptType == 2) { // 修改
			// 资金来源对收入项目表
			String sSql = "Update fb_Iae_Pfs_To_IncItem  Set Pfs_Code='"
					+ ATagPfsCode + "' Where set_Year="
					+ String.valueOf(setYear) + " and Pfs_Code ='"
					+ ASrcPfsCode + "'";
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

	}

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
			throws Exception {
		// 设置相关业务表对应字段的值为零
		List lstOptSql = null;
		if (iOptType == 1) {
			// 得到与资金来源结构有关的表
			DataSet ds = getFundSouceDataTable(setYear);
			lstOptSql = UpdateStruPub.getUpdateToZeroSql(ds, sPfsField);
		}

		if (sTagPfsCode == null || "".equals(sTagPfsCode)) {// 删除节点
			String sSql = "delete from fb_iae_pfs_to_acctcode where pfs_code = '"
					+ sSrcPfsCode + "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		} else {// 删除的节点信息转换给父节点
			String sSql = "update fb_iae_pfs_to_acctcode set pfs_code ='"
					+ sTagPfsCode + "' where pfs_code = '" + sSrcPfsCode
					+ "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}
		ModFundSourceRefInfo(setYear, sSrcPfsCode, sTagPfsCode, sPfsField,
				iOptType);
		dataSet.post();

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IPayOutFS.PFS_ROOT + "-" + sPfsName, lstOptSql);
	}

	/**
	 * 获得收入项目类别
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getIncTypeTre(int setYear) throws Exception {
		String sSQL = "select lvl_id||' '||INCTYPE_NAME as name ,a.*  from fb_iae_inctype a where set_year=? order by lvl_id";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear) });
		ds.setSqlClause(sSQL);
		ds.open();
		return ds;
	}

	/**
	 * 判断收入项目类别是否能删除
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableDel(String sCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// // 得到isInc字段值
		// int isInc = DBSqlExec
		// .getIntValue(
		// "select is_inc from fb_iae_inctype where inctype_code =? and
		// set_year=?",
		// new Object[] { sCode, setYear });

		// 判断是否从支出预算表取数
		// if (isInc != 2) {
		// 判断是否被资金来源对收入项目表使用不着
		if (DBSqlExec.getRecordCount("fb_Iae_Pfs_To_IncItem", "IncType_Code='"
				+ sCode + "' and set_Year = " + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入项目类别已被资金来源对收入项目表使用");
			return infoPackage;
		}
		// }
		// 判断是否被收入预算表使用
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming_budget",
				"IncType_Code='" + sCode + "' and INC_MONEY<>0 and set_Year = "
						+ setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入项目类别已被收入预算表使用");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 判断收入项目类别是否能修改
	 * 
	 * @param sCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgeIncTypeEnableModify(String sCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// 判断是否被收入预算表使用
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming_budget",
				"IncType_Code='" + sCode + "' and INC_MONEY<>0 and set_Year = "
						+ setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入项目类别已被收入预算表使用，只能修改编码!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

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
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // 增加
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// 修改
			sFilterA = "lvl_id ='" + sLvlId + "' and inctype_code <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_incType", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("编码已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

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
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and inctype_name = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and inctype_name = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// 是否是修改,判断条件加上不等于自己
			sFilter = sFilter + " and inctype_code != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_inctype", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("收入项目类别名称已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 判断收入项目类别编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgeIncTypeParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_inctype", setYear);
	}

	/**
	 * 删除收入项目类别
	 * 
	 * @param dataSet
	 * @param sCode,当前树lvl编码 *
	 * @param sParCode,当前节点父节点编码
	 * @param setYear
	 * @param sIncTypeName
	 * 
	 */
	public void delIncType(DataSet dataSet, String sCode, String sParCode,
			String setYear, String sIncTypeName) throws Exception {
		// 删除fb_u_Div_Incoming_budget表中PAYOUT_KIND_CODE=sCode记录
		String OptSql = UpdateStruPub.getClearDataSql(
				"fb_u_Div_Incoming_budget", sCode, IIncType.INCTYPE_CODE);

		// 将本节点与收入栏目的对应关系设置给父节点
		if (!"".equals(sParCode) && sParCode != null) {
			String sSql = "update fb_iae_inctype_to_incolumn set inctype_code ='"
					+ sParCode
					+ "' where inctype_code = '"
					+ sCode
					+ "' and set_year=" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
			sSql = "update fb_iae_pfs_to_incitem set inctype_code ='"
					+ sParCode + "' where inctype_code = '" + sCode
					+ "' and set_year=" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		} else {
			// DataSet dsPfsToInc = this.getPfsToIncCode(sCode, setYear);
			// if (dsPfsToInc != null && !dsPfsToInc.isEmpty()) {
			// dsPfsToInc.beforeFirst();
			// dsPfsToInc.next();
			// String sPfsCode = dsPfsToInc.fieldByName(IPayOutFS.PFS_CODE)
			// .getString();
			// // 将某个可支配收入数的某个支出资金来源金额设为零
			// if (!Common.isNullStr(sPfsCode))
			// updateFundsourceIncZero(sPfsCode, setYear);
			// }

			// 删除对应关系
			String sSql = "delete fb_iae_inctype_to_incolumn where inctype_code ='"
					+ sCode + "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
			// 删除对应关系
			sSql = "delete fb_iae_pfs_to_incitem where inctype_code ='" + sCode
					+ "' and set_year =" + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);

		}
		// 提交数据
		dataSet.post();
		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IIncType.INCTYPE_ROOT + "-" + sIncTypeName, OptSql);
	}

	/**
	 * 将某个可支配收入数的某个支出资金来源金额设为零。
	 * 
	 * @param sPfsCode
	 * @param setYear
	 * @throws Exception
	 */
	// private void updateFundsourceIncZero(String sPfsCode, String setYear)
	// throws Exception {
	// String sSql = "UPDATE fb_u_Div_FundSource_Inc SET " + sPfsCode
	// + "=0.00 where set_year=" + setYear;
	// QueryStub.getQueryTool().execute(sSql);
	// }
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
			String sInctypeName, List lstPfsCode) throws Exception {
		DataSet dsOldPfsToInc = null;
		// 对应的支出资金来源编码，因一一对应
		String sPfsCode = null;
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			sPfsCode = lstPfsCode.get(0).toString();
		}
		// 得到optMenu值
		String optMenu = UpdateStruPub.getOptMenu(dsIncType, sIncTypeCode,
				IIncType.INCTYPE_CODE, IIncType.INCTYPE_NAME,
				IIncType.INCTYPE_ROOT);

		// 收入项目类别数据集提交数据
		dsIncType.post();

		String sSql = null;
		// 删除原对应关系
		if (!"".equals(sOldIncTypeCode) && sOldIncTypeCode != null) {
			if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
				// 得到原收入项目与支出资金来源原对应关系
				dsOldPfsToInc = getPfsToIncCode(sOldIncTypeCode, setYear);

				// 删除收入栏目与资金来源的对应关系根据收入栏目编码,不包含资金来源计算列
				sSql = "delete fb_iae_pfs_to_incitem where inctype_code ='"
						+ sOldIncTypeCode
						+ "' and pfs_code in ( select pfs_code from fb_iae_payout_fundsource where data_source=0 and set_year="
						+ setYear + ") and set_year=" + setYear;
				QueryStub.getQueryTool().execute(sSql);

				// 删除收入栏目与资金来源的对应关系根据资金来源编码
				sSql = "delete fb_iae_pfs_to_incitem where pfs_code ='"
						+ sPfsCode + "' and set_year=" + setYear;
				QueryStub.getQueryTool().execute(sSql);
			}

			sSql = "delete fb_iae_inctype_to_incolumn where inctype_code ='"
					+ sOldIncTypeCode + "' and set_year=" + setYear;
			QueryStub.getQueryTool().execute(sSql);

		}

		// 收入栏目新对应关系
		if (dsInccolumnToInc != null && !dsInccolumnToInc.isEmpty()) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			dsInccolumnToInc.beforeFirst();
			while (dsInccolumnToInc.next()) {
				sSql = "insert into fb_iae_inctype_to_incolumn(INCTYPE_CODE,INCCOL_CODE,TOLL_FILTER,set_year,rg_code) values(?,?,?,?,?)";
				QueryStub.getQueryTool().executeUpdate2(
						sSql,
						new Object[] {
								sIncTypeCode,
								dsInccolumnToInc.fieldByName(
										IIncColumn.INCCOL_CODE).getString(),
								dsInccolumnToInc.fieldByName(
										IIncType.TOLL_FILTER).getString(),
								setYear, sRgCode });
			}
		}

		// 支出资金来源新对应关系
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			List lstSql = new ArrayList();
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (Iterator it = lstPfsCode.iterator(); it.hasNext();) {
				String pfsCode = it.next().toString();
				// 删除选中的资金来源与收入栏目的对应关系
				sSql = "delete from  fb_iae_pfs_to_incitem where pfs_code='"
						+ pfsCode + "' and set_year=" + setYear;
				lstSql.add(sSql);

				sSql = "insert into fb_iae_pfs_to_incitem(PFS_CODE,INCTYPE_CODE,set_year,rg_code) values('"
						+ pfsCode
						+ "','"
						+ sIncTypeCode
						+ "',"
						+ setYear
						+ ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
			QueryStub.getQueryTool().executeBatch(lstSql);
		}

		// 原对应资金来源
		if (!"".equals(sOldIncTypeCode) && sOldIncTypeCode != null) {
			// 重新计算原支资来源
			if (dsOldPfsToInc != null && !dsOldPfsToInc.isEmpty()) {
				int iBatchNo = 0;
				dsOldPfsToInc.beforeFirst();
				while (dsOldPfsToInc.next()) {
					String oldPfsCode = dsOldPfsToInc.fieldByName(
							IPayOutFS.PFS_CODE).getString();
					// 原对应关系包含新选择的支出资金来源，不重复计算
					if (oldPfsCode.equals(sPfsCode)) {
						continue;
					}
					String sSqlDet = this.getSqlDetValue(oldPfsCode, setYear);
					// 更新原支出资金来源Sql_DET字段值
					sSql = "update fb_iae_payout_fundsource set sql_det=? where pfs_code = ? and set_year=?";
					QueryStub.getQueryTool().executeUpdate2(sSql,
							new Object[] { sSqlDet, oldPfsCode, setYear });
					// 重新计算
					calc_Div_FundSource_Inc(setYear, null, iBatchNo, -1,
							oldPfsCode);
				}
			}
		}
		// 新对应资金来源
		if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
			// 填写支出资金来源SQL_DET字段值
			updateSqlDet(sIncTypeCode, lstPfsCode, setYear);
			// 重新计算
			int iBatchNo = 0;
			calc_Div_FundSource_Inc(setYear, null, iBatchNo, -1, sPfsCode);
		}

		// 更改fb_u_Div_Incoming_budget名称
		String optSql = null;
		if (sInctypeName != null) {
			optSql = "update fb_u_Div_Incoming_budget set inctype_name = ''"
					+ sInctypeName + "'' where inctype_code = ''"
					+ sIncTypeCode + "'' and set_year = " + setYear;
		}

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

	/**
	 * 更改fb_iae_payout_fundsource表中sql_det值
	 * 
	 * @param sIncTypeCode
	 * @param lstPfsCode
	 * @param setYear
	 * @throws Exception
	 */
	private void updateSqlDet(String sIncTypeCode, List lstPfsCode,
			String setYear) throws Exception {
		// 对应的支出资金来源编码，因一一对应
		String pfsCode = lstPfsCode.get(0).toString();
		String sqlDet = "(INCTYPE_CODE = '" + sIncTypeCode + "') ";
		String sSql = "update fb_iae_payout_fundsource set sql_det=? where pfs_code = ? and set_year=?";
		QueryStub.getQueryTool().executeUpdate2(sSql,
				new Object[] { sqlDet, pfsCode, setYear });
	}

	/**
	 * 根据支出资金来源编码，得到对应的收入项目，组织fb_iae_payout_fundsource表中sql_det字段值
	 * 
	 * @param sPfsCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private String getSqlDetValue(String sPfsCode, String setYear)
			throws Exception {
		DataSet dsPfsCodeToInc = this.getIncWithPfsCode(sPfsCode, setYear);
		if (dsPfsCodeToInc == null && dsPfsCodeToInc.isEmpty()) {
			return "";
		}
		String sSqlIncTypeCode = "";
		dsPfsCodeToInc.beforeFirst();
		while (dsPfsCodeToInc.next()) {
			String sInctypeCode = dsPfsCodeToInc.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			if ("".equals(sSqlIncTypeCode) || sSqlIncTypeCode == null)
				sSqlIncTypeCode = "INCTYPE_CODE = '" + sInctypeCode + "'";
			else
				sSqlIncTypeCode = sSqlIncTypeCode + " OR INCTYPE_CODE = '"
						+ sInctypeCode + "'";
		}
		if (!"".equals(sSqlIncTypeCode)) {
			sSqlIncTypeCode = "(" + sSqlIncTypeCode + ")";
		}
		return sSqlIncTypeCode;
	}

	/**
	 * 根据收入项目类别编码，得到收入项目类别与收入栏目的对应关系
	 * 
	 * @param setYear年份
	 * @param sIncTypeCode收入类目类别编码
	 * @return
	 * @throws Exception
	 */
	public DataSet getInctypeToIncolumn(String setYear, String sIncTypeCode)
			throws Exception {
		String sSql = "select INCTYPE_CODE,INCCOL_CODE,TOLL_FILTER"
				+ " from fb_iae_inctype_to_incolumn  "
				+ " where inctype_code = ? and set_year=? order by inccol_code";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { sIncTypeCode, setYear });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;

	}

	/**
	 * 删除支出项目类别
	 * 
	 * @param dataSet
	 * @param sCode
	 * @param sParCode
	 * @param setYear
	 * @throws Exception
	 */
	public void delPayOutKind(DataSet dataSet, String sCode, String sParCode,
			String sParName, String sParParId, String setYear,
			String sPayoutKindName) throws Exception {
		// 删除表中PAYOUT_KIND_CODE=sCode记录
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		List lstOptSql = UpdateStruPub.getClearDataSql(dsTable, sCode,
				IPayOutType.PAYOUT_KIND_CODE);

		String sSql;
		// 提交数据
		dataSet.post();
		if ("".equals(sParCode)) {// 父节点至少有两个或两个以上儿子，删除节点信息
			// 支出项目单元格公式表
			sSql = " select distinct Formula_ID,FFieldName,payout_Item_code From fb_Iae_PayOut_Formula_To_Div where payout_Item_code='"
					+ sCode
					+ "' and payout_item_kind=0 and set_Year = "
					+ setYear
					+ " union all "
					+ " select distinct Formula_ID,FFieldName,payout_Item_code From  fb_Iae_PayOut_Formula_To_Div "
					+ " where payout_Item_code in (select Acct_code_jj from fb_Iae_PayOut_Kind_To_JJ where  payout_kind_code='"
					+ sCode
					+ "' and Set_Year = "
					+ setYear
					+ " ) and payout_item_kind=1 and par_id ='"
					+ sCode
					+ "' and set_Year = " + setYear;
			DataSet ds = DataSet.create();
			ds.setSqlClause(sSql);
			ds.open();

			InfoPackage infoPackage = new InfoPackage();
			ds.beforeFirst();
			while (ds.next()) {
				if (!PubUntSys.unReg_Div_Formula(setYear, "", ds.fieldByName(
						"Formula_ID").getString(), ds.fieldByName("FFieldName")
						.getString(), ds.fieldByName("payout_Item_code")
						.getString(), -1, -1, infoPackage))
					return;
			}

			ArrayList lstSql = new ArrayList();
			// 支出项目单元格公式表
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and payout_Item_code ='"
					+ sCode + "' and payout_item_kind=0";
			lstSql.add(sSql);
			sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
					+ String.valueOf(setYear) + " and par_id ='" + sCode
					+ "' and payout_item_kind=1";
			lstSql.add(sSql);
			// 支出项目单元格公式表
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and payout_Item_code ='"
					+ sCode + "' and payout_item_kind=0";
			lstSql.add(sSql);
			sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
					+ String.valueOf(setYear) + " and par_id ='" + sCode
					+ "' and payout_item_kind=1";
			lstSql.add(sSql);
			QueryStub.getQueryTool().executeBatch(lstSql);

			// 支出项目类别对经济科目
			sSql = "delete from  fb_Iae_PayOut_Kind_To_JJ where payout_Kind_code= '"
					+ sCode + "'  and Set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);

		} else {// 父节点只有一个儿子，将儿子信息传给父节点
			// 修改相关表的对应关系
			updateFormula(sParCode, sParParId, sCode, setYear);
			// 支出项目类别对经济科目
			sSql = "update  fb_Iae_PayOut_Kind_To_JJ set Payout_kind_code='"
					+ sParCode + "',Payout_kind_name='" + sParName
					+ "' where PayOut_Kind_Code='" + sCode
					+ "' and set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._DEL
				+ IPayOutType.PAYOUTKIND_ROOT + "-" + sPayoutKindName,
				lstOptSql);
	}

	/**
	 * 保存支出项目类别
	 * 
	 * @param dataSet
	 * @throws Exception
	 */
	public void savePayOutKind(DataSet dataSet, DataSet dsPayoutKindToJj,
			String sSaveType, String sPayoutKindCode, String sPayOutKindName,
			String setYear, String sParCode) throws Exception {
		// 得到optMenu值
		String optMenu = UpdateStruPub.getOptMenu(dataSet, sPayoutKindCode,
				IPayOutType.PAYOUT_KIND_CODE, IPayOutType.PAYOUT_KIND_NAME,
				IPayOutType.PAYOUTKIND_ROOT);

		List optSql = null;

		if (dataSet.fieldByName(IPayOutType.PAYOUT_KIND_NAME).getOldValue() != null) {
			String sOldPayoutKindName = dataSet.fieldByName(
					IPayOutType.PAYOUT_KIND_NAME).getOldValue().toString();
			if (!sPayOutKindName.equals(sOldPayoutKindName)) {
				if (optSql == null)
					optSql = new ArrayList();
				optSql.addAll(getUpdatePayouKindName(sPayoutKindCode,
						sPayOutKindName, setYear));
			}
		}

		// 提交数据
		dataSet.post();
		String sSql;

		// 如增加的是第一个子节点，修改父节点部分信息
		if ("addfirstson".equals(sSaveType)) {
			// 修改相关表的对应关系
			updateFormula(sPayoutKindCode, sParCode, sParCode, setYear);
		}

		String sCode = null;
		if ("addfirstson".equals(sSaveType)) {
			sCode = sParCode;
		} else if ("mod".equals(sSaveType) || "modformate".equals(sSaveType)) {
			sCode = sPayoutKindCode;
		}
		if ("addfirstson".equals(sSaveType) || "mod".equals(sSaveType)
				|| "modformate".equals(sSaveType)) {
			// 查询原来支出项目对经济科目对应关系
			DataSet dsPayOutKindToJJOld = DataSet.create();
			sSql = "select * from fb_iae_payout_kind_to_jj  where payout_kind_code='"
					+ sCode + "' and set_Year = " + setYear;
			DBSqlExec.getDataSet(sSql, dsPayOutKindToJJOld);
			// 判断原支出项目对经济科目对应关系在新支出项目对经济科目对应关系是否存在，
			// 如不存在，删除fb_Iae_PayOut_Formula_To_Div表中的经济科目对应关系
			InfoPackage infoPackage = new InfoPackage();
			dsPayOutKindToJJOld.beforeFirst();
			// boolean bEquals = false;
			String sPayoutItemCode;
			while (dsPayOutKindToJJOld.next()) {
				String sBSI_ID = dsPayOutKindToJJOld.fieldByName(
						IPubInterface.BSI_ID).getString();
				// modify by qzc 2009,6,4
				if (dsPayoutKindToJj != null
						&& !dsPayoutKindToJj.isEmpty()
						&& !dsPayoutKindToJj.locate(IPubInterface.BSI_ID,
								sBSI_ID)) {
					sSql = " select distinct Formula_ID,FFieldName,payout_Item_code From  fb_Iae_PayOut_Formula_To_Div "
							+ " where bsi_id='"
							+ sBSI_ID
							+ "' and Set_Year = "
							+ setYear
							+ " and payout_item_kind=1 and set_Year = "
							+ setYear + " and par_id ='" + sCode + "'";
					DataSet ds = DataSet.create();
					DBSqlExec.getDataSet(sSql, ds);
					ds.beforeFirst();
					while (ds.next()) {
						if (!PubUntSys.unReg_Div_Formula(setYear, "", ds
								.fieldByName("Formula_ID").getString(), ds
								.fieldByName("FFieldName").getString(), ds
								.fieldByName("payout_Item_code").getString(),
								-1, -1, infoPackage))
							return;
					}

					sPayoutItemCode = dsPayOutKindToJJOld.fieldByName(
							"acct_code_jj").getString();
					ArrayList lstSql = new ArrayList();
					// 支出项目单元格公式表
					sSql = "Delete From fb_Iae_PayOut_Cell_Formula Where set_Year="
							+ String.valueOf(setYear)
							+ " and par_id ='"
							+ sCode
							+ "' and payout_item_kind=1 and payout_Item_code='"
							+ sPayoutItemCode + "'";
					lstSql.add(sSql);
					// 支出项目单元格公式表
					sSql = "Delete From fb_Iae_PayOut_Cell_RefElet Where set_Year="
							+ String.valueOf(setYear)
							+ " and par_id ='"
							+ sCode
							+ "' and payout_item_kind=1 and payout_Item_code='"
							+ sPayoutItemCode + "'";
					lstSql.add(sSql);
					QueryStub.getQueryTool().executeBatch(lstSql);

					// 取消经济科目与支出项目类别对应关系，删除相关业务表中数据，注：只删除所有批次的业务数据
					if (optSql == null)
						optSql = new ArrayList();
					List lstUpdateScript = clearPayOutKindAcctJjData(sCode,
							sPayoutItemCode, setYear, -1, -1);
					optSql.addAll(lstUpdateScript);

				}
			}
			// 删除支出项目类别与经济科目的对应关系
			sSql = "delete from  fb_Iae_PayOut_Kind_To_JJ where payout_kind_code='"
					+ sCode + "' and set_Year = " + setYear;
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		// 保存支出项目类别与经济科目对应关系
		if (dsPayoutKindToJj != null) {
			dsPayoutKindToJj.setName("fb_Iae_PayOut_Kind_To_JJ");
			dsPayoutKindToJj.post();
		}

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, optSql);
	}

	/**
	 * 取消经济科目与支出项目类别对应关系，删除相关业务表中数据
	 * 
	 * @param payoutKindCode支出项目类别
	 * @param acctJjCode经济科目
	 * @param setYear年份
	 * @return
	 * @throws Exception
	 */
	private List clearPayOutKindAcctJjData(String payoutKindCode,
			String acctJjCode, String setYear, int iBatchNo, int iDataType)
			throws Exception {
		if (Common.isNullStr(payoutKindCode) || Common.isNullStr(acctJjCode)) {
			return null;
		}

		String filter;
		List lstSql = new ArrayList();
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			filter = " PayOut_Kind_Code=''" + payoutKindCode
					+ "'' and ACCT_CODE_JJ = ''" + acctJjCode + "''";
			// 表名
			String tableName = dsTable.fieldByName("name").getString();
			// 判断批次值及表是否存在此字段
			if (iBatchNo != -1
					&& QueryStub.getQueryTool().getColumnInfo(tableName)
							.contains("batch_no")) {
				filter = filter + " and Batch_No = " + iBatchNo;
			}
			// 判断数据类型值及表是否存在此字段
			if (iDataType != -1
					&& QueryStub.getQueryTool().getColumnInfo(tableName)
							.contains("data_type")) {
				filter = filter + " and Data_Type=" + iDataType;
			}
			filter = filter + " and set_Year = " + setYear;
			String sSql = "delete from " + tableName + " where " + filter;
			lstSql.add(sSql);
		}
		return lstSql;
	}

	/**
	 * 得到支出项目类别更改名称影响业务数据表的sql更新语句
	 * 
	 * @param payoutKindCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private List getUpdatePayouKindName(String payoutKindCode,
			String payoutKindName, String setYear) throws Exception {
		String filter;
		List lstSql = new ArrayList();
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			filter = " PayOut_Kind_Code=''" + payoutKindCode + "''";
			// 表名
			String tableName = dsTable.fieldByName("name").getString();
			filter = filter + " and set_Year = " + setYear;
			String sSql = "update  " + tableName + " set payout_kind_name =''"
					+ payoutKindName + "'' where " + filter;
			lstSql.add(sSql);
		}
		return lstSql;
	}

	/**
	 * 修改支出项目类别内码，修改有关表中相应的对应关系
	 * 
	 * @param sNewPayoutKindCode新的编码
	 * @param sNewParCode新的编码的父编码
	 * @param sOldPayoutKindCode原编码
	 * @param setYear
	 * @throws Exception
	 */
	private void updateFormula(String sNewPayoutKindCode, String sNewParCode,
			String sOldPayoutKindCode, String setYear) throws Exception {
		ArrayList lstSql = new ArrayList();
		// 支出项目公式值表
		// String sSql = " update fb_u_Div_PayOut_Formula_Value set
		// Payout_Item_code='"
		// + sNewPayoutKindCode
		// + "',Par_ID='"
		// + sNewParCode
		// + "' where Payout_Item_code='"
		// + sOldPayoutKindCode
		// + "' and payout_item_kind=0 and set_Year = " + setYear;
		// lstSql.add(sSql);
		// sSql = " update fb_u_Div_PayOut_Formula_Value set par_id='"
		// + sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
		// + "' and payout_item_kind=1 and set_Year = " + setYear;
		// lstSql.add(sSql);

		// 支出项目公式对单位表
		String sSql = " update  fb_iae_payout_formula_to_div set Payout_Item_code='"
				+ sNewPayoutKindCode
				+ "',Par_ID='"
				+ sNewParCode
				+ "',Calc_Flag=1 where Payout_Item_code='"
				+ sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_iae_payout_formula_to_div set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// 支出项目单元格公式相关引用元素表
		sSql = " update  fb_Iae_Payout_Cell_Refelet set Payout_Item_code='"
				+ sNewPayoutKindCode + "',Par_ID='" + sNewParCode
				+ "' where Payout_Item_code='" + sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_Iae_Payout_Cell_Refelet set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// 支出项目单元格公式表
		sSql = " update  fb_Iae_PayOut_Cell_Formula set Payout_Item_code='"
				+ sNewPayoutKindCode + "',Par_ID='" + sNewParCode
				+ "' where Payout_Item_code='" + sOldPayoutKindCode
				+ "' and payout_item_kind=0 and set_Year = " + setYear;
		lstSql.add(sSql);
		sSql = " update  fb_Iae_PayOut_Cell_Formula set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);
		QueryStub.getQueryTool().executeBatch(lstSql);

	}

	/**
	 * 作用：生成支出预算中支出资金来源可供支配的收入。 说明：部门预算编报讲求收支平衡，预算单位录入的每一项支出在收入预算表中都应该有
	 * 对应的收入，在用户设置了收入项目与资金来源的关系后，系统需要准备出一份相对于
	 * 支出资金来源的可支配收入数，以便在支出预算录入、部门预算收支审核时发生作用。
	 * 因此，本函数将在以下几种情况下被调用：一是用户改变了收入预算后；二是财政部门
	 * 在改变了支出资金来源信息后。三是财政部门在改变了资金来源与收入项目间的对应关系后。
	 * 
	 * @param setYear预算年度
	 * @param sDivCode单位编码
	 * @param iBatchNo批次
	 * @param iDataType数据类型
	 * @param sPFSCode资金来源编码，默认为空，不为空时表示计算指定支出资金来源的可供支配收入数。
	 * @throws Exception
	 * @returnTrue成功，False失败
	 */
	public boolean calc_Div_FundSource_Inc(String setYear, String sDivCode,
			int iCurBatchNo, int iDataType, String sPFSCode) throws Exception {
		if ((iCurBatchNo == -1 && iDataType != -1))
			return false;
		String sSql = "SELECT PFS_Code,PFS_EName,IncCol_EName,FORMULA_DET,SQL_DET"
				+ "  FROM fb_Iae_PayOut_FundSource"
				+ " WHERE set_Year="
				+ setYear + " AND End_Flag=1";
		if (!"".equals(sPFSCode) && sPFSCode != null)
			sSql = sSql + " AND PFS_Code='" + sPFSCode + "'";
		DataSet dsPfs = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsPfs);
		if (dsPfs.isEmpty())
			return false;

		ArrayList lstSql = new ArrayList();
		if (iDataType == -1) {
			for (int i = 1; i <= iCurBatchNo; i++) {
				fCalc_Div_FundSource_Inc(setYear, sDivCode, i, 0, dsPfs, lstSql);
				fCalc_Div_FundSource_Inc(setYear, sDivCode, i, 1, dsPfs, lstSql);
			}
		} else
			fCalc_Div_FundSource_Inc(setYear, sDivCode, iCurBatchNo, iDataType,
					dsPfs, lstSql);

		QueryStub.getQueryTool().executeBatch(lstSql);
		return true;
	}

	/**
	 * 计算支出预算中支出资金来源可供支配的收入
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @return
	 * @throws Exception
	 */
	private void fCalc_Div_FundSource_Inc(String setYear, String sDivCode,
			int iBatchNo, int iDataType, DataSet dsPfs, ArrayList lstSql)
			throws Exception {

		String sSql = "SELECT en_id,Div_Code,Div_Name" + " FROM vw_fb_division"
				+ " WHERE set_Year=" + setYear + " AND is_leaf=1"
				+ " and en_id in ( select en_id from fb_u_Div_FundSource_Inc"
				+ "  where set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + ")";
		if (!"".equals(sDivCode) && sDivCode != null)
			sSql = sSql + " AND Div_Code='" + sDivCode + "'";
		DataSet dsDiv = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsDiv);
		dsDiv.beforeFirst();
		while (dsDiv.next()) {
			incUpdate(setYear, iBatchNo, iDataType, dsPfs, dsDiv, lstSql);
		}

		sSql = "SELECT en_id,Div_Code,Div_Name"
				+ " FROM vw_fb_division"
				+ " WHERE set_Year="
				+ setYear
				+ " AND is_leaf=1"
				+ " and en_id not in ( select en_id from fb_u_Div_FundSource_Inc"
				+ "  where set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + ")";
		if (!"".equals(sDivCode) && sDivCode != null)
			sSql = sSql + " AND Div_Code='" + sDivCode + "'";
		DBSqlExec.getDataSet(sSql, dsDiv);
		dsDiv.beforeFirst();
		while (dsDiv.next()) {
			incInsert(setYear, iBatchNo, iDataType, dsPfs, dsDiv, lstSql);
		}
	}

	private double getValue(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv) throws Exception {
		if ("".equals(dsPfs.fieldByName("IncCol_EName").getString())
				|| dsPfs.fieldByName("IncCol_EName").getString() == null)
			return 0;

		Object oIncColEname = dsPfs.fieldByName("IncCol_EName").getValue();
		if ("".equals(oIncColEname) || oIncColEname == null)
			return 0;
		String sSql = "SELECT SUM(isnull(" + oIncColEname.toString()
				+ ",0)) AS " + oIncColEname.toString()
				+ "  FROM fb_u_Div_Incoming_budget" + " WHERE set_Year="
				+ setYear + "   AND Batch_No=" + String.valueOf(iBatchNo)
				+ "   AND Data_Type=" + String.valueOf(iDataType)
				+ "   AND Div_Code='"
				+ dsDiv.fieldByName("Div_Code").getString() + "'";
		Object oFormulaDet = dsPfs.fieldByName("SQL_DET").getString();
		if ("".equals(oFormulaDet) || oFormulaDet == null)
			return 0;
		sSql = sSql + " AND (" + dsPfs.fieldByName("SQL_DET").getString() + ")";
		return DBSqlExec.getFloatValue(sSql);
	}

	/**
	 * 可支配收入表插入数据
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @param lstSql
	 * @throws Exception
	 */
	private void incInsert(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv, ArrayList lstSql) throws Exception {
		double fValue;
		String sInsert = "";
		String sValues = "";
		dsPfs.beforeFirst();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		while (dsPfs.next()) {
			fValue = getValue(setYear, iBatchNo, iDataType, dsPfs, dsDiv);
			if ("".equals(sInsert)) {
				sInsert = "INSERT INTO fb_u_Div_FundSource_Inc(en_id,Div_Code,"
						+ "set_year,Batch_No,Data_Type,Data_Src,Data_Attr,Rg_Code,"
						+ dsPfs.fieldByName("PFS_EName").getString();
				sValues = " VALUES('" + dsDiv.fieldByName("en_id").getString()
						+ "','" + dsDiv.fieldByName("Div_Code").getString()
						+ "'," + setYear + "," + String.valueOf(iBatchNo) + ","
						+ String.valueOf(iDataType) + ",0,0,'" + sRgCode + "',"
						+ String.valueOf(fValue);
			} else {
				sInsert = sInsert + ","
						+ dsPfs.fieldByName("PFS_EName").getString();
				sValues = sValues + "," + String.valueOf(fValue);
			}
		}
		String sSql = sInsert + ")" + sValues + ")";
		lstSql.add(sSql);
	}

	/**
	 * 更新可支配收入表数据
	 * 
	 * @param setYear
	 * @param iBatchNo
	 * @param iDataType
	 * @param dsPfs
	 * @param dsDiv
	 * @param lstSql
	 * @throws Exception
	 */
	private void incUpdate(String setYear, int iBatchNo, int iDataType,
			DataSet dsPfs, DataSet dsDiv, ArrayList lstSql) throws Exception {
		double fValue;
		String sUpdate = "";

		dsPfs.beforeFirst();
		while (dsPfs.next()) {
			fValue = getValue(setYear, iBatchNo, iDataType, dsPfs, dsDiv);
			if ("".equals(sUpdate))
				sUpdate = "UPDATE fb_u_Div_FundSource_Inc SET "
						+ dsPfs.fieldByName("PFS_EName").getString() + "="
						+ String.valueOf(fValue);
			else
				sUpdate = sUpdate + ","
						+ dsPfs.fieldByName("PFS_EName").getString() + "="
						+ String.valueOf(fValue);
		}
		String sSql = sUpdate + " WHERE set_Year=" + setYear + " AND Batch_No="
				+ String.valueOf(iBatchNo) + " AND Data_Type="
				+ String.valueOf(iDataType) + " AND Div_Code='"
				+ dsDiv.fieldByName("Div_Code").getString() + "'";
		lstSql.add(sSql);
	}

	/**
	 * 作用，保存资金来源对应收入
	 * 
	 * @param setYear年份
	 * @param sDivCode
	 * @param iBatchNo
	 * @param iDataType
	 * @param sPFSCode
	 * @param dsPfsToIncitem,资金来源对应收入项目DataSet
	 * @throws Exception
	 */
	public DataSet savePfsToItem(String setYear, String sDivCode,
			String sPFSCode, DataSet dsPayOutFS, DataSet dsPfsToIncitem)
			throws Exception {
		// 公共方法数据接口
		IPubInterface iPubInterface = PubInterfaceStub.getServerMethod();
		dsPayOutFS.edit();
		dsPayOutFS.setName("fb_iae_payout_fundsource");
		dsPayOutFS.post();

		// 判断原对应关系是否是一对一关系，收入栏目从该资金来源取数
		DataSet dsPfsToIncitemOld = getIncWithPfsCode(sPFSCode, setYear);
		if (dsPfsToIncitemOld != null && !dsPfsToIncitemOld.isEmpty()
				&& dsPfsToIncitemOld.getRecordCount() == 1) {
			dsPfsToIncitemOld.beforeFirst();
			dsPfsToIncitemOld.next();
			String incTypeCode = dsPfsToIncitemOld.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			int oldIsInc = DBSqlExec
					.getIntValue("select is_inc from fb_iae_inctype where inctype_code ='"
							+ incTypeCode + "' and set_year=" + setYear);
			if (oldIsInc == 2) {
				// 判断是否包含此对应关系
				if (dsPfsToIncitem.locate(IIncType.INCTYPE_CODE, incTypeCode)) {
					// 判断对应关系数是否大于1，如大于1，更改收入栏目is_inc值(从2改为0)
					if (dsPfsToIncitem.getRecordCount() != 1) {
						String sSql = "update fb_iae_inctype set is_inc =0 where inctype_code ='"
								+ incTypeCode + "' and set_year=" + setYear;
						QueryStub.getQueryTool().executeUpdate(sSql);
					}
				} else {// 不包含原对应关系
					// 更is_inc值(从2改为0)
					String sSql = "update fb_iae_inctype set is_inc =0 where inctype_code ='"
							+ incTypeCode + "' and set_year=" + setYear;
					QueryStub.getQueryTool().executeUpdate(sSql);
				}
			}
		}

		// 删除资金来源对收入项目对应关系
		String sSql = "delete from fb_iae_pfs_to_incitem where pfs_code ='"
				+ sPFSCode + "' and set_year =" + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
		// 保存对应关系
		dsPfsToIncitem.post();
		// 重新计算
		int iBatchNo = 0;
		calc_Div_FundSource_Inc(setYear, sDivCode, iBatchNo, -1, sPFSCode);
		return dsPfsToIncitem;

	}

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
			String sSubType) throws Exception {
		// 更改当前科目子科目类型
		String sSql = "update ele_budget_subject_item set SubItem_Type ="
				+ sSubType + " where chr_id = '" + sBsiId + "' and set_year = "
				+ setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
	}

	/**
	 * 保存存收入预算科目对收费项目
	 * 
	 * @param setYear
	 * @param sSelectNodeId
	 * @param sIN_BS_ID
	 * @param sAcctCodeInc
	 * @param sSubType
	 * @param iDataSrc
	 * @throws Exception
	 */
	public void saveIncAcctToIncItem(String setYear, ArrayList sSelectNodeId,
			ArrayList sSelectNodeCode, String sIN_BS_ID, String sAcctCodeInc,
			String sSubType, int iDataSrc) throws Exception {
		String sSql = "";
		if (iDataSrc == 1) {
			sSql = " update ele_budget_subject_income set subItem_type="
					+ sSubType + " where chr_id = '" + sIN_BS_ID
					+ "' and set_year = " + setYear;
		} else if (iDataSrc == 2) {
			sSql = " update fb_e_acctitem_inc set subItem_type=" + sSubType
					+ " where chr_id = '" + sIN_BS_ID + "' and set_year = "
					+ setYear;
		}
		QueryStub.getQueryTool().executeUpdate(sSql);

		// 判断是否有取消经济科目与收费项目对应关系
		sSql = "select INCITEM_CODE from fb_Iae_IncAcct_To_IncItem where IN_BS_ID = '"
				+ sIN_BS_ID + "' and set_Year = " + setYear;
		DataSet dsIncAcctToIncItem = DataSet.create();
		dsIncAcctToIncItem.setSqlClause(sSql);
		dsIncAcctToIncItem.open();
		String incItemCode;
		List lstOptSql = null;
		dsIncAcctToIncItem.beforeFirst();
		while (dsIncAcctToIncItem.next()) {
			incItemCode = dsIncAcctToIncItem.fieldByName("INCITEM_CODE")
					.getString();
			if (!sSelectNodeCode.contains(incItemCode)) {
				if (lstOptSql == null) {
					lstOptSql = new ArrayList();
				}
				sSql = "delete fb_u_div_incoming where ACCT_CODE_INC=''"
						+ sAcctCodeInc + "'' and TOLL_CODE=''" + incItemCode
						+ "'' and set_year = " + setYear;
				lstOptSql.add(sSql);
			}
		}

		// 删除对应关系
		sSql = "delete from fb_Iae_IncAcct_To_IncItem  where IN_BS_ID = '"
				+ sIN_BS_ID + "' and set_Year = " + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);

		// 保存对应关系
		ArrayList listSql = new ArrayList();
		for (int i = 0; i < sSelectNodeId.size(); i++) {
			String sCode = sSelectNodeCode.get(i).toString();
			sSql = "insert into fb_Iae_IncAcct_To_IncItem(IN_BS_ID, ACCT_CODE_INC, INCITEM_CODE, set_year,toll_id)"
					+ " values('"
					+ sIN_BS_ID
					+ "','"
					+ sAcctCodeInc
					+ "','"
					+ sCode
					+ "',"
					+ setYear
					+ ",'"
					+ sSelectNodeId.get(i).toString() + "')";
			listSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(listSql);

		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(UpdateStruPub._MOD
				+ "收入预算科目对收费项-预算科目编码" + sAcctCodeInc, lstOptSql);
	}

	/**
	 * 判断编码是否存在,且不是不叶子节点,公共函数
	 * 
	 * @param sCode
	 * @param sFieldName
	 * @param sTableName
	 * @param setYear
	 * @return true编码存在且不是叶节点
	 * @throws Exception
	 */
	private InfoPackage judgeParExist(String sParLvlId, String sFieldName,
			String sTableName, String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sSql = "select * from " + sTableName + " where " + sFieldName
				+ " ='" + sParLvlId + "' and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		if (ds.isEmpty()) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("父对象不存在,请重新填写!");
			return infoPackage;
		}
		ds.beforeFirst();
		ds.next();
		if (ds.fieldByName("end_flag").getInteger() == 1) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("填写的父对象是叶子节点，不可修改!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 获得表中某个字段,存于数组中
	 * 
	 * @param sFieldName
	 * @param sTableName
	 * @param sFilter
	 * @return
	 * @throws Exception
	 */
	private String[] getArrayID(String sFieldName, String sTableName,
			String sFilter) throws Exception {
		DataSet ds = DataSet.create();
		String sSql = "select " + sFieldName + " from " + sTableName
				+ " where " + sFilter;
		DBSqlExec.getDataSet(sSql, ds);
		if (ds.isEmpty())
			return null;
		String sID[] = new String[ds.getRecordCount()];
		ds.beforeFirst();
		int i = 0;
		while (ds.next()) {
			sID[i] = ds.fieldByName(sFieldName).getString();
			i++;
		}
		return sID;
	}

	/**
	 * 得到收费项目cha_id,根据收入预算科目编码
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getIncItemWithCode(String sIN_BS_ID, String setYear)
			throws Exception {
		String sFilter = " IN_BS_ID='" + sIN_BS_ID + "' and set_year="
				+ setYear;
		return getArrayID("toll_id", "fb_Iae_IncAcct_To_IncItem", sFilter);
	}

	/**
	 * 收支科目挂接明细， 得到支出子项目cha_id,根据支出经济科目编码
	 * 
	 * @param sIncTypeCode
	 * @param setYear
	 * @throws Exception
	 */

	public InfoPackage judgeJjEnableModify(String sBsiId, String sName,
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();

		// 判断是否已使用
		String sFilter = " bsi_id='" + sBsiId + "' and set_year=" + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("[" + sName + "]已经被使用,不能修改!");
			return infoPackage;
		}
		// 判断是否定义了公式
		String sSql = "  select * from vw_fb_Iae_PayOut_Formula_Count where bsi_id='"
				+ sBsiId + "' and set_year =" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		ds.beforeFirst();
		while (ds.next()) {
			String sFormulaId = ds.fieldByName("formula_id").getString();
			if (!PubUntSys.isDefaultFormula(sFormulaId)) {
				infoPackage.setSuccess(false);
				infoPackage.setsMessage("[" + sName + "]已经定义了公式,不能修改!");
				return infoPackage;
			}
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public InfoPackage judgeIncEnableModify(String sActtCode, String sName,
			String setYear) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// 判断是否已使用
		String sFilter = " ACCT_CODE_INC='" + sActtCode + "' and set_year="
				+ setYear;
		if (DBSqlExec.getRecordCount("fb_u_Div_Incoming", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("[" + sName + "]已经被使用,不能修改!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 判断支出项目类别是否已被使用,用于修改和删除时判断
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayOutTypeUse(String sPayoutKindCode, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		// 判断是否被使用
		String sFilter = "payout_kind_code='" + sPayoutKindCode
				+ "' and set_Year = " + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", sFilter) > 0) {
			infoPackage.setsMessage("支出项目类别已被支出预算表使用");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (DBSqlExec.getRecordCount("fb_u_Payout_Budget_Rae", sFilter) > 0) {
			infoPackage.setsMessage("支出项目类别已被支出控制数使用");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind_to_div", sFilter) > 0) {
			infoPackage.setsMessage("支出项目类别已设置与单位的对应关系");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 支出项目类别，得到经济科目id,根据支出项目类别编码
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public String[] getJjWithPayoutKindCode(String sPayoutKindCode,
			String setYear) throws Exception {
		String sFilter = " PAYOUT_KIND_CODE='" + sPayoutKindCode
				+ "' and set_year=" + setYear;
		return getArrayID("BSI_ID", "fb_iae_payout_kind_to_jj", sFilter);
	}

	/**
	 * 支出资金来源对收入，判断英文名称是否已被使用
	 * 
	 * @param sEname
	 * @param setYear
	 * @return true:没有被使用,false已被使用
	 * @throws Exception
	 */
	public boolean judgePfsEnameUse(String sEname, String setYear)
			throws Exception {
		String aFilter = sEname + ">0 and set_year = " + setYear;
		if (DBSqlExec.getRecordCount("vw_fb_u_Payout_Budget", aFilter) > 0)
			return false;
		else
			return true;
	}

	/**
	 * 支出资金来源对收入，根据支出资金来源编码
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getIncWithPfsCode(String sPfsCode, String setYear)
			throws Exception {
		String sSql = "select * from  fb_iae_pfs_to_incitem where pfs_code='"
				+ sPfsCode + "' and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 判断能否删除支出资金来源
	 * 
	 * @param sEName英文名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePfsEnableDel(String sEName, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String Epq = "%{" + sEName + "}%";
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource",
				"formula_Det like '" + Epq + "' and set_Year=" + setYear) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("该支出资金来源已作为其他栏目计算公式的内容,请先删除其他栏目!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

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
			String sCode, boolean bModidy) throws Exception {
		String sFilterA;
		if (!bModidy) { // 增加
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// 修改
			sFilterA = "lvl_id ='" + sLvlId + "' and payout_kind_code <> '"
					+ sCode + "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind", sFilterA) > 0) {
			return false;
		}
		return true;

	}

	/**
	 * 支出项目类别,判断编码是否存在,且不是不叶子结，如果是叶子节点
	 * 
	 * @param sParLvlId
	 * @param setYear
	 * @throws Exception
	 * @return：true编码存在且不是叶节点
	 */
	public InfoPackage judgePayOutTypeParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_payout_kind", setYear);
	}

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
			String setYear, String sCode, boolean bModidy) throws Exception {
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and PAYOUT_KIND_NAME = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and PAYOUT_KIND_NAME = '"
					+ sName + "' and set_year=" + setYear;
		}
		if (bModidy) {// 是否是修改,判断条件加上不等于自己
			sFilter = sFilter + " and PAYOUT_KIND_CODE != '" + sCode + "'";
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind", sFilter) > 0) {
			return false;
		}
		return true;
	}

	public InfoPackage judgePfsParExist(String sParLvlId, String setYear)
			throws Exception {
		return judgeParExist(sParLvlId, "lvl_id", "fb_iae_payout_fundsource",
				setYear);
	}

	public InfoPackage judgePfsCodeRepeat(String sLvlId, String setYear,
			String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilterA;
		if (!bModidy) { // 增加
			sFilterA = "lvl_id ='" + sLvlId + "' and set_year=" + setYear;
		} else {// 修改
			sFilterA = "lvl_id ='" + sLvlId + "' and PFS_CODE <> '" + sCode
					+ "' and set_year=" + setYear;
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilterA) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("编码已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public InfoPackage judgePfsNameRepeat(String sName, String sPar,
			String setYear, String sCode, boolean bModidy) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sFilter;
		if ("".equals(sPar) || sPar == null) {
			sFilter = " PAR_ID is null and PFS_Name = '" + sName
					+ "' and set_year=" + setYear;
		} else {
			sFilter = " PAR_ID = '" + sPar + "' and PFS_Name = '" + sName
					+ "' and set_year=" + setYear;
		}
		if (bModidy) {// 是否是修改,判断条件加上不等于自己
			sFilter = sFilter + " and PFS_CODE != '" + sCode + "'";
		}

		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilter) > 0) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("支出资金来源名称已经被使用!");
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public boolean judgePfsStdTypeCode(String sCode, String setYear,
			boolean bModify) throws Exception {
		String sFilter = " std_type_code='001'" + " and set_year=" + setYear;
		if (bModify) {
			sFilter = sFilter + " and pfs_code<>'" + sCode + "'";
		}
		if (DBSqlExec.getRecordCount("fb_iae_payout_fundsource", sFilter) > 0)
			return false;
		return true;
	}

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
			String setYear, String sPfs) throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		boolean bUseFlag = false;
		String sMsg = "";
		String sVaule;
		String sName;
		for (int i = 0; i < lstId.size(); i++) {
			String sIncTypeCode = lstId.get(i).toString();
			String sSql = "select pfs_code from fb_iae_pfs_to_incitem  where inctype_code = '"
					+ sIncTypeCode + "' and set_year=" + setYear;
			if (sPfs != null && !"".equals(sPfs))
				sSql = sSql + " and  pfs_code<>'" + sPfs + "'";
			sVaule = DBSqlExec.getStringValue(sSql);
			if (sVaule != null) {
				sSql = "select pfs_name from fb_iae_payout_fundsource  where pfs_code = '"
						+ sVaule + "' and set_year=" + setYear;
				sName = DBSqlExec.getStringValue(sSql);
				sMsg = sMsg + "[" + lstName.get(i).toString() + "]在[" + sName
						+ "]中已经被使用！\n";
				bUseFlag = true;
			}
		}

		if (bUseFlag) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage(sMsg);
		} else {
			infoPackage.setSuccess(true);
		}
		return infoPackage;
	}

	/**
	 * 保存支出资金来源
	 */
	public void savePayOutFS(DataSet dsPayOutFS, String sPfsCode,
			String sParPfsCode, String setYear, List lstAcctCode,
			boolean bAddFirstNode) throws Exception {
		// 得到optMenu值
		String optMenu = UpdateStruPub.getOptMenu(dsPayOutFS, sPfsCode,
				IPayOutFS.PFS_CODE, IPayOutFS.PFS_NAME, IPayOutFS.PFS_ROOT);

		dsPayOutFS.post();
		// 资金来源对收入项目表
		if (bAddFirstNode) {
			String sSql = "Update fb_Iae_Pfs_To_IncItem  Set Pfs_Code='"
					+ sPfsCode + "' Where set_Year=" + setYear
					+ " and Pfs_Code ='" + sParPfsCode + "'";
			QueryStub.getQueryTool().executeUpdate(sSql);
		}

		ArrayList lstSql = new ArrayList();
		String sSql = null;
		// 删除原对应关系
		if (!"".equals(sParPfsCode) && sParPfsCode != null) {
			sSql = "delete fb_iae_pfs_to_acctcode where pfs_code ='"
					+ sParPfsCode + "' and set_year=" + setYear;
		}
		lstSql.add(sSql);
		// 新对应关系
		if (lstAcctCode != null) {
			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			for (int i = 0; i < lstAcctCode.size(); i++) {
				sSql = "insert into fb_iae_pfs_to_acctcode(pfs_code,acct_code,set_year,rg_code) values('"
						+ sPfsCode
						+ "','"
						+ lstAcctCode.get(i).toString()
						+ "'," + setYear + ",'" + sRgCode + "')";
				lstSql.add(sSql);
			}
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv(optMenu, "");

	}

	/**
	 * 得到资金来源对功能科目
	 * 
	 * @param setYear
	 * @param sPfsCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsToAcct(String setYear, String sPfsCode)
			throws Exception {
		String sSql = "select acct_code "
				+ " from fb_iae_pfs_to_acctcode where set_year=? and  pfs_code=? ";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { setYear, sPfsCode });
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * 判断是否已设置公式和公式与单位对应关系,
	 * 
	 * @param payOutKindId支出项目类别内码
	 * @return
	 * @throws Exception
	 */
	public InfoPackage judgePayoutTypeFormulaUse(String payOutKindCode)
			throws Exception {
		InfoPackage info = new InfoPackage();
		String filter = " (PAYOUT_ITEM_CODE='" + payOutKindCode
				+ "' or PAR_ID = '" + payOutKindCode + "')"
				+ " and set_year = "
				+ SessionUtil.getUserInfoContext().getSetYear();

		if (DBSqlExec.getRecordCount("fb_iae_payout_formula_to_div", filter) > 0) {
			info.setSuccess(false);
			info.setsMessage("已设置公式及公式与单位的对应关系");
			return info;
		}

		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula", filter) > 0) {
			info.setSuccess(false);
			info.setsMessage("已设置公式");
			return info;
		}
		info.setSuccess(true);
		return info;
	}

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
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String filter = " PAYOUT_KIND_CODE='" + sPayoutKindCode
				+ "' and Acct_code_jj ='" + acctJJCode + "' and set_year="
				+ setYear;
		if (DBSqlExec.getRecordCount("fb_iae_payout_kind_to_jj", filter) > 0) {
			return true;
		} else {
			return false;
		}
	}

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
			String acctJjCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		ArrayList lstSql = new ArrayList();
		// 支出项目公式值表
		// String sSql = " update fb_u_Div_PayOut_Formula_Value set par_id='"
		// + sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
		// + "' and Payout_Item_code='" + acctJjCode + "'"
		// + " and payout_item_kind=1 and set_Year = " + setYear;
		// lstSql.add(sSql);

		// 支出项目公式对单位表
		String sSql = " update  fb_iae_payout_formula_to_div set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ "  and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// 支出项目单元格公式相关引用元素表
		sSql = " update  fb_Iae_Payout_Cell_Refelet set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ "  and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);

		// 支出项目单元格公式表
		lstSql.add(sSql);
		sSql = " update  fb_Iae_PayOut_Cell_Formula set par_id='"
				+ sNewPayoutKindCode + "' where par_id='" + sOldPayoutKindCode
				+ "' and Payout_Item_code='" + acctJjCode + "'"
				+ " and payout_item_kind=1 and set_Year = " + setYear;
		lstSql.add(sSql);
		// 支出项目类别对经济科目
		sSql = "update  fb_Iae_PayOut_Kind_To_JJ set Payout_kind_code='"
				+ sNewPayoutKindCode + "',Payout_kind_name='"
				+ sNewPayoutKindName + "' where PayOut_Kind_Code='"
				+ sOldPayoutKindCode + "' and ACCT_CODE_JJ = '" + acctJjCode
				+ "' and set_Year = " + setYear;
		lstSql.add(sSql);
		QueryStub.getQueryTool().executeBatch(lstSql);

		// 获得与支出项目类别经济科目相关的业务数据表
		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		List lstOptSql = new ArrayList();
		while (dsTable.next()) {
			String tableName = dsTable.fieldByName("name").getString();
			sSql = "update  " + tableName + " set Payout_kind_code=''"
					+ sNewPayoutKindCode + "'',Payout_kind_name=''"
					+ sNewPayoutKindName + "'' where PayOut_Kind_Code=''"
					+ sOldPayoutKindCode + "'' and ACCT_CODE_JJ = ''"
					+ acctJjCode + "'' and set_Year = " + setYear;
			lstOptSql.add(sSql);
			Set setCol = QueryStub.getQueryTool().getColumnInfo(tableName);
			if (setCol.contains("data_src")) {
				sSql = "update  " + tableName + " set PROJECT_NAME=''"
						+ sNewPayoutKindName + "'' where PayOut_Kind_Code=''"
						+ sOldPayoutKindCode + "'' and ACCT_CODE_JJ = ''"
						+ acctJjCode + "'' and data_src = 50 and set_Year = "
						+ setYear;
				lstOptSql.add(sSql);
			}
		}
		// 维护结构变动情况
		UpdateStruPub.exeStruSqlToAllDiv("改变支出项目类别-经济科目编码" + acctJjCode,
				lstOptSql);
	}

	/**
	 * 获得与支出项目类别经济科目相关的业务数据表
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private DataSet getPayOutKindAcctJjTable(String setYear) throws Exception {
		// 与资金来源有关的表
		String sSql = "Select Name From fb_s_PubCode Where set_Year=" + setYear
				+ " And TypeID = 'PAYOUTKINDTABLE' Order By Lvl_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 判断支出项目类别中经济科目是否使用
	 * 
	 * @param payOutKindId支出项目类别内码
	 * @param acctJjCode经济科目编码
	 * @return使用情况信息，未使用返回空值
	 * @throws Exception
	 */
	public String judgePayoutTypeAcctJjUse(String payOutKindCode,
			String acctJjCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sFilter = "payout_kind_code='" + payOutKindCode
				+ "' and acct_code_jj='" + acctJjCode + "' and set_Year = "
				+ setYear;

		DataSet dsTable = this.getPayOutKindAcctJjTable(setYear);
		dsTable.beforeFirst();
		while (dsTable.next()) {
			String tableName = dsTable.fieldByName("name").getString();
			if (DBSqlExec.getRecordCount(tableName, sFilter) > 0)
				return this.getTableChinaName(tableName) + "已有数据";
		}

		sFilter = "  PAYOUT_ITEM_CODE='" + acctJjCode + "' and PAR_ID = '"
				+ payOutKindCode + "' and  set_year = " + setYear;

		if (DBSqlExec.getRecordCount("fb_iae_payout_formula_to_div", sFilter) > 0) {
			return "已设置公式及公式与单位的对应关系";
		}

		if (DBSqlExec.getRecordCount("fb_Iae_PayOut_Cell_Formula", sFilter) > 0) {
			return "已设置公式";
		}
		return "";
	}

	/**
	 * 得到支出项目类别-专项支出记录数
	 * 
	 * @param payouKindCode
	 * @return
	 * @throws Exception
	 */
	public String getPayoutKindStandPrj(String payouKindCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sFilter = IPayOutType.STD_TYPE_CODE + "='"
				+ IPayOutType.PAYOUTKINDSTAND_PRJ + "'";
		if (!Common.isNullStr(payouKindCode)) {
			sFilter = sFilter + " and " + IPayOutType.PAYOUT_KIND_CODE + "<>'"
					+ payouKindCode + "'";
		}

		sFilter = sFilter + " and set_year=" + setYear;
		return DBSqlExec
				.getStringValue(" select PAYOUT_KIND_NAME from fb_iae_payout_kind where "
						+ sFilter);
	}

	/**
	 * 收入对支出资金来源，得到支出资金来源编码,根据收入项目编码
	 * 
	 * @param sPayoutKindCode
	 * @param setYear
	 * @throws Exception
	 */
	public DataSet getPfsToIncCode(String sIncTypeCode, String setYear)
			throws Exception {
		String sSql = "select pfs_code,INCTYPE_CODE"
				+ " from  fb_iae_pfs_to_incitem "
				+ " where inctype_code='"
				+ sIncTypeCode
				+ "' and pfs_code in ( select pfs_code from fb_iae_payout_fundsource where data_source=0 and set_year="
				+ setYear + ") and set_year=" + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 获得支出资金来源(不包含数据来源为计算的记录）
	 * 
	 * @param budgetYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPayOutFSNotCalcTre(int setYear) throws Exception {
		String payOutFSSQL = "select lvl_id||' '||PFS_NAME as name ,a.* from fb_iae_payout_fundsource a where end_flag = 1 and data_source=0 and set_year=?"
				+ " union all"
				+ " select lvl_id||' '||PFS_NAME as name ,a.* from fb_iae_payout_fundsource A  where a.end_flag <> 1 and a.set_year=?"
				+ " and exists (select 1 from fb_iae_payout_fundsource b where b.Lvl_Id like a.lvl_id||'%' and b.end_flag = 1 and b.set_year=?)";
		DataSet ds = DataSet.create();
		ds.setQueryParams(new Object[] { new Integer(setYear),
				new Integer(setYear), new Integer(setYear) });
		ds.setSqlClause(payOutFSSQL);
		ds.open();
		return ds;
	}

	/**
	 * 得到收入项目类别is_inc字段值
	 * 
	 * @param incTypeCode
	 * @return
	 * @throws Exception
	 */
	public int getIncTypeIsInc(String incTypeCode) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return DBSqlExec
				.getIntValue("select is_inc from fb_iae_inctype where inctype_code ='"
						+ incTypeCode + "' and set_year=" + setYear);
	}

	public DataSet judgeIncColYLBLExist(String setYear, String sCode)
			throws Exception {
		String sSql = "select * from fb_iae_inccolumn where INCCOL_CODE<>'"
				+ sCode + "' and n2=1 and set_year=" + setYear;
		return DBSqlExec.getDataSet(sSql);
	}

	/**
	 * 得到支出资金来源总计记录
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getPfsHj(String setYear) throws Exception {
		String sSql = " select * from fb_iae_payout_fundsource"
				+ " where std_type_code='001'" + " and set_year=" + setYear;
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}
}