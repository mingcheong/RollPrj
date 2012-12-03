package mappingfiles.sysdb;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class SysFieldmanager implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** nullable persistent field */
	private int SET_YEAR;

	/** persistent field */
	private String FIELD_CODE;

	/** persistent field */
	private String FIELD_NAME;

	/** nullable persistent field */
	private int FIELD_DISPTYPE;

	/** nullable persistent field */
	private String DEFAULT_VALUE;

	private String TIPS;

	private String FIELD_VALUESET;

	private String REMARK;

	private int IS_SYSTEM;

	private String CREATE_DATE;

	private String CREATE_USER;

	private String LATEST_OP_DATE;

	private String LATEST_OP_USER;

	private int IS_DELETED;

	private String SOURCE;

	private String LAST_VER;

	private String TABLE_CODE;

	/** full constructor */
	public SysFieldmanager(String CHR_ID, int SET_YEAR, String FIELD_CODE,
			int FIELD_DISPTYPE, String DEFAULT_VALUE, String TIPS,
			String FIELD_VALUESET, String REMARK, int IS_SYSTEM,
			String CREATE_DATE, String CREATE_USER, String LATEST_OP_DATE,
			String LATEST_OP_USER, int IS_DELETED, String SOURCE,
			String LAST_VER, String TABLE_CODE) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_CODE = FIELD_CODE;
		this.FIELD_DISPTYPE = FIELD_DISPTYPE;
		this.DEFAULT_VALUE = DEFAULT_VALUE;
		this.TIPS = TIPS;
		this.FIELD_VALUESET = FIELD_VALUESET;
		this.REMARK = REMARK;
		this.IS_SYSTEM = IS_SYSTEM;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.IS_DELETED = IS_DELETED;
		this.SOURCE = SOURCE;
		this.LAST_VER = LAST_VER;
		this.TABLE_CODE = TABLE_CODE;
	}

	/** default constructor */
	public SysFieldmanager() {
	}

	public String getCHR_ID() {
		return CHR_ID;
	}

	public void setCHR_ID(String chr_id) {
		CHR_ID = chr_id;
	}

	public String getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(String create_date) {
		CREATE_DATE = create_date;
	}

	public String getCREATE_USER() {
		return CREATE_USER;
	}

	public void setCREATE_USER(String create_user) {
		CREATE_USER = create_user;
	}

	public String getDEFAULT_VALUE() {
		return DEFAULT_VALUE;
	}

	public void setDEFAULT_VALUE(String default_value) {
		DEFAULT_VALUE = default_value;
	}

	public String getFIELD_CODE() {
		return FIELD_CODE;
	}

	public void setFIELD_CODE(String field_code) {
		FIELD_CODE = field_code;
	}

	public int getFIELD_DISPTYPE() {
		return FIELD_DISPTYPE;
	}

	public void setFIELD_DISPTYPE(int field_disptype) {
		FIELD_DISPTYPE = field_disptype;
	}

	public String getFIELD_NAME() {
		return FIELD_NAME;
	}

	public void setFIELD_NAME(String field_name) {
		FIELD_NAME = field_name;
	}

	public String getFIELD_VALUESET() {
		return FIELD_VALUESET;
	}

	public void setFIELD_VALUESET(String field_valueset) {
		FIELD_VALUESET = field_valueset;
	}

	public int getIS_DELETED() {
		return IS_DELETED;
	}

	public void setIS_DELETED(int is_deleted) {
		IS_DELETED = is_deleted;
	}

	public int getIS_SYSTEM() {
		return IS_SYSTEM;
	}

	public void setIS_SYSTEM(int is_system) {
		IS_SYSTEM = is_system;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public String getLATEST_OP_DATE() {
		return LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String latest_op_date) {
		LATEST_OP_DATE = latest_op_date;
	}

	public String getLATEST_OP_USER() {
		return LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String latest_op_user) {
		LATEST_OP_USER = latest_op_user;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String remark) {
		REMARK = remark;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int set_year) {
		SET_YEAR = set_year;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String source) {
		SOURCE = source;
	}

	public String getTABLE_CODE() {
		return TABLE_CODE;
	}

	public void setTABLE_CODE(String table_code) {
		TABLE_CODE = table_code;
	}

	public String getTIPS() {
		return TIPS;
	}

	public void setTIPS(String tips) {
		TIPS = tips;
	}

}
