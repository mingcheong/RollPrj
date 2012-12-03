package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * ��֧��Ŀ�ҽ���ϸ,���ÿ�Ŀ��SelectionListener�¼�

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
			// ���ð�ť״̬
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
			JOptionPane.showMessageDialog(acctJjInc, "��ʾ��ϸ��Ϣ���󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
