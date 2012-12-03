package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysSession implements Serializable {

	/** identifier field */
	private String SESSION_ID;

	/** persistent field */
	private String SYS_ID;

	/** persistent field */
	private String USER_ID;

	/** persistent field */
	private String ROLE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String USER_IP;

	/** nullable persistent field */
	private String LOGIN_DATE;

	/** nullable persistent field */
	private String LOGOUT_DATE;

	/** nullable persistent field */
	private String SESSION_VALUE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysSession(String SESSION_ID, String SYS_ID, String USER_ID,
			String ROLE_ID, int SET_YEAR, String USER_IP, String LOGIN_DATE,
			String LOGOUT_DATE, String SESSION_VALUE, String LAST_VER) {
		this.SESSION_ID = SESSION_ID;
		this.SYS_ID = SYS_ID;
		this.USER_ID = USER_ID;
		this.ROLE_ID = ROLE_ID;
		this.SET_YEAR = SET_YEAR;
		this.USER_IP = USER_IP;
		this.LOGIN_DATE = LOGIN_DATE;
		this.LOGOUT_DATE = LOGOUT_DATE;
		this.SESSION_VALUE = SESSION_VALUE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysSession() {
	}

	/** minimal constructor */
	public SysSession(String SESSION_ID, String SYS_ID, String USER_ID,
			String ROLE_ID, int SET_YEAR) {
		this.SESSION_ID = SESSION_ID;
		this.SYS_ID = SYS_ID;
		this.USER_ID = USER_ID;
		this.ROLE_ID = ROLE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getSESSION_ID() {
		return this.SESSION_ID;
	}

	public void setSESSION_ID(String SESSION_ID) {
		this.SESSION_ID = SESSION_ID;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getUSER_IP() {
		return this.USER_IP;
	}

	public void setUSER_IP(String USER_IP) {
		this.USER_IP = USER_IP;
	}

	public String getLOGIN_DATE() {
		return this.LOGIN_DATE;
	}

	public void setLOGIN_DATE(String LOGIN_DATE) {
		this.LOGIN_DATE = LOGIN_DATE;
	}

	public String getLOGOUT_DATE() {
		return this.LOGOUT_DATE;
	}

	public void setLOGOUT_DATE(String LOGOUT_DATE) {
		this.LOGOUT_DATE = LOGOUT_DATE;
	}

	public String getSESSION_VALUE() {
		return this.SESSION_VALUE;
	}

	public void setSESSION_VALUE(String SESSION_VALUE) {
		this.SESSION_VALUE = SESSION_VALUE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SESSION_ID", getSESSION_ID())
				.toString();
	}

}
