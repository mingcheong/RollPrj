package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysOrgtype implements Serializable {

	/** identifier field */
	private String ORGTYPE_CODE;

	/** nullable persistent field */
	private String ORGTYPE_NAME;

	/** nullable persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysOrgtype(String ORGTYPE_CODE, String ORGTYPE_NAME,
			String ELE_CODE, String LAST_VER) {
		this.ORGTYPE_CODE = ORGTYPE_CODE;
		this.ORGTYPE_NAME = ORGTYPE_NAME;
		this.ELE_CODE = ELE_CODE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysOrgtype() {
	}

	/** minimal constructor */
	public SysOrgtype(String ORGTYPE_CODE) {
		this.ORGTYPE_CODE = ORGTYPE_CODE;
	}

	public String getORGTYPE_CODE() {
		return this.ORGTYPE_CODE;
	}

	public void setORGTYPE_CODE(String ORGTYPE_CODE) {
		this.ORGTYPE_CODE = ORGTYPE_CODE;
	}

	public String getORGTYPE_NAME() {
		return this.ORGTYPE_NAME;
	}

	public void setORGTYPE_NAME(String ORGTYPE_NAME) {
		this.ORGTYPE_NAME = ORGTYPE_NAME;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ORGTYPE_CODE",
				getORGTYPE_CODE()).toString();
	}

}
