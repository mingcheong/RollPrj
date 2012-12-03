package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRoleRcid implements Serializable {

	/** identifier field */
	private Integer SET_YEAR;

	/** identifier field */
	private String ROLE_ID;

	/** identifier field */
	private String RCID;

	/** full constructor */
	public SysRoleRcid(Integer SET_YEAR, String ROLE_ID, String RCID) {
		this.SET_YEAR = SET_YEAR;
		this.ROLE_ID = ROLE_ID;
		this.RCID = RCID;
	}

	/** default constructor */
	public SysRoleRcid() {
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getRCID() {
		return this.RCID;
	}

	public void setRCID(String RCID) {
		this.RCID = RCID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SET_YEAR", getSET_YEAR())
				.append("ROLE_ID", getROLE_ID()).append("RCID", getRCID())
				.toString();
	}

}
