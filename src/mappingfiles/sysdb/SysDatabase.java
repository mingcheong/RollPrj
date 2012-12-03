package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysDatabase implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private Integer INT_DBTYPE;

	/** persistent field */
	private String SYS_ID;

	/** persistent field */
	private int IS_DSTORE;

	/** nullable persistent field */
	private String DB_NAME;

	/** persistent field */
	private String DB_USER;

	/** nullable persistent field */
	private String DB_PWD;

	/** nullable persistent field */
	private String DB_HOST;

	/** nullable persistent field */
	private String DB_PORT;

	/** nullable persistent field */
	private String DB_VERSION;

	/** persistent field */
	private int MAX_POOL_SIZE;

	/** persistent field */
	private String POOL_NAME;

	/** nullable persistent field */
	private String RG_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysDatabase(String CHR_ID, int SET_YEAR, Integer INT_DBTYPE,
			String SYS_ID, int IS_DSTORE, String DB_NAME, String DB_USER,
			String DB_PWD, String DB_HOST, String DB_PORT, String DB_VERSION,
			int MAX_POOL_SIZE, String POOL_NAME, String RG_CODE, String LAST_VER) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.INT_DBTYPE = INT_DBTYPE;
		this.SYS_ID = SYS_ID;
		this.IS_DSTORE = IS_DSTORE;
		this.DB_NAME = DB_NAME;
		this.DB_USER = DB_USER;
		this.DB_PWD = DB_PWD;
		this.DB_HOST = DB_HOST;
		this.DB_PORT = DB_PORT;
		this.DB_VERSION = DB_VERSION;
		this.MAX_POOL_SIZE = MAX_POOL_SIZE;
		this.POOL_NAME = POOL_NAME;
		this.RG_CODE = RG_CODE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysDatabase() {
	}

	/** minimal constructor */
	public SysDatabase(String CHR_ID, int SET_YEAR, String SYS_ID,
			int IS_DSTORE, String DB_USER, int MAX_POOL_SIZE, String POOL_NAME) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.SYS_ID = SYS_ID;
		this.IS_DSTORE = IS_DSTORE;
		this.DB_USER = DB_USER;
		this.MAX_POOL_SIZE = MAX_POOL_SIZE;
		this.POOL_NAME = POOL_NAME;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public Integer getINT_DBTYPE() {
		return this.INT_DBTYPE;
	}

	public void setINT_DBTYPE(Integer INT_DBTYPE) {
		this.INT_DBTYPE = INT_DBTYPE;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public int getIS_DSTORE() {
		return this.IS_DSTORE;
	}

	public void setIS_DSTORE(int IS_DSTORE) {
		this.IS_DSTORE = IS_DSTORE;
	}

	public String getDB_NAME() {
		return this.DB_NAME;
	}

	public void setDB_NAME(String DB_NAME) {
		this.DB_NAME = DB_NAME;
	}

	public String getDB_USER() {
		return this.DB_USER;
	}

	public void setDB_USER(String DB_USER) {
		this.DB_USER = DB_USER;
	}

	public String getDB_PWD() {
		return this.DB_PWD;
	}

	public void setDB_PWD(String DB_PWD) {
		this.DB_PWD = DB_PWD;
	}

	public String getDB_HOST() {
		return this.DB_HOST;
	}

	public void setDB_HOST(String DB_HOST) {
		this.DB_HOST = DB_HOST;
	}

	public String getDB_PORT() {
		return this.DB_PORT;
	}

	public void setDB_PORT(String DB_PORT) {
		this.DB_PORT = DB_PORT;
	}

	public String getDB_VERSION() {
		return this.DB_VERSION;
	}

	public void setDB_VERSION(String DB_VERSION) {
		this.DB_VERSION = DB_VERSION;
	}

	public int getMAX_POOL_SIZE() {
		return this.MAX_POOL_SIZE;
	}

	public void setMAX_POOL_SIZE(int MAX_POOL_SIZE) {
		this.MAX_POOL_SIZE = MAX_POOL_SIZE;
	}

	public String getPOOL_NAME() {
		return this.POOL_NAME;
	}

	public void setPOOL_NAME(String POOL_NAME) {
		this.POOL_NAME = POOL_NAME;
	}

	public String getRG_CODE() {
		return this.RG_CODE;
	}

	public void setRG_CODE(String RG_CODE) {
		this.RG_CODE = RG_CODE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("CHR_ID", getCHR_ID())
				.toString();
	}

}
