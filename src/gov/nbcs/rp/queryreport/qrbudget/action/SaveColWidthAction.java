package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * 保存列宽
 * 

 * 
 */
public class SaveColWidthAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;
			if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
				return;
			try {
				// 当前显示页面
				int index = qrBudget.getTabPaneReport().getSelectedIndex();

				ReportInfo reportInfo = (ReportInfo) qrBudget
						.getTabPaneReport().getLstReport().get(index);
				// 报表类型
				int typeFlag = reportInfo.getBussType();
				if (typeFlag == QrBudget.TYPE_COVER
						|| typeFlag == QrBudget.TYPE_SZZB) {
					saveSzzbColWidth((Report) qrBudget.getReportUI()
							.getReport(), reportInfo, qrBudget
							.getQrBudgetServ());
					// JOptionPane.showMessageDialog(qrBudget,
					// "封面目录、收支总表不可设置保存列宽！", "提示",
					// JOptionPane.INFORMATION_MESSAGE);
					// return;
				} else {
					saveColWidth((Report) qrBudget.getReportUI().getReport(),
							reportInfo, qrBudget.getQrBudgetServ());
				}
				JOptionPane.showMessageDialog(qrBudget, "保存列宽成功！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表保存列宽发生错误，错误信息:"
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 保存列宽
	 * 
	 * @throws Exception
	 */
	private void saveColWidth(Report report, ReportInfo reportInfo,
			IQrBudget qrBudgetServ) throws Exception {
		List lstFields = report.getReportHeader().getFields();
		List lstColWidth = new ArrayList();
		for (int i = 0; i < lstFields.size(); i++) {
			lstColWidth.add(String.valueOf(report.getColumnWidth(i)));
		}
		String sReportId = reportInfo.getReportID();
		qrBudgetServ.saveColWidth(sReportId, Global.loginYear, lstFields,
				lstColWidth);
	}

	/**
	 * 保存列宽(收支总表和封面目录）
	 * 
	 * @throws Exception
	 */
	private void saveSzzbColWidth(Report report, ReportInfo reportInfo,
			IQrBudget qrBudgetServ) throws Exception {
		List lstOldFieldColWithd = reportInfo.getLstFieldColWidth();

		int size = report.getColumnCount();
		List lstColWidth = new ArrayList();
		for (int i = 0; i < size; i++) {
			lstColWidth.add(String
					.valueOf(report.getColumnWidth(i)
							- Double.parseDouble(lstOldFieldColWithd.get(i)
									.toString())));
		}
		String sReportId = reportInfo.getReportID();
		qrBudgetServ.saveSzzbColWidth(sReportId, Global.loginYear, lstColWidth);
	}

	/**
	 * 保存报表原列宽
	 * 
	 * @param report
	 * @param reportInfo
	 */
	public static void saveOldColWidth(Report report, ReportInfo reportInfo) {
		List lstFieldColWidth = new ArrayList();
		int count = report.getColumnCount();
		for (int i = 0; i < count; i++) {
			lstFieldColWidth.add(String.valueOf(report.getColumnWidth(i)));
		}
		reportInfo.setLstFieldColWidth(lstFieldColWidth);
	}

}
