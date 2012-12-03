/*
 * @(#)UserTreeMouseListener.java	Mar 12, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.usertoreport.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * UserTreeMouseListener.java
 * <p>
 * Title:

 * @version 1.0
 */
public class UserTreeMouseListener extends MouseAdapter {
	//
	private FModulePanel module;

	//
	private UserToReportUI ui = null;

	//服务接口
	private IUserToReport service;

	//所有用户（存入的是用户的user_id）
	List userList = new ArrayList();
	//选中的报表结点
	List selectedNodes = new ArrayList();
	//存放用户的code对应的ID
	Map code2id = new HashMap();
	//用户报表
	private List ds;
	//
	private XMLData nodeData;
	//
	public UserTreeMouseListener(FModulePanel module, IUserToReport service) {
		this.module = module;
		this.service = service;
	}
	
	public void mouseClicked(MouseEvent arg0) {	
		ui = (UserToReportUI) module;
		nodeData = ui.getUserTree().getSelectedData();
		//如果选中的根结点，返回
		if(nodeData == null) {
			return;
		}		
		ui.getChangeReportList().clear();
		if(!ui.isSave()) {
			if (!selectedReportIsChanged()) {
				int res = JOptionPane.showConfirmDialog(Global.mainFrame,
						"当前信息没有保存，是否需要保存?", "保存信息", JOptionPane.YES_NO_OPTION);
				// 如果 选择是,进行保存
				if (res == 0) {
					ui.doSave();
				} else {
					ui.setSave(true);
				}
			}
		}
		String userId = "";
		if (nodeData.get("user_id") != null) {
			userId = nodeData.get("user_id").toString();
		}else
		{
			userId = "";
		}
		selectedNodes.clear();
		
		try {
			//设置当前用户对报表的
			ds = service.getHaveSavedRecodeByUserId(userId);
			//找到已经存储的记录
			Iterator iter = ds.iterator();
			XMLData data;
			ui.getSelectedReportList().clear();
			StringBuffer key = null;
			while(iter.hasNext()) {
				data = (XMLData)iter.next();
				key = new StringBuffer();
				key.append(data.get("report_type").toString());
				key.append(data.get("report_id").toString());
				selectedNodes.add(key.toString());
				ui.getSelectedReportList().add(new ReportInfo("no useing",key.toString()));
			}
			ui.expandToSelectedNode(selectedNodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		userList.clear();
		//获取所有子结点
		visitAllExpandedNodes(ui.getUserTree(),ui.getUserTree().getSelectionPath());
		ui.setUserList(userList);		
	}
	private boolean selectedReportIsChanged() {
		ui.getChangeReportList().clear();
		MyTreeNode[] selectNodes = ui.getReportTree().getSelectedNodes(false);
		for (int i = 0; i < selectNodes.length; i++) {
			if (!selectNodes[i].isLeaf()) {
				continue;
			}
			ui.getChangeReportList().add(
					new ReportInfo("", selectNodes[i].sortKeyValue()));
		}
		if (ui.getSelectedReportList().size() == ui.getChangeReportList()
				.size()
				&& ui.getSelectedReportList().containsAll(
						ui.getChangeReportList())
				&& ui.getChangeReportList().containsAll(
						ui.getSelectedReportList())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到当前选中的用户及该用户的所有 子结点
	 * @param tree
	 * @param parent
	 */
	public void visitAllExpandedNodes(FTree tree, TreePath parent) {

		// node is visible and is visited exactly once
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		DefaultMutableTreeNode n1 = (DefaultMutableTreeNode)parent.getLastPathComponent();
		PfTreeNode obj  = (PfTreeNode)n1.getUserObject();
		XMLData ds = (XMLData)obj.getCustomerObject();
		if(ds.get("user_id")!=null) {
			this.userList.add(ds.get("user_id").toString());
		}
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				visitAllExpandedNodes(tree, path);
			}
		}
	}
}
