/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ToolsEx;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;

import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * <p>
 * Title:收支结构变更，保存更新记录即更新语句单元
 * </p>
 * <p>
 * Description:收支结构变更，保存更新记录即更新语句单元
 * </p>
 * <p>

 * @version 6.2.40
 */
public class UpdateStruPub {

	static String _ADD = "增加";

	static String _DEL = "删除";

	static String _MOD = "修改";

	private static String OPT_TYPE_IAE = "002";

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @throws Exception
	 */
	public static void exeStruSqlToAllDiv(String optMenu, String optSql)
			throws Exception {
//		// 判断是否需要将变动情况保存到结构变动情况表
//		if (!WfStub.getServerMethod().isControlVer()) {
//			return;
//		}
		List lstSql = getStruSqlToAllDivSql(optMenu, optSql);
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @throws Exception
	 */
	public static void exeStruSqlToAllDiv(String optMenu, List lstOptSql)
			throws Exception {
		// 判断是否需要将变动情况保存到结构变动情况表
//	s
		List lstSql = new ArrayList();
		if (lstOptSql == null) {
			lstSql = getStruSqlToAllDivSql(optMenu, "");
		} else {

			int size = lstOptSql.size();
			for (int i = 0; i < size; i++) {
				lstSql.addAll(getStruSqlToAllDivSql(optMenu, lstOptSql.get(i)
						.toString()));
			}
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	private static List getStruSqlToAllDivSql(String optMenu, String optSql)
			throws Exception {
		List lstSql = new ArrayList();
		String optId = UUID.randomUUID().toString();
		lstSql.add(getStruOptInfo(optId, optMenu, optSql));
		lstSql.add(getStruOptToAllDiv(optId));
		return lstSql;
	}

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @param lstDiv
	 * @throws Exception
	 */
	public static void exeStruSqlToDiv(String optMenu, String optSql,
			DataSet dsDiv) throws Exception {
		// 判断是否需要将变动情况保存到结构变动情况表
//		if (!WfStub.getServerMethod().isControlVer()) {
//			return;
//		}
		List lstSql = new ArrayList();
		String optId = UUID.randomUUID().toString();
		lstSql.add(getStruOptInfo(optId, optMenu, optSql));
		List lstStruOptToDiv = getStruOptToDiv(optId, dsDiv);
		if (lstStruOptToDiv != null) {
			lstSql.addAll(lstStruOptToDiv);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @param lstDiv
	 * @throws Exception
	 */
	public static void exeStruSqlToDiv(String optMenu, String optSql,
			List lstDiv) throws Exception {
		// 判断是否需要将变动情况保存到结构变动情况表
//		if (!WfStub.getServerMethod().isControlVer()) {
//			return;
//		}
		List lstSql = new ArrayList();
		String optId = UUID.randomUUID().toString();
		lstSql.add(getStruOptInfo(optId, optMenu, optSql));
		lstSql.addAll(getStruOptToDiv(optId, lstDiv));
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @param lstDiv
	 * @throws Exception
	 */
	public static void exeStruSqlToDiv(String optMenu, List lstOptSql,
			String divCode, List lstOptId) throws Exception {
		List lstSql = null;
		int i = 0;
		for (Iterator it = lstOptSql.iterator(); it.hasNext();) {
			lstSql = new ArrayList();
			String optSql = (String) it.next();
			String optId = (String) lstOptId.get(i);
			lstSql.add(getStruOptInfo(optId, optMenu, optSql));
			lstSql.add(getStruOptToDiv(optId, divCode));
			QueryStub.getQueryTool().executeBatch(lstSql);
			i++;
		}

	}

	/**
	 * 维护结构变动情况
	 * 
	 * @param optMenu
	 * @param optSql
	 * @param lstDiv
	 * @throws Exception
	 */
	public static String exeStruSqlToDiv(String optMenu, String optSql,
			String divCode) throws Exception {
		// 判断是否需要将变动情况保存到结构变动情况表
//		if (!WfStub.getServerMethod().isControlVer()) {
//			return "";
//		}
		List lstSql = new ArrayList();
		String optId = UUID.randomUUID().toString();
		lstSql.add(getStruOptInfo(optId, optMenu, optSql));
		lstSql.add(getStruOptToDiv(optId, divCode));
		QueryStub.getQueryTool().executeBatch(lstSql);
		return optId;
	}

	/**
	 * 结构变动关联单位表，增加记录
	 * 
	 * @param optId
	 * @param divCode
	 * @throws Exception
	 */
	public static void exeStruOptToDiv(String optId, String divCode)
			throws Exception {
		// 判断是否需要将变动情况保存到结构变动情况表
//		s
		String sSql = getStruOptToDiv(optId, divCode);
		// 处理来回多次取消一个单位时，重复插入记录，主键重复问题
		String sSqlDel = "delete from FB_S_STRUOPT_TO_DIV where opt_id='"
				+ optId + "' and " + " DIV_CODE='" + divCode
				+ "' and SET_YEAR = "
				+ SessionUtil.getUserInfoContext().getSetYear();
		List list = new ArrayList();
		list.add(sSqlDel);
		list.add(sSql);
		QueryStub.getQueryTool().executeBatch(list);
	}

	/**
	 * 结构变动情况表
	 * 
	 * @param optId
	 * @param optMenu
	 * @param optSql
	 * @return
	 * @throws Exception
	 */
	private static String getStruOptInfo(String optId, String optMenu,
			String optSql) throws Exception {
		if (optSql == null)
			optSql = "";
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String userID = SessionUtil.getUserInfoContext().getUserID();
		String maxValue = DBSqlExec.getMaxValueFromField("fb_s_stru_optinfo",
				"sql_xh", "set_year =" + setYear);
		maxValue = String.valueOf(Integer.parseInt(maxValue) + 1);
		return "insert into fb_s_stru_optinfo"
				+ "(opt_id, opt_type,  ver_flag, opt_menu, opt_sql, set_year,sql_xh,OPT_USER,OPT_DATE)"
				+ " values " + "('" + optId + "','" + OPT_TYPE_IAE + "' ,1, '"
				+ optMenu + "', '" + optSql + "', " + setYear + "," + maxValue
				+ ",'" + userID + "','" + ToolsEx.getServerDate() + "')";
	}

	/**
	 * 结构变动关联所有单位
	 * 
	 * @param OptId
	 * @return
	 */
	private static String getStruOptToAllDiv(String OptId) {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return "insert into FB_S_STRUOPT_TO_DIV"
				+ "(opt_id, DIV_CODE,  SET_YEAR)" + " values " + "('" + OptId
				+ "','-1' ," + setYear + ")";
	}

	/**
	 * 结构变动关联单位表
	 * 
	 * @param OptId
	 * @param lstDiv
	 * @return
	 * @throws Exception
	 */
	private static List getStruOptToDiv(String OptId, DataSet dsDiv)
			throws Exception {
		if (dsDiv.isEmpty())
			return null;
		List lstSql = null;
		dsDiv.beforeFirst();
		while (dsDiv.next()) {
			if (lstSql == null) {
				lstSql = new ArrayList();
			}
			lstSql.add(getStruOptToDiv(OptId, dsDiv.fieldByName(
					IPubInterface.DIV_CODE).getString()));

		}
		return lstSql;
	}

	/**
	 * 结构变动关联单位表
	 * 
	 * @param OptId
	 * @param lstDiv
	 * @return
	 * @throws Exception
	 */
	private static List getStruOptToDiv(String OptId, List lstDiv)
			throws Exception {
		if (lstDiv == null)
			return null;
		List lstSql = null;
		int size = lstDiv.size();
		for (int i = 0; i < size; i++) {
			if (lstSql == null) {
				lstSql = new ArrayList();
			}
			lstSql.add(getStruOptToDiv(OptId, lstDiv.get(i).toString()));

		}
		return lstSql;
	}

	/**
	 * 结构变动关联单位表
	 * 
	 * @param optId
	 * @param lstDiv
	 * @return
	 * @throws Exception
	 */
	private static String getStruOptToDiv(String optId, String divCode)
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return "insert into FB_S_STRUOPT_TO_DIV"
				+ "(opt_id, DIV_CODE,  SET_YEAR)" + " values " + "('" + optId
				+ "','" + divCode + "' ," + setYear + ")";

	}

	/**
	 * 得到optMenu字段值
	 * 
	 * @param ds
	 * @param sCode
	 * @param sCodeField
	 * @param sNameField
	 * @param modeName
	 * @return
	 * @throws Exception
	 */
	public static String getOptMenu(DataSet ds, String sCode,
			String sCodeField, String sNameField, String modeName)
			throws Exception {
		if (!ds.locate(sCodeField, sCode))
			return null;

		String optMenu = "";
		if (ds.getRecordState() == DataSet.FOR_UPDATE) {// 修改
			optMenu = UpdateStruPub._MOD + modeName + "-"
					+ ds.fieldByName(sNameField).getOldValue().toString();
		} else if (ds.getRecordState() == DataSet.FOR_APPEND) {// 增加
			optMenu = UpdateStruPub._ADD + modeName + "-"
					+ ds.fieldByName(sNameField).getString();
		} else {
			optMenu = UpdateStruPub._MOD + modeName + "-"
					+ ds.fieldByName(sNameField).getOldValue().toString();
		}
		return optMenu;
	}

	/**
	 * 得到收入栏目的OptSql
	 * 
	 * @param ds
	 * @param sCode
	 * @return
	 * @throws Exception
	 */
	public static String getIncColFormulaOptSql(DataSet ds, String sCode)
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		if (!ds.locate(IIncColumn.INCCOL_CODE, sCode))
			return null;
		if (ds.fieldByName(IIncColumn.DATA_SOURCE).getInteger() == 1) {
			String sFormula = ds.fieldByName(IIncColumn.FORMULA_DET)
					.getString();
			String sOldFormula = null;
			if (ds.fieldByName(IIncColumn.FORMULA_DET).getOldValue() != null) {
				sOldFormula = ds.fieldByName(IIncColumn.FORMULA_DET)
						.getOldValue().toString();
			}

			// 判断公式是否发生改变
			if (sFormula.equalsIgnoreCase(sOldFormula)) {
				return "";
			}
			sFormula = sFormula.replaceAll("\\{", "isnull(");
			sFormula = sFormula.replaceAll("\\}", ",0)");
			return " update fb_u_Div_Incoming set "
					+ ds.fieldByName(IIncColumn.INCCOL_ENAME).getString()
					+ " = " + sFormula + " where set_year=" + setYear;
		} else {
			return "";
		}
	}

	/**
	 * 将表中某字段值清为零
	 * 
	 * @param sTable
	 * @param ename
	 * @return
	 */
	public static String getUpdateToZeroSql(String sTable, String ename) {
		if (Common.isNullStr(sTable))
			return null;
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return " update " + sTable + " set " + ename + "=0 where set_year ="
				+ setYear;
	}

	/**
	 * 将表中某字段值清为零
	 * 
	 * @param lstTable
	 * @param ename
	 * @return
	 * @throws Exception
	 */
	public static List getUpdateToZeroSql(DataSet dsTable, String ename)
			throws Exception {
		if (dsTable.isEmpty())
			return null;
		dsTable.beforeFirst();

		List lstSql = new ArrayList();
		while (dsTable.next()) {
			lstSql.add(getUpdateToZeroSql(dsTable.fieldByName("name")
					.getString(), ename));
		}
		return lstSql;
	}

	/**
	 * 根据编码，删除业务数据语句
	 * 
	 * @param sTable
	 * @param sCode
	 * @param sFieldName
	 * @return
	 */
	public static String getClearDataSql(String sTable, String sCode,
			String sFieldName) {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return " delete " + sTable + " where " + sFieldName + "=''" + sCode
				+ "'' and set_year =" + setYear;
	}

	/**
	 * 根据编码，删除业务数据语句
	 * 
	 * @param sTable
	 * @param sCode
	 * @param sFieldName
	 * @return
	 * @throws Exception
	 */
	public static List getClearDataSql(DataSet dsTable, String sCode,
			String sFieldName) throws Exception {
		if (dsTable.isEmpty())
			return null;
		dsTable.beforeFirst();

		List lstSql = new ArrayList();
		while (dsTable.next()) {
			lstSql.add(getClearDataSql(dsTable.fieldByName("name").getString(),
					sCode, sFieldName));
		}
		return lstSql;
	}
}
