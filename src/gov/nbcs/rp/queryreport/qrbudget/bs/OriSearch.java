/**
 * @# OraSearch.java    <文件名>
 */
package gov.nbcs.rp.queryreport.qrbudget.bs;

import java.util.HashMap;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.dicinfo.datadict.bs.DataDictBO;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.DivObject;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import gov.nbcs.rp.sys.sysrefcol.ui.SysRefColI;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;


public class OriSearch {
	private ReportInfo reportInfo = null;// 报表信息

	private GeneralDAO dao = null;// 查询通道

	private DataDictBO dataDictBO;// 数据源服务

	public OriSearch(ReportInfo reportInfo, GeneralDAO dao) {
		this.reportInfo = reportInfo;
		this.dao = dao;

		dataDictBO = new DataDictBO();
		dataDictBO.setDao(this.dao);
	}

	/**
	 * 这里的List里有三个部分，第一部分是个String ：临时表名称，二为CREATE TABLE语句 三为LIST
	 * 几条INSERT语句，最后是DROP语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getReportExecuteSql(String sVerNo,
			String sBatchNoFilter, List lstDept, List lstDiv, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag,
			ConditionObj conditionObj) throws Exception {

		QrBudgetBO qrBudgetBO = new QrBudgetBO();
		qrBudgetBO.setDao(dao);

		// 取得SQLLINE中的数据并添加到查询体中
		List lstSqlLines = qrBudgetBO.getSqlLines(reportInfo.getReportID(),
				setYear, iLoginmode);

		return getReportExecuteSql_A(sVerNo, sBatchNoFilter, lstDept, lstDiv,
				setYear, iUserType, iLoginmode, iTypeFlag, lstSqlLines,
				conditionObj);
	}

	/**
	 * 根据组织lstSqlLines语名
	 * 
	 * 这里的List里有三个部分，第一部分是个String ：临时表名称，二为CREATE TABLE语句 三为LIST
	 * 几条INSERT语句，最后是DROP语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getReportExecuteSql_A(String sVerNo,
			String sBatchNoFilter, List lstDept, List lstDiv, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag, List lstSqlLines,
			ConditionObj conditionObj) throws Exception {
		if (reportInfo == null || dao == null)
			return null;
		boolean isNeedChangeTable = iUserType == QrBudget.USER_DW;// 是否要修改数据源

		// 先生成临时表名称
		String sTempTable = null;
		try {
			sTempTable = OriSearchObj.generateTempTableName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (sTempTable == null)
			return null;

		OriSearchObj oraSearchObj = new OriSearchObj(sTempTable);
		QrBudgetBO qrBudgetBO = new QrBudgetBO();
		qrBudgetBO.setDao(dao);

		if (lstSqlLines == null || lstSqlLines.size() == 0)
			return null;
		// 取得单位的过滤条件
		String sDivSqlWhere = null;
		try {
			sDivSqlWhere = getDivWhereSql(lstDept, lstDiv, setYear);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		int iCount = lstSqlLines.size();
		HashMap aData = null;
		String sSqlTream;
		for (int i = 0; i < iCount; i++) {// 循环取出各语句
			aData = (HashMap) lstSqlLines.get(i);
			if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.CREATE_QUERY)) {
				sSqlTream = ((String) aData.get(IBesQryReport.SQLTREAM))
						.toUpperCase();
				// 离线需替换字段类型
				if (iLoginmode == 1) {
					sSqlTream = sSqlTream.replaceAll("VARCHAR2", "VARCHAR");
					sSqlTream = sSqlTream.replaceAll("NUMBER", "NUMERIC");
				}

				oraSearchObj.setCreateTableSql(sSqlTream);
			} else if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.MID_QUERY)) {
				XMLData xmlSource = null;
				if (aData.get(IBesQryReport.VIEWNAME) != null
						&& !Common.isNullStr(aData.get(IBesQryReport.VIEWNAME)
								.toString())) {
					xmlSource = dataDictBO.getTableMainInfo((String) aData
							.get(IBesQryReport.VIEWNAME), SessionUtil
							.getUserInfoContext().getSetYear());
				}

				String sOther = "";
				if (Common.estimate(reportInfo.getCompareFlag())) {// 对比分析表条件
					// 替换表名
					XMLData xmlCompareSource = replaceCompareSource(xmlSource,
							conditionObj, aData);
					if (xmlCompareSource != null)
						xmlSource = xmlCompareSource;
					sOther = getOtherWhereCompare(xmlSource, sVerNo, iTypeFlag,
							setYear, conditionObj, aData);
				} else {
					sOther = getOtherWhere(xmlSource, sVerNo, sBatchNoFilter,
							iTypeFlag, setYear);
				}

				String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
				sInsert = SysRefColI.getServerMethod().replaceRefColFixFlag(
						sInsert);

				if (isNeedChangeTable
						&& !Common.estimate(reportInfo.getCompareFlag()))
					sInsert = convertDataSource(sInsert, xmlSource);
				// 判断语句中是否有DIV_CODE字段
				if (aData.get(IBesQryReport.VIEWNAME) != null) {
					oraSearchObj.addInsertSql(sInsert + sDivSqlWhere + sOther);
				} else {
					if (sInsert.indexOf(IPubInterface.DIV_CODE) != -1) {
						oraSearchObj.addInsertSql(sInsert + sDivSqlWhere
								+ sOther);
					} else {
						oraSearchObj.addInsertSql(sInsert + sOther);
					}
				}

			} else if (aData.get(IBesQryReport.SQLTYPE).equals(
					IDefineReport.LAST_QUERY)) {
				oraSearchObj.setSearchSql("select * from ("
						+ (String) aData.get(IBesQryReport.SQLTREAM)
						+ ") TMPT order by xh");
			}

		}
		return oraSearchObj;
	}

	/**
	 * 得到查询条件
	 */
	private String getDivWhereSql(List lstDept, List lstDiv, String setYear)
			throws Exception {
		String sFilter = "";
		// 得到业务处室查询条件
		String sDeptWhere = getDeptFilter(lstDept, setYear);
		// 得到单位查询条件
		String sDivWhere = getDivFilter(lstDiv);

		if ("".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDivWhere + ")";
		} else if (!"".equals(sDeptWhere) && "".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + ")";
		} else if (!"".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + " or" + sDivWhere + ")";
		}

		// 总查询条件
		sFilter = sFilter + getUserToDiv(setYear);

		return sFilter;
	}

	/**
	 * 得到业务处室查询条件
	 * 
	 * @param lstDept
	 * @param setYear
	 * @return
	 */
	private String getDeptFilter(List lstDept, String setYear) {
		// 业务处室
		String sDeptWhere = "";
		String sDept;
		for (int i = 0; i < lstDept.size(); i++) {
			sDept = "'" + lstDept.get(i).toString() + "'";
			if ("".equals(sDeptWhere))
				sDeptWhere = sDept;
			else
				sDeptWhere = sDeptWhere + "," + sDept;
		}
		if (lstDept.size() > 0) {
			sDeptWhere = "  div_code in (select div_code from fb_u_deptodiv where dep_id in("
					+ sDeptWhere + ") and set_year = " + setYear + ")";
		}
		return sDeptWhere;
	}

	/**
	 * 得到单位查询条件
	 * 
	 * @param lstDiv
	 * @return
	 */
	private String getDivFilter(List lstDiv) {
		String sDivWhereisLeaf = "";
		String sDivWhereNoLeaf = "";
		DivObject divObject;
		for (int i = 0; i < lstDiv.size(); i++) {
			divObject = ((DivObject) lstDiv.get(i));
			if (divObject.isLeaf) { // 叶节点
				if ("".equals(sDivWhereisLeaf)) {
					sDivWhereisLeaf = "'" + divObject.sDivCode + "'";
				} else {
					sDivWhereisLeaf = sDivWhereisLeaf + ",'"
							+ divObject.sDivCode + "'";
				}
			} else { // 不是叶节点
				if ("".equals(sDivWhereNoLeaf)) {
					sDivWhereNoLeaf = " div_code like '" + divObject.sDivCode
							+ "%'";
				} else {
					sDivWhereNoLeaf = sDivWhereNoLeaf + " or div_code like '"
							+ divObject.sDivCode + "%'";
				}
			}
		}
		if (!"".equals(sDivWhereisLeaf)) {
			sDivWhereisLeaf = " div_code in (" + sDivWhereisLeaf + ")";
		}

		String sFilter = "";
		if ("".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereNoLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && "".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf + " or " + sDivWhereNoLeaf;
		}
		return sFilter;
	}

	private String getUserToDiv(String setYear) throws Exception {
		String sFilterWhere = " and div_code in "
				+ KitPub.vw_fb_filterDiv(setYear, "chr_code");
		return sFilterWhere;
	}

	/**
	 * 对比分析表处理查询数据源
	 * 
	 * @param xmlSource
	 * @param conditionObj
	 * @param aData
	 * @throws Exception
	 */
	private XMLData replaceCompareSource(XMLData xmlSource,
			ConditionObj conditionObj, HashMap aData) throws Exception {

		if (xmlSource.get(IDataDictBO.YEAR_FLAG) == null) {
			throw new Exception("数据源未标识上年或本年数据!");
		}
		// 判断是否是上年数据
		if (ConditionObj.PRE_YEAR.equals(xmlSource.get(IDataDictBO.YEAR_FLAG)
				.toString())) {
			// 判断是否要改变数据源
			if (!ConditionObj.PRE_YEAR.equals(conditionObj.getOneYear())) {
				return replaceSource(xmlSource, conditionObj.getOneDataType(),
						aData);
			} else {
				if ("0".equals(conditionObj.getOneDataType())) {
					String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
					sInsert = convertDataSource(sInsert, xmlSource);
					aData.put(IBesQryReport.SQLTREAM, sInsert);
				}
			}
		} else if (ConditionObj.CUR_YEAR.equals(xmlSource.get(
				IDataDictBO.YEAR_FLAG).toString())) {

			// 判断是否要改变数据源
			if (!ConditionObj.CUR_YEAR.equals(conditionObj.getTwoYear())) {
				return replaceSource(xmlSource, conditionObj.getTwoDataType(),
						aData);
			} else {
				if ("0".equals(conditionObj.getTwoDataType())) {
					String sInsert = (String) aData.get(IBesQryReport.SQLTREAM);
					sInsert = convertDataSource(sInsert, xmlSource);
					aData.put(IBesQryReport.SQLTREAM, sInsert);
				}
			}
		} else {
			throw new Exception("数据源标识无法识别!");
		}
		return null;
	}

	// 替换数据源名称
	private XMLData replaceSource(XMLData xmlSource, String dataType,
			HashMap aData) throws Exception {

		if (xmlSource.get(IDataDictBO.COMPARE_DICID) == null)
			throw new Exception("数据源未标识对比数据源!");
		// 重新获得数据源信息根据表Dcid
		XMLData xmlCompareSource = dataDictBO.getTableMainInfo(
				(String) xmlSource.get(IDataDictBO.COMPARE_DICID), SessionUtil
						.getUserInfoContext().getSetYear());
		if (xmlSource == null)
			throw new Exception("对比数据源信息不存在!");
		// 判断是否支持批次
		if (Common.estimate(xmlCompareSource.get("isbatchno"))) {
			convertCompareSource(aData, xmlSource, xmlCompareSource, false);
		} else {
			if ("0".equals(dataType)) {
				convertCompareSource(aData, xmlSource, xmlCompareSource, true);
			} else {
				convertCompareSource(aData, xmlSource, xmlCompareSource, false);
			}
		}
		// 标识年份
		xmlCompareSource.put(IDataDictBO.YEAR_FLAG, xmlSource
				.get(IDataDictBO.YEAR_FLAG));
		return xmlCompareSource;
	}

	/**
	 * 得到对比分析表数据源条件
	 * 
	 * @param xmlSource
	 * @param sVerNo
	 * @param iTypeFlag
	 * @param setYear
	 * @param conditionObj
	 * @param aData
	 * @return
	 * @throws Exception
	 */
	private String getOtherWhereCompare(XMLData xmlSource, String sVerNo,
			int iTypeFlag, String setYear, ConditionObj conditionObj,
			HashMap aData) throws Exception {
		if (xmlSource == null)
			return "";
		StringBuffer sb = new StringBuffer();
		if (Common.estimate(xmlSource.get("sup_ver")))
			sb.append(" and VER_NO=" + sVerNo);

		if (xmlSource.get(IDataDictBO.YEAR_FLAG) == null) {
			throw new Exception("数据源未设置上年或本年标识!");
		}

		// 判断是否支持批次
		if (Common.estimate(xmlSource.get("isbatchno"))) {
			if (ConditionObj.PRE_YEAR.equals(xmlSource.get(
					IDataDictBO.YEAR_FLAG).toString())) {
				return " and batch_no=" + conditionObj.getOneBatchNo()
						+ " and data_type =" + conditionObj.getOneDataType();
			} else if (ConditionObj.CUR_YEAR.equals(xmlSource.get(
					IDataDictBO.YEAR_FLAG).toString())) {
				return " and batch_no=" + conditionObj.getTwoBatchNo()
						+ " and data_type =" + conditionObj.getTwoDataType();
			} else {
				throw new Exception("数据源标识无法识别!");
			}
		}

		sb.append(" and set_year=" + setYear);
		return sb.toString();
	}

	// 依据数据源的情况来取得其它的条件
	private String getOtherWhere(XMLData xmlSource, String sVerNo,
			String sBatchNoFilter, int iTypeFlag, String setYear) {
		if (xmlSource == null)
			return "";
		StringBuffer sb = new StringBuffer();
		if (Common.estimate(xmlSource.get("sup_ver")))
			sb.append(" and VER_NO=" + sVerNo);

		if (Common.estimate(xmlSource.get("isbatchno")))
			sb.append(sBatchNoFilter);
		sb.append(" and set_year=" + setYear);

		return sb.toString();

	}

	// 将查询的数据改为单位表
	private String convertDataSource(String sSql, XMLData xmlDataSource) {
		if (xmlDataSource == null)
			return sSql;
		String divSourceName = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME_DIV);
		if (divSourceName == null || divSourceName.equals(""))
			return sSql;
		sSql = sSql.toUpperCase();
		divSourceName = divSourceName.toUpperCase();
		String oriTable = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME);

		return sSql.replaceAll(oriTable, divSourceName);
	}

	// 将查询的数据改为对比数据表名
	private void convertCompareSource(HashMap aData, XMLData xmlDataSource,
			XMLData xmlCompareSource, boolean divTable) throws Exception {
		String sSql = (String) aData.get(IBesQryReport.SQLTREAM);
		if (xmlDataSource == null)
			return;

		sSql = sSql.toUpperCase();

		String compareSourceName;
		if (divTable) {
			compareSourceName = xmlCompareSource.get(
					IDataDictBO.MAIN_FIELD_TABLEENAME_DIV).toString()
					.toUpperCase();
		} else {
			compareSourceName = xmlCompareSource.get(
					IDataDictBO.MAIN_FIELD_TABLEENAME).toString().toUpperCase();
		}

		String oriTable = (String) xmlDataSource
				.get(IDataDictBO.MAIN_FIELD_TABLEENAME);

		aData.put(IBesQryReport.SQLTREAM, sSql.replaceAll(oriTable,
				compareSourceName));
		aData.put(IBesQryReport.VIEWNAME, xmlCompareSource
				.get(IDataDictBO.DICID));
	}
}
