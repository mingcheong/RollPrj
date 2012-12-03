/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
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

	// û��check��
	public static final int iNoCheck = 0;

	// ��check��
	public static final int iChcek = 1;

	// �Ƿ�ȷ���޸�
	public boolean modified = false;

	private String fieldName; // ������ʾ���ֶ���

	private String aFieldName; // �����ڵ���ֶ���

	private Object ob;

	// ��
	CustomTree tree;

	DataSet ds;

	int Flag;

	FTextField prjTextField;

	FButton findButton; // �����ҡ���ť

	FScrollPane treePanel;

	public TreeUI(String rootName, DataSet dataset, String aFieldID,
			String aFieldName, String parID, int i, SysCodeRule codeRule,
			Object obj, String fieldCName, String sortKey) throws Exception {
		super(Global.mainFrame);
		this.setSize(500, 600);
		this.setResizable(false);

		this.dispose();
		this.setTitle("��ѡ��");
		this.setModal(true);
		ob = obj;
		ds = dataset;
		Flag = i;
		fieldName = fieldCName; // ������ʾ���ֶ�
		this.aFieldName = aFieldName; // �����ڵ���ֶ���
		ds.beforeFirst();
		ds.next();

		// �������Ϸ���panel����Ϊ������panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(10);

		// �������������
		prjTextField = new FTextField();
		prjTextField.setTitle("�������������:");
		prjTextField.setProportion(0.25f);

		// �������Ұ�ť
		FFlowLayoutPanel findPanel = new FFlowLayoutPanel();
		findPanel.setAlignment(FlowLayout.CENTER);
		// ���Ұ�ť
		findButton = new FButton();
		findButton.setText("����");
		findButton.addActionListener(new searchAction());

		FButton findNextButton = new FButton();
		findNextButton.setText("������һ��");
		findPanel.addControl(findButton);
		findNextButton.addActionListener(new searchNextAction());

		findPanel.addControl(findNextButton);

		// ��
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

		// ˫�����ڵ㣬����ֵ���͡�ȷ������ť����һ����
		tree.addMouseListener(new treeListener());

		// "ȷ��"��"ȡ��"��ť
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// ȷ����ť
		FButton okButton = new FButton();
		okButton.setText("ȷ��");
		// �����¼�
		okButton.addActionListener(new OkButtonListener());
		// ȡ����ť
		FButton cancelButton = new FButton();
		cancelButton.setText("ȡ��");
		// �����¼�
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modified = false;
				dispose();
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		// ��Ԫ�ض����뵽mainPanel��
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

	// ��ȡ����ֵ
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

	// ƥ��ؼ�,Ϊ�ؼ���ֵ
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

	// ȷ����ť�ļ����¼�
	class OkButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			modified = true;
			if ((ob == null) && (Common.isNullStr(fieldName))) {
				setVisible(false);
			} else {
				try {
					if (tree.getSelectedNode() == null) {
						JOptionPane.showMessageDialog(TreeUI.this, "��ѡ��һ��ֵ��");
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
						JOptionPane.showMessageDialog(TreeUI.this, "��ѡ��ĩ����㣡");
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

	}

	// ˫�����ڵ㣬�����¼�
	public class treeListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				if ((ob == null) && (Common.isNullStr(fieldName))) {
					setVisible(false);
				} else {
					try {
						if (tree.getSelectedNode() == null) {
							// JOptionPane.showMessageDialog(treeui, "��ѡ��һ��ֵ��");
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
							// "��ѡ��ĩ����㣡");
							return;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}

	// [����]��ť�����¼�
	public class searchAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String findval = prjTextField.getValue().toString();
			if ("".equals(findval)) {
				JOptionPane.showMessageDialog(TreeUI.this, "��������ҵ�����");
				return;
			}

			try {
				if (!tree.blurExpandTo(aFieldName, findval, true)) {
					JOptionPane.showMessageDialog(TreeUI.this, "û�в��ҵ������Ϣ��");
					return;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	// [������һ��]��ť�����¼�
	public class searchNextAction implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String findval = prjTextField.getValue().toString();
			if ("".equals(findval)) {
				JOptionPane.showMessageDialog(TreeUI.this, "��������ҵ�����");
				return;
			}

			try {
				if (!tree.blurExpandTo(aFieldName, findval, false)) {
					JOptionPane.showMessageDialog(TreeUI.this, "�Ѿ������һ����");
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
