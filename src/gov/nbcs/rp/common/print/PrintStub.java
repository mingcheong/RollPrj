package gov.nbcs.rp.common.print;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.common.print.ibs.IPrint;

public class PrintStub {
	public static IPrint getMethod() {
		IPrint itserv = (IPrint) ServiceFactory
				.getServiceInterface("rp.PrintService");
		return itserv;
	}
}
