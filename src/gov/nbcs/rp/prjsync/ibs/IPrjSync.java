/**
 * @Copyright �㽭���Ű�Ȩ����
 * 
 * @ProjectName ��������������Ŀϵͳ
 * 
 * @aouthor ���ܱ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 * @author ë����
 * 
 * @version ����ʱ�䣺2012-6-7 ����10:50:12
 * 
 * @Description
 */
public interface IPrjSync
{

	/**
	 * ��ȡδͬ������Ŀ������������δ����Ԥ�㣩
	 */
	public DataSet getOldPrjSync(String[] divCode, String setYear, String rgCode, String queryType) throws Exception;


	/**
	 * ��ȡָ��ϵͳ��δͬ������Ŀ
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
	 * ��ȡ��ͬ������Ŀ������������δ����Ԥ�㣩
	 */
	public DataSet getYetPrjSync(String[] divCode, String setYear, String rgCode, String queryType) throws Exception;


	/**
	 * ��Ŀͬ��
	 */
	public String prjSync(List en_id, List bis_name) throws Exception;


	/**
	 * ִ����Ŀͬ��
	 */
	public String makeSynchronous(List syncList) throws Exception;


	public String isXmjl(String id, String name, String money) throws Exception;


	/**
	 * ������Ŀ
	 */
	public DataSet getQueryProject(String cond) throws Exception;


	/**
	 * �Ѳ�����Ŀ
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
