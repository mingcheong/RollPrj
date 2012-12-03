package gov.nbcs.rp.queryreport.szzbset.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * ������֧�ܱ����ݿ�ӿ�
 * 
 * @author qzc
 * 
 */
public class SzzbSetI {

	public static ISzzbSet getMethod() {
		ISzzbSet szzbSetServ = (ISzzbSet) ServiceFactory
				.getServiceInterface("rp.szzbSetService");
		return szzbSetServ;
	}

	/**
	 * ����˻�ȡ��ӿ�
	 * 
	 * @return
	 */
	public static ISzzbSet getServerMethod() {
		return (ISzzbSet) SessionUtil.getServerBean("rp.szzbSetService");
	}

}
