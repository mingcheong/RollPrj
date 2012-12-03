/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧����Ŀ���ɾ��������
 * </p>
 * <p>
 * Description:֧����Ŀ���ɾ��������
 * </p>
 * <p>

 */
public class PayOutTypeDel {
	// ֧����Ŀ������ͻ�����������
	private PayOutType payOutType = null;

	// ֧����Ŀ������
	private PayOutTypeObj payOutTypeObj = null;

	// ֧����Ŀ������ݼ�
	private DataSet dsPayOutType = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutType
	 *            ֧����Ŀ������ͻ�����������
	 */
	public PayOutTypeDel(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.dsPayOutType = payOutType.dsPayOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * ɾ��֧����Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {
		String sParCode = ""; // ��Ÿ��ڵ���
		String sParName = ""; // ��Ÿ��ڵ�����
		String sParParID = "";// ��Ÿ��ڵ�ParID
		// �ж��Ƿ���ĩ�ڵ㣬����ĩ�ڵ㣬������ɾ��
		if (dsPayOutType.fieldByName("End_flag").getValue() != null
				&& dsPayOutType.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(payOutType, "֧����Ŀ����������Ŀ,����ɾ������Ŀ!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж��Ƿ�ʹ��
		InfoPackage infoPackage = sysIaeStruServ.judgePayOutTypeUse(
				payOutTypeObj.payout_kind_code, Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutType, infoPackage.getsMessage()
					+ ",����ɾ��!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж��Ƿ������ù�ʽ�͹�ʽ�뵥λ��Ӧ��ϵ
		infoPackage = sysIaeStruServ
				.judgePayoutTypeFormulaUse(payOutTypeObj.payout_kind_code);
		if (!infoPackage.getSuccess()) {
			if (JOptionPane.showConfirmDialog(payOutType, infoPackage
					.getsMessage()
					+ ",ɾ��������¼��ͬʱɾ��"
					+ infoPackage.getsMessage()
					+ "���޷��ָ�!\n���Ƿ�ȷ��Ҫɾ��������¼?", "��ʾ", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return false;
		} else {
			// ��ʾ�Ƿ�ȷ��ɾ��
			if (JOptionPane.showConfirmDialog(payOutType, "���Ƿ�ȷ��Ҫɾ��������¼?",
					"��ʾ", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return false;
		}
		// �ж�ѡ�еĽڵ�ĸ��ڵ��Ƿ����
		MyTreeNode node = payOutType.ftreePayOutType
				.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		if (parentNode != (MyTreeNode) payOutType.ftreePayOutType.getRoot()) {
			// �ж��м����ֵܣ����ֻ��һ���ڵ㣬�����ڵĲ�����Ϣ�������ڵ�,��һЩ���Ĳ���
			if (parentNode.getChildCount() == 1) {
				// ��λ�����ڵ�
				dsPayOutType.gotoBookmark(parentNode.getBookmark());
				sParCode = dsPayOutType.fieldByName(
						IPayOutType.PAYOUT_KIND_CODE).getString();
				sParName = dsPayOutType.fieldByName(
						IPayOutType.PAYOUT_KIND_NAME).getString();
				// ��������Ϣ
				dsPayOutType.maskDataChange(true);
				dsPayOutType.edit();
				dsPayOutType.fieldByName("std_type_code").setValue(
						payOutTypeObj.std_type_code);
				dsPayOutType.fieldByName("end_flag").setValue(
						new Integer(payOutTypeObj.end_flag));
				dsPayOutType.maskDataChange(false);
				if (parentNode.getParent() != payOutType.ftreePayOutType
						.getRoot()) {
					dsPayOutType.gotoBookmark(((MyTreeNode) parentNode
							.getParent()).getBookmark());
					sParParID = dsPayOutType.fieldByName(
							IPayOutType.PAYOUT_KIND_CODE).getString();
				} else
					sParParID = "";
				// ��λ�ر��ڵ�
				dsPayOutType.gotoBookmark(node.getBookmark());

			}
		}
		// ɾ�����ڵ�
		dsPayOutType.delete();
		// �ύ���ݿ�
		sysIaeStruServ.delPayOutKind(dsPayOutType,
				payOutTypeObj.payout_kind_code, sParCode, sParName, sParParID,
				String.valueOf(payOutTypeObj.set_year),
				payOutTypeObj.payout_kind_name);
		dsPayOutType.applyUpdate();
		payOutType.setChangeFlag(true);

		return true;

	}
}
