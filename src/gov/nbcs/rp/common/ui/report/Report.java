/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 报表类，封装一些FineReport的基础应用，比且将报表数据和WorkSheet绑定
 * 
 * @author 钱自成
 * 
 * @version 1.0 移植到FineReport6.1.2版本
 */
package gov.nbcs.rp.common.ui.report;

import java.awt.Color;
import java.awt.print.PrinterException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.formula.RowFormula;
import gov.nbcs.rp.common.tree.TreeFactory;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.report.cell.CellStyle;
import gov.nbcs.rp.common.ui.report.cell.GUIAttribute;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.common.ui.report.cell.PropertyProviderAdapter;
import gov.nbcs.rp.common.ui.report.cell.PropertyProviderEx;
import gov.nbcs.rp.common.ui.report.event.ReportChangeListener;
import com.fr.base.Constants;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.Background;
import com.fr.base.background.ColorBackground;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.WorkSheet;
import com.fr.report.cellElement.CellGUIAttr;
import com.fr.report.core.DynamicValueList;
import com.fr.report.core.ReportHelper;
import com.fr.report.core.paint.PaintUtil;

public class Report extends WorkSheet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** 最大行高 */
	protected static final int MAX_ROW_HEIGHT = 190;
	
//	private FRFont sumRowFont = this.createSumRowFont();
//
//	private FRFont negativeSumRowFont = this.createNegativeSumRowFont();
//
//	private FRFont commonNegativeFont = this.createCommonNegativeFont();
//
//	private FRFont commonFont = this.createCommonFont();

	protected Style defaultStyle = CellStyle.NORMAL;

	protected GUIAttribute defaultAttr = new GUIAttribute(this);

//	protected NumberCellEditorDef numCellEditorDefCache = new NumberCellEditorDef();

	protected boolean printing;

//qj 移入CellStyle
//	public static Style PRINTING_STYLE = Style.getInstance();
//	static {
//		//设置为黑色边框
//		PRINTING_STYLE = PRINTING_STYLE.deriveBorder(1, Color.BLACK, 1,
//				Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);
//	}

	public boolean isPrinting() {
		return printing;
	}

	public void setPrinting(boolean printing) {
//		// 设定头部的printing属性
//		for (Iterator iter = header.getHeaderCells().iterator(); iter.hasNext();) {
//			Cell cell = (Cell) iter.next();
//			cell.setPrinting(printing);
//		}
		this.printing = printing;
	}

//	public NumberCellEditorDef getNumCellEditorDef() {
//		return numCellEditorDefCache;
//	}

//	/**
//	 * 创建合计行的字体
//	 *
//	 * @return
//	 */
//	protected FRFont createSumRowFont() {
//		FRFont font = FRFont.getInstance("宋体", FRFont.PLAIN, 13, new Color(39,
//				66, 171));
//		return font;
//	}
//
//	/**
//	 * 创建合计行的字体（负数）
//	 *
//	 * @return
//	 */
//	protected FRFont createNegativeSumRowFont() {
//		FRFont font = FRFont.getInstance("宋体", FRFont.PLAIN, 13, Color.RED);
//		return font;
//	}
//
//	/**
//	 * 创建普通的负数单元格字体
//	 *
//	 * @return
//	 */
//	protected FRFont createCommonNegativeFont() {
//		return FRFont.getInstance("宋体", FRFont.PLAIN, 12, Color.RED);
//	}
//
//	/**
//	 * 创建普通的单元格字体
//	 *
//	 * @return
//	 */
//	protected FRFont createCommonFont() {
//		return FRFont.getInstance("宋体", FRFont.PLAIN, 12, Color.BLACK);
//	}

	/**
	 * 表头对象
	 */
	protected transient TableHeader header;

	/**
	 * 表体数据集
	 */
	protected DataSet bodyData;

	/**
	 * 单元格属性
	 */
	private transient PropertyProvider cellProperty;

	/**
	 * 回车确认后输入焦点移动方向
	 */
//	private int enterMoveDirection = ReportConstants.MOVE_DIRECTION_DOWN;

	/**
	 * 表体数据字段
	 */
	private int recordCount = 0;

	/**
	 * 合计行行号缓冲
	 */
	private Set sumRows = new HashSet();

	/**
	 * 数据集的标签集合
	 */
	protected Set bookmarkSet = new HashSet();

	/**
	 * 是否屏蔽单元格修改
	 */
	private boolean maskCellValueChange;

	/**
	 * 只读单元格的背景色
	 */
	private Background readOnlyBackground;	
	
	/** The mask negative value. */
	private boolean maskNegativeValue;
	
	/**
	 * 最大行高
	 */
	private int maxRowHeight = MAX_ROW_HEIGHT;

	/**
	 * @return the maxRowHeight
	 */
	public int getMaxRowHeight() {
		return maxRowHeight;
	}

	/**
	 * @param maxRowHeight the maxRowHeight to set
	 */
	public void setMaxRowHeight(int maxRowHeight) {
		this.maxRowHeight = maxRowHeight;
	}

	/**
	 * Checks if is mask negative value.
	 * 
	 * @return true, if is mask negative value
	 */
	public boolean isMaskNegativeValue() {
		return maskNegativeValue;
	}

	/**
	 * Sets the mask negative value.
	 * 
	 * @param maskNegativeValue
	 *            the new mask negative value
	 */
	public void maskNegativeValue(boolean maskNegativeValue) {
		this.maskNegativeValue = maskNegativeValue;
	}	

	/**
	 * 屏蔽0值
	 */
	private boolean maskZeroValue;

	/**
	 * 屏蔽0值
	 */
	public void maskZeroValue(boolean maskZeroValue) {
		this.maskZeroValue = maskZeroValue;
	}

	/**
	 * 获取是否屏蔽0值
	 *
	 * @return
	 */
	public boolean isMaskZeroValue() {
		return maskZeroValue;
	}

	/**
	 * 报表界面对象引用
	 */
	ReportUI reportUI;

//	/**
//	 * 数字格式输入的单元格编辑框
//	 */
//	private CellEditor numberEditor = new NumberCellEditor();
//
//	/**
//	 * 文本输入的单元格编辑框
//	 */
//	private CellEditor textEditor = new TextCellEditor();

	/**
	 * Clear the sum row set.
	 */
	public void clearSumRowSet() {
		this.sumRows.clear();
	}

	/**
	 * Clear the bookmark set.
	 */
	public void clearBookmarkSet() {
		this.bookmarkSet.clear();
	}

	public ReportUI getUI() {
		return reportUI;
	}

	public void maskCellValueChange(boolean maskCellValueChange) {
		this.maskCellValueChange = maskCellValueChange;
	}

	public boolean isMaskCellValueChange() {
		return maskCellValueChange;
	}

	/**
	 * Gets the table header.
	 *
	 * @return the table header
	 */
	public TableHeader getTableHeader() {
		return header;
	}

	/**
	 * Sets the table header. 设置表头信息，但不重建
	 *
	 * @param header the new table header
	 */
	public void setTableHeader(TableHeader header) {
		this.header = header;
	}

	/**
	 * Gets the body data.
	 * 
	 * @return the body data
	 */
	public DataSet getBodyData() {
		return bodyData;
	}	
	
	/** The report data change listener. */
	ReportDataChangeListener reportDataChangeListener = new ReportDataChangeListener(
			this);
	
	/** The report state change listener. */
	ReportStateChangeListener reportStateChangeListener = new ReportStateChangeListener(
			this);
	
	/** The cancel action listener. */
	CancelActionListener cancelActionListener = new CancelActionListener(this);
	
	/** The delete action listener. */
	DeleteActionListener deleteActionListener = new DeleteActionListener(this);
	
	/** The insert action listener. */
	InsertActionListener insertActionListener = new InsertActionListener(this);
	
	/** The edit action listener. */
	EditActionListener editActionListener = new EditActionListener(this);

	/**
	 * 设置表体数据集
	 *
	 * @param bodyData
	 */
	public void setBodyData(DataSet bodyData) {
		this.bodyData = bodyData;
		if (this.bodyData != null) { 
			// 绑定事件
			this.bodyData.addDataChangeListener(reportDataChangeListener);
			this.bodyData.addStateChangeListener(reportStateChangeListener);
			this.bodyData.addCancelListener(cancelActionListener);
			this.bodyData.addDeleteListener(deleteActionListener);
			this.bodyData.addInsertListener(insertActionListener);
			this.bodyData.addEditListener(editActionListener);
		}
	}

	public PropertyProvider getCellProperty() {
		return cellProperty;
	}

	public void setCellProperty(PropertyProvider cellProperty) {
		this.cellProperty = cellProperty;
	}

	public TableHeader getReportHeader() {
		return header;
	}

	/**
	 * 设置表头对象，会重建表格、表头
	 *
	 * @param header
	 */
	public void setReportHeader(TableHeader header) {
		this.header = header;
		if (header == null) {
			this.header = this.createDefaultHeader();
		}
		this.appendHeader();
	}

	public Report(TableHeader header) throws Exception {
		this(header, null, null);
	}

	public Report() throws Exception {
		this(null, null, null);
	}

	/**
	 * 返回全表是否允许编辑
	 *
	 * @return
	 */
	public boolean isTableEditable() {
		return (bodyData.getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT;
	}

	/**
	 * 设置回车确认后输入焦点移动方向
	 *
	 * @param direction
	 *            焦点移动方向，常量定义在<code>com.fr.report.ReportConstants</code>
	 *
	 * @deprecated 建议使用<code>reportUI.getGrid().setEnterMoveDirection(int enterMoveDirection)</code>
	 *
	 * @see com.fr.cell.Grid
	 */
	public void setEnterMoveDirection(int direction) {
//qj		this.enterMoveDirection = direction;
		reportUI.getGrid().setEnterMoveDirection(direction);
	}

	/**
	 * 获取回车确认后输入焦点移动方向
	 *
	 * @return
	 *
	 * @deprecated 建议使用<code>reportUI.getGrid().getEnterMoveDirection()</code>
	 *
	 * @see com.fr.cell.Grid
	 *
	 */
	public int getEnterMoveDirection() {
//qj		return this.enterMoveDirection;
		return reportUI.getGrid().getEnterMoveDirection();
	}

	/**
	 * 判断是否合计、小计行
	 *
	 * @return
	 */
	public boolean isSumRow(int rowIndex) {
		return this.sumRows.contains(new Integer(rowIndex));
	}

	/**
	 * 根据内容自动调整列宽
	 */
	public void autoAdjustColumnWidth() {
		for (int i = 0; i < header.getColumns(); i++) {
			CellElement cell = null;
			int maxLength = 0;
			CellElement maxLengthCell = null;
			for (int j = 0; j < this.getBodyRowCount(); j++) {
				cell = this.getCellElement(i + header.getStartCol(), j
						+ header.getRows());
				int len = Common.nonNullStr(cell.getValue()).length();
				if (len > maxLength) {
					maxLength = len;
					maxLengthCell = cell;
				}
			}
			if (maxLengthCell != null) {
				String value = Common.nonNullStr(maxLengthCell.getValue());
				FRFont font = maxLengthCell.getStyle().getFRFont();
				double width = font.getTransform().getScaleX() * font.getSize()
						* value.length();
				if (width > this.getColumnWidth(maxLengthCell.getColumn())) {
					this.setColumnWidth(i, width);
				}
			}
		}
	}

	/**
	 * 压缩行高以适应文字: 2. 3.遍历表头行 3.1遍历该行的列 3.1.1取得该列宽度和内容 3.1.2如果下一列内容与该列相同，则累计当前列宽，
	 * 否则计算当前格子的高度。如果该高度至今最大，则记作行高
	 *
	 * 3.1.3 3.1.3.1取得该列宽度 3.1.3.2获得字体Front的高度fontH和宽度fontW 3.1.3.3计算文字的行数
	 * 3.1.3.4计算出该单元格的行高lingH 3.1.3.5如果该行高是最大值，则记录到该行最大值里 3.2取得该行的最大高度，设置行高
	 *
	 *
	 *
	 * 
	 *
	 */
	public void tShrinkToFitRowHeight() {
		for (int i = 0; i < header.getRows(); i++) {// 遍历行
			CellElement cell = null;
			double maxHeight = 0;// 最大行高

			double totalColWidth = 0;// 合并列宽度
			for (int j = 0; j < header.getColumns(); j++) {// 遍历列
				// 取当前单元格
				cell = this.getCellElement(j, i);
				// 当前单元格的值
				String curCellValue = ("" + cell.getValue()).trim();
				// 当前列宽度
				double colWidth = this.getColumnWidth(j);
				// 下一单元格
				String nextCellValue = "";
				if (j < header.getColumns() - 1) {// 最后一列
					nextCellValue = ("" + this.getCellElement(j + 1, i)
							.getValue()).trim();
				}
				// 如果当前列内容等于下一列内容，则累加列宽
				if (nextCellValue.equalsIgnoreCase(curCellValue)) {
					totalColWidth += colWidth;
					continue;
				}
				// 否则，计算当前列高度，清空合计列宽度
				totalColWidth += colWidth;// 累计列宽
				// 计算文字占据的行数

				int fontLen = curCellValue.length();
				FRFont font = cell.getStyle().getFRFont();
				int fontSize = font.getSize();// 字体大小
				int delt = 2;// 误差
				// 内容宽度
				double fontWidth = fontSize * fontLen;

				int zs = (int) (fontWidth / totalColWidth);
				double ys = fontWidth % totalColWidth;
				int tRows = zs + (ys > 0 ? 1 : 0);// 文字占几行

				// 计算出合理的行高
				double tHeight = (tRows * (fontSize + delt)) < 25 ? 25
						: (tRows * (fontSize + delt));
				// 如果较大，则记录下来
				if (tHeight > maxHeight) {
					maxHeight = tHeight;
				}

				// 清空合计列宽度
				totalColWidth = 0;
			}
			this.setRowHeight(i, maxHeight > maxRowHeight ? maxRowHeight : maxHeight);
		}
	}

	/**
	 * 调整行号，但受最大行高限制 
	 */
	public void shrinkToFitRowHeightLimited() {
        CellElement cellElement = null;
        Iterator ceIt = cellIterator();
        do
        {
            if(!ceIt.hasNext()) {
				break;
			}
            cellElement = (CellElement)ceIt.next();
            if(cellElement != null)
            {
                CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
                if(cellGUIAttr == null) {
					cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
				}
                if(cellGUIAttr.isVisible() && (cellGUIAttr.isPreviewContent() || cellGUIAttr.isPrintContent()) && cellGUIAttr.isAutoAdjustHeightOfRow())
                {
                    Object editElementValue = cellElement.getValue();
                    if((editElementValue != null) && ((editElementValue instanceof String) || (editElementValue instanceof Number)))
                    {
                        DynamicValueList columnWidthList = ReportHelper.getColumnWidthList(this);
                        DynamicValueList rowHeightList = ReportHelper.getRowHeightList(this);
                        int editElementcolumn = cellElement.getColumn();
                        double preferredHeight = PaintUtil.analyzeCellElementPreferredHeight(cellElement, columnWidthList.getRangeValue(editElementcolumn, editElementcolumn + cellElement.getColumnSpan()));
                        if(cellElement.getRowSpan() == 1)
                        {
                            double maxHeight = Math.max(preferredHeight, rowHeightList.get(cellElement.getRow()));
                            maxHeight = maxHeight > maxRowHeight ? maxRowHeight : maxHeight;
							rowHeightList.set(cellElement.getRow(), maxHeight);
                        } else
                        {
                            int lastRowIndex = (cellElement.getRow() + cellElement.getRowSpan()) - 1;
                            double extraHeight = preferredHeight - rowHeightList.getRangeValue(cellElement.getRow(), lastRowIndex + 1);
                            if(extraHeight > 0.0D)
                            {
                                int j = cellElement.getRow();
                                while(j <= lastRowIndex) 
                                {
                                    double maxHeight = rowHeightList.get(j) + extraHeight / cellElement.getRowSpan();
                                    maxHeight = maxHeight > maxRowHeight ? maxRowHeight : maxHeight;
									rowHeightList.set(j, maxHeight);
                                    j++;
                                }
                            }
                        }
                    }
                }
            }
        } while(true);
    }

	/**
	 * 设置合计、小计行
	 *
	 * @param rowIndex
	 */
	public void setSumRow(int rowIndex) {
		this.sumRows.add(new Integer(rowIndex));
	}

	/**
	 * 将行号翻译为对应DataSet数据行之书签
	 *
	 * @param rowIndex
	 *            行号
	 * @return
	 */
	public String rowToBookmark(int rowIndex) {
		if ((rowIndex >= 0)
				&& (rowIndex >= header.getRows())
				&& (rowIndex < header.getRows() + this.getBodyRowCount())) {
			Cell cell = (Cell) this.getCellElement(0, rowIndex);
			if (cell==null)
				return null;
			return cell.getBookmark();
		}
		return null;
	}

	/**
	 * 将DataSet数据行之书签翻译为对应行号
	 *
	 * @param bookmark
	 *            书签
	 * @return
	 */
	public int bookmarkToRow(String bookmark) throws Exception {
		Object columnId = header.getFields().get(0);
		String fieldName = cellProperty.getFieldName(columnId);
		String cellName = Report.translateToCellName(bookmark, fieldName);
		CellElement cell = getCellElement(cellName);
		return cell != null ? cell.getRow() : -1;
	}

	/**
	 * 使用表头和表体数据集创建表格
	 */
	public Report(TableHeader header, DataSet bodyData,
			PropertyProvider cellPropProvider) throws Exception {
		this.header = header;
		if (header == null) {
			this.header = createDefaultHeader();
		}
		this.setBodyData(bodyData);
		this.cellProperty = cellPropProvider;
		if (cellProperty == null) {
			cellProperty = createDefaultCellProperty();
		}
		appendHeader();
		refreshBody();

	}

	/**
	 * 创建默认表头
	 *
	 * @return
	 */
	protected TableHeader createDefaultHeader() {
		return new TableHeader(TreeFactory.getInstance().createTreeNode(null));
	}

	/**
	 * 使用表头和表体数据集创建表格
	 */
	public Report(TableHeader header, DataSet bodyData) throws Exception {
		this(header, bodyData, null);
	}

	protected PropertyProvider createDefaultCellProperty() {
		return new PropertyProviderAdapter() {
			public boolean isEditable(String bookmark, Object fieldId) {
				return Report.this.isTableEditable();
			}
		};
	}

	/**
	 * 把表头信息添加到表格中
	 */
	protected void appendHeader() {
		fireBeforeHeaderChange();
		if (header != null) {
			Collection headerCells = header.getHeaderCells();
			for (Iterator it = headerCells.iterator(); it.hasNext();) {
				CellElement cell = (CellElement) it.next();
				this.addCellElement(cell);
			}
		}
		fireAfterHeaderChange();
	}

	/**
	 * 插入一行记录的同时记录数据的行数
	 */
	public void insertRow(int rowIndex) {
		super.insertRow(rowIndex);
		recordCount++;
		createRowCells(rowIndex);
	}

	/**
	 * 插入一行记录的同时记录数据的行数
	 */
	public void appendRow() throws Exception {
		recordCount++;
		createRowCells(header.getRows() + getBodyRowCount() - 1);
	}

	/**
	 * 在指定的一行创建单元格
	 *
	 * @param rowIndex
	 */
	public void createRowCells(int rowIndex) {
		try {
			List fields = header.getFields();
			for (int i = 0; i < fields.size(); i++) {
				CellElement cell = createCell(i + header.getStartCol(),
						rowIndex, fields.get(i));
				this.addCellElement(cell);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.fr.report.Report#addCellElement(com.fr.report.CellElement)
	 */
	public void addCellElement(CellElement cell) {
		super.addCellElement(cell, false);
		if (cell.getRow() >= recordCount + header.getRows()) {
			recordCount++;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.fr.report.Report#addCellElement(com.fr.report.CellElement,
	 *      boolean)
	 */
	public void addCellElement(CellElement cell, boolean isOverride) {
		super.addCellElement(cell, isOverride);
		if (cell.getRow() >= recordCount + header.getRows()) {
			recordCount++;
		}
	}

	/**
	 * 根据记录的书签值判断该条记录是否存在于Report之中
	 *
	 * @param bookmark
	 * @return
	 */
	public boolean containsRecord(String bookmark) {
		return this.bookmarkSet.contains(bookmark);
	}

	/**
	 * 获取选中的单元格
	 *
	 * @return
	 */
	public CellElement getSelectedCell() {
		CellSelection cs = this.getUI().getCellSelection();
		return getCellElement(cs.getColumn(), cs.getRow());
	}

	/**
	 * 获取选中的行号
	 *
	 * @return
	 */
	public int getSelectedRow() {
		CellElement cell = getSelectedCell();
		return cell == null ? -1 : cell.getRow();
	}

	/**
	 * 获取某一行的所有单元格
	 *
	 * @param row
	 *            行标号
	 * @return
	 */
	public CellElement[] getSelectedCells(int row) {
		int columns = header.getColumns();
		if ((columns > 0) && (row >= 0)) {
			CellElement[] cells = new CellElement[columns];
			for (int i = 0; i < columns; i++) {
				cells[i] = getCellElement(i, row);
			}
			return cells;
		}
		return null;
	}

	/**
	 * 获取选中区域（矩形）的所有单元格
	 *
	 * @return
	 */
	public CellElement[][] getSelectedCells() {
		CellSelection cs = this.getUI().getCellSelection();
		int startCol = cs.getColumn();
		int endCol = cs.getColumn() + cs.getColumnSpan();
		int startRow = cs.getRow();
		int endRow = cs.getRow() + cs.getRowSpan();
		CellElement cells[][] = new CellElement[cs.getRowSpan()][cs
				.getColumnSpan()];
		for (int i = 0; (i < cells.length) && (startRow < endRow); i++, startRow++) {
			for (int j = 0; (j < cells[i].length) && (startCol < endCol); j++, startCol++) {
				cells[i][j] = getCellElement(startCol, startRow);
			}
		}
		return cells;
	}

	public int removeRow(int rowIndex) {
		this.recordCount--;
		return super.removeRow(rowIndex);
	}

	/**
	 * 遍历bodyData，添加表格数据
	 */
	protected void appendBody(Object fields[]) throws Exception {
		fireBeforeBodyChange();
		if ((bodyData != null) && !bodyData.isEmpty()) {
			this.maskCellValueChange(true);
			bodyData.maskDataChange(true);
			bodyData.beforeFirst();
			for (int i = 0; bodyData.next(); i++) {
				addBodyRow(fields, i);
				initBodyRow(i);
			}
			bodyData.beforeFirst();
			bodyData.next();
			bodyData.maskDataChange(false);
			this.maskCellValueChange(false);
		}
		fireAfterBodyChange();
	}

	/**
	 * Inits the body row. 对当前记录对应的单元行的初始操作
	 *
	 * @param bodyRowIndex the body row index
	 */
	protected void initBodyRow(int bodyRowIndex) {
		// 记录数据集书签。由上面的循环体中移出
		this.bookmarkSet.add(bodyData.toogleBookmark());
		// 记录计算行
		if (bodyData
				.containsField(RowFormula.GROUP_SUM_ROW_FLAG_FIELD)) {
			setSumRow(bodyRowIndex + header.getRows());
		}
	}

	/**
	 * Inits the body row. 对指定记录对应的单元行的初始操作
	 *
	 * @param bodyRowIndex the body row index
	 */
	protected void initBodyRow(int bodyRowIndex, Map record) {
		// 记录数据集书签。由上面的循环体中移出 by qj
		this.bookmarkSet.add(bodyData.toggleBookmark(bodyRowIndex));

		// 记录计算行
		if (bodyData
				.containsField(RowFormula.GROUP_SUM_ROW_FLAG_FIELD, record)) {
			setSumRow(bodyRowIndex + header.getRows());
		}
	}

//	public CellElement getCellElement(int arg0, int arg1) {
//		CellElement _cell = super.getCellElement(arg0, arg1);

//		// qj 兼职功能，不提倡
//		if (_cell != null && _cell instanceof Cell) {
//			Cell cell = (Cell) _cell;
//			cell.setPrinting(this.isPrinting());
			// #001 if (cell.getCellType() == Cell.BODY_CELL
			// && cell.getStyle() != null
			// && cell.getStyle().getClass().hashCode() == CellStyle.class
			// .hashCode()) {
			// CellStyle style = (CellStyle) cell.getStyle();
			// style.setCell(cell);
			// }
//		}
//		return _cell;
//	}

//	/**
//	 * 获取指定序号的单元格元素
//	 *
//	 * @param i
//	 *            序号
//	 * @return
//	 *
//	 * @deprecated
//	 */
//	public CellElement getCellElement(int i) {
//		CellElement _cell = null;
//		int idx = 0;
//		Iterator cellIterator = cellIterator();
//		while (cellIterator.hasNext()) {
//			_cell = (CellElement) cellIterator.next();
//			if (++idx > i)
//				return _cell;
//		}
		// if (i < this.getCellElementCount()) {
		// CellElement _cell = super.getCellElement(i);

//		// qj 兼职功能，不提倡
//		if (_cell != null && _cell instanceof Cell) {
//			Cell cell = (Cell) _cell;
//			cell.setPrinting(this.isPrinting());
			// if (cell.getCellType() == Cell.BODY_CELL
			// && cell.getStyle() != null
			// && cell.getStyle().getClass().hashCode() == CellStyle.class
			// .hashCode()) {
			// CellStyle style = (CellStyle) cell.getStyle();
			// style.setCell(cell);
			// }
//			return cell;
//		}
		// }
//		return null;
//	}

//	protected Style createCellStyle(Cell cell, Object fieldId) {
//		return this.defaultStyle;
//	}

	/**
	 * 以bodyData当前记录，向表格中添加一行单元格.
	 *
	 * @param bodyDataRow
	 *            the body data row
	 * @param fieldIds
	 *            the field ids
	 *
	 * @throws Exception
	 *             the exception
	 */
	protected void addBodyRow(Object[] fieldIds, int bodyDataRow)
			throws Exception {
		for (int j = 0; j < fieldIds.length; j++) {
			CellElement cell = createCell(j + header.getStartCol(), bodyDataRow
					+ header.getRows(), fieldIds[j]);
			this.addCellElement(cell);
		}
	}


	/**
	 * 使用指定record，向表格中添加一行单元格.
	 *
	 * @param fields
	 *            the fields
	 * @param row
	 *            the bodyDataRow
	 * @param record
	 *            the record
	 */
	protected void addBodyRow(Object[] fieldIds, int bodyDataRow, Map record)
			throws Exception {
		for (int j = 0; j < fieldIds.length; j++) {
			CellElement cell = createCell(j + header.getStartCol(), bodyDataRow
					+ header.getRows(), fieldIds[j], bodyDataRow, record);
			this.addCellElement(cell);
		}
	}

	/**
	 * 以bodyData当前记录，设置表格中的一行单元格.
	 *
	 * @param bodyDataRow
	 *            the body data row
	 * @param fieldIds
	 *            the field ids
	 *
	 * @throws Exception
	 *             the exception
	 */
	protected void setupBodyRow(Object[] fieldIds, int bodyDataRow)
			throws Exception {
		for (int j = 0; j < fieldIds.length; j++) {
			// setupCell(j, bodyDataRow, fields[j]);
			Cell cell = (Cell) getCellElement(j, bodyDataRow + header.getRows());
			// 判断该行是否已经设置
			if (cell.getReport() == null) {
				setupCell(fieldIds[j], cell);
			}
		}
	}

	/**
	 * 用指定的记录，设置表格中的一行单元格
	 *
	 * @param fields
	 *            the fields
	 * @param row
	 *            the row
	 * @param record
	 *            the record
	 */
	protected void setupBodyRow(Object[] fieldIds, int bodyDataRow, Map record) {
		setupBodyRow(fieldIds, bodyDataRow, record, false);
	}

	/**
	 * 用指定的记录，设置表格中的一行单元格.
	 *
	 * @param record
	 *            the record
	 * @param fieldIds
	 *            the field ids
	 * @param bodyDataRow
	 *            the body data row
	 * @param force
	 *            the forceUpdate 是否强制更新
	 */
	protected void setupBodyRow(Object[] fieldIds, int bodyDataRow, Map record, boolean forceUpdate) {
		for (int j = 0; j < fieldIds.length; j++) {
			Cell cell = (Cell) getCellElement(j + header.getStartCol(),
					bodyDataRow + header.getRows());
			if (cell == null) {
				cell = new Cell(j + header.getStartCol(), bodyDataRow
						+ header.getRows());
				this.addCellElement(cell);
			}
			// 避免重复填入数据
			if(forceUpdate || (cell.getReport() == null)) {
				setupCell(fieldIds[j], cell, record, bodyDataRow);
			}
		}
	}

	/**
	 * Sets the up cell style.
	 *
	 * 继承类通过override该方法，在单元格创建后设置其样式
	 *
	 * @param cell
	 *            the new up cell style
	 */
	protected void setupCellStyle(Cell cell) {}

	/**
	 * 以DataSet当前记录，创建一个单元格对象.
	 *
	 * @param column
	 *            the column
	 * @param row
	 *            the row
	 * @param fieldId
	 *            the field id
	 *
	 * @return the cell element
	 *
	 * @throws Exception
	 *             the exception
	 */
	protected CellElement createCell(int column, int row, Object fieldId)
			throws Exception {
		Cell cell = new Cell(column, row);

		setupCell(fieldId, cell);

		return cell;
	}

	/**
	 * Creates the cell.
	 *
	 * 使用指定记录，创建一个单元格
	 *
	 * @param column
	 *            the column
	 * @param row
	 *            the row
	 * @param fieldId
	 *            the field id
	 * @param bodyDataRow
	 *            the body data row
	 * @param record
	 *            the record
	 *
	 * @return the cell element
	 *
	 * @throws Exception
	 *             the exception
	 */
	protected CellElement createCell(int column, int row, Object fieldId, int bodyDataRow, Map record)
			throws Exception {
		Cell cell = new Cell(column, row);

		setupCell(fieldId, cell, record, bodyDataRow);

		return cell;
	}

	/**
	 * 以DataSet当前记录，设定一个单元格对象.
	 *
	 * @param column
	 *            the column
	 * @param row
	 *            the row
	 * @param fieldId
	 *            the field id
	 *
	 * @throws Exception
	 *             the exception
	 */
	protected void setupCell(int column, int row, Object fieldId)
			throws Exception {
		Cell cell = (Cell) getCellElement(column, row);

		setupCell(fieldId, cell);
	}

	/**
	 * 以DataSet当前记录，设定一个单元格对象.
	 *
	 * @param column the column
	 * @param fieldId the field id
	 * @param cell the cell
	 *
	 * @throws Exception the exception
	 */
	private void setupCell(Object fieldId, Cell cell) throws Exception {
//qj		if(cell != null) {
//			String fieldName = cellProperty.getFieldName(fieldId);
//			String bookmark = bodyData.toogleBookmark();
//			cell.setCellType(Cell.BODY_CELL);
//			cell.setReport(this);
//			Field field = bodyData.fieldByName(fieldName);
//			Class fieldType = field.getValueType();
//			Object value = field.getValue();
//			if ((value != null && Common.isNumber(value.toString()) || (fieldType != null && Number.class
//					.isAssignableFrom(fieldType)))
//					&& cellProperty.getEditor(bookmark, fieldId) == null) {
//				cell.setInnerEditor(numberEditor);
//			} else {
//				cell.setInnerEditor(textEditor);
//			}
//			cell.setValue(value);
//			Format format = cellProperty.getFormat(bookmark, fieldId);
//			if (format != null
//					&& NumberFormat.class.isAssignableFrom(format.getClass())) {
//				int fractionDigits = ((NumberFormat) format)
//						.getMaximumFractionDigits();
//				cell.setFractionDigits(fractionDigits);
//			}
//			cell.setFormat(format);
//			cell
//					.setEditableState(getCellProperty().isEditable(bookmark,
//							fieldId) ? Constants.EDITABLE_STATE_TRUE
//							: Constants.EDITABLE_STATE_FALSE);
//			double width = getCellProperty().getColumnWidth(fieldId);
//			if (width > 0) {
//				setColumnWidth(cell.getColumn(), width);
//			}
//			cell.setCellGUIAttr(defaultAttr);
//			cell.setBookmark(bookmark);
//			cell.setFieldName(fieldName);
//			cell.setName(Report.translateToCellName(bookmark, fieldName));
//
//			// #001 cell.setStyle(this.createCellStyle(cell, field));
//	//		cell.setStyle(null);
//			setupCellStyle(cell);
//		}

		String fieldName = cellProperty.getFieldName(fieldId);
		Field field = bodyData.fieldByName(fieldName);
		String bookmark = bodyData.toogleBookmark();
		setupCell(fieldId, cell, field, bookmark);

		//设置列宽
		if((fieldId != null) && (cell != null)
//				&& bodyDataRow == 0
				&& (cell.getRow() < 10)) //qinj 应该是第0行，安全起见操作前10条（避开表头），避免过多的的操作)
				 {
			double width = cellProperty.getColumnWidth(fieldId);
			if (width > 0) {
				setColumnWidth(cell.getColumn(), width);
			}
		}
	}

	/**
	 * Setup the cell.
	 *
	 * 用指定的记录设置指定的单元格
	 *
	 * @param bodyDataRow
	 *            the body data row
	 * @param fieldId
	 *            the field id
	 * @param cell
	 *            the cell
	 * @param record
	 *            the record
	 */
	protected void setupCell(Object fieldId, Cell cell, Map record, int bodyDataRow) {
		String fieldName = cellProperty.getFieldName(fieldId);
		Field field = (Field) record.get(fieldName);
		String bookmark = bodyData.getBookmark(bodyDataRow);
		setupCell(fieldId, cell, field, bookmark);

		//设置列宽
		if ((cell != null) && (bodyDataRow == 0)) {
			double width = cellProperty.getColumnWidth(fieldId);
			if (width > 0) {
				setColumnWidth(cell.getColumn(), width);
			}
		}

	}

	/**
	 * Setup the cell.
	 *
	 * 用指定的记录设置指定的单元格
	 *
	 * @param fieldId
	 *            the field id
	 * @param cell
	 *            the cell
	 * @param field
	 *            the field
	 * @param bookmark
	 *            the bookmark
	 */
	protected void setupCell(Object fieldId, Cell cell, Field field, String bookmark) {
		if ((cell != null) && (field != null)) {
			String fieldName = cellProperty.getFieldName(fieldId);
			cell.setCellType(Cell.BODY_CELL);
			cell.setReport(this);
//			CellEditor cellEditor = getCellEditor(field, fieldId, bookmark);
//			if (cellEditor != null)
//				cell.setInnerEditor(cellEditor);
			Object value = field.getValue();
			cell.setValue(value);
			Format format = cellProperty.getFormat(bookmark, fieldId);
			if ((format != null)
//					&& NumberFormat.class.isAssignableFrom(format.getClass())) {
					&& (format instanceof NumberFormat)) {
				int fractionDigits = ((NumberFormat) format)
						.getMaximumFractionDigits();
				cell.setFractionDigits(fractionDigits);
			}
			cell.setFormat(format);
			cell
					.setEditableState(cellProperty.isEditable(bookmark,
							fieldId) ? Constants.EDITABLE_STATE_TRUE
							: Constants.EDITABLE_STATE_FALSE);
			// TODO cell.setEditableState()
			
			cell.setCellGUIAttr(defaultAttr);
			cell.setBookmark(bookmark);
			cell.setFieldName(fieldName);
			cell.setName(Report.translateToCellName(bookmark, fieldName));

			// 通过PropertyProviderEx提供单元格样式
			if (cellProperty instanceof PropertyProviderEx) {
				Style style = ((PropertyProviderEx) cellProperty)
						.getCellStyle(cell);
				if (style != null) {
					cell.setStyle(style);
				}
			}

			// #001 cell.setStyle(this.createCellStyle(cell, field));
	//		cell.setStyle(null);

			setupCellStyle(cell);
		}
	}

//	/**
//	 * 判断该使用何种cell editor.
//	 *
//	 * @param value
//	 *            the value
//	 * @param fieldType
//	 *            the field type
//	 * @param fieldId
//	 *            the field id
//	 * @param bookmark
//	 *            the bookmark
//	 *
//	 * @return the cell editor
//	 */
//	private CellEditor getCellEditor(Field field, Object fieldId,
//			String bookmark) {
//		if (field != null) {
//			Object value = field.getValue();
//			Class fieldType = field.getValueType();
//			CellEditor editor = cellProperty.getEditor(bookmark, fieldId);
//			if (editor != null) {
//				return editor;
//			} else if ((value != null && Common.isNumber(value.toString()))
//					|| (fieldType != null && Number.class
//							.isAssignableFrom(fieldType))) {
//				return numberEditor;
//			} else {
//				return textEditor;
//			}
//		}
//		return null;
//	}

	/**
	 * 根据记录书签、字段名组合成单元格名字
	 *
	 * @param bookmark
	 *            行书签
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static String translateToCellName(String bookmark, String fieldName) {
		return bookmark + "_" + Common.nonNullStr(fieldName).toUpperCase();
	}

	/**
	 * 重新生成表头
	 *
	 * @throws Exception
	 */
	public void refreshHeader() throws Exception {
		if (header == null) {
			header = this.createDefaultHeader();
		}
		appendHeader();
	}

	/**
	 * 重新生成所有表体单元格
	 */
	public void refreshBody() throws Exception {
		Object fields[] = header.getFields().toArray();
		this.recordCount = 0;
		appendBody(fields);
	}

	/**
	 * 重新生成所有单元格
	 *
	 * @throws Exception the exception
	 */
	public void refresh() throws Exception {
		resetWithData();
		refreshHeader();
		refreshBody();
	}
	
	/**
	 * Refreshes the body state.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void refreshBodyState() throws Exception {
		Object fields[] = header.getFields().toArray();
		if ((bodyData != null) && !bodyData.isEmpty()) {
			String bk = bodyData.toogleBookmark();
			bodyData.maskDataChange(true);
		 	bodyData.beforeFirst();
			for (int i = 0; bodyData.next(); i++) {
				String bookmark = bodyData.getBookmark(i);
				for (int j = 0; j < fields.length; j++) {
					Cell cell = (Cell) getCellElement(j + header.getStartCol(),
							i + header.getRows());
					Object fieldId = fields[j];
					// 定位到表头列信息所对应的行
					cellProperty.getFieldName(fieldId);
					cell
							.setEditableState(cellProperty
									.isEditable(bookmark, fieldId) ? Constants.EDITABLE_STATE_TRUE
									: Constants.EDITABLE_STATE_FALSE);
				}
			}
			bodyData.gotoBookmark(bk);
			bodyData.maskDataChange(false);
		 }		
	}

	/**
	 * 获取表体数据的行总数
	 *
	 * @return
	 */
	public int getBodyRowCount() {
		return recordCount;
	}

	/**
	 * 获取只读单元格设定的背景
	 *
	 * @return
	 *
	 * @deprecated 不再有效 since cvs 1.4
	 */
	public Background getReadOnlyBackground() {
		return readOnlyBackground;
	}

	/**
	 * 设置只读单元格的背景
	 *
	 * @param readOnlyBackground
	 *
	 * @deprecated 不再有效 since cvs 1.4
	 */
	public void setReadOnlyBackground(Background readOnlyBackground) {
		this.readOnlyBackground = readOnlyBackground;
	}

	/**
	 * 设置只读单元格的背景色
	 *
	 * @param readOnlyBackground
	 *
	 * @deprecated 不再有效 since cvs 1.4
	 */
	public void setReadOnlyBackground(Color backColor) {
		this.readOnlyBackground = ColorBackground.getInstance(backColor);
	}

//	public FRFont getCommonFont() {
//		return commonFont;
//	}
//
//	public FRFont getCommonNegativeFont() {
//		return commonNegativeFont;
//	}
//
//	public FRFont getNegativeSumRowFont() {
//		return negativeSumRowFont;
//	}
//
//	public FRFont getSumRowFont() {
//		return sumRowFont;
//	}

	public void print() throws PrinterException {
		this.setPrinting(true);
		super.print();
		this.setPrinting(false);
	}

	public void print(boolean arg0, String arg1, int arg2)
			throws PrinterException {
		this.setPrinting(true);
		super.print(arg0, arg1, arg2);
		this.setPrinting(false);
	}

	public void print(boolean arg0, String arg1) throws PrinterException {
		this.setPrinting(true);
		super.print(arg0, arg1);
		this.setPrinting(false);
	}

	public void print(boolean arg0) throws PrinterException {
		this.setPrinting(true);
		super.print(arg0);
		this.setPrinting(false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.fr.report.WorkSheet#removeAllCellElements()
	 */
	public void removeAllCellElements() {
		super.removeAllCellElements();
		removeColumnRange(0, getColumnCount());
	}

	/**
	 * Reset with data.
	 *
	 * <p>
	 * 重置的内容不含各监听器
	 */
	public void resetWithData() {
		reset();
		removeAllFloatElements();
		clearAllTableData();
		clearSumRowSet();
		clearBookmarkSet();
	}

	/**
	 * 报表单元格变化
	 */
	private ArrayList reportChangeListeners = new ArrayList();

	/**
	 * Adds the report change listener.
	 * 
	 * @param reportChangeListener
	 *            the report change listener
	 */
	public void addReportChangeListener(
			ReportChangeListener reportChangeListener) {
		if (!reportChangeListeners.contains(reportChangeListener)) {
			reportChangeListeners.add(reportChangeListener);
		}
	}

	/**
	 * Removes the report change listener.
	 * 
	 * @param reportChangeListener
	 *            the report change listener
	 */
	public void removeReportChangeListener(
			ReportChangeListener reportChangeListener) {
		reportChangeListeners.remove(reportChangeListener);
	}

	/**
	 * Removes all of the report change listeners.
	 */
	public void removeAllReportChangeListeners() {
		reportChangeListeners.clear();
	}

	/**
	 * Executes before header change.
	 */
	private void fireBeforeHeaderChange() {
		for (Iterator iter = reportChangeListeners.iterator(); iter.hasNext();) {
			ReportChangeListener lsnr = (ReportChangeListener) iter.next();
			lsnr.beforeHeaderChange(this);
		}
	}

	/**
	 * Executes after header change.
	 */
	private void fireAfterHeaderChange() {
		for (Iterator iter = reportChangeListeners.iterator(); iter.hasNext();) {
			ReportChangeListener lsnr = (ReportChangeListener) iter.next();
			lsnr.afterHeaderChange(this);
		}
	}

	/**
	 * Executes before body change.
	 */
	private void fireBeforeBodyChange() {
		for (Iterator iter = reportChangeListeners.iterator(); iter.hasNext();) {
			ReportChangeListener lsnr = (ReportChangeListener) iter.next();
			lsnr.beforeBodyChange(this);
		}
	}

	/**
	 * Executes after body change.
	 */
	private void fireAfterBodyChange() {
		for (Iterator iter = reportChangeListeners.iterator(); iter.hasNext();) {
			ReportChangeListener lsnr = (ReportChangeListener) iter.next();
			lsnr.afterBodyChange(this);
		}
	}
}
