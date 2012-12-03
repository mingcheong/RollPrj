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
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailSelectUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailUI;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * <p>
 * Title:��Ŀ�걨�����������ӣ��޸ģ���Ԫ����
 * </p>
 * <p>
 * Description:��Ŀ�걨�����������ӣ��޸ģ���Ԫ�����Ӧ�ļ�����
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
public class PrjMain_DetailListener {

	/**
	 * �����棬���ڵ��ø����� 
	 */
	private PrjMain_DetailUI prjMainDetailUI;

	/**
	 * ��������UI���ݽ��������ڻ�ȡ�����ڵĸ�����
	 * 
	 * @param detailUI
	 *            ������UI����
	 */
	public PrjMain_DetailListener(PrjMain_DetailUI detailUI) {
		// ����ָ��ָ��ö���
		this.prjMainDetailUI = detailUI;
	}

	/**
	 * ѡ��ؼ�����
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_modAction(ActionEvent e) {
		// ����ѡ�����
		PrjMain_DetailSelectUI prjMainDetailSelectUI = new PrjMain_DetailSelectUI(prjMainDetailUI.getTable_detail().getData());
		// �õ�ѡ���������ݲ�չʾ
		List list = prjMainDetailSelectUI.getReturnData();
		prjMainDetailUI.getTable_detail().setData(list);
	}

	/**
	 * �����ؼ�λ��-��ǰ
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_upAction(ActionEvent e) {
		prjMainDetailUI.getTable_detail().moveUpCurrentRow();
	}

	/**
	 * �����ؼ�λ��-����
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_downAction(ActionEvent e) {
		prjMainDetailUI.getTable_detail().moveDownCurrentRow();
	}

	/**
	 * ȷ��-��������
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_okAction(ActionEvent e) {
		// �ж��Ƿ���д�����ƣ�����򵯳���ʾ
		if ( (prjMainDetailUI.getInput_set_name().getValue() == null)
				|| prjMainDetailUI.getInput_set_name().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "�Բ���������д���ƣ�", MessageBox.WARNING);
			return;
		}

		// �ж��Ƿ���д�˼�ƣ�����򵯳���ʾ
		if ( (prjMainDetailUI.getInput_set_shortname().getValue() == null)
				|| prjMainDetailUI.getInput_set_shortname().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "�Բ���������д��ƣ�", MessageBox.WARNING);
			return;
		}

		// �ж��Ƿ���д��ȫ�ƣ�����򵯳���ʾ
		if ( (prjMainDetailUI.getInput_set_longname().getValue() == null)
				|| prjMainDetailUI.getInput_set_longname().getValue().equals("") ) {
			MessageBox.msgBox(prjMainDetailUI, "�Բ���������дȫ�ƣ�", MessageBox.WARNING);
			return;
		}
		
		//�ж��Ƿ�����ϸ���ݣ�û����������
		List list = prjMainDetailUI.getTable_detail().getData();
		if ( (list == null) || (list.size() == 0) ) {
			MessageBox.msgBox(prjMainDetailUI, "�Բ�������ѡ����ϸ��", MessageBox.WARNING);
			return;
		}

		//���ݲ�ͬ�Ĳ������ͽ��в�ͬ�Ĳ���
		switch ( prjMainDetailUI.getOperationType() ) {
			//��Ӳ���
			case PrjMainIBS.OPERATION_ADD: {
				//��װ����
				XMLData xmlData = new XMLData();
				xmlData.put("input_set_id", "0");
				xmlData.put("input_set_name", prjMainDetailUI.getInput_set_name().getValue());
				xmlData.put("input_set_shortname", prjMainDetailUI.getInput_set_shortname().getValue());
				xmlData.put("input_set_longname", prjMainDetailUI.getInput_set_longname().getValue());
				xmlData.put("is_default", prjMainDetailUI.getIs_default().getValue());
				xmlData.put("detail_data", prjMainDetailUI.getTable_detail().getData());
				//��Ӳ���
				int returnValue = prjMainDetailUI.getPrjMainService().insertPrjSetting(xmlData);
				//���ݽ����ʾ��Ϣ
				switch ( returnValue ) {
					case -1: {
						MessageBox.msgBox(prjMainDetailUI, "��ӿؼ�ʧ��!", MessageBox.ERROR);
						return;
					}
					default: {
						MessageBox.msgBox(prjMainDetailUI, "��ӿؼ��ɹ�!", MessageBox.MESSAGE);
					}
				}
			}
				break;
			//�޸Ĳ���
			case PrjMainIBS.OPERATION_MOD: {
				//��װ����
				XMLData xmlData = new XMLData();
				xmlData.put("input_set_id", prjMainDetailUI.getInput_set_id().getValue());
				xmlData.put("input_set_name", prjMainDetailUI.getInput_set_name().getValue());
				xmlData.put("input_set_shortname", prjMainDetailUI.getInput_set_shortname().getValue());
				xmlData.put("input_set_longname", prjMainDetailUI.getInput_set_longname().getValue());
				xmlData.put("is_default", prjMainDetailUI.getIs_default().getValue());
				xmlData.put("detail_data", prjMainDetailUI.getTable_detail().getData());
				//�޸Ĳ���
				int returnValue = prjMainDetailUI.getPrjMainService().updatePrjSetting(xmlData);
				//���ݽ����ʾ��Ϣ
				switch ( returnValue ) {
					case -1: {
						MessageBox.msgBox(prjMainDetailUI, "�޸Ŀؼ�ʧ��!", MessageBox.ERROR);
						return;
					}
					default: {
						MessageBox.msgBox(prjMainDetailUI, "�޸Ŀؼ��ɹ�!", MessageBox.MESSAGE);
					}
				}
			}
				break;
		}
		//�رս���
		prjMainDetailUI.dispose();
	}

	/**
	 * ȡ��-�˳�����
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_cancelAction(ActionEvent e) {
//		�رս���
		prjMainDetailUI.dispose();
	}

	/**
	 * �޸�����ʱ��ȫ�ƣ����ͬʱ�޸�
	 * 
	 * @param arg0
	 */
	public void input_set_nameValueChanged(ValueChangeEvent arg0) {
		//ͬ�����ü��
		prjMainDetailUI.getInput_set_shortname().setValue(prjMainDetailUI.getInput_set_name().getValue());
		//ͬ������ȫ��
		prjMainDetailUI.getInput_set_longname().setValue(prjMainDetailUI.getInput_set_name().getValue());
	}
}
