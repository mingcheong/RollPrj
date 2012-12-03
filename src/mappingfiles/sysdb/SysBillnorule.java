package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBillnorule implements Serializable {

	/** identifier field */
	private String BILLNORULE_ID;

	/** persistent field */
	private String BILLNORULE_CODE;

	/** nullable persistent field */
	private String BILLNORULE_NAME;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String SYS_ID;

	/** full constructor */
	public SysBillnorule(String BILLNORULE_ID, String BILLNORULE_CODE,
			String BILLNORULE_NAME, String LATEST_OP_DATE,
			String LATEST_OP_USER, int SET_YEAR, String LAST_VER, String SYS_ID) {
		this.BILLNORULE_ID = BILLNORULE_ID;
		this.BILLNORULE_CODE = BILLNORULE_CODE;
		this.BILLNORULE_NAME = BILLNORULE_NAME;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
		this.SYS_ID = SYS_ID;
	}

	/** default constructor */
	public SysBillnorule() {
	}

	/** minimal constructor */
	public SysBillnorule(String BILLNORULE_ID, String BILLNORULE_CODE,
			int SET_YEAR) {
		this.BILLNORULE_ID = BILLNORULE_ID;
		this.BILLNORULE_CODE = BILLNORULE_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getBILLNORULE_ID() {
		return this.BILLNORULE_ID;
	}

	public void setBILLNORULE_ID(String BILLNORULE_ID) {
		this.BILLNORULE_ID = BILLNORULE_ID;
	}

	public String getBILLNORULE_CODE() {
		return this.BILLNORULE_CODE;
	}

	public void setBILLNORULE_CODE(String BILLNORULE_CODE) {
		this.BILLNORULE_CODE = BILLNORULE_CODE;
	}

	public String getBILLNORULE_NAME() {
		return this.BILLNORULE_NAME;
	}

	public void setBILLNORULE_NAME(String BILLNORULE_NAME) {
		this.BILLNORULE_NAME = BILLNORULE_NAME;
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
		return new ToStringBuilder(this).append("BILLNORULE_ID",
				getBILLNORULE_ID()).toString();
	}

	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

}
