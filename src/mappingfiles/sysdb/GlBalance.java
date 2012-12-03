package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBalance implements Serializable {

	/** identifier field */
	private String SUM_ID;

	/** persistent field */
	private String SUM_TYPE_ID;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private int SET_MONTH;

	/** nullable persistent field */
	private String CCID;

	/** nullable persistent field */
	private BigDecimal AVI_MONEY;

	/** nullable persistent field */
	private BigDecimal USE_MONEY;

	/** nullable persistent field */
	private BigDecimal MINUS_MONEY;

	/** nullable persistent field */
	private BigDecimal AVING_MONEY;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String RCID;

	/** nullable persistent field */
	private String FROMCTRLID;

	/** full constructor */
	public GlBalance(String SUM_ID, String SUM_TYPE_ID, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			int SET_YEAR, int SET_MONTH, String CCID, BigDecimal AVI_MONEY,
			BigDecimal USE_MONEY, BigDecimal MINUS_MONEY,
			BigDecimal AVING_MONEY, String LAST_VER, String RCID,
			String FROMCTRLID) {
		this.SUM_ID = SUM_ID;
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.SET_YEAR = SET_YEAR;
		this.SET_MONTH = SET_MONTH;
		this.CCID = CCID;
		this.AVI_MONEY = AVI_MONEY;
		this.USE_MONEY = USE_MONEY;
		this.MINUS_MONEY = MINUS_MONEY;
		this.AVING_MONEY = AVING_MONEY;
		this.LAST_VER = LAST_VER;
		this.RCID = RCID;
		this.FROMCTRLID = FROMCTRLID;
	}

	/** default constructor */
	public GlBalance() {
	}

	/** minimal constructor */
	public GlBalance(String SUM_ID, String SUM_TYPE_ID, String LATEST_OP_DATE,
			int SET_YEAR, int SET_MONTH) {
		this.SUM_ID = SUM_ID;
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.SET_YEAR = SET_YEAR;
		this.SET_MONTH = SET_MONTH;
	}

	public String getSUM_ID() {
		return this.SUM_ID;
	}

	public void setSUM_ID(String SUM_ID) {
		this.SUM_ID = SUM_ID;
	}

	public String getSUM_TYPE_ID() {
		return this.SUM_TYPE_ID;
	}

	public void setSUM_TYPE_ID(String SUM_TYPE_ID) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
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

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public int getSET_MONTH() {
		return this.SET_MONTH;
	}

	public void setSET_MONTH(int SET_MONTH) {
		this.SET_MONTH = SET_MONTH;
	}

	public String getCCID() {
		return this.CCID;
	}

	public void setCCID(String CCID) {
		this.CCID = CCID;
	}

	public BigDecimal getAVI_MONEY() {
		return this.AVI_MONEY;
	}

	public void setAVI_MONEY(BigDecimal AVI_MONEY) {
		this.AVI_MONEY = AVI_MONEY;
	}

	public BigDecimal getUSE_MONEY() {
		return this.USE_MONEY;
	}

	public void setUSE_MONEY(BigDecimal USE_MONEY) {
		this.USE_MONEY = USE_MONEY;
	}

	public BigDecimal getMINUS_MONEY() {
		return this.MINUS_MONEY;
	}

	public void setMINUS_MONEY(BigDecimal MINUS_MONEY) {
		this.MINUS_MONEY = MINUS_MONEY;
	}

	public BigDecimal getAVING_MONEY() {
		return this.AVING_MONEY;
	}

	public void setAVING_MONEY(BigDecimal AVING_MONEY) {
		this.AVING_MONEY = AVING_MONEY;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String getRCID() {
		return this.RCID;
	}

	public void setRCID(String RCID) {
		this.RCID = RCID;
	}

	public String getFROMCTRLID() {
		return this.FROMCTRLID;
	}

	public void setFROMCTRLID(String FROMCTRLID) {
		this.FROMCTRLID = FROMCTRLID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SUM_ID", getSUM_ID())
				.toString();
	}

}
