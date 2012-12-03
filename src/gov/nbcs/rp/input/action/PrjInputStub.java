package gov.nbcs.rp.input.action;

import gov.nbcs.rp.input.ibs.IPrjInput;
import org.springframework.beans.factory.BeanFactory;
import com.foundercy.pf.util.BeanFactoryUtil;

public class PrjInputStub {
	public static IPrjInput getMethod() {
		// IPrjInput itserv = (IPrjInput)
		// ServiceFactory.getServiceInterface("rp.PrjInputService");
		// return itserv;
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/input/conf/PrjInputConf.xml");
		IPrjInput itserv = (IPrjInput) beanfac.getBean("rp.PrjInputService");
		return itserv;
	}
}
