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
 * Title:֧���ʽ���Դ�޸Ĳ�����
 * </p>
 * <p>
 * Description:֧���ʽ���Դ�޸Ĳ�����
 * </p>

 */
public class PayOutFSModify {
	// ֧���ʽ���Դ����ͻ�����������
	private PayOutFS payOutFS = null;

	// ֧���ʽ���Դ����
	private PayOutFsObj payOutFsObj = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutFS
	 *            ֧���ʽ���Դ����ͻ�����������
	 */
	public PayOutFSModify(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.payOutFsObj = payOutFS.payOutFsObj;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * �޸�֧���ʽ���Դ��������
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public void modify() throws HeadlessException, Exception {
		// �ж��Ƿ���Ҷ�ڵ�
		if (payOutFsObj.end_flag != 1) {
			payOutFS.sSaveType = "modname";
		} else {
			InfoPackage infoPackage = sysIaeStruServ.chkFundSourceByRef(String
					.valueOf(payOutFsObj.set_year), payOutFsObj.PFS_CODE,
					payOutFsObj.pfs_ename);
			int iReUse = Integer.parseInt(infoPackage.getObject().toString());
			if (iReUse != 0) {
				payOutFS.sSaveType = "modformate";
				JOptionPane.showMessageDialog(payOutFS, "���ʽ���Դ\n"
						+ infoPackage.getsMessage() + "ֻ���޸���ʾ��ʽ!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				payOutFS.sSaveType = "mod";
			}
		}
		payOutFS.dsPayOutFS.edit();
	}

}
