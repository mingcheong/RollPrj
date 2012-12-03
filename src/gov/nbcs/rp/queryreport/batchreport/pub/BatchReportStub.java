/**
 * Copyright zjyq ��Ȩ����
 * 
 * ����Ԥ�����ϵͳ
 * 
 * @title 
 * 
 * @author qzcun
 * 
 * @version 1.0
 */
package gov.nbcs.rp.queryreport.batchreport.pub;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.queryreport.batchreport.ibs.IBatchReport;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * @author Administrator
 * 
 */
public class BatchReportStub {
	/**
	 * �ͻ��˵��ýӿ�
	 */
	public static IBatchReport getMethod() {
		return (IBatchReport) ServiceFactory
				.getServiceInterface("fb.BatchReportService",
						"gov/nbcs/rp/queryreport/batchreport/conf/batchreportConf.xml");

	}

	/**
	 * ����˵��ýӿ�
	 */
	public static IBatchReport getServerMethod() {
		return (IBatchReport) SessionUtil
				.getServerBean("fb.BatchReportService");
	}

	/**
	 * ����˵��ýӿ�NT
	 */
	public static IBatchReport getServerMethodNT() {
		return (IBatchReport) SessionUtil.getServerBean("fb.BatchReportBO");
	}

}
