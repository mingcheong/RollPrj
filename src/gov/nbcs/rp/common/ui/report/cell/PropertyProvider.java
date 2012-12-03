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

import com.fr.cell.editor.CellEditor;

/**
 * The Interface PropertyProvider.
 */
public interface PropertyProvider {

	/**
	 * Checks if is editable.
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @param fieldId
	 *            the field id
	 * @return true, if is editable
	 */
	public boolean isEditable(String bookmark, Object fieldId);

	/**
	 * Gets the editor. <br>
	 * ע�⣺��Ҫͬʱʵ�� getFieldId(String fieldName)����
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @param fieldId
	 *            the field id
	 * @return the editor
	 * @see PropertyProvider#getFieldId(java.lang.String)
	 */
	public CellEditor getEditor(String bookmark, Object fieldId);

	/**
	 * Gets the column width.
	 * 
	 * @param fieldId
	 *            the field id
	 * @return the column width
	 */
	public double getColumnWidth(Object fieldId);

	/**
	 * ��ȡ�ֶζ�Ӧ��������ʾ��ʽ�����ֶ����ڵ��б������ݶ��ᱻ��Ӧ�ĸ�ʽ����ʾ.
	 * 
	 * @param fieldId
	 *            the field id
	 * @param bookmark
	 *            the bookmark
	 * @return the format
	 */
	public Format getFormat(String bookmark, Object fieldId);

	/**
	 * �ֶ�ID���ֶ�����ת�����ֶ�ID�Ǳ��ͷ������Ԫ����ʹ�õĹؼ��ֶˣ�����ͨ�����ͷ��ȡ
	 * �����ȡ��ʱҪ��ȡ�����ֶ����֣��ֶ�ID��ȡ���ֶ�������һ���������ܲ�һ������˾��� Ҫ���ת������.
	 * 
	 * @param fieldId
	 *            the field id
	 * @return the field name
	 */
	public String getFieldName(Object fieldId);

	/**
	 * ��<code>getFieldName()</code>�����Ĺ����෴.
	 * 
	 * @param fieldName
	 *            the field name
	 * @return the field id
	 */
	public Object getFieldId(String fieldName);
}
