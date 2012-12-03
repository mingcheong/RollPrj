/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧���ʽ���Դ���Ӳ�����
 * </p>
 * <p>
 * Description:֧���ʽ���Դ���Ӳ�����
 * </p>

 */
public class PayOutFSAdd {
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
	public PayOutFSAdd(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.payOutFsObj = payOutFS.payOutFsObj;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * ����֧���ʽ���Դ��������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// ȡ��֧��Ԥ��������ٸ���
		int MaxFieldNum = sysIaeStruServ.getPayOutFsIValue(Integer
				.parseInt(Global.loginYear));
		// ȡ��֧��Ԥ���յ��ֶ�����
		String sEname = PubInterfaceStub.getMethod().assignNewCol("PFS_ENAME",
				"F", MaxFieldNum, "fb_iae_payout_fundsource", Global.loginYear,
				"");
		if ("".equals(sEname)) {
			// ����ʧ�ܣ���ʾ������Ϣ
			JOptionPane.showMessageDialog(payOutFS, "֧���ʽ���Դ��Ŀ�ѵ�������÷�Χ��", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// ����Զ����ɵı���
		String sPfsCode = PubInterfaceStub.getMethod().getMaxCode(
				"fb_iae_payout_fundsource", "PFS_CODE",
				"set_Year = " + Global.loginYear, ISysIaeStru.iCodeLen);
		// ����Զ����ɵĲ����
		String sLvlIdCode;
		if ("".equals(payOutFsObj.lvl_id)) {
			if (payOutFS.dsPayOutFS.isEmpty())
				sLvlIdCode = ISysIaeStru.ROOT_CODE;
			else
				sLvlIdCode = PubInterfaceStub.getMethod().getNodeID(
						"fb_iae_payout_fundsource",
						"LVL_ID",
						payOutFsObj.lvl_id,
						"set_Year = " + Global.loginYear
								+ " and par_id is null", payOutFS.lvlIdRule);
		} else {
			sLvlIdCode = PubInterfaceStub.getMethod().getNodeID(
					"fb_iae_payout_fundsource",
					"LVL_ID",
					payOutFsObj.lvl_id,
					"set_Year = " + Global.loginYear + " and par_id ='"
							+ payOutFsObj.lvl_id + "'", payOutFS.lvlIdRule);
		}
		// �жϱ����Ƿ��óɹ�
		if (sPfsCode == null) {
			JOptionPane.showMessageDialog(payOutFS, "�޷�������һ��δ��룬��ǰ����������󼶣�",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �жϱ��볤����д��"9999"
		int iLevel = payOutFS.lvlIdRule.levelOf(sLvlIdCode); // ��õ�ǰ����ڴ�
		if (iLevel < 0) {
			JOptionPane.showMessageDialog(payOutFS, "�޷����ӣ��ѵ�������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �ж����ӣ��ǲ������ӵ�һ��Ҷ���ӣ�������ӵ�һ��Ҷ�ڵ㣬�����ڵ����Ϣ�����ӽڵ�
		if (payOutFsObj.end_flag == 1)
			payOutFS.sSaveType = "addfirstson";
		else
			payOutFS.sSaveType = "add";

		payOutFS.dsPayOutFS.append();
		// �ж�����ѡ�еĽڵ��Ƿ���Ҷ�ڵ�,������Ҷ�ڵ㣬��������Ϣ������һ��Ҷ�ڵ�
		payOutFS.dsPayOutFS.maskDataChange(true);
		payOutFS.ftxtPriCode.setValue(sPfsCode);// ����
		payOutFS.ftxtfPfsCode.setValue(sLvlIdCode);// ��α���
		payOutFS.ftxtfPfsName.setValue("");// ����
		if ("addfirstson".equals(payOutFS.sSaveType)) {// ���ӵ�һ���ӽڵ�
			// Ӣ������
			payOutFS.dsPayOutFS.fieldByName("PFS_ENAME").setValue(
					payOutFsObj.pfs_ename);
		} else {// �����ӽڵ�
			// ����
			payOutFS.flstPfsKind.setSelectedIndex(0);
			// �������������
			((JCheckBox) payOutFS.fchkPfsFlag.getEditor()).setSelected(false);
			// Ĭ������
			((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(false);
			// ��֧ƽ��
			((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(true);
			// ֧����Ŀ���
			payOutFS.frdoSupPrj.setValue("0");
			// ������Դ����
			payOutFS.frdoIncColDts.setValue("0");
			// ��ʾ��ʽ
			payOutFS.fcbxSFormate.setValue("");
			// �༭��ʽ
			// payOutFS.fcbxEFormate.setValue("");
			// ���㹫ʽ
			payOutFS.ftxtaPfsFormula.setValue("");
			// �������ȼ�
			payOutFS.jspPfsCalcPRI.setValue(new Integer(0));
			// Ӣ������
			payOutFS.dsPayOutFS.fieldByName("PFS_ENAME").setValue(sEname);
		}
		payOutFS.dsPayOutFS.fieldByName("PFS_CODE").setValue(sPfsCode);
		payOutFS.dsPayOutFS.fieldByName("END_FLAG").setValue(new Integer(1));
		payOutFS.dsPayOutFS.fieldByName("set_Year").setValue(Global.loginYear);
		payOutFS.dsPayOutFS.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		payOutFS.dsPayOutFS.maskDataChange(false);
		payOutFS.ftxtfPfsName.setFocus();
		return true;
	}
}
