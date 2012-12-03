/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ��𱣴������
 * </p>
 * <p>
 * Description:������Ŀ��𱣴������
 * </p>

 */
public class IncTypeSave {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	// ������Ŀ������
	private IncTypeObj incTypeObj = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	// ������Ŀ���DataSet
	private DataSet dsIncType = null;

	// ��������
	private String sSaveType = null;

	// ѡ�е�������Ŀ�ڵ�
	private MyTreeNode incColNodes[] = null;

	// ѡ�е�֧���ʽ���Դ�ڵ�
	private MyTreeNode pfsNodes[] = null;

	/**
	 * ���캯��
	 * 
	 * @param incType
	 *            ������Ŀ������ͻ�����������
	 */
	public IncTypeSave(IncType incType) {
		this.incType = incType;
		this.dsIncType = incType.dsIncType;
		this.incTypeObj = incType.incTypeObj;
		this.sSaveType = incType.sSaveType;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	/**
	 * ����������Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void save() throws Exception {
		boolean bRefresh = false;
		// �ж���Ϣ��д�Ƿ�����
		if (!judgeFillInfo())
			return;
		String slvlId = incType.ftxtfIncTypeCode.getValue().toString();
		String sIncTypeName = incType.ftxtfIncTypeName.getValue().toString();

		String sParId = null;

		dsIncType.maskDataChange(true);
		// �жϱ����Ƿ��޸�,�жϱ���Ƿ��޸�
		if ("mod".equals(incType.sSaveType.substring(0, 3))) {
			sParId = incType.lvlIdRule.previous(slvlId);
			if (sParId == null)
				sParId = "";
			// �ڵ���뷢���ı�
			if (!slvlId.equals(incTypeObj.lvl_id)) {
				ReplaceUnt replaceUnt = new ReplaceUnt();
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incType.ftreeIncType);
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						incType.ftreeIncType, dsIncType);
				replaceUnt.ReplaceLvlPar(lstBookmark, dsIncType, slvlId,
						incTypeObj.lvl_id, incType.lvlIdRule);
				replaceUnt.delParNode(lstDelBookmark, dsIncType);
				bRefresh = true;
			}
		}

		// �޸ĵĽڵ���Ҷ�ڵ㣬ֻ���޸����ƺͱ���
		if ("modname".equals(sSaveType)) {
			dsIncType.fieldByName(IIncType.INCTYPE_NAME).setValue(sIncTypeName);
		}
		// add,addfirstson,mod,��ֵ����dataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)
				|| "modformate".equals(sSaveType)) {
			// ����
			dsIncType.fieldByName(IIncType.INCTYPE_NAME).setValue(
					incType.ftxtfIncTypeName.getValue().toString());
			// ����
			dsIncType.fieldByName(IIncType.STD_TYPE_CODE).setValue(
					incType.flstIncTypeKind.getSelectedElement().getId());
			// ��������,¼����˰��ȡ��
			dsIncType.fieldByName(IIncType.IS_INC).setValue(
					incType.frdoIsInc.getValue());
			// �Ƿ�������
			dsIncType.fieldByName(IIncType.IS_SUM).setValue(
					new Integer("false".equals(incType.fchkIsMid.getValue()
							.toString()) ? 1 : 0));

		}

		if ("addfirstson".equals(sSaveType)) {
			String sBookmark = dsIncType.toogleBookmark();
			MyTreeNode node = incType.ftreeIncType
					.getSelectedNode();
			MyTreeNode parentNode = (MyTreeNode) node.getParent();
			if (parentNode != null) {
				dsIncType.gotoBookmark(node.getBookmark());
				dsIncType.fieldByName(IIncType.STD_TYPE_CODE).setValue("");
				dsIncType.fieldByName("end_flag").setValue(new Integer(0));
				dsIncType.fieldByName("IS_INC").setValue(new Integer(0));
				dsIncType.fieldByName(IIncType.IS_SUM).setValue(new Integer(1));
				// ��λ�ر��ڵ�
				dsIncType.gotoBookmark(sBookmark);
			}
		}

		dsIncType.maskDataChange(false);
		dsIncType.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsIncType.fieldByName("par_id").setValue(incTypeObj.lvl_id);
		else
			dsIncType.fieldByName("par_id").setValue(sParId);
		dsIncType.fieldByName("name").setValue(slvlId + " " + sIncTypeName);

		// �õ�ǰ�༭�ڵ������
		String sIncTypeCode = dsIncType.fieldByName(IIncType.INCTYPE_CODE)
				.getString();

		// ������Դ
		String isInc = incType.frdoIsInc.getValue().toString();

		// ֧���ʽ���Դ
		List lstPfsCode = null;
		// �õ�ѡ�е��������ڵ�����
		if ("0".equals(isInc)) {// ¼��
			incType.dsInccolumnToInc = null;
		} else if ("1".equals(isInc)) {// ��˰����ȡ��
			// ��֯������Ŀ�����������Ŀ�Ķ�Ӧ��ϵ
			incType.dsInccolumnToInc = orgInctypeToIncolumn(
					incType.dsInccolumnToInc,
					incType.fpnlIncCol.ftreeIncColumn, sIncTypeCode);
		} else if ("2".equals(isInc)) {// ��֧��Ԥ���ȡ��
			incType.dsInccolumnToInc = null;
			DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
			lstPfsCode = SysUntPub.getLeafNodeCode(incType.fpnlPfs.ftreePfs,
					dsPayOutFS, IPayOutFS.PFS_CODE);
		}

		// �ύ����
		String sNewIncTypeName = null;
		if ("mod".equals(sSaveType)) {
			if (!sIncTypeName.equals(incTypeObj.inctype_name)) {
				sNewIncTypeName = sIncTypeName;
			}
		}
		sysIaeStruServ.saveIncType(dsIncType, incTypeObj.inctype_code,
				sIncTypeCode, incType.dsInccolumnToInc, Global.loginYear,
				sNewIncTypeName, lstPfsCode);

		dsIncType.applyUpdate();
		// add״̬��λ�����ӵĽڵ�
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			incType.ftreeIncType.expandTo("lvl_id", slvlId);
		if (bRefresh) { // ˢ����
			incType.ftreeIncType.reset();
			incType.ftreeIncType.expandTo("lvl_id", slvlId);
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
		String sLvlId = incType.ftxtfIncTypeCode.getValue().toString();
		String sIncTypeName = incType.ftxtfIncTypeName.getValue().toString()
				.trim();
		String sParId = incType.lvlIdRule.previous(sLvlId);// ��ø��������
		// �жϱ����Ƿ��޸�,�жϱ���Ƿ��޸�
		if ("mod".equals(incType.sSaveType.substring(0, 3))) {
			// �жϱ����Ƿ���д��ȷ
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(incType, "���벻��Ϊ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
			// �жϱ�����д���Ƿ�������
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(incType, "������������֣���������д��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}

			// ���뱻�޸ģ�Ҫ�жϱ���
			if (sLvlId != incTypeObj.lvl_id) {
				// �жϱ��볤����д�Ƿ���ȷ,�޸ĵ����Ҫ�ж�
				int iLevel = incType.lvlIdRule.levelOf(sLvlId); // ��õ�ǰ����ڴ�
				int iCount = incType.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(incType,
							"���벻��ȷ����������λһ���Ҳ������ڴγ���" + String.valueOf(iCount)
									+ "�� ����������д!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					incType.ftxtfIncTypeCode.setFocus();
					return false;
				}
				// �޸ı��븸�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽڵ㣬�����Ҷ�ӽڵ㣬�����޸�
				if (!"".equals(sParId) && sParId != null) {
					InfoPackage infoPackage = sysIaeStruServ
							.judgeIncTypeParExist(sParId, Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(incType, infoPackage
								.getsMessage(), "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
						incType.ftxtfIncTypeCode.setFocus();
						return false;
					}
				}
			}
			// �жϱ����Ƿ��ظ�,�����Ӻ��޸��������
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeCodeRepeat(
					sLvlId, Global.loginYear, incTypeObj.inctype_code, true);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
			// �жϽڵ㲻��ֱ�Ӹ�Ϊ�¼��ڵ㣬�����������
			ReplaceUnt replaceUnt = new ReplaceUnt();
			if (!replaceUnt.checkCode(sLvlId, dsIncType.fieldByName("lvl_id")
					.getOldValue().toString(), incType.lvlIdRule)) {
				JOptionPane.showMessageDialog(incType,
						"���ܽ��ڵ��޸ĳ��Լ����¼��ڵ�,��������д����!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				incType.ftxtfIncTypeCode.setFocus();
				return false;
			}
		}
		// �ж������Ƿ���д
		if ("".equals(sIncTypeName)) {
			JOptionPane.showMessageDialog(incType, "������Ŀ������Ʋ���Ϊ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			incType.ftxtfIncTypeCode.setFocus();
			return false;
		}
		// �ж�ͬ�������Ƿ��ظ�
		InfoPackage infoPackage;
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			infoPackage = sysIaeStruServ.judgeIncTypeNameRepeat(sIncTypeName,
					sParId, Global.loginYear, null, false);
		} else {
			infoPackage = sysIaeStruServ.judgeIncTypeNameRepeat(sIncTypeName,
					sParId, Global.loginYear, incTypeObj.inctype_code, true);
		}
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incType, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		String isInc = incType.frdoIsInc.getValue().toString();
		// �ж�������ԴΪ�ӷ�˰��ȡֵʱ���Ƿ���������������Ŀ�Ķ�Ӧ��ϵ
		if ("1".equals(isInc)) {
			incColNodes = incType.fpnlIncCol.ftreeIncColumn
					.getSelectedNodes(true);
			if (incColNodes.length == 0) {
				JOptionPane.showMessageDialog(incType, "��ѡ���Ӧ��������Ŀ!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} else if ("2".equals(isInc)) { // ��֧��Ԥ���ȡ��
			pfsNodes = incType.fpnlPfs.ftreePfs.getSelectedNodes(true);
			if (pfsNodes.length == 0) {
				JOptionPane.showMessageDialog(incType, "��ѡ���Ӧ��֧���ʽ���Դ!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			if (pfsNodes.length > 1) {
				JOptionPane.showMessageDialog(incType,
						"ֻ����֧���ʽ���Դһһ��Ӧ��ϵ,��ѡ��һ��֧���ʽ���Դ!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			// �ж�ѡ�е��ʽ���Դ�Ƿ�������������Ŀ�����˶�Ӧ��ϵ
			String bookmark = pfsNodes[0].getBookmark();
			DataSet dsPfs = incType.fpnlPfs.ftreePfs.getDataSet();
			if (dsPfs.gotoBookmark(bookmark)) {
				// ֧���ʽ���Դ����
				DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
				List lstPfsCode = SysUntPub.getLeafNodeCode(
						incType.fpnlPfs.ftreePfs, dsPayOutFS,
						IPayOutFS.PFS_CODE);
				// ����֧���ʽ���Դ����õ���Ӧ��������Ŀ����
				DataSet dsIncTypeToPfs = sysIaeStruServ.getIncWithPfsCode(
						lstPfsCode.get(0).toString(), Global.loginYear);
				// �õ�typeCodeֵ
				String sIncTypeCode = dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString();

				dsIncType.maskDataChange(true);
				String curBookmark = dsIncType.toogleBookmark();
				dsIncTypeToPfs.beforeFirst();
				while (dsIncTypeToPfs.next()) {
					String incTypeCodeTmp = dsIncTypeToPfs.fieldByName(
							IIncType.INCTYPE_CODE).getString();
					// �ж��Ƿ��Ǳ�����¼
					if (sIncTypeCode.equals(incTypeCodeTmp)) {
						continue;
					}
					if (dsIncType.locate(IIncType.INCTYPE_CODE, incTypeCodeTmp)) {
						String incTypeCode = incType.dsIncType.fieldByName(
								IIncType.INCTYPE_CODE).getString();
						int isIncTmp = sysIaeStruServ
								.getIncTypeIsInc(incTypeCode);
						// �ж��Ƿ�ӷ�˰����ȡ��
						if (isIncTmp == 2) {
							JOptionPane.showMessageDialog(incType, "\""
									+ dsIncType.fieldByName(ISysIaeStru.NAME)
											.getString()
									+ "\"������Ŀ�����ô�ѡ�е�֧���ʽ���Դ��ȡ������ѡ�������ʽ���Դ!",
									"��ʾ", JOptionPane.INFORMATION_MESSAGE);
							dsIncType.gotoBookmark(curBookmark);
							dsIncType.maskDataChange(false);
							return false;
						}
					}
				}
				dsIncType.gotoBookmark(curBookmark);
				dsIncType.maskDataChange(false);

			}

		}

		return true;
	}

	/**
	 * ��֯������Ŀ�����������Ŀ�Ķ�Ӧ��ϵ
	 * 
	 * @param dsInccolumnToInc
	 * @param ftreeIncColumn
	 * @param inctypeCode
	 * @throws Exception
	 */
	private DataSet orgInctypeToIncolumn(DataSet dsInccolumnToInc,
			CustomTree ftreeIncColumn, String inctypeCode) throws Exception {
		List lstCode = SysUntPub.getLeafNodeCode(ftreeIncColumn, ftreeIncColumn
				.getDataSet(), IIncColumn.INCCOL_CODE);
		if (lstCode == null || lstCode.size() == 0)
			dsInccolumnToInc = null;
		// ��Ӧ��ϵDataSet����δ�����������Ŀ��Ӧ��ϵ
		for (Iterator it = lstCode.iterator(); it.hasNext();) {
			String incColCode = it.next().toString();
			if (dsInccolumnToInc != null
					&& !dsInccolumnToInc.isEmpty()
					&& dsInccolumnToInc.locate(IIncColumn.INCCOL_CODE,
							incColCode)) {
				continue;
			}
			if (dsInccolumnToInc == null) {
				dsInccolumnToInc = DataSet.create();
			}
			dsInccolumnToInc.append();
			dsInccolumnToInc.fieldByName(IIncType.INCTYPE_CODE).setValue(
					inctypeCode);
			dsInccolumnToInc.fieldByName(IIncColumn.INCCOL_CODE).setValue(
					incColCode);
			dsInccolumnToInc.fieldByName(IIncType.TOLL_FILTER).setValue("");
		}
		// ��Ӧ��ϵDataSet��ȥ��δѡ�����ڵ�Ķ�Ӧ��ϵ
		if (dsInccolumnToInc == null || dsInccolumnToInc.isEmpty()) {
			return null;
		}
		List lstBookmark = null;
		dsInccolumnToInc.beforeFirst();
		while (dsInccolumnToInc.next()) {
			String incColCode = dsInccolumnToInc.fieldByName(
					IIncColumn.INCCOL_CODE).getString();
			if (lstCode.indexOf(incColCode) == -1) {
				if (lstBookmark == null)
					lstBookmark = new ArrayList();
				lstBookmark.add(dsInccolumnToInc.toogleBookmark());
			}
		}
		if (lstBookmark != null) {
			for (Iterator it = lstBookmark.iterator(); it.hasNext();) {
				String bookmark = it.next().toString();
				if (dsInccolumnToInc.gotoBookmark(bookmark)) {
					dsInccolumnToInc.delete();
				}
			}
		}
		dsInccolumnToInc.applyUpdate();
		return dsInccolumnToInc;
	}
}
