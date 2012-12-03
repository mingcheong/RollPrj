package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysModule implements Serializable {

	/** identifier field */
	private String MODULE_ID;

	/** persistent field */
	private String MODULE_CODE;

	/** nullable persistent field */
	private String MODULE_NAME;

	/** nullable persistent field */
	private String SYS_ID;

	/** persistent field */
	private int MODULE_TYPE;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String DEFAULT_ICON;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private String CLASS_NAME;

	/** nullable persistent field */
	private String FUNC_NAME;

	/** nullable persistent field */
	private String PARAM;

	/** nullable persistent field */
	private String BILL_TYPE_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysModule(String MODULE_ID, String MODULE_CODE, String MODULE_NAME,
			String SYS_ID, int MODULE_TYPE, int ENABLED, String DEFAULT_ICON,
			String TIPS, String CLASS_NAME, String FUNC_NAME, String PARAM,
			String BILL_TYPE_CODE, String LAST_VER) {
		this.MODULE_ID = MODULE_ID;
		this.MODULE_CODE = MODULE_CODE;
		this.MODULE_NAME = MODULE_NAME;
		this.SYS_ID = SYS_ID;
		this.MODULE_TYPE = MODULE_TYPE;
		this.ENABLED = ENABLED;
		this.DEFAULT_ICON = DEFAULT_ICON;
		this.TIPS = TIPS;
		this.CLASS_NAME = CLASS_NAME;
		this.FUNC_NAME = FUNC_NAME;
		this.PARAM = PARAM;
		this.BILL_TYPE_CODE = BILL_TYPE_CODE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysModule() {
	}

	/** minimal constructor */
	public SysModule(String MODULE_ID, String MODULE_CODE, int MODULE_TYPE,
			int ENABLED) {
		this.MODULE_ID = MODULE_ID;
		this.MODULE_CODE = MODULE_CODE;
		this.MODULE_TYPE = MODULE_TYPE;
		this.ENABLED = ENABLED;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getMODULE_CODE() {
		return this.MODULE_CODE;
	}

	public void setMODULE_CODE(String MODULE_CODE) {
		this.MODULE_CODE = MODULE_CODE;
	}

	public String getMODULE_NAME() {
		return this.MODULE_NAME;
	}

	public void setMODULE_NAME(String MODULE_NAME) {
		this.MODULE_NAME = MODULE_NAME;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public int getMODULE_TYPE() {
		return this.MODULE_TYPE;
	}

	public void setMODULE_TYPE(int MODULE_TYPE) {
		this.MODULE_TYPE = MODULE_TYPE;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getDEFAULT_ICON() {
		return this.DEFAULT_ICON;
	}

	public void setDEFAULT_ICON(String DEFAULT_ICON) {
		this.DEFAULT_ICON = DEFAULT_ICON;
	}

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public String getCLASS_NAME() {
		return this.CLASS_NAME;
	}

	public void setCLASS_NAME(String CLASS_NAME) {
		this.CLASS_NAME = CLASS_NAME;
	}

	public String getFUNC_NAME() {
		return this.FUNC_NAME;
	}

	public void setFUNC_NAME(String FUNC_NAME) {
		this.FUNC_NAME = FUNC_NAME;
	}

	public String getPARAM() {
		return this.PARAM;
	}

	public void setPARAM(String PARAM) {
		this.PARAM = PARAM;
	}

	public String getBILL_TYPE_CODE() {
		return this.BILL_TYPE_CODE;
	}

	public void setBILL_TYPE_CODE(String BILL_TYPE_CODE) {
		this.BILL_TYPE_CODE = BILL_TYPE_CODE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MODULE_ID", getMODULE_ID())
				.toString();
	}

}
