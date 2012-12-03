/*
 * $Id: SelCodeRule.java,v 1.5 2008/06/18 02:09:45 qianzicheng Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */
package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
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

/**
 * <p>
 * Title:获取功能科目
 * </p>
 * <p>
 * Description:功能科目的界面，提供选择并返回编码和名称
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
 * </p>
 * <p>
 * CreateData 2011-6-27
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class SelAcct extends FDialog {
	
	TFixTextWithButtonCellEditor cellEditor = null;

	private static final long serialVersionUID = 1L;

	private CustomTree tree;

	public boolean isOK = false;

	private String oldValue;

	private DataSet ds;

	public SelAcct(TFixTextWithButtonCellEditor cellEditor1,DataSet dsPost, String oldValue) {
		super(Global.mainFrame);
		this.cellEditor = cellEditor1;
		this.ds = dsPost;
		this.oldValue = oldValue;
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

		// SearchPanel sp = new SearchPanel(tree, "ACCT_CODE", "ACCT_NAME");
		pnlBase.addControl(pnlTop, new TableConstraints(2, 1, false, true));
		FScrollPane pnl = new FScrollPane();
		pnl.addControl(tree);
		pnlBase.addControl(new CustomTreeFinder(tree), new TableConstraints(1,
				1, false, true));
		pnlBase.addControl(pnl, new TableConstraints(10, 1, true, true));
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
		this.setTitle("选择功能科目");
		this.setModal(true);
	}

	private void initData() {
		try {
			tree = new CustomTree("功能科目", ds, "BS_ID", "ACCT_NAME",
					"PARENT_ID", null, "ACCT_CODE");
			tree.expandTo("acct_name", oldValue);
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
		} catch (Exception ee) {
		}
	}

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
