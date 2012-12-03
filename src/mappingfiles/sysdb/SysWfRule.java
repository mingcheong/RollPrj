package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4226338950821133319L;

	/** identifier field */
	private String WF_ID;

	/** identifier field */
	private String RULE_ID;

	/** full constructor */
	public SysWfRule(String WF_ID, String RULE_ID) {
		this.WF_ID = WF_ID;
		this.RULE_ID = RULE_ID;
	}

	/** default constructor */
	public SysWfRule() {
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).append(
				"RULE_ID", getRULE_ID()).toString();
	}

}
