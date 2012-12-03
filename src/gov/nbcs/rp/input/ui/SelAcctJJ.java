/*
 * $Id: SelDataSource.java,v 1.6 2008/06/18 02:09:45 qianzicheng Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */
package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.SearchPanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class SelAcctJJ extends FDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	TFixTextWithButtonCellEditor cellEditor = null;

	private CustomTree tree;

	public boolean isOK = false;
	
	private String oldValue;
	
	private DataSet ds;

	public SelAcctJJ(TFixTextWithButtonCellEditor cellEditor1,DataSet dsPost,String oldValue) {
		super(Global.mainFrame);
		this.oldValue = oldValue;
		this.ds = dsPost;
		this.cellEditor = cellEditor1;
		initData();
		FPanel pnlBase = new FPanel();
		RowPreferedLayout layBase = new RowPreferedLayout(1);
		pnlBase.setLayout(layBase);

		FButton btnOK = new FButton("", "确定");
		btnOK.setIcon("images/rp/save.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

		FButton btnCancel = new FButton("", "取消");
		btnCancel.setIcon("images/rp/close.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

		FToolBar toolbar = new FToolBar();
		FPanel pnlTop = new FPanel();

		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);

		RowPreferedLayout layTop = new RowPreferedLayout(1);
		pnlTop.setLayout(layTop);
		layTop.setRowHeight(50);
		pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));

		SearchPanel sp = new SearchPanel(tree, "ACCT_CODE_JJ", "ACCT_NAME_JJ");
		pnlBase.addControl(pnlTop, new TableConstraints(2, 1, false, true));
		pnlBase.addControl(sp, new TableConstraints(1, 1, false, true));
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		pnlBase.addControl(pnlTree, new TableConstraints(10, 1, true, true));
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					isOK = true;
					cellEditor.refreshBean();
					setVisible(false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					isOK = false;
					cellEditor.refreshBean();
					setVisible(false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.setSize(400, 600);
		pnlBase.setLeftInset(10);
		pnlBase.setRightInset(10);
		pnlBase.setBottomInset(10);
		this.getContentPane().add(pnlBase);
		this.dispose();
		this.setTitle("经济科目选择");
		this.setModal(true);
	}

	private void initData() {
		try {
			tree = new CustomTree("经济科目列表", ds, "BSI_ID", "ACCT_NAME_JJ",
					"PARENT_ID", null, "ACCT_CODE_JJ", false);
			tree.expandTo("ACCT_NAME_JJ", oldValue);
			tree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2){
						MyTreeNode node = tree.getSelectedNode();
						if (node!=null && node.isLeaf()){
							try {
								isOK = true;
								cellEditor.refreshBean();
								setVisible(false);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			});
//			tree.setIsCheckBoxEnabled(true);
//			setTreeSelect();
		} catch (Exception ee) {
		}
	}

//	private void setTreeSelect() throws Exception {
//		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
//		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
//		if (!Common.isNullStr(oldValue)) {
//			while (enumeration.hasMoreElements()) { // 开始遍历
//				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
//				if (!node.isLeaf())
//					continue;
//				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
//				if (pNode == null)
//					continue;
//				if (Common.isEqual(oldValue, pNode.getShowContent())) {
//					pNode.setIsSelect(true);
//				} else
//					pNode.setIsSelect(false);
//			}
//		} else {
//		}
//	}

//	public List getTreeSelect() {
//		List list = new ArrayList();
//		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
//		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
//		String[] ans = new String[tree.getSelectedNodes(true).length];
//		String[] abs = new String[tree.getSelectedNodes(true).length];
//
//		String value = "";
//		int i = 0;
//		while (enumeration.hasMoreElements()) { // 开始遍历
//			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
//			if (!node.isLeaf())
//				continue;
//			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
//			if (pNode.getIsSelect()) {
//				if (i == 0) {
//					value = pNode.getShowContent();
//				} else {
//					value = value + ";" + pNode.getShowContent();
//				}
//				if (pNode.getShowContent().indexOf("]") < 0)
//					continue;
//				ans[i] = pNode.getShowContent();
//				abs[i] = pNode.getValue();
//				i++;
//			} else {
//
//			}
//		}
//		list.add(0, abs);
//		list.add(1, ans);
//		return list;
//	}
	

	/**
	 * 获取选择的值
	 * 
	 * @return
	 */
	public String[] getReturnValue() {
		String[] value = new String[3];
		MyTreeNode node = tree.getSelectedNode();
		if (node != null && node != tree.getRoot()) {
			PfTreeNode pNode = (PfTreeNode) node.getUserObject();
			value[0] = pNode.getValue();
			value[1] = pNode.getShowContent();
		} else
			return null;
		return value;
	}

}
