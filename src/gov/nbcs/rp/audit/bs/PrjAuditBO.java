/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 项目绩效-服务端类
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.audit.bs;

import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.bs.PrjectManageBO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.datactrl.SQLTool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.foundercy.pf.dictionary.interfaces.IControlDictionary;
import com.foundercy.pf.util.DTO;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.UUIDRandom;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;




public class PrjAuditBO extends PrjectManageBO implements IPrjAudit
{
	// private GeneralDAO dao;
	//
	// public GeneralDAO getDao() {
	// return dao;
	// }
	//
	// public void setDao(GeneralDAO dao) {
	// this.dao = dao;
	// }

	public DataSet getPrjDataAll(String userCode, String aDivCode, String setYear, String filter, String filter2, String batch_no, String dataType) throws Exception
	{
		return getPrjInBo(userCode, "FB_P_BASE", aDivCode, "", setYear, filter, filter2, batch_no, dataType);
	}


	/**
	 * 获取项目信息
	 * 
	 * @param tableName
	 * @param aDivCode
	 * @param prjCode
	 * @param setYear
	 * @param filter
	 * @param filter2
	 * @return
	 * @throws Exception
	 */
	private DataSet getPrjInBo(String userCode, String tableName, String aDivCode, String prjCode, String setYear, String filter, String filter2, String batch_no, String data_type) throws Exception
	{
		String sql = getPrjApprInfoSql(tableName, aDivCode, prjCode, setYear, filter, filter2, batch_no, data_type);
		// 添加过滤条件
		StringBuffer sqlLast = new StringBuffer();
		sqlLast.append("select * from (" + sql + ") nt");
		if (!"FB_P_BASE".equalsIgnoreCase(tableName))
		{
			sqlLast.append(" left join (select d2.wname||'  '||case when d1.wisend = 1 then '审核通过' else '未审核' end wname ");
			sqlLast.append(" ,d1.prj_code as pj1, isnull(d1.wid,-1) as wid,d1.nid, d1.wisend, d1.auditcontext, d1.isback");
			sqlLast.append(" from fb_p_appr_audit_curstate d1,fb_p_appr_audit_wstep d2");
			sqlLast.append(" where d1.wid = d2.wid ");
			sqlLast.append(" ) d on nt.prj_code = d.pj1 ");
			if (!Common.isNullStr(userCode))
			{
				sqlLast.append(" where exists (select 1 from fb_p_appr_usertoprj up where ");
				sqlLast.append(" up.user_code like '" + userCode + "%' and ");
				sqlLast.append(" nt.prj_code = up.prj_code and nt.set_year = up.set_year " + " and nt.prj_code not in(select sort_code from fb_p_appr_sort"
						+ ") or nt.prj_code in (select sort_code from fb_p_appr_sort )) ");
			}
		}
		sqlLast.append(" order by div_code,xh,prj_code");
		DataSet ds = DataSet.create();
		ds.setSqlClause(sqlLast.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public DataSet getPrjInBoNotUserTo(String userCode, String aDivCode, String prjCode, String setYear, String filter, String filter2, String batch_no, String data_type) throws Exception
	{
		String sql = getPrjApprInfoSql("USERCHOOSE", aDivCode, prjCode, setYear, filter, filter2, batch_no, data_type);
		// 添加过滤条件
		StringBuffer sqlLast = new StringBuffer();
		sqlLast.append("select * from (" + sql + ") nt");
		sqlLast.append(" where not exists");
		sqlLast.append(" (select vb.div_code");
		sqlLast.append(" from vw_fb_user vu, vw_fb_division vb, fb_p_appr_usertoprj vup");
		sqlLast.append(" where vu.USER_CODE = vup.user_code");
		sqlLast.append(" and vu.BELONG_ORG = vb.EN_ID");
		sqlLast.append(" and nt.prj_code = vup.prj_code");
		sqlLast.append(" and nt.set_year = vup.set_year");
		sqlLast.append(" and vu.user_code = '" + userCode + "'");
		sqlLast.append(")");
		sqlLast.append(" and nt.div_code in (");
		sqlLast.append(" select vfb.div_code from sys_user_org vuo,");
		sqlLast.append(" vw_fb_division vfb,vw_fb_user vfu");
		sqlLast.append(" where vuo.org_id = vfb.EN_ID");
		sqlLast.append(" and vuo.user_id = vfu.USER_ID");
		sqlLast.append(" and vfu.USER_CODE = '" + userCode + "'");
		sqlLast.append(")");
		sqlLast.append("order by div_code,xh,prj_code");
		sqlLast.append("");
		DataSet ds = DataSet.create();
		ds.setSqlClause(sqlLast.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	private String getPrjApprInfoSql(String tableName, String aDivCode, String prjCode, String setYear, String filter, String filter2, String batch_no, String data_type)
	{
		// String batchNoSel = "1";
		// String dataTypeSel = "1";
		// if (Integer.parseInt(batch_no) == 1) {
		// // 一上不变
		// batchNoSel = batch_no;
		// dataTypeSel = data_type;
		// } else {
		// // 二上的时候，挑选则挑选二上的原始数，如果有改动，则修改视图。
		// batchNoSel = "1";
		// dataTypeSel = "1";
		// }
		StringBuffer sql = new StringBuffer();
		sql.append("select " + "nullif(a." + SORT_CODE + ",'999999999')" + "||a.div_code as xh,a.div_code,a.div_name, a.div_kind ,a.prj_code,");
		sql.append("a.prj_name,a.SORT_ALL,a.PRJSORT_CODE,a." + SORT_FNAME + ",");
		sql.append("c.prjsort_name,a.acct_code,a.acct_name,");
		sql.append("case when (select count(1) from fb_p_appr ");
		sql.append("ab where ab.prjcode = a.prj_code and a.batch_no = ");
		sql.append("ab.batch_no and a.data_type = ab.data_type)>0 then ");
		sql.append("'是' else '否' end ISINPUTPRJAPPR,");
		sql.append("a.prjattr_code,");
		sql.append("(select e.item_name from fb_p_input_combox_values ");
		sql.append("e where e.comp_id=1 and e.item_code = ");
		sql.append("a.prjattr_code) as prjattr_name,");
		sql.append("a.prjaccord_code,");
		sql.append("(select e.item_name from fb_p_input_combox_values");
		sql.append(" e where e.comp_id=2 and e.item_code = a.prjaccord_code)");
		sql.append(" as prjaccord_name,");
		sql.append("a.PRJ_TRANSFER,");
		sql.append("(select e.item_name from fb_p_input_combox_values ");
		sql.append("e where e.comp_id=3 and e.item_code = a.PRJ_TRANSFER)");
		sql.append(" as PRJ_TRANSFER_name,");
		sql.append("a.prj_content,");
		sql.append("a.set_year,");
		sql.append("a.f1,a.f2,a.f3,a.f4,a.f5,a.f6,a.f7,a.f8,a.f9,a.f10");
		sql.append(",a.f11,a.f12,a.f13,a.f14,a.f15");
		sql.append(",a.f16,a.f17,a.f18,a.f19,a.f20,");
		sql.append("a.data_type,a.batch_no from ");
		if ("FB_P_BASE".equalsIgnoreCase(tableName))
		{
			sql.append(PROJECT_SOURCE);
			sql.append(" a,fb_e_prjsort c");
			sql.append(" where ");
			sql.append(" a.batch_no = " + batch_no);
			sql.append(" and a.data_type = " + data_type);
		}
		else if ("FB_P_BASE_APPR".equalsIgnoreCase(tableName))
		{
			sql.append(" FB_P_BASE_APPR a,fb_e_prjsort c,FB_P_BASE_APPR ee");
			sql.append(" where a.prj_code = ee.prj_code and ");
			sql.append("a.batch_no = ee.batch_no and ");
			sql.append("a.data_type = ee.data_type");
			sql.append(" and a.batch_no = " + batch_no);
			sql.append(" and a.data_type = " + data_type);
		}
		else if ("USERCHOOSE".equalsIgnoreCase(tableName))
		{
			sql.append(" FB_P_BASE_APPR a,fb_e_prjsort c,FB_P_BASE_APPR ee");
			sql.append(" where a.prj_code = ee.prj_code and ");
			sql.append("a.batch_no = ee.batch_no and ");
			sql.append("a.data_type = ee.data_type");
			sql.append(" and a.batch_no = " + batch_no);
			sql.append(" and a.data_type = " + data_type);
		}
		else
		{
			sql.append(" FB_P_BASE_APPR a,fb_e_prjsort c");
			sql.append(" where a.batch_no = " + batch_no);
			sql.append(" and a.data_type = " + data_type);
		}
		if (!Common.isNullStr(aDivCode))
		{
			sql.append(" and a.div_code like '" + aDivCode + "%'");
		}
		if (!Common.isNullStr(prjCode))
		{
			sql.append(" and a.prj_code like '");
			sql.append(prjCode);
			sql.append("%'");
		}
		if (!Common.isNullStr(setYear))
		{
			sql.append(" and a.set_year = '" + setYear + "'");
		}
		if (!Common.isNullStr(filter))
		{
			sql.append(" and " + filter);
		}
		// 项目中数据未在绩效项目中
		if ("FB_P_BASE".equalsIgnoreCase(tableName))
		{
			// 项目挑选未挑选项目
			sql.append(" and not exists(select 1 from ");
			sql.append(IPrjAudit.PrjTable.TABLENAME_APPR);
			sql.append(" tt where a.prj_code = tt.prj_code");
			sql.append(" and a.set_year = tt.set_year and ");
			sql.append("tt.batch_no = " + batch_no + " and tt.data_type = " + data_type + " )");
		}
		sql.append(" and a.prjsort_code = c.prjsort_code");
		sql.append("");
		if (!Common.isNullStr(filter2))
		{
			sql.append(filter2);
		}
		if (!"FB_P_BASE".equalsIgnoreCase(tableName))
		{
			sql.append(" union all ");
			sql.append(" select ");
			sql.append(" sort_code as xh,i.div_code, ");
			sql.append(" i.div_name, '' as div_kind ,sort_code as prj_code,");
			sql.append("sort_name as prj_name,0 as SORT_ALL,'' as PRJSORT_CODE,");
			sql.append("'' as " + SORT_FNAME + ",");
			sql.append("'' as prjsort_name,'' as acct_code,'' as acct_name,");
			sql.append("'' as  ISINPUTPRJAPPR,");
			sql.append("'' as prjattr_code,");
			sql.append("'' as prjattr_name,");
			sql.append("'' as prjaccord_code,");
			sql.append("'' as prjaccord_name,");
			sql.append("'' as PRJ_TRANSFER,");
			sql.append("'' as PRJ_TRANSFER_name,");
			sql.append("'' as prj_content,");
			sql.append(Integer.parseInt(setYear) + " as set_year,");
			// sql.append("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
			sql.append("(select sum(f1) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f2) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f3) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f4) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f5) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f6) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f7) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f8) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f9) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f10) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f11) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f12) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f13) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f14) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f15) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f16) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f17) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f18) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f19) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");
			sql.append("(select sum(f20) from fb_p_base_appr pba where ");
			sql.append(" pba.batch_no = " + batch_no + " and pba.data_type = " + data_type);
			sql.append(" and pba.div_code like '" + aDivCode + "%' ");
			sql.append(" and instr(pba.cl1,g.sort_code)=1),");

			sql.append("0 as data_type,0 as batch_no ");
			sql.append(" from fb_p_appr_sort g,vw_fb_division i where 1=1 ");
			sql.append(" and i.div_code like '" + aDivCode + "%'");
			sql.append(" and not exists(");
			sql.append(" select 1 from FB_P_APPR h where g.sort_code=h.prjcode ");
			sql.append(" and  h.batch_no = " + batch_no + " and h.data_type = " + data_type + ")");
			sql.append(" and g.DIVCODES is null");
			sql.append(" union all ");
			sql.append(" select ");
			sql.append(" sort_code as xh,i.div_code, ");
			sql.append(" i.div_name, '' as div_kind ,sort_code as prj_code,");
			sql.append("sort_name as prj_name,0 as SORT_ALL,'' as PRJSORT_CODE,");
			sql.append("'' as " + SORT_FNAME + ",");
			sql.append("'' as prjsort_name,'' as acct_code,'' as acct_name,");
			sql.append("'' as  ISINPUTPRJAPPR,");
			sql.append("'' as prjattr_code,");
			sql.append("'' as prjattr_name,");
			sql.append("'' as prjaccord_code,");
			sql.append("'' as prjaccord_name,");
			sql.append("'' as PRJ_TRANSFER,");
			sql.append("'' as PRJ_TRANSFER_name,");
			sql.append("'' as prj_content,");
			sql.append(Integer.parseInt(setYear) + " as set_year,");
			sql.append("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
			// sql.append("(select sum(f1) from fb_p_base_appr pba where ");
			// sql.append(" pba.batch_no = "+batch_no+" and pba.data_type =
			// "+data_type);
			// sql.append(" and pba.div_code like '"+aDivCode+"%' ");
			// sql.append(" and
			// instr(pba.cl1,g.sort_code)=1),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");

			sql.append("0 as data_type,0 as batch_no ");
			sql.append(" from fb_p_appr_sort g,vw_fb_division i where 1=1 ");
			sql.append(" and i.div_code like '%" + aDivCode + "%'");
			sql.append(" and not exists(");
			sql.append(" select 1 from FB_P_APPR h ");
			sql.append(" where g.sort_code=h.prjcode ");
			sql.append(" and  h.batch_no = " + batch_no + " and h.data_type = " + data_type + ")");
			sql.append(" and g.DIVCODES like '%" + aDivCode + "%'");
			sql.append(" order by xh,prj_code");
		}
		else
		{
			sql.append(" order by a.prj_code");
		}

		return sql.toString();
	}


	/**
	 * 获取项目主表数据集
	 */
	public DataSet getMainPrjData(String userCode, String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception
	{
		return getPrjInBo(userCode, "CHOOSEDPRJ", aDivCode, "", setYear, filter1, filter2, batch_no, dataType);
	}


	/**
	 * 获取项目明细数据集
	 */
	public DataSet getPrjData(String userCode, String aDivCode, String PrjCode, String setYear, String filter, String batch_no, String dataType) throws Exception
	{
		return getPrjInBo(userCode, IPrjAudit.PrjTable.TABLENAME_APPR, aDivCode, PrjCode, setYear, "", "", batch_no, dataType);
	}


	/**
	 * 获取项目主表数据集(不在用户对项目表中的数据)
	 */
	public DataSet getMainPrjDataNotUserTo(String userCode, String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception
	{
		return getPrjInBoNotUserTo(userCode, aDivCode, "", setYear, filter1, filter2, batch_no, dataType);
	}


	/**
	 * 获取资金来源的数据集
	 */
	public DataSet getFundsData() throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM FB_IAE_PAYOUT_FUNDSOURCE order by lvl_id");
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	/**
	 * 获取项目绩效类型信息
	 */
	public DataSet getPriaseTypeData(String setYear) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM FB_S_PUBCODE WHERE TYPEID = 'PRJAPPRAISETYPE' and set_year = " + setYear + " ORDER BY lvl_id");
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	/**
	 * 获取绩效评价指标的表体数据集
	 */
	public DataSet getPriaseStandData(String parCode, String setYear, String batch_no, String dataType) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ");
		sql.append(IPrjAudit.DetailInfo.TableName);
		sql.append(" where parcode = '");
		sql.append(parCode);
		sql.append("' and set_year=");
		sql.append(setYear);
		sql.append(" and batch_no = " + batch_no);
		sql.append(" and data_type = ");
		sql.append(dataType);
		sql.append(" order by batch_no,data_type,parcode,sortcode,detailcode");
		ds.setSqlClause(sql.toString());
		ds.open();
		return ds;
	}


	/**
	 * 获取项目绩效主表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjApprMainData(String prjCode, String setYear, String batch_no, String data_type) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select DATA_TYPE,BATCH_NO,PRJCODE,SORTCODE,ISINPUTPRJAPPR,SBDEPARTMENT,SBMANAGER ,SBTEL,EXECUTEDEPARTMENT,EXECUTEMANAGER,");
		sql.append("EXECUTETEL,TOTLEMONEY,FINANCEMONEY,DIVCOLMONEY,OTHERMONEY,PRJBEGINDATE,PRJENDDATE,PRJGENERALCONTENT,");
		sql.append("SPEED,OLDAPPRABOUT,TARGETGENERAL,TARGETSTAGE,TARGETTIME,TARGETCOST,TARGETQUALITY,");
		sql.append("SET_YEAR,RG_CODE,ROW_ID,DIV_CODE,FUNDSPLAN");
		sql.append(" from ");
		sql.append(IPrjAudit.MainTable.TableName);
		sql.append(" where ");
		sql.append(IPrjAudit.MainTable.GeneralInfo.PrjCode);
		sql.append(" = '");
		sql.append(prjCode);
		sql.append("' and");
		sql.append(" set_year=");
		sql.append(setYear);
		sql.append(" and batch_no = " + batch_no + " and data_type = ");
		sql.append(data_type);
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	/**
	 * 保存信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public InfoPackage saveData(DataSet dsMain, DataSet dsDetail)
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			dsMain.setPrimarykey(new String[] { IPrjAudit.MainTable.GeneralInfo.PrjCode, "batch_no", "data_type", "set_year" });
			// saveAuditInfo(dsMain);
			dsMain.post();
			dsDetail.setPrimarykey(new String[] { "PARCODE", "DETAILCODE", "PARCODE", "SET_YEAR", "DATA_TYPE", "BATCH_NO" });
			dsDetail.post();
			dsMain.beforeFirst();
		}
		catch (Exception ee)
		{
			info.setSuccess(false);
			info.setsMessage(Common.nonNullStr(ee));
			return info;
		}
		return info;
	}


	/**
	 * 更新blob内容：
	 * 
	 * @throws Exception
	 */
	public void modifyDocBlobs(String tableName, String rowid, String set_year, byte[] longColData) throws Exception
	{
		// 服务器类型
		// String serverType =
		// PubInterfaceStub.getServerMethod().getServerType();
		if (GlobalEx.loginmode == 0)
		{// 在线
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			sql.append(IPrjAudit.MainTable.Affix.AFFIX_CONT);
			sql.append(" ,LENGTH(");
			sql.append(IPrjAudit.MainTable.Affix.AFFIX_CONT);
			sql.append(")+1 LEN FROM ");
			sql.append(tableName);
			sql.append(" where ");
			sql.append(IPrjAudit.MainTable.Affix.ROW_ID);
			sql.append(" = '");
			sql.append(rowid);
			sql.append("' and set_year=");
			sql.append(set_year);
			sql.append("  FOR UPDATE ");
			Session session = dao.getSession();
			PreparedStatement pstmt2 = session.connection().prepareStatement(sql.toString());
			ResultSet rs2 = pstmt2.executeQuery();
			if ((longColData != null) && (longColData.length > 0))
			{
				if (rs2.next())
				{
					Blob b = rs2.getBlob("AFFIX_CONT");
					if (b instanceof weblogic.jdbc.vendor.oracle.OracleThinBlob)
					{
						weblogic.jdbc.vendor.oracle.OracleThinBlob blob = (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs2.getBlob(IPrjAudit.MainTable.Affix.AFFIX_CONT);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					}
					else if (b instanceof oracle.sql.BLOB)
					{
						BLOB blob = (BLOB) rs2.getBlob(IPrjAudit.MainTable.Affix.AFFIX_CONT);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					}
					else
					{
						BLOB blob = (BLOB) rs2.getBlob(IPrjAudit.MainTable.Affix.AFFIX_CONT);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					}
				}
			}
			rs2.close();
			pstmt2.close();
			if (session != null)
			{
				dao.closeSession(session);
			}
		}
		else
		{// 离线
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ");
			sql.append(tableName);
			sql.append(" set ");
			sql.append(IPrjAudit.MainTable.Affix.AFFIX_CONT);
			sql.append(" = ? where ");
			sql.append(" ROW_ID='");
			sql.append(rowid);
			sql.append("'");
			Session session = dao.getSession();
			PreparedStatement pstmt2 = session.connection().prepareStatement(sql.toString());
			ByteArrayInputStream bis = new ByteArrayInputStream(longColData);
			pstmt2.setBinaryStream(1, bis, longColData.length);
			pstmt2.executeUpdate();
			pstmt2.close();
			if (session != null)
			{
				dao.closeSession(session);
			}
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.prj.prjinput.ibs.IPrjInput#getDocBlob(int,
	 *      java.lang.String)
	 */
	public byte[] getDocBlob(String tableName, int i, String rowid, String setYear) throws SQLException, IOException, ClassNotFoundException
	{
		InputStream is = getBigDataStream(tableName, rowid, setYear);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		if (is == null)
		{
			return null;
		}
		else
		{
			int bit;
			int count = 1;
			is.skip(1024 * 1024 * (i - 1));
			while (((bit = is.read()) != -1) && (count <= 1024 * 1024))
			{
				data.write(bit);
				count++;
			}
			is.close();
			return data.toByteArray();
		}
	}


	/**
	 * 获取blob内容
	 * 
	 * @param row_id
	 *            主键
	 * @return 返回InputStream
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private InputStream getBigDataStream(String tableName, String rowid, String setYear) throws SQLException, IOException, ClassNotFoundException
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(IPrjAudit.MainTable.Affix.AFFIX_CONT);
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" where ");
		sql.append(IPrjAudit.MainTable.Affix.ROW_ID);
		sql.append(" = '");
		sql.append(rowid);
		sql.append("' and set_year=");
		sql.append(setYear);

		InputStream is = null;
		Session session = dao.getSession();
		PreparedStatement pstmt2 = session.connection().prepareStatement(sql.toString());
		ResultSet rs2 = pstmt2.executeQuery();
		if (rs2.next())
		{
			if (Global.loginmode == 0)
			{// 在线
				is = rs2.getBlob(1).getBinaryStream();
			}
			else
			{// 离线
				is = rs2.getBinaryStream(1);
			}
		}
		rs2.close();
		pstmt2.close();
		if (session != null)
		{
			if (session.isConnected() || session.isOpen())
			{
				dao.closeSession(session);
				// if (session != null)
				// session.close();
			}
		}
		return is;
	}


	public DataSet getPrjSort(String setYear) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT prjsort_code,prjsort_name,'['||prjsort_code||']'||prjsort_name as viewname,set_year FROM fb_e_prjsort where set_year=" + setYear + " order by prjsort_code");
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public DataSet getPrjAttr(String compID, String setYear) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT item_code,'['||item_code||']'||item_name as viewname,item_name FROM fb_p_input_combox_values where comp_id = '" + compID + "' and set_year = " + setYear
				+ " order by item_code");
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public void doChoosePrj(String filter, int batch_no, int data_type) throws Exception
	{
		for (int i = 0; i < batch_no; i++)
		{
			StringBuffer sql = new StringBuffer();
			int dataTypea = 0;
			if (i == 0)
			{
				dataTypea = data_type;
			}
			else
			{
				dataTypea = (data_type == 0) ? 1 : 0;
			}
			StringBuffer sqlD = new StringBuffer();
			sqlD.append("delete from ");
			sqlD.append(IPrjAudit.PrjTable.TABLENAME_APPR);
			sqlD.append(filter);
			sqlD.append(" and batch_no = " + batch_no + " and data_type = " + dataTypea);
			sql.append("insert into ");
			sql.append(IPrjAudit.PrjTable.TABLENAME_APPR + "(");
			sql.append("row_id,set_year, batch_no, data_type, ver_no, opt_no, data_src");
			sql.append(", data_attr, prj_type, en_id, div_kind, div_code, div_name, mb_id, ");
			sql.append("dept_code, dept_name, prj_code, prj_name, bs_id, acct_code, acct_name,");
			sql.append(" prjsort_code, prjattr_code, prjaccord_code, prjstd_code, stock_flag,");
			sql.append(" stock_type, start_date, end_date, prj_content, prj_manager, sort_all,");
			sql.append(" sort_charge, sort_div, prj_status, audit_status, isperennial, revisectl_code,");
			sql.append(" report_flag, detail_degree, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11,");
			sql.append(" c12, c13, c14, c15, c16, c17, c18, c19, c20, cl1, cl2, cl3, f1, f2, f3, f4, ");
			sql.append("f5, n1, n2, n3, n4, n5, d1, d2, rg_code, last_ver, dep_status, fis_status, ");
			sql.append("prj_transfer, prj_parent, ca1, ca2, ca3, ca4, ca5, cs1, cs2, cs3, cs4, cs5");
			sql.append(")");
			sql.append(" select  ");
			sql.append("row_id,set_year, " + batch_no + ", " + dataTypea + ", ver_no, opt_no, data_src");
			sql.append(", data_attr, prj_type, en_id, div_kind, div_code, div_name, mb_id, ");
			sql.append("dept_code, dept_name, prj_code, prj_name, bs_id, acct_code, acct_name,");
			sql.append(" prjsort_code, prjattr_code, prjaccord_code, prjstd_code, stock_flag,");
			sql.append(" stock_type, start_date, end_date, prj_content, prj_manager, sort_all,");
			sql.append(" sort_charge, sort_div, prj_status, audit_status, isperennial, revisectl_code,");
			sql.append(" report_flag, detail_degree, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11,");
			sql.append(" c12, c13, c14, c15, c16, c17, c18, c19, c20, cl1, cl2, cl3, f1, f2, f3, f4, ");
			sql.append("f5, n1, n2, n3, n4, n5, d1, d2, rg_code, last_ver, dep_status, fis_status, ");
			sql.append("prj_transfer, prj_parent, ca1, ca2, ca3, ca4, ca5, cs1, cs2, cs3, cs4, cs5");
			sql.append(" from " + PROJECT_SOURCE);
			sql.append(filter);
			int batchNo1 = batch_no;
			int dataType1 = dataTypea;
			// if (batch_no == 2) {
			// batchNo1 = 1;
			// dataType1 = 1;
			// }
			sql.append(" and batch_no = " + batchNo1 + " and data_type = " + dataType1);
			List list = new ArrayList();
			list.add(sqlD.toString());
			list.add(sql.toString());
			QueryStub.getQueryTool().executeBatch(list);
		}
		int iExistBeforeData = DBSqlExec.getRecordCount(IPrjAudit.MainTable.TableName, filter.replaceAll("where", "").replaceAll("prj_code", "prjcode") + " and batch_no =" + (batch_no - 1)
				+ " and data_type=1");
		if (iExistBeforeData > 0)
		{
			// 如果上一批次已经录入数据，那么这一批次直接取上一批次的数据来生成。
			// 牵涉到四张表，一张是主表，一张是指标表，一张是附件表,一张专家评审表
			List list = new ArrayList();
			StringBuffer sql1 = new StringBuffer();
			sql1.append("delete from " + IPrjAudit.MainTable.TableName);
			sql1.append(filter.replaceAll("prj_code", "prjcode"));
			sql1.append(" and batch_no =" + batch_no);
			list.add(sql1.toString());
			for (int i = 0; i < 2; i++)
			{
				StringBuffer sql11 = new StringBuffer();
				sql11.append("insert into fb_p_appr(");
				sql11.append("div_code, prjcode, sortcode, isinputprjappr, sbdepartment, ");
				sql11.append("sbmanager, sbtel, executedepartment, executemanager, executetel, ");
				sql11.append("totlemoney, financemoney, divcolmoney, othermoney, prjbegindate, prjenddate, ");
				sql11.append("prjgeneralcontent, speed, oldapprabout, targetgeneral, targetstage, targettime,");
				sql11.append(" targetcost, targetquality, set_year, batch_no, data_type, rg_code, row_id, fundsplan");
				sql11.append(") select ");
				sql11.append("div_code, prjcode, sortcode, isinputprjappr, sbdepartment, ");
				sql11.append("sbmanager, sbtel, executedepartment, executemanager, executetel, ");
				sql11.append("totlemoney, financemoney, divcolmoney, othermoney, prjbegindate, prjenddate, ");
				sql11.append("prjgeneralcontent, speed, oldapprabout, targetgeneral, targetstage, targettime,");
				sql11.append(" targetcost, targetquality, set_year, " + batch_no + ", " + i + ", rg_code, row_id, fundsplan");
				sql11.append(" from fb_p_appr ");
				sql11.append(filter.replaceAll("prj_code", "prjcode"));
				sql11.append(" and batch_no = " + (batch_no - 1) + " and data_type=1");
				list.add(sql11.toString());
			}
			StringBuffer sql2 = new StringBuffer();
			sql2.append("delete from " + IPrjAudit.DetailInfo.TableName);
			sql2.append(filter.replaceAll("prj_code", "parcode"));
			sql2.append(" and batch_no =" + batch_no);
			list.add(sql2.toString());
			for (int i = 0; i < 2; i++)
			{
				StringBuffer sql22 = new StringBuffer();
				sql22.append("insert into fb_p_appr_detail(");
				sql22.append("detailcode, detailname, sortcode, pubindex, singleindex, ");
				sql22.append(" set_year, rg_code, parcode, div_code, end_flag, batch_no, data_type");
				sql22.append(")");
				sql22.append("select ");
				sql22.append("detailcode, detailname, sortcode, pubindex, singleindex, ");
				sql22.append(" set_year, rg_code, parcode, div_code, end_flag, " + batch_no + "," + i + " ");
				sql22.append(" from " + IPrjAudit.DetailInfo.TableName);
				sql22.append(filter.replaceAll("prj_code", "parcode"));
				sql22.append(" and batch_no =" + (batch_no - 1) + " and data_type=1");
				list.add(sql22.toString());
			}

			StringBuffer sql3 = new StringBuffer();
			sql3.append("delete from " + IPrjAudit.MainTable.Affix.TABLENAME);
			sql3.append(filter);
			sql3.append(" and batch_no =" + batch_no);
			list.add(sql3.toString());
			for (int i = 0; i < 2; i++)
			{
				StringBuffer sql33 = new StringBuffer();
				sql33.append("insert into fb_p_affix_appr(");
				sql33.append("row_id, set_year, div_code, prj_code, affix_title,");
				sql33.append("affix_type, affix_file, affix_cont, c1, c2, c3, c4,");
				sql33.append("c5, n1, n2, n3, n4, f1, f2, rg_code, last_ver, batch_no, data_type");
				sql33.append(")");
				sql33.append("select ");
				sql33.append("newid, set_year, div_code, prj_code, affix_title,");
				sql33.append("affix_type, affix_file, affix_cont, c1, c2, c3, c4,");
				sql33.append("c5, n1, n2, n3, n4, f1, f2, rg_code, last_ver, " + batch_no + ", " + i);
				sql33.append(" from " + IPrjAudit.MainTable.Affix.TABLENAME);
				sql33.append(filter);
				sql33.append(" and batch_no =" + (batch_no - 1) + " and data_type=1");
				list.add(sql33.toString());
			}

			StringBuffer sql4 = new StringBuffer();
			sql4.append("delete from " + IPrjAudit.AuditNewInfo.EXPERT.TableName);
			sql4.append(filter);
			sql4.append(" and batch_no =" + batch_no);
			list.add(sql4.toString());
			for (int i = 0; i < 2; i++)
			{
				StringBuffer sql44 = new StringBuffer();
				sql44.append("insert into fb_p_appr_expert(");
				sql44.append("row_id, set_year, div_code, prj_code, affix_title,");
				sql44.append("affix_type, affix_file, affix_cont, last_ver,");
				sql44.append("batch_no, data_type, expert_id, expert_name, expert_sug, opt_id");
				sql44.append(") select ");
				sql44.append("row_id, set_year, div_code, prj_code, affix_title,");
				sql44.append("affix_type, affix_file, affix_cont, last_ver,");
				sql44.append(batch_no + ", " + i + ", expert_id, expert_name, expert_sug, opt_id");
				sql44.append(" from " + IPrjAudit.AuditNewInfo.EXPERT.TableName);
				sql44.append(filter);
				sql44.append(" and batch_no =" + (batch_no - 1) + " and data_type=1");
				list.add(sql44.toString());
			}
			QueryStub.getQueryTool().executeBatch(list);
		}
	}


	/**
	 * 删除时要判断是否已经录入项目绩效，如果录入的话，则不允许再删除
	 */
	public InfoPackage doCancelChoosePrj(String filter, int batch_no, int data_type) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		filter = filter + " and batch_no =" + batch_no + " and data_type=" + data_type;
		int r = DBSqlExec.getRecordCount("fb_p_appr_usertoprj", filter.replaceAll("where", ""));
		if (r > 0)
		{
			info.setSuccess(false);
			info.setsMessage("存在项目已经设置了用户对应关系，请先取消用户对应关系后再删除");
			return info;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ");
		sql.append(IPrjAudit.PrjTable.TABLENAME_APPR);
		sql.append(" ");
		sql.append(filter);
		QueryStub.getQueryTool().executeUpdate(sql.toString());

		List list = new ArrayList();
		StringBuffer sql1 = new StringBuffer();
		sql1.append("delete from " + IPrjAudit.MainTable.TableName);
		sql1.append(filter.replaceAll("prj_code", "prjcode"));
		sql1.append(" and batch_no =" + batch_no);
		list.add(sql1.toString());

		StringBuffer sql2 = new StringBuffer();
		sql2.append("delete from " + IPrjAudit.DetailInfo.TableName);
		sql2.append(filter.replaceAll("prj_code", "parcode"));
		sql2.append(" and batch_no =" + batch_no);
		list.add(sql2.toString());

		StringBuffer sql3 = new StringBuffer();
		sql3.append("delete from " + IPrjAudit.MainTable.Affix.TABLENAME);
		sql3.append(filter);
		sql3.append(" and batch_no =" + batch_no);
		list.add(sql3.toString());

		StringBuffer sql4 = new StringBuffer();
		sql4.append("delete from " + IPrjAudit.AuditNewInfo.EXPERT.TableName);
		sql4.append(filter);
		sql4.append(" and batch_no =" + batch_no);
		list.add(sql4.toString());
		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	/**
	 * 获取方案分组列表 注意：这里不处理‘分组类型--系统内置’
	 */
	public DataSet getSchemeGroup(String SCHEME_CODE)
	{
		String sql = " select GROUP_CODE TAB_CODE,GROUP_NAME TAB_ZH_NAME,PAR_CODE,0 isGroup" + " from FB_O_FS_GROUP where GROUP_CODE ='{0FF37881-C447-11DE-B912-EBA08E614509}'";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		try
		{
			ds.open();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ds;
	}


	/**
	 * 获取附件信息
	 */
	public DataSet getAffixInfo(String divCode, String prjCode, String setYear, String batch_no, String data_type) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append(" select row_id,set_year,prj_code,AFFIX_TITLE,AFFIX_TYPE,AFFIX_FILE,rg_code,last_ver,batch_no,data_type from ");
		sql.append(IPrjAudit.MainTable.Affix.TABLENAME);
		sql.append(" where set_year= ");
		sql.append(setYear);
		if (!Common.isNullStr(prjCode))
		{
			sql.append(" and prj_code='");
			sql.append(prjCode);
			sql.append("'");
		}
		if (!Common.isNullStr(divCode))
		{
			sql.append(" and div_code like '");
			sql.append(divCode);
			sql.append("%'");
		}
		sql.append(" and batch_no = " + batch_no + " and data_type = ");
		sql.append(data_type);
		ds.setSqlClause(sql.toString());
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public void dsPost(DataSet ds) throws Exception
	{
		if (IPrjAudit.DetailInfo.TableName.equalsIgnoreCase(ds.getName()))
		{
			ds.setPrimarykey(new String[] { IPrjAudit.DetailInfo.Index.PARCODE, IPrjAudit.DetailInfo.Index.DETAILCODE, IPrjAudit.DetailInfo.Index.SortCode, IPrjAudit.DetailInfo.Index.SETYEAR,
					"DIV_CODE", "BATCH_NO", "DATA_TYPE" });
		}
		if (IPrjAudit.AuditNewInfo.EXPERT.TableName.equals(ds.getName()))
		{
			ds.setPrimarykey(new String[] { IPrjAudit.AuditNewInfo.EXPERT.ROW_ID, IPrjAudit.AuditNewInfo.EXPERT.SET_YEAR });
		}
		if (IPrjAudit.AuditNewInfo.AudtiStep.TableName.equals(ds.getName()))
		{
			ds.setPrimarykey(new String[] { IPrjAudit.AuditNewInfo.WID, IPrjAudit.AuditNewInfo.SET_YEAR });
		}
		ds.post();
	}


	/**
	 * 带部门单位数据集
	 */
	public DataSet getDepToDivData(String sYear, int levl, boolean isRight, int batch_no) throws Exception
	{
		String sSql = "";
		if (isRight)
		{
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a "
					+ " where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code = a.Dep_code and a.set_year=ENTAB.set_year " + swhere + " )" + " and a.set_year =" + sYear
					+ " union all " + " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year=" + sYear + " and a.level_num <= "
					+ String.valueOf(levl) + " " + swhere;
		}
		else
		{
			sSql = " select distinct dep_id as EN_ID,dep_id as EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a "
					+ " where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year)" + " and a.set_year =" + sYear + " union all "
					+ " select distinct a.EN_ID,dep_ID||a.EN_ID as EID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code = ENTAB.div_code and a.set_year=" + sYear;
		}
		// sSql = "select * from (" + sSql + ") a " +
		// getAuditDivFilter(batch_no);
		// sSql = "select distinct dep_id as EN_ID,dep_id as
		// EID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,"
		// + "dep_name as code_name from vw_fb_department a where exists (select
		// 1 from fb_u_dePtodiv ENTAB where "
		// + "ENTAB.dep_code = a.Dep_code and a.set_year=ENTAB.set_year AND 1=1
		// AND 1=1 ) and a.set_year ="
		// + sYear + " union all " + sSql;
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * 带权限的单位数据集
	 */
	public DataSet getDivDataPop(String sYear, int batch_no) throws Exception
	{
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID().toLowerCase(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
		String sSql = " select * from vw_fb_division ENTAB " + " where is_leaf = 1 and set_year=" + sYear + " " + swhere + " union all " + " select * from vw_fb_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year=" + sYear + " and " + " exists (select 1 from vw_fb_division ENTAB where ENTAB.div_code like tab.div_code||'%'"
				+ " and ENTAB.is_leaf = 1 and ENTAB.set_year=" + sYear + " " + swhere + ")";
		// sSql = "select * from (" + sSql + ") a " +
		// getAuditDivFilter(batch_no);
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}


	/**
	 * 获取绩效项目审核信息
	 * 
	 * @param type
	 *            0:审核表 1：历史表
	 * @param divCode
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	// getTasksNode()
	public DataSet getAuditInfo(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter, String flowstatus,
			String moduleId) throws Exception
	{
		DataSet dsData = DataSet.create();
		// String strSql = "";
		String flowcondition = "";
		if (flowstatus != null && !flowstatus.equals(""))
			flowcondition = getFlowCondition("RP_TB_BILL", "BILL_ID", flowstatus, moduleId);// 返回工作流执行SQL

		String sql = "";
		if (flowcondition != null && !flowcondition.equals(""))
		{
			sql = getAuditSqlInfo(type, divCodes, prjCodes, setYear, loginUserCode, loginDepCode, batch_no, qtype, filter);
			sql = sql + flowcondition;
		}
		else
		{
			sql = getAuditSqlInfo2(type, divCodes, prjCodes, setYear, loginUserCode, loginDepCode, batch_no, qtype, filter);

		}
		DBSqlExec.getDataSet(sql, dsData);

		return dsData;
	}


	private String getAuditSqlInfo(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SET_YEAR,A.RG_CODE,A.EN_ID,A.DIV_CODE,DECODE(A.LOCKED,1,'锁定','未锁定') AS LOCKED,");
		sql.append("A.DIV_NAME,A.XMXH AS PRJ_ID,A.XMXH,A.XMBM AS PRJ_CODE,");
		sql.append("A.XMMC AS PRJ_NAME,(F.F2+F.F3+F.F6+F.F7) SMONEY,F.f2,F.F3, F.F6,F.F7 ");
		sql.append("FROM RP_XMJL A,");
		sql.append("RP_XMSB F,RP_TB_BILL  WHERE A.XMXH=RP_TB_BILL.XMXH AND A.XMXH=F.XMXH AND A.SET_YEAR = F.SET_YEAR AND F.SB_CODE='333'  " + filter + " ");
		sql.append(getAuditFilter(0, divCodes));
		sql.append(" AND A.SET_YEAR = " + setYear);
		sql.append(" AND A.RG_CODE = " + SessionUtilEx.getUserInfoContext().getCurrRegion());
		return sql.toString();
	}


	private String getAuditSqlInfo2(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SET_YEAR,A.RG_CODE,A.EN_ID,A.DIV_CODE,DECODE(A.LOCKED,1,'锁定','未锁定') AS LOCKED,");
		sql.append("A.DIV_NAME,A.XMXH AS PRJ_ID,A.XMXH,A.XMBM AS PRJ_CODE,");
		sql.append("A.XMMC AS PRJ_NAME,(F.F2+F.F3+F.F6+F.F7) SMONEY ");
		sql.append("FROM RP_XMJL A,");
		sql.append("RP_XMSB F  WHERE  A.XMXH=F.XMXH AND A.SET_YEAR = F.SET_YEAR AND F.SB_CODE='333'  " + filter + " ");
		sql.append(getAuditFilter(0, divCodes));
		sql.append(" AND A.SET_YEAR = " + setYear);
		sql.append(" AND A.RG_CODE = " + SessionUtilEx.getUserInfoContext().getCurrRegion());
		return sql.toString();
	}


	/**
	 * 获取过滤条件
	 * 
	 * @param type
	 *            0:单位编码 1：项目编码
	 * @param divCodes
	 * @return
	 */
	private String getAuditFilter(int type, String[] divCodes)
	{
		StringBuffer sql = new StringBuffer();
		if (divCodes == null) { return ""; }
		if (divCodes.length == 0)
		{
			return "";
		}
		else
		{
			if (type == 0)
			{
				sql.append(" and a.div_code in(");
			}
			else if (type == 1)
			{
				sql.append(" and a.prj_code in(");
			}
			for (int i = 0; i < divCodes.length; i++)
			{
				if (i == 0)
				{
					sql.append("'");
				}
				else
				{
					sql.append(",'");
				}
				sql.append(divCodes[i]);
				sql.append("'");
			}
			sql.append(")");
		}
		return sql.toString();
	}


	public List getStepAllInfo(String setYear)
	{
		String sql = "select * from " + IPrjAudit.PrjAuditTable.PrjAuditSetTable.TABLENAME_AUDIT_SET + " order by " + IPrjAudit.PrjAuditTable.PrjAuditSetTable.STEP_ID;
		return QueryStub.getQueryTool().findBySql(sql);
	}


	public DataSet getStepNowInfo(String prjCode) throws Exception
	{
		String sql = "select * from " + IPrjAudit.AuditNewInfo.AuditCurPrjState.TableName + " where " + IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE + "='" + prjCode + "'";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sql, ds);
		return ds;
	}


	public DataSet getStepAllData(String setYear) throws Exception
	{
		String sql = "select * from " + IPrjAudit.AuditNewInfo.AudtiStep.TableName + " order by " + IPrjAudit.AuditNewInfo.AudtiStep.WID;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sql, ds);
		return ds;
	}


	public DataSet getUserToPrj(String setYear, String userCode) throws Exception
	{
		String sql = "select * from fb_u_usertoreport where set_year = ";
		sql = sql + setYear;
		if (!Common.isNullStr(userCode))
		{
			sql = sql + " and user_code = '" + userCode + "'";
		}
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sql, ds);
		return ds;
	}


	public DataSet getUserInfo(String setYear) throws Exception
	{
		// String sql =
		// "SELECT user_id,user_code ,'['||user_code||']'||user_name as
		// user_name ,a.is_leaf,belong_type,b.EN_ID,b.div_code,b.div_name FROM
		// vw_fb_user a,vw_fb_division b where a.belong_type ='"
		// + UntPub.DIV_VIS
		// + "' and a.belong_org = b.en_id ORDER BY user_code";
		// DataSet ds = DataSet.create();
		// DBSqlExec.getDataSet(sql, ds);
		// return ds;
		return getDivWithRight();
	}


	public void doChoosePrjToUser(String userCode, String prjCodeFilter, String setYear, int batch_no, int data_type) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into fb_p_appr_usertoprj");
		sql.append("(user_code,prj_code,set_year,data_type,batch_no)");
		sql.append(" select '" + userCode + "',prj_code," + setYear + ",data_type,batch_no from fb_p_base_appr");
		sql.append(prjCodeFilter);
		sql.append(" and batch_no = " + batch_no + " and data_type=" + data_type);
		QueryStub.getQueryTool().execute(sql.toString());
	}


	public InfoPackage doCancelChoosePrjToUser(String userCode, String prjCodeFilter, String setYear, int batch_no, int data_type) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		// int r = DBSqlExec.getRecordCount("fb_p_appr",
		// prjCodeFilter.replaceAll(
		// "where", "").replaceAll("prj_code", "prjCode"));
		// if (r > 0) {
		// info.setSuccess(false);
		// info.setsMessage("存在项目已经录入数据，不允许删除");
		// return info;
		// }
		StringBuffer sql = new StringBuffer();
		sql.append("delete from fb_p_appr_usertoprj");
		sql.append(prjCodeFilter);
		sql.append(" and user_code = '" + userCode + "'");
		sql.append(" and set_year = " + setYear);
		QueryStub.getQueryTool().execute(sql.toString());
		return info;
	}


	public void saveApprSptBtnData(boolean isSplit, DataSet ds, String oldPrjCode, String setYear, int batch_no, int data_type) throws Exception
	{
		List list = new ArrayList();
		String blank = "   ";
		String prj_code = "";
		String par_id = "";
		if (!isSplit)
		{
			blank = "";
			int id = oldPrjCode.indexOf(",");
			if (id >= 0)
			{
				prj_code = " where prj_code = " + oldPrjCode.substring(oldPrjCode.indexOf("(") + 1, oldPrjCode.indexOf(","));
			}
		}
		if ((ds != null) && !ds.isEmpty())
		{
			ds.beforeFirst();
			while (ds.next())
			{
				String fields = getFieldStr().replaceAll("prj_code", "'" + ds.fieldByName("prj_code").getString() + "'");
				fields = fields.replaceAll("prj_name", "'" + blank + ds.fieldByName("prj_name").getString() + "'");
				fields = fields.replaceAll("par_id", "'" + ds.fieldByName("par_id").getString() + "'");
				fields = fields.replaceAll("row_id", "newid");
				StringBuffer sql = new StringBuffer();
				sql.append("insert into fb_p_base_appr(");
				sql.append(getFieldStr() + ") ");
				sql.append(" select " + fields);
				sql.append(" from fb_p_base_appr");
				if (!isSplit)
				{
					sql.append(prj_code);
				}
				else
				{
					sql.append(oldPrjCode);
				}
				sql.append(" and set_year = " + setYear);
				list.add(sql.toString());
				par_id = ds.fieldByName("par_id").getString();
			}
		}
		else
		{
			return;
		}
		if (!isSplit)
		{
			list.add("delete from fb_p_base_appr " + oldPrjCode + " and set_year=" + setYear + " and batch_no = " + batch_no + " and data_type=" + data_type);
			if (!Common.isNullStr(par_id))
			{
				list.add("delete from fb_p_base_appr where row_id = '" + par_id + "' and set_year=" + setYear + " and batch_no = " + batch_no + " and data_type = " + data_type);
			}
		}
		QueryStub.getQueryTool().executeBatch(list);
	}


	private String getFieldStr()
	{
		StringBuffer fields = new StringBuffer();
		fields.append("row_id, set_year, batch_no, data_type, ver_no,");
		fields.append("opt_no, data_src, data_attr, prj_type, en_id, div_kind,");
		fields.append("div_code, div_name, mb_id, dept_code, dept_name, prj_code,");
		fields.append("prj_name, bs_id, acct_code, acct_name, prjsort_code,");
		fields.append("prjattr_code, prjaccord_code, prjstd_code, stock_flag,");
		fields.append("stock_type, start_date, end_date, prj_content, prj_manager,");
		fields.append("sort_all, sort_charge, sort_div, prj_status, audit_status,");
		fields.append("isperennial, revisectl_code, report_flag, detail_degree,");
		fields.append("c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14,");
		fields.append("c15, c16, c17, c18, c19, c20, cl1, cl2, cl3, f1, f2, f3, f4,");
		fields.append("f5, n1, n2, n3, n4, n5, d1, d2, rg_code, last_ver, dep_status,");
		fields.append("fis_status, prj_transfer, prj_parent, ca1, ca2, ca3, ca4, ca5,");
		fields.append("cs1, cs2, cs3, cs4, cs5, f6, f7, f8, f9, f10, f11, f12, f13,");
		fields.append("f14, f15, f16, f17, f18, f19, f20,par_id");
		return fields.toString();
	}


	public DataSet getLockInfoPrj(boolean isLock, String divCode, String setYear) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}


	/**
	 * 审核配置界面获取元素数据集
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditElement(boolean isUser, String dep_id) throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		if (!isUser)
		{
			sql.append("select * from (");
			sql.append(" select '0' as code,'用户' as name,'' as par_id from dual");
			sql.append(" union all");
			sql.append(" select 'dep'||dep_code as code,dep_name as name,'0' as par_id from vw_fb_department");
			sql.append(" union all");
			sql.append(" select user_code,'['||user_code||']'||user_name,'dep'||(select dep_code from vw_fb_department a ");
			sql.append(" where a.Dep_ID = b.BELONG_ORG) from vw_fb_user b");
			sql.append(" where belong_type = '007' and belong_org is not null");
			sql.append(" union all");
			sql.append(" select '1' as code,'业务处室','' from dual");
			sql.append(" union all");
			// sql.append(" select '-1','业务处室','1' from dual");
			// sql.append(" union all");
			sql.append(" select dep_code,'['||dep_code||']'||dep_name,'1' from vw_fb_department");
			sql.append(" ) order by par_id,code");
		}
		else
		{
			if (Common.isNullStr(dep_id))
			{
				sql.append(" select 'dep'||dep_code as code,dep_name as name,'0' as par_id,dep_id from vw_fb_department");
				sql.append(" union all");
			}
			sql.append(" select user_code,'['||user_code||']'||user_name as name ,'dep'||(select dep_code from vw_fb_department a ");
			sql.append(" where a.Dep_ID = b.BELONG_ORG) as par_id,belong_org from vw_fb_user b");
			sql.append(" where belong_type = '007' and belong_org is not null");
			if (!Common.isNullStr(dep_id))
			{
				sql.append(" and belong_org='" + dep_id + "'");
			}
		}
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}


	public DataSet getAuditStepInfo() throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from FB_P_APPR_AUDIT_WSTEP order by wid");
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}


	public DataSet getPrjAuditStepInfo() throws Exception
	{
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from FB_P_APPR_AUDIT_PRJSTATE order by STEP");
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}


	/**
	 * 批办项目
	 */
	public InfoPackage sentPrjToUser(String userCode, String[] prjCodes, String[] toUserCodes, String[] canUserCodes, String[] toUserNames, String curTime, String setYear, String wid, String batch_no)
			throws Exception
	{
		StringBuffer cU = new StringBuffer();
		for (int i = 0; (canUserCodes != null) && (i < canUserCodes.length); i++)
		{
			if (i != 0)
			{
				cU.append(",");
			}
			cU.append("'");
			cU.append(canUserCodes[i]);
			cU.append("'");
		}
		if (canUserCodes.length == 0)
		{
			cU.append("''");
		}
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		List list = new ArrayList();
		for (int i = 0; i < prjCodes.length; i++)
		{
			StringBuffer sqlDelPBR = new StringBuffer();
			sqlDelPBR.append("delete from " + IPrjAudit.AuditNewInfo.AuditPrjNState.TableName);
			sqlDelPBR.append(" where ");
			sqlDelPBR.append(IPrjAudit.AuditNewInfo.AuditPrjNState.PRJ_CODE);
			sqlDelPBR.append("='" + prjCodes[i] + "' and ");
			sqlDelPBR.append(IPrjAudit.AuditNewInfo.WID);
			sqlDelPBR.append("=" + wid + " and ");
			sqlDelPBR.append(IPrjAudit.AuditNewInfo.AuditPrjNState.PBR);
			sqlDelPBR.append("='" + userCode + "' and ");
			sqlDelPBR.append(IPrjAudit.AuditNewInfo.SET_YEAR);
			sqlDelPBR.append("=" + setYear);
			sqlDelPBR.append(" and nid in (" + cU.toString() + ")");
			QueryStub.getQueryTool().execute(sqlDelPBR.toString());
			for (int j = 0; j < toUserCodes.length; j++)
			{
				if (Common.isEqual(userCode, toUserCodes[j]))
				{
					// 不允许自己给自己指派
					continue;
				}
				// 如果项目被你指派给同一个人了，则允许再指派给相同的人,只是批办人里要加入新批办的人
				StringBuffer filter = new StringBuffer();
				filter.append(IPrjAudit.AuditNewInfo.AuditPrjNState.PRJ_CODE);
				filter.append("='" + prjCodes[i] + "'");
				filter.append(" and " + IPrjAudit.AuditNewInfo.AuditPrjNState.NID);
				filter.append("='" + toUserCodes[j] + "'");
				int r = DBSqlExec.getRecordCount(IPrjAudit.AuditNewInfo.AuditPrjNState.TableName, filter.toString());
				StringBuffer sql = new StringBuffer();
				if (r >= 1)
				{
					sql.append("update " + IPrjAudit.AuditNewInfo.AuditPrjNState.TableName);
					sql.append(" set " + IPrjAudit.AuditNewInfo.AuditPrjNState.ISEND);
					sql.append("=0,");
					sql.append(IPrjAudit.AuditNewInfo.AuditPrjNState.PBR);
					sql.append("='" + userCode + "'");
					sql.append(" where " + filter);
				}
				else
				{
					sql.append("insert into " + IPrjAudit.AuditNewInfo.AuditPrjNState.TableName);
					sql.append("(prj_code, step, wid, nid, nname, auditcontext,");
					sql.append("isend, set_year, isback, pbr, last_ver,div_code) ");
					sql.append(" select '" + prjCodes[i] + "',");
					sql.append("(select isnull(max(step),-1)+1 ");
					sql.append("from FB_P_APPR_AUDIT_NSTATE");
					sql.append(" where " + IPrjAudit.AuditNewInfo.AuditPrjNState.PRJ_CODE + "='" + prjCodes[i] + "' and " + IPrjAudit.AuditNewInfo.AuditPrjNState.PBR + "='" + userCode + "'),");
					sql.append(wid + ",");
					sql.append("'" + toUserCodes[j] + "',");
					sql.append("'" + toUserNames[j] + "','',0," + setYear + ",0,'" + userCode + "','" + curTime + "', ");
					sql.append("(select distinct(div_code) From fb_p_base_appr");
					sql.append(" where prj_code = '" + prjCodes[i] + "' and batch_no = ");
					sql.append(batch_no);
					sql.append(" and data_type=1) as div_code");
					sql.append(" from dual");
				}
				StringBuffer sqlC = new StringBuffer();
				sqlC.append("update " + IPrjAudit.AuditNewInfo.AuditCurPrjState.TableName);
				sqlC.append(" set " + IPrjAudit.AuditNewInfo.AuditCurPrjState.WID);
				sqlC.append(" =" + (("0".equals(wid)) ? "1" : wid) + ",");
				sqlC.append(IPrjAudit.AuditNewInfo.AuditCurPrjState.NID);
				sqlC.append(" ='" + toUserCodes[j] + "',");
				sqlC.append(IPrjAudit.AuditNewInfo.AuditCurPrjState.WISEND);
				sqlC.append(" =0");
				sqlC.append(" where " + IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE);
				sqlC.append(" ='" + prjCodes[i] + "'");
				list.add(sqlC.toString());
				list.add(sql.toString());
				StringBuffer sqlu = new StringBuffer();
				sqlu.append("update " + IPrjAudit.AuditNewInfo.AuditCurPrjState.TableName);
				sqlu.append(" set nid='" + toUserCodes[j] + "'");
				sqlu.append(" where " + IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE);
				sqlu.append(" = '" + prjCodes[i] + "'");
				list.add(sqlu.toString());
			}
		}
		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	public DataSet getMainPrjDataNotLock(String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception
	{
		String sql1 = getPrjApprInfoSql(IPrjAudit.PrjTable.TABLENAME_APPR, aDivCode, "", setYear, filter1, filter2, batch_no, dataType);
		// 添加过滤条件
		StringBuffer sqlLast = new StringBuffer();
		sqlLast.append("select * from (" + sql1 + ") nt");
		sqlLast.append(" where exists(");
		sqlLast.append(" select vb.div_code from vw_fb_user vu,vw_fb_division vb,fb_p_appr_usertoprj vup");
		sqlLast.append(" where vu.USER_CODE = vup.user_code and vu.BELONG_ORG = vb.EN_ID");
		sqlLast.append(" and nt.prj_code = vup.prj_code and nt.set_year = vup.set_year)");
		String sql = "select * from (" + sqlLast.toString() + ") abcd where not exists (select 1 from fb_p_appr_prjlock bcde where abcd.prj_code = bcde.prj_code)";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public DataSet getMainPrjDataLock(String aDivCode, String setYear, String filter1, String filter2, String batch_no, String dataType) throws Exception
	{
		String sql1 = getPrjApprInfoSql(IPrjAudit.PrjTable.TABLENAME_APPR, aDivCode, "", setYear, filter1, filter2, batch_no, dataType);
		// 添加过滤条件
		StringBuffer sqlLast = new StringBuffer();
		sqlLast.append("select * from (" + sql1 + ") nt");
		sqlLast.append(" where exists(");
		sqlLast.append(" select vb.div_code from vw_fb_user vu,vw_fb_division vb,fb_p_appr_usertoprj vup");
		sqlLast.append(" where vu.USER_CODE = vup.user_code and vu.BELONG_ORG = vb.EN_ID");
		sqlLast.append(" and nt.prj_code = vup.prj_code and nt.set_year = vup.set_year)");
		String sql = "select * from (" + sqlLast.toString() + ") abcd where exists (select 1 from fb_p_appr_prjlock bcde where abcd.prj_code = bcde.prj_code)";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		ds.beforeFirst();
		ds.next();
		return ds;
	}


	public DataSet getDivWithRight() throws Exception
	{
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(getUserTreeAndDivSql(), dsData);
		if ((dsData == null) || dsData.isEmpty()) { return null; }
		return dsData;
	}


	private String getUserTreeAndDivSql() throws Exception
	{
		// 获取预算处编码
		StringBuffer sql = new StringBuffer();
		if (Global.loginmode == 0)
		{// 在线
			String ysccode = DBSqlExec.getStringValue("select cvalue from fb_s_pubcode where upper(typeid)='YSCID'");
			IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
			sql.append("select DISTINCT chr_id as user_id,chr_code as user_code,chr_code as code,");
			sql.append("(case when parent_id is not null then '[' || chr_code || ']'  end) || chr_name as name,parent_id as par_code from vw_fb_usertree  ST");
			sql.append("  start with ST.PARENT_ID is not null and ST.CHR_CODE<>'999999999' ");
			sql.append(Common.isEqual(SessionUtil.getUserInfoContext().getOrgCode(), ysccode) ? "or  ST.chr_code = '007' " : "");
			sql.append(" AND EXISTS ( SELECT 1 FROM (");
			sql.append(" select *  from ele_enterprise ENTAB where 1=1");
			sql.append(swhere);
			sql.append(" ) TMP WHERE TMP.CHR_ID=ST.filter_id)");
			sql.append(" connect by prior st.PARENT_ID = st.CHR_ID");
			sql.append(" order by chr_code");
		}
		else
		{// 离线
			sql.append("select b.user_id,b.user_code,");
			sql.append(" b.user_code as code,");
			sql.append(" '['||b.user_code||']'||b.user_name as name,");
			sql.append(" '' as par_code");
			sql.append(" from sys_user_org a, sys_usermanage b");
			sql.append(" where org_id in (select org_id from sys_user_org ");
			sql.append(" where user_id = '" + SessionUtil.getUserInfoContext().getUserID() + "')");
			sql.append(" and b.belong_type = 002");
			sql.append(" and a.user_id = b.user_id");
			sql.append(" and exists(");
			sql.append(" select 1 from sys_usermanage c where ");
			sql.append(" c.user_id = '" + SessionUtil.getUserInfoContext().getUserID() + "'");
			sql.append(" and substr(b.user_code, 0, 3) = substr(user_code, 0, 3)");
			sql.append(")");
		}
		return sql.toString();
	}


	public void doPostImportData(DataSet dsAppr, DataSet dsApprDetail, String prjCode, int batchNo, int dataType) throws Exception
	{
		dsAppr.setName("fb_p_appr");
		dsApprDetail.setName("fb_p_appr_detail");
		List list = new ArrayList();
		list.add("delete from fb_p_appr where prjcode = '" + prjCode + "' and batch_no = " + batchNo + " and data_type = " + dataType);
		list.add("delete from fb_p_appr_detail where parcode = '" + prjCode + "' and batch_no = " + batchNo + " and data_type = " + dataType);
		dsAppr.beforeFirst();
		while (dsAppr.next())
		{
			list.add(SQLTool.insertSQL(dsAppr.getName(), dsAppr.fields(), QueryStub.getQueryTool().getColumnInfo(dsAppr.getName())));
		};
		dsApprDetail.beforeFirst();
		while (dsApprDetail.next())
		{
			list.add(SQLTool.insertSQL(dsApprDetail.getName(), dsApprDetail.fields(), QueryStub.getQueryTool().getColumnInfo(dsApprDetail.getName())));
		};
		QueryStub.getQueryTool().executeBatch(list);
	}


	public InfoPackage isRightUserWhenSend(String[] userID, String sYear) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String[] roleID = new String[userID.length];
		String userFilter = "";
		for (int i = 0; i < userID.length; i++)
		{
			if (i == 0)
			{
				userFilter = userID[i];
			}
			else
			{
				userFilter = userFilter + "," + userID[i];
			}
		}
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet("select * from sys_user_role_rule", ds);

		String sSql = "";
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
		for (int i = 0; i < userID.length; i++)
		{
			String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
			swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
			sSql = " select distinct dep_id as EN_ID,parent_ID,dep_code as Div_Code,dep_name as Div_Name,dep_name as code_name from vw_fb_department a "
					+ " where  exists (select 1  from fb_u_dePtodiv ENTAB where ENTAB.dep_code like a.Dep_code||'%' and a.set_year=ENTAB.set_year " + swhere + " )" + " and a.set_year =" + sYear
					+ " union all " + " select distinct a.EN_ID,case when a.Parent_id is null then dep_ID else a.parent_Id end as parent_Id,a.Div_code,a.div_name,code_name"
					+ " from vw_fb_division a,fb_u_DepToDiv ENTAB " + " where a.set_year = ENTAB.set_year and a.div_code like ENTAB.div_code||'%' and a.set_year=" + sYear + " " + swhere;
		}
		return info;
	}


	public InfoPackage setPrjSort(String sortCode, String sortName, String sortFName, String filter) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		StringBuffer sql = new StringBuffer();
		sql.append(" update ");
		sql.append(IPrjAudit.PrjTable.TABLENAME_APPR);
		sql.append(" set " + SORT_CODE + "='" + sortCode + "',");
		sql.append(SORT_NAME + "='" + sortName + "',");
		sql.append(SORT_FNAME + "='" + sortFName + "'");
		sql.append(filter);
		QueryStub.getQueryTool().executeUpdate(sql.toString());

		List list = new ArrayList();
		list.add(sql.toString());

		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	public InfoPackage setPrjDivInfo(String divCode, String filter) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		StringBuffer sql = new StringBuffer();
		sql.append("update " + IPrjAudit.PrjTable.TABLENAME_APPR);
		sql.append(" set div_code ='" + divCode + "'");
		sql.append(",div_name=(select div_name from vw_fb_division where div_code='" + divCode + "')");
		sql.append(filter);
		System.out.println(sql.toString());
		if (!Common.isNullStr(sql.toString()))
		{
			QueryStub.getQueryTool().execute(sql.toString());
		}
		return info;
	}


	public DataSet getSortData(String setYear) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select * From " + IPrjAudit.PrjTable.TABLENAME_SORT + " where set_year = " + setYear;
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	public List getAuditSqlInfoForSend(int isback, int isend, boolean backtoback, String filter, String[] divCodes, String loginUserCode, String setYear) throws Exception
	{

		StringBuffer sql = new StringBuffer();
		sql.append("select * from vw_rp_xm_audit e ");
		sql.append("where 1=1 " + filter + " ");
		sql.append("and is_back = " + isback + " and  audit_isend = " + isend + " ");;
		sql.append("and set_year =" + setYear + " ");
		sql.append(getAuditFilter(0, divCodes));
		if (!backtoback)
		{
			sql.append("and audit_state in (select t.step_id  from rp_s_audit_step t where t.audit_oper like '%" + loginUserCode + "%') ");
		}
		else
		{
			sql.append("and audit_state in (select t.step_id-1  from rp_s_audit_step t where t.audit_oper like '%" + loginUserCode + "%') ");
		}

		return dao.findBySql(sql.toString());
	}


	public DataSet getAuditInfoforInput(String[] prjCode) throws Exception
	{
		// TODO Auto-generated method stub
		DataSet dsData = DataSet.create();
		// if (type == 0)

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  A.EN_ID,A.XMXH,A.XMBM,A.XMMC,A.DIV_CODE,DIV_NAME,A.C1,A.C2,");
		sql.append("(SELECT CHR_NAME FROM DM_ZXZQ WHERE CHR_CODE =  A.C3 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C3,");
		sql.append("(SELECT CHR_NAME FROM DM_XMFL WHERE CHR_CODE =  A.C4 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C4,");
		sql.append(" C5,");
		sql.append("(SELECT CHR_NAME FROM DM_XMZT WHERE CHR_CODE =  A.C6 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C6,");
		sql.append("ACCTS,GKDW_NAME,GKDW_ID,GKDW_CODE,APPRCOMMON,NODE_NAME,VER_DATE,VER_USER,P_NO ");
		sql.append(" FROM RP_XMJL_HISTORY A  WHERE   ");
		sql.append(" SET_YEAR=");
		sql.append(SessionUtil.getLoginYear());
		sql.append(" AND  XMXH IN(");
		for (int i = 0; i < prjCode.length; i++)
		{
			sql.append("'" + prjCode[i] + "',");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(") ORDER BY XMBM,VER_DATE ");

		DBSqlExec.getDataSet(sql.toString(), dsData);
		return dsData;
	}


	public int[] getxmshZT(String userCode, String SetYear) throws Exception
	{
		int[] zts = new int[3];

		String en_code = getxmshztsQXDiv(SetYear);

		zts[0] = DBSqlExec.getIntValue("select count(prj_code) from rp_audit_cur b where  b.is_back = 0 and b.div_code in (" + en_code
				+ ") and b.audit_state  in (select step_id from rp_s_audit_step where audit_oper like '%" + userCode + "%')");
		zts[1] = DBSqlExec.getIntValue("select count(prj_code) from rp_audit_cur b where  b.is_back = 0 and b.div_code in (" + en_code
				+ ") and b.audit_state  in (select step_id+1 from rp_s_audit_step where audit_oper like '%" + userCode + "%')");
		zts[2] = DBSqlExec.getIntValue("select count(prj_code) from  rp_audit_cur b where  b.is_back = 1  and b.div_code in (" + en_code
				+ ") and b.audit_state  in (select step_id from rp_s_audit_step where audit_oper like '%" + userCode + "%')");
		// TODO Auto-generated method stub
		return zts;

	}


	/**
	 * 对应权限 获得单位数
	 */

	public String getxmshztsQXDiv(String SetYear) throws Exception
	{
		IControlDictionary iCDictionary = (IControlDictionary) SessionUtil.getServerBean("sys.controlDictionaryService");
		String swhere = iCDictionary.getSqlElemRight(SessionUtil.getUserInfoContext().getUserID().toLowerCase(), SessionUtil.getUserInfoContext().getRoleID(), "EN", "ENTAB").toUpperCase();
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_ID", "ENTAB.EN_ID");
		swhere = StringUtils.replace(swhere, "ENTAB.CHR_CODE", "ENTAB.Div_Code");
		String sSql = " select div_code from vw_fb_division ENTAB " + " where is_leaf = 1 and set_year=" + SetYear + " " + swhere + " union all " + " select div_code from vw_fb_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year=" + SetYear + " and " + " exists (select 1 from vw_fb_division ENTAB where ENTAB.div_code like tab.div_code||'%'"
				+ " and ENTAB.is_leaf = 1 and ENTAB.set_year=" + SetYear + " " + swhere + ")";
		return sSql.toString();

	}


	public DataSet getFinshAuditInfo(int type, String[] divCodes, String[] prjCodes, String setYear, String loginUserCode, String loginDepCode, int batch_no, int qtype, String filter)
			throws Exception
	{
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		String tableName = IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR;

		sql.append("select e.* ,(f.f2+f.f3+f.f6+f.f7) smoney,f.f2,f.f3,f.f6,f.f7 from (");
		sql.append("select a.set_year,a.rg_code,a.en_id,a.div_code,a.batch_no,");
		sql.append("a.div_name,a.xmxh as prj_id,a.xmbm as prj_code,");
		sql.append("a.xmmc as prj_name,d.audit_userid,d.audit_username,");
		sql.append("isnull(d.audit_state,0) as step_id,isnull(d.audit_isend,0) as audit_isend,");

		sql.append("d.audit_text,d.audit_time,");
		sql.append("d.is_back");
		sql.append(" from (select a.*,b.batch_no from rp_xmjl  a left join rp_prjbatch b on a.xmbm=b.prj_code) a");
		sql.append(" left join ");
		sql.append(tableName + " d ");
		// sql.append("select b.en_id,b.div_code,b.div_name,b.prj_code,");
		// sql.append("b.audit_username,b.audit_state,c.step_name,");
		// sql.append("b.prj_name,b.audit_userid,b.set_year,B.AUDIT_ISEND,");
		// sql.append("c.rg_code,b.audit_text,b.audit_time,b.is_back");
		// sql.append(" From "+tableName+" b,rp_s_audit_step c");
		// sql.append(" where c.step_id = b.audit_state");
		// sql.append(" and c.set_year = b.set_year");
		// sql.append(" and c.rg_code = b.rg_code ");
		// sql.append(") d");
		sql.append(" on  a.xmbm = d.prj_code");
		sql.append(" and a.set_year = d.set_year");
		sql.append(" and a.rg_code = d.rg_code");
		sql.append(" and a.div_code = d.div_code");
		sql.append(") e , rp_xmsb f WHERE 1 = 1 and e.prj_id=f.xmxh and f.sb_code='333'  " + filter + " ");
		sql.append(getAuditFilter(0, divCodes));
		if (type == 0)
			sql.append("and e.step_id  = (select max(step_id)+1  from rp_s_audit_step ) and (isnull(audit_isend,0)=1 )");
		else
			sql.append("and (isnull(audit_isend, 0) = 0 and is_back=0 and step_id = 0 and batch_no >0)");

		sql.append("order by audit_time ");

		DBSqlExec.getDataSet(sql.toString(), dsData);

		return dsData;
	}


	public InfoPackage finshAudit(List prjCodes, String aUserCode, String rgCode, String setYear, String moduleid, int type) throws Exception
	{

		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		info.setsMessage("您有该项目的审核项目");

		reStartWorkFlow(prjCodes, rgCode, moduleid, type);

		// return info;
		// QueryStub.getQueryTool().executeBatch(list);
		info.setsMessage("下达成功  " + prjCodes.size() + "   条项目");
		return info;
	}


	public String reStartWorkFlow(List xmxhs, String rgCode, String moduleId, int type) throws Exception
	{
		String flowstate = "";
		if (type == 0)
			flowstate = "TONEXT";
		else if (type == 1)
			flowstate = "TORECALLNEXT";
		else if (type == 2)
			flowstate = "TOPREVIOUS";
		else if (type == 3)
			flowstate = "TORECALLPREVIOUS";

		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
				String enId = Common.nonNullStr(((Map) xmxhs.get(i)).get("enid"));
				billId = getbill(xmxh, "RP_TB_BILL");
				goFlow(null, "RP_TB_BILL", "BILL_ID", billId, flowstate, moduleId);
				// 重新流程到项目填报
				intoBill(enId, billId, xmxh, "RP_TB_BILL");
				inputFlow("RP_TB_BILL", "XMXH", billId, moduleId);
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		return msg;
	}


	public String calcelWorkFlow(List xmxhs, String rgCode, String moduleId, int type) throws Exception
	{
		String flowstate = "";
		if (type == 0)
			flowstate = "TONEXT";
		else if (type == 1)
			flowstate = "TORECALLNEXT";
		else if (type == 2)
			flowstate = "TOPREVIOUS";
		else if (type == 3)
			flowstate = "TORECALLPREVIOUS";

		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
				billId = getbill(xmxh, "RP_TB_BILL");
				goFlow(null, "RP_TB_BILL", "BILL_ID", billId, flowstate, moduleId);
				// dao.executeBySql("delete from RP_TB_BILL where xmxh ='" +
				// xmxh + "'");
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		return msg;
	}


	public String workflow(List xmxhs, String rgCode, String moduleId, int type) throws Exception
	{
		// TODO Auto-generated method stub
		String flowstate = "";
		if (type == 0)
			flowstate = "TONEXT";
		else if (type == 1)
			flowstate = "TORECALLNEXT";
		else if (type == 2)
			flowstate = "TOPREVIOUS";
		else if (type == 3)
			flowstate = "TORECALLPREVIOUS";

		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = xmxhs.get(i).toString();
				billId = getbill(xmxh, "RP_TB_BILL");
				goFlow(null, "RP_TB_BILL", "BILL_ID", billId, flowstate, moduleId);
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		return msg;
	}


	public int intoBill(String enID, String bill_id, String xmxh, String tablename)
	{

		dao.executeBySql("delete from " + tablename + " where xmxh ='" + xmxh + "'");
		String sql = "insert into " + tablename + "(en_id,bill_id,xmxh,set_year)values('" + enID + "','" + bill_id + "','" + xmxh + "','" + SessionUtil.getLoginYear() + "')";
		return dao.executeBySql(sql);
	}


	public String getbill(String xmxh, String tablename)
	{
		String bill = "select bill_id from " + tablename + " where xmxh='" + xmxh + "'";

		List list = dao.findBySql(bill);

		return ((Map) (list.get(0))).get("bill_id").toString();
	}


	public InfoPackage backFinshAudit(List prjCodes, String aUserCode, String rgCode, String moduleId, int type) throws Exception
	{

		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		calcelWorkFlow(prjCodes, rgCode, moduleId, type);

		info.setsMessage("撤消下达  " + prjCodes.size() + "   条项目");
		return info;
	}


	public String saveAffixFile(String tableName, String en_code, String filename, String setYear, byte[] buf, String suptype)
	{
		// 创建原文件输出流

		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");

		String sql = "SELECT C_VALUE FROM RP_S_CONFIG  WHERE SUPTYPE='" + suptype + "' ";
		String path = "";
		try
		{
			path = DBSqlExec.getStringValue(sql);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		// String Memu =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\";
		// String file =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\"+filename;
		String Memu = path + en_code + "\\" + matter1.format(dt).toString() + "\\";
		String file = path + en_code + "\\" + matter1.format(dt).toString() + "\\" + filename;

		File Memufile = new File(Memu);
		Memufile.mkdirs();

		// Memufile.delete();
		File outFile = new File(file);

		try
		{
			if (outFile.exists())
			{
				outFile.delete();
			}

			fileOut = new FileOutputStream(outFile);

			bufOut = new BufferedOutputStream(fileOut);
			boolean isSuccessful = true;

			bufOut.write(buf);
			bufOut.flush();
			/* 释放流资源 */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists())
			{
				outFile.delete();
			}
			if (outFile.length() == 0)
			{
				isSuccessful = false;
				outFile.delete();
			}
			return file;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			/* 关闭流 */
			try
			{
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				Memufile.canRead();
				Memufile.canWrite();
				fileOut = null;
				bufOut = null;
			}
			catch (Exception eek)
			{
				eek.printStackTrace();
			}
		}
		return file;
	}


	public String saveAffixFiles(String tableName, String en_code, String filename, String setYear, byte[] buf, String suptype)
	{
		// 创建原文件输出流

		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		// Date dt = new Date();
		// SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");

		String sql = "SELECT C_VALUE FROM RP_S_CONFIG  WHERE SUPTYPE='" + suptype + "' ";
		String path = "";
		try
		{
			path = DBSqlExec.getStringValue(sql);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		// String Memu =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\";
		// String file =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\"+filename;
		String Memu = path + en_code;
		String file = path + en_code + "\\" + filename;

		File Memufile = new File(Memu);
		Memufile.mkdirs();

		// Memufile.delete();
		File outFile = new File(file);

		try
		{
			if (outFile.exists())
			{
				outFile.delete();
			}

			fileOut = new FileOutputStream(outFile);

			bufOut = new BufferedOutputStream(fileOut);
			boolean isSuccessful = true;

			bufOut.write(buf);
			bufOut.flush();
			/* 释放流资源 */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists())
			{
				outFile.delete();
			}
			if (outFile.length() == 0)
			{
				isSuccessful = false;
				outFile.delete();
			}
			return file;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			/* 关闭流 */
			try
			{
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				Memufile.canRead();
				Memufile.canWrite();
				fileOut = null;
				bufOut = null;
			}
			catch (Exception eek)
			{
				eek.printStackTrace();
			}
		}
		return file;
	}


	public byte[] getFileFinds(String enId,String fileName, String setYear, String suptype, String rgCode) throws Exception
	{
		String path = DBSqlExec.getStringValue("SELECT C_VALUE FROM RP_S_CONFIG  where suptype='" + suptype + "' ");
		StringBuffer sql = new StringBuffer("select b.name from rp_fj_files b");
		sql.append(" where b.set_year = '").append(setYear).append("' and b.en_id = '").append(enId);
		sql.append("' and b.name = '").append(fileName).append("' and b.rg_code = '").append(rgCode).append("'");

		String filePath = DBSqlExec.getStringValue(sql.toString());

		File file = new File(path + enId + "\\" + filePath);
		InputStream is = new FileInputStream(file);

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		int bit;
		while ((bit = is.read()) != -1)
		{
			data.write(bit);
		}
		is.close();
		data.close();
		file.canRead();
		file.canWrite();

		return data.toByteArray();
	}


	public String saveFileAsFj(String tableName, String en_code, String filename, String setYear, byte[] buf, String suptype)
	{
		// 创建原文件输出流

		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");

		String sql = "select c_value from RP_S_CONFIG  where suptype='" + suptype + "' ";
		String path = "";
		try
		{
			path = DBSqlExec.getStringValue(sql);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		// String Memu =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\";
		// String file =
		// "D:\\滚动项目附件\\"+en_code+"\\"+matter1.format(dt).toString()+"\\"+filename;
		String Memu = path + en_code + "\\" + matter1.format(dt).toString() + "\\";
		String file = path + en_code + "\\" + matter1.format(dt).toString() + "\\" + filename;

		File Memufile = new File(Memu);
		Memufile.mkdirs();

		// Memufile.delete();
		File outFile = new File(file);

		try
		{
			if (outFile.exists())
			{
				outFile.delete();
			}

			fileOut = new FileOutputStream(outFile);

			bufOut = new BufferedOutputStream(fileOut);
			boolean isSuccessful = true;

			bufOut.write(buf);
			bufOut.flush();
			/* 释放流资源 */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists())
			{
				outFile.delete();
			}
			if (outFile.length() == 0)
			{
				isSuccessful = false;
				outFile.delete();
			}
			return file;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			/* 关闭流 */
			try
			{
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				Memufile.canRead();
				Memufile.canWrite();
				fileOut = null;
				bufOut = null;
			}
			catch (Exception eek)
			{
				eek.printStackTrace();
			}
		}
		return file;
	}


	public byte[] getFileFind(String tableName, String rowid, String setYear) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT FILE_PLACE");

		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" where ");
		sql.append(IPrjAudit.MainTable.Affix.ROW_ID);
		sql.append(" = '");
		sql.append(rowid);
		sql.append("' and set_year=");
		sql.append(setYear);
		File file = new File(DBSqlExec.getStringValue(sql.toString()));
		InputStream is = new FileInputStream(file);

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		int bit;
		while ((bit = is.read()) != -1)
		{
			data.write(bit);
		}
		is.close();
		data.close();
		file.canRead();
		file.canWrite();

		return data.toByteArray();
	}


	public int[] executeBatch(List sql) throws Exception
	{
		return QueryStub.getQueryTool().executeBatch(sql);
	}


	public boolean execute(String sql) throws Exception
	{
		return QueryStub.getQueryTool().execute(sql);
	}


	public DataSet getAuditHis(String xmxh, String setYear, String rgCode, String aduitRol, String p_no) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*");
		sql.append(" From rp_xmsb_history a where xmxh='");
		sql.append(xmxh);
		sql.append("' and set_year = ");
		sql.append(setYear);
		sql.append(" and rg_code = ");
		sql.append(rgCode);
		sql.append(" and p_no = ");
		sql.append(p_no);
		sql.append(" and node_name='" + aduitRol + "'");
		sql.append(" order by sb_code,sb_type");
		DataSet dsBody = DBSqlExec.getDataSet(sql.toString());
		if (dsBody != null && !dsBody.isEmpty())
			return dsBody;
		else
		{
			dsBody.append();
			dsBody.fieldByName("xmxh").setValue(xmxh);
			dsBody.fieldByName("rg_code").setValue(rgCode);
			dsBody.fieldByName("setYear").setValue(setYear);
			dsBody.fieldByName("YSJC_MC").setValue("");
			dsBody.fieldByName("SB_CODE").setValue("111");
			dsBody.fieldByName("ACCT_NAME").setValue("");
			dsBody.fieldByName("ACCT_NAME_JJ").setValue("");
			dsBody.fieldByName("SB_TYPE").setValue("总预算");
			dsBody.append();
			dsBody.fieldByName("xmxh").setValue(xmxh);
			dsBody.fieldByName("rg_code").setValue(rgCode);
			dsBody.fieldByName("setYear").setValue(setYear);
			dsBody.fieldByName("SB_CODE").setValue("222");
			dsBody.fieldByName("YSJC_MC").setValue("");
			dsBody.fieldByName("ACCT_NAME").setValue("");
			dsBody.fieldByName("ACCT_NAME_JJ").setValue("");
			dsBody.fieldByName("SB_TYPE").setValue("已安排数");
			dsBody.append();
			dsBody.fieldByName("xmxh").setValue(xmxh);
			dsBody.fieldByName("rg_code").setValue(rgCode);
			dsBody.fieldByName("setYear").setValue(setYear);
			dsBody.fieldByName("SB_CODE").setValue("333");
			dsBody.fieldByName("YSJC_MC").setValue("");
			dsBody.fieldByName("ACCT_NAME").setValue("");
			dsBody.fieldByName("ACCT_NAME_JJ").setValue("");
			dsBody.fieldByName("SB_TYPE").setValue("本年预算");
			StringBuffer sqlkm = new StringBuffer();
			sqlkm.append("select b.chr_id,b.chr_code,b.chr_name from");
			sqlkm.append(" rp_xmjl_km a , ele_budget_subject b");
			sqlkm.append(" where a.kmdm = b.chr_id");
			sqlkm.append(" and a.set_year = b.set_year");
			sqlkm.append(" and a.xmxh = '");
			sqlkm.append(xmxh);
			sqlkm.append("' order by b.chr_code");
			DataSet dskm = DBSqlExec.getDataSet(sqlkm.toString());
			if (dskm != null && !dskm.isEmpty())
			{
				dskm.beforeFirst();
				int i = 1;
				while (dskm.next())
				{
					dsBody.append();
					dsBody.fieldByName("xmxh").setValue(xmxh);
					dsBody.fieldByName("rg_code").setValue(rgCode);
					dsBody.fieldByName("ACCT_NAME").setValue("[" + dskm.fieldByName("CHR_CODE").getString() + "]" + dskm.fieldByName("chr_name").getString());
					dsBody.fieldByName("BS_ID").setValue(dskm.fieldByName("chr_id").getString());
					dsBody.fieldByName("ACCT_CODE").setValue(dskm.fieldByName("CHR_CODE").getString());
					dsBody.fieldByName("setYear").setValue(setYear);
					dsBody.fieldByName("YSJC_MC").setValue("");
					dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
					dsBody.fieldByName("SB_CODE").setValue(String.valueOf(400000 + i));
				}
				dsBody.applyUpdate();
			}
		}
		return dsBody;
	}
}
