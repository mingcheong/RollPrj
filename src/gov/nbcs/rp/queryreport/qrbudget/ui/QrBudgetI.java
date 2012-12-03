package gov.nbcs.rp.queryreport.qrbudget.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

public class QrBudgetI {

	public static IQrBudget getMethod() {
		IQrBudget qrBudgetServ = (IQrBudget) ServiceFactory
				.getServiceInterface("rp.qrBudgetService");

		return qrBudgetServ;
	}

	/**
	 * 服务端获取类接口
	 * 
	 * @return
	 */
	public static IQrBudget getServerMethod() {
		return (IQrBudget) SessionUtil.getServerBean("rp.qrBudgetService");
	}

}
