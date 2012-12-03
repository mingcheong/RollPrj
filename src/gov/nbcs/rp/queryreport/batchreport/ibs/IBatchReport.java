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
package gov.nbcs.rp.queryreport.batchreport.ibs;

import java.util.List;

/**
 * @author Administrator
 * 
 */
public interface IBatchReport {

	/**
	 * Gets the report list.
	 * 
	 * @param reportIds
	 *            the report ids
	 * @return the report list
	 */
	public List getReportList(String reportIds);

	/**
	 * Gets the en list.
	 * 
	 * @return the en list
	 */
	public List getEnList();
}
