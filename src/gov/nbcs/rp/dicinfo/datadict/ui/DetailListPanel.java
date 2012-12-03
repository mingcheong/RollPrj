/**
 * @# DetailListPanel.java    <�ļ���>
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
 * ����˵��:�ֵ���ϸ�ֶε���ʾ�ı༭
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

	private XMLData xmlRefFields;// �ο��е�ѡ�񻺴�

	public DetailListPanel() {
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public DetailListPanel(String tableID) {
		try {
			initUI();
			setTableID(tableID);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
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
		// ����
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
				// TODO �Զ����ɷ������

			}

		});
		cbxRefField = new FComboBox();
		cbxRefField.setRefModel(dataDicBean.getAllRefFields(Global.loginYear));

		cbxRefField.getComponent(1).addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// ���ǰ��û��ѡ��IDֵ����˴�û��ѡ��
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

		// ������ʾ���ֶ�
		tbDetail = new FTable();

		FBaseTableColumn aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.TABLE_ENAME);
		aCol.setVisible(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_CNAME);
		aCol.setTitle("�ֶ�����");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.setWidth(300);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_ENAME);
		aCol.setTitle("�ֶ�Ӣ������");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_TYPE);
		aCol.setTitle("��������");
		aCol.setEditable(false);
		aCol.setSortable(false);
		FComboBox cbxFIELD_TYPE = new FComboBox();
		cbxFIELD_TYPE
				.setRefModel("�ַ���#�ַ���+�ַ���#�ַ���+������#������+����#����+������#������+����#����+������#������");
		aCol.addControl(cbxFIELD_TYPE);

		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AIS_VISIBLE);
		aCol.setTitle("�Ƿ�ɼ�");
		aCol.setEditable(false);
		FComboBox cbxIsVisible = new FComboBox();
		cbxIsVisible.setRefModel("#+��#��+��#��");
		aCol.addControl(cbxIsVisible);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AFIELD_SORT);
		aCol.setTitle("�����");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);
		// ________________
		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.REFCOL_ID);
		aCol.setTitle("�ο���");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.addControl(cbxRefID);
		tbDetail.addColumn(aCol);
		// �ο���ѡ��
		cbxRefID.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				XMLData aData = tbDetail.getCurrentRow();
				if (aData == null)
					return;
				aData.put(IDataDictBO.REFCOL_NAME, cbxRefID.getRefModel()
						.getNameByValue(cbxRefID.getValue()));
				// ��ֵ˵���ֶ�(���)//�޸�ѡ����
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
		aCol.setTitle("�ο�������");
		aCol.setEditable(false);
		aCol.setVisible(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.CON_FIELDENAME);
		aCol.setTitle("ȡ�������е�����");
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.addControl(cbxRefField);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.CON_FIELDCNAME);
		aCol.setTitle("ȡ�������е���˵��");
		aCol.setEditable(false);
		aCol.setSortable(false);
		tbDetail.addColumn(aCol);

		aCol = new FBaseTableColumn();
		aCol.setId(IDataDictBO.AMEMO);
		aCol.setTitle("��ע");
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
	// ����ʾ˳��ȡ�ø��ֶ�
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
