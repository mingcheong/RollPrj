/**
 * @# ReportInfo.java    <文件名>
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class ReportInfo {

	public static final String FIELD_ID = "REPORT_ID";

	public static final String FIELD_CNAME = "REPORT_CNAME";

	public static final String FIELD_TITLE = "TITLE";

	public static final String FIELD_UNIT = "CURRENCYUNIT";

	public static final String FIELD_USER = "DATA_USER";

	public static final String FIELD_VIEWNAME = "OBJECT_NAMES";

	public static final String FIELD_BATCH = "IS_HASBATCH";

	public static final String FIELD_MULTI = "IS_MULTICOND";

	public static final String FIELD_BUSSTYPE = "TYPE_FLAG";

	public static final String FIELD_OPERTYPE = "OPER_TYPE";

	public static final String FIELD_COL_NUM = "COL_NUM";

	public static final String FIELD_FUNDSOURCE_FLAG = "FUNDSOURCE_FLAG";

	public static final String FIELD_COMPARE_FLAG = "COMPARE_FLAG";

	private String reportID;// 报表ID

	private String title;// 标题

	private String name;// 名称

	private String operType;// 操作类型，分为我们处理，和平台处理

	private String unit;// 资金单位

	private String viewName;// 视图名称

	private boolean hasBatch;// 是否有批次

	private boolean multiCond;// 是否多选

	private int bussType;// 业务类型

	// private boolean isZHCReport = false;;// 是不是综合处的报表

	private Object searchObj; // 查询表类

	private Map paraMap;// 参数

	private int colNum;// 列数

	// 变换数据源
	private String changeSource;

	// 对比分析表对比1，对比2标识
	private String compareFlag;

	// 表标题附加信息
	private String sAddOnsTitle;

	// 列宽(收支总表和封面）
	private List lstFieldColWidth;

	public ReportInfo() {
		super();
	}

	public ReportInfo(XMLData aData) {
		super();
		if (aData == null)
			return;
		this.reportID = Common.getAStringField(aData, FIELD_ID);
		this.name = Common.getAStringField(aData, FIELD_CNAME);
		this.title = Common.getAStringField(aData, FIELD_TITLE);
		this.operType = Common.getAStringField(aData, FIELD_OPERTYPE);
		this.viewName = Common.getAStringField(aData, FIELD_VIEWNAME);
		this.hasBatch = ("" + Common.getAStringField(aData, FIELD_BATCH))
				.equals("是");
		this.multiCond = ("" + Common.getAStringField(aData, FIELD_BATCH))
				.equals("是");
		this.bussType = Integer.parseInt(Common.getAStringField(aData,
				FIELD_BUSSTYPE));
		// if (this.bussType >= IDefineReport.ZCH_FLAG) {
		// this.bussType = bussType;
		// isZHCReport = true;
		// }
		this.unit = Common.getAStringField(aData, FIELD_UNIT);
		String sNum = Common.getAStringField(aData, FIELD_COL_NUM);
		if (Common.isNullStr(sNum))
			sNum = "-1";
		this.colNum = Integer.parseInt(sNum);
		if (aData.get(FIELD_FUNDSOURCE_FLAG) == null) {
			this.changeSource = "0";
		} else {
			this.changeSource = aData.get(FIELD_FUNDSOURCE_FLAG).toString();
		}
		if (aData.get(FIELD_COMPARE_FLAG) == null) {
			this.compareFlag = "0";
		} else {
			this.compareFlag = aData.get(FIELD_COMPARE_FLAG).toString();
		}

	}

	public ReportInfo(DataSet dsHeader) throws Exception {
		if (dsHeader == null || dsHeader.bof() || dsHeader.getRecordCount() < 1)
			return;

		this.reportID = dsHeader.fieldByName(FIELD_ID).getString();
		this.name = dsHeader.fieldByName(FIELD_CNAME).getString();
		this.title = dsHeader.fieldByName(FIELD_TITLE).getString();
		this.operType = dsHeader.fieldByName(FIELD_OPERTYPE).getString();
		this.viewName = dsHeader.fieldByName(FIELD_VIEWNAME).getString();
		this.hasBatch = ("" + dsHeader.fieldByName(FIELD_BATCH).getString())
				.equals("是");
		this.multiCond = ("" + dsHeader.fieldByName(FIELD_MULTI).getString())
				.equals("是");
		this.bussType = dsHeader.fieldByName(FIELD_BUSSTYPE).getInteger();
		// if (this.bussType >= IDefineReport.ZCH_FLAG) {
		// this.bussType = (bussType % IDefineReport.ZCH_FLAG);
		// isZHCReport = true;
		// }
		this.unit = dsHeader.fieldByName(FIELD_UNIT).getString();
		if (dsHeader.fieldByName(FIELD_COL_NUM).getValue() != null)
			this.colNum = dsHeader.fieldByName(FIELD_COL_NUM).getInteger();

		if (dsHeader.fieldByName(FIELD_FUNDSOURCE_FLAG).getValue() == null) {
			this.changeSource = "0";
		} else {
			this.changeSource = dsHeader.fieldByName(FIELD_FUNDSOURCE_FLAG)
					.toString();
		}
		if (dsHeader.fieldByName(FIELD_COMPARE_FLAG).getValue() == null) {
			this.compareFlag = "0";
		} else {
			this.compareFlag = dsHeader.fieldByName(FIELD_COMPARE_FLAG)
					.toString();
		}

	}

	/**
	 * add by zlx
	 * 
	 * @param map
	 *            repset表一条记录
	 * @throws Exception
	 */
	public ReportInfo(Map map) throws Exception {

		this.reportID = map.get(FIELD_ID).toString();
		this.name = map.get(FIELD_CNAME).toString();
		this.title = map.get(FIELD_TITLE).toString();
		// this.operType = map.get(FIELD_OPERTYPE).toString();
		if (map.get(FIELD_VIEWNAME) != null)
			this.viewName = map.get(FIELD_VIEWNAME).toString();
		if (map.get(FIELD_BATCH) != null)
			this.hasBatch = ("" + map.get(FIELD_BATCH).toString()).equals("是");
		if (map.get(FIELD_MULTI) != null)
			this.multiCond = ("" + map.get(FIELD_MULTI).toString()).equals("是");
		this.bussType = Integer.parseInt(map.get(FIELD_BUSSTYPE).toString());
		// if (this.bussType >= IDefineReport.ZCH_FLAG) {
		// this.bussType = (bussType % IDefineReport.ZCH_FLAG);
		// isZHCReport = true;
		// }
		if (map.get(FIELD_UNIT) != null)
			this.unit = map.get(FIELD_UNIT).toString();
		Object sNum = map.get(FIELD_COL_NUM);
		if (sNum == null || Common.isNullStr(sNum.toString()))
			sNum = "-1";
		this.colNum = Integer.parseInt(sNum.toString());
		if (map.get(FIELD_FUNDSOURCE_FLAG) == null) {
			this.changeSource = "0";
		} else {
			this.changeSource = map.get(FIELD_FUNDSOURCE_FLAG).toString();
		}
		if (map.get(FIELD_COMPARE_FLAG) == null) {
			this.compareFlag = "0";
		} else {
			this.compareFlag = map.get(FIELD_COMPARE_FLAG).toString();
		}
	}

	public boolean isHasBatch() {
		return hasBatch;
	}

	public void setHasBatch(boolean hasBatch) {
		this.hasBatch = hasBatch;
	}

	public boolean isMultiCond() {
		return multiCond;
	}

	public void setMultiCond(boolean multiCond) {
		this.multiCond = multiCond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public int getBussType() {
		return bussType;
	}

	public void setBussType(int bussType) {
		this.bussType = bussType;
	}

	public Object getSearchObj() {
		return searchObj;
	}

	public void setSearchObj(Object searchObj) {
		this.searchObj = searchObj;
	}

	public Map getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map paraMap) {
		this.paraMap = paraMap;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public String getChangeSource() {
		return changeSource;
	}

	public void setChangeSource(String changeSource) {
		this.changeSource = changeSource;
	}

	public String getCompareFlag() {
		return compareFlag;
	}

	public void setCompareFlag(String compareFlag) {
		this.compareFlag = compareFlag;
	}

	public String getAddOnsTitle() {
		return sAddOnsTitle;
	}

	public void setAddOnsTitle(String addOnsTitle) {
		sAddOnsTitle = addOnsTitle;
	}

	public List getLstFieldColWidth() {
		return lstFieldColWidth;
	}

	public void setLstFieldColWidth(List lstFieldColWidth) {
		this.lstFieldColWidth = lstFieldColWidth;
	}

}
