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
 * Title:֧����Ŀ���DataSet״̬�ı��¼�
 * </p>
 * <p>
 * Description:֧����Ŀ���DataSet״̬�ı��¼�,����DataSet״̬���ƽ���͹���������Ƿ�ɲ���
 * </p>
 * <p>
 
 */
public class PayOutTypeStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// ֧����Ŀ������ͻ�����������
	private PayOutType payOutType = null;

	public PayOutTypeStateChangeListener(PayOutType payOutType) {
		this.payOutType = payOutType;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(
					payOutType.rightPanel, false);
			payOutType.ftreePayOutType.setEnabled(true);
		} else {
			if ("modname".equals(payOutType.sSaveType)) { // �޸ĵĽڵ���Ҷ�ڵ�
				payOutType.ftxtfPayOutTypeCode.setEditable(true);
				payOutType.ftxtfPayOutTypeName.setEditable(true);
			} else if ("modformate".equals(payOutType.sSaveType)) {// �޸ĵ�Ҷ�ڵ��ѱ�ʹ�ã�ֻ���޸ı����ʽ��
				payOutType.ftxtfPayOutTypeCode.setEditable(true);
				payOutType.ftreeAcctJJ.setIsCheckBoxEnabled(true);
			} else {// add,ins,mod
				Common.changeChildControlsEditMode(
						payOutType.rightPanel, true);
				payOutType.ftreePayOutType.setEnabled(false);
				payOutType.ftxtPriCode.setEditable(false);
				if ("add".equals(payOutType.sSaveType)
						|| "addfirstson".equals(payOutType.sSaveType))
					payOutType.ftxtfPayOutTypeCode.setEditable(false);
			}

		}
		// ���ð�ť״̬
		SetActionStatus setActionStatus = new SetActionStatus(
				payOutType.dsPayOutType, payOutType, payOutType.ftreePayOutType);
		setActionStatus.setState(true, true);

	}
}
