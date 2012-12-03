/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class MyTable extends JTable {
	/**
	 * 是否有CheckBox控件
	 */
	boolean hasCheck;

	CustomTable parent;

	MyTable() {
		super();
		setTableHeader(createDefaultTableHeader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#getSelectedRows()
	 */
	public int[] getSelectedRows() {
		if (!hasCheck) {
			return super.getSelectedRows();
		} else {
			TableModel model = this.getModel();
			List rows = new ArrayList();
			for (int i = 0; i < model.getRowCount(); i++) {
				Boolean check = (Boolean) model.getValueAt(i, 0);
				if (check.booleanValue()) {
					rows.add(new Integer(i));
				}
			}
			int result[] = new int[rows.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = ((Integer) rows.get(i)).intValue();
			}
			return result;
		}
	}

	public static class MyTableHeader extends JTableHeader {
		private String checkLabel;

		public String getCheckLabel() {
			return checkLabel;
		}

		public void setCheckLabel(String checkLabel) {
			this.checkLabel = checkLabel;
			((MyHeaderRenderer)this.getDefaultRenderer()).setCheckLabel(checkLabel);
		}

		public MyTableHeader(TableColumnModel cm) {
			super(cm);
			this.setDefaultRenderer(createDefaultRenderer());
		}

		protected TableCellRenderer createDefaultRenderer() {
			MyHeaderRenderer render = new MyHeaderRenderer();
			if (checkLabel != null) {
				render.setCheckLabel(checkLabel);
			}
			return render;// 好的啊,很舒服
		}
	}

	public void setTableHeader(JTableHeader header) {
		if ((header != null) && (header != this.tableHeader)) {
			super.setTableHeader(header);
			this.attachMouseEvent(header);
		}
	}

	protected void attachMouseEvent(JTableHeader header) {
		/**
		 * 侦听表头的点击事件，如果勾选/反选了表头的CheckBox，将会全选/反选表内同列CheckBox
		 */
		header.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (hasCheck) {
					JTableHeader h = (JTableHeader) e.getSource();
					TableColumnModel columnModel = h.getColumnModel();
					int viewColumn = columnModel.getColumnIndexAtX(e.getX());
					int column = columnModel.getColumn(viewColumn)
							.getModelIndex();
					if (column == 0) {
						MyHeaderRenderer renderer = (MyHeaderRenderer) h
								.getDefaultRenderer();
						renderer.setChecked(!renderer.isChecked());
						if (renderer.isChecked()) {
							parent.checkAll();
						} else {
							parent.unCheckAll();
						}
					}
				}
			}
		});
	}

	/**
	 * 覆盖了<code>JTable.createDefaultTableHeader()</code>方法
	 * 获得一个自定义的表头显示类，带有CheckBox功能
	 */
	protected JTableHeader createDefaultTableHeader() {
		JTableHeader header = new MyTableHeader(columnModel);
		this.attachMouseEvent(header);
		return header;
	}
}
