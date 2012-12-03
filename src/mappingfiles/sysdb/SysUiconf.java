package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUiconf implements Serializable {

	/** identifier field */
	private String UICONF_TYPE;

	/** identifier field */
	private String UICONF_FIELD;

	/** nullable persistent field */
	private String UICONF_FIELD_NAME;

	/** nullable persistent field */
	private String UICONF_FIELD_TYPE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUiconf(String UICONF_TYPE, String UICONF_FIELD,
			String UICONF_FIELD_NAME, String UICONF_FIELD_TYPE, String LAST_VER) {
		this.UICONF_TYPE = UICONF_TYPE;
		this.UICONF_FIELD = UICONF_FIELD;
		this.UICONF_FIELD_NAME = UICONF_FIELD_NAME;
		this.UICONF_FIELD_TYPE = UICONF_FIELD_TYPE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysUiconf() {
	}

	/** minimal constructor */
	public SysUiconf(String UICONF_TYPE, String UICONF_FIELD) {
		this.UICONF_TYPE = UICONF_TYPE;
		this.UICONF_FIELD = UICONF_FIELD;
	}

	public String getUICONF_TYPE() {
		return this.UICONF_TYPE;
	}

	public void setUICONF_TYPE(String UICONF_TYPE) {
		this.UICONF_TYPE = UICONF_TYPE;
	}

	public String getUICONF_FIELD() {
		return this.UICONF_FIELD;
	}

	public void setUICONF_FIELD(String UICONF_FIELD) {
		this.UICONF_FIELD = UICONF_FIELD;
	}

	public String getUICONF_FIELD_NAME() {
		return this.UICONF_FIELD_NAME;
	}

	public void setUICONF_FIELD_NAME(String UICONF_FIELD_NAME) {
		this.UICONF_FIELD_NAME = UICONF_FIELD_NAME;
	}

	public String getUICONF_FIELD_TYPE() {
		return this.UICONF_FIELD_TYPE;
	}

	public void setUICONF_FIELD_TYPE(String UICONF_FIELD_TYPE) {
		this.UICONF_FIELD_TYPE = UICONF_FIELD_TYPE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("UICONF_TYPE", getUICONF_TYPE()).append("UICONF_FIELD",
						getUICONF_FIELD()).toString();
	}

}
