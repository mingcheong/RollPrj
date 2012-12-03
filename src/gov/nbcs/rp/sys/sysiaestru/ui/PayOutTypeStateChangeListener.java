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
 * Title:支出项目类别DataSet状态改变事件
 * </p>
 * <p>
 * Description:支出项目类别DataSet状态改变事件,根据DataSet状态控制界面和工具栏组件是否可操作
 * </p>
 * <p>
 
 */
public class PayOutTypeStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// 支出项目类别管理客户端主界面类
	private PayOutType payOutType = null;

	public PayOutTypeStateChangeListener(PayOutType payOutType) {
		this.payOutType = payOutType;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(
					payOutType.rightPanel, false);
			payOutType.ftreePayOutType.setEnabled(true);
		} else {
			if ("modname".equals(payOutType.sSaveType)) { // 修改的节点有叶节点
				payOutType.ftxtfPayOutTypeCode.setEditable(true);
				payOutType.ftxtfPayOutTypeName.setEditable(true);
			} else if ("modformate".equals(payOutType.sSaveType)) {// 修改的叶节点已被使用，只能修改编码格式样
				payOutType.ftxtfPayOutTypeCode.setEditable(true);
				payOutType.ftreeAcctJJ.setIsCheckBoxEnabled(true);
			} else {// add,ins,mod
				Common.changeChildControlsEditMode(
						payOutType.rightPanel, true);
				payOutType.ftreePayOutType.setEnabled(false);
				payOutType.ftxtPriCode.setEditable(false);
				if ("add".equals(payOutType.sSaveType)
						|| "addfirstson".equals(payOutType.sSaveType))
					payOutType.ftxtfPayOutTypeCode.setEditable(false);
			}

		}
		// 设置按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				payOutType.dsPayOutType, payOutType, payOutType.ftreePayOutType);
		setActionStatus.setState(true, true);

	}
}
