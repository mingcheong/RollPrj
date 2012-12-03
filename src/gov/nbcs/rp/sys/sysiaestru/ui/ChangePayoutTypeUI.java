/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:经济科目改变支出项目类别，选择支出项目类别对话框
 * </p>
 * <p>
 * Description:经济科目改变支出项目类别，选择支出项目类别对话框
 * </p>
 * <p>

 */
public abstract class ChangePayoutTypeUI extends FDialog {

	private static final long serialVersionUID = 1L;

	/** 支出项目类别dataset */
	protected DataSet dsPayoutKind;

	/** 支出项目类别树 */
	private CustomTree trePaypoutKind;

	/** 返回选中的支出项目类别记录 */
	private Map resultValue = null;

	public ChangePayoutTypeUI(DataSet dsPayoutKind) {

		super(Global.mainFrame);
		this.setSize(400, 360);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);
		this.setTitle("选择支出项目类别");

		this.dsPayoutKind = (DataSet) dsPayoutKind.clone();
		try {
			// 初始化界面
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "显示界面发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 初始化界面
	 * 
	 * @throws Exception
	 * 
	 */
	private void init() throws Exception {
		// FLabel flblTitle = new FLabel();
		// flblTitle.setTitle("选择支出项目类别");
		// flblTitle.setHorizontalAlignment(FLabel.CENTER);

		trePaypoutKind = new CustomTree(IPayOutType.PAYOUTKIND_ROOT,
				this.dsPayoutKind, IPayOutType.LVL_ID, ISysIaeStru.NAME,
				IPayOutType.PAR_ID, null);
		trePaypoutKind.setSortKey(IPayOutType.LVL_ID);
		trePaypoutKind.expandAll();
		FScrollPane fScroll = new FScrollPane(trePaypoutKind);
		FPanel fPanelReportCol = new FPanel();
		fPanelReportCol.setTopInset(5);
		fPanelReportCol.setLeftInset(5);
		fPanelReportCol.setRightInset(5);
		fPanelReportCol.setLayout(new RowPreferedLayout(1));
		fPanelReportCol.addControl(fScroll);

		// 按钮Panel
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		FButton fbtnOk = new FButton("fbtnOk", "确定");
		fbtnOk.addActionListener(new OkActionListener());

		FButton fbtnCancel = new FButton("fbtnCancel", "取消");
		fbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChangePayoutTypeUI.this.dispose();
			}
		});
		choosePanel.addControl(fbtnOk);
		choosePanel.addControl(fbtnCancel);

		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(15);
		this.getContentPane().setLayout(rLay);
		// this.getContentPane().add(flblTitle,
		// new TableConstraints(1, 1, false, true));
		this.getContentPane().add(fPanelReportCol,
				new TableConstraints(1, 1, true, true));
		this.getContentPane().add(choosePanel,
				new TableConstraints(2, 1, false, true));
	}

	/**
	 * 确认按钮事件
	 * 
	 */
	private class OkActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (!check()) {
				return;
			}
			try {
				resultValue = dsPayoutKind.getOriginData();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"改变支出项目类别发生错误，错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
			ChangePayoutTypeUI.this.dispose();
		}
	}

	/**
	 * 检查是否新的支出项目类别与原支出项目类别关系
	 * 
	 * @param payoutKindCode
	 */
	public abstract String checkChoice(String payoutKindCode);

	/**
	 * 检查选中的支出项目类别
	 * 
	 * @return
	 */
	private boolean check() {

		MyTreeNode node = trePaypoutKind.getSelectedNode();
		if (node == trePaypoutKind.getRoot() || !node.isLeaf()) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请末级支出项目类别!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try {
			// 根据节点bookmark定位DataSet记录
			if (!dsPayoutKind.gotoBookmark(node.getBookmark())) {
				return false;
			}
			String payoutKindCode = dsPayoutKind.fieldByName(
					IPayOutType.PAYOUT_KIND_CODE).getString();
			// 检查是否新的支出项目类别与原支出项目类别关系
			String strErr = checkChoice(payoutKindCode);
			if (!Common.isNullStr(strErr)) {
				JOptionPane.showMessageDialog(Global.mainFrame, strErr, "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"改变支出项目类别发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}

		return true;
	}

	public Map getResultValue() {
		return resultValue;
	}

}
