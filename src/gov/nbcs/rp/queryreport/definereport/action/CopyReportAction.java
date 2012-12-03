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

 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
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

		// 得到选中节点
		MyTreeNode selectNode = ftreReport.getSelectedNode();
		if (selectNode == null || selectNode == ftreReport.getRoot()) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择报表!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String sReportName;
		while (true) {
			sReportName = JOptionPane.showInputDialog(Global.mainFrame,
					"请输入新生成报表名称", "报表名称", JOptionPane.INFORMATION_MESSAGE);
			if (sReportName == null)
				return;
			if ("".equals(sReportName.trim())) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"报表名称不能为空,请重新填写！", "提示",
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
				// 修改或增加节点时，刷新树节点
				defineReport.refreshNodeEdit(repSetObject, lstType, reportId);

				JOptionPane.showMessageDialog(Global.mainFrame, "复制成功!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(Global.mainFrame, "复制失败!,失败原因:"
						+ info.getsMessage(), "提示",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
