package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusvouType implements Serializable {

	/** identifier field */
	private String VOU_TYPE_ID;

	/** persistent field */
	private String VOU_TYPE_CODE;

	/** nullable persistent field */
	private String VOU_TYPE_NAME;

	/** nullable persistent field */
	private String COA_ID;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private Integer IS_MANUAL;

	/** nullable persistent field */
	private String DATASOURCE_SETTING;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String DS_ID;

	/** nullable persistent field */
	private String CS_ID;

	/** nullable persistent field */
	private String ST_ID;

	/** full constructor */
	public GlBusvouType(String VOU_TYPE_ID, String VOU_TYPE_CODE,
			String VOU_TYPE_NAME, String COA_ID, String LATEST_OP_DATE,
			String LATEST_OP_USER, Integer IS_MANUAL,
			String DATASOURCE_SETTING, String LAST_VER, int SET_YEAR,
			String DS_ID, String CS_ID, String ST_ID) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.VOU_TYPE_CODE = VOU_TYPE_CODE;
		this.VOU_TYPE_NAME = VOU_TYPE_NAME;
		this.COA_ID = COA_ID;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.IS_MANUAL = IS_MANUAL;
		this.DATASOURCE_SETTING = DATASOURCE_SETTING;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
		this.DS_ID = DS_ID;
		this.CS_ID = CS_ID;
		this.ST_ID = ST_ID;
	}

	/** default constructor */
	public GlBusvouType() {
	}

	/** minimal constructor */
	public GlBusvouType(String VOU_TYPE_ID, String VOU_TYPE_CODE,
			String LATEST_OP_DATE, int SET_YEAR) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.VOU_TYPE_CODE = VOU_TYPE_CODE;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getVOU_TYPE_ID() {
		return this.VOU_TYPE_ID;
	}

	public void setVOU_TYPE_ID(String VOU_TYPE_ID) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
	}

	public String getVOU_TYPE_CODE() {
		return this.VOU_TYPE_CODE;
	}

	public void setVOU_TYPE_CODE(String VOU_TYPE_CODE) {
		this.VOU_TYPE_CODE = VOU_TYPE_CODE;
	}

	public String getVOU_TYPE_NAME() {
		return this.VOU_TYPE_NAME;
	}

	public void setVOU_TYPE_NAME(String VOU_TYPE_NAME) {
		this.VOU_TYPE_NAME = VOU_TYPE_NAME;
	}

	public String getCOA_ID() {
		return this.COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
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

	public Integer getIS_MANUAL() {
		return this.IS_MANUAL;
	}

	public void setIS_MANUAL(Integer IS_MANUAL) {
		this.IS_MANUAL = IS_MANUAL;
	}

	public Object getDATASOURCE_SETTING() {
		return this.DATASOURCE_SETTING;
	}

	public void setDATASOURCE_SETTING(String DATASOURCE_SETTING) {
		this.DATASOURCE_SETTING = DATASOURCE_SETTING;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getDS_ID() {
		return this.DS_ID;
	}

	public void setDS_ID(String DS_ID) {
		this.DS_ID = DS_ID;
	}

	public String getCS_ID() {
		return this.CS_ID;
	}

	public void setCS_ID(String CS_ID) {
		this.CS_ID = CS_ID;
	}

	public String getST_ID() {
		return this.ST_ID;
	}

	public void setST_ID(String ST_ID) {
		this.ST_ID = ST_ID;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("VOU_TYPE_ID", getVOU_TYPE_ID()).toString();
	}

}
