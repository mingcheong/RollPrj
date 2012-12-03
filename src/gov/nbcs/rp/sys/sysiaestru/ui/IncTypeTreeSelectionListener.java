/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ�����SelectionListerner
 * </p>
 * <p>
 * Description:������Ŀ�����SelectionListerner
 * </p>
 * <p>
 *
 */
public class IncTypeTreeSelectionListener implements TreeSelectionListener {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 */
	public IncTypeTreeSelectionListener(IncType incType) {
		this.incType = incType;
		this.sysIaeStruServ = incType.sysIaeStruServ;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(
					incType.dsIncType, incType, incType.ftreeIncType);
			setActionStatus.setState(true, true);
			if (incType.dsIncType.isEmpty() || incType.dsIncType.bof()
					|| incType.dsIncType.eof())
				return;
			// ����
			if (incType.dsIncType.fieldByName(IIncType.INCTYPE_CODE).getValue() != null)
				incType.ftxtPriCode.setValue(incType.dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString());
			else
				incType.ftxtPriCode.setValue("");
			// ����
			if (incType.dsIncType.fieldByName(IIncType.LVL_ID).getString() != "")
				incType.ftxtfIncTypeCode.setValue(incType.dsIncType
						.fieldByName(IIncType.LVL_ID).getString());
			// ����
			incType.ftxtfIncTypeName.setValue(incType.dsIncType.fieldByName(
					IIncType.INCTYPE_NAME).getString());

			// ����
			incType.flstIncTypeKind.setMaskValueChange(true);
			if ("".equals(incType.dsIncType.fieldByName(IIncType.STD_TYPE_CODE)
					.getString())) {
				incType.flstIncTypeKind.setSelectedIndex(0);
			} else
				incType.flstIncTypeKind.setSelectedValue(incType.dsIncType
						.fieldByName(IIncType.STD_TYPE_CODE).getString(), true);
			incType.flstIncTypeKind.setMaskValueChange(false);

			// �Ƿ���������
			if (incType.dsIncType.fieldByName(IIncType.IS_SUM).getValue() == null) {
				((JCheckBox) incType.fchkIsMid.getEditor()).setSelected(false);
			} else {
				if (incType.dsIncType.fieldByName(IIncType.IS_SUM).getValue() != null) {
					((JCheckBox) incType.fchkIsMid.getEditor())
							.setSelected(!Common.estimate(new Integer(
									incType.dsIncType.fieldByName(
											IIncType.IS_SUM).getInteger())));
				}
			}

			// ������Դ
			// Object isInc = incType.dsIncType.fieldByName(IIncType.IS_INC)
			// .getValue();
			// if (isInc == null) {
			// isInc = "0";
			// }
			// �������´ӿ��л�ȡ���ʽ���Դ����������޸ģ���ɿ��������ʾ��һ��
			String incTypeCode = incType.dsIncType.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			int isInc = sysIaeStruServ.getIncTypeIsInc(incTypeCode);
			incType.dsIncType.fieldByName(IIncType.IS_INC).setValue(
					String.valueOf(isInc));

			incType.frdoIsInc.setValue(String.valueOf(isInc));

			switch (isInc) {
			case 1: // �ӷ�˰��ȡ��
				// �õ�������Ŀ������
				String sIncTypeCode = incType.dsIncType.fieldByName(
						IIncType.INCTYPE_CODE).getString();

				// ���������Ŀ�����������Ŀ��Ӧ��ϵ
				incType.dsInccolumnToInc = sysIaeStruServ.getInctypeToIncolumn(
						Global.loginYear, sIncTypeCode);
				// ��֯��List
				List lstIncColCode = SysUntPub.getFieldValueOrgList(
						incType.dsInccolumnToInc, IIncColumn.INCCOL_CODE);
				// ����INCCOL_CODEֵ�õ���Ӧ��lvl_idֵ
				DataSet dsIncCol = incType.fpnlIncCol.ftreeIncColumn
						.getDataSet();
				String[] slvlArr = SysUntPub.getIdWithCode(lstIncColCode,
						dsIncCol, IIncColumn.INCCOL_CODE, IIncColumn.LVL_ID);

				// ��ʾ������Ŀ���ڵ�ѡ��״̬
				SetSelectTree.setIsCheck(incType.fpnlIncCol.ftreeIncColumn,
						slvlArr);
				// ��λ����һ����ѡ�ڵ�
				if (slvlArr != null && slvlArr.length > 0) {
					TreePath path = new TreePath(
							incType.fpnlIncCol.ftreeIncColumn.getRoot());
					incType.fpnlIncCol.ftreeIncColumn.expandPath(path);
					incType.fpnlIncCol.ftreeIncColumn.getSelectionModel()
							.setSelectionPath(path);
					incType.fpnlIncCol.ftreeIncColumn.scrollPathToVisible(path);
					incType.fpnlIncCol.ftreeIncColumn.expandTo(
							IIncColumn.LVL_ID, slvlArr[0]);

				}
				break;
			case 2: // ��֧��Ԥ���ȡ��
				showIncTypeToPfs(incType);
				break;
			default: // ¼��
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incType, "��ʾ��ϸ��Ϣ�������󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ��ʾ������Ŀ��֧���ʽ���Դ�Ķ�Ӧ��ϵ
	 * 
	 * @param incType
	 * @throws Exception
	 */
	public static void showIncTypeToPfs(IncType incType) throws Exception {
		if (incType.dsIncType == null || incType.dsIncType.isEmpty()
				|| incType.dsIncType.bof() || incType.dsIncType.eof()) {// ��ʾ֧���ʽ���Դ�ڵ�ѡ��״̬
			SetSelectTree.setIsNoCheck(incType.fpnlPfs.ftreePfs);
		} else {
			// �õ�������Ŀ������
			String sIncTypeCode = incType.dsIncType.fieldByName(
					IIncType.INCTYPE_CODE).getString();
			// ���������Ŀ�����֧���ʽ���Դ��Ӧ��ϵ
			DataSet dsPfsToIncitem = incType.sysIaeStruServ.getPfsToIncCode(
					sIncTypeCode, Global.loginYear);
			// ��֯��List
			List lstPfsCode = SysUntPub.getFieldValueOrgList(dsPfsToIncitem,
					IPayOutFS.PFS_CODE);
			// ����pfs_codeֵ�õ���Ӧ��lvl_idֵ
			DataSet dsPayOutFS = incType.fpnlPfs.ftreePfs.getDataSet();
			String[] slvlArr = SysUntPub.getIdWithCode(lstPfsCode, dsPayOutFS,
					IPayOutFS.PFS_CODE, IPayOutFS.LVL_ID);
			// ��ʾ֧���ʽ���Դ�ڵ�ѡ��״̬
			SetSelectTree.setIsCheck(incType.fpnlPfs.ftreePfs, slvlArr);
		}
	}

}
