package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.ui.tree.CustomTree;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;

/**
 * ����toolbar��ť

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
	 * @param bParEnableMod,���ڵ��Ƿ����޸ģ�true:�����޸ģ�false:�������޸�
	 * @param bNeedStateControl,true:�ܵ�״̬���ƣ�false:���μ��ܵ�״̬����
	 * @throws Exception
	 */
	public void setState(boolean bParEnableMod, boolean bNeedStateControl)
			throws Exception {
//		// �μ�״̬�ؼ���ϵͳ���ò����Ա༭
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
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("�ı�֧����Ŀ���".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
			return;
		}
		if ((dataSet.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("�޸�".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty()
							&& customTree.getSelectedNode() != customTree
									.getRoot()) {
						if (bParEnableMod) // �����޸ĸ��ڵ�
							((FButton) controls.get(i)).setEnabled(true);
						else {// �������޸ĸ��ڵ�
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
				if ("ɾ��".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty()
							&& customTree.getSelectedNode() != customTree
									.getRoot())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("�ı�֧����Ŀ���".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
			}
		} else {
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("�ı�֧����Ŀ���".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
		}
	}

	public void setState() throws Exception {
		List controls = fToolBarPanel.getSubControls();
		if (dataSet == null) {
			for (int i = 0; i < controls.size(); i++) {
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("�ı�֧����Ŀ���".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
			return;
		}
		if ((dataSet.getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("�޸�".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("ɾ��".equals(((FButton) controls.get(i)).getText())) {
					if (!dataSet.isEmpty())
						((FButton) controls.get(i)).setEnabled(true);
					else {
						((FButton) controls.get(i)).setEnabled(false);
					}
				}
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
			}
		} else {
			for (int i = 0; i < controls.size(); i++) {
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("�޸�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(false);
				if ("����".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);
				if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(true);

			}
		}
	}

	/**
	 * ����Toolbar��ťȫ��������
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
				if (!"�ر�".equals(((FButton) controls.get(i)).getText()))
					((FButton) controls.get(i)).setEnabled(bState);
			}
		}
	}

	/**
	 * ����ĳ����ť��״̬
	 * 
	 * @param sBtnName����ť����
	 * @param bState,״̬:true,false
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
	 * ����ToolBar��ť������
	 * 
	 */
	private void setUnEnable(List controls) {
		for (int i = 0; i < controls.size(); i++) {
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("�޸�".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("ɾ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("����".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("ȡ��".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
			if ("�ı�֧����Ŀ���".equals(((FButton) controls.get(i)).getText()))
				((FButton) controls.get(i)).setEnabled(false);
		}
	}

	/**
	 * ����ĳ��ť������
	 * 
	 * @param sBtnName����ť����
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
	 * ����ĳ����ť��״̬
	 * 
	 * @param sBtnName����ť����
	 * @param bState,״̬:true,false
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
