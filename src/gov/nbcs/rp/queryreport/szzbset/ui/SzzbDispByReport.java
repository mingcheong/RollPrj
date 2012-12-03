/**
 * @# SzzbDispByReport.java    <文件名>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.Report;

/**
 * 功能说明:收支总表的显示，可以传入一报表，以便不用再创建报表实例
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * 版权所有：浙江易桥
 * <P>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <P>
 * 侵权者将受到法律追究。
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
 * DATE: 2011-6-14
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class SzzbDispByReport {

	private Report report;

	private String reportID;// 报表的ID字段

	private int titleStart;

	private List lstConsCell;

	private XMLData xmlForCell = new XMLData();

	private XMLData xmlColStyle = new XMLData();

	private XMLData xmlIsHideCol = new XMLData();

	private DataSet dsRepset;

	private int bodyStart;

	private int headerStartCol;// 表头开始列

	private int headerEndCol;// 表头结束列

	public SzzbDispByReport(Report report, String sReportID) throws Exception {
		this.report = report;
		if (this.report == null)
			this.report = new gov.nbcs.rp.common.ui.report.Report();
		setReportID(sReportID);
	}

	public void setReportID(String sReportID) {
		try {
			this.reportID = sReportID;
			report.removeAllCellElements();
			getData();
			dispConsCells();
		} catch (Exception e) {
			new MessageBox("初始化界面失败!", e.getMessage(), MessageBox.ERROR,
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
		headerStartCol = cells.getColumn();
		headerEndCol = headerStartCol + cells.getColumnSpan();
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
			// 设格式
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
			// add by zlx 2009-5-20 设置表头颜色
			if (iRow < bodyStart) {
				style = style.deriveBackground(ColorBackground
						.getInstance(UntPub.HEADER_COLOR));
			}

			cellElement.setStyle(style);
			report.setColumnWidth(iColumn, Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_COLWIDTH)));
			report.setRowHeight(iRow, Integer.parseInt(Common.getAStringField(
					aData, ISzzbSet.FIELD_ROWHEIGHT)));
			report.addCellElement(cellElement, false);
			report.shrinkToFitRowHeight();
		}
	}

	/**
	 * 取得各种数据
	 * 
	 */
	private void getData() throws Exception {
		// 固定单元格
		lstConsCell = SzzbSetI.getMethod().getSzzbCons(this.reportID,
				Global.getSetYear());
		List lstFor = SzzbSetI.getMethod().getSzzbFor(reportID,
				Global.getSetYear());
		if (lstConsCell == null || lstConsCell.size() == 0 || lstFor == null)
			return;
		// 活动单元格
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

		// 列的格式信息
		DataSet dsHeader = SzzbSetI.getMethod().getReportHeader(this.reportID,
				Global.getSetYear());
		// 取得表头的区域信息
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
				String sIsHideCol = dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
						.getString();
				if (Common.estimate(sIsHideCol))
					xmlIsHideCol.put(transCharToNumber(sColChar), sIsHideCol);
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

	public Report getSearchReport(String sVerNo, String sReportID,
			String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception {

		if (Common.isNullStr(reportID))
			throw new Exception("报表编号没有指定，或有错误");
		// 刷新界面上的数据
		report.removeAllCellElements();

		dispConsCells();

		XMLData aData = SzzbSetI.getMethod().getSzzbValuesByDetail(xmlForCell,
				sVerNo, sReportID, sBatchNoFilter, lstDept, lstDiv,
				sFieldSelect, setYear, iTypeFlag,isConvert);
		dispValues(aData);

		report.shrinkToFitRowHeight();
		// add by zlx 2009-5-20 不显示标题,统一风格
		this.removeTitle();
		this.setHideCol();
		return report;

	}

	/**
	 * 展示数据
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

			CellElement eleCell = report.getCellElement(col, row);
			if (eleCell == null) {
				eleCell = new CellElement(col, row);
				report.addCellElement(eleCell);
			}
			Object obj = aData.get(sKey);
			String sValue = "";
			if (obj != null)
				sValue = obj.toString();

			if (Common.isNullStr(sValue))
				continue;
			try {
				BigDecimal bd = new BigDecimal(sValue);
				if (bd.compareTo(new BigDecimal("0")) == 0)
					continue;
			} catch (Exception e) {

			}
			eleCell.setValue(aData.get(sKey));
			Object objFormat = xmlColStyle.get(String.valueOf(col));
			if (objFormat != null) {
				Style st = eleCell.getStyle().deriveFormat(
						new DecimalFormat(objFormat.toString()));
				eleCell.setStyle(st);
			}
		}
	}

	private static int getColFromKey(String sKey) {
		sKey = sKey.substring(1);
		int iPos = sKey.indexOf("_");
		return Integer.parseInt(sKey.substring(0, iPos));

	}

	private static int getRowFromKey(String sKey) {
		sKey = sKey.substring(1);
		int iPos = sKey.indexOf("_");
		return Integer.parseInt(sKey.substring(iPos + 1));

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
		col = col - 1;// 列是从零开始的
		return String.valueOf(col);
	}

	public Report getReportWithHeader() {
		// add by zlx 2009-5-20 不显示标题,统一风格
		this.removeTitle();
		this.setHideCol();
		return report;
	}

	public static DataSet getReportDS(String sReportId) throws Exception {
		return SzzbSetI.getMethod().getSzzbConsDsWithoutTitle(sReportId,
				Global.getSetYear());

	}

	public static DataSet getReportDsByValue(String sVerNo, String sReportID,
			String sBatchNoFilter, List lstDept, List lstDiv,
			String sFieldSelect, String setYear, int iTypeFlag,boolean isConvert)
			throws Exception {

		if (Common.isNullStr(sReportID))
			throw new Exception("报表编号没有指定，或有错误");
		DataSet dsCons = getReportDS(sReportID);

		List lstFor = SzzbSetI.getMethod().getSzzbFor(sReportID,
				Global.getSetYear());
		if (lstFor == null)
			return dsCons;
		// 活动单元格
		XMLData xmlForCell = new XMLData();
		int iCount = lstFor.size();
		XMLData aData;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstFor.get(i);
			CellFieldInfo aField = new CellFieldInfo(aData);
			xmlForCell.put(
					"" + aField.getCellRow() + ":" + aField.getCellCol(),
					aField);
		}

		aData = SzzbSetI.getMethod().getSzzbValuesByDetail(xmlForCell, sVerNo,
				sReportID, sBatchNoFilter, lstDept, lstDiv, sFieldSelect,
				setYear, iTypeFlag,isConvert);
		// 合并设值

		if (aData == null || aData.isEmpty())
			return dsCons;

		dsCons = setValue(aData, dsCons);
		// 将所有的行修正从零开始计算
		dsCons.beforeFirst();
		int iHeadStart = SzzbSetI.getMethod().getHeadStartRow(sReportID,
				setYear);
		if (iHeadStart < 0)
			throw new Exception("表头定义可能有错误，没有查询到表头的范围信息! ");
		dsCons.edit();
		while (dsCons.next()) {
			dsCons.fieldByName("field_row").setValue(
					String.valueOf(dsCons.fieldByName("field_row").getInteger()
							- iHeadStart));
		}
		dsCons.applyUpdate();
		return dsCons;

	}

	private static DataSet setValue(XMLData xmlValue, DataSet dsCons)
			throws Exception {
		dsCons.maskDataChange(true);
		dsCons.edit();
		try {
			Iterator it = xmlValue.keySet().iterator();
			int col = 1, row = 1;
			while (it.hasNext()) {
				String sKey = it.next().toString();
				col = getColFromKey(sKey);
				row = getRowFromKey(sKey);
				boolean found = dsCons.locate("FIELD_COLUMN", new Integer(col),
						"FIELD_ROW", new Integer(row));
				if (!found)
					throw new Exception("没有找到待插入的单元格信息");
				dsCons.fieldByName("FIELD_VALUE").setValue(xmlValue.get(sKey));

			}
		} finally {
			dsCons.maskDataChange(false);
		}
		return dsCons;

	}

	public int getTitleStart() {
		return titleStart;
	}

	/**
	 * 不显示标题,统一风格
	 * 
	 */
	private void removeTitle() {
		for (int i = titleStart - 1; i >= 0; i--) {
			report.removeRow(i);
		}
	}

	private void setHideCol() {
		// add by zlx 隐藏列
		for (int i = headerStartCol; i < headerEndCol; i++) {
			if (xmlIsHideCol.containsKey(String.valueOf(i))) {
				report.setColumnWidth(i, 0);
			}
		}
	}

}
