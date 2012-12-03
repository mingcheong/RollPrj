package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * 资金来源对应收入修改

 * 
 */
public class PFSToIncItemModify {
	private PFSToIncItem pfsToIncItem;

	// 数据库接口
	private ISysIaeStru sysIaeStruServ;

	public PFSToIncItemModify(PFSToIncItem pfsToIncItem) {
		this.pfsToIncItem = pfsToIncItem;
		this.sysIaeStruServ = pfsToIncItem.sysIaeStruServ;
	}

	public void modify() throws HeadlessException, Exception {
		// int iCurBatchNo = PubInterfaceStub.getMethod().getCurBatchNO();
		// int iPayOutUpLoadBatch = PubInterfaceStub.getMethod()
		// .getPayoutUploadBatch();
		// if (iCurBatchNo >= iPayOutUpLoadBatch) {
		// JOptionPane.showMessageDialog(pfsToIncItem,
		// "当前批次大于等于支出预算表的填报批次，不允许修改!", "提示",
		// JOptionPane.INFORMATION_MESSAGE);
		// return;
		// }

		// 判断资金来源与收入项目是否设置取数关系，如设置取数关系，提示是否确定修改
		int count = pfsToIncItem.ftreeIncItem.getSelectedNodeCount(true);
		if (count == 1) {
			// 得到选中的收入栏目数据来源
			List lstIsInc = SysUntPub.getLeafNodeCode(
					pfsToIncItem.ftreeIncItem, pfsToIncItem.ftreeIncItem
							.getDataSet(), IIncType.IS_INC);
			if (lstIsInc != null && !lstIsInc.isEmpty()) {
				// 判断是否从支出预算表取数
				if (Integer.parseInt(lstIsInc.get(0).toString()) == 2) {
					if (JOptionPane.showConfirmDialog(pfsToIncItem,
							"收入栏目已设置从支出预算表该支出资金来源取数，确定修改此关系吗？", "提示",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
						return;
				}
			}

		}

		String sEname = pfsToIncItem.dsPayOutFS.fieldByName("PFS_Ename")
				.getString();
		if (!sysIaeStruServ.judgePfsEnameUse(sEname, Global.loginYear)) {
			if (JOptionPane.showConfirmDialog(pfsToIncItem,
					"修改该对应关系将有可能导致现有的收入与支出数据不平衡，是否继续？", "提示",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
		}
		pfsToIncItem.dsPayOutFS.edit();
	}
}
