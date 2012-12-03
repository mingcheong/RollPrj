package gov.nbcs.rp.prjset.auditset.action;

import gov.nbcs.rp.prjset.auditset.ibs.IPrjAuditSet;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;

public class PrjAuditSetStub {
	public static IPrjAuditSet getMethod() {
		// IPrjInput itserv = (IPrjInput)
		// ServiceFactory.getServiceInterface("rp.PrjInputService");
		// return itserv;
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/prjset/auditset/conf/PrjAuditSetConf.xml");
		IPrjAuditSet itserv = (IPrjAuditSet) beanfac.getBean("rp.PrjAuditSetService");
		return itserv;
	}
}
