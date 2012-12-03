/**
 * @# RowSetDisp.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportFactory;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.report.CellElement;

/**
 * ����˵��:�б����չʾ
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 
 */
public class RowSetDisp extends Report {
	private ReportInfo reportInfo;

	private int rows;

	private IRowSet rowSetBO;

	private List lstLineCons = null;// ������

	private List lstRows = null;// ������

	private int colRound = -1;// ��¼���ظ�����

	public RowSetDisp() throws Exception {
		super();
		rowSetBO = (IRowSet) BOCache.getBO("fb.rowSetService");
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6487401626035306786L;

	public RowSetDisp(ReportInfo reportInfo) throws Exception {
		super();
		rowSetBO = (IRowSet) BOCache.getBO("fb.rowSetService");
		this.reportInfo = reportInfo;
		setReportID(reportInfo);
		this.shrinkToFitRowHeight();
	}

	// ��ʼ������
	private void initData() {
		lstRows = rowSetBO.getReportRows(reportInfo.getReportID(), Global
				.getSetYear());
		this.rows = new Double(Math.ceil(new Double(lstRows.size())
				.doubleValue()
				/ reportInfo.getColNum())).intValue();
		lstLineCons = rowSetBO.getReportCons(reportInfo.getReportID(), Global
				.getSetYear());
	};

	private void dispRows() throws Exception {
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();

		getColRound();

		DataSet dsHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());
		List lstCols = new ArrayList();// ��¼����Ϣ
		ReportFactory.getReportWithHead(this, dsHeader, true, lstCols,
				reportInfo);
		// ��ӱ�ͷ
		// for (int i = 0; i < colRound; i++) {
		// dsHeader.append();
		// dsHeader.fieldByName(IQrBudget.FIELD_CODE).setValue(
		// StringUtils.leftPad(String.valueOf(iColCount), 3, "0"));
		// dsHeader.fieldByName(IQrBudget.FIELD_CNAME).setValue("���");
		// dsHeader.fieldByName(IQrBudget.FIELD_DISPWIDTH).setValue(
		// new Integer(200));
		//
		// iColCount++;
		//
		// dsHeader.append();
		// dsHeader.fieldByName(IQrBudget.FIELD_CODE).setValue(
		// StringUtils.leftPad(String.valueOf(iColCount), 3, "0"));
		// dsHeader.fieldByName(IQrBudget.FIELD_CNAME).setValue("��Ŀ");
		// dsHeader.fieldByName(IQrBudget.FIELD_DISPWIDTH).setValue(
		// new Integer(400));
		// iColCount++;
		// dsHeader.append();
		// dsHeader.fieldByName(IQrBudget.FIELD_CODE).setValue(
		// StringUtils.leftPad(String.valueOf(iColCount), 3, "0"));
		// dsHeader.fieldByName(IQrBudget.FIELD_CNAME).setValue("��ֵ");
		// dsHeader.fieldByName(IQrBudget.FIELD_DISPWIDTH).setValue(
		// new Integer(200));
		//
		// iColCount++;
		// }
		// String sCur = reportInfo.getUnit();
		// if (sCur != null && !sCur.equals(""))
		// sCur = "��λ:" + sCur;
		// TableHeader tableHeader;
		// tableHeader = new TableHeader(hg.generate(dsHeader,
		// IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
		// BesAct.codeRule_Col, IQrBudget.FIELD_CODE), reportInfo
		// .getTitle(), sCur);

		// tableHeader = new TableHeader(hg.generate(dsHeader,
		// IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
		// BesAct.codeRule_Col, IQrBudget.FIELD_CODE));

		// this.setReportHeader(tableHeader);
		dispData();
	}

	private void dispData() {
		if (colRound == -1 || rows <= 0)
			return;
		int iCount = lstRows.size();
		int iReadyCount = 0;
		XMLData aData;
		int jBegin = this.getReportHeader().getStartRow();
		Style st = Style.getInstance();
		st = st.deriveHorizontalAlignment(Constants.LEFT);
		// ColorBackground colorBackground = ColorBackground
		// .getInstance(Color.WHITE);
		// st = st.deriveBackground(colorBackground).deriveBorder(1,
		// Color.DARK_GRAY,
		// 1, Color.DARK_GRAY, 1, Color.DARK_GRAY, 1, Color.DARK_GRAY);
		;

		for (int i = 0; i < colRound; i++)
			// ��
			for (int j = jBegin + 1; j < rows + jBegin + 1; j++) {// ��
				if (iCount <= iReadyCount)
					break;
				aData = (XMLData) lstRows.get(iReadyCount);
				iReadyCount++;
				// ���
				Cell aCell = new Cell(i * 3, j, 1, 1, aData
						.get(RowInfo.FIELD_ITEM_ID));
				aCell.setStyle(st);
				this.addCellElement(aCell);
				// ��Ŀ��
				aCell = new Cell(i * 3 + 1, j, 1, 1, aData
						.get(RowInfo.FIELD_ITEM));

				aCell.setStyle(st);
				// �յ�
				this.addCellElement(aCell);
				aCell = new Cell(i * 3 + 2, j, 1, 1, aData.get(null));
				aCell.setStyle(st);

				this.addCellElement(aCell);

			}
		// this.getCellElementCount();
	}

	/**
	 * ���ñ���IDʱ���ͻ�ˢ��
	 * 
	 * @param sReportID
	 * @param rows
	 */
	public void setReportID(ReportInfo reportInfo) throws Exception {
		this.reportInfo = reportInfo;
		this.removeAllCellElements();

		initData();
		dispRows();
//		initColWidth();

	}

	private void getColRound() {
		if (lstRows == null) {
			this.colRound = -1;
		}
		int iRowCount = lstRows.size();
		colRound = iRowCount / rows;
		if ((iRowCount % rows) > 0)
			colRound += 1;

	}

	public void dispCon() {
		List lstData = rowSetBO.getReportCons(reportInfo.getReportID(), Global
				.getSetYear());
		if (lstData == null)
			return;

	}

//	private void initColWidth() {
//		for (int i = 0; i < colRound; i++) {
//			this.setColumnWidth(i * 3, 50);
//			this.setColumnWidth(i * 3 + 1, 200);
//			this.setColumnWidth(i * 3 + 2, 100);
//		}
//
//	}

	public void doSearch(List depts, List divs, String sVerNo,
			String sBatchNoFilter, int iLoginmode) throws Exception {
		// �����
		setReportID(reportInfo);
		XMLData aData = rowSetBO.getRowSetData(rows, reportInfo.getReportID(),
				depts, divs, sVerNo, sBatchNoFilter, iLoginmode);
		dispValue(aData);
	}

	private void dispValue(XMLData aData) {
		if (aData == null)
			return;
		Iterator it = aData.keySet().iterator();
		String key;
		Style st = Style.getInstance();
		st=st.deriveHorizontalAlignment(Constants.RIGHT);
		st=st.deriveFormat(new DecimalFormat("##0.##"));
		int rowBegin = this.getReportHeader().getRows();
		while (it.hasNext()) {
			key = (String) it.next();
			String[] arrKey = key.split("_");
			int col = Integer.parseInt(arrKey[0]);
			int row = Integer.parseInt(arrKey[1]) + rowBegin;// ��������1��ʼ������Ҫ��һ���ټ��ϱ�ͷ�ĸ�
			Object value = aData.get(key);
			if (value == null || value.equals("null"))
				value = "";
			CellElement aCell = new Cell(col, row, 1, 1, value);
			aCell.setStyle(st);
			this.addCellElement(aCell);
		}
	}
}
