/*
 * @(#)ReportTreeMouseListener.java	Apr 14, 2008 4:56:20 PM GeXinying
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.usertoreport.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * ReportTreeMouseListener.java
 * <p>
 * Title:报表树 Mouse监听器
 * </p>
 * <p>
 * Description: 主要作用是,为是判断,当前的信息是否被保存 如果没有保存,在切换用户的时候,给出相应的提示信息 提醒用户是否需要保存
 * </p>
 * <p>

 * @version 1.0
 */
public class ReportTreeMouseListener extends MouseAdapter {
	//
	private Logger log = Logger.getLogger(ReportTreeMouseListener.class);
	//
	private UserToReportUI ui ;

	public ReportTreeMouseListener(FModulePanel module) {
		this.ui = (UserToReportUI)module;
	}

	public void mouseClicked(MouseEvent arg0) {
		log.info("colin-- report tree clicked");
		TreePath node = ui.getReportTree().getSelectionPath();
		log.info("colin-- report tree clicked"+node);
		if(node != null)
		{
			ui.setSave(false);
		}else {
			ui.setSave(true);
		}
	}
}
