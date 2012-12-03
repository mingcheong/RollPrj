package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysToolbar implements Serializable {

	/** identifier field */
	private String TOOLBAR_ID;

	/** persistent field */
	private String TOOLBAR_CODE;

	/** nullable persistent field */
	private String TOOLBAR_NAME;

	/** persistent field */
	private int TOOLBAR_TYPE;

	/** nullable persistent field */
	private String MENU_ID;

	/** nullable persistent field */
	private String TOOLBAR_ICON;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysToolbar(String TOOLBAR_ID, String TOOLBAR_CODE,
			String TOOLBAR_NAME, int TOOLBAR_TYPE, String MENU_ID,
			String TOOLBAR_ICON, String TIPS, String LAST_VER) {
		this.TOOLBAR_ID = TOOLBAR_ID;
		this.TOOLBAR_CODE = TOOLBAR_CODE;
		this.TOOLBAR_NAME = TOOLBAR_NAME;
		this.TOOLBAR_TYPE = TOOLBAR_TYPE;
		this.MENU_ID = MENU_ID;
		this.TOOLBAR_ICON = TOOLBAR_ICON;
		this.TIPS = TIPS;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysToolbar() {
	}

	/** minimal constructor */
	public SysToolbar(String TOOLBAR_ID, String TOOLBAR_CODE, int TOOLBAR_TYPE) {
		this.TOOLBAR_ID = TOOLBAR_ID;
		this.TOOLBAR_CODE = TOOLBAR_CODE;
		this.TOOLBAR_TYPE = TOOLBAR_TYPE;
	}

	public String getTOOLBAR_ID() {
		return this.TOOLBAR_ID;
	}

	public void setTOOLBAR_ID(String TOOLBAR_ID) {
		this.TOOLBAR_ID = TOOLBAR_ID;
	}

	public String getTOOLBAR_CODE() {
		return this.TOOLBAR_CODE;
	}

	public void setTOOLBAR_CODE(String TOOLBAR_CODE) {
		this.TOOLBAR_CODE = TOOLBAR_CODE;
	}

	public String getTOOLBAR_NAME() {
		return this.TOOLBAR_NAME;
	}

	public void setTOOLBAR_NAME(String TOOLBAR_NAME) {
		this.TOOLBAR_NAME = TOOLBAR_NAME;
	}

	public int getTOOLBAR_TYPE() {
		return this.TOOLBAR_TYPE;
	}

	public void setTOOLBAR_TYPE(int TOOLBAR_TYPE) {
		this.TOOLBAR_TYPE = TOOLBAR_TYPE;
	}

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String MENU_ID) {
		this.MENU_ID = MENU_ID;
	}

	public String getTOOLBAR_ICON() {
		return this.TOOLBAR_ICON;
	}

	public void setTOOLBAR_ICON(String TOOLBAR_ICON) {
		this.TOOLBAR_ICON = TOOLBAR_ICON;
	}

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("TOOLBAR_ID", getTOOLBAR_ID())
				.toString();
	}

}
