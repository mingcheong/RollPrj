/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧���ʽ���Դ��SelectionListerner
 * </p>
 * <p>
 * Description:֧���ʽ���Դ��SelectionListerner
 * </p>

 */

public class PayOutFSTreeSelectionListener implements TreeSelectionListener {
	// ֧���ʽ���Դ����ͻ�����������
	private PayOutFS payOutFS = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutFS
	 *            ֧���ʽ���Դ����ͻ�����������
	 */
	public PayOutFSTreeSelectionListener(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * ���÷���
	 */
	public void valueChanged(TreeSelectionEvent e) {
		try {
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(
					payOutFS.dsPayOutFS, payOutFS, payOutFS.ftreePayOutFS);
			setActionStatus.setState(true, true);
			if (payOutFS.dsPayOutFS.isEmpty() || payOutFS.dsPayOutFS.bof()
					|| payOutFS.dsPayOutFS.eof())
				return;
			// ����
			if (payOutFS.dsPayOutFS.fieldByName("Pfs_code").getValue() != null)
				payOutFS.ftxtPriCode.setValue(payOutFS.dsPayOutFS.fieldByName(
						"Pfs_code").getString());
			else
				payOutFS.ftxtPriCode.setValue("");
			// ����
			if (payOutFS.dsPayOutFS.fieldByName("LVL_ID").getValue() != null)
				payOutFS.ftxtfPfsCode.setValue(payOutFS.dsPayOutFS.fieldByName(
						"LVL_ID").getString());
			else
				payOutFS.ftxtfPfsCode.setValue("");
			// ����
			payOutFS.ftxtfPfsName.setValue(payOutFS.dsPayOutFS.fieldByName(
					"PFS_NAME").getString());
			// ����
			payOutFS.flstPfsKind.setMaskValueChange(true);
			if ("".equals(payOutFS.dsPayOutFS.fieldByName("STD_TYPE_CODE")
					.getString())) {
				payOutFS.flstPfsKind.setSelectedIndex(0);
			} else
				payOutFS.flstPfsKind.setSelectedValue(payOutFS.dsPayOutFS
						.fieldByName("STD_TYPE_CODE").getString(), true);
			payOutFS.flstPfsKind.setMaskValueChange(false);
			// �������������
			if (payOutFS.dsPayOutFS.fieldByName("CF_PFS_FLAG").getValue() != null) {
				((JCheckBox) payOutFS.fchkPfsFlag.getEditor())
						.setSelected(Common.estimate(new Integer(
								payOutFS.dsPayOutFS.fieldByName("CF_PFS_FLAG")
										.getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkPfsFlag.getEditor())
						.setSelected(false);
			}
			// Ĭ������
			if (payOutFS.dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE)
					.getValue() != null) {
				((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(Common
						.estimate(new Integer(payOutFS.dsPayOutFS.fieldByName(
								IPayOutFS.IN_COMMON_USE).getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkHide.getEditor()).setSelected(false);
			}
			// ֧��ƽ�� mod by Cl 09-09-04
			if (payOutFS.dsPayOutFS.fieldByName(IPayOutFS.IS_BALANCE)
					.getValue() != null) {
				((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(Common
						.estimate(new Integer(payOutFS.dsPayOutFS.fieldByName(
								IPayOutFS.IS_BALANCE).getInteger())));
			} else {
				((JCheckBox) payOutFS.fchkIsBalance.getEditor()).setSelected(false);
			}
			// ֧����Ŀ���
			if (payOutFS.dsPayOutFS.fieldByName("SUP_PRJ").getValue() != null) {
				payOutFS.frdoSupPrj.setValue(payOutFS.dsPayOutFS.fieldByName(
						"SUP_PRJ").getString());
			} else {
				payOutFS.frdoSupPrj.setValue("0");
			}
			// ������Դ
			if (payOutFS.dsPayOutFS.fieldByName("DATA_SOURCE").getValue() != null) {
				payOutFS.frdoIncColDts.setValue(payOutFS.dsPayOutFS
						.fieldByName("DATA_SOURCE").getString());
			} else {
				payOutFS.frdoIncColDts.setValue("0");
			}
			// ��ʾ��ʽ
			payOutFS.fcbxSFormate.setValue(payOutFS.dsPayOutFS.fieldByName(
					"DISPLAY_FORMAT").getString());
			// ���㹫ʽ
			if (payOutFS.dsPayOutFS.fieldByName("FORMULA_DET").getString() != "") {
				payOutFS.ftxtaPfsFormula.setValue(PubInterfaceStub.getMethod()
						.replaceTextEx(
								payOutFS.dsPayOutFS.fieldByName("FORMULA_DET")
										.getString(), 0,
								"fb_iae_payout_fundsource", "PFS_ENAME",
								"PFS_FNAME", "set_year =" + Global.loginYear));
			} else {
				payOutFS.ftxtaPfsFormula.setValue("");
			}
			// �������ȼ�
			if (payOutFS.dsPayOutFS.fieldByName("CALC_PRI").getValue() != null) {
				payOutFS.jspPfsCalcPRI.setValue(new Integer(payOutFS.dsPayOutFS
						.fieldByName("CALC_PRI").getInteger()));
			}
			payOutFS.ftxtfPfsCode.setFocus();

			// ����������Դ����ʾ��Ϣ
			if (payOutFS.dsPayOutFS.fieldByName("DATA_SOURCE").getInteger() == 0) {// ¼��
				// �õ�������Ŀ��Ӧ�շ���Ŀ��Ϣ
				DataSet dsPfsToAcct = sysIaeStruServ.getPfsToAcct(
						Global.loginYear, payOutFS.dsPayOutFS.fieldByName(
								"PFS_CODE").getValue().toString());
				// ����֧���ʽ���Դ�빦�ܿ�Ŀ��Ӧ��ϵ���ù��ܿ�Ŀ�ڵ�ѡ��״̬
				SetSelectTree.setIsCheck(payOutFS.fpnlAcct.getFtreAcct(),
						dsPfsToAcct, "acct_code");
			} else {
				SetSelectTree.setIsNoCheck(payOutFS.fpnlAcct.getFtreAcct());
			}
			// ��ѯ��������
			payOutFS.ftxtReportPfsName.setValue(payOutFS.dsPayOutFS
					.fieldByName("C1").getString());

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(payOutFS, "��ʾ֧���ʽ���Դ��ϸ��Ϣ�������󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
