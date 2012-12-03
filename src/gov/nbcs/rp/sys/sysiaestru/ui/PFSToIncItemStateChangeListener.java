package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;

/**
 * 资金来源对应收入DataSet状态转换事件
 * 

 * 
 */
public class PFSToIncItemStateChangeListener implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	PFSToIncItem pfsToIncItem;

	public PFSToIncItemStateChangeListener(PFSToIncItem pfsToIncItem) {
		this.pfsToIncItem = pfsToIncItem;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(
					pfsToIncItem.fnlIncItem, false);
			pfsToIncItem.ftreePayOutFS.setEnabled(true);
		} else {
			Common.changeChildControlsEditMode(
					pfsToIncItem.fnlIncItem, true);
			pfsToIncItem.ftreePayOutFS.setEnabled(false);
		}
		// 设置按钮状态
		SetActionStatus setActionStatus = new SetActionStatus(
				pfsToIncItem.dsPayOutFS, pfsToIncItem,
				pfsToIncItem.ftreePayOutFS);
		setActionStatus.setState(false);
	}
}
