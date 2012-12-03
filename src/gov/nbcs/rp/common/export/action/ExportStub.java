package gov.nbcs.rp.common.export.action;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.common.export.ibs.IExport;

public class ExportStub {
	public static IExport getMethod() {
		IExport itserv = (IExport) ServiceFactory.getServiceInterface("rp.ExportService"); 
		return itserv;
	}
}	
