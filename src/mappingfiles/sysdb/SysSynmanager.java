package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysSynmanager implements Serializable {

	/** identifier field */
	private String GUID;

	/** persistent field */
	private int TABLE_TYPE;

	/** nullable persistent field */
	private String TABLE_NAME;

	/** nullable persistent field */
	private String SYN_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String SYN_TIME;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysSynmanager(String GUID, int TABLE_TYPE, String TABLE_NAME,
			String SYN_USER, String LATEST_OP_DATE, String SYN_TIME,
			int SET_YEAR, String LAST_VER) {
		this.GUID = GUID;
		this.TABLE_TYPE = TABLE_TYPE;
		this.TABLE_NAME = TABLE_NAME;
		this.SYN_USER = SYN_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SYN_TIME = SYN_TIME;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysSynmanager() {
	}

	/** minimal constructor */
	public SysSynmanager(String GUID, int TABLE_TYPE, int SET_YEAR) {
		this.GUID = GUID;
		this.TABLE_TYPE = TABLE_TYPE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getGUID() {
		return this.GUID;
	}

	public void setGUID(String GUID) {
		this.GUID = GUID;
	}

	public int getTABLE_TYPE() {
		return this.TABLE_TYPE;
	}

	public void setTABLE_TYPE(int TABLE_TYPE) {
		this.TABLE_TYPE = TABLE_TYPE;
	}

	public String getTABLE_NAME() {
		return this.TABLE_NAME;
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME = TABLE_NAME;
	}

	public String getSYN_USER() {
		return this.SYN_USER;
	}

	public void setSYN_USER(String SYN_USER) {
		this.SYN_USER = SYN_USER;
	}

	public String getLATEST_OP_DATE() {
		return this.LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String LATEST_OP_DATE) {
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public String getSYN_TIME() {
		return this.SYN_TIME;
	}

	public void setSYN_TIME(String SYN_TIME) {
		this.SYN_TIME = SYN_TIME;
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
		return new ToStringBuilder(this).append("GUID", getGUID()).toString();
	}

}
