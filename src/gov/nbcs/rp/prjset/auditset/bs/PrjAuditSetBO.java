package gov.nbcs.rp.prjset.auditset.bs;

import com.foundercy.pf.util.dao.springdao.GeneralDAO;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.prjset.auditset.ibs.IPrjAuditSet;

public class PrjAuditSetBO implements IPrjAuditSet{
	
	private GeneralDAO dao;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * 审核配置界面获取元素数据集
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getAuditElement(boolean isUser, String dep_id)
			throws Exception {
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		if (!isUser) {
			sql.append("select * from (");
			sql
					.append(" select '0' as code,'用户' as name,'' as par_id from dual");
			sql.append(" union all");
			sql
					.append(" select 'dep'||dep_code as code,dep_name as name,'0' as par_id from vw_fb_department");
			sql.append(" union all");
			sql
					.append(" select user_code,'['||user_code||']'||user_name,'dep'||(select distinct dep_code from vw_fb_department a ");
			sql.append(" where a.Dep_ID = b.BELONG_ORG) from vw_fb_user b");
			sql.append(" where belong_type = '007' and belong_org is not null");
			sql.append(" union all");
			sql.append(" select '1' as code,'业务处室','' from dual");
			sql.append(" union all");
			// sql.append(" select '-1','业务处室','1' from dual");
			// sql.append(" union all");
			sql
					.append(" select dep_code,'['||dep_code||']'||dep_name,'1' from vw_fb_department");
			sql.append(" ) order by par_id,code");
		} else {
			if (Common.isNullStr(dep_id)) {
				sql
						.append(" select 'dep'||dep_code as code,dep_name as name,'0' as par_id,dep_id from vw_fb_department");
				sql.append(" union all");
			}
			sql
					.append(" select user_code,'['||user_code||']'||user_name as name ,'dep'||(select dep_code from vw_fb_department a ");
			sql
					.append(" where a.Dep_ID = b.BELONG_ORG) as par_id,belong_org from vw_fb_user b");
			sql.append(" where belong_type = '007' and belong_org is not null");
			if (!Common.isNullStr(dep_id)) {
				sql.append(" and belong_org='" + dep_id + "'");
			}
		}
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}
	
	public void dsPost(DataSet ds) throws Exception {
//		if (IPrjAppr.DetailInfo.TableName.equalsIgnoreCase(ds.getName())) {
//			ds.setPrimarykey(new String[] { IPrjAppr.DetailInfo.Index.PARCODE,
//					IPrjAppr.DetailInfo.Index.DETAILCODE,
//					IPrjAppr.DetailInfo.Index.SortCode,
//					IPrjAppr.DetailInfo.Index.SETYEAR, "DIV_CODE", "BATCH_NO",
//					"DATA_TYPE" });
//		}
//		if (IPrjAppr.AuditNewInfo.EXPERT.TableName.equals(ds.getName())) {
//			ds.setPrimarykey(new String[] {
//					IPrjAppr.AuditNewInfo.EXPERT.ROW_ID,
//					IPrjAppr.AuditNewInfo.EXPERT.SET_YEAR });
//		}
//		if (IPrjAppr.AuditNewInfo.AudtiStep.TableName.equals(ds.getName())) {
//			ds.setPrimarykey(new String[] { IPrjAppr.AuditNewInfo.WID,
//					IPrjAppr.AuditNewInfo.SET_YEAR });
//		}
		ds.setPrimarykey(new String[]{"STEP_ID"});
		ds.post();
	}

	public DataSet getAuditStepInfo() throws Exception {
		DataSet ds = DataSet.create();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from rp_s_audit_step order by step_id");
		DBSqlExec.getDataSet(sql.toString(), ds);
		return ds;
	}
}
