package gov.nbcs.rp.sys.usertoreport.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport;

public class UserToReportStub {

	public static IUserToReport getMethod() { 
		IUserToReport iReport = (IUserToReport) ServiceFactory
				.getServiceInterface("rp.UserToReportBOService");
		return iReport;
	}
}
