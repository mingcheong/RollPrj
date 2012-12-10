package gov.nbcs.rp.input.bs;

import gov.nbcs.rp.bs.PrjectManageBO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SessionUtilEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.input.ibs.IPrjInput;
import gov.nbcs.rp.statc.ConstStr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.sessionmanager.SessionUtil;




public class PrjInputBO extends PrjectManageBO implements IPrjInput
{

	// public GeneralDAO dao;
	//
	// public GeneralDAO getDao() {
	// return dao;
	// }
	//
	// public void setDao(GeneralDAO dao) {
	// this.dao = dao;
	// }

	/*
	 * 测试方法 (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.input.ibs.IPrjInput#getTestData()
	 */
	// public List getTestData() throws Exception {
	// List list = new ArrayList();
	// StringBuffer sql = new StringBuffer();
	// sql.append("select * from ele_enterprise");
	// list = dao.findBySql(sql.toString());
	// return list;
	// }
	// /**
	// *
	// */
	// public DataSet getEnterprise() throws Exception {
	// DataSet ds = DataSet.create();
	// ds.setSqlClause("select * from ele_enterprise order by chr_code");
	// ds.open();
	// return ds;
	// }
	/**
	 * 获取执行周期
	 */
	public List getDmZxzq(String setYear, String rgCode) throws Exception
	{
		List zxzqList = dao.findBySql("select * from dm_zxzq where set_year = " + setYear + " and rg_code = " + rgCode + " order by chr_code");
		return zxzqList;
	}


	/**
	 * 获取项目分类
	 */
	public List getDmXmfl(String setYear, String rgCode) throws Exception
	{
		return dao.findBySql("select * from dm_xmfl where set_year = " + setYear + " and rg_code = " + rgCode + " order by chr_code");
	}


	/**
	 * 获取项目状态
	 */
	public List getDmXmzt(String setYear, String rgCode) throws Exception
	{
		return dao.findBySql("select * from dm_xmzt where set_year = " + setYear + " and rg_code = " + rgCode + " order by chr_code ");
	}


	/**
	 * 获取项目属性
	 */
	public List getDmXmsx(String setYear, String rgCode) throws Exception
	{
		return dao.findBySql("select * from dm_xmsx where set_year = " + setYear + " and rg_code = " + rgCode + " order by chr_code ");
	}


	/**
	 * 获取科目选择
	 */
	public List getDmKmxz(String setYear, String rgCode) throws Exception
	{
		return dao.findBySql("select * from ele_budget_subject where set_year = " + setYear + " and rg_code = " + rgCode + " order by chr_code ");
	}


	/**
	 * 获取科目选择
	 */
	public DataSet getjjKm(String setYear, String rgCode) throws Exception
	{

		DataSet ds = DataSet.create();

		ds.setSqlClause("select * from ele_budget_subject_item where is_leaf  = 0 and set_year = " + setYear + " and rg_code = " + rgCode);
		ds.open();
		return ds;
	}


	/**
	 * 获取科目选择
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDmKmxzByExitKm(String inKm) throws Exception
	{
		return dao.findBySql("select * from ele_budget_subject where chr_id in (" + inKm + ") order by chr_code ");
	}


	/**
	 * 判断项目名称是否已经保存
	 * 
	 * @param enDm预算单位代码
	 * @param xmmc项目名称
	 * @return
	 * @throws Exception
	 */
	public List isExistsXmmc(String enDm, String xmmc, String setYear, String rgCode) throws Exception
	{
		String sql = "select * from rp_xmjl where xmmc = '" + xmmc + "' and en_id = '" + enDm + "' and set_year = " + setYear + " and rg_code = " + rgCode;
		return dao.findBySql(sql);
	}


	/**
	 * 根据预算单位ID获取CODE
	 */
	public List getEnCode(String enID) throws Exception
	{
		return dao.findBySql("select * from ele_enterprise where chr_id= '" + enID + "'");
	}


	/**
	 * 获取项目编码
	 * 
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public String getXmbm(String enCode, String setYear) throws Exception
	{
		String str = enCode;
		str += DBSqlExec.getIntValue("select isnull(max(substr(xmbm,-5,5)),10001)+1  from rp_xmjl_divsb where div_code = " + enCode + " and  set_year =" + setYear + "");
		return str;

		// Connection conn = null;
		// CallableStatement call = null;
		// Session session = null;
		// try {
		// session = dao.getSession();
		// if (session == null) {
		// throw new Exception("数据库连接已关闭,无法使用");
		// }
		// conn = session.connection();
		// call = conn.prepareCall("{? =call P_RP_GET_XMXH"
		// + Tools.addDbLink() + "(?,?,?)}");
		// call.registerOutParameter(1, Types.NUMERIC);
		// call.registerOutParameter(4, Types.VARCHAR);
		// call.setString(2, enCode);
		// call.setString(3, setYear);
		// call.setString(4, "100");
		// // boolean rs = call.execute();
		// str = call.getObject(4).toString();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (call != null) {
		// call.close();
		// call = null;
		// }
		// if (session != null) {
		// dao.closeSession(session);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// return str;

	}


	/**
	 * 保存项目
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public InfoPackage saveXmjl(List list) throws Exception
	{
		InfoPackage info = new InfoPackage();
		try
		{
			dao.saveOrUpdateAll(list);

			info.setSuccess(true);
		}
		catch (Exception e)
		{
			info.setSuccess(false);
		}
		return info;
	}


	/**
	 * 获取项目列表
	 * 
	 * @param year
	 * @param rgCode
	 * @param en_id
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmByEnId(String year, String rgCode, String en_id) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2," + "(select chr_name from dm_zxzq b where chr_code =  a.c3 and a.set_year = b.set_year and a.rg_code = b.rg_code) c3,"
				+ "(select chr_name from dm_xmfl b where chr_code =  a.c4 and a.set_year = b.set_year and a.rg_code = b.rg_code) c4,"
				+ "(Select chr_name from dm_xmsx b where chr_code =  a.c5 and a.set_year = b.set_year and a.rg_code = b.rg_code) c5,"
				+ "(select chr_name from dm_xmzt b where chr_code =  a.c6 and a.set_year = b.set_year and a.rg_code = b.rg_code) c6,'' km" + " from rp_xmjl a where a.set_year =" + year
				+ " and a.rg_code='" + rgCode + "' and en_id='" + en_id + "'  order by xmbm";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	/**
	 * 获取项目列表
	 * 
	 * @param year
	 * @param rgCode
	 * @return 点击不是叶子节点的时候查询项目
	 * @throws Exception
	 */
	public DataSet getXmByEnLike(String year, String rgCode, String div_code) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,(select chr_name from dm_zxzq where chr_code =  a.c3) c3,(select chr_name from dm_xmfl where chr_code =  a.c4) c4,(Select chr_name from dm_xmsx where chr_id =  a.c5) c5,(select chr_name from dm_xmzt where chr_code =  a.c6) c6,'' km"
				+ " from rp_xmjl a where a.set_year =" + year + " and a.rg_code='" + rgCode + "' and div_code like '" + div_code + "%'  order by xmbm";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	/**
	 * 删除项目
	 * 
	 * @param listSql
	 * @throws Exception
	 */
	public void postData(List listSql) throws Exception
	{
		// TODO Auto-generated method stub
		QueryStub.getQueryTool().executeBatch(listSql);

	}


	/**
	 * 根据项目序号获取项目
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmByXmxh(String xmxh, String setYear, String rgCode) throws Exception
	{
		String sql = "select a.* " + "	from rp_xmjl a   where a.xmxh = '" + xmxh + "' and set_year = " + setYear + " and rg_code = " + rgCode;
		return dao.findBySql(sql);
	}


	// /**
	// * 根据项目序号获取项目
	// *
	// * @param xmxh
	// * @return
	// * @throws Exception
	// */
	// public List getXmByXmxhHs(String xmxh) throws Exception {
	// String sql = "select a.* ";
	//
	// sql += " from rp_xmjl_history a where a.xmxh = '" + xmxh
	// + "' and a.batch_no='0'";
	// return dao.findBySql(sql);
	// }

	/**
	 * 根据项目序号获取项目
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */

	public List getXmmxByXmxhHs(String xmxh) throws Exception
	{
		return dao.findBySql("select * from rp_xmsb_history a where a.xmxh= '" + xmxh + "' and a.batch_no='0' order by a.sb_code");
	}


	/**
	 * 修改项目,
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editXmjl(DataSet ds) throws Exception
	{
		InfoPackage info = new InfoPackage();
		try
		{
			ds.setName("rp_xmjl");
			ds.setPrimarykey(new String[] { "xmxh", "set_year", "rg_code" });
			ds.post();
			info.setSuccess(true);
		}
		catch (Exception e)
		{
			info.setSuccess(false);
		}
		return info;
	}


	/**
	 * 根据预算单位获取项目列表
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmListByEnID(String enID, String rgCode) throws Exception
	{
		DataSet ds = DataSet.create();
		ds
				.setSqlClause("select a.xmxh,'(' ||decode(b.step_name,'',' 单位未送审 ',null,' 单位未送审 ',b.step_name)||')   '||a.xmmc xmmc from rp_xmjl  a  left join  (select t.is_back,t1.step_name,t.prj_code from rp_audit_cur t ,rp_s_audit_step t1 where t.audit_state =t1.step_id )  b on a.xmbm=b.prj_code where a.en_id='"
						+ enID + "' and a.wfzt_dm is null and a.set_year=" + GlobalEx.getSetYear() + " and a.rg_code='" + rgCode + "' order by a.xmbm");
		ds.open();
		return ds;
	}


	/**
	 * 根据项目序号获取项目
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmDataByXmxh(String xmxh) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select a.*  ";
		sql += "	from rp_xmjl a   where a.xmxh = '" + xmxh + "'";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	/**
	 * 根据项目序号获取项目金额信息
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmmxByXmxh(String xmxh) throws Exception
	{
		return dao.findBySql("select * from rp_xmsb where xmxh= '" + xmxh + "' order by sb_code");
	}


	/**
	 * 判断项目是否被使用过
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List isExistsXm(String xmxhs) throws Exception
	{
		return dao.findBySql("select distinct xmmc from rp_xmjl a,rp_xmsb b where a.xmxh = b.xmxh and  b.xmxh in ( " + xmxhs + ") ");
	}


	/**
	 * 根据预算单位ID获取单位控制数
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */
	// public DataSet getDwkzsByEnId(String year, String rgCode, String enId)
	// throws Exception {
	// DataSet ds = DataSet.create();
	// String sql = "select b.dwkzsid, a.chr_name en,b.f2,b.f3,b.f6,b.f7,b.bz
	// from ele_enterprise a, rp_dwkzs b where a.chr_id = b.en_id and b.set_year
	// ="+year+" and b.rg_code ='"+rgCode+"' and b.en_id ='"+enId+"'";
	// ds.setSqlClause(sql);
	// ds.open();
	// return ds;
	// }
	/**
	 * 根据预算单位ID获取单位控制数ALL
	 * 
	 * @param year
	 * @param rgCode
	 * @param enId
	 * @return
	 * @throws Exception
	 */

	// public DataSet getDwkzsByEnIdLike(String year, String rgCode, String
	// enCode) throws Exception {
	// DataSet ds = DataSet.create();
	// String sql = "select b.dwkzsid, a.chr_name en,b.f1,b.f2,b.f3,b.f4,b.bz
	// from ele_enterprise a, rp_dwkzs b where a.chr_id = b.en_id and b.set_year
	// ="+year+" and b.rg_code ='"+rgCode+"' and b.en_code like '"+enCode+"%'
	// order by b.en_code";
	// ds.setSqlClause(sql);
	// ds.open();
	// return ds;
	// }
	/**
	 * 判断预算单位是否有控制数
	 * 
	 * @param enID
	 * @return
	 * @throws Exception
	 */
	public List isExistsDwkzs(String enID) throws Exception
	{
		return dao.findBySql("select * from rp_dwkzs where en_id ='" + enID + "' ");
	}


	/**
	 * 根据ID获取单位控制数
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public List getDwkzsByDwkzsId(String dwkzsid) throws Exception
	{
		return dao.findBySql("select * from rp_dwkzs where dwkzsid ='" + dwkzsid + "' ");
	}


	/**
	 * 根据ID获取单位控制数
	 * 
	 * @param dwkzsid
	 * @return
	 * @throws Exception
	 */
	public DataSet getDwkzsByDwkzsIdForDs(String dwkzsid) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select * from rp_dwkzs where dwkzsid = '" + dwkzsid + "'";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	/**
	 * 单位控制数
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public InfoPackage editDwkzs(DataSet ds) throws Exception
	{
		InfoPackage info = new InfoPackage();
		try
		{
			ds.setName("rp_dwkzs");
			ds.setPrimarykey(new String[] { "dwkzsid" });
			ds.post();
			info.setSuccess(true);
		}
		catch (Exception e)
		{
			info.setSuccess(false);
		}
		return info;
	}


	public String getAcctInfo(String aPrjID) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		StringBuffer value = new StringBuffer();
		sql.append("select a.*,b.Acct_Code,b.acct_name,");
		sql.append("b.Acct_Code||' '||b.acct_name as fullname");
		sql.append(" from rp_xmjl_km a");
		sql.append(" left join vw_fb_acct_func b");
		sql.append(" on a.kmdm = b.BS_ID");
		sql.append(" where a.xmxh = '");
		sql.append(aPrjID);
		sql.append("'");
		int i = 0;
		DataSet ds = DBSqlExec.getDataSet(sql.toString());
		if ((ds != null) && !ds.isEmpty())
		{
			ds.beforeFirst();
			while (ds.next())
			{
				if (i++ != 0)
				{
					value.append(";");
				}
				value.append(ds.fieldByName("fullname").getString());
			}
		}
		else
		{
			return "";
		}
		return value.toString();
	}


	/**
	 * 项目排序-获取项目
	 * 
	 * @param year
	 * @param rgCode
	 * @param enCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getXmSortListByEnID(String year, String rgCode, String enCode, String identity) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select * from rp_xmjl t" + " where t.set_year = " + year + " and t.en_id = '" + enCode + "' and t.rg_code ='" + rgCode + "' order by " + identity + "_sort ";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	/**
	 * 获取项目上报理由
	 * 
	 * @param year
	 * @param rgCode
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmSbLyByXmxh(String year, String rgCode, String xmxh) throws Exception
	{
		return dao.findBySql("select * from rp_xmsb_ly where set_year =" + year + " and rg_code ='" + rgCode + "' and xmxh = '" + xmxh + "'");
	}


	/**
	 * 根据项目序号获取对应的科目列表
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getXmKmListByXmxh(String xmxh) throws Exception
	{
		return dao.findBySql("select * from rp_xmjl_km where xmxh = '" + xmxh + "'");
	}


	/**
	 * 根据功能科目code获取id
	 * 
	 * @param bsCode
	 * @return
	 * @throws Exception
	 */
	public String getBsIDByCode(String bsCode) throws Exception
	{
		String bs_id = "";
		String sql = "select * from ele_budget_subject where chr_code = '" + bsCode + "'";
		List list = dao.findBySql(sql);
		if (list.size() > 0)
		{
			Map map = (Map) list.get(0);
			bs_id = map.get("chr_id").toString();
		}
		return bs_id;
	}


	/**
	 * 获取某表数据
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List getList(String tableName) throws Exception
	{
		return dao.findBySql("select * from " + tableName + " order by chr_code");
	}


	/**
	 * 根据项目序号获取项目名称
	 * 
	 * @param xmxh
	 * @return
	 * @throws Exception
	 */
	public List getKmmcByXmxh(String xmxh) throws Exception
	{
		return dao.findBySql("select * from rp_xmjl_km a,ele_budget_subject b where a.set_year = b.set_year and a.kmdm = b.chr_id and a.xmxh = '" + xmxh + "'");
	}


	/**
	 * 获取DBLINK链接
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBLink() throws Exception
	{
		return dao.findBySql("select * from rp_scheme");
	}


	/**
	 * 获取远程数据-项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDBDataXm(String dbLinkName, String tableName, String divCode) throws Exception
	{
		return dao.findBySql("select * from " + tableName + "@" + dbLinkName + " where div_code = '" + divCode + "' and data_type='1' and batch_no = '2'  and set_year =" + GlobalEx.getSetYear());
	}


	/**
	 * 获取导入数据的单位
	 * 
	 * @return
	 * @throws Exception
	 */
	public List getDisDivCode(String dbLinkName) throws Exception
	{
		return dao.findBySql("select distinct div_code from fb_p_base@" + dbLinkName + " where data_type='1' and batch_no = '2' and set_year=" + GlobalEx.getSetYear() + "  order by div_code");
	}


	/**
	 * 获取导入的科目
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDisDivKm(String dbLinkName, String divCode, String prjCode) throws Exception
	{
		return dao.findBySql("select distinct b.bs_id from FB_P_BASE@" + dbLinkName + " a,fb_p_detail_pfs@" + dbLinkName + " b where a.prj_code = b.prj_code and b.detail_type ='11' and b.div_code='"
				+ divCode + "' and b.prj_code='" + prjCode + "' and a.data_type='1' and b.data_type='1' and  b.batch_no='2'  and a.set_year= " + GlobalEx.getSetYear());
	}


	/**
	 * 获取导入项目明细
	 * 
	 * @param dbLinkName
	 * @param divCode
	 * @return
	 * @throws Exception
	 */
	public List getDataTbxm(String dbLinkName, String divCode, String prjCode) throws Exception
	{
		return dao.findBySql("select b.* from FB_P_BASE@" + dbLinkName + " a,fb_p_detail_pfs@" + dbLinkName + " b where a.prj_code = b.prj_code  and b.div_code='" + divCode + "' and b.prj_code='"
				+ prjCode + "' and a.data_type='1' and b.data_type='1' and a.batch_no = '2' and  b.batch_no='2' and a.set_year=" + GlobalEx.getSetYear() + "  order by b.detail_type");
	}


	public String getlcwz(String xmbm) throws Exception
	{
		String step_id = null;
		String sql = "select * from rp_audit_cur where prj_code = '" + xmbm + "'";
		List list = dao.findBySql(sql);
		if (list.size() > 0)
		{
			Map map = (Map) list.get(0);
			step_id = map.get("audit_state").toString();
		}
		return step_id;

	}


	public boolean insertJlDatahis(String xmbmd)
	{
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * 根据单位获得获取项目列表
	 * 
	 * @param divCode
	 * @return
	 * @throws Exception
	 */

	public List getxmTableInfo(String divCode, int isBack, int stepId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select a.set_year,");
		sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code, a.xmmc as prj_name,d.audit_time,d.audit_text,d.audit_state, d.is_back,d.audit_userid,");
		sql.append("d.audit_isend from rp_xmjl a ");
		sql.append(" left join  RP_AUDIT_CUR d on a.xmbm = d.prj_code ");
		sql.append("  and a.set_year =d.set_year and a.rg_code = d.rg_code  where a.en_id ='" + divCode);
		sql.append("') where 1=1  ");

		if ((0 == isBack) && (0 == stepId))
		{
			sql.append(" and ((is_back = " + isBack + " and  audit_state = " + stepId + ")  or audit_state is null)");
		}
		else
		{
			sql.append(" and is_back = " + isBack + "and  audit_state = " + stepId + " ");
		}

		return dao.findBySql(sql.toString());
	}


	public DataSet getxmTableInfods(String filer, String moduleid, String flowstatus) throws Exception
	{
		String strSql = "";
		String flowcondition = "";
		if (flowstatus != null && !flowstatus.equals(""))
		{
			flowcondition = getFlowCondition("RP_TB_BILL", "BILL_ID", flowstatus, moduleid);// 返回工作流执行SQL
			strSql = getxmxl2(filer) + flowcondition;
		}
		else
		{
			strSql = getxmxl(filer);
		}

		if ("001".equals(flowstatus))
		{
			filer = filer + " AND A.XMXH NOT IN (SELECT XMXH FROM RP_TB_BILL WHERE SET_YEAR=" + SessionUtil.getLoginYear() + ")";
			String weish = getxmxl(filer);
			strSql = strSql + " union all " + weish;
		}
		DataSet ds = DBSqlExec.getDataSet("select * from (" + strSql + " ORDER BY DIV_CODE,PRJ_CODE) a  where 1=1 and exists(select 1 from sys_user_org where org_id = a.en_id and user_id = '"
				+ SessionUtil.getUserInfoContext().getUserID() + "') order by cs_sort");
		return ds;

	}


	String getxmxl(String filer)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.set_year,");
		// sql.append("
		// decode(d.audit_state,null,'可填报项目','','可填报项目',0,'可填报项目',");
		// sql.append("'审核中项目') as step_name,");
		sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code,");
		sql.append("a.wfzt_dm,a.c6,a.c5,a.xmmc as prj_name,");
		sql.append("a.rg_code,a.en_sort,a.cs_sort,decode(a.locked,1,'不可编辑','可编辑') as lockedstr,a.locked from rp_xmjl a where  1=1 ");
		sql.append(filer);
		return sql.toString();

	}


	String getxmxl2(String filer)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.SET_YEAR,");
		sql.append("A.XMXH,A.DIV_CODE,A.EN_ID,A.DIV_NAME,A.XMBM AS PRJ_CODE,");
		sql.append("A.WFZT_DM,A.C6,A.C5,A.XMMC AS PRJ_NAME,");
		sql.append("A.RG_CODE,A.EN_SORT,A.CS_SORT,decode(a.locked,1,'不可编辑','可编辑') as lockedstr,a.locked FROM RP_XMJL A,RP_TB_BILL   WHERE  A.XMXH=RP_TB_BILL.XMXH AND 1=1 ");
		sql.append(" AND  A.SET_YEAR=RP_TB_BILL.SET_YEAR");
		sql.append(filer);
		return sql.toString();
	}


	String getxmxlTZ(String filer)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.SET_YEAR,");
		// sql.append("
		// decode(d.audit_state,null,'可填报项目','','可填报项目',0,'可填报项目',");
		// sql.append("'审核中项目') as step_name,");
		sql.append("A.XMXH,A.DIV_CODE,A.EN_ID,A.DIV_NAME,A.XMBM AS PRJ_CODE,");
		sql.append("A.WFZT_DM,A.C6,A.XMMC AS PRJ_NAME,");
		sql.append("A.RG_CODE,A.EN_SORT,A.CS_SORT FROM RP_XMJL A,RP_TZ_BILL   WHERE  A.XMXH=RP_TZ_BILL.XMXH AND 1=1 ");
		sql.append(filer);
		return sql.toString();
	}


	// public String getSql(String cond){
	// StringBuffer sql = new StringBuffer();
	// sql.append("select
	// a.en_id,a.xmxh,a.xmbm,a.xmmc,a.div_code,div_name,a.c1,a.c2,");
	// sql.append("(select chr_name from dm_zxzq where chr_code = a.c3 and
	// a.set_year = set_year and a.rg_code = rg_code) as c3,");
	// sql.append("(select chr_name from dm_xmfl where chr_code = a.c4 and
	// a.set_year = set_year and a.rg_code = rg_code) as c4,");
	// sql.append(" c5,");
	// sql.append("(select chr_name from dm_xmzt where chr_code = a.c6 and
	// a.set_year = set_year and a.rg_code = rg_code) as c6,");
	// sql.append("accts,gkdw_name,gkdw_id,gkdw_code, sb_ly");
	// sql.append(" from rp_xmjl_divsb a,RP_SB_BILL where A.XMXH=RP_SB_BILL.XMXH
	// AND 1=1 ");
	// sql.append(cond);
	// return sql.toString();
	// }
	// public String getSql2(String cond){
	// StringBuffer sql = new StringBuffer();
	// sql.append("select
	// a.en_id,a.xmxh,a.xmbm,a.xmmc,a.div_code,div_name,a.c1,a.c2,");
	// sql.append("(select chr_name from dm_zxzq where chr_code = a.c3 and
	// a.set_year = set_year and a.rg_code = rg_code) as c3,");
	// sql.append("(select chr_name from dm_xmfl where chr_code = a.c4 and
	// a.set_year = set_year and a.rg_code = rg_code) as c4,");
	// sql.append(" c5,");
	// sql.append("(select chr_name from dm_xmzt where chr_code = a.c6 and
	// a.set_year = set_year and a.rg_code = rg_code) as c6,");
	// sql.append("accts,gkdw_name,gkdw_id,gkdw_code, sb_ly");
	// sql.append(" from rp_xmjl_divsb a where 1=1 ");
	// sql.append(cond);
	// return sql.toString();
	// }

	// public DataSet getxmTableInfods(String divCode, int isBack, int stepId,
	// boolean type, boolean isFisVis, boolean isTB) throws Exception {
	// String bfilter = isTB ? "=0" : ">0";
	// StringBuffer sql = new StringBuffer();
	// sql.append("select * from (select a.set_year,");
	// sql.append(" decode(d.audit_state,null,'可填报项目','','可填报项目',0,'可填报项目',");
	// sql.append("'审核中项目') as step_name,");
	// sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code,");
	// sql.append("a.wfzt_dm,a.c6,a.xmmc as
	// prj_name,d.audit_time,d.audit_text,");
	// sql.append("d.audit_state, d.is_back,d.audit_userid,");
	// sql.append("d.audit_isend,a.rg_code,a.en_sort,a.cs_sort from rp_xmjl a
	// ");
	// sql.append(" left join RP_AUDIT_CUR d on a.xmbm = d.prj_code ");
	// sql.append(" and a.set_year =d.set_year and a.rg_code = d.rg_code");
	// sql.append(" where a.en_id ='" + divCode + "' and a.set_year = "
	// + SessionUtilEx.getUserInfoContext().getSetYear()
	// + " and a.rg_code = "
	// + SessionUtilEx.getUserInfoContext().getCurrRegion());
	// sql.append(") p where 1=1 ");
	// if (!isFisVis) {
	// sql.append("and c6 !='001' ");
	// }
	// if ((0 == isBack) && (0 == stepId)) {
	// sql.append(" and ((is_back = " + isBack);
	// sql.append(" and audit_state=" + stepId);
	// sql.append(") or audit_state is null)");
	// } else {
	// sql.append(" and is_back= " + isBack);
	// sql.append("and audit_state=" + stepId);
	// }
	// sql.append(" and (exists(select 1 from rp_prjbatch pb ");
	// sql.append(" where p.prj_code=pb.prj_code and pb.batch_no");
	// sql.append(bfilter);
	// sql.append(" and pb.set_year = p.set_year and p.rg_code = pb.rg_code)");
	// if (isTB) {
	// sql.append(" or not exists(select 1 from rp_prjbatch pb where");
	// sql.append(" p.prj_code = pb.prj_code and pb.set_year=");
	// sql.append(" p.set_year and p.rg_code = pb.rg_code)");
	// }
	// sql.append(") ");
	// if(isFisVis)
	// sql.append("order by cs_sort ");
	// else
	// sql.append("order by en_sort ");
	// DataSet ds = DataSet.create();
	// ds.setSqlClause(sql.toString());
	// ds.open();
	// return ds;
	// }

	public DataSet getDwkzsByEnId(String year, String rgCode, String enId) throws Exception
	{
		String sql = "select b.dwkzsid,c.chr_name en_name,c.chr_id en_id,c.chr_code en_code,b.f1, b.f2, b.f3, b.f6, b.f7,decode(b.enable,1,'是','否') as enable,b.bz from";
		sql += " (select  a.chr_id,a.chr_name,a.chr_code  from ele_enterprise a";
		sql += " where a.chr_id  = '" + enId + "' and a.isbudget=1 and a.set_year = " + year + ") c";
		sql += " left join (select * from rp_dwkzs where rg_code ='" + rgCode + "' and set_year ='" + year + "') b on c.chr_id=b.en_id ";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	public DataSet getDwkzsByEnIdLike(String year, String rgCode, String enCode) throws Exception
	{
		String sql = "select b.dwkzsid, c.chr_name en_name,c.chr_id en_id,c.chr_code en_code,b.f1, b.f2, b.f3, b.f6, b.f7, decode(b.enable,1,'是','否') as enable,b.bz from";
		sql += " (select  a.chr_id,a.chr_name,a.chr_code  from ele_enterprise a";
		sql += " where a.chr_code  like '" + enCode + "%'  and a.isbudget=1 and set_year = " + year + ") c";
		sql += " left join (select * from rp_dwkzs where rg_code ='" + rgCode + "' and set_year ='" + year + "') b on  c.chr_code=b.en_code order by en_code";
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	public DataSet getControlMoney(String divCode, String year) throws Exception
	{

		String sql = "";
		// String sql = "select * from vw_rp_division b left join (select
		// a.xmxh,a.en_id,sum(a.total_sum) as total_sum,sum(a.F3) as
		// F3,sum(a.F2) as F2 from rp_xmsb a " +
		// " where sb_code=333 group by a.xmxh,a.en_id) c on b.EN_ID = c.en_id";
		// sql += " where b.set_year = '" + year + "' and b.div_code like '"
		// + divCode + "%'";
		sql = "select *  from vw_rp_division b where b.set_year = " + year + "  and b.div_code like '" + divCode + "%'";

		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		ds.beforeFirst();
		while (ds.next())
		{
			ds.edit();
			String div_code = ds.fieldByName("div_code").getValue().toString();
			sql = "select sum(f2) f2_sum,sum(f3) f3_sum from rp_xmsb a,rp_xmjl b where b.c6='002' and " + "a.xmxh=b.xmxh  and a.sb_code=333 and b.div_code " + "like '" + div_code
					+ "%' and b.set_year=a.set_year and a.set_year=" + year;
			List list = dao.findBySql(sql);
			String f2_sum = ((Map) (list.get(0))).get("f2_sum").toString();
			String f3_sum = ((Map) (list.get(0))).get("f3_sum").toString();
			ds.fieldByName("f2").setValue(f2_sum);
			ds.fieldByName("f3").setValue(f3_sum);
		}

		return ds;
	}


	public void deleteDwkzs(String kzsid) throws Exception
	{
	}


	// public DataSet getxmTableInfoAllds(String divCode, boolean isFisVis,
	// boolean isTB) throws Exception {
	// int maxaduit = DBSqlExec
	// .getIntValue("select max(step_id) from rp_s_audit_step");
	// String bfilter = isTB ? "=0" : ">0";
	// StringBuffer sql = new StringBuffer();
	// sql.append("select * from (select a.set_year,");
	// sql.append(" decode(d.audit_state,null,'可填报项目','','可填报项目',0,'可填报项目','");
	// sql.append(maxaduit);
	// sql.append("','已完成可重新填报', '审核中项目') as step_name,");
	// sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code");
	// sql.append(",a.c6,a.xmmc as prj_name,d.audit_time,d.audit_text,");
	// sql.append("d.audit_state, d.is_back,d.audit_userid,");
	// sql.append("d.audit_isend,a.rg_code from rp_xmjl a ");
	// sql.append(" left join RP_AUDIT_CUR d on a.xmbm = d.prj_code ");
	// sql.append(" and a.set_year =d.set_year and a.rg_code = d.rg_code ");
	// sql.append(" where a.en_id ='" + divCode + "' and a.set_year = "
	// + SessionUtilEx.getUserInfoContext().getSetYear()
	// + " and a.rg_code ="
	// + SessionUtilEx.getUserInfoContext().getCurrRegion());
	// sql.append(") p where 1=1 ");
	// if (!isFisVis)
	// sql.append(" and c6!='001'");
	// sql.append(" and (exists(select 1 from rp_prjbatch pb ");
	// sql.append(" where p.prj_code=pb.prj_code and pb.batch_no");
	// sql.append(bfilter);
	// sql.append(" and pb.set_year = p.set_year and p.rg_code = pb.rg_code)");
	// if (isTB) {
	// sql.append(" or not exists(select 1 from rp_prjbatch pb where");
	// sql.append(" p.prj_code = pb.prj_code and pb.set_year=");
	// sql.append(" p.set_year and p.rg_code = pb.rg_code)");
	// }
	// sql.append(")");
	// DataSet ds = DataSet.create();
	// ds.setSqlClause(sql.toString());
	// ds.open();
	// return ds;
	//
	// }
	public DataSet getxmTableInfods(String divCode, boolean isFisVis, boolean isTB) throws Exception
	{

		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select a.set_year,");
		DataSet ds = null;
		return ds;

	}


	public List getxmTableInfoAll(String divCode) throws Exception
	{
		// TODO Auto-generated method stu
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select a.set_year,");
		sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code, a.xmmc as prj_name,d.audit_time,d.audit_text,d.audit_state, d.is_back,d.audit_userid,");
		sql.append("d.audit_isend from rp_xmjl a ");
		sql.append(" left join  RP_AUDIT_CUR d on a.xmbm = d.prj_code ");
		sql.append("  and a.set_year =d.set_year and a.rg_code = d.rg_code  where a.en_id ='" + divCode);
		sql.append("') where 1=1  ");

		return dao.findBySql(sql.toString());

	}


	public boolean getHisNull(String prjCode) throws Exception
	{
		String sql = "select * from rp_audit_his where prj_code =" + prjCode + "";

		return dao.equals(sql);

	}


	public List getDwkzsListByEnId(String year, String rgCode, String enId) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}


	public List getDwkzsListByEnIdLike(String year, String rgCode, String enCode) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}


	public int[] getxmztNum(String userCode, boolean isTB) throws Exception
	{
		int[] zts = new int[4];
		String bfilter = isTB ? "=0" : ">0";
		StringBuffer filterEn = new StringBuffer();
		filterEn.append("(select b.org_id from sys_usermanage a ,");
		filterEn.append("sys_user_org b where a.user_id =");
		filterEn.append("b.user_id  and user_code='");
		filterEn.append(userCode + "')");
		StringBuffer filterTB = new StringBuffer();
		filterTB.append(" and (exists(select 1 from rp_prjbatch pb ");
		filterTB.append(" where a.xmbm=pb.prj_code and pb.batch_no");
		filterTB.append(bfilter);
		filterTB.append(" and pb.set_year = a.set_year and a.rg_code = pb.rg_code)");
		if (isTB)
		{
			filterTB.append(" or not exists(select 1 from  rp_prjbatch pb where");
			filterTB.append(" a.xmbm = pb.prj_code and pb.set_year=");
			filterTB.append(" a.set_year and a.rg_code = pb.rg_code)");
		}
		filterTB.append(")");
		StringBuffer filter = new StringBuffer();
		filter.append(filterEn.toString() + filterTB.toString());
		filter.append(" and a.set_year = " + SessionUtilEx.getUserInfoContext().getSetYear() + " and a.rg_code = " + SessionUtilEx.getUserInfoContext().getCurrRegion());
		// 可填报
		StringBuffer sql0 = new StringBuffer();
		sql0.append("select count(1) from rp_xmjl a where a.c6!='001' ");
		sql0.append("and (a.xmbm not in (select prj_code from rp_audit_cur b ");
		sql0.append("where (b.audit_state > 0 or b.is_back=1) and b.set_year = a.set_year and b.rg_code = a.rg_code) or a.xmbm in ");
		sql0.append("(select prj_code from rp_audit_cur b where b.audit_state");
		sql0.append("=5 or b.audit_isend=1)) and a.en_id in " + filter.toString());
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select count(1) from rp_xmjl  a where a.xmbm  in ");
		sql1.append("(select prj_code from rp_audit_cur b where ");
		sql1.append("b.audit_state = 1 and b.is_back=0 and b.set_year = a.set_year and b.rg_code = a.rg_code) and a.en_id in");
		sql1.append(filter.toString());
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select count(1) from rp_xmjl  a where a.xmbm  in ");
		sql2.append("(select prj_code from rp_audit_cur b ");
		sql2.append("where b.audit_state = 0 and b.is_back=1 and b.set_year = a.set_year and b.rg_code = a.rg_code) ");
		sql2.append("and a.en_id in" + filter.toString());
		StringBuffer sql3 = new StringBuffer();
		sql3.append("select count(1) from rp_xmjl  a");
		sql3.append(" where  a.c6!='001' and a.en_id in");
		sql3.append(filter.toString());
		zts[0] = DBSqlExec.getIntValue(sql0.toString());
		zts[1] = DBSqlExec.getIntValue(sql1.toString());
		zts[2] = DBSqlExec.getIntValue(sql2.toString());
		zts[3] = DBSqlExec.getIntValue(sql3.toString());

		// TODO Auto-generated method stub
		return zts;
	}


	public DataSet getXmBALL(String year, String rgCode) throws Exception
	{
		DataSet ds = DataSet.create();
		String sql = "select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,(select chr_name from dm_zxzq where chr_code =  a.c3) c3,(select chr_name from dm_xmfl where chr_code =  a.c4) c4,(Select chr_name from dm_xmsx where chr_id =  a.c5) c5,(select chr_name from dm_xmzt where chr_code =  a.c6) c6,'' km"
				+ " from rp_xmjl a where a.set_year =" + year + " and a.rg_code='" + rgCode + "' order by xmbm";
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}


	public DataSet getXmSearch(String year, String rgCode, Map parMap, String[] kmxz, String[] enxz) throws Exception
	{
		DataSet dsData = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql
				.append("select a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,(select chr_name from dm_zxzq where chr_code =  a.c3) c3,(select chr_name from dm_xmfl where chr_code =  a.c4) c4,(Select chr_name from dm_xmsx where chr_id =  a.c5) c5,(select chr_name from dm_xmzt where chr_code =  a.c6) c6,'' km"
						+ " from rp_xmjl a where a.set_year =" + year + " and a.rg_code='" + rgCode + "' ");
		String fxmbm = (String) parMap.get("fxmbm");
		String fxmmc = (String) parMap.get("fxmmc");
		String fqsnd = (String) parMap.get("fqsnd");
		String fjsnd = (String) parMap.get("fjsnd");
		String fzxzq = (String) parMap.get("fzxzq");
		String fxmfl = (String) parMap.get("fxmfl");
		String fxmsx = (String) parMap.get("fxmsx");
		String fxmzt = (String) parMap.get("fxmzt");
		// String fkmxz = (String)parMap.get("fkmxz");

		if (!"".equals(fxmbm))
		{
			sql.append(" and a.xmbm like '%" + fxmbm + "%'");
		}
		if (!"".equals(fxmmc))
		{
			sql.append(" and a.xmmc like '%" + fxmmc + "%'");
		}
		if (!"".equals(fqsnd))
		{
			sql.append(" and a.c1 like '%" + fqsnd + "%'");
		}
		if (!"".equals(fjsnd))
		{
			sql.append(" and a.c2 like '%" + fjsnd + "%'");
		}
		if (!"null".equals(fzxzq))
		{
			sql.append(" and a.c3 like '%" + fzxzq + "%'");
		}
		if (!"null".equals(fxmfl))
		{
			sql.append(" and a.c4 like '%" + fxmfl + "%'");
		}
		if (!"null".equals(fxmsx))
		{
			sql.append(" and a.c5 like '%" + fxmsx + "%'");
		}
		if (!"null".equals(fxmzt))
		{
			sql.append(" and a.c6 like '%" + fxmzt + "%'");
		}

		if ((kmxz != null) && !kmxz.equals("null"))
		{
			sql.append(" and a.xmxh in (select xmxh from rp_xmjl_km  where kmdm in(");
			for (int i = 0; i < kmxz.length; i++)
			{
				if (i == 0)
				{
					sql.append("'");
				}
				else
				{
					sql.append(",'");
				}
				sql.append(kmxz[i]);
				sql.append("'");

			}
			sql.append("))");
		}
		if ((enxz != null) && !enxz.equals("null"))
		{
			sql.append(" and a.en_id in (");
			for (int i = 0; i < enxz.length; i++)
			{
				if (i == 0)
				{
					sql.append("'");
				}
				else
				{
					sql.append(",'");
				}
				sql.append(enxz[i]);
				sql.append("'");

			}
			sql.append(")");
		}

		dsData.setSqlClause(sql.toString());
		dsData.open();
		return dsData;
	}


	//
	// public List getEnlist() throws Exception {
	// // TODO Auto-generated method stub
	// return dao
	// .findBySql("select * from ele_enterprise where 1=1 and isbudget=1 order
	// by chr_code ");
	// }

	public DataSet getEnxmxhs(String xmxhs) throws Exception
	{
		// TODO Auto-generated method stub
		DataSet dsData = DataSet.create();

		dsData
				.setSqlClause("select a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,(select chr_name from dm_zxzq where chr_code =  a.c3) c3,(select chr_name from dm_xmfl where chr_code =  a.c4) c4,(Select chr_name from dm_xmsx where chr_id =  a.c5) c5,(select chr_name from dm_xmzt where chr_code =  a.c6) c6,'' km"
						+ " from rp_xmjl a where a.xmxh in (" + xmxhs + ")");
		dsData.open();
		return dsData;
	}


	public DataSet getPrjCreateInfo(String filter) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,");
		sql.append("(select chr_name from dm_zxzq where chr_code =  a.c3 and a.set_year = set_year " + filter + ") as c3,");
		sql.append("(select chr_name from dm_xmfl where chr_code =  a.c4 and a.set_year = set_year " + filter + ") as c4,");
		sql.append(" a.c5,");
		sql.append("(select chr_name from dm_xmzt where chr_code =  a.c6 and a.set_year = set_year" + filter + ") as c6,");
		sql.append("accts,gkdw_name,gkdw_id,gkdw_code,a.apprcommon,a.total_sum");
		sql.append(" from rp_xmjl a where 1=1 ");
		sql.append(filter);
		return DBSqlExec.getDataSet(sql.toString());
	}


	public DataSet getPrjCreateHisInfo(String set_year, String filter) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select * From(");
		sql.append("select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,a.c5,");
		sql.append("(select chr_name from dm_zxzq where chr_code =  a.c3 and a.set_year = set_year and a.rg_code = rg_code) as c3,");
		sql.append("(select chr_name from dm_xmfl where chr_code =  a.c4 and a.set_year = set_year and a.rg_code = rg_code) as c4,");
		sql.append("(select chr_name from dm_xmzt where chr_code =  a.c6 and a.set_year = set_year and a.rg_code = rg_code) as c6,");
		sql.append("accts,gkdw_name,gkdw_id,gkdw_code");
		sql.append(" from rp_xmjl_history a where 1=1  ");
		sql.append("and set_year= ");
		sql.append(Integer.parseInt(set_year));
		sql.append(filter);
		sql.append("and NODE_NAME='预算科二审' order by ver_date desc");
		sql.append(") where rownum=1");
		DataSet ds = DBSqlExec.getDataSet(sql.toString());
		if (ds.getRecordCount() == 0)
		{
			sql.delete(0, sql.length());
			sql.append("select  a.xmxh,a.xmbm,a.xmmc,a.c1,a.c2,a.c5,");
			sql.append("(select chr_name from dm_zxzq where chr_code =  a.c3 and a.set_year = set_year and a.rg_code = rg_code) as c3,");
			sql.append("(select chr_name from dm_xmfl where chr_code =  a.c4 and a.set_year = set_year and a.rg_code = rg_code) as c4,");
			sql.append("(select chr_name from dm_xmzt where chr_code =  a.c6 and a.set_year = set_year and a.rg_code = rg_code) as c6,");
			sql.append("accts,gkdw_name,gkdw_id,gkdw_code");
			sql.append(" from rp_xmjl a where 1=1 ");
			sql.append(filter);
			sql.append("and set_year= ");
			sql.append(Integer.parseInt(set_year) - 1);

			ds = DBSqlExec.getDataSet(sql.toString());
			if (ds.getRecordCount() == 0)
				return null;
		}
		return ds;
	}


	public void savePrjCreateInfo(String setYear, String xmxh, DataSet dsPrj, String[] kmInfo, String rgCode, String batchNo, String auditNo) throws Exception
	{
		dsPrj.setName("rp_xmjl");
		dsPrj.setPrimarykey(new String[] { "xmxh" });
		dsPrj.post();
		List list = new ArrayList();
		if (kmInfo != null && kmInfo.length > 0)
		{
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append("delete from rp_xmjl_km where set_year=" + setYear + " and xmxh='" + xmxh + "'");
			list.add(sqlDel.toString());
			for (int i = 0; i < kmInfo.length; i++)
			{
				StringBuffer sqlAdd = new StringBuffer();
				sqlAdd.append("insert into rp_xmjl_km(set_year,xhid,xmxh,kmdm) values(");
				sqlAdd.append(setYear + ",newid,'" + xmxh + "','" + kmInfo[i] + "')");
				list.add(sqlAdd.toString());
			}
		}
		if (list.size() > 0)
			QueryStub.getQueryTool().executeBatch(list);
		List list1 = new ArrayList();
		// 保存历史数据
		StringBuffer sql1 = new StringBuffer();
		sql1.append("delete from rp_xmjl_history where xmxh='" + xmxh + "' and set_year = " + setYear);
		StringBuffer sql11 = new StringBuffer();
		sql11.append(" insert into rp_xmjl_history");
		sql11.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sql11.append(" accts)");
		sql11.append("select ");
		sql11.append(" set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sql11.append("accts");
		sql11.append(" from rp_xmjl ");
		sql11.append(" where xmxh='" + xmxh + "' and ");
		sql11.append(" set_year = " + setYear);
		StringBuffer sql2 = new StringBuffer();
		sql2.append("delete from rp_xmjl_km_his where xmxh='" + xmxh + "' and set_year = " + setYear);
		StringBuffer sql22 = new StringBuffer();
		sql22.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmxh='" + xmxh + "' and set_year = " + setYear);
		list1.add(sql1.toString());
		list1.add(sql11.toString());
		list1.add(sql2.toString());
		list1.add(sql22.toString());
		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);
	}


	/**
	 * 获取项目明细信息
	 */
	public DataSet getPrjDetailInfo(String setYear, String xmxh, String rgCode) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*");
		sql.append(" From rp_xmsb a where xmxh='");
		sql.append(xmxh);
		sql.append("' and set_year = ");
		sql.append(setYear);
		sql.append(" and rg_code = ");
		sql.append(rgCode);
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
			dsBody.fieldByName("SB_TYPE").setValue("总计");
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


	/**
	 * 获取项目明细信息
	 */
	public DataSet getPrjDetailInfoHis(String setYear, String xmxh, String rgCode) throws Exception
	{

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM RP_XMSB_HISTORY WHERE VER_DATE IN(");
		sql.append("SELECT MAX(VER_DATE) ");
		sql.append(" FROM RP_XMSB_HISTORY A WHERE XMXH='");
		sql.append(xmxh);
		sql.append("' AND SET_YEAR = ");
		sql.append(setYear);
		sql.append(" AND RG_CODE = ");
		sql.append(rgCode);
		sql.append("AND NODE_NAME='预算科二审'");
		sql.append(")  ");
		sql.append("AND XMXH = '");
		sql.append(xmxh);
		sql.append("'AND SET_YEAR = ");
		sql.append(setYear);
		sql.append(" AND RG_CODE = ");
		sql.append(rgCode);
		sql.append(" AND NODE_NAME='预算科二审'");
		sql.append(" ORDER BY SB_CODE,SB_TYPE");
		DataSet ds = DBSqlExec.getDataSet(sql.toString());
		if (ds.getRecordCount() == 0)
		{
			sql.delete(0, sql.length());
			sql.append("SELECT * FROM RP_XMSB A WHERE XMXH='");
			sql.append(xmxh);
			sql.append("' AND SET_YEAR = ");
			sql.append(Integer.parseInt(setYear) - 1);
			sql.append(" AND RG_CODE = ");
			sql.append(rgCode);
			sql.append(" ORDER BY SB_CODE,SB_TYPE");

			ds = DBSqlExec.getDataSet(sql.toString());
			if (ds.getRecordCount() == 0)
				return null;
		}
		return ds;
	}


	public DataSet doAuditDivData(String divCode, String setYear, String rgCode) throws Exception
	{
		StringBuffer sqlK = new StringBuffer();
		sqlK.append("select * from rp_dwkzs where is_lock =1 and set_year = ");
		sqlK.append(setYear);
		sqlK.append(" and rg_code = ");
		sqlK.append(rgCode);
		sqlK.append(" and en_code='");
		sqlK.append(divCode + "'");
		DataSet dsK = DBSqlExec.getDataSet(sqlK.toString());
		DataSet ds = DataSet.create();
		if (dsK != null && !dsK.isEmpty())
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select trim(to_char(sum(f2),'99,999,999,999.00')) as f2,trim(to_char(sum(f3),'99,999,999,999.00')) as f3,");
			sql.append(" trim(to_char(sum(f6),'99,999,999,999.00')) as f6,trim(to_char(sum(f7),'99,999,999,999.00')) as f7 ");
			sql.append(" from rp_xmsb a");
			sql.append(" where a.set_year = " + setYear);
			sql.append(" and a.rg_code = " + rgCode);
			sql.append(" and exists(");
			sql.append(" select 1 from rp_xmjl b ");
			sql.append(" where a.set_year = b.set_year");
			sql.append(" and a.rg_code = b.rg_code");
			sql.append(" and a.xmxh = b.xmxh");
			sql.append(" and b.div_code = '" + divCode + "'");
			sql.append(" and b.c6 = '002')");
			sql.append(" and a.sb_code = '333'");
			sql.append(" group by a.rg_code,a.set_year");
			DataSet dsS = DBSqlExec.getDataSet(sql.toString());
			dsK.beforeFirst();
			dsK.next();
			dsS.beforeFirst();
			dsS.next();
			int i = 0;
			if (!Common.isNullStr(dsK.fieldByName("f2").getString()) && !Common.isNullStr(dsS.fieldByName("f2").getString()))
			{
				if (dsK.fieldByName("f2").getDouble() < dsS.fieldByName("f2").getDouble())
				{
					ds.append();
					ds.fieldByName("code").setValue(String.valueOf(++i));
					ds.fieldByName("kvalue").setValue(dsK.fieldByName("f2").getString());
					ds.fieldByName("svalue").setValue(dsS.fieldByName("f2").getString());
					ds.fieldByName("remark").setValue("单位“一般预算”数超过控制数");
				}
			}
			if (!Common.isNullStr(dsK.fieldByName("f3").getString()) && !Common.isNullStr(dsS.fieldByName("f3").getString()))
			{
				if (dsK.fieldByName("f3").getDouble() < dsS.fieldByName("f3").getDouble())
				{
					ds.append();
					ds.fieldByName("code").setValue(String.valueOf(++i));
					ds.fieldByName("kvalue").setValue(dsK.fieldByName("f3").getString());
					ds.fieldByName("svalue").setValue(dsS.fieldByName("f3").getString());
					ds.fieldByName("remark").setValue("单位“基金预算”数超过控制数");
				}
			}
			if (!Common.isNullStr(dsK.fieldByName("f6").getString()) && !Common.isNullStr(dsS.fieldByName("f6").getString()))
			{
				if (dsK.fieldByName("f6").getDouble() < dsS.fieldByName("f6").getDouble())
				{
					ds.append();
					ds.fieldByName("code").setValue(String.valueOf(++i));
					ds.fieldByName("kvalue").setValue(dsK.fieldByName("f6").getString());
					ds.fieldByName("svalue").setValue(dsS.fieldByName("f6").getString());
					ds.fieldByName("remark").setValue("单位“其他”数超过控制数");
				}
			}
			if (!Common.isNullStr(dsK.fieldByName("f7").getString()) && !Common.isNullStr(dsS.fieldByName("f7").getString()))
			{
				if (dsK.fieldByName("f7").getDouble() < dsS.fieldByName("f7").getDouble())
				{
					ds.append();
					ds.fieldByName("code").setValue(String.valueOf(++i));
					ds.fieldByName("kvalue").setValue(dsK.fieldByName("f7").getString());
					ds.fieldByName("svalue").setValue(dsS.fieldByName("f7").getString());
					ds.fieldByName("remark").setValue("单位“上级”数超过控制数");
				}
			}
		}
		ds.applyUpdate();
		return ds;
	}


	public InfoPackage savePrjDetailInfo(String enID, String xmxh, DataSet ds, String setYear, String rgCode, String userID, String date) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		List list = new ArrayList();
		StringBuffer sqlDel = new StringBuffer();
		sqlDel.append("delete from rp_xmsb where xmxh='" + xmxh + "'");
		sqlDel.append(" and set_year = " + setYear);
		sqlDel.append(" and rg_code = " + rgCode);
		list.add(sqlDel.toString());
		ds.beforeFirst();
		while (ds.next())
		{
			StringBuffer sqlIns = new StringBuffer();
			sqlIns.append(" insert into rp_xmsb");
			sqlIns.append(" (set_year, xmsbid, xmxh, ysjc_dm, bs_id, bsi_id, ");
			sqlIns.append(" f1,f2, f3, f6, f7,f8,f9,f10, en_id, rg_code, ");
			sqlIns.append("sb_type, wszt_dm, lrr_dm, lrrq, xgr_dm, xgrq, bz, ");
			sqlIns.append("sb_code, total_sum,ysjc_mc,acct_name,acct_name_jj)  values (");
			sqlIns.append(setYear + ",newid,'" + xmxh + "','" + ds.fieldByName("YSJC_DM").getString() + "','");
			sqlIns.append(ds.fieldByName("BS_ID").getString() + "','");
			sqlIns.append(ds.fieldByName("BSI_ID").getString() + "',");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f1").getString()) ? 0 : ds.fieldByName("f1").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f2").getString()) ? 0 : ds.fieldByName("f2").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f3").getString()) ? 0 : ds.fieldByName("f3").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f6").getString()) ? 0 : ds.fieldByName("f6").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f7").getString()) ? 0 : ds.fieldByName("f7").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f8").getString()) ? 0 : ds.fieldByName("f8").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f9").getString()) ? 0 : ds.fieldByName("f9").getFloat()) + ",");
			sqlIns.append((Common.isNullStr(ds.fieldByName("f10").getString()) ? 0 : ds.fieldByName("f10").getFloat()) + ",'");
			sqlIns.append(enID.trim() + "'," + rgCode + ",'" + ds.fieldByName("SB_TYPE").getString() + "',0,'");
			sqlIns.append(userID + "','" + date + "','" + userID + "','" + date + "','");
			sqlIns.append(ds.fieldByName("BZ").getString() + "','");
			sqlIns.append(ds.fieldByName("SB_CODE").getString() + "',");
			sqlIns.append((Common.isNullStr(ds.fieldByName("total_sum").getString()) ? 0 : ds.fieldByName("total_sum").getFloat()) + ",'");
			sqlIns.append(ds.fieldByName("ysjc_mc").getString() + "','");
			sqlIns.append(ds.fieldByName("acct_name").getString() + "','");
			sqlIns.append(ds.fieldByName("acct_name_jj").getString());
			sqlIns.append("')");
			list.add(sqlIns.toString());
		}
		QueryStub.getQueryTool().executeBatch(list);
		return info;
	}


	public void savePrjInfoHistory(String[] xmxh, int batchNo, int stepID) throws Exception
	{
		List list = new ArrayList();
		String setYear = Global.loginYear;
		String rgCode = Global.getCurrRegion();
		if (xmxh != null && xmxh.length > 0)
		{
			for (int i = 0; i < xmxh.length; i++)
			{
				// 插入简历表历史记录
				StringBuffer sql1 = new StringBuffer();
				sql1.append("delete from rp_xmjl_history where xmxh='" + xmxh + "' and set_year = " + setYear + " and batch_no = " + batchNo + " and step_id=" + stepID);
				list.add(sql1.toString());
				StringBuffer sql11 = new StringBuffer();
				sql11.append(" insert into rp_xmjl_history");
				sql11.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
				sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
				sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
				sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
				sql11.append(" accts, step_id, batch_no,gkdw_name)");
				sql11.append("select ");
				sql11.append(" set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
				sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
				sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
				sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
				sql11.append("accts," + batchNo + "," + stepID + "," + "gkdw_name");
				sql11.append(" from rp_xmjl ");
				sql11.append(" where xmxh='" + xmxh[i] + "' and ");
				sql11.append(" set_year = " + setYear);
				sql11.append(" rg_code = " + rgCode);
				list.add(sql11.toString());
				// 插入项目申报表历史记录
				StringBuffer sql2 = new StringBuffer();
				sql2.append("delete from rp_xmsb_history where xmxh='" + xmxh + "' and set_year = " + setYear + " and batch_no = " + batchNo + " and step_id=" + stepID);
				list.add(sql2.toString());
				StringBuffer sql22 = new StringBuffer();
				sql22.append("insert into rp_xmsb_history");
				sql22.append(" (set_year, xmsbid, xmxh, ysjc_dm, bs_id, bsi_id, ");
				sql22.append(" f1, f2, f3, f4, f5, f6, f7, f8,f9,f10, en_id, rg_code, ");
				sql22.append(" sb_type, wszt_dm, lrr_dm, lrrq, xgr_dm, xgrq, ");
				sql22.append(" bz, sb_code, total_sum, step_id, batch_no)");
				sql22.append("select ");
				sql22.append(" set_year, xmsbid, xmxh, ysjc_dm, bs_id, bsi_id, ");
				sql22.append(" f1, f2, f3, f4, f5, f6, f7, f8,f9,f10, en_id, rg_code, ");
				sql22.append(" sb_type, wszt_dm, lrr_dm, lrrq, xgr_dm, xgrq, ");
				sql22.append(" bz, sb_code, total_sum, step_id, batch_no");
				sql22.append("from rp_xmsb");
				sql11.append(" where xmxh='" + xmxh[i] + "' and ");
				sql11.append(" set_year = " + setYear);
				sql11.append(" rg_code = " + rgCode);
			}
		}
	}


	public int getPrjCount(String div_code)
	{
		int result = 0;
		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT max(substr(xmbm , LENGTH(xmbm)-3 ,4)) AS COUNT FROM  rp_xmjl ").append(" WHERE DIV_CODE =? ");
		// .append(" WHERE SET_YEAR=? AND DIV_CODE =? ");

		// int setYear = new
		// Integer(SessionUtil.getUserInfoContext().getSetYear())
		// .intValue();
		Session session = dao.getSession();
		PreparedStatement pstmt;
		try
		{
			pstmt = session.connection().prepareStatement(sb.toString());
			// pstmt.setInt(1, setYear);
			pstmt.setString(1, div_code);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
			}
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 关闭会话
			if (session != null)
			{
				dao.closeSession(session);
			}
		}
		return result;
	}


	// public DataSet getPrjSbCreateInfo(String filer) throws Exception {
	// StringBuffer sql = new StringBuffer();
	// sql.append("select
	// a.en_id,a.xmxh,a.xmbm,a.xmmc,a.div_code,div_name,a.c1,a.c2,");
	// sql.append("(select chr_name from dm_zxzq where chr_code = a.c3 and
	// a.set_year = set_year and a.rg_code = rg_code) as c3,");
	// sql
	// .append("(select chr_name from dm_xmfl where chr_code = a.c4 and
	// a.set_year = set_year and a.rg_code = rg_code) as c4,");
	// sql.append(" c5,");
	// // .append("(Select chr_code||' '||chr_name from dm_xmsx where chr_id =
	// // a.c5 and a.set_year = set_year and a.rg_code = rg_code) as c5,");
	// sql
	// .append("(select chr_name from dm_xmzt where chr_code = a.c6 and
	// a.set_year = set_year and a.rg_code = rg_code) as c6,");
	// sql.append("accts,gkdw_name,gkdw_id,gkdw_code, sb_ly");
	// sql.append(" from rp_xmjl_divsb a where 1=1 ");
	// sql.append(filer);
	// return DBSqlExec.getDataSet(sql.toString());
	// }

	public void savePrjCreateSbInfo(DataSet ds, String setYear, String xmxh, String[] kmInfo, String rgCode) throws Exception
	{
		ds.setName("rp_xmjl_divsb");
		ds.setPrimarykey(new String[] { "xmxh" });
		ds.post();
		List list = new ArrayList();
		if (kmInfo != null && kmInfo.length > 0)
		{
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append("delete from rp_xmjl_km_divsb where set_year=" + setYear + " and xmxh='" + xmxh + "'");
			list.add(sqlDel.toString());
			for (int i = 0; i < kmInfo.length; i++)
			{
				StringBuffer sqlAdd = new StringBuffer();
				sqlAdd.append("insert into rp_xmjl_km_divsb(set_year,xhid,xmxh,kmdm) values(");
				sqlAdd.append(setYear + ",newid,'" + xmxh + "','" + kmInfo[i] + "')");
				list.add(sqlAdd.toString());
			}
		}
		QueryStub.getQueryTool().executeBatch(list);
	}


	// public void sendbackAudit(int type, String xmxhs, String rgCode,
	// String setYear) throws Exception {
	// StringBuffer sql = new StringBuffer();
	// List list = new ArrayList();
	// if (type == 1) {
	// list.add("update rp_xmjl_divsb set is_back = 0 where xmxh in("
	// + xmxhs + ") and set_year ='" + setYear
	// + "' and rg_code ='" + rgCode + "'");
	// sql.append("update rp_xmjl_divsb set step_id = " + type
	// + " where xmxh in(" + xmxhs + ") and set_year ='" + setYear
	// + "' and rg_code ='" + rgCode + "'");
	//
	// }
	// if (type == 0 || type == 2)
	// sql.append("update rp_xmjl_divsb set step_id = 0 where xmxh in("
	// + xmxhs + ") and set_year ='" + setYear
	// + "' and rg_code ='" + rgCode + "'");
	// list.add(sql.toString());
	// if (type == 2)
	// list.add("update rp_xmjl_divsb set is_back = 1 where xmxh in("
	// + xmxhs + ") and set_year ='" + setYear
	// + "' and rg_code ='" + rgCode + "'");
	//
	// QueryStub.getQueryTool().executeBatch(list);
	// }

	// public void sendbackAudit(int type, List xmxhs, String rgCode,String
	// setYear) throws Exception {
	public String sendbackAudit(List xmxhs, String rgCode, String setYear, String moduleId, int type) throws Exception
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
				billId = getbill(xmxh, "RP_SB_BILL", setYear);
				goFlow(null, "RP_SB_BILL", "BILL_ID", billId, flowstate, moduleId);
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		return msg;
	}


	public void doPrjCreateInfo(String setYear, String xmxhstr, String prjCode, String rgCode, String step_id, String batchNo, String auditNo) throws Exception
	{

		List list = new ArrayList();
		StringBuffer sqlDel = new StringBuffer();
		sqlDel.append("delete from rp_xmjl_km where set_year=" + setYear + " and xmxh = '" + xmxhstr + "'");
		list.add(sqlDel.toString());
		StringBuffer sql1 = new StringBuffer();
		sql1.append("delete from rp_xmjl where xmxh = '" + xmxhstr + "' and set_year = " + setYear + " and rg_code = " + rgCode);
		StringBuffer sql11 = new StringBuffer();
		sql11.append(" insert into rp_xmjl");
		sql11.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sql11.append(" accts,gkdw_name)");
		sql11.append("select ");
		sql11.append(" set_year, xmxh, '" + prjCode + "', xmmc, c1, c2, c3, c4, ");
		sql11.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sql11.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sql11.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sql11.append("accts,gkdw_name");
		sql11.append(" from rp_xmjl_divsb ");
		sql11.append(" where xmxh = '" + xmxhstr + "' and ");
		sql11.append(" set_year = " + setYear + " and rg_code = " + rgCode);
		StringBuffer sql2 = new StringBuffer();
		//		
		//		
		//		
		// sql2.append(" update rp_xmjl_divsb set step_id =2 where xmxh = '"
		// + xmxhstr + "' and ");
		// sql2.append(" set_year = " + setYear + " and rg_code = " + rgCode);
		StringBuffer sql22 = new StringBuffer();
		sql22.append("insert into rp_xmsb_ly (SET_YEAR, LY_ID, RG_CODE, XMXH, LY)");
		sql22.append("select set_year,newid," + rgCode + ",xmxh,sb_ly from rp_xmjl_divsb ");

		sql22.append(" where xmxh = '" + xmxhstr + "' and ");
		sql22.append(" set_year = " + setYear + " and rg_code = " + rgCode);
		StringBuffer sqlAdd = new StringBuffer();
		sqlAdd.append("insert into rp_xmjl_km(set_year,xhid,xmxh,kmdm)");
		sqlAdd.append(" select set_year,xhid,xmxh,kmdm from  rp_xmjl_km_divsb where xmxh = '" + xmxhstr + "' and set_year = " + setYear + "");

		list.add(sql1.toString());
		list.add(sql11.toString());
		// list.add(sql2.toString());
		list.add(sql22.toString());
		list.add(sqlAdd.toString());

		if (list.size() > 0)
			QueryStub.getQueryTool().executeBatch(list);

		List list1 = new ArrayList();
		// 保存历史数据
		StringBuffer sqlh = new StringBuffer();

		sqlh.append("delete from rp_xmjl_history where xmxh='" + xmxhstr + "' and set_year = " + setYear);
		StringBuffer sqlh1 = new StringBuffer();
		sqlh1.append(" insert into rp_xmjl_history");
		sqlh1.append(" (set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append(" accts)");
		sqlh1.append("select ");
		sqlh1.append(" set_year, xmxh, xmbm, xmmc, c1, c2, c3, c4, ");
		sqlh1.append(" c5, c6, c7, c8, c9, c10, en_id, rg_code, lrr_dm, ");
		sqlh1.append(" lrrq, xgr_dm, xgrq, wfzt_dm, div_code, div_name, ");
		sqlh1.append(" cs_sort, en_sort, gkdw_id, gkdw_code, ");
		sqlh1.append("accts");
		sqlh1.append(" from rp_xmjl ");
		sqlh1.append(" where xmxh='" + xmxhstr + "' and ");
		sqlh1.append(" set_year = " + setYear);
		StringBuffer sqlh2 = new StringBuffer();
		sql2.append("delete from rp_xmjl_km_his where xmxh='" + xmxhstr + "' and set_year = " + setYear);
		StringBuffer sqlh22 = new StringBuffer();
		sql22.append("insert into rp_xmjl_km_his select * from rp_xmjl_km where xmxh='" + xmxhstr + "' and set_year = " + setYear);
		list1.add(sqlh.toString());
		list1.add(sqlh1.toString());
		list1.add(sqlh2.toString());
		list1.add(sqlh22.toString());
		if (list1.size() > 0)
			QueryStub.getQueryTool().executeBatch(list1);

	}


	public DataSet getPrjSbCreateInfo(String filer, String flowstatus, String moduleId) throws Exception
	{
		DataSet ds = null;
		String strSql = "";
		String flowcondition = "";
		if (flowstatus != null && !flowstatus.equals(""))
			flowcondition = getFlowCondition("RP_SB_BILL", "BILL_ID", flowstatus, moduleId);// 返回工作流执行SQL
		// 当工作流执行SQL语句不等于空时

		if (flowcondition != null && !flowcondition.equals(""))
		{
			strSql = getSql(filer) + flowcondition;
		}
		else
		{
			strSql = getSql2(filer);
		}
		if ("001".equals(flowstatus))
		{
			filer = filer + " AND XMXH NOT IN (SELECT XMXH FROM RP_SB_BILL WHERE RP_SB_BILL.SET_YEAR =" + SessionUtil.getLoginYear() + ") ";
			String weish = getSql2(filer);
			strSql = strSql + " union all " + weish;
		}

		ds = DBSqlExec.getDataSet("select * from (" + strSql + " ORDER BY DIV_CODE) a  where 1=1 and exists(select 1 from sys_user_org where org_id = a.en_id and user_id = '"
				+ SessionUtil.getUserInfoContext().getUserID() + "')");
		return ds;
	}


	public String getSql(String cond)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  A.EN_ID,A.XMXH,A.XMBM,A.XMMC,A.DIV_CODE,DIV_NAME,A.C1,A.C2,");
		sql.append("(SELECT CHR_NAME FROM DM_ZXZQ WHERE CHR_CODE =  A.C3 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C3,");
		sql.append("(SELECT CHR_NAME FROM DM_XMFL WHERE CHR_CODE =  A.C4 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C4,");
		sql.append(" C5,");
		sql.append("(SELECT CHR_NAME FROM DM_XMZT WHERE CHR_CODE =  A.C6 AND A.SET_YEAR = SET_YEAR AND A.RG_CODE = RG_CODE) AS C6,");
		sql.append("ACCTS,GKDW_NAME,GKDW_ID,GKDW_CODE, SB_LY");
		sql.append(" FROM RP_XMJL_DIVSB A,RP_SB_BILL  WHERE A.XMXH=RP_SB_BILL.XMXH AND 1=1  ");
		sql.append(" AND RP_SB_BILL.SET_YEAR=A.SET_YEAR  ");
		sql.append(cond);
		return sql.toString();
	}


	public String getSql2(String cond)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.en_id,a.xmxh,a.xmbm,a.xmmc,a.div_code,div_name,a.c1,a.c2,");
		sql.append("(select chr_name from dm_zxzq where chr_code =  a.c3 and a.set_year = set_year and a.rg_code = rg_code) as c3,");
		sql.append("(select chr_name from dm_xmfl where chr_code =  a.c4 and a.set_year = set_year and a.rg_code = rg_code) as c4,");
		sql.append(" c5,");
		sql.append("(select chr_name from dm_xmzt where chr_code =  a.c6 and a.set_year = set_year and a.rg_code = rg_code) as c6,");
		sql.append("accts,gkdw_name,gkdw_id,gkdw_code, sb_ly");
		sql.append(" from rp_xmjl_divsb a where  1=1  ");
		sql.append(cond);
		return sql.toString();
	}


	public String doSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception
	{
		String msg = "审核成功";
		String billId = "";
		for (int i = 0; i < xmxhs.size(); i++)
		{
			String xmxh = Common.nonNullStr(((Map) xmxhs.get(i)).get("xmxh"));
			if (!isworkflow(xmxh, "RP_SB_BILL"))
			{
				billId = UUIDRandom.generate();
				intoBill(Common.nonNullStr(((Map) xmxhs.get(i)).get("enid")), billId, xmxh, "RP_SB_BILL");
				inputFlow("RP_SB_BILL", "BILL_ID", billId, moduleId);
				goFlow(null, "RP_SB_BILL", "BILL_ID", billId, "TONEXT", moduleId);
			}
			else
			{
				billId = getbill(xmxh, "RP_SB_BILL", setYear);
				goFlow(null, "RP_SB_BILL", "XMXH", billId, "TONEXT", moduleId);
			}
		}

		return msg;
	}


	/**
	 * 撤销退回
	 * 
	 * @param prjCode
	 *            项目编码
	 * @return
	 * @throws Exception
	 */
	public InfoPackage BackAuditInfoByDiv(int type, String[] prjCodes, String aUserCode, String rgCode, String moduleid, String year) throws Exception
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
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
			for (int i = 0; i < prjCodes.length; i++)
			{
				String xmxh = prjCodes[i].toString();
				billId = getbill(xmxh, "RP_SB_BILL", year);
				goFlow(null, "RP_SB_BILL", "XMXH", billId, flowstate, moduleid);
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		info.setsMessage("撤消成功  " + prjCodes.length + "   条项目");
		return info;
	}


	public boolean isworkflow(String xmxh, String tablename)
	{
		String sql = "SELECT 1 FROM  " + tablename + " where xmxh='" + xmxh + "'";
		if (dao.findBySql(sql).size() > 0)
			return true;
		else
			return false;
	}


	public String getbill(String xmxh, String tablename, String set_year)
	{
		String bill = "select bill_id from " + tablename + " where xmxh='" + xmxh + "' and set_year=" + set_year;

		List list = dao.findBySql(bill);
		if (list == null || list.isEmpty())
			return null;

		return ((Map) (list.get(0))).get("bill_id").toString();
	}


	public String getNodeName(String xmxh, String setYear) throws Exception
	{
		String node_name = null;
		String bill_id = getbill(xmxh, "RP_TB_BILL", setYear);
		if (bill_id == null || getTasksNode(bill_id, "RP_TB_BILL") == null)
		{
			node_name = "未送审";
		}
		else
		{
			node_name = ((Map) (getTasksNode(bill_id, "RP_TB_BILL").get(0))).get("node_name").toString();
		}
		return node_name;
	}


	public int intoBill(String enID, String bill_id, String xmxh, String tablename)
	{

		dao.executeBySql("delete from " + tablename + " where xmxh ='" + xmxh + "'");
		String sql = "insert into " + tablename + "(en_id,bill_id,xmxh,set_year )values('" + enID + "','" + bill_id + "','" + xmxh + "','" + SessionUtil.getLoginYear() + "')";
		return dao.executeBySql(sql);
	}


	public DataSet getPrjSbshCreateInfo(String filer, String flowstatus, String moduleId) throws Exception
	{
		// TODO Auto-generated method stub
		DataSet ds = null;
		String strSql = "";
		String flowcondition = "";
		if (flowstatus != null && !flowstatus.equals(""))
			flowcondition = getFlowCondition("RP_SB_BILL", "BILL_ID", flowstatus, moduleId);// 返回工作流执行SQL

		if (flowcondition != null && !flowcondition.equals(""))

			strSql = getSql(filer) + flowcondition;
		else
		{
			strSql = getSql2(filer);
		}

		ds = DBSqlExec.getDataSet(strSql);
		return ds;
	}


	public String doshSend(List xmxhs, String rgCode, String setYear, String moduleId) throws Exception
	{
		// TODO Auto-generated method stub
		String msg = "";
		String billId = "";
		try
		{
			for (int i = 0; i < xmxhs.size(); i++)
			{
				String xmxh = xmxhs.get(i).toString();
				billId = getbill(xmxh, "RP_SB_BILL", setYear);
				goFlow(null, "RP_SB_BILL", "BILL_ID", billId, "TONEXT", moduleId);
			}
		}
		catch (Exception ex)
		{
			msg = "审核出错";
			ex.printStackTrace();
		}
		return msg;
	}


	public DataSet getxmTableInfoAllds(String filer, String flowstatus, String moduleid) throws Exception
	{
		DataSet ds = null;
		String strSql = "";
		String flowcondition = "";
		if (flowstatus != null && !flowstatus.equals(""))
			flowcondition = getFlowCondition("RP_TZ_BILL", "BILL_ID", flowstatus, moduleid);// 返回工作流执行SQL
		// 当工作流执行SQL语句不等于空时
		strSql = getxmxlTZ(filer);
		strSql = strSql + "";
		if (flowcondition != null && !flowcondition.equals(""))
			strSql = strSql + flowcondition;
		if ("001".equals(flowstatus))
		{
			filer = filer + " AND A.XMXH NOT IN (SELECT XMXH FROM RP_TZ_BILL) ";
			String canAdjust = canAdjust(filer);
			strSql = strSql + " union all " + canAdjust;
		}
		ds = DBSqlExec.getDataSet(strSql);
		return ds;

	}


	String canAdjust(String filer)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.set_year,");
		// sql.append("
		// decode(d.audit_state,null,'可填报项目','','可填报项目',0,'可填报项目',");
		// sql.append("'审核中项目') as step_name,");
		sql.append("a.xmxh,a.div_code,a.en_id,a.div_name,a.xmbm as prj_code,");
		sql.append("a.wfzt_dm,a.c6,a.xmmc as prj_name,");
		sql.append("a.rg_code,a.en_sort,a.cs_sort from rp_xmjl a, ");
		sql.append("sys_wf_end_tasks b,rp_tb_bill c where  1=1 and a.xmxh=c.xmxh and c.bill_id=b.entity_id and b.wf_table_name='RP_TB_BILL'");
		sql.append(filer);
		return sql.toString();

	}


	public DataSet getQueryProject2(String cond) throws Exception
	{
		// TODO Auto-generated method stub
		String condition = " AND (EXISTS (SELECT * FROM SYS_WF_COMPLETE_ITEM B,RP_TB_BILL C WHERE B.ENTITY_ID=C.BILL_ID AND NVL(A.XMXH, '')=C.XMXH)  OR"
				+ " EXISTS (SELECT * FROM SYS_WF_CURRENT_ITEM B ,RP_TB_BILL C WHERE B.ENTITY_ID=C.BILL_ID AND NVL(A.XMXH, '')=C.XMXH))";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT PRJ_BTTYPE STEP_ID,EN_CODE DIV_CODE,BIS_NAME XMMC,EN_NAME DIV_NAME,BIS_CODE XMBM,EN_ID,SET_YEAR,RG_CODE,C1,C2,ACCTS,XMXH,");
		sb.append("(SELECT CHR_NAME FROM DM_ZXZQ WHERE CHR_CODE=a.C3 AND A.SET_YEAR=SET_YEAR) AS C3,");
		sb.append("(SELECT  CHR_NAME  FROM DM_XMFL WHERE CHR_CODE=A.C4 AND A.SET_YEAR=SET_YEAR) AS C4,");
		sb.append("(SELECT  CHR_NAME  FROM DM_XMZT  WHERE CHR_CODE=A.C6 AND A.SET_YEAR=SET_YEAR) AS C6,C5,C4 C11 ");
		sb.append("FROM VW_RP_RETURN A WHERE 1=1  AND EN_CODE LIKE '");
		sb.append(cond + "%' " + condition);
		sb.append(" GROUP BY PRJ_BTTYPE,EN_CODE,BIS_NAME,EN_NAME,BIS_CODE,EN_ID,SET_YEAR,RG_CODE,C1,C2,C3,C4,C5,C6,ACCTS,XMXH ");
		sb.append(" ORDER BY STEP_ID");
		return DBSqlExec.getDataSet(sb.toString());
	}


	public DataSet getQueryProject(String cond) throws Exception
	{

		String condition = " AND NOT EXISTS (SELECT * FROM SYS_WF_COMPLETE_ITEM B,RP_TB_BILL C WHERE B.ENTITY_ID=NVL(C.BILL_ID,'') AND C.XMXH=NVL(A.XMXH, '') )  AND"
				+ " NOT EXISTS (SELECT * FROM SYS_WF_CURRENT_ITEM B,RP_TB_BILL C WHERE B.ENTITY_ID=NVL(C.BILL_ID,'') AND C.XMXH=NVL(A.XMXH, '') )";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT PRJ_BTTYPE STEP_ID,EN_CODE DIV_CODE,BIS_NAME XMMC,EN_NAME DIV_NAME,BIS_CODE XMBM,EN_ID,SET_YEAR,RG_CODE,C1,C2,ACCTS,nvl(XMXH,'') XMXH,");
		sb.append("(SELECT CHR_NAME FROM DM_ZXZQ WHERE CHR_CODE=a.C3 AND A.SET_YEAR=SET_YEAR) AS C3,");
		sb.append("(SELECT  CHR_NAME  FROM DM_XMFL WHERE CHR_CODE=A.C4 AND A.SET_YEAR=SET_YEAR) AS C4,");
		sb.append("(SELECT  CHR_NAME  FROM DM_XMZT  WHERE CHR_CODE=A.C6 AND A.SET_YEAR=SET_YEAR) AS C6,C5,C4 C11 ");
		sb.append("FROM VW_RP_RETURN A WHERE 1=1");
		if (StringUtils.isNotEmpty(cond))
		{
			sb.append(" AND (EN_CODE LIKE '").append(cond).append("%' OR A.GL_CODE LIKE '").append(cond).append("%')");
		}
		sb.append(condition);
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
				String sql = "SELECT bs_code, bs_name,budget_money FROM  RP_PRJ_RETURN  WHERE EN_CODE=" + div_code + " AND BIS_NAME='" + xmmc + "'";
				List list = dao.findBySql(sql);
				String gnkm_prj = "";
				String budgetMoney = "";
				for (int j = 0; j < list.size(); j++)
				{
					String gnkm_code = ((Map) (list.get(j))).get("bs_code").toString();// 功能科目
					String gnkm = ((Map) (list.get(j))).get("bs_name").toString();// 功能科目
					gnkm_prj = "[" + gnkm_code + "]" + gnkm + ";";
					budgetMoney = ((Map) (list.get(j))).get("budget_money").toString();
				}
				xmxh = UUIDRandom.generate();
				ds.fieldByName("XMXH").setValue(xmxh);
				ds.fieldByName("ACCTS").setValue(gnkm_prj);
				ds.fieldByName("budget_money").setValue(budgetMoney);
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
		String newid1 = UUIDRandom.generate();

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

		dsBody.append();
		dsBody.fieldByName("xmsbid").setValue(newid1);
		dsBody.fieldByName("xmxh").setValue(xmxh);
		dsBody.fieldByName("rg_code").setValue(rgCode);
		dsBody.fieldByName("setyear").setValue(sYear);
		dsBody.fieldByName("ysjc_mc").setValue("");
		dsBody.fieldByName("sb_code").setValue("111");
		dsBody.fieldByName("acct_name").setValue("");
		dsBody.fieldByName("acct_name_jj").setValue("");
		dsBody.fieldByName("sb_type").setValue("总预算");
		dsBody.append();
		String newid2 = UUIDRandom.generate();

		dsBody.fieldByName("xmsbid").setValue(newid2);
		dsBody.fieldByName("xmxh").setValue(xmxh);
		dsBody.fieldByName("rg_code").setValue(rgCode);
		dsBody.fieldByName("setyear").setValue(sYear);
		dsBody.fieldByName("sb_code").setValue("222");
		dsBody.fieldByName("ysjc_mc").setValue("");
		dsBody.fieldByName("acct_name").setValue("");
		dsBody.fieldByName("acct_name_jj").setValue("");
		dsBody.fieldByName("sb_type").setValue("已安排数");
		dsBody.append();
		String newid3 = UUIDRandom.generate();
		dsBody.fieldByName("xmsbid").setValue(newid3);
		dsBody.fieldByName("xmxh").setValue(xmxh);
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
			dsBody.fieldByName("xmxh").setValue(xmxh);
			String xmxh_new = UUIDRandom.generate();
			dsBody.fieldByName("xmsbid").setValue(xmxh_new); // ADD BY XYS
			dsBody.fieldByName("acct_name").setValue("[" + ds.fieldByName("bs_code").getString() + "]" + ds.fieldByName("bs_name").getString());
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
	 * 保存项目填报明细信息
	 * 
	 * @return String
	 */
	public String savePrjTbDetailInfo(String enID, String xmxh, DataSet ds, String aYear, String rgCode, String userID, String date)
	{
		String msg = "";
		try
		{
			List list = new ArrayList();
			StringBuffer sql = new StringBuffer();
			// 删除项目填报明细历史数据
			sql.append("DELETE FROM RP_XMSB WHERE XMXH='").append(xmxh).append("'");
			// list.add( sql.toString());
			dao.executeBySql(sql.toString());
			sql.delete(0, sql.length());

			ds.beforeFirst();
			while (ds.next())
			{
				sql.delete(0, sql.length());
				String ysjcDm = ds.fieldByName("ysjc_dm").getString();
				String bsId = ds.fieldByName("bs_id").getString();
				String bsiId = ds.fieldByName("bsi_id").getString();
				float f1 = ConstStr.isNullStr(ds.fieldByName("f1").getString()) ? 0 : ds.fieldByName("f1").getFloat();
				float f2 = ConstStr.isNullStr(ds.fieldByName("f2").getString()) ? 0 : ds.fieldByName("f2").getFloat();
				float f3 = ConstStr.isNullStr(ds.fieldByName("f3").getString()) ? 0 : ds.fieldByName("f3").getFloat();
				float f6 = ConstStr.isNullStr(ds.fieldByName("f6").getString()) ? 0 : ds.fieldByName("f6").getFloat();
				float f7 = ConstStr.isNullStr(ds.fieldByName("f7").getString()) ? 0 : ds.fieldByName("f7").getFloat();
				float f8 = ConstStr.isNullStr(ds.fieldByName("f8").getString()) ? 0 : ds.fieldByName("f8").getFloat();
				float f9 = ConstStr.isNullStr(ds.fieldByName("f9").getString()) ? 0 : ds.fieldByName("f9").getFloat();
				float f10 = ConstStr.isNullStr(ds.fieldByName("f10").getString()) ? 0 : ds.fieldByName("f10").getFloat();
				String sbType = ds.fieldByName("sb_type").getString();
				String bz = ds.fieldByName("bz").getString();
				String sbCode = ds.fieldByName("sb_code").getString();
				float totalSum = ConstStr.isNullStr(ds.fieldByName("total_sum").getString()) ? 0 : ds.fieldByName("total_sum").getFloat();
				String ysjcMc = ds.fieldByName("ysjc_mc").getString();
				String acctName = ds.fieldByName("acct_name").getString();
				String acctNamejj = ds.fieldByName("acct_name_jj").getString();
				String xmxh_new = ds.fieldByName("xmsbid").getString();

				// 插入明细执行SQL
				sql.append(" INSERT INTO RP_XMSB ");
				sql.append("(SET_YEAR, XMSBID, XMXH, YSJC_DM, BS_ID, BSI_ID,");
				sql.append("F1,F2, F3, F6, F7,F8,F9,F10, EN_ID, RG_CODE,");
				sql.append("SB_TYPE, WSZT_DM, LRR_DM, LRRQ, XGR_DM, XGRQ, BZ,");
				sql.append("SB_CODE, TOTAL_SUM,YSJC_MC,ACCT_NAME,ACCT_NAME_JJ) VALUES (");
				sql.append(aYear + ",'" + xmxh_new + "','" + xmxh + "','" + ysjcDm + "','");
				sql.append(bsId + "','");
				sql.append(bsiId + "',");
				sql.append(f1 + ",");
				sql.append(f2 + ",");
				sql.append(f3 + ",");
				sql.append(f6 + ",");
				sql.append(f7 + ",");
				sql.append(f8 + ",");
				sql.append(f9 + ",");
				sql.append(f10 + ",");
				if (enID.length() <= 0)
					sql.append("' '");
				else
					sql.append("'").append(enID.trim()).append("'");
				sql.append("," + rgCode + ",'" + sbType + "',0,'");
				sql.append(userID + "','" + date + "','" + userID + "','" + date + "','");
				sql.append(bz + "','");
				sql.append(sbCode + "',");
				sql.append(totalSum + ",'");
				sql.append(ysjcMc + "','");
				sql.append(acctName + "','");
				sql.append(acctNamejj);
				sql.append("')");
				String strSql = sql.toString();
				list.add(strSql);
			}
			QueryStub.getQueryTool().executeBatch(list);
		}
		catch (Exception ex)
		{
			msg = "保存失败";
			ex.printStackTrace();
		}
		return msg;
	}


	public String saveTbPrj(Map map) throws Exception
	{
		// TODO Auto-generated method stub
		String sql2 = "DELETE FROM RP_XMJL WHERE XMBM='" + map.get("xmbm") + "'";
		dao.executeBySql(sql2);
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO RP_XMJL ");
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
		sql.append("'补填','','',''");
		sql.append(" FROM dual");
		return dao.executeBySql(sql.toString()) == 0 ? "失败" : "";
	}


	/**
	 * 保存项目申报功能科目数据
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String saveTbPrjSubject(String sYear, String rgCode, String[] subject, String chrId)
	{
		String msg = "";
		try
		{
			List list = new ArrayList();
			if (subject != null && subject.length > 0)
			{
				StringBuffer sqlDel = new StringBuffer();
				sqlDel.append("DELETE FROM RP_XMJL_KM WHERE SET_YEAR=" + sYear + " AND XMXH='" + chrId + "'");
				list.add(sqlDel.toString());
				for (int i = 0; i < subject.length; i++)
				{
					StringBuffer sqlAdd = new StringBuffer();
					sqlAdd.append("INSERT INTO RP_XMJL_KM(SET_YEAR,XHID,XMXH,KMDM) VALUES(");
					sqlAdd.append(sYear + ",NEWID,'" + chrId + "','" + subject[i] + "')");
					list.add(sqlAdd.toString());
				}
			}
			QueryStub.getQueryTool().executeBatch(list);
		}
		catch (Exception ex)
		{
			msg = "保存失败";
			ex.printStackTrace();
		}
		return msg;
	}


	/**
	 * 根据单位得到附件列表
	 * 
	 * @param enId
	 * @return
	 * @throws Exception
	 */
	public DataSet getFjFiles(String enId, String flowstatus) throws Exception
	{
		StringBuffer sb = new StringBuffer("select t.*,DECODE(CZ_SURE,NULL,'','√') CZ_SURE,DECODE(DW_SURE,NULL,'','√') DW_SURE ");
		sb.append("from RP_FJ_FILES t where (en_id,last_ver)");
		if ("002".equals(flowstatus))
			sb.append(" not ");
		sb.append("in( ");
		sb.append("select en_id,max(last_ver) from RP_FJ_FILES group by en_id) ");
		sb.append(enId).append(" order by t.en_id");
		return DBSqlExec.getDataSet(sb.toString());
	}
}
