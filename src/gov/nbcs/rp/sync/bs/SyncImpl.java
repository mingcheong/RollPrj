package gov.nbcs.rp.sync.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.sync.ibs.SyncInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;




public class SyncImpl implements SyncInterface
{
	public GeneralDAO dao;
	private List sqlBatchList;



	public GeneralDAO getDao()
	{
		return this.dao;
	}


	public void setDao(GeneralDAO dao)
	{
		this.dao = dao;
	}


	public List getSchemaData(String type, String setYear) throws Exception
	{
		String findsql = "select table_type as type , max(table_type_name) as name from rp_sort_table ";
		if ((type != null) && (!"".equals(type)))
		{
			findsql = findsql + " where table_type='" + type + "'";
		}

		// if ((setYear != null) && (!"".equals(setYear))) {
		// // if (findsql.indexOf("where") != -1)
		// // findsql = findsql + " and set_year='" + setYear + "'";
		// // else {
		// findsql = findsql + " ";
		// // }
		// }

		findsql = findsql + " group by table_type";
		return this.dao.findBySql(findsql);
	}


	public DataSet getTableData(String type) throws Exception
	{
		String findsql = "select * from rp_compare_table ";
		if ((type != null) && (!"".equals(type)))
		{
			findsql = findsql + " where table_id in (select table_id from rp_sort_table where table_type = " + type + ")";
		}

		DataSet ds = DataSet.create();
		ds.setSqlClause(findsql);
		ds.open();
		return ds;
	}


	public List getTableDataByType(String type) throws Exception
	{
		String findsql = "select * from rp_compare_table ";
		if ((type != null) && (!"".equals(type)))
		{
			findsql = findsql + " where table_id in (select table_id from rp_sort_table where table_type = " + type + ")";
		}

		return this.dao.findBySql(findsql);
	}


	public void execBatchSql(List sqlList) throws Exception
	{
		if ((sqlList != null) && (sqlList.size() > 0))
			QueryStub.getQueryTool().executeBatch(sqlList);
	}


	public void syncTableData(String setYear, String dblink, String direction, String exeFunc1, String exeFunc2, List list, String beforeSql, String endSql, String whereSql) throws Exception
	{
		if ((dblink == null) || ("".equals(dblink))) { throw new Exception("dblink名称为空！"); }
		if ((direction == null) || ("".equals(direction))) { throw new Exception("导出方向标识为空！"); }
		if ((exeFunc1 == null) || ("".equals(exeFunc1))) { throw new Exception("操作方式标识为空！"); }
		if ((exeFunc2 == null) || ("".equals(exeFunc2))) { throw new Exception("是否删除重复数据标识为空！"); }
		if ((list == null) || ("".equals(list))) { throw new Exception("无可同步表！"); }
		this.sqlBatchList = new ArrayList();
		if ((beforeSql != null) && (!"".equals(beforeSql)))
		{
			String[] bSql = beforeSql.split(";");
			if ((bSql != null) && (bSql.length > 0))
			{
				for (int i = 0; i < bSql.length; i++)
				{
					this.sqlBatchList.add(bSql[i]);
				}

			}

		}

		if ("0".equals(direction))
		{
			if ("0".equals(exeFunc1))
			{
				for (int i = 0; i < list.size(); i++)
				{
					Map tempMap = (Map) list.get(i);
					String tempRPname = (String) tempMap.get("table_ename");
					String tempPFname = (String) tempMap.get("pf_ename");

					this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink);
					this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
				}

			}

			if ("1".equals(exeFunc1))
			{
				if ("0".equals(exeFunc2))
				{
					for (int i = 0; i < list.size(); i++)
					{
						Map tempMap = (Map) list.get(i);
						String tempRPname = (String) tempMap.get("table_ename");
						String tempPFname = (String) tempMap.get("pf_ename");
						String tempRPpk = (String) tempMap.get("table_pk");
						String tempPFpk = (String) tempMap.get("pf_table_pk");

						if ((tempRPpk == null) || ("".equals(tempRPpk)))
						{
							new MessageBox("表 " + tempRPname + " 配置的主键为空！", 2, 1).show();
							return;
						}

						if ((tempPFpk == null) || ("".equals(tempPFpk)))
						{
							new MessageBox("表 " + tempPFname + " 配置的主键为空！", 2, 1).show();
							return;
						}

						this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink + "" + " t where t." + tempPFpk + " in  (select " + tempRPpk + " from " + tempRPname + " where 1=1 "
								+ whereSql + ")");
						this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
					}

				}

				if ("1".equals(exeFunc2))
				{
					for (int i = 0; i < list.size(); i++)
					{
						Map tempMap = (Map) list.get(i);
						String tempRPname = (String) tempMap.get("table_ename");
						String tempPFname = (String) tempMap.get("pf_ename");
						String tempRPpk = (String) tempMap.get("table_pk");
						String tempPFpk = (String) tempMap.get("pf_table_pk");
						this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " pf select * from " + tempRPname + " rp where rp." + tempRPpk + " not in (select " + tempPFpk + " from "
								+ tempPFname + "@" + dblink + " ) " + whereSql);
					}
				}
				else if ("2".equals(exeFunc2))
				{
					for (int i = 0; i < list.size(); i++)
					{
						Map tempMap = (Map) list.get(i);
						String tempRPname = (String) tempMap.get("table_ename");
						String tempPFname = (String) tempMap.get("pf_ename");
						String tempRPpk = (String) tempMap.get("table_pk");
						String tempPFpk = (String) tempMap.get("pf_table_pk");

						this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql + "");
						this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
					}

				}

			}

		}

		if ("1".equals(direction))
		{
			if ("0".equals(exeFunc1))
			{
				for (int i = 0; i < list.size(); i++)
				{
					Map tempMap = (Map) list.get(i);
					String tempRPname = (String) tempMap.get("table_ename");
					String tempPFname = (String) tempMap.get("pf_ename");
					this.sqlBatchList.add("delete from  " + tempRPname);
					this.sqlBatchList.add("insert into " + tempRPname + " select * from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql);
				}
			}

			if ("1".equals(exeFunc1))
			{
				if ("0".equals(exeFunc2))
				{
					for (int i = 0; i < list.size(); i++)
					{
						Map tempMap = (Map) list.get(i);
						String tempRPname = (String) tempMap.get("table_ename");
						String tempPFname = (String) tempMap.get("pf_ename");
						String tempRPpk = (String) tempMap.get("table_pk");
						String tempPFpk = (String) tempMap.get("pf_table_pk");
						this.sqlBatchList.add("delete from " + tempRPname + " where " + tempRPpk + " in  (select " + tempPFpk + "from " + tempPFname + "@" + dblink + " )");
						this.sqlBatchList.add("insert into " + tempRPname + " select * from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql);
					}
				}

				if ("1".equals(exeFunc2))
				{
					for (int i = 0; i < list.size(); i++)
					{
						Map tempMap = (Map) list.get(i);
						String tempRPname = (String) tempMap.get("table_ename");
						String tempPFname = (String) tempMap.get("pf_ename");
						String tempRPpk = (String) tempMap.get("table_pk");
						String tempPFpk = (String) tempMap.get("pf_table_pk");
						this.sqlBatchList.add("insert into " + tempRPname + " rp select * from " + tempPFname + "@" + dblink + " pf where pf." + tempPFpk + " not in (select " + tempRPpk + " from "
								+ tempRPname + ") " + whereSql);
					}
				}
			}
		}

		if ((endSql != null) && (!"".equals(endSql)))
		{
			String[] eSql = endSql.split(";");
			if ((eSql != null) && (eSql.length > 0))
			{
				for (int i = 0; i < eSql.length; i++)
				{
					this.sqlBatchList.add(eSql[i]);
				}
			}
		}
		execBatchSql(this.sqlBatchList);
	}


	public List getSchemeList(int code) throws Exception
	{
		if ((String.valueOf(code) == null) || ("".equals(String.valueOf(code)))) { return null; }
		String findsql = "select * from rp_scheme where scheme_code = " + String.valueOf(code);
		return this.dao.findBySql(findsql);
	}


	public synchronized void syncTableByType(String setYear, int typeCode, String whereSql) throws Exception
	{
		List tempList = getSchemeList(typeCode);
		if ((tempList == null) || (tempList.size() == 0)) { throw new Exception("未找到此编码方案，类型号：" + typeCode); }
		if (tempList.size() > 1) { throw new Exception("方案表返回多个方案，请管理员修改此类型配置，类型号：" + typeCode); }

		Map schemeMap = (Map) tempList.get(0);
		String direction = (String) schemeMap.get("sync_direction");
		String exeFunc1 = (String) schemeMap.get("sync_pro_function");
		String exeFunc2 = (String) schemeMap.get("sync_del_duplicate");
		String sortTableType = (String) schemeMap.get("sync_table_type");
		String dblinkName = (String) schemeMap.get("dblink_name");
		List schemaList = getSchemaData(sortTableType, setYear);
		List tableList = null;
		if ((schemaList != null) && (schemaList.size() > 0))
		{
			String type = (String) ((Map) schemaList.get(0)).get("type");
			tableList = getTableDataByType(type);
		}
		List updateList = new ArrayList();
		updateList.add(" update fb_p_base a set a.stock_flag = '是'  where " + " exists (select 1 from fb_p_base@" + dblinkName + " b " + "  where a.prj_code = b.prj_code and a.data_type=b.data_type "
				+ "  and a.batch_no=b.batch_no and b.stock_flag = '是') ");

		updateList.add(" update fb_p_detail_pfs a set a.stock_flag = '是' where  " + " exists (select 1 from fb_p_detail_pfs@" + dblinkName + " b "
				+ "    where a.prj_code = b.prj_code and a.data_type=b.data_type " + "    and a.batch_no=b.batch_no and b.stock_flag = '是') ");
		QueryStub.getQueryTool().executeBatch(updateList);

		syncTableData(setYear, dblinkName, direction, exeFunc1, exeFunc2, tableList, null, null, whereSql);

	}


	public synchronized void syncBudget1(String setYear, String batchNo, String dateType, List lBudgetData) throws Exception
	{
		List sqlList = new ArrayList();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "").replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);

		String sDblink = "";
		sDblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");
		String sCreateTempTable = "create table " + sTableName + "(div_code varchar2(40),prj_code varchar2(20),prj_name varchar2(100),gkdw_code varchar2(20))";

		dao.executeBySql(sCreateTempTable);
		String[] sData = null;
		for (int i = 0; i < lBudgetData.size(); i++)
		{
			String filter = lBudgetData.get(i).toString();
			sData = filter.split(",");
			String s = "insert into " + sTableName + "(div_code,prj_code,prj_name,gkdw_code)values('" + sData[0] + "','" + sData[1] + "','" + sData[2] + "','" + sData[3] + "')";
			dao.executeBySql(s);
		}

		StringBuffer sInsFb_baseSql = new StringBuffer();
		sInsFb_baseSql.append("insert into fb_p_base_p2");
		sInsFb_baseSql.append("(row_id,set_year,batch_no,data_type,ver_no,opt_no,data_src,");
		sInsFb_baseSql.append("data_attr,prj_type ,en_id,div_kind,div_code,div_name,mb_id,dept_code,dept_name ,");
		sInsFb_baseSql.append("prj_code,prj_name ,bs_id,acct_code,acct_name,prjsort_code,prjattr_code,");
		sInsFb_baseSql.append("prjaccord_code ,prjstd_code,stock_flag,stock_type,start_date,end_date,prj_content,");
		sInsFb_baseSql.append("prj_manager,sort_all,sort_charge,sort_div,prj_status,revisectl_code,report_flag,audit_status,");
		sInsFb_baseSql.append("isperennial,detail_degree,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,");
		sInsFb_baseSql.append("c19,c20,cl1,cl2,cl3,f1,f2,f3,f4,f5,n1,n2,n3,n4,n5,d1,d2,rg_code,last_ver,prj_parent,");
		sInsFb_baseSql.append("dep_status,fis_status,prj_transfer,ca1,ca2,ca3,ca4,ca5,cs1,cs2,cs3,cs4,cs5)");
		sInsFb_baseSql.append("(select");
		sInsFb_baseSql.append(" newid as row_id,set_year,'");
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
		sInsFb_baseSql.append(" from vw_fb_p_base_p2 r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		DataSet filter = DBSqlExec.getDataSet("select efield from rp_s_support where suptype='TOBUDGET'");
		String filterE = filter.fieldByName("efield").getValue().toString();

		StringBuffer sInsFb_deSql = new StringBuffer();
		sInsFb_deSql.append("insert into fb_p_detail_pfs_p2");
		sInsFb_deSql.append("(row_id,set_year,");
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
		sInsFb_deSql.append(" from vw_fb_p_detail_pfs_p2 r ");
		sInsFb_deSql.append(" where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		String sDelOldFb_baseSql = "delete from fb_p_base_p2 r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.prj_code=t.prj_code )";
		String sDelOldFb_deSql = "delete from fb_p_detail_pfs_p2 r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.prj_code=t.prj_code  )";

		StringBuffer sqlLink_delBase = new StringBuffer("delete from fb_p_base@").append(sDblink).append(" r where set_year='");
		sqlLink_delBase.append(setYear).append("' and batch_no='").append(batchNo).append("' and data_type='");
		sqlLink_delBase.append(dateType).append("' and exists (select 1 from ").append(sTableName).append(" t where r.prj_code=t.prj_code)");

		StringBuffer sqlLink_Deldetail = new StringBuffer("delete from fb_p_detail_pfs@").append(sDblink).append(" r where set_year='");
		sqlLink_Deldetail.append(setYear).append("' and batch_no='").append(batchNo).append("' and data_type='");
		sqlLink_Deldetail.append(dateType).append("' and exists (select 1 from ").append(sTableName).append(" t where r.prj_code=t.prj_code)");

		StringBuffer sqlLink_base = new StringBuffer("insert into fb_p_base@").append(sDblink);
		sqlLink_base.append("(select row_id,set_year,batch_no,data_type,ver_no,opt_no,data_src,data_attr,prj_type,en_id,div_kind,div_code,");
		sqlLink_base.append("div_name,mb_id,dept_code,dept_name,prj_code,prj_name,bs_id,acct_code,acct_name,prjsort_code,prjattr_code,");
		sqlLink_base.append("prjaccord_code,prjstd_code,stock_flag,stock_type,start_date,end_date,prj_content,prj_manager,sort_all,");
		sqlLink_base.append("sort_charge,sort_div,prj_status,audit_status,isperennial,revisectl_code,report_flag,detail_degree,c1,c2,");
		sqlLink_base.append("c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,cl1,cl2,cl3,f1,f2,f3,f4,f5,n1,n2,n3,n4,n5,");
		sqlLink_base.append("d1,d2,rg_code,last_ver,prj_parent,dep_status,fis_status,prj_transfer,ca1,ca2,ca3,ca4,ca5,cs1,cs2,cs3,cs4,cs5");
		sqlLink_base.append(" from fb_p_base_p2 r where r.set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		StringBuffer sqlLink_detail = new StringBuffer("insert into fb_p_detail_pfs@").append(sDblink);
		sqlLink_detail.append("(select * from fb_p_detail_pfs_p2 r where r.set_year='" + setYear + "' and exists (select 1 from " + sTableName
				+ " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");
		sqlList.add(sDelOldFb_baseSql);
		sqlList.add(sDelOldFb_deSql);
		sqlList.add(sqlLink_delBase.toString());
		sqlList.add(sqlLink_Deldetail.toString());

		sqlList.add(sInsFb_baseSql.toString());
		sqlList.add(sInsFb_deSql.toString());
		sqlList.add(sqlLink_base.toString());
		sqlList.add(sqlLink_detail.toString());
		QueryStub.getQueryTool().executeBatch(sqlList);
		String sUpdateFb = "update fb_u_payout_budget@" + sDblink + " a set a.stock_flag = 1 where exists(select 1 " + "from fb_u_payout_gov_purchase@" + sDblink + " b where a.project_code ="
				+ "b.project_code and a.batch_no ='" + batchNo + "' and a.data_type='" + dateType + "')";
		dao.executeBySql(sUpdateFb);
		dao.executeBySql("drop table " + sTableName);
	}


	public synchronized void syncBudget2(String setYear, String batchNo, String dateType, List lBudgetData) throws Exception
	{
		List sqlList = new ArrayList();
		String sTableName = UUID.randomUUID().toString();
		sTableName = sTableName.replaceAll("-", "").replaceAll("\\{", "").replaceAll("\\}", "");
		sTableName = "temp_" + sTableName.substring(0, 16);

		String sDblink = "";
		sDblink = DBSqlExec.getStringValue("select dblink_name from rp_scheme");
		String sCreateTempTable = "create table " + sTableName + "(div_code varchar2(40),prj_code varchar2(20),prj_name varchar2(100),gkdw_code varchar2(20))";

		dao.executeBySql(sCreateTempTable);
		String[] sData = null;
		for (int i = 0; i < lBudgetData.size(); i++)
		{
			String filter = lBudgetData.get(i).toString();
			sData = filter.split(",");
			String s = "insert into " + sTableName + "(div_code,prj_code,prj_name,gkdw_code)values('" + sData[0] + "','" + sData[1] + "','" + sData[2] + "','" + sData[3] + "')";
			dao.executeBySql(s);
		}

		StringBuffer sInsFb_baseSql = new StringBuffer();
		sInsFb_baseSql.append("insert into fb_p_base");
		sInsFb_baseSql.append("(row_id,set_year,batch_no,data_type,ver_no,opt_no,data_src,");
		sInsFb_baseSql.append("data_attr,prj_type ,en_id,div_kind,div_code,div_name,mb_id,dept_code,dept_name ,");
		sInsFb_baseSql.append("prj_code,prj_name ,bs_id,acct_code,acct_name,prjsort_code,prjattr_code,");
		sInsFb_baseSql.append("prjaccord_code ,prjstd_code,stock_flag,stock_type,start_date,end_date,prj_content,");
		sInsFb_baseSql.append("prj_manager,sort_all,sort_charge,sort_div,prj_status,revisectl_code,report_flag,audit_status,");
		sInsFb_baseSql.append("isperennial,detail_degree,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,");
		sInsFb_baseSql.append("c19,c20,cl1,cl2,cl3,f1,f2,f3,f4,f5,n1,n2,n3,n4,n5,d1,d2,rg_code,last_ver,prj_parent,");
		sInsFb_baseSql.append("dep_status,fis_status,prj_transfer,ca1,ca2,ca3,ca4,ca5,cs1,cs2,cs3,cs4,cs5)");
		sInsFb_baseSql.append("(select");
		sInsFb_baseSql.append(" newid as row_id,set_year,'");
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
		sInsFb_baseSql.append(" from vw_fb_p_base r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		DataSet filter = DBSqlExec.getDataSet("select efield from rp_s_support where suptype='TOBUDGET'");
		String filterE = filter.fieldByName("efield").getValue().toString();

		StringBuffer sInsFb_deSql = new StringBuffer();
		sInsFb_deSql.append("insert into fb_p_detail_pfs");
		sInsFb_deSql.append("(row_id,set_year,");
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
		sInsFb_deSql.append(" where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ) and r.detail_type not in ('111', '222', '333') and r.c6 in ('002')) ");

		String sDelOldFb_baseSql = "delete from fb_p_base r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.prj_code=t.prj_code )";
		String sDelOldFb_deSql = "delete from fb_p_detail_pfs r where set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.prj_code=t.prj_code  )";

		StringBuffer sqlLink_delBase = new StringBuffer("delete from fb_p_base@").append(sDblink).append(" r where set_year='");
		sqlLink_delBase.append(setYear).append("' and batch_no='").append(batchNo).append("' and data_type='");
		sqlLink_delBase.append(dateType).append("' and exists (select 1 from ").append(sTableName).append(" t where r.prj_code=t.prj_code)");

		StringBuffer sqlLink_Deldetail = new StringBuffer("delete from fb_p_detail_pfs@").append(sDblink).append(" r where set_year='");
		sqlLink_Deldetail.append(setYear).append("' and batch_no='").append(batchNo).append("' and data_type='");
		sqlLink_Deldetail.append(dateType).append("' and exists (select 1 from ").append(sTableName).append(" t where r.prj_code=t.prj_code)");

		StringBuffer sqlLink_base = new StringBuffer("insert into fb_p_base@").append(sDblink);
		sqlLink_base.append("(select row_id,set_year,batch_no,data_type,ver_no,opt_no,data_src,data_attr,prj_type,en_id,div_kind,div_code,");
		sqlLink_base.append("div_name,mb_id,dept_code,dept_name,prj_code,prj_name,bs_id,acct_code,acct_name,prjsort_code,prjattr_code,");
		sqlLink_base.append("prjaccord_code,prjstd_code,stock_flag,stock_type,start_date,end_date,prj_content,prj_manager,sort_all,");
		sqlLink_base.append("sort_charge,sort_div,prj_status,audit_status,isperennial,revisectl_code,report_flag,detail_degree,c1,c2,");
		sqlLink_base.append("c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,cl1,cl2,cl3,f1,f2,f3,f4,f5,n1,n2,n3,n4,n5,");
		sqlLink_base.append("d1,d2,rg_code,last_ver,prj_parent,dep_status,fis_status,prj_transfer,ca1,ca2,ca3,ca4,ca5,cs1,cs2,cs3,cs4,cs5");
		sqlLink_base.append(" from fb_p_base r where r.set_year='" + setYear + "' and exists (select 1 from " + sTableName + " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");

		StringBuffer sqlLink_detail = new StringBuffer("insert into fb_p_detail_pfs@").append(sDblink);
		sqlLink_detail.append("(select * from fb_p_detail_pfs r where r.set_year='" + setYear + "' and exists (select 1 from " + sTableName
				+ " t where r.div_code=t.div_code and r.prj_code=t.prj_code ))");
		sqlList.add(sDelOldFb_baseSql);
		sqlList.add(sDelOldFb_deSql);
		sqlList.add(sqlLink_delBase.toString());
		sqlList.add(sqlLink_Deldetail.toString());

		sqlList.add(sInsFb_baseSql.toString());
		sqlList.add(sInsFb_deSql.toString());
		sqlList.add(sqlLink_base.toString());
		sqlList.add(sqlLink_detail.toString());
		QueryStub.getQueryTool().executeBatch(sqlList);
		String sUpdateFb = "update fb_u_payout_budget@" + sDblink + " a set a.stock_flag = 1 where exists(select 1 " + "from fb_u_payout_gov_purchase@" + sDblink + " b where a.project_code ="
				+ "b.project_code and a.batch_no ='" + batchNo + "' and a.data_type='" + dateType + "')";
		dao.executeBySql(sUpdateFb);
		dao.executeBySql("drop table " + sTableName);
	}


	public DataSet getScronyTableData(String type) throws Exception
	{
		String findsql = "select * from rp_s_scronytab order by table_num ";
		DataSet ds = DataSet.create();
		ds.setSqlClause(findsql);
		ds.open();
		return ds;
	}


	public void scronyNextyear(String setYear, String rgCode, String[] tablename) throws Exception
	{
		List listds = new ArrayList();

		int year = Integer.valueOf(setYear).intValue() + 1;
		String str2 = "";
		String str = "";
		String str3 = "";
		for (int i = 0; i < tablename.length; i++)
		{
			if (tablename[i].substring(0, 3).equalsIgnoreCase("ELE"))
			{
				str2 = "SET_YEAR";
				str = year + "";
				str3 = "'SET_YEAR'";
			}
			else
			{
				str2 = "SET_YEAR";
				str = year + "";
				str3 = "'SET_YEAR'";
			}

			List list = this.dao.findBySql("select column_name from user_tab_columns where table_name = '" + tablename[i] + "' and column_name not in(" + str3 + ")");
			Map mm = new HashMap();

			for (int j = 0; j < list.size(); j++)
			{
				mm = (Map) list.get(j);

				str = str + "," + mm.get("column_name");
				str2 = str2 + "," + mm.get("column_name");
			}

			String sqlp = "delete  from " + tablename[i] + " where set_year=" + year + " ";
			String sql = "insert into " + tablename[i] + "(" + str2 + ")(select " + str + " from " + tablename[i] + "  where set_year='" + setYear + "')";
			StringBuffer sqlu = new StringBuffer();
			// sqlu.append("update " + tablename[i] + " a set
			// a.parent_id=(select chr_id from " + tablename[i] + " where
			// ").append("chr_code=a.chr_code1 and a.set_year=set_year and
			// a.is_leaf=1),a.chr_id1=(select chr_id ").append("from " +
			// tablename[i] + " where chr_code=a.chr_code1 and
			// ").append("a.set_year=set_year),a.chr_id2=(select chr_id from " +
			// tablename[i] + " where ").append("chr_code=a.chr_code and
			// a.set_year=set_year and a.is_leaf=1) where set_year ='" + year +
			// "'");
			listds.add(sqlp.toString());
			listds.add(sql.toString());
			if (tablename[i].substring(0, 3).equalsIgnoreCase("ELE"))
				listds.add(sqlu.toString());
		}
		QueryStub.getQueryTool().executeBatch(listds);
	}


	public void Sametablebyyear(String setYear, String rgCode, String[] tablename) throws Exception
	{
		// 同步表年度

	}

}
