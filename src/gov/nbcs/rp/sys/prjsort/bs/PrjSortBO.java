
package gov.nbcs.rp.sys.prjsort.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.sys.prjsort.ibs.IPrjSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.sessionmanager.SessionUtil;


/**
 * <p>
 * Title:项目类别管理的服务端操作类
 * </p>

 * </p>
 * <p>
 * CreateData 2011-3-19
 * </p>
 * 
 * @author 钱自成
 * @version 1.2
 */

public class PrjSortBO implements IPrjSort {
	// 选择项目类别的语句
	private String prjSortSQL = "SELECT * FROM fb_e_prjsort WHERE set_year = ?";

	// 根据项目类别选择项目对明细管理的语句，由主表拼出
	private String detailSQL = "SELECT a.DETAIL_ID CODE,a.DETAIL_NAME NAME,b.SEQ_NO SEQ,b.PRJSORT_CODE SORTCODE"
			+ " FROM fb_p_simp_master a,fb_p_sort_to_detail b "
			+ "where a.Detail_ID = b.DETAIL_CODE AND"
			+ " a.SET_YEAR = b.SET_YEAR AND b.PRJSORT_CODE = ? AND b.SET_YEAR = ?";

	// 选择项目明细的语句
	private String detailAllSQL = "SELECT a.DETAIL_ID CODE,a.DETAIL_NAME NAME,b.SEQ_NO SEQ,b.PRJSORT_CODE SORTCODE"
			+ " FROM fb_p_simp_master a,fb_p_sort_to_detail b "
			+ "where a.Detail_ID = b.DETAIL_CODE AND"
			+ " a.SET_YEAR = b.SET_YEAR AND b.SET_YEAR = ?";

	// 根据项目类别选择项目对明细管理的语句（在对应表里直接选择）
	private String sSortSTD = "select * from Fb_p_Sort_To_Detail WHERE set_year = ? and PRJSORT_CODE = ?";

	// 选择项目类别对明细管理的语句
	private String sSTD = "select * from Fb_p_Sort_To_Detail WHERE set_year = ?";

	// 选取值的语句
	private String sInputSet = "select input_set_id,input_set_name from fb_p_input_set where set_year = ?";

	/**
	 * 获取项目类别对明细的数据集
	 * 
	 * @param sSortCode
	 *            项目类别编码
	 * @throws Exception
	 *             如果失败，抛出异常
	 * @return 返回类别对明细的数据集
	 */
	public DataSet getSortToDetail(String sSortCode) throws Exception { // 带参数获取dataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(detailSQL);
		ds.setQueryParams(new Object[] { new Integer(sSortCode),
				new Integer(SessionUtil.getUserInfoContext().getSetYear()) }); // 传递项目类别编码
		ds.open();
		return ds;
	}

	/**
	 * 获取类别对明细的所有数据的数据集
	 */
	public DataSet getSortToDetailDataSet() throws Exception { // 不带参数获取dataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(detailAllSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	/**
	 * 获取项目类别的数据集
	 */
	public DataSet getDataset() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(prjSortSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}
	
	/**
	 * 获取一个项目类别的数据集
	 * String[]:=0 detail_degree 项目明细化类型
	 *          =1 jjtype 支出经济科目范围
	 */
	public String[] getOnePrjSort(String set_year,String PRJSORT_CODE) throws Exception {
		String strSQL = 
			" select detail_degree,jjtype from fb_e_prjsort where set_year="+set_year+" and PRJSORT_CODE='"+PRJSORT_CODE+"'";
		List list = QueryStub.getQueryTool().findBySqlByUpper(strSQL);  
		
		if((list==null)||(list.size()<1)) {
			return null;
		}
		Map map = (Map)list.get(0);
		String[] value = new String[]{map.get("DETAIL_DEGREE").toString(),map.get("JJTYPE").toString()};
		
		return value;
	}

	/**
	 * 获取项目类别的编码规则
	 */
	public SysCodeRule getCodeRule() throws Exception {
		SysCodeRule sysCodeRule = new SysCodeRule(Integer.parseInt(SessionUtil
				.getUserInfoContext().getSetYear()), IPrjSort.CODE_TYPE);
		return sysCodeRule;
	}

	/**
	 * 把数据保存到库里
	 * @param ds 要保存的数据集
	 */
	public void dsPost(DataSet ds) throws Exception {
		ds.post();
	}

	/**
	 *  获取项目类别表里类别编码的最大值
	 *  @return 最大值加一
	 */
	public int getMaxID() throws Exception {
		String sFilter = IPrjSort.SET_YEAR + "="
				+ SessionUtil.getUserInfoContext().getSetYear();
		int maxID = Integer.parseInt(DBSqlExec.getMaxValueFromField(
				IPrjSort.Table_Name, IPrjSort.PRJSORT_ID, sFilter)) + 1;
		return maxID;
	}

	/**
	 *获取项目对明细列表的DATASET，该DATASET 只为保存所用 
	 */
	public DataSet getSortSTD(String aSortCode) throws Exception {
		// 由类别编码来确定dataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSortSTD);
		ds.setQueryParams(new Object[] {
				new Integer(SessionUtil.getUserInfoContext().getSetYear()),
				aSortCode });
		ds.open();
		return ds;
	}

	/**
	 * 由类别编码和明细编码来确定dataset
	 */
	public DataSet getSTD() throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSTD);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	/**
	 * 由类别编码和明细编码来确定dataset
	 */
	public String GetInputSetString() {
		String inputSet = "";
		try {
			DataSet ds = DataSet.create();
			ds.setSqlClause(sInputSet);
			ds.setQueryParams(new Object[] { new Integer(SessionUtil
					.getUserInfoContext().getSetYear()) });
			ds.open();
			ds.beforeFirst();
			inputSet = "#";
			while (ds.next()) {
				inputSet = inputSet + "+"
						+ ds.fieldByName("input_set_id").getString() + "#"
						+ ds.fieldByName("input_set_name").getString();
			}
			return inputSet;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * 获取专项的经济科目(VW_FB_ACCT_ECONOMY_PRJ,项目专用) 
	 * @throws Exception 
	 */
	public DataSet getPrjAcctJJ(String set_Year) throws Exception{
		DataSet ds = DataSet.create();
		ds.setSqlClause("select * from VW_FB_ACCT_ECONOMY_PRJ" +
				" where set_Year='"+set_Year+"' and ACCT_CODE_JJ<>'000'");
		ds.open();
		ds.beforeFirst();
		
		return ds;
	}
	
	
	/**
	 * 获取专项类别对应的经济科目,这里仅取最末级
	 * @throws Exception 
	 */
	public DataSet getPrjSortToAcctJJ(String PRJSORT_CODE) throws Exception{
		DataSet ds = DataSet.create();
		ds.setSqlClause("select a.* " +
				" from FB_P_SORT_TO_ACCTJJ a,VW_FB_ACCT_ECONOMY_PRJ b " +
				" where a.ACCT_CODE_JJ=b.ACCT_CODE_JJ and b.is_leaf=1 " +
				" and a.PRJSORT_CODE='"+PRJSORT_CODE+"'");
		ds.open();
		ds.beforeFirst();
		
		return ds;
	}
	
	/**
	 * 删除专项类别对应的经济科目
	 * @throws Exception 
	 */
	public void deletePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception{
		ArrayList alSQL = new ArrayList();
		//先删除
		alSQL.add("delete FB_P_SORT_TO_ACCTJJ where PRJSORT_CODE='"+PRJSORT_CODE+"'"); 
		QueryStub.getQueryTool().executeBatch(alSQL);
	}
	
	/**
	 * 保存专项类别对应的经济科目,保存之间先删除原来的
	 * @throws Exception 
	 */
	public void savePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception{
		ArrayList alSQL = new ArrayList();
		//先删除
		alSQL.add("delete FB_P_SORT_TO_ACCTJJ where PRJSORT_CODE='"+PRJSORT_CODE+"'");
		for(int i=0;(list!=null)&&(i<list.size());i++){
			XMLData data = (XMLData)list.get(i); 
			alSQL.add(
					" insert into FB_P_SORT_TO_ACCTJJ(SET_YEAR,PRJSORT_CODE,ACCT_CODE_JJ,RG_CODE) " +
					" values("+SessionUtil.getUserInfoContext().getSetYear()+",'"+data.get("PRJSORT_CODE")+"','"+data.get("ACCT_CODE_JJ")+"'," +
					" '"+data.get("RG_CODE")+"')");
		}
		
		QueryStub.getQueryTool().executeBatch(alSQL);
	}

	/**
	 * 执行sql语句
	 * @param list 保存要执行 sql语句的容器
	 */
	public void ExecuteSql(List list) throws Exception {
		QueryStub.getQueryTool().executeBatch(list);
	}
}
