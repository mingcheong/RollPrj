/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * <p>
 * Title:勾选框的列表组件
 * </p>
 * <p>
 * Description:勾选框的列表组件
 * </p>
 * <p>

 */
public class CheckBoxList extends JScrollPane {

	private static final long serialVersionUID = 1L;

	protected JList m_list;

	private List mouseListeners;

	private List keyListeners;

	private boolean checkEnable = true;

	public CheckBoxList() {
		m_list = new JList();
		init();
	}

	public CheckBoxList(InstallData[] data) {
		m_list = new JList(data);
		init();
	}

	private void init() {
		CheckListCellRenderer renderer = new CheckListCellRenderer();
		m_list.setCellRenderer(renderer);
		m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CheckListener checkListener = new CheckListener(m_list);
		m_list.addMouseListener(checkListener);
		m_list.addKeyListener(checkListener);
		this.getViewport().add(m_list);
	}

	private class CheckListCellRenderer extends JCheckBox implements
			ListCellRenderer {

		private static final long serialVersionUID = 1L;

		protected Border m_noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		public CheckListCellRenderer() {
			super();
			setOpaque(true);
			setBorder(m_noFocusBorder);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());

			setBackground(isSelected ? list.getSelectionBackground() : list
					.getBackground());
			setForeground(isSelected ? list.getSelectionForeground() : list
					.getForeground());

			InstallData data = (InstallData) value;
			setSelected(data.isSelected());
			//
			setFont(list.getFont());
			setBorder((cellHasFocus) ? UIManager
					.getBorder("List.focusCellHighlightBorder")
					: m_noFocusBorder);

			return this;
		}
	}

	private class CheckListener implements MouseListener, KeyListener {
		JList m_list;

		public CheckListener(JList list) {
			m_list = list;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getX() < 20 && CheckBoxList.this.getIsCheckBoxEnabled())
				doCheck();
			if (mouseListeners == null)
				return;
			for (int i = mouseListeners.size() - 1; i >= 0; i--) {
				((MouseListener) mouseListeners.get(i)).mouseClicked(e);
			}
		}

		public void mousePressed(MouseEvent e) {
			if (mouseListeners == null)
				return;
			for (int i = mouseListeners.size() - 1; i >= 0; i--) {
				((MouseListener) mouseListeners.get(i)).mousePressed(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (mouseListeners == null)
				return;
			for (int i = mouseListeners.size() - 1; i >= 0; i--) {
				((MouseListener) mouseListeners.get(i)).mouseReleased(e);
			}
		}

		public void mouseEntered(MouseEvent e) {
			if (mouseListeners == null)
				return;
			for (int i = mouseListeners.size() - 1; i >= 0; i--) {
				((MouseListener) mouseListeners.get(i)).mouseEntered(e);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (mouseListeners == null)
				return;
			for (int i = mouseListeners.size() - 1; i >= 0; i--) {
				((MouseListener) mouseListeners.get(i)).mouseExited(e);
			}
		}

		public void keyPressed(KeyEvent e) {
			// if (e.getKeyChar() == " ")
			if (CheckBoxList.this.getIsCheckBoxEnabled())
				doCheck();
			if (keyListeners == null)
				return;
			for (int i = keyListeners.size() - 1; i >= 0; i--) {
				((KeyListener) keyListeners.get(i)).keyPressed(e);
			}
		}

		public void keyTyped(KeyEvent e) {
			if (keyListeners == null)
				return;
			for (int i = keyListeners.size() - 1; i >= 0; i--) {
				((KeyListener) keyListeners.get(i)).keyTyped(e);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (keyListeners == null)
				return;
			for (int i = keyListeners.size() - 1; i >= 0; i--) {
				((KeyListener) keyListeners.get(i)).keyReleased(e);
			}
		}

		protected void doCheck() {
			int index = m_list.getSelectedIndex();
			if (index < 0)
				return;
			InstallData data = (InstallData) m_list.getModel().getElementAt(
					index);
			data.invertSelected();
			m_list.repaint();
		}
	}

	/**
	 * 设置勾选状态
	 * 
	 * @param lstValue
	 * @param flag
	 */
	public void setSelected(List lstValue, boolean flag) {
		if (lstValue == null || lstValue.size() == 0) {
			return;
		}
		int length = lstValue.size();

		ListModel model = m_list.getModel();

		int size = model.getSize();
		if (size == 0)
			return;

		InstallData data;
		for (int j = 0; j < size; j++) {
			data = (InstallData) model.getElementAt(j);
			data.m_selected = !flag;
		}

		String value;
		for (int i = 0; i < length; i++) {
			value = lstValue.get(i).toString();
			for (int j = 0; j < size; j++) {
				data = (InstallData) model.getElementAt(j);
				if (!value.equalsIgnoreCase(data.getValue().toString())) {
					continue;
				}
				data.m_selected = flag;
				break;
			}
		}
		m_list.repaint();
	}

	/**
	 * 设置显示信息
	 * 
	 * @param data
	 */
	public void setData(InstallData[] data) {
		DefaultListModel model;
		if (m_list.getModel() instanceof DefaultListModel) {
			model = (DefaultListModel) m_list.getModel();
			model.removeAllElements();
		} else {
			model = new DefaultListModel();
			m_list.setModel(model);
		}
		if (data != null) {
			int size = data.length;
			for (int i = 0; i < size; i++) {
				model.addElement(data[i]);
			}
		}
	}

	/**
	 * 得到数据内容
	 * 
	 * @return
	 */
	public InstallData[] getData() {
		DefaultListModel model = (DefaultListModel) m_list.getModel();
		int size = model.size();
		InstallData[] data = new InstallData[size];
		for (int i = 0; i < size; i++) {
			data[i] = (InstallData) model.get(i);
		}
		return data;
	}

	/**
	 * 得到选中的数据值
	 * 
	 * @return
	 */
	public List getSelectData() {
		List lstData = new ArrayList();
		DefaultListModel model = (DefaultListModel) m_list.getModel();
		int size = model.size();
		InstallData[] data = new InstallData[size];
		for (int i = 0; i < size; i++) {
			data[i] = (InstallData) model.get(i);
			if (data[i].isSelected()) {
				lstData.add(data[i].getValue());
			}
		}
		return lstData;
	}

	/**
	 * 鼠标事件
	 */
	public void addMouseListener(MouseListener ml) {
		if (mouseListeners == null) {
			mouseListeners = new ArrayList();
		}
		this.mouseListeners.add(ml);
	}

	public void addKeyListener(KeyListener kl) {
		if (keyListeners == null) {
			keyListeners = new ArrayList();
		}
		this.keyListeners.add(kl);
	}

	/**
	 * 是否可选
	 * 
	 * @param checkBoxEnabled
	 */
	public void setIsCheckBoxEnabled(boolean checkBoxEnabled) {
		this.checkEnable = checkBoxEnabled;
	}

	public boolean getIsCheckBoxEnabled() {
		return checkEnable;
	}

	/**
	 * 取消所有已选择的节点
	 * 
	 */
	public void cancelSelected() {
		ListModel model = m_list.getModel();

		int size = model.getSize();
		if (size == 0)
			return;

		InstallData data;
		for (int j = 0; j < size; j++) {
			data = (InstallData) model.getElementAt(j);
			data.m_selected = false;
		}
		m_list.repaint();
	}
}
