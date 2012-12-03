/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:收入项目类别DataSet状态改变事件
 * </p>
 * <p>
 * Description:收入项目类别DataSet状态改变事件,根据DataSet状态控制界面和工具栏组件是否可操作
 * </p>

 */
public class IncTypeStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// 收入项目类别管理客户端主界面类
	private IncType incType = null;

	/**
	 * 构造函数
	 * 
	 */
	public IncTypeStateChangeListener(IncType incType) {
		this.incType = incType;
	}

	/**
	 * 状态转换调用方法
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(incType.rightPanel,
					false);
			incType.setAllControlEnabledFalse();
			incType.ftreeIncType.setEnabled(true);
			incType.setState();
		} else {
			incType.ftreeIncType.setEnabled(false);
			if ("modname".equals(incType.sSaveType)) { // 修改的节点有叶节点
				incType.ftxtfIncTypeCode.setEditable(true);
				incType.ftxtfIncTypeName.setEditable(true);
			} else if ("modformate".equals(incType.sSaveType)) {
				incType.ftxtfIncTypeCode.setEditable(true);
				incType.frdoIsInc.setEditable(true);
				incType.setState();
			} else {// add,ins,mod
				// 设置右边编辑区可以编辑
				incType.setAllControlEnabledTrue();
				if ("add".equals(incType.sSaveType)
						|| "addfirstson".equals(incType.sSaveType))
					incType.ftxtfIncTypeCode.setEditable(false);
				incType.setState();
			}

		}
		// 设置按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				incType.dsIncType, incType, incType.ftreeIncType);
		setActionStatus.setState(true, true);
	}
}
