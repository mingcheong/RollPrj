package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUserpara implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private String CHR_CODE;

	/** nullable persistent field */
	private String CHR_NAME;

	/** nullable persistent field */
	private String CHR_VALUE;

	/** nullable persistent field */
	private String CHR_DESC;

	/** nullable persistent field */
	private String SYS_ID;

	/** persistent field */
	private int IS_VISIBLE;

	/** persistent field */
	private int IS_EDIT;

	/** nullable persistent field */
	private String FIELD_VALUESET;

	/** nullable persistent field */
	private Integer FIELD_DISPTYPE;

	/** nullable persistent field */
	private String GROUP_NAME;

	/** nullable persistent field */
	private String SET_ID;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysUserpara(String CHR_ID, String CHR_CODE, String CHR_NAME,
			String CHR_VALUE, String CHR_DESC, String SYS_ID, int IS_VISIBLE,
			int IS_EDIT, String FIELD_VALUESET, Integer FIELD_DISPTYPE,
			String GROUP_NAME, String SET_ID, String LAST_VER) {
		this.CHR_ID = CHR_ID;
		this.CHR_CODE = CHR_CODE;
		this.CHR_NAME = CHR_NAME;
		this.CHR_VALUE = CHR_VALUE;
		this.CHR_DESC = CHR_DESC;
		this.SYS_ID = SYS_ID;
		this.IS_VISIBLE = IS_VISIBLE;
		this.IS_EDIT = IS_EDIT;
		this.FIELD_VALUESET = FIELD_VALUESET;
		this.FIELD_DISPTYPE = FIELD_DISPTYPE;
		this.GROUP_NAME = GROUP_NAME;
		this.SET_ID = SET_ID;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysUserpara() {
	}

	/** minimal constructor */
	public SysUserpara(String CHR_ID, String CHR_CODE, int IS_VISIBLE,
			int IS_EDIT) {
		this.CHR_ID = CHR_ID;
		this.CHR_CODE = CHR_CODE;
		this.IS_VISIBLE = IS_VISIBLE;
		this.IS_EDIT = IS_EDIT;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public String getCHR_CODE() {
		return this.CHR_CODE;
	}

	public void setCHR_CODE(String CHR_CODE) {
		this.CHR_CODE = CHR_CODE;
	}

	public String getCHR_NAME() {
		return this.CHR_NAME;
	}

	public void setCHR_NAME(String CHR_NAME) {
		this.CHR_NAME = CHR_NAME;
	}

	public String getCHR_VALUE() {
		return this.CHR_VALUE;
	}

	public void setCHR_VALUE(String CHR_VALUE) {
		this.CHR_VALUE = CHR_VALUE;
	}

	public String getCHR_DESC() {
		return this.CHR_DESC;
	}

	public void setCHR_DESC(String CHR_DESC) {
		this.CHR_DESC = CHR_DESC;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public int getIS_VISIBLE() {
		return this.IS_VISIBLE;
	}

	public void setIS_VISIBLE(int IS_VISIBLE) {
		this.IS_VISIBLE = IS_VISIBLE;
	}

	public int getIS_EDIT() {
		return this.IS_EDIT;
	}

	public void setIS_EDIT(int IS_EDIT) {
		this.IS_EDIT = IS_EDIT;
	}

	public String getFIELD_VALUESET() {
		return this.FIELD_VALUESET;
	}

	public void setFIELD_VALUESET(String FIELD_VALUESET) {
		this.FIELD_VALUESET = FIELD_VALUESET;
	}

	public Integer getFIELD_DISPTYPE() {
		return this.FIELD_DISPTYPE;
	}

	public void setFIELD_DISPTYPE(Integer FIELD_DISPTYPE) {
		this.FIELD_DISPTYPE = FIELD_DISPTYPE;
	}

	public String getGROUP_NAME() {
		return this.GROUP_NAME;
	}

	public void setGROUP_NAME(String GROUP_NAME) {
		this.GROUP_NAME = GROUP_NAME;
	}

	public String getSET_ID() {
		return this.SET_ID;
	}

	public void setSET_ID(String SET_ID) {
		this.SET_ID = SET_ID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("CHR_ID", getCHR_ID())
				.toString();
	}

}
