package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;

/**
 * ִ�в�ѯ
 * 
 * @author qzc
 * 
 */
public class ExeSearchAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	QrBudget qrBudget;

	DataSet dsReportNames;

	public void actionPerformed(ActionEvent arg0) {
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			qrBudget = (QrBudget) modulePanel;
			if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
				return;

			qrBudget.setUUID(UUID.randomUUID().toString());
			try {
				ExeSearch exeSearch = new ExeSearch(qrBudget);
				exeSearch.doExeSearch(qrBudget.getTabPaneReport()
						.getPara(false), true);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "ִ�в�ѯ�������󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
