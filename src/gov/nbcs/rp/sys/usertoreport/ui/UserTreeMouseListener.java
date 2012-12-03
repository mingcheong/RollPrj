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

	//����ӿ�
	private IUserToReport service;

	//�����û�����������û���user_id��
	List userList = new ArrayList();
	//ѡ�еı�����
	List selectedNodes = new ArrayList();
	//����û���code��Ӧ��ID
	Map code2id = new HashMap();
	//�û�����
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
		//���ѡ�еĸ���㣬����
		if(nodeData == null) {
			return;
		}		
		ui.getChangeReportList().clear();
		if(!ui.isSave()) {
			if (!selectedReportIsChanged()) {
				int res = JOptionPane.showConfirmDialog(Global.mainFrame,
						"��ǰ��Ϣû�б��棬�Ƿ���Ҫ����?", "������Ϣ", JOptionPane.YES_NO_OPTION);
				// ��� ѡ����,���б���
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
			//���õ�ǰ�û��Ա����
			ds = service.getHaveSavedRecodeByUserId(userId);
			//�ҵ��Ѿ��洢�ļ�¼
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
		//��ȡ�����ӽ��
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
	 * �õ���ǰѡ�е��û������û������� �ӽ��
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
