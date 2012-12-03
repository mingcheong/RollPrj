/**
 * @(#)Modify.java
 * @title - ������Ŀ��
 * @copyright �㽭���� ��Ȩ����
 */
package gov.nbcs.rp.common.action;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

import java.awt.event.ActionEvent;

/**
 * The Class Print.
 * 
 * @author qzc(Ǯ�Գ�)
 * @version 1.0, May 19, 2011
 * @since rp 1.0.00
 */
public class Print extends CommonAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.pf.framework.systemmanager.CommonAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		FModulePanel mPanel = getModulePanel();
		if (mPanel instanceof ActionedUIEx) {
			((ActionedUIEx) mPanel).doPrint();
		} else if (mPanel instanceof HasActionOp) {
			((HasActionOp) mPanel).getActionOP().doPrint();
		}
	}

}
