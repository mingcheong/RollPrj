package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusvouAcctmdlRule implements Serializable {

	/** identifier field */
	private String ACCTMDL_ID;

	/** identifier field */
	private String RULE_ID;

	/** full constructor */
	public GlBusvouAcctmdlRule(String ACCTMDL_ID, String RULE_ID) {
		this.ACCTMDL_ID = ACCTMDL_ID;
		this.RULE_ID = RULE_ID;
	}

	/** default constructor */
	public GlBusvouAcctmdlRule() {
	}

	public String getACCTMDL_ID() {
		return this.ACCTMDL_ID;
	}

	public void setACCTMDL_ID(String ACCTMDL_ID) {
		this.ACCTMDL_ID = ACCTMDL_ID;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ACCTMDL_ID", getACCTMDL_ID())
				.append("RULE_ID", getRULE_ID()).toString();
	}

}
