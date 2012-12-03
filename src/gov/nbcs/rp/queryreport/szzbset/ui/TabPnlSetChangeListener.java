package gov.nbcs.rp.queryreport.szzbset.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.sys.sysiaestru.ui.SysIaeStruI;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;

public class TabPnlSetChangeListener implements ChangeListener {
	SzzbSet szzbSet;

	JFrame curFrame;

	public TabPnlSetChangeListener(SzzbSet szzbSet) {
		this.szzbSet = szzbSet;
		if (szzbSet.frame == null)
			curFrame = Global.mainFrame;
		else
			curFrame = szzbSet.frame;
	}

	public void stateChanged(ChangeEvent e) {
		if (szzbSet.maskPageChange)
			return;
		// 如果第一个界面还没有保存，则不允许转入
		if (Common.isNullStr(szzbSet.getReportID())) {
			new MessageBox(curFrame, "当前新增报表还未保存，请保存后再切换设置界面!",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
			szzbSet.maskPageChange = true;
			szzbSet.setActivePage(SzzbSet.PAGE_BASE);
			szzbSet.maskPageChange = false;
			return;

		}
		// 如果第一个界面已经修改了，则要求保存
		if (szzbSet.isChanged()) {
			MessageBox mb = new MessageBox(curFrame, "数据已修改，要保存所做的修改吗?",
					MessageBox.MESSAGE, MessageBox.BUTTON_OK
							| MessageBox.BUTTON_CANCEL);
			mb.show();
			if (mb.result == MessageBox.OK) {
				szzbSet.maskPageChange = true;

				if (szzbSet.ftabPnlSet.getSelectedIndex() != SzzbSet.PAGE_BASE) {
					szzbSet.saveBaseInfo();
					szzbSet.setActivePage(SzzbSet.PAGE_BASE);
					if (szzbSet.isNeedFresh)
						try {
							szzbSet.cellPanel.dispData(szzbSet.getReportID());
						} catch (Exception e1) {
							// TODO 自动生成 catch 块
							e1.printStackTrace();
						}

				} else {
					szzbSet.saveColInfo();
					szzbSet.setActivePage(SzzbSet.PAGE_COL);
				}

				szzbSet.maskPageChange = false;

			} else
				szzbSet.refreshData();

		}
		if (szzbSet.isRowOrColChanged()) {
			// 保存设置信息
			String sErr = szzbSet.cellPanel.saveCols();
			if (!Common.isNullStr(sErr)) {
				JOptionPane.showMessageDialog(szzbSet, sErr, "提示",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			szzbSet.setRowOrColChanged(false);
		}

		if ((szzbSet.ftabPnlSet.getSelectedIndex() == 1 && szzbSet.pnlCellSet.dsIncType == null)) {
			UpdateProgressBar updateProgressBar = new UpdateProgressBar();
			updateProgressBar.start();

		}
		// 如果表元素变化则要重新显示设计表格

	}

	private class UpdateProgressBar {
		// 定义进度条
		private ProgressBar pro;

		public UpdateProgressBar() {

		}

		public void start() {

			pro = ProgressBar.createRefreshing(curFrame, "正在加载数据,请稍候......");
			new Thread() {
				public void run() {
					try {
						// 收入项目类别
						szzbSet.pnlCellSet.dsIncType = SysIaeStruI.getMethod()
								.getIncTypeTre(
										Integer.parseInt(Global.loginYear));
						szzbSet.pnlCellSet.ftreeIncType
								.setDataSet(szzbSet.pnlCellSet.dsIncType);
						szzbSet.pnlCellSet.ftreeIncType.reset();
						szzbSet.pnlCellSet.ftreeIncType.getModel()
								.addTreeModelListener(
										new DefaultTreeModelListener() {

											public void treeNodesChanged(
													TreeModelEvent e) {
												szzbSet.pnlCellSet.incomingPanel
														.treeNodesChanged(e);

											}
										});

						// 支出资金来源
						szzbSet.pnlCellSet.dsPfs = SysIaeStruI.getMethod()
								.getPayOutFSTre(
										Integer.parseInt(Global.loginYear));
						szzbSet.pnlCellSet.ftreePayOutFS
								.setDataSet(szzbSet.pnlCellSet.dsPfs);
						szzbSet.pnlCellSet.ftreePayOutFS.reset();
						szzbSet.pnlCellSet.ftreePayOutFS.getModel()
								.addTreeModelListener(
										new DefaultTreeModelListener() {

											public void treeNodesChanged(
													TreeModelEvent e) {
												szzbSet.pnlCellSet.payOutPanel
														.treeNodesChangedPayOut2(e);

											}
										});
						// 支出项目类别
						szzbSet.pnlCellSet.dsPayOutKind = SzzbSetI
								.getMethod().getPayOutKind(
										Integer.parseInt(Global.loginYear));
						szzbSet.pnlCellSet.ftreePayoutKind
								.setDataSet(szzbSet.pnlCellSet.dsPayOutKind);
						szzbSet.pnlCellSet.ftreePayoutKind.reset();
						szzbSet.pnlCellSet.ftreePayoutKind.getModel()
								.addTreeModelListener(
										new DefaultTreeModelListener() {

											public void treeNodesChanged(
													TreeModelEvent e) {
												szzbSet.pnlCellSet.payOutPanel
														.treeNodesChangedPayoutKind(e);

											}
										});

						// 功能科目
						szzbSet.pnlCellSet.dsAcctFunc = PubInterfaceStub
								.getMethod().getAcctFunc(
										"set_year=" + Global.loginYear);
						szzbSet.pnlCellSet.ftreeAcctFunc
								.setDataSet(szzbSet.pnlCellSet.dsAcctFunc);
						szzbSet.pnlCellSet.ftreeAcctFunc.reset();
						szzbSet.pnlCellSet.ftreeAcctFunc.getModel()
								.addTreeModelListener(
										new DefaultTreeModelListener() {

											public void treeNodesChanged(
													TreeModelEvent e) {
												szzbSet.pnlCellSet.payOutPanel
														.treeNodesChangedAccFunc(e);

											}
										});

						// 经济科目
						szzbSet.pnlCellSet.dsAcctEconomy = PubInterfaceStub
								.getMethod().getAcctEconomy(
										"set_year=" + Global.loginYear);
						szzbSet.pnlCellSet.ftreeAcctEconomy
								.setDataSet(szzbSet.pnlCellSet.dsAcctEconomy);
						szzbSet.pnlCellSet.ftreeAcctEconomy.reset();
						szzbSet.pnlCellSet.ftreeAcctEconomy.getModel()
								.addTreeModelListener(
										new DefaultTreeModelListener() {

											public void treeNodesChanged(
													TreeModelEvent e) {
												szzbSet.pnlCellSet.payOutPanel
														.treeNodesChangedJJ(e);

											}
										});

					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(curFrame,
								"显示信息发生错误，错误信息：" + e.getMessage(), "提示",
								JOptionPane.ERROR_MESSAGE);
					} finally {
						if (pro != null)
							pro.dispose();
					}
				}
			}.start();
			if (pro != null)
				pro.show();
		}
	}

	class DefaultTreeModelListener implements TreeModelListener {

		public void treeNodesChanged(TreeModelEvent e) {
			// TODO 自动生成方法存根

		}

		public void treeNodesInserted(TreeModelEvent e) {
			// TODO 自动生成方法存根

		}

		public void treeNodesRemoved(TreeModelEvent e) {
			// TODO 自动生成方法存根

		}

		public void treeStructureChanged(TreeModelEvent e) {
			// TODO 自动生成方法存根

		}

	}

}
