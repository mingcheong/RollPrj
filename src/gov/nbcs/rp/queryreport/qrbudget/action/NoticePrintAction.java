/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.progress.ProfressBarWin;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;
import com.fr.report.CellElement;
import com.fr.report.WorkBook;

/**
 * <p>
 * Title:一下通知书打印
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */
public class NoticePrintAction extends CommonAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;

			// 打印一下通知书
			printNoticeOperate(qrBudget.getFtreDivName());
		}

	}

	/**
	 * 打印一下通知书根据选中树节点
	 * 
	 * @param ftreDivName
	 *            单位树
	 * @throws Exception
	 */
	public static void printNoticeOperate(CustomTree ftreDivName) {
		MyTreeNode[] selectNode = ftreDivName.getSelectedNodes(true);
		if (selectNode.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"未选择单位，请选择需要打印一下通知书单位!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		MyProgressBar myProgressBar = new MyProgressBar(ftreDivName, selectNode);
		myProgressBar.display();

	}

	private static class MyProgressBar implements Runnable {
		private CustomTree ftreDivName;

		private MyTreeNode[] selectNode;

		public MyProgressBar(CustomTree ftreDivName, MyTreeNode[] selectNode) {
			this.ftreDivName = ftreDivName;
			this.selectNode = selectNode;
		}

		public void display() {
			Thread myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			try {
				printNotice(ftreDivName, selectNode);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"打印一下通知书发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * 打印一下通知书根据选中树节点
	 * 
	 * @param ftreDivName
	 *            单位树
	 * @param selectNode单位树选中的节点
	 * @throws Exception
	 */
	private static void printNotice(CustomTree ftreDivName,
			MyTreeNode[] selectNode) throws Exception {
		int len = selectNode.length;

		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("正在准备数据，请稍候······");

		try {
			DataSet dsDivName = ftreDivName.getDataSet();
			WorkBook workBook = readXmlModule("/report/noticeModule.xls");

			String sDivCode;
			String sDivName;
			for (int i = 0; i < len; i++) {
				if (!dsDivName.gotoBookmark(selectNode[i].getBookmark())) {
					continue;
				}
				sDivCode = dsDivName.fieldByName(IQrBudget.DIV_CODE)
						.getString();
				sDivName = dsDivName.fieldByName(IPubInterface.DIV_NAME)
						.getString();

				pf.setValue(i);
				pf.setText("正在打印[" + sDivCode + "]" + sDivName
						+ "单位一下通知书，请稍候······");
				printOneNotice((WorkBook) workBook.clone(), sDivCode, sDivName);
			}
		} finally {
			pf.dispose();
		}
	}

	/**
	 * 打印某一单位一下通知书模版
	 * 
	 * @param workBook一下通知书模版
	 * @param sDivCode单位编码
	 * @param sDivName单位名称
	 * @throws PrinterException
	 * @throws ParseException
	 */
	private static void printOneNotice(WorkBook workBook, String sDivCode,
			String sDivName) throws PrinterException, ParseException {

		String moneyAll = "0.00";
		String moneyJb = "0.00";
		String moneyPrj = "0.00";
		List lstRaeData = QrBudgetI.getMethod().getDivRae(sDivCode);
		int size = lstRaeData.size();
		DecimalFormat df = new DecimalFormat("###,##0.00");

		for (int i = 0; i < size; i++) {
			XMLData data = (XMLData) lstRaeData.get(i);
			if ("∑".equals(data.get("payout_kind_code").toString())) {
				moneyAll = data.get("f38").toString();
				moneyAll = df.format(Double.parseDouble(moneyAll));
			} else if ("001".equals(data.get("payout_kind_code").toString())) {
				moneyJb = data.get("f38").toString();
				moneyJb = df.format(Double.parseDouble(moneyJb));
			} else if ("006".equals(data.get("payout_kind_code").toString())) {
				moneyPrj = data.get("f38").toString();
				moneyPrj = df.format(Double.parseDouble(moneyPrj));
			}
		}
		int columnCount = workBook.getColumnCount();
		int rowCount = workBook.getRowCount();
		Object objCell;
		String sContext;

		CellElement cell;
		String sDate = getDate();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				objCell = workBook.getCellValue(j, i);

				if (objCell != null) {
					sContext = objCell.toString();

					sContext = sContext.replaceAll("DIV_NAME", sDivName);
					sContext = sContext.replaceAll("MONEY_ALL", moneyAll);
					sContext = sContext.replaceAll("MONEY_JB", moneyJb);
					sContext = sContext.replaceAll("MONEY_PRJ", moneyPrj);

					sContext = sContext.replaceAll("DATE", sDate);
					cell = workBook.getCellElement(j, i);
					if (cell != null)
						workBook.setCellValue(j, i, sContext);
				}
			}
		}

		String sPrintName = null;
		PrintService printService = PrintServiceLookup
				.lookupDefaultPrintService();
		if (printService != null)
			sPrintName = printService.getName();
		else {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"未设置默认打印机，请设置后再打印!", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		workBook.print(false, sPrintName);
	}

	private static String getDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(Tools.getServerDate().toString());
		return (new SimpleDateFormat("yyyy年MM月dd日")).format(date);

	}

	/**
	 * 读取Excel模板
	 * 
	 * @return
	 * @throws Exception
	 */
	private static WorkBook readXmlModule(String path) throws Exception {
//		URL url = PrjDeclareMessage.class.getResource(path);
//		InputStream in = url.openStream();
//		ExcelImporter ei = new ExcelImporter(in);
//		return ei.generateWorkBook();
		return null;
	}

}
