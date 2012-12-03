package gov.nbcs.rp.sync.action;

import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.sync.ibs.SyncInterface;
import gov.nbcs.rp.sync.ui.SyncMainUI;

import java.awt.event.ActionEvent;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;

public class SyncAction extends CommonAction {

	private static final long serialVersionUID = 1L;
	private String DoMethod;
	// 父界面
	private SyncMainUI bgMain;
	private SyncInterface synserv;
	private String direction;
	private String exeFunc1;
	private String exeFunc2;
	private String dblink;

	public void actionPerformed(ActionEvent e) {
		DoMethod = this.getParameter("method").toString();
		if (this.getModulePanel() instanceof SyncMainUI)
			bgMain = (SyncMainUI) this.getModulePanel();
		if ("close".equals(DoMethod)) {
			((FFrame) Global.mainFrame).closeMenu();
		}

		if ("sync".equals(DoMethod)) {
			try {
				syncData();
			} catch (Exception e1) {
				new MessageBox(e1.getMessage(), MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
		}
	}

	private synchronized void syncData() throws Exception {

		TProgressBar pb = new TProgressBar(1);
		pb.display();
	}

	public SyncInterface getSynserv() {
		if (synserv == null) {
			BeanFactory beanfac = BeanFactoryUtil
					.getBeanFactory("gov/nbcs/rp/sync/conf/SyncConf.xml");
			synserv = (SyncInterface) beanfac.getBean("rp.SyncService");
		}
		return synserv;
	}

	protected class TProgressBar implements Runnable {

		protected ProgressBar dlg = null;
		protected int type = 0;
		// private List batchSqlList;
		private String beforeSql;
		private String endSql;

		public TProgressBar(int type) {
			this.type = type;
		}

		Thread myThread;

		public void display() {
			myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			try {
				dlg = new ProgressBar(Global.mainFrame, "正在处理，请稍候······", false);
				// myThread.sleep(1000);
				beforeSql = (String) bgMain.beforeTField.getValue();
				endSql = (String) bgMain.afterTField.getValue();
				dblink = (String) bgMain.dbLinkName.getValue();

				// 获取导出方向
				// "0" + "#" + "项目库到平台" + "+";
				// "1" + "#" + "平台到项目库";
				direction = (String) bgMain.exportLineComboBox.getValue();

				// modelString = "0" + "#" + "同步前清空表数据" + "+";
				// modelString += "1" + "#" + "同步前不清空表数据";
				exeFunc1 = (String) bgMain.execFuncComboBox1.getValue();
				// modelString = "0" + "#" + "删除同步重复数据" + "+";
				// modelString += "1" + "#" + "不删除同步重复数据";
				exeFunc2 = (String) bgMain.execFuncComboBox2.getValue();
				// DataSet ds = bgMain.tbDetail.getDataSet();

				List list = null;
				try {
					list = bgMain.tbDetail.getHasCheckData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ((list == null) || (list.size() == 0)) {
					new MessageBox("选择同步表后进行同步操作！", MessageBox.MESSAGE,
							MessageBox.BUTTON_OK).show();
					return;
				}

				getSynserv().syncTableData(null, dblink, direction, exeFunc1,
						exeFunc2, list, beforeSql, endSql, "");

			} catch (Exception e) {
				ErrorInfo.showErrorDialog(e, e.getMessage());
			} finally {
				dlg.dispose();
			}
		}
	}

}