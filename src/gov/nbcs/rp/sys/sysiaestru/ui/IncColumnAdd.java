/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ���Ӳ�����
 * </p>
 * <p>
 * Description:������Ŀ���Ӳ�����
 * </p>

 */
public class IncColumnAdd {
	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	// ������Ŀ����
	private IncColumnObj incColumnObj = null;

	// ������ĿDataSet
	private DataSet dsIncCol = null;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	// �����������ݽӿ�
	private IPubInterface iPubInterface = null;

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public IncColumnAdd(IncColumn incColumn) {
		// ��������Ŀ����ͻ�����������
		this.incColumn = incColumn;
		// ����������Ŀ����
		this.incColumnObj = incColumn.incColumnObj;
		// ����������ĿDataSet
		this.dsIncCol = incColumn.dsIncCol;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
		// �����������ݽӿ�
		this.iPubInterface = incColumn.iPubInterface;
	}

	/**
	 * ����������Ŀ��������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {

		// ȡ������Ԥ��������ٸ�������Ŀ��
		int MaxFieldNum = sysIaeStruServ.getIncColIValue(Global.loginYear);

		// ȡ������Ԥ���յ��ֶ�����
		String sEname = iPubInterface.assignNewCol("IncCol_EName", "F",
				MaxFieldNum, "fb_iae_inccolumn", Global.loginYear, "");
		if ("".equals(sEname)) {
			// ����ʧ�ܣ���ʾ������Ϣ
			JOptionPane.showMessageDialog(incColumn, "������Ŀ��Ŀ�������÷�Χ��", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// ����Զ����ɵı���
		String sInccolCode = iPubInterface.getMaxCode("fb_iae_inccolumn",
				"IncCol_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);
		// �жϱ����Ƿ��óɹ�
		if (sInccolCode == null) {
			JOptionPane.showMessageDialog(incColumn, "���������������ʧ�ܣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// ����Զ����ɵĲ����
		String sLvlIdCode;
		// �жϸ���Ĳ�����Ƿ�Ϊ��
		if ("".equals(incColumnObj.LVL_ID)) {// ����Ĳ����Ϊ��
			// �ж�������ĿDataSet�Ƿ�Ϊ��(���Ƿ����ӵĵ�һ��������Ŀ�ڵ㣩
			if (dsIncCol.isEmpty()) {// ������ĿDataSetΪ��
				// ���ò����
				sLvlIdCode = ISysIaeStru.ROOT_CODE;
			} else {// ������ĿDataSetΪ��,���ò����
				// ���ݹ����ڶ�Ӧ�����ݱ��л�ȡ�µĽڵ�code
				sLvlIdCode = iPubInterface.getNodeID("fb_iae_inccolumn",
						"LVL_ID", incColumnObj.LVL_ID, "set_Year = "
								+ Global.loginYear + " and par_id is null",
						incColumn.lvlIdRule);
			}
		} else {// ����Ĳ���벻Ϊ��
			// ���ݹ����ڶ�Ӧ�����ݱ��л�ȡ�µĽڵ�code
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inccolumn", "LVL_ID",
					incColumnObj.LVL_ID, "set_Year = " + Global.loginYear
							+ " and par_id ='" + incColumnObj.LVL_ID + "'",
					incColumn.lvlIdRule);
		}
		// �жϲ�����Ƿ��óɹ�
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(incColumn, "�޷�������һ�����룬�ѵ��������Ӽ��Σ�",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �жϱ��볤����д��"9999"
		int iLevel = incColumn.lvlIdRule.levelOf(sLvlIdCode); // ��õ�ǰ����ڴ�
		if (iLevel < 0) {
			JOptionPane.showMessageDialog(incColumn, "�޷����ӣ��ѵ�������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// �ж����ӣ��ǲ������ӵ�һ��Ҷ���ӣ�������ӵ�һ��Ҷ�ڵ㣬�����ڵ����Ϣ�����ӽڵ�
		if (incColumnObj.END_FLAG == 1)
			incColumn.sSaveType = "addfirstson";
		else
			incColumn.sSaveType = "add";
		dsIncCol.append();
		// �ж�����ѡ�еĽڵ��Ƿ���Ҷ�ڵ�,������Ҷ�ڵ㣬��������Ϣ������һ��Ҷ�ڵ�
		dsIncCol.maskDataChange(true);
		incColumn.ftxtPriCode.setValue(sInccolCode); // ����
		incColumn.ftxtfIncColCode.setValue(sLvlIdCode);// ��α���
		incColumn.ftxtfIncColName.setValue("");// ����
		if ("addfirstson".equals(incColumn.sSaveType)) {// ���ӵ�һ���ӽڵ�
			// Ӣ������
			dsIncCol.fieldByName("inccol_ename").setValue(
					incColumnObj.INCCOL_ENAME);
		} else {// �����ӽڵ�
			((JCheckBox) incColumn.fchkSumFlag.getEditor()).setSelected(false);
			((JCheckBox) incColumn.fchkHideFlag.getEditor()).setSelected(false);
			//mod by CL ,09,08,24
			((JCheckBox) incColumn.fchkRPFlag.getEditor()).setSelected(false);
			incColumn.frdoIncColDts.setValue("0");
			// ��ʾ��ʽ
			incColumn.fcbxSFormate.setValue("");
			// ���㹫ʽ
			incColumn.ftxtaIncColFormula.setValue("");
			// �������ȼ�
			incColumn.jspIncColCalcPRI.setValue(new Integer(0));
			// Ӣ������
			dsIncCol.fieldByName("INCCOL_ENAME").setValue(sEname);
			SetSelectTree.setIsNoCheck(incColumn.ftreIncomeSubItem);
		}

		dsIncCol.fieldByName("INCCOL_CODE").setValue(sInccolCode);
		dsIncCol.fieldByName("END_FLAG").setValue(new Integer(1));
		dsIncCol.fieldByName("set_Year").setValue(Global.loginYear);
		dsIncCol.fieldByName("RG_CODE").setValue(Global.getCurrRegion());
		dsIncCol.maskDataChange(false);
		incColumn.ftxtfIncColName.setFocus();
		return true;
	}
}
