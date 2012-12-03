

package gov.nbcs.rp.sys.prjsort.ibs;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;


/**
 * <p>
 * Title:��Ŀ������Ľӿ���
 * </p>

 * 
 * @author Ǯ�Գ�
 * @version 1.2
 */

public interface IPrjSort {
	// ��Ŀ�������
	public final static String Table_Name = "fb_e_prjsort";

	// Ԥ�����
	public String SET_YEAR = "SET_YEAR";

	// ���� ������λ��ˮ��
	public String PRJSORT_ID = "PRJSORT_ID";

	// ���� �ɱ����������
	public String PRJSORT_CODE = "PRJSORT_CODE";

	// ����
	public String PRJSORT_NAME = "PRJSORT_NAME";

	// ȫ�� ����޸��ڵ����Ǳ�������и��ڵ���������׵�fname+��������
	public String PRJSORT_FNAME = "PRJSORT_FNAME";

	// ĩ����־�������ĩ������Ϊ1������0
	public String END_FLAG = "END_FLAG";

	// ��Ŀ��ϸ���̶�
	public String DETAIL_DEGREE = "DETAIL_DEGREE";

	// ˵��
	public String NOTE = "NOTE";

	// ���ڵ�
	public String PAR_ID = "PAR_CODE";

	// ��Ŀ����ڱ�������еı��
	public String CODE_TYPE = "PRJSORTCODE";

	// ���ÿ�Ŀ����
	public String JJTYPE = "JJTYPE";

	// ѡȡֵ
	public String INPUT_SET_ID = "INPUT_SET_ID";

	/**
	 *  ��ȡ��ѯ���������ݵ�dataset
	 * @return ��Ŀ�������ݼ�
	 * @throws Exception
	 */
	public DataSet getDataset() throws Exception;

	/**
	 * ��ȡ��Ŀ���ı������
	 * @return ��Ŀ���ı������
	 * @throws Exception
	 */
	public SysCodeRule getCodeRule() throws Exception; 

	/**
	 * ��������
	 * @param ds
	 * @throws Exception
	 */
	public void dsPost(DataSet ds) throws Exception; 

	/**
	 * ��ȡ��ǰ������ˮ�Ų���+1
	 * @return
	 * @throws Exception
	 */
	public int getMaxID() throws Exception; 

	/**
	 * ��������ȡdataset
	 * @param sSortCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortToDetail(String sSortCode) throws Exception;  

	/**
	 * �޲�����ȡdataset
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortToDetailDataSet() throws Exception; 

	/**
	 * ��������Ż�ȡ������ϸ��dataset
	 * @param aSortCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortSTD(String aSortCode) throws Exception; 

	/**
	 * ��������ż���ϸ�����ȷ��������ϸ��dataset
	 * @return
	 * @throws Exception
	 */
	public DataSet getSTD() throws Exception; 

	public String GetInputSetString();

	//��Ŀ����code�ı��볤��
	public final static int iPrjSortIDLength = 3;

	// ִ��sql���
	public void ExecuteSql(List list) throws Exception;
	
	/**
	 * ��ȡר������Ӧ�ľ��ÿ�Ŀ
	 * @throws Exception 
	 */
	public DataSet getPrjSortToAcctJJ(String PRJSORT_CODE) throws Exception;
	/**
	 * ����ר������Ӧ�ľ��ÿ�Ŀ,����֮����ɾ��ԭ����
	 * @throws Exception 
	 */
	public void savePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception;
	
	/**
	 * ɾ��ר������Ӧ�ľ��ÿ�Ŀ
	 * @throws Exception 
	 */
	public void deletePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception;
	
	/**
	 * ��ȡר��ľ��ÿ�Ŀ(VW_FB_ACCT_ECONOMY_PRJ,��Ŀר��)
	 * @throws Exception 
	 */
	public DataSet getPrjAcctJJ(String set_Year) throws Exception;
	
	/**
	 * ��ȡһ����Ŀ�������ݼ�
	 * String[]:=0 detail_degree ��Ŀ��ϸ������
	 *          =1 jjtype ֧�����ÿ�Ŀ��Χ
	 */
	public String[] getOnePrjSort(String set_year,String PRJSORT_CODE) throws Exception ;
}
