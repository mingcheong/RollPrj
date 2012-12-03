package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUiconfDetail implements Serializable {

	/** identifier field */
	private String UICONF_ID;

	/** identifier field */
	private String UICONF_FIELD;

	/** identifier field */
	private String UICONF_VALUE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUiconfDetail(String UICONF_ID, String UICONF_FIELD,
			String UICONF_VALUE, String LAST_VER) {
		this.UICONF_ID = UICONF_ID;
		this.UICONF_FIELD = UICONF_FIELD;
		this.UICONF_VALUE = UICONF_VALUE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysUiconfDetail() {
	}

	/** minimal constructor */
	public SysUiconfDetail(String UICONF_ID, String UICONF_FIELD,
			String UICONF_VALUE) {
		this.UICONF_ID = UICONF_ID;
		this.UICONF_FIELD = UICONF_FIELD;
		this.UICONF_VALUE = UICONF_VALUE;
	}

	public String getUICONF_ID() {
		return this.UICONF_ID;
	}

	public void setUICONF_ID(String UICONF_ID) {
		this.UICONF_ID = UICONF_ID;
	}

	public String getUICONF_FIELD() {
		return this.UICONF_FIELD;
	}

	public void setUICONF_FIELD(String UICONF_FIELD) {
		this.UICONF_FIELD = UICONF_FIELD;
	}

	public String getUICONF_VALUE() {
		return this.UICONF_VALUE;
	}

	public void setUICONF_VALUE(String UICONF_VALUE) {
		this.UICONF_VALUE = UICONF_VALUE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("UICONF_ID", getUICONF_ID())
				.append("UICONF_FIELD", getUICONF_FIELD()).append(
						"UICONF_VALUE", getUICONF_VALUE()).toString();
	}

}
