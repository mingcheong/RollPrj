package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrSendEnterprise;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.fr.report.io.ExcelExporter;

public class SendEnterpriseExcel extends CommonAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String sSqlHeader;

	QrSendEnterprise qrBudget;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrSendEnterprise) {
			qrBudget = (QrSendEnterprise) modulePanel;
			String unitState = qrBudget.getUnitState();
			ReportUI reportUI = qrBudget.getReport();
			try {
				JFileChooser fileChooser = null;// 文件选择器
				// 创建文件选择器
				fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				// 设定可用的文件的后缀名
				fileChooser
						.setFileFilter(new javax.swing.filechooser.FileFilter() {
							public boolean accept(File f) {
								if (f.getName().toLowerCase().endsWith(".xls")
										|| f.isDirectory()) {
									return true;
								}
								
								return false;
							}

							public String getDescription() {
								return "所有文件(*.xls)";
							}
						});
				if ("SEND".equals(unitState))
					fileChooser.setSelectedFile(new File("已上报单位"));
				else if ("UNSEND".equals(unitState))
					fileChooser.setSelectedFile(new File("未上报单位"));
				fileChooser.showSaveDialog(Global.mainFrame);
				String filepath = "";
				filepath = fileChooser.getSelectedFile().getPath();
				if (!".xls".equalsIgnoreCase(filepath.substring(filepath
						.length() - 4, filepath.length())))
					filepath = filepath + ".xls";
				if (!"".equals(filepath)) {
					ExcelExporter eep = new ExcelExporter(filepath);
					eep.exportReport(reportUI.getReport());
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			} finally {

			}

		}
	}

}
