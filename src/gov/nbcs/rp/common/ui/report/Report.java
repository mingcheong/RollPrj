/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title �����࣬��װһЩFineReport�Ļ���Ӧ�ã����ҽ��������ݺ�WorkSheet��
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0 ��ֲ��FineReport6.1.2�汾
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
	
	/** ����и� */
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

//qj ����CellStyle
//	public static Style PRINTING_STYLE = Style.getInstance();
//	static {
//		//����Ϊ��ɫ�߿�
//		PRINTING_STYLE = PRINTING_STYLE.deriveBorder(1, Color.BLACK, 1,
//				Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);
//	}

	public boolean isPrinting() {
		return printing;
	}

	public void setPrinting(boolean printing) {
//		// �趨ͷ����printing����
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
//	 * �����ϼ��е�����
//	 *
//	 * @return
//	 */
//	protected FRFont createSumRowFont() {
//		FRFont font = FRFont.getInstance("����", FRFont.PLAIN, 13, new Color(39,
//				66, 171));
//		return font;
//	}
//
//	/**
//	 * �����ϼ��е����壨������
//	 *
//	 * @return
//	 */
//	protected FRFont createNegativeSumRowFont() {
//		FRFont font = FRFont.getInstance("����", FRFont.PLAIN, 13, Color.RED);
//		return font;
//	}
//
//	/**
//	 * ������ͨ�ĸ�����Ԫ������
//	 *
//	 * @return
//	 */
//	protected FRFont createCommonNegativeFont() {
//		return FRFont.getInstance("����", FRFont.PLAIN, 12, Color.RED);
//	}
//
//	/**
//	 * ������ͨ�ĵ�Ԫ������
//	 *
//	 * @return
//	 */
//	protected FRFont createCommonFont() {
//		return FRFont.getInstance("����", FRFont.PLAIN, 12, Color.BLACK);
//	}

	/**
	 * ��ͷ����
	 */
	protected transient TableHeader header;

	/**
	 * �������ݼ�
	 */
	protected DataSet bodyData;

	/**
	 * ��Ԫ������
	 */
	private transient PropertyProvider cellProperty;

	/**
	 * �س�ȷ�Ϻ����뽹���ƶ�����
	 */
//	private int enterMoveDirection = ReportConstants.MOVE_DIRECTION_DOWN;

	/**
	 * ���������ֶ�
	 */
	private int recordCount = 0;

	/**
	 * �ϼ����кŻ���
	 */
	private Set sumRows = new HashSet();

	/**
	 * ���ݼ��ı�ǩ����
	 */
	protected Set bookmarkSet = new HashSet();

	/**
	 * �Ƿ����ε�Ԫ���޸�
	 */
	private boolean maskCellValueChange;

	/**
	 * ֻ����Ԫ��ı���ɫ
	 */
	private Background readOnlyBackground;	
	
	/** The mask negative value. */
	private boolean maskNegativeValue;
	
	/**
	 * ����и�
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
	 * ����0ֵ
	 */
	private boolean maskZeroValue;

	/**
	 * ����0ֵ
	 */
	public void maskZeroValue(boolean maskZeroValue) {
		this.maskZeroValue = maskZeroValue;
	}

	/**
	 * ��ȡ�Ƿ�����0ֵ
	 *
	 * @return
	 */
	public boolean isMaskZeroValue() {
		return maskZeroValue;
	}

	/**
	 * ��������������
	 */
	ReportUI reportUI;

//	/**
//	 * ���ָ�ʽ����ĵ�Ԫ��༭��
//	 */
//	private CellEditor numberEditor = new NumberCellEditor();
//
//	/**
//	 * �ı�����ĵ�Ԫ��༭��
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
	 * Sets the table header. ���ñ�ͷ��Ϣ�������ؽ�
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
	 * ���ñ������ݼ�
	 *
	 * @param bodyData
	 */
	public void setBodyData(DataSet bodyData) {
		this.bodyData = bodyData;
		if (this.bodyData != null) { 
			// ���¼�
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
	 * ���ñ�ͷ���󣬻��ؽ���񡢱�ͷ
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
	 * ����ȫ���Ƿ�����༭
	 *
	 * @return
	 */
	public boolean isTableEditable() {
		return (bodyData.getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT;
	}

	/**
	 * ���ûس�ȷ�Ϻ����뽹���ƶ�����
	 *
	 * @param direction
	 *            �����ƶ����򣬳���������<code>com.fr.report.ReportConstants</code>
	 *
	 * @deprecated ����ʹ��<code>reportUI.getGrid().setEnterMoveDirection(int enterMoveDirection)</code>
	 *
	 * @see com.fr.cell.Grid
	 */
	public void setEnterMoveDirection(int direction) {
//qj		this.enterMoveDirection = direction;
		reportUI.getGrid().setEnterMoveDirection(direction);
	}

	/**
	 * ��ȡ�س�ȷ�Ϻ����뽹���ƶ�����
	 *
	 * @return
	 *
	 * @deprecated ����ʹ��<code>reportUI.getGrid().getEnterMoveDirection()</code>
	 *
	 * @see com.fr.cell.Grid
	 *
	 */
	public int getEnterMoveDirection() {
//qj		return this.enterMoveDirection;
		return reportUI.getGrid().getEnterMoveDirection();
	}

	/**
	 * �ж��Ƿ�ϼơ�С����
	 *
	 * @return
	 */
	public boolean isSumRow(int rowIndex) {
		return this.sumRows.contains(new Integer(rowIndex));
	}

	/**
	 * ���������Զ������п�
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
	 * ѹ���и�����Ӧ����: 2. 3.������ͷ�� 3.1�������е��� 3.1.1ȡ�ø��п�Ⱥ����� 3.1.2�����һ�������������ͬ�����ۼƵ�ǰ�п�
	 * ������㵱ǰ���ӵĸ߶ȡ�����ø߶��������������и�
	 *
	 * 3.1.3 3.1.3.1ȡ�ø��п�� 3.1.3.2�������Front�ĸ߶�fontH�Ϳ��fontW 3.1.3.3�������ֵ�����
	 * 3.1.3.4������õ�Ԫ����и�lingH 3.1.3.5������и������ֵ�����¼���������ֵ�� 3.2ȡ�ø��е����߶ȣ������и�
	 *
	 *
	 *
	 * 
	 *
	 */
	public void tShrinkToFitRowHeight() {
		for (int i = 0; i < header.getRows(); i++) {// ������
			CellElement cell = null;
			double maxHeight = 0;// ����и�

			double totalColWidth = 0;// �ϲ��п��
			for (int j = 0; j < header.getColumns(); j++) {// ������
				// ȡ��ǰ��Ԫ��
				cell = this.getCellElement(j, i);
				// ��ǰ��Ԫ���ֵ
				String curCellValue = ("" + cell.getValue()).trim();
				// ��ǰ�п��
				double colWidth = this.getColumnWidth(j);
				// ��һ��Ԫ��
				String nextCellValue = "";
				if (j < header.getColumns() - 1) {// ���һ��
					nextCellValue = ("" + this.getCellElement(j + 1, i)
							.getValue()).trim();
				}
				// �����ǰ�����ݵ�����һ�����ݣ����ۼ��п�
				if (nextCellValue.equalsIgnoreCase(curCellValue)) {
					totalColWidth += colWidth;
					continue;
				}
				// ���򣬼��㵱ǰ�и߶ȣ���պϼ��п��
				totalColWidth += colWidth;// �ۼ��п�
				// ��������ռ�ݵ�����

				int fontLen = curCellValue.length();
				FRFont font = cell.getStyle().getFRFont();
				int fontSize = font.getSize();// �����С
				int delt = 2;// ���
				// ���ݿ��
				double fontWidth = fontSize * fontLen;

				int zs = (int) (fontWidth / totalColWidth);
				double ys = fontWidth % totalColWidth;
				int tRows = zs + (ys > 0 ? 1 : 0);// ����ռ����

				// �����������и�
				double tHeight = (tRows * (fontSize + delt)) < 25 ? 25
						: (tRows * (fontSize + delt));
				// ����ϴ����¼����
				if (tHeight > maxHeight) {
					maxHeight = tHeight;
				}

				// ��պϼ��п��
				totalColWidth = 0;
			}
			this.setRowHeight(i, maxHeight > maxRowHeight ? maxRowHeight : maxHeight);
		}
	}

	/**
	 * �����кţ���������и����� 
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
	 * ���úϼơ�С����
	 *
	 * @param rowIndex
	 */
	public void setSumRow(int rowIndex) {
		this.sumRows.add(new Integer(rowIndex));
	}

	/**
	 * ���кŷ���Ϊ��ӦDataSet������֮��ǩ
	 *
	 * @param rowIndex
	 *            �к�
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
	 * ��DataSet������֮��ǩ����Ϊ��Ӧ�к�
	 *
	 * @param bookmark
	 *            ��ǩ
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
	 * ʹ�ñ�ͷ�ͱ������ݼ��������
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
	 * ����Ĭ�ϱ�ͷ
	 *
	 * @return
	 */
	protected TableHeader createDefaultHeader() {
		return new TableHeader(TreeFactory.getInstance().createTreeNode(null));
	}

	/**
	 * ʹ�ñ�ͷ�ͱ������ݼ��������
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
	 * �ѱ�ͷ��Ϣ��ӵ������
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
	 * ����һ�м�¼��ͬʱ��¼���ݵ�����
	 */
	public void insertRow(int rowIndex) {
		super.insertRow(rowIndex);
		recordCount++;
		createRowCells(rowIndex);
	}

	/**
	 * ����һ�м�¼��ͬʱ��¼���ݵ�����
	 */
	public void appendRow() throws Exception {
		recordCount++;
		createRowCells(header.getRows() + getBodyRowCount() - 1);
	}

	/**
	 * ��ָ����һ�д�����Ԫ��
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
	 * ���ݼ�¼����ǩֵ�жϸ�����¼�Ƿ������Report֮��
	 *
	 * @param bookmark
	 * @return
	 */
	public boolean containsRecord(String bookmark) {
		return this.bookmarkSet.contains(bookmark);
	}

	/**
	 * ��ȡѡ�еĵ�Ԫ��
	 *
	 * @return
	 */
	public CellElement getSelectedCell() {
		CellSelection cs = this.getUI().getCellSelection();
		return getCellElement(cs.getColumn(), cs.getRow());
	}

	/**
	 * ��ȡѡ�е��к�
	 *
	 * @return
	 */
	public int getSelectedRow() {
		CellElement cell = getSelectedCell();
		return cell == null ? -1 : cell.getRow();
	}

	/**
	 * ��ȡĳһ�е����е�Ԫ��
	 *
	 * @param row
	 *            �б��
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
	 * ��ȡѡ�����򣨾��Σ������е�Ԫ��
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
	 * ����bodyData����ӱ������
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
	 * Inits the body row. �Ե�ǰ��¼��Ӧ�ĵ�Ԫ�еĳ�ʼ����
	 *
	 * @param bodyRowIndex the body row index
	 */
	protected void initBodyRow(int bodyRowIndex) {
		// ��¼���ݼ���ǩ���������ѭ�������Ƴ�
		this.bookmarkSet.add(bodyData.toogleBookmark());
		// ��¼������
		if (bodyData
				.containsField(RowFormula.GROUP_SUM_ROW_FLAG_FIELD)) {
			setSumRow(bodyRowIndex + header.getRows());
		}
	}

	/**
	 * Inits the body row. ��ָ����¼��Ӧ�ĵ�Ԫ�еĳ�ʼ����
	 *
	 * @param bodyRowIndex the body row index
	 */
	protected void initBodyRow(int bodyRowIndex, Map record) {
		// ��¼���ݼ���ǩ���������ѭ�������Ƴ� by qj
		this.bookmarkSet.add(bodyData.toggleBookmark(bodyRowIndex));

		// ��¼������
		if (bodyData
				.containsField(RowFormula.GROUP_SUM_ROW_FLAG_FIELD, record)) {
			setSumRow(bodyRowIndex + header.getRows());
		}
	}

//	public CellElement getCellElement(int arg0, int arg1) {
//		CellElement _cell = super.getCellElement(arg0, arg1);

//		// qj ��ְ���ܣ����ᳫ
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
//	 * ��ȡָ����ŵĵ�Ԫ��Ԫ��
//	 *
//	 * @param i
//	 *            ���
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

//		// qj ��ְ���ܣ����ᳫ
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
	 * ��bodyData��ǰ��¼�����������һ�е�Ԫ��.
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
	 * ʹ��ָ��record�����������һ�е�Ԫ��.
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
	 * ��bodyData��ǰ��¼�����ñ���е�һ�е�Ԫ��.
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
			// �жϸ����Ƿ��Ѿ�����
			if (cell.getReport() == null) {
				setupCell(fieldIds[j], cell);
			}
		}
	}

	/**
	 * ��ָ���ļ�¼�����ñ���е�һ�е�Ԫ��
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
	 * ��ָ���ļ�¼�����ñ���е�һ�е�Ԫ��.
	 *
	 * @param record
	 *            the record
	 * @param fieldIds
	 *            the field ids
	 * @param bodyDataRow
	 *            the body data row
	 * @param force
	 *            the forceUpdate �Ƿ�ǿ�Ƹ���
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
			// �����ظ���������
			if(forceUpdate || (cell.getReport() == null)) {
				setupCell(fieldIds[j], cell, record, bodyDataRow);
			}
		}
	}

	/**
	 * Sets the up cell style.
	 *
	 * �̳���ͨ��override�÷������ڵ�Ԫ�񴴽�����������ʽ
	 *
	 * @param cell
	 *            the new up cell style
	 */
	protected void setupCellStyle(Cell cell) {}

	/**
	 * ��DataSet��ǰ��¼������һ����Ԫ�����.
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
	 * ʹ��ָ����¼������һ����Ԫ��
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
	 * ��DataSet��ǰ��¼���趨һ����Ԫ�����.
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
	 * ��DataSet��ǰ��¼���趨һ����Ԫ�����.
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

		//�����п�
		if((fieldId != null) && (cell != null)
//				&& bodyDataRow == 0
				&& (cell.getRow() < 10)) //qinj Ӧ���ǵ�0�У���ȫ�������ǰ10�����ܿ���ͷ�����������ĵĲ���)
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
	 * ��ָ���ļ�¼����ָ���ĵ�Ԫ��
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

		//�����п�
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
	 * ��ָ���ļ�¼����ָ���ĵ�Ԫ��
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

			// ͨ��PropertyProviderEx�ṩ��Ԫ����ʽ
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
//	 * �жϸ�ʹ�ú���cell editor.
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
	 * ���ݼ�¼��ǩ���ֶ�����ϳɵ�Ԫ������
	 *
	 * @param bookmark
	 *            ����ǩ
	 * @param fieldName
	 *            �ֶ���
	 * @return
	 */
	public static String translateToCellName(String bookmark, String fieldName) {
		return bookmark + "_" + Common.nonNullStr(fieldName).toUpperCase();
	}

	/**
	 * �������ɱ�ͷ
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
	 * �����������б��嵥Ԫ��
	 */
	public void refreshBody() throws Exception {
		Object fields[] = header.getFields().toArray();
		this.recordCount = 0;
		appendBody(fields);
	}

	/**
	 * �����������е�Ԫ��
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
					// ��λ����ͷ����Ϣ����Ӧ����
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
	 * ��ȡ�������ݵ�������
	 *
	 * @return
	 */
	public int getBodyRowCount() {
		return recordCount;
	}

	/**
	 * ��ȡֻ����Ԫ���趨�ı���
	 *
	 * @return
	 *
	 * @deprecated ������Ч since cvs 1.4
	 */
	public Background getReadOnlyBackground() {
		return readOnlyBackground;
	}

	/**
	 * ����ֻ����Ԫ��ı���
	 *
	 * @param readOnlyBackground
	 *
	 * @deprecated ������Ч since cvs 1.4
	 */
	public void setReadOnlyBackground(Background readOnlyBackground) {
		this.readOnlyBackground = readOnlyBackground;
	}

	/**
	 * ����ֻ����Ԫ��ı���ɫ
	 *
	 * @param readOnlyBackground
	 *
	 * @deprecated ������Ч since cvs 1.4
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
	 * ���õ����ݲ�����������
	 */
	public void resetWithData() {
		reset();
		removeAllFloatElements();
		clearAllTableData();
		clearSumRowSet();
		clearBookmarkSet();
	}

	/**
	 * ����Ԫ��仯
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
