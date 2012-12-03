package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysTablemanager implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private String TABLE_CODE;

	/** nullable persistent field */
	private String TABLE_NAME;

	/** nullable persistent field */
	private Integer TABLE_TYPE;

	/** nullable persistent field */
	private String TABLE_DESC;

	/** nullable persistent field */
	private Integer IS_SYSTEM;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String ID_COLUMN_NAME;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String SYS_ID;

	/** full constructor */
	public SysTablemanager(String CHR_ID, String TABLE_CODE, String TABLE_NAME,
			Integer TABLE_TYPE, String TABLE_DESC, Integer IS_SYSTEM,
			int SET_YEAR, String CREATE_DATE, String CREATE_USER,
			String LATEST_OP_DATE, String LATEST_OP_USER,
			String ID_COLUMN_NAME, String LAST_VER, String SYS_ID) {
		this.CHR_ID = CHR_ID;
		this.TABLE_CODE = TABLE_CODE;
		this.TABLE_NAME = TABLE_NAME;
		this.TABLE_TYPE = TABLE_TYPE;
		this.TABLE_DESC = TABLE_DESC;
		this.IS_SYSTEM = IS_SYSTEM;
		this.SET_YEAR = SET_YEAR;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
		this.LAST_VER = LAST_VER;
		this.SYS_ID = SYS_ID;
	}

	/** default constructor */
	public SysTablemanager() {
	}

	/** minimal constructor */
	public SysTablemanager(String CHR_ID, String TABLE_CODE, int SET_YEAR) {
		this.CHR_ID = CHR_ID;
		this.TABLE_CODE = TABLE_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public String getTABLE_CODE() {
		return this.TABLE_CODE;
	}

	public void setTABLE_CODE(String TABLE_CODE) {
		this.TABLE_CODE = TABLE_CODE;
	}

	public String getTABLE_NAME() {
		return this.TABLE_NAME;
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME = TABLE_NAME;
	}

	public Integer getTABLE_TYPE() {
		return this.TABLE_TYPE;
	}

	public void setTABLE_TYPE(Integer TABLE_TYPE) {
		this.TABLE_TYPE = TABLE_TYPE;
	}

	public String getTABLE_DESC() {
		return this.TABLE_DESC;
	}

	public void setTABLE_DESC(String TABLE_DESC) {
		this.TABLE_DESC = TABLE_DESC;
	}

	public Integer getIS_SYSTEM() {
		return this.IS_SYSTEM;
	}

	public void setIS_SYSTEM(Integer IS_SYSTEM) {
		this.IS_SYSTEM = IS_SYSTEM;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
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

	public String getID_COLUMN_NAME() {
		return this.ID_COLUMN_NAME;
	}

	public void setID_COLUMN_NAME(String ID_COLUMN_NAME) {
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
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

	/**
	 * @return the sYS_ID
	 */
	public String getSYS_ID() {
		return SYS_ID;
	}

	/**
	 * @param sys_id
	 *            the sYS_ID to set
	 */
	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}
}
