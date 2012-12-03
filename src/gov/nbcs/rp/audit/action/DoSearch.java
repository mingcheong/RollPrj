package gov.nbcs.rp.audit.action;

import gov.nbcs.rp.audit.ui.FinshAudit;
import gov.nbcs.rp.audit.ui.PrjAuditUI;
import gov.nbcs.rp.input.ui.PrjFileSendMainUI;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;




/**
 * @author 谢昀荪
 * 
 * @version 创建时间：Jul 4, 20122:43:11 PM
 * 
 * @Description
 */
public class DoSearch extends CommonAction
{

	private static final long serialVersionUID = 1L;



	public void actionPerformed(ActionEvent e)
	{
		FModulePanel mPanel = getModulePanel();
		if (mPanel instanceof PrjAuditUI)
		{
			((PrjAuditUI) mPanel).dosearch();
		}
		else if (mPanel instanceof FinshAudit)
		{
			((FinshAudit) mPanel).dosearch();
		}
		else if (mPanel instanceof PrjFileSendMainUI)
		{
			((PrjFileSendMainUI) mPanel).dosearch();
		}
	}
}
