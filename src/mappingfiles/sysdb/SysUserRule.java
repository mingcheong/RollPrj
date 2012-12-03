package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUserRule implements Serializable {

	/** identifier field */
	private String USER_ID;

	/** identifier field */
	private String RULE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUserRule(String USER_ID, String RULE_ID, int SET_YEAR,
			String LAST_VER) {
		this.USER_ID = USER_ID;
		this.RULE_ID = RULE_ID;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysUserRule() {
	}

	/** minimal constructor */
	public SysUserRule(String USER_ID, int SET_YEAR) {
		this.USER_ID = USER_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getRULE_ID() {
		return this.RULE_ID;
	}

	public void setRULE_ID(String RULE_ID) {
		this.RULE_ID = RULE_ID;
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

	public String toString() {
		return new ToStringBuilder(this).append("USER_ID", getUSER_ID())
				.toString();
	}

}
