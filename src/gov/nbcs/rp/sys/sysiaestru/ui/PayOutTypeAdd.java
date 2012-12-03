/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧����Ŀ������Ӳ�����
 * </p>
 * <p>
 * Description:֧����Ŀ������Ӳ�����
 * </p>
 * <p>

 */
public class PayOutTypeAdd {
	// ֧����Ŀ������ͻ�����������
	private PayOutType payOutType = null;

	// ֧����Ŀ������
	private PayOutTypeObj payOutTypeObj = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	// �����������ݽӿ�
	private IPubInterface iPubInterface = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutType
	 *            ֧����Ŀ������ͻ�����������
	 */
	public PayOutTypeAdd(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.iPubInterface = payOutType.iPubInterface;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * ����֧����Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// �ж����ӣ��ǲ������ӵ�һ��Ҷ���ӣ�������ӵ�һ��Ҷ�ڵ㣬�����ڵ�Ĳ�����Ϣ�����ӽڵ�
		if (payOutTypeObj.end_flag == 1)
			payOutType.sSaveType = "addfirstson";
		else
			payOutType.sSaveType = "add";

		// �ж��ڽڵ������ӵ�һ���ӽڵ㣬����ڵ��ѱ�ʹ�ã����������ӽڵ�
		if ("addfirstson".equals(payOutType.sSaveType)) {
			InfoPackage infoPackage = sysIaeStruServ.judgePayOutTypeUse(
					payOutTypeObj.payout_kind_code, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(payOutType, infoPackage
						.getsMessage()
						+ ",���������ӽڵ�!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		// ����Զ����ɵı���
		String sPayOutCode = iPubInterface.getMaxCode("fb_iae_payout_kind",
				"PAYOUT_KIND_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);

		// �жϱ����Ƿ��óɹ�
		if (sPayOutCode == null) {
			JOptionPane.showMessageDialog(payOutType, "��ñ�����������ʧ�ܣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// ����Զ����ɵĲ����
		String sLvlIdCode;
		if ("".equals(payOutTypeObj.lvl_id)) {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_payout_kind",
					"LVL_ID", payOutTypeObj.lvl_id, "set_Year = "
							+ Global.loginYear + " and par_id is null",
					payOutType.lvlIdRule);
		} else {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_payout_kind",
					"LVL_ID", payOutTypeObj.lvl_id, "set_Year = "
							+ Global.loginYear + " and par_id ='"
							+ payOutTypeObj.lvl_id + "'", payOutType.lvlIdRule);
		}
		// �жϱ����Ƿ��óɹ�
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(payOutType, "�޷�������һ��δ��룬�ѵ��������Ӽ��Σ�",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		payOutType.dsPayOutType.append();
		// �ж�����ѡ�еĽڵ��Ƿ���Ҷ�ڵ�,������Ҷ�ڵ㣬��������Ϣ������һ��Ҷ�ڵ�
		payOutType.dsPayOutType.maskDataChange(true);
		payOutType.ftxtPriCode.setValue(sPayOutCode);// ����
		payOutType.ftxtfPayOutTypeCode.setValue(sLvlIdCode);// ��α���
		payOutType.ftxtfPayOutTypeName.setValue("");// ����
		if ("addfirstson".equals(payOutType.sSaveType)) {// ���ӵ�һ���ӽڵ�

		} else {// �����ӽڵ�
			// ����
			payOutType.flstPayOutTypeKind.setSelectedIndex(0);
			SetSelectTree.setIsNoCheck(payOutType.ftreeAcctJJ);
		}
		payOutType.dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_CODE)
				.setValue(sPayOutCode);
		payOutType.dsPayOutType.fieldByName("END_FLAG")
				.setValue(new Integer(1));
		payOutType.dsPayOutType.fieldByName("set_Year").setValue(
				Global.loginYear);
		payOutType.dsPayOutType.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		payOutType.dsPayOutType.maskDataChange(false);
		payOutType.ftxtfPayOutTypeCode.setFocus();
		return true;
	}
}
