package mappingfiles.sysdb;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ReportLink implements Serializable {

	/** identifier field */
	private String linkId;

	/** identifier field */
	private String linkName;

	/** identifier field */
	private String reportId;

	/** identifier field */
	private Clob linkStr;

	/** identifier field */
	private Integer enable;

	/** identifier field */
	private Integer isSystem;

	/** identifier field */
	private String remark;

	/** identifier field */
	private String ownerName;

	/** identifier field */
	private Date createDate;

	/** full constructor */
	public ReportLink(String linkId, String linkName, String reportId,
			Clob linkStr, Integer enable, Integer isSystem, String remark,
			String ownerName, Date createDate) {
		this.linkId = linkId;
		this.linkName = linkName;
		this.reportId = reportId;
		this.linkStr = linkStr;
		this.enable = enable;
		this.isSystem = isSystem;
		this.remark = remark;
		this.ownerName = ownerName;
		this.createDate = createDate;
	}

	/** default constructor */
	public ReportLink() {
	}

	public String getLinkId() {
		return this.linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkName() {
		return this.linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Clob getLinkStr() {
		return this.linkStr;
	}

	public void setLinkStr(Clob linkStr) {
		this.linkStr = linkStr;
	}

	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Integer getIsSystem() {
		return this.isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
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

	public String toString() {
		return new ToStringBuilder(this).append("linkId", getLinkId()).append(
				"linkName", getLinkName()).append("reportId", getReportId())
				.append("linkStr", getLinkStr()).append("enable", getEnable())
				.append("isSystem", getIsSystem())
				.append("remark", getRemark()).append("ownerName",
						getOwnerName()).append("createDate", getCreateDate())
				.toString();
	}

}
