package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * <p>
 * Title: ��֧��Ŀ�ҽ���ϸ,����Ԥ���ĿDataSet״̬ת���¼�
 * 
 * </p>
 * <p>
 * Description: ��֧��Ŀ�ҽ���ϸ,����Ԥ���ĿDataSet״̬ת���¼�
 * 

 */
public class AcctIncStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	private AcctJjInc acctJjInc = null;

	public AcctIncStateChangeListener(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(acctJjInc.rightPanel,
					false);
			acctJjInc.ftreeIncAcctitem.setEnabled(true);
		} else {
			Common.changeChildControlsEditMode(acctJjInc.rightPanel,
					true);
			acctJjInc.ftreeIncAcctitem.setEnabled(false);
			AcctIncSetState acctIncSetState = new AcctIncSetState(acctJjInc);
			acctIncSetState.setState();
		}
		// ����ToolBar��ť״̬
		SetActionStatus setActionStatus = new SetActionStatus(
				acctJjInc.dsAcctitem, acctJjInc, acctJjInc.ftreeIncAcctitem);
		setActionStatus.setState(false, true);
	}
}
