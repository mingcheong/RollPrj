/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ibs;

import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;


public interface ICustomStatisticCaliber extends IStatisticCaliber {

	public abstract String getContext();

	public abstract String getLParenthesis();

	public abstract String getRParenthesis();
	
	public abstract void setContext(String context);

	public abstract void setLParenthesis(String parenthesis);

	public abstract void setRParenthesis(String parenthesis);
	
	public abstract String getEnumID();

	public abstract void setEnumID(String enumID) ;

	public abstract String getNodeID() ;

	public abstract void setNodeID(String nodeID);

}
