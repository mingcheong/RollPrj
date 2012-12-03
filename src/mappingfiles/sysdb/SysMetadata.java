package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysMetadata implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String FIELD_CODE;

	/** nullable persistent field */
	private String FIELD_NAME;

	/** nullable persistent field */
	private Integer FIELD_DISPTYPE;

	/** nullable persistent field */
	private String DEFAULT_VALUE;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private String FIELD_VALUESET;

	/** nullable persistent field */
	private String REMARK;

	/** nullable persistent field */
	private Integer IS_SYSTEM;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private BigDecimal IS_DELETED;

	/** nullable persistent field */
	private String SOURCE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysMetadata(String CHR_ID, int SET_YEAR, String FIELD_CODE,
			String FIELD_NAME, Integer FIELD_DISPTYPE, String DEFAULT_VALUE,
			String TIPS, String FIELD_VALUESET, String REMARK,
			Integer IS_SYSTEM, String CREATE_DATE, String CREATE_USER,
			String LATEST_OP_DATE, String LATEST_OP_USER,
			BigDecimal IS_DELETED, String SOURCE, String LAST_VER) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.FIELD_CODE = FIELD_CODE;
		this.FIELD_NAME = FIELD_NAME;
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
	}

	/** default constructor */
	public SysMetadata() {
	}

	/** minimal constructor */
	public SysMetadata(String CHR_ID, int SET_YEAR, String LATEST_OP_DATE,
			BigDecimal IS_DELETED) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
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

	public String getFIELD_CODE() {
		return this.FIELD_CODE;
	}

	public void setFIELD_CODE(String FIELD_CODE) {
		this.FIELD_CODE = FIELD_CODE;
	}

	public String getFIELD_NAME() {
		return this.FIELD_NAME;
	}

	public void setFIELD_NAME(String FIELD_NAME) {
		this.FIELD_NAME = FIELD_NAME;
	}

	public Integer getFIELD_DISPTYPE() {
		return this.FIELD_DISPTYPE;
	}

	public void setFIELD_DISPTYPE(Integer FIELD_DISPTYPE) {
		this.FIELD_DISPTYPE = FIELD_DISPTYPE;
	}

	public String getDEFAULT_VALUE() {
		return this.DEFAULT_VALUE;
	}

	public void setDEFAULT_VALUE(String DEFAULT_VALUE) {
		this.DEFAULT_VALUE = DEFAULT_VALUE;
	}

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public String getFIELD_VALUESET() {
		return this.FIELD_VALUESET;
	}

	public void setFIELD_VALUESET(String FIELD_VALUESET) {
		this.FIELD_VALUESET = FIELD_VALUESET;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public Integer getIS_SYSTEM() {
		return this.IS_SYSTEM;
	}

	public void setIS_SYSTEM(Integer IS_SYSTEM) {
		this.IS_SYSTEM = IS_SYSTEM;
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

	public BigDecimal getIS_DELETED() {
		return this.IS_DELETED;
	}

	public void setIS_DELETED(BigDecimal IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
	}

	public String getSOURCE() {
		return this.SOURCE;
	}

	public void setSOURCE(String SOURCE) {
		this.SOURCE = SOURCE;
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
