/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ui.ChangePayoutTypeUI;
import gov.nbcs.rp.sys.sysiaestru.ui.PayOutType;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * <p>
 * Title:支出项目类别对经济科目，改变经济科目对应的支出项目类别
 * </p>
 * <p>
 * Description:支出项目类别对经济科目，改变经济科目对应的支出项目类别
 * </p>
 * <p>

 */
public class ChangePayoutKindAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	/** 支出项目类别类 */
	private PayOutType payOutType;

	private ChangePayoutTypeUI changePayoutTypeUI;

	public void actionPerformed(ActionEvent evt) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof PayOutType) {
			payOutType = (PayOutType) modulePanel;
		}
		if (payOutType == null)
			return;

		// 经济科目改变支出项目前检查
		if (!checkBefore())
			return;

		changePayoutTypeUI = new ChangePayoutTypeUI(payOutType
				.getFtreePayOutType().getDataSet()) {
			private static final long serialVersionUID = 1L;

			// 检查是否新的支出项目类别与原支出项目类别关系
			public String checkChoice(String payoutKindCode) {
				return ChangePayoutKindAction.this.check(payoutKindCode);
			}
		};
		Tools.centerWindow(changePayoutTypeUI);
		changePayoutTypeUI.setVisible(true);
		if (changePayoutTypeUI.getResultValue() != null) {
			try {
				// 改变支出项目类别处理
				changePayoutOpe();
				payOutType.setChangeFlag(true);
				JOptionPane.showMessageDialog(Global.mainFrame,
						"改变支出项目类别操作成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"改变支出项目类别发生错误，错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		changePayoutTypeUI.dispose();
	}

	/**
	 * 改变支出项目类别处理
	 * 
	 * @throws Exception
	 * 
	 */
	private void changePayoutOpe() throws Exception {
		// 得到新支出项目类别编码
		String sNewPayoutKindCode = changePayoutTypeUI.getResultValue().get(
				IPayOutType.PAYOUT_KIND_CODE).toString();

		// 得到新支出项目类别名称
		String sNewPayoutKindName = changePayoutTypeUI.getResultValue().get(
				IPayOutType.PAYOUT_KIND_NAME).toString();

		// 得到原支出项目类别
		DataSet dsPayoutKind = payOutType.getFtreePayOutType().getDataSet();
		String sOldPayoutKindCode = dsPayoutKind.fieldByName(
				IPayOutType.PAYOUT_KIND_CODE).getString();

		// 得到经济科目编码
		String acctJjCode = null;
		DataSet dsAcctJj = payOutType.getFtreeAcctJJ().getDataSet();
		MyTreeNode node = payOutType.getFtreeAcctJJ().getSelectedNode();
		if (dsAcctJj.gotoBookmark(node.getBookmark())) {
			acctJjCode = dsAcctJj.fieldByName(IPubInterface.ACCT_CODE_JJ)
					.getString();
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"根据选中结点游标定位经济科目出错!请与管理员联系。", "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 经济科目改变支出项目类别,改变数据库值
		payOutType.getSysIaeStruServ().changePayOutKind(sNewPayoutKindCode,
				sNewPayoutKindName, sOldPayoutKindCode, acctJjCode);
		// 刷新当前选中支出项目对经济科目树
		MyPfNode pNode = (MyPfNode) node.getUserObject();
		pNode.setIsSelect(false);
		((DefaultTreeModel) payOutType.getFtreeAcctJJ().getModel())
				.nodeChanged(node);
	}

	/**
	 * 改变支出项目前检查
	 * 
	 * @return
	 */
	private boolean checkBefore() {
		// 判断是否选择了经济科目
		MyTreeNode node = payOutType.getFtreeAcctJJ().getSelectedNode();
		if (node == null) {
			JOptionPane
					.showMessageDialog(Global.mainFrame, "请选择需改变支出项目类别的经济科目!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// 判断选中的经济科目是否是叶节点
		if (!node.isLeaf()) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"改变支出项目类别，只支持末级经济科目，请选择末级经济科目!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		MyPfNode pNode = (MyPfNode) node.getUserObject();
		if (pNode.getSelectStat() != MyPfNode.SELECT) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"经济科目未选中状态，不需改变支出项目类别！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * 选中支出项目类别后检查
	 * 
	 * @param payoutKindCode支出项目类别编码
	 * @return
	 */
	private String check(String payoutKindCode) {

		if (payoutKindCode == null)
			return "选中的支出项目类别编码为空。";
		MyTreeNode node = payOutType.getFtreePayOutType().getSelectedNode();
		// 根据节点bookmark定位DataSet记录
		try {
			DataSet dsPayoutKind = payOutType.getFtreePayOutType().getDataSet();
			if (!dsPayoutKind.gotoBookmark(node.getBookmark())) {
				return "根据树节点游标定位支出项目类别数据集出错。";
			}
			// 得到原支出项目类别
			String sOldPayoutKindCode = dsPayoutKind.fieldByName(
					IPayOutType.PAYOUT_KIND_CODE).getString();
			// 1.检查是否选中的是原支出项目类别
			if (payoutKindCode.equals(sOldPayoutKindCode)) {
				return "选中是原支出项目类别，不需改变！请选择其他支出项目类别！";
			}
			// 2.判断新的支出项目类别是否已包含此经济科目（选中要改变支出项目类别的经济科目）
			MyTreeNode acctJjNode = payOutType.getFtreeAcctJJ()
					.getSelectedNode();
			DataSet dsAcctJj = payOutType.getFtreeAcctJJ().getDataSet();
			if (!dsAcctJj.gotoBookmark(acctJjNode.getBookmark())) {
				return "根据树节点游标定位经济科目数据集出错。";
			}
			String acctJjCode = dsAcctJj
					.fieldByName(IPubInterface.ACCT_CODE_JJ).getString();

			if (payOutType.getSysIaeStruServ().JudgeAcctJJExist(payoutKindCode,
					acctJjCode)) {
				return "选中的支出项目类别与该经济科目已设置对应关系,请选择其他支出项目类别。";
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"改变支出项目类别发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
		return "";

	}

	/**
	 * 设置PayOutType值
	 * 
	 * @param payOutType
	 *            支出项目类别界面
	 */
	public void setPayOutType(PayOutType payOutType) {
		this.payOutType = payOutType;
	}

}
