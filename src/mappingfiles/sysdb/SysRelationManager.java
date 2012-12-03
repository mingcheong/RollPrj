package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRelationManager implements Serializable {

	/** identifier field */
	private String RELATION_ID;

	/** persistent field */
	private String RELATION_CODE;

	/** nullable persistent field */
	private String PRI_ELE_CODE;

	/** nullable persistent field */
	private String SEC_ELE_CODE;

	/** nullable persistent field */
	private Integer SET_YEAR;

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
	private String LAST_VER;

	private int RELATION_TYPE;

	/** full constructor */
	public SysRelationManager(String RELATION_ID, String RELATION_CODE,
			String PRI_ELE_CODE, String SEC_ELE_CODE, Integer SET_YEAR,
			String CREATE_DATE, String CREATE_USER, String LATEST_OP_DATE,
			String LATEST_OP_USER, BigDecimal IS_DELETED, String LAST_VER,
			int RELATION_TYPE) {
		this.RELATION_ID = RELATION_ID;
		this.RELATION_CODE = RELATION_CODE;
		this.PRI_ELE_CODE = PRI_ELE_CODE;
		this.SEC_ELE_CODE = SEC_ELE_CODE;
		this.SET_YEAR = SET_YEAR;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.IS_DELETED = IS_DELETED;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysRelationManager() {
	}

	/** minimal constructor */
	public SysRelationManager(String RELATION_ID, String RELATION_CODE,
			String LATEST_OP_DATE, BigDecimal IS_DELETED) {
		this.RELATION_ID = RELATION_ID;
		this.RELATION_CODE = RELATION_CODE;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
	}

	public String getRELATION_ID() {
		return this.RELATION_ID;
	}

	public void setRELATION_ID(String RELATION_ID) {
		this.RELATION_ID = RELATION_ID;
	}

	public String getRELATION_CODE() {
		return this.RELATION_CODE;
	}

	public void setRELATION_CODE(String RELATION_CODE) {
		this.RELATION_CODE = RELATION_CODE;
	}

	public String getPRI_ELE_CODE() {
		return this.PRI_ELE_CODE;
	}

	public void setPRI_ELE_CODE(String PRI_ELE_CODE) {
		this.PRI_ELE_CODE = PRI_ELE_CODE;
	}

	public String getSEC_ELE_CODE() {
		return this.SEC_ELE_CODE;
	}

	public void setSEC_ELE_CODE(String SEC_ELE_CODE) {
		this.SEC_ELE_CODE = SEC_ELE_CODE;
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
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

	public BigDecimal getIS_DELETED() {
		return this.IS_DELETED;
	}

	public void setIS_DELETED(BigDecimal IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("RELATION_ID", getRELATION_ID()).toString();
	}

	public int getRELATION_TYPE() {
		return RELATION_TYPE;
	}

	public void setRELATION_TYPE(int relation_type) {
		RELATION_TYPE = relation_type;
	}

}
