package mappingfiles.sysdb;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportManager implements Serializable {

	/** identifier field */
	private String reportId;

	/** nullable persistent field */
	private String reportCode;

	/** nullable persistent field */
	private String reportName;

	/** persistent field */
	private int intDbtype;

	/** nullable persistent field */
	private String reportSource;

	/** nullable persistent field */
	private String retroId;

	/** persistent field */
	private int isMixcol;

	/** persistent field */
	private int isTmptable;

	/** persistent field */
	private int defaultFixcol;

	/** persistent field */
	private int defaultPagesize;

	/** nullable persistent field */
	private Clob sqlBefore;

	/** nullable persistent field */
	private Clob sqlMiddle;

	/** nullable persistent field */
	private Clob sqlAfter;

	/** nullable persistent field */
	private String remark;

	/** nullable persistent field */
	private String ownerName;

	/** nullable persistent field */
	private Date createDate;

	/** nullable persistent field */
	private Date startDate;

	/** nullable persistent field */
	private Date endDate;

	/** persistent field */
	private int enable;

	/** persistent field */
	private int isValueonlydisp;

	/** persistent field */
	private int isDisadvance;

	/** nullable persistent field */
	private Integer isGraphana;

	/** nullable persistent field */
	private Integer isCheckbill;

	/** nullable persistent field */
	private Clob checkHint;

	/** full constructor */
	public ReportManager(String reportId, String reportCode, String reportName,
			int intDbtype, String reportSource, String retroId, int isMixcol,
			int isTmptable, int defaultFixcol, int defaultPagesize,
			Clob sqlBefore, Clob sqlMiddle, Clob sqlAfter, String remark,
			String ownerName, Date createDate, Date startDate, Date endDate,
			int enable, int isValueonlydisp, int isDisadvance,
			Integer isGraphana, Integer isCheckbill, Clob checkHint) {
		this.reportId = reportId;
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.intDbtype = intDbtype;
		this.reportSource = reportSource;
		this.retroId = retroId;
		this.isMixcol = isMixcol;
		this.isTmptable = isTmptable;
		this.defaultFixcol = defaultFixcol;
		this.defaultPagesize = defaultPagesize;
		this.sqlBefore = sqlBefore;
		this.sqlMiddle = sqlMiddle;
		this.sqlAfter = sqlAfter;
		this.remark = remark;
		this.ownerName = ownerName;
		this.createDate = createDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.enable = enable;
		this.isValueonlydisp = isValueonlydisp;
		this.isDisadvance = isDisadvance;
		this.isGraphana = isGraphana;
		this.isCheckbill = isCheckbill;
		this.checkHint = checkHint;
	}

	/** default constructor */
	public ReportManager() {
	}

	/** minimal constructor */
	public ReportManager(String reportId, int intDbtype, int isMixcol,
			int isTmptable, int defaultFixcol, int defaultPagesize, int enable,
			int isValueonlydisp, int isDisadvance) {
		this.reportId = reportId;
		this.intDbtype = intDbtype;
		this.isMixcol = isMixcol;
		this.isTmptable = isTmptable;
		this.defaultFixcol = defaultFixcol;
		this.defaultPagesize = defaultPagesize;
		this.enable = enable;
		this.isValueonlydisp = isValueonlydisp;
		this.isDisadvance = isDisadvance;
	}

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getIntDbtype() {
		return this.intDbtype;
	}

	public void setIntDbtype(int intDbtype) {
		this.intDbtype = intDbtype;
	}

	public String getReportSource() {
		return this.reportSource;
	}

	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}

	public String getRetroId() {
		return this.retroId;
	}

	public void setRetroId(String retroId) {
		this.retroId = retroId;
	}

	public int getIsMixcol() {
		return this.isMixcol;
	}

	public void setIsMixcol(int isMixcol) {
		this.isMixcol = isMixcol;
	}

	public int getIsTmptable() {
		return this.isTmptable;
	}

	public void setIsTmptable(int isTmptable) {
		this.isTmptable = isTmptable;
	}

	public int getDefaultFixcol() {
		return this.defaultFixcol;
	}

	public void setDefaultFixcol(int defaultFixcol) {
		this.defaultFixcol = defaultFixcol;
	}

	public int getDefaultPagesize() {
		return this.defaultPagesize;
	}

	public void setDefaultPagesize(int defaultPagesize) {
		this.defaultPagesize = defaultPagesize;
	}

	public Clob getSqlBefore() {
		return this.sqlBefore;
	}

	public void setSqlBefore(Clob sqlBefore) {
		this.sqlBefore = sqlBefore;
	}

	public Clob getSqlMiddle() {
		return this.sqlMiddle;
	}

	public void setSqlMiddle(Clob sqlMiddle) {
		this.sqlMiddle = sqlMiddle;
	}

	public Clob getSqlAfter() {
		return this.sqlAfter;
	}

	public void setSqlAfter(Clob sqlAfter) {
		this.sqlAfter = sqlAfter;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getEnable() {
		return this.enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public int getIsValueonlydisp() {
		return this.isValueonlydisp;
	}

	public void setIsValueonlydisp(int isValueonlydisp) {
		this.isValueonlydisp = isValueonlydisp;
	}

	public int getIsDisadvance() {
		return this.isDisadvance;
	}

	public void setIsDisadvance(int isDisadvance) {
		this.isDisadvance = isDisadvance;
	}

	public Integer getIsGraphana() {
		return this.isGraphana;
	}

	public void setIsGraphana(Integer isGraphana) {
		this.isGraphana = isGraphana;
	}

	public Integer getIsCheckbill() {
		return this.isCheckbill;
	}

	public void setIsCheckbill(Integer isCheckbill) {
		this.isCheckbill = isCheckbill;
	}

	public Clob getCheckHint() {
		return this.checkHint;
	}

	public void setCheckHint(Clob checkHint) {
		this.checkHint = checkHint;
	}

	public String toString() {
		return new ToStringBuilder(this).append("reportId", getReportId())
				.toString();
	}

}
