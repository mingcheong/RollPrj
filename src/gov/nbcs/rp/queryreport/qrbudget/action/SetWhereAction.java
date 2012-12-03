/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReportI;
import gov.nbcs.rp.queryreport.definereport.ui.ReportPanel;
import gov.nbcs.rp.queryreport.definereport.ui.SummaryIndexUnt;
import gov.nbcs.rp.queryreport.qrbudget.ui.ChangeSourcePanel;
import gov.nbcs.rp.queryreport.qrbudget.ui.CompareWherePanel;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.GroupWhereSetPanel;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportHeaderOpe;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportHeaderShow;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:�任������Դ
 * </p>
 * <p>
 * Description:�任������Դ
 * </p>
 * <p>
 
 */
public class SetWhereAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	private QrBudget qrBudget;

	// ��������
	private GroupWhereSetPanel groupWhereSetPanel;

	// �����ʽ���Դ
	private ChangeSourcePanel changeSourcePanel;

	// �Ա�����
	private CompareWherePanel compareWherePanel;

	private GroupReport groupReport;

	private ChangeSourceDlg changeSourceDlg;

	// private String sOldReportID;

	private ReportInfo reportInfo;

	// �����ļ���д
	private SetWhereReadWrite setWhereReadWrite;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (!(modulePanel instanceof QrBudget)) {
			return;
		}
		this.qrBudget = (QrBudget) this.getModulePanel();
		if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
			return;

		int index = qrBudget.getTabPaneReport().getSelectedIndex();
		reportInfo = (ReportInfo) qrBudget.getTabPaneReport().getLstReport()
				.get(index);

		if (changeSourceDlg == null
				|| !reportInfo.getReportID().equals(qrBudget.getSOldReportID())) {
			qrBudget.setSOldReportID(reportInfo.getReportID());
			changeSourceDlg = new ChangeSourceDlg();
		}
		// try {
		Tools.centerWindow(changeSourceDlg);
		changeSourceDlg.setVisible(true);
		// } finally {
		// changeSourceDlg.dispose();
		// }
	}

	/**
	 * �任�ʽ���Դ�Ի���
	 * 
	 */
	private class ChangeSourceDlg extends FDialog {

		private static final long serialVersionUID = 1L;

		public ChangeSourceDlg() {
			super(Global.mainFrame);
			this.setSize(600, 450);
			this.setResizable(false);
			this.setModal(true);
			this.setTitle("��������");
			// ��ʼ������
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "��ʾ�任�ʽ���Դ���淢�����󣬴�����Ϣ��"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * �����ʼ��
		 * 
		 * @throws Exception
		 * 
		 */
		private void init() throws Exception {

			IDefineReport definReportServ = DefineReportI.getMethod();
			groupReport = ReportPanel.getGroupReport(reportInfo.getReportID(),
					definReportServ);
			// У��ԭ���屨��summaryIndexû������ֵ����
			SummaryIndexUnt.checkGroupDetail(groupReport);

			setWhereReadWrite = new SetWhereReadWrite(reportInfo.getReportID());

			// ����������ҳ�����
			FTabbedPane ftabPnlReportSet = new FTabbedPane();
			groupWhereSetPanel = new GroupWhereSetPanel(groupReport);
			boolean isSame = false;
			// ��������Ƿ����
			if (setWhereReadWrite.isReportExists()) {
				// �õ�����lstLevIsTotalField
				List lstLevIsTotalField = groupWhereSetPanel
						.getLevIsTotalField();
				// �жϱ����ļ������뱨���Ƿ�һ��
				isSame = setWhereReadWrite.checkFilePara(lstLevIsTotalField);
				if (isSame) {// һ��
					// ���ݱ��ز����ļ����ݣ�����lstLevIsTotalFieldֵ
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
				}
			}
			groupWhereSetPanel.refresh();

			ftabPnlReportSet.addControl("������������", groupWhereSetPanel);
			if (Common.estimate(reportInfo.getChangeSource())) {
				List lstPfsCode = null;
				if (isSame) {
					lstPfsCode = setWhereReadWrite.getPfsCode();
				}
				changeSourcePanel = new ChangeSourcePanel(lstPfsCode);
				ftabPnlReportSet.addControl("�ʽ���Դѡ��", changeSourcePanel);
			}
			if (Common.estimate(reportInfo.getCompareFlag())) {
				ConditionObj conditionObj = null;
				if (isSame) {
					conditionObj = setWhereReadWrite.getConditionObj();
				}
				compareWherePanel = new CompareWherePanel(qrBudget,
						conditionObj);
				ftabPnlReportSet.addControl("�Աȷ�������", compareWherePanel);
			}

			ButtonPanel buttonPanel = new ButtonPanel();

			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new RowPreferedLayout(1));
			fpnlMain.addControl(ftabPnlReportSet, new TableConstraints(1, 1,
					true, true));
			fpnlMain.addControl(buttonPanel, new TableConstraints(2, 1, false,
					true));

			this.getContentPane().add(fpnlMain);

		}

		/**
		 * �������ȡ����ť���
		 */
		public class ButtonPanel extends FFlowLayoutPanel {

			private static final long serialVersionUID = 1L;

			// ���ȫ����������
			private FButton fbtnAllCleateFilter;

			// �����������
			private FButton fbtnCleateFilter;

			// "���"��ť
			private FButton fbtnDown = null;

			// "ȡ��"��ť
			private FButton fbtnCancel = null;

			public ButtonPanel() {

				// ���ÿ�����ʾ
				this.setAlignment(FlowLayout.CENTER);
				fbtnAllCleateFilter = new FButton("fbtnAllCleateFilter",
						"���ȫ������");
				fbtnAllCleateFilter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// ��ʾ�Ƿ�ȷ��ɾ��
						if (JOptionPane.showConfirmDialog(ChangeSourceDlg.this,
								"���Ƿ�ȷ�����ȫ������?", "��ʾ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							return;
						}
						try {
							setWhereReadWrite.clearAllFilter();
							groupWhereSetPanel.refreshClear();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"�ɹ���", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"���ȫ�������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"���ȫ�������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				fbtnCleateFilter = new FButton("fbtnCleateFilter", "�������");
				fbtnCleateFilter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// ��ʾ�Ƿ�ȷ��ɾ��
						if (JOptionPane.showConfirmDialog(ChangeSourceDlg.this,
								"���Ƿ�ȷ���������?", "��ʾ", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							return;
						}
						try {
							setWhereReadWrite.clearOneReportFilter();
							groupWhereSetPanel.refreshClear();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"�ɹ���", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"��������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"��������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				fbtnDown = new FButton("fbtnDown", "ȷ ��");
				fbtnDown.addActionListener(new DownActionListener());
				fbtnCancel = new FButton("fbtnDown", "ȡ ��");
				fbtnCancel.addActionListener(new CancelActionListener());
				// ���
				this.addControl(fbtnAllCleateFilter, new TableConstraints(1, 1,
						true, false));
				// ȡ��
				this.addControl(fbtnCleateFilter, new TableConstraints(1, 1,
						true, false));

				// ���
				this.addControl(fbtnDown, new TableConstraints(1, 1, true,
						false));
				// ȡ��
				this.addControl(fbtnCancel, new TableConstraints(1, 1, true,
						false));
			}

			/**
			 * ��ɲ���
			 */
			private class DownActionListener implements ActionListener {

				public void actionPerformed(ActionEvent arg0) {
					List lstFieldEname = null;
					String sFieldFname = null;
					String info = null;
					try {
						// �ı����������
						groupWhereSetPanel.changeLevValue();
						info = "����������" + groupWhereSetPanel.getMsgInfo();

						// ����֧���ʽ���Դ
						if (changeSourcePanel != null) {
							// �õ�ѡ�е�enameֵ
							lstFieldEname = changeSourcePanel.getFieldEname();
							// �õ�ѡ���ʽ���Դ�ڵ����������
							sFieldFname = changeSourcePanel
									.getFundSourceFName();
							reportInfo.setAddOnsTitle(sFieldFname);
							// �ı��ͷ�ʽ���Դ��ʾ
							DataSet dsReportHeader = ReportHeaderOpe
									.changeDataSource(qrBudget
											.getDsReportHeader(), sFieldFname);
							// ˢ�±����ͷ��ʾ
							ReportHeaderShow.showNormalHeader(dsReportHeader,
									qrBudget);
							info += "  �ʽ���Դ��" + sFieldFname;
						}

						// ��������ʾ��Ϣ
						qrBudget.getFlblInfo().setText(info);

						// �õ��ԱȽ���Ա�����
						ConditionObj conditionObj = null;
						if (compareWherePanel != null) {
							conditionObj = compareWherePanel.getConditionObj();
						}

						ExeSearch exeSearch = new ExeSearch(qrBudget);
						exeSearch.doExeSearch(lstFieldEname, groupReport,
								conditionObj, qrBudget.getTabPaneReport()
										.getPara(false));

						SetWhereReadWrite.saveFile(reportInfo.getReportID(),
								groupWhereSetPanel.getLevIsTotalField(),
								lstFieldEname, conditionObj, sFieldFname, info);

					} catch (Exception e) {
						e.printStackTrace();
					}
					ChangeSourceDlg.this.setVisible(false);
				}
			}

			/**
			 * �رհ�ť�����ť�¼�
			 */
			private class CancelActionListener implements ActionListener {

				public void actionPerformed(ActionEvent arg0) {
					ChangeSourceDlg.this.setVisible(false);
				}
			}
		}

	}

	public void setChangeSourceDlg(ChangeSourceDlg changeSourceDlg) {
		this.changeSourceDlg = changeSourceDlg;
	}
}
