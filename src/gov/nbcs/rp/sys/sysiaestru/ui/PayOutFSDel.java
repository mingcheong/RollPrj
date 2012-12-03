/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧���ʽ���Դɾ��������
 * </p>
 * <p>
 * Description:֧���ʽ���Դɾ��������
 * </p>

 */

public class PayOutFSDel {
	// ֧���ʽ���Դ����ͻ�����������
	private PayOutFS payOutFs = null;

	// ֧���ʽ���Դ����
	private PayOutFsObj payOutFsObj = null;

	// ֧���ʽ���ԴDataSet
	private DataSet dsPayOutFS = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutFS
	 *            ֧���ʽ���Դ����ͻ�����������
	 */
	public PayOutFSDel(PayOutFS payOutFs) {
		this.payOutFs = payOutFs;
		this.payOutFsObj = payOutFs.payOutFsObj;
		this.dsPayOutFS = payOutFs.dsPayOutFS;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = payOutFs.sysIaeStruServ;
	}

	/**
	 * ɾ��֧���ʽ���Դ��������
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public boolean delete() throws HeadlessException, Exception {
		// �ж��Ƿ���ĩ�ڵ㣬����ĩ�ڵ㣬������ɾ��
		if (payOutFsObj.end_flag == 0) {
			JOptionPane.showMessageDialog(payOutFs, "֧���ʽ���Դ��������Ŀ,����ɾ������Ŀ!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// ȡ��֧���ʽ���ԴӢ����
		String sEngName = payOutFsObj.pfs_ename;
		String sPfsCode = payOutFsObj.PFS_CODE;
		String sPfsName = payOutFsObj.PFS_NAME;
		// �жϸ�֧���ʽ���Դ����Ϊ������Ŀ���㹫ʽ������,����ǣ�������ɾ��
		InfoPackage infoPackage = sysIaeStruServ.judgePfsEnableDel(sEngName,
				Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutFs, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// ��ʾ�Ƿ�ȷ��ɾ��
		if (JOptionPane.showConfirmDialog(payOutFs, "���Ƿ�ȷ��Ҫɾ��������¼?", "��ʾ",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return false;

		infoPackage = sysIaeStruServ.chkFundSourceByRef(Global.loginYear,
				sPfsCode, sEngName); // 0δ��ʹ��1�����ã�����ṹ��2��ʹ�ã�¼�����ݣ�3�ȱ������ֱ�ʹ�á�
		int iReUse = Integer.parseInt(infoPackage.getObject().toString());
		MyTreeNode curNode = payOutFs.ftreePayOutFS.getSelectedNode();
		MyTreeNode parNode = (MyTreeNode) curNode.getParent();

		if (parNode.getChildCount() == 1) {// ���ڵ�ֻ��һ���ڵ�
			// ȡ�����ϼ�ֻ��һ�����ӵĽڵ�
			MyTreeNode curNodeTmp = curNode;
			MyTreeNode parNodeTmp = parNode;
			while (parNodeTmp.getChildCount() == 1
					&& parNodeTmp != payOutFs.ftreePayOutFS.getRoot()) {
				// �ж��Ƿ�ֻ��һ�����ӽڵ�
				curNodeTmp = parNodeTmp;
				parNodeTmp = (MyTreeNode) curNodeTmp.getParent();
			}
			String sShowInfo;
			if (iReUse == 0)
				sShowInfo = "��ո��ʽ���Դ�����ϼ�[" + curNodeTmp.getUserObject()
						+ "]��ص���Ϣ��?";
			else
				sShowInfo = "���ʽ���Դ\n" + infoPackage.getsMessage()
						+ "��ո��ʽ���Դ�����ϼ�[" + parNode.getUserObject() + "]��ص���Ϣ��?";
			if (JOptionPane.showConfirmDialog(payOutFs, sShowInfo, "��ʾ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				Enumeration allNodes = curNodeTmp.breadthFirstEnumeration();
				List bookmarkList = new ArrayList();
				MyTreeNode node;
				while (allNodes.hasMoreElements()) {
					node = (MyTreeNode) allNodes.nextElement();
					bookmarkList.add(node.getBookmark());
				}
				for (int i = bookmarkList.size() - 1; i >= 0; i--) {
					dsPayOutFS.gotoBookmark(bookmarkList.get(i).toString());
					dsPayOutFS.delete();
				}
				sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode, "",
						sEngName, 1, dsPayOutFS, iReUse == 0 ? false : true,
						sPfsName);

			} else {// ����ո��ʽ���Դ�����ϼ�,��ոýڵ㣬���ýڵ㲿����Ϣ�������ڵ�
				if (JOptionPane
						.showConfirmDialog(payOutFs,
								"�Ѹ��ʽ���Դ��Ӧ�����ݡ���ʽȫ��Ǩ�Ƶ����ϼ��ʽ���Դ����?", "��ʾ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
					return false;
				// ɾ�����ڵ�
				dsPayOutFS.delete();
				dsPayOutFS.edit();
				// �����ڵ���Ϣ�������ڵ�
				dsPayOutFS.gotoBookmark(parNode.getBookmark());
				String sTagPfsCode = dsPayOutFS.fieldByName("pfs_code")
						.getString();
				dsPayOutFS.maskDataChange(true);
				dsPayOutFS.fieldByName("PFS_EName").setValue(
						payOutFsObj.pfs_ename);
				dsPayOutFS.fieldByName("std_type_code").setValue(
						payOutFsObj.std_type_code);
				dsPayOutFS.fieldByName("data_source").setValue(
						new Integer(payOutFsObj.data_source));
				dsPayOutFS.fieldByName("formula_det").setValue(
						payOutFsObj.formula_det);
				dsPayOutFS.fieldByName("calc_pri").setValue(
						new Integer(payOutFsObj.calc_pri));
				dsPayOutFS.fieldByName("sup_prj").setValue(
						new Integer(payOutFsObj.sup_prj));
				dsPayOutFS.fieldByName("CF_PFS_flag").setValue(
						new Integer(payOutFsObj.cf_pfs_flag));
				dsPayOutFS.fieldByName("end_flag").setValue(new Integer(1));
				dsPayOutFS.maskDataChange(false);
				sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode,
						sTagPfsCode, sEngName, 2, dsPayOutFS,
						iReUse == 0 ? false : true, sPfsName);
			}
		} else {// ���ڵ����������Ͻڵ㣬ɾ��ʱֱ��ɾ��
			// ɾ�����ڵ�
			dsPayOutFS.delete();
			dsPayOutFS.gotoBookmark(parNode.getBookmark());
			sysIaeStruServ.delFundSource(Global.loginYear, sPfsCode, "",
					sEngName, 1, dsPayOutFS, iReUse == 0 ? false : true,
					sPfsName);
		}
		dsPayOutFS.applyUpdate();
		// ˢ��ϵͳ�����ֵ���Ϣ
		if (!SysUntPub.synDict(IPayOutFS.PFS_TABLE)) {
			JOptionPane.showMessageDialog(payOutFs, "ˢ��ϵͳ�����ֵ���Ϣ��������!", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
}
