package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUidetail implements Serializable {

	/** identifier field */
	private String UI_DETAIL_ID;

	/** persistent field */
	private String UI_ID;

	/** persistent field */
	private String FIELD_NAME;

	/** nullable persistent field */
	private String DISP_MODE;

	/** persistent field */
	private int IS_NESSARY;

	/** persistent field */
	private int IS_ENABLED;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private long FIELD_INDEX;

	/** nullable persistent field */
	private String ID;

	/** nullable persistent field */
	private String TITLE;

	/** nullable persistent field */
	private Integer COLS;

	/** nullable persistent field */
	private String VISIBLE;

	private String ENABLED;

	/** nullable persistent field */
	private String EDITABLE;

	/** nullable persistent field */
	private String VALUE;

	/** nullable persistent field */
	private String REF_MODEL;

	/** nullable persistent field */
	private String SOURCE;

	/** nullable persistent field */
	private Integer WIDTH;

	/** nullable persistent field */
	private String IS_MUST_INPUT;

	/** nullable persistent field */
	private String LAST_VER;

	/**
	 * nullable persistent field
	 */
	private String QUERY_RELATION_SIGN;

	private Integer DETAIL_TYPE;

	private String PARENT_ID;

	/** full constructor */
	public SysUidetail(String UI_DETAIL_ID, String UI_ID, String FIELD_NAME,
			String DISP_MODE, int IS_NESSARY, int IS_ENABLED, int SET_YEAR,
			long FIELD_INDEX, String ID, String TITLE, Integer COLS,
			String VISIBLE, String ENABLED, String EDITABLE, String VALUE,
			String REF_MODEL, String SOURCE, Integer WIDTH,
			String IS_MUST_INPUT, String LAST_VER, String QUERY_RELATION_SIGN,
			Integer DETAIL_TYPE, String PARENT_ID) {
		this.UI_DETAIL_ID = UI_DETAIL_ID;
		this.UI_ID = UI_ID;
		this.FIELD_NAME = FIELD_NAME;
		this.DISP_MODE = DISP_MODE;
		this.IS_NESSARY = IS_NESSARY;
		this.IS_ENABLED = IS_ENABLED;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_INDEX = FIELD_INDEX;
		this.ID = ID;
		this.TITLE = TITLE;
		this.COLS = COLS;
		this.VISIBLE = VISIBLE;
		this.EDITABLE = EDITABLE;
		this.ENABLED = ENABLED;
		this.VALUE = VALUE;
		this.REF_MODEL = REF_MODEL;
		this.SOURCE = SOURCE;
		this.WIDTH = WIDTH;
		this.IS_MUST_INPUT = IS_MUST_INPUT;
		this.LAST_VER = LAST_VER;
		this.QUERY_RELATION_SIGN = QUERY_RELATION_SIGN;
		this.DETAIL_TYPE = DETAIL_TYPE;
		this.PARENT_ID = PARENT_ID;
	}

	/** default constructor */
	public SysUidetail() {
	}

	/** minimal constructor */
	public SysUidetail(String UI_DETAIL_ID, String UI_ID, String FIELD_NAME,
			int IS_NESSARY, int IS_ENABLED, int SET_YEAR, long FIELD_INDEX) {
		this.UI_DETAIL_ID = UI_DETAIL_ID;
		this.UI_ID = UI_ID;
		this.FIELD_NAME = FIELD_NAME;
		this.IS_NESSARY = IS_NESSARY;
		this.IS_ENABLED = IS_ENABLED;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_INDEX = FIELD_INDEX;
	}

	public String getUI_DETAIL_ID() {
		return this.UI_DETAIL_ID;
	}

	public void setUI_DETAIL_ID(String UI_DETAIL_ID) {
		this.UI_DETAIL_ID = UI_DETAIL_ID;
	}

	public String getUI_ID() {
		return this.UI_ID;
	}

	public void setUI_ID(String UI_ID) {
		this.UI_ID = UI_ID;
	}

	public String getFIELD_NAME() {
		return this.FIELD_NAME;
	}

	public void setFIELD_NAME(String FIELD_NAME) {
		this.FIELD_NAME = FIELD_NAME;
	}

	public String getDISP_MODE() {
		return this.DISP_MODE;
	}

	public void setDISP_MODE(String DISP_MODE) {
		this.DISP_MODE = DISP_MODE;
	}

	public int getIS_NESSARY() {
		return this.IS_NESSARY;
	}

	public void setIS_NESSARY(int IS_NESSARY) {
		this.IS_NESSARY = IS_NESSARY;
	}

	public int getIS_ENABLED() {
		return this.IS_ENABLED;
	}

	public void setIS_ENABLED(int IS_ENABLED) {
		this.IS_ENABLED = IS_ENABLED;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public long getFIELD_INDEX() {
		return this.FIELD_INDEX;
	}

	public void setFIELD_INDEX(long FIELD_INDEX) {
		this.FIELD_INDEX = FIELD_INDEX;
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

	public Integer getCOLS() {
		return this.COLS;
	}

	public void setCOLS(Integer COLS) {
		this.COLS = COLS;
	}

	public String getVISIBLE() {
		return this.VISIBLE;
	}

	public void setVISIBLE(String VISIBLE) {
		this.VISIBLE = VISIBLE;
	}

	public String getEDITABLE() {
		return this.EDITABLE;
	}

	public void setEDITABLE(String EDITABLE) {
		this.EDITABLE = EDITABLE;
	}

	public String getVALUE() {
		return this.VALUE;
	}

	public void setVALUE(String VALUE) {
		this.VALUE = VALUE;
	}

	public String getREF_MODEL() {
		return this.REF_MODEL;
	}

	public void setREF_MODEL(String REF_MODEL) {
		this.REF_MODEL = REF_MODEL;
	}

	public String getSOURCE() {
		return this.SOURCE;
	}

	public void setSOURCE(String SOURCE) {
		this.SOURCE = SOURCE;
	}

	public Integer getWIDTH() {
		return this.WIDTH;
	}

	public void setWIDTH(Integer WIDTH) {
		this.WIDTH = WIDTH;
	}

	public String getIS_MUST_INPUT() {
		return this.IS_MUST_INPUT;
	}

	public void setIS_MUST_INPUT(String IS_MUST_INPUT) {
		this.IS_MUST_INPUT = IS_MUST_INPUT;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("UI_DETAIL_ID",
				getUI_DETAIL_ID()).toString();
	}

	public String getENABLED() {
		return ENABLED;
	}

	public void setENABLED(String enable) {
		ENABLED = enable;
	}

	public String getQUERY_RELATION_SIGN() {
		return QUERY_RELATION_SIGN;
	}

	public void setQUERY_RELATION_SIGN(String query_relation_sign) {
		QUERY_RELATION_SIGN = query_relation_sign;
	}

	/**
	 * @return the dETAIL_TYPE
	 */
	public Integer getDETAIL_TYPE() {
		return DETAIL_TYPE;
	}

	/**
	 * @param detail_type
	 *            the dETAIL_TYPE to set
	 */
	public void setDETAIL_TYPE(Integer detail_type) {
		DETAIL_TYPE = detail_type;
	}

	/**
	 * @return the pARENT_ID
	 */
	public String getPARENT_ID() {
		return PARENT_ID;
	}

	/**
	 * @param parent_id
	 *            the pARENT_ID to set
	 */
	public void setPARENT_ID(String parent_id) {
		PARENT_ID = parent_id;
	}

}
