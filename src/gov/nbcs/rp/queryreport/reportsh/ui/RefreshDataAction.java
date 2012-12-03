/**
 * @# RefreshDataAction.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class RefreshDataAction extends CommonAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2411458303442764160L;

	List lstDiv;

	public void actionPerformed(ActionEvent actionevent) {

		try {
			FModulePanel parent = this.getModulePanel();
			if (!(parent instanceof HasDivTree)) {
				MessageBox mb = new MessageBox("不能在此功能上进行此操作!",
						MessageBox.INFOMATION, MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
				return;
			}
			MessageBox mb = new MessageBox(
					"由于数据准备将耗费十几秒到数分钟时间，若您的人员经费、公用经费和项目等数据没有变化，\n\r"
							+ "无需操作此功能。点‘确定’继续数据准备，点‘取消’返回，直接点‘查询’即可！",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK
							| MessageBox.BUTTON_CANCEL);
			mb.show();
			if (mb.result != MessageBox.OK)
				return;
			// 取得单位
			CustomTree tre = ((HasDivTree) parent).getFtreDivName();
			MyTreeNode[] nodes = tre.getSelectedNodes(true);
			if (nodes == null) {
				mb = new MessageBox("请选择要刷新的单位信息!", MessageBox.INFOMATION,
						MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
				return;
			}
			lstDiv = new ArrayList();
			Map map = new HashMap();
			int iCount = nodes.length;
			for (int i = 0; i < iCount; i++) {
				String divCode = nodes[i].sortKeyValue();
				try {

					String sub = divCode.substring(0, 3);
					if (map.containsKey(sub))
						continue;
					map.put(sub, null);
					lstDiv.add(sub);

				} catch (Exception e) {

				}
			}

			if (lstDiv.isEmpty()) {
				mb = new MessageBox("请选择要刷新的单位信息!", MessageBox.INFOMATION,
						MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
				return;
			}
		
		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}

	}

	public void doRun() {
		try {
			RepDispStub.getMethod().refreshCurUserQrData(lstDiv);

		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}
		MessageBox mb = new MessageBox("刷新数据成功!", MessageBox.INFOMATION,
				MessageBox.BUTTON_OK);
		mb.setVisible(true);
		mb.dispose();

	}
}
