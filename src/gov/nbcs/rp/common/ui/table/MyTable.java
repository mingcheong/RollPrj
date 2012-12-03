/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
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
	 * �Ƿ���CheckBox�ؼ�
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
			return render;// �õİ�,�����
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
		 * ������ͷ�ĵ���¼��������ѡ/��ѡ�˱�ͷ��CheckBox������ȫѡ/��ѡ����ͬ��CheckBox
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
	 * ������<code>JTable.createDefaultTableHeader()</code>����
	 * ���һ���Զ���ı�ͷ��ʾ�࣬����CheckBox����
	 */
	protected JTableHeader createDefaultTableHeader() {
		JTableHeader header = new MyTableHeader(columnModel);
		this.attachMouseEvent(header);
		return header;
	}
}
