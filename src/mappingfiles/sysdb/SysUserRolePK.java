package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUserRolePK implements Serializable {

	/** identifier field */
	private String userId;

	/** identifier field */
	private String roleId;

	/** full constructor */
	public SysUserRolePK(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	/** default constructor */
	public SysUserRolePK() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String toString() {
		return new ToStringBuilder(this).append("userId", getUserId()).append(
				"roleId", getRoleId()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SysUserRolePK))
			return false;
		SysUserRolePK castOther = (SysUserRolePK) other;
		return new EqualsBuilder().append(this.getUserId(),
				castOther.getUserId()).append(this.getRoleId(),
				castOther.getRoleId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getUserId()).append(getRoleId())
				.toHashCode();
	}

}
