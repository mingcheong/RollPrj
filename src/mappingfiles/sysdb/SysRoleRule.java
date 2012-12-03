package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRoleRule implements Serializable {

	/** identifier field */
	private String ROLE_ID;

	/** identifier field */
	private String RULE_ID;

	/** full constructor */
	public SysRoleRule(String ROLE_ID, String RULE_ID) {
		this.ROLE_ID = ROLE_ID;
		this.RULE_ID = RULE_ID;
	}

	/** default constructor */
	public SysRoleRule() {
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ROLE_ID", getROLE_ID())
				.append("RULE_ID", getRULE_ID()).toString();
	}

}
