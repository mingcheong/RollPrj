package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusvouAcctmdl implements Serializable {

	/** identifier field */
	private String ACCTMDL_ID;

	/** nullable persistent field */
	private String VOU_TYPE_ID;

	/** nullable persistent field */
	private String ACCTMDL_CODE;

	/** nullable persistent field */
	private String ACCTMDL_NAME;

	/** nullable persistent field */
	private String ST_ID;

	/** nullable persistent field */
	private String DEFINE_ID;

	/** nullable persistent field */
	private String DS_ID;

	/** nullable persistent field */
	private String DS_SUMMARY;

	/** nullable persistent field */
	private String CS_ID;

	/** nullable persistent field */
	private String CS_SUMMARY;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlBusvouAcctmdl(String ACCTMDL_ID, String VOU_TYPE_ID,
			String ACCTMDL_CODE, String ACCTMDL_NAME, String ST_ID,
			String DEFINE_ID, String DS_ID, String DS_SUMMARY, String CS_ID,
			String CS_SUMMARY, String LATEST_OP_DATE, String LATEST_OP_USER,
			String LAST_VER, int SET_YEAR) {
		this.ACCTMDL_ID = ACCTMDL_ID;
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.ACCTMDL_CODE = ACCTMDL_CODE;
		this.ACCTMDL_NAME = ACCTMDL_NAME;
		this.ST_ID = ST_ID;
		this.DEFINE_ID = DEFINE_ID;
		this.DS_ID = DS_ID;
		this.DS_SUMMARY = DS_SUMMARY;
		this.CS_ID = CS_ID;
		this.CS_SUMMARY = CS_SUMMARY;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlBusvouAcctmdl() {
	}

	/** minimal constructor */
	public GlBusvouAcctmdl(String ACCTMDL_ID, int SET_YEAR) {
		this.ACCTMDL_ID = ACCTMDL_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getACCTMDL_ID() {
		return this.ACCTMDL_ID;
	}

	public void setACCTMDL_ID(String ACCTMDL_ID) {
		this.ACCTMDL_ID = ACCTMDL_ID;
	}

	public String getVOU_TYPE_ID() {
		return this.VOU_TYPE_ID;
	}

	public void setVOU_TYPE_ID(String VOU_TYPE_ID) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
	}

	public String getACCTMDL_CODE() {
		return this.ACCTMDL_CODE;
	}

	public void setACCTMDL_CODE(String ACCTMDL_CODE) {
		this.ACCTMDL_CODE = ACCTMDL_CODE;
	}

	public String getACCTMDL_NAME() {
		return this.ACCTMDL_NAME;
	}

	public void setACCTMDL_NAME(String ACCTMDL_NAME) {
		this.ACCTMDL_NAME = ACCTMDL_NAME;
	}

	public String getST_ID() {
		return this.ST_ID;
	}

	public void setST_ID(String ST_ID) {
		this.ST_ID = ST_ID;
	}

	public String getDEFINE_ID() {
		return this.DEFINE_ID;
	}

	public void setDEFINE_ID(String DEFINE_ID) {
		this.DEFINE_ID = DEFINE_ID;
	}

	public String getDS_ID() {
		return this.DS_ID;
	}

	public void setDS_ID(String DS_ID) {
		this.DS_ID = DS_ID;
	}

	public String getDS_SUMMARY() {
		return this.DS_SUMMARY;
	}

	public void setDS_SUMMARY(String DS_SUMMARY) {
		this.DS_SUMMARY = DS_SUMMARY;
	}

	public String getCS_ID() {
		return this.CS_ID;
	}

	public void setCS_ID(String CS_ID) {
		this.CS_ID = CS_ID;
	}

	public String getCS_SUMMARY() {
		return this.CS_SUMMARY;
	}

	public void setCS_SUMMARY(String CS_SUMMARY) {
		this.CS_SUMMARY = CS_SUMMARY;
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

	public String toString() {
		return new ToStringBuilder(this).append("ACCTMDL_ID", getACCTMDL_ID())
				.toString();
	}

}
