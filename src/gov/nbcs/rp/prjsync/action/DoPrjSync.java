/**
 * @Copyright 浙江易桥版权所有
 *
 * @ProjectName 宁海财政扶持项目系统
 *
 * @aouthor 陈宪标
 *
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.action;

import gov.nbcs.rp.prjsync.ui.PrjSyncUI;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * @author 毛建亮
 *
 * @version 创建时间：2012-6-7 上午11:20:12
 *
 * @Description
 */
public class DoPrjSync extends CommonAction{
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
		FModulePanel mPanel = getModulePanel();
		if (mPanel instanceof PrjSyncUI) {
			 ((PrjSyncUI) mPanel).doSync();
		}
	}
}

