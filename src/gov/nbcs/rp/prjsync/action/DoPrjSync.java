/**
 * @Copyright �㽭���Ű�Ȩ����
 *
 * @ProjectName ��������������Ŀϵͳ
 *
 * @aouthor ���ܱ�
 *
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.action;

import gov.nbcs.rp.prjsync.ui.PrjSyncUI;

import java.awt.event.ActionEvent;

import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * @author ë����
 *
 * @version ����ʱ�䣺2012-6-7 ����11:20:12
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

