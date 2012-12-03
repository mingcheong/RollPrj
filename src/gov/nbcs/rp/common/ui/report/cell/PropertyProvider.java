/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
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
	 * 注意：需要同时实现 getFieldId(String fieldName)方法
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
	 * 获取字段对应的数据显示格式，在字段所在的列表体数据都会被相应的格式化显示.
	 * 
	 * @param fieldId
	 *            the field id
	 * @param bookmark
	 *            the bookmark
	 * @return the format
	 */
	public Format getFormat(String bookmark, Object fieldId);

	/**
	 * 字段ID和字段名的转换，字段ID是表格头创建单元格所使用的关键字端，但是通过表格头获取
	 * 表格体取数时要用取数的字段名字，字段ID和取数字段名可能一样，更可能不一样，因此就需 要这个转换方法.
	 * 
	 * @param fieldId
	 *            the field id
	 * @return the field name
	 */
	public String getFieldName(Object fieldId);

	/**
	 * 和<code>getFieldName()</code>方法的功能相反.
	 * 
	 * @param fieldName
	 *            the field name
	 * @return the field id
	 */
	public Object getFieldId(String fieldName);
}
