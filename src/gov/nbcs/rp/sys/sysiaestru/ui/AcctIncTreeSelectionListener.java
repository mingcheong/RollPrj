package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:��֧��Ŀ�ҽ���ϸ,����Ԥ���Ŀ��SelectionListener�¼�
 * 
 * </p>
 * <p>
 * Description:��֧��Ŀ�ҽ���ϸ,����Ԥ���Ŀ��SelectionListener�¼�

 */
public class AcctIncTreeSelectionListener implements TreeSelectionListener {
	AcctJjInc acctJjInc;

	// �շ���Ŀ
	DataSet dsAcctitem;

	public AcctIncTreeSelectionListener(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
		dsAcctitem = acctJjInc.dsAcctitem;
	}

	public void valueChanged(TreeSelectionEvent e) {
		try {
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(
					acctJjInc.dsAcctitem, acctJjInc, acctJjInc.ftreeIncAcctitem);
			setActionStatus.setState(false, true);
			if (dsAcctitem.isEmpty() || dsAcctitem.bof() || dsAcctitem.eof())
				return;
			int iSubType = dsAcctitem.fieldByName("SubItem_Type").getInteger();
			acctJjInc.frdoSubTypeInc.setValue(String.valueOf(iSubType));
			// �ж��Ƿ��ǹ̶�ѡ����ʾ��
			if (iSubType == 2) {
				// ��ʾ�շ���Ŀ��
				ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();
				String sIN_BS_ID = dsAcctitem.fieldByName(
						"").getString();
				String[] sID = sysIaeStruServ.getIncItemWithCode(sIN_BS_ID,
						Global.loginYear);
				SetSelectTree.setIsCheck(acctJjInc.ftreIncomeSubItem, sID,
						true, true);
			} else {
				// ���֧������Ŀ��
				SetSelectTree.setIsNoCheck(acctJjInc.ftreIncomeSubItem);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(acctJjInc, "��ʾ��ϸ��Ϣ���󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
