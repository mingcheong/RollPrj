package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.print.PrintUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * 打印

 * 
 */
public class PrintAction extends CommonAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;
			if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
				return;
			try {
				SearchPublic searchPublic = new SearchPublic(qrBudget);
				String sDivName = searchPublic.getDivNameValue();
				ReportInfo reportInfo = (ReportInfo) qrBudget
						.getTabPaneReport().getLstReport().get(
								qrBudget.getTabPaneReport().getSelectedIndex());
				InfoPackage info = PrintUtility.print((Report) qrBudget
						.getReportUI().getReport(), reportInfo.getReportID(),
						Global.loginYear, false, new String[] { sDivName, "" });
				PrintUtility.printPreview(null,(Report) qrBudget
						.getReportUI().getReport(),reportInfo.getReportID(),Global.loginYear,new String[] { sDivName, "" });
				if (info.getSuccess() == false)
					JOptionPane.showMessageDialog(Global.mainFrame, info
							.getsMessage());
			} catch (Exception ee) {
				ee.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame, "打印发生错误，错误信息："
						+ ee.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
