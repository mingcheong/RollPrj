package gov.nbcs.rp.sys.sysrefcol.action;

import java.awt.event.ActionEvent;

import gov.nbcs.rp.sys.sysrefcol.ui.SysRefCol;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * �����й���Ԥ�����
 * 

 * 
 */
public class SysRefColPreviewAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		FModulePanel mPanel = getModulePanel();
		((SysRefCol) mPanel).doPreview();
	}
}
