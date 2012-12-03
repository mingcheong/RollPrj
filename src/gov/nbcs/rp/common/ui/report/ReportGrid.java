/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ���������࣬����CellEditor�Ĺ���
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import com.fr.cell.Grid;
import com.fr.cell.ReportPane;
import com.fr.cell.editor.CellEditor;
import com.fr.cell.editor.NumberCellEditor;
import com.fr.cell.editor.TextCellEditor;
import com.fr.report.CellElement;

public class ReportGrid extends Grid {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// /**
	// * �������
	// */
	// private Report report;
	//
	// /**
	// * Instantiates a new report grid.
	// *
	// * @param report
	// * the report
	// */
	// public ReportGrid(Report report) {
	// this.report = report;
	// }

	/**
	 * ���ָ�ʽ����ĵ�Ԫ��༭��
	 */
	private CellEditor numberEditor = new NumberCellEditor();

	/**
	 * �ı�����ĵ�Ԫ��༭��
	 */
	private CellEditor textEditor = new TextCellEditor();

	/**
	 * ��ȡ��Ԫ��༭��
	 */
	public CellEditor getCellEditor(int column, int row) {
		ReportPane reportPane = this.getReportPane();
		com.fr.report.Report _report = reportPane.getReport();
		CellElement _cell = _report.getCellElement(column, row);
		if ((_report instanceof Report) && (_cell instanceof Cell)) {
			Report report = (Report) _report;
			Cell cell = (Cell) _cell;
			Object fieldId = report.getCellProperty().getFieldId(
					cell.getFieldName());
			String bookmark = cell.getBookmark();
			CellEditor editor = report.getCellProperty().getEditor(bookmark,
					fieldId);
			if (editor == null) {
				try {
					if (report.getBodyData() == null) {
						return null;
					}
					Field field = report.getBodyData().fieldByName(
							cell.getFieldName());
					if (field == null) {
						return null;
					}
					Class fieldType = field.getValueType();
					if ((fieldType == null) && (cell.getValue() != null)) {
						fieldType = cell.getValue().getClass();
					}
					if ((fieldType != null)
							&& Number.class.isAssignableFrom(fieldType)) {
						editor = numberEditor;
					} else {
						editor = textEditor;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (editor != null) {
				// if
				// (NumberCellEditor.class.isAssignableFrom(editor.getClass())
				if ((editor instanceof NumberCellEditor)
						&& (cell.getFractionDigits() >= 0)) {
					((NumberCellEditor) editor).setMaxDecimalLength(cell
							.getFractionDigits());
				}
			}
			return editor;
		}
		return super.getCellEditor(column, row);
	}

	// /**
	// * Sets the report.
	// *
	// * @param report
	// * the new report
	// */
	// public void setReport(Report report) {
	// this.report = report;
	// }

}
