package gov.nbcs.rp.prjset.auditset.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

public interface IPrjAuditSet {

	public DataSet getAuditElement(boolean isUser, String dep_id)
	throws Exception;
	
	public void dsPost(DataSet ds) throws Exception;
	
	public DataSet getAuditStepInfo() throws Exception;
}
