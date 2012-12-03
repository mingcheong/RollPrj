package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;

public class Close extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		((FFrame) Global.mainFrame).closeMenu();
	}
}
