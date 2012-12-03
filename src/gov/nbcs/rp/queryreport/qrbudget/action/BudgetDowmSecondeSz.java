package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.progress.ProfressBarWin;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.BudgetDowmSecondeDialog;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.fr.base.Constants;
import com.fr.report.CellElement;
import com.fr.report.WorkBook;

public class BudgetDowmSecondeSz extends CommonAction {
	// modify by zlx �޸ĸ�ʽ
	// private static DecimalFormat decimalFormat = new
	// DecimalFormat("###,###.##");
	private static DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;

			BudgetDowmSecondeDialog budgetDowmSecondeDialog = new BudgetDowmSecondeDialog();
			budgetDowmSecondeDialog.show();
			List data = budgetDowmSecondeDialog.getTablePanel()
					.getSelectedDataByCheckBox();

			boolean isPrjGovJj;
//			if (PubVariable.PRJGOVJJ_FLAG.equals(qrBudget.getprjtype())) {
				isPrjGovJj = true;
//			} else {
//				isPrjGovJj = false;
//			}
			qrBudget.getprjtype();
			// ����ָ���´�
			printIncomming(qrBudget.getFtreDivName(), data, isPrjGovJj);
		}

	}

	/**
	 * ��ӡ2009������Ԥ�����´���
	 * 
	 * @param ftreDivName
	 *            ��λ��
	 * @throws Exception
	 */
	public static void printIncomming(CustomTree ftreDivName, List data,
			boolean isPrjGovJj) {
		MyTreeNode[] selectNode = ftreDivName.getSelectedNodes(true);
		if (selectNode.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"δѡ��λ����ѡ����Ҫ��ӡ�ĵ�λ!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		MyProgressBar myProgressBar = new MyProgressBar(ftreDivName,
				selectNode, data, isPrjGovJj);
		myProgressBar.display();

	}

	/**
	 * ��ӡһ��֪ͨ�����ѡ�����ڵ�
	 * 
	 * @param ftreDivName
	 *            ��λ��
	 * @throws Exception
	 */
	public static void printBudgetOperate(CustomTree ftreDivName) {
		MyTreeNode[] selectNode = ftreDivName.getSelectedNodes(true);
		if (selectNode.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"δѡ��λ����ѡ����Ҫ��ӡһ��֪ͨ�鵥λ!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		MyProgressBar myProgressBar = new MyProgressBar(ftreDivName,
				selectNode, null);
		myProgressBar.display();

	}

	private static class MyProgressBar implements Runnable {
		private CustomTree ftreDivName;

		private MyTreeNode[] selectNode;

		// ѡ��Ĵ�ӡ��
		private List data = null;

		boolean isPrjGovJj = false;

		public MyProgressBar(CustomTree ftreDivName, MyTreeNode[] selectNode,
				List data, boolean isPrjGovJj) {
			this.ftreDivName = ftreDivName;
			this.selectNode = selectNode;
			this.data = data;
			this.isPrjGovJj = isPrjGovJj;
		}

		public MyProgressBar(CustomTree ftreDivName, MyTreeNode[] selectNode,
				List data) {
			this.ftreDivName = ftreDivName;
			this.selectNode = selectNode;
			this.data = data;
		}

		public void display() {
			Thread myThread = new Thread(this);
			myThread.start();
		}

		public void run() {
			try {
				boolean bl = printBudegtDowns(ftreDivName, selectNode, data,
						isPrjGovJj);
				if (bl == false)
					JOptionPane.showMessageDialog(Global.mainFrame, "��ӡ���!",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame, "��ӡ�������󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * ��ӡ2009������Ԥ�����´���
	 * 
	 * @param ftreDivName
	 *            ��λ��
	 * @param selectNode��λ��ѡ�еĽڵ�
	 * @throws Exception
	 */
	private static boolean printBudegtDowns(CustomTree ftreDivName,
			MyTreeNode[] selectNode, List data, boolean isPrjGovJj)
			throws Exception {
		int len = selectNode.length;

		ProfressBarWin pf = new ProfressBarWin(len);
		pf.setText("����׼�����ݣ����Ժ򡤡���������");

		WorkBook workBook = null;
		WorkBook workBookBudget = null;
		WorkBook workBookBudgetGov = null;

		try {
			DataSet dsDivName = ftreDivName.getDataSet();

			String index1 = "0";
			String index2 = "0";
			String index3 = "0";
			if (data == null) {
				// ȫ����ӡ
				index1 = "1";
				index2 = "2";
				index3 = "3";
			} else
				for (int i = 0; i < data.size(); i++) {
					Map map = (Map) data.get(i);
					System.out.println(map.get("index"));
					if ("1".equals(map.get("index")))
						index1 = "1";
					else if ("2".equals(map.get("index")))
						index2 = "2";
					else if ("3".equals(map.get("index")))
						index3 = "3";
				}

			if ("1".equals(index1))
				if (isPrjGovJj) {
					workBook = readXmlModule("/report/incommingDownzfjj.xls");
				} else {
					workBook = readXmlModule("/report/incommingDown.xls");
				}

			if ("2".equals(index2))
				workBookBudget = readXmlModule("/report/budgetDown.xls");

			if ("3".equals(index3))
				workBookBudgetGov = readXmlModule("/report/budgetDownGov.xls");

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
				pf.setText("���ڴ�ӡ��λ[" + sDivCode + "]" + sDivName
						+ "��������Ԥ�����´��飬���Ժ򡤡���������");
				if (workBook != null) {
					// modify by zlx 2009-12-03 ����2010�������ݷ����ı�
					printOneIncomming((WorkBook) workBook.clone(), sDivCode,
							sDivName, isPrjGovJj);
				}
				if (workBookBudget != null)
					printOneBudget((WorkBook) workBookBudget.clone(), sDivCode,
							sDivName, isPrjGovJj);
				if (workBookBudgetGov != null)
					printOneBudgetGov((WorkBook) workBookBudgetGov.clone(),
							sDivCode, sDivName, isPrjGovJj);
			}
		} finally {
			pf.dispose();
		}

		return workBook == null && workBookBudget == null
				&& workBookBudgetGov == null;
	}

	/**
	 * ֧��Ԥ�����´���
	 * 
	 * @param workBookģ��
	 * @param sDivCode��λ����
	 * @param sDivName��λ����
	 * @throws Exception
	 */
	private static void printOneBudget(WorkBook workBook, String sDivCode,
			String sDivName, boolean isPrjGovJj) throws Exception {

		// DataSet ds = QrBudgetI.getMethod().getBudgetDown(sDivCode,2,1);
//		int batchNo = PubInterfaceStub.getMethod().getCurBatchNO();
		int batchNo = 0;

		CellElement otherCell = null;// ����ͷ�չ��ĿCELL
		CellElement oCell = null;// ������ʽ

		int columnCount = workBook.getColumnCount();
		int rowCount = workBook.getRowCount();

		Object[] prj = null;
		if (isPrjGovJj) {
		} else {
			// ȡ����֧��
			prj = QrBudgetI.getMethod().getBudgetDownJBZC(sDivCode, batchNo, 1);
		}

		int i = 0;
		for (; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				Object objCell = workBook.getCellValue(j, i);
				if (objCell == null)
					objCell = new CellElement(j, i, "");
				// System.out.println("j="+j+":i="+i+"=="+objCell.toString());
				if (objCell != null) {
					// j=2:i=1[2009������Ԥ�����´���]
					if (j == 1 && i == 1) {
						String sContext = objCell.toString();
						if (isPrjGovJj) {
							sContext = Global.loginYear + "�������Ի���֧��Ԥ�����´���";
						} else {
							sContext = Global.loginYear + "��֧��Ԥ�����´���";
						}
						workBook.setCellValue(j, i, sContext);
					} else if (j == 1 && i == 2) {
						// j=2:i=2[��λ���ƣ�]
						String sContext = objCell.toString();
						sContext = "��λ���ƣ�" + sDivName;
						workBook.setCellValue(j, i, sContext);
					} else if (j == 1 && i == 3) {
						// j=2:i=3[��������(����ר��)]
						String sContext = objCell.toString();
						if (isPrjGovJj) {
							sContext = "һ��" + Global.loginYear + "��������Ŀ�´���";
						} else {
							sContext = "һ��" + Global.loginYear
									+ "�����֧��/��������Ŀ�´���";
						}
						workBook.setCellValue(j, i, sContext);

						// System.out.println("objCell=="+objCell.getClass().getName());
						otherCell = workBook.getCellElement(j, i);
						otherCell.setStyle(otherCell.getStyle()
								.deriveBorderRight(1, Color.black));
					} else if (j == 1 && i == 6) {
						oCell = workBook.getCellElement(j, i);
					}
					if (isPrjGovJj) {
					} else {
						if (j == 2 && i == 6) {
							// j=3:i=5[����֧��-- �ϼ�]
							workBook.setCellValue(j, i,
									getFormateNumber(prj[1]));
						} else if (j == 3 && i == 6) {
							// j=4:i=6[��������(����ר��)]
							workBook.setCellValue(j, i,
									getFormateNumber(prj[2]));
						} else if (j == 4 && i == 6) {
							// j=4:i=6�������ר���ʽ�]����Ϊ����Ԥ������������ҵ���շѡ���û���롢ά���������
							workBook.setCellValue(j, i,
									getFormateNumber(prj[3]));
						} else if (j == 5 && i == 6) {
							// j=6:i=6[Ԥ�����ʽ𣨿�������]
							workBook.setCellValue(j, i,
									getFormateNumber(prj[4]));
						} else if (j == 6 && i == 6) {
							// j=7:i=6[�����ʽ�]
							workBook.setCellValue(j, i,
									getFormateNumber(prj[5]));
						}
					}
				}
			}
		}

		if (isPrjGovJj) {
			i--;
		}

		// д��ϸ����
		Object[] prjJCXXM = QrBudgetI.getMethod().getBudgetDownPrjDetail("001",
				sDivCode, batchNo, 1, isPrjGovJj);
		for (int k = 0; prjJCXXM != null && k < prjJCXXM.length; k++) {
			Object[] prjInfo = (Object[]) prjJCXXM[k];
			int j = 1;
			for (int m = 0; prjInfo != null && m < prjInfo.length; m++) {
				String name = getFormateNumber(prjInfo[m]);
				if (m == 0 && k == 0)
					name = "��������Ŀ";
				else {
					// ��������
					// if(name.length()>14){
					// name = name.substring(0,14)+"\n"+name.substring(14);
					// }
				}
				CellElement Cell = new CellElement(j++, i, name);
				Cell.setAttributes(oCell.getAttributes());
				Cell.setCellGUIAttr(oCell.getCellGUIAttr());
				Cell.setCellExpandAttr(oCell.getCellExpandAttr());
				Cell.setCellGroupAttr(oCell.getCellGroupAttr());
				Cell.setCellWriteAttr(oCell.getCellWriteAttr());
				// modify by zlx 2009-01-19 ������Ҷ���
				if (j == 2)
					Cell.setStyle(oCell.getStyle());
				else
					Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
							Constants.RIGHT));

				workBook.addCellElement(Cell);
			}

			i++;// ����һ��
		}

		// ����2009�꽨��/��չ����Ŀ��֪��
		if (otherCell == null)
			otherCell = new CellElement(2, i);
		CellElement newCell = new CellElement(1, i, 6, 1, "");
		newCell.setAttributes(otherCell.getAttributes());
		newCell.setCellGUIAttr(otherCell.getCellGUIAttr());
		newCell.setCellExpandAttr(otherCell.getCellExpandAttr());
		newCell.setCellGroupAttr(otherCell.getCellGroupAttr());
		newCell.setCellWriteAttr(otherCell.getCellWriteAttr());
		newCell.setStyle(otherCell.getStyle());

		newCell.setValue("����" + Global.loginYear + "�꽨��/��չ����Ŀ��֪��");
		workBook.addCellElement(newCell);

		i++;// ����һ��
		Object[] prjJSXM = QrBudgetI.getMethod().getBudgetDownPrjDetail("002",
				sDivCode, batchNo, 1, isPrjGovJj);
		for (int k = 0; prjJSXM != null && k < prjJSXM.length; k++) {
			Object[] prjInfo = (Object[]) prjJSXM[k];
			int j = 1;
			for (int m = 0; prjInfo != null && m < prjInfo.length; m++) {
				String name = getFormateNumber(prjInfo[m]);
				if (m == 0 && k == 0)
					name = "��������Ŀ";
				else {
					// ��������
					// if(name.length()>14){
					// name = name.substring(0,14)+"\n"+name.substring(14);
					// }
				}
				CellElement Cell = new CellElement(j++, i, name);
				Cell.setAttributes(oCell.getAttributes());
				Cell.setCellGUIAttr(oCell.getCellGUIAttr());
				Cell.setCellExpandAttr(oCell.getCellExpandAttr());
				Cell.setCellGroupAttr(oCell.getCellGroupAttr());
				Cell.setCellWriteAttr(oCell.getCellWriteAttr());
				// modify by zlx 2009-01-19 ������Ҷ���
				if (j == 2)
					Cell.setStyle(oCell.getStyle());
				else
					Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
							Constants.RIGHT));

				workBook.addCellElement(Cell);
			}

			i++;// ����һ��
		}
		//		
		Object[] prjFZXM = QrBudgetI.getMethod().getBudgetDownPrjDetail("003",
				sDivCode, batchNo, 1, isPrjGovJj);
		for (int k = 0; prjFZXM != null && k < prjFZXM.length; k++) {
			Object[] prjInfo = (Object[]) prjFZXM[k];
			int j = 1;
			for (int m = 0; prjInfo != null && m < prjInfo.length; m++) {
				String name = getFormateNumber(prjInfo[m]);
				if (m == 0 && k == 0)
					// modify by zlx 2009-01-20
					name = "��չ����Ŀ";
				else {
					// ��������
					// if(name.length()>14){
					// name = name.substring(0,14)+"\n"+name.substring(14);
					// }
				}
				CellElement Cell = new CellElement(j++, i, name);
				Cell.setAttributes(oCell.getAttributes());
				Cell.setCellGUIAttr(oCell.getCellGUIAttr());
				Cell.setCellExpandAttr(oCell.getCellExpandAttr());
				Cell.setCellGroupAttr(oCell.getCellGroupAttr());
				Cell.setCellWriteAttr(oCell.getCellWriteAttr());
				// modify by zlx 2009-01-20 ������Ҷ���
				if (j == 2)
					Cell.setStyle(oCell.getStyle());
				else
					Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
							Constants.RIGHT));

				workBook.addCellElement(Cell);
			}

			i++;// ����һ��
		}

		// ����2011���ϡ��¼�����֧����֪��
		if (isPrjGovJj) {
		} else {

			if (otherCell == null)
				otherCell = new CellElement(2, i);
			newCell = new CellElement(1, i, 6, 1, "");
			newCell.setAttributes(otherCell.getAttributes());
			newCell.setCellGUIAttr(otherCell.getCellGUIAttr());
			newCell.setCellExpandAttr(otherCell.getCellExpandAttr());
			newCell.setCellGroupAttr(otherCell.getCellGroupAttr());
			newCell.setCellWriteAttr(otherCell.getCellWriteAttr());
			newCell.setStyle(otherCell.getStyle());

			newCell.setValue("����" + Global.loginYear + "���ϡ��¼�����֧����֪��");
			workBook.addCellElement(newCell);
			i++;// ����һ��
			// ����֧��
			Object[] bzZc = QrBudgetI.getMethod().getBudgetDownBz(sDivCode,
					batchNo, 1);
			for (int k = 0; bzZc != null && k < bzZc.length; k++) {
				Object[] prjInfo = (Object[]) bzZc[k];
				int j = 1;
				for (int m = 0; prjInfo != null && m < prjInfo.length; m++) {
					String name = getFormateNumber(prjInfo[m]);
					CellElement Cell = new CellElement(j++, i, name);
					Cell.setAttributes(oCell.getAttributes());
					Cell.setCellGUIAttr(oCell.getCellGUIAttr());
					Cell.setCellExpandAttr(oCell.getCellExpandAttr());
					Cell.setCellGroupAttr(oCell.getCellGroupAttr());
					Cell.setCellWriteAttr(oCell.getCellWriteAttr());
					// modify by zlx 2009-01-19 ������Ҷ���
					if (j == 2)
						Cell.setStyle(oCell.getStyle());
					else
						Cell.setStyle(oCell.getStyle()
								.deriveHorizontalAlignment(Constants.RIGHT));

					workBook.addCellElement(Cell);
				}

				i++;// ����һ��
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

		// add by zlx 2009-01-19
		workBook.setColumnWidth(0, 1);
		workBook.shrinkToFitRowHeight();

		// FileOutputStream outputStream = new FileOutputStream(new File(
		// "d:\\d.xls"));
		// ExcelExporter excelExporter = new ExcelExporter(outputStream);
		// excelExporter.exportReport(workBook);

		workBook.print(false, sPrintName);
	}

	/**
	 * ֧��Ԥ�����´��� -- 2009�������ɹ�Ԥ��֪ͨ��
	 * 
	 * @param workBookģ��
	 * @param sDivCode��λ����
	 * @param sDivName��λ����
	 * @throws Exception
	 */
	private static void printOneBudgetGov(WorkBook workBook, String sDivCode,
			String sDivName, boolean isPrjGovJj) throws Exception {

		// DataSet ds = QrBudgetI.getMethod().getBudgetDown(sDivCode,2,1);

		CellElement oCell = null;// ������ʽ

		int columnCount = workBook.getColumnCount();
		int rowCount = workBook.getRowCount();

		int i = 0;
		for (; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				Object objCell = workBook.getCellValue(j, i);
				if (objCell == null)
					objCell = new CellElement(j, i, "");
				// System.out.println("j="+j+":i="+i+"=="+objCell.toString());
				if (objCell != null) {
					// j=2:i=1[2009������Ԥ�����´���]
					if (j == 0 && i == 1) {
						String sContext = objCell.toString();
						if (isPrjGovJj) {
							sContext = Global.loginYear + "�������Ի��������ɹ�Ԥ��֪ͨ��";
						} else {
							sContext = Global.loginYear + "�������ɹ�Ԥ��֪ͨ��";
						}
						workBook.setCellValue(j, i, sContext);
					} else if (j == 0 && i == 2) {
						// j=2:i=2[��λ���ƣ�]
						String sContext = objCell.toString();
						sContext = "��λ���ƣ�" + sDivName;
						workBook.setCellValue(j, i, sContext);
					} else if (j == 0 && i == 6) {
						// ��Ϊģ��
						oCell = workBook.getCellElement(j, i);
					}
				}
			}
		}

//		int batchNo = PubInterfaceStub.getMethod().getCurBatchNO();
		int batchNo = 0;
		// д��ϸ����
		DataSet ds = QrBudgetI.getMethod().getBudgetDownGov(sDivCode, batchNo,
				1, isPrjGovJj);
		// ��ʼ������
		i = 6;

		ds.beforeFirst();
		while (ds.next()) {
			int j = 0;
			String name = (String) ds.fieldByName("Prj_Name").getValue();
			if (name == null)
				name = "";
			if (name.length() > 8) {
				name = name.substring(0, 8) + name.substring(8);
				workBook.setRowHeight(i, 30);
			}
			CellElement Cell = new CellElement(j++, i, name);
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle());

			workBook.addCellElement(Cell);

			name = (String) ds.fieldByName("BUY_NAME").getValue();
			if (name == null)
				name = "";
			if (name.length() > 8) {
				name = name.substring(0, 8) + name.substring(8);
				workBook.setRowHeight(i, 30);
			}

			Cell = new CellElement(j++, i, name);
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle());

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds
					.fieldByName("hj").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds.fieldByName(
					"czbk").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds.fieldByName(
					"czbk_zx").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds.fieldByName(
					"ysw").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds.fieldByName(
					"ysw_zx").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			Cell = new CellElement(j++, i, getFormateNumber(ds.fieldByName(
					"qtzj").getValue()));
			Cell.setAttributes(oCell.getAttributes());
			Cell.setCellGUIAttr(oCell.getCellGUIAttr());
			Cell.setCellExpandAttr(oCell.getCellExpandAttr());
			Cell.setCellGroupAttr(oCell.getCellGroupAttr());
			Cell.setCellWriteAttr(oCell.getCellWriteAttr());
			Cell.setStyle(oCell.getStyle().deriveHorizontalAlignment(
					Constants.RIGHT));

			workBook.addCellElement(Cell);

			i++;// ����һ��
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
		// add by zlx 2009-01-19
		workBook.shrinkToFitRowHeight();

		// FileOutputStream outputStream = new FileOutputStream(new File(
		// "d:\\c.xls"));
		// ExcelExporter excelExporter = new ExcelExporter(outputStream);
		// excelExporter.exportReport(workBook);

		workBook.print(false, sPrintName);
	}

	/**
	 * ��ȡExcelģ��
	 * 
	 * @return
	 * @throws Exception
	 */
	private static WorkBook readXmlModule(String path) throws Exception {
//		URL url = PrjDeclareMessage.class.getResource(path);
//		URL url = PrjDeclareMessage.class.getResource(path);
//		InputStream in = url.openStream();
//		ExcelImporter ei = new ExcelImporter(in);
		return null;
//		return ei.generateWorkBook();
	}

	/**
	 * ��ʽ����ʾ���
	 * 
	 * @param value
	 * @return
	 */
	public static String getFormateNumber(Object value) {
		if (value == null || "".equals(value.toString().trim()))
			return "";

		String result = "";
		if (value instanceof BigDecimal) {
			result = decimalFormat.format(value);
		} else if (value instanceof String)
			result = value.toString();
		else
			result = decimalFormat.format(value);
		// modify by zlx 2009-1-19 0��Ϊ0.00
		// Ϊ0.00�Ĳ�����
		if (result.equals("0.00") || "0.0".equals(result))
			result = "";

		return result;

	}

	/**
	 * ��������Ԥ�����´���
	 * 
	 * @param workBookģ��
	 * @param sDivCode��λ����
	 * @param sDivName��λ����
	 * @throws Exception
	 */
	private static void printOneIncomming(WorkBook workBook, String sDivCode,
			String sDivName, boolean isPrjGovJj) throws Exception {

		// DataSet dsIncType = SysIaeStruI.getMethod().getIncTypeTre(
		// Integer.parseInt(Global.loginYear));
//		int batchNo = PubInterfaceStub.getMethod().getCurBatchNO();
		int batchNo = 0;
		DataSet ds = QrBudgetI.getMethod().getBudgetDown(sDivCode, batchNo, 1);

		// j=2:i=1[����Ԥ�����´���]
		String sContext;
		if (isPrjGovJj) {
			sContext = Global.loginYear + "�����Ի�������Ԥ��ƻ���";
		} else {
			sContext = Global.loginYear + "����Ԥ��ƻ���";
		}

		workBook.setCellValue(batchNo, 1, sContext);
		// j=2:i=2[��λ���ƣ�]
		sContext = "��λ���ƣ�" + sDivName;
		workBook.setCellValue(2, 2, sContext);

		int rowCount = workBook.getRowCount();
		// ������д��
		int valueColumn = 7;
		Object objCell;
		String[] inctypeCodeArr;
		String inctypeCode;
		double value;
		for (int i = 0; i < rowCount; i++) {
			objCell = workBook.getCellValue(valueColumn, i);
			if (objCell == null) {
				objCell = new CellElement(valueColumn, i, "");
				continue;
			}
			if (Common.isNullStr(objCell.toString())) {
				continue;
			}

			// �ж��ǲ��ǡ�#����ͷ��
			if (!"#".equals(objCell.toString().substring(0, 1))) {
				continue;
			}

			// �õ�inctypeCodeֵ
			value = 0.00;
			inctypeCodeArr = objCell.toString().split("_");
			for (int j = 0; j < inctypeCodeArr.length; j++) {
				inctypeCode = inctypeCodeArr[j].substring(1);
				if (ds.locate(IIncType.INCTYPE_CODE, inctypeCode)) {
					if (ds.fieldByName("inc_money").getValue() != null
							&& !Common.isNullStr(ds.fieldByName("inc_money")
									.getString())) {
						value += ds.fieldByName("inc_money").getDouble();
					}
				}
			}

			workBook.setCellValue(valueColumn, i, getFormateNumber(Double
					.valueOf(String.valueOf(value))));
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
		workBook.setColumnWidth(0, 1);
		workBook.setColumnWidth(1, 1);

		// FileOutputStream outputStream = new FileOutputStream(new File(
		// "d:\\b.xls"));
		// ExcelExporter excelExporter = new ExcelExporter(outputStream);
		// excelExporter.exportReport(workBook);

		workBook.print(false, sPrintName);
	}
}
