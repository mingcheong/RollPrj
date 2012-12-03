/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title:֧���ʽ���ԴDataSet״̬�ı��¼�
 * </p>
 * <p>
 * Description:֧���ʽ���ԴDataSet״̬�ı��¼�,����DataSet״̬���ƽ���͹���������Ƿ�ɲ���

 */

public class PayOutFsStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	// ֧���ʽ���Դ����ͻ�����������
	private PayOutFS payOutFS = null;

	/**
	 * ���캯��
	 * 
	 * @param payOutFs
	 *            ֧���ʽ���Դ����ͻ�����������
	 */
	public PayOutFsStateChangeListener(PayOutFS payOutFs) {
		this.payOutFS = payOutFs;
	}

	/**
	 * ״̬ת�����÷���
	 */
	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			payOutFS.setAllControlEnabledFalse();
			payOutFS.ftreePayOutFS.setEnabled(true);
		} else {
			payOutFS.ftreePayOutFS.setEnabled(false);
			if ("modname".equals(payOutFS.sSaveType)) { // �޸ĵĽڵ���Ҷ�ڵ�
				payOutFS.ftxtfPfsCode.setEditable(true);
				payOutFS.ftxtfPfsName.setEditable(true);
				payOutFS.ftxtReportPfsName.setEditable(true);
			} else if ("modformate".equals(payOutFS.sSaveType)) {// �޸ĵ�Ҷ�ڵ��ѱ�ʹ�ã�ֻ���޸ı����ʽ��
				payOutFS.ftxtfPfsCode.setEditable(true);
				payOutFS.fcbxSFormate.setEnabled(true);
				// if (payOutFS.payOutFsObj.data_source == 0)
				// payOutFS.fcbxEFormate.setEnabled(true);
			} else {// add,ins,mod
				payOutFS.setAllControlEnabledTrue();
				if ("add".equals(payOutFS.sSaveType)
						|| "addfirstson".equals(payOutFS.sSaveType))
					payOutFS.ftxtfPfsCode.setEditable(false);
			}
		}
		// ���ð�ť״̬
		SetActionStatus setActionStatus = new SetActionStatus(
				payOutFS.dsPayOutFS, payOutFS, payOutFS.ftreePayOutFS);
		setActionStatus.setState(true, true);
	}
}
