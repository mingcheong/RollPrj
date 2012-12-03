package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysModuleStatus implements Serializable {

	/** identifier field */
	private String MODULE_ID;

	/** identifier field */
	private String STATUS_ID;

	/** persistent field */
	private Integer DISPLAY_ORDER;

	/** persistent field */
	private String DISPLAY_TITLE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysModuleStatus(String MODULE_ID, String STATUS_ID,
			Integer DISPLAY_ORDER, String DISPLAY_TITLE, String LAST_VER) {
		this.MODULE_ID = MODULE_ID;
		this.STATUS_ID = STATUS_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysModuleStatus() {
	}

	/** minimal constructor */
	public SysModuleStatus(String MODULE_ID, String STATUS_ID,
			Integer DISPLAY_ORDER, String DISPLAY_TITLE) {
		this.MODULE_ID = MODULE_ID;
		this.STATUS_ID = STATUS_ID;
		this.DISPLAY_ORDER = DISPLAY_ORDER;
		this.DISPLAY_TITLE = DISPLAY_TITLE;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getSTATUS_ID() {
		return this.STATUS_ID;
	}

	public void setSTATUS_ID(String STATUS_ID) {
		this.STATUS_ID = STATUS_ID;
	}

	public Integer getDISPLAY_ORDER() {
		return this.DISPLAY_ORDER;
	}

	public void setDISPLAY_ORDER(Integer DISPLAY_ORDER) {
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
		return new ToStringBuilder(this).append("MODULE_ID", getMODULE_ID())
				.append("STATUS_ID", getSTATUS_ID()).toString();
	}

}
