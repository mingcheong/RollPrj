package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRoleMenuModule implements Serializable {

	/** identifier field */
	private String ROLE_ID;

	/** identifier field */
	private String MENU_ID;

	/** identifier field */
	private String MODULE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysRoleMenuModule(String ROLE_ID, String MENU_ID, String MODULE_ID,
			int SET_YEAR, String LAST_VER) {
		this.ROLE_ID = ROLE_ID;
		this.MENU_ID = MENU_ID;
		this.MODULE_ID = MODULE_ID;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysRoleMenuModule() {
	}

	/** minimal constructor */
	public SysRoleMenuModule(String ROLE_ID, String MENU_ID, String MODULE_ID,
			int SET_YEAR) {
		this.ROLE_ID = ROLE_ID;
		this.MENU_ID = MENU_ID;
		this.MODULE_ID = MODULE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String MENU_ID) {
		this.MENU_ID = MENU_ID;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
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
		return new ToStringBuilder(this).append("ROLE_ID", getROLE_ID())
				.append("MENU_ID", getMENU_ID()).append("MODULE_ID",
						getMODULE_ID()).toString();
	}

}
