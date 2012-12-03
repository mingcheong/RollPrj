/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.framework.dto.ButtonDTO;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ModulePanelHelper {

	/**
	 * 设置 tool button by module status.
	 * 
	 * @param modulePanel
	 *            the module panel
	 * @param statusID
	 *            the status id
	 */
	public static void setToolButtonsByModuleStatus(FModulePanel modulePanel,
			String statusID) {
		// 存放当前状态对应Button的ID
		Set setIDs = new HashSet();
		modulePanel.setCurrentStatusID(statusID);
		List lstCurrentStatusButtonDTO = modulePanel
				.findModuleStatusToolButtons();
		for (Iterator iterator = lstCurrentStatusButtonDTO.iterator(); iterator
				.hasNext();) {
			ButtonDTO btn = (ButtonDTO) iterator.next();
			setIDs.add(btn.getButton_id());
		}

		// 当前状态所对的按钮可用，否则不可用
		FToolBarPanel toolbar = modulePanel.getToolbarPanel();
		Component[] comps = toolbar.getComponents();
		for (int i = 0; i < comps.length; i++) {
			Component comp = comps[i];
			if (comp instanceof FButton) {
				FButton btn = (FButton) comp;
				if (setIDs.contains(btn.getId())) {
					btn.setEnabled(true);
				} else {
					btn.setEnabled(false);
				}
			}
		}

	}

	/**
	 * 设置 tool buttons by inner module.
	 * 
	 * @param modulePanel
	 *            the module panel
	 * @param statusID
	 *            the status id
	 * @param innerButtons
	 *            the inner buttons
	 */
	public static void setToolButtonsByInnerModule(FModulePanel modulePanel,
			String statusID, String innerModuleId) {
		// 存放当前状态对应Button的ID
		Set setIDs = new HashSet();
		modulePanel.setCurrentStatusID(statusID);
		List lstCurrentStatusButtonDTO = modulePanel
				.findModuleStatusToolButtons();
		for (Iterator iterator = lstCurrentStatusButtonDTO.iterator(); iterator
				.hasNext();) {
			ButtonDTO btn = (ButtonDTO) iterator.next();
			setIDs.add(btn.getButton_id());
		}

		// 当前状态所对的按钮可用，否则不可用
		FToolBarPanel toolbar = modulePanel.getToolbarPanel();
		Component[] comps = toolbar.getComponents();
		if ((comps != null) && (comps.length > 0)) {
			for (int i = 0; i < comps.length; i++) {
				Component comp = comps[i];
				if (comp instanceof FButton) {
					FButton btn = (FButton) comp;
					boolean isEnabled = false;
					boolean isVisible = false;

					ActionListener[] lsnrs = btn.getActionListeners();
					if ((lsnrs != null) && (lsnrs.length > 0)) {
						ActionListener lsnr = lsnrs[0];
						if (lsnr instanceof CommonAction) {
							// 判断forAll参数，始终显示
							CommonAction action = (CommonAction) lsnr;
							String forModuleId = (String) action
									.getParameter("for");

							if ((forModuleId == null)
									|| (forModuleId.length() == 0)
									|| "all".equalsIgnoreCase(forModuleId)
									|| ((innerModuleId != null) && forModuleId
											.equals(innerModuleId))) {
								isEnabled = setIDs.contains(btn.getId());
								isVisible = true;
							} else {
								isEnabled = false;
								isVisible = false;
							}
						}
					}

					btn.setEnabled(isEnabled);
					btn.setVisible(isVisible);
				}
			}
		}

	}
}
