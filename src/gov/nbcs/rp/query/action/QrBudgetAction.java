package gov.nbcs.rp.query.action;



import gov.nbcs.rp.query.ibs.IQrBudget;
import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;


public class QrBudgetAction {
	public static IQrBudget getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/query/conf/QrBudgetConf.xml");
		IQrBudget itserv = (IQrBudget) beanfac
				.getBean("rp.QrBudgetService");
		return itserv;
//		IPrjAudit itserv = (IPrjAudit) ServiceFactory.getServiceInterface("rp.PrjAuditService"); 
//		return itserv;
	}
}
