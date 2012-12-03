package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * 收支科目挂接明细,经济科目树SelectionListener事件

 */
public class AcctJjTreeSelectionListener implements TreeSelectionListener {
	private AcctJjInc acctJjInc = null;

	private DataSet dsAcctJJ = null;

	public AcctJjTreeSelectionListener(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
		dsAcctJJ = acctJjInc.dsAcctJJ;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(
					acctJjInc.dsAcctJJ, acctJjInc, acctJjInc.ftreeAcctJJ);
			setActionStatus.setState(false, true);
			if (dsAcctJJ.isEmpty() || dsAcctJJ.bof() || dsAcctJJ.eof())
				return;

			int iSubType = 0;
			if (dsAcctJJ.fieldByName("SubItem_Type").getValue() != null)
				iSubType = dsAcctJJ.fieldByName("SubItem_Type").getInteger();

			acctJjInc.frdoSubTypeJJ.setValue(String.valueOf(iSubType));

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(acctJjInc, "显示明细信息错误，错误信息："
					+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
}
