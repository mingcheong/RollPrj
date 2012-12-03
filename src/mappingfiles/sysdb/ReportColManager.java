package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportColManager implements Serializable {

	/** identifier field */
	private String colId;

	/** nullable persistent field */
	private String reportId;

	/** persistent field */
	private int enable;

	/** persistent field */
	private int colType;

	/** persistent field */
	private int isVisual;

	/** persistent field */
	private int colIndex;

	/** nullable persistent field */
	private String colCode;

	/** nullable persistent field */
	private String dispName;

	/** nullable persistent field */
	private String unionName;

	/** nullable persistent field */
	private String colField;

	/** persistent field */
	private int dispWidth;

	/** nullable persistent field */
	private String colAlign;

	/** persistent field */
	private int orderIndex;

	/** persistent field */
	private int isTotal;

	/** persistent field */
	private int isMultilevel;

	/** persistent field */
	private int blevel;

	/** persistent field */
	private int elevel;

	/** persistent field */
	private int isCross;

	/** nullable persistent field */
	private String refDataSource;

	/** persistent field */
	private int refIndex;

	/** persistent field */
	private int isNumber;

	/** persistent field */
	private int intNumber;

	/** nullable persistent field */
	private String remark;

	/** nullable persistent field */
	private String eleDispmode;

	/** persistent field */
	private int isDispother;

	/** full constructor */
	public ReportColManager(String colId, String reportId, int enable,
			int colType, int isVisual, int colIndex, String colCode,
			String dispName, String unionName, String colField, int dispWidth,
			String colAlign, int orderIndex, int isTotal, int isMultilevel,
			int blevel, int elevel, int isCross, String refDataSource,
			int refIndex, int isNumber, int intNumber, String remark,
			String eleDispmode, int isDispother) {
		this.colId = colId;
		this.reportId = reportId;
		this.enable = enable;
		this.colType = colType;
		this.isVisual = isVisual;
		this.colIndex = colIndex;
		this.colCode = colCode;
		this.dispName = dispName;
		this.unionName = unionName;
		this.colField = colField;
		this.dispWidth = dispWidth;
		this.colAlign = colAlign;
		this.orderIndex = orderIndex;
		this.isTotal = isTotal;
		this.isMultilevel = isMultilevel;
		this.blevel = blevel;
		this.elevel = elevel;
		this.isCross = isCross;
		this.refDataSource = refDataSource;
		this.refIndex = refIndex;
		this.isNumber = isNumber;
		this.intNumber = intNumber;
		this.remark = remark;
		this.eleDispmode = eleDispmode;
		this.isDispother = isDispother;
	}

	/** default constructor */
	public ReportColManager() {
	}

	/** minimal constructor */
	public ReportColManager(String colId, int enable, int colType,
			int isVisual, int colIndex, int dispWidth, int orderIndex,
			int isTotal, int isMultilevel, int blevel, int elevel, int isCross,
			int refIndex, int isNumber, int intNumber, int isDispother) {
		this.colId = colId;
		this.enable = enable;
		this.colType = colType;
		this.isVisual = isVisual;
		this.colIndex = colIndex;
		this.dispWidth = dispWidth;
		this.orderIndex = orderIndex;
		this.isTotal = isTotal;
		this.isMultilevel = isMultilevel;
		this.blevel = blevel;
		this.elevel = elevel;
		this.isCross = isCross;
		this.refIndex = refIndex;
		this.isNumber = isNumber;
		this.intNumber = intNumber;
		this.isDispother = isDispother;
	}

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
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

	public int getColType() {
		return this.colType;
	}

	public void setColType(int colType) {
		this.colType = colType;
	}

	public int getIsVisual() {
		return this.isVisual;
	}

	public void setIsVisual(int isVisual) {
		this.isVisual = isVisual;
	}

	public int getColIndex() {
		return this.colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public String getColCode() {
		return this.colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public String getDispName() {
		return this.dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public String getUnionName() {
		return this.unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public String getColField() {
		return this.colField;
	}

	public void setColField(String colField) {
		this.colField = colField;
	}

	public int getDispWidth() {
		return this.dispWidth;
	}

	public void setDispWidth(int dispWidth) {
		this.dispWidth = dispWidth;
	}

	public String getColAlign() {
		return this.colAlign;
	}

	public void setColAlign(String colAlign) {
		this.colAlign = colAlign;
	}

	public int getOrderIndex() {
		return this.orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public int getIsTotal() {
		return this.isTotal;
	}

	public void setIsTotal(int isTotal) {
		this.isTotal = isTotal;
	}

	public int getIsMultilevel() {
		return this.isMultilevel;
	}

	public void setIsMultilevel(int isMultilevel) {
		this.isMultilevel = isMultilevel;
	}

	public int getBlevel() {
		return this.blevel;
	}

	public void setBlevel(int blevel) {
		this.blevel = blevel;
	}

	public int getElevel() {
		return this.elevel;
	}

	public void setElevel(int elevel) {
		this.elevel = elevel;
	}

	public int getIsCross() {
		return this.isCross;
	}

	public void setIsCross(int isCross) {
		this.isCross = isCross;
	}

	public String getRefDataSource() {
		return this.refDataSource;
	}

	public void setRefDataSource(String refDataSource) {
		this.refDataSource = refDataSource;
	}

	public int getRefIndex() {
		return this.refIndex;
	}

	public void setRefIndex(int refIndex) {
		this.refIndex = refIndex;
	}

	public int getIsNumber() {
		return this.isNumber;
	}

	public void setIsNumber(int isNumber) {
		this.isNumber = isNumber;
	}

	public int getIntNumber() {
		return this.intNumber;
	}

	public void setIntNumber(int intNumber) {
		this.intNumber = intNumber;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEleDispmode() {
		return this.eleDispmode;
	}

	public void setEleDispmode(String eleDispmode) {
		this.eleDispmode = eleDispmode;
	}

	public int getIsDispother() {
		return this.isDispother;
	}

	public void setIsDispother(int isDispother) {
		this.isDispother = isDispother;
	}

	public String toString() {
		return new ToStringBuilder(this).append("colId", getColId()).toString();
	}

}
