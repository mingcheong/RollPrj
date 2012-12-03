/**
 * Copyright 浙江易桥
 * 

 * 
 * @title 定制查询报表-实现类
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.sys.besqryreport.bs;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class BesQryReportBO implements IBesQryReport {

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

	private String sGetSourceSql = "SELECT TNAME,case TABTYPE when 'TABLE' then '基表' else '视图' end  TABTYPE FROM TAB WHERE TNAME LIKE '%FB_%' AND TABTYPE = 'VIEW'";

	public DataSet getDataSet(String aTable, String aFilter) throws Exception {
		DataSet ds = DataSet.create();
		String sFilter = (Common.isNullStr(aFilter)) ? "" : " WHERE " + aFilter;
		String sql = "SELECT * FROM " + aTable + sFilter;
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}

	public DataSet getSourceData() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(sGetSourceSql);
		ds.open();
		return ds;
	}

	/**
	 * 返回所选表的所有的列信息
	 */
	public DataSet getFieldInfo(String aTableName) throws Exception {
		if (Common.isNullStr(aTableName))
			return null;
		DataSet ds = DataSet.create();
		String loginMode = SessionUtil.getUserInfoContext().getLoginMode();
		String sSql;
		if ("0".equals(loginMode)) {// 在线
			sSql = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE,lpad(COLUMN_ID, 5, 0) as COLUMN_ID"
					+ " FROM COLS WHERE TABLE_NAME = UPPER('"
					+ aTableName.toUpperCase() + "' ) ";
		} else {// 离线
			sSql = "SELECT a.name as TABLE_NAME, b.name as COLUMN_NAME, b.type as DATA_TYPE,"
					+ "replicate(0,5-length(colid))||colid as COLUMN_ID"
					+ " FROM dbo.sysobjects a, dbo.syscolumns b"
					+ " WHERE a.name = UPPER('fb_u_div_incoming' ) and a.id=b.id";
		}
		ds.setSqlClause(sSql);
		ds.open();
		return ds;
	}

	/**
	 * 获取表里某字段的最大值
	 */
	public String getMaxValueFromField(String aTableName, String aFieldName,
			String aFilter) throws Exception {
		return DBSqlExec.getMaxValueFromField(aTableName, aFieldName, "");
	}

	/**
	 * 保存报表
	 */
	public void execSql(List sqls, List lstSqllines, String sReportID,
			List lstType) throws Exception {
		QueryStub.getQueryTool().executeBatch(sqls);

		// 得到登陆年份
		String setYear = SessionUtil.getUserInfoContext().getSetYear();
		String sRgCode = (String) SessionUtil.getUserInfoContext()
				.getAttribute("cur_region");

		Session session = null; 
		try { 
			session = dao.getSession();
			Connection con = session.connection();
			PreparedStatement smt = null;
			int size = lstSqllines.size();
			Map mapSqlLines;
			String sqllines;
			String sqllinesValue;
	
			for (int i = 0; i < size; i++) {
				mapSqlLines = (Map) lstSqllines.get(i);
				if (!"LASTQUERY".equals(mapSqlLines.get(IBesQryReport.SQLTYPE)))
					sqllinesValue = getSql(mapSqlLines.get(IBesQryReport.VIEWNAME)
							.toString());
				else
					sqllinesValue = "";
				sqllines = mapSqlLines.get(IBesQryReport.SQLLINES).toString();
	
				smt = con.prepareStatement(sqllines);
				smt.setCharacterStream(1, new StringReader(sqllinesValue),
						sqllinesValue.length());
				smt.executeQuery();
			}
	
			dao
					.executeBySql("delete from fb_u_qr_report_to_type where REPORT_ID= '"
							+ sReportID + "' and set_year=" + setYear);
			// 保存fb_u_qr_report_to_type
			size = lstType.size();
			for (int i = 0; i < size; i++) {
				dao
						.executeBySql(
								"insert into fb_u_qr_report_to_type(report_id,type_code,set_year,rg_code) values (?,?,?,?)",
								new Object[] { sReportID, lstType.get(i), setYear,
										sRgCode });
			}
		} finally {
			dao.closeSession(session);
		}

	}

	/**
	 * 获取建立试图语句信息
	 */
	public String getSql(String aViewName) throws Exception {
		String sql = "";
		String sFilter = IBesQryReport.VIEW_NAME + "='" + aViewName + "'";
		List lstResult = dao.findBySql("select " + IBesQryReport.TEXT
				+ " from " + IBesQryReport.TABLE_SQLCOLS + " where " + sFilter);
		if (lstResult == null || lstResult.size() == 0)
			return null;
		sql = (String) ((XMLData) lstResult.get(0)).get(IBesQryReport.TEXT);

		// DataSet dsSql = getDataSet(IBesQryReport.TABLE_SQLCOLS, sFilter);
		// dsSql.beforeFirst();
		// dsSql.next();
		// sql = dsSql.fieldByName(IBesQryReport.TEXT).getString();
		// sql = sql.replaceAll("'", "''");
		return sql;
	}
}
