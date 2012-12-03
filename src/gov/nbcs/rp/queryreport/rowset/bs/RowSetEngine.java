/**
 * @# RowSetEngine.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.bs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.dicinfo.datadict.bs.DataDictBO;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.qrbudget.bs.KitPub;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ui.Condition;
import gov.nbcs.rp.queryreport.rowset.ui.RowInfo;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 功能说明:行报表查询引擎 主要的机制:尽量将查询一个表的数据项都并入一个查询，
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class RowSetEngine {
	private List lstRows;

	private XMLData xmlRows;// 主键为ITMEID 值为ROWINFO

	private XMLData xmlDsRow;// 主键为数据源,值是LIST list中存了ROWINFO

	private XMLData xmlRowFValue;// 暂存值的地方，主键为F+ITEMID;值为ROWINFO

	private List lstRowSqls;// 以SQL为查询的行值为ROWINFO

	private int rows = -1;

	private String reportID;

	private String setYear;

	private List depts;

	private List divs;

	private String divWhere = "1=1";// 单位的过滤

	private GeneralDAO dao;

	private RowSetBO rowSetBO;

	DataDictBO dataDicBO;

	String sVerNo;

	String sBatchNoFilter;

	int iLoginmode;

	/**
	 * 初始化时提供
	 * 
	 * @param rows
	 *            一列显示的行数
	 * @param reportID
	 *            报表ID
	 * @param depts
	 *            部门条件
	 * @param divs
	 *            单位条件
	 */
	public RowSetEngine(int rows, String reportID, List depts, List divs,
			String sVerNo, String sBatchNoFilter, int iLoginmode, GeneralDAO dao) {
		this.rows = rows;
		this.reportID = reportID;
		this.depts = depts;
		this.divs = divs;
		this.sVerNo = sVerNo;
		this.sBatchNoFilter = sBatchNoFilter;
		this.iLoginmode = iLoginmode;
		this.dao = dao;
		setYear = SessionUtil.getUserInfoContext().getSetYear();
		initData();
	}

	// 初始化数据，从XMLData变为ROWINFO类列表
	private void initData() {
		rowSetBO = new RowSetBO();
		rowSetBO.setDao(this.dao);
		List lstXMLRows = rowSetBO.getReportRows(reportID, setYear);
		List lstXMLCons = rowSetBO.getReportCons(reportID, setYear);

		dataDicBO = new DataDictBO();
		dataDicBO.setDao(dao);
		if (lstXMLRows != null && lstXMLRows.size() > 0) {
			int iCount = lstXMLRows.size();

			// 初始化行
			for (int i = 0; i < iCount; i++) {
				RowInfo aRow = new RowInfo((XMLData) lstXMLRows.get(i), null);
				addRow(aRow);
			}
			// 初始化条件lstCons
			if (lstXMLCons != null && lstXMLCons.size() > 0) {
				iCount = lstXMLCons.size();
				for (int i = 0; i < iCount; i++) {
					Condition aCon = new Condition((XMLData) lstXMLCons.get(i));
					Object obj = xmlRows.get(aCon.getItemID());
					if (obj == null)
						continue;
					((RowInfo) obj).addCons(aCon);
				}

			}
		} else {
			lstRows = new ArrayList();
			xmlRows = new XMLData();
		}
	}

	/**
	 * 添加一行
	 * 
	 * @param aRow
	 */
	private void addRow(RowInfo aRow) {
		if (aRow == null)
			return;
		if (lstRows == null)
			lstRows = new ArrayList();
		if (xmlRows == null)
			xmlRows = new XMLData();
		lstRows.add(aRow);
		xmlRows.put(aRow.getItemID(), aRow);

	}

	/**
	 * 查询返回XMLDATA，组织形式为，行，列
	 * 
	 */
	public XMLData doSearch() {
		xmlRowFValue = new XMLData();
		try {// 查找
			divWhere = getDivWhereSql(depts, divs, setYear);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		seperateSearch();// 分类
		searchSeperate();
		searchSql();
		makeToCellValue();
		// 可能要添加单元格的计算
		return xmlRowFValue;

	}

	private void searchSeperate() {
		if (xmlDsRow == null)
			return;
		Iterator it = xmlDsRow.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List lstRow = (List) xmlDsRow.get(key);
			searchDsGroup(key, lstRow);
		}

	}

	// 查询语句
	private void searchSql() {
		if (lstRowSqls == null || lstRowSqls.size() == 0)
			return;

		RowInfo rowInfo;
		String sSql = "";
		for (int i = 0; i < lstRowSqls.size(); i++) {
			rowInfo = (RowInfo) lstRowSqls.get(i);
			// if (Common.isNullStr(rowInfo.getTableName())) {// 如果有数据源，则要添加查询条件
			// sSql = rowInfo.getSql();
			// } else {
			// if (rowInfo.getSql().toLowerCase().indexOf("where") == -1)
			// sSql = rowInfo.getSql() + " where 1=1 " + divWhere
			// + getSqlWhere(rowInfo.getSourceID());
			// else
			// sSql = rowInfo.getSql() + divWhere
			// + getSqlWhere(rowInfo.getSourceID());
			// }
			sSql = rowInfo.getSql();
			sSql = replaceAllPamam(sSql);
			List lstData = null;
			try {
				if (rowInfo.getItemID().equals("5")) {
					System.out.println(sSql);
				}
				lstData = dao.findBySql(sSql);
			} catch (Exception e) {
				System.out.println(sSql);
				e.printStackTrace();
			}
			if (lstData == null || lstData.size() == 0)
				continue;
			// 将取得的第一个行的第一个字段插入
			XMLData aData = (XMLData) lstData.get(0);
			Iterator it = aData.values().iterator();
			xmlRowFValue.put("f" + rowInfo.getItemID(), it.next());

		}

	}

	// 将查询分类,以数据源分类
	private void seperateSearch() {
		if (lstRows == null || lstRows.size() == 0)
			return;
		xmlDsRow = new XMLData();
		lstRowSqls = new ArrayList();
		int iCount = lstRows.size();
		String sourceID;
		RowInfo rowInfo;
		for (int i = 0; i < iCount; i++) {
			rowInfo = (RowInfo) lstRows.get(i);
			if (rowInfo.getMeasureType() == RowInfo.MEASURE_SQL) {
				lstRowSqls.add(rowInfo);
				continue;
			}

			sourceID = rowInfo.getSourceID();
			Object obj = xmlDsRow.get(sourceID);
			if (obj == null) {
				List lstDs = new ArrayList();
				lstDs.add(rowInfo);
				xmlDsRow.put(sourceID, lstDs);
				continue;
			}
			((List) obj).add(rowInfo);

		}

	}

	// 以分组的查询
	// 查询是以CASE when 进行
	private void searchDsGroup(String sourceID, List lstRowInfo) {
		RowInfo rowInfo = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		for (int i = 0; i < lstRowInfo.size(); i++) {
			rowInfo = (RowInfo) lstRowInfo.get(i);
			sb.append(rowInfo.getSearchSql()).append(",");
		}
		// 添加条件
		sb = sb.deleteCharAt(sb.length() - 1);

		sb.append(getSourceAndWhere(sourceID));
		List lstData = dao.findBySql(sb.toString());
		if (lstData == null || lstData.size() == 0)
			return;
		xmlRowFValue.putAll((XMLData) lstData.get(0));

	}

	private String getSourceAndWhere(String sourceID) {
		XMLData aData = dataDicBO.getTableMainInfo(sourceID, SessionUtil
				.getUserInfoContext().getSetYear());
		StringBuffer sb = new StringBuffer();
		String tableName = getDsName(sourceID, aData);

		// 添加表
		sb.append(" from ").append(tableName);
		sb.append(" where 1=1 ");
		// 如果是多表复用的则要添加报表过滤
//		if (IAudit.MUTI_TABLE.indexOf(tableName.toLowerCase()) != -1)
//			sb.append(" and report_id='" + sourceID + "'");

		sb.append(divWhere);// 单位过滤
		if ("1".equals(Common.getAStringField(aData,// 批次过滤
				IDataDictBO.MAIN_FIELD_ISBATCHNO)))
			sb.append(sBatchNoFilter);
		if ("1".equals(Common.getAStringField(aData,// 版本过滤
				IDataDictBO.MAIN_FIELD_SUP_VER)))
			sb.append(" and  VER_NO=" + sVerNo);
		return sb.toString();
	}

	// 公式的计算
	// private void searchFormula() {
	//
	// }

	/**
	 * 得到查询条件 // 根据数据源的信息确定所要的过滤条件
	 */
	public String getDivWhereSql(List lstDept, List lstDiv, String setYear)
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
		IQrBudget.DivObject divObject;
		for (int i = 0; i < lstDiv.size(); i++) {
			divObject = ((IQrBudget.DivObject) lstDiv.get(i));
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

	// 将F开头的键值转换为列行为键的形式
	private void makeToCellValue() {
		XMLData aData = new XMLData();
		Iterator it = xmlRowFValue.keySet().iterator();
		String key;
		while (it.hasNext()) {
			key = (String) it.next();
			// 取得数据
			int i = Integer.parseInt(key.substring(1));
			String newKey = getKeyValue(rows, i);
			aData.put(newKey, xmlRowFValue.get(key));
		}
		xmlRowFValue = aData;
	}

	// iRows 一列要显示的行
	// iCur 当前的行，从1开始的
	private String getKeyValue(int iRows, int iCur) {
		iCur = iCur - 1;
		int col = iCur / iRows * 3 + 2;
		int row = iCur % iRows;

		return "" + col + "_" + row;
	}

	private String getDsName(String curDsName, XMLData xmlSource) {
		if (UntPub.FIS_VIS.equals(SessionUtilEx.getBelongType()))// 财政
			return (String) xmlSource.get(IDataDictBO.MAIN_FIELD_TABLEENAME);
		else
			return (String) xmlSource
					.get(IDataDictBO.MAIN_FIELD_TABLEENAME_DIV);

	}

	private String replaceAllPamam(String sSql) {
		if (Common.isNullStr(sSql))
			return "";
		sSql = sSql.replaceAll(RowInfo.PARAM_DIV, " 1=1 " + divWhere);
		if (!Common.isNullStr(sBatchNoFilter)) {
			sBatchNoFilter = sBatchNoFilter.toLowerCase();
			String batchNum = "";
			String dataType = "";

			String arrPar[] = sBatchNoFilter.split("and");
			for (int i = 0; i < arrPar.length; i++) {
				String temp = arrPar[i];
				String arrTemp[] = temp.split("=");
				if (arrTemp[0].toString().trim().equals("batch_no"))
					batchNum = arrTemp[1];
				else if (arrTemp[0].toString().trim().equals("data_type")) {
					dataType = arrTemp[1];
				}
			}
			sSql = sSql.replaceAll(RowInfo.PARAM_BATCH, batchNum);
			sSql = sSql.replaceAll(RowInfo.PARAM_DATA_TYPE, dataType);
		}
		sSql = sSql.replaceAll(RowInfo.PARAM_DATA_VER, sVerNo == null ? ""
				: sVerNo);
		sSql = sSql.replaceAll(RowInfo.PARAM_REPORT_ID, reportID == null ? ""
				: reportID);
		sSql = sSql.replaceAll(RowInfo.PARAM_SET_YEAR, SessionUtil
				.getUserInfoContext().getSetYear());
		// 替换表
		Pattern p = Pattern.compile("\\$table-[\\da-zA-z\\_]{1,100}");
		Matcher m = p.matcher(sSql);
		while (m.find()) {
			String tableName = sSql.substring(m.start(), m.end());
			tableName = tableName.substring(7);
			// modify by zlx 2009-06-25 原因：基础信息不使用M表
			// if (UntPub.FIS_VIS.equals(SessionUtilEx.getBelongType()))// 财政
			// tableName = "FB_B_" + BasPubAction.GetLastName(tableName);
			// else
			// tableName = "FB_B_M_" + BasPubAction.GetLastName(tableName);
			sSql = sSql.substring(0, m.start() - 1) + " " + tableName + " "
					+ sSql.substring(m.end());
			m = p.matcher(sSql);
		}

		return sSql;
	}
}
