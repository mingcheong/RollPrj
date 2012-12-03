package gov.nbcs.rp.common.action;

import gov.nbcs.rp.prjsync.action.PrjSyncActionedUI;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;




public class Merger extends CommonAction
{

	private static final long serialVersionUID = 1L;



	public void actionPerformed(ActionEvent e)
	{
		FModulePanel mPanel = getModulePanel();
		if (mPanel instanceof ActionedUIEx)
		{
			((PrjSyncActionedUI) mPanel).doMerger();
		}
	}
}
