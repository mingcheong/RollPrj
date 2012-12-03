/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:֧���ʽ���Դ���������
 * </p>
 * <p>
 * Description:֧���ʽ���Դ���������
 * </p>

 */
public class PayOutFSSave {
	private PayOutFS payOutFS = null;

	private String sSaveType = null;

	private PayOutFsObj payOutFsObj = null;

	private DataSet dsPayOutFS = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutFS
	 *            ֧���ʽ���Դ����ͻ�����������
	 */
	public PayOutFSSave(PayOutFS payOutFS) {
		this.payOutFS = payOutFS;
		this.sSaveType = payOutFS.sSaveType;
		this.payOutFsObj = payOutFS.payOutFsObj;
		this.dsPayOutFS = payOutFS.dsPayOutFS;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = payOutFS.sysIaeStruServ;
	}

	/**
	 * ����֧���ʽ���Դ��������
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	public void save() throws HeadlessException, Exception {
		// �õ��շ���Ŀѡ�еĽڵ����
		ArrayList lstAcctCode = null;

		// boolean bRefresh = false;
		// �ж���Ϣ��д�Ƿ�����
		if (!judgeFillInfo())
			return;
		String slvlId = payOutFS.ftxtfPfsCode.getValue().toString();
		String sPfsName = payOutFS.ftxtfPfsName.getValue().toString();
		// ��������
		dsPayOutFS.fieldByName("C1").setValue(
				payOutFS.ftxtReportPfsName.getValue().toString());

		payOutFS.dsPayOutFS.maskDataChange(true);
		// �жϱ����Ƿ��޸�,�жϱ���Ƿ��޸�
		if ("mod".equals(sSaveType.substring(0, 3))) {
			String sParId = payOutFS.lvlIdRule.previous(slvlId);
			if (sParId == null)
				sParId = "";

			ReplaceUnt replaceUnt = new ReplaceUnt();
			String sNewFName = "";
			if (!"".equals(sParId)) {
				List lstValue = replaceUnt.getParNodeInfo(sParId,
						new String[] { IPayOutFS.PFS_FNAME }, dsPayOutFS);
				if (lstValue != null)
					sNewFName = lstValue.get(0).toString();
			}
			if (sNewFName != null && !"".equals(sNewFName))
				sNewFName = sNewFName + UntPub.PARTITION_TAG + sPfsName;
			else
				sNewFName = sPfsName;

			if (!slvlId.equals(payOutFsObj.lvl_id)) {// �ڵ���뷢���ı�
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutFS.ftreePayOutFS);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						payOutFS.ftreePayOutFS, dsPayOutFS);

				replaceUnt.ReplaceFname(lstBookmark, dsPayOutFS, sNewFName,
						dsPayOutFS.fieldByName("pfs_fname").getOldValue()
								.toString(), IPayOutFS.PFS_FNAME);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsPayOutFS, slvlId,
						payOutFsObj.lvl_id, payOutFS.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsPayOutFS);
				// bRefresh = true;
			} else if (!sPfsName.equals(payOutFsObj.PFS_NAME)) {// ����δ�ı��ж������Ƿ�ı�
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(payOutFS.ftreePayOutFS);
				replaceUnt.ReplaceFname(lstBookmark, dsPayOutFS, sNewFName,
						dsPayOutFS.fieldByName(IPayOutFS.PFS_FNAME)
								.getOldValue().toString(), IPayOutFS.PFS_FNAME);
				// add by xxl ��Ϊ���ñ�����ǰû�д���ˢ�£������ڱ���ı�ʱ��Ҳˢ����
				// bRefresh = true;
			}
		}

		// �޸ĵĽڵ���Ҷ�ڵ㣬ֻ���޸����ƺͱ���,����ֻ�ڵ��fnameֵ
		if ("modname".equals(sSaveType)) {
			dsPayOutFS.fieldByName("pfs_name").setValue(sPfsName);
		}
		// �޸ĵĽڵ�ΪҶ�ڵ㣬ֻ���޸ĸ�ʽ�ͱ���
		if ("modformate".equals(sSaveType)) {
			dsPayOutFS.fieldByName("DISPLAY_FORMAT").setValue(
					payOutFS.fcbxSFormate.getValue().toString());
		}
		// add,addfirstson,mod,��ֵ����dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// ����fname
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
				String sPfsfNameNew;
				if ("".equals(payOutFsObj.pfs_fname)) // û�ϼ���fname���䱾��
				{
					sPfsfNameNew = sPfsName;
				} else { // ���ϼ���fname= �ϼ�fname+��д��name
					sPfsfNameNew = payOutFsObj.pfs_fname + UntPub.PARTITION_TAG
							+ sPfsName;
				}
				dsPayOutFS.fieldByName("pfs_fname").setValue(sPfsfNameNew);
			} else {// mod,���������ж�

			}

			// ����
			dsPayOutFS.fieldByName("pfs_name").setValue(
					payOutFS.ftxtfPfsName.getValue().toString());
			// ����
			dsPayOutFS.fieldByName("STD_TYPE_CODE").setValue(
					payOutFS.flstPfsKind.getSelectedElement().getId());
			// �������������
			dsPayOutFS.fieldByName("CF_PFS_FLAG").setValue(
					new Integer("false".equals(payOutFS.fchkPfsFlag.getValue()
							.toString()) ? 0 : 1));
			// Ĭ������
			dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE).setValue(
					new Integer("false".equals(payOutFS.fchkHide.getValue()
							.toString()) ? 0 : 1));
			// ��֧ƽ��
			dsPayOutFS.fieldByName(IPayOutFS.IS_BALANCE).setValue(
					new Integer("false".equals(payOutFS.fchkIsBalance
							.getValue().toString()) ? 0 : 1));
			// ֧����Ŀ���
			dsPayOutFS.fieldByName("SUP_PRJ").setValue(
					payOutFS.frdoSupPrj.getValue().toString());
			// ������Դ
			dsPayOutFS.fieldByName("DATA_SOURCE").setValue(
					payOutFS.frdoIncColDts.getValue().toString());
			dsPayOutFS.fieldByName("DISPLAY_FORMAT").setValue(
					payOutFS.fcbxSFormate.getValue().toString());
			// ���㹫ʽ
			String sForumla = payOutFS.ftxtaPfsFormula.getValue().toString();
			if (!"".equals(sForumla)) {
				dsPayOutFS.fieldByName("FORMULA_DET").setValue(
						PubInterfaceStub.getMethod().replaceTextEx(sForumla, 0,
								IPayOutFS.PFS_TABLE, IPayOutFS.PFS_FNAME,
								IPayOutFS.PFS_ENAME,
								"set_year =" + Global.loginYear));
			} else {
				dsPayOutFS.fieldByName("FORMULA_DET").setValue("");
			}
			// �������ȼ�
			dsPayOutFS.fieldByName("CALC_PRI").setValue(
					payOutFS.jspPfsCalcPRI.getValue().toString());

			if ("addfirstson".equals(sSaveType)) { // ���ӵ�һ���ӽڵ㣬���ĸ��ڵ�Ĳ�����Ϣ
				String sBookmark = dsPayOutFS.toogleBookmark();
				MyTreeNode node = (MyTreeNode) payOutFS.ftreePayOutFS
						.getSelectedNode();
				if (node != null) {
					dsPayOutFS.gotoBookmark(node.getBookmark());
					dsPayOutFS.fieldByName("PFS_EName").setValue("");
					dsPayOutFS.fieldByName("std_type_code").setValue("");
					dsPayOutFS.fieldByName("data_source").setValue(
							new Integer(0));
					dsPayOutFS.fieldByName("formula_det").setValue("");
					dsPayOutFS.fieldByName("calc_pri").setValue(new Integer(0));
					dsPayOutFS.fieldByName("sup_prj").setValue(new Integer(0));
					dsPayOutFS.fieldByName("CF_PFS_flag").setValue(
							new Integer(0));
					dsPayOutFS.fieldByName(IPayOutFS.IN_COMMON_USE).setValue(
							new Integer(0));
					dsPayOutFS.fieldByName("end_flag").setValue(new Integer(0));
					dsPayOutFS.fieldByName("display_format").setValue("");
					dsPayOutFS.fieldByName("edit_format").setValue("");
					// ��λ�ر��ڵ�
					dsPayOutFS.gotoBookmark(sBookmark);
				}
			}
		}

		if ("0".equals(payOutFS.frdoIncColDts.getValue().toString())) { // ������Դ:¼��
			// ���㹫ʽ
			dsPayOutFS.fieldByName("FORMULA_DET").setValue("");
			// �������ȼ�
			dsPayOutFS.fieldByName("CALC_PRI").setValue("0");
			// �õ��շ���Ŀѡ�еĽڵ����
			lstAcctCode = SysUntPub
					.getCodeList(payOutFS.fpnlAcct.getFtreAcct());
		}
		// modified by xxl 20090915ԭ���ǣ���ͬ���޸���Դ�ı��������ʱ���ᱨNULL��
		dsPayOutFS.fieldByName("name").setValue(slvlId + " " + sPfsName);
		dsPayOutFS.maskDataChange(false);

		dsPayOutFS.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsPayOutFS.fieldByName("par_id").setValue(payOutFsObj.lvl_id);

		// �����ж���Ŀ֧�ֵ��Զ��ж�
		// �õ�ԭsup_prj�ֶ�ֵ
		Object oldValue = dsPayOutFS.fieldByName(IPayOutFS.SUP_PRJ)
				.getOldValue();
		// �õ���sup_prj�ֶ�ֵ
		Object newValue = dsPayOutFS.fieldByName(IPayOutFS.SUP_PRJ).getString();
		String sBookmark = dsPayOutFS.toogleBookmark();
		// �ж�sup_prj�ֶ�ֵ��û�з����ı�
		if (oldValue != null
				&& !oldValue.toString().equals(newValue.toString())) {
			String pfsEname = dsPayOutFS.fieldByName(IPayOutFS.PFS_ENAME)
					.getString();
			// ʹ�õݹ飬����ʹ�ø��ʽ���Դ��Ϊ��ʽԪ�صļ�¼��sup_prj�ֶ�ֵ
			changeSupPrj(pfsEname, dsPayOutFS);
		}
		dsPayOutFS.gotoBookmark(sBookmark);

		// �ύ����
		if ("addfirstson".equals(sSaveType)) {
			sysIaeStruServ.savePayOutFS(dsPayOutFS, dsPayOutFS.fieldByName(
					IPayOutFS.PFS_CODE).getString(), payOutFsObj.PFS_CODE,
					Global.loginYear, lstAcctCode, true);
		} else {
			sysIaeStruServ.savePayOutFS(dsPayOutFS, dsPayOutFS.fieldByName(
					IPayOutFS.PFS_CODE).getString(), payOutFsObj.PFS_CODE,
					Global.loginYear, lstAcctCode, false);
		}
		dsPayOutFS.applyUpdate();
		// add״̬��λ�����ӵĽڵ�
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			payOutFS.ftreePayOutFS.expandTo("lvl_id", slvlId);
		// ˢ����
		dsPayOutFS.maskDataChange(true);
		payOutFS.ftreePayOutFS.reset();
		dsPayOutFS.maskDataChange(false);
		payOutFS.ftreePayOutFS.expandTo("lvl_id", slvlId);

		// ˢ��ϵͳ�����ֵ���Ϣ
		if (!SysUntPub.synDict(IPayOutFS.PFS_TABLE)) {
			JOptionPane.showMessageDialog(payOutFS, "ˢ��ϵͳ�����ֵ���Ϣ��������!", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
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
		String sLvlId = payOutFS.ftxtfPfsCode.getValue().toString();
		String sParId = payOutFS.lvlIdRule.previous(sLvlId);// ��ø��������
		String sPfsName = payOutFS.ftxtfPfsName.getValue().toString().trim();
		String sPfsCode = dsPayOutFS.fieldByName("pfs_code").getString();
		// ���뱻�޸ģ�Ҫ�жϱ���
		if ("mod".equals(payOutFS.sSaveType.substring(0, 3))
				&& sLvlId != payOutFsObj.lvl_id) {
			// �жϱ����Ƿ���д��ȷ
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(payOutFS, "���벻��Ϊ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// �жϱ�����д���Ƿ�������
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(payOutFS, "������������֣���������д��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// �жϱ��볤����д�Ƿ���ȷ,�޸ĵ����Ҫ�ж�
			int iLevel = payOutFS.lvlIdRule.levelOf(sLvlId); // ��õ�ǰ����ڴ�
			int iCount = payOutFS.lvlIdRule.originRules().size();
			if (iLevel < 0) {
				JOptionPane.showMessageDialog(payOutFS, "���벻��ȷ����������λһ���Ҳ������ڴγ���"
						+ String.valueOf(iCount) + "�� ����������д!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// �ж����������Ҫ>=1001
			String sRootCode = payOutFS.lvlIdRule.rootCode(sLvlId); // ���ݵ�ǰ����������ı���ֵ
			if (sRootCode.compareTo(ISysIaeStru.ROOT_CODE) < 0) {
				JOptionPane.showMessageDialog(payOutFS, "����������1000,��������д!",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
			// �޸ı��븸�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽڵ㣬�����Ҷ�ӽڵ㣬�����޸�
			if (!"".equals(sParId) && sParId != null) {
				InfoPackage infoPackage = sysIaeStruServ.judgePfsParExist(
						sParId, Global.loginYear);
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(payOutFS, infoPackage
							.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					payOutFS.ftxtfPfsCode.setFocus();
					return false;
				}
			}
			// �жϱ����Ƿ��ظ�
			InfoPackage infoPackage = sysIaeStruServ.judgePfsCodeRepeat(sLvlId,
					Global.loginYear, payOutFsObj.PFS_CODE, true);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(payOutFS, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}

			// �жϽڵ㲻��ֱ�Ӹ�Ϊ�¼��ڵ㣬�����������
			ReplaceUnt replaceUnt = new ReplaceUnt();
			if (!replaceUnt.checkCode(sLvlId, dsPayOutFS.fieldByName("lvl_id")
					.getOldValue().toString(), payOutFS.lvlIdRule)) {
				JOptionPane.showMessageDialog(payOutFS,
						"���ܽ��ڵ��޸ĳ��Լ����¼��ڵ�,��������д����!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				payOutFS.ftxtfPfsCode.setFocus();
				return false;
			}
		}

		// �ж��ʽ������Ƿ���д
		if ("".equals(sPfsName)) {
			JOptionPane.showMessageDialog(payOutFS, "�ʽ���Դ���Ʋ���Ϊ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			payOutFS.ftxtfPfsName.setFocus();
			return false;
		}
		// �ж�ͬ�������Ƿ��ظ�
		InfoPackage infoPackage;
		if ("mod".equals(sSaveType.substring(0, 3))) // �Ƿ����޸�,�ж��������ϲ������Լ�
			infoPackage = sysIaeStruServ.judgePfsNameRepeat(sPfsName, sParId,
					Global.loginYear, sPfsCode, true);
		else
			infoPackage = sysIaeStruServ.judgePfsNameRepeat(sPfsName, sParId,
					Global.loginYear, null, false);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(payOutFS, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �ж�������Դ�Ǽ���ı������ü��㹫ʽ
		if ("1".equals(payOutFS.frdoIncColDts.getValue().toString())
				&& "".equals(payOutFS.ftxtaPfsFormula.getValue().toString())) {
			JOptionPane.showMessageDialog(payOutFS, "������Դ�Ǽ���ı������ü��㹫ʽ!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �ж��ʽ���Դ�ܺϼ��Ƿ��ѱ�ʹ�ã�����ֻ����һ���ֶ����ܺϼ�
		if ("001".equals(payOutFS.flstPfsKind.getSelectedElement().getId())) {
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)
					|| "mod".equals(sSaveType)) {
				boolean bFlag;
				if ("mod".equals(sSaveType)) {
					bFlag = sysIaeStruServ.judgePfsStdTypeCode(
							payOutFsObj.PFS_CODE, Global.loginYear, true);
				} else {
					bFlag = sysIaeStruServ.judgePfsStdTypeCode(null,
							Global.loginYear, false);
				}
				if (!bFlag) {
					JOptionPane.showMessageDialog(payOutFS,
							"����[�ʽ���Դ�ܺϼ�]�Ѿ���ʹ��!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (dsPayOutFS.fieldByName("end_flag").getInteger() != 1) {
					JOptionPane.showMessageDialog(payOutFS,
							"����[�ʽ���Դ�ܺϼ�]ֻ��ʹ����ĩ���ʽ���Դ!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (!"1".equals(payOutFS.frdoIncColDts.getValue().toString())) {
					JOptionPane.showMessageDialog(payOutFS,
							"����[�ʽ���Դ�ܺϼ�]������Դֻ���Ǽ���!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * /ʹ�õݹ飬����ʹ�ø��ʽ���Դ��Ϊ��ʽԪ�صļ�¼��sup_prj�ֶ�ֵ
	 * 
	 * @param pfsEname
	 * @param dsPfs
	 * @throws Exception
	 */
	private void changeSupPrj(String pfsEname, DataSet dsPfs) throws Exception {
		// ��ѯʹ�ø��ʽ���Դ�ļ�¼
		// DatadsPfs.locateByFilter()
		// String sSql = "select PFS_CODE ,FORMULA_DET from
		// fb_iae_payout_fundsource where FORMULA_DET like '%{"
		// + pfsEname + "}%'";
		// String sFilter = "FORMULA_DET like '%{" + pfsEname + "}%'";
		// DataSet ds = DataSetUtil.filterBy(dsPfs, sFilter);
		// ds.setSqlClause(sSql);
		// ds.open();
		// ˢ�¼��㹫ʽʹ�õ����ʽ���Դ��sup_prjֵ
		DataSet ds = getRecordWithEname(dsPfs, pfsEname);
		if (ds == null)
			return;

		ds.beforeFirst();
		while (ds.next()) {
			// �õ���ʽֵ����
			String sForumla = ds.fieldByName(IPayOutFS.FORMULA_DET).getString();
			int supPrj = payOutFS.getsupPrjValue(sForumla, dsPayOutFS);
			// �õ���ʽֵ����
			String sPfsCode = ds.fieldByName(IPayOutFS.PFS_CODE).getString();
			if (dsPfs.locate(IPayOutFS.PFS_CODE, sPfsCode)) {
				dsPfs.fieldByName(IPayOutFS.SUP_PRJ).setValue(
						String.valueOf(supPrj));
				// �õ�Enameֵ
				String pfsEnameSearch = dsPfs.fieldByName(IPayOutFS.PFS_ENAME)
						.getString();
				changeSupPrj(pfsEnameSearch, dsPfs);
			}

		}
	}

	/**
	 * �õ���ʽ���ݰ���pfsEnameֵ�ļ�¼
	 * 
	 * @param dsPfs
	 * @param pfsEname
	 * @return
	 * @throws Exception
	 */
	private DataSet getRecordWithEname(DataSet dsPfs, String pfsEname)
			throws Exception {
		if (dsPfs == null && dsPfs.isEmpty()) {
			return null;
		}
		DataSet ds = null;
		dsPfs.beforeFirst();
		while (dsPfs.next()) {
			String sForumla = dsPfs.fieldByName(IPayOutFS.FORMULA_DET)
					.getString();
			if (sForumla.indexOf("{" + pfsEname + "}") != -1) {
				if (ds == null) {
					ds = DataSet.create();
				}
				ds.append();
				ds.setOriginData(dsPfs.getOriginData());
			}
		}
		if (ds != null) {
			ds.applyUpdate();
		}
		return ds;
	}

}
