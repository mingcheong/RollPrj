package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysButton implements Serializable {

	/** identifier field */
	private String BUTTON_ID;

	/** persistent field */
	private String ACTION_ID;

	/** persistent field */
	private String MODULE_ID;

	/** persistent field */
	private int DISPLAY_ORDER;

	/** persistent field */
	private String DISPLAY_TITLE;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private String GROUP_NAME;

	/** nullable persistent field */
	private String BUTTON_ICON;

	/** nullable persistent field */
	private String GROUP_ICON;

	/** nullable persistent field */
	private String PARENT_BUTTON_ID;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysButton(String BUTTON_ID, String ACTION_ID, String MODULE_ID,
			int DISPLAY_ORDER, String DISPLAY_TITLE, String TIPS,
			String GROUP_NAME, String BUTTON_ICON, String GROUP_ICON,
			String PARENT_BUTTON_ID, String LAST_VER) {
		this.BUTTON_ID = BUTTON_ID;
		this.ACTION_ID = ACTION_ID;
		this.MODULE_ID = MODULE_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
		this.TIPS = TIPS;
		this.GROUP_NAME = GROUP_NAME;
		this.BUTTON_ICON = BUTTON_ICON;
		this.GROUP_ICON = GROUP_ICON;
		this.PARENT_BUTTON_ID = PARENT_BUTTON_ID;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysButton() {
	}

	/** minimal constructor */
	public SysButton(String BUTTON_ID, String ACTION_ID, String MODULE_ID,
			int DISPLAY_ORDER, String DISPLAY_TITLE) {
		this.BUTTON_ID = BUTTON_ID;
		this.ACTION_ID = ACTION_ID;
		this.MODULE_ID = MODULE_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
	}

	public String getBUTTON_ID() {
		return this.BUTTON_ID;
	}

	public void setBUTTON_ID(String BUTTON_ID) {
		this.BUTTON_ID = BUTTON_ID;
	}

	public String getACTION_ID() {
		return this.ACTION_ID;
	}

	public void setACTION_ID(String ACTION_ID) {
		this.ACTION_ID = ACTION_ID;
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

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public String getGROUP_NAME() {
		return this.GROUP_NAME;
	}

	public void setGROUP_NAME(String GROUP_NAME) {
		this.GROUP_NAME = GROUP_NAME;
	}

	public String getBUTTON_ICON() {
		return this.BUTTON_ICON;
	}

	public void setBUTTON_ICON(String BUTTON_ICON) {
		this.BUTTON_ICON = BUTTON_ICON;
	}

	public String getGROUP_ICON() {
		return this.GROUP_ICON;
	}

	public void setGROUP_ICON(String GROUP_ICON) {
		this.GROUP_ICON = GROUP_ICON;
	}

	public String getPARENT_BUTTON_ID() {
		return this.PARENT_BUTTON_ID;
	}

	public void setPARENT_BUTTON_ID(String PARENT_BUTTON_ID) {
		this.PARENT_BUTTON_ID = PARENT_BUTTON_ID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("BUTTON_ID", getBUTTON_ID())
				.toString();
	}

}
