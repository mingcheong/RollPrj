package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ChoiceShowCol;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * 选择显示列
 * 
 * @author qzc
 * 
 */
public class ChoiceShowColAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;
			if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
				return;
			String sReportTitle;
			try {
				// 当前显示页面
				int index = qrBudget.getTabPaneReport().getSelectedIndex();

				ReportInfo reportInfo = (ReportInfo) qrBudget
						.getTabPaneReport().getLstReport().get(index);

				// 报表类型
				int typeFlag = reportInfo.getBussType();
				if (typeFlag == QrBudget.TYPE_COVER
						|| typeFlag == QrBudget.TYPE_SZZB
						|| typeFlag == QrBudget.TYPE_ROWSET) {
					JOptionPane.showMessageDialog(qrBudget,
							"封面目录、收支总表、单位综合情况表不可选择显示列！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				qrBudget.getFpnlToolBar().getJspFrozenColumn().setValue(
						new Integer(0));
				qrBudget.getReportUI().freezeColumn(0);
				DataSet dsReportName =qrBudget.getFtreReportName().getDataSet();
				
				if (dsReportName.fieldByName(IQrBudget.TITLE)
						.getValue() != null)
					sReportTitle = dsReportName.fieldByName(
							IQrBudget.TITLE).getString();
				else
					sReportTitle = "";

				ChoiceShowCol choiceShowCol = new ChoiceShowCol(
						Global.mainFrame, sReportTitle, qrBudget
								.getDsReportHeader(), (Report) qrBudget.getReportUI().getReport(),
						qrBudget.getReportUI());
				Tools.centerWindow(choiceShowCol);
				choiceShowCol.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget,
						"部门预算查询表选择显示列发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
