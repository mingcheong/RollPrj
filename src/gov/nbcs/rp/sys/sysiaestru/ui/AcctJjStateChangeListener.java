package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * 收支科目挂接明细，经济科目DataSet状态转换事件

 * 
 */
public class AcctJjStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	private AcctJjInc acctJjInc = null;

	public AcctJjStateChangeListener(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(acctJjInc.rightPanel,
					false);
			acctJjInc.ftreeAcctJJ.setEnabled(true);
		} else {
			Common.changeChildControlsEditMode(acctJjInc.rightPanel,
					true);
			acctJjInc.ftreeAcctJJ.setEnabled(false);
		}
		// 设置ToolBar按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				acctJjInc.dsAcctJJ, acctJjInc, acctJjInc.ftreeAcctJJ);
		setActionStatus.setState(false, true);

	}
}
