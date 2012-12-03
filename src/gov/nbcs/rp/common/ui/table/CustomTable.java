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
	 * Table绑定的数据集
	 */
	DataSet dataSet;

	/**
	 * 隐藏字段名（每条记录生成书签时用这个字段标识书签那一列）
	 */
	static final String HIDE_FIELD_BOOKMARK = UUID.randomUUID().toString();

	/**
	 * CheckBox那一列的字段标识
	 */
	public static final String CHECK_FIELD = UUID.randomUUID().toString();

	/**
	 * 注册的格式化patter，以字段名为关键字Key
	 */
	private Map formatterMap;

	/**
	 * 设置表格可以编辑的列，以字段名为关键字Key
	 */
	private Set editableCache;

	/**
	 * 是否有CheckBox
	 */
	private boolean hasCheck;

	private String[] columnText;

	private String[] columnField;

	private LinkedList selectionListeners;

	private Map componentMap = new MyMap();

	/**
	 * 实际存放数据的JTable子类对象
	 */
	MyTable table = new MyTable();

	/**
	 * 放置JTable的ScrollPane（Swing需要用ScrollPane来放置Table控件，否则要单独处理表头显示）
	 */
	JScrollPane contentPane = new JScrollPane(table);

	public void setColumnField(String[] columnField) {
		this.columnField = columnField;
	}

	public void setColumnText(String[] columnText) {
		this.columnText = columnText;
	}

	/**
	 * 根据行索引号获得对应DataSet记录的书签
	 * 
	 * @param rowIndex
	 *            行号
	 * @return
	 */
	public String rowToBookmark(int rowIndex) {
		return (String) table.getModel().getValueAt(rowIndex,
				table.getModel().getColumnCount() - 1);
	}

	/**
	 * 创建表格
	 * 
	 * @param columnText
	 *            表头文本
	 * @param columnField
	 *            表头字段
	 * @param ds
	 *            数据内容
	 * 
	 * @param editFields
	 *            可编辑字段
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

		// 若是多选，则加多选功能
		// 注意：若有多选功能，不可以使用getTable().getTableHeader().setReorderingAllowed(false)
		if (hasCheck) {
			// 增加表头第一列的信息,如果不加,全选没有做用,虽然也可以触发事件.但是,在View上表现不出来
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
	 * 设置是否可以对表格进行多选
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
	 * 设置是否可以点击表头排序
	 * 
	 * @param allowOrder
	 */
	public void setAllowOrder(boolean allowOrder) {
		((TableSorter) table.getModel()).allowOrder = allowOrder;
	}

	/**
	 * 为一个字段注册一个格式化的Pattern
	 * 
	 * @param fieldName
	 *            字段名
	 * @param format
	 *            格式化用的Pattern
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
	 * 设置某个字段是否可编辑
	 * 
	 * @param fieldName
	 */
	public void setEditable(String[] fieldName) {
		if ((fieldName != null) && (fieldName.length > 0)) {
			if (editableCache == null) {
				editableCache = new HashSet();
			}
			int delta = 0;// 如果表格设置了CheckBox那么计算后面数据在表格中的列索引都要累加1
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
	 * 设置数据集
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
	 * 判断一行是否被选中，如果设定了表格有CheckBox，以该行是否被Check为标准
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
	 * 获取被选中的数据集
	 * 
	 * @return list 组织形式：Map
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
	 * 重新生成表的数据内容
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
	 * 添加表数据
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
	 * 设置列的额外信息，例如把CheckBox栏设置为很小的宽度，把DataSet书签栏设为0宽度，并且都固定宽度
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
			reduce = 1;// 如果有CheckBox的时候计算 columnField的下标要减去1
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
	 * 添加列信息
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
	 * 生成一条表格数据
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
	 * 根据列模型信息生成一条表格数据
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
	 * 根据字段名设置显示内容，如果该字段注册了格式化模式，将会进行格式化
	 * 
	 * @param fieldName
	 *            字段名
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
	 * 全选所有checkBox
	 */
	public void checkAll() {
		checkAll(true);
	}

	/**
	 * 反选所有checkBox
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
	 * 获取当前table是否可以选择
	 * 
	 * @return
	 */
	public boolean getHasCheck() {
		return this.hasCheck;
	}

	/**
	 * 将表中字段field所有值以startValue打头的选中或取消选中，主要用于单位列表选择 只适用于有选择框的table
	 * 
	 * @param field
	 * @param startValue
	 * @author sst
	 * @return
	 * @throws Exception
	 */
	public int setTableCheck(String field, String startValue, boolean blCheck) {
		// 操作行的数量
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
	 * 是否屏蔽表格的选择事件对DataSet游标的影响
	 */
	boolean maskValueChange;

	public void maskValueChange(boolean maskValueChange) {
		this.maskValueChange = maskValueChange;
	}

	/**
	 * 当Table未设置CheckBox的时候定位DataSet游标用这个关联表格选择事件 否则使用<code>TableDataChangeListener</code>
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!hasCheck) {
			fireValueChanged(e);
		}
	}

	/**
	 * 设置表格所有列宽度
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
	 * 根据行标书签返回行下标号
	 * 
	 * @param bookmark
	 *            行标书签
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
	 * 数据集内容变化的时候改变关联表格单元格的内容
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