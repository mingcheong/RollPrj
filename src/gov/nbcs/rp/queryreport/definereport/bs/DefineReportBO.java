/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.bs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.szzbset.ui.SzzbSetI;

import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * <p>
 * Title:自定义表设置服务端实现类
 * </p>
 * <p>
 * Description:自定义表设置服务端实现类
 * </p>
 * <p>

 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
 * </p>
 * <p>
 * CreateDate 2011-7
 * </p>
 * 
 * @author WUYAL

 * @version 1.0
 */
public class DefineReportBO implements IDefineReport {

	// 声明GeneralDao对象
	GeneralDAO dao = null;

	/**
	 * @return Returns the dao.
	 */
	public GeneralDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            The dao to set.
	 */
	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * 得到新增报表可选种类
	 * 
	 * @return返回新增报表可选类型列表
	 */
	public List getReportSort() {
		String sql_database = "select * from fb_u_qr_reportType  order by lvl_id";
		return dao.findBySql(sql_database);
	}

	/**
	 * 得到报表类型信息
	 * 
	 * @throws Exception
	 * 
	 * 
	 * @return返回报表类型
	 */
	public DataSet getReportType() throws Exception {
		String sql_database = "select code,name,lvl_id,lvl_id||' '||name as code_name,c1 from FB_S_PUBCODE "
				+ "where typeid='QUERYTYPE' "
			
				+ " order by lvl_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sql_database, ds);
		return ds;
	}

	/**
	 * 得到数据源信息
	 * 
	 * @return 返回数据源信息列表
	 */
	public List getDataSource() {
		String sql_database = "select a.DICID,a.TABLECNAME as object_cname,a.TABLEENAME as object_ename,b.TYPE_NAME as object_class,"
				+ "ISBATCHNO,SUP_VER,a.YEAR_FLAG,a.COMPARE_DICID"
				+ " from FB_DICT_INFO a,FB_DICT_TYPE b "
				+ "where a.TABPROP=b.TYPE_CODE and a.ISINUSE =1 and a.issource=1 and a.set_year =?  "
				+ "order by b.type_code,a.lvl_id";
		return dao.findBySql(sql_database, new Object[] { SessionUtil
				.getUserInfoContext().getSetYear() });
	}

	/**
	 * 得到选中的数据源数
	 * 
	 * @param lstDataSource
	 *            选中的数据源信息
	 * @throws Exception
	 * @return返回选中的数据源详细信息
	 */
	public DataSet getDataSoureDetail(String sDataSource) throws Exception {
		String loginMode = SessionUtil.getUserInfoContext().getLoginMode();
		String sSql = null;
		if ("0".equals(loginMode)) {// 在线
			sSql = "select DICID, '' OBJECT_ENAME ,'' OBJECT_CNAME, tablecname as  FIELD_FNAME, "
					+ "tableename FIELD_ENAME,'' FIELD_TYPE,DICID as chr_id,'' parent_id"
					+ " from FB_DICT_INFO "
					+ "where "
					+ " DICID in ("
					+ sDataSource
					+ ")"
					+ " union all "
					+ "select a.DICID, b.tableename as OBJECT_ENAME, b.tablecname as OBJECT_CNAME,a.AFIELD_CNAME as FIELD_FNAME,"
					+ "a.AFIELD_ENAME as FIELD_ENAME, a.AFIELD_TYPE as FIELD_TYPE,a.dicid || lpad(a.afield_sort, 4, 0) as chr_id,a.dicid parent_id"
					+ " from FB_DICT_INFO_DETAIL a, FB_DICT_INFO b "
					+ "where a.dicid = b.dicid and a.set_year= b.set_year and "
					
					+ " a.DICID in ("
					+ sDataSource
					+ ") and a.AFIELD_CNAME<>'未命名字段'";
		} else {// 离线
			sSql = "select DICID, '' OBJECT_ENAME ,'' OBJECT_CNAME, tablecname as  FIELD_FNAME, "
					+ "tableename FIELD_ENAME,'' FIELD_TYPE,DICID as chr_id,'' parent_id"
					+ " from FB_DICT_INFO "
					+ "where  set_year = "
					+ SessionUtil.getUserInfoContext().getSetYear()
					+ " and DICID in ("
					+ sDataSource
					+ ")"
					+ " union all "
					+ "select a.DICID, b.tableename as OBJECT_ENAME, b.tablecname as OBJECT_CNAME,a.AFIELD_CNAME as FIELD_FNAME,"
					+ "a.AFIELD_ENAME as FIELD_ENAME, a.AFIELD_TYPE as FIELD_TYPE,a.dicid||replicate(0,4-length(afield_sort))||afield_sort as chr_id,a.dicid parent_id"
					+ " from FB_DICT_INFO_DETAIL a, FB_DICT_INFO b "
					+ "where a.dicid = b.dicid and a.set_year= b.set_year and a.set_year ="
					+ SessionUtil.getUserInfoContext().getSetYear()
					+ " and a.DICID in ("
					+ sDataSource
					+ ") and a.AFIELD_CNAME<>'未命名字段'";
		}

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 得到选中数据源的明细信息
	 * 
	 * @param sDataSource
	 *            选中的数据源信息
	 * @return返回选中数据源的明细信息
	 */
	public List getEnumWhere(String sDataSource) {
		String sSql = " select a.TABLEENAME as object_ename, a.TABLECNAME as object_cname,"
				+ "b.AFIELD_ENAME as field_ename,b.AFIELD_TYPE as field_type,b.refcol_id,b.refcol_name,b.CON_FIELDENAME  "
				+ " from FB_DICT_INFO a,fb_dict_info_detail b"
				+ " where a.DICID = b.DICID  and a.set_year = b.set_year"
				+ " and a.DICID in (?)"
				+ " and b.refcol_ID is not null and a.set_year=?";
		return dao.findBySql(sSql, new Object[] { sDataSource,
				SessionUtil.getUserInfoContext().getSetYear() });
	}

	/**
	 * 根据选中的数据源，得到需要使用的枚举源信息
	 * 
	 * @param sDataSource
	 * @return
	 */
	public List getEnumInfo(String sDataSource) {
		String sSql = "select  *  from fb_s_refcol "
				+ "where refcol_id in (select distinct refcol_id from fb_dict_info_detail where DICID in ("
				+ sDataSource + ") and set_year = "
				+ SessionUtil.getUserInfoContext().getSetYear() + " )";

		return dao.findBySql(sSql);
	}

	/**
	 * 得到报表信息
	 * 
	 * @return 返回报表信息
	 * @throws Exception
	 */
	public DataSet getReport() throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		boolean type ="002".equals(SessionUtil.getUserInfoContext().getOrgType());
		String sSql = "select c1,code show_lvl, name as report_cname, '' as title, '' as IS_MULTICOND,  0 as is_end,"
				+ " 0 as type_flag, '' as CURRENCYUNIT, '' IS_ACTIVE,2 DATA_USER,"
				+ " lvl_id , '' par_id,code as report_id,'' COLUMN_AREA,'' FUNDSOURCE_FLAG,'' COMPARE_FLAG"
				+ " from fb_s_pubcode tab where typeid = 'QUERYTYPE' "
				
//				+ " union all"
//				+ " select a.report_id||c.type_code show_lvl, a.report_cname, a.title,a.IS_MULTICOND, a.is_end , "
//				+ "a.type_flag, a.CURRENCYUNIT,a.IS_ACTIVE, a.DATA_USER,"
//				+ " a.lvl_id , c.type_code par_id,a.report_id,COLUMN_AREA,a.FUNDSOURCE_FLAG,a.COMPARE_FLAG"
//				+ " from fb_u_qr_repset a,fb_s_pubcode b,fb_u_qr_report_to_type c"
//				+ " where a.report_id=c.report_id and c.type_code =b.code "
//				+ " and a.set_year = b.set_year and a.set_year =c.set_year "
//				+ " and a.set_year = " + setYear + " and b.typeid = 'QUERYTYPE'"
//				+ " and a.c1<>1 "
				+ " union all"
				+ " select b.c1,a.report_id||c.type_code show_lvl, a.report_cname, a.title,a.IS_MULTICOND, a.is_end , "
				+ "a.type_flag, a.CURRENCYUNIT,a.IS_ACTIVE, a.DATA_USER,"
				+ " a.lvl_id , c.type_code par_id,a.report_id,COLUMN_AREA,a.FUNDSOURCE_FLAG,a.COMPARE_FLAG"
				+ " from fb_u_qr_repset a,fb_s_pubcode b,fb_u_qr_report_to_type c " ;
		if(type)
				sSql+= 	",fb_u_usertoreport d";
		sSql+= " where a.report_id=c.report_id   and c.type_code =b.code "
				+ ""
				+ " and b.typeid = 'QUERYTYPE'   ";
		if(type)
			sSql+= "and a.report_id=d.report_id and d.user_id='"+SessionUtil.getUserInfoContext().getUserID()+"'";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 根据数据源EName值取得字段详细信息
	 * 
	 * @param sObjectEName
	 *            数据源表或视图名
	 * @return
	 */
	public List getFieldWithEname(String sObjectEName) {
		String sSql = " select AFIELD_ENAME  as field_ename,AFIELD_CNAME as field_fname,AFIELD_TYPE as FIELD_TYPE,refcol_id "
				+ " from FB_DICT_INFO_DETAIL where DICID=?  and set_year=? order by AFIELD_SORT";
		return dao.findBySql(sSql, new Object[] { sObjectEName,
				SessionUtil.getUserInfoContext().getSetYear() });
	}

	/**
	 * 得到引用列主键编码
	 * 
	 * @return
	 */
	public List getRefColPriCode() {
		String sSql = " select refcol_id, lvl_field from fb_s_refcol where set_year=?";
		return dao.findBySql(sSql, new Object[] { SessionUtil
				.getUserInfoContext().getSetYear() });
	}

	/**
	 * 根据enumId得到引用列信息
	 * 
	 * @param sEnumID
	 *            ，枚举ID
	 * @return
	 * @throws Exception
	 */
	public Map getEnumDataWithEnumID(String sEnumID) throws Exception {
		String sSql = "select SQL_DET,REFCOL_NAME,NAME_FIELD,LVL_FIELD,LVL_STYLE,PRIMARY_FIELD,PRIMARY_TYPE"
				+ " from fb_s_refcol where REFCOL_ID =? ";
		List lstRefCol = dao.findBySql(sSql, new Object[] { sEnumID });

		if (lstRefCol == null || lstRefCol.size() == 0)
			return null;

		Map refColMap = (Map) lstRefCol.get(0);
		// SQL
		String sqlDet = setSql(refColMap.get("sql_det").toString()
				.toUpperCase(), refColMap);
		// 得到DataSet
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sqlDet, ds);

		Map tempMap = new HashMap();
		tempMap.putAll(refColMap);
		tempMap.put(IDefineReport.ENUM_DATA, ds);

		return tempMap;
	}

	/**
	 * 处理传入的Sql,去掉年份条件和排序
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	private String setSql(String sSql, Map map) throws Exception {
		// // 去掉排序
		// int iLen = sSql.toUpperCase().indexOf("ORDER");
		// if (iLen != -1)
		// sSql = sSql.substring(0, iLen);

		String value = "select a.*,'['||" + map.get("lvl_field").toString()
				+ "||']'||" + map.get("name_field").toString()
				+ " as name from ";

		// 去掉年份条件
//		sSql =  .getServerMethod().replaceRefColFixFlag(sSql);

		// // 判断where是否有查询条件
		// int index = sSql.toUpperCase().indexOf("WHERE");
		// iLen = index + "WHERE".length();
		// String sSqlTmp = sSql.substring(iLen).trim();
		// if ("".equals(sSqlTmp)) {
		// sSql = sSql.substring(0, index);
		// } else {// 有多个查询条件,去掉and
		//
		// }
		// index = sSql.toUpperCase().indexOf("FROM") - 1;

		sSql = value + "(" + sSql + ") a";

		return sSql;
	}

	public List getDataSourceRefString(String dsIDs) {
		String sSql = " select a.DICID,a.TABLE_ENAME as object_ename,b.TABLECNAME object_cname,"
				+ "a.AFIELD_ENAME as field_ename,a.AFIELD_CNAME as field_fname,a.AFIELD_TYPE as field_type"
				+ " from fb_dict_info_detail a,fb_dict_info b"
				+ " where a.DICID=b.DICID and a.DICID in ("
				+ dsIDs
				+ ")  order by a.dicid,a.afield_sort";
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0) {
			return null;
		}
		int iCount = lstData.size();
		XMLData resultFieldToName = new XMLData();
		XMLData resultNameToField = new XMLData();

		XMLData aData;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstData.get(i);
			String objectName = aData.get("dicid").toString();
			Object obj = resultFieldToName.get(objectName);
			if (obj == null) {
				obj = new XMLData();
				resultFieldToName.put(objectName, obj);

			}
			((XMLData) obj).put(aData.get("field_ename"), aData
					.get("field_fname"));
			resultNameToField.put(""
					+ aData.get("object_cname").toString().toLowerCase() + "."
					+ aData.get("field_fname").toString().toLowerCase(), ""
					+ aData.get("dicid") + "," + aData.get("field_ename") + ","
					+ aData.get("field_type"));

		}
		List lstResult = new ArrayList();
		lstResult.add(resultFieldToName);
		lstResult.add(resultNameToField);

		return lstResult;
	}

	/**
	 * 保存报表
	 * 
	 * @param setYear
	 *            年份
	 * @param blobByte
	 * @param repSetObject
	 *            报表主表对象
	 * @param sOldReportId
	 *            报表主表对象
	 * @param dsHeader
	 *            表头
	 * @param lstSqlLines
	 *            报表查询语句
	 * @param lstType
	 *            报表类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public int saveReportFile(byte[] blobByte, RepSetObject repSetObject,
			String sOldReportId, DataSet dsHeader, List lstSqlLines,
			List lstType) throws Exception {

		// 得到登陆年份
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		String loginmode = SessionUtil.getUserInfoContext().getLoginMode();

		String sSqlTmp = null;
		String reportID = repSetObject.getREPORT_ID();
		int reportType = repSetObject.getREPORT_TYPE();
		String reportCName = repSetObject.getREPORT_CNAME();
		String isActive = repSetObject.getIS_ACTIVE();
		String lvlID = repSetObject.getLVL_ID();
		int typeFlag = repSetObject.getTYPE_FLAG();
		int dataUser = repSetObject.getDATA_USER();
		String fundsourceFlag = repSetObject.getFUNDSOURCE_FLAG();
		String compareFlag = repSetObject.getCOMPARE_FLAG();
		String sCurrencyUnit = repSetObject.getCURRENCYUNIT();
		// 判断是增加还是修改
		if (sOldReportId == null || "".equals(sOldReportId)) { // 增加
			sSqlTmp = "insert into fb_u_qr_repset"
					+ " (set_year, type_no, report_id, report_type, report_cname,title,  "
					+ "report_source, is_passverify, is_active, data_user, is_end,lvl_id, rg_code,  type_flag ,"
					+ "IS_HASBATCH,IS_MULTICOND,CURRENCYUNIT,FUNDSOURCE_FLAG,COMPARE_FLAG"
					+ ")" + " values" + " (" + setYear + ", 1, '" + reportID
					+ "', " + reportType + ", '" + reportCName + "','"
					+ reportCName + "','定制','是','" + isActive + "'," + dataUser
					+ ",1,'" + lvlID + "','" + sRgCode + "'," + typeFlag
					+ ",'是','是','" + sCurrencyUnit + "','" + fundsourceFlag
					+ "','" + compareFlag + "')";

		} else {// 修改
			sSqlTmp = "update fb_u_qr_repset" + " set set_year = " + setYear
					+ "," + " type_no =1," + " report_id = '" + reportID + "',"
					+ " report_type = '" + reportType + "',"
					+ " report_cname = '" + reportCName + "'," + " title = '"
					+ reportCName + "'," + " report_source = '定制',"
					+ " is_passverify = '是'," + " is_active = '" + isActive
					+ "'," + " data_user = " + dataUser + ","
					+ " is_end = 1,lvl_id ='" + lvlID + "'," + " rg_code = '"
					+ sRgCode + "'," + " type_flag =" + typeFlag
					+ ",CURRENCYUNIT='" + sCurrencyUnit
					+ "',FUNDSOURCE_FLAG ='" + fundsourceFlag
					+ "',COMPARE_FLAG='" + compareFlag + "' where report_id = '" + sOldReportId + "'";
		}
		dao.executeBySql(sSqlTmp);

		if (sOldReportId != null && !"".equals(sOldReportId)) { // 修改
			dao
					.executeBySql("delete from FB_U_QR_GROUPREPORT where REPORT_ID= '"
							+ reportID + "' ");
			dao.executeBySql("delete from fb_u_qr_colset where REPORT_ID= '"
					+ reportID + "' ");
			dao.executeBySql("delete from fb_u_qr_sqllines where REPORT_ID= '"
					+ reportID + "' ");
			dao
					.executeBySql("delete from fb_u_qr_report_to_type where REPORT_ID= '"
							+ reportID + "' ");
		}
		// 保存fb_
		dao.executeBySql("delete from fb_u_usertoreport where REPORT_ID='"+reportID+"' and user_id='"+SessionUtil.getUserInfoContext().getUserID()+"' and set_year="+setYear+"");
		
//        dao.executeBySql("insert into fb_u_usertoreport (LAST_VER,RG_CODE,USER_ID,REPORT_ID,SET_YEAR,REPORT_TYPE)values('','"+
//				sRgCode+"','"+ SessionUtil.getUserInfoContext().getUserID()+"','"+reportID+"','"+setYear+"','"+reportType+"')");
		// 保存fb_u_qr_report_to_type
		if (lstType != null) {
			int size = lstType.size();
			for (int i = 0; i < size; i++) {
				dao
						.executeBySql(
								"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
								new Object[] { reportID, lstType.get(i),
										setYear, sRgCode });
				// add by ymq 增加用户对报表
				sSqlTmp = " insert into  fb_u_usertoreport(RG_CODE,user_id,report_id,set_year,report_type)"
							+ " values ('"+sRgCode+"','"
							+ SessionUtil.getUserInfoContext().getUserID()
							+ "','"
							+ reportID
							+ "',"
							+ setYear
							+ ",'"
							+ lstType.get(i) + "')";
					dao.executeBySql(sSqlTmp);
				
			}
		}

		// 修改报表处理用户对报表,将取消的对应关系删除
		if (lstType != null) {
			StringBuffer sSQLReportType = new StringBuffer();
			int size = lstType.size();
			for (int i = 0; i < size; i++) {
				if (!Common.isNullStr(sSQLReportType.toString()))
					sSQLReportType.append(",");
				sSQLReportType.append("'" + lstType.get(i) + "'");
			}
			if (!Common.isNullStr(sSQLReportType.toString()))
				dao
						.executeBySql(
								"delete fb_u_usertoreport where  report_id =?  and set_year = ? and report_type not in ("
										+ sSQLReportType.toString() + ")",
								new Object[] { reportID, setYear, });
		}
		// 报表表头
		dsHeader.setName("fb_u_qr_colset");
		dsHeader.post();

		// 执行sqlline语句
		executeSqlLines(lstSqlLines, reportID);

		int i = 0;
		if (blobByte != null) {
			// Blob blob = null;
			Session session = null;
			PreparedStatement pstmt = null;
			try {
				session = dao.getSession();
				Connection conn = session.connection();
				// conn.setAutoCommit(false);

				String blobField;
				if ("0".equals(loginmode)) {// 在线
					blobField = "empty_blob()";

				} else
					// 离线
					blobField = "null";

				StringBuffer sSql = new StringBuffer();
				sSql.append(" insert into FB_U_QR_GROUPREPORT ");
				sSql.append(" (Set_year ,report_id,rg_code,FILESTREAM)");
				sSql.append(" values(?,?,?," + blobField + ")");
				pstmt = conn.prepareStatement(sSql.toString());
				pstmt.setString(1, setYear);
				pstmt.setString(2, reportID);
				pstmt.setString(3, sRgCode);
				pstmt.executeUpdate();

				// 采用共用的blob写入函数 by qinj at Nov 26, 2008
				i = QueryStub.getQueryTool().updateBlob("FB_U_QR_GROUPREPORT",
						"FILESTREAM", "report_id=? and set_year=?",
						new Object[] { reportID, setYear }, blobByte);

				// PreparedStatement pstmt = null;
				// pstmt = conn
				// .prepareStatement("select FILESTREAM from FB_U_QR_GROUPREPORT
				// where report_id= ? and set_year=? for update");
				// pstmt.setString(1, reportID);
				// pstmt.setString(2, setYear);
				// ResultSet rt = pstmt.executeQuery();
				// if ("0".equals(loginmode)) {// 在线
				// if (rt.next())
				// blob = (Blob) rt.getBlob(1);
				//	
				// if (null != blob) {
				// OutputStream out = null;
				// String app_server = (String) SessionUtil.getServerInfo();
				// if (app_server == null || app_server.equals(""))
				// app_server = "tomcat";
				// else
				// app_server = app_server.toLowerCase().toString();
				// // weblogic
				// if (app_server.indexOf("weblogic") != -1)
				// out = ((OracleThinBlob) blob).getBinaryOutputStream();
				// // tomcat
				// if (app_server.indexOf("tomcat") != -1)
				// out = ((BLOB) blob).getBinaryOutputStream();
				//	
				// out.write(blobByte);
				// out.close();
				// pstmt = conn
				// .prepareStatement("update FB_U_QR_GROUPREPORT set
				// FILESTREAM=? where report_id=? and set_year=?");
				// pstmt.setBlob(1, blob);
				// pstmt.setString(2, reportID);
				// pstmt.setString(3, setYear);
				// i = pstmt.executeUpdate();
				//	
				// }
				// } else {// 离线
				//	
				// pstmt = conn
				// .prepareStatement("update FB_U_QR_GROUPREPORT set
				// FILESTREAM=? where report_id=? and set_year=?");
				//	
				// ByteArrayInputStream bis = new
				// ByteArrayInputStream(blobByte);
				//	
				// pstmt.setBinaryStream(1, bis, blobByte.length);
				// pstmt.setString(2, reportID);
				// pstmt.setString(3, setYear);
				// i = pstmt.executeUpdate();
				//	
				// }
				// pstmt.close();
			} finally {
				if (pstmt != null)
					pstmt.close();
				dao.closeSession(session);
			}
			return i;
		}
		return 0;
	}

	/**
	 * 存储sqlLine表中信息
	 * 
	 * @param list
	 * @param sReportID
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws IOException
	 */
	private void executeSqlLines(List list, String sReportID)
			throws HibernateException, SQLException, IOException {
		int iMax = 0;
		Object oldSqlType = "";
		Map map;
		String sSql;
		// 得到登陆年份
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		String loginmode = SessionUtil.getUserInfoContext().getLoginMode();
		Object sqlLines;
		Object viewName;
		Object sqlType;
		int iCount = list.size();
		Session session = null;
		PreparedStatement smt = null;
		try {
			session = dao.getSession();
			Connection con = session.connection();

			String clobField;
			if ("0".equals(loginmode)) {// 在线
				clobField = "empty_clob()";

			} else
				// 离线
				clobField = "null";

			for (int i = 0; i < iCount; i++) {
				map = (Map) list.get(i);
				sqlLines = map.get(IBesQryReport.SQLTREAM);
				viewName = map.get(IBesQryReport.VIEWNAME);
				sqlType = map.get(IBesQryReport.SQLTYPE);
				if (!oldSqlType.equals(sqlType)) {
					iMax = 0;
					oldSqlType = sqlType;
				}
				iMax++;
				sSql = "insert into fb_u_qr_sqllines "
						+ " (set_year, report_id, sqltype,viewlevel, viewname,  rg_code,  sqltream)"
						+ " values" + " (?,?,?,?,?,?," + clobField + ")";

				smt = con.prepareStatement(sSql);
				smt.setString(1, setYear);
				smt.setString(2, sReportID);
				smt.setObject(3, sqlType);
				smt.setInt(4, iMax);
				smt.setObject(5, viewName);
				smt.setString(6, sRgCode);
				// smt.setCharacterStream(7, new
				// StringReader(sqlLines.toString()),
				// sqlLines.toString().length());
				// smt.setClob(7, oracle.sql.CLOB.empty_lob());
				smt.executeUpdate();

				// 采用通用的Clob写入函数 by qinj at Nov 26, 2008
				QueryStub
						.getQueryTool()
						.updateClob(
								"fb_u_qr_sqllines",
								"sqltream",
								"set_year = ? and  report_id = ? and sqltype = ? and viewlevel = ?",
								new Object[] { setYear, sReportID, sqlType,
										new Integer(iMax) },
								sqlLines.toString());

				// ResultSet rs;
				// PreparedStatement smt = null;
				// sSql = "select * from fb_u_qr_sqllines where set_year = ? and
				// report_id = ? and sqltype = ? and viewlevel = ? ";
				// smt = con.prepareStatement(sSql);
				// smt.setString(1, setYear);
				// smt.setString(2, sReportID);
				// smt.setObject(3, sqlType);
				// smt.setInt(4, iMax);
				// rs = smt.executeQuery();
				//					
				// Clob clob = null;
				//	
				// if ("0".equals(loginmode)) {// 在线
				// if (rs.next())
				// clob = (Clob) rs.getClob("sqltream");
				//	
				// if (null != clob) {
				// Writer w = null;
				// String app_server = (String) SessionUtil.getServerInfo();
				// if (app_server == null || app_server.equals(""))
				// app_server = "tomcat";
				// else
				// app_server = app_server.toLowerCase().toString();
				// // weblogic
				// if (app_server.indexOf("weblogic") != -1)
				// w = ((OracleThinClob) clob).getCharacterOutputStream();
				// // tomcat
				// if (app_server.indexOf("tomcat") != -1)
				// w = ((CLOB) clob).getCharacterOutputStream();
				//	
				// w.write(sqlLines.toString());
				// w.close();
				// smt = con
				// .prepareStatement("update fb_u_qr_sqllines set sqltream = ?
				// where set_year = ? and report_id = ? and sqltype = ? and
				// viewlevel = ? ");
				// smt.setClob(1, clob);
				// smt.setString(2, setYear);
				// smt.setString(3, sReportID);
				// smt.setObject(4, sqlType);
				// smt.setInt(5, iMax);
				// smt.executeUpdate();
				//	
				// }
				// } else {// 离线
				//	
				// smt = con
				// .prepareStatement("update fb_u_qr_sqllines set sqltream = ?
				// where set_year = ? and report_id = ? and sqltype = ? and
				// viewlevel = ? ");
				//	
				// ByteArrayInputStream bis = new ByteArrayInputStream(sqlLines
				// .toString().getBytes());
				// smt.setBinaryStream(1, bis,
				// sqlLines.toString().getBytes().length);
				// smt.setString(2, setYear);
				// smt.setString(3, sReportID);
				// smt.setObject(4, sqlType);
				// smt.setInt(5, iMax);
				// smt.executeUpdate();
				//	
				// }
				// rs.close();
				// smt.close();
			}
		} finally {
			if (smt != null)
				smt.close();
			dao.closeSession(session);
		}
	}

	/**
	 * 取得ID中的图片信息
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID, int loginmode)
			throws Exception {
		if (reportID == null)
			return null;

		// 采用共用方法读取blob by qinj at Nov 26, 2008
		String sSql = "select FILESTREAM from FB_U_QR_GROUPREPORT where report_id=? ";
		return QueryStub.getQueryTool().getBlob(sSql,
				new Object[] { reportID }, "FILESTREAM");

		// String sSql = "select FILESTREAM from FB_U_QR_GROUPREPORT where
		// report_id ='"
		// + reportID + "' ";
		// Session session = null;
		// try {
		// session = dao.getSession();
		// Connection conn = session.connection();
		// // conn.setAutoCommit(false);
		// PreparedStatement pstmt = conn.prepareStatement(sSql);
		//
		// ResultSet rt = pstmt.executeQuery();
		// if (rt.getRow() < 0)
		// return new byte[0];
		// rt.next();
		// InputStream input;
		// byte[] result;
		// if (loginmode == 0) {// 在线
		// Blob blob = (Blob) rt.getBlob("filestream");
		// int iLength = (int) blob.length();
		// result = new byte[iLength];
		// input = blob.getBinaryStream();
		// input.read(result);
		// input.close();
		//
		// } else {// 离线
		// input = rt.getBinaryStream("filestream");
		// result = new byte[input.available()];
		// input.read(result);
		// input.close();
		// }
		//
		// pstmt.close();
		// return result;
		// } catch (Exception e) {
		// Log.error(e.getMessage());
		// throw e;
		// } finally {
		// if (session != null)
		// dao.closeSession(session);
		// }

	}

	/**
	 * XXL 删除报表
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public String deleteReport(String reportID, String setYear)
			throws Exception {
		if (Common.isNullStr(reportID))
			return "未指定报表编号";
		String sSql = "select type_flag from fb_u_qr_repset where set_Year="
				+ setYear + " and report_id='" + reportID + "'";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return "没有查询到指定的报表";
		int iType = (Integer.parseInt((String) ((XMLData) lstData.get(0))
				.get("type_flag")));
		switch (iType) {
		case IDefineReport.REPORTTYPE_OTHER:
			delOtherReport(reportID, setYear);
			break;
		case IDefineReport.REPORTTYPE_COVER:
			delCover(reportID, setYear);
			break;
		case IDefineReport.REPORTTYPE_GROUP:
			delGroupReport(reportID, setYear);
			break;
		case IDefineReport.REPORTTYPE_ROW:
			delRowReport(reportID, setYear);
			break;
		case IDefineReport.REPORTTYPE_SZZB:
			delSZZB(reportID, setYear);
			break;
		default:
			return "不识别的报表类型";

		}
		delReportMain(reportID, setYear);// 删除主表
		delReportGrant(reportID, setYear);// 删除授权
		// 删除报表打印设置
		delPrintinfo(reportID, setYear);
		// 删除报表对报表类型表
		delReportToType(reportID, setYear);
		return "";

	}

	// 删除其它普通表
	private void delOtherReport(String reportID, String setYear)
			throws Exception {
		String sSql = "delete  from fb_u_qr_colset where  report_ID='"
				+ reportID + "' ";// 删除主表
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_SQLLINES where  report_ID='" + reportID
				+ "' ";// 删除设置表
		dao.executeBySql(sSql);
	}

	// 删除收支总表
	private void delSZZB(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_szzbcell where report_ID='"
				+ reportID + "' ";// 删除从表
		dao.executeBySql(sSql);
		sSql = "delete from fb_u_qr_szzb where report_ID='" + reportID
				+ "' ";// 删除主表
		dao.executeBySql(sSql);
		sSql = "delete  from fb_u_qr_colset where  report_ID='" + reportID
				+ "' ";// 删除列表
		dao.executeBySql(sSql);
	}

	// 删除封面
	private void delCover(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_szzb where report_ID='" + reportID
				+ "' and set_year=" + setYear;// 删除主表
		dao.executeBySql(sSql);
	}

	// 删除行报表
	private void delRowReport(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_rowset_con where report_ID='"
				+ reportID + "' ";// 删除从表
		dao.executeBySql(sSql);
		sSql = "delete from fb_u_qr_rowset where report_ID='" + reportID
				+ "' ";// 删除主表
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_COLSET where report_ID='" + reportID
				+ "' ";// 删除列表
		dao.executeBySql(sSql);
	}

	// 删除分组报表
	private void delGroupReport(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_qr_groupreport where report_ID='"
				+ reportID + "' ";// 删除表
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_SQLLINES where  report_ID='" + reportID
				+ "'";// 删除设置表
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_COLSET where  report_ID='" + reportID
				+ "' ";// 删除列表
		dao.executeBySql(sSql);
	}

	// 删除主表信息
	private void delReportMain(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_qr_repset where report_ID='" + reportID
				+ "'  ";
		dao.executeBySql(sSql);
	}

	// 删除授权信息
	private void delReportGrant(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_usertoreport where report_ID='"
				+ reportID + "' ";
		dao.executeBySql(sSql);
	}

	// 删除报表打印设置
	private void delPrintinfo(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_s_printinfo where report_ID='" + reportID
				+ "' ";
		dao.executeBySql(sSql);
	}

	// 删除报表对报表类型表
	private void delReportToType(String reportID, String setYear) {
		String sSql = "delete from fb_u_qr_report_to_type where report_ID='"
				+ reportID + "' ";
		dao.executeBySql(sSql);
	}

	/**
	 * 根据编码删除
	 * 
	 * @param reportID
	 */
	public String deleteReportType(String sCode) {
		String sSql = "select count(*) sl from fb_u_qr_report_to_type where type_code=? ";
		List lstTmp = dao.findBySql(sSql, new Object[] { sCode });
		int iCount = Integer.parseInt(((Map) lstTmp.get(0)).get("sl")
				.toString());
		if (iCount > 0) {
			return "已被查询报表使用，不允许删除！";
		}

		sSql = "delete from FB_S_PUBCODE "
				+ " where typeid='QUERYTYPE'  and code = ?";
		dao.executeBySql(sSql, new Object[] {
				 sCode });
		return "";
	}

	/**
	 * 保存报表类型
	 * 
	 * @param sLvl
	 * @param sName
	 * @throws Exception
	 */
	public String saveReportType(String sCode, String sLvl, String sName,String c1)
			throws Exception {
		String sSql;
		if (Common.isNullStr(sCode)) {// 增加
			// 得到code最大编码加1
			String sMaxCode = DBSqlExec.getMaxValueFromField("FB_S_PUBCODE",
					"code", "typeid='QUERYTYPE' and set_year="
							+ SessionUtil.getUserInfoContext().getSetYear());
			DecimalFormat df = new DecimalFormat("000000");
			sMaxCode = df.format(Integer.parseInt(sMaxCode) + 1);

			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			sSql = "INSERT into FB_S_PUBCODE ( TYPEID ,TYPENAME,CODE,NAME,LVL_ID,c1,ISINSIDE,SET_YEAR,RG_CODE)"
					+ " values('QUERYTYPE','查询报表类别',?,?,?,?,1,?,?)";
			dao.executeBySql(sSql, new Object[] { sMaxCode, sName, sLvl,c1,
					SessionUtil.getUserInfoContext().getSetYear(), sRgCode });
			return sMaxCode;
		} else {// 修改
			sSql = "update FB_S_PUBCODE  set NAME = ?,lvl_id =? ,c1=?"
					+ "where code =? and typeid='QUERYTYPE'";
			dao.executeBySql(sSql, new Object[] { sName, sLvl,c1, sCode});
			return "";
		}
	}

	/**
	 * 得到最大节次
	 * 
	 * @param sTableName表名
	 * @return
	 * @throws Exception
	 */
	public int[] getMaxLevel(String[] sTableName, String[] sLevelCodeArray,
			String[] sLevelInfoArray) throws Exception {

		int length = sTableName.length;
		int[] level = new int[length];
		String sSql;
		List lstResult;
		for (int i = 0; i < length; i++) {
			if (Common.isNullStr(sTableName[i])) {
				level[i] = 0;
				continue;
			}

			// 判断是否有SET_YEAR字段

			sSql = "select max(length(" + sLevelCodeArray[i]
					+ "))  as value from (" + sTableName[i] + ") a ";

			lstResult = dao.findBySql(sSql);

			int iMaxCode = Integer.parseInt(((Map) lstResult.get(0)).get(
					"value").toString());
			if (iMaxCode == 0)
				level[i] = 0;
			else
				level[i] = getMaxNum(iMaxCode, sLevelInfoArray[i]);

		}
		return level;
	}

	/**
	 * 得到最大值
	 * 
	 * @param iMax
	 * @param sLevevcode
	 * @return
	 */
	private int getMaxNum(int iMax, String sLevcode) {
		String[] sLevCode = sLevcode.split("-");
		int iNum = 0;
		for (int i = 0; i < sLevCode.length; i++) {
			if (Common.isInteger(sLevCode[i])) {
				iNum = iNum + Integer.parseInt(sLevCode[i]);
				if (iMax == iNum)
					return ++i;
			}
		}
		return 0;
	}

	/**
	 * 得报表类型根据报表ID
	 * 
	 * @param sReportId报表ID
	 * @return报表类型列表
	 */
	public List getReportToType(String sReportId) {
		String sSql = "select Type_Code as code from fb_u_qr_report_to_type"
				+ " where report_id = ? ";
		List lstResult = dao.findBySql(sSql, new Object[] { sReportId });
		return lstResult;
	}

	/**
	 * 得到基础信息选择列的值，根据报表id和ename值
	 * 
	 * @param sReportId
	 * @param sFieldEname
	 * @return
	 * @throws Exception
	 */
	public List getBasSelectInputValue(String sReportId, String sFieldEname)
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		// 判断是否是项目
		if (sReportId.indexOf(IDataDictBO.PRJ_PFS + "-") == 0
				|| sReportId.indexOf(IDataDictBO.PRJ_MX + "-") == 0) {
			String inputSetId;
			if (sReportId.indexOf(IDataDictBO.PRJ_MX + "-") == 0) {// 项目明细
				// 得到项目面板sortID ,reprotId =prjmx-sortId-detailcode组成
				String prjSortId = sReportId.substring(
						sReportId.indexOf("-") + 1,
						sReportId.lastIndexOf("-") );
				String sSql = "select INPUT_SET_ID from FB_E_PRJSORT where PRJSORT_ID =?";
				inputSetId = DBSqlExec.getStringValue(sSql,
						new Object[] { prjSortId });
			} else {
				inputSetId = sReportId
						.substring(sReportId.lastIndexOf("-") + 1);
			}
			// 判断是否是base_开头的
			if (sFieldEname.toLowerCase().indexOf(IDataDictBO.PRJ_BASE_PREFIX) == 0) {
				// 取IDataDictBO.PRJ_BASE_PREFIX后面值
				sFieldEname = sFieldEname.substring(IDataDictBO.PRJ_BASE_PREFIX
						.length());
			}
			String sSql = "select b.comp_id,b.field_type from fb_p_input_setdetail a ,fb_p_input_components b "
					+ " where a.input_comp_id= b.comp_id and a.set_year = b.set_year"
					+ " and a.input_set_id = ? and b.comp_ename = ?  ";
			DataSet ds = DataSet.create();
			DBSqlExec.getDataSet(sSql, new Object[] { inputSetId, sFieldEname
					 }, ds);
			ds.beforeFirst();
			if (ds.isEmpty()) {
				return null;
			}
			ds.next();
			// 得到控件类型 1:字符串 2:整数 3:浮点数 4:下拉框 5:日期 6:时间
			int fieldType = ds.fieldByName(IDefineReport.FIELD_TYPE)
					.getInteger();
			if (fieldType == 4) {
				int compId = ds.fieldByName("comp_id").getInteger();
				// 查询下拉框内容
				sSql = "select item_code as code,item_name as name"
						+ " from fb_p_input_combox_values where COMP_ID = ? order by item_code";
				return dao.findBySql(sSql, new Object[] {
						String.valueOf(compId) });
			} else {
				return null;
			}
		} else { // 基础信息
			String sSql = "select IS_SELECTINPUT,REFCOL_ID from fb_b_set_colset where report_id =? and field_ename=? ";
			List lstResult = dao.findBySql(sSql, new Object[] { sReportId,
					sFieldEname });
			if (lstResult == null || lstResult.size() == 0)
				return null;
			if (((Map) lstResult.get(0)).get("is_selectinput") == null
					|| ((Map) lstResult.get(0)).get("refcol_id") == null)
				return null;
			String isSelectInput = ((Map) lstResult.get(0)).get(
					"is_selectinput").toString();
			// 判断是否是选择项
			if (!Common.estimate(isSelectInput))
				return null;

			String refColId = ((Map) lstResult.get(0)).get("refcol_id")
					.toString();
			if (Common.isNullStr(refColId)) {
				return null;
			}
			sSql = "select ItemName as code,ItemName as Name from fb_b_pubcode"
					+ " where typeid=?  order by itemxh";
			return dao.findBySql(sSql, new Object[] { refColId });
		}
	}

	/**
	 * 根据数据源名称，得到列信息
	 * 
	 * @param sDataSourceName
	 *            数据源名称
	 * @return 列信息
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sDataSourceName)
			throws Exception {
		return QueryStub.getQueryTool().getColumnInfoBySQL(
				"select * from (" + sDataSourceName + ") where 1=2");
	}

	/**
	 * 报表调整顺序
	 * 
	 * @param oneLvl
	 *            交换节点一lvl_id
	 * @param twoLvl
	 *            交换节点二lvl_id
	 * @param oneId
	 *            交换节点一ID号
	 * @param twoId
	 *            交换节点二ID号
	 * @param tableName
	 *            表名
	 * @param lvlFieldName
	 *            lvl_id字段名
	 * @param idFieleName
	 *            id（主键）字段名 *
	 * @param filter
	 *            条件
	 */
	public void changeLvlValue(String oneLvl, String twoLvl, String oneId,
			String twoId, String tableName, String lvlFieldName,
			String idFieleName, String filter) {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sSql = "update " + tableName + " set " + lvlFieldName + "= ? "
				+ " where " + idFieleName + " = ? ";
		// 过滤条件
		if (!Common.isNullStr(filter)) {
			sSql = sSql + " and " + filter;
		}
		dao.executeBySql(sSql, new Object[] { oneLvl, twoId });
		dao.executeBySql(sSql, new Object[] { twoLvl, oneId });
	}

	/**
	 * 复制查询报表
	 * 
	 * @param reportID报表ID
	 * @param reportName复制生成的新报表名称
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage copyReport(String reportID, String reportName,
			String setYear) throws Exception {
		InfoPackage info = new InfoPackage();
		if (Common.isNullStr(reportID)) {
			info.setSuccess(false);
			info.setsMessage("未指定报表编号");
			return info;
		}
		String sSql = "select type_flag from fb_u_qr_repset "
			+ " where report_id='" + reportID + "'";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0) {
			info.setSuccess(false);
			info.setsMessage("没有查询到指定的报表");
			return info;
		}
		List lstSql = new ArrayList();
//
		String sToReportId = SzzbSetI.getServerMethod().getMaxCode("REPORT_ID");
		String sToLvlId = SzzbSetI.getServerMethod().getMaxCode("LVL_ID");

		int iType = (Integer.parseInt((String) ((XMLData) lstData.get(0))
				.get("type_flag")));
		Map mapReportField = new HashMap();
		mapReportField.put(IBesQryReport.REPORT_ID,sToReportId);
		mapReportField.put(IBesQryReport.LVL_ID, sToLvlId);
		mapReportField.put(IBesQryReport.REPORT_CNAME, reportName);
		mapReportField.put(IBesQryReport.TITLE, reportName);

		switch (iType) {
		case IDefineReport.REPORTTYPE_OTHER:
			lstSql.add(getCopyTableSql("FB_U_QR_COLSET", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_SQLLINES", reportID,
					mapReportField, setYear));
			break;
		case IDefineReport.REPORTTYPE_COVER:
			lstSql.add(getCopyTableSql("FB_U_QR_SZZB", reportID,
					mapReportField, setYear));
			break;
		case IDefineReport.REPORTTYPE_GROUP:
			lstSql.add(getCopyTableSql("FB_U_QR_GROUPREPORT", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_SQLLINES", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_COLSET", reportID,
					mapReportField, setYear));
			break;
		case IDefineReport.REPORTTYPE_ROW:
			lstSql.add(getCopyTableSql("FB_U_QR_ROWSET_CON", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_ROWSET", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_COLSET", reportID,
					mapReportField, setYear));
			break;
		case IDefineReport.REPORTTYPE_SZZB:
			lstSql.add(getCopyTableSql("FB_U_QR_SZZBCELL", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_SZZB", reportID,
					mapReportField, setYear));
			lstSql.add(getCopyTableSql("FB_U_QR_COLSET", reportID,
					mapReportField, setYear));
			break;
		default:
			info.setSuccess(false);
			info.setsMessage("不识别的报表类型");
			return info;
		}

		// 复制主表
		lstSql.add(getCopyTableSql("FB_U_QR_REPSET", reportID, mapReportField,
				setYear));
		// 复制打印设置
		lstSql.add(getCopyTableSql("FB_S_PRINTINFO", reportID, mapReportField,
				setYear));
		// 复制报表对报表类型表
		lstSql.add(getCopyTableSql("FB_U_QR_REPORT_TO_TYPE", reportID,
				mapReportField, setYear));
		// 复制报表对报表类型表
		lstSql.add(getCopyTableSqlUser("fb_u_usertoreport", reportID,
				mapReportField, setYear));
		QueryStub.getQueryTool().executeBatch(lstSql);
		info.setSuccess(true);
		List lstValue = new ArrayList();
		lstValue.add(sToReportId);
		lstValue.add(sToLvlId);
		info.setObject(lstValue);
		return info;
	}

	/**
	 * 得到复制报表sql
	 * 
	 * @param tableName表名
	 * @param reportId报表ID
	 * @param replaceField需替换的字段,key字段名称，value字段值
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private String getCopyTableSql(String tableName, String reportId,
			Map replaceField, String setYear) throws Exception {
		String sSql = " select * from " + tableName + " where 1=2";
		TableColumnInfo tableColumnInfo[] = QueryStub.getQueryTool()
				.getColumnInfoBySQL(sSql);
		String sFieldSql = "";
		for (int j = 0; j < tableColumnInfo.length; j++) {
			String sFieldName = tableColumnInfo[j].getColumnName().toString();
			if (!Common.isNullStr(sFieldSql)) {
				sFieldSql += ",";
			}
			if (replaceField.containsKey(sFieldName)) {
				int colType = tableColumnInfo[j].getColumnType();
				if (Types.CHAR == colType || Types.VARCHAR == colType
						|| Types.TIMESTAMP == colType) {
					sFieldSql += "'" + replaceField.get(sFieldName) + "'";
				} else {
					sFieldSql += replaceField.get(sFieldName);
				}
			} else {
				sFieldSql += sFieldName;
			}
		}
		sSql = " insert into " + tableName + " select " + sFieldSql + " from "
				+ tableName + " where report_id ='" + reportId 
				+ "' ";
		return sSql;
	}
	
	private String getCopyTableSqlUser(String tableName, String reportId,
			Map replaceField, String setYear) throws Exception {
		String sSql = " select * from " + tableName + " where 1=2";
		TableColumnInfo tableColumnInfo[] = QueryStub.getQueryTool()
				.getColumnInfoBySQL(sSql);
		String sFieldSql = "";
		for (int j = 0; j < tableColumnInfo.length; j++) {
			String sFieldName = tableColumnInfo[j].getColumnName().toString();
			if (!Common.isNullStr(sFieldSql)) {
				sFieldSql += ",";
			}
			if (replaceField.containsKey(sFieldName)) {
				int colType = tableColumnInfo[j].getColumnType();
				if (Types.CHAR == colType || Types.VARCHAR == colType
						|| Types.TIMESTAMP == colType) {
					sFieldSql += "'" + replaceField.get(sFieldName) + "'";
				} else {
					sFieldSql += replaceField.get(sFieldName);
				}
			} else {
				sFieldSql += sFieldName;
			}
		}
		sSql = " insert into " + tableName + " select " + sFieldSql + " from "
				+ tableName + " where report_id ='" + reportId +"' and user_id='"+SessionUtil.getUserInfoContext().getUserID()
				+ "' ";
		return sSql;
	}
}
