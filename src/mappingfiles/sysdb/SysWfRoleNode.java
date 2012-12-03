package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfRoleNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4994565074410422326L;

	/** identifier field */
	private String NODE_ID;

	/** identifier field */
	private String ROLE_ID;

	/** full constructor */
	public SysWfRoleNode(String NODE_ID, String ROLE_ID) {
		this.NODE_ID = NODE_ID;
		this.ROLE_ID = ROLE_ID;
	}

	/** default constructor */
	public SysWfRoleNode() {
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("NODE_ID", getNODE_ID())
				.append("ROLE_ID", getROLE_ID()).toString();
	}

}
