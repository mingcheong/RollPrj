/**
 * @# DetailListPanel.java    <文件名>
 */
package gov.nbcs.rp.dicinfo.datadict.ui;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


import gov.nbcs.rp.basinfo.common.BaseUtil;
import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:字典明细字段的显示的编辑
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class DetailListPanel extends FPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1767774218195593684L;

	private boolean isEdit = false;

	private String sTableID = null;

	private FTable tbDetail = null;

	private IDataDictBO dataDicBean = null;

	private FComboBox cbxRefID;

	private FComboBox cbxRefField;

	private XMLData xmlRefFields;// 参考列的选择缓存

	public DetailListPanel() {
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public DetailListPanel(String tableID) {
		try {
			initUI();
			setTableID(tableID);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void setTableID(String tableID) {
		setEditable(false);
		this.sTableID = tableID;
		initData();
	}

	public void cancelEdit() {
		this.setTableID(this.sTableID);
	}

	private void initUI() throws Exception {
	//	dataDicBean = (IDataDictBO) BOCache.getBO("fb.dataDictService");
		dataDicBean = (IDataDictBO)ServiceFactory.getServiceInterface("rp.dataDictService", "gov/nbcs/rp/dicinfo/datadict/conf/dataDictConf.xml");
		initCols();
		this.setLayout(new BorderLayout());
		this.add(tbDetail, BorderLayout.CENTER);
		// 缓存
		xmlRefFields = dataDicBean.getRefDataFieldSelection(Global.loginYear);
	}

	private void initData() {

		this.tbDetail.setData((List) null);
		List lstDetail = dataDicBean.getDicField(sTableID, Global.loginYear);
		this.tbDetail.setData(lstDetail);
	}

	private void initCols() throws Exception {
		cbxRefID = new FComboBox();
		cbxRefID.setRefModel(dataDicBean.getRefDataString(Global.loginYear));
		cbxRefID.getComponent(1).addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (cbxRefID.getValue() == null
						|| cbxRefID.getValue().equals(""))
					cbxRefID.setSelectedIndex(0);

			}

			public void focusLost(FocusEvent e) {
				// TODO 自动生成方法存根

			}

		});
		cbxRefField = new FComboBox();
		cbxRefField.setRefModel(dataDicBean.getAllRefFields(Global.loginYear));

		cbxRefField.getComponent(1).addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// 如果前面没有选择ID值，则此处没有选项
				XMLData aData = tbDetail.getCurrentRow();
				if (aData == null || (aData.get(IDataDictBO.REFCOL_ID) == null)
						|| (aData.get(IDataDictBO.REFCOL_ID).equals(""))) {
					cbxRefField.setRefModel("#");
				} else
					cbxRefField.setRefModel((String) xmlRefFields.get((aData
							.get(IDataDictBO.REFCOL_ID))));

			}

			public void focusLost(FocusEvent e) {

			}

		});

		// 生成显示的字段
		tbDetail = new FTable();

		FBaseTableColumn aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.TABLE_ENAME);
		aCol.setVisible(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_CNAME);
		aCol.setTitle("字段名称");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.setWidth(300);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_ENAME);
		aCol.setTitle("字段英文名称");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_TYPE);
		aCol.setTitle("数据类型");
		aCol.setEditable(false);
		aCol.setSortable(false);
		FComboBox cbxFIELD_TYPE = new FComboBox();
		cbxFIELD_TYPE
				.setRefModel("字符型#字符型+字符串#字符串+浮点型#浮点型+货币#货币+货币型#货币型+整型#整型+日期型#日期型");
		aCol.addControl(cbxFIELD_TYPE);

		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AIS_VISIBLE);
		aCol.setTitle("是否可见");
		aCol.setEditable(false);
		FComboBox cbxIsVisible = new FComboBox();
		cbxIsVisible.setRefModel("#+是#是+否#否");
		aCol.addControl(cbxIsVisible);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_SORT);
		aCol.setTitle("排序号");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);
		// ________________
		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.REFCOL_ID);
		aCol.setTitle("参考列");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.addControl(cbxRefID);
		tbDetail.addColumn(aCol);
		// 参考列选项
		cbxRefID.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				XMLData aData = tbDetail.getCurrentRow();
				if (aData == null)
					return;
				aData.put(IDataDictBO.REFCOL_NAME, cbxRefID.getRefModel()
						.getNameByValue(cbxRefID.getValue()));
				// 赋值说明字段(清空)//修改选择项
				if (arg0.getNewValue() == null || arg0.getNewValue().equals("")) {
					aData.put(IDataDictBO.CON_FIELDCNAME, null);
					cbxRefField.setRefModel("#");
					cbxRefField.setValue(null);
				} else {
					aData.put(IDataDictBO.CON_FIELDCNAME, aData
							.get(IDataDictBO.AFIELD_CNAME));
					cbxRefField.setRefModel((String) xmlRefFields.get(arg0
							.getNewValue()));
				}

			}

		});

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.REFCOL_NAME);
		aCol.setTitle("参考列名称");
		aCol.setEditable(false);
		aCol.setVisible(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.CON_FIELDENAME);
		aCol.setTitle("取数条件中的列名");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.addControl(cbxRefField);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.CON_FIELDCNAME);
		aCol.setTitle("取数条件中的列说明");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AMEMO);
		aCol.setTitle("备注");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

	}

	public void setEditable(boolean editAble) {
		if (isEdit == editAble)
			return;
		isEdit = editAble;

		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.REFCOL_ID, editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.CON_FIELDENAME,
				editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.CON_FIELDCNAME,
				editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.AMEMO, editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.AFIELD_SORT,
				editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.AIS_VISIBLE,
				editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.AFIELD_CNAME,
				editAble);
		BaseUtil.setTableColEditable(tbDetail, IDataDictBO.AFIELD_TYPE,
				editAble);

	}

	// public List getSaveString() throws Exception {
	// List lstData = tbDetail.getData();
	// int iCount = lstData.size();
	// List lstString = new ArrayList();
	// IDataDictBO aDicField;
	// XMLData aData;
	//
	// for (int i = 0; i < iCount; i++) {
	// aData = (XMLData) lstData.get(i);
	// aDicField = new IDataDictBO(aData, tbDetail
	// .convertModelRowIndexToView(i));
	// lstString.add((aDicField.getUpdateSql()));
	// }
	// return lstString;
	// }
	// 按显示顺序取得各字段
	public List getFieldByOrdered() {
		List lstData = tbDetail.getData();
		List lstResult = new ArrayList();
		int iCount = lstData.size();
		for (int i = 0; i < iCount; i++) {
			lstResult.add(lstData.get(tbDetail.convertViewRowIndexToModel(i)));
		}
		return lstResult;
	}

}
