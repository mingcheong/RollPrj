package gov.nbcs.rp.input.action;

import gov.nbcs.rp.input.ui.PrjActionUI;
import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

public class AuditoBack extends CommonAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
        FModulePanel mPanel = getModulePanel();
        if(mPanel instanceof PrjActionUI) {
            ((PrjActionUI)mPanel).doAuditToBack();
        }
    }
}