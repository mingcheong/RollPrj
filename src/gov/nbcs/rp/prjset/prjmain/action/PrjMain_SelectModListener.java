/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.action;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:��Ŀ�걨�ؼ�ά������
 * </p>
 * <p>
 * Description:��Ŀ�걨�ؼ�ά�������Ӧ�ļ�����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class PrjMain_SelectModListener {

	private static Logger log = Logger.getLogger(PrjMain_SelectModListener.class);
	//�����������Ϣ�б�
	private List resultList = new ArrayList();
	/**
	 * �����棬���ڵ��ø�����
	 */
	private PrjMain_SelectModUI prjMainSelectModUI;
	/**
	 * GeXinying
	 * @throws Exception
	 */
	private void setCompEnameValue() throws Exception {
		List hadList = prjMainSelectModUI.getPrjMainService().getFieldHadUsed();
		List allList = prjMainSelectModUI.getPrjMainService().getFieldNameByTable("RP_P_BASE");
		//
		allList.removeAll(hadList);
		resultList.clear();
		for(int i=0;i<allList.size();i++) {
			resultList.add(new FieldInfo((String)allList.get(i)));
		}
		//����
		Collections.sort(resultList);
		if(resultList.size()>0) {
			prjMainSelectModUI.getComp_ename().setValue(((FieldInfo)resultList.get(0)).getName());
		}else {
			throw new Exception("����ĸC��ͷ���ֶ��Ѿ������޷����������µ���Ϣ��");
		}
	}
	/**
	 * ��ʼ������������
	 */
	private void clearUICompotents() {
		prjMainSelectModUI.getComp_id().setValue("");
		prjMainSelectModUI.getComp_name().setValue("");
		
		prjMainSelectModUI.getComp_ename().setValue("");

		//---
		prjMainSelectModUI.getField_type().setValue("");
//		prjMainSelectModUI.getIs_comm().setValue("");
//		prjMainSelectModUI.getComp_min_len().setValue("0");
//		prjMainSelectModUI.getComp_max_len().setValue("0");
//		prjMainSelectModUI.getComp_min_value().setValue("");
//		prjMainSelectModUI.getComp_max_value().setValue("");
		prjMainSelectModUI.getIs_hide().setValue("");
//		prjMainSelectModUI.getData_format().setValue("");
		// �����ϸ���ݱ�
		prjMainSelectModUI.getTable_data_detail().deleteAllRows();
	}

	/**
	 * ������ϸ���ݱ�
	 */
	private void setTable_data_detail() {
		// �����ϸ���ݱ�
		prjMainSelectModUI.getTable_data_detail().deleteAllRows();
		// ���û��ѡ�����ڵ� ���� ѡ����ڵ� ���˳�
		if ( prjMainSelectModUI.getTree_data().getSelectionPath() != null ) {
			if ( prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() != null ) {
				String comp_id;
				try {
					// ��ȡ������
					comp_id = prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_id").getString();
					// ��ȡ��ϸ����
					List list = prjMainSelectModUI.getPrjMainService().getComboxValues(comp_id);
					// ������ϸ����
					prjMainSelectModUI.getTable_data_detail().setData(list);
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �ж��б��Ƿ������ص�����
	 * 
	 * @param xmlData
	 *            ��Ҫ�жϵ�����
	 * @param exceptData
	 *            ��Ҫ��ȥ�жϵ�����
	 * @return �Ƿ������ص�����
	 */
	private boolean HasItemcode(XMLData xmlData, XMLData exceptData) {
		// ���������ϸ����
		List list = prjMainSelectModUI.getTable_data_detail().getData();
		// �����Ҫ�жϵı��
		String sID = (String) xmlData.get("item_code");
		// �õ���ȥ�жϵı��
		String sExceptID = null;
		if ( exceptData != null ) {
			sExceptID = (String) exceptData.get("item_code");
		}
		// ���û���������ֱ�ӷ���false
		if ( list != null ) {
			// ����ϸ�����ݽ��б������ж�
			for ( int i = 0 ; i < list.size() ; i++ ) {
				String data = (String) ((XMLData) list.get(i)).get("item_code");
				// �����ԭ���޸ĵ�������ݣ�������ͨ����Ҳ����˵���޸�ԭ�����ݣ�
				if ( !data.equals(sExceptID) ) {
					// ���list��������ݰ����޸ĵ����ݣ��Ͳ�����ͨ��
					if ( sID.equals(data) ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * ��������UI���ݽ��������ڻ�ȡ�����ڵĸ�����
	 * 
	 * @param modUI
	 *            ������UI����
	 */
	public PrjMain_SelectModListener(PrjMain_SelectModUI modUI) {
		this.prjMainSelectModUI = modUI;
	}

	/**
	 * ���ڵ�ѡ���¼�
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void tree_datavalueChanged(TreeSelectionEvent e) {
		try {
			// ���δѡ�����ڵ����ѡ����ڵ� ��ʼ������
			if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
					|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
				clearUICompotents();
			} else {
				// ���ݽڵ�������ʾ����
				prjMainSelectModUI.getComp_id().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_id").getString());
				prjMainSelectModUI.getComp_name().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_name").getString());
				prjMainSelectModUI.getComp_ename().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_ename").getString());
				prjMainSelectModUI.getField_type().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("field_type").getString());
//				prjMainSelectModUI.getIs_comm().setValue(Boolean.valueOf(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("is_comm").getString().equals("1")));
//				prjMainSelectModUI.getComp_min_len().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_min_len").getString());
//				prjMainSelectModUI.getComp_max_len().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_max_len").getString());
//				prjMainSelectModUI.getComp_min_value().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_min_value").getString());
//				prjMainSelectModUI.getComp_max_value().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("comp_max_value").getString());
				prjMainSelectModUI.getIs_hide().setValue(Boolean.valueOf(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("is_inner").getString().equals("1")));
//				prjMainSelectModUI.getData_format().setValue(prjMainSelectModUI.getTree_data().getDataSet().fieldByName("data_format").getString());
				// ������ϸ����
				this.setTable_data_detail();
			}
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}
	}

	/**
	 * ���ڵ����¼���ֻ��˫�����в���
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void tree_dataClicked(MouseEvent e) {
		// �������˫�����˳�
		if ( e.getClickCount() != 2 ) {
			return;
		}
		// �������������˳�
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		// ���δѡ�����ڵ����ѡ����ڵ����˳�
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			return;
		}
		// �õ����
		String tmpString = null;
		try {
			tmpString = prjMainSelectModUI.getTree_data().getDataSet().fieldByName("chr_id").getString();
		} catch ( Exception e1 ) {
			e1.printStackTrace();
		}
		// ��Ϊ�޸�״̬
		if ( (tmpString != null) && !"".equals(tmpString) ) {
			prjMainSelectModUI.setModStatus(PrjMainIBS.MOD_STATUS);
		}
	}

	/**
	 * ����رհ�ť��ִ�йرղ���
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_closeAction(ActionEvent e) {
		prjMainSelectModUI.dispose();
	}

	/**
	 * �����Ӱ�ť����Ϊ���״̬
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_addAction(ActionEvent e) {
		// �����������
		this.clearUICompotents();
		//
		try {
			setCompEnameValue();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(Global.mainFrame, e1.getMessage(),"������Ϣ", MessageBox.WARNING);
			return;
		}
		// ��Ϊ���״̬
		prjMainSelectModUI.setModStatus(PrjMainIBS.ADD_STATUS);
	}

	/**
	 * ���ɾ����ť,����ɾ������
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_delAction(ActionEvent e) {
		//��������õ��ֶΣ�����ɾ��
		boolean is_inner = ((Boolean)prjMainSelectModUI.getIs_hide().getValue()).booleanValue();
		if(is_inner) {
			MessageBox.msgBox(prjMainSelectModUI, "�����ֶ��޷�ɾ��!", MessageBox.WARNING);
			return;
		}
		// ���δѡ�����ڵ����ѡ����ڵ�����ʾ
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "��ѡ��һ��������Ϣɾ��!", MessageBox.WARNING);
			return;
		}
		try {
			// ��ʾ�Ƿ�ɾ��
			MessageBox messageBox = new MessageBox("ȷ��Ҫɾ����ѡ���������Ϣ��", MessageBox.MESSAGE, MessageBox.BUTTON_OK
					| MessageBox.BUTTON_CANCEL);
			messageBox.setVisible(true);
			if ( messageBox.getResult() == MessageBox.CANCEL ) {
				return;
			}
			messageBox = null;
			// �����ϸ�鼮
			XMLData xmldata = new XMLData();
			xmldata.put("comp_id", prjMainSelectModUI.getComp_id().getValue());
			// �ж��ܷ�ɾ���������ݷ�����Ϣ��ʾ��Ϣ
			String dialogResult = prjMainSelectModUI.getPrjMainService().componentCanDelete(xmldata);
			if ( (dialogResult != null) && !dialogResult.equals("") ) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult, MessageBox.WARNING);
				return;
			}
			// ִ��ɾ�������������ݷ�����Ϣ��ʾ��Ϣ
			String returnValue = Integer.toString(prjMainSelectModUI.getPrjMainService().deleteComponent(xmldata));
			if ( returnValue.equals("-1") ) {
				MessageBox.msgBox(prjMainSelectModUI, "ɾ���ؼ�ʧ��!", MessageBox.ERROR);
				return;
			}
			MessageBox.msgBox(prjMainSelectModUI, "ɾ���ؼ��ɹ�!", MessageBox.MESSAGE);
			// ����Ϊ����״̬����������������
			prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
			prjMainSelectModUI.initTree();
		} catch ( Exception e1 ) {
		}
	}

	/**
	 * ����޸İ�ť��������Ϊ�޸�״̬
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_modAction(ActionEvent e) {
		// ���δѡ�����ڵ����ѡ����ڵ�����ʾ
		if ( (prjMainSelectModUI.getTree_data().getSelectionPath() == null)
				|| (prjMainSelectModUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "��ѡ��һ��������Ϣ�����޸�!", MessageBox.WARNING);
			return;
		}
		// ����Ϊ�޸�״̬
		prjMainSelectModUI.setModStatus(PrjMainIBS.MOD_STATUS);
	}

	/**
	 * ������水ť��ִ�б������
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_saveAction(ActionEvent e) {
		// �ж��Ƿ�д�ؼ����Ƶ�
		if (prjMainSelectModUI.getComp_name().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "����д�ؼ�����!",
					MessageBox.WARNING);
			return;
		}
		// �ж��Ƿ�д�ؼ��ֶ�����
		if (prjMainSelectModUI.getComp_ename().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "����д�ؼ��ֶ�����!",
					MessageBox.WARNING);
			return;
		}
		// �ж��Ƿ�ѡ��ؼ�����
		if (prjMainSelectModUI.getField_type().getValue().equals("")
				|| (prjMainSelectModUI.getComp_name().getValue() == null)) {
			MessageBox.msgBox(prjMainSelectModUI, "��ѡ��ؼ�����!",
					MessageBox.WARNING);
			return;
		}
		// ��װҳ������
		String returnValue = null;
		XMLData xmldata = new XMLData();
		xmldata.put("comp_id", prjMainSelectModUI.getComp_id().getValue());
		xmldata.put("comp_name", prjMainSelectModUI.getComp_name().getValue());
		xmldata
				.put("comp_ename", prjMainSelectModUI.getComp_ename()
						.getValue());
		xmldata
				.put("field_type", prjMainSelectModUI.getField_type()
						.getValue());
		// xmldata.put("is_comm", prjMainSelectModUI.getIs_comm().getValue());
		// xmldata.put("comp_min_len",
		// prjMainSelectModUI.getComp_min_len().getValue());
		// xmldata.put("comp_max_len",
		// prjMainSelectModUI.getComp_max_len().getValue());
		// xmldata.put("comp_min_value",
		// prjMainSelectModUI.getComp_min_value().getValue());
		// xmldata.put("comp_max_value",
		// prjMainSelectModUI.getComp_max_value().getValue());
		xmldata.put("is_inner", prjMainSelectModUI.getIs_hide().getValue());
		// xmldata.put("data_format",
		// prjMainSelectModUI.getData_format().getValue());
		// ��װ��ϸ����
		List tableData = null;
		// "4"��ָ������
		if ("4".equals(prjMainSelectModUI.getField_type().getValue())) {
			tableData = prjMainSelectModUI.getTable_data_detail().getData();
		}
		switch (prjMainSelectModUI.getModStatus()) {
		// �������Ӳ���
		case PrjMainIBS.ADD_STATUS: {
			// �ж��Ƿ�������ӣ������ݷ���ֵ��ʾ��Ϣ
			String dialogResult = prjMainSelectModUI.getPrjMainService()
					.componentCanInsert(xmldata);
			if ((dialogResult != null) && !dialogResult.equals("")) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult,
						MessageBox.WARNING);
				return;
			}
			// ������ݣ������ݷ���ֵ��ʾ�Ƿ�ɹ���Ϣ
			returnValue = ""
					+ prjMainSelectModUI.getPrjMainService().insertComponent(
							xmldata, tableData);
			if (returnValue.equals("-1")) {
				MessageBox.msgBox(prjMainSelectModUI, "��ӿؼ�ʧ��!",
						MessageBox.ERROR);
				return;
			} else {
				MessageBox.msgBox(prjMainSelectModUI, "��ӿؼ��ɹ�!",
						MessageBox.MESSAGE);
			}
		}
			break;
		// ������޸Ĳ���
		case PrjMainIBS.MOD_STATUS: {
			// �ж��Ƿ������޸ģ������ݷ���ֵ��ʾ��Ϣ
			String dialogResult = prjMainSelectModUI.getPrjMainService()
					.componentCanUpdate(xmldata);
			if ((dialogResult != null) && !dialogResult.equals("")) {
				MessageBox.msgBox(prjMainSelectModUI, dialogResult,
						MessageBox.WARNING);
				return;
			}
			// �޸����ݣ������ݷ���ֵ��ʾ�Ƿ�ɹ���Ϣ
			returnValue = ""
					+ prjMainSelectModUI.getPrjMainService().updateComponent(
							xmldata, tableData);
			if (returnValue.equals("-1")) {
				MessageBox.msgBox(prjMainSelectModUI, "�޸Ŀؼ�ʧ��!",
						MessageBox.ERROR);
				return;
			} else {
				MessageBox.msgBox(prjMainSelectModUI, "�޸Ŀؼ��ɹ�!",
						MessageBox.MESSAGE);
			}
		}
			break;
		default: {

		}
		}
		// ��ΪĬ��״̬�����»�ȡ��
		prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
		prjMainSelectModUI.initTree();
		tree_datavalueChanged(null);
	}

	/**
	 * ���ȡ����ť����ȡ�� ��ӻ������޸Ĳ���
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_cancelAction(ActionEvent e) {
		//ȡ������
		prjMainSelectModUI.setModStatus(PrjMainIBS.NORMAL_STATUS);
		tree_datavalueChanged(null);
	}

	/**
	 * ����ˢ������
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_qryAction(ActionEvent e) {
		// Object selectValue = prjMainSelectModUI.getTree_data().getValue();
		try {
			prjMainSelectModUI.initTree();
		} finally {
			// prjMainSelectModUI.getTree_data().setValue(selectValue);
		}
	}
	/**
	 * ��֤,�����Ƿ�Ϊ����
	 * @throws Exception
	 * @author GeXinying
	 */
	private void isNumberCheck() throws NumberFormatException {
		/*
		 * ���벻��Ϊ����,by GeXinying
		 */
		String new_code = prjMainSelectModUI.getItem_code().getValue().toString();
		long l_value=0;
		try {
			l_value = Long.parseLong(new_code);
		} catch (NumberFormatException e) {
			log.info("parse... "+l_value);
			throw new NumberFormatException("�Բ��𣬱���["+new_code+"]���������֣�");
		}
	}
	/**
	 * ������ϸ����
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_adddetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//�ж��Ƿ���д�˱���
		if ( (prjMainSelectModUI.getItem_code().getValue() == null)
				|| prjMainSelectModUI.getItem_code().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ���������д���룡", MessageBox.WARNING);
			return;
		}
		//�ж��Ƿ���д������
		if ( (prjMainSelectModUI.getItem_name().getValue() == null)
				|| prjMainSelectModUI.getItem_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ���������д���ƣ�", MessageBox.WARNING);
			return;
		}
		try {
			//���벻��Ϊ����,by GeXinying
			isNumberCheck();
		} catch (NumberFormatException e1) {
			MessageBox.msgBox(prjMainSelectModUI, e1.getMessage(), MessageBox.WARNING);
			return;
		}
		//��װ����
		XMLData xmldata = new XMLData();
		xmldata.put("item_code", prjMainSelectModUI.getItem_code().getValue());
		xmldata.put("item_name", prjMainSelectModUI.getItem_name().getValue());
		//�ж��Ƿ��������
		if ( this.HasItemcode(xmldata, null) ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ��𣬸ñ����Ѿ����ڣ�", MessageBox.WARNING);
			return;
		}

		//������ϸ����
		if ( iRow < 0 ) {
			prjMainSelectModUI.getTable_data_detail().addRow(xmldata);
		} else {
			prjMainSelectModUI.getTable_data_detail().addRow(xmldata, iRow + 1);
		}
	}

	/**
	 * �޸���ϸ����
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_moddetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//�ж��Ƿ�ѡ��������
		if ( iRow < 0 ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ�������ѡ����Ҫ�޸ĵ����ݣ�", MessageBox.WARNING);
			return;
		}
		//�ж��Ƿ���д�˱���
		if ( (prjMainSelectModUI.getItem_code().getValue() == null)
				|| prjMainSelectModUI.getItem_code().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ���������д���룡", MessageBox.WARNING);
			return;
		}
		//�ж��Ƿ���д������
		if ( (prjMainSelectModUI.getItem_name().getValue() == null)
				|| prjMainSelectModUI.getItem_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ���������д���ƣ�", MessageBox.WARNING);
			return;
		}
		/*
		 * ���벻��Ϊ����,by GeXinying
		 */
		try {
			//���벻��Ϊ����,by GeXinying
			isNumberCheck();
		} catch (NumberFormatException e1) {
			MessageBox.msgBox(prjMainSelectModUI, e1.getMessage(), MessageBox.WARNING);
			return;
		}
		//��װ����
		XMLData xmldata = new XMLData();
		xmldata.put("item_code", prjMainSelectModUI.getItem_code().getValue());
		xmldata.put("item_name", prjMainSelectModUI.getItem_name().getValue());
		//�ж��Ƿ��������
		if ( this.HasItemcode(xmldata, prjMainSelectModUI.getTable_data_detail().getCurrentRow()) ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ��𣬸ñ����Ѿ����ڣ�", MessageBox.WARNING);
			return;
		}
		//�޸���ϸ����
		prjMainSelectModUI.getTable_data_detail().deleteRow(iRow);
		prjMainSelectModUI.getTable_data_detail().addRow(xmldata, iRow);
	}

	/**
	 * ɾ����ϸ����
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_deldetailAction(ActionEvent e) {
		int iRow = prjMainSelectModUI.getTable_data_detail().getCurrentRowIndex();
		//�ж��Ƿ�ѡ��������
		if ( iRow < 0 ) {
			MessageBox.msgBox(prjMainSelectModUI, "�Բ�������ѡ����Ҫɾ�������ݣ�", MessageBox.WARNING);
			return;
		}
		//ɾ����ϸ����
		prjMainSelectModUI.getTable_data_detail().deleteRow(iRow);
	}

	/**
	 * ��ϸ����ѡ��ʱ�ؼ��ı仯
	 * @param e
	 *            �¼���װ����
	 */
	public void table_data_detailValueChanged(ListSelectionEvent e) {
		XMLData currentRow = prjMainSelectModUI.getTable_data_detail().getCurrentRow();
		if ( currentRow != null ) {
			prjMainSelectModUI.getItem_code().setValue(currentRow.get("item_code"));
			prjMainSelectModUI.getItem_name().setValue(currentRow.get("item_name"));
		}
	}
}
