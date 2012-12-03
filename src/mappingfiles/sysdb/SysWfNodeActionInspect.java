package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeActionInspect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5920759326250923107L;

	/** identifier field */
	private String NODE_ID;

	/** identifier field */
	private String ACTION_TYPE_CODE;

	/** persistent field */
	private String INSPECT_RULE_ID;

	/** full constructor */
	public SysWfNodeActionInspect(String NODE_ID, String ACTION_TYPE_CODE,
			String INSPECT_RULE_ID) {
		this.NODE_ID = NODE_ID;
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
		this.INSPECT_RULE_ID = INSPECT_RULE_ID;
	}

	/** default constructor */
	public SysWfNodeActionInspect() {
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getACTION_TYPE_CODE() {
		return this.ACTION_TYPE_CODE;
	}

	public void setACTION_TYPE_CODE(String ACTION_TYPE_CODE) {
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
	}

	public String getINSPECT_RULE_ID() {
		return this.INSPECT_RULE_ID;
	}

	public void setINSPECT_RULE_ID(String INSPECT_RULE_ID) {
		this.INSPECT_RULE_ID = INSPECT_RULE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("NODE_ID", getNODE_ID())
				.append("ACTION_TYPE_CODE", getACTION_TYPE_CODE()).append(
						"INSPECT_RULE_ID", getINSPECT_RULE_ID()).toString();
	}

}
