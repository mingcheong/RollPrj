package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysReportparam implements Serializable {

	/** identifier field */
	private String chrId;

	/** identifier field */
	private String chrName;

	/** identifier field */
	private String chrCode;

	/** full constructor */
	public SysReportparam(String chrId, String chrName, String chrCode) {
		this.chrId = chrId;
		this.chrName = chrName;
		this.chrCode = chrCode;
	}

	/** default constructor */
	public SysReportparam() {
	}

	public String getChrId() {
		return this.chrId;
	}

	public void setChrId(String chrId) {
		this.chrId = chrId;
	}

	public String getChrName() {
		return this.chrName;
	}

	public void setChrName(String chrName) {
		this.chrName = chrName;
	}

	public String getChrCode() {
		return this.chrCode;
	}

	public void setChrCode(String chrCode) {
		this.chrCode = chrCode;
	}

	public String toString() {
		return new ToStringBuilder(this).append("chrId", getChrId()).append(
				"chrName", getChrName()).append("chrCode", getChrCode())
				.toString();
	}

}
