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

import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_DetailSelectUI;
import gov.nbcs.rp.prjset.prjmain.ui.PrjMain_SelectModUI;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:�ؼ�ѡ��Ԫ����
 * </p>
 * <p>
 * Description:�ؼ�ѡ��Ԫ��Ӧ�ļ�����
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
public class PrjMain_DetailSelectListener {
	//
	private static Logger logger  = Logger.getLogger(PrjMain_DetailSelectListener.class);

	private boolean can_value_change = false;
	/**
	 * �����棬���ڵ��ø�����
	 */
	private PrjMain_DetailSelectUI prjMainDetailSelectUI = null;

	/**
	 * ��������UI���ݽ��������ڻ�ȡ�����ڵĸ�����
	 * 
	 * @param selectUI
	 *            ������UI����
	 */
	public PrjMain_DetailSelectListener(PrjMain_DetailSelectUI selectUI) {
		// ����ָ��ָ��ö���
		this.prjMainDetailSelectUI = selectUI;
	}

	/**
	 * ����������ý���
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_setAction(ActionEvent e) {
		// �������ý���
		new PrjMain_SelectModUI();
		//-----------------------------
		//Added by GeXinying
		// �˳�����Ҫ���³�ʼ������
		// ���ȵõ��Ѿ�ѡ�е���
		List selected = prjMainDetailSelectUI.getTable_top().getSelectedDataByCheckBox();
		//
		this.prjMainDetailSelectUI.initTableData();
		// Added by GeXinying
		// ����ѡ�е���������ѡ��
		// ���û��list�е�ֵ������
		Iterator iter_b = selected.iterator();
		Iterator iter_a = null;
		XMLData data_b = null;
		XMLData data_a = null;
		int index = 0;
		/**
		 * �ȱ��� ��ʼ�Ѿ�ѡ�е�������Ϣ
		 * ���ű��� Ŀǰ���е�����
		 * ��Ϊcomp_id�ǲ������ظ���,���Ը���comp_id����λ��ǰ���е���
		 * Ȼ����ݴ˽� ��ѡ�е����ݵ���Ϣ ����д����ǰ��֮comp_idһ����������
		 */
		while(iter_b.hasNext()) {
			index = 0;
			data_b = (XMLData)iter_b.next();
			iter_a = prjMainDetailSelectUI.getTable_top().getData().iterator();			
			while(iter_a.hasNext()) {
				data_a = (XMLData)iter_a.next();
				if(data_b.get("comp_id").equals(data_a.get("comp_id"))) {
					prjMainDetailSelectUI.getTable_top().setCheckBoxSelectedAtRow(index,true);
					data_a.put("is_must",data_b.get("is_must"));
					data_a.put("is_must_name",data_b.get("is_must_name"));
					prjMainDetailSelectUI.getTable_top().getData().set(index,data_b);
					logger.debug("row ---"+index);
					break;
				}
				index++;
			}
		}
		
	}

	/**
	 * ȷ��ѡ������
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_okAction(ActionEvent e) {
		// �õ���ѡ������
		prjMainDetailSelectUI.selectReturnData();
		// �ر�
		prjMainDetailSelectUI.dispose();
	}

	/**
	 * ȡ��ѡ������
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void btn_cancelAction(ActionEvent e) {
		// �ر�
		prjMainDetailSelectUI.dispose();

	}

	/**
	 * ˢ����ϸ����
	 * 
	 * @param e
	 *            �¼���װ����
	 */
	public void table_topValueChanged(ListSelectionEvent e) {
		// ������ϸ����
		prjMainDetailSelectUI.setDetailTableData();
		// ���ö�Ӧ��CheckBoxֵ���Ƿ���룩
		XMLData xmlData = prjMainDetailSelectUI.getTable_top().getCurrentRow();
		if ( xmlData != null ) {
			Object is_must = xmlData.get("is_must");
			if ( "1".equals(is_must) ) {
				// �����"������д"�ģ���
				can_value_change = false;
				prjMainDetailSelectUI.getCbx_is_must().setValue(Boolean.valueOf(true));
				can_value_change = true;
			} else {
				// �������"������д"�ģ�ȡ����
				can_value_change = false;
				prjMainDetailSelectUI.getCbx_is_must().setValue(Boolean.valueOf(false));
				can_value_change = true;
			}
		}
	}


	public void cbx_is_mustValueChanged(ValueChangeEvent arg0) {
		if (!can_value_change) {return;}
		XMLData xmlData = prjMainDetailSelectUI.getTable_top().getCurrentRow();
		if ( xmlData != null ) {
			Object is_must = prjMainDetailSelectUI.getCbx_is_must().getValue();
			if ( Boolean.valueOf(is_must.toString()).booleanValue() ) {
				// ����򹳣���"������д"��
				xmlData.put("is_must","1");
				xmlData.put("is_must_name","����");
				prjMainDetailSelectUI.getTable_top().getData().set(prjMainDetailSelectUI.getTable_top().getCurrentRowIndex(),xmlData);
				prjMainDetailSelectUI.getTable_top().repaint();
			} else {
				// ���ȡ���򹳣�����"������д"��
				xmlData.put("is_must","0");
				xmlData.put("is_must_name","�Ǳ���");
				prjMainDetailSelectUI.getTable_top().getData().set(prjMainDetailSelectUI.getTable_top().getCurrentRowIndex(),xmlData);
				prjMainDetailSelectUI.getTable_top().repaint();
			}
		}
	}
}
