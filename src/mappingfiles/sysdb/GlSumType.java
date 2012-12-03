package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlSumType implements Serializable {

	/** identifier field */
	private String SUM_TYPE_ID;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private String COA_ID;

	/** nullable persistent field */
	private String SUM_TYPE_NAME;

	/** nullable persistent field */
	private Integer SUM_DATE_TYPE;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String SUM_TYPE_CODE;

	/** nullable persistent field */
	private String TABLE_NAME;

	/** full constructor */
	public GlSumType(String SUM_TYPE_ID, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			String COA_ID, String SUM_TYPE_NAME, Integer SUM_DATE_TYPE,
			int ENABLED, String LAST_VER, int SET_YEAR, String SUM_TYPE_CODE,
			String TABLE_NAME) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.COA_ID = COA_ID;
		this.SUM_TYPE_NAME = SUM_TYPE_NAME;
		this.SUM_DATE_TYPE = SUM_DATE_TYPE;
		this.ENABLED = ENABLED;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
		this.SUM_TYPE_CODE = SUM_TYPE_CODE;
		this.TABLE_NAME = TABLE_NAME;
	}

	/** default constructor */
	public GlSumType() {
	}

	/** minimal constructor */
	public GlSumType(String SUM_TYPE_ID, String LATEST_OP_DATE, String COA_ID,
			int ENABLED, int SET_YEAR) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.COA_ID = COA_ID;
		this.ENABLED = ENABLED;
		this.SET_YEAR = SET_YEAR;
	}

	public String getSUM_TYPE_ID() {
		return this.SUM_TYPE_ID;
	}

	public void setSUM_TYPE_ID(String SUM_TYPE_ID) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
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

	public String getCOA_ID() {
		return this.COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
	}

	public String getSUM_TYPE_NAME() {
		return this.SUM_TYPE_NAME;
	}

	public void setSUM_TYPE_NAME(String SUM_TYPE_NAME) {
		this.SUM_TYPE_NAME = SUM_TYPE_NAME;
	}

	public Integer getSUM_DATE_TYPE() {
		return this.SUM_DATE_TYPE;
	}

	public void setSUM_DATE_TYPE(Integer SUM_DATE_TYPE) {
		this.SUM_DATE_TYPE = SUM_DATE_TYPE;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
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

	public String getSUM_TYPE_CODE() {
		return this.SUM_TYPE_CODE;
	}

	public void setSUM_TYPE_CODE(String SUM_TYPE_CODE) {
		this.SUM_TYPE_CODE = SUM_TYPE_CODE;
	}

	public String getTABLE_NAME() {
		return this.TABLE_NAME;
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME = TABLE_NAME;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("SUM_TYPE_ID", getSUM_TYPE_ID()).toString();
	}

}
