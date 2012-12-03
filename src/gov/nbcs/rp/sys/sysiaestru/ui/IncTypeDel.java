/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * <p>
 * Title:������Ŀ���ɾ��������
 * </p>
 * <p>
 * Description:������Ŀ���ɾ��������
 * </p>
 * <p>

 */

public class IncTypeDel {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	// ������Ŀ������ݼ�
	private DataSet dsIncType = null;

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
	public IncTypeDel(IncType incType) {
		this.incType = incType;
		this.dsIncType = incType.dsIncType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * ɾ��������Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {

		// ��ɾ���ڵ�ĸ��ڵ�ֻ��һ���ӽڵ��ǣ����游�ڵ����
		String sParIncTypeCode = null;// ���ڵ����

		// �ж��Ƿ���ĩ�ڵ㣬����ĩ�ڵ㣬������ɾ��
		if (dsIncType.fieldByName("End_flag").getValue() != null
				&& dsIncType.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(incType, "������Ŀ����������Ŀ,����ɾ������Ŀ!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж��ܷ�ɾ��
		InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableDel(
				incTypeObj.inctype_code, String.valueOf(incTypeObj.set_year));
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incType, infoPackage.getsMessage()
					+ "������ɾ����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// ��ʾ�Ƿ�ȷ��ɾ��
		if (JOptionPane.showConfirmDialog(incType, "���Ƿ�ȷ��Ҫɾ��������¼?", "��ʾ",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return false;
		// �ж�ѡ�еĽڵ�ĸ��ڵ��Ƿ����
		MyTreeNode node = incType.ftreeIncType.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		if (parentNode != (MyTreeNode) incType.ftreeIncType.getRoot()) {
			// �ж��м����ֵܣ����ֻ��һ���ڵ㣬�౾�ڵĲ�����Ϣ�������ڵ�
			if (parentNode.getChildCount() == 1) {
				// ��λ�����ڵ�
				dsIncType.gotoBookmark(parentNode.getBookmark());
				sParIncTypeCode = dsIncType.fieldByName("inctype_code")
						.getString();
				// ��������Ϣ
				dsIncType.maskDataChange(true);
				dsIncType.edit();
				dsIncType.fieldByName("std_type_code").setValue(
						incTypeObj.std_type_code);
				dsIncType.fieldByName("end_flag").setValue(
						new Integer(incTypeObj.end_flag));
				dsIncType.fieldByName("is_inc").setValue(
						new Integer(incTypeObj.is_inc));
				dsIncType.fieldByName(IIncType.IS_SUM).setValue(
						new Integer(incTypeObj.is_sum));
				dsIncType.maskDataChange(false);
				// ��λ�ر��ڵ�
				dsIncType.gotoBookmark(node.getBookmark());
			}
		}
		// �õ���Ӧ��֧���ʽ���Դ����
		String sPfsCode = null;
		if (dsIncType.fieldByName(IIncType.IS_INC).getInteger() == 2) {
			List lstPfsCode = SysUntPub.getLeafNodeCode(
					incType.fpnlPfs.ftreePfs, incType.fpnlPfs.ftreePfs
							.getDataSet(), IPayOutFS.PFS_CODE);
			if (lstPfsCode != null && !lstPfsCode.isEmpty()) {
				sPfsCode = lstPfsCode.get(0).toString();
			}
		}
		// ɾ�����ڵ�
		dsIncType.delete();
		// �ύ���ݿ�
		sysIaeStruServ.delIncType(dsIncType, incTypeObj.inctype_code,
				sParIncTypeCode, String.valueOf(incTypeObj.set_year),
				incTypeObj.inctype_name);

		dsIncType.applyUpdate();
		return true;
	}
}
