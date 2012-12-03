package gov.nbcs.rp.sys.sysrefcol.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 引用列管理
 * 

 * 
 */
public class SysRefColBO implements ISysRefCol {

	public DataSet getRefColRecord(String setYear) throws Exception {
		String sSql = "select '['||refcol_id||'] '||refcol_name as name ,a.* from fb_s_refcol a "
				+ " where set_year=" + setYear + " order by refcol_id";
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public void exeSql(String sSql) throws Exception {
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
	}

	public DataSet exeSqlDs(String sSql) throws Exception {
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public DataSet getDisplayFormat(String setYear) throws Exception {
		String sSql = "select name,cvalue from fb_s_pubcode  where typeID='DQRDISPLAY' and set_year = "
				+ setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public DataSet getReservedField(String setYear) throws Exception {
		String sSql = "select FIELD_CNAME,FIELD_ENAME,DATA_TYPE "
				+ "from FB_S_RESERVED_FIELD where set_year = " + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public TableColumnInfo[] getFieldInfo(String sSql) throws Exception {
		return QueryStub.getQueryTool().getColumnInfoBySQL(sSql);
	}

	public DataSet getRefColDetailWithRefColId(String RefCol_id, String setYear)
			throws Exception {
		String sSql = "select * from fb_s_refcol_detail where refcol_id = '"
				+ RefCol_id + "' and  set_year = " + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public void saveRefCol(DataSet dsRefCol, DataSet dsRefColDetail,
			DataSet dsRefColRelatable) throws Exception {
		dsRefCol.setName("fb_s_refcol");
		dsRefCol.post();
		dsRefColDetail.post();
		dsRefColRelatable.post();

	}

	public DataSet getRefColRelatable(String RefCol_Id, String setYear)
			throws Exception {
		String sSql = "select * from fb_s_refcol_relatable where refcol_id = '"
				+ RefCol_Id + "' and  set_year = " + setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		return ds;
	}

	public void DeleteRefCol(String sRefColId, String setYear) throws Exception {
		String sSql = "delete fb_s_refcol where refcol_id = '" + sRefColId
				+ "' and set_year =" + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
		sSql = "delete fb_s_refcol_detail where refcol_id = '" + sRefColId
				+ "' and set_year =" + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
		sSql = "delete fb_s_refcol_relatable where refcol_id = '" + sRefColId
				+ "' and set_year =" + setYear;
		QueryStub.getQueryTool().executeUpdate(sSql);
	}

	public InfoPackage checkRefColUsed(String sRefColId, String setYear)
			throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sSql = "select code,cvalue from fb_s_PubCode where typeid='REFCOLSTRUTABLE' and set_year="
				+ setYear;
		DataSet ds = DataSet.create();
		DBSqlExec.getDataSet(sSql, ds);
		if (ds.isEmpty()) {
			infoPackage.setsMessage("未取到使用引用表和字段的系统定义信息");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		ds.beforeFirst();
		String sFilter;
		while (ds.next()) {
			sFilter = " set_year = " + setYear + " and "
					+ ds.fieldByName("cvalue").getString() + " like '"
					+ sRefColId + "%'";
			if (DBSqlExec.getRecordCount(ds.fieldByName("code").getString(),
					sFilter) > 0) {
				infoPackage.setsMessage("该引用列已经被使用，不允许更改或删除!");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 检查名称填写是否重复
	 * 
	 * @param sName名称
	 * @param sCode编码，增加时传null,修改时填当前记录的refcol_id
	 * @param setYear
	 * @return true:未被使用,false：已被使用
	 * @throws Exception
	 */
	public boolean checkRefNameUsed(String sName, String sCode, String setYear)
			throws Exception {
		String sFilter = " refcol_name ='" + sName + "' and set_year = "
				+ setYear;
		if (sCode != null) {
			sFilter = sFilter + " and refcol_id <>'" + sCode + "'";
		}
		if (DBSqlExec.getRecordCount("fb_s_refcol", sFilter) > 0)
			return false;
		return true;
	}

	public String replaceRefColFixFlag(String sSql) throws Exception {
		String sSqlTmp = sSql;
		sSqlTmp = sSqlTmp.replaceAll("#SET_YEAR#", SessionUtil
				.getUserInfoContext().getSetYear());
//		sSqlTmp = sSqlTmp.replaceAll("#BATCH_NO#", String
//				.valueOf(PubInterfaceStub.getServerMethod().getCurBatchNO()));
		return sSqlTmp;
	}
}
