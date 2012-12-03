package gov.nbcs.rp.dicinfo.prjdetail.ui;

import org.springframework.beans.factory.BeanFactory;

import gov.nbcs.rp.dicinfo.prjdetail.ibs.IPrjDetail;
import com.foundercy.pf.util.BeanFactoryUtil;

public class PrjDetailStub {
	public static IPrjDetail getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("com/foundercy/fiscalbudget/dicinfo/prjdetail/conf/PrjDetailConf.xml");
		IPrjDetail itserv = (IPrjDetail) beanfac
				.getBean("bmys.PrjDetailTreeService");
		return itserv;
	}
}
