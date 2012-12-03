package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.util.Global;

/**
 * ��֧��Ŀ�ҽ���ϸ�޸�
 * 
  * 
 */
public class AcctJjIncModify {
	private AcctJjInc acctJjInc = null;

	// ���ÿ�Ŀ
	private DataSet dsAcctJJ = null;

	// ����Ԥ���Ŀ
	private DataSet dsAcctitem = null;

	private CustomTree ftreeAcctJJ = null;

	private CustomTree ftreeIncAcctitem = null;

	private ISysIaeStru sysIaeStruServ;

	public AcctJjIncModify(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
		this.dsAcctJJ = acctJjInc.dsAcctJJ;
		this.dsAcctitem = acctJjInc.dsAcctitem;
		this.ftreeAcctJJ = acctJjInc.ftreeAcctJJ;
		this.ftreeIncAcctitem = acctJjInc.ftreeIncAcctitem;
		this.sysIaeStruServ = acctJjInc.sysIaeStruServ;
	}

	public boolean modify() throws HeadlessException, Exception {
		if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 0) {
			MyTreeNode node = ftreeIncAcctitem.getSelectedNode();
			if (node == null) {
				JOptionPane.showMessageDialog(acctJjInc, "��ѡ��һ��Ҷ�ڵ㣡", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			String sName = node.getUserObject().toString();
			// �ж��Ƿ���ʹ��
			InfoPackage infoPackage = sysIaeStruServ.judgeIncEnableModify(node
					.sortKeyValue(), sName, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(acctJjInc, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			dsAcctitem.edit();
		} else if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 1) {

			MyTreeNode node = ftreeAcctJJ.getSelectedNode();
			if (node == null) {
				JOptionPane.showMessageDialog(acctJjInc, "��ѡ��һ��Ҷ�ڵ㣡", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
			String sBsiId = pfNode.getValue().toString();
			String sName = node.getUserObject().toString();
			acctJjInc.sBSI_ID = sBsiId;
			InfoPackage infoPackage = sysIaeStruServ.judgeJjEnableModify(
					sBsiId, sName, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(acctJjInc, infoPackage
						.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			dsAcctJJ.edit();

		}
		return true;
	}
}
