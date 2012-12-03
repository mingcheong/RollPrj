package mappingfiles.sysdb;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class SysRole implements Serializable {

	/** identifier field */
	private String ROLE_ID;

	/** persistent field */
	private String ROLE_CODE;

	/** nullable persistent field */
	private String ROLE_NAME;

	/** nullable persistent field */
	private String USER_SYS_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private Integer ENABLED;

	/** nullable persistent field */
	private String ROLE_TYPE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysRole(String ROLE_ID, String ROLE_CODE, String ROLE_NAME,
			String USER_SYS_ID, int SET_YEAR, Integer ENABLED,
			String ROLE_TYPE, String LAST_VER) {
		this.ROLE_ID = ROLE_ID;
		this.ROLE_CODE = ROLE_CODE;
		this.ROLE_NAME = ROLE_NAME;
		this.USER_SYS_ID = USER_SYS_ID;
		this.SET_YEAR = SET_YEAR;
		this.ENABLED = ENABLED;
		this.ROLE_TYPE = ROLE_TYPE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysRole() {
	}

	/** minimal constructor */
	public SysRole(String ROLE_ID, String ROLE_CODE, int SET_YEAR) {
		this.ROLE_ID = ROLE_ID;
		this.ROLE_CODE = ROLE_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getROLE_CODE() {
		return this.ROLE_CODE;
	}

	public void setROLE_CODE(String ROLE_CODE) {
		this.ROLE_CODE = ROLE_CODE;
	}

	public String getROLE_NAME() {
		return this.ROLE_NAME;
	}

	public void setROLE_NAME(String ROLE_NAME) {
		this.ROLE_NAME = ROLE_NAME;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public Integer getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(Integer ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getROLE_TYPE() {
		return this.ROLE_TYPE;
	}

	public void setROLE_TYPE(String ROLE_TYPE) {
		this.ROLE_TYPE = ROLE_TYPE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return ROLE_NAME;
	}

	/**
	 * @return the uSER_SYS_ID
	 */
	public String getUSER_SYS_ID() {
		return USER_SYS_ID;
	}

	/**
	 * @param user_sys_id
	 *            the uSER_SYS_ID to set
	 */
	public void setUSER_SYS_ID(String user_sys_id) {
		USER_SYS_ID = user_sys_id;
	}

}
