package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收支科目挂接明细,收入预算科目树SelectionListener事件
 * 
 * </p>
 * <p>
 * Description:收支科目挂接明细,收入预算科目树SelectionListener事件

 */
public class AcctIncTreeSelectionListener implements TreeSelectionListener {
	AcctJjInc acctJjInc;

	// 收费项目
	DataSet dsAcctitem;

	public AcctIncTreeSelectionListener(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
		dsAcctitem = acctJjInc.dsAcctitem;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					acctJjInc.dsAcctitem, acctJjInc, acctJjInc.ftreeIncAcctitem);
			setActionStatus.setState(false, true);
			if (dsAcctitem.isEmpty() || dsAcctitem.bof() || dsAcctitem.eof())
				return;
			int iSubType = dsAcctitem.fieldByName("SubItem_Type").getInteger();
			acctJjInc.frdoSubTypeInc.setValue(String.valueOf(iSubType));
			// 判断是否是固定选择，显示树
			if (iSubType == 2) {
				// 显示收费项目树
				ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();
				String sIN_BS_ID = dsAcctitem.fieldByName(
						"").getString();
				String[] sID = sysIaeStruServ.getIncItemWithCode(sIN_BS_ID,
						Global.loginYear);
				SetSelectTree.setIsCheck(acctJjInc.ftreIncomeSubItem, sID,
						true, true);
			} else {
				// 清空支出子项目树
				SetSelectTree.setIsNoCheck(acctJjInc.ftreIncomeSubItem);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(acctJjInc, "显示明细信息错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
}
