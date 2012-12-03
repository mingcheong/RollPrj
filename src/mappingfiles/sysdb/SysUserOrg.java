package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUserOrg implements Serializable {

	/** identifier field */
	private String USER_ID;

	/** identifier field */
	private String ORG_ID;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUserOrg(String USER_ID, String ORG_ID, String LAST_VER) {
		this.USER_ID = USER_ID;
		this.ORG_ID = ORG_ID;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysUserOrg() {
	}

	/** minimal constructor */
	public SysUserOrg(String USER_ID, String ORG_ID) {
		this.USER_ID = USER_ID;
		this.ORG_ID = ORG_ID;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getORG_ID() {
		return this.ORG_ID;
	}

	public void setORG_ID(String ORG_ID) {
		this.ORG_ID = ORG_ID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("USER_ID", getUSER_ID())
				.append("ORG_ID", getORG_ID()).toString();
	}

}
