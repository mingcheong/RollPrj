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
 * ѡ����ʾ��
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
				// ��ǰ��ʾҳ��
				int index = qrBudget.getTabPaneReport().getSelectedIndex();

				ReportInfo reportInfo = (ReportInfo) qrBudget
						.getTabPaneReport().getLstReport().get(index);

				// ��������
				int typeFlag = reportInfo.getBussType();
				if (typeFlag == QrBudget.TYPE_COVER
						|| typeFlag == QrBudget.TYPE_SZZB
						|| typeFlag == QrBudget.TYPE_ROWSET) {
					JOptionPane.showMessageDialog(qrBudget,
							"����Ŀ¼����֧�ܱ���λ�ۺ��������ѡ����ʾ�У�", "��ʾ",
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
						"����Ԥ���ѯ��ѡ����ʾ�з������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
