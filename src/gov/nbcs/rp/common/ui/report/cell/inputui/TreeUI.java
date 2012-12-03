/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.inputui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class TreeUI extends AbstractInputDialog { 

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// 没有check框
	public static final int iNoCheck = 0;

	// 有check框
	public static final int iChcek = 1;

	// 是否确定修改
	public boolean modified = false;

	private String fieldName; // 返回显示的字段名

	private String aFieldName; // 绑定树节点的字段名

	private Object ob;

	// 树
	CustomTree tree;

	DataSet ds;

	int Flag;

	FTextField prjTextField;

	FButton findButton; // 【查找】按钮

	FScrollPane treePanel;

	public TreeUI(String rootName, DataSet dataset, String aFieldID,
			String aFieldName, String parID, int i, SysCodeRule codeRule,
			Object obj, String fieldCName, String sortKey) throws Exception {
		super(Global.mainFrame);
		this.setSize(500, 600);
		this.setResizable(false);

		this.dispose();
		this.setTitle("请选择");
		this.setModal(true);
		ob = obj;
		ds = dataset;
		Flag = i;
		fieldName = fieldCName; // 返回显示的字段
		this.aFieldName = aFieldName; // 绑定树节点的字段名
		ds.beforeFirst();
		ds.next();

		// 主窗体上放入panel，作为主布局panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(10);

		// 请输入查找内容
		prjTextField = new FTextField();
		prjTextField.setTitle("请输入查找内容:");
		prjTextField.setProportion(0.25f);

		// 两个查找按钮
		FFlowLayoutPanel findPanel = new FFlowLayoutPanel();
		findPanel.setAlignment(FlowLayout.CENTER);
		// 查找按钮
		findButton = new FButton();
		findButton.setText("查找");
		findButton.addActionListener(new searchAction());

		FButton findNextButton = new FButton();
		findNextButton.setText("查找下一个");
		findPanel.addControl(findButton);
		findNextButton.addActionListener(new searchNextAction());

		findPanel.addControl(findNextButton);

		// 树
		treePanel = new FScrollPane();
		if (Flag == iChcek) {
			tree = new CustomTree(rootName, ds, aFieldID, aFieldName, parID,
					codeRule, sortKey, true);
			tree.setIsCheckBoxEnabled(true);
			// tree.setIsCheck(true);
		} else {
			tree = new CustomTree(rootName, ds, aFieldID, aFieldName, parID,
					codeRule, sortKey);			
		}
		treePanel.addControl(tree);

		// 双击树节点，返回值（和【确定】按钮功能一样）
		tree.addMouseListener(new treeListener());

		// "确定"、"取消"按钮
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// 确定按钮
		FButton okButton = new FButton();
		okButton.setText("确定");
		// 监听事件
		okButton.addActionListener(new OkButtonListener());
		// 取消按钮
		FButton cancelButton = new FButton();
		cancelButton.setText("取消");
		// 监听事件
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modified = false;
				dispose();
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		// 把元素都加入到mainPanel中
		mainPanel.addControl(prjTextField, new TableConstraints(1, 1, false,
				true));
		mainPanel
				.addControl(findPanel, new TableConstraints(2, 1, false, true));
		mainPanel.add(treePanel, new TableConstraints(8, 1, true, true));
		mainPanel.addControl(choosePanel, new TableConstraints(2, 1, false,
				true));
		this.getContentPane().add(mainPanel);
	}

	public TreeUI(String rootName, DataSet dataset, String aFieldID,
			String aFieldName, String parID, int i, SysCodeRule codeRule,
			Object obj, String fieldCName) throws Exception {
		this(rootName, dataset, aFieldID, aFieldName, parID, i, codeRule, obj,
				fieldCName, "");
	}

	public CustomTree getTree() {
		return this.tree;
	}

	public void setTree(CustomTree tree) {
		treePanel.removeAll();
		this.tree = tree;
		treePanel.addControl(this.tree);
		treePanel.updateUI();
	}

	// 获取返回值
	public Object getValue() throws Exception {
		if (modified) {
			if (!(tree.getSelectedNode() == null)) {
				if (Flag == iNoCheck) {
					return ds.fieldByName(fieldName).getString();
				} else {
					String string = new String();
					MyTreeNode[] node = tree.getSelectedNodes(true);
					for (int i = 0; i < node.length; i++) {
						String bookmark = node[i].getBookmark();
						ds.gotoBookmark(bookmark);
						if (i != node.length - 1) {
							string += ds.fieldByName(fieldName).getString()
									+ ";";
						} else {
							string += ds.fieldByName(fieldName).getString();
						}
					}
					return string;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	// 匹配控件,为控件赋值
	public void resultObject(Object ob) throws Exception {
		if (ob instanceof FTextField) {
			((FTextField) ob).setValue(getValue());
		}
		if (ob instanceof FTextArea) {
			((FTextArea) ob).setValue(getValue());
		}
		if (ob instanceof FList) {
			DefaultListModel listModel = (DefaultListModel) ((FList) ob)
					.getModel();
			((FList) ob).setModel(listModel);
			listModel.addElement(getValue());
		}
		if (ob instanceof JTextField) {
			if (getValue() != null) {
				((JTextField) ob).setText(getValue().toString());
			}
		}
	}

	public void setUITitle(String title) {
		this.setTitle(title);
	}

	// 确定按钮的监听事件
	class OkButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			modified = true;
			if ((ob == null) && (Common.isNullStr(fieldName))) {
				setVisible(false);
			} else {
				try {
					if (tree.getSelectedNode() == null) {
						JOptionPane.showMessageDialog(TreeUI.this, "请选择一个值！");
						return;
					}
					if ((tree.getSelectedNode() != null)
							&& tree.getSelectedNode().isLeaf()) {
						if (ob != null) {
							resultObject(ob);
						}
						dispatchEvent(new WindowEvent(TreeUI.this,
								WindowEvent.WINDOW_CLOSING));
						// setVisible(false);
					} else {
						JOptionPane.showMessageDialog(TreeUI.this, "请选择末级结点！");
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

	}

	// 双击树节点，监听事件
	public class treeListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				if ((ob == null) && (Common.isNullStr(fieldName))) {
					setVisible(false);
				} else {
					try {
						if (tree.getSelectedNode() == null) {
							// JOptionPane.showMessageDialog(treeui, "请选择一个值！");
							return;
						}
						if ((tree.getSelectedNode() != null)
								&& tree.getSelectedNode().isLeaf()) {
							modified = true;
							if (ob != null) {
								resultObject(ob);
							}
							dispatchEvent(new WindowEvent(TreeUI.this,
									WindowEvent.WINDOW_CLOSING));
							// setVisible(false);
						} else {
							// JOptionPane.showMessageDialog(treeui,
							// "请选择末级结点！");
							return;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}

	// [查找]按钮监听事件
	public class searchAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String findval = prjTextField.getValue().toString();
			if ("".equals(findval)) {
				JOptionPane.showMessageDialog(TreeUI.this, "请输入查找的内容");
				return;
			}

			try {
				if (!tree.blurExpandTo(aFieldName, findval, true)) {
					JOptionPane.showMessageDialog(TreeUI.this, "没有查找到相关信息！");
					return;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	// [查找下一个]按钮监听事件
	public class searchNextAction implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String findval = prjTextField.getValue().toString();
			if ("".equals(findval)) {
				JOptionPane.showMessageDialog(TreeUI.this, "请输入查找的内容");
				return;
			}

			try {
				if (!tree.blurExpandTo(aFieldName, findval, false)) {
					JOptionPane.showMessageDialog(TreeUI.this, "已经到最后一条！");
					return;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

}
