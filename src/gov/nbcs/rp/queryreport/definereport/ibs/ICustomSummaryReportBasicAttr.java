/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ibs;

import com.foundercy.pf.reportcy.summary.iface.ISummaryReportBasicAttr;


public interface ICustomSummaryReportBasicAttr extends ISummaryReportBasicAttr {

	public abstract void setIsShowTotalRow(String isShowTotalRow);

	public abstract String getIsShowTotalRow();
}
