package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.ui.tree.CustomTree;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;

/**
 * 控制toolbar按钮

 * 
 */
public class SetActionStatus {
	private DataSet dataSet;

	// private FModulePanel fModulePanel;

	private FToolBarPanel fToolBarPanel;

	private CustomTree customTree;

	public SetActionStatus(DataSet dataSet, FToolBarPanel fToolBarPanel,
			CustomTree customTree) {
		this.dataSet = dataSet;
		this.fToolBarPanel = fToolBarPanel;
		this.customTree = customTree;
	}

	public SetActionStatus(DataSet dataSet, FModulePanel fModulePanel,
			CustomTree customTree) {
		this(dataSet, fModulePanel.getToolbarPanel(), customTree);
	}

	public SetActionStatus(FModulePanel fModulePanel) {
		this.fToolBarPanel = fModulePanel.getToolbarPanel();
	}

	/**
	 * 
	 * @param bParEnableMod,父节点是否能修改，true:可能修改，false:不可能修改
	 * @param bNeedStateControl,true:总的状态控制，false:不参加总的状态控制
	 * @throws Exception
	 */
	public void setState(boolean bParEnableMod, boolean bNeedStateControl)
			throws Exception {
//		// 参加状态控件且系统配置不可以编辑
//		if (bNeedStateControl && !WfStub.getMethod().getCanEditStru()) {
//			setStateUnEnable();
//			return;
//		}
		this.setState(bParEnableMod);
	}

	public void setState(boolean bParEnableMod) throws Exception {
		List controls = fToolBarPanel.getSubControls();
		if (dataSet == null) {
			for (int i = 0; i < controls.size(); i++) {
				if ("修改".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("删除".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("改变支出项目类别".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
			return;
		}
		if ((dataSet.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			for (int i = 0; i < controls.size(); i++) {
				if ("增加".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("修改".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty()
							&& customTree.getSelectedNode() != customTree
									.getRoot()) {
						if (bParEnableMod) // 可以修改父节点
							((FButton) controls.get(i)).setEnabled(true);
						else {// 不可以修改父节点
							if (customTree.getSelectedNode() != null) {
								dataSet.gotoBookmark(customTree
										.getSelectedNode().getBookmark());
								String sFieldName = null;
								if (dataSet.containsField("end_flag")) {
									sFieldName = "end_flag";
								} else if (dataSet.containsField("is_leaf")) {
									sFieldName = "is_leaf";
								}
								if (sFieldName != null
										&& dataSet.fieldByName(sFieldName)
												.getValue() != null
										&& dataSet.fieldByName(sFieldName)
												.getInteger() == 1)
									((FButton) controls.get(i))
											.setEnabled(true);
								else
									((FButton) controls.get(i))
											.setEnabled(false);
							} else
								((FButton) controls.get(i)).setEnabled(false);
						}
					} else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("删除".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty()
							&& customTree.getSelectedNode() != customTree
									.getRoot())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("改变支出项目类别".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
			}
		} else {
			for (int i = 0; i < controls.size(); i++) {
				if ("增加".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("修改".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("删除".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("改变支出项目类别".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
		}
	}

	public void setState() throws Exception {
		List controls = fToolBarPanel.getSubControls();
		if (dataSet == null) {
			for (int i = 0; i < controls.size(); i++) {
				if ("修改".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("删除".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("改变支出项目类别".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
			return;
		}
		if ((dataSet.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			for (int i = 0; i < controls.size(); i++) {
				if ("增加".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("修改".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("删除".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
		} else {
			for (int i = 0; i < controls.size(); i++) {
				if ("增加".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("修改".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("删除".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("保存".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("取消".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);

			}
		}
	}

	/**
	 * 控制Toolbar按钮全部不可用
	 * 
	 */
	public void setStateUnEnable() {
		List controls = fToolBarPanel.getSubControls();
		setUnEnable(controls);
	}

	public void setStateAll(boolean bState) {
		List controls = fToolBarPanel.getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i) instanceof FButton) {
				if (!"关闭".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(bState);
			}
		}
	}

	/**
	 * 控制某个按钮的状态
	 * 
	 * @param sBtnName：按钮名称
	 * @param bState,状态:true,false
	 */
	public void setOneBtnState(String sBtnName, boolean bState) {
		List controls = fToolBarPanel.getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i) instanceof FButton) {
				if (sBtnName.equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(bState);
			}
		}
	}

	/**
	 * 设置ToolBar按钮不可用
	 * 
	 */
	private void setUnEnable(List controls) {
		for (int i = 0; i < controls.size(); i++) {
			if ("增加".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("修改".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("删除".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("保存".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("取消".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("改变支出项目类别".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
		}
	}

	/**
	 * 设置某按钮的名称
	 * 
	 * @param sBtnName：按钮名称
	 * @param caption
	 */
	public void setOneBtnText(String sBtnName, String caption) {
		List controls = fToolBarPanel.getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i) instanceof FButton) {
				if (sBtnName.equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setText(caption);
			}
		}
	}

	/**
	 * 控制某个按钮的状态
	 * 
	 * @param sBtnName：按钮名称
	 * @param bState,状态:true,false
	 */
	public void setOneBtnVisible(String sBtnName, boolean bState) {
		List controls = fToolBarPanel.getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i) instanceof FButton) {
				if (sBtnName.equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setVisible(bState);
			}
		}
	}

}
