package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBillnoruleele implements Serializable {

	/** identifier field */
	private String BILLNORULELINE_ID;

	/** identifier field */
	private String ELE_CODE;

	/** nullable persistent field */
	private Integer LEVEL_NUM;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysBillnoruleele(String BILLNORULELINE_ID, String ELE_CODE,
			Integer LEVEL_NUM, int SET_YEAR, String LAST_VER) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.ELE_CODE = ELE_CODE;
		this.LEVEL_NUM = LEVEL_NUM;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysBillnoruleele() {
	}

	/** minimal constructor */
	public SysBillnoruleele(String BILLNORULELINE_ID, String ELE_CODE,
			int SET_YEAR) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
		this.ELE_CODE = ELE_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getBILLNORULELINE_ID() {
		return this.BILLNORULELINE_ID;
	}

	public void setBILLNORULELINE_ID(String BILLNORULELINE_ID) {
		this.BILLNORULELINE_ID = BILLNORULELINE_ID;
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
				getBILLNORULELINE_ID()).append("ELE_CODE", getELE_CODE())
				.toString();
	}

}
