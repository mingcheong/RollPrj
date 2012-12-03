/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title: ִ�нű���Ϊ��߲�ѯ�ٶȣ�����Щ�����ͼ��ѯ����̻�
 * </p>
 * <p>
 * Description:

 */
public class ExeScriptAction extends CommonAction {

	private Thread myThread;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {
		try {
			List lstScript = QrBudgetI.getMethod().getScriptInfo(
					Global.loginmode);
			if (lstScript == null || lstScript.size() == 0) {
				JOptionPane.showMessageDialog(Global.mainFrame, "û����Ҫִ�еĽű���",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			doOperate(lstScript);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "ִ�нű��������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "ִ�нű��������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void doOperate(List lstScript) {
		ExecuteOpeProgressBar executeOpeProgressBar = new ExecuteOpeProgressBar(
				lstScript);
		executeOpeProgressBar.display();

	}

	private class ExecuteOpeProgressBar implements Runnable {

		private List lstScript;

		public ExecuteOpeProgressBar(List lstScript) {
			this.lstScript = lstScript;

		}

		public void display() {
			myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			try {
				doOperateRun(lstScript);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"ִ�нű��������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * �����ϼ���λ�·�����
	 * 
	 * @throws Exception
	 */
	private void doOperateRun(List lstScript) throws Exception {
		int size = lstScript.size();
		ProgressBar pf = new ProgressBar(Global.mainFrame, "", false);

		String sErr = "";

		try {
			String sSql;
			for (int i = 0; i < size; i++) {
				pf.setValue(i + 1);
				pf.setTitle("����ִ��" + size + "���ű�,����ִ�е�" + (i + 1)
						+ "���ű������Ժ�......");
				if (lstScript.get(i) != null) {
					sSql = lstScript.get(i).toString();
				} else {
					continue;
				}

				try {
					QrBudgetI.getMethod().exeScript(sSql);
				} catch (Exception e) {
					sErr = sErr + "ִ�е�" + (i + 1) + "�ű�����/n";
				}

			}
		} finally {
			pf.dispose();
		}
		if (!Common.isNullStr(sErr)) {
			JOptionPane.showMessageDialog(Global.mainFrame, sErr, "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame, "ִ�гɹ���", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
