/**
 * @Copyright 浙江易桥版权所有
 * 
 * @ProjectName 宁海财政扶持项目系统
 * 
 * @aouthor 陈宪标
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 * @author 毛建亮
 * 
 * @version 创建时间：2012-6-7 上午10:50:12
 * 
 * @Description
 */
public interface IPrjSync
{

	/**
	 * 获取未同步的项目（已审核填报，但未生成预算）
	 */
	public DataSet getOldPrjSync(String[] divCode, String setYear, String rgCode, String queryType) throws Exception;


	/**
	 * 获取指标系统中未同步的项目
	 * 
	 * @param divCodes
	 * @param setYear
	 * @param rgCode
	 * @param queryType
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjFromIndexSys(String[] divCodes, String setYear, String rgCode, String queryType) throws Exception;


	/**
	 * 获取已同步的项目（已审核填报，但未生成预算）
	 */
	public DataSet getYetPrjSync(String[] divCode, String setYear, String rgCode, String queryType) throws Exception;


	/**
	 * 项目同步
	 */
	public String prjSync(List en_id, List bis_name) throws Exception;


	/**
	 * 执行项目同步
	 */
	public String makeSynchronous(List syncList) throws Exception;


	public String isXmjl(String id, String name, String money) throws Exception;


	/**
	 * 补填项目
	 */
	public DataSet getQueryProject(String cond) throws Exception;


	/**
	 * 已补填项目
	 */
	public DataSet getQueryProject2(String cond) throws Exception;


	/**
	 * 
	 */
	public DataSet getQueryProject3(String cond) throws Exception;


	public DataSet getPrjTbDetailInfo(String sYear, String rgCode, String xmbm, String en_code, String xmxh) throws Exception;


	public String saveTbPrj(Map map) throws Exception;


	public DataSet getDivDataPop(String sYear) throws Exception;


	public String prjCancelSync(ArrayList enid, ArrayList bisname, ArrayList tomoney);

}
