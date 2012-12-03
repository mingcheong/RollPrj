/*
 * @(#)ReportInfo.java	Apr 22, 2008 10:06:14 AM GeXinying
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
*/
package gov.nbcs.rp.sys.usertoreport.ui;
/**
 * ReportInfo.java
 * <p>
 * Title:����ѡ�еı���
 * </p>
 * <p>
 * Description:
 * ���Ƚ�,���л��û�ʱ,��ѡ�񱨱�������Ƿ����仯
 * ��������仯,��ʾ�û���Ҫ����
 * </p>

 * @version 1.0
 */
public class ReportInfo {
	private String name;
	private String reportId;
	
	public ReportInfo(String name,String reportId) {
		this.name = name;
		this.reportId = reportId;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public boolean equals(Object arg0) {
		if(!(arg0 instanceof ReportInfo)) {
			return false;
		}
		ReportInfo ri = (ReportInfo) arg0;
		return ri.reportId.equals(reportId);
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + reportId.hashCode();
		return result;
	}

}
