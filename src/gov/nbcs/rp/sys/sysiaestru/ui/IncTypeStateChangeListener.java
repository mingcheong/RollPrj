/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:������Ŀ���DataSet״̬�ı��¼�
 * </p>
 * <p>
 * Description:������Ŀ���DataSet״̬�ı��¼�,����DataSet״̬���ƽ���͹���������Ƿ�ɲ���
 * </p>

 */
public class IncTypeStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	/**
	 * ���캯��
	 * 
	 */
	public IncTypeStateChangeListener(IncType incType) {
		this.incType = incType;
	}

	/**
	 * ״̬ת�����÷���
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(incType.rightPanel,
					false);
			incType.setAllControlEnabledFalse();
			incType.ftreeIncType.setEnabled(true);
			incType.setState();
		} else {
			incType.ftreeIncType.setEnabled(false);
			if ("modname".equals(incType.sSaveType)) { // �޸ĵĽڵ���Ҷ�ڵ�
				incType.ftxtfIncTypeCode.setEditable(true);
				incType.ftxtfIncTypeName.setEditable(true);
			} else if ("modformate".equals(incType.sSaveType)) {
				incType.ftxtfIncTypeCode.setEditable(true);
				incType.frdoIsInc.setEditable(true);
				incType.setState();
			} else {// add,ins,mod
				// �����ұ߱༭�����Ա༭
				incType.setAllControlEnabledTrue();
				if ("add".equals(incType.sSaveType)
						|| "addfirstson".equals(incType.sSaveType))
					incType.ftxtfIncTypeCode.setEditable(false);
				incType.setState();
			}

		}
		// ���ð�ť״̬
		SetActionStatus setActionStatus = new SetActionStatus(
				incType.dsIncType, incType, incType.ftreeIncType);
		setActionStatus.setState(true, true);
	}
}
