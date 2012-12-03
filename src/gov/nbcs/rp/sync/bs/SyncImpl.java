package gov.nbcs.rp.sync.bs;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Query;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.sync.ibs.SyncInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncImpl
  implements SyncInterface
{
  public GeneralDAO dao;
  private List sqlBatchList;

  public GeneralDAO getDao()
  {
    return this.dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public List getSchemaData(String type, String setYear) throws Exception {
    String findsql = "select table_type as type , max(table_type_name) as name from rp_sort_table ";
    if ((type != null) && (!"".equals(type))) {
      findsql = findsql + " where table_type='" + type + "'";
    }

//    if ((setYear != null) && (!"".equals(setYear))) {
////      if (findsql.indexOf("where") != -1)
////        findsql = findsql + " and set_year='" + setYear + "'";
////      else {
//        findsql = findsql + " ";
////      }
//    }

    findsql = findsql + " group by table_type";
    return this.dao.findBySql(findsql);
  }

  public DataSet getTableData(String type) throws Exception {
    String findsql = "select * from rp_compare_table ";
    if ((type != null) && (!"".equals(type))) {
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
    if ((type != null) && (!"".equals(type))) {
      findsql = findsql + " where table_id in (select table_id from rp_sort_table where table_type = " + type + ")";
    }

    return this.dao.findBySql(findsql);
  }

  public void execBatchSql(List sqlList)
    throws Exception
  {
    if ((sqlList != null) && (sqlList.size() > 0))
      QueryStub.getQueryTool().executeBatch(sqlList);
  }

  public void syncTableData(String setYear, String dblink, String direction, String exeFunc1, String exeFunc2, List list, String beforeSql, String endSql, String whereSql)
    throws Exception
  {
    if ((dblink == null) || ("".equals(dblink))) {
	      throw new Exception("dblink名称为空！");
    }
    if ((direction == null) || ("".equals(direction))) {
      throw new Exception("导出方向标识为空！");
    }
    if ((exeFunc1 == null) || ("".equals(exeFunc1))) {
      throw new Exception("操作方式标识为空！");
    }
    if ((exeFunc2 == null) || ("".equals(exeFunc2))) {
      throw new Exception("是否删除重复数据标识为空！");
    }
    if ((list == null) || ("".equals(list))) {
      throw new Exception("无可同步表！");
    }
    this.sqlBatchList = new ArrayList();
    if ((beforeSql != null) && (!"".equals(beforeSql))) {
      String[] bSql = beforeSql.split(";");
      if ((bSql != null) && (bSql.length > 0)) {
        for (int i = 0; i < bSql.length; i++) {
          this.sqlBatchList.add(bSql[i]);
        }

      }

    }

    if ("0".equals(direction))
    {
      if ("0".equals(exeFunc1)) {
        for (int i = 0; i < list.size(); i++) {
          Map tempMap = (Map)list.get(i);
          String tempRPname = (String)tempMap.get("table_ename");
          String tempPFname = (String)tempMap.get("pf_ename");

          this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink);
          this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
        }

      }

      if ("1".equals(exeFunc1))
      {
        if ("0".equals(exeFunc2)) {
          for (int i = 0; i < list.size(); i++) {
            Map tempMap = (Map)list.get(i);
            String tempRPname = (String)tempMap.get("table_ename");
            String tempPFname = (String)tempMap.get("pf_ename");
            String tempRPpk = (String)tempMap.get("table_pk");
            String tempPFpk = (String)tempMap.get("pf_table_pk");

            if ((tempRPpk == null) || ("".equals(tempRPpk))) {
              new MessageBox("表 " + tempRPname + " 配置的主键为空！", 2, 1).show();
              return;
            }

            if ((tempPFpk == null) || ("".equals(tempPFpk))) {
              new MessageBox("表 " + tempPFname + " 配置的主键为空！", 2, 1).show();
              return;
            }

            this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink + "" + " t where t." + tempPFpk + " in  (select " + tempRPpk + " from " + tempRPname + " where 1=1 " + whereSql + ")");
            this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
          }

        }

        if ("1".equals(exeFunc2)) {
          for (int i = 0; i < list.size(); i++) {
            Map tempMap = (Map)list.get(i);
            String tempRPname = (String)tempMap.get("table_ename");
            String tempPFname = (String)tempMap.get("pf_ename");
            String tempRPpk = (String)tempMap.get("table_pk");
            String tempPFpk = (String)tempMap.get("pf_table_pk");
            this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " pf select * from " + tempRPname + " rp where rp." + tempRPpk + " not in (select " + tempPFpk + " from " + tempPFname + "@" + dblink + " ) " + whereSql);
          }
        }
        else if ("2".equals(exeFunc2))
        {
          for (int i = 0; i < list.size(); i++) {
            Map tempMap = (Map)list.get(i);
            String tempRPname = (String)tempMap.get("table_ename");
            String tempPFname = (String)tempMap.get("pf_ename");
            String tempRPpk = (String)tempMap.get("table_pk");
            String tempPFpk = (String)tempMap.get("pf_table_pk");

            this.sqlBatchList.add("delete from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql + "");
            this.sqlBatchList.add("insert into " + tempPFname + "@" + dblink + " select * from " + tempRPname + " where 1=1 " + whereSql);
          }

        }

      }

    }

    if ("1".equals(direction))
    {
      if ("0".equals(exeFunc1)) {
        for (int i = 0; i < list.size(); i++) {
          Map tempMap = (Map)list.get(i);
          String tempRPname = (String)tempMap.get("table_ename");
          String tempPFname = (String)tempMap.get("pf_ename");
          this.sqlBatchList.add("delete from  " + tempRPname);
          this.sqlBatchList.add("insert into " + tempRPname + " select * from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql);
        }
      }

      if ("1".equals(exeFunc1))
      {
        if ("0".equals(exeFunc2)) {
          for (int i = 0; i < list.size(); i++) {
            Map tempMap = (Map)list.get(i);
            String tempRPname = (String)tempMap.get("table_ename");
            String tempPFname = (String)tempMap.get("pf_ename");
            String tempRPpk = (String)tempMap.get("table_pk");
            String tempPFpk = (String)tempMap.get("pf_table_pk");
            this.sqlBatchList.add("delete from " + tempRPname + " where " + tempRPpk + " in  (select " + tempPFpk + "from " + tempPFname + "@" + dblink + " )");
            this.sqlBatchList.add("insert into " + tempRPname + " select * from " + tempPFname + "@" + dblink + " where 1=1 " + whereSql);
          }
        }

        if ("1".equals(exeFunc2)) {
          for (int i = 0; i < list.size(); i++) {
            Map tempMap = (Map)list.get(i);
            String tempRPname = (String)tempMap.get("table_ename");
            String tempPFname = (String)tempMap.get("pf_ename");
            String tempRPpk = (String)tempMap.get("table_pk");
            String tempPFpk = (String)tempMap.get("pf_table_pk");
            this.sqlBatchList.add("insert into " + tempRPname + " rp select * from " + tempPFname + "@" + dblink + " pf where pf." + tempPFpk + " not in (select " + tempRPpk + " from " + tempRPname + ") " + whereSql);
          }
        }
      }
    }

    if ((endSql != null) && (!"".equals(endSql))) {
      String[] eSql = endSql.split(";");
      if ((eSql != null) && (eSql.length > 0)) {
        for (int i = 0; i < eSql.length; i++) {
          this.sqlBatchList.add(eSql[i]);
        }
      }
    }
    execBatchSql(this.sqlBatchList);
  }

  public List getSchemeList(int code) throws Exception {
    if ((String.valueOf(code) == null) || ("".equals(String.valueOf(code)))) {
      return null;
    }
    String findsql = "select * from rp_scheme where scheme_code = " + String.valueOf(code);
    return this.dao.findBySql(findsql);
  }

  public synchronized void syncTableByType(String setYear, int typeCode, String whereSql) throws Exception
  {
    List tempList = getSchemeList(typeCode);
    if ((tempList == null) || (tempList.size() == 0)) {
      throw new Exception("未找到此编码方案，类型号：" + typeCode);
    }
    if (tempList.size() > 1) {
      throw new Exception("方案表返回多个方案，请管理员修改此类型配置，类型号：" + typeCode);
    }

    Map schemeMap = (Map)tempList.get(0);
    String direction = (String)schemeMap.get("sync_direction");
    String exeFunc1 = (String)schemeMap.get("sync_pro_function");
    String exeFunc2 = (String)schemeMap.get("sync_del_duplicate");
    String sortTableType = (String)schemeMap.get("sync_table_type");
    String dblinkName = (String)schemeMap.get("dblink_name");
    List schemaList = getSchemaData(sortTableType, setYear);
    List tableList = null;
    if ((schemaList != null) && (schemaList.size() > 0)) {
      String type = (String)((Map)schemaList.get(0)).get("type");
      tableList = getTableDataByType(type);
    }
    List updateList = new ArrayList();
    updateList.add(" update fb_p_base a set a.stock_flag = '是'  where "
    		+" exists (select 1 from fb_p_base@"+dblinkName+" b "
    		  +     "  where a.prj_code = b.prj_code and a.data_type=b.data_type "
    		      +   "  and a.batch_no=b.batch_no and b.stock_flag = '是') ");

    updateList.add( " update fb_p_detail_pfs a set a.stock_flag = '是' where  "
    		+ " exists (select 1 from fb_p_detail_pfs@"+dblinkName+" b "
    		 +     "    where a.prj_code = b.prj_code and a.data_type=b.data_type "
    		   +     "    and a.batch_no=b.batch_no and b.stock_flag = '是') "
    		         );
    QueryStub.getQueryTool().executeBatch(updateList);
    
  
    
    syncTableData(setYear, dblinkName, direction, exeFunc1, exeFunc2, tableList, null, null, whereSql);
  
  
  }

  public DataSet getScronyTableData(String type)
    throws Exception
  {
    String findsql = "select * from rp_s_scronytab order by table_num ";
    DataSet ds = DataSet.create();
    ds.setSqlClause(findsql);
    ds.open();
    return ds;
  }

  public void scronyNextyear(String setYear, String rgCode, String[] tablename) throws Exception {
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
        mm = (Map)list.get(j);

        str = str + "," + mm.get("column_name");
        str2 = str2 + "," + mm.get("column_name");
      }

      String sqlp = "delete  from " + tablename[i] + " where set_year=" + year + " ";
      String sql = "insert into " + tablename[i] + "(" + str2 + ")(select " + str + " from " + tablename[i] + "  where set_year='" + setYear + "')";
      StringBuffer sqlu = new StringBuffer();
//      sqlu.append("update " + tablename[i] + "  a set a.parent_id=(select chr_id from " + tablename[i] + " where ").append("chr_code=a.chr_code1 and a.set_year=set_year and a.is_leaf=1),a.chr_id1=(select chr_id ").append("from " + tablename[i] + " where chr_code=a.chr_code1  and  ").append("a.set_year=set_year),a.chr_id2=(select chr_id from " + tablename[i] + " where ").append("chr_code=a.chr_code  and  a.set_year=set_year and a.is_leaf=1) where set_year ='" + year + "'");
      listds.add(sqlp.toString());
      listds.add(sql.toString());
      if (tablename[i].substring(0, 3).equalsIgnoreCase("ELE"))
        listds.add(sqlu.toString());
    }
    QueryStub.getQueryTool().executeBatch(listds);
  }

  public void Sametablebyyear(String setYear, String rgCode, String[] tablename)
		throws Exception {
	// 同步表年度
	
	}
  
  
  
}