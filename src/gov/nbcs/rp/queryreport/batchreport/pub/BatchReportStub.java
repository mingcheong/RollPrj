/**
 * Copyright zjyq 版权所有
 * 
 * 部门预算编审系统
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
	 * 客户端调用接口
	 */
	public static IBatchReport getMethod() {
		return (IBatchReport) ServiceFactory
				.getServiceInterface("fb.BatchReportService",
						"gov/nbcs/rp/queryreport/batchreport/conf/batchreportConf.xml");

	}

	/**
	 * 服务端调用接口
	 */
	public static IBatchReport getServerMethod() {
		return (IBatchReport) SessionUtil
				.getServerBean("fb.BatchReportService");
	}

	/**
	 * 服务端调用接口NT
	 */
	public static IBatchReport getServerMethodNT() {
		return (IBatchReport) SessionUtil.getServerBean("fb.BatchReportBO");
	}

}
