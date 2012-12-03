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
package gov.nbcs.rp.common.ui.report.cell;

import java.text.Format;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.ui.report.Report;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.report.CellElement;
import com.fr.report.cellElement.CellGUIAttr;

public class Cell extends CellElement {

	private static final long serialVersionUID = 1L;

	private String bookmark;

	private String fieldName;

	private Report report;

	private boolean locked;

//	private CellEditor innerEditor;

//	private Background rowSelectedBack;
	
	private boolean rowSelected;

	private int decorateAttr;

	private int editableState;

	private Format format;

	private int fractionDigits = -1;

	public static final int BODY_CELL = 1;

	public static final int HEADER_CELL = 2;

	private int cellType = BODY_CELL;

	// private boolean printing;

	private boolean hasCustomStyle = false;

	public Cell() {
		super();
	}

	public Cell(int column, int row, int colSpan, int rowSpan, Object value) {
		super(column, row, colSpan, rowSpan, value);
	}

	public Cell(int column, int row, Object value) {
		super(column, row, value);
	}

	public Cell(int column, int row) {
		super(column, row);
	}
	
	public boolean canIsZero =true;
	

	/**
	 * �����Զ���style
	 * 
	 * @param style
	 *            �Զ����style��nullʱ��ʹ��ϵͳȷ������ʽ
	 * @author qj
	 */
	public void setStyle(Style style) {
		hasCustomStyle = style != null;
		super.setStyle(style);
	}

	/**
	 * override CellElement��getStyle()������ʹ�õ�Ԫ���ܸ�������״̬ѡ��Style
	 * <p>
	 * �������setStyle(Style style)�趨style������getStyle()������
	 * </p>
	 * 
	 * @author qj
	 */
	public Style getStyle() {
		Style style;
		if (hasCustomStyle) {
			style = super.getStyle();
		} else {
			// ��ӡʱ����ʽ
			if (report==null) {
				return CellStyle.getInstance(this);
			}
			if (report.isPrinting()) {
				style = CellStyle.PRINTING_STYLE;
			} else {
//				if (!report.isTableEditable()) {
//					style = CellStyle.getIntanceReadonly(this);
//				} else {
					style = CellStyle.getInstance(this);
//				}
			}
		}

		// ��ReportΪ�Ǵ�ӡ���Ǳ༭״̬�£�ѡ���е�Ч��
		// modify by qzc 20090714
		if (rowSelected && (report != null) && !report.isPrinting()
				&& !report.isTableEditable()) {
			style = style.deriveBackground(ColorBackground
					.getInstance(CellStyle.BackgroundColors.SELECTED_COLOR));
		}

		// ���Ӹ�ʽ
		if (format != null) {
			style = style.deriveFormat(format);
		}

		return style;
	}

	// public Style getStyle() {
	// if(getCellType()==HEADER_CELL) {
	// return TableHeader.PRINTING_STYLE;
	// }
	// if (getCellType() == BODY_CELL
	// && super.getStyle() != null
	// && super.getStyle().getClass().hashCode() == CellStyle.class
	// .hashCode()) {
	// CellStyle style = (CellStyle) super.getStyle();
	// style.setCell(this);
	// return style;
	// }
	// return super.getStyle();
	// }

	public CellGUIAttr getCellGUIAttr() {

		if (getCellType() == BODY_CELL) {
			if (super.getCellGUIAttr() instanceof GUIAttribute) {
				GUIAttribute attr = (GUIAttribute) super.getCellGUIAttr();
				if (attr != null) {
					attr.setCell(this);
				}
				return attr;
			}
		}
		return super.getCellGUIAttr();
	}

	/**
	 * ������Ԫ��Ϊ��ֵ
	 * 
	 * @param locked
	 */
	public void lock() throws Exception {
		this.locked = true;
		if ((this.report != null) && (this.report.getBodyData() != null)) {
			DataSet ds = report.getBodyData();
			String tmpBookmark = ds.toogleBookmark();
			ds.maskDataChange(true);
			ds.gotoBookmark(bookmark);
			ds.fieldByName(this.fieldName).lock();
			ds.gotoBookmark(tmpBookmark);
			ds.maskDataChange(false);
		}
	}

	public void unLock() throws Exception {
		this.locked = false;
		if ((this.report != null) && (this.report.getBodyData() != null)) {
			DataSet ds = report.getBodyData();
			String tmpBookmark = ds.toogleBookmark();
			ds.maskDataChange(true);
			ds.gotoBookmark(bookmark);
			ds.fieldByName(this.fieldName).unlock();
			ds.gotoBookmark(tmpBookmark);
			ds.maskDataChange(false);
		}
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getBookmark() {
		return bookmark;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	} 

	// /**
	// * ���õ�Ԫ���ֵ
	// */
	// public void setValue(Object value) {
	// if (this.isLocked())
	// return;
	// if (report != null && report.isMaskZeroValue()
	// && Common.isZeroNumber(value)) {
	// super.setValue(null);
	// } else {
	// super.setValue(value);
	// }
	// if (report != null && !report.isMaskCellValueChange()) {
	// try {
	// DataSet ds = report.getBodyData();
	// boolean located = false;
	// Field field = null;
	// String tmpBookmark = ds.toogleBookmark();
	// ds.maskDataChange(true);
	// if (located = ds.gotoBookmark(this.getBookmark())) {
	// field = ds.fieldByName(this.getFieldName());
	// field.setValue(value);
	// }
	// ds.gotoBookmark(tmpBookmark);
	// ds.maskDataChange(false);
	// if (located)
	// ds.fireDataChange(report, field,
	// DataChangeEvent.FIELD_MODIRED);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	// }
	/**
	 * ���õ�Ԫ���ֵ
	 */
	public void setValue(Object value) {
		if (this.isLocked()) {
			return;
		}
		if ((report != null) && report.isMaskZeroValue()
				&& Common.isZeroNumber(value)&&canIsZero) {
			super.setValue(null);
		} else if ((report != null) && report.isMaskNegativeValue()
				&& Common.isNegativeNumber(value)) {
			super.setValue(null);
		} else {
			super.setValue(value);
		}
		if ((report != null) && !report.isMaskCellValueChange()) {
			try {
				DataSet ds = report.getBodyData();
				boolean located = false;
				Field field = null;
				String tmpBookmark = ds.toogleBookmark();
				ds.maskDataChange(true);
				if (located = ds.gotoBookmark(this.getBookmark())) {
					field = ds.fieldByName(this.getFieldName());
					field.setValue(value);
				}
				ds.gotoBookmark(tmpBookmark);
				ds.maskDataChange(false);
				ds.gotoBookmark(this.getBookmark());
				if (located) {
					ds.fireDataChange(report, field,
							DataChangeEvent.FIELD_MODIRED);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public int getCellType() {
		return cellType;
	}

	public void setCellType(int cellType) {
		this.cellType = cellType;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public int getEditableState() {
		return editableState;
	}

	public void setEditableState(int editableState) {
		this.editableState = editableState;
	}

//	public CellEditor getInnerEditor() {
//		return innerEditor;
//	}
//
//	public void setInnerEditor(CellEditor innerEditor) {
//		this.innerEditor = innerEditor;
//	}

	public boolean isLocked() {
		return locked;
	}

	public int getFractionDigits() {
		return fractionDigits;
	}

	public void setFractionDigits(int fractionDigits) {
		this.fractionDigits = fractionDigits;
	}

	/**
	 * @return the decorateAttr
	 */
	public int getDecorateAttr() {
		return decorateAttr;
	}

	/**
	 * @param decorateAttr
	 *            the decorateAttr to set
	 */
	public void setDecorateAttr(int decorateAttr) {
		this.decorateAttr = decorateAttr;
	}

	/**
	 * @return the rowSelected
	 */
	public boolean isRowSelected() {
		return rowSelected;
	}

	/**
	 * @param rowSelected the rowSelected to set
	 */
	public void setRowSelected(boolean rowSelected) {
		this.rowSelected = rowSelected;
	}

	public boolean isCanIsZero() {
		return canIsZero;
	}

	public void setCanIsZero(boolean canIsZero) {
		this.canIsZero = canIsZero;
	}

	// public boolean isPrinting() {
	// return report != null ? report.isPrinting() : printing;
	// }
	//
	// public void setPrinting(boolean printing) {
	// this.printing = printing;
	// }
}
