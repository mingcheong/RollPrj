package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;
import com.foundercy.pf.control.Compound;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;

public class StateChangeListenerI implements StateChangeListener {

	private static final long serialVersionUID = 1L;

	Compound compound;

	FTextField ftxtfCodeUp;

	FTextArea ftxtaformula;

	public StateChangeListenerI(Compound compound, FTextField ftxtfCodeUp,
			FTextArea ftxtaformula) {
		this.compound = compound;
		this.ftxtfCodeUp = ftxtfCodeUp;
		this.ftxtaformula = ftxtaformula;
	}

	public void onStateChange(DataSetEvent e) throws Exception {
		if ((e.getDataSet().getState() & StatefulData.DS_BROWSE) == StatefulData.DS_BROWSE) {
			Common.changeChildControlsEditMode(compound, false);
		} else {
			Common.changeChildControlsEditMode(compound, true);
			ftxtfCodeUp.setEditable(false);
			if (ftxtaformula != null) {
				ftxtaformula.setEditable(false);
			}
		}
	}
}
