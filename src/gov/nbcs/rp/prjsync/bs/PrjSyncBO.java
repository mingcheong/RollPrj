/**
 * @Copyright 浙江易桥版权所有
 * 
 * @ProjectName 宁海财政扶持项目系统
 * 
 * @aouthor 陈宪标
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.prjsync.ibs.IPrjSync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;




/**
 * @author 毛建亮
 * 
 * @version 创建时间：2012-6-7 上午10:53:35
 * 
 * @Description
 */
public class PrjSyncBO implements IPrjSync
{
	public GeneralDAO dao;



	public GeneralDAO getDao()
	{
		return dao;
	}


	public void setDao(GeneralDAO dao)
	{
		this.dao = dao;
	}


	/**
	 * 获取未同步的项目（同时不在填报主表的项目）
	 */
	public DataSet getOldPrjSync(String[] divCodes, String setYear, String rgCode, String queryType) throws Exception
	{
		String strSql = null;
		DataSet ds = DataSet.create();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "").replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);
		try
		{
			String dblink = "";
			dblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");
			StringBuffer sql = new StringBuffer();
			sql.append("select ");
			sql.append("en_id, mb_name,mb_code,mk_name,");
			sql.append("mk_code,en_name,en_code,bs_code, bs_name,");
			sql.append("bi_code,bi_name,bis_code,bis_name,bl_code,bl_name,");
			sql.append("bt_code,bt_name,pk_code,pk_name,remark,");
			sql.append("file_name,ROUND(budget_money/10000, 2) as budget_money,set_year,rg_code");
			sql.append(" from  ");
			sql.append("vw_query_budget_yszx_budget@").append(dblink).append(" gb where gb.bi_code like '002%'");
			sql.append(" and not exists");
			sql.append("( select 1 from vw_rp_prjbudget ");
			sql.append(" where bis_name=xmmc and vw_rp_prjbudget.en_id=gb.en_id and vw_rp_prjbudget.total_sum = gb.budget_money)");
			sql.append(" and set_year='").append(setYear).append("' and rg_code='").append(rgCode).append("'");
			sql.append(" and bl_code != '001001'");
			sql.append(" and bi_code not in('001001','001002')");

			String sqltr = "create table " + sTableName + " (divCode  VARCHAR2(60))";
			dao.executeBySql(sqltr);
			if (queryType.equals("1"))
			{
				for (int i = 0; i < divCodes.length; i++)
				{
					sqltr = "insert into " + sTableName + "(divCode) values ('" + divCodes[i] + "')";
					dao.executeBySql(sqltr);
				}
				sql.append(" and exists(").append("select divcode from " + sTableName + " c where gb.en_id = c.divcode)").append(" order by en_code,bis_name,budget_money");
				strSql = sql.toString();
				sql.delete(0, sql.length());
				ds = DBSqlExec.getDataSet(strSql);
			}
			else
			{
				sql.append("order by en_code,bis_name,budget_money");
				strSql = sql.toString();
				sql.delete(0, sql.length());
				ds = DBSqlExec.getDataSet(strSql);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dao.executeBySql("drop table " + sTableName);
		}
		return ds;
	}


	public DataSet getPrjFromIndexSys(String[] divCodes, String setYear, String rgCode, String queryType) throws Exception
	{
		// 重新拼装单位编码的字符串
		String enIds = null;
		if (divCodes != null && divCodes.length > 0)
		{
			StringBuffer dCodes = new StringBuffer();
			for (int i = 0; i < divCodes.length; i++)
			{
				dCodes.append(",'").append(divCodes[i]).append("'");
			}
			enIds = dCodes.substring(1);
		}

		// 读取连接指标系统的dblink名称
		String dblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");

		StringBuffer sql = new StringBuffer("select * from (");
		sql.append("select set_year,rg_code,en_id,mb_id,max(mb_name) as mb_name,mb_code,mk_code,max(mk_name) as mk_name,max(en_name) as en_name,");
		sql.append("en_code,bs_code,max(bs_name) as bs_name,bi_code,max(bi_name) as bi_name,bis_code,max(bis_name) as bis_name,");
		sql.append("bl_code,max(bl_name) as bl_name,bt_code,max(bt_name) as bt_name,pk_code,max(file_name) as file_name,");
		sql.append("max(pk_name) as pk_name,max(remark) as remark,sum(nvl(budget_money, 0)) as budget_money,");
		sql.append("sum(nvl(plan_money, 0)) as plan_money,sum(nvl(budget_money, 0) - nvl(plan_money, 0)) as surplus_usemoney,");
		sql.append("sum(nvl(pay_money, 0)) as pay_money,sum(nvl(budget_money, 0) - nvl(pay_money, 0)) as surplus_money from (");

		sql.append("select en_id,mb_id,mb_name,mb_code,mk_name,mk_code,en_name,en_code,bs_code,bs_name,bi_code,bi_name,bis_code,");
		sql.append("bis_name,bl_code,bl_name,bt_code,bt_name,pk_code,file_name,pk_name,remark,budget_money,plan_money,");
		sql.append("0 as pay_money,set_year,rg_code from vw_query_budget_yszx_budget@").append(dblink).append(" gb where gb.bl_code != '001001'");
		sql.append(" union all ");
		sql.append("select en_id,mb_id,mb_name,mb_code,mk_name,mk_code,en_name,en_code,bs_code,bs_name,bi_code,bi_name,bis_code,");
		sql.append("bis_name,bl_code,bl_name,bt_code,bt_name,substr(pk_code, 1, 3) as pk_code,file_name,remark,(");
		sql.append("select chr_name from ele_payoff_kind e where e.chr_code = substr(c.pk_code, 1, 3)) as pk_name,");
		sql.append("0 as budget_money,0 as plan_money,pay_money,set_year,rg_code from vw_query_pay_voucher_c@").append(dblink);
		sql.append(" c where bl_code != '001001')");

		sql.append("group by set_year,rg_code,en_id,mb_id,mb_code,mk_code,en_code,bs_code,bi_code,bis_code,bl_code,bt_code,pk_code");
		sql.append(" having sum(nvl(budget_money, 0)) > 0 order by mb_code, en_code, bs_code, bi_code)");
		sql.append(" where set_year = '").append(setYear).append("' and rg_code = '").append(rgCode).append("'");
		sql.append(" and bi_code not in('001001','001002')");
		sql.append(" and not exists (select 1 from vw_rp_prjbudget v where bis_name=v.xmmc and v.en_id=en_id and v.total_sum = budget_money and set_year = '").append(setYear).append("')");
		if (queryType.equals("1"))
			sql.append(" and en_id in (").append(enIds).append(")");
		return DBSqlExec.getDataSet(sql.toString());
	}


	public DataSet getQueryProject(String cond) throws Exception
	{
		// String sql1="SELECT '' XMBM,'' DIV_CODE,'' DIV_NAME,'' XMMC,'' C1,''
		// C2,'' C3,'' C4,'' C5,'' ACCTS FROM DUAL WHERE 1<>1";
		// DataSet dsall=DBSqlExec.getDataSet(sql1);
		// dsall.open();
		// dsall.beforeFirst();
		String condition = " AND NOT EXISTS (SELECT * FROM SYS_WF_COMPLETE_ITEM B WHERE B.ENTITY_ID=NVL(A.XMXH,''))  AND"
				+ " NOT EXISTS (SELECT * FROM SYS_WF_CURRENT_ITEM C WHERE C.ENTITY_ID=NVL(A.XMXH,'') )";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT PRJ_BTTYPE STEP_ID,EN_CODE DIV_CODE,BIS_NAME XMMC,EN_NAME DIV_NAME,BIS_CODE XMBM,EN_ID,SET_YEAR,RG_CODE,C1,C2,ACCTS,nvl(XMXH,'') XMXH,");
		sb.append("(SELECT CHR_NAME FROM RP_BASE_EXECYCLE WHERE CHR_CODE=a.C3 AND A.SET_YEAR=SET_YEAR) AS C3,");
		sb.append("(SELECT  CHR_NAME  FROM RP_BASE_PRJSORT WHERE CHR_CODE=A.C4 AND A.SET_YEAR=SET_YEAR) AS C4,");
		sb.append("(SELECT  CHR_NAME  FROM RP_BASE_PRJSTATE  WHERE CHR_CODE=A.C6 AND A.SET_YEAR=SET_YEAR) AS C6,C5,C4 C11 ");
		sb.append("FROM VW_RP_RETURN A WHERE 1=1 ");
		sb.append(cond + " " + condition);
		sb.append(" GROUP BY PRJ_BTTYPE,EN_CODE,BIS_NAME,EN_NAME,BIS_CODE,EN_ID,SET_YEAR,RG_CODE,C1,C2,C3,C4,C5,C6,ACCTS,XMXH");
		sb.append(" ORDER BY STEP_ID");
		DataSet ds = DBSqlExec.getDataSet(sb.toString());
		ds.beforeFirst();

		String xmxh = "";
		while (ds.next())
		{
			ds.edit();

			if ("未补填".equals(ds.fieldByName("STEP_ID").getValue().toString()))
			{
				String xmmc = ds.fieldByName("XMMC").getValue().toString();
				String div_code = ds.fieldByName("DIV_CODE").getValue().toString();
				// String xmbm=ds.fieldByName("bis_code").getValue().toString();
				// String
				// div_name=ds.fieldByName("en_name").getValue().toString();
				// String en_id=ds.fieldByName("en_id").getValue().toString();
				// String
				// set_year=ds.fieldByName("set_year").getValue().toString();
				// String
				// rg_code=ds.fieldByName("rg_code").getValue().toString();
				String sql = "SELECT bs_name FROM  RP_PRJ_RETURN  WHERE EN_CODE=" + div_code + " AND BIS_NAME='" + xmmc + "'";
				List list = dao.findBySql(sql);
				String gnkm_prj = "";
				for (int j = 0; j < list.size(); j++)
				{
					String gnkm = ((Map) (list.get(j))).get("bs_name").toString();// 功能科目
					gnkm_prj = gnkm_prj + gnkm + ";";
				}
				xmxh = UUIDRandom.generate();
				ds.fieldByName("XMXH").setValue(xmxh);
				ds.fieldByName("ACCTS").setValue(gnkm_prj);
			}
		}

		return ds;
	}


	/**
	 * 项目填报详细信息(金额)
	 * 
	 * @return DataSet
	 * @throws Exception
	 */
	public DataSet getPrjTbDetailInfo(String sYear, String rgCode, String xmbm, String en_code, String xmxh) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		String strSql = "SELECT B.FIELD_ENAME,A.BT_NAME,A.BS_CODE,A.BS_NAME ,A.BUDGET_MONEY " + "FROM RP_PRJ_RETURN A,RP_FUND_SOURSE B WHERE MK_CODE=LVL_ID ";
		sql.append(strSql);
		sql.append("AND EN_CODE=");
		sql.append(en_code);
		sql.append("AND BIS_CODE=");
		sql.append(xmbm);

		DataSet ds = DBSqlExec.getDataSet(sql.toString());
		ds.beforeFirst();

		DataSet dsBody = DBSqlExec.getDataSet("SELECT * FROM RP_PRJ_DETAIL WHERE 1<>1");
		dsBody.open();
		dsBody.beforeFirst();
		// if (dsBody != null && !dsBody.isEmpty())
		// return dsBody;
		// else {
		dsBody.append();
		dsBody.fieldByName("xmxh").setValue("");
		dsBody.fieldByName("xmtbid").setValue(xmxh);
		dsBody.fieldByName("rg_code").setValue(rgCode);
		dsBody.fieldByName("setyear").setValue(sYear);
		dsBody.fieldByName("ysjc_mc").setValue("");
		dsBody.fieldByName("sb_code").setValue("111");
		dsBody.fieldByName("acct_name").setValue("");
		dsBody.fieldByName("acct_name_jj").setValue("");
		dsBody.fieldByName("sb_type").setValue("总预算");
		dsBody.append();
		dsBody.fieldByName("xmxh").setValue("");
		dsBody.fieldByName("xmtbid").setValue(xmxh);
		dsBody.fieldByName("rg_code").setValue(rgCode);
		dsBody.fieldByName("setyear").setValue(sYear);
		dsBody.fieldByName("sb_code").setValue("222");
		dsBody.fieldByName("ysjc_mc").setValue("");
		dsBody.fieldByName("acct_name").setValue("");
		dsBody.fieldByName("acct_name_jj").setValue("");
		dsBody.fieldByName("sb_type").setValue("已安排数");
		dsBody.append();
		dsBody.fieldByName("xmxh").setValue("");
		dsBody.fieldByName("xmtbid").setValue(xmxh);
		dsBody.fieldByName("rg_code").setValue(rgCode);
		dsBody.fieldByName("setyear").setValue(sYear);
		dsBody.fieldByName("sb_code").setValue("333");
		dsBody.fieldByName("ysjc_mc").setValue("");
		dsBody.fieldByName("acct_name").setValue("");
		dsBody.fieldByName("acct_name_jj").setValue("");
		dsBody.fieldByName("sb_type").setValue("本年预算");
		while (ds.next())
		{
			dsBody.append();
			int i = 1;
			dsBody.fieldByName("xmtbid").setValue(xmxh);
			String xmxh_new = UUIDRandom.generate();
			dsBody.fieldByName("xmxh").setValue(xmxh_new); // ADD BY XYS
			// dsBody.fieldByName("xmtbid").setValue(xmxh);
			// dsBody.fieldByName("rg_code").setValue(rgCode);
			dsBody.fieldByName("acct_name").setValue("[" + ds.fieldByName("bs_code").getString() + "]" + ds.fieldByName("bs_name").getString());
			// dsBody.fieldByName("bs_id").setValue(ds.fieldByName("chr_id").getString());
			// dsBody.fieldByName("acct_code").setValue(ds.fieldByName("chr_code").getString());
			// dsBody.fieldByName("setYear").setValue(sYear);
			dsBody.fieldByName(ds.fieldByName("FIELD_ENAME").getString()).setValue(String.valueOf((ds.fieldByName("BUDGET_MONEY").getDouble() / 1000)));
			dsBody.fieldByName("ysjc_mc").setValue(ds.fieldByName("BT_NAME").getString());

			dsBody.fieldByName("sb_type").setValue(String.valueOf(i++));
			dsBody.fieldByName("sb_code").setValue(String.valueOf(400000 + i));
			dsBody.fieldByName("acct_name_jj").setValue("");
		}
		dsBody.applyUpdate();
		return dsBody;
	}


	/**
	 * 获取已同步的项目（同时不在填报主表的项目）
	 */
	public DataSet getYetPrjSync(String[] divCodes, String setYear, String rgCode, String queryType) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "").replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);
		try
		{
			sql.append("select ");
			sql.append("en_id, mb_name,mb_code,mk_name,");
			sql.append("mk_code,en_name,en_code,bs_code, bs_name,bi_code,");
			sql.append("bi_name,bis_code,bis_name,bl_code,bl_name, ");
			sql.append(" bt_code,bt_name,budget_money,set_year,rg_code,gl_name");
			sql.append(" from rp_prj_return a ");
			sql.append("where 1=1");
			sql.append(" and set_year='" + setYear + "' and rg_code='" + rgCode + "'");
			String sqltr = "create table " + sTableName + " (divCode  VARCHAR2(60))";
			dao.executeBySql(sqltr);
			if (queryType.endsWith("1"))
			{
				for (int i = 0; i < divCodes.length; i++)
				{
					sqltr = "insert into " + sTableName + "(divCode) values ('" + divCodes[i] + "')";
					dao.executeBySql(sqltr);
				}
				sql.append(" and exists(").append("select divcode from " + sTableName + " c where a.en_id = c.divcode)").append(" order by en_code,bis_name,budget_money");

				String strSql = sql.toString();
				sql.delete(0, sql.length());
				ds = DBSqlExec.getDataSet(strSql);
			}
			else
			{
				sql.append("order by en_code,bis_name,budget_money");
				String strSql = sql.toString();
				sql.delete(0, sql.length());
				ds = DBSqlExec.getDataSet(strSql);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dao.executeBySql("drop table " + sTableName);
		}
		return ds;
	}


	/**
	 * 项目同步
	 */
	public String prjSync(List enid, List bisname)
	{
		String sMsg = "项目同步成功";
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "").replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);
		try
		{
			String sDblink = "";
			sDblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");
			StringBuffer sqlTemp = new StringBuffer();
			sqlTemp.append("create table " + sTableName + "(enid varchar2(38),bisname varchar2(200) )");
			// QueryStub.getQueryTool().execute(sqlTemp.toString());
			dao.executeBySql(sqlTemp.toString());
			List listInTemp = new ArrayList();
			for (int i = 0; i < enid.size(); i++)
			{
				StringBuffer sqlInTemp = new StringBuffer();
				sqlInTemp.append("insert into " + sTableName);
				sqlInTemp.append("(enid,bisname)");
				sqlInTemp.append(" values(");
				sqlInTemp.append("'" + enid.get(i) + "',");
				sqlInTemp.append("'" + bisname.get(i) + "'");
				sqlInTemp.append(")");
				listInTemp.add(sqlInTemp.toString());
			}
			QueryStub.getQueryTool().executeBatch(listInTemp);

			StringBuffer sqlInPrj = new StringBuffer();
			sqlInPrj.append("insert into rp_prj_return(");
			sqlInPrj.append(" en_id,mb_name,mb_code,mk_name,mk_code,");
			sqlInPrj.append(" en_name,en_code,bs_code,bs_name,bi_code,");
			sqlInPrj.append(" bi_name,bis_code,bis_name,bl_code,bl_name,");
			sqlInPrj.append(" bt_code,bt_name,budget_money,SET_YEAR,RG_CODE)");
			sqlInPrj.append(" select  ");
			sqlInPrj.append(" en_id,mb_name,mb_code,mk_name,mk_code,");
			sqlInPrj.append(" en_name,en_code,bs_code,bs_name,bi_code,");
			sqlInPrj.append(" bi_name,bis_code,bis_name,bl_code,bl_name,");
			sqlInPrj.append(" bt_code,bt_name,budget_money,SET_YEAR,RG_CODE");
			sqlInPrj.append(" from vw_query_budget_yszx_budget@" + sDblink + " m");
			sqlInPrj.append(" where exists(select 1 from " + sTableName + " n");
			sqlInPrj.append(" where m.en_id = n.enid and m.bis_name=n.bisname");
			sqlInPrj.append(")");
			QueryStub.getQueryTool().execute(sqlInPrj.toString());
		}
		catch (Exception ex)
		{
			sMsg = "项目同步失败";
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				dao.executeBySql("drop table " + sTableName);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return sMsg;
	}


	public String makeSynchronous(List syncList) throws Exception
	{
		if (syncList == null || syncList.isEmpty())
			return "";

		Map prjMap = null;
		String sql = null;
		List dataList = null;
		List sbList = null;
		List sqlList = new ArrayList();

		// 项目序号
		String xmxh = null;
		double totalSum = 0d;
		double budgetMoney = 0d;
		int num = 0;
		for (int i = 0; i < syncList.size(); i++)
		{
			prjMap = (Map) syncList.get(i);
			sql = "select * from rp_xmjl where en_id = ? and xmmc = ?";
			dataList = dao.findBySql(sql, new Object[] { prjMap.get("en_id"), prjMap.get("bis_name") });
			if (dataList == null || dataList.isEmpty())
			{
				xmxh = UUIDRandom.generate();
				totalSum = 0d;
				num = 1;

				// 保存指标项目信息
				sql = "insert into rp_xmjl (set_year,xmxh,xmbm,xmmc,en_id,rg_code,lrr_dm,lrrq,xgr_dm,xgrq,div_code,div_name,gkdw_id,gkdw_code,accts,gkdw_name)";
				sql += " values (";
				sql += SessionUtil.getLoginYear() + ",'";
				sql += xmxh + "','";
				sql += getNewPrjCode(prjMap.get("en_code").toString()) + "','";
				sql += prjMap.get("bis_name").toString() + "','";
				sql += prjMap.get("en_id").toString() + "','";
				sql += SessionUtil.getParaMap().get("RG_CODE").toString() + "','";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
				sql += prjMap.get("en_code").toString() + "','";
				sql += prjMap.get("en_name").toString() + "','";
				sql += prjMap.get("en_id").toString() + "','";
				sql += prjMap.get("en_code").toString() + "','";
				sql += "[" + prjMap.get("bs_code").toString() + "]" + prjMap.get("bs_name").toString() + "','";
				sql += prjMap.get("en_name").toString() + "')";
				sqlList.add(sql);

				// 保存指标项目明细（总计）
				sql = "insert into rp_xmsb (set_year,xmsbid,xmxh,f1,f2,en_id,rg_code,sb_type,wszt_dm,lrr_dm,lrrq,xgr_dm,xgrq,sb_code,total_sum,is_idx)";
				sql += " values (";
				sql += SessionUtil.getLoginYear() + ",newid,'";
				sql += xmxh + "',";
				sql += (budgetMoney - totalSum) + ",";
				sql += (budgetMoney - totalSum) + ",'";
				sql += prjMap.get("en_id").toString() + "','";
				sql += SessionUtil.getParaMap().get("RG_CODE").toString() + "','总计',0,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'111',";
				sql += (budgetMoney - totalSum) + ",0)";
				sqlList.add(sql);

				// 保存指标项目明细（已安排数）
				sql = "insert into rp_xmsb (set_year,xmsbid,xmxh,en_id,rg_code,sb_type,wszt_dm,lrr_dm,lrrq,xgr_dm,xgrq,sb_code,is_idx)";
				sql += " values (";
				sql += SessionUtil.getLoginYear() + ",newid,'";
				sql += xmxh + "','";
				sql += prjMap.get("en_id").toString() + "','";
				sql += SessionUtil.getParaMap().get("RG_CODE").toString() + "','已安排数',0,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'222',0)";
				sqlList.add(sql);

				// 保存指标项目明细（本年预算）
				sql = "insert into rp_xmsb (set_year,xmsbid,xmxh,f1,f2,en_id,rg_code,sb_type,wszt_dm,lrr_dm,lrrq,xgr_dm,xgrq,sb_code,total_sum,is_idx)";
				sql += " values (";
				sql += SessionUtil.getLoginYear() + ",newid,'";
				sql += xmxh + "',";
				sql += (budgetMoney - totalSum) + ",";
				sql += (budgetMoney - totalSum) + ",'";
				sql += prjMap.get("en_id").toString() + "','";
				sql += SessionUtil.getParaMap().get("RG_CODE").toString() + "','本年预算',0,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
				sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'333',";
				sql += (budgetMoney - totalSum) + ",0)";
				sqlList.add(sql);
			}
			else
			{
				xmxh = ((Map) (dataList.get(0))).get("xmxh").toString();

				sql = "select sum(a.total_sum) as total_sum from rp_xmsb a where a.xmxh = ? and a.set_year = ? and a.sb_code not in ('111', '222')";
				sbList = dao.findBySql(sql, new Object[] { xmxh, SessionUtil.getLoginYear() });
				if (sbList != null && !sbList.isEmpty())
					totalSum = Double.parseDouble(((Map) (sbList.get(0))).get("total_sum").toString());
				else
					totalSum = 0d;

				sql = "select max(a.sb_type) as sb_type from rp_xmsb a where a.xmxh = ? and a.set_year = ? and a.sb_code not in('111','222','333')";
				sbList = dao.findBySql(sql, new Object[] { xmxh, SessionUtil.getLoginYear() });
				if (sbList != null && !sbList.isEmpty())
					num = Integer.parseInt(((Map) (sbList.get(0))).get("sb_type").toString());
				else
					num = 1;

			}

			budgetMoney = Double.parseDouble(prjMap.get("budget_money").toString());

			// 保存指标项目明细
			sql = "insert into rp_xmsb (set_year,xmsbid,xmxh,bs_id,f1,f2,en_id,rg_code,sb_type,wszt_dm,lrr_dm,lrrq,xgr_dm,xgrq,bz,sb_code,total_sum,acct_name,is_idx)";
			sql += " values (";
			sql += SessionUtil.getLoginYear() + ",newid,'";
			sql += xmxh + "','";
			sql += prjMap.get("bs_id").toString() + "',";
			sql += (budgetMoney - totalSum) + ",";
			sql += (budgetMoney - totalSum) + ",'";
			sql += prjMap.get("en_id").toString() + "','";
			sql += SessionUtil.getParaMap().get("RG_CODE").toString() + "','";
			sql += String.valueOf(num++) + "',0,'";
			sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
			sql += SessionUtil.getUserInfoContext().getUserID() + "',sysdate,'";
			sql += prjMap.get("bl_name").toString() + "','";
			sql += String.valueOf(400000 + num) + "',";
			sql += (budgetMoney - totalSum) + ",'";
			sql += "[" + prjMap.get("bs_code").toString() + "]" + prjMap.get("bs_name").toString() + "',1)";
			sqlList.add(sql);

		}

		if (!sqlList.isEmpty())
			QueryStub.getQueryTool().executeBatch(sqlList);

		return "同步成功!";
	}


	/**
	 * 创建项目编码：单位编号+项目类别拼音+流水号(000+order)
	 * 
	 * @return
	 */
	public String getNewPrjCode(String div_code)
	{
		// String pinyin = "";
		// String[] firstChar = Pinyin.getFirstChineseChar(""
		// + xmfl, 4);
		// for (int i = 0; i < firstChar.length; i++) {
		// pinyin += firstChar[i];
		// }
		String order = "";
		try
		{
			order = String.valueOf(PrjInputStub.getMethod().getPrjCount(div_code) + 1);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "创建专项编码失败！");
			return "";
		}
		int orderLength = order.length();
		switch (orderLength)
		{
			case 1:
				order = "000" + order;
				break;
			case 2:
				order = "00" + order;
				break;
			case 3:
				order = "0" + order;
				break;
			default:
				break;
		}
		// String curYear = Global.loginYear;
		// String kind = "";
		// // 如果是财政用户，则标示C，否则标示D
		// if (GlobalEx.isFisVis()) {
		// kind = "C";
		// } else {
		// kind = "D";
		// }
		// return div_code + pinyin + curYear + kind + order;
		return div_code + order;
		// return div_code + curYear + kind + order;
	}


	public DataSet getQueryProject2(String cond) throws Exception
	{
		// TODO Auto-generated method stub
		String condition = " AND (EXISTS (select * from sys_wf_complete_item b WHERE B.entity_id=A.XMXH)  OR" + " EXISTS (select * from sys_wf_current_item c WHERE c.entity_id=A.XMXH ))";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT PRJ_BTTYPE STEP_ID,EN_CODE DIV_CODE,BIS_NAME XMMC,EN_NAME DIV_NAME,BIS_CODE XMBM,EN_ID,SET_YEAR,RG_CODE,C1,C2,ACCTS,XMXH,");
		sb.append("(SELECT CHR_NAME FROM RP_BASE_EXECYCLE WHERE CHR_CODE=a.C3 AND A.SET_YEAR=SET_YEAR) AS C3,");
		sb.append("(SELECT  CHR_NAME  FROM RP_BASE_PRJSORT WHERE CHR_CODE=A.C4 AND A.SET_YEAR=SET_YEAR) AS C4,");
		sb.append("(SELECT  CHR_NAME  FROM RP_BASE_PRJSTATE  WHERE CHR_CODE=A.C6 AND A.SET_YEAR=SET_YEAR) AS C6,C5,C4 C11 ");
		sb.append("FROM VW_RP_RETURN A WHERE 1=1 ");
		sb.append(cond + " " + condition);
		sb.append(" GROUP BY PRJ_BTTYPE,EN_CODE,BIS_NAME,EN_NAME,BIS_CODE,EN_ID,SET_YEAR,RG_CODE,C1,C2,C3,C4,C5,C6,ACCTS,XMXH ");
		sb.append(" ORDER BY STEP_ID");
		return DBSqlExec.getDataSet(sb.toString());
	}


	public DataSet getQueryProject3(String cond) throws Exception
	{
		// TODO Auto-generated method stub
		String sql3 = "SELECT BIS_CODE " + "FROM  RP_PRJ_RETURN WHERE 1=1 " + cond + " GROUP BY EN_CODE,BIS_NAME,EN_NAME,BIS_CODE,EN_ID";
		cond = cond + " and xmbm in(" + sql3 + ")";
		String strSql = getQueryPrjTableName(cond, 0);
		// strSql =strSql+getFlowCondition("RP_PRJ_MAIN", "XMXH", "001", "");
		String cond1 = "AND  EXISTS (select * from sys_wf_complete_item b WHERE B.entity_id=RP_PRJ_MAIN.XMXH )";
		strSql = strSql + cond1;
		return DBSqlExec.getDataSet(strSql);
	}


	private String getQueryPrjTableName(String cond, int tableType)
	{
		StringBuffer sql = new StringBuffer();
		String tableName = "RP_PRJ_MAIN";
		if (tableType == 1)
			tableName = "RP_PRJ_HISTORY";
		sql.append("SELECT ");
		sql.append(tableName);
		sql.append(".EN_ID,");
		sql.append(tableName);
		sql.append(".XMXH,");
		sql.append(tableName);
		sql.append(".XMBM,");
		sql.append(tableName);
		sql.append(".XMMC,");
		sql.append(tableName);
		sql.append(".DIV_CODE,");
		sql.append(tableName);
		sql.append(".DIV_NAME,");
		sql.append(tableName);
		sql.append(".C1,");
		sql.append(tableName);
		sql.append(".C2,");
		sql.append("(SELECT CHR_NAME ");
		sql.append("FROM RP_BASE_EXECYCLE ");
		sql.append("WHERE CHR_CODE = ");
		sql.append(tableName);
		sql.append(".C3 ");
		sql.append("AND ");
		sql.append(tableName);
		sql.append(".SET_YEAR = SET_YEAR) AS C3,");
		sql.append("(SELECT CHR_NAME ");
		sql.append("FROM RP_BASE_PRJSORT ");
		sql.append("WHERE CHR_CODE = ");
		sql.append(tableName);
		sql.append(".C4 ");
		sql.append("AND ");
		sql.append(tableName);
		sql.append(".SET_YEAR = SET_YEAR) AS C4,");
		sql.append("C5,");
		sql.append("(SELECT CHR_NAME ");
		sql.append("FROM RP_BASE_PRJSTATE ");
		sql.append("WHERE CHR_CODE = ");
		sql.append(tableName);
		sql.append(".C6 ");
		sql.append("AND ");
		sql.append(tableName);
		sql.append(".SET_YEAR = SET_YEAR) AS C6,");
		sql.append(tableName);
		sql.append(".C7,");
		sql.append(tableName);
		sql.append(".C8,");
		sql.append(tableName);
		sql.append(".C9,");
		sql.append(tableName);
		sql.append(".C10,");
		if (tableType == 1)
			sql.append("NODE_NAME,NODE_ID,LAST_VER,");
		sql.append("ACCTS,GKDW_NAME,GKDW_ID,GKDW_CODE,SB_LY ,C4 AS C11,STEP_ID FROM ");
		sql.append(tableName);
		sql.append(" WHERE 1 = 1").append(cond);
		if (tableType == 1)
			sql.append(" order by last_ver");
		String strSql = sql.toString();
		sql.delete(0, sql.length());
		return strSql;
	}


	public String saveTbPrj(Map map) throws Exception
	{
		// TODO Auto-generated method stub
		String sql2 = "DELETE FROM RP_PRJ_MAIN WHERE XMXH='" + map.get("xmbm") + "'";
		dao.executeBySql(sql2);
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO RP_PRJ_MAIN ");
		sql.append("SELECT ");
		sql.append(map.get("set_year"));
		sql.append(" AS SET_YEAR,'");
		sql.append(map.get("xmxh"));
		sql.append("' AS XMXH,'");
		sql.append(map.get("xmbm"));
		sql.append("' AS XMBM,'");
		sql.append(map.get("xmmc"));
		sql.append("' AS XMMC,'");
		sql.append(map.get("c1"));
		sql.append("' AS C1,'");
		sql.append(map.get("c2"));
		sql.append("' AS C2,'");
		sql.append(map.get("c3"));
		sql.append("' AS C3,'");
		sql.append(map.get("c4"));
		sql.append("' AS C4,'");
		sql.append(map.get("c5"));
		sql.append("' AS C5,'");
		sql.append(map.get("c6"));
		sql.append("' AS C6,'' C7,'' C8,'' C9,'' C10,'");
		sql.append(map.get("en_id"));
		sql.append("' AS EN_ID, ");
		sql.append(map.get("rg_code"));
		sql.append(" AS RG_CODE, ");
		sql.append("'' LRR_DM, '' LRRQ,'' XGR_DM,'' XGRQ,");
		sql.append("'' WFZT_DM,'");
		sql.append(map.get("div_code"));
		sql.append("' AS DIV_CODE,'");
		sql.append(map.get("div_name"));
		sql.append("' AS DIV_NAME,");
		sql.append("'' CS_SORT,'' EN_SORT,'' GKDW_ID,");
		sql.append("'' GKDW_CODE,'");
		sql.append(map.get("accts"));
		sql.append("' AS ACCTS,'");
		sql.append(map.get("gkdw_name"));
		sql.append("' AS GKDW_NAME, ");
		sql.append("'补填','' IS_BACK,'");
		sql.append(map.get("sb_ly"));
		sql.append("' AS SB_LY ");
		sql.append(" FROM dual");
		return dao.executeBySql(sql.toString()) == 0 ? "失败" : "";
	}


	public DataSet getDivDataPop(String sYear) throws Exception
	{
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.DIV_CODE");
		String sSql = " SELECT * FROM VW_RP_DIVISION ENTAB " + " WHERE IS_LEAF = 1 AND SET_YEAR=" + sYear + " " + swhere + " UNION ALL " + " SELECT * FROM VW_RP_DIVISION TAB "
				+ " WHERE TAB.IS_LEAF <> 1 AND TAB.SET_YEAR=" + sYear + " AND " + " EXISTS (SELECT 1 FROM VW_RP_DIVISION ENTAB WHERE ENTAB.DIV_CODE LIKE TAB.DIV_CODE||'%'"
				+ " AND ENTAB.IS_LEAF = 1 AND ENTAB.SET_YEAR=" + sYear + " " + swhere + ")";
		return DBSqlExec.getDataSet(sSql);
	}


	public String prjCancelSync(ArrayList enid, ArrayList bisname, ArrayList tomoney)
	{
		String msg = "项目撤销成功";

		try
		{
			List list = new ArrayList();

			for (int i = 0; i < enid.size(); i++)
			{
				StringBuffer sql = new StringBuffer();
				String en_id = (String) enid.get(i);
				String bis_name = (String) bisname.get(i);
				String to_money = (String) tomoney.get(i);

				sql.append("delete from rp_prj_return  where ").append("en_id='" + en_id + "'").append(" and bis_name= '" + bis_name + "'").append(" and budget_money='" + to_money + "'");

				String strSql = sql.toString();
				list.add(strSql);
				sql.delete(0, sql.length());
			}
			QueryStub.getQueryTool().executeBatch(list);

		}
		catch (Exception ex)
		{
			msg = "撤销同步失败";
			ex.printStackTrace();
		}
		return msg;
	}


	public String isXmjl(String id, String name, String money) throws Exception
	{
		// TODO Auto-generated method stub
		String msg = "";
		String sql = "SELECT * FROM RP_XMJL WHERE EN_ID='" + id + "' AND XMMC='" + name + "'";
		List list = dao.findBySql(sql);
		if (list.size() > 0)
		{
			String xmxh = ((Map) (list.get(0))).get("xmxh").toString();
			String sql2 = "UPDATE RP_XMSB SET F11='" + money + "' WHERE XMXH='" + xmxh + "' AND SB_CODE='333'";
			if (dao.executeBySql(sql2) == 0)
			{
				msg = "项目序号" + xmxh + "追加失败";
				return msg;
			}
		}
		else
		{
			return "ok";
		}

		return msg;
		// return false;
	}

}
