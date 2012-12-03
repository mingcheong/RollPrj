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
 * Title:֧����Ŀ���Ծ��ÿ�Ŀ���ı侭�ÿ�Ŀ��Ӧ��֧����Ŀ���
 * </p>
 * <p>
 * Description:֧����Ŀ���Ծ��ÿ�Ŀ���ı侭�ÿ�Ŀ��Ӧ��֧����Ŀ���
 * </p>
 * <p>

 */
public class ChangePayoutKindAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	/** ֧����Ŀ����� */
	private PayOutType payOutType;

	private ChangePayoutTypeUI changePayoutTypeUI;

	public void actionPerformed(ActionEvent evt) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof PayOutType) {
			payOutType = (PayOutType) modulePanel;
		}
		if (payOutType == null)
			return;

		// ���ÿ�Ŀ�ı�֧����Ŀǰ���
		if (!checkBefore())
			return;

		changePayoutTypeUI = new ChangePayoutTypeUI(payOutType
				.getFtreePayOutType().getDataSet()) {
			private static final long serialVersionUID = 1L;

			// ����Ƿ��µ�֧����Ŀ�����ԭ֧����Ŀ����ϵ
			public String checkChoice(String payoutKindCode) {
				return ChangePayoutKindAction.this.check(payoutKindCode);
			}
		};
		Tools.centerWindow(changePayoutTypeUI);
		changePayoutTypeUI.setVisible(true);
		if (changePayoutTypeUI.getResultValue() != null) {
			try {
				// �ı�֧����Ŀ�����
				changePayoutOpe();
				payOutType.setChangeFlag(true);
				JOptionPane.showMessageDialog(Global.mainFrame,
						"�ı�֧����Ŀ�������ɹ���", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"�ı�֧����Ŀ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		changePayoutTypeUI.dispose();
	}

	/**
	 * �ı�֧����Ŀ�����
	 * 
	 * @throws Exception
	 * 
	 */
	private void changePayoutOpe() throws Exception {
		// �õ���֧����Ŀ������
		String sNewPayoutKindCode = changePayoutTypeUI.getResultValue().get(
				IPayOutType.PAYOUT_KIND_CODE).toString();

		// �õ���֧����Ŀ�������
		String sNewPayoutKindName = changePayoutTypeUI.getResultValue().get(
				IPayOutType.PAYOUT_KIND_NAME).toString();

		// �õ�ԭ֧����Ŀ���
		DataSet dsPayoutKind = payOutType.getFtreePayOutType().getDataSet();
		String sOldPayoutKindCode = dsPayoutKind.fieldByName(
				IPayOutType.PAYOUT_KIND_CODE).getString();

		// �õ����ÿ�Ŀ����
		String acctJjCode = null;
		DataSet dsAcctJj = payOutType.getFtreeAcctJJ().getDataSet();
		MyTreeNode node = payOutType.getFtreeAcctJJ().getSelectedNode();
		if (dsAcctJj.gotoBookmark(node.getBookmark())) {
			acctJjCode = dsAcctJj.fieldByName(IPubInterface.ACCT_CODE_JJ)
					.getString();
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"����ѡ�н���α궨λ���ÿ�Ŀ����!�������Ա��ϵ��", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// ���ÿ�Ŀ�ı�֧����Ŀ���,�ı����ݿ�ֵ
		payOutType.getSysIaeStruServ().changePayOutKind(sNewPayoutKindCode,
				sNewPayoutKindName, sOldPayoutKindCode, acctJjCode);
		// ˢ�µ�ǰѡ��֧����Ŀ�Ծ��ÿ�Ŀ��
		MyPfNode pNode = (MyPfNode) node.getUserObject();
		pNode.setIsSelect(false);
		((DefaultTreeModel) payOutType.getFtreeAcctJJ().getModel())
				.nodeChanged(node);
	}

	/**
	 * �ı�֧����Ŀǰ���
	 * 
	 * @return
	 */
	private boolean checkBefore() {
		// �ж��Ƿ�ѡ���˾��ÿ�Ŀ
		MyTreeNode node = payOutType.getFtreeAcctJJ().getSelectedNode();
		if (node == null) {
			JOptionPane
					.showMessageDialog(Global.mainFrame, "��ѡ����ı�֧����Ŀ���ľ��ÿ�Ŀ!",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// �ж�ѡ�еľ��ÿ�Ŀ�Ƿ���Ҷ�ڵ�
		if (!node.isLeaf()) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"�ı�֧����Ŀ���ֻ֧��ĩ�����ÿ�Ŀ����ѡ��ĩ�����ÿ�Ŀ!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		MyPfNode pNode = (MyPfNode) node.getUserObject();
		if (pNode.getSelectStat() != MyPfNode.SELECT) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"���ÿ�Ŀδѡ��״̬������ı�֧����Ŀ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * ѡ��֧����Ŀ������
	 * 
	 * @param payoutKindCode֧����Ŀ������
	 * @return
	 */
	private String check(String payoutKindCode) {

		if (payoutKindCode == null)
			return "ѡ�е�֧����Ŀ������Ϊ�ա�";
		MyTreeNode node = payOutType.getFtreePayOutType().getSelectedNode();
		// ���ݽڵ�bookmark��λDataSet��¼
		try {
			DataSet dsPayoutKind = payOutType.getFtreePayOutType().getDataSet();
			if (!dsPayoutKind.gotoBookmark(node.getBookmark())) {
				return "�������ڵ��α궨λ֧����Ŀ������ݼ�����";
			}
			// �õ�ԭ֧����Ŀ���
			String sOldPayoutKindCode = dsPayoutKind.fieldByName(
					IPayOutType.PAYOUT_KIND_CODE).getString();
			// 1.����Ƿ�ѡ�е���ԭ֧����Ŀ���
			if (payoutKindCode.equals(sOldPayoutKindCode)) {
				return "ѡ����ԭ֧����Ŀ��𣬲���ı䣡��ѡ������֧����Ŀ���";
			}
			// 2.�ж��µ�֧����Ŀ����Ƿ��Ѱ����˾��ÿ�Ŀ��ѡ��Ҫ�ı�֧����Ŀ���ľ��ÿ�Ŀ��
			MyTreeNode acctJjNode = payOutType.getFtreeAcctJJ()
					.getSelectedNode();
			DataSet dsAcctJj = payOutType.getFtreeAcctJJ().getDataSet();
			if (!dsAcctJj.gotoBookmark(acctJjNode.getBookmark())) {
				return "�������ڵ��α궨λ���ÿ�Ŀ���ݼ�����";
			}
			String acctJjCode = dsAcctJj
					.fieldByName(IPubInterface.ACCT_CODE_JJ).getString();

			if (payOutType.getSysIaeStruServ().JudgeAcctJJExist(payoutKindCode,
					acctJjCode)) {
				return "ѡ�е�֧����Ŀ�����þ��ÿ�Ŀ�����ö�Ӧ��ϵ,��ѡ������֧����Ŀ���";
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"�ı�֧����Ŀ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
		return "";

	}

	/**
	 * ����PayOutTypeֵ
	 * 
	 * @param payOutType
	 *            ֧����Ŀ������
	 */
	public void setPayOutType(PayOutType payOutType) {
		this.payOutType = payOutType;
	}

}
