package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportParManager implements Serializable {

	/** identifier field */
	private String parId;

	/** nullable persistent field */
	private String reportId;

	/** persistent field */
	private int enable;

	/** nullable persistent field */
	private String parCode;

	/** nullable persistent field */
	private String dispName;

	/** persistent field */
	private int parIndex;

	/** nullable persistent field */
	private String parType;

	/** persistent field */
	private int isVisual;

	/** nullable persistent field */
	private String parField;

	/** nullable persistent field */
	private String selectSource;

	/** nullable persistent field */
	private String compareType;

	/** nullable persistent field */
	private String parDefaultvalue;

	/** nullable persistent field */
	private String parDefaultdisplayvalue;

	/** persistent field */
	private int isDisplay;

	/** persistent field */
	private int isMust;

	/** persistent field */
	private int isAdvanced;

	/** nullable persistent field */
	private String remark;

	/** nullable persistent field */
	private String colId;

	/** full constructor */
	public ReportParManager(String parId, String reportId, int enable,
			String parCode, String dispName, int parIndex, String parType,
			int isVisual, String parField, String selectSource,
			String compareType, String parDefaultvalue,
			String parDefaultdisplayvalue, int isDisplay, int isMust,
			int isAdvanced, String remark, String colId) {
		this.parId = parId;
		this.reportId = reportId;
		this.enable = enable;
		this.parCode = parCode;
		this.dispName = dispName;
		this.parIndex = parIndex;
		this.parType = parType;
		this.isVisual = isVisual;
		this.parField = parField;
		this.selectSource = selectSource;
		this.compareType = compareType;
		this.parDefaultvalue = parDefaultvalue;
		this.parDefaultdisplayvalue = parDefaultdisplayvalue;
		this.isDisplay = isDisplay;
		this.isMust = isMust;
		this.isAdvanced = isAdvanced;
		this.remark = remark;
		this.colId = colId;
	}

	/** default constructor */
	public ReportParManager() {
	}

	/** minimal constructor */
	public ReportParManager(String parId, int enable, int parIndex,
			int isVisual, int isDisplay, int isMust, int isAdvanced) {
		this.parId = parId;
		this.enable = enable;
		this.parIndex = parIndex;
		this.isVisual = isVisual;
		this.isDisplay = isDisplay;
		this.isMust = isMust;
		this.isAdvanced = isAdvanced;
	}

	public String getParId() {
		return this.parId;
	}

	public void setParId(String parId) {
		this.parId = parId;
	}

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public int getEnable() {
		return this.enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getParCode() {
		return this.parCode;
	}

	public void setParCode(String parCode) {
		this.parCode = parCode;
	}

	public String getDispName() {
		return this.dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public int getParIndex() {
		return this.parIndex;
	}

	public void setParIndex(int parIndex) {
		this.parIndex = parIndex;
	}

	public String getParType() {
		return this.parType;
	}

	public void setParType(String parType) {
		this.parType = parType;
	}

	public int getIsVisual() {
		return this.isVisual;
	}

	public void setIsVisual(int isVisual) {
		this.isVisual = isVisual;
	}

	public String getParField() {
		return this.parField;
	}

	public void setParField(String parField) {
		this.parField = parField;
	}

	public String getSelectSource() {
		return this.selectSource;
	}

	public void setSelectSource(String selectSource) {
		this.selectSource = selectSource;
	}

	public String getCompareType() {
		return this.compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public String getParDefaultvalue() {
		return this.parDefaultvalue;
	}

	public void setParDefaultvalue(String parDefaultvalue) {
		this.parDefaultvalue = parDefaultvalue;
	}

	public String getParDefaultdisplayvalue() {
		return this.parDefaultdisplayvalue;
	}

	public void setParDefaultdisplayvalue(String parDefaultdisplayvalue) {
		this.parDefaultdisplayvalue = parDefaultdisplayvalue;
	}

	public int getIsDisplay() {
		return this.isDisplay;
	}

	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}

	public int getIsMust() {
		return this.isMust;
	}

	public void setIsMust(int isMust) {
		this.isMust = isMust;
	}

	public int getIsAdvanced() {
		return this.isAdvanced;
	}

	public void setIsAdvanced(int isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String toString() {
		return new ToStringBuilder(this).append("parId", getParId()).toString();
	}

}
