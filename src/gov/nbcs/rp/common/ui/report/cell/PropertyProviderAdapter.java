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
import com.fr.base.Style;
import com.fr.cell.editor.CellEditor;

/**
 * The Class PropertyProviderAdapter. �ӿ�PropertyProviderEx����������
 *
 */
public abstract class PropertyProviderAdapter implements PropertyProviderEx {

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#getColumnWidth(java.lang.Object)
	 */
	public double getColumnWidth(Object fieldId) {
		return 50;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#getEditor(java.lang.String,
	 *      java.lang.Object)
	 */
	public CellEditor getEditor(String bookmark, Object fieldId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#getFieldId(java.lang.String)
	 */
	public Object getFieldId(String fieldName) {
		return fieldName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#getFieldName(java.lang.Object)
	 */
	public String getFieldName(Object fieldId) {
		return Common.nonNullStr(fieldId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#getFormat(java.lang.String,
	 *      java.lang.Object)
	 */
	public Format getFormat(String bookmark, Object fieldId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProvider#isEditable(java.lang.String,
	 *      java.lang.Object)
	 */
	public boolean isEditable(String bookmark, Object fieldId) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nbcs.rp.common.ui.report.cell.PropertyProviderEx#getCellStyle(gov.nbcs.rp.common.ui.report.cell.Cell)
	 */
	public Style getCellStyle(Cell cell) {
		return null;
	}

}
