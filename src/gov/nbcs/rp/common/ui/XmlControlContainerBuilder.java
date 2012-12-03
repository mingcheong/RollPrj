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

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.UIControlFactory;
import com.foundercy.pf.control.UITools;

import java.awt.Component;
import java.awt.Container;

public class XmlControlContainerBuilder {

	/** The ctrl pane. */
	private Container ctrlPane;

	/**
	 * Instantiates a new xml control container builder.
	 * 
	 * @param ctrlPane
	 *            the ctrl pane
	 */
	public XmlControlContainerBuilder(Container ctrlPane) {
		super();
		this.ctrlPane = ctrlPane;
	}

	/**
	 * Builds the.
	 * 
	 * @param configXmlFilePath
	 *            the config xml file path
	 * @param container
	 *            the container
	 * @return the component
	 * @throws ControlException
	 *             the control exception
	 */
	public Component build(String configXmlFilePath,
			XmlControlContainerTemplate template, CtrlBuildHandler handler)
			throws ControlException {
		UIControlFactory factory = new UIControlFactory(configXmlFilePath);
		FPanel pnl = (FPanel) factory.createControl();
		if (ctrlPane != null) {
			ctrlPane.add(pnl);
		} else {
			throw new ControlException("The control pane is null.");
		}

		if (handler != null) {
			handler.beforeAssignCtrlVar(pnl);
		}

		if (template != null) {
			template.assignCtrlVar();
		}

		if (handler != null) {
			handler.afterAssignCtrlVar(pnl);
		}

		if (template != null) {
			template.bindCtrlEvent();
		}
		return pnl;
	}
	
	public Object getFirstControlById(String id) {
		return UITools.getFirstControlById(ctrlPane, id);
	}
}
