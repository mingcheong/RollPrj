package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.ArrayList;

import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.util.Global;

/**
 * 收支科目挂接明细保存
 * 

 * 
 */
public class AcctJjIncSave {
	private AcctJjInc acctJjInc = null;

	private ISysIaeStru sysIaeStruServ = null;

	public AcctJjIncSave(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
		this.sysIaeStruServ = acctJjInc.sysIaeStruServ;
	}

	public void save() throws Exception {

		if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 0) {
			String sSubType = acctJjInc.frdoSubTypeInc.getValue().toString();
			// 固定选择，判断是否选择了叶点节
			MyTreeNode[] myTreeNode = acctJjInc.ftreIncomeSubItem
					.getSelectedNodes(true);
			acctJjInc.dsAcctitem.fieldByName("subItem_type").setValue(sSubType);
			MyTreeNode node = acctJjInc.ftreeIncAcctitem.getSelectedNode();
			PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
			String sIN_BS_ID = pfNode.getValue().toString();
			String sAcctCodeInc = acctJjInc.dsAcctitem.fieldByName(
					"acct_code_inc").getString();
			int iDataSrc = acctJjInc.dsAcctitem.fieldByName("data_src")
					.getInteger();

			// 取和收费项目树信息
			String sBookmark = acctJjInc.dsIncome.toogleBookmark();
			ArrayList sSelectNodeCode = new ArrayList();
			ArrayList sSelectNodeId = new ArrayList();
			String sTollID;
			String sIncItemCode;
			if ("2".equals(sSubType)) {
				for (int i = 0; i < myTreeNode.length; i++) {
					acctJjInc.dsIncome
							.gotoBookmark(myTreeNode[i].getBookmark());
					sTollID = acctJjInc.dsIncome.fieldByName(
							"").getString();
					sIncItemCode = acctJjInc.dsIncome.fieldByName(
							"").getString();
					sSelectNodeId.add(sTollID);
					sSelectNodeCode.add(sIncItemCode);

					node = (MyTreeNode) myTreeNode[i].getParent();
					while (node != acctJjInc.ftreIncomeSubItem.getRoot()) { // 得到父节点的id
						acctJjInc.dsIncome.gotoBookmark(node.getBookmark());
						sTollID = acctJjInc.dsIncome.fieldByName("chr_id")
								.getString();
						sIncItemCode = acctJjInc.dsIncome.fieldByName(
							"").getString();
						// 判断编码是否重复
						boolean bEqualFlag = false;
						for (int k = 0; k < sSelectNodeId.size(); k++) {
							if ("".equals(sSelectNodeId.get(k)))
								continue;
							if (sTollID.equals(sSelectNodeId.get(k))) {
								bEqualFlag = true;
								break;
							}
						}
						if (!bEqualFlag) {
							sSelectNodeId.add(sTollID);
							sSelectNodeCode.add(sIncItemCode);
						}
						node = (MyTreeNode) node.getParent();
					}

				}
			}
			acctJjInc.dsIncome.gotoBookmark(sBookmark);
			sysIaeStruServ.saveIncAcctToIncItem(Global.loginYear,
					sSelectNodeId, sSelectNodeCode, sIN_BS_ID, sAcctCodeInc,
					sSubType, iDataSrc);
			acctJjInc.dsAcctitem.applyUpdate();

		} else if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 1) {
			String sSubType = acctJjInc.frdoSubTypeJJ.getValue().toString();
			acctJjInc.dsAcctJJ.fieldByName("subItem_type").setValue(sSubType);
			MyTreeNode node = acctJjInc.ftreeAcctJJ.getSelectedNode();
			PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
			String sBsiId = pfNode.getValue().toString();
			sysIaeStruServ.saveAcctJjToSubItem(Global.loginYear, sBsiId,
					sSubType);
			acctJjInc.dsAcctJJ.applyUpdate();

		}
	}
}
