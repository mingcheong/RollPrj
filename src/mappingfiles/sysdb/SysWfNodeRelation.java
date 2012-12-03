package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeRelation implements Serializable {

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
	private String RELATION_TYPE;

	/** full constructor */
	public SysWfNodeRelation(String WF_ID, String NODE_ID, String NEXT_NODE_ID,
			String RELATION_TYPE) {
		this.WF_ID = WF_ID;
		this.NODE_ID = NODE_ID;
		this.NEXT_NODE_ID = NEXT_NODE_ID;
		this.RELATION_TYPE = RELATION_TYPE;
	}

	/** default constructor */
	public SysWfNodeRelation() {
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

	public String getRELATION_TYPE() {
		return this.RELATION_TYPE;
	}

	public void setRELATION_TYPE(String RELATION_TYPE) {
		this.RELATION_TYPE = RELATION_TYPE;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).append(
				"NODE_ID", getNODE_ID()).append("NEXT_NODE_ID",
				getNEXT_NODE_ID()).append("ROUTING_TYPE", getRELATION_TYPE())
				.toString();
	}

}
