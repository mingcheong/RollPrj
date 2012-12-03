/*
 * $Id: TFixComboBoxCellEditor.java,v 1.3 2008/03/27 11:43:02 yinmingquan Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */
package gov.nbcs.rp.input.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.report.cell.editors.BoundFocusEvent;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:下来框的cellEditor
 * </p>
 * <p>
 * Description:在finereport上添加的celleditor,下拉框形式，显示名称，返回名称
 * </p>
 * <p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class TFixComboBoxCellEditor extends AbstractCellEditor {
	// 复选框
	protected JComboBox comboBox = null;
	// 复选框里的列表
	protected Object[] alist = null;
	// 表格当前行的数据集
	protected DataSet ds = null;
	// 搭档列字段
	protected String field = "";
	// 表格界面 finereport刷新用
	protected ReportUI reportUI;

	protected DataSet dsField;
	protected String fieldID;

	/** 编码名称数组 */

	/**
	 * 构造方法：
	 * 
	 * @param alist
	 *            comboBox数据
	 * @param col
	 *            返回列：0列编码，1列名称
	 * @param ds
	 *            fineReport当前行的数据集
	 * @param field
	 *            搭档的表列字段
	 */
	public TFixComboBoxCellEditor(ReportUI reportUI, Object[] alist,
			DataSet ds, String field) {
		this.reportUI = reportUI;
		this.alist = alist;
		this.ds = ds;
		this.field = field;
		if (alist == null) {
			comboBox = new JComboBox(new DefaultComboBoxModel());
		} else {
			comboBox = new JComboBox(new DefaultComboBoxModel(alist));
		}
		comboBox.setCursor(Cursor.getDefaultCursor());
		comboBox.addItemListener(new TItemListener(this, ds, field));
		new BoundFocusEvent().bind(this, comboBox);
	}

	public TFixComboBoxCellEditor(ReportUI reportUI, Object[] alist,
			DataSet ds, DataSet dsField, String field, String fieldID) {
		this.reportUI = reportUI;
		this.alist = alist;
		this.ds = ds;
		this.field = field;
		this.dsField = dsField;
		this.fieldID = fieldID;
		if (alist == null) {
			comboBox = new JComboBox(new DefaultComboBoxModel());
		} else {
			comboBox = new JComboBox(new DefaultComboBoxModel(alist));
		}
		comboBox.setCursor(Cursor.getDefaultCursor());
		comboBox.addItemListener(new TItemListener(this, ds, field));
		new BoundFocusEvent().bind(this, comboBox);
	}

	/**
	 * 
	 * comboBox监听器， 用途：为搭档赋值
	 * 
	 */
	class TItemListener implements ItemListener {
		TFixComboBoxCellEditor cellEditor = null;

		DataSet ds = null;

		String field = null;

		TItemListener(TFixComboBoxCellEditor cellEditor, DataSet ds,
				String field) {
			this.cellEditor = cellEditor;
			this.ds = ds;
			this.field = field;
		}

		public void itemStateChanged(ItemEvent e) {
			try {
				Report report = (Report) reportUI.getWorkSheet();
				Cell cell = (Cell) report.getSelectedCell();
				ds.gotoBookmark(cell.getBookmark());
				String value = comboBox.getSelectedItem().toString();
				if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof()) {
					ds.fieldByName(field).setValue(value);
					if (!Common.isNullStr(fieldID)) {
						if (dsField != null && !dsField.isEmpty()
								&& dsField.locate("NAME", value)) {
							String code = dsField.fieldByName("CODE")
									.getString();
							String name = dsField.fieldByName("NAME")
							.getString();
							ds.edit();
							String id = dsField.fieldByName("YSJC_BM").getString();
							ds.fieldByName("YSJC_DM").setValue(code);
							ds.fieldByName("YSJC_DM").setValue(id);
							ds.fieldByName("YSJC_MC").setValue(name);
							
						}
					}
					reportUI.repaint();
					cellEditor.fireEditingStopped();
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 获取单元格的值
	 */
	public Object getCellEditorValue() throws Exception {
		if (comboBox.getItemCount() > 0) {
			return comboBox.getSelectedItem().toString().trim();
		}
		return null;

	}

	/**
	 * 获取单元格的编辑区
	 */
	public Component getCellEditorComponent(Grid grid, CellElement cell) {
		Object value = cell.getValue();
		if (value != null) {// &&
			for (int i = 0; i < alist.length; i++) {
				String str = Common.nonNullStr(alist[i]);
				if (value.toString().equalsIgnoreCase(str)) {
					comboBox.setSelectedIndex(i);
					break;
				}
			}
			comboBox.setSelectedItem(value);
		}
		return comboBox;
	}
}
