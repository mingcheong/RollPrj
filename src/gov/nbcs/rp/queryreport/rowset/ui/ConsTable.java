/**
 * @# ConsTable.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.control.AbstractDataField;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableCellRenderer;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:条件显示列表
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class ConsTable extends FTable {

	public static final String FIELD_CNAME = "FIELD_CNAME";

	private FComboBox cbxSourceField;

	private FComboBox cbxOper;

	private FTextField txtValue;

	private RowInfo aRow;

	private List lstRef = null;

	private FBaseTableColumn aColField;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5867250658938339613L;

	public ConsTable() {
		init();
	}

	private void init() {
		cbxOper = new FComboBox();
		cbxOper.setRefModel("=#等于+!=#不等于+<=#小于等于+>=#大于等于"
				+ "+<#小于+>#大于+is#is+is not#is not +in#in ");

		((JComboBox) (cbxOper.getComponent(1)))
				.addItemListener(new EditValueChanged(cbxOper));
		cbxSourceField = new FComboBox();
		cbxSourceField.setRefModel("1#1");

		cbxSourceField.addValueChangeListener(new ValueChangeListener() {// 当选项变动时，则要设置字段类型

					public void valueChanged(ValueChangeEvent arg0) {
						if (aRow == null)
							return;
						String sType = TableSourceServer.getFieldType(aRow
								.getSourceID(), (String) arg0.getNewValue());
						ConsTable.this.getCurrentRow().put(
								Condition.FIELD_TYPE, sType);
					}

				});

		((JComboBox) (cbxSourceField.getComponent(1)))
				.addItemListener(new EditValueChanged(cbxSourceField));

		txtValue = new FTextField();
		txtValue.addValueChangeListener(new EditValueChanged(txtValue));
		// 初始化两列
		aColField = new FBaseTableColumn();

		aColField.setTitle("字段名");

		aColField.setId(Condition.FIELD_CON_FIELD);
		aColField.setIdentifier(Condition.FIELD_CON_FIELD);
		aColField.setWidth(100);
		aColField.addControl(cbxSourceField);
		aColField.setEditable(true);
		// aCol.setEditable(false);
		// aCol.setSortable(false);
		this.addColumn(aColField);
		FBaseTableColumn aCol = new FBaseTableColumn();
		aCol = new FBaseTableColumn();
		aCol.setTitle("比较符");
		aCol.setId(Condition.FIELD_CON_OPER);
		aCol.setIdentifier(Condition.FIELD_CON_OPER);
		aCol.setWidth(100);
		aCol.addControl(cbxOper);
		// aCol.setEditable(false);
		aCol.setEditable(true);
		this.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setTitle("值");
		aCol.setId(Condition.FIELD_CON_VALUE);
		aCol.setIdentifier(Condition.FIELD_CON_VALUE);
		aCol.setWidth(100);
		aCol.addControl(txtValue);
		aCol.setEditable(true);
		aCol.setSortable(false);

		this.addColumn(aCol);
		this.updateUI();

	}

	/**
	 * 显示条件
	 * 
	 * @param aRow
	 *            一格的全部信息
	 * @param aData
	 *            数据源数据,KEY为字段英文名，value为字段中文名
	 */
	public void setValue(RowInfo aRow, List lstRef) {
		this.setData(new ArrayList());
		this.aRow = aRow;
		if (this.lstRef != lstRef) {
			this.lstRef = lstRef;
			cbxSourceField.setRefModel(transRefString(lstRef));
			aColField.addControl(cbxSourceField);
			((FBaseTableCellRenderer) aColField.getCellRenderer())
					.setRefModel(cbxSourceField.getRefModel());

		}
		this.setData(aRow.getConList());
	}

	public void add() {
		if (aRow == null)
			return;
		if (Common.isNullStr(aRow.getTableName()))
			return;
		Condition aCon = aRow.addDefaultCon();
		this.addRow(aCon.getXML());
	}

	private String transRefString(List aData) {
		if (aData == null || aData.size() < 1)
			return "#";
		StringBuffer sb = new StringBuffer();
		List lstKey = (List) aData.get(0);
		List lstValue = (List) aData.get(1);
		int size = lstKey.size();
		for (int i = 0; i < size; i++) {
			sb.append(lstKey.get(i)).append("#").append(lstValue.get(i))
					.append("+");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public boolean del() {
		XMLData aData = this.getCurrentRow();
		if (aData == null) {
			return false;
		}
		int i = this.getCurrentRowIndex();
		this.deleteRow(i);
		return (aRow.getLstCons().remove(i) != null);

	}

	class EditValueChanged implements ItemListener, ValueChangeListener {
		private AbstractDataField aComp;

		public EditValueChanged(AbstractDataField aComp) {
			this.aComp = aComp;
		}

		public void itemStateChanged(ItemEvent e) {
			if (ConsTable.this.getCurrentRowIndex() < 0)
				return;
			Condition aCon = (Condition) aRow.getLstCons().get(
					ConsTable.this.getCurrentRowIndex());
			if (aComp == cbxSourceField) {// 当字段改变
				aCon.setConField(aComp.getValue().toString());
				if (aRow == null)
					return;
				String sType = TableSourceServer.getFieldType(aRow
						.getSourceID(), aComp.getValue().toString());
				ConsTable.this.getCurrentRow().put(Condition.FIELD_TYPE, sType);
				aCon.setFieldType(sType);

			} else if (aComp == cbxOper) {// 当比较符选中
				aCon.setConOper(aComp.getValue().toString());
			} else {
				aCon.setConValue(aComp.getValue().toString());
			}

		}

		public void valueChanged(ValueChangeEvent arg0) {
			Condition aCon = (Condition) aRow.getLstCons().get(
					ConsTable.this.getCurrentRowIndex());
			aCon.setConValue((String) aComp.getValue());

		}
	}

}
