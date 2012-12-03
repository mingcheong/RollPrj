package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysAction implements Serializable {

	/** identifier field */
	private String ACTION_ID;

	/** persistent field */
	private String ACTION_CODE;

	/** nullable persistent field */
	private String ACTION_NAME;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String FUNC_NAME;

	/** nullable persistent field */
	private String CLASS_NAME;

	/** nullable persistent field */
	private String PARAM;

	/** nullable persistent field */
	private String ACTION_TYPE;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String SYS_ID;

	/** full constructor */
	public SysAction(String ACTION_ID, String ACTION_CODE, String ACTION_NAME,
			int ENABLED, String FUNC_NAME, String CLASS_NAME, String PARAM,
			String ACTION_TYPE, String LAST_VER, String SYS_ID) {
		this.ACTION_ID = ACTION_ID;
		this.ACTION_CODE = ACTION_CODE;
		this.ACTION_NAME = ACTION_NAME;
		this.ENABLED = ENABLED;
		this.FUNC_NAME = FUNC_NAME;
		this.CLASS_NAME = CLASS_NAME;
		this.PARAM = PARAM;
		this.ACTION_TYPE = ACTION_TYPE;
		this.LAST_VER = LAST_VER;
		this.SYS_ID = SYS_ID;
	}

	/** default constructor */
	public SysAction() {
	}

	/** minimal constructor */
	public SysAction(String ACTION_ID, String ACTION_CODE, int ENABLED) {
		this.ACTION_ID = ACTION_ID;
		this.ACTION_CODE = ACTION_CODE;
		this.ENABLED = ENABLED;
	}

	public String getACTION_ID() {
		return this.ACTION_ID;
	}

	public void setACTION_ID(String ACTION_ID) {
		this.ACTION_ID = ACTION_ID;
	}

	public String getACTION_CODE() {
		return this.ACTION_CODE;
	}

	public void setACTION_CODE(String ACTION_CODE) {
		this.ACTION_CODE = ACTION_CODE;
	}

	public String getACTION_NAME() {
		return this.ACTION_NAME;
	}

	public void setACTION_NAME(String ACTION_NAME) {
		this.ACTION_NAME = ACTION_NAME;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getFUNC_NAME() {
		return this.FUNC_NAME;
	}

	public void setFUNC_NAME(String FUNC_NAME) {
		this.FUNC_NAME = FUNC_NAME;
	}

	public String getCLASS_NAME() {
		return this.CLASS_NAME;
	}

	public void setCLASS_NAME(String CLASS_NAME) {
		this.CLASS_NAME = CLASS_NAME;
	}

	public String getPARAM() {
		return this.PARAM;
	}

	public void setPARAM(String PARAM) {
		this.PARAM = PARAM;
	}

	public String getACTION_TYPE() {
		return this.ACTION_TYPE;
	}

	public void setACTION_TYPE(String ACTION_TYPE) {
		this.ACTION_TYPE = ACTION_TYPE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ACTION_ID", getACTION_ID())
				.toString();
	}

	/**
	 * @return the sYS_ID
	 */
	public String getSYS_ID() {
		return SYS_ID;
	}

	/**
	 * @param sys_id
	 *            the sYS_ID to set
	 */
	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

}
