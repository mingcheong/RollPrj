package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUimanager implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String UI_ID;

	/** persistent field */
	private String UI_CODE;

	/** nullable persistent field */
	private String UI_NAME;

	/** nullable persistent field */
	private String UI_TYPE;

	/** nullable persistent field */
	private String UI_SOURCE;

	/** nullable persistent field */
	private String REMARK;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private Integer COLUMNS;

	/** nullable persistent field */
	private String ID;

	/** nullable persistent field */
	private String TITLE;

	/** nullable persistent field */
	private String LAST_VER;

	/**
	 * nullable persistent field
	 */
	private String QUERY_RELATION_SIGN;

	private String SYS_ID;

	/** full constructor */
	public SysUimanager(String UI_ID, String UI_CODE, String UI_NAME,
			String UI_TYPE, String UI_SOURCE, String REMARK, int SET_YEAR,
			String CREATE_USER, String CREATE_DATE, String LATEST_OP_USER,
			String LATEST_OP_DATE, Integer COLUMNS, String ID, String TITLE,
			String LAST_VER, String QUERY_RELATION_SIGN, String SYS_ID) {
		this.UI_ID = UI_ID;
		this.UI_CODE = UI_CODE;
		this.UI_NAME = UI_NAME;
		this.UI_TYPE = UI_TYPE;
		this.UI_SOURCE = UI_SOURCE;
		this.REMARK = REMARK;
		this.SET_YEAR = SET_YEAR;
		this.CREATE_USER = CREATE_USER;
		this.CREATE_DATE = CREATE_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.COLUMNS = COLUMNS;
		this.ID = ID;
		this.TITLE = TITLE;
		this.LAST_VER = LAST_VER;
		this.QUERY_RELATION_SIGN = QUERY_RELATION_SIGN;
		this.SYS_ID = SYS_ID;
	}

	/** default constructor */
	public SysUimanager() {
	}

	/** minimal constructor */
	public SysUimanager(String UI_ID, String UI_CODE, int SET_YEAR) {
		this.UI_ID = UI_ID;
		this.UI_CODE = UI_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getUI_ID() {
		return this.UI_ID;
	}

	public void setUI_ID(String UI_ID) {
		this.UI_ID = UI_ID;
	}

	public String getUI_CODE() {
		return this.UI_CODE;
	}

	public void setUI_CODE(String UI_CODE) {
		this.UI_CODE = UI_CODE;
	}

	public String getUI_NAME() {
		return this.UI_NAME;
	}

	public void setUI_NAME(String UI_NAME) {
		this.UI_NAME = UI_NAME;
	}

	public String getUI_TYPE() {
		return this.UI_TYPE;
	}

	public void setUI_TYPE(String UI_TYPE) {
		this.UI_TYPE = UI_TYPE;
	}

	public String getUI_SOURCE() {
		return this.UI_SOURCE;
	}

	public void setUI_SOURCE(String UI_SOURCE) {
		this.UI_SOURCE = UI_SOURCE;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
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

	public Integer getCOLUMNS() {
		return this.COLUMNS;
	}

	public void setCOLUMNS(Integer COLUMNS) {
		this.COLUMNS = COLUMNS;
	}

	public String getID() {
		return this.ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getTITLE() {
		return this.TITLE;
	}

	public void setTITLE(String TITLE) {
		this.TITLE = TITLE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("UI_ID", getUI_ID()).toString();
	}

	public String getQUERY_RELATION_SIGN() {
		return QUERY_RELATION_SIGN;
	}

	public void setQUERY_RELATION_SIGN(String query_relation_sign) {
		QUERY_RELATION_SIGN = query_relation_sign;
	}

	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

}
