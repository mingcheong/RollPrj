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
 * Title: 执行脚本，为提高查询速度，将有些表或视图查询结果固化
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
				JOptionPane.showMessageDialog(Global.mainFrame, "没有需要执行的脚本！",
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			doOperate(lstScript);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "执行脚本发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "执行脚本发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
						"执行脚本发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 接收上级单位下发数据
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
				pf.setTitle("共需执行" + size + "个脚本,正在执行第" + (i + 1)
						+ "个脚本，请稍侯......");
				if (lstScript.get(i) != null) {
					sSql = lstScript.get(i).toString();
				} else {
					continue;
				}

				try {
					QrBudgetI.getMethod().exeScript(sSql);
				} catch (Exception e) {
					sErr = sErr + "执行第" + (i + 1) + "脚本出错。/n";
				}

			}
		} finally {
			pf.dispose();
		}
		if (!Common.isNullStr(sErr)) {
			JOptionPane.showMessageDialog(Global.mainFrame, sErr, "提示",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(Global.mainFrame, "执行成功！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
