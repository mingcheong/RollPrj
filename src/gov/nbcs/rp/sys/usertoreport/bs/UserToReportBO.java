package gov.nbcs.rp.sys.usertoreport.bs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.sessionmanager.SessionUtil;


public class UserToReportBO implements IUserToReport {
	public GeneralDAO dao;
	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/*
	 * 获得所有用户 
	 */
	public DataSet getAllUser() throws Exception {
		return null;
	}
	/**
	 * 加载所有用户
	 * @return
	 */
	public List loadAllUser() throws Exception {
//			String sqlSelect = "SELECT  user_id,user_code ,user_name ,is_leaf,belong_type FROM vw_fb_user where belong_type ='"+UntPub.FIS_VIS+"' ORDER BY user_code";

		String sqlSelect = "SELECT  user_id,user_code ,user_name ,is_leaf,belong_type FROM vw_fb_user where belong_org in (select org_id from sys_user_org where user_id='"+SessionUtil.getUserInfoContext().getUserID()+"') ORDER BY user_code";
		List list = dao.findBySql(sqlSelect);
//		for (Iterator iter = list.iterator(); iter.hasNext();) {
//			Map map = (Map) iter.next();
//			map.put("chr_id", map.get("user_id"));
//			map.put("chr_code", map.get("user_code"));
//			map.put("chr_name", map.get("user_name"));
//			map.put("chr_is_leaf", map.get("is_leaf"));
//		}
		return list;
	}

	/* 
	 * 获得当前的报表
	 */
	public DataSet getReport(String data_user) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql =new StringBuffer();
		sql.append("select key_id,report_id,par_id,report_name,data_user,"+SessionUtil.getLoginYear()+",lvl_ID from ")
		   .append("(")
		   .append(" select code as report_ID ,code as key_ID ,'' as par_id, name as report_name,2 as Data_user,set_year ,'D'||lvl_ID as LVL_ID from fb_s_pubcode where Typeid ='QUERYTYPE' ")
		   .append(" union all select a.report_id,type_code||a.report_id as key_ID, type_code as par_id,report_cname,data_user,a.set_year,lvl_ID from fb_u_qr_repset a, fb_u_qr_report_to_type b" );
		if(!"001".equals(SessionUtil.getUserInfoContext().getOrgType()))   	
	    sql.append(", fb_u_usertoreport d" );
		sql.append(	" where a.report_id=b.report_id ");
		if(!"001".equals(SessionUtil.getUserInfoContext().getOrgType()))  
		sql.append(" and  a.report_id=d.report_id and d.user_id ='"+SessionUtil.getUserInfoContext().getUserID()+"'");
		
		sql.append(" union all select 'LogSend' as report_id, 'LogSend' as key_id,'' as par_id, '下发日志' as report_name, 9 as Data_user , "+SessionUtil.getLoginYear()+",'Z00001' from dual ")
           .append(" union all select 'LogIncept' as report_id, 'LogIncept'  as key_id,'' as par_id, '上报日志' as report_name, 9 as Data_user , "+SessionUtil.getLoginYear()+",'Z00001' from dual")
           

		     .append(" ) where data_user in "+data_user);
		
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sql.toString(), ds);
		
		return ds;
	}

	/* 
	 *	保存处室对单位的信息
	 */
	public void saveUserToReport(DataSet ds, String user_id) throws Exception {
		// TODO Auto-generated method stub
		deleteInfoByUserId(user_id);
		ds.post();
	}
	/**
	 * 删除以前的信息
	 * @param user_id
	 * @throws Exception
	 */
	private void deleteInfoByUserId(String user_id) throws Exception
	{
		String sql="delete from fb_u_usertoreport where user_id='"+user_id+"' and set_year="+SessionUtil
		.getUserInfoContext().getSetYear();
		QueryStub.getQueryTool().executeUpdate(sql);
	}

	/* 
	 *保存用户对报表
	 * @see gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport#updataUser2Report(java.util.List, java.util.List)
	 */
	public void updataUser2Report(List userId, List sql) throws Exception {
		// TODO Auto-generated method stub
		String user="";
		for(int i=0;i<userId.size();i++)
		{
			user=(String)userId.get(i);
			deleteInfoByUserId(user);
		}
		QueryStub.getQueryTool().executeBatch(sql);
	}
	/**
	  得到已经存储的记录
	 * @param user_id
	 * @return
	 * @throws Exception
	 * 
	 */
	public List getHaveSavedRecodeByUserId(String user_id) throws Exception {
		String sql="select a.report_type, a.user_id,a.report_id,a.set_year,a.rg_code,a.last_ver from fb_u_usertoreport a where a.user_id=? " +
		" and  set_year=? ";
		return dao.findBySql(sql,new Object[]{user_id,SessionUtil
				.getUserInfoContext().getSetYear()});		
	}
	
}
