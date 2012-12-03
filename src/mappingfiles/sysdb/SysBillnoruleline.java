package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBillnoruleline implements Serializable {

	/** identifier field */
	private String BILLNORULELINE_ID;

	/** persistent field */
	private String BILLNORULE_ID;

	/** nullable persistent field */
	private Integer LINE_NO;

	/** nullable persistent field */
	private Integer LINE_TYPE;

	/** nullable persistent field */
	private String LINE_FORMAT;

	/** nullable persistent field */
	private String INIT_VALUE;

	/** nullable persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private Integer LEVEL_NUM;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysBillnoruleline(String BILLNORULELINE_ID, String BILLNORULE_ID,
			Integer LINE_NO, Integer LINE_TYPE, String LINE_FORMAT,
			String INIT_VALUE, String ELE_CODE, Integer LEVEL_NUM,
			String LATEST_OP_DATE, String LATEST_OP_USER, int SET_YEAR,
			String LAST_VER) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.BILLNORULE_ID = BILLNORULE_ID;
		this.LINE_NO = LINE_NO;
		this.LINE_TYPE = LINE_TYPE;
		this.LINE_FORMAT = LINE_FORMAT;
		this.INIT_VALUE = INIT_VALUE;
		this.ELE_CODE = ELE_CODE;
		this.LEVEL_NUM = LEVEL_NUM;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysBillnoruleline() {
	}

	/** minimal constructor */
	public SysBillnoruleline(String BILLNORULELINE_ID, String BILLNORULE_ID,
			int SET_YEAR) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.BILLNORULE_ID = BILLNORULE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getBILLNORULELINE_ID() {
		return this.BILLNORULELINE_ID;
	}

	public void setBILLNORULELINE_ID(String BILLNORULELINE_ID) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
	}

	public String getBILLNORULE_ID() {
		return this.BILLNORULE_ID;
	}

	public void setBILLNORULE_ID(String BILLNORULE_ID) {
		this.BILLNORULE_ID = BILLNORULE_ID;
	}

	public Integer getLINE_NO() {
		return this.LINE_NO;
	}

	public void setLINE_NO(Integer LINE_NO) {
		this.LINE_NO = LINE_NO;
	}

	public Integer getLINE_TYPE() {
		return this.LINE_TYPE;
	}

	public void setLINE_TYPE(Integer LINE_TYPE) {
		this.LINE_TYPE = LINE_TYPE;
	}

	public String getLINE_FORMAT() {
		return this.LINE_FORMAT;
	}

	public void setLINE_FORMAT(String LINE_FORMAT) {
		this.LINE_FORMAT = LINE_FORMAT;
	}

	public String getINIT_VALUE() {
		return this.INIT_VALUE;
	}

	public void setINIT_VALUE(String INIT_VALUE) {
		this.INIT_VALUE = INIT_VALUE;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
	}

	public Integer getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(Integer LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
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

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("BILLNORULELINE_ID",
				getBILLNORULELINE_ID()).toString();
	}

}
