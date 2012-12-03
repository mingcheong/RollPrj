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
 * Title:变换数据来源
 * </p>
 * <p>
 * Description:变换数据来源
 * </p>
 * <p>
 
 */
public class SetWhereAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	private QrBudget qrBudget;

	// 分组条件
	private GroupWhereSetPanel groupWhereSetPanel;

	// 更换资金来源
	private ChangeSourcePanel changeSourcePanel;

	// 对比条件
	private CompareWherePanel compareWherePanel;

	private GroupReport groupReport;

	private ChangeSourceDlg changeSourceDlg;

	// private String sOldReportID;

	private ReportInfo reportInfo;

	// 参数文件读写
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
	 * 变换资金来源对话框
	 * 
	 */
	private class ChangeSourceDlg extends FDialog {

		private static final long serialVersionUID = 1L;

		public ChangeSourceDlg() {
			super(Global.mainFrame);
			this.setSize(600, 450);
			this.setResizable(false);
			this.setModal(true);
			this.setTitle("条件设置");
			// 初始化界面
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "显示变换资金来源界面发生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * 界面初始化
		 * 
		 * @throws Exception
		 * 
		 */
		private void init() throws Exception {

			IDefineReport definReportServ = DefineReportI.getMethod();
			groupReport = ReportPanel.getGroupReport(reportInfo.getReportID(),
					definReportServ);
			// 校验原定义报表summaryIndex没有设置值问题
			SummaryIndexUnt.checkGroupDetail(groupReport);

			setWhereReadWrite = new SetWhereReadWrite(reportInfo.getReportID());

			// 定义条件多页面面板
			FTabbedPane ftabPnlReportSet = new FTabbedPane();
			groupWhereSetPanel = new GroupWhereSetPanel(groupReport);
			boolean isSame = false;
			// 报表参数是否存在
			if (setWhereReadWrite.isReportExists()) {
				// 得到报表lstLevIsTotalField
				List lstLevIsTotalField = groupWhereSetPanel
						.getLevIsTotalField();
				// 判断本地文件参数与报表是否一致
				isSame = setWhereReadWrite.checkFilePara(lstLevIsTotalField);
				if (isSame) {// 一致
					// 根据本地参数文件内容，更改lstLevIsTotalField值
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
				}
			}
			groupWhereSetPanel.refresh();

			ftabPnlReportSet.addControl("分组条件设置", groupWhereSetPanel);
			if (Common.estimate(reportInfo.getChangeSource())) {
				List lstPfsCode = null;
				if (isSame) {
					lstPfsCode = setWhereReadWrite.getPfsCode();
				}
				changeSourcePanel = new ChangeSourcePanel(lstPfsCode);
				ftabPnlReportSet.addControl("资金来源选择", changeSourcePanel);
			}
			if (Common.estimate(reportInfo.getCompareFlag())) {
				ConditionObj conditionObj = null;
				if (isSame) {
					conditionObj = setWhereReadWrite.getConditionObj();
				}
				compareWherePanel = new CompareWherePanel(qrBudget,
						conditionObj);
				ftabPnlReportSet.addControl("对比分析设置", compareWherePanel);
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
		 * 定义完成取消按钮面板
		 */
		public class ButtonPanel extends FFlowLayoutPanel {

			private static final long serialVersionUID = 1L;

			// 清除全部保存条件
			private FButton fbtnAllCleateFilter;

			// 清除保存条件
			private FButton fbtnCleateFilter;

			// "完成"按钮
			private FButton fbtnDown = null;

			// "取消"按钮
			private FButton fbtnCancel = null;

			public ButtonPanel() {

				// 设置靠右显示
				this.setAlignment(FlowLayout.CENTER);
				fbtnAllCleateFilter = new FButton("fbtnAllCleateFilter",
						"清除全部条件");
				fbtnAllCleateFilter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 提示是否确定删除
						if (JOptionPane.showConfirmDialog(ChangeSourceDlg.this,
								"您是否确认清除全部条件?", "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							return;
						}
						try {
							setWhereReadWrite.clearAllFilter();
							groupWhereSetPanel.refreshClear();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"成功！", "提示",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"清除全部条件发生错误，错误信息：" + e1.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"清除全部条件发生错误，错误信息：" + e1.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				fbtnCleateFilter = new FButton("fbtnCleateFilter", "清除条件");
				fbtnCleateFilter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 提示是否确定删除
						if (JOptionPane.showConfirmDialog(ChangeSourceDlg.this,
								"您是否确认清除条件?", "提示", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							return;
						}
						try {
							setWhereReadWrite.clearOneReportFilter();
							groupWhereSetPanel.refreshClear();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"成功！", "提示",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"清除条件发生错误，错误信息：" + e1.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(Global.currentPanel,
									"清除条件发生错误，错误信息：" + e1.getMessage(), "提示",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				fbtnDown = new FButton("fbtnDown", "确 定");
				fbtnDown.addActionListener(new DownActionListener());
				fbtnCancel = new FButton("fbtnDown", "取 消");
				fbtnCancel.addActionListener(new CancelActionListener());
				// 完成
				this.addControl(fbtnAllCleateFilter, new TableConstraints(1, 1,
						true, false));
				// 取消
				this.addControl(fbtnCleateFilter, new TableConstraints(1, 1,
						true, false));

				// 完成
				this.addControl(fbtnDown, new TableConstraints(1, 1, true,
						false));
				// 取消
				this.addControl(fbtnCancel, new TableConstraints(1, 1, true,
						false));
			}

			/**
			 * 完成操作
			 */
			private class DownActionListener implements ActionListener {

				public void actionPerformed(ActionEvent arg0) {
					List lstFieldEname = null;
					String sFieldFname = null;
					String info = null;
					try {
						// 改变更分组条件
						groupWhereSetPanel.changeLevValue();
						info = "分组条件：" + groupWhereSetPanel.getMsgInfo();

						// 更改支出资金来源
						if (changeSourcePanel != null) {
							// 得到选中的ename值
							lstFieldEname = changeSourcePanel.getFieldEname();
							// 得到选中资金来源节点的中文名称
							sFieldFname = changeSourcePanel
									.getFundSourceFName();
							reportInfo.setAddOnsTitle(sFieldFname);
							// 改变表头资金来源显示
							DataSet dsReportHeader = ReportHeaderOpe
									.changeDataSource(qrBudget
											.getDsReportHeader(), sFieldFname);
							// 刷新报表表头显示
							ReportHeaderShow.showNormalHeader(dsReportHeader,
									qrBudget);
							info += "  资金来源：" + sFieldFname;
						}

						// 主界面提示信息
						qrBudget.getFlblInfo().setText(info);

						// 得到对比界面对比条件
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
			 * 关闭按钮点击按钮事件
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
