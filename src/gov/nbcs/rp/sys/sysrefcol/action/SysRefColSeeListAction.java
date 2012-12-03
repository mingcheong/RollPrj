package gov.nbcs.rp.sys.sysrefcol.action;

import java.awt.event.ActionEvent;

import gov.nbcs.rp.sys.sysrefcol.ui.SysRefCol;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * 引用列管理，查看明细
 * 

 * 
 */
public class SysRefColSeeListAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		FModulePanel mPanel = getModulePanel();
		((SysRefCol) mPanel).doSeeList();
	}
}
