package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfFlowRight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1425793195369099938L;

	/** identifier field */
	private String WF_ID;

	/** identifier field */
	private String RIGHT_GROUP_ID;

	/** full constructor */
	public SysWfFlowRight(String WF_ID, String RIGHT_GROUP_ID) {
		this.WF_ID = WF_ID;
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
	}

	/** default constructor */
	public SysWfFlowRight() {
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getRIGHT_GROUP_ID() {
		return this.RIGHT_GROUP_ID;
	}

	public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).append(
				"RIGHT_GROUP_ID", getRIGHT_GROUP_ID()).toString();
	}

}
