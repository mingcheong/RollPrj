

package gov.nbcs.rp.sys.prjsort.ibs;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;


/**
 * <p>
 * Title:项目类别管理的接口类
 * </p>

 * 
 * @author 钱自成
 * @version 1.2
 */

public interface IPrjSort {
	// 项目类别表表名
	public final static String Table_Name = "fb_e_prjsort";

	// 预算年度
	public String SET_YEAR = "SET_YEAR";

	// 主键 生成四位流水号
	public String PRJSORT_ID = "PRJSORT_ID";

	// 编码 由编码规则生成
	public String PRJSORT_CODE = "PRJSORT_CODE";

	// 名称
	public String PRJSORT_NAME = "PRJSORT_NAME";

	// 全名 如果无父节点则是本身，如果有父节点则把它父亲的fname+本级名称
	public String PRJSORT_FNAME = "PRJSORT_FNAME";

	// 末级标志，如果是末级则设为1，否则0
	public String END_FLAG = "END_FLAG";

	// 项目明细化程度
	public String DETAIL_DEGREE = "DETAIL_DEGREE";

	// 说明
	public String NOTE = "NOTE";

	// 父节点
	public String PAR_ID = "PAR_CODE";

	// 项目类别在编码规则中的编号
	public String CODE_TYPE = "PRJSORTCODE";

	// 经济科目类型
	public String JJTYPE = "JJTYPE";

	// 选取值
	public String INPUT_SET_ID = "INPUT_SET_ID";

	/**
	 *  获取查询出所有数据的dataset
	 * @return 项目类别的数据集
	 * @throws Exception
	 */
	public DataSet getDataset() throws Exception;

	/**
	 * 获取项目类别的编码规则
	 * @return 项目类别的编码规则
	 * @throws Exception
	 */
	public SysCodeRule getCodeRule() throws Exception; 

	/**
	 * 保存数据
	 * @param ds
	 * @throws Exception
	 */
	public void dsPost(DataSet ds) throws Exception; 

	/**
	 * 获取当前最大的流水号并且+1
	 * @return
	 * @throws Exception
	 */
	public int getMaxID() throws Exception; 

	/**
	 * 带参数获取dataset
	 * @param sSortCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortToDetail(String sSortCode) throws Exception;  

	/**
	 * 无参数获取dataset
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortToDetailDataSet() throws Exception; 

	/**
	 * 根据类别编号获取类别对明细的dataset
	 * @param aSortCode
	 * @return
	 * @throws Exception
	 */
	public DataSet getSortSTD(String aSortCode) throws Exception; 

	/**
	 * 根据类别编号及明细编号来确定类别对明细的dataset
	 * @return
	 * @throws Exception
	 */
	public DataSet getSTD() throws Exception; 

	public String GetInputSetString();

	//项目类别的code的编码长度
	public final static int iPrjSortIDLength = 3;

	// 执行sql语句
	public void ExecuteSql(List list) throws Exception;
	
	/**
	 * 获取专项类别对应的经济科目
	 * @throws Exception 
	 */
	public DataSet getPrjSortToAcctJJ(String PRJSORT_CODE) throws Exception;
	/**
	 * 保存专项类别对应的经济科目,保存之间先删除原来的
	 * @throws Exception 
	 */
	public void savePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception;
	
	/**
	 * 删除专项类别对应的经济科目
	 * @throws Exception 
	 */
	public void deletePrjSortToAcctJJ(String PRJSORT_CODE,List list) throws Exception;
	
	/**
	 * 获取专项的经济科目(VW_FB_ACCT_ECONOMY_PRJ,项目专用)
	 * @throws Exception 
	 */
	public DataSet getPrjAcctJJ(String set_Year) throws Exception;
	
	/**
	 * 获取一个项目类别的数据集
	 * String[]:=0 detail_degree 项目明细化类型
	 *          =1 jjtype 支出经济科目范围
	 */
	public String[] getOnePrjSort(String set_year,String PRJSORT_CODE) throws Exception ;
}
