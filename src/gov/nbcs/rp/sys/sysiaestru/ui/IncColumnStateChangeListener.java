/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:������ĿDataSet״̬�ı��¼�
 * </p>
 * <p>
 * Description:������ĿDataSet״̬�ı��¼�,����DataSet״̬���ƽ���͹���������Ƿ�ɲ���
 * </p>

 */
public class IncColumnStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// ������Ŀ����ͻ�����������
	private IncColumn incColumn = null;

	/**
	 * ���캯��
	 * 
	 * @param incColumn
	 *            ������Ŀ����ͻ�����������
	 */
	public IncColumnStateChangeListener(IncColumn incColumn) {
		this.incColumn = incColumn;
	}

	/**
	 * ״̬ת�����÷���
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		// �ж�������ĿDataSet��ǰ״̬�Ƿ�Ϊ���״̬
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {// ���״̬
			// ���ұ߱༭��������ɱ༭
			incColumn.setAllControlEnabledFalse();
			// �������������Ŀ�����Բ���
			incColumn.ftreeIncColumn.setEnabled(true);
		} else {// �༭״̬
			// �������������Ŀ�������Բ���
			incColumn.ftreeIncColumn.setEnabled(false);
			// �ж��޸ĵĽڵ��Ƿ����ӽڵ�
			if ("modname".equals(incColumn.sSaveType)) { // �޸ĵĽڵ����ӽڵ�
				// ֻ�����޸Ĳ���������
				incColumn.ftxtfIncColCode.setEditable(true);
				incColumn.ftxtfIncColName.setEditable(true);
			} else if ("modformate".equals(incColumn.sSaveType)) {// �޸ĵ�Ҷ�ڵ��ѱ�ʹ�ã�ֻ���޸ı����ʽ��
				incColumn.ftxtfIncColCode.setEditable(true);
				incColumn.fcbxSFormate.setEnabled(true);
				incColumn.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			} else {// add,ins,mod
				// ���ұ߱༭������ɱ༭
				incColumn.setAllControlEnabledTrue();
				if ("add".equals(incColumn.sSaveType)
						|| "addfirstson".equals(incColumn.sSaveType)) {
					incColumn.ftxtfIncColCode.setEditable(false);
				}
			}
		}
		// ����ToolBar��ť״̬
		SetActionStatus setActionStatus = new SetActionStatus(
				incColumn.dsIncCol, incColumn, incColumn.ftreeIncColumn);
		setActionStatus.setState(true, true);
	}
}
