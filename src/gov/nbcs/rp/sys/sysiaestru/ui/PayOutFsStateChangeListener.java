/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:支出资金来源DataSet状态改变事件
 * </p>
 * <p>
 * Description:支出资金来源DataSet状态改变事件,根据DataSet状态控制界面和工具栏组件是否可操作

 */

public class PayOutFsStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// 支出资金来源管理客户端主界面类
	private PayOutFS payOutFS = null;

	/**
	 * 构造函数
	 * 
	 * @param payOutFs
	 *            支出资金来源管理客户端主界面类
	 */
	public PayOutFsStateChangeListener(PayOutFS payOutFs) {
		this.payOutFS = payOutFs;
	}

	/**
	 * 状态转换调用方法
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			payOutFS.setAllControlEnabledFalse();
			payOutFS.ftreePayOutFS.setEnabled(true);
		} else {
			payOutFS.ftreePayOutFS.setEnabled(false);
			if ("modname".equals(payOutFS.sSaveType)) { // 修改的节点有叶节点
				payOutFS.ftxtfPfsCode.setEditable(true);
				payOutFS.ftxtfPfsName.setEditable(true);
				payOutFS.ftxtReportPfsName.setEditable(true);
			} else if ("modformate".equals(payOutFS.sSaveType)) {// 修改的叶节点已被使用，只能修改编码格式样
				payOutFS.ftxtfPfsCode.setEditable(true);
				payOutFS.fcbxSFormate.setEnabled(true);
				// if (payOutFS.payOutFsObj.data_source == 0)
				// payOutFS.fcbxEFormate.setEnabled(true);
			} else {// add,ins,mod
				payOutFS.setAllControlEnabledTrue();
				if ("add".equals(payOutFS.sSaveType)
						|| "addfirstson".equals(payOutFS.sSaveType))
					payOutFS.ftxtfPfsCode.setEditable(false);
			}
		}
		// 设置按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				payOutFS.dsPayOutFS, payOutFS, payOutFS.ftreePayOutFS);
		setActionStatus.setState(true, true);
	}
}
