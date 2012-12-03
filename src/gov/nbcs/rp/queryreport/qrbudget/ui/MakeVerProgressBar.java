package gov.nbcs.rp.queryreport.qrbudget.ui;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import com.foundercy.pf.util.Global;

public class MakeVerProgressBar implements Runnable {
	String verNo;

	QrBudgetVerInfoUI mainUI;

	public MakeVerProgressBar(QrBudgetVerInfoUI mainUI, String verNo) {
		this.verNo = verNo;
		this.mainUI = mainUI;
	}

	public void display() {
		Thread myThread = new Thread(this);
		myThread.start();

	}

	public void run() {
		try {
			ProgressBar pf = new ProgressBar(Global.mainFrame,
					"正在生成查询版本数据，请稍候······", false);
			InfoPackage infopackage = new InfoPackage();
			DataSet ds = QrBudgetI.getMethod().getReportInfo(verNo);
			if (ds != null && !ds.isEmpty())
				ds.beforeFirst();
			while (ds.next()) {
				infopackage = QrBudgetI.getMethod().CreatTabelReportVer(verNo,
						ds.fieldByName("report_id").getString(),
						ds.fieldByName("sqllines").getString());
				pf.setTitle("正在准备报表:“"
						+ ds.fieldByName("report_cname").getString() + "”");
				if (!infopackage.getSuccess()) {
					JOptionPane.showMessageDialog(mainUI, "生成报表“"
							+ ds.fieldByName("report_cname").getString()
							+ "”时出错，出错信息：" + infopackage.getsMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
					continue;
				}
			}
			pf.dispose();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
