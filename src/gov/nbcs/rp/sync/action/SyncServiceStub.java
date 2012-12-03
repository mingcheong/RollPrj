package gov.nbcs.rp.sync.action;

import gov.nbcs.rp.sync.ibs.SyncInterface;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;

public class SyncServiceStub {
	public static SyncInterface getMethod() {
		BeanFactory beanfac = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/sync/conf/SyncConf.xml");
		SyncInterface itserv = (SyncInterface) beanfac.getBean("rp.SyncService");
		return itserv;
	}
}
