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

/**
 * @author qj
 *
 */
public abstract class ReportChangeAdapter implements ReportChangeListener {

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.event.ReportChangeListener#beforeHeaderChange(gov.nbcs.rp.common.ui.report.Report)
	 */
	public void beforeHeaderChange(Report report) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.event.ReportChangeListener#afterHeaderChange(gov.nbcs.rp.common.ui.report.Report)
	 */
	public void afterHeaderChange(Report report) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.event.ReportChangeListener#beforeBodyChange(gov.nbcs.rp.common.ui.report.Report)
	 */
	public void beforeBodyChange(Report report) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.event.ReportChangeListener#afterBodyChange(gov.nbcs.rp.common.ui.report.Report)
	 */
	public void afterBodyChange(Report report) {
	}

}
