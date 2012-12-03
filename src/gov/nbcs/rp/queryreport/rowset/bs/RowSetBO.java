/**
 * @# RowSetBO.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.bs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.dicinfo.datadict.bs.DataDictBO;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;
import gov.nbcs.rp.queryreport.rowset.ui.Condition;
import gov.nbcs.rp.queryreport.rowset.ui.RowInfo;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.service.Log;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>


 */
public class RowSetBO implements IRowSet {

	GeneralDAO dao;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	public void saveRows(List lstSql, String reportID, String setYear,
			XMLData xmlReport, List lstType) throws Exception {

		// 检查并插入COLSET表中数据
		int iCol = 0;
		try {
//			iCol = Integer.parseInt(BaseUtil.getAStringField(xmlReport,
//					IQrBudget.FIELD_COLUMN));
		} catch (Exception e) {

		}
		int lastCol = getReportCol(reportID);
		if (iCol != lastCol && iCol != 0) {
			initRowReportCol(reportID, iCol);
		}
		// _----------------------

		List lstDelete = getReportSql(xmlReport, reportID, setYear);
		String sSql = "delete from " + Condition.TABLE_NAME + " where "
				+ Condition.FIELD_REPORT_ID + "='" + reportID
				+ "' and set_year=" + setYear;
		lstDelete.add(sSql);
		sSql = "delete from  " + RowInfo.TABLE_NAME + " where "
				+ RowInfo.FIELD_REPORT_ID + "='" + reportID + "' and set_year="
				+ setYear;
		lstDelete.add(sSql);
		// 删除fb_u_qr_report_to_type
		sSql = "delete from fb_u_qr_report_to_type where REPORT_ID= '"
				+ reportID + "' and set_year=" + setYear;
		lstDelete.add(sSql);
		QueryStub.getQueryTool().executeBatch(lstDelete);
		QueryStub.getQueryTool().executeBatch(lstSql);

		// 保存fb_u_qr_report_to_type
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");
		int size = lstType.size();
		for (int i = 0; i < size; i++) {
			dao
					.executeBySql(
							"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
							new Object[] { reportID, lstType.get(i), setYear,
									sRgCode });
		}

	}

	private int getReportCol(String reportID) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void initRowReportCol(String sReportID, int col) throws Exception {
		String sSql = "delete fb_u_qr_colset where report_id='" + sReportID
				+ "' and set_year="
				+ SessionUtil.getUserInfoContext().getSetYear();
		dao.executeBySql(sSql);

		List lstSql = new ArrayList();
		for (int i = 1; i <= col; i++) {
			StringBuffer sb = new StringBuffer();
			int iSer = (i - 1) * 3;
			String subSer;
			// 序号
			subSer = "000" + (iSer + 1);
			sb
					.append("insert into fb_u_qr_colset( SET_YEAR,TYPE_NO,REPORT_ID,FIELD_ID,FIELD_CODE,FIELD_CNAME,FIELD_ENAME,FIELD_FNAME,IS_LEAF,FIELD_LEVEL,FIELD_TYPE,FIELD_WIDTH,FIELD_DISPWIDTH,IS_HIDECOL,FIELD_DISFORMAT,RG_CODE,LAST_VER");
			sb.append(") values(");
			sb.append(SessionUtil.getUserInfoContext().getSetYear());
			sb.append(",1,'").append(sReportID).append("','").append(
					(subSer).substring(subSer.length() - 4) + "','");
			sb.append(subSer.substring(subSer.length() - 3) + "','");
			sb.append("序号','");
			sb.append(RowInfo.FIELD_ITEM_ID + "_"
					+ subSer.substring(subSer.length() - 3) + "','");
			sb.append("序号','");
			sb.append("1',1,'字符型',50,50,'否',null,'");
			sb.append(SessionUtil.getUserInfoContext().getAttribute(
					"cur_region"));
			sb.append("','").append(Tools.getCurrDate() + "')");
			lstSql.add(sb.toString());
			// 标题
			sb = new StringBuffer();
			subSer = "000" + (iSer + 2);
			sb
					.append("insert into fb_u_qr_colset( SET_YEAR,TYPE_NO,REPORT_ID,FIELD_ID,FIELD_CODE,FIELD_CNAME,FIELD_ENAME,FIELD_FNAME,IS_LEAF,FIELD_LEVEL,FIELD_TYPE,FIELD_WIDTH,FIELD_DISPWIDTH,IS_HIDECOL,FIELD_DISFORMAT,RG_CODE,LAST_VER");
			sb.append(") values(");
			sb.append(SessionUtil.getUserInfoContext().getSetYear());
			sb.append(",1,'").append(sReportID).append("','").append(
					subSer.substring(subSer.length() - 4) + "','");
			sb.append(subSer.substring(subSer.length() - 3) + "','");
			sb.append("标题','");
			sb.append(RowInfo.FIELD_ITEM + "_"
					+ subSer.substring(subSer.length() - 3) + "','");
			sb.append("标题','");
			sb.append("1',1,'字符型',200,200,'否',null,'");
			sb.append(SessionUtil.getUserInfoContext().getAttribute(
					"cur_region"));
			sb.append("','").append(Tools.getCurrDate() + "')");
			lstSql.add(sb.toString());
			// 值
			sb = new StringBuffer();
			subSer = "000" + (iSer + 3);
			sb
					.append("insert into fb_u_qr_colset( SET_YEAR,TYPE_NO,REPORT_ID,FIELD_ID,FIELD_CODE,FIELD_CNAME,FIELD_ENAME,FIELD_FNAME,IS_LEAF,FIELD_LEVEL,FIELD_TYPE,FIELD_WIDTH,FIELD_DISPWIDTH,IS_HIDECOL,FIELD_DISFORMAT,RG_CODE,LAST_VER");
			sb.append(") values(");
			sb.append(SessionUtil.getUserInfoContext().getSetYear());
			sb.append(",1,'").append(sReportID).append("','").append(
					subSer.substring(subSer.length() - 4) + "','");
			sb.append(subSer.substring(subSer.length() - 3) + "','");
			sb.append("值','");
			sb.append(RowInfo.FIELD_VALUE + "_"
					+ subSer.substring(subSer.length() - 3) + "','");
			sb.append("值','");
			sb.append("1',1,'浮点型',100,100,'否','#,##0.00','");
			sb.append(SessionUtil.getUserInfoContext().getAttribute(
					"cur_region"));
			sb.append("','").append(Tools.getCurrDate() + "')");
			lstSql.add(sb.toString());

		}
		QueryStub.getQueryTool().executeBatch(lstSql);

	}

//	private int getReportCol(String reportID) {
//		try {
//			XMLData aData = getRepset(SessionUtil.getUserInfoContext()
//					.getSetYear(), 2, reportID);
//			if (aData == null
//					|| BaseUtil.getAStringField(aData, IQrBudget.FIELD_COLUMN) == null)
//				return 0;
//			return Integer.parseInt(BaseUtil.getAStringField(aData,
//					IQrBudget.FIELD_COLUMN));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	public List getReportCons(String reportID, String setYear) {
//		String sSql = "select * from " + Condition.TABLE_NAME + " where "
//				+ Condition.FIELD_REPORT_ID + "='" + reportID
//				+ "' and set_year=" + setYear + " order by "
//				+ Condition.FIELD_LINE_NUM;
//		return PubDataCache.convertToXMLList(dao.findBySqlByUpper(sSql));
//	}
//
//	public List getReportRows(String reportID, String setYear) {
//		String sSql = "select * from " + RowInfo.TABLE_NAME + " where "
//				+ RowInfo.FIELD_REPORT_ID + "='" + reportID + "' and set_year="
//				+ setYear + " order by length(" + RowInfo.FIELD_ITEM_ID + "),"
//				+ RowInfo.FIELD_ITEM_ID;
//		return PubDataCache.convertToXMLList(dao.findBySqlByUpper(sSql));
//	}

	/**
	 * 得到选中的数据源数
	 * 
	 * @param lstDataSource
	 *            选中的数据源信息
	 * @throws Exception
	 * @return返回选中的数据源详细信息
	 */
	public List getDataSoureDetail() throws Exception {
		// String sSql = "select code, object_ename,'' object_cname,
		// FIELD_FNAME,'' FIELD_CODE,'' FIELD_ENAME,'' FIELD_TYPE"
		// + " from (select lpad(rownum, 3, '0') as code,object_ename,
		// object_cname as FIELD_FNAME,set_year "
		// + "from fb_u_qr_object "
		// + "order by OBJECT_CLASS)"
		// + " union all"
		// + " select code|| lpad(rownum, 3, '0')
		// ,object_ename,object_cname,field_fname
		// ,FIELD_CODE,FIELD_ENAME,FIELD_TYPE "
		// + " from ( select
		// b.code,b.object_ename,b.object_cname,a.field_fname,a.set_year,a.FIELD_CODE,a.FIELD_ENAME,a.FIELD_TYPE
		// from fb_u_qr_field a,"
		// + " (select lpad(rownum, 3, '0') as code, object_cname,
		// object_ename,set_year"
		// + " from fb_u_qr_object"
		// + " order by object_class) b"
		// + " where a.object_ename = b.object_ename and a.set_year = b.set_year
		// "
		// + " order by b.code||a.field_code) where set_year="
		// + SessionUtil.getUserInfoContext().getSetYear();
		// return dao.findBySqlByUpper(sSql);
		DataDictBO dataDictBO = new DataDictBO();
		dataDictBO.setDao(dao);
		return dataDictBO.getDataSourceForTree(SessionUtil.getUserInfoContext()
				.getSetYear());
	}

	public XMLData getRepset(String setYear, int iTypeFlag, String sReportID)
			throws Exception {
		String sSql = "select * from fb_u_qr_repset where type_flag = "
				+ iTypeFlag + " and  set_year = " + setYear
				+ " and report_ID='" + sReportID + "'";
		List lstData = dao.findBySqlByUpper(sSql);
		if (lstData == null || lstData.size() == 0)
			return null;
		XMLData aData = new XMLData();
		aData.putAll((Map) lstData.get(0));
		return aData;
	}

	private List getReportSql(XMLData xmlReport, String report, String setYear) {

		List lstSql = new ArrayList();
		String sSql = "delete from fb_u_qr_repset where set_year=" + setYear
				+ " and report_id='" + report + "'";
		lstSql.add(sSql);
		StringBuffer sb = new StringBuffer();
		sb.append("insert into fb_u_qr_repset (set_year,").append(
				IQrBudget.TYPE_NO + ",").append(IQrBudget.REPORT_ID + ",")
				.append(IQrBudget.REPORT_CNAME + ",").append(
						IQrBudget.TITLE + ",").append(
						IQrBudget.REPORT_TYPE + ",").append(
						IQrBudget.REPORT_SOURCE + ",").append(
						IQrBudget.IS_PASSVERIFY + ",").append(
						IQrBudget.IS_ACTIVE + ",").append(
						IQrBudget.DATA_USER + ",").append(
						IQrBudget.IS_HASBATCH + ",").append(
						IQrBudget.IS_MULTICOND + ",").append(
						IQrBudget.IS_END + ",").append(IQrBudget.LVL_ID + ",")
				.append(IQrBudget.TYPE_FLAG + ",").append(
						IQrBudget.FIELD_COLUMN + "," + IQrBudget.RG_CODE + ")");
		sb.append("values(").append(setYear + ",").append("1,'").append(
				xmlReport.get(IQrBudget.REPORT_ID)).append("','").append(
				xmlReport.get(IQrBudget.REPORT_CNAME)).append("','").append(
				xmlReport.get(IQrBudget.TITLE)).append("',").append(
				"50,'定制','是','是',").append(xmlReport.get(IQrBudget.DATA_USER))
				.append(",'是','是',1,'").append(
						xmlReport.get(IQrBudget.LVL_ID)).append(
						"',2,").append(xmlReport.get(IQrBudget.FIELD_COLUMN))
				.append(
						",'"
								+ SessionUtilEx.getUserInfoContext()
										.getAttribute("cur_region") + "')");
		lstSql.add(sb.toString());
		return lstSql;

	}

	public int deleteRowSetReport(String reportID, String setYear)
			throws Exception {
		String sSql = "delete from fb_u_qr_repset where set_year=" + setYear
				+ " and report_id='" + reportID + "'";
		int i = dao.executeBySql(sSql);
		sSql = "delete from fb_u_qr_rowset where set_year=" + setYear
				+ " and report_id='" + reportID + "'";
		dao.executeBySql(sSql);

		sSql = "delete from fb_u_qr_rowset_con where set_year=" + setYear
				+ " and report_id='" + reportID + "'";
		dao.executeBySql(sSql);
		return i;
	}

	public int saveReportFile(String setYear, byte[] blobByte, String reportID) {
		// -----------

		List lstData = dao
				.findBySql("select 1 from FB_U_QR_GROUPREPORT where REPORT_ID= '"
						+ reportID + "' and set_year=" + setYear);
		boolean isAdd = true;
		if (lstData != null && lstData.size() > 0)
			isAdd = false;
		int i = 0;
		if (blobByte != null) {
			Session session = null;
			try {
				// Blob blob = null;
				session = dao.getSession();
				Connection conn = session.connection();
				// conn.setAutoCommit(false);
				// PreparedStatement pstmt;

				String blobField;
				if (Global.loginmode == 0) {// 在线
					blobField = "empty_blob()";

				} else
					// 离线
					blobField = "null";
				if (isAdd) {
					StringBuffer sSql = new StringBuffer();
					sSql.append(" insert into FB_U_QR_GROUPREPORT ");
					sSql.append(" (Set_year ,report_id,rg_code,FILESTREAM)");
					sSql.append(" values(?,?,?," + blobField + ")");
					PreparedStatement pstmt = conn.prepareStatement(sSql
							.toString());
					pstmt.setString(1, setYear);
					pstmt.setString(2, reportID);
					pstmt.setString(3, SessionUtil.getRgCode());
					pstmt.executeUpdate();
					pstmt.close();
				}

				// 采用共用的blob写入方法 by qinj at Nov 26, 2008
				i = QueryStub.getQueryTool().updateBlob("FB_U_QR_GROUPREPORT",
						"FILESTREAM", "report_id=? and set_year=?",
						new Object[] { reportID, setYear }, blobByte);

				// PreparedStatement pstmt;
				// pstmt = conn
				// .prepareStatement("select FILESTREAM from FB_U_QR_GROUPREPORT
				// where report_id= ? and set_year=? for update");
				// pstmt.setString(1, reportID);
				// pstmt.setString(2, setYear);
				// ResultSet rt = pstmt.executeQuery();
				// if (Global.loginmode == 0) {// 在线
				// if (rt.next())
				// blob = (Blob) rt.getBlob(1);
				//
				// if (null != blob) {
				// OutputStream out = null;
				// String app_server = (String) SessionUtil
				// .getServerInfo();
				// if (app_server == null || app_server.equals(""))
				// app_server = "tomcat";
				// else
				// app_server = app_server.toLowerCase().toString();
				// // weblogic
				// if (app_server.indexOf("weblogic") != -1)
				// out = ((OracleThinBlob) blob)
				// .getBinaryOutputStream();
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
				// ByteArrayInputStream bis = new ByteArrayInputStream(
				// blobByte);
				//
				// pstmt.setBinaryStream(1, bis, blobByte.length);
				// pstmt.setString(2, reportID);
				// pstmt.setString(3, setYear);
				// i = pstmt.executeUpdate();
				//
				// }
				// rt.close();
				// pstmt.close();

				return i;
			} catch (Exception e1) {
				Log.error(e1.getMessage());
				e1.printStackTrace();
			} finally {
				dao.closeSession(session);
			}
		}
		return 0;
	}

	/**
	 * 取得ID中的图片信息
	 * 
	 * @param IDs
	 * @return
	 * @throws Exception
	 */
	public byte[] getOBByID(String setYear, String reportID) throws Exception {
		if (reportID == null)
			return null;

		// 采用共用方法读取blob by qinj at Nov 26, 2008
		String sSql = "select FILESTREAM from FB_U_QR_GROUPREPORT where report_id=? and set_year=?";
		return QueryStub.getQueryTool().getBlob(sSql,
				new Object[] { reportID, setYear }, "FILESTREAM");

		// Session session = dao.getSession();
		// try {
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
		// if (Global.loginmode == 0) {// 在线
		// Blob blob = (Blob) rt.getBlob("filestream");
		// int iLength = (int) blob.length();
		// result = new byte[iLength];
		// input = blob.getBinaryStream();
		// input.read(result);
		// input.close();
		//
		// } else {// 离线
		// input = rt.getBinaryStream(1);
		// result = new byte[input.available()];
		// input.read(result);
		// input.close();
		// }
		//
		// pstmt.close();
		// rt.close();
		// return result;
		// } catch (Exception e) {
		// Log.error(e.getMessage());
		// throw e;
		// } finally {
		// if (session != null)
		// dao.closeSession(session);
		// }

	}

	public XMLData getDataSourceTables(String setYear) {
		String sSql = "select TABLE_NAME_DIV,OBJECT_ENAME where set_year="
				+ setYear;
		List lstData = dao.findBySql(sSql);
		if (lstData == null || lstData.size() == 0)
			return new XMLData();
		int iCount = lstData.size();
		XMLData aData;
		XMLData result = new XMLData();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstData.get(i);
			result.put(aData.get("object_ename"), "table_name_div");
		}
		return result;
	}

	public DataSet getEmptyField() throws Exception {
		String sSql = " select   Field_Id, FIELD_CODE ,FIELD_CNAME,FIELD_ENAME,FIELD_FNAME,FIELD_TYPE,FIELD_DISPWIDTH,FIELD_DISFORMAT,IS_LEAF "
				+ " from fb_u_qr_colset where 1=2 ";

		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	/**
	 * 行报表引擎查询
	 * 
	 * @param rows
	 *            一行显示的数量
	 * @param reportID报表ID
	 * @param depts
	 *            部门条件
	 * @param divs
	 *            单位条件
	 * @param sVerNo
	 *            数据版本
	 * @param sBatchNoFilter
	 *            批次
	 * @param iLoginmode
	 *            登录方式
	 * @return
	 */
	public XMLData getRowSetData(int rows, String reportID, List depts,
			List divs, String sVerNo, String sBatchNoFilter, int iLoginmode) {
		RowSetEngine rowSetEngine = new RowSetEngine(rows, reportID, depts,
				divs, sVerNo, sBatchNoFilter, iLoginmode, dao);
		return rowSetEngine.doSearch();

	}

	public void executeQuery(String sSql) throws Exception {
		dao.findBySql(sSql);
	}

	public List getReportCons(String reportID, String setYear) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getReportRows(String reportID, String setYear) {
		// TODO Auto-generated method stub
		return null;
	}
}
