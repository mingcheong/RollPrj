package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1446287468673945154L;

	/** identifier field */
	private String WF_ID;

	/** identifier field */
	private String NODE_ID;

	/** identifier field */
	private String NEXT_NODE_ID;

	/** identifier field */
	private String CONDITION_ID;

	/** identifier field */
	private String ROUTING_TYPE;

	/** full constructor */
	public SysWfNodeCondition(String WF_ID, String NODE_ID,
			String NEXT_NODE_ID, String CONDITION_ID, String ROUTING_TYPE) {
		this.WF_ID = WF_ID;
		this.NODE_ID = NODE_ID;
		this.NEXT_NODE_ID = NEXT_NODE_ID;
		this.CONDITION_ID = CONDITION_ID;
		this.ROUTING_TYPE = ROUTING_TYPE;
	}

	/** default constructor */
	public SysWfNodeCondition() {
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getNEXT_NODE_ID() {
		return this.NEXT_NODE_ID;
	}

	public void setNEXT_NODE_ID(String NEXT_NODE_ID) {
		this.NEXT_NODE_ID = NEXT_NODE_ID;
	}

	public String getCONDITION_ID() {
		return this.CONDITION_ID;
	}

	public void setCONDITION_ID(String CONDITION_ID) {
		this.CONDITION_ID = CONDITION_ID;
	}

	public String getROUTING_TYPE() {
		return this.ROUTING_TYPE;
	}

	public void setROUTING_TYPE(String ROUTING_TYPE) {
		this.ROUTING_TYPE = ROUTING_TYPE;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).append(
				"NODE_ID", getNODE_ID()).append("NEXT_NODE_ID",
				getNEXT_NODE_ID()).append("CONDITION_ID", getCONDITION_ID())
				.append("ROUTING_TYPE", getROUTING_TYPE()).toString();
	}

}
