/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * <p>
 * Title:������Ŀ����޸Ĳ�����
 * </p>
 * <p>
 * Description:������Ŀ����޸Ĳ�����
 * </p>

 */
public class IncTypeModify {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	// ������Ŀ������
	private IncTypeObj incTypeObj = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param incType
	 *            ������Ŀ������ͻ�����������
	 */
	public IncTypeModify(IncType incType) {
		this.incType = incType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * �޸�������Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void modify() throws HeadlessException, Exception {
		// �ж��Ƿ���Ҷ�ڵ�
		if (incTypeObj.end_flag != 1) {
			incType.sSaveType = "modname";
		} else {
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableModify(
					incTypeObj.inctype_code, String
							.valueOf(incTypeObj.set_year));
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				incType.sSaveType = "modformate";
			} else {
				incType.sSaveType = "mod";
			}
		}
		incType.dsIncType.edit();
	}

}
