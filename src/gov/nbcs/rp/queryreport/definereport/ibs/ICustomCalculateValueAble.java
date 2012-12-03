/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ibs;

import com.foundercy.pf.reportcy.summary.iface.cell.ICalculateValueAble;


public interface ICustomCalculateValueAble extends ICalculateValueAble {
	public abstract void setIsSumUp(String isSumUp);

	public abstract String getIsSumUp();

	public abstract void setDispContent(String dispContent);

	public abstract String getDispContent();

}
