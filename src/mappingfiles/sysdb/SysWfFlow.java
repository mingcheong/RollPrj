package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3788784016730079571L;

	/** identifier field */
	private String WF_ID;

	/** persistent field */
	private String WF_CODE;

	/** nullable persistent field */
	private String WF_NAME;

	/** persistent field */
	private String WF_TABLE_NAME;

	/** persistent field */
	private String ID_COLUMN_NAME;

	/** nullable persistent field */
	private String CONDITION_ID;

	/** nullable persistent field */
	private String REMARK;

	/** nullable persistent field */
	private Integer ENABLED;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** persistent field */
	private int SET_YEAR;

	private String COA_ID;
	private String RIGHT_CCID;
	private String RIGHT_RCID;

	/** full constructor */
	public SysWfFlow(String WF_ID, String WF_CODE, String WF_NAME,
			String WF_TABLE_NAME, String ID_COLUMN_NAME, String CONDITION_ID,
			String REMARK, Integer ENABLED, String CREATE_USER,
			String CREATE_DATE, String LATEST_OP_USER, String LATEST_OP_DATE,
			int SET_YEAR, String COA_ID, String RIGHT_CCID, String RIGHT_RCID) {
		this.WF_ID = WF_ID;
		this.WF_CODE = WF_CODE;
		this.WF_NAME = WF_NAME;
		this.WF_TABLE_NAME = WF_TABLE_NAME;
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
		this.CONDITION_ID = CONDITION_ID;
		this.REMARK = REMARK;
		this.ENABLED = ENABLED;
		this.CREATE_USER = CREATE_USER;
		this.CREATE_DATE = CREATE_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SET_YEAR = SET_YEAR;
		this.COA_ID = COA_ID;
		this.RIGHT_CCID = RIGHT_CCID;
		this.RIGHT_RCID = RIGHT_RCID;
	}

	/** default constructor */
	public SysWfFlow() {
	}

	/** minimal constructor */
	public SysWfFlow(String WF_ID, String WF_CODE, String WF_TABLE_NAME,
			String ID_COLUMN_NAME, int SET_YEAR, String COA_ID,
			String RIGHT_CCID, String RIGHT_RCID) {
		this.WF_ID = WF_ID;
		this.WF_CODE = WF_CODE;
		this.WF_TABLE_NAME = WF_TABLE_NAME;
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
		this.SET_YEAR = SET_YEAR;
		this.COA_ID = COA_ID;
		this.RIGHT_CCID = RIGHT_CCID;
		this.RIGHT_RCID = RIGHT_RCID;
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getWF_CODE() {
		return this.WF_CODE;
	}

	public void setWF_CODE(String WF_CODE) {
		this.WF_CODE = WF_CODE;
	}

	public String getWF_NAME() {
		return this.WF_NAME;
	}

	public void setWF_NAME(String WF_NAME) {
		this.WF_NAME = WF_NAME;
	}

	public String getWF_TABLE_NAME() {
		return this.WF_TABLE_NAME;
	}

	public void setWF_TABLE_NAME(String WF_TABLE_NAME) {
		this.WF_TABLE_NAME = WF_TABLE_NAME;
	}

	public String getID_COLUMN_NAME() {
		return this.ID_COLUMN_NAME;
	}

	public void setID_COLUMN_NAME(String ID_COLUMN_NAME) {
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
	}

	public String getCONDITION_ID() {
		return this.CONDITION_ID;
	}

	public void setCONDITION_ID(String CONDITION_ID) {
		this.CONDITION_ID = CONDITION_ID;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public Integer getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(Integer ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getCREATE_USER() {
		return this.CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public String getCREATE_DATE() {
		return this.CREATE_DATE;
	}

	public void setCREATE_DATE(String CREATE_DATE) {
		this.CREATE_DATE = CREATE_DATE;
	}

	public String getLATEST_OP_USER() {
		return this.LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String LATEST_OP_USER) {
		this.LATEST_OP_USER = LATEST_OP_USER;
	}

	public String getLATEST_OP_DATE() {
		return this.LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String LATEST_OP_DATE) {
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).toString();
	}

	public String getCOA_ID() {
		return COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
	}

	public String getRIGHT_CCID() {
		return RIGHT_CCID;
	}

	public void setRIGHT_CCID(String RIGHT_CCID) {
		this.RIGHT_CCID = RIGHT_CCID;
	}

	public String getRIGHT_RCID() {
		return RIGHT_RCID;
	}

	public void setRIGHT_RCID(String RIGHT_RCID) {
		this.RIGHT_RCID = RIGHT_RCID;
	}

}
