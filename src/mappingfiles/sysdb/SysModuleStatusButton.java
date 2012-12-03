package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysModuleStatusButton implements Serializable {

	/** identifier field */
	private String MODULE_ID;

	/** identifier field */
	private String STATUS_ID;

	/** identifier field */
	private String BUTTON_ID;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysModuleStatusButton(String MODULE_ID, String STATUS_ID,
			String BUTTON_ID, String LAST_VER) {
		this.MODULE_ID = MODULE_ID;
		this.STATUS_ID = STATUS_ID;
		this.BUTTON_ID = BUTTON_ID;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysModuleStatusButton() {
	}

	/** minimal constructor */
	public SysModuleStatusButton(String MODULE_ID, String STATUS_ID,
			String BUTTON_ID) {
		this.MODULE_ID = MODULE_ID;
		this.STATUS_ID = STATUS_ID;
		this.BUTTON_ID = BUTTON_ID;
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

	public String getBUTTON_ID() {
		return this.BUTTON_ID;
	}

	public void setBUTTON_ID(String BUTTON_ID) {
		this.BUTTON_ID = BUTTON_ID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MODULE_ID", getMODULE_ID())
				.append("STATUS_ID", getSTATUS_ID()).append("BUTTON_ID",
						getBUTTON_ID()).toString();
	}

}
