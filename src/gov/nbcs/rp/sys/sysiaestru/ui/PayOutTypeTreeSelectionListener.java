/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧����Ŀ�����SelectionListerner
 * </p>
 * <p>
 * Description:֧����Ŀ�����SelectionListerner
 * </p>
 * <p>
 
 * @version 6.2.40
 */
public class PayOutTypeTreeSelectionListener implements TreeSelectionListener {
	// ֧����Ŀ������ͻ�����������
	private PayOutType payOutType = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	public PayOutTypeTreeSelectionListener(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(
					payOutType.dsPayOutType, payOutType.getToolbarPanel(),
					payOutType.ftreePayOutType);
			setActionStatus.setState(true, true);
			if (payOutType.dsPayOutType.isEmpty()
					|| payOutType.dsPayOutType.bof()
					|| payOutType.dsPayOutType.eof())
				return;
			// ����
			if (payOutType.dsPayOutType.fieldByName("PAYOUT_KIND_CODE")
					.getValue() != null)
				payOutType.ftxtPriCode.setValue(payOutType.dsPayOutType
						.fieldByName("PAYOUT_KIND_CODE").getString());
			else
				payOutType.ftxtPriCode.setValue("");
			// ����
			if (payOutType.dsPayOutType.fieldByName("LVL_ID").getString() != "")
				payOutType.ftxtfPayOutTypeCode.setValue(payOutType.dsPayOutType
						.fieldByName("LVL_ID").getString());
			// ����
			payOutType.ftxtfPayOutTypeName.setValue(payOutType.dsPayOutType
					.fieldByName("PAYOUT_KIND_NAME").getString());

			// ����
			payOutType.flstPayOutTypeKind.setMaskValueChange(true);
			if ("".equals(payOutType.dsPayOutType.fieldByName(
					IPayOutType.STD_TYPE_CODE).getString())) {
				payOutType.flstPayOutTypeKind.setSelectedIndex(0);
			} else
				payOutType.flstPayOutTypeKind.setSelectedValue(
						payOutType.dsPayOutType.fieldByName(
								IPayOutType.STD_TYPE_CODE).getString(), true);
			payOutType.flstPayOutTypeKind.setMaskValueChange(false);
			// ���������䵽��ϸ
			if (payOutType.dsPayOutType.fieldByName("N1").getValue() != null) {
				((JCheckBox) payOutType.fchkPayOutFlag.getEditor())
						.setSelected(Common.estimate(new Integer(
								payOutType.dsPayOutType.fieldByName("N1")
										.getInteger())));
			} else {
				((JCheckBox) payOutType.fchkPayOutFlag.getEditor())
						.setSelected(false);
			}

			// ���ÿ�Ŀ
			String[] sID = sysIaeStruServ.getJjWithPayoutKindCode(payOutType.dsPayOutType
					.fieldByName("payout_kind_code").getString(),
					Global.loginYear);
			SetSelectTree.setIsCheck(payOutType.ftreeAcctJJ, sID);

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(payOutType, "֧����Ŀ�����ʾ��ϸ��Ϣ�������󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
