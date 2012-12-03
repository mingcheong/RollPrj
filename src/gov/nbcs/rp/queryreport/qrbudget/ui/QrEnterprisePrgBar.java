package gov.nbcs.rp.queryreport.qrbudget.ui;

import gov.nbcs.rp.common.ui.progress.ProgressBar;
import com.foundercy.pf.util.Global;

public class QrEnterprisePrgBar implements Runnable {
	QrSendEnterprise exeSearch;

	public QrEnterprisePrgBar(QrSendEnterprise exeSearch) {
		this.exeSearch = exeSearch;
	}

	public void display() {
		Thread myThread = new Thread(this);
		myThread.start();
	}

	public void run() {
		ProgressBar pf = new ProgressBar(Global.mainFrame, "���ڲ�ѯ���ݣ����Ժ򡤡���������",
				false);

		exeSearch.ViewEnterprise();
		// ��ɴ����رչ�����
		pf.dispose();
	}
}
