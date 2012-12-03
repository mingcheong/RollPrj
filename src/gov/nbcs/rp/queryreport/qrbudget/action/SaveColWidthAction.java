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
 * �����п�
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
				// ��ǰ��ʾҳ��
				int index = qrBudget.getTabPaneReport().getSelectedIndex();

				ReportInfo reportInfo = (ReportInfo) qrBudget
						.getTabPaneReport().getLstReport().get(index);
				// ��������
				int typeFlag = reportInfo.getBussType();
				if (typeFlag == QrBudget.TYPE_COVER
						|| typeFlag == QrBudget.TYPE_SZZB) {
					saveSzzbColWidth((Report) qrBudget.getReportUI()
							.getReport(), reportInfo, qrBudget
							.getQrBudgetServ());
					// JOptionPane.showMessageDialog(qrBudget,
					// "����Ŀ¼����֧�ܱ������ñ����п�", "��ʾ",
					// JOptionPane.INFORMATION_MESSAGE);
					// return;
				} else {
					saveColWidth((Report) qrBudget.getReportUI().getReport(),
							reportInfo, qrBudget.getQrBudgetServ());
				}
				JOptionPane.showMessageDialog(qrBudget, "�����п�ɹ���", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ�����п������󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * �����п�
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
	 * �����п�(��֧�ܱ�ͷ���Ŀ¼��
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
	 * ���汨��ԭ�п�
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
