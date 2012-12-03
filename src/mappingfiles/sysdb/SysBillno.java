package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBillno implements Serializable {

	/** identifier field */
	private String BILLNORULELINE_ID;

	/** identifier field */
	private String ELE_VALUE;

	/** persistent field */
	private int MAX_NO;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysBillno(String BILLNORULELINE_ID, String ELE_VALUE, int MAX_NO,
			String LATEST_OP_DATE, String LATEST_OP_USER, int SET_YEAR,
			String LAST_VER) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.ELE_VALUE = ELE_VALUE;
		this.MAX_NO = MAX_NO;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysBillno() {
	}

	/** minimal constructor */
	public SysBillno(String BILLNORULELINE_ID, String ELE_VALUE, int MAX_NO,
			int SET_YEAR) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.ELE_VALUE = ELE_VALUE;
		this.MAX_NO = MAX_NO;
		this.SET_YEAR = SET_YEAR;
	}

	public String getBILLNORULELINE_ID() {
		return this.BILLNORULELINE_ID;
	}

	public void setBILLNORULELINE_ID(String BILLNORULELINE_ID) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
	}

	public String getELE_VALUE() {
		return this.ELE_VALUE;
	}

	public void setELE_VALUE(String ELE_VALUE) {
		this.ELE_VALUE = ELE_VALUE;
	}

	public int getMAX_NO() {
		return this.MAX_NO;
	}

	public void setMAX_NO(int MAX_NO) {
		this.MAX_NO = MAX_NO;
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
				getBILLNORULELINE_ID()).append("ELE_VALUE", getELE_VALUE())
				.toString();
	}

}
