/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**

 * Copyright: Copyright (c) 2011 �㽭����
 * </p>
 * <p>
 * Company: �㽭����
 * </p>
 * <p>
 * CreateDate 2011-7
 * </p>
 * 
 * @author WUYAL

 * @version 1.0
 */
public class CopyReportAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (!(modulePanel instanceof DefineReport)) {
			return;
		}

		DefineReport defineReport = (DefineReport) modulePanel;
		CustomTree ftreReport = defineReport.getFtreReport();
		RepSetObject repSetObject = defineReport.getRepSetObject();
		IDefineReport definReportServ = defineReport.getDefinReportServ();

		// �õ�ѡ�нڵ�
		MyTreeNode selectNode = ftreReport.getSelectedNode();
		if (selectNode == null || selectNode == ftreReport.getRoot()) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�񱨱�!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String sReportName;
		while (true) {
			sReportName = JOptionPane.showInputDialog(Global.mainFrame,
					"�����������ɱ�������", "��������", JOptionPane.INFORMATION_MESSAGE);
			if (sReportName == null)
				return;
			if ("".equals(sReportName.trim())) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"�������Ʋ���Ϊ��,��������д��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				continue;
			}
			break;
		}

		try {
			InfoPackage info = definReportServ.copyReport(repSetObject
					.getREPORT_ID(), sReportName, Global.loginYear);
			if (info.getSuccess()) {
				List lstValue = (List) info.getObject();
				String reportId = (String) lstValue.get(0);
				String lvlId = (String) lstValue.get(1);

				List lstType = defineReport.getReportTypeList().getSelectData();
				repSetObject.setREPORT_ID(reportId);
				repSetObject.setLVL_ID(lvlId);
				repSetObject.setREPORT_CNAME(sReportName);
				repSetObject.setTITLE(sReportName);
				// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
				defineReport.refreshNodeEdit(repSetObject, lstType, reportId);

				JOptionPane.showMessageDialog(Global.mainFrame, "���Ƴɹ�!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��!,ʧ��ԭ��:"
						+ info.getsMessage(), "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
