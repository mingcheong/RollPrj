package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysEnumerate implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private String FIELD_CODE;

	/** nullable persistent field */
	private String ENU_CODE;

	/** nullable persistent field */
	private String ENU_NAME;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private Integer IS_DELETED;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysEnumerate(String CHR_ID, int SET_YEAR, String FIELD_CODE,
			String ENU_CODE, String ENU_NAME, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			Integer IS_DELETED, String LAST_VER) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_CODE = FIELD_CODE;
		this.ENU_CODE = ENU_CODE;
		this.ENU_NAME = ENU_NAME;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.IS_DELETED = IS_DELETED;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysEnumerate() {
	}

	/** minimal constructor */
	public SysEnumerate(String CHR_ID, int SET_YEAR, String FIELD_CODE) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_CODE = FIELD_CODE;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getFIELD_CODE() {
		return this.FIELD_CODE;
	}

	public void setFIELD_CODE(String FIELD_CODE) {
		this.FIELD_CODE = FIELD_CODE;
	}

	public String getENU_CODE() {
		return this.ENU_CODE;
	}

	public void setENU_CODE(String ENU_CODE) {
		this.ENU_CODE = ENU_CODE;
	}

	public String getENU_NAME() {
		return this.ENU_NAME;
	}

	public void setENU_NAME(String ENU_NAME) {
		this.ENU_NAME = ENU_NAME;
	}

	public String getCREATE_DATE() {
		return this.CREATE_DATE;
	}

	public void setCREATE_DATE(String CREATE_DATE) {
		this.CREATE_DATE = CREATE_DATE;
	}

	public String getCREATE_USER() {
		return this.CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public String getLATEST_OP_DATE() {
		return this.LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String LATEST_OP_DATE) {
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public String getLATEST_OP_USER() {
		return this.LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String LATEST_OP_USER) {
		this.LATEST_OP_USER = LATEST_OP_USER;
	}

	public Integer getIS_DELETED() {
		return this.IS_DELETED;
	}

	public void setIS_DELETED(Integer IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
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
