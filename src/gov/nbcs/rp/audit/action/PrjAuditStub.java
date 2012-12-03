package gov.nbcs.rp.audit.action;

import gov.nbcs.rp.audit.ibs.IPrjAudit;
import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;

public class PrjAuditStub {
	public static IPrjAudit getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/audit/conf/PrjAuditConf.xml");
		IPrjAudit itserv = (IPrjAudit) beanfac
				.getBean("rp.PrjAuditService");
		return itserv;
//		IPrjAudit itserv = (IPrjAudit) ServiceFactory.getServiceInterface("rp.PrjAuditService"); 
//		return itserv;
	}
}
