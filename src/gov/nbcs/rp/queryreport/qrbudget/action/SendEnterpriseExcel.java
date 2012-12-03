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
				JFileChooser fileChooser = null;// �ļ�ѡ����
				// �����ļ�ѡ����
				fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				// �趨���õ��ļ��ĺ�׺��
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
								return "�����ļ�(*.xls)";
							}
						});
				if ("SEND".equals(unitState))
					fileChooser.setSelectedFile(new File("���ϱ���λ"));
				else if ("UNSEND".equals(unitState))
					fileChooser.setSelectedFile(new File("δ�ϱ���λ"));
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
