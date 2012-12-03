/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ��SelectionListerner
 * </p>
 * <p>
 * Description:������Ŀ��SelectionListerner
 * </p>
 *
 */
public class IncColumnTreeSelectionListener implements TreeSelectionListener {
	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	// ������ĿDataSet
	private DataSet dsIncCol = null;

	// ������Ŀ��Ӧ�շ���ĿDataSet
	private DataSet dsInccolumnToToll = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public IncColumnTreeSelectionListener(IncColumn incColumn) {
		this.incColumn = incColumn;
		this.dsIncCol = incColumn.dsIncCol;
		// �������ݿ�ӿ�
		this.sysIaeStruServ = incColumn.sysIaeStruServ;
	}

	/**
	 * ���÷���
	 */
	public void valueChanged(TreeSelectionEvent e) {
		try {
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(
					incColumn.dsIncCol, incColumn, incColumn.ftreeIncColumn);
			setActionStatus.setState(true, true);
			if (dsIncCol.isEmpty() || dsIncCol.eof() || dsIncCol.bof())
				return;

			if (dsIncCol.fieldByName("INCCOL_CODE").getValue() != null)
				incColumn.ftxtPriCode.setValue(dsIncCol.fieldByName(
						"INCCOL_CODE").getString());
			else
				incColumn.ftxtPriCode.setValue("");
			if (dsIncCol.fieldByName("LVL_ID").getValue() != null)
				incColumn.ftxtfIncColCode.setValue(dsIncCol.fieldByName(
						"LVL_ID").getString());
			else
				incColumn.ftxtfIncColCode.setValue("");
			if (dsIncCol.fieldByName("INCCOL_NAME").getValue() != null)
				incColumn.ftxtfIncColName.setValue(dsIncCol.fieldByName(
						"INCCOL_NAME").getString());
			else
				incColumn.ftxtfIncColName.setValue("");
			// ����Ŀ���������
			if (dsIncCol.fieldByName("SUM_FLAG").getValue() != null) {
				((JCheckBox) incColumn.fchkSumFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("SUM_FLAG").getInteger())));
			}
			// ����Ŀ����
			if (dsIncCol.fieldByName("IS_HIDE").getValue() != null) {
				((JCheckBox) incColumn.fchkHideFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("IS_HIDE").getInteger())));
			}
			//����ĿΪԤ������
			if (dsIncCol.fieldByName("N2").getValue() != null) {
				((JCheckBox) incColumn.fchkRPFlag.getEditor())
						.setSelected(Common.estimate(new Integer(dsIncCol
								.fieldByName("N2").getInteger())));
			}
			// ������Դmod by CL ,09,08,24
			if (dsIncCol.fieldByName("DATA_SOURCE").getValue() != null) {
				if(dsIncCol.fieldByName("DATA_SOURCE").getInteger() == 0)
				incColumn.frdoIncColDts.setValue(new String(dsIncCol
						.fieldByName("DATA_SOURCE").getString()));
				else
				{incColumn.frdoIncColDts.setValue(new String("1").toString());
				// incColumn.DSFlag = true;
				}
			}
			// ��ʾ��ʽ
			incColumn.fcbxSFormate.setValue(dsIncCol.fieldByName(
					"DISPLAY_FORMAT").getString());

			// ���㹫ʽ
			if (dsIncCol.fieldByName("FORMULA_DET").getString() != "") {
				incColumn.ftxtaIncColFormula
						.setValue(PubInterfaceStub.getMethod()
								.replaceTextEx(
										dsIncCol.fieldByName("FORMULA_DET")
												.getString(), 0,
										"fb_iae_inccolumn", "IncCol_Ename",
										"IncCol_Fname",
										"set_year =" + Global.loginYear));
			} else {
				incColumn.ftxtaIncColFormula.setValue("");
			}
			// �������ȼ�
			if (dsIncCol.fieldByName("CALC_PRI").getValue() != null)
				incColumn.jspIncColCalcPRI.setValue(new Integer(dsIncCol
						.fieldByName("CALC_PRI").getInteger()));

			// ����������Դ����ʾ��Ϣ   
			if (dsIncCol.fieldByName("DATA_SOURCE") != null) {// ¼��
			//if (dsIncCol.fieldByName("DATA_SOURCE").getInteger() == 0) {// ¼��
				// �õ�������Ŀ��Ӧ�շ���Ŀ��Ϣ
				dsInccolumnToToll = sysIaeStruServ.getInccolumnToToll(
						Global.loginYear, dsIncCol.fieldByName("INCCOL_CODE")
								.getValue().toString());
				// ����������Ŀ���շ���Ŀ��Ӧ��ϵ�����շ���Ŀ�ڵ�ѡ��״̬
				SetSelectTree.setIsCheck(incColumn.ftreIncomeSubItem,
						dsInccolumnToToll, "toll_code");
			} else {
				SetSelectTree.setIsNoCheck(incColumn.ftreIncomeSubItem);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incColumn, "��ʾ������Ŀ��ϸ��Ϣ�������󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

}
