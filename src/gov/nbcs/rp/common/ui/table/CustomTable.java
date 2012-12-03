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

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

public class CustomTable extends FPanel implements DataChangeListener,
		ListSelectionListener {
	/**
	 * Table�󶨵����ݼ�
	 */
	DataSet dataSet;

	/**
	 * �����ֶ�����ÿ����¼������ǩʱ������ֶα�ʶ��ǩ��һ�У�
	 */
	static final String HIDE_FIELD_BOOKMARK = UUID.randomUUID().toString();

	/**
	 * CheckBox��һ�е��ֶα�ʶ
	 */
	public static final String CHECK_FIELD = UUID.randomUUID().toString();

	/**
	 * ע��ĸ�ʽ��patter�����ֶ���Ϊ�ؼ���Key
	 */
	private Map formatterMap;

	/**
	 * ���ñ����Ա༭���У����ֶ���Ϊ�ؼ���Key
	 */
	private Set editableCache;

	/**
	 * �Ƿ���CheckBox
	 */
	private boolean hasCheck;

	private String[] columnText;

	private String[] columnField;

	private LinkedList selectionListeners;

	private Map componentMap = new MyMap();

	/**
	 * ʵ�ʴ�����ݵ�JTable�������
	 */
	MyTable table = new MyTable();

	/**
	 * ����JTable��ScrollPane��Swing��Ҫ��ScrollPane������Table�ؼ�������Ҫ���������ͷ��ʾ��
	 */
	JScrollPane contentPane = new JScrollPane(table);

	public void setColumnField(String[] columnField) {
		this.columnField = columnField;
	}

	public void setColumnText(String[] columnText) {
		this.columnText = columnText;
	}

	/**
	 * �����������Ż�ö�ӦDataSet��¼����ǩ
	 * 
	 * @param rowIndex
	 *            �к�
	 * @return
	 */
	public String rowToBookmark(int rowIndex) {
		return (String) table.getModel().getValueAt(rowIndex,
				table.getModel().getColumnCount() - 1);
	}

	/**
	 * �������
	 * 
	 * @param columnText
	 *            ��ͷ�ı�
	 * @param columnField
	 *            ��ͷ�ֶ�
	 * @param ds
	 *            ��������
	 * 
	 * @param editFields
	 *            �ɱ༭�ֶ�
	 */
	public CustomTable(String[] columnText, String[] columnField, DataSet ds,
			boolean hasCheck, String[] editFields) throws Exception {
		this.setLayout(new RowPreferedLayout(1));
		table.getSelectionModel().addListSelectionListener(this);
		contentPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(contentPane, new TableConstraints(1, 1, true, true));
		if ((columnText != null) && (columnText != null)) {
			if (columnText.length != columnField.length) {
				throw new IllegalArgumentException(
						"Error occurs on creating table column,your \"columnText\" array's size must match the size of \"columnField\"");
			}
		}
		this.hasCheck = hasCheck;
		if (hasCheck) {
			this.setCanMultiSelect(true);
		}
		table.hasCheck = hasCheck;
		table.parent = this;
		setDataSet(ds);
		this.setColumnField(columnField);
		this.setColumnText(columnText);
		this.setEditable(editFields);

		// ���Ƕ�ѡ����Ӷ�ѡ����
		// ע�⣺���ж�ѡ���ܣ�������ʹ��getTable().getTableHeader().setReorderingAllowed(false)
		if (hasCheck) {
			// ���ӱ�ͷ��һ�е���Ϣ,�������,ȫѡû������,��ȻҲ���Դ����¼�.����,��View�ϱ��ֲ�����
			MyTable.MyTableHeader header = new MyTable.MyTableHeader(this
					.getTable().getColumnModel());
			header.setCheckLabel("");
			this.getTable().setTableHeader(header);
		}
	}

	public CustomTable(String[] columnText, String[] columnField, DataSet ds,
			boolean hasCheck) throws Exception {
		this(columnText, columnField, ds, hasCheck, null);
	}

	public JScrollPane getContentPane() {
		return this.contentPane;
	}

	public void setPreferredScrollableViewportSize(Dimension portSize) {
		table.setPreferredScrollableViewportSize(portSize);
	}

	/**
	 * �����Ƿ���ԶԱ����ж�ѡ
	 * 
	 * @param multiSelect
	 */
	public void setCanMultiSelect(boolean multiSelect) {
		if (multiSelect) {
			table
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/**
	 * �����Ƿ���Ե����ͷ����
	 * 
	 * @param allowOrder
	 */
	public void setAllowOrder(boolean allowOrder) {
		((TableSorter) table.getModel()).allowOrder = allowOrder;
	}

	/**
	 * Ϊһ���ֶ�ע��һ����ʽ����Pattern
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @param format
	 *            ��ʽ���õ�Pattern
	 */
	public void setFormat(String fieldName, String format) {
		if (this.formatterMap == null) {
			this.formatterMap = new MyMap();
		}
		formatterMap.put(fieldName, format);
	}

	public void setSelectList(String fieldName, JComboBox component) {
		componentMap.put(fieldName, component);
	}

	/**
	 * ����ĳ���ֶ��Ƿ�ɱ༭
	 * 
	 * @param fieldName
	 */
	public void setEditable(String[] fieldName) {
		if ((fieldName != null) && (fieldName.length > 0)) {
			if (editableCache == null) {
				editableCache = new HashSet();
			}
			int delta = 0;// ������������CheckBox��ô������������ڱ���е���������Ҫ�ۼ�1
			if (hasCheck) {
				editableCache.add(new Integer(0));
				delta = 1;
			}

			for (int i = 0; i < fieldName.length; i++) {
				for (int j = 0; j < this.columnField.length; j++) {
					if (fieldName[i].equalsIgnoreCase(columnField[j])) {
						editableCache.add(new Integer(j + delta));
					}
				}
			}
		}
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * �������ݼ�
	 * 
	 * @param dataSet
	 */
	public void setDataSet(DataSet ds) {
		if (ds != this.dataSet) {
			this.dataSet = ds;
			this.dataSet.addDataChangeListener(this);
			this.dataSet.addCancelListener(new CancelActionListener());
			this.dataSet.addDeleteListener(new DeleteActionListener());
			this.dataSet.addInsertListener(new InsertActionListener());
		}
	}

	public JTable getTable() {
		return table;
	}

	/**
	 * �ж�һ���Ƿ�ѡ�У�����趨�˱����CheckBox���Ը����Ƿ�CheckΪ��׼
	 * 
	 * @return
	 */
	public boolean isSelected(int rowIndex) {
		TableModel model = table.getModel();
		if ((rowIndex < 0) || (rowIndex >= model.getRowCount())) {
			return false;
		}
		if (hasCheck) {
			Boolean checkValue = (Boolean) model.getValueAt(rowIndex, 0);
			return checkValue == null ? false : checkValue.booleanValue();
		} else {
			return rowIndex == table.getSelectedRow();
		}
	}

	/**
	 * ��ȡ��ѡ�е����ݼ�
	 * 
	 * @return list ��֯��ʽ��Map
	 */
	public List getHasCheckData() throws Exception {
		List list = new ArrayList();
		int count = table.getRowCount();
		for (int i = 0; i < count; i++) {
			if (isSelected(i)
					&& dataSet.gotoBookmark(this.rowToBookmark(i))) {
				list.add(dataSet.getOriginData());
			}
		}
		return list;
	}

	/**
	 * �������ɱ����������
	 * 
	 * @throws Exception
	 */
	public void reset() throws Exception {
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				if (hasCheck && (col == 0)) {
					return true;
				}
				if ((editableCache != null)
						&& editableCache.contains(new Integer(col))) {
					return true && ((getDataSet().getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT);
				}
				return false;
			}

			public Class getColumnClass(int i) {
				if (dataVector != null) {
					Object value = super.getValueAt(0, i);
					return value == null ? String.class : value.getClass();
				}
				return String.class;
			}
		};
		appendColumn(model);
		if (dataSet != null) {
			appendBody(model);

		}
		TableSorter sorter = new TableSorter(model);
		model
				.addTableModelListener(new TableDataChangeListener(this,
						hasCheck));
		sorter.setTableHeader(table.getTableHeader());
		this.table.setModel(sorter);
		setExtraColInfo(sorter);
	}

	/**
	 * ��ӱ�����
	 * 
	 * @param model
	 */
	protected void appendBody(DefaultTableModel model) throws Exception {
		dataSet.maskDataChange(true);
		dataSet.beforeFirst();
		while (dataSet.next()) {
			model.addRow(this.createRowData());
		}
		dataSet.maskDataChange(false);
	}

	/**
	 * �����еĶ�����Ϣ�������CheckBox������Ϊ��С�Ŀ�ȣ���DataSet��ǩ����Ϊ0��ȣ����Ҷ��̶����
	 * 
	 * @param sorter
	 */
	protected void setExtraColInfo(TableSorter sorter) {
		TableColumnModel columnModel = table.getColumnModel();
		if (hasCheck) {
			TableColumn col = columnModel.getColumn(0);
			col.setMinWidth(40);
			col.setMaxWidth(40);
			col.setResizable(false);
			sorter.addNotSort(0);
		}
		TableColumn col = columnModel
				.getColumn(columnModel.getColumnCount() - 1);
		col.setMinWidth(0);
		col.setMaxWidth(0);
		col.setResizable(false);
		sorter.addNotSort(columnModel.getColumnCount() - 1);
		int i = 0, reduce = 0;
		if (hasCheck) {
			columnModel.getColumn(i).setIdentifier(CustomTable.CHECK_FIELD);
			i++;
			reduce = 1;// �����CheckBox��ʱ����� columnField���±�Ҫ��ȥ1
		}
		for (; i < columnModel.getColumnCount() - 1; i++) {
			columnModel.getColumn(i)
					.setIdentifier(this.columnField[i - reduce]);
			if (this.componentMap.containsKey(this.columnField[i - reduce])) {
				columnModel.getColumn(i).setCellEditor(
						new DefaultCellEditor((JComboBox) componentMap
								.get(columnField[i - reduce])));
			}
		}
		columnModel.getColumn(i)
				.setHeaderValue(CustomTable.HIDE_FIELD_BOOKMARK);
	}

	/**
	 * �������Ϣ
	 * 
	 * @param model
	 */
	protected void appendColumn(DefaultTableModel model) {
		if (hasCheck) {
			model.addColumn(CustomTable.CHECK_FIELD);
		}
		for (int i = 0; i < this.columnField.length; i++) {
			model.addColumn(columnText[i]);
		}
		model.addColumn(CustomTable.HIDE_FIELD_BOOKMARK);
	}

	/**
	 * ����һ���������
	 * 
	 * @return
	 * @throws Exception
	 */
	protected Object[] createRowData() throws Exception {
		java.util.List dataList = new ArrayList();
		if (hasCheck) {
			Object o = dataSet.fieldByName(CHECK_FIELD).getValue();
			dataList.add(o == null ? new Boolean(false) : new Boolean(Common
					.estimate(o)));
		}
		for (int i = 0; i < columnField.length; i++) {
			dataList.add(this.getDisplayData(columnField[i]));
		}
		dataList.add(dataSet.toogleBookmark());
		Object rowData[] = dataList.toArray();
		return rowData;
	}

	/**
	 * ������ģ����Ϣ����һ���������
	 * 
	 * @param columnModel
	 * @return
	 * @throws Exception
	 */
	protected Object[] createRowData(TableColumnModel columnModel)
			throws Exception {
		Object[] rowData = new Object[columnModel.getColumnCount()];
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn col = columnModel.getColumn(i);
			String colId = Common.nonNullStr(col.getIdentifier());
			if (CustomTable.CHECK_FIELD.equalsIgnoreCase(colId)) {
				rowData[col.getModelIndex()] = new Boolean(false);
			} else if (CustomTable.HIDE_FIELD_BOOKMARK.equalsIgnoreCase(colId)) {
				rowData[col.getModelIndex()] = dataSet.toogleBookmark();
			} else {
				rowData[col.getModelIndex()] = this.getDisplayData(colId);
			}
		}
		return rowData;
	}

	/**
	 * �����ֶ���������ʾ���ݣ�������ֶ�ע���˸�ʽ��ģʽ��������и�ʽ��
	 * 
	 * @param fieldName
	 *            �ֶ���
	 * @return
	 */
	String getDisplayData(String fieldName) throws Exception {
		if ((formatterMap != null) && formatterMap.containsKey(fieldName)) {
			return dataSet.fieldByName(fieldName).getString(
					(String) formatterMap.get(fieldName));
		}
		return dataSet.fieldByName(fieldName).getString();
	}

	/**
	 * ȫѡ����checkBox
	 */
	public void checkAll() {
		checkAll(true);
	}

	/**
	 * ��ѡ����checkBox
	 */
	public void unCheckAll() {
		checkAll(false);
	}

	protected void checkAll(boolean isCheck) {
		if (hasCheck) {
			TableModel model = table.getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				model.setValueAt(new Boolean(isCheck), i, 0);
			}
		}
	}

	/**
	 * ��ȡ��ǰtable�Ƿ����ѡ��
	 * 
	 * @return
	 */
	public boolean getHasCheck() {
		return this.hasCheck;
	}

	/**
	 * �������ֶ�field����ֵ��startValue��ͷ��ѡ�л�ȡ��ѡ�У���Ҫ���ڵ�λ�б�ѡ�� ֻ��������ѡ����table
	 * 
	 * @param field
	 * @param startValue
	 * @author sst
	 * @return
	 * @throws Exception
	 */
	public int setTableCheck(String field, String startValue, boolean blCheck) {
		// �����е�����
		int count = 0;
		if (this.hasCheck && (startValue != null)) {
			TableModel model = table.getModel();
			String value = "";
			try {
				int row = -1;
				this.dataSet.beforeFirst();
				while (this.dataSet.next()) {
					row++;
					value = dataSet.fieldByName(field).getValue().toString();
					if (value != null) {
						if (value.startsWith(startValue)) {
							model.setValueAt(new Boolean(blCheck), row, 0);
							count++;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return count;
	}

	/**
	 * �Ƿ����α���ѡ���¼���DataSet�α��Ӱ��
	 */
	boolean maskValueChange;

	public void maskValueChange(boolean maskValueChange) {
		this.maskValueChange = maskValueChange;
	}

	/**
	 * ��Tableδ����CheckBox��ʱ��λDataSet�α�������������ѡ���¼� ����ʹ��<code>TableDataChangeListener</code>
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!hasCheck) {
			fireValueChanged(e);
		}
	}

	/**
	 * ���ñ�������п��
	 * 
	 * @param width
	 */
	public void setColumnWidth(int width) {
		TableColumnModel colModel = table.getColumnModel();
		for (int i = 0; i < colModel.getColumnCount(); i++) {
			colModel.getColumn(i).setMaxWidth(width);
		}
	}

	void fireValueChanged(ListSelectionEvent e) {
		if (maskValueChange) {
			return;
		}
		try {
			int index = table.getSelectedRow();
			if (!e.getValueIsAdjusting() && isSelected(index)) {
				String bookmark = this.rowToBookmark(index);
				dataSet.maskDataChange(true);
				dataSet.gotoBookmark(bookmark);
				dataSet.maskDataChange(false);
				dataSet
						.fireDataChange(this, null,
								DataChangeEvent.CURSOR_MOVED);
			}
			if (selectionListeners != null) {
				for (int i = 0; i < selectionListeners.size(); i++) {
					((ListSelectionListener) selectionListeners.get(i))
							.valueChanged(e);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class CancelActionListener implements DataSetProcListener {
		public void beforeProc(DataSetEvent event) throws Exception {
		}

		public void afterProc(DataSetEvent event) throws Exception {
			reset();
		}
	}

	class DeleteActionListener implements DataSetProcListener {
		public void beforeProc(DataSetEvent event) throws Exception {
			int idx = bookmarkToRow(dataSet.toogleBookmark());
			if (idx >= 0) {
				CustomTable.this.maskValueChange(true);
				((DefaultTableModel) ((TableSorter) table.getModel())
						.getTableModel()).removeRow(idx);
				CustomTable.this.maskValueChange(false);
			}
		}

		public void afterProc(DataSetEvent event) throws Exception {
		}
	}

	class InsertActionListener implements DataSetProcListener {
		public void beforeProc(DataSetEvent event) throws Exception {
		}

		public void afterProc(DataSetEvent event) throws Exception {
			DefaultTableModel model = (DefaultTableModel) ((TableSorter) table
					.getModel()).getTableModel();
			if ((table.getSelectedRow() >= 0)
					&& (dataSet.getRecordState() == DataSet.FOR_INSERT)) {
				model.insertRow(table.getSelectedRow(), CustomTable.this
						.createRowData(table.getColumnModel()));
			} else {
				model.addRow(CustomTable.this.createRowData(table
						.getColumnModel()));
			}
		}
	}

	/**
	 * �����б���ǩ�������±��
	 * 
	 * @param bookmark
	 *            �б���ǩ
	 * @return
	 */
	public int bookmarkToRow(String bookmark) {
		int columnCount = table.getColumnCount();
		TableSorter model = (TableSorter) table.getModel();
		for (int i = 0; i < table.getRowCount(); i++) {
			String _bookmark = (String) model.getValueAt(i, columnCount - 1);
			if (_bookmark.equals(bookmark)) {
				return i;
			}
		}
		return -1;
	}

	public void addListSelectionListener(ListSelectionListener listener) {
		if (this.selectionListeners == null) {
			selectionListeners = new LinkedList();
		}
		selectionListeners.addFirst(listener);
	}

	/**
	 * ���ݼ����ݱ仯��ʱ��ı�������Ԫ�������
	 */
	public void onDataChange(DataChangeEvent event) throws Exception {
		if (event.type() != DataChangeEvent.FIELD_MODIRED) {
			return;
		}
		if ((!dataSet.bof() && !dataSet.eof()) || dataSet.isModified()) {
			Field field = (Field) event.getSource();
			int index = bookmarkToRow(dataSet.toogleBookmark());
			DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
					.getColumnModel();
			DefaultTableModel model = (DefaultTableModel) ((TableSorter) table
					.getModel()).getTableModel();
			int cols = colModel.getColumnCount();
			if (index >= 0) {
				if (field.isLatestModified()) {
					for (int i = 0; i < cols; i++) {
						if (Common.nonNullStr(
								colModel.getColumn(i).getIdentifier())
								.equalsIgnoreCase(field.getName())) {
							int colIndex = colModel.getColumn(i)
									.getModelIndex();
							model.setValueAt(getDisplayData(field.getName()),
									index, colIndex);
							break;
						}
					}
				}
			}
		}
	}
}