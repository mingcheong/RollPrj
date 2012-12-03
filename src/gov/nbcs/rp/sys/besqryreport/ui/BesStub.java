package gov.nbcs.rp.sys.besqryreport.ui;

import org.springframework.beans.factory.BeanFactory;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.util.BeanFactoryUtil;

public class BesStub {
	public static IBesQryReport getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("com/foundercy/fiscalbudget/sys/besqryreport/conf/BesQryReportConf.xml");
		IBesQryReport itserv = (IBesQryReport) beanfac
				.getBean("fb.BesQryReportService");
		return itserv;
	}
}
