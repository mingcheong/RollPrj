package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.table.DefaultTableModel;

import gov.nbcs.rp.common.ui.table.TableSorter;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.incors.plaf.alloy.AlloyComboBoxUI;
import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * 下拉列表框
 * 

 * 
 */
public class TableComboBox extends JComboBox {

	private static final long serialVersionUID = 1L;

	private FPanel fPanel;

	private JTable jTable;

	String key = null;

	String value = null;

	public TableComboBox() {
		this.setEditable(false);
		this.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent arg0) {
			}

			public void componentMoved(ComponentEvent arg0) {

			}

			public void componentResized(ComponentEvent arg0) {
				Dimension dimension = new Dimension(TableComboBox.this
						.getWidth() - 10, 160);
				fPanel.setSize(dimension);
				fPanel.setPreferredSize(dimension);
			}

			public void componentShown(ComponentEvent arg0) {

			}
		});
		updateUIThis();
	}

	public void updateUIThis() {
		ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
		if (cui instanceof MetalComboBoxUI) {
			cui = new CustomMetalComboBoxUI();
		} else if (cui instanceof MotifComboBoxUI) {
			cui = new CustomMotifComboBoxUI();
		} else if (cui instanceof WindowsComboBoxUI) {
			cui = new CustomWindowsComboBoxUI();
		} else if (cui instanceof AlloyComboBoxUI) {
			cui = new CustomAlloyComboBoxUI();
		}
		this.setUI(cui);
	}

	private class CustomMetalComboBoxUI extends MetalComboBoxUI {
		protected ComboPopup createPopup() {
			return new TablePopup(this.comboBox);
		}
	}

	private class CustomMotifComboBoxUI extends MotifComboBoxUI {

		private static final long serialVersionUID = 1L;

		protected ComboPopup createPopup() {
			return new TablePopup(this.comboBox);
		}
	}

	private class CustomWindowsComboBoxUI extends WindowsComboBoxUI {
		protected ComboPopup createPopup() {
			return new TablePopup(this.comboBox);
		}
	}

	private class CustomAlloyComboBoxUI extends AlloyComboBoxUI {
		protected ComboPopup createPopup() {
			return new TablePopup(this.comboBox);
		}
	}

	private class TablePopup extends BasicComboPopup {

		private static final long serialVersionUID = 1L;

		public TablePopup(JComboBox arg0) {
			super(arg0);
			this.setLayout(new FlowLayout());
			fPanel = new FPanel();
			fPanel.setLayout(new RowPreferedLayout(1));
			jTable = new JTable();

			jTable.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent arg0) {
					setOpaque(true);

				}

				public void mouseExited(MouseEvent arg0) {
					setOpaque(false);
				}

				public void mousePressed(MouseEvent arg0) {
					setSelectedItem(jTable.getValueAt(jTable.getSelectedRow(),
							0));
					key = jTable.getValueAt(jTable.getSelectedRow(), 1)
							.toString();
					value = jTable.getValueAt(jTable.getSelectedRow(), 0)
							.toString();
				}

				public void mouseReleased(MouseEvent arg0) {
					comboBox.setPopupVisible(false);
					comboBox.setOpaque(false);
					jTable.setFocusable(false);
				}
			});
			JScrollPane jScrollPane = new JScrollPane(jTable);
			fPanel.add(jScrollPane, new TableConstraints(1, 1, true, true));
			this.removeAll();
			this.add(fPanel);
		}
	}

	public void setSelectedItem(Object item) {
		removeAllItems();
		addItem(item);
		super.setSelectedItem(item);
	}

	public void setModel(Object[][] data, Object[] header) {
		Object sRecord[] = new Object[data[0].length];
		DefaultTableModel model = new DefaultTableModel();
		for (int i = 0; i < header.length; i++) {
			model.addColumn(header[i]);
		}
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				sRecord[j] = data[i][j];
			}
			model.addRow(sRecord);
		}
		TableSorter sorter = new TableSorter(model);
		jTable.setModel(sorter);

	}

	/**
	 * 设置隐藏列
	 * 
	 * @param iCol
	 */
	public void setHideCol(int iCol) {
		jTable.getTableHeader().getColumnModel().getColumn(iCol).setWidth(0);
		jTable.getTableHeader().getColumnModel().getColumn(iCol).setMaxWidth(0);
		jTable.getTableHeader().getColumnModel().getColumn(iCol).setMinWidth(0);
		jTable.getTableHeader().getColumnModel().getColumn(iCol)
				.setPreferredWidth(0);
	}

	public void setModel(DefaultTableModel model) {
		TableSorter sorter = new TableSorter(model);
		jTable.setModel(sorter);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public String getValue(int iCol) {
		return jTable.getValueAt(jTable.getSelectedRow(), iCol).toString();
	}

	public void setValue(String value) {
		this.value = value;
		this.setSelectedItem(value);
	}

}
