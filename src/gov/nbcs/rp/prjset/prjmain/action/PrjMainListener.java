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
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMainUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;

/**
 * <p>
 * Title:��Ŀ�걨�������õ�Ԫ����
 * </p>
 * <p>
 * Description:��Ŀ�걨�������õ�Ԫ�����Ӧ�ļ�����
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
public class PrjMainListener {

	/** �����棬���ڵ��ø����� */
	private PrjMainUI prjMainUI = null;

	/**
	 * �����������ʾ����
	 */
	private void clearUICompotents() {
		// �����������
		prjMainUI.getInput_set_id().setValue("");
		prjMainUI.getInput_set_name().setValue("");
		prjMainUI.getInput_set_shortname().setValue("");
		prjMainUI.getInput_set_longname().setValue("");
		prjMainUI.getIs_default().setValue("");
		// �����ϸ���ݣ���ص�Table��
		prjMainUI.getTable_detail().deleteAllRows();
	}

	/**
	 * ������ϸ������
	 */
	private void setTable_data_detail() {
		// �����ϸ������
		prjMainUI.getTable_detail().deleteAllRows();
		// �ж�һ��ѡ������ڵ��ǲ���Ϊ�գ������Ǹ��ڵ�
		if ( prjMainUI.getTree_data() != null ) {
			if ( prjMainUI.getTree_data().getSelectionPath() != null ) {
				if ( prjMainUI.getTree_data().getSelectionPath().getParentPath() != null ) {
					//��ǰ���ñ�ţ�������
					String input_set_id;
					try {
						input_set_id = prjMainUI.getTree_data().getDataSet().fieldByName("input_set_id").getString();
						//��ȡ��Ӧ�Ĳ�ѯ���s
						List list = prjMainUI.getPrjMainService().getSetDetailValues(input_set_id);
						prjMainUI.getTable_detail().setData(list);
					} catch ( Exception e ) {
						Log.error(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * ��������UI���ݽ��������ڻ�ȡ�����ڵĸ�����
	 * @param mainUI ������UI����
	 */
	public PrjMainListener(PrjMainUI mainUI) {
		//����ָ��ָ��ö���
		this.prjMainUI = mainUI;
	}

	/**
	 * "���"��ť����Ӧ�Ĺ���
	 * @param e �¼���װ����
	 */
	public void btn_addAction(ActionEvent e) {
		//����ά������
		new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_ADD);
		//����ˢ��
		btn_qryAction(e);
	}
	
	/**
	 * "�ر�"��ť��Ӧ�Ĺ���
	 * @param e
	 * yangxuefeng add 20080428
	 */
	public void btn_closeAction(ActionEvent e){
		((FFrame)Global.mainFrame).closeMenu();
	}

	/**
	 * "ɾ��"��ť����Ӧ�Ĺ���
	 * @param e �¼���װ����
	 */
	public void btn_delAction(ActionEvent e) {
		//�ж�һ���Ƿ�ѡ������Ӧ�����ݣ���δѡ��������ʾ
		if ( (prjMainUI.getTree_data().getSelectionPath() == null)
				|| (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainUI, "��ѡ��һ��������Ϣɾ��!", MessageBox.WARNING);
			return;
		}
		try {
			//��ʾ�Ƿ�ɾ��
			MessageBox messageBox = new MessageBox("ȷ��Ҫɾ����ѡ���������Ϣ��", MessageBox.MESSAGE, MessageBox.BUTTON_OK
					| MessageBox.BUTTON_CANCEL);
			messageBox.setVisible(true);
			if ( messageBox.getResult() == MessageBox.CANCEL ) {
				return;
			}
			messageBox = null;
			//�ж�һ���Ƿ����ɾ��ѡ�����Ϣ���������������ʾ
			String dialogResult = prjMainUI.getPrjMainService().canDeleteInputSet(prjMainUI.getInput_set_id().getValue().toString());
			if ( (dialogResult != null) && !dialogResult.equals("") ) {
				MessageBox.msgBox(prjMainUI, dialogResult, MessageBox.WARNING);
				return;
			}
			//����ɾ�����������Ҹ��ݷ��ص���Ϣ�ж��Ƿ�ɾ���ɹ�
			String returnValue = ""
					+ prjMainUI.getPrjMainService().deleteInputSet(prjMainUI.getInput_set_id().getValue().toString());
			if ( returnValue.equals("-1") ) {
				MessageBox.msgBox(prjMainUI, "ɾ��������Ϣʧ��!", MessageBox.ERROR);
				return;
			}
			MessageBox.msgBox(prjMainUI, "ɾ��������Ϣ�ɹ�!", MessageBox.MESSAGE);
			//����ˢ��ҳ��
			btn_qryAction(e);
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}

	}

	/**
	 * "�޸�"��ť����Ӧ�Ĺ���
	 * @param e �¼���װ����
	 */
	public void btn_modAction(ActionEvent e) {
		//�ж�һ���Ƿ�ѡ������Ӧ�����ݣ���δѡ��������ʾ
		if ( (prjMainUI.getTree_data().getSelectionPath() == null)
				|| (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
			MessageBox.msgBox(prjMainUI, "��ѡ��һ��������Ϣ�����޸�!", MessageBox.WARNING);
			return;
		}
		//�����༭ҳ��
		new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_MOD);
		//����ˢ��ҳ��
		btn_qryAction(e);
	}

	/**
	 * "�ؼ�����"��ť����Ӧ�Ĺ���
	 * @param e �¼���װ����
	 */
	public void btn_selectmodAction(ActionEvent e) {
		//������ϸ���ý���
		new PrjMain_SelectModUI();
	}

	/**
	 * "ˢ��"��ť����Ӧ�Ĺ���
	 * @param e �¼���װ����
	 */
	public void btn_qryAction(ActionEvent e) {
		//���³�ʼ�����ṹ����
		prjMainUI.initTree();
		//����ؼ���ֵ
		clearUICompotents();
	}

	/**
	 * ���ڵ�˫������
	 * @param e �¼���װ����
	 */
	public void tree_dataClicked(MouseEvent e) {
		//�ж��Ƿ���е���˫��������˫�����ټ���ִ��
		if ( e.getClickCount() != 2 ) {
			return;
		}
		//�������������˳�
		if ( e.getButton() != MouseEvent.BUTTON1 ) {
			return;
		}
		//�ж�һ���Ƿ�ѡ������Ӧ������
		if ( (prjMainUI.getTree_data().getSelectionPath() != null)
				&& (prjMainUI.getTree_data().getSelectionPath().getParentPath() != null) ) {
			//�����༭ҳ��
			new PrjMain_DetailUI(prjMainUI.getTree_data().getDataSet(), PrjMainIBS.OPERATION_MOD);
			//����ˢ��ҳ��
			btn_qryAction(null);
		}
	}

	/**
	 * ѡ�����ڵ�ʱ��Ҫˢ���������
	 * @param e �¼���װ����
	 */
	public void tree_datavalueChanged(TreeSelectionEvent e) {
		try {
			//���δѡ��ڵ����ѡ����ڵ����������
			if ( (prjMainUI.getTree_data().getSelectionPath() == null) || (prjMainUI.getTree_data().getSelectionPath().getParentPath() == null) ) {
				clearUICompotents();
			} else {
				//ѡ��һ��ڵ㣬��ʾ����
				//������������
				prjMainUI.getInput_set_id().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_id").getString());
				prjMainUI.getInput_set_name().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_name").getString());
				prjMainUI.getInput_set_shortname().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_shortname").getString());
				prjMainUI.getInput_set_longname().setValue(prjMainUI.getTree_data().getDataSet().fieldByName("input_set_longname").getString());
				prjMainUI.getIs_default().setValue(Boolean.valueOf(prjMainUI.getTree_data().getDataSet().fieldByName("is_default").getString().equals("1")));
				//������ϸ������
				this.setTable_data_detail();
			}
		} catch ( Exception e1 ) {
			Log.error(e1.getMessage());
		}
	}
}
