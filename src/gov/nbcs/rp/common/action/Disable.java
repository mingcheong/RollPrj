/**
 * @(#)Add.java
 * @title - ������Ŀ��
 * @copyright �㽭���� ��Ȩ����
 */
package gov.nbcs.rp.common.action;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

import java.awt.event.ActionEvent;




/**
 * The Class Add.
 * 
 * @author qzc(Ǯ�Գ�)
 * @version 1.0, May 19, 2011
 * @since rp 1.0.00
 */
public class Disable extends CommonAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public void actionPerformed(ActionEvent e)
	{
		FModulePanel mPanel = getModulePanel();
		if (mPanel instanceof OperatorUI)
		{
			((OperatorUI) mPanel).doDisable();
		}
		else if (mPanel instanceof HasActionOp)
		{
			((HasActionOp) mPanel).getActionOP().doAdd();
		}
	}
}
