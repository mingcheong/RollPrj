package gov.nbcs.rp.generateBudget.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.generateBudget.ibs.IGeBudget;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class GeBudgetBO implements IGeBudget {
	private GeneralDAO dao;

	public GeneralDAO getDao() {
		return this.dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	public DataSet getDepToDivData(String sYear, int levl, boolean isRight,
			int batch_no) throws Exception {
		String sSql = "";
		if (isRight) {
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
					.getServerBean("sys.controlDictionaryService");

			String swhere = iCDictionary
					.getSqlElemRight(
							SessionUtil.getUserInfoContext().getUserID(),
							SessionUtil.getUserInfoContext().getRoleID(), "EN",
							"ENTAB").toUpperCase();

			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE",
					"ENTAB.Div_Code");

			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a  where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code = a.Dep_code and a.set_year=ENTAB.set_year "
					+ swhere
					+ " )"
					+ " and a.set_year ="
					+ sYear
					+ " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB "
					+ " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year="
					+ sYear
					+ " and a.level_num <= "
					+ String.valueOf(levl)
					+ " " + swhere;
		} else {
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a  where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year) and a.set_year ="
					+ sYear
					+ " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB "
					+ " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year="
					+ sYear;
		}

		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}

	public DataSet getDivTreePop(String sYear, int batch_no) throws Exception {
		DataSet dsData = DataSet.create();

		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil
				.getServerBean("sys.controlDictionaryService");

		String swhere = iCDictionary.getSqlElemRight(
				SessionUtil.getUserInfoContext().getUserID().toLowerCase(),
				SessionUtil.getUserInfoContext().getRoleID(), "en", "entab")
				.toUpperCase();

		swhere = StringUtils.replace(swhere, "entab.chr_id", "entab.en_id");
		swhere = StringUtils
				.replace(swhere, "entab.chr_code", "entab.div_code");

		String sSql = " select * from vw_rp_division entab  where is_leaf = 1 and set_year="
				+ sYear
				+ " "
				+ swhere
				+ " union all "
				+ " select * from vw_rp_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year="
				+ sYear
				+ " and "
				+ " exists (select 1 from vw_rp_division entab where ENTAB.div_code like tab.div_code||'%'"
				+ " and entab.is_leaf = 1 and entab.set_year="
				+ sYear
				+ " "
				+ swhere + ")";

		DBSqlExec.getDataSet(sSql, dsData);

		return dsData;

	}

	// private String getAuditDivFilter(int batch_no) {
	// StringBuffer sql = new StringBuffer();
	// sql.append(" where exists(");
	// sql
	// .append(" select 1 from rp_xmjl b where (b.div_code = a.div_code or
	// substring(b.div_code,1,3)=a.div_code)");
	//
	// sql.append(" and b.batch_no = " + batch_no + " and b.data_type = 1)");
	// sql.append(" order by div_code");
	// return sql.toString();
	// }

	public DataSet findProjectByPara(String[] divCodes, String setYear,
			String queryType) {
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "")
				.replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);
		try {
			DataSet filter = DBSqlExec
					.getDataSet("select commvlue from rp_s_support where suptype='TOBUDGET'");

			sql.append("select ");
			sql
					.append("distinct a.en_id, a.div_code, a.div_name,a.prj_code,a.prj_name,");
			sql.append(" a.gkdw_code,a.gkdw_name, ");
			sql.append("cast((select t.f1 from vw_fb_p_detail_pfs t ");
			sql
					.append(" where t.prj_code=a.prj_code and t.detail_type='111') as decimal(16, 2)) as total_money");
			sql.append(",cast((select t.f1   ");

			sql.append("from vw_fb_p_detail_pfs t");
			sql
					.append(" where t.prj_code=a.prj_code  and t.detail_type='333') as decimal(16, 2)) as cur_money");
			sql.append(" from vw_fb_p_base a, vw_fb_p_detail_pfs b");
			sql
					.append(" where "
							+ filter.fieldByName("commvlue").getValue()
									.toString() + "");
			sql
					.append(" and a.prj_code = b.prj_code and a.div_code = b.div_code");
			sql
					.append(" and not exists (select 1 from fb_p_base  where prj_code =a.prj_code ) ");

			String sqltr = "create table " + sTableName
					+ " (divCode  VARCHAR2(60))";
			dao.executeBySql(sqltr);
			if ("1".equals(queryType)) {
				sqltr = "";
				for (int i = 0; i < divCodes.length; i++) {
					sqltr = "insert into " + sTableName + "(divCode) values ('"
							+ divCodes[i] + "')";
					dao.executeBySql(sqltr);
				}
				sql.append(" and exists (");
				sql.append("select divcode from " + sTableName
						+ " c where a.en_id = c.divcode)");

				sql.append(" order by a.div_code,a.prj_code,total_money");
				DBSqlExec.getDataSet(sql.toString(), dsData);
			} else {
				sql.append(" order by a.div_code,a.prj_code,total_money");
				DBSqlExec.getDataSet(sql.toString(), dsData);
			}

			dsData.beforeFirst();
			float sum1 = 0;
			float sum2 = 0;
			float cur1 = 0;
			float cur2 = 0;
			while (dsData.next()) {
				cur1 = dsData.fieldByName("cur_money").getFloat();
				cur2 = dsData.fieldByName("total_money").getFloat();
				sum1 = sum1 + cur1;
				sum2 = sum2 + cur2;
			}
			dsData.append();
			dsData.fieldByName("total_money").setValue(String.valueOf(sum1));
			dsData.fieldByName("cur_money").setValue(String.valueOf(sum2));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.executeBySql("drop table " + sTableName);
		}

		return dsData;
	}

	public String findkmdm(String xmxh) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer value = new StringBuffer();
		sql.append("select a.*,b.Acct_Code,b.acct_name,");
		sql.append("b.Acct_Code||' '||b.acct_name as fullname");
		sql.append(" from rp_xmjl_km a");
		sql.append(" left join vw_fb_acct_func b");
		sql.append(" on a.kmdm = b.BS_ID");
		sql.append(" where a.xmxh = '");
		sql.append(xmxh);
		sql.append("'");
		int i = 0;
		DataSet ds = DBSqlExec.getDataSet(sql.toString());
		if ((ds != null) && (!ds.isEmpty()))
			ds.beforeFirst();
		while (ds.next()) {
			if (i++ != 0) {
				value.append(";");
			}
			value.append(ds.fieldByName("fullname").getString());
			continue;

		}
		return value.toString();
	}

	public void gunDongNextyear(String xmbms, String[] xmxhs, String UserCode,
			String setYear, String rgCode) throws Exception {
		List list = new ArrayList();
		String xmxh = "";

		int year = Integer.valueOf(setYear).intValue() + 1;
		String sqlpost = "delete from rp_xmjl_km where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";
		String sqlpost2 = "delete from rp_xmsb where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";
		String sqlpost3 = "delete from rp_xmjl where xmbm in (" + xmbms
				+ ") and set_year=" + year + "";

		String sqlpost4 = "delete from rp_xmsb_ly where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";

		list.add(sqlpost.toString());
		list.add(sqlpost2.toString());
		list.add(sqlpost3.toString());
		list.add(sqlpost4.toString());

		for (int i = 0; i < xmxhs.length; i++) {
			xmxh = UUIDRandom.generate();
			StringBuffer sql = new StringBuffer();
			sql
					.append("insert into rp_xmjl (select '"
							+ year
							+ "', '"
							+ xmxh
							+ "', XMBM, XMMC, C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, EN_ID, RG_CODE, '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, 1, DIV_CODE, DIV_NAME, CS_SORT, EN_SORT, GKDW_ID, GKDW_CODE,ACCTS,GKDW_NAME ");
			sql.append(" from rp_xmjl where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql1 = new StringBuffer();
			sql1.append("insert into rp_xmsb_ly (select " + year
					+ ", newid, RG_CODE, '" + xmxh + "', LY");
			sql1.append(" from rp_xmsb_ly where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql2 = new StringBuffer();

			sql2.append("insert into rp_xmjl_km (select '" + year
					+ "', newid,'" + xmxh + "',KMDM from rp_xmjl_km");
			sql2.append(" where xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql3 = new StringBuffer();
			sql3.append("insert into rp_xmsb (");
			sql3
					.append("select "
							+ year
							+ ", newid, '"
							+ xmxh
							+ "', YSJC_DM, BS_ID, BSI_ID, F1, F2, F3, F4, F5, F6, F7, F8, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, BZ, SB_CODE, TOTAL_SUM,' ',' ',' ',f9,f10,f11,f12 from ");
			sql3.append("rp_xmsb where sb_code='111' ");
			sql3.append("and xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql4 = new StringBuffer();
			sql4.append("insert into rp_xmsb (");
			sql4
					.append("select "
							+ year
							+ ", newid, '"
							+ xmxh
							+ "', YSJC_DM, BS_ID, BSI_ID, 0, 0, 0, 0, 0, 0, 0, 0, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, BZ, SB_CODE, 0,' ',' ',' ',0,0,0,0 from ");
			sql4.append("rp_xmsb where sb_code not in ('111','222') ");
			sql4.append("and xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql5 = new StringBuffer();
			sql5.append("insert into rp_xmsb (");
			sql5
					.append("select "
							+ year
							+ ", newid, '"
							+ xmxh
							+ "', '', ' ', ' ', sum(nvl(f1,0)), sum(nvl(f2,0)), sum(nvl(f3,0)), 0, 0, sum(nvl(f6,0)), sum(nvl(f7,0)), sum(nvl(f8,0)), ' ', '"
							+ rgCode
							+ "', '已安排数', '', '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, '', '222', sum(TOTAL_SUM),'','','',sum(nvl(f9,0)),sum(nvl(f10,0)),sum(nvl(f11,0)),sum(nvl(f12,0)) from ");
			sql5.append("rp_xmsb where sb_code  in ('222','333') ");
			sql5.append("and xmxh = '" + xmxhs[i] + "') ");
			list.add(sql.toString());
			list.add(sql1.toString());
			list.add(sql2.toString());
			list.add(sql3.toString());
			list.add(sql4.toString());
			list.add(sql5.toString());

			if (list.size() > 0) {
				QueryStub.getQueryTool().executeBatch(list);
			}

		}

		List list1 = new ArrayList();
		StringBuffer sqlh = new StringBuffer();
		sqlh.append("delete from rp_xmjl_history where xmbm in (" + xmbms
				+ ") and set_year = " + year + " and batch_no = 0"
				+ " and step_id= 5");

		StringBuffer sqlh1 = new StringBuffer();
		sqlh1.append(" insert into rp_xmjl_history");
		sqlh1.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append(" accts, step_id, batch_no,his_no)");
		sqlh1.append("select ");
		sqlh1.append(" set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append("accts,5,0,1");
		sqlh1.append(" from rp_xmjl ");
		sqlh1.append(" where xmbm in (" + xmbms + ") and ");

		sqlh1.append(" set_year = " + year);
		StringBuffer sqlh2 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		sql2.append("delete from rp_xmjl_km_his where xmbm in (" + xmbms
				+ ") and set_year = " + year);

		StringBuffer sqlh22 = new StringBuffer();
		StringBuffer sql22 = new StringBuffer();

		sql22
				.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmbm in ("
						+ xmbms + ") and set_year = " + year);

		String sqlsb_his_del = "delete from rp_xmsb_history  where step_id=5 and batch_no=0 and his_no=1 and xmxh in (select  xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year = " + year + ")";

		String sqlsb_his = "insert into rp_xmsb_history(select a.*,5,0,1 from rp_xmsb  a where a.xmxh in(select  xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year = " + year + "))";

		list1.add(sqlh.toString());
		list1.add(sqlh1.toString());
		list1.add(sqlh2.toString());
		list1.add(sqlh22.toString());
		list1.add(sqlsb_his_del.toString());
		list1.add(sqlsb_his.toString());
		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);
	}

	public void gunDongNextyearLXXM(String xmbms, String[] xmxhs,
			String UserCode, String setYear, String rgCode) throws Exception {
		List list = new ArrayList();
		String xmxh = "";

		int year = Integer.valueOf(setYear).intValue() + 1;
		String sqlpost = "delete from rp_xmjl_km where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";
		String sqlpost2 = "delete from rp_xmsb where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";
		String sqlpost3 = "delete from rp_xmjl where xmbm in (" + xmbms
				+ ") and set_year=" + year + "";

		String sqlpost4 = "delete from rp_xmsb_ly where xmxh in (select xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year=" + year + ")";

		list.add(sqlpost.toString());
		list.add(sqlpost2.toString());
		list.add(sqlpost3.toString());
		list.add(sqlpost4.toString());

		for (int i = 0; i < xmxhs.length; i++) {
			xmxh = UUIDRandom.generate();
			StringBuffer sql = new StringBuffer();
			sql
					.append("insert into rp_xmjl (select '"
							+ year
							+ "', '"
							+ xmxh
							+ "', XMBM, XMMC, '"
							+ year
							+ "', '"
							+ year
							+ "', C3, C4, C5, C6, C7, C8, C9, C10, EN_ID, RG_CODE, '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, 1, DIV_CODE, DIV_NAME, CS_SORT, EN_SORT, GKDW_ID, GKDW_CODE,ACCTS,GKDW_NAME ");
			sql.append(" from rp_xmjl where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql1 = new StringBuffer();
			sql1.append("insert into rp_xmsb_ly (select " + year
					+ ", newid, RG_CODE, '" + xmxh + "', LY");
			sql1.append(" from rp_xmsb_ly where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql2 = new StringBuffer();

			sql2.append("insert into rp_xmjl_km (select '" + year
					+ "', newid,'" + xmxh + "',KMDM from rp_xmjl_km");
			sql2.append(" where xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql3 = new StringBuffer();
			sql3.append("insert into rp_xmsb (");
			sql3
					.append("select "
							+ year
							+ ", newid, '"
							+ xmxh
							+ "', YSJC_DM, BS_ID, BSI_ID, F1, F2, F3, F4, F5, F6, F7, F8, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '"
							+ UserCode
							+ "', sysdate, '"
							+ UserCode
							+ "', sysdate, BZ, SB_CODE, TOTAL_SUM,ysjc_mc,acct_name,acct_name_jj,f9,f10,f11,f12 from ");
			sql3.append("rp_xmsb  where 1=1 ");

			sql3.append("and xmxh = '" + xmxhs[i] + "')");

			list.add(sql.toString());
			list.add(sql1.toString());
			list.add(sql2.toString());
			list.add(sql3.toString());
		}

		if (list.size() > 0) {
			QueryStub.getQueryTool().executeBatch(list);
		}
		List list1 = new ArrayList();
		StringBuffer sqlh = new StringBuffer();
		sqlh.append("delete from rp_xmjl_history where xmbm in (" + xmbms
				+ ") and set_year = " + year + " and batch_no = 0"
				+ " and step_id= 5");

		StringBuffer sqlh1 = new StringBuffer();
		sqlh1.append(" insert into rp_xmjl_history");
		sqlh1.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append(" accts,gkdw_name, step_id, batch_no,his_no)");
		sqlh1.append("select ");
		sqlh1.append(" set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append("accts,gkdw_name,5,0,1");
		sqlh1.append(" from rp_xmjl ");
		sqlh1.append(" where xmbm in (" + xmbms + ") and ");

		sqlh1.append(" set_year = " + year);
		StringBuffer sqlh2 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		sql2.append("delete from rp_xmjl_km_his where xmbm in (" + xmbms
				+ ") and set_year = " + year);

		StringBuffer sqlh22 = new StringBuffer();
		StringBuffer sql22 = new StringBuffer();
		sql22
				.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmbm in ("
						+ xmbms + ") and set_year = " + year);

		String sqlsb_his_del = "delete from rp_xmsb_history  where step_id=5 and batch_no=0 and his_no=1 and xmxh in (select  xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year = " + year + ")";

		String sqlsb_his = "insert into rp_xmsb_history(select a.*,5,0,1 from rp_xmsb  a where a.xmxh in(select  xmxh from rp_xmjl where xmbm in ("
				+ xmbms + ") and set_year = " + year + "))";

		list1.add(sqlh.toString());
		list1.add(sqlh1.toString());
		list1.add(sqlh2.toString());
		list1.add(sqlh22.toString());
		list1.add(sqlsb_his_del.toString());
		list1.add(sqlsb_his.toString());

		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);
	}

	public void finishSameBudget(String xmbms, String xmxhs, String userCode,
			String setYear, String rgCode) throws Exception {
		List list = new ArrayList();
		StringBuffer sqlpost = new StringBuffer();
		sqlpost.append("delete from rp_finishSameBt where prj_id in (" + xmxhs
				+ ") and set_year ='" + setYear + "'");

		StringBuffer sql = new StringBuffer();
		sql.append("insert into rp_finishSameBt (select '" + setYear + "', '"
				+ rgCode + "',XMXH, XMBM, XMMC, sysdate");
		sql.append(" from rp_xmjl where xmxh in (" + xmxhs + "))");
		list.add(sqlpost.toString());
		list.add(sql.toString());

		QueryStub.getQueryTool().executeBatch(list);
	}

	public void changeHs(String setYear, String batchNo, String dateType,
			String table1, String table2) throws Exception {
		Connection conn = null;
		CallableStatement call = null;
		Session session = null;
		try {
			session = this.dao.getSession();
			if (session == null) {
				throw new Exception("数据库连接已关闭,无法使用");
			}
			conn = session.connection();
			call = conn.prepareCall("{? =call gcgl_execute_batch(?,?,?,?,?)}");
			call.registerOutParameter(1, 2);
			call.registerOutParameter(6, 12);
			call.setString(2, setYear);
			call.setString(3, batchNo);
			call.setString(4, dateType);
			call.setString(5, table1);
			call.setString(6, table2);
			call.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (call != null) {
					call.close();
					call = null;
				}
				if (session != null)
					this.dao.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String insertTempBudget(String setYear, String batchNo,
			String dateType, List lBudgetData) throws Exception {
		List sqlList = new ArrayList();
		String sum = "";
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "")
				.replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);

		String sDblink = "";
		sDblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");

		String sCreateTempTable = "create table "
				+ sTableName
				+ "(div_code varchar2(40),prj_code varchar2(20),prj_name varchar2(100),gkdw_code varchar2(20))";

		dao.executeBySql(sCreateTempTable);
		for (int i = 0; i < lBudgetData.size(); i++) {
			String filter = lBudgetData.get(i).toString();
			String[] sData = new String[4];
			sData = filter.split(",");
			String s = "insert into " + sTableName
					+ "(div_code,prj_code,prj_name,gkdw_code)values('"
					+ sData[0] + "','" + sData[1] + "','" + sData[2] + "','"
					+ sData[3] + "')";
			dao.executeBySql(s);
		}

		StringBuffer sInsFb_baseSql = new StringBuffer();
		sInsFb_baseSql.append("insert into fb_p_base(");
		sInsFb_baseSql.append("row_id,set_year,");
		sInsFb_baseSql.append("batch_no,data_type ,");
		sInsFb_baseSql.append("ver_no,opt_no,data_src,");
		sInsFb_baseSql.append("data_attr,prj_type ,en_id,");
		sInsFb_baseSql.append("div_kind,div_code,div_name,");
		sInsFb_baseSql.append("mb_id,dept_code,dept_name ,");
		sInsFb_baseSql.append("prj_code,prj_name ,bs_id,acct_code,");
		sInsFb_baseSql.append("acct_name,prjsort_code,prjattr_code,");
		sInsFb_baseSql.append("prjaccord_code ,prjstd_code,stock_flag,");
		sInsFb_baseSql.append("stock_type,start_date,end_date,prj_content,");
		sInsFb_baseSql.append("prj_manager,sort_all,sort_charge,sort_div,");
		sInsFb_baseSql
				.append("prj_status,revisectl_code,report_flag,audit_status,");
		sInsFb_baseSql.append("isperennial,detail_degree,c1,c2,c3,c4,c5,c6,");
		sInsFb_baseSql.append("c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,");
		sInsFb_baseSql.append("c19,c20,cl1,cl2,cl3,f1,f2,f3,f4,f5,n1,n2,");
		sInsFb_baseSql.append("n3,n4,n5,d1,d2,rg_code,last_ver,prj_parent,");
		sInsFb_baseSql.append("dep_status,fis_status,prj_transfer,ca1,ca2,");
		sInsFb_baseSql.append("ca3,ca4,ca5,cs1,cs2,cs3,cs4,cs5)");
		sInsFb_baseSql.append("(select");
		sInsFb_baseSql.append(" row_id,set_year,'");
		sInsFb_baseSql.append(batchNo + "','" + dateType + "',");
		sInsFb_baseSql.append("ver_no,opt_no,");
		sInsFb_baseSql.append("data_src,data_attr,prj_type ,gkdw_id,");
		sInsFb_baseSql.append("div_kind,gkdw_code,gkdw_name,mb_id,");
		sInsFb_baseSql.append("dept_code,dept_name ,prj_code,prj_name ,");
		sInsFb_baseSql.append("bs_id,acct_code,acct_name,prjsort_code,");
		sInsFb_baseSql.append("prjattr_code,prjaccord_code ,prjstd_code,");
		sInsFb_baseSql.append("stock_flag,stock_type,start_date ,");
		sInsFb_baseSql.append("end_date,prj_content ,prj_manager ,");
		sInsFb_baseSql.append("sort_all ,sort_charge ,sort_div , ");
		sInsFb_baseSql.append("prj_status ,audit_status ,isperennial,");
		sInsFb_baseSql.append("revisectl_code ,report_flag ,detail_degree, ");
		sInsFb_baseSql.append("c1,c2,c3 ,c4 ,c5 ,c6 ,");
		sInsFb_baseSql.append("c7 ,c8 ,c9 ,c10,c11,c12,");
		sInsFb_baseSql.append("c13,c14,c15,c16,c17,c18,");
		sInsFb_baseSql.append("c19,c20,cl1,cl2,cl3,f1 ,");
		sInsFb_baseSql.append("f2 ,f3 ,f4 ,f5 ,n1 ,n2 ,");
		sInsFb_baseSql.append("n3 ,n4 ,n5 ,d1 ,d2 ,rg_code , ");
		sInsFb_baseSql.append("last_ver , prj_parent,dep_status,");
		sInsFb_baseSql.append("fis_status,prj_transfer,ca1,ca2,");
		sInsFb_baseSql.append("ca3,ca4,ca5,cs1,cs2,cs3,");
		sInsFb_baseSql.append("cs4,cs5 ");
		sInsFb_baseSql
				.append(" from vw_fb_p_base r where set_year='"
						+ setYear
						+ "' and exists (select 1 from "
						+ sTableName
						+ " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		DataSet filter = DBSqlExec
				.getDataSet("select efield from rp_s_support where suptype='TOBUDGET'");
		String filterE = filter.fieldByName("efield").getValue().toString();

		StringBuffer sInsFb_deSql = new StringBuffer();
		sInsFb_deSql.append("insert into fb_p_detail_pfs(");
		sInsFb_deSql.append("row_id,set_year,");
		sInsFb_deSql.append("ver_no,opt_no,batch_no,");
		sInsFb_deSql.append("data_type,data_src,data_attr,");
		sInsFb_deSql.append("en_id,div_kind,div_code,div_name,");
		sInsFb_deSql.append("detail_type,detail_type_name,prj_code,");
		sInsFb_deSql.append("prj_name,bs_id,acct_code,acct_name,bsi_id,");
		sInsFb_deSql.append("acct_code_jj,acct_name_jj,start_date,end_date,");
		sInsFb_deSql.append("stock_flag,total_prices," + filterE + ")(");
		sInsFb_deSql.append("select ");
		sInsFb_deSql.append(" row_id,set_year,");
		sInsFb_deSql.append(" ver_no,opt_no,'" + batchNo + "','" + dateType);
		sInsFb_deSql.append("',data_src,data_attr,");
		sInsFb_deSql.append("gkdw_id,div_kind,gkdw_code,gkdw_name,");
		sInsFb_deSql.append("detail_type,detail_type_name,prj_code,");
		sInsFb_deSql.append("prj_name,bs_id,acct_code,acct_name,bsi_id,");
		sInsFb_deSql.append("acct_code_jj,acct_name_jj,start_date,end_date,");
		sInsFb_deSql.append("stock_flag,total_prices," + filterE);
		sInsFb_deSql.append(" from vw_fb_p_detail_pfs r ");
		sInsFb_deSql
				.append(" where set_year='"
						+ setYear
						+ "' and exists (select 1 from "
						+ sTableName
						+ " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		String sDelOldFb_baseSql = "delete from fb_p_base  r where set_year='"
				+ setYear + "' and batch_no='" + batchNo + "'"
				+ " and data_type='" + dateType + "'"
				+ " and exists (select 1 from " + sTableName
				+ " t where r.prj_code=t.gkdw_code )";

		String sDelOldFb_deSql = "delete from fb_p_detail_pfs  r where set_year='"
				+ setYear
				+ "' and batch_no='"
				+ batchNo
				+ "'"
				+ " and data_type='"
				+ dateType
				+ "'"
				+ " and exists (select 1 from "
				+ sTableName
				+ " t where r.prj_code=t.gkdw_code  )";

		sqlList.add(sDelOldFb_baseSql);
		sqlList.add(sDelOldFb_deSql);
		sqlList.add(sInsFb_baseSql.toString());
		sqlList.add(sInsFb_deSql.toString());
		QueryStub.getQueryTool().executeBatch(sqlList);
		String sUpdateFb = "update fb_u_payout_budget@" + sDblink
				+ " a set a.stock_flag = 1 where exists(select 1 "
				+ "from fb_u_payout_gov_purchase@" + sDblink
				+ " b where a.project_code ="
				+ "b.project_code and a.batch_no ='" + batchNo
				+ "' and a.data_type='" + dateType + "')";
		dao.executeBySql(sUpdateFb);
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select sum (f1) F1 from vw_fb_p_detail_pfs r where set_year='"
						+ setYear
						+ "' and exists (select 1 from "
						+ sTableName
						+ " t where r.div_code=t.div_code and r.prj_code=t.prj_code ) and detail_type=333 ");

		if (dao.findBySql(sql.toString()).get(0) != null) {
			sum = ((Map) (dao.findBySql(sql.toString()).get(0))).get("f1")
					.toString();
		}
		dao.executeBySql("drop table " + sTableName);
		return sum;
	}

	public DataSet findRPBUDGET(String[] divCodes, Map parMap, String setYear)
			throws Exception {

		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from VW_RP_P_BUDGET a where 1=1 and a.set_year='"
				+ setYear + "' ");

		String fxmbm = (String) parMap.get("fxmbm");
		String fxmmc = (String) parMap.get("fxmmc");
		String batch_no = (String) parMap.get("batch_no");
		String data_type = (String) parMap.get("data_type");

		sql.append(" and a.en_id in (");
		for (int i = 0; i < divCodes.length; i++) {
			if (i == 0)
				sql.append("'");
			else {
				sql.append(",'");
			}
			sql.append(divCodes[i]);
			sql.append("'");
		}

		sql.append(")");
		if (!"".equals(fxmbm)) {
			sql.append(" and a.prj_code like '%" + fxmbm + "%'");
		}
		if ((!"".equals(fxmmc)) && (!"null".equals(fxmmc)) && (fxmmc != null)) {
			sql.append(" and a.prj_name like '%" + fxmmc + "%'");
		}
		if ((!"".equals(batch_no)) && (!"null".equals(batch_no))) {
			sql.append(" and a.batch_no = " + batch_no + "");
		}
		if ((!"".equals(data_type)) && (!"null".equals(data_type))) {
			sql.append(" and a.data_type = " + data_type + "");
		}
		sql.append("  order by a.div_code,a.prj_code");

		DBSqlExec.getDataSet(sql.toString(), dsData);

		return dsData;
	}

	public void DeleteTempPro(String wheresql, String setYear, String rgCode)
			throws Exception {
		List list = new ArrayList();
		StringBuffer sqlh = new StringBuffer();
		StringBuffer sqlhdetail = new StringBuffer();
		sqlh.append("delete from fb_p_base  where 1=1 " + wheresql);
		sqlhdetail.append("delete from fb_p_detail_pfs where 1=1" + wheresql);
		list.add(sqlh.toString());
		list.add(sqlhdetail.toString());

		if (list.size() > 0)
			QueryStub.getQueryTool().executeBatch(list);
	}

	public DataSet findDoProjectByPara(String[] divCodes, String loginYear,
			String filter1, String queryType) {
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "")
				.replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);
		try {
			// String sDblink = "";
			// sDblink = DBSqlExec
			// .getStringValue("select dblink_name from rp_scheme");
			DataSet filter = DBSqlExec
					.getDataSet("select commvlue from rp_s_support where suptype='TOBUDGET'");
			sql
					.append("select distinct a.en_id, a.div_code, a.div_name,a.prj_code,a.prj_name,");
			sql.append("cast((select t.f1 from fb_p_detail_pfs t");
			sql.append(" where t.prj_code=a.prj_code  ");
			sql.append(" and t.batch_no = a.batch_no ");
			sql.append(" and t.data_type = a.data_type ");
			sql.append(" and t.div_code=a.div_code ");
			sql.append(" and t.detail_type = '111' ) ");
			sql.append(" as decimal(16, 2)) as total_money");
			sql.append(",cast((select t.f1 ");
			sql.append("from fb_p_detail_pfs t ");
			sql.append("where t.prj_code=a.prj_code  ");
			sql.append("and t.batch_no = a.batch_no ");
			sql.append("and t.div_code=a.div_code ");
			sql.append("and t.detail_type = '333' ");
			sql.append("and t.data_type = a.data_type) ");
			sql.append("as decimal(16, 2)) as cur_money");
			// sql.append("decode(a.batch_no, '1', '一上', '2', '二上') as
			// batch_no,");
			// sql.append("decode(a.data_type, '1', '财政审核数', '0', '原始数')
			// data_type ");
			sql.append(" from fb_p_base a, fb_p_detail_pfs b");
			sql
					.append(" where "
							+ filter.fieldByName("commvlue").getValue()
									.toString() + "");
			sql
					.append(" and a.prj_code = b.prj_code and a.div_code = b.div_code");
			String sqltr = "create table " + sTableName
					+ " (divCode  varchar2(60))";
			dao.executeBySql(sqltr);
			if ("1".equals(queryType)) {
				sqltr = "";
				for (int i = 0; i < divCodes.length; i++) {

					sqltr = "insert into " + sTableName + "(divCode) values ('"
							+ divCodes[i] + "')";
					dao.executeBySql(sqltr);
				}
				sql.append(" and exists (");
				sql.append("select divcode from " + sTableName
						+ " c where a.en_id = c.divcode) " + filter1);
				sql.append("   order by a.div_code,a.prj_code,total_money");
				DBSqlExec.getDataSet(sql.toString(), dsData);
			} else {
				sql.append(filter1);
				sql.append("   order by a.div_code,a.prj_code,total_money");
				DBSqlExec.getDataSet(sql.toString(), dsData);
			}

			dsData.beforeFirst();
			float sum1 = 0;
			float sum2 = 0;
			float cur1 = 0;
			float cur2 = 0;
			while (dsData.next()) {
				cur1 = dsData.fieldByName("cur_money").getFloat();
				cur2 = dsData.fieldByName("total_money").getFloat();
				sum1 = sum1 + cur1;
				sum2 = sum2 + cur2;
			}
			dsData.append();
			dsData.fieldByName("total_money").setValue(String.valueOf(sum1));
			dsData.fieldByName("cur_money").setValue(String.valueOf(sum2));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.executeBySql("drop table " + sTableName);
		}

		return dsData;
	}

	public void cancleBudget(String loginYear, String whereSql) {
		String sql = "";
		try {
			sql = "delete from fb_p_base where set_year ='" + loginYear + "' "
					+ whereSql;
			dao.executeBySql(sql);
			// sql = "insert into fb_p_base (select * from fb_p_base_his where
			// 1=1 "
			// + whereSql + ")";
			// dao.executeBySql(sql);
			sql = "delete from fb_p_detail_pfs where set_year ='" + loginYear
					+ "'  " + whereSql;
			dao.executeBySql(sql);
			// sql = "insert into fb_p_detail_pfs (select * from
			// fb_p_detail_pfs_his where 1=1 "
			// + whereSql + ")";
			// dao.executeBySql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDivToDiv(List lBudgetData, String en_id, String en_code,
			String en_name) throws Exception {
		StringBuffer sInsertXmjl = new StringBuffer();
		List list = new ArrayList();
		try {
			for (int i = 0; i < lBudgetData.size(); i++) {
				XMLData xData = (XMLData) lBudgetData.get(i);
				sInsertXmjl.append(" update rp_xmjl ");
				sInsertXmjl.append(" set gkdw_id='" + en_id);
				sInsertXmjl.append("', gkdw_code='" + en_code);
				sInsertXmjl.append("', gkdw_name='" + en_name);
				sInsertXmjl.append("' where div_code='" + xData.get("div_code")
						+ "'");
				sInsertXmjl.append(" and xmbm='" + xData.get("prj_code") + "'");
				sInsertXmjl.append(" and xmmc='" + xData.get("prj_name") + "'");
				list.add(sInsertXmjl.toString());
				sInsertXmjl.delete(0, sInsertXmjl.length());
			}

			QueryStub.getQueryTool().executeBatch(list);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void setGLToDiv(List lBudgetData, String en_id, String en_code,
			String en_name) throws Exception {
		StringBuffer sInsertXmjl = new StringBuffer();
		List list = new ArrayList();
		try {
			for (int i = 0; i < lBudgetData.size(); i++) {
				XMLData xData = (XMLData) lBudgetData.get(i);
				sInsertXmjl.append(" update rp_prj_return ");
				sInsertXmjl.append(" set gl_enid='" + en_id);
				sInsertXmjl.append("', gl_code='" + en_code);
				sInsertXmjl.append("', gl_name='" + en_name);
				sInsertXmjl.append("' where en_id ='" + xData.get("en_id")
						+ "'");
				list.add(sInsertXmjl.toString());
				sInsertXmjl.delete(0, sInsertXmjl.length());
			}

			QueryStub.getQueryTool().executeBatch(list);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}