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
 * Title:һ��֪ͨ���ӡ
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

			// ��ӡһ��֪ͨ��
			printNoticeOperate(qrBudget.getFtreDivName());
		}

	}

	/**
	 * ��ӡһ��֪ͨ�����ѡ�����ڵ�
	 * 
	 * @param ftreDivName
	 *            ��λ��
	 * @throws Exception
	 */
	public static void printNoticeOperate(CustomTree ftreDivName) {
		MyTreeNode[] selectNode = ftreDivName.getSelectedNodes(true);
		if (selectNode.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"δѡ��λ����ѡ����Ҫ��ӡһ��֪ͨ�鵥λ!", "��ʾ",
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
						"��ӡһ��֪ͨ�鷢�����󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * ��ӡһ��֪ͨ�����ѡ�����ڵ�
	 * 
	 * @param ftreDivName
	 *            ��λ��
	 * @param selectNode��λ��ѡ�еĽڵ�
	 * @throws Exception
	 */
	private static void printNotice(CustomTree ftreDivName,
			MyTreeNode[] selectNode) throws Exception {
		int len = selectNode.length;

		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("����׼�����ݣ����Ժ򡤡���������");

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
				pf.setText("���ڴ�ӡ[" + sDivCode + "]" + sDivName
						+ "��λһ��֪ͨ�飬���Ժ򡤡���������");
				printOneNotice((WorkBook) workBook.clone(), sDivCode, sDivName);
			}
		} finally {
			pf.dispose();
		}
	}

	/**
	 * ��ӡĳһ��λһ��֪ͨ��ģ��
	 * 
	 * @param workBookһ��֪ͨ��ģ��
	 * @param sDivCode��λ����
	 * @param sDivName��λ����
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
			if ("��".equals(data.get("payout_kind_code").toString())) {
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
					"δ����Ĭ�ϴ�ӡ���������ú��ٴ�ӡ!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		workBook.print(false, sPrintName);
	}

	private static String getDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(Tools.getServerDate().toString());
		return (new SimpleDateFormat("yyyy��MM��dd��")).format(date);

	}

	/**
	 * ��ȡExcelģ��
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
