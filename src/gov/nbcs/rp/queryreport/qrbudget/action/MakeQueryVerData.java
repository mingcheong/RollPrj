package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.qrbudget.ui.MakeVerProgressBar;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetVerInfoUI;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

public class MakeQueryVerData extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudgetVerInfoUI) {
			try {
				QrBudgetVerInfoUI qrBudgetVer = (QrBudgetVerInfoUI) modulePanel;
				MyTreeNode node = qrBudgetVer.getTreVerInfo().getSelectedNode();
				MyTreeNode root = (MyTreeNode) qrBudgetVer.getTreVerInfo()
						.getRoot();
				String verNo = "";
				if (node != null && node != root) {
					DataSet dsVer = qrBudgetVer.getTreVerInfo().getDataSet();
					dsVer.gotoBookmark(node.getBookmark());
					verNo = dsVer.fieldByName("ver_no").getString();
				} else
					return;
				InfoPackage infopackage = new InfoPackage();
				// 删除报表版本物理表
				infopackage = QrBudgetI.getMethod().DelTableReportVer(verNo);
				if (!infopackage.getSuccess()) {
					JOptionPane.showMessageDialog(qrBudgetVer.getMainUI(),
							infopackage.getsMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// 创建报表版本物理表
				MakeVerProgressBar progbar = new MakeVerProgressBar(qrBudgetVer
						.getMainUI(), verNo);
				progbar.display();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
}
