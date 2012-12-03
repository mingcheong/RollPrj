/**
 * @# SzzbDisp.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;

/**
 * ����˵��:�����֧��Ĳ�ѯչʾ����������
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-21
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class SzzbDisp extends Report {

	private String reportID;// �����ID�ֶ�

	private int titleStart;

	private List lstConsCell;

	private XMLData xmlForCell = new XMLData();

	private XMLData xmlColStyle = new XMLData();

	private DataSet dsRepset;

	private int bodyStart;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4393455155153034872L;

	public SzzbDisp() throws Exception {
		super();
	}

	public SzzbDisp(String sReportID) throws Exception {
		super();
		setReportID(sReportID);

	}

	public void setReportID(String sReportID) {
		try {
			this.reportID = sReportID;
			this.removeAllCellElements();
			getData();
			dispConsCells();
		} catch (Exception e) {
			new MessageBox("��ʼ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			return;
		}
	}

	private void dispConsCells() throws Exception {

		if (lstConsCell == null || lstConsCell.size() == 0)
			return;
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;

		// int colStart = thDetail.getColumns();
		Object oValue;
		Cell cellElement;
		XMLData aData;
		int iCount = lstConsCell.size();
		dsRepset.beforeFirst();
		dsRepset.next();
		CellSelection cells = ReportUtil.translateToNumber(dsRepset
				.fieldByName(IQrBudget.COLUMN_AREA).getString());
		titleStart = cells.getRow();
		bodyStart = titleStart + cells.getRowSpan();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstConsCell.get(i);
			iColumn = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMN));
			iColumnSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMNSPAN));
			iRow = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROW));
			iRowSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROWSPAN));
			oValue = Common.getAStringField(aData, ISzzbSet.FIELD_VALUE);

			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// ���ʽ
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.HOR_ALIGNMENT))).deriveVerticalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.VER_ALIGNMENT))).deriveFRFont(
					FRFont.getInstance(Common.getAStringField(aData,
							ISzzbSet.FIELD_FONT), Integer.parseInt(Common
							.getAStringField(aData, ISzzbSet.FIELD_FONTSTYPE)),
							Integer.parseInt(Common.getAStringField(aData,
									ISzzbSet.FIELD_FONTSIZE))));

			if (iRow >= titleStart) {
				style = style.deriveBorder(1, Color.BLACK, 1, Color.BLACK, 1,
						Color.BLACK, 1, Color.BLACK);

			}

			cellElement.setStyle(style);
			setColumnWidth(iColumn, Integer.parseInt(Common.getAStringField(
					aData, ISzzbSet.FIELD_COLWIDTH)));
			setRowHeight(iRow, Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROWHEIGHT)));
			addCellElement(cellElement, false);
		}
	}

	/**
	 * ȡ�ø�������
	 * 
	 */
	private void getData() throws Exception {
		// �̶���Ԫ��
		lstConsCell = SzzbSetI.getMethod().getSzzbCons(this.reportID,
				Global.getSetYear());
		List lstFor = SzzbSetI.getMethod().getSzzbFor(reportID,
				Global.getSetYear());
		if (lstConsCell == null || lstConsCell.size() == 0 || lstFor == null)
			return;
		// ���Ԫ��
		xmlForCell.clear();
		int iCount = lstFor.size();
		XMLData aData;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstFor.get(i);
			CellFieldInfo aField = new CellFieldInfo(aData);
			xmlForCell.put(
					"" + aField.getCellRow() + ":" + aField.getCellCol(),
					aField);
		}

		// �еĸ�ʽ��Ϣ
		DataSet dsHeader = SzzbSetI.getMethod().getReportHeader(this.reportID,
				Global.getSetYear());
		// ȡ�ñ�ͷ��������Ϣ
		if (dsHeader != null && !dsHeader.isEmpty()) {
			dsHeader.beforeFirst();
			dsHeader.next();
			while (!dsHeader.eof()) {
				String sColChar = dsHeader.fieldByName(IQrBudget.FIELD_CNAME)
						.getString();
				String sCoFormat = dsHeader.fieldByName(
						IQrBudget.FIELD_DISFORMAT).getString();
				if (!Common.isNullStr(sCoFormat))
					xmlColStyle.put(transCharToNumber(sColChar), sCoFormat);
				dsHeader.next();
			}
		}
		dsRepset = SzzbSetI.getMethod()
				.getRepset(Global.loginYear, 1, reportID);
		if (lstConsCell == null || lstConsCell.size() == 0
				|| dsRepset.getRecordCount() < 1) {
			this.reportID = null;
		}

	}

	public int getBodyStart() {
		return bodyStart;
	}

	public String doQuery(String sVerNo, String sReportID,
			String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception {

		if (Common.isNullStr(reportID))
			return "������û��ָ�������д���";
		// ˢ�½����ϵ�����
		this.removeAllCellElements();

		try {
			dispConsCells();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();

		}
		XMLData aData = SzzbSetI.getMethod().getSzzbValuesByDetail(xmlForCell,
				sVerNo, sReportID, sBatchNoFilter, lstDept, lstDiv,
				sFieldSelect, setYear, iTypeFlag, isConvert);
		dispValues(aData);
		return "";

	}

	/**
	 * չʾ����
	 * 
	 * @param aData
	 */
	private void dispValues(XMLData aData) {
		if (aData == null || aData.isEmpty())
			return;
		Iterator it = aData.keySet().iterator();
		int col = 1, row = 1;
		while (it.hasNext()) {
			String sKey = it.next().toString();
			col = getColFromKey(sKey);
			row = getRowFromKey(sKey);

			CellElement eleCell = this.getCellElement(col, row);
			if (eleCell == null) {
				eleCell = new CellElement(col, row);
				this.addCellElement(eleCell);
			}
			eleCell.setValue(aData.get(sKey));
			Object objFormat = xmlColStyle.get(String.valueOf(col));
			if (objFormat != null)
				eleCell.getStyle().deriveFormat(
						new DecimalFormat(objFormat.toString()));

		}

	}

	private int getColFromKey(String sKey) {
		sKey = sKey.substring(1);
		int iPos = sKey.indexOf("_");
		return Integer.parseInt(sKey.substring(0, iPos));

	}

	private int getRowFromKey(String sKey) {
		sKey = sKey.substring(1);
		int iPos = sKey.indexOf("_");
		return Integer.parseInt(sKey.substring(iPos + 1));

	}

	public static void main(String[] args) {
		SzzbDisp sd = null;
		try {
			sd = new SzzbDisp();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

		String i = sd.transCharToNumber("b");
		System.out.println(String.valueOf(i));
	}

	private String transCharToNumber(String colChar) {
		byte[] bytes = new byte[2];
		bytes = Common.trimNumberToStr(colChar.toUpperCase()).getBytes();
		int j = 0;
		int col = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			j++;
			col = col + 25 * (j - 1) + (bytes[i] - 65 + 1);
		}
		col = col - 1;// ���Ǵ��㿪ʼ��
		return String.valueOf(col);
	}
}
