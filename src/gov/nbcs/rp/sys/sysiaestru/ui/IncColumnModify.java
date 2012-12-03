/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ�޸Ĳ�����
 * </p>
 * <p>
 * Description:������Ŀ�޸Ĳ�����
 * </p>

 */
public class IncColumnModify {
	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	// ������ĿDataSet
	private DataSet dsIncCol = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public IncColumnModify(IncColumn incColumn) {
		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * �޸�������Ŀ��������
	 * 
	 * @return �����޸Ĳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean modify() throws HeadlessException, Exception {

		// �ж��ֶ�ֵ�Ƿ�����
		if (dsIncCol.fieldByName("end_flag").getValue() == null) {
			JOptionPane.showConfirmDialog(incColumn, "���ݲ�����!", "��ʾ",
					JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж��Ƿ���Ҷ�ڵ㣬����Ҷ�ڵ㣬ֻ���޸�����
		if (dsIncCol.fieldByName("end_flag").getInteger() != 1) {
			incColumn.sSaveType = "modname";
		} else { // �޸�Ҷ�ڵ�
			// �жϸýڵ��Ƿ�����Ԥ���ʹ��,��ʹ��ֻ���޸ĸ�ʽ
			String sEngName = dsIncCol.fieldByName("IncCol_Ename").getString();
			InfoPackage infoPackage = sysIaeStruServ.judgeIncColEnableModify(
					sEngName, Global.loginYear);

			// �ýڵ��ѱ�����Ԥ���ʹ��,ֻ���޸���ʾ��ʽ
			if (!infoPackage.getSuccess()) {
				incColumn.sSaveType = "modformate";
				JOptionPane.showMessageDialog(Global.mainFrame, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			} else { // �ڵ�δ��ʹ��
				incColumn.sSaveType = "mod";
			}
		}
		// ����������ĿDataSetΪ�༭״̬
		dsIncCol.edit();
		return true;
	}
}
