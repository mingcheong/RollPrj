/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.tree;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * The Class CustomTreeFinder.
 * 
 * @author Administrator
 */
public class CustomTreeFinder extends FPanel {

	private static final String HINT_INPUT_KEYWORD = "输入关键字";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The tree. */
	private CustomTree tree;
	
	private FTree fTree;

	/** The txt key. */
	private FTextField txtKey;

	/**
	 * Instantiates a new tree node finder.
	 * 
	 */
	public CustomTreeFinder() {
		initComponent();
	}

	/**
	 * Instantiates a new tree node finder.
	 * 
	 * @param tree
	 *            the tree
	 */
	public CustomTreeFinder(CustomTree tree) {
		this.tree = tree;
		initComponent();
	}

	/**
	 * Inits the component.
	 */
	private void initComponent() {
		RowPreferedLayout layoutDivFinder = new RowPreferedLayout(100);
		this.setTopInset(0);
		this.setBottomInset(0);
		layoutDivFinder.setRowGap(1);
		layoutDivFinder.setColumnGap(1);
		layoutDivFinder.setColumnWidth(1);
		layoutDivFinder.setRowHeight(18);
		this.setLayout(layoutDivFinder);

		txtKey = new FTextField();
		txtKey.setTitleVisible(false);
		txtKey.setValue(HINT_INPUT_KEYWORD);

		FButton btnFind = new FButton();
		btnFind.setTitle("查找");

		FButton btnFindNext = new FButton();
		btnFindNext.setTitle("下一个");

		this.addControl(txtKey, new TableConstraints(1, 34, false, true));
		this.addControl(btnFind, new TableConstraints(1, 30, false, false));
		this.addControl(btnFindNext, new TableConstraints(1, 36, false, false));

		txtKey.getEditor().addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (HINT_INPUT_KEYWORD.equals(txtKey.getValue())) {
					txtKey.setValue("");
				}
			}

			public void focusLost(FocusEvent e) {
				if ("".equals(txtKey.getValue())) {
					txtKey.setValue(HINT_INPUT_KEYWORD);
				}
			}
		});

		btnFind.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(tree==null) {
					findFTreeFirst();
				} else {
					findFirst();
				}
			}
		});
		btnFindNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(tree==null) {
					findFTreeNext();
				} else {
					findNext();
				}
			}
		});
	}

	protected DefaultMutableTreeNode findNodeByFilter(
			DefaultMutableTreeNode root, String textFieldName, Object value) {
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
			if (pfNode.getShowContent().indexOf((String) value) >= 0) {
				return node;
			} else {
				node = findNodeByFilter(node, textFieldName, value);
				if (node != null) {
					return node;
				}
			}
		}
		return null;
	}

	private boolean isStartNodeFound = false;

	protected DefaultMutableTreeNode findNodeByFilter(
			DefaultMutableTreeNode root, String textFieldName, Object value,
			TreeNode startNode) {
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			if (node == startNode) {
				isStartNodeFound = true;
				node = findNodeByFilter(node, textFieldName, value, startNode);
				if (node != null) {
					return node;
				}
				continue;
			}
			if (isStartNodeFound) {
				PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
				if (pfNode.getShowContent().indexOf((String) value) >= 0) {
					return node;
				} else {
					node = findNodeByFilter(node, textFieldName, value,
							startNode);
					if (node != null) {
						return node;
					}
				}
			} else {
				node = findNodeByFilter(node, textFieldName, value, startNode);
				if (node != null) {
					return node;
				}
			}
		}
		return null;
	}
	
	
	protected DefaultMutableTreeNode findNodeByFilter(
			DefaultMutableTreeNode root, String textFieldName, Object value,
			PfTreeNode startNode) {
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			if (node.getUserObject() == startNode) {
				isStartNodeFound = true;
				node = findNodeByFilter(node, textFieldName, value, startNode);
				if (node != null) {
					return node;
				}
				continue;
			}
			if (isStartNodeFound) {
				PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
				if (pfNode.getShowContent().indexOf((String) value) >= 0) {
					return node;
				} else {
					node = findNodeByFilter(node, textFieldName, value,
							startNode);
					if (node != null) {
						return node;
					}
				}
			} else {
				node = findNodeByFilter(node, textFieldName, value, startNode);
				if (node != null) {
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * @return the tree
	 */
	public CustomTree getTree() {
		return tree;
	}

	/**
	 * @param tree
	 *            the tree to set
	 */
	public void setTree(CustomTree tree) {
		this.tree = tree;
	}
	
	/**
	 * Ftree的设置
	 * @param fTree
	 */
	public void setFTree(FTree fTree) {
		this.fTree = fTree;
	}

	private void findFirst() {
		if (tree != null) {
			String key = (String) txtKey.getValue();
			if ((key != null) && (key.trim().length() > 0)) {
				try {
					DefaultMutableTreeNode node = null;
					DefaultMutableTreeNode root = tree.getRoot();
					String textFieldName = tree.getTextName();
					node = findNodeByFilter(root, textFieldName, key);
					if (node != null) {
						tree.expandTo(node);
						TreePath path = new TreePath(node.getPath());
						tree.setSelectionPath(path);
						tree.scrollPathToVisible(path);
					} else {
						MessageBox.msgBox(Global.mainFrame, "没有匹配项！",
								MessageBox.INFOMATION);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 在ftree上查找
	 *
	 */
	private void findFTreeFirst() {
		if (fTree != null) {
			String key = (String) txtKey.getValue();
			if ((key != null) && (key.trim().length() > 0)) {
				try {
					DefaultMutableTreeNode node = null;
					DefaultMutableTreeNode root = fTree.getRoot();
					String textFieldName = "";
					node = findNodeByFilter(root, textFieldName, key);
					if (node != null) {
						TreePath path = new TreePath(node.getPath());
						fTree.expandPath(path);
						fTree.setSelectionPath(path);
						fTree.scrollPathToVisible(path);
					} else {
						MessageBox.msgBox(Global.mainFrame, "没有匹配项！",
								MessageBox.INFOMATION);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void findNext() {
		if (tree != null) {
			String key = (String) txtKey.getValue();
			if ((key != null) && (key.trim().length() > 0)) {
				try {
					TreeNode startNode = tree.getSelectedNode();
					isStartNodeFound = false;

					DefaultMutableTreeNode node = null;
					DefaultMutableTreeNode root = tree.getRoot();
					String textFieldName = tree.getTextName();
					node = findNodeByFilter(root, textFieldName, key, startNode);
					if (node != null) {
						tree.expandTo(node);
						TreePath path = new TreePath(node.getPath());
						tree.setSelectionPath(path);
						tree.scrollPathToVisible(path);
					} else {
						MessageBox msgbx = new MessageBox(Global.mainFrame,
								"没有匹配项！从头查找？", MessageBox.INFOMATION,
								MessageBox.BUTTON_OK | MessageBox.BUTTON_CANCEL);
						msgbx.setVisible(true);
						if (msgbx.getResult() == MessageBox.OK) {
							findFirst();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 在ftree上查找
	 *
	 */
	private void findFTreeNext() {
		if (fTree != null) {
			String key = (String) txtKey.getValue();
			if ((key != null) && (key.trim().length() > 0)) {
				try {
					PfTreeNode startNode = fTree.getSelectedPfTreeNode();
					isStartNodeFound = false;

					DefaultMutableTreeNode node = null;
					DefaultMutableTreeNode root = fTree.getRoot();
					String textFieldName = "";
					node = findNodeByFilter(root, textFieldName, key, startNode);
					if (node != null) {
						TreePath path = new TreePath(node.getPath());
						fTree.expandPath(path); 
						fTree.setSelectionPath(path);
						fTree.scrollPathToVisible(path);
					} else {
						MessageBox msgbx = new MessageBox(Global.mainFrame,
								"没有匹配项！从头查找？", MessageBox.INFOMATION,
								MessageBox.BUTTON_OK | MessageBox.BUTTON_CANCEL);
						msgbx.setVisible(true);
						if (msgbx.getResult() == MessageBox.OK) {
							findFirst();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
