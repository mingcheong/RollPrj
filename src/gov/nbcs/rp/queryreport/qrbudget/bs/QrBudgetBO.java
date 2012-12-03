package gov.nbcs.rp.queryreport.qrbudget.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.ToolsEx;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.log4j.FbLogger;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OtherSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class QrBudgetBO implements IQrBudget {

	public DataSet getReportName(int iUserType, String setYear,
			String reportType) throws Exception {
		int iDataUser = UntPub.FIS_VIS.equals(SessionUtilEx.getBelongType()) ? 1
				: 0;
		boolean type =!"001".equals(SessionUtil.getUserInfoContext().getOrgType());
		SessionUtil.getUserInfoContext();

		String sUserToReportFilter = "";
		String Ssql2 = "";
		if (type) { // 1:业务处室,其他单位
			sUserToReportFilter = "  and b.c1 = 1  and exists ( select report_id,report_type from fb_u_usertoreport d where user_id ='"
					+ SessionUtil.getUserInfoContext().getUserID()
					+ "'  "
			
					+ " and a.report_id =d.report_id  "
					+ "  "
					+ " and c.type_code = d.report_type "
					+ " )";
			Ssql2=	 " union all"
			  + " select a.lvl_id, a.report_id, a.report_cname ,a.title,"
			  + "a.is_hasbatch, a.IS_MULTICOND,a.is_end as end_flag,a.type_flag,"
			  + "a.CURRENCYUNIT,a.report_id||c.type_code show_lvl,c.type_code par_id,a.col_num,a.FUNDSOURCE_FLAG,a.COMPARE_FLAG "
			  + " from fb_u_qr_repset a,fb_s_pubcode b,fb_u_qr_report_to_type c"
			  + " where  a.IS_PASSVERIFY = '是' "
			+ " and a.IS_ACTIVE='是' and a.data_user in("
			+ String.valueOf(iDataUser)
			+ ", 2)"
			+ " and  a.report_id=c.report_id and c.type_code =b.code "
		
			+ " and  b.typeid = 'QUERYTYPE' and  b.c1<>1"
			+ " and a.report_type=" + reportType;
	
		
		
		}
		String sSql = "select lvl_id,code as report_id ,name as report_cname,'' as title,"
				+ "'' as is_hasbatch, '' as  IS_MULTICOND, 0 as end_flag ,0 as type_flag,"
				+ "'' as CURRENCYUNIT,code as show_lvl ,'' par_id,0 as col_num,'0' FUNDSOURCE_FLAG,'0' COMPARE_FLAG"
				+ " from fb_s_pubcode tab where typeid = 'QUERYTYPE'  "
				
				+ " and exists (select 1  from fb_u_qr_repset A,fb_u_qr_report_to_type C "
				+ "where  "
				+" A.IS_PASSVERIFY = '是' "
				+ " and A.IS_ACTIVE='是' and A.data_user in("
				+ String.valueOf(iDataUser)
				+ ", 2) "
				+ " "
				+ " and A.report_id = C.report_id "
				+ " and C.type_code=TAB.code "
				+ " and A.is_end = 1 and A.report_type="
				+ reportType
				+ ")"
				+ " union all"
				+ " select a.lvl_id, a.report_id, a.report_cname ,a.title,"
				+ "a.is_hasbatch, a.IS_MULTICOND,a.is_end as end_flag,a.type_flag,"
				+ "a.CURRENCYUNIT,a.report_id||c.type_code show_lvl,c.type_code par_id,a.col_num,a.FUNDSOURCE_FLAG,a.COMPARE_FLAG "
				+ " from fb_u_qr_repset a,fb_s_pubcode b,fb_u_qr_report_to_type c"
				+ " where  a.IS_PASSVERIFY = '是' "
				+ " and a.IS_ACTIVE='是' and a.data_user in("
				+ String.valueOf(iDataUser)
				+ ", 2)"
				+ " and  a.report_id=c.report_id and c.type_code =b.code "
				
				+ " and  b.typeid = 'QUERYTYPE' "
				+ sUserToReportFilter + " and a.report_type=" + reportType;
		
	
         if(type)
        	 sSql+=Ssql2;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public DataSet getDivName(String setYear, int iLevel, int iUserType)
			throws Exception {
		// 1:业务处室,其他单位
		if (iUserType == 1) {
			return PubInterfaceStub.getServerMethod().getDepToDivData(setYear,
					iLevel, true);
		} else {
			String swhere = KitPub.getFilterDiv();
			String sSql = "select en_id,en_id as EID,div_code,div_name,code_name,parent_id from vw_rp_division ENTAB "
					+ " where  1=1 and set_year = "+SessionUtil.getLoginYear()+" " + swhere;
			DataSet ds = DataSet.create();
			DBSqlExec.getDataSet(sSql, ds);
			return ds;
		}
	}

	/**
	 * 得到报表表头sql
	 */
	public String getReportHeaderSql(String sReportId, String setYear)
			throws Exception {
		String sSql = "select distinct a.Field_Id, a.FIELD_CODE ,a.FIELD_CNAME,a.FIELD_ENAME,a.FIELD_FNAME,"
				+ "a.FIELD_TYPE,a.FIELD_DISPWIDTH,a.FIELD_DISFORMAT,a.IS_LEAF,a.OBJECT_CNAME,a.OBJECT_ENAME,"
				+ "a.FUNDSOURCE_FLAG,a.COMPARE_FLAG"
				+ " from fb_u_qr_colset a , fb_u_qr_colset b"
				+ " where b.field_code like a.field_code||'%'  and b.report_id = a.report_id"
				+ " and  b.report_id = '"
				+ sReportId
				+ "' and ((b.is_hidecol='否' or b.is_hidecol is null or b.is_hidecol ='') and b.is_leaf=1)   "
			+ " order by a.FIELD_CODE ";
		return sSql;
	}

	public DataSet getReportHeader(String sReportId, String setYear)
			throws Exception {
		String sSql = getReportHeaderSql(sReportId, setYear);
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 保存列宽
	 * 
	 * @throws Exception
	 */
	public void saveColWidth(String sReportId, String setYear, List lstFields,
			List lstColWidth) throws Exception {
		List lstSql = new ArrayList();
		String sSql;
		for (int i = 0; i < lstFields.size(); i++) {
			sSql = " update fb_u_qr_colset set FIELD_DISPWIDTH = "
					+ lstColWidth.get(i) + " where report_id = '" + sReportId
					+ "'  and FIELD_CODE= '"
					+ lstFields.get(i) + "'";
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 得到查询条件
	 */
	public String getDivWhereSql(int iTypeFlag, List lstDept, List lstDiv,
			String setYear) throws Exception {
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
	 * 查询
	 */
	public List getData(String sVerNo, String sReportID, String sBatchNoFilter,
			List lstDept, List lstDiv, String sFieldSelect, String setYear,
			int iUserType, int iLoginmode, int iTypeFlag) throws Exception {

		OtherSearchObj otherSearchObj = getOtherSearchObj(sVerNo, sReportID,
				sBatchNoFilter, lstDept, lstDiv, sFieldSelect, setYear,
				iUserType, iLoginmode);

		if (otherSearchObj == null)
			throw new Exception("生成查询体失败");
		String sErr = otherSearchObj.check();// 检查查询的准备是否正确
		if (!sErr.equals(""))
			throw new Exception(sErr);
		try {
			// 创建中间视图
			QueryStub.getQueryTool().executeBatch(
					otherSearchObj.getLstSqlMidView());
			// 得到查询结果
			List lstData = dao.findBySql(otherSearchObj.getSSqlBody());
			List lstResult = new ArrayList();
			lstResult.add(lstData);
			lstResult.add(otherSearchObj);
			return lstResult;
		} catch (Exception e) {
			// 输出调试语句到文件
			FbLogger.fileLogger().error(
					"--查询表" + sReportID + "   " + e.getMessage() + " start --");
			List lstMideViewSql = otherSearchObj.getLstSqlMidView();
			for (Iterator itVer = lstMideViewSql.iterator(); itVer.hasNext();) {
				String sSqlTmp = (String) itVer.next();
				FbLogger.fileLogger().error(sSqlTmp);
			}
			FbLogger.fileLogger().error(otherSearchObj.getSSqlBody());
			List lstDeleteRecord = otherSearchObj.getLstDeleteRecord();
			for (Iterator itVer = lstDeleteRecord.iterator(); itVer.hasNext();) {
				String sSqlTmp = (String) itVer.next();
				FbLogger.fileLogger().error(sSqlTmp);
			}
			FbLogger.fileLogger().error("--查询表" + sReportID + " end --");

			if (otherSearchObj.getLstDeleteRecord() != null
					&& otherSearchObj.getLstDeleteRecord().size() != 0)
				QueryStub.getQueryTool().executeBatch(
						otherSearchObj.getLstDeleteRecord());

			throw new Exception(e.getMessage());
		} finally {
			try {
				if (otherSearchObj.getLstDeleteRecord() != null
						&& otherSearchObj.getLstDeleteRecord().size() != 0)
					QueryStub.getQueryTool().executeBatch(
							otherSearchObj.getLstDeleteRecord());
			} catch (Exception e1) {
			}
		}
	}

	/**
	 * 返回查询语句
	 * 
	 * @param sVerNo
	 * @param sReportId
	 * @param sBatchNoFilter
	 * @param lstDept
	 * @param lstDiv
	 * @param sFieldSelect
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @return
	 * @throws Exception
	 */
	public OtherSearchObj getOtherSearchObj(String sVerNo, String sReportId,
			String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iUserType, int iLoginmode)
			throws Exception {
		// 得到单位查询条件
		String sDivWhereSql = getDivWhereSql(-1, lstDept, lstDiv, setYear);
		String sFilter = sBatchNoFilter + sDivWhereSql;
		// 创建中间视图
		OtherSearchObj otherSearchObj = createMidViewSql(sVerNo, sReportId,
				sFilter, setYear, iUserType, iLoginmode);

		// 得到查询语句
		String sSqlBody = getExcuteSearchSql(sReportId, sFieldSelect, setYear,
				iUserType, iLoginmode, otherSearchObj);
		otherSearchObj.setSSqlBody(sSqlBody);

		return otherSearchObj;
	}

	/**
	 * 创建中间视图Sql
	 * 
	 * @throws Exception
	 * 
	 */
	private OtherSearchObj createMidViewSql(String verNo, String sReportID,
			String sFilter, String setYear, int iUserType, int iLoginmode)
			throws Exception {

		String sSql = "";
		if ("0".equals(verNo))
			sSql = "SELECT VIEWNAME,SQLLINES FROM fb_u_qr_sqllines WHERE "
				
					+ "  Report_ID='"
					+ sReportID
					+ "' AND SqlType='MIDQUERY' " + " Order By ViewLevel";
		else
			sSql = "SELECT a.VIEWNAME,b.exec_sql SQLLINES FROM fb_u_qr_sqllines a ,fb_u_qr_querytotable b  WHERE"
					+ " a.Report_ID='"
					+ sReportID
					+ "' AND a.SqlType='MIDQUERY' "
					+ " and a.report_id=b.report_id and b.ver_no="
					+ verNo
					+ " Order By ViewLevel";

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);

		List lstSql = new ArrayList();
		List lstSqlDel = new ArrayList();
		List lstTempTable = new ArrayList();
		List lstViewName = new ArrayList();
		String sViewName;
		String sSqlLines;
		String sSqlDel;
		ds.beforeFirst();
		while (ds.next()) {
			sViewName = ds.fieldByName("VIEWNAME").getString();
			Object oSqlLines = ds.fieldByName("SQLLINES").getValue();
			if (oSqlLines == null || "".equals(oSqlLines.toString())) {
				throw new Exception("查询设置有误，查询表内容表中SQLLINES字段内容为空。");
			}
			sSqlLines = ds.fieldByName("SQLLINES").getString();
			if (sSqlLines.toUpperCase().indexOf("WHERE") < 0) {
				sSqlLines = sSqlLines + " where 1=1";
			}
			sSqlLines = sSqlLines + sFilter;

			// 0:省厅,1:宁波
			if (getSearchType(setYear) == 0) {
				if (iUserType == 1) {// iUserType == 1财政用户
					sSqlLines = "CREATE OR REPLACE  VIEW " + sViewName + " AS "
							+ sSqlLines;
				} else {
					if (iLoginmode == 1) {// iLoginmode == 0，离线登陆方式
						sSqlLines = "Alter  VIEW " + sViewName + "_Div"
								+ " AS " + sSqlLines;
					} else {
						// sSqlLines = "CREATE OR REPLACE VIEW " + sViewName
						// + "_Div" + " AS " + sSqlLines;
						sSqlDel = "delete GT_" + sViewName
								+ "_Div where login_id = '"
								+ SessionUtil.getUserInfoContext().getUserID()
								+ "'";
						lstSqlDel.add(sSqlDel);
						sSqlLines = "insert into GT_" + sViewName + "_Div"
								+ " select a.*,"
								+ SessionUtil.getUserInfoContext().getUserID()
								+ " from (" + sSqlLines + ") a";
					}
				}
			} else {
				if (iLoginmode == 1) {// iLoginmode == 0，离线登陆方式
					sSqlLines = "Alter  VIEW " + sViewName + " AS " + sSqlLines;
				} else {
					// 先生成临时表名称
					String sTempTable = null;
					try {
						sTempTable = OriSearchObj.generateTempTableName();
					} catch (Exception e) {
						e.printStackTrace();
						throw new Exception("生成临时表失败!");
					}
					sSqlDel = "Drop table " + sTempTable;
					lstSqlDel.add(sSqlDel);
					lstViewName.add(sViewName);
					lstTempTable.add(sTempTable);
					// 创建临时表，并插入数据
					sSqlLines = "CREATE TABLE  " + sTempTable + " AS "
							+ sSqlLines;

				}
			}
			lstSql.add(sSqlLines);
		}
		OtherSearchObj otherSearchObj = new OtherSearchObj();
		otherSearchObj.setLstSqlMidView(lstSql);
		otherSearchObj.setLstDeleteRecord(lstSqlDel);
		otherSearchObj.setLstTempTable(lstTempTable);
		otherSearchObj.setLstViewName(lstViewName);
		return otherSearchObj;
	}

	private String getUserToDiv(String setYear) throws Exception {
		String sFilterWhere = " and div_code in "
				+ KitPub.vw_fb_filterDiv(setYear, "chr_code");
		return sFilterWhere;
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
					+ sDeptWhere + "))";
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

	GeneralDAO dao;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * 执行查询
	 * 
	 * @throws Exception
	 */
	// private BodyInfo excuteSearch(String sReportId, String sFieldSelect,
	// String setYear, int iUserType, int iLoginmode) throws Exception {
	// // 得到表体查询语句
	// String sSqlBody = getExcuteSearchSql(sReportId, sFieldSelect, setYear,
	// iUserType, iLoginmode);
	//
	// Session session = dao.getSession();
	// Connection con = session.connection();
	// /** 结果集 */
	// ResultSet rs;
	//
	// Statement st;
	//
	// List lstList = new ArrayList();
	// try {
	//
	// st = con.createStatement();
	//
	// rs = st.executeQuery(sSqlBody);
	//
	// int iColumnCount = rs.getMetaData().getColumnCount();
	//
	// while (rs.next()) {
	// ArrayList arrayList = new ArrayList();
	// for (int i = 1; i <= iColumnCount; i++) {
	// arrayList.add(rs.getString(i));
	// }
	// lstList.add(arrayList);
	// if (lstList.size() > 10000) {
	// break;
	// }
	// }
	//
	// rs.close();
	// st.close();
	//
	// } finally {
	// dao.closeSession(session);
	// }
	//
	// BodyInfo bodyInfo = new BodyInfo();
	// bodyInfo.lstBody = dao.findBySql(sSqlBody);
	// bodyInfo.sSqlBody = sSqlBody;
	// return bodyInfo;
	// }
	/**
	 * 得到表体查询语句
	 */
	private String getExcuteSearchSql(String sReportId, String sFieldSelect,
			String setYear, int iUserType, int iLoginmode,
			OtherSearchObj otherSearchObj) throws Exception {
		String sSql = "SELECT VIEWNAME FROM fb_u_qr_sqllines WHERE "
				+ "  Report_ID='" + sReportId
				+ "' AND SqlType='LASTQUERY' ";
		String sViewName = DBSqlExec.getStringValue(sSql);
		if (sViewName == null || "".equals(sViewName)) {
			return null;
		}

		String sSqlBody;
		// 0:省厅,1:宁波
		if (getSearchType(setYear) == 0) {
			if (iUserType == 0) {// 单位
				if (iLoginmode == 1) {// iLoginmode == 0，离线登陆方式
					sSqlBody = "select " + sFieldSelect + " from " + sViewName
							+ "_DIV order by xh";
				} else {
					sSqlBody = "select " + sFieldSelect + " from " + sViewName
							+ "_DIV where login_id = '"
							+ SessionUtil.getUserInfoContext().getUserID()
							+ "' order by xh";
				}
			} else {// 用户类型:1：业务处室
				sSqlBody = "select " + sFieldSelect + " from " + sViewName
						+ " order by xh";
			}
		} else {
			if (iLoginmode == 1) {// iLoginmode == 0，离线登陆方式
				sSqlBody = "select " + sFieldSelect + " from " + sViewName
						+ " order by xh";
			} else {

				List lstTempTable = otherSearchObj.getLstTempTable();
				List lstViewName = otherSearchObj.getLstViewName();
				List lstResult = dao
						.findBySql("select text from user_views where view_name ='"
								+ sViewName + "'");
				if (lstResult == null || lstResult.size() == 0)
					throw new Exception("视图不存在，请与管理员联系！");

				sSqlBody = ((XMLData) lstResult.get(0)).get("text").toString()
						.toUpperCase();

				if (lstTempTable == null || lstTempTable.size() == 0) {
					return null;
				}

				int size = lstTempTable.size();
				for (int i = 0; i < size; i++) {
					if (sSqlBody.indexOf(lstViewName.get(i).toString()
							.toUpperCase()) == -1)
						throw new Exception("报表设置有误，最终视图未在中间视图上查询,请与管理员联系!");
				}
				// 替换视图名称为临时表名称
				for (int i = 0; i < size; i++) {
					sSqlBody = sSqlBody.replaceAll(lstViewName.get(i)
							.toString().toUpperCase(), lstTempTable.get(i)
							.toString().toUpperCase());

				}
				sSqlBody = "select " + sFieldSelect + " from (" + sSqlBody
						+ ") order by xh";
			}
		}
		return sSqlBody;
	}

	// 查询类型0：表示江苏和江苏相同的地区 ，1：表示宁波
	public int getSearchType(String sYear) throws Exception {
		String sql = "SELECT IVALUE FROM FB_S_PUBCODE WHERE TYPEID = 'SEARCHTYPE' "
				;
		return DBSqlExec.getIntValue(sql);
	}

	// chcx add 版本处理2007.09.09
	// 得到版本信息
	public DataSet getVerInfo() throws Exception {
		DataSet ds = DataSet.create();
		String sSQL = "select t.*,'['||ver_no||']'||ver_date FULLNAME,'' parent_id "
				+ " from FB_U_QR_QUERYVER t order by ver_no";
		DBSqlExec.getDataSet(sSQL, ds);
		return ds;
	}

	/**
	 * 根据报表得到版本信息,author:zlx
	 */
	public DataSet getVerInfoWithReport(String sReportId) throws Exception {
		DataSet ds = DataSet.create();
		String sSQL = "select DISTINCT B.VER_NO,'['||B.ver_no||']'||B.ver_date FULLNAME from FB_U_QR_QUERYTOTABLE A "
				+ " left join FB_U_QR_QUERYVER B on A.ver_no = B.Ver_No"
				+ " where report_id ='" + sReportId + "' ORDER BY VER_NO";
		DBSqlExec.getDataSet(sSQL, ds);
		return ds;
	}

	// 删除版本信息
	public InfoPackage delVerInfo(String verNo) throws Exception {
		InfoPackage infopackage = new InfoPackage();
		try {
			DataSet dsTemp = DataSet.create();
			DataSet ds = DataSet.create();
			List sSqls = new LinkedList();
			String sSQL = "";
			sSQL = "select ACCO_TABLE from FB_U_QR_QUERYTOTABLE where ver_no="
					+ verNo;
			DBSqlExec.getDataSet(sSQL, ds);
			if (ds != null && !ds.isEmpty())
				ds.beforeFirst();
			while (ds.next()) {
				sSQL = "select * from user_tables  t where t.table_name =Upper('"
						+ ds.fieldByName("ACCO_TABLE").getString() + "')";
				DBSqlExec.getDataSet(sSQL, dsTemp);
				if (dsTemp != null && !dsTemp.isEmpty()) {
					sSQL = "drop table "
							+ ds.fieldByName("ACCO_TABLE").getString();
					sSqls.add(sSQL);
				}
			}
			sSQL = "delete from FB_U_QR_QUERYVER where ver_no=" + verNo;
			sSqls.add(sSQL);

			sSQL = "delete from FB_U_QR_QUERYTOTABLE where ver_no=" + verNo;
			sSqls.add(sSQL);

			if (sSqls.size() > 0)
				QueryStub.getQueryTool().executeBatch(sSqls);

		} catch (Exception ex) {
			infopackage.setsMessage(ex.getMessage());
			infopackage.setSuccess(false);
			return infopackage;
		}
		infopackage.setSuccess(true);
		return infopackage;
	}

	public InfoPackage saveVerInfo(String verNo, String verDate,
			String verRemark, String EditorType) throws Exception {
		InfoPackage infopackge = new InfoPackage();
		DataSet ds = DataSet.create();
		List sSqls = new LinkedList();
		String sSQL = "";
		if (IQrBudget.ADD.equals(EditorType)) {
			sSQL = "select * from FB_U_QR_QUERYVER where ver_no=" + verNo;
			DBSqlExec.getDataSet(sSQL, ds);
			if (ds != null && ds.getRecordCount() > 0) {
				infopackge.setsMessage("版本号重复，请重新输入！");
				infopackge.setSuccess(false);
				return infopackge;
			}
			sSQL = "insert into FB_U_QR_QUERYVER(ver_no,ver_date,ver_remark,set_year,last_ver) values("
					+ verNo
					+ ",'"
					+ verDate
					+ "','"
					+ verRemark
					+ "',"
					+ SessionUtil.getUserInfoContext().getSetYear()
					+ ",'"
					+ ToolsEx.getServerDate() + "')";
			sSqls.add(sSQL);
		} else {
			sSQL = "update FB_U_QR_QUERYVER set ver_date='" + verDate
					+ "',ver_remark='" + verRemark + "',set_year="
					+ SessionUtil.getUserInfoContext().getSetYear()
					+ ",last_ver='" + ToolsEx.getServerDate()
					+ "' where ver_no=" + verNo;
			sSqls.add(sSQL);
		}
		if (sSqls.size() > 0)
			QueryStub.getQueryTool().executeBatch(sSqls);
		infopackge.setSuccess(true);
		return infopackge;
	}

	// 删除版本物理表
	public InfoPackage DelTableReportVer(String verNo) throws Exception {
		InfoPackage infopackage = new InfoPackage();
		try {
			List sSqls = new LinkedList();
			DataSet dsTemp = DataSet.create();
			DataSet ds = DataSet.create();
			String sSQL = "";
			// 删除版本物理表
			sSQL = "select ACCO_TABLE from FB_U_QR_QUERYTOTABLE where ver_no="
					+ verNo;
			DBSqlExec.getDataSet(sSQL, ds);
			if (ds != null && !ds.isEmpty())
				ds.beforeFirst();
			while (ds.next()) {
				sSQL = "select * from user_tables  t where t.table_name =Upper('"
						+ ds.fieldByName("ACCO_TABLE").getString() + "')";
				DBSqlExec.getDataSet(sSQL, dsTemp);
				if (dsTemp != null && !dsTemp.isEmpty()) {
					sSQL = "drop table "
							+ ds.fieldByName("ACCO_TABLE").getString();
					sSqls.add(sSQL);
				}
			}
			sSQL = "delete from FB_U_QR_QUERYTOTABLE where ver_no=" + verNo;
			sSqls.add(sSQL);
			// 从视图生成物理表配置表
			if (Global.loginmode == 0)
				sSQL = "insert into fb_u_qr_querytotable(REPORT_ID,VER_NO,EXEC_SQL,ACCO_TABLE) select a.report_id,"
						+ verNo
						+ ",Substr(b.sqllines,0,instr(upper(b.sqllines),'FROM')-1)||' FROM  FB_MV_'||a.REPORT_ID||'_'||"
						+ verNo
						+ ",'FB_MV_'||a.REPORT_ID||'_'||"
						+ verNo
						+ " from fb_u_qr_repset a,fb_u_qr_sqllines b where a.data_user in (1,2) "
						+ " and a.report_id=b.report_id and b.sqltype='MIDQUERY' and IS_ACTIVE='是'";
			else
				sSQL = "insert into fb_u_qr_querytotable(REPORT_ID,VER_NO,EXEC_SQL,ACCO_TABLE) select a.report_id,"
						+ verNo
						+ ",Substr(b.sqllines,0,charindex('FROM',upper(b.sqllines))-1)||' FROM  FB_MV_'||a.REPORT_ID||'_'||"
						+ verNo
						+ ",'FB_MV_'||a.REPORT_ID||'_'||"
						+ verNo
						+ " from fb_u_qr_repset a,fb_u_qr_sqllines b where a.data_user in (1,2) "
						+ " and a.report_id=b.report_id and b.sqltype='MIDQUERY' and IS_ACTIVE='是'";

			sSqls.add(sSQL);
			if (sSqls.size() > 0)
				QueryStub.getQueryTool().executeBatch(sSqls);

		} catch (Exception ex) {
			infopackage.setsMessage(ex.getMessage());
			infopackage.setSuccess(false);
			return infopackage;
		}
		infopackage.setSuccess(true);
		return infopackage;

	}

	public DataSet getReportInfo(String verNo) throws Exception {
		DataSet ds = DataSet.create();
		String sSQL = "";
		sSQL = "select a.report_id,a.report_cname ,b.sqllines from "
				+ " fb_u_qr_repset a,fb_u_qr_sqllines b where a.data_user in (1,2) "
				+ " and a.report_id=b.report_id and b.sqltype='MIDQUERY' and IS_ACTIVE='是'";
		DBSqlExec.getDataSet(sSQL, ds);
		return ds;

	}

	// 创建报表版本物理表
	public InfoPackage CreatTabelReportVer(String verNo, String reportId,
			String sqllines) throws Exception {
		InfoPackage infopackage = new InfoPackage();
		try {
			String sSQL = "";
			String tablename = "FB_MV_" + reportId + "_" + verNo;
			sSQL = "create table " + tablename + " as " + sqllines;
			QueryStub.getQueryTool().executeUpdate(sSQL);
		} catch (Exception ex) {
			infopackage.setsMessage(ex.getMessage());
			infopackage.setSuccess(false);
			return infopackage;
		}
		infopackage.setSuccess(true);
		return infopackage;
	}

	// 已上报、未上报单位情况
	public DataSet getSendEnterprise(String sendFlag) throws Exception {
		DataSet ds = DataSet.create();
		String sSQL = "";
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		if (sendFlag.equals(IQrBudget.SEND))
			sSQL = "select chr_code,chr_name  from ele_enterprise a"
					+ " where exists (select decode(div_code,null,send_div_code,div_code) chr_code"
					+ " from fb_s_trandetail b"
					+ " where (decode(div_code,null,send_div_code,div_code)=a.chr_code or instr(a.chr_code,b.send_div_code)=1"
					+ " or instr(b.send_div_code,a.chr_code)=1)"
					+ " and a.set_year=b.set_year and TRANSMIT_FLAG = 0 ) and a.enabled = 1 and a.set_year ="
					+ setYear + " order by chr_code";
		else if (sendFlag.equals(IQrBudget.UNSEND))
			sSQL = "select chr_code,chr_name from ele_enterprise a where   a.enabled = 1   and a.set_year = "
					+ setYear
					+ "  minus select chr_code,chr_name  from ele_enterprise a where exists"
					+ "  (select decode(div_code,null,send_div_code,div_code) chr_code"
					+ " from fb_s_trandetail b where (decode(div_code,null,send_div_code,div_code)=a.chr_code or "
					+ " instr(a.chr_code,b.send_div_code)=1 or instr(b.send_div_code,a.chr_code)=1)"
					+ "  and a.set_year=b.set_year and  TRANSMIT_FLAG = 0) and a.enabled = 1 and a.set_year = "
					+ setYear + " order by chr_code";
		else
			sSQL = "select chr_code,chr_name from ele_enterprise where 1=2";
		DBSqlExec.getDataSet(sSQL, ds);
		return ds;
	}

	// 查询条件
	public DataSet getCondition() throws Exception {
		DataSet ds = DataSet.create();
		ds.append();
		ds.fieldByName("code").setValue("");
		ds.fieldByName("name").setValue("请选择");
		ds.append();
		ds.fieldByName("code").setValue(IQrBudget.UNSEND);
		ds.fieldByName("name").setValue("未上报");
		ds.append();
		ds.fieldByName("code").setValue(IQrBudget.SEND);
		ds.fieldByName("name").setValue("已上报");
		return ds;
	}

	// 单位报表表头
	public DataSet getEnterpriseReportHeader() throws Exception {
		DataSet ds = DataSet.create();
		ds.append();
		ds.fieldByName("id").setValue("001");
		ds.fieldByName("code").setValue("chr_code");
		ds.fieldByName("name").setValue("单位编码");
		ds.append();
		ds.fieldByName("id").setValue("002");
		ds.fieldByName("code").setValue("chr_name");
		ds.fieldByName("name").setValue("单位名称");
		return ds;
	}

	public List getSqlLines(String reportID, String setYear, int iLoginMode)
			throws SQLException, IOException {
		String sSql = "select SQLTYPE,VIEWNAME,SQLLINES,SQLTREAM from FB_U_QR_SQLLINES where report_id='"
				+ reportID + "' ";

		List lstResult = new ArrayList();

		Session session = null;
		try {
			session = dao.getSession();
			Connection con = session.connection();
			PreparedStatement smt = con.prepareStatement(sSql);
			ResultSet rs = smt.executeQuery();

			String sqlTream;
			java.sql.Clob osc = null;
			XMLData data;
			Reader inStream;
			char[] c;
			while (rs.next()) {
				data = new XMLData();
				if (iLoginMode == 1) {// iLoginmode == 0，离线登陆方式
					sqlTream = rs.getString("SQLTREAM");
				} else {

					// 下2行是用于从clob类型里读数据的，转成字符串。
					osc = rs.getClob("SQLTREAM");
					// subString是截取字符串（从1截到length），如果用 osc.getString的话出错。
					inStream = osc.getCharacterStream();
					c = new char[(int) osc.length()];
					inStream.read(c);
					// data是读出并需要返回的数据，类型是String
					sqlTream = new String(c);
					inStream.close();
				}

				// sqlTream = osc.getSubString((long) 1, (int) osc.length());

				data.put("SQLTYPE", rs.getString("SQLTYPE"));
				data.put("VIEWNAME", rs.getString("VIEWNAME"));
				data.put("SQLLINES", rs.getString("SQLLINES"));
				data.put("SQLTREAM", sqlTream);
				lstResult.add(data);
			}
			rs.close();
			smt.close();
		} finally {
			dao.closeSession(session);
		}
		return lstResult;
	}

	/**
	 * 查询数据，并返回数据和查询语句类,注返回的字段名都是小写
	 * 
	 * @param sVerNo
	 *            版本号
	 * @param sBatchNoFilter
	 *            批次
	 * @param lstDept
	 *            部门条件
	 * @param lstDiv
	 *            单位条件
	 * @param setYear
	 *            年度
	 * @param iUserType
	 *            用户类型
	 * @param iLoginmode
	 *            登录方式
	 * @param iTypeFlag
	 *            报表类型
	 * @param reportInfo
	 *            报表信息
	 * @return List中第一个为数据LIST，第二个为一个OriSearchObj，以备导出
	 * @throws Exception
	 */

	public List getOriData(String sVerNo, String sBatchNoFilter, List lstDept,
			List lstDiv, String setYear, int iUserType, int iLoginmode,
			int iTypeFlag, ReportInfo reportInfo, ConditionObj conditionObj)
			throws Exception {
		OriSearchObj oriSearchObj = getSearchObj(sVerNo, sBatchNoFilter,
				lstDept, lstDiv, setYear, iUserType, iLoginmode, iTypeFlag,
				reportInfo, conditionObj);
		if (oriSearchObj == null)
			throw new Exception("生成查询体失败");
		String sErr = oriSearchObj.check();// 检查查询的准备是否正确
		if (!sErr.equals(""))
			throw new Exception(sErr);
		try {
			dao.executeBySql(oriSearchObj.getCreateTableSql());// 生成临时表
			QueryStub.getQueryTool().executeBatch(
					oriSearchObj.getLstInsertSql());

			List lstData = dao.findBySql(oriSearchObj.getSearchSql());
			List lstResult = new ArrayList();
			lstResult.add(lstData);
			lstResult.add(oriSearchObj);
			return lstResult;
		} catch (Exception e) {
			// 输出调试语句到文件
			FbLogger.fileLogger().error(
					"--查询表" + reportInfo.getReportID() + reportInfo.getTitle()
							+ "   " + e.getMessage() + " start --");
			FbLogger.fileLogger().error(oriSearchObj.getCreateTableSql());
			List lstInsertSql = oriSearchObj.getLstInsertSql();
			for (Iterator itVer = lstInsertSql.iterator(); itVer.hasNext();) {
				String sSqlTmp = (String) itVer.next();
				FbLogger.fileLogger().error(sSqlTmp);
			}
			FbLogger.fileLogger().error(oriSearchObj.getSearchSql());
			FbLogger.fileLogger().error(oriSearchObj.getDropTableSql());
			FbLogger.fileLogger().error(
					"--查询表" + reportInfo.getReportID() + reportInfo.getTitle()
							+ " end --");

			dao.executeBySql(oriSearchObj.getDropTableSql());
			throw e;
		} finally {
			try {
				dao.executeBySql(oriSearchObj.getDropTableSql());
			} catch (Exception e1) {
			}
		}

	}

	/**
	 * 查询数据根据查询语句，并返回数据
	 */
	public List getOriDataWhere(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			List lstSqlLines, ConditionObj conditionObj) throws Exception {
		OriSearchObj oriSearchObj = this.getSearchObjWhere(sVerNo,
				sBatchNoFilter, lstDept, lstDiv, setYear, iUserType,
				iLoginmode, iTypeFlag, reportInfo, lstSqlLines, conditionObj);
		if (oriSearchObj == null)
			throw new Exception("生成查询体失败");
		String sErr = oriSearchObj.check();// 检查查询的准备是否正确
		if (!sErr.equals(""))
			throw new Exception(sErr);
		try {
			dao.executeBySql(oriSearchObj.getCreateTableSql());// 生成临时表
			QueryStub.getQueryTool().executeBatch(
					oriSearchObj.getLstInsertSql());

			List lstData = dao.findBySql(oriSearchObj.getSearchSql());
			List lstResult = new ArrayList();
			lstResult.add(lstData);
			lstResult.add(oriSearchObj);
			return lstResult;
		} catch (Exception e) {
			dao.executeBySql(oriSearchObj.getDropTableSql());
			throw e;
		} finally {
			try {
				dao.executeBySql(oriSearchObj.getDropTableSql());
			} catch (Exception e1) {
			}
		}
	}

	/**
	 * 取得查询体
	 * 
	 * @param sVerNo
	 * @param sBatchNoFilter
	 * @param lstDept
	 * @param lstDiv
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @param iTypeFlag
	 * @param reportInfo
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getSearchObj(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			ConditionObj conditionObj) throws Exception {
		OriSearch oriSearch = new OriSearch(reportInfo, this.dao);
		OriSearchObj oriSearchObj = oriSearch.getReportExecuteSql(sVerNo,
				sBatchNoFilter, lstDept, lstDiv, setYear, iUserType,
				iLoginmode, iTypeFlag, conditionObj);
		return oriSearchObj;

	}

	/**
	 * 取得查询体根据sql列表
	 * 
	 * @param sVerNo
	 * @param sBatchNoFilter
	 * @param lstDept
	 * @param lstDiv
	 * @param setYear
	 * @param iUserType
	 * @param iLoginmode
	 * @param iTypeFlag
	 * @param reportInfo
	 * @param lstSqlLines
	 * @return
	 * @throws Exception
	 */
	public OriSearchObj getSearchObjWhere(String sVerNo, String sBatchNoFilter,
			List lstDept, List lstDiv, String setYear, int iUserType,
			int iLoginmode, int iTypeFlag, ReportInfo reportInfo,
			List lstSqlLines, ConditionObj conditionObj) throws Exception {

		OriSearch oriSearch = new OriSearch(reportInfo, this.dao);
		OriSearchObj oriSearchObj = oriSearch.getReportExecuteSql_A(sVerNo,
				sBatchNoFilter, lstDept, lstDiv, setYear, iUserType,
				iLoginmode, iTypeFlag, lstSqlLines, conditionObj);
		return oriSearchObj;

	}

	/**
	 * 得到报表报头(包含隐藏列)
	 * 
	 * @throws Exception
	 */
	public DataSet getReportHeader_A(String sReportId) throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sSql = "select distinct Field_Id,FIELD_ENAME,IS_LEAF,FIELD_CODE "
				+ " from fb_u_qr_colset where report_id = '"
				+ sReportId
				+ "'   " + " order by FIELD_CODE ";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 得到需执行脚本
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getScriptInfo(int iLoginMode) throws SQLException, IOException {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sSql = "select BIG_SCRIPT from fb_u_qr_script  "
				+ "  order by lvl_id ";

		List lstResult = new ArrayList();

		Session session = null;
		try {
			session = dao.getSession();
			Connection con = session.connection();
			PreparedStatement smt = con.prepareStatement(sSql);
			ResultSet rs = smt.executeQuery();

			String sqlTream;
			java.sql.Clob osc = null;
			Reader inStream;
			char[] c;
			while (rs.next()) {
				if (iLoginMode == 1) {// iLoginmode == 0，离线登陆方式
					sqlTream = rs.getString("BIG_SCRIPT");
				} else {

					// 下2行是用于从clob类型里读数据的，转成字符串。
					osc = rs.getClob("BIG_SCRIPT");
					// subString是截取字符串（从1截到length），如果用 osc.getString的话出错。
					inStream = osc.getCharacterStream();
					c = new char[(int) osc.length()];
					inStream.read(c);
					// data是读出并需要返回的数据，类型是String
					sqlTream = new String(c);
					inStream.close();
				}

				// sqlTream = osc.getSubString((long) 1, (int) osc.length());
				lstResult.add(sqlTream);
			}
			rs.close();
			smt.close();
		} finally {
			dao.closeSession(session);
		}
		return lstResult;

	}

	/**
	 * 执行脚本
	 * 
	 * @throws Exception
	 */
	public void exeScript(String sSql) throws Exception {
		String sSqlArray[] = sSql.split(";");
		if (sSqlArray == null)
			return;
		int size = sSqlArray.length;
		if (size == 0)
			return;
		List lstSql = new ArrayList();
		for (int i = 0; i < size; i++) {
			lstSql.add(sSqlArray[i]);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 得到单位控制数值
	 */
	public List getDivRae(String sDivCode) {
		String sSql = "select f38,PAYOUT_KIND_CODE from fb_u_payout_budget_rae where div_code =? and set_year=?";
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		return dao.findBySql(sSql, new Object[] { sDivCode, setYear });
	}

	/**
	 * 部门预算二下表---2009年支出预算数下达书,用于基本支出
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownJBZC(String divCode, int batchNo, int dataType)
			throws Exception {
		// 增加财政拨款（专项资金）这项纳入预算管理的行政事业性收费、罚没收入、维护费三项和
		String sSql = "select div_code,div_name, '基本支出' as prj_name,"
				+ "sum(isnull(CZBK, 0)) - sum(isnull(CZBK_ZX, 0)) + "
				+ "sum(isnull(F4, 0)) + sum(isnull(F6, 0)) + sum(isnull(F37, 0))+sum(CZBK_ZX_BF) as hj,"
				+ "sum(isnull(CZBK, 0)) - sum(isnull(CZBK_ZX, 0)) as czfzxzc,"
				+ "sum(isnull(F4, 0)) as yswzc,sum(isnull(F6, 0)) + sum(isnull(F37, 0)) as qtzc,"
				+ "sum(isnull(czbk_zx_bf,0)) as czbk_zx_bf"
				+ " from  ("
				+ " select x.DIV_CODE, x.DIV_NAME, x.BATCH_NO, x.DATA_TYPE,"
				+ "CZBK,CZBK_ZX, 0 as F4,0 as F6, 0 as F37,isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0) as CZBK_ZX_BF"
				+ " from VExp_Payout_Ysn x" + " union all"
				+ " select y.div_code,y.div_name, y.batch_no,y.data_type,"
				+ " 0 as czbk,0 as czbk_zx,F4,F6,F37,0"
				+ " from FB_U_PAYOUT_BUDGET y)" + " where DIV_CODE ='"
				+ divCode + "' and BATCH_NO=" + batchNo + " and DATA_TYPE="
				+ dataType + " GROUP BY DIV_CODE, DIV_NAME";

		// String sSql = "select x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE, "
		// + " '基本支出' as prj_name,"
		// + "
		// sum(isnull(CZBK,0))-sum(isnull(CZBK_ZX,0))+sum(isnull(y.F4,0))+sum(isnull(y.F6,0))
		// +sum(isnull(y.F37,0)) as hj, "
		// + // 合计
		// " sum(isnull(CZBK,0))-sum(isnull(CZBK_ZX,0)) as czfzxzc, "
		// + // 财政拨款(不含专项)
		// " sum(isnull(y.F4,0)) as yswzc,"
		// + // 预算外资金（控制数）
		// " sum(isnull(y.F6,0))+sum(isnull(y.F37,0)) as qtzc "
		// + // 其他资金
		// " from VExp_Payout_Ysn x ,FB_U_PAYOUT_BUDGET y "
		// + " WHERE x.DIV_CODE=y.DIV_CODE AND x.BATCH_NO=y.BATCH_NO AND
		// x.DATA_TYPE=y.DATA_TYPE"
		// + " AND x.DIV_CODE='" + divCode + "' AND x.BATCH_NO="
		// + batchNo + " and x.DATA_TYPE=" + dataType
		// + " GROUP BY x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE ";
		// " select x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,"+
		// " '基本支出' as prj_name," +
		// "
		// sum(isnull(CZBK,0))-sum(isnull(CZBK_ZX,0))+sum(isnull(F4,0))+sum(isnull(F6,0))
		// +sum(isnull(F37,0)) as hj,"+//合计
		// " sum(isnull(CZBK,0))-sum(isnull(CZBK_ZX,0)) as czfzxzc,"
		// +//财政拨款(不含专项)
		// " sum(isnull(F4,0)) as yswzc," +//预算外资金（控制数） VExp_Payout_Ysw
		// " sum(isnull(F6,0))+sum(isnull(F37,0)) as qtzc"+//其他资金
		// " from VExp_Payout_Ysn x "+
		// " where x.DIV_CODE ='"+divCode+"' and x.BATCH_NO="+batchNo+" and
		// x.DATA_TYPE="+dataType+
		// " GROUP BY x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE";

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		ds.beforeFirst();
		Object[] prjInfo = new Object[6];
		if (ds.next()) {
			prjInfo[0] = ds.fieldByName("prj_name").getValue();
			prjInfo[1] = ds.fieldByName("hj").getValue();
			prjInfo[2] = ds.fieldByName("czfzxzc").getValue();
			prjInfo[3] = ds.fieldByName("czbk_zx_bf").getValue();
			prjInfo[4] = ds.fieldByName("yswzc").getValue();
			prjInfo[5] = ds.fieldByName("qtzc").getValue();

		}
		return prjInfo;
	}

	/**
	 * 部门预算二下表---2009年支出预算数下达书,用于专项明细
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownPrjDetail(String prjType, String divCode,
			int batchNo, int dataType, boolean isPrjGovJj) throws Exception {
		String sSql;
		if (isPrjGovJj) {
			sSql = " SELECT  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
					+ " ,'经常性项目' AS prj_name,sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj ,"
					+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc, sum(isnull(F58,0)) as CZBK_ZX_BF"
					+ " from VW_FB_QR_P_BUDGET x"
					+ " where prj_type = '"
					+ prjType
					+ "' "
					+ " and x.DIV_CODE ='"
					+ divCode
					+ "' and x.BATCH_NO="
					+ batchNo
					+ " and x.DATA_TYPE="
					+ dataType
					+ " GROUP BY x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
					+

					" union all select * from ("
					+ " select  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,"
					+ " '   '||x.prj_Name prj_Name,sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj,"
					+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc, sum(isnull(F58,0)) as CZBK_ZX_BF"
					+ " from VW_FB_QR_P_BUDGET x "
					+ " where prj_type = '"
					+ prjType
					+ "' "
					+ " and x.DIV_CODE ='"
					+ divCode
					+ "' and x.BATCH_NO="
					+ batchNo
					+ " and x.DATA_TYPE="
					+ dataType
					+ " group by x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,x.prj_code,x.prj_Name order by x.prj_code)";
		} else {
			// modiyf by zlx 2009-01-19 增加 order by prj_code排序
			sSql = " SELECT  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
					+ " ,'经常性项目' AS prj_name,sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj ,"
					+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc,"
					+ "  sum(isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) as CZBK_ZX_BF"
					+ " from VW_FB_QR_P_BUDGET x"
					+ " where prj_type = '"
					+ prjType
					+ "' "
					+ " and x.DIV_CODE ='"
					+ divCode
					+ "' and x.BATCH_NO="
					+ batchNo
					+ " and x.DATA_TYPE="
					+ dataType
					+ " GROUP BY x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
					+

					" union all select * from ("
					+ " select  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,"
					+ " '   '||x.prj_Name prj_Name,sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj,"
					+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc, "
					+ "  sum(isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) as CZBK_ZX_BF"
					+ " from VW_FB_QR_P_BUDGET x "
					+ " where prj_type = '"
					+ prjType
					+ "' "
					+ " and x.DIV_CODE ='"
					+ divCode
					+ "' and x.BATCH_NO="
					+ batchNo
					+ " and x.DATA_TYPE="
					+ dataType
					+ " group by x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,x.prj_code,x.prj_Name order by x.prj_code)";
		}

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		ds.beforeFirst();
		ArrayList alValue = new ArrayList();
		while (ds.next()) {
			Object[] prjInfo = new Object[6];
			prjInfo[0] = ds.fieldByName("prj_name").getValue();
			prjInfo[1] = ds.fieldByName("hj").getValue();
			prjInfo[2] = ds.fieldByName("czfzxzc").getValue();
			prjInfo[3] = ds.fieldByName("czbk_zx_bf").getValue();
			prjInfo[4] = ds.fieldByName("yswzc").getValue();
			prjInfo[5] = ds.fieldByName("qtzc").getValue();

			alValue.add(prjInfo);
		}

		Object[] prj = new Object[alValue.size()];
		for (int i = 0; i < alValue.size(); i++) {
			prj[i] = (Object[]) alValue.get(i);
		}
		return prj;
	}

	/**
	 * 部门预算二下表---2009年政府采购预算通知书
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDownGov(String divCode, int batchNo, int dataType,
			boolean isPrjGovJj) throws Exception {

		String sSql;
		if (isPrjGovJj) {
			sSql = "select * from ("
					+ " select ' ' as xh,div_code,div_name,t.BATCH_NO,t.DATA_TYPE,"
					+ "'' as  PROJECT_CODE,'合 计' as prj_name,"
					+ " '' as buy_code,'' buy_name,"
					+ "sum(isnull(t.F1, 0)) hj,sum(isnull(t.F2, 0)) czbk, sum(isnull(t.F3, 0)) czbk_zx,"
					+ "sum(isnull(t.F4, 0)) ysw,sum(isnull(t.f57, 0)) ysw_zx, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ " from FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code, div_name, batch_no, data_type"

					+ " union all"

					+ " select PROJECT_CODE as xh ,div_code,div_name,t.BATCH_NO,t.DATA_TYPE,"
					+ "PROJECT_CODE , substr(Max(PROJECT_NAME),instr(Max(PROJECT_NAME),'[',1,1)+1,instr(Max(PROJECT_NAME),']',1,1)-2) as prj_name,"
					+ "'' as buy_code,'' buy_name,"
					+ "sum(isnull(t.F1, 0)) f1,sum(isnull(t.F2, 0)) f2, sum(isnull(t.F3, 0)) f3,"
					+ "sum(isnull(t.F4, 0)) f4,sum(isnull(t.f57, 0)) f57, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ " from FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code,div_name,PROJECT_CODE,batch_no,data_type"

					+ " union all"

					+ " SELECT PROJECT_CODE||buy_code as xh,t.DIV_CODE, t.DIV_NAME, t.BATCH_NO,t.DATA_TYPE,"
					+ " '' as prj_code,'' as Prj_Name,"
					+ " t.buy_code,t.BUY_NAME,"
					+ "sum(isnull(t.F1, 0)) f1, sum(isnull(t.F2, 0)) f2, sum(isnull(t.F3, 0)) f3,"
					+ "sum(isnull(t.F4, 0)) f4, sum(isnull(t.f57, 0)) f57, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ "  FROM FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code,div_name,PROJECT_CODE,buy_code,buy_name,batch_no,data_type)"
					+ " WHERE DIV_CODE = '" + divCode + "'"
					+ " AND BATCH_NO = " + batchNo + " AND DATA_TYPE = "
					+ dataType + "  order by xh";
		} else {
			// modify by zlx 2011-01-19,先项目再采购明细
			sSql = "select * from ("
					+ " select ' ' as xh,div_code,div_name,t.BATCH_NO,t.DATA_TYPE,"
					+ "'' as  PROJECT_CODE,'合 计' as prj_name,"
					+ " '' as buy_code,'' buy_name,"
					+ "sum(isnull(t.F1, 0)) hj,sum(isnull(t.F2, 0)) czbk, sum(isnull(t.F3, 0)) czbk_zx,"
					+ "sum(isnull(t.F4, 0)) ysw,sum(isnull(t.F39, 0)) ysw_zx, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ " from FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code, div_name, batch_no, data_type"

					+ " union all"

					+ " select PROJECT_CODE as xh ,div_code,div_name,t.BATCH_NO,t.DATA_TYPE,"
					+ "PROJECT_CODE , substr(Max(PROJECT_NAME),instr(Max(PROJECT_NAME),'[',1,1)+1,instr(Max(PROJECT_NAME),']',1,1)-2) as prj_name,"
					+ "'' as buy_code,'' buy_name,"
					+ "sum(isnull(t.F1, 0)) f1,sum(isnull(t.F2, 0)) f2, sum(isnull(t.F3, 0)) f3,"
					+ "sum(isnull(t.F4, 0)) f4,sum(isnull(t.F39, 0)) f39, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ " from FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code,div_name,PROJECT_CODE,batch_no,data_type"

					+ " union all"

					+ " SELECT PROJECT_CODE||buy_code as xh,t.DIV_CODE, t.DIV_NAME, t.BATCH_NO,t.DATA_TYPE,"
					+ " '' as prj_code,'' as Prj_Name,"
					+ " t.buy_code,t.BUY_NAME,"
					+ "sum(isnull(t.F1, 0)) f1, sum(isnull(t.F2, 0)) f2, sum(isnull(t.F3, 0)) f3,"
					+ "sum(isnull(t.F4, 0)) f4, sum(isnull(t.f39, 0)) f39, sum(isnull(t.F6, 0)+isnull(f37,0)) as qtzj"
					+ "  FROM FB_U_PAYOUT_GOV_PURCHASE t"
					+ " group by div_code,div_name,PROJECT_CODE,buy_code,buy_name,batch_no,data_type)"
					+ " WHERE DIV_CODE = '" + divCode + "'"
					+ " AND BATCH_NO = " + batchNo + " AND DATA_TYPE = "
					+ dataType + "  order by xh";
		}

		// String sSql = "SELECT t.DIV_CODE,t.DIV_NAME,t.BATCH_NO,t.DATA_TYPE,"
		// + " t.BUY_NAME,x.Prj_Name," + " isnull(t.F1,0) f1," + // 支出合计
		// " isnull(t.F2,0) f2," + // 资金来源.财政拨款.小计
		// " isnull(t.F3,0) f3," + // 资金来源.财政拨款.专项资金
		// " isnull(t.F4,0) f4," + // 资金来源.预算外资金.小计
		// " isnull(t.F5,0) f5," + // 资金来源.预算外资金.其中:专项资金
		// " isnull(t.F6,0) f6 " + // 资金来源.其他资金
		// " FROM FB_U_PAYOUT_GOV_PURCHASE t,VW_FB_QR_P_BUDGET x"
		// + " WHERE t.DIV_CODE='" + divCode + "'" + " AND t.BATCH_NO="
		// + batchNo + " AND t.DATA_TYPE=" + dataType
		// + " AND t.PROJECT_CODE=x.Prj_Code AND t.DIV_CODE=x.Div_Code"
		// + " AND t.BATCH_NO=x.Batch_No AND t.DATA_TYPE=x.Data_Type";

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 保存列宽(收支总表和封面目录）
	 * 
	 * @throws Exception
	 */
	public void saveSzzbColWidth(String sReportId, String setYear,
			List lstColWidth) throws Exception {
		List lstSql = new ArrayList();
		String sSql;
		for (int i = 0; i < lstColWidth.size(); i++) {
			sSql = " update fb_u_qr_szzb set FIELD_COLWIDTH = FIELD_COLWIDTH+"
					+ lstColWidth.get(i) + " where report_id = '" + sReportId
					+ "'  and FIELD_COLUMN= '" + i
					+ "'";
			lstSql.add(sSql);
		}
		QueryStub.getQueryTool().executeBatch(lstSql);
	}

	/**
	 * 部门预算二下表---2010年收入预算数下达书
	 * 
	 * @throws Exception
	 */
	public DataSet getBudgetDown(String divCode, int batchNo, int dataType)
			throws Exception {
		String sSql = "select A.INCTYPE_CODE,A.INC_MONEY,b.is_sum,b.lvl_id "
				+ " from FB_U_DIV_INCOMING_BUDGET a left join fb_iae_inctype b"
				+ " on a.inctype_code = b.inctype_code "
				+ " where A.DIV_CODE ='" + divCode + "' and A.BATCH_NO="
				+ batchNo + " and A.DATA_TYPE=" + dataType;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);

		sSql = "select sum(isnull(F38,0)) as F38,sum(isnull(F7,0)) as F7,sum(isnull(F8,0)) as F8,sum(isnull(F9,0)) as F9,"
				+ "sum(isnull(F43,0)) as F43,sum(isnull(F42,0)) as F42"
				+ " from ("
				+ "select f38,f7,f8,f9,f43,f42  from VEXP_PAYOUT_YSN A where A.DIV_CODE ='"
				+ divCode
				+ "' and A.BATCH_NO="
				+ batchNo
				+ " and A.DATA_TYPE="
				+ dataType
				+ " union all"
				+ " select f38,f7,f8,f9,f43,f42  from VW_FB_QR_P_BUDGET A where A.DIV_CODE ='"
				+ divCode
				+ "' and A.BATCH_NO="
				+ batchNo
				+ " and A.DATA_TYPE="
				+ dataType
				+ " union all"
				+ " select f38,f7,f8,f9,f43,f42  from fb_u_ir_data A where  A.DIV_CODE ='"
				+ divCode
				+ "' and A.BATCH_NO="
				+ batchNo
				+ " and A.DATA_TYPE="
				+ dataType
				+ " and report_id in ('100002','100003','100004')"
				+ ")";
		DataSet dsTmp = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsTmp);

		dsTmp.beforeFirst();
		if (dsTmp.next()) {
			ds.edit();
			if (ds.locate(IIncType.INCTYPE_CODE, "004")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F38").getValue());
			}
			if (ds.locate(IIncType.INCTYPE_CODE, "050")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F7").getValue());
			}
			if (ds.locate(IIncType.INCTYPE_CODE, "051")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F43").getValue());
			}
			if (ds.locate(IIncType.INCTYPE_CODE, "026")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F8").getValue());
			}
			if (ds.locate(IIncType.INCTYPE_CODE, "027")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F9").getValue());
			}
			if (ds.locate(IIncType.INCTYPE_CODE, "049")) {
				ds.fieldByName("INC_MONEY").setValue(
						dsTmp.fieldByName("F42").getValue());
			}
			ds.applyUpdate();
		}

		return ds;
	}

	/**
	 * 补助支出
	 * 
	 * @throws Exception
	 */
	public Object[] getBudgetDownBz(String divCode, int batchNo, int dataType)
			throws Exception {
		String sSql = " SELECT  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
				+ " ,'上下级补助支出数' AS prj_name,sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj ,"
				+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc,"
				+ "  sum(isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) as CZBK_ZX_BF"
				+ " from fb_u_ir_data x"
				+ " where  x.DIV_CODE ='"
				+ divCode
				+ "' and x.BATCH_NO="
				+ batchNo
				+ " and x.DATA_TYPE="
				+ dataType
				+ " and  x.report_id in ('100002','100003','100004')"
				+ " GROUP BY x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE "
				+ " union all select * from ("
				+ " select  x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,"
				+ " '   '||case x.report_id when '100002' then '补助下属单位预算' when '100003' then '补助区县单位预算'"
				+ " when '100004' then '上缴主管部门预算' end as prj_Name,"
				+ " sum(isnull(F38,0)+isnull(F4,0)+isnull(F6,0)+isnull(F37,0)+isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) hj,"
				+ " sum(isnull(F38,0)) czfzxzc,sum(isnull(F4,0)) yswzc, sum(isnull(F6,0)+isnull(F37,0)) qtzc, "
				+ "  sum(isnull(F7,0)+ isnull(F8,0)+isnull(F9,0)+ isnull(F43,0)+isnull(F42,0)) as CZBK_ZX_BF"
				+ " from fb_u_ir_data x"
				+ " where  x.DIV_CODE ='"
				+ divCode
				+ "' and x.BATCH_NO="
				+ batchNo
				+ " and x.DATA_TYPE="
				+ dataType
				+ " and  x.report_id in ('100002','100003','100004')"
				+ " group by x.DIV_CODE,x.DIV_NAME,x.BATCH_NO,x.DATA_TYPE,x.report_id order by x.report_id)";

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		ds.beforeFirst();
		ArrayList alValue = new ArrayList();
		while (ds.next()) {
			Object[] prjInfo = new Object[6];
			prjInfo[0] = ds.fieldByName("prj_name").getValue();
			prjInfo[1] = ds.fieldByName("hj").getValue();
			prjInfo[2] = ds.fieldByName("czfzxzc").getValue();
			prjInfo[3] = ds.fieldByName("czbk_zx_bf").getValue();
			prjInfo[4] = ds.fieldByName("yswzc").getValue();
			prjInfo[5] = ds.fieldByName("qtzc").getValue();

			alValue.add(prjInfo);
		}

		Object[] prj = new Object[alValue.size()];
		for (int i = 0; i < alValue.size(); i++) {
			prj[i] = (Object[]) alValue.get(i);
		}
		return prj;
	}
}
