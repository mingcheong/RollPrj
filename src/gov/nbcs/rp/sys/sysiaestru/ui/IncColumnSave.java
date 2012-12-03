/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ���������
 * </p>
 * <p>
 * Description:������Ŀ���������
 * </p>
 * <p>

 */
public class IncColumnSave {
	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	// ������ĿDataSet
	private DataSet dsIncCol = null;

	// �շ���Ŀ��
	private CustomTree ftreIncomeSubItem = null;

	// ������Ŀ����
	private IncColumnObj incColumnObj = null;

	// ��������
	private String sSaveType = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	public IncColumnSave(IncColumn incColumn) {

		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		this.ftreIncomeSubItem = incColumn.ftreIncomeSubItem;
		this.incColumnObj = incColumn.incColumnObj;
		this.sSaveType = incColumn.sSaveType;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public void save() throws Exception {
		// �õ��շ���Ŀѡ�еĽڵ����
		ArrayList lstTollCode = null;

		// �Ƿ����ӵĵ�ǰ�ڵ�ĵ�һ���ڵ�
		boolean bAddFirstNode = false;

		boolean bRefresh = false;
		// �ж���Ϣ��д�Ƿ�����
		if (!judgeFillInfo())
			return;

		// �����
		String slvlId = incColumn.ftxtfIncColCode.getValue().toString();
		// ������Ŀ����
		String sIncColName = incColumn.ftxtfIncColName.getValue().toString();
		// �жϱ����Ƿ��޸�,�жϱ���Ƿ��޸�
		String sParId = null;

		dsIncCol.maskDataChange(true);
		// �жϱ��������Ƿ����޸�
		if ("mod".equals(sSaveType.substring(0, 3))) {
			// �õ���ǰ��д�����ĸ��ڱ���
			sParId = incColumn.lvlIdRule.previous(slvlId);
			if (sParId == null) {
				sParId = "";
			}

			// ��������ȫ��������
			ReplaceUnt replaceUnt = new ReplaceUnt();
			String sNewFName = "";
			// �жϸ������Ƿ�Ϊ��
			if (!"".equals(sParId)) {// �����벻Ϊ��
				// ���ݸ�����ֵ�õ����ڵ����Ϣ
				List lstValue = replaceUnt.getParNodeInfo(sParId,
						new String[] { IIncColumn.INCCOL_FNAME }, dsIncCol);
				if (lstValue != null)
					sNewFName = lstValue.get(0).toString();
			}
			// ��֯������Ŀȫ����ֵ
			if (sNewFName != null && !"".equals(sNewFName)) {
				sNewFName = sNewFName + UntPub.PARTITION_TAG + sIncColName;
			} else {
				sNewFName = sIncColName;
			}

			// �жϲ�����Ƿ����ı�
			if (!slvlId.equals(incColumnObj.LVL_ID)) {// ����뷢���ı�
				// �õ�ѡ�����ڵ㼰���ӽڵ��bookmark�б�
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incColumn.ftreeIncColumn);
				// �õ���ɾ���Ľڵ��б�
				List lstDelBookmark = replaceUnt.getParNode(sParId,
						incColumn.ftreeIncColumn, dsIncCol);

				// ����뷢���ı䣬������Ŀȫ�����»��
				replaceUnt.ReplaceFname(lstBookmark, dsIncCol, sNewFName,
						dsIncCol.fieldByName("inccol_fname").getOldValue()
								.toString(), IIncColumn.INCCOL_FNAME);
				// �滻lvl_id,par_idֵ,�ı��ӽڵ��lvl_id
				replaceUnt.ReplaceLvlPar(lstBookmark, dsIncCol, slvlId,
						incColumnObj.LVL_ID, incColumn.lvlIdRule);
				// ɾ���ڵ��б��нڵ�
				replaceUnt.delParNode(lstDelBookmark, dsIncCol);
				bRefresh = true;
			} else if (!sIncColName.equals(incColumnObj.INCCOL_NAME)) {// ����δ�ı��ж������Ƿ�ı�
				// �õ�ѡ�����ڵ㼰���ӽڵ��bookmark�б�
				List lstBookmark = replaceUnt
						.getSelectTreeNodeBookmark(incColumn.ftreeIncColumn);
				// ������Ŀȫ�����»��
				replaceUnt.ReplaceFname(lstBookmark, dsIncCol, sNewFName,
						dsIncCol.fieldByName(IIncColumn.INCCOL_FNAME)
								.getOldValue().toString(),
						IIncColumn.INCCOL_FNAME);
			}
		}

		// �޸ĵĽڵ���Ҷ�ڵ㣬ֻ���޸����ƺͱ���,����ֻ�ڵ��fnameֵ
		if ("modname".equals(sSaveType)) {
			dsIncCol.fieldByName("INCCOL_NAME").setValue(sIncColName);
		}
		// �޸ĵĽڵ�ΪҶ�ڵ㣬ֻ���޸ĸ�ʽ�ͱ���
		if ("modformate".equals(sSaveType)) {
			dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
					incColumn.fcbxSFormate.getValue().toString());
		}
		// ��������Ϊadd,addfirstson,mod,��ֵ����������ĿDataSet
		if ("add".equals(sSaveType) || "mod".equals(sSaveType)
				|| "addfirstson".equals(sSaveType)) {
			// ����fname
			String sIncColNameNew = null;
			if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
				if ("".equals(incColumnObj.INCCOL_FNAME)) // û�ϼ���fname���䱾��
				{
					sIncColNameNew = sIncColName;
				} else { // ���ϼ���fname= �ϼ�fname+��д��name
					sIncColNameNew = incColumnObj.INCCOL_FNAME
							+ UntPub.PARTITION_TAG + sIncColName;
				}
				dsIncCol.fieldByName("inccol_fname").setValue(sIncColNameNew);
			} else {// mod,���������ж�

			}
			// ����
			dsIncCol.fieldByName("inccol_name").setValue(
					incColumn.ftxtfIncColName.getValue().toString());
			// ����Ŀ�������
			dsIncCol.fieldByName("SUM_FLAG").setValue(
					new Integer("false".equals(incColumn.fchkSumFlag.getValue()
							.toString()) ? 0 : 1));
			// ����Ŀ����
			dsIncCol.fieldByName("IS_HIDE").setValue(
					new Integer("false".equals(incColumn.fchkHideFlag
							.getValue().toString()) ? 0 : 1));
			// ����Ŀ��Ԥ������,mod by CL ,09,08,24
			dsIncCol.fieldByName("N2").setValue(
					new Integer("false".equals(incColumn.fchkRPFlag.getValue()
							.toString()) ? 0 : 1));

			dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
					incColumn.fcbxSFormate.getValue().toString());

			// ����������Դ���治ͬ��Ϣ
			if ("1".equals(incColumn.frdoIncColDts.getValue().toString())) {// ����
				// ���㹫ʽ
				String sForumla = incColumn.ftxtaIncColFormula.getValue()
						.toString();
				if (!"".equals(sForumla)) {
					dsIncCol.fieldByName("FORMULA_DET").setValue(
							PubInterfaceStub.getMethod().replaceTextEx(
									sForumla, 0, IIncColumn.INCCOL_TABLE,
									IIncColumn.INCCOL_FNAME,
									IIncColumn.INCCOL_ENAME,
									"set_year =" + Global.loginYear));
				} else {
					dsIncCol.fieldByName("FORMULA_DET").setValue("");
				}
				// �������ȼ�
				dsIncCol.fieldByName("CALC_PRI").setValue(
						incColumn.jspIncColCalcPRI.getValue().toString());
			}

			if ("addfirstson".equals(sSaveType)) { // ���ӵ�һ���ӽڵ㣬���ĸ��ڵ�Ĳ�����Ϣ
				bAddFirstNode = true;
				String sBookmark = dsIncCol.toogleBookmark();
				MyTreeNode node = (MyTreeNode) incColumn.ftreeIncColumn
						.getSelectedNode();
				if (node != null) {
					dsIncCol.gotoBookmark(node.getBookmark());
					dsIncCol.fieldByName("inccol_ename").setValue("");
					dsIncCol.fieldByName("data_source")
							.setValue(new Integer(0));
					dsIncCol.fieldByName("formula_det").setValue("");
					dsIncCol.fieldByName("calc_pri").setValue(new Integer(0));
					dsIncCol.fieldByName("sum_flag").setValue(new Integer(0));
					dsIncCol.fieldByName("is_hide").setValue(new Integer(0));
					dsIncCol.fieldByName("end_flag").setValue(new Integer(0));
					dsIncCol.fieldByName("display_format").setValue("");
					dsIncCol.fieldByName("edit_format").setValue("");
					// ��λ�ر��ڵ�
					dsIncCol.gotoBookmark(sBookmark);
				}
			}
		}

		// ������Դ mod by Cl,09,08,24
		lstTollCode = SysUntPub.getCodeList(ftreIncomeSubItem);
		if ("0".equals(incColumn.frdoIncColDts.getValue().toString())) {
			dsIncCol.fieldByName("DATA_SOURCE").setValue(
					incColumn.frdoIncColDts.getValue().toString());
		} else {
			if (lstTollCode != null) {
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						new String("2").toString());
			} else {
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						incColumn.frdoIncColDts.getValue().toString());
			}
		}
		if ("0".equals(incColumn.frdoIncColDts.getValue().toString())) { // ������Դ:¼��
			// ���㹫ʽ
			dsIncCol.fieldByName("FORMULA_DET").setValue("");
			// �������ȼ�
			dsIncCol.fieldByName("CALC_PRI").setValue("0");
			// �õ��շ���Ŀѡ�еĽڵ����
			// lstTollCode = SysUntPub.getCodeList(ftreIncomeSubItem);
		}
		// mod by CL,09,08,24

		dsIncCol.fieldByName("lvl_id").setValue(slvlId);
		if ("addfirstson".equals(sSaveType) || "add".equals(sSaveType))
			dsIncCol.fieldByName("par_id").setValue(incColumnObj.LVL_ID);
		dsIncCol.fieldByName("name").setValue(slvlId + " " + sIncColName);

		dsIncCol.maskDataChange(false);

		// �õ�ǰ�༭�ڵ������
		String sIncColCode = dsIncCol.fieldByName("INCCOL_CODE").getString();
		// �ύ����
		sysIaeStruServ.saveIncCol(dsIncCol, incColumnObj.INCCOL_CODE,
				sIncColCode, bAddFirstNode, lstTollCode, Global.loginYear);
		dsIncCol.applyUpdate();
		incColumn.ftreeIncColumn.reset();
		// add״̬��λ�����ӵĽڵ�
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType))
			incColumn.ftreeIncColumn.expandTo("lvl_id", slvlId);
		if (bRefresh) { // ˢ����
			incColumn.ftreeIncColumn.reset();
			incColumn.ftreeIncColumn.expandTo("lvl_id", slvlId);
		}
		// ˢ��ϵͳ�����ֵ���Ϣ
		if (!SysUntPub.synDict(IIncColumn.INCCOL_TABLE)) {
			JOptionPane.showMessageDialog(incColumn, "ˢ��ϵͳ�����ֵ���Ϣ��������!", "��ʾ",
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
		InfoPackage infoPackage;
		String sLvlId = incColumn.ftxtfIncColCode.getValue().toString();
		String sIncColName = incColumn.ftxtfIncColName.getValue().toString()
				.trim();
		String sParId = incColumn.lvlIdRule.previous(sLvlId); // ����ϼ�����
		// �жϱ����Ƿ���д��ȷ
		if ("mod".equals(sSaveType.substring(0, 3))
				&& sLvlId != incColumnObj.LVL_ID) {
			if ("".equals(sLvlId)) {
				JOptionPane.showMessageDialog(incColumn, "���벻��Ϊ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				incColumn.ftxtfIncColCode.setFocus();
				return false;
			}
			// �жϱ�����д���Ƿ�������
			if (!sLvlId.matches("\\d+")) {
				JOptionPane.showMessageDialog(incColumn, "������������֣���������д��",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				incColumn.ftxtfIncColCode.setFocus();
				return false;
			}

			// ���뱻�޸ģ�Ҫ�жϱ���
			if (sLvlId != incColumnObj.LVL_ID) {
				// �жϱ��볤����д�Ƿ���ȷ,�޸ĵ����Ҫ�ж�
				int iLevel = incColumn.lvlIdRule.levelOf(sLvlId); // ��õ�ǰ����ڴ�
				int iCount = incColumn.lvlIdRule.originRules().size();
				if (iLevel < 0) {
					JOptionPane.showMessageDialog(incColumn,
							"���벻��ȷ����������λһ���Ҳ������ڴγ���" + String.valueOf(iCount)
									+ "�� ����������д!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
				// �ж����������Ҫ>=1001
				String sRootCode = incColumn.lvlIdRule.rootCode(sLvlId); // ���ݵ�ǰ����������ı���ֵ
				if (sRootCode.compareTo(ISysIaeStru.ROOT_CODE) < 0) {
					JOptionPane.showMessageDialog(incColumn,
							"����������1000,��������д!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
				// �޸ı��븸�����Ƿ����,�Ҳ��ǲ�Ҷ�ӽڵ㣬�����Ҷ�ӽڵ㣬�����޸�
				if (!"".equals(sParId) && sParId != null) {
					infoPackage = sysIaeStruServ.judgeIncColParExist(sParId,
							Global.loginYear);
					if (!infoPackage.getSuccess()) {
						JOptionPane.showMessageDialog(incColumn, infoPackage
								.getsMessage(), "��ʾ",
								JOptionPane.INFORMATION_MESSAGE);
						incColumn.ftxtfIncColCode.setFocus();
						return false;
					}
				}
				// �жϱ����Ƿ��ظ�,�����Ӻ��޸��������,���ӱ����Զ�����
				infoPackage = sysIaeStruServ.judgeIncColCodeRepeat(sLvlId,
						Global.loginYear, incColumnObj.INCCOL_CODE, true);
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(incColumn, infoPackage
							.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}

				// �жϽڵ㲻��ֱ�Ӹ�Ϊ�¼��ڵ㣬�����������
				ReplaceUnt replaceUnt = new ReplaceUnt();
				if (!replaceUnt.checkCode(sLvlId, dsIncCol
						.fieldByName("lvl_id").getOldValue().toString(),
						incColumn.lvlIdRule)) {
					JOptionPane.showMessageDialog(incColumn,
							"���ܽ��ڵ��޸ĳ��Լ����¼��ڵ�,��������д����!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					incColumn.ftxtfIncColCode.setFocus();
					return false;
				}
			}
		}

		// �ж������Ƿ���д
		if ("".equals(sIncColName)) {
			JOptionPane.showMessageDialog(incColumn, "������Ŀ���Ʋ���Ϊ��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			incColumn.ftxtfIncColName.setFocus();
			return false;
		}
		// �ж�ͬ�������Ƿ��ظ�
		if ("add".equals(sSaveType) || "addfirstson".equals(sSaveType)) {
			infoPackage = sysIaeStruServ.judgeIncColNameRepeat(sIncColName,
					sParId, Global.loginYear, null, false);
		} else {
			infoPackage = sysIaeStruServ.judgeIncColNameRepeat(sIncColName,
					sParId, Global.loginYear, incColumnObj.INCCOL_CODE, true);
		}
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incColumn, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж�������Դ�Ǽ���ı������ü��㹫ʽ
		if ("1".equals(incColumn.frdoIncColDts.getValue().toString())
				&& ""
						.equals(incColumn.ftxtaIncColFormula.getValue()
								.toString())) {
			JOptionPane.showMessageDialog(incColumn, "������Դ�Ǽ���ı������ü��㹫ʽ!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �жϸ���ĿΪԤ���Ƚ�ֻ����һ��
		if (Common.estimate(incColumn.fchkRPFlag.getValue())) {
			String incColCode = incColumn.ftxtPriCode.getValue().toString();
			DataSet dsIncColTmp = sysIaeStruServ.judgeIncColYLBLExist(
					Global.loginYear, incColCode);
			if (dsIncColTmp.getRecordCount() > 0) {
				String info = "";
				dsIncColTmp.beforeFirst();
				while (dsIncColTmp.next()) {
					if (!Common.isNullStr(info)) {
						info += ",";
					}
					info += dsIncColTmp.fieldByName(IIncColumn.INCCOL_CODE)
							.getString()
							+ dsIncColTmp.fieldByName(IIncColumn.INCCOL_NAME)
									.getString();
				}
				JOptionPane.showMessageDialog(incColumn, "Ԥ������ֻ����һ����Ŀ����,"
						+ info + "������!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
