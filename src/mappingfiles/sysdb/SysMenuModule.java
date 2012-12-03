package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysMenuModule implements Serializable {

	/** identifier field */
	private String MENU_ID;

	/** identifier field */
	private String MODULE_ID;

	/** persistent field */
	private int DISPLAY_ORDER;

	/** persistent field */
	private String DISPLAY_TITLE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysMenuModule(String MENU_ID, String MODULE_ID, int DISPLAY_ORDER,
			String DISPLAY_TITLE, String LAST_VER) {
		this.MENU_ID = MENU_ID;
		this.MODULE_ID = MODULE_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysMenuModule() {
	}

	/** minimal constructor */
	public SysMenuModule(String MENU_ID, String MODULE_ID, int DISPLAY_ORDER,
			String DISPLAY_TITLE) {
		this.MENU_ID = MENU_ID;
		this.MODULE_ID = MODULE_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
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

	public int getDISPLAY_ORDER() {
		return this.DISPLAY_ORDER;
	}

	public void setDISPLAY_ORDER(int DISPLAY_ORDER) {
		this.DISPLAY_ORDER = DISPLAY_ORDER;
	}

	public String getDISPLAY_TITLE() {
		return this.DISPLAY_TITLE;
	}

	public void setDISPLAY_TITLE(String DISPLAY_TITLE) {
		this.DISPLAY_TITLE = DISPLAY_TITLE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MENU_ID", getMENU_ID())
				.append("MODULE_ID", getMODULE_ID()).toString();
	}

}
