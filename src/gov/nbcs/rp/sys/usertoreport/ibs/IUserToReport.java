package gov.nbcs.rp.sys.usertoreport.ibs;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
/**
 *
 */
public interface IUserToReport {

	//������еı���
	public DataSet getReport(String data_user) throws Exception;
	
	/**
	 * �õ��Ѿ��洢�ļ�¼
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List getHaveSavedRecodeByUserId(String user_id) throws Exception;
	
	//���洦�ҶԵ�λ����Ϣ
	public void saveUserToReport(DataSet ds,String user_id)throws Exception;
	
	//��������û�
	public DataSet getAllUser() throws Exception;
	
	//�����û��Ա���
	public void updataUser2Report(List userId,List sql) throws Exception;
	/**
	 * ���������û�
	 * @return
	 */
	public List loadAllUser() throws Exception;
	
}
