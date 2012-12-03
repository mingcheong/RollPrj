package gov.nbcs.rp.audit.action;

import gov.nbcs.rp.audit.ui.PrjAuditActionUI;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

public class BackAudit extends CommonAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
        FModulePanel mPanel = getModulePanel();
        if(mPanel instanceof PrjAuditActionUI) {
            ((PrjAuditActionUI)mPanel).doBackAudit();
        }
    }
}
