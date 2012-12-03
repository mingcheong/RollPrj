package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysModuleToolbar implements Serializable {

	/** identifier field */
	private String TOOLBAR_ID;

	/** nullable persistent field */
	private String MODULE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public SysModuleToolbar(String TOOLBAR_ID, String MODULE_ID, int SET_YEAR) {
		this.TOOLBAR_ID = TOOLBAR_ID;
		this.MODULE_ID = MODULE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public SysModuleToolbar() {
	}

	/** minimal constructor */
	public SysModuleToolbar(String TOOLBAR_ID, int SET_YEAR) {
		this.TOOLBAR_ID = TOOLBAR_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getTOOLBAR_ID() {
		return this.TOOLBAR_ID;
	}

	public void setTOOLBAR_ID(String TOOLBAR_ID) {
		this.TOOLBAR_ID = TOOLBAR_ID;
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

	public String toString() {
		return new ToStringBuilder(this).append("TOOLBAR_ID", getTOOLBAR_ID())
				.toString();
	}

}
