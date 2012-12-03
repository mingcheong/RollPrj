package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUserRoleRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4226338950821133319L;

	/** identifier field */
	private String USER_ID;

	/** identifier field */
	private String ROLE_ID;

	/** identifier field */
	private String RULE_ID;

	/** identifier field */
	private int IS_DEFINED;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUserRoleRule(String ROLE_ID, String RULE_ID, String USER_ID,
			int SET_YEAR, String LAST_VER, int IS_DEFINED) {
		this.ROLE_ID = ROLE_ID;
		this.RULE_ID = RULE_ID;
		this.USER_ID = USER_ID;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
		this.IS_DEFINED = IS_DEFINED;
	}

	public SysUserRoleRule(String ROLE_ID, String USER_ID) {
		this.ROLE_ID = ROLE_ID;
		this.USER_ID = USER_ID;
	}

	/** default constructor */
	public SysUserRoleRule() {
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public int getIS_DEFINED() {
		return this.IS_DEFINED;
	}

	public void setIS_DEFINED(int IS_DEFINED) {
		this.IS_DEFINED = IS_DEFINED;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ROLE_ID", getROLE_ID())
				.append("USER_ID", getUSER_ID()).toString();
	}

}
