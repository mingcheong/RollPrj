 /**
  * @# MoveDownAction.java    <ÎÄ¼þÃû>
  */
package gov.nbcs.rp.dicinfo.datadict.action;

import java.awt.event.ActionEvent;

import gov.nbcs.rp.dicinfo.datadict.ui.DataDictionaryUI;
import com.foundercy.pf.framework.systemmanager.CommonAction;


public class MoveDownAction extends CommonAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8088167595696583974L;
	public void actionPerformed(final ActionEvent e) {
		DataDictionaryUI mPanel = (DataDictionaryUI)getModulePanel();
		mPanel.moveDown();
    }
	

}
