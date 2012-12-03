/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:收入栏目DataSet状态改变事件
 * </p>
 * <p>
 * Description:收入栏目DataSet状态改变事件,根据DataSet状态控制界面和工具栏组件是否可操作
 * </p>

 */
public class IncColumnStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// 收入栏目管理客户端主界面类
	private IncColumn incColumn = null;

	/**
	 * 构造函数
	 * 
	 * @param incColumn
	 *            收入栏目管理客户端主界面类
	 */
	public IncColumnStateChangeListener(IncColumn incColumn) {
		this.incColumn = incColumn;
	}

	/**
	 * 状态转换调用方法
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		// 判断收入栏目DataSet当前状态是否为浏览状态
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {// 浏览状态
			// 设右边编辑区组件不可编辑
			incColumn.setAllControlEnabledFalse();
			// 设置左边收入栏目树可以操作
			incColumn.ftreeIncColumn.setEnabled(true);
		} else {// 编辑状态
			// 设置左边收入栏目树不可以操作
			incColumn.ftreeIncColumn.setEnabled(false);
			// 判断修改的节点是否有子节点
			if ("modname".equals(incColumn.sSaveType)) { // 修改的节点有子节点
				// 只允许修改层次码和名称
				incColumn.ftxtfIncColCode.setEditable(true);
				incColumn.ftxtfIncColName.setEditable(true);
			} else if ("modformate".equals(incColumn.sSaveType)) {// 修改的叶节点已被使用，只能修改编码格式样
				incColumn.ftxtfIncColCode.setEditable(true);
				incColumn.fcbxSFormate.setEnabled(true);
				incColumn.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			} else {// add,ins,mod
				// 设右边编辑区组件可编辑
				incColumn.setAllControlEnabledTrue();
				if ("add".equals(incColumn.sSaveType)
						|| "addfirstson".equals(incColumn.sSaveType)) {
					incColumn.ftxtfIncColCode.setEditable(false);
				}
			}
		}
		// 设置ToolBar按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				incColumn.dsIncCol, incColumn, incColumn.ftreeIncColumn);
		setActionStatus.setState(true, true);
	}
}
