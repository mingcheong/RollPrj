/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧����Ŀ��𱣴������
 * </p>
 * <p>
 * Description:֧����Ŀ��𱣴������

 */
public class PayOutTypeSave {
	private PayOutType payOutType = null;

	private DataSet dsPayOutType = null;

	private DataSet dsAcctJJ = null;

	private PayOutTypeObj payOutTypeObj = null;

	private ISysIaeStru sysIaeStruServ = null;

	private String sSaveType = null;

	private MyTreeNode myTreeNode[] = null;

	private CustomTree ftreeAcctJJ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutType
	 *            ֧����Ŀ������ͻ�����������
	 */
	public PayOutTypeSave(PayOutType payOutType) {
		this.payOutType = payOutType;
		this.dsPayOutType = payOutType.dsPayOutType;
		this.payOutTypeObj = payOutType.payOutTypeObj;
		this.sSaveType = payOutType.sSaveType;
		this.ftreeAcctJJ = payOutType.ftreeAcctJJ;
		this.dsAcctJJ = payOutType.dsAcctJJ;
		this.sysIaeStruServ = payOutType.sysIaeStruServ;
	}

	/**
	 * ����֧����Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void save() throws Exception {
		boolean bRefresh = false;
		// modify by qzc 2009,6,4
		myTreeNode = ftreeAcctJJ.getSelectedNodes(true);
		// �ж���Ϣ��д�Ƿ�����
		if (!judgeFillInfo())
			return;
		String slvlId = payOutType.ftxtfPayOutTypeCode.getValue().toString();
		String sPayOutTypeName = payOutType.ftxtfPayOutTypeName.getValue()
				.toString();
		String sPayoutKindCode = dsPayOutType.fieldByName(
				IPayOutType.PAYOUT_KIND_CODE).getString();

		String sParCode = "";
		String sParId = null;
		dsPayOutType.maskDataChange(true);
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))// ����
			sParCode = payOutTypeObj.payout_kind_code;
		else {// �޸�
			sParId = payOutType.lvlIdRule.previous(slvlId);
			if (sParId == null) {
				sParId = "";
			}

			ReplaceUnt replaceUnt = new ReplaceUnt();
			List lstValue = replaceUnt.getParNodeInfo(sParId,
					new String[] { "PAYOUT_KIND_CODE" }, dsPayOutType);
			if (lstValue != null)
				sParCode = lstValue.get(0).toString();

			if (!slvlId.equals(payOutTypeObj.lvl_id)) {// �ڵ���뷢���ı�
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutType.ftreePayOutType);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						payOutType.ftreePayOutType, dsPayOutType);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsPayOutType, slvlId,
						payOutTypeObj.lvl_id, payOutType.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsPayOutType);
				bRefresh = true;
			}
		}

		if ("modformate".equals(sSaveType)) {
			// ���������䵽��ϸ
			dsPayOutType.fieldByName("N1").setValue(
					new Integer("false".equals(payOutType.fchkPayOutFlag
							.getValue().toString()) ? 0 : 1));
		}

		if ("modname".equals(sSaveType)) {
			// ����
			dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_NAME).setValue(
					payOutType.ftxtfPayOutTypeName.getValue().toString());
		}

		// add,addfirstson,mod,��ֵ����dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// ����
			dsPayOutType.fieldByName(IPayOutType.PAYOUT_KIND_NAME).setValue(
					payOutType.ftxtfPayOutTypeName.getValue().toString());
			// ����
			dsPayOutType.fieldByName(IPayOutType.STD_TYPE_CODE).setValue(
					payOutType.flstPayOutTypeKind.getSelectedElement().getId());
			// ���������䵽��ϸ
			dsPayOutType.fieldByName("N1").setValue(
					new Integer("false".equals(payOutType.fchkPayOutFlag
							.getValue().toString()) ? 0 : 1));
		}
		// �����ӵ��ǵ�һ���ӽڵ㣬�޸ĸ��ڵ㲿����Ϣ
		if ("addfirstson".equals(sSaveType)) {
			String sBookmark = dsPayOutType.toogleBookmark();
			MyTreeNode node = payOutType.ftreePayOutType
					.getSelectedNode();
			MyTreeNode parentNode = (MyTreeNode) node.getParent();
			if (parentNode != null) {
				dsPayOutType.gotoBookmark(node.getBookmark());
				dsPayOutType.fieldByName(IIncType.STD_TYPE_CODE).setValue("");
				dsPayOutType.fieldByName("end_flag").setValue(new Integer(0));
			}
			// ��λ�ر��ڵ�
			dsPayOutType.gotoBookmark(sBookmark);
		}

		DataSet dsPayoutKindToJj = null;
		if ("addfirstson".equals(sSaveType) || "mod".equals(sSaveType)
				|| "add".equals(sSaveType) || "modformate".equals(sSaveType)) {
			dsPayoutKindToJj = DataSet.create();
			// �����Ӧ���ÿ�Ŀ��Ϣ
			dsPayoutKindToJj.clearAll();
			if (myTreeNode != null) {
				for (int i = 0; i < myTreeNode.length; i++) {
					dsAcctJJ.gotoBookmark(myTreeNode[i].getBookmark());
					String sBSI_ID = dsAcctJJ.fieldByName(IPubInterface.BSI_ID)
							.getString();
					String sAcctCodeJj = dsAcctJJ.fieldByName(
							IPubInterface.ACCT_CODE_JJ).getString();
					String sAcctNameJj = dsAcctJJ.fieldByName(
							IPubInterface.ACCT_NAME_JJ).getString();
					dsPayoutKindToJj.append();
					dsPayoutKindToJj.fieldByName("PAYOUT_KIND_CODE").setValue(
							sPayoutKindCode);
					dsPayoutKindToJj.fieldByName("PAYOUT_KIND_NAME").setValue(
							sPayOutTypeName);
					dsPayoutKindToJj.fieldByName("BSI_ID").setValue(sBSI_ID);
					dsPayoutKindToJj.fieldByName("ACCT_CODE_JJ").setValue(
							sAcctCodeJj);
					dsPayoutKindToJj.fieldByName("ACCT_NAME_JJ").setValue(
							sAcctNameJj);
					dsPayoutKindToJj.fieldByName("SET_YEAR").setValue(
							Global.loginYear);
					dsPayoutKindToJj.fieldByName("RG_CODE").setValue(
							Global.getCurrRegion());
				}
			}
		}
		dsPayOutType.maskDataChange(false);
		dsPayOutType.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsPayOutType.fieldByName("par_id").setValue(payOutTypeObj.lvl_id);
		else
			dsPayOutType.fieldByName("par_id").setValue(sParId);
		dsPayOutType.fieldByName("name").setValue(
				slvlId + " " + sPayOutTypeName);
		// �ύ����
		sysIaeStruServ.savePayOutKind(dsPayOutType, dsPayoutKindToJj,
				sSaveType, sPayoutKindCode, sPayOutTypeName, Global.loginYear,
				sParCode);
		dsPayOutType.applyUpdate();
		payOutType.setChangeFlag(true);
		// add״̬��λ�����ӵĽڵ�
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			payOutType.ftreePayOutType.expandTo("lvl_id", slvlId);
		if (bRefresh) { // ˢ����
			payOutType.ftreePayOutType.reset();
			payOutType.ftreePayOutType.expandTo("lvl_id", slvlId);
		}

	}

	/**
	 * �ж���д��Ϣ�Ƿ�������
	 * 
	 * @throws Exception
	 * @throws HeadlessException
	 * 
	 * @return��true��дû�����⣬false,��д������
	 */
	private boolean judgeFillInfo() throws HeadlessException, Exception {
		String sLvlId = payOutType.ftxtfPayOutTypeCode.getValue().toString();
		String sPayOutKindName = payOutType.ftxtfPayOutTypeName.getValue()
				.toString().trim();
		String sParId = payOutType.lvlIdRule.previous(sLvlId); // ����ϼ�����
		if ("mod".equals(sSaveType.substring(0, 3))) {
			// �жϱ����Ƿ�Ϊ��
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(payOutType, "���벻��Ϊ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutType.ftxtfPayOutTypeCode.setFocus();
				return false;
			}

			// ���뱻�޸ģ�Ҫ�жϱ���
			if (sLvlId != payOutTypeObj.lvl_id) {
				// �жϱ�����д���Ƿ�������
				if (!sLvlId.matches("\\d+")) {
					JOptionPane.showMessageDialog(payOutType, "������������֣���������д��",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// �жϱ��볤����д�Ƿ���ȷ,�޸ĵ����Ҫ�ж�
				int iLevel = payOutType.lvlIdRule.levelOf(sLvlId); // ��õ�ǰ����ڴ�
				int iCount = payOutType.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(payOutType,
							"���벻��ȷ����������λһ���Ҳ������ڴγ���" + String.valueOf(iCount)
									+ "�� ����������д!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// �޸ı��븸�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽڵ㣬�����Ҷ�ӽڵ㣬�����޸�
				String sPar = payOutType.lvlIdRule.previous(sLvlId);// ��ø��������
				if (!"".equals(sPar) && sPar != null) {
					InfoPackage infoPackage = sysIaeStruServ
							.judgePayOutTypeParExist(sPar, Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(payOutType, infoPackage
								.getsMessage(), "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
						payOutType.ftxtfPayOutTypeCode.setFocus();
						return false;
					}
				}
				// �жϱ����Ƿ��ظ�,�޸�����жϣ�������Ϊ�������Զ������Ҳ����޸ĵ�
				if (!sysIaeStruServ.judgePayOutTypeCodeRepeat(sLvlId,
						Global.loginYear, payOutTypeObj.payout_kind_code, true)) {
					JOptionPane.showMessageDialog(payOutType, "�����Ѿ���ʹ��!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
				// �жϽڵ㲻��ֱ�Ӹ�Ϊ�¼��ڵ㣬�����������
				ReplaceUnt replaceUnt = new ReplaceUnt();
				if (!replaceUnt.checkCode(sLvlId, dsPayOutType.fieldByName(
						"lvl_id").getOldValue().toString(),
						payOutType.lvlIdRule)) {
					JOptionPane.showMessageDialog(payOutType,
							"���ܽ��ڵ��޸ĳ��Լ����¼��ڵ�,��������д����!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					payOutType.ftxtfPayOutTypeCode.setFocus();
					return false;
				}
			}
		}
		// �ж������Ƿ���д
		if ("".equals(sPayOutKindName)) {
			JOptionPane.showMessageDialog(payOutType, "������Ŀ���Ʋ���Ϊ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			payOutType.ftxtfPayOutTypeCode.setFocus();
			return false;
		}
		// �ж�ͬ�������Ƿ��ظ�
		boolean bFlag;
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			bFlag = sysIaeStruServ.judgePayOutTypeNameRepeat(sPayOutKindName,
					sParId, Global.loginYear, null, false);
		} else {
			bFlag = sysIaeStruServ.judgePayOutTypeNameRepeat(sPayOutKindName,
					sParId, Global.loginYear, payOutTypeObj.payout_kind_code,
					true);
		}
		if (!bFlag) {
			JOptionPane.showMessageDialog(payOutType, "�ʽ���Դ�����Ѿ���ʹ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			payOutType.ftxtfPayOutTypeName.setFocus();
			return false;
		}

		// �ж϶�Ӧ�ľ��ÿ�Ŀ,Ҷ�ڵ���Ҫ�ж�
		if (dsPayOutType.fieldByName("end_flag").getInteger() == 1
				&& !IPayOutType.PAYOUTKINDSTAND_PRJ
						.equals(payOutType.flstPayOutTypeKind
								.getSelectedElement().getId())) {
			// �ж϶�Ӧ�ľ��ÿ�Ŀ����Ϊ��
			myTreeNode = ftreeAcctJJ.getSelectedNodes(true);
			if (myTreeNode.length == 0) {
				JOptionPane.showMessageDialog(payOutType, "��Ӧ�ľ��ÿ�Ŀ����Ϊ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutType.ftxtfPayOutTypeName.setFocus();
				return false;
			}
		}
		// �ж�֧����Ŀ�������-ר��֧��ֻ����ѡ��һ��
		String stdTypeCode = payOutType.flstPayOutTypeKind.getSelectedElement()
				.getId();
		if (IPayOutType.PAYOUTKINDSTAND_PRJ.equals(stdTypeCode)) {
			String payoutKindName = sysIaeStruServ
					.getPayoutKindStandPrj(payOutType.ftxtPriCode.getValue()
							.toString());
			if (!Common.isNullStr(payoutKindName)) {
				JOptionPane.showMessageDialog(payOutType, "ר��֧��ֻ�ɱ�\""
						+ payoutKindName
						+ "\"֧����Ŀ���ʹ��,ר��֧��ֻ���Ա�һ��֧����Ŀ���ʹ�ã���ѡ����������!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		return true;
	}
}
