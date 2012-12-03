package gov.nbcs.rp.generateBudget.action;



import gov.nbcs.rp.generateBudget.ibs.IGeBudget;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;


public class GeBudgetAction {
	public static IGeBudget getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/generateBudget/conf/GeBudgetConf.xml");
		IGeBudget itserv = (IGeBudget) beanfac
				.getBean("rp.GeBudgetService");
		return itserv;
//		IPrjAudit itserv = (IPrjAudit) ServiceFactory.getServiceInterface("rp.PrjAuditService"); 
//		return itserv;
	}
}
