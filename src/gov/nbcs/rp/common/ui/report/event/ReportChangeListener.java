/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.event;

import gov.nbcs.rp.common.ui.report.Report;

public interface ReportChangeListener {

	/**
	 * Runs before the header change.
	 *
	 * @param report the report
	 */
	public void beforeHeaderChange(Report report);

	/**
	 * Runs after the header change.
	 *
	 * @param report the report
	 */
	public void afterHeaderChange(Report report);

	/**
	 * Runs before the body change.
	 *
	 * @param report the report
	 */
	public void beforeBodyChange(Report report);

	/**
	 * Runs after the body change.
	 *
	 * @param report the report
	 */
	public void afterBodyChange(Report report);
}
