/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-29
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MyTree extends JTree {

	private static final long serialVersionUID = 1L;

	public MyTree() {
		super();
	}

	public MyTree(Hashtable arg0) {
		super(arg0);
	}

	public MyTree(Object[] arg0) {
		super(arg0);
	}

	public MyTree(TreeModel arg0) {
		super(arg0);
	}

	public MyTree(TreeNode arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public MyTree(TreeNode arg0) {
		super(arg0);
	}

	public MyTree(Vector arg0) {
		super(arg0);
	}

	/**
	 * �õ���������Ҷ�ڵ�
	 * 
	 * @param jTree��
	 * @return
	 */
	public List getIsLeafNode() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel()
				.getRoot();
		if (root == null)
			return null;

		List cache = new ArrayList();
		for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
				.hasMoreElements();) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
					.nextElement();
			if (node == root)
				continue;
			if (node.isLeaf()) {
				cache.add(node.getUserObject());
			}
		}
		return cache;
	}

	/**
	 * * ��ȫչ��һ��JTree *
	 * 
	 * @param tree
	 *            JTree
	 */
	public void expandTree() {
		TreeNode root = (TreeNode) this.getModel().getRoot();
		expandAll(this, new TreePath(root), true);
	}

	/**
	 * * ��ȫչ����ر�һ����,���ڵݹ�ִ��
	 * 
	 * @param tree
	 *            JTree
	 * @param parent
	 *            ���ڵ�
	 * @param expand
	 *            Ϊtrue���ʾչ����,����Ϊ�ر�������
	 */
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	/**
	 * ��λ��ĳ�ڵ�
	 * 
	 * @param node
	 */
	public void expendTo(TreeNode node) {
		TreePath path = new TreePath(((DefaultTreeModel) this.getModel())
				.getPathToRoot(node));
		this.setSelectionPath(path);
	}
}
