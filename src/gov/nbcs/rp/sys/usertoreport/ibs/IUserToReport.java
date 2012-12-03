package gov.nbcs.rp.sys.usertoreport.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
/**
 *
 */
public interface IUserToReport {

	//获得所有的报表
	public DataSet getReport(String data_user) throws Exception;
	
	/**
	 * 得到已经存储的记录
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List getHaveSavedRecodeByUserId(String user_id) throws Exception;
	
	//保存处室对单位的信息
	public void saveUserToReport(DataSet ds,String user_id)throws Exception;
	
	//获得所有用户
	public DataSet getAllUser() throws Exception;
	
	//保存用户对报表
	public void updataUser2Report(List userId,List sql) throws Exception;
	/**
	 * 加载所有用户
	 * @return
	 */
	public List loadAllUser() throws Exception;
	
}
