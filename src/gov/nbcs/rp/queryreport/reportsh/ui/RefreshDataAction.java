/**
 * @# RefreshDataAction.java    <�ļ���>
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
 * ����˵��:
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
				MessageBox mb = new MessageBox("�����ڴ˹����Ͻ��д˲���!",
						MessageBox.INFOMATION, MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
				return;
			}
			MessageBox mb = new MessageBox(
					"��������׼�����ķ�ʮ���뵽������ʱ�䣬��������Ա���ѡ����þ��Ѻ���Ŀ������û�б仯��\n\r"
							+ "��������˹��ܡ��㡮ȷ������������׼�����㡮ȡ�������أ�ֱ�ӵ㡮��ѯ�����ɣ�",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK
							| MessageBox.BUTTON_CANCEL);
			mb.show();
			if (mb.result != MessageBox.OK)
				return;
			// ȡ�õ�λ
			CustomTree tre = ((HasDivTree) parent).getFtreDivName();
			MyTreeNode[] nodes = tre.getSelectedNodes(true);
			if (nodes == null) {
				mb = new MessageBox("��ѡ��Ҫˢ�µĵ�λ��Ϣ!", MessageBox.INFOMATION,
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
				mb = new MessageBox("��ѡ��Ҫˢ�µĵ�λ��Ϣ!", MessageBox.INFOMATION,
						MessageBox.BUTTON_OK);
				mb.setVisible(true);
				mb.dispose();
				return;
			}
		
		} catch (Exception e) {
			new MessageBox("ˢ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}

	}

	public void doRun() {
		try {
			RepDispStub.getMethod().refreshCurUserQrData(lstDiv);

		} catch (Exception e) {
			new MessageBox("ˢ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}
		MessageBox mb = new MessageBox("ˢ�����ݳɹ�!", MessageBox.INFOMATION,
				MessageBox.BUTTON_OK);
		mb.setVisible(true);
		mb.dispose();

	}
}
