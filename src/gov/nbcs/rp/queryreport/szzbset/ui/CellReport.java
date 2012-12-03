/**
 * @# CellReport.java    <文件名>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.util.List;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:报表主类，
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
 * DATE: 2011-3-18
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class CellReport {
	XMLData xmlCodeCell = new XMLData();

	public static final String FIELD_YEAR = "SET_YEAR";

	public static final String FIELD_NO = "TYPE_NO";

	public static final String FIELD_ID = "REPORT_ID";

	public static final String FIELD_TYPE = "REPORT_TYPE";

	public static final String FIELD_CNAME = "REPORT_CNAME";

	public static final String FIELD_TITLE = "TITLE";

	public static final String FIELD_TITLEAREA = "TITLE_AREA";

	public static final String FIELD_FONT = "TITLE_FONT";

	public static final String FIELD_FONTSIZE = "TITLE_FONTSIZE";

	public static final String FIELD_COLUMN_AREA = "COLUMN_AREA";

	public static final String FIELD_COLUMN_FONT = "COLUMN_FONT";

	public static final String FIELD_COLUMN_FONTSIZE = "COLUMN_FONTSIZE";

	public static final String FIELD_UNIT = "CURRENCYUNIT";

	public static final String FIELD_OBJECTNAME = "OBJECT_NAMES";

	public static final String FIELD_ISEND = "IS_END";

	public static final String FIELD_CODE = "LVL_ID";

	public static final String FIELD_TYPE_FLAG = "TYPE_FLAG";

	public static final String FIELD_HASBATCH = "IS_HASBATCH";

	private int setYear;// 年度

	private int typeNo = 1;//

	private String reportID;// 报表ID 一报表编号规则生成

	private String reportType;// 报表类型

	private String name;// 报表名称

	private String title;// 标题

	private String titleArea;// 标题域

	//
	private String titleFont;// 标题字体名

	private int titleFontSize;// 标题字体大小

	private String columnArea;// 列区域

	private String columnFont;// 列字体

	private String columnFontsize;// 列字体大小

	private String currencyUnit;// 金额单位

	private String objectName;// 报表名

	private int isEnd;// 是报表还是目录

	//
	private String lvlID;// 编号

	private int hasBatch;// 有没有批次

	private List lstCells;// 固定单元格列表

	private List lstTitles;// 表头信息

	private List lstField;// 填充字段列表

	public String getColumnArea() {
		return columnArea;
	}

	public void setColumnArea(String columnArea) {
		this.columnArea = columnArea;
	}

	public String getColumnFont() {
		return columnFont;
	}

	public void setColumnFont(String columnFont) {
		this.columnFont = columnFont;
	}

	public String getColumnFontsize() {
		return columnFontsize;
	}

	public void setColumnFontsize(String columnFontsize) {
		this.columnFontsize = columnFontsize;
	}

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public int getHasBatch() {
		return hasBatch;
	}

	public void setHasBatch(int hasBatch) {
		this.hasBatch = hasBatch;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}

	public String getLvlID() {
		return lvlID;
	}

	public void setLvlID(String lvlID) {
		this.lvlID = lvlID;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int getSetYear() {
		return setYear;
	}

	public void setSetYear(int setYear) {
		this.setYear = setYear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleArea() {
		return titleArea;
	}

	public void setTitleArea(String titleArea) {
		this.titleArea = titleArea;
	}

	public String getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(String titleFont) {
		this.titleFont = titleFont;
	}

	public int getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public int getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(int typeNo) {
		this.typeNo = typeNo;
	}

	public XMLData getXmlCodeCell() {
		return xmlCodeCell;
	}

	public void setXmlCodeCell(XMLData xmlCodeCell) {
		this.xmlCodeCell = xmlCodeCell;
	}

	public CellReport(XMLData aData) {
		setYear = Integer
				.parseInt(Common.getAStringField(aData, FIELD_YEAR) == null ? Global
						.getSetYear()
						: Common.getAStringField(aData, FIELD_YEAR));

		typeNo = Integer.parseInt(Common.getAStringField(aData, FIELD_NO));

		reportID = Common.getAStringField(aData, FIELD_ID);

		reportType = Common.getAStringField(aData, FIELD_TYPE);

		title = Common.getAStringField(aData, FIELD_TITLE);

		titleArea = Common.getAStringField(aData, FIELD_TITLEAREA);

		titleFont = Common.getAStringField(aData, FIELD_FONT);

		titleFontSize = Integer.parseInt(Common.getAStringField(aData,
				FIELD_FONTSIZE));

		columnArea = Common.getAStringField(aData, FIELD_COLUMN_AREA);

		columnFont = Common.getAStringField(aData, FIELD_COLUMN_FONT);

		columnFontsize = Common.getAStringField(aData, FIELD_COLUMN_FONTSIZE);
		currencyUnit = Common.getAStringField(aData, FIELD_UNIT);
		objectName = Common.getAStringField(aData, FIELD_OBJECTNAME);

		isEnd = Integer
				.parseInt(Common.getAStringField(aData, FIELD_ISEND) == null ? Global
						.getSetYear()
						: Common.getAStringField(aData, FIELD_ISEND));

		lvlID = Common.getAStringField(aData, FIELD_CODE);

		hasBatch = Integer.parseInt(Common.getAStringField(aData,
				FIELD_HASBATCH));
		name = Common.getAStringField(aData, FIELD_CNAME);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getLstCells() {
		return lstCells;
	}

	public void setLstCells(List lstCells) {
		this.lstCells = lstCells;
	}

	public List getLstField() {
		return lstField;
	}

	public void setLstField(List lstField) {
		this.lstField = lstField;
	}

	public List getLstTitles() {
		return lstTitles;
	}

	public void setLstTitles(List lstTitles) {
		this.lstTitles = lstTitles;
	}

}
