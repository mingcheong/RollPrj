package gov.nbcs.rp.query.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.query.ibs.IQrBudget;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;




public class QrBudgetBO implements IQrBudget
{
	private GeneralDAO dao;



	public GeneralDAO getDao()
	{
		return this.dao;
	}


	public void setDao(GeneralDAO dao)
	{
		this.dao = dao;
	}


	public DataSet getDepToDivData(String sYear, int levl, boolean isRight, int batch_no) throws Exception
	{
		String sSql = "";
		if (isRight)
		{
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");

			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();

			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");

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
					+ String.valueOf(levl) + " " + swhere;
		}
		else
		{
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a  where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year) and a.set_year ="
					+ sYear
					+ " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year=" + sYear;
		}

		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	public DataSet getDivDataPop(String sYear, int batch_no) throws Exception
	{
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");

		String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID().toLowerCase(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();

		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");

		String sSql = " select * from vw_fb_division ENTAB  where is_leaf = 1 and set_year=" + sYear + " " + swhere + " union all " + " select * from vw_fb_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year=" + sYear + " and " + " exists (select 1 from vw_fb_division ENTAB where ENTAB.div_code like tab.div_code||'%'"
				+ " and ENTAB.is_leaf = 1 and ENTAB.set_year=" + sYear + " " + swhere + ")";

		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	private String getAuditDivFilter(int batch_no)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" where exists(");
		sql.append(" select 1 from rp_xmjl b where (b.div_code = a.div_code or substring(b.div_code,1,3)=a.div_code)");

		sql.append(" and b.batch_no = " + batch_no + " and b.data_type = 1)");
		sql.append(" order by div_code");
		return sql.toString();
	}


	public DataSet findProjectByPara(String[] divCodes, Map parMap, String[] kmxz, String setYear) throws Exception
	{
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from vw_rp_xmcx a where 1=1 and a.set_year='" + setYear + "' ");

		String fxmbm = (String) parMap.get("fxmbm");
		String fxmmc = (String) parMap.get("fxmmc");
		String fqsnd = (String) parMap.get("fqsnd");
		String fjsnd = (String) parMap.get("fjsnd");
		String fzxzq = (String) parMap.get("fzxzq");
		String fxmfl = (String) parMap.get("fxmfl");
		String fxmsx = (String) parMap.get("fxmsx");
		String fxmzt = (String) parMap.get("fxmzt");
		String fkmxz = (String) parMap.get("fkmxz");
		sql.append(" and a.en_id in (");
		for (int i = 0; i < divCodes.length; i++)
		{
			if (i == 0)
				sql.append("'");
			else
			{
				sql.append(",'");
			}
			sql.append(divCodes[i]);
			sql.append("'");
		}

		sql.append(")");
		if (!"".equals(fxmbm))
		{
			sql.append(" and a.xmbm like '%" + fxmbm + "%'");
		}
		if ((!"".equals(fxmmc)) && (!"null".equals(fxmmc)) && (fxmmc != null))
		{
			sql.append(" and a.xmmc like '%" + fxmmc + "%'");
		}
		if ((!"".equals(fqsnd)) && (!"null".equals(fqsnd)))
		{
			sql.append(" and a.qsnd >= " + fqsnd + "");
		}
		if ((!"".equals(fjsnd)) && (!"null".equals(fjsnd)))
		{
			sql.append(" and a.jsnd <= " + fjsnd + "");
		}
		if (!"null".equals(fzxzq))
		{
			sql.append(" and a.zxzq like '%" + fzxzq + "%'");
		}
		if (!"null".equals(fxmfl))
		{
			sql.append(" and a.xmfl like '%" + fxmfl + "%'");
		}
		if (!"null".equals(fxmsx))
		{
			String xmsx = DBSqlExec.getStringValue("select chr_code from dm_xmsx where chr_id='" + fxmsx + " and set_year=" + setYear + " ");

			sql.append(" and a.xmsx like '%" + xmsx + "%'");
		}
		if (!"null".equals(fxmzt))
		{
			sql.append(" and a.xmzt like '%" + fxmzt + "%'");
		}

		if ((kmxz != null) && (!kmxz.equals("null")))
		{
			sql.append(" and a.xmxh in (select xmxh from rp_xmjl_km  where kmdm in(");
			for (int i = 0; i < kmxz.length; i++)
			{
				if (i == 0)
					sql.append("'");
				else
				{
					sql.append(",'");
				}
				sql.append(kmxz[i]);
				sql.append("'");
			}

			sql.append("))");
		}

		DBSqlExec.getDataSet(sql.toString(), dsData);

		return dsData;
	}


	public DataSet find_alllife(String[] divCodes, Map parMap, String[] kmxz, String setYear) throws Exception
	{

		String dblink = "";
		dblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select a.*, ");
		sql.append(" nvl(c.pay_monty, 0) pay_monty, ");
		sql.append(" f11_alls + sb_333 as total_money, ");
		sql.append("  sb_111 - sb_222 - sb_333 - f11_all as lack_money,");
		sql.append("  case when nvl(sb_333, 0) = 0 then  '0.0%'");
		sql.append(" when nvl(pay_monty, 0) = 0 then '0.0%'");
		sql.append("  else SUBSTR(to_char(to_number(pay_monty / (f11_alls + sb_333) / 100)), ");
		sql.append("0,INSTR(to_char(to_number(pay_monty / (f11_alls + sb_333) / 100)),'.') + 2) || '%'  end pay_plan ");
		sql.append(" from (select a.*, nvl(f11_all, 0) f11_alls ");
		sql.append("   from vw_rp_all_life a  where set_year = ");
		sql.append(setYear);
		sql.append(") a ");
		sql.append(" left join (select b.en_id, ");
		sql.append(" b.en_code,b.en_name,sum(b.pay_money) as pay_monty,bis_name from vw_pay_voucher@");
		sql.append(dblink);
		sql.append(" b where b.bi_code like '002%' and b.set_year = ");
		sql.append(setYear);
		sql.append("   group by en_id, en_code, en_name, bis_name  order by en_code, bis_name) c");
		sql.append("   on a.en_id = c.en_id and a.xmmc = c.bis_name) a where 1=1");

		String fxmbm = (String) parMap.get("fxmbm");
		String fxmmc = (String) parMap.get("fxmmc");
		String fqsnd = (String) parMap.get("fqsnd");
		String fjsnd = (String) parMap.get("fjsnd");
		String fzxzq = (String) parMap.get("fzxzq");
		String fxmfl = (String) parMap.get("fxmfl");
		String fxmsx = (String) parMap.get("fxmsx");
		String fxmzt = (String) parMap.get("fxmzt");
		String fkmxz = (String) parMap.get("fkmxz");
		sql.append(" and a.en_id in (");
		for (int i = 0; i < divCodes.length; i++)
		{
			if (i == 0)
				sql.append("'");
			else
			{
				sql.append(",'");
			}
			sql.append(divCodes[i]);
			sql.append("'");
		}

		sql.append(")");
		if (!"".equals(fxmbm))
		{
			sql.append(" and a.xmbm like '%" + fxmbm + "%'");
		}
		if ((!"".equals(fxmmc)) && (!"null".equals(fxmmc)) && (fxmmc != null))
		{
			sql.append(" and a.xmmc like '%" + fxmmc + "%'");
		}
		if ((!"".equals(fqsnd)) && (!"null".equals(fqsnd)))
		{
			sql.append(" and a.qsnd >= " + fqsnd + "");
		}
		if ((!"".equals(fjsnd)) && (!"null".equals(fjsnd)))
		{
			sql.append(" and a.jsnd <= " + fjsnd + "");
		}
		if (!"null".equals(fzxzq))
		{
			sql.append(" and a.zxzq like '%" + fzxzq + "%'");
		}
		if (!"null".equals(fxmfl))
		{
			sql.append(" and a.xmfl like '%" + fxmfl + "%'");
		}
		if (!"null".equals(fxmsx))
		{
			String xmsx = DBSqlExec.getStringValue("select chr_code from dm_xmsx where chr_id='" + fxmsx + " and set_year=" + setYear + " ");

			sql.append(" and a.xmsx like '%" + xmsx + "%'");
		}
		if (!"null".equals(fxmzt))
		{
			sql.append(" and a.xmzt like '%" + fxmzt + "%'");
		}

		if ((kmxz != null) && (!kmxz.equals("null")))
		{
			sql.append(" and a.xmxh in (select xmxh from rp_xmjl_km  where kmdm in(");
			for (int i = 0; i < kmxz.length; i++)
			{
				if (i == 0)
					sql.append("'");
				else
				{
					sql.append(",'");
				}
				sql.append(kmxz[i]);
				sql.append("'");
			}

			sql.append("))");
		}

		DBSqlExec.getDataSet(sql.toString(), dsData);

		return dsData;
	}


	public String findkmdm(String xmxh) throws Exception
	{
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
		while (ds.next())
		{
			if (i++ != 0)
			{
				value.append(";");
			}
			value.append(ds.fieldByName("fullname").getString());
			continue;

		}
		return value.toString();
	}


	public void gunDongNextyear(String xmbms, String[] xmxhs, String UserCode, String setYear, String rgCode) throws Exception
	{
		List list = new ArrayList();
		String xmxh = "";

		int year = Integer.valueOf(setYear).intValue() + 1;
		String sqlpost = "delete from rp_xmjl_km where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";
		String sqlpost2 = "delete from rp_xmsb where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";
		String sqlpost3 = "delete from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + "";

		String sqlpost4 = "delete from rp_xmsb_ly where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";

		list.add(sqlpost.toString());
		list.add(sqlpost2.toString());
		list.add(sqlpost3.toString());
		list.add(sqlpost4.toString());

		for (int i = 0; i < xmxhs.length; i++)
		{
			xmxh = UUIDRandom.generate();
			StringBuffer sql = new StringBuffer();
			sql.append("insert into rp_xmjl (select '" + year + "', '" + xmxh + "', XMBM, XMMC, C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, EN_ID, RG_CODE, '" + UserCode + "', sysdate, '" + UserCode
					+ "', sysdate, 1, DIV_CODE, DIV_NAME, CS_SORT, EN_SORT, GKDW_ID, GKDW_CODE,ACCTS,GKDW_NAME ");
			sql.append(" from rp_xmjl where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql1 = new StringBuffer();
			sql1.append("insert into rp_xmsb_ly (select " + year + ", newid, RG_CODE, '" + xmxh + "', LY");
			sql1.append(" from rp_xmsb_ly where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql2 = new StringBuffer();

			sql2.append("insert into rp_xmjl_km (select '" + year + "', newid,'" + xmxh + "',KMDM from rp_xmjl_km");
			sql2.append(" where xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql3 = new StringBuffer();
			sql3.append("insert into rp_xmsb (");
			sql3.append("select " + year + ", newid, '" + xmxh + "', YSJC_DM, BS_ID, BSI_ID, F1, F2, F3, F4, F5, F6, F7, F8, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '" + UserCode + "', sysdate, '"
					+ UserCode + "', sysdate, BZ, SB_CODE, TOTAL_SUM,' ',' ',' ',f9,f10,f11,f12 from ");
			sql3.append("rp_xmsb where sb_code='111' ");
			sql3.append("and xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql4 = new StringBuffer();
			sql4.append("insert into rp_xmsb (");
			sql4.append("select " + year + ", newid, '" + xmxh + "', YSJC_DM, BS_ID, BSI_ID, 0, 0, 0, 0, 0, 0, 0, 0, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '" + UserCode + "', sysdate, '" + UserCode
					+ "', sysdate, BZ, SB_CODE, 0,' ',' ',' ',0,0,0,0 from ");
			sql4.append("rp_xmsb where sb_code not in ('111','222') ");
			sql4.append("and xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql5 = new StringBuffer();
			sql5.append("insert into rp_xmsb (");
			sql5.append("select " + year + ", newid, '" + xmxh + "', '', ' ', ' ', sum(nvl(f1,0)), sum(nvl(f2,0)), sum(nvl(f3,0)), 0, 0, sum(nvl(f6,0)), sum(nvl(f7,0)), sum(nvl(f8,0)), ' ', '"
					+ rgCode + "', '已安排数', '', '" + UserCode + "', sysdate, '" + UserCode
					+ "', sysdate, '', '222', sum(TOTAL_SUM),'','','',sum(nvl(f9,0)),sum(nvl(f10,0)),sum(nvl(f11,0)),sum(nvl(f12,0)) from ");
			sql5.append("rp_xmsb where sb_code  in ('222','333') ");
			sql5.append("and xmxh = '" + xmxhs[i] + "') ");
			list.add(sql.toString());
			list.add(sql1.toString());
			list.add(sql2.toString());
			list.add(sql3.toString());
			list.add(sql4.toString());
			list.add(sql5.toString());

			if (list.size() > 0)
			{
				QueryStub.getQueryTool().executeBatch(list);
			}

		}

		List list1 = new ArrayList();
		StringBuffer sqlh = new StringBuffer();
		sqlh.append("delete from rp_xmjl_history where xmbm in (" + xmbms + ") and set_year = " + year + " and batch_no = 0" + " and step_id= 5");

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
		sql2.append("delete from rp_xmjl_km_his where xmbm in (" + xmbms + ") and set_year = " + year);

		StringBuffer sqlh22 = new StringBuffer();
		StringBuffer sql22 = new StringBuffer();

		sql22.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmbm in (" + xmbms + ") and set_year = " + year);

		String sqlsb_his_del = "delete from rp_xmsb_history  where step_id=5 and batch_no=0 and his_no=1 and xmxh in (select  xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year = " + year
				+ ")";

		String sqlsb_his = "insert into rp_xmsb_history(select a.*,5,0,1 from rp_xmsb  a where a.xmxh in(select  xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year = " + year + "))";

		list1.add(sqlh.toString());
		list1.add(sqlh1.toString());
		list1.add(sqlh2.toString());
		list1.add(sqlh22.toString());
		list1.add(sqlsb_his_del.toString());
		list1.add(sqlsb_his.toString());
		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);
	}


	public void gunDongNextyearLXXM(String xmbms, String[] xmxhs, String UserCode, String setYear, String rgCode) throws Exception
	{
		List list = new ArrayList();
		String xmxh = "";

		int year = Integer.valueOf(setYear).intValue() + 1;
		String sqlpost = "delete from rp_xmjl_km where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";
		String sqlpost2 = "delete from rp_xmsb where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";
		String sqlpost3 = "delete from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + "";

		String sqlpost4 = "delete from rp_xmsb_ly where xmxh in (select xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year=" + year + ")";

		list.add(sqlpost.toString());
		list.add(sqlpost2.toString());
		list.add(sqlpost3.toString());
		list.add(sqlpost4.toString());

		for (int i = 0; i < xmxhs.length; i++)
		{
			xmxh = UUIDRandom.generate();
			StringBuffer sql = new StringBuffer();
			sql.append("insert into rp_xmjl (select '" + year + "', '" + xmxh + "', XMBM, XMMC, '" + year + "', '" + year + "', C3, C4, C5, C6, C7, C8, C9, C10, EN_ID, RG_CODE, '" + UserCode
					+ "', sysdate, '" + UserCode + "', sysdate, 1, DIV_CODE, DIV_NAME, CS_SORT, EN_SORT, GKDW_ID, GKDW_CODE,ACCTS,GKDW_NAME,'' ");
			sql.append(" from rp_xmjl where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql1 = new StringBuffer();
			sql1.append("insert into rp_xmsb_ly (select " + year + ", newid, RG_CODE, '" + xmxh + "', LY");
			sql1.append(" from rp_xmsb_ly where xmxh = '" + xmxhs[i] + "')");

			StringBuffer sql2 = new StringBuffer();

			sql2.append("insert into rp_xmjl_km (select '" + year + "', newid,'" + xmxh + "',KMDM from rp_xmjl_km");
			sql2.append(" where xmxh = '" + xmxhs[i] + "')");
			StringBuffer sql3 = new StringBuffer();
			sql3.append("insert into rp_xmsb (");
			sql3.append("select " + year + ", newid, '" + xmxh + "', YSJC_DM, BS_ID, BSI_ID, F1, F2, F3, F4, F5, F6, F7, F8, EN_ID, RG_CODE, SB_TYPE, WSZT_DM, '" + UserCode + "', sysdate, '"
					+ UserCode + "', sysdate, BZ, SB_CODE, TOTAL_SUM,ysjc_mc,acct_name,acct_name_jj,f9,f10,f11,f12 from ");
			sql3.append("rp_xmsb  where 1=1 ");

			sql3.append("and xmxh = '" + xmxhs[i] + "')");

			list.add(sql.toString());
			list.add(sql1.toString());
			list.add(sql2.toString());
			list.add(sql3.toString());
		}

		if (list.size() > 0)
		{
			QueryStub.getQueryTool().executeBatch(list);
		}
		List list1 = new ArrayList();
		StringBuffer sqlh = new StringBuffer();
		sqlh.append("delete from rp_xmjl_history where xmbm in (" + xmbms + ") and set_year = " + year + " and batch_no = 0" + " and step_id= 5");

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
		sql2.append("delete from rp_xmjl_km_his where xmbm in (" + xmbms + ") and set_year = " + year);

		StringBuffer sqlh22 = new StringBuffer();
		StringBuffer sql22 = new StringBuffer();
		sql22.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmbm in (" + xmbms + ") and set_year = " + year);

		String sqlsb_his_del = "delete from rp_xmsb_history  where step_id=5 and batch_no=0 and his_no=1 and xmxh in (select  xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year = " + year
				+ ")";

		String sqlsb_his = "insert into rp_xmsb_history(select a.*,5,0,1,'','' from rp_xmsb  a where a.xmxh in(select  xmxh from rp_xmjl where xmbm in (" + xmbms + ") and set_year = " + year + "))";

		list1.add(sqlh.toString());
		list1.add(sqlh1.toString());
		list1.add(sqlh2.toString());
		list1.add(sqlh22.toString());
		list1.add(sqlsb_his_del.toString());
		list1.add(sqlsb_his.toString());

		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);
	}


	public void finishSameBudget(String xmbms, String xmxhs, String userCode, String setYear, String rgCode) throws Exception
	{
		List list = new ArrayList();
		StringBuffer sqlpost = new StringBuffer();
		sqlpost.append("delete from rp_finishSameBt where prj_id in (" + xmxhs + ") and set_year ='" + setYear + "'");

		StringBuffer sql = new StringBuffer();
		sql.append("insert into rp_finishSameBt (select '" + setYear + "', '" + rgCode + "',XMXH, XMBM, XMMC, sysdate");
		sql.append(" from rp_xmjl where xmxh in (" + xmxhs + "))");
		list.add(sqlpost.toString());
		list.add(sql.toString());

		QueryStub.getQueryTool().executeBatch(list);
	}


	public void changeHs(String setYear, String batchNo, String dateType, String table1, String table2) throws Exception
	{
		Connection conn = null;
		CallableStatement call = null;
		Session session = null;
		try
		{
			session = this.dao.getSession();
			if (session == null) { throw new Exception("数据库连接已关闭,无法使用"); }
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (call != null)
				{
					call.close();
					call = null;
				}
				if (session != null)
					this.dao.closeSession(session);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	public void insertTempBudget(String setYear, String batchNo, String dateType, String wheresql) throws Exception
	{
		List list = new ArrayList();
		StringBuffer sqlpost = new StringBuffer();
		sqlpost.append("delete from fb_p_base where set_year='" + setYear + "' and batch_no='" + batchNo + "' and data_type='" + dateType + "'" + wheresql + "");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into fb_p_base (select * from vw_fb_p_base where set_year='" + setYear + "' and batch_no='" + batchNo + "' and data_type='" + dateType + "'" + wheresql + ")");
		StringBuffer sqlpostDetail = new StringBuffer();
		sqlpostDetail.append("delete from fb_p_detail_pfs where set_year='" + setYear + "' and batch_no='" + batchNo + "' and data_type='" + dateType + "'" + wheresql + "");
		StringBuffer sqlpDetail = new StringBuffer();
		sqlpDetail.append("insert into fb_p_detail_pfs (select * from vw_fb_p_detail_pfs where set_year='" + setYear + "' and batch_no='" + batchNo + "' and data_type='" + dateType + "'" + wheresql
				+ ")");
		list.add(sqlpost.toString());
		list.add(sql.toString());
		list.add(sqlpostDetail.toString());
		list.add(sqlpDetail.toString());

		QueryStub.getQueryTool().executeBatch(list);
	}


	public DataSet findRPBUDGET(String[] divCodes, String dataType, String fpType, String setYear, String rgCode) throws Exception
	{
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SET_YEAR,A.RG_CODE,A.EN_ID,A.DIV_CODE,A.DIV_NAME,A.XMXH AS PRJ_ID,A.XMXH,");
		sql.append("A.XMBM AS PRJ_CODE,A.XMMC AS PRJ_NAME,nvl(F.F2 + F.F3 + F.F6 + F.F7,0) as SMONEY,A.NODE_NAME,A.GKDW_CODE FROM");

		if (fpType.equals("1"))
			sql.append(" VW_RP_XMCX_LC_P2 A left join  RP_XMSB_p2 F");
		else if (fpType.equals("2"))
			sql.append(" VW_RP_XMCX_LC A left join  RP_XMSB F");

		sql.append(" on A.XMXH = F.XMXH where ");
		sql.append(" A.SET_YEAR = F.SET_YEAR AND F.SB_CODE = '333' AND F.IS_IDX IS NULL");

		if (divCodes != null && divCodes.length > 0)
		{
			StringBuffer dCodes = new StringBuffer();
			for (int i = 0; i < divCodes.length; i++)
			{
				dCodes.append(",'").append(divCodes[i]).append("'");
			}
			sql.append(" AND A.DIV_CODE IN (").append(dCodes.substring(1)).append(")");
		}

		sql.append(" AND A.SET_YEAR = ? AND A.RG_CODE = ?");
		if (dataType.equals("1"))
			sql.append(" AND A.NODE_CODE = 3");
		else
			sql.append(" AND A.NODE_CODE != 3");
		sql.append(" and a.c6 = '002'");
		DBSqlExec.getDataSet(sql.toString(), new Object[] { setYear, rgCode }, dsData);
		return dsData;
	}


	public void DeleteTempPro(String wheresql, String setYear, String rgCode) throws Exception
	{
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
}
