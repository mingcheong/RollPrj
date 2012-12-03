package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBilltypeAssele implements Serializable {

	/** identifier field */
	private String ASSELE_ID;

	/** persistent field */
	private String BILLTYPE_ID;

	/** nullable persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private Integer LEVEL_NUM;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysBilltypeAssele(String ASSELE_ID, String BILLTYPE_ID,
			String ELE_CODE, String LATEST_OP_USER, String LATEST_OP_DATE,
			int SET_YEAR, Integer LEVEL_NUM, String LAST_VER) {
		this.ASSELE_ID = ASSELE_ID;
		this.BILLTYPE_ID = BILLTYPE_ID;
		this.ELE_CODE = ELE_CODE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SET_YEAR = SET_YEAR;
		this.LEVEL_NUM = LEVEL_NUM;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysBilltypeAssele() {
	}

	/** minimal constructor */
	public SysBilltypeAssele(String ASSELE_ID, String BILLTYPE_ID, int SET_YEAR) {
		this.ASSELE_ID = ASSELE_ID;
		this.BILLTYPE_ID = BILLTYPE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getASSELE_ID() {
		return this.ASSELE_ID;
	}

	public void setASSELE_ID(String ASSELE_ID) {
		this.ASSELE_ID = ASSELE_ID;
	}

	public String getBILLTYPE_ID() {
		return this.BILLTYPE_ID;
	}

	public void setBILLTYPE_ID(String BILLTYPE_ID) {
		this.BILLTYPE_ID = BILLTYPE_ID;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
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

	public Integer getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(Integer LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("ASSELE_ID", getASSELE_ID())
				.toString();
	}

}
