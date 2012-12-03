/**
 * @# RepDispStub.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import org.springframework.beans.factory.BeanFactory;

import gov.nbcs.rp.queryreport.reportsh.ibs.IRepDisplay;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * ����˵��:
 *<P> Copyright 
 * <P>All rights reserved.


 */
public class RepDispStub {
	public static IRepDisplay getMethod() {
		BeanFactory beanFactory = BeanFactoryUtil
				.getBeanFactory("com/foundercy/fiscalbudget/queryreport/reportsh/conf/reportshconf.xml");
		IRepDisplay dataTransferServ = (IRepDisplay) beanFactory
				.getBean("fb.ReportColService");
		return dataTransferServ;
	}

	public static IRepDisplay getServerMethod() {
		return (IRepDisplay) SessionUtil.getServerBean("fb.ReportColService");

	}

}
