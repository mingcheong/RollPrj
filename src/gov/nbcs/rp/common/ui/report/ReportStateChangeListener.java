/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

public class ReportStateChangeListener implements StateChangeListener {
	Report report;

	ReportStateChangeListener(Report report) {
		this.report = report;
	}

	public void onStateChange(DataSetEvent event) throws Exception {
		if ((event.getDataSet().getState() & StatefulData.DS_EDIT) != StatefulData.DS_EDIT) {
			report.getUI().setRowSelect(true);
		} else {
			report.getUI().setRowSelect(false);
		}
	}

}
