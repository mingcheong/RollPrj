/**
 * @# RowSetDispByReport.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportFactory;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.report.CellElement;
import com.fr.report.Report;

/**
 * ����˵��:���ڴ���Report��ʾ,�������Բ������µ�Report�����Խ�ʡ�ռ�
 * <P>
 * Copyright
 * <P>

 */
public class RowSetDispByReport {

	private Report report;

	private ReportInfo reportInfo;

	private int rows;

	private IRowSet rowSetBO;

	private List lstRows = null;// ������

	private int colRound = -1;// ��¼���ظ�����

	DecimalFormat df = new DecimalFormat("##0.00");

	public RowSetDispByReport(Report report, ReportInfo reportInfo) {

		try {
			if (report == null)
				report = new gov.nbcs.rp.common.ui.report.Report();

			this.report = report;
			rowSetBO = (IRowSet) BOCache.getBO("fb.rowSetService");
			this.reportInfo = reportInfo;

			setReportID(reportInfo);
			((gov.nbcs.rp.common.ui.report.Report) report)
					.tShrinkToFitRowHeight();
		} catch (Exception e) {
			new MessageBox("���ɱ������!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return;
		}
	}

	// ��ʼ������
	private void initData() {
		lstRows = rowSetBO.getReportRows(reportInfo.getReportID(), Global
				.getSetYear());
		this.rows = new Double(Math.ceil(new Double(lstRows.size())
				.doubleValue()
				/ reportInfo.getColNum())).intValue();
	};

	private void dispRows() throws Exception {
		getColRound();

		DataSet dsHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());
		List lstCols = new ArrayList();// ��¼����Ϣ
		ReportFactory.getReportWithHead(report, dsHeader, true, lstCols,
				reportInfo);

		dispData();
	}

	private void dispData() {
		if (colRound == -1 || rows <= 0)
			return;
		int iCount = lstRows.size();
		int iReadyCount = 0;
		XMLData aData;
		int jBegin = ((gov.nbcs.rp.common.ui.report.Report) report)
				.getReportHeader().getStartRow();
		Style st = Style.getInstance();
		st = st.deriveHorizontalAlignment(Constants.LEFT);

		((gov.nbcs.rp.common.ui.report.Report) report)
				.setBodyData(null);

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
				report.addCellElement(aCell);
				// ��Ŀ��
				aCell = new Cell(i * 3 + 1, j, 1, 1, aData
						.get(RowInfo.FIELD_ITEM));

				aCell.setStyle(st);
				// �յ�
				report.addCellElement(aCell);
				aCell = new Cell(i * 3 + 2, j, 1, 1, aData.get(null));
				aCell.setStyle(st);

				report.addCellElement(aCell);

			}

		// report.getCellElementCount();
	}

	/**
	 * ���ñ���IDʱ���ͻ�ˢ��
	 * 
	 * @param sReportID
	 * @param rows
	 */
	public void setReportID(ReportInfo reportInfo) throws Exception {
		this.reportInfo = reportInfo;
		report.removeAllCellElements();

		initData();
		dispRows();
		// initColWidth();

	}

	private void getColRound() {
		// ����Ϊָ��
		colRound = reportInfo.getColNum();

	}

	public void dispCon() {
		List lstData = rowSetBO.getReportCons(reportInfo.getReportID(), Global
				.getSetYear());
		if (lstData == null)
			return;

	}

	// private void initColWidth() {
	// for (int i = 0; i < colRound; i++) {
	// report.setColumnWidth(i * 3, 50);
	// report.setColumnWidth(i * 3 + 1, 200);
	// report.setColumnWidth(i * 3 + 2, 100);
	// }
	//
	// }

	public Report getSearchReport(List depts, List divs, String sVerNo,
			String sBatchNoFilter, int iLoginmode) throws Exception {
		// �����
		setReportID(reportInfo);
		XMLData aData = rowSetBO.getRowSetData(rows, reportInfo.getReportID(),
				depts, divs, sVerNo, sBatchNoFilter, iLoginmode);
		dispValue(aData);
		return report;
	}

	/**
	 * ȡ�ò�ѯ�����ݣ���ƴ��һ��LIST����
	 * 
	 * @param depts
	 * @param divs
	 * @param sVerNo
	 * @param sBatchNoFilter
	 * @param iLoginmode
	 * @return
	 * @throws Exception
	 */
	public static List getSearchList(ReportInfo reportInfo, List depts,
			List divs, String sVerNo, String sBatchNoFilter, int iLoginmode)
			throws Exception {
		IRowSet rowSetBO = (IRowSet) BOCache.getBO("fb.rowSetService");

		// ��ʼ������
		List lstRows = rowSetBO.getReportRows(reportInfo.getReportID(), Global
				.getSetYear());
		int rows = new Double(Math.ceil(new Double(lstRows.size())
				.doubleValue()
				/ reportInfo.getColNum())).intValue();

		XMLData aData = rowSetBO.getRowSetData(rows, reportInfo.getReportID(),
				depts, divs, sVerNo, sBatchNoFilter, iLoginmode);
		// �������ݲ���
		if (lstRows == null)
			throw new Exception("û�����ò�ѯ����Ŀ");
		int iCount = lstRows.size();
		// �Թ̶���Ϊ��׼�������
		// ��ʼ��һ�����ڱ��ڲ��� KEY:�У�VALUE��һ��XMLData
		List lstResult = new ArrayList();
		XMLData xmlLine;// һ�е�MAP
		XMLData xmlTemp;// ��¼��ǰ�̶��е���Ϣ
		int iCol = 0, iRow = 0;
		String[] fields = getFields(reportInfo.getColNum());// ȡ���ֶ��б�
		for (int i = 0; i < iCount; i++) {
			iRow = i % rows;
			iCol = new Double(Math.floor(i / rows)).intValue();
			if (i < rows) {
				xmlLine = new XMLData();// ��һ���Ǵ���MAP
				lstResult.add(xmlLine);
			} else
				xmlLine = (XMLData) lstResult.get(iRow);

			xmlTemp = (XMLData) lstRows.get(i);
			// �����ID
			xmlLine.put(fields[iCol * 3], xmlTemp.get(RowInfo.FIELD_ITEM_ID));
			// ���������
			xmlLine.put(fields[iCol * 3 + 1], xmlTemp.get(RowInfo.FIELD_ITEM));
			// ���ֵ
			Object obj = aData.get("" + (iCol * 3 + 2) + "_" + iRow);// �Ͳ�ѯʱ���ɵ�KEY���Ӧ
			if (obj == null)
				obj = new Integer(0);
			xmlLine.put(fields[iCol * 3 + 2], obj);
		}
		// ����Ҫ��Ҫ�����û�����ݵ���
		int iMaxRows = rows * (iCol + 1);// ���㜺�еĸ���
		for (int i = iCount; i < iMaxRows; i++) {
			iRow = i % rows;
			iCol = new Double(Math.floor(i / rows)).intValue();
			if (i < rows) {
				xmlLine = new XMLData();// ��һ���Ǵ���MAP
				lstResult.add(xmlLine);
			} else
				xmlLine = (XMLData) lstResult.get(iRow);

			// �����ID
			xmlLine.put(fields[iCol * 3], null);
			// ���������
			xmlLine.put(fields[iCol * 3 + 1], null);
			// ���ֵ
			xmlLine.put(fields[iCol * 3 + 2], new Integer(0));
		}
		return lstResult;
	}

	public Report getReportWithHeader() {
		((gov.nbcs.rp.common.ui.report.Report) report)
				.tShrinkToFitRowHeight();
		return report;
	}

	private void dispValue(XMLData aData) {
		if (aData == null)
			return;
		Iterator it = aData.keySet().iterator();
		String key;
		Style st = Style.getInstance();
		st = st.deriveHorizontalAlignment(Constants.RIGHT);
		st = st.deriveFormat(new DecimalFormat("##0.##"));
		int rowBegin = ((gov.nbcs.rp.common.ui.report.Report) report)
				.getReportHeader().getRows();
		// �޸��ַ���ʾΪ����� add by XXL 20080919
		Style stLeft = st.deriveHorizontalAlignment(Constants.LEFT);
		Style curStyle;
		while (it.hasNext()) {
			curStyle = st;
			key = (String) it.next();
			String[] arrKey = key.split("_");
			int col = Integer.parseInt(arrKey[0]);
			int row = Integer.parseInt(arrKey[1]) + rowBegin;// ��������1��ʼ������Ҫ��һ���ټ��ϱ�ͷ�ĸ�
			Object value = aData.get(key);
			try {
				if (value == null || value.equals("null"))
					value = "";
				// ����ʾ��
				else if (Double.parseDouble(value.toString()) == 0) {
					value = "";
				}
			} catch (NumberFormatException e) {
				// value = "";
				// �͵��ַ���ʾ
				curStyle = stLeft;
			}

			CellElement aCell = new Cell(col, row, 1, 1, value);
			aCell.setStyle(curStyle);
			report.addCellElement(aCell);
		}
	}

	public static String[] getFields(int colCount) {
		if (colCount < 1)
			return null;
		String[] fields = new String[colCount * 3];
		for (int i = 0; i < colCount; i++) {
			String ser = ("000" + (i * 3 + 1)).substring(("000" + (i * 3 + 1))
					.length() - 3);
			fields[i * 3] = "ITEM_ID_" + ser;
			ser = ("000" + (i * 3 + 2)).substring(("000" + (i * 3 + 2))
					.length() - 3);
			fields[i * 3 + 1] = "ITEM_" + ser;
			ser = ("000" + (i * 3 + 3)).substring(("000" + (i * 3 + 3))
					.length() - 3);
			fields[i * 3 + 2] = "VALUE_" + ser;
		}
		return fields;

	}

}
