/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:报表类型组件
 * </p>
 * <p>
 * Description:报表类型组件
 * </p>

 */
public class ReportTypeList extends CheckBoxList {

	private static final long serialVersionUID = 1L;

	// 定义数据库接口
	private IDefineReport definReportServ;

	public ReportTypeList() {
		// 定义数据库接口
		definReportServ = DefineReportI.getMethod();
		try {
			// 初始化界面
			init();
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox(Global.mainFrame, e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
		}

	}

	/**
	 * 初始化界面
	 * 
	 * @throws Exception
	 * 
	 */
	private void init() throws Exception {

		DataSet dsReportType = definReportServ.getReportType();
		int size = dsReportType.getRecordCount();
		Map tmpMap = null;
		InstallData[] installData = new InstallData[size];

		int i = 0;
		dsReportType.beforeFirst();
		while (dsReportType.next()) {
			tmpMap = dsReportType.getOriginData();
			installData[i] = new InstallData(tmpMap.get(IDefineReport.NAME)
					.toString(), tmpMap.get(IDefineReport.CODE).toString());
			i++;
		}
		this.setData(installData);

	}

	public void setSelected(String sReportID) {
		if (Common.isNullStr(sReportID)) {
			return;
		}
		List lstType = definReportServ.getReportToType(sReportID);
		int size = lstType.size();

		List lstTypeSave = null;
		for (int i = 0; i < size; i++) {
			if (lstTypeSave == null)
				lstTypeSave = new ArrayList();
			lstTypeSave.add(((Map) lstType.get(i)).get(IDefineReport.CODE));
		}
		super.setSelected(lstTypeSave, true);
	}

}
