package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfModuleNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566229575308676280L;

	/** identifier field */
	private String NODE_ID;

	/** identifier field */
	private String MODULE_ID;

	/** full constructor */
	public SysWfModuleNode(String NODE_ID, String MODULE_ID) {
		this.NODE_ID = NODE_ID;
		this.MODULE_ID = MODULE_ID;
	}

	/** default constructor */
	public SysWfModuleNode() {
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("NODE_ID", getNODE_ID())
				.append("MODULE_ID", getMODULE_ID()).toString();
	}

}
