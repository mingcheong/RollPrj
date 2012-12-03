package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRoleRightGroup implements Serializable {

	/** identifier field */
	private String ROLE_ID;

	/** identifier field */
	private String RIGHT_GROUP_ID;

	/** full constructor */
	public SysRoleRightGroup(String ROLE_ID, String RIGHT_GROUP_ID) {
		this.ROLE_ID = ROLE_ID;
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
	}

	/** default constructor */
	public SysRoleRightGroup() {
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getRIGHT_GROUP_ID() {
		return this.RIGHT_GROUP_ID;
	}

	public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ROLE_ID", getROLE_ID())
				.append("RIGHT_GROUP_ID", getRIGHT_GROUP_ID()).toString();
	}

}
