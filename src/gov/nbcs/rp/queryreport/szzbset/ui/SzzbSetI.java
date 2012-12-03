package gov.nbcs.rp.queryreport.szzbset.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * 设置收支总表数据库接口
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
	 * 服务端获取类接口
	 * 
	 * @return
	 */
	public static ISzzbSet getServerMethod() {
		return (ISzzbSet) SessionUtil.getServerBean("rp.szzbSetService");
	}

}
