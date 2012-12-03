package mappingfiles.sysdb;

import java.io.Serializable;
import java.sql.Clob;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportRetroManager implements Serializable {

	/** identifier field */
	private String retroId;

	/** nullable persistent field */
	private String reportId;

	/** persistent field */
	private int enable;

	/** nullable persistent field */
	private String retroCode;

	/** nullable persistent field */
	private String dispName;

	/** nullable persistent field */
	private String retroReportId;

	/** nullable persistent field */
	private String retroCol;

	/** nullable persistent field */
	private Clob retroReplaceXml;

	/** nullable persistent field */
	private String remark;

	/** persistent field */
	private int retroIndex;

	/** full constructor */
	public ReportRetroManager(String retroId, String reportId, int enable,
			String retroCode, String dispName, String retroReportId,
			String retroCol, Clob retroReplaceXml, String remark, int retroIndex) {
		this.retroId = retroId;
		this.reportId = reportId;
		this.enable = enable;
		this.retroCode = retroCode;
		this.dispName = dispName;
		this.retroReportId = retroReportId;
		this.retroCol = retroCol;
		this.retroReplaceXml = retroReplaceXml;
		this.remark = remark;
		this.retroIndex = retroIndex;
	}

	/** default constructor */
	public ReportRetroManager() {
	}

	/** minimal constructor */
	public ReportRetroManager(String retroId, int enable, int retroIndex) {
		this.retroId = retroId;
		this.enable = enable;
		this.retroIndex = retroIndex;
	}

	public String getRetroId() {
		return this.retroId;
	}

	public void setRetroId(String retroId) {
		this.retroId = retroId;
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

	public String getRetroCode() {
		return this.retroCode;
	}

	public void setRetroCode(String retroCode) {
		this.retroCode = retroCode;
	}

	public String getDispName() {
		return this.dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public String getRetroReportId() {
		return this.retroReportId;
	}

	public void setRetroReportId(String retroReportId) {
		this.retroReportId = retroReportId;
	}

	public String getRetroCol() {
		return this.retroCol;
	}

	public void setRetroCol(String retroCol) {
		this.retroCol = retroCol;
	}

	public Clob getRetroReplaceXml() {
		return this.retroReplaceXml;
	}

	public void setRetroReplaceXml(Clob retroReplaceXml) {
		this.retroReplaceXml = retroReplaceXml;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getRetroIndex() {
		return this.retroIndex;
	}

	public void setRetroIndex(int retroIndex) {
		this.retroIndex = retroIndex;
	}

	public String toString() {
		return new ToStringBuilder(this).append("retroId", getRetroId())
				.toString();
	}

}
