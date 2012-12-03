package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlCoa implements Serializable {

	/** identifier field */
	private String COA_ID;

	/** nullable persistent field */
	private String COA_CODE;

	/** nullable persistent field */
	private String COA_NAME;

	/** nullable persistent field */
	private String COA_DESC;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlCoa(String COA_ID, String COA_CODE, String COA_NAME,
			String COA_DESC, int ENABLED, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			String LAST_VER, int SET_YEAR) {
		this.COA_ID = COA_ID;
		this.COA_CODE = COA_CODE;
		this.COA_NAME = COA_NAME;
		this.COA_DESC = COA_DESC;
		this.ENABLED = ENABLED;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlCoa() {
	}

	/** minimal constructor */
	public GlCoa(String COA_ID, int ENABLED, String LATEST_OP_DATE, int SET_YEAR) {
		this.COA_ID = COA_ID;
		this.ENABLED = ENABLED;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getCOA_ID() {
		return this.COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
	}

	public String getCOA_CODE() {
		return this.COA_CODE;
	}

	public void setCOA_CODE(String COA_CODE) {
		this.COA_CODE = COA_CODE;
	}

	public String getCOA_NAME() {
		return this.COA_NAME;
	}

	public void setCOA_NAME(String COA_NAME) {
		this.COA_NAME = COA_NAME;
	}

	public String getCOA_DESC() {
		return this.COA_DESC;
	}

	public void setCOA_DESC(String COA_DESC) {
		this.COA_DESC = COA_DESC;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
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
		return new ToStringBuilder(this).append("COA_ID", getCOA_ID())
				.toString();
	}

}
