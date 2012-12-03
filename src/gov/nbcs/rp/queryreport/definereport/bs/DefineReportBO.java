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
 * Title:�Զ�������÷����ʵ����
 * </p>
 * <p>
 * Description:�Զ�������÷����ʵ����
 * </p>
 * <p>

 * Copyright: Copyright (c) 2011 �㽭����
 * </p>
 * <p>
 * Company: �㽭����
 * </p>
 * <p>
 * CreateDate 2011-7
 * </p>
 * 
 * @author WUYAL

 * @version 1.0
 */
public class DefineReportBO implements IDefineReport {

	// ����GeneralDao����
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
	 * �õ����������ѡ����
	 * 
	 * @return�������������ѡ�����б�
	 */
	public List getReportSort() {
		String sql_database = "select * from fb_u_qr_reportType  order by lvl_id";
		return dao.findBySql(sql_database);
	}

	/**
	 * �õ�����������Ϣ
	 * 
	 * @throws Exception
	 * 
	 * 
	 * @return���ر�������
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
	 * �õ�����Դ��Ϣ
	 * 
	 * @return ��������Դ��Ϣ�б�
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
	 * �õ�ѡ�е�����Դ��
	 * 
	 * @param lstDataSource
	 *            ѡ�е�����Դ��Ϣ
	 * @throws Exception
	 * @return����ѡ�е�����Դ��ϸ��Ϣ
	 */
	public DataSet getDataSoureDetail(String sDataSource) throws Exception {
		String loginMode = SessionUtil.getUserInfoContext().getLoginMode();
		String sSql = null;
		if ("0".equals(loginMode)) {// ����
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
					+ ") and a.AFIELD_CNAME<>'δ�����ֶ�'";
		} else {// ����
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
					+ ") and a.AFIELD_CNAME<>'δ�����ֶ�'";
		}

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * �õ�ѡ������Դ����ϸ��Ϣ
	 * 
	 * @param sDataSource
	 *            ѡ�е�����Դ��Ϣ
	 * @return����ѡ������Դ����ϸ��Ϣ
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
	 * ����ѡ�е�����Դ���õ���Ҫʹ�õ�ö��Դ��Ϣ
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
	 * �õ�������Ϣ
	 * 
	 * @return ���ر�����Ϣ
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
	 * ��������ԴENameֵȡ���ֶ���ϸ��Ϣ
	 * 
	 * @param sObjectEName
	 *            ����Դ�����ͼ��
	 * @return
	 */
	public List getFieldWithEname(String sObjectEName) {
		String sSql = " select AFIELD_ENAME  as field_ename,AFIELD_CNAME as field_fname,AFIELD_TYPE as FIELD_TYPE,refcol_id "
				+ " from FB_DICT_INFO_DETAIL where DICID=?  and set_year=? order by AFIELD_SORT";
		return dao.findBySql(sSql, new Object[] { sObjectEName,
				SessionUtil.getUserInfoContext().getSetYear() });
	}

	/**
	 * �õ���������������
	 * 
	 * @return
	 */
	public List getRefColPriCode() {
		String sSql = " select refcol_id, lvl_field from fb_s_refcol where set_year=?";
		return dao.findBySql(sSql, new Object[] { SessionUtil
				.getUserInfoContext().getSetYear() });
	}

	/**
	 * ����enumId�õ���������Ϣ
	 * 
	 * @param sEnumID
	 *            ��ö��ID
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
		// �õ�DataSet
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sqlDet, ds);

		Map tempMap = new HashMap();
		tempMap.putAll(refColMap);
		tempMap.put(IDefineReport.ENUM_DATA, ds);

		return tempMap;
	}

	/**
	 * �������Sql,ȥ���������������
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	private String setSql(String sSql, Map map) throws Exception {
		// // ȥ������
		// int iLen = sSql.toUpperCase().indexOf("ORDER");
		// if (iLen != -1)
		// sSql = sSql.substring(0, iLen);

		String value = "select a.*,'['||" + map.get("lvl_field").toString()
				+ "||']'||" + map.get("name_field").toString()
				+ " as name from ";

		// ȥ���������
//		sSql =  .getServerMethod().replaceRefColFixFlag(sSql);

		// // �ж�where�Ƿ��в�ѯ����
		// int index = sSql.toUpperCase().indexOf("WHERE");
		// iLen = index + "WHERE".length();
		// String sSqlTmp = sSql.substring(iLen).trim();
		// if ("".equals(sSqlTmp)) {
		// sSql = sSql.substring(0, index);
		// } else {// �ж����ѯ����,ȥ��and
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
	 * ���汨��
	 * 
	 * @param setYear
	 *            ���
	 * @param blobByte
	 * @param repSetObject
	 *            �����������
	 * @param sOldReportId
	 *            �����������
	 * @param dsHeader
	 *            ��ͷ
	 * @param lstSqlLines
	 *            �����ѯ���
	 * @param lstType
	 *            ��������
	 * 
	 * @return
	 * @throws Exception
	 */
	public int saveReportFile(byte[] blobByte, RepSetObject repSetObject,
			String sOldReportId, DataSet dsHeader, List lstSqlLines,
			List lstType) throws Exception {

		// �õ���½���
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
		// �ж������ӻ����޸�
		if (sOldReportId == null || "".equals(sOldReportId)) { // ����
			sSqlTmp = "insert into fb_u_qr_repset"
					+ " (set_year, type_no, report_id, report_type, report_cname,title,  "
					+ "report_source, is_passverify, is_active, data_user, is_end,lvl_id, rg_code,  type_flag ,"
					+ "IS_HASBATCH,IS_MULTICOND,CURRENCYUNIT,FUNDSOURCE_FLAG,COMPARE_FLAG"
					+ ")" + " values" + " (" + setYear + ", 1, '" + reportID
					+ "', " + reportType + ", '" + reportCName + "','"
					+ reportCName + "','����','��','" + isActive + "'," + dataUser
					+ ",1,'" + lvlID + "','" + sRgCode + "'," + typeFlag
					+ ",'��','��','" + sCurrencyUnit + "','" + fundsourceFlag
					+ "','" + compareFlag + "')";

		} else {// �޸�
			sSqlTmp = "update fb_u_qr_repset" + " set set_year = " + setYear
					+ "," + " type_no =1," + " report_id = '" + reportID + "',"
					+ " report_type = '" + reportType + "',"
					+ " report_cname = '" + reportCName + "'," + " title = '"
					+ reportCName + "'," + " report_source = '����',"
					+ " is_passverify = '��'," + " is_active = '" + isActive
					+ "'," + " data_user = " + dataUser + ","
					+ " is_end = 1,lvl_id ='" + lvlID + "'," + " rg_code = '"
					+ sRgCode + "'," + " type_flag =" + typeFlag
					+ ",CURRENCYUNIT='" + sCurrencyUnit
					+ "',FUNDSOURCE_FLAG ='" + fundsourceFlag
					+ "',COMPARE_FLAG='" + compareFlag + "' where report_id = '" + sOldReportId + "'";
		}
		dao.executeBySql(sSqlTmp);

		if (sOldReportId != null && !"".equals(sOldReportId)) { // �޸�
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
		// ����fb_
		dao.executeBySql("delete from fb_u_usertoreport where REPORT_ID='"+reportID+"' and user_id='"+SessionUtil.getUserInfoContext().getUserID()+"' and set_year="+setYear+"");
		
//        dao.executeBySql("insert into fb_u_usertoreport (LAST_VER,RG_CODE,USER_ID,REPORT_ID,SET_YEAR,REPORT_TYPE)values('','"+
//				sRgCode+"','"+ SessionUtil.getUserInfoContext().getUserID()+"','"+reportID+"','"+setYear+"','"+reportType+"')");
		// ����fb_u_qr_report_to_type
		if (lstType != null) {
			int size = lstType.size();
			for (int i = 0; i < size; i++) {
				dao
						.executeBySql(
								"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
								new Object[] { reportID, lstType.get(i),
										setYear, sRgCode });
				// add by ymq �����û��Ա���
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

		// �޸ı������û��Ա���,��ȡ���Ķ�Ӧ��ϵɾ��
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
		// �����ͷ
		dsHeader.setName("fb_u_qr_colset");
		dsHeader.post();

		// ִ��sqlline���
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
				if ("0".equals(loginmode)) {// ����
					blobField = "empty_blob()";

				} else
					// ����
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

				// ���ù��õ�blobд�뺯�� by qinj at Nov 26, 2008
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
				// if ("0".equals(loginmode)) {// ����
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
				// } else {// ����
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
	 * �洢sqlLine������Ϣ
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
		// �õ���½���
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
			if ("0".equals(loginmode)) {// ����
				clobField = "empty_clob()";

			} else
				// ����
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

				// ����ͨ�õ�Clobд�뺯�� by qinj at Nov 26, 2008
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
				// if ("0".equals(loginmode)) {// ����
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
				// } else {// ����
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
	 * ȡ��ID�е�ͼƬ��Ϣ
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID, int loginmode)
			throws Exception {
		if (reportID == null)
			return null;

		// ���ù��÷�����ȡblob by qinj at Nov 26, 2008
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
		// if (loginmode == 0) {// ����
		// Blob blob = (Blob) rt.getBlob("filestream");
		// int iLength = (int) blob.length();
		// result = new byte[iLength];
		// input = blob.getBinaryStream();
		// input.read(result);
		// input.close();
		//
		// } else {// ����
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
	 * XXL ɾ������
	 * 
	 * @param reportID
	 * @param setYear
	 * @return
	 */
	public String deleteReport(String reportID, String setYear)
			throws Exception {
		if (Common.isNullStr(reportID))
			return "δָ��������";
		String sSql = "select type_flag from fb_u_qr_repset where set_Year="
				+ setYear + " and report_id='" + reportID + "'";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return "û�в�ѯ��ָ���ı���";
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
			return "��ʶ��ı�������";

		}
		delReportMain(reportID, setYear);// ɾ������
		delReportGrant(reportID, setYear);// ɾ����Ȩ
		// ɾ�������ӡ����
		delPrintinfo(reportID, setYear);
		// ɾ������Ա������ͱ�
		delReportToType(reportID, setYear);
		return "";

	}

	// ɾ��������ͨ��
	private void delOtherReport(String reportID, String setYear)
			throws Exception {
		String sSql = "delete  from fb_u_qr_colset where  report_ID='"
				+ reportID + "' ";// ɾ������
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_SQLLINES where  report_ID='" + reportID
				+ "' ";// ɾ�����ñ�
		dao.executeBySql(sSql);
	}

	// ɾ����֧�ܱ�
	private void delSZZB(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_szzbcell where report_ID='"
				+ reportID + "' ";// ɾ���ӱ�
		dao.executeBySql(sSql);
		sSql = "delete from fb_u_qr_szzb where report_ID='" + reportID
				+ "' ";// ɾ������
		dao.executeBySql(sSql);
		sSql = "delete  from fb_u_qr_colset where  report_ID='" + reportID
				+ "' ";// ɾ���б�
		dao.executeBySql(sSql);
	}

	// ɾ������
	private void delCover(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_szzb where report_ID='" + reportID
				+ "' and set_year=" + setYear;// ɾ������
		dao.executeBySql(sSql);
	}

	// ɾ���б���
	private void delRowReport(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_u_qr_rowset_con where report_ID='"
				+ reportID + "' ";// ɾ���ӱ�
		dao.executeBySql(sSql);
		sSql = "delete from fb_u_qr_rowset where report_ID='" + reportID
				+ "' ";// ɾ������
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_COLSET where report_ID='" + reportID
				+ "' ";// ɾ���б�
		dao.executeBySql(sSql);
	}

	// ɾ�����鱨��
	private void delGroupReport(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_qr_groupreport where report_ID='"
				+ reportID + "' ";// ɾ����
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_SQLLINES where  report_ID='" + reportID
				+ "'";// ɾ�����ñ�
		dao.executeBySql(sSql);
		sSql = "delete from FB_U_QR_COLSET where  report_ID='" + reportID
				+ "' ";// ɾ���б�
		dao.executeBySql(sSql);
	}

	// ɾ��������Ϣ
	private void delReportMain(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_qr_repset where report_ID='" + reportID
				+ "'  ";
		dao.executeBySql(sSql);
	}

	// ɾ����Ȩ��Ϣ
	private void delReportGrant(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_usertoreport where report_ID='"
				+ reportID + "' ";
		dao.executeBySql(sSql);
	}

	// ɾ�������ӡ����
	private void delPrintinfo(String reportID, String setYear) throws Exception {
		String sSql = "delete from fb_s_printinfo where report_ID='" + reportID
				+ "' ";
		dao.executeBySql(sSql);
	}

	// ɾ������Ա������ͱ�
	private void delReportToType(String reportID, String setYear) {
		String sSql = "delete from fb_u_qr_report_to_type where report_ID='"
				+ reportID + "' ";
		dao.executeBySql(sSql);
	}

	/**
	 * ���ݱ���ɾ��
	 * 
	 * @param reportID
	 */
	public String deleteReportType(String sCode) {
		String sSql = "select count(*) sl from fb_u_qr_report_to_type where type_code=? ";
		List lstTmp = dao.findBySql(sSql, new Object[] { sCode });
		int iCount = Integer.parseInt(((Map) lstTmp.get(0)).get("sl")
				.toString());
		if (iCount > 0) {
			return "�ѱ���ѯ����ʹ�ã�������ɾ����";
		}

		sSql = "delete from FB_S_PUBCODE "
				+ " where typeid='QUERYTYPE'  and code = ?";
		dao.executeBySql(sSql, new Object[] {
				 sCode });
		return "";
	}

	/**
	 * ���汨������
	 * 
	 * @param sLvl
	 * @param sName
	 * @throws Exception
	 */
	public String saveReportType(String sCode, String sLvl, String sName,String c1)
			throws Exception {
		String sSql;
		if (Common.isNullStr(sCode)) {// ����
			// �õ�code�������1
			String sMaxCode = DBSqlExec.getMaxValueFromField("FB_S_PUBCODE",
					"code", "typeid='QUERYTYPE' and set_year="
							+ SessionUtil.getUserInfoContext().getSetYear());
			DecimalFormat df = new DecimalFormat("000000");
			sMaxCode = df.format(Integer.parseInt(sMaxCode) + 1);

			String sRgCode = (String) SessionUtil.getUserInfoContext()
					.getAttribute("cur_region");
			sSql = "INSERT into FB_S_PUBCODE ( TYPEID ,TYPENAME,CODE,NAME,LVL_ID,c1,ISINSIDE,SET_YEAR,RG_CODE)"
					+ " values('QUERYTYPE','��ѯ�������',?,?,?,?,1,?,?)";
			dao.executeBySql(sSql, new Object[] { sMaxCode, sName, sLvl,c1,
					SessionUtil.getUserInfoContext().getSetYear(), sRgCode });
			return sMaxCode;
		} else {// �޸�
			sSql = "update FB_S_PUBCODE  set NAME = ?,lvl_id =? ,c1=?"
					+ "where code =? and typeid='QUERYTYPE'";
			dao.executeBySql(sSql, new Object[] { sName, sLvl,c1, sCode});
			return "";
		}
	}

	/**
	 * �õ����ڴ�
	 * 
	 * @param sTableName����
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

			// �ж��Ƿ���SET_YEAR�ֶ�

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
	 * �õ����ֵ
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
	 * �ñ������͸��ݱ���ID
	 * 
	 * @param sReportId����ID
	 * @return���������б�
	 */
	public List getReportToType(String sReportId) {
		String sSql = "select Type_Code as code from fb_u_qr_report_to_type"
				+ " where report_id = ? ";
		List lstResult = dao.findBySql(sSql, new Object[] { sReportId });
		return lstResult;
	}

	/**
	 * �õ�������Ϣѡ���е�ֵ�����ݱ���id��enameֵ
	 * 
	 * @param sReportId
	 * @param sFieldEname
	 * @return
	 * @throws Exception
	 */
	public List getBasSelectInputValue(String sReportId, String sFieldEname)
			throws Exception {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		// �ж��Ƿ�����Ŀ
		if (sReportId.indexOf(IDataDictBO.PRJ_PFS + "-") == 0
				|| sReportId.indexOf(IDataDictBO.PRJ_MX + "-") == 0) {
			String inputSetId;
			if (sReportId.indexOf(IDataDictBO.PRJ_MX + "-") == 0) {// ��Ŀ��ϸ
				// �õ���Ŀ���sortID ,reprotId =prjmx-sortId-detailcode���
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
			// �ж��Ƿ���base_��ͷ��
			if (sFieldEname.toLowerCase().indexOf(IDataDictBO.PRJ_BASE_PREFIX) == 0) {
				// ȡIDataDictBO.PRJ_BASE_PREFIX����ֵ
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
			// �õ��ؼ����� 1:�ַ��� 2:���� 3:������ 4:������ 5:���� 6:ʱ��
			int fieldType = ds.fieldByName(IDefineReport.FIELD_TYPE)
					.getInteger();
			if (fieldType == 4) {
				int compId = ds.fieldByName("comp_id").getInteger();
				// ��ѯ����������
				sSql = "select item_code as code,item_name as name"
						+ " from fb_p_input_combox_values where COMP_ID = ? order by item_code";
				return dao.findBySql(sSql, new Object[] {
						String.valueOf(compId) });
			} else {
				return null;
			}
		} else { // ������Ϣ
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
			// �ж��Ƿ���ѡ����
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
	 * ��������Դ���ƣ��õ�����Ϣ
	 * 
	 * @param sDataSourceName
	 *            ����Դ����
	 * @return ����Ϣ
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sDataSourceName)
			throws Exception {
		return QueryStub.getQueryTool().getColumnInfoBySQL(
				"select * from (" + sDataSourceName + ") where 1=2");
	}

	/**
	 * �������˳��
	 * 
	 * @param oneLvl
	 *            �����ڵ�һlvl_id
	 * @param twoLvl
	 *            �����ڵ��lvl_id
	 * @param oneId
	 *            �����ڵ�һID��
	 * @param twoId
	 *            �����ڵ��ID��
	 * @param tableName
	 *            ����
	 * @param lvlFieldName
	 *            lvl_id�ֶ���
	 * @param idFieleName
	 *            id���������ֶ��� *
	 * @param filter
	 *            ����
	 */
	public void changeLvlValue(String oneLvl, String twoLvl, String oneId,
			String twoId, String tableName, String lvlFieldName,
			String idFieleName, String filter) {
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sSql = "update " + tableName + " set " + lvlFieldName + "= ? "
				+ " where " + idFieleName + " = ? ";
		// ��������
		if (!Common.isNullStr(filter)) {
			sSql = sSql + " and " + filter;
		}
		dao.executeBySql(sSql, new Object[] { oneLvl, twoId });
		dao.executeBySql(sSql, new Object[] { twoLvl, oneId });
	}

	/**
	 * ���Ʋ�ѯ����
	 * 
	 * @param reportID����ID
	 * @param reportName�������ɵ��±�������
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public InfoPackage copyReport(String reportID, String reportName,
			String setYear) throws Exception {
		InfoPackage info = new InfoPackage();
		if (Common.isNullStr(reportID)) {
			info.setSuccess(false);
			info.setsMessage("δָ��������");
			return info;
		}
		String sSql = "select type_flag from fb_u_qr_repset "
			+ " where report_id='" + reportID + "'";

		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0) {
			info.setSuccess(false);
			info.setsMessage("û�в�ѯ��ָ���ı���");
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
			info.setsMessage("��ʶ��ı�������");
			return info;
		}

		// ��������
		lstSql.add(getCopyTableSql("FB_U_QR_REPSET", reportID, mapReportField,
				setYear));
		// ���ƴ�ӡ����
		lstSql.add(getCopyTableSql("FB_S_PRINTINFO", reportID, mapReportField,
				setYear));
		// ���Ʊ���Ա������ͱ�
		lstSql.add(getCopyTableSql("FB_U_QR_REPORT_TO_TYPE", reportID,
				mapReportField, setYear));
		// ���Ʊ���Ա������ͱ�
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
	 * �õ����Ʊ���sql
	 * 
	 * @param tableName����
	 * @param reportId����ID
	 * @param replaceField���滻���ֶ�,key�ֶ����ƣ�value�ֶ�ֵ
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
