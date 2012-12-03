/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀɾ��������
 * </p>
 * <p>
 * Description:������Ŀɾ��������
 * </p>

 */
public class IncColumnDel {
	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	// ������ĿDataSet
	private DataSet dsIncCol = null;

	// ������Ŀ����
	private IncColumnObj incColumnObj = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public IncColumnDel(IncColumn incColumn) {
		// ��������Ŀ����ͻ�����������
		this.incColumn = incColumn;
		// ����������ĿDataSet
		this.dsIncCol = incColumn.dsIncCol;
		// ����������Ŀ����
		this.incColumnObj = incColumn.incColumnObj;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * ɾ��������Ŀ��������
	 * 
	 * @throws Exception
	 */
	public boolean delete() throws Exception {
		// ��ɾ���ڵ�ĸ��ڵ�ֻ��һ���ӽڵ��ǣ����游�ڵ����
		String sIncColParCode = null;

		// �ж��Ƿ���ĩ�ڵ㣬����ĩ�ڵ㣬������ɾ��
		if (dsIncCol.fieldByName("End_flag").getValue() != null
				&& dsIncCol.fieldByName("End_flag").getInteger() == 0) {
			JOptionPane.showMessageDialog(incColumn, "������Ŀ��������Ŀ,����ɾ������Ŀ!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// ȡ��������ĿӢ����,�ж��Ƿ����ɾ��
		String sEngName = dsIncCol.fieldByName("IncCol_Ename").getString();
		String sIncColCode = dsIncCol.fieldByName("IncCol_Code").getString();
		String sIncColName = dsIncCol.fieldByName(IIncColumn.INCCOL_NAME)
				.getString();
		String sIncColEname = dsIncCol.fieldByName(IIncColumn.INCCOL_ENAME)
				.getString();
		InfoPackage infoPackage = sysIaeStruServ.judgeIncColEnableDel(sEngName,
				sIncColCode, Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(incColumn, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// ��ʾ�Ƿ�ȷ��ɾ��
		if (JOptionPane.showConfirmDialog(incColumn, "���Ƿ�ȷ��Ҫɾ��������¼?", "��ʾ",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
			return false;
		}

		// �ж�ѡ�еĽڵ�ĸ��ڵ��Ƿ����
		MyTreeNode node = incColumn.ftreeIncColumn
				.getSelectedNode();
		MyTreeNode parentNode = (MyTreeNode) node.getParent();
		// �ж�ѡ�еĽڵ��ǲ��Ǹ��ڵ�
		if (parentNode != (MyTreeNode) incColumn.ftreeIncColumn.getRoot()) {
			// �ж��м����ֵܣ����ֻ��һ���ڵ㣬�౾�ڵĲ�����Ϣ�������ڵ�
			if (parentNode.getChildCount() == 1) {
				// ��λ�����ڵ�
				dsIncCol.gotoBookmark(parentNode.getBookmark());
				// ��������Ϣ���ø����ڵ�
				dsIncCol.maskDataChange(true);
				// ����������ĿDataSetΪ�༭״̬
				dsIncCol.edit();

				sIncColParCode = dsIncCol.fieldByName("INCCOL_CODE")
						.getString();

				// ����������Ŀ��Ӧ�ֶ�
				dsIncCol.fieldByName("INCCOL_ENAME").setValue(
						incColumnObj.INCCOL_ENAME);
				// ����������Դ
				dsIncCol.fieldByName("DATA_SOURCE").setValue(
						new Integer(incColumnObj.DATA_SOURCE));
				// ���ü��㹫ʽ����
				dsIncCol.fieldByName("FORMULA_DET").setValue(
						incColumnObj.FORMULA_DET);
				// ���ü������ȼ�
				dsIncCol.fieldByName("CALC_PRI").setValue(
						new Integer(incColumnObj.CALC_PRI));
				// ������Ŀ�Ƿ���Ҫ�������
				dsIncCol.fieldByName("SUM_FLAG").setValue(
						new Integer(incColumnObj.SUM_FLAG));
				// ���� ĩ����־
				dsIncCol.fieldByName("end_flag").setValue(
						new Integer(incColumnObj.END_FLAG));
				// ���� ��ʾ��ʽ
				dsIncCol.fieldByName("DISPLAY_FORMAT").setValue(
						incColumnObj.DISPLAY_FORMAT);
				// ���ñ༭��ʽ
				dsIncCol.fieldByName("EDIT_FORMAT").setValue(
						incColumnObj.EDIT_FORMAT);
				// �����Ƿ������Ƿ�����
				dsIncCol.fieldByName("IS_HIDE").setValue(
						new Integer(incColumnObj.IS_HIDE));
				dsIncCol.maskDataChange(false);
				// ��λ�ر��ڵ�
				dsIncCol.gotoBookmark(node.getBookmark());
			}
		}
		// ɾ�����ڵ�
		dsIncCol.delete();
		// �ύ���ݿ�
		sysIaeStruServ.delIncCol(dsIncCol, sIncColParCode,
				incColumnObj.INCCOL_CODE, Global.loginYear, sIncColName,
				sIncColEname);
		dsIncCol.applyUpdate();
		// ˢ��ϵͳ�����ֵ���Ϣ
		if (!SysUntPub.synDict(IIncColumn.INCCOL_TABLE)) {
			JOptionPane.showMessageDialog(incColumn, "ˢ��ϵͳ�����ֵ���Ϣ��������!", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
}
