
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
 * Title:��Ŀ������ķ���˲�����
 * </p>

 * </p>
 * <p>
 * CreateData 2011-3-19
 * </p>
 * 
 * @author Ǯ�Գ�
 * @version 1.2
 */

public class PrjSortBO implements IPrjSort {
	// ѡ����Ŀ�������
	private String prjSortSQL = "SELECT * FROM fb_e_prjsort WHERE set_year = ?";

	// ������Ŀ���ѡ����Ŀ����ϸ�������䣬������ƴ��
	private String detailSQL = "SELECT a.DETAIL_ID CODE,a.DETAIL_NAME NAME,b.SEQ_NO SEQ,b.PRJSORT_CODE SORTCODE"
			+ " FROM fb_p_simp_master a,fb_p_sort_to_detail b "
			+ "where a.Detail_ID = b.DETAIL_CODE AND"
			+ " a.SET_YEAR = b.SET_YEAR AND b.PRJSORT_CODE = ? AND b.SET_YEAR = ?";

	// ѡ����Ŀ��ϸ�����
	private String detailAllSQL = "SELECT a.DETAIL_ID CODE,a.DETAIL_NAME NAME,b.SEQ_NO SEQ,b.PRJSORT_CODE SORTCODE"
			+ " FROM fb_p_simp_master a,fb_p_sort_to_detail b "
			+ "where a.Detail_ID = b.DETAIL_CODE AND"
			+ " a.SET_YEAR = b.SET_YEAR AND b.SET_YEAR = ?";

	// ������Ŀ���ѡ����Ŀ����ϸ�������䣨�ڶ�Ӧ����ֱ��ѡ��
	private String sSortSTD = "select * from Fb_p_Sort_To_Detail WHERE set_year = ? and PRJSORT_CODE = ?";

	// ѡ����Ŀ������ϸ��������
	private String sSTD = "select * from Fb_p_Sort_To_Detail WHERE set_year = ?";

	// ѡȡֵ�����
	private String sInputSet = "select input_set_id,input_set_name from fb_p_input_set where set_year = ?";

	/**
	 * ��ȡ��Ŀ������ϸ�����ݼ�
	 * 
	 * @param sSortCode
	 *            ��Ŀ������
	 * @throws Exception
	 *             ���ʧ�ܣ��׳��쳣
	 * @return ����������ϸ�����ݼ�
	 */
	public DataSet getSortToDetail(String sSortCode) throws Exception { // ��������ȡdataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(detailSQL);
		ds.setQueryParams(new Object[] { new Integer(sSortCode),
				new Integer(SessionUtil.getUserInfoContext().getSetYear()) }); // ������Ŀ������
		ds.open();
		return ds;
	}

	/**
	 * ��ȡ������ϸ���������ݵ����ݼ�
	 */
	public DataSet getSortToDetailDataSet() throws Exception { // ����������ȡdataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(detailAllSQL);
		ds.setQueryParams(new Object[] { new Integer(SessionUtil
				.getUserInfoContext().getSetYear()) });
		ds.open();
		return ds;
	}

	/**
	 * ��ȡ��Ŀ�������ݼ�
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
	 * ��ȡһ����Ŀ�������ݼ�
	 * String[]:=0 detail_degree ��Ŀ��ϸ������
	 *          =1 jjtype ֧�����ÿ�Ŀ��Χ
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
	 * ��ȡ��Ŀ���ı������
	 */
	public SysCodeRule getCodeRule() throws Exception {
		SysCodeRule sysCodeRule = new SysCodeRule(Integer.parseInt(SessionUtil
				.getUserInfoContext().getSetYear()), IPrjSort.CODE_TYPE);
		return sysCodeRule;
	}

	/**
	 * �����ݱ��浽����
	 * @param ds Ҫ��������ݼ�
	 */
	public void dsPost(DataSet ds) throws Exception {
		ds.post();
	}

	/**
	 *  ��ȡ��Ŀ����������������ֵ
	 *  @return ���ֵ��һ
	 */
	public int getMaxID() throws Exception {
		String sFilter = IPrjSort.SET_YEAR + "="
				+ SessionUtil.getUserInfoContext().getSetYear();
		int maxID = Integer.parseInt(DBSqlExec.getMaxValueFromField(
				IPrjSort.Table_Name, IPrjSort.PRJSORT_ID, sFilter)) + 1;
		return maxID;
	}

	/**
	 *��ȡ��Ŀ����ϸ�б��DATASET����DATASET ֻΪ�������� 
	 */
	public DataSet getSortSTD(String aSortCode) throws Exception {
		// ����������ȷ��dataset
		DataSet ds = DataSet.create();
		ds.setSqlClause(sSortSTD);
		ds.setQueryParams(new Object[] {
				new Integer(SessionUtil.getUserInfoContext().getSetYear()),
				aSortCode });
		ds.open();
		return ds;
	}

	/**
	 * �����������ϸ������ȷ��dataset
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
	 * �����������ϸ������ȷ��dataset
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
	 * ��ȡר��ľ��ÿ�Ŀ(VW_FB_ACCT_ECONOMY_PRJ,��Ŀר��) 
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
	 * ��ȡר������Ӧ�ľ��ÿ�Ŀ,�����ȡ��ĩ��
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
	 * ɾ��ר������Ӧ�ľ��ÿ�Ŀ
	 * @throws Exception 
	 */
	public void deletePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception{
		ArrayList alSQL = new ArrayList();
		//��ɾ��
		alSQL.add("delete FB_P_SORT_TO_ACCTJJ where PRJSORT_CODE='"+PRJSORT_CODE+"'"); 
		QueryStub.getQueryTool().executeBatch(alSQL);
	}
	
	/**
	 * ����ר������Ӧ�ľ��ÿ�Ŀ,����֮����ɾ��ԭ����
	 * @throws Exception 
	 */
	public void savePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception{
		ArrayList alSQL = new ArrayList();
		//��ɾ��
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
	 * ִ��sql���
	 * @param list ����Ҫִ�� sql��������
	 */
	public void ExecuteSql(List list) throws Exception {
		QueryStub.getQueryTool().executeBatch(list);
	}
}
