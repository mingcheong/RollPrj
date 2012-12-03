package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

public class BudgetDowmSecondeBsatchSz extends CommonAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private QrBudget qrBudget;

	private CustomTree ftreDivName;

	public void actionPerformed(ActionEvent arg0) {
		this.qrBudget = (QrBudget) this.getModulePanel();
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;
			PrintNoticeDialog printNoticeDialog = new PrintNoticeDialog(
					qrBudget);
			try {
				Tools.centerWindow(printNoticeDialog);
				printNoticeDialog.setVisible(true);
			} finally {
				printNoticeDialog.dispose();
			}
		}
	}

	/**
	 * 
	 * ����Ի���
	 */
	private class PrintNoticeDialog extends FDialog {

		private static final long serialVersionUID = 1L;

		public PrintNoticeDialog(QrBudget qrBudget) {
			super(Global.mainFrame);
			this.setSize(400, 360);
			this.setResizable(false);
			this.dispose();
			this.setModal(true);
			this.setTitle("������ӡ����ָ��");
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "������ӡ����ָ�귢�����󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void init() throws Exception {

			// ��ʾ��
			DataSet dsDivName = (DataSet) qrBudget.getFtreDivName()
					.getDataSet().clone();
			ftreDivName = new CustomTree("����", null, IQrBudget.EN_ID,
					IQrBudget.CODE_NAME, IQrBudget.PARENT_ID, null);
			ftreDivName.setIsCheck(true);
			ftreDivName.setIsCheckBoxEnabled(true);
			ftreDivName.setSortKey(IQrBudget.DIV_CODE);
			ftreDivName.setDataSet(dsDivName);
			ftreDivName.reset();

			FScrollPane fScroll = new FScrollPane(ftreDivName);
			FPanel fPanelDivName = new FPanel();
			fPanelDivName.setTitle("��ѡ��������ӡ�ĵ�λ");
			fPanelDivName.setTopInset(5);
			fPanelDivName.setLeftInset(5);
			fPanelDivName.setRightInset(5);
			fPanelDivName.setLayout(new RowPreferedLayout(1));
			fPanelDivName.addControl(fScroll);

			// ��ťPanel
			FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
			choosePanel.setAlignment(FlowLayout.CENTER);
			FButton fbtnPrint = new FButton("fbtnPrint", "��ӡ...");
			fbtnPrint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					BudgetDowmSecondeSz.printBudgetOperate(ftreDivName);
				}
			});

			FButton cancelButton = new FButton("cancelButton", "ȡ��");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PrintNoticeDialog.this.dispose();
				}
			});
			choosePanel.addControl(fbtnPrint);
			choosePanel.addControl(cancelButton);

			RowPreferedLayout rLay = new RowPreferedLayout(1);
			rLay.setRowHeight(15);
			this.getContentPane().setLayout(rLay);
			this.getContentPane().add(fPanelDivName,
					new TableConstraints(1, 1, true, true));
			this.getContentPane().add(choosePanel,
					new TableConstraints(2, 1, false, true));

		}
	}

}
