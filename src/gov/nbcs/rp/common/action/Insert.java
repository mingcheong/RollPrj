/**
 * @(#)Insert.java
 * @title - 滚动项目库
 * @copyright 浙江易桥 版权所有
 */
package gov.nbcs.rp.common.action;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

import java.awt.event.ActionEvent;

/**
 * The Class Insert.
 * 
 * @author qzc(钱自成)
 * @version 1.0, May 19, 2011
 * @since rp 1.0.00
 */
public class Insert extends CommonAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
        FModulePanel mPanel = getModulePanel();
        if(mPanel instanceof ActionedUI) {
            ((ActionedUI)mPanel).doInsert();
        } else if (mPanel instanceof HasActionOp) {
			((HasActionOp) mPanel).getActionOP().doInsert();
		}
    }
}
