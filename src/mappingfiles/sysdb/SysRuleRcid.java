package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRuleRcid implements Serializable {

	/** identifier field */
	private Integer SET_YEAR;

	/** identifier field */
	private String RULE_ID;

	/** identifier field */
	private String RCID;

	/** full constructor */
	public SysRuleRcid(Integer SET_YEAR, String RULE_ID, String RCID) {
		this.SET_YEAR = SET_YEAR;
		this.RULE_ID = RULE_ID;
		this.RCID = RCID;
	}

	/** default constructor */
	public SysRuleRcid() {
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
	}

	public String getRCID() {
		return this.RCID;
	}

	public void setRCID(String RCID) {
		this.RCID = RCID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SET_YEAR", getSET_YEAR())
				.append("RULE_ID", getRULE_ID()).append("RCID", getRCID())
				.toString();
	}

}
