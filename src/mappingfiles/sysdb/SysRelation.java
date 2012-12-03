package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRelation implements Serializable {

	/** identifier field */
	private String RELATION_DETAIL_ID;

	/** persistent field */
	private String RELATION_ID;

	/** nullable persistent field */
	private String PRI_ELE_VALUE;

	/** nullable persistent field */
	private String SEC_ELE_VALUE;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private BigDecimal IS_DELETED;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysRelation(String RELATION_DETAIL_ID, String RELATION_ID,
			String PRI_ELE_VALUE, String SEC_ELE_VALUE, int SET_YEAR,
			BigDecimal IS_DELETED, String LAST_VER) {
		this.RELATION_DETAIL_ID = RELATION_DETAIL_ID;
		this.RELATION_ID = RELATION_ID;
		this.PRI_ELE_VALUE = PRI_ELE_VALUE;
		this.SEC_ELE_VALUE = SEC_ELE_VALUE;
		this.SET_YEAR = SET_YEAR;
		this.IS_DELETED = IS_DELETED;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysRelation() {
	}

	/** minimal constructor */
	public SysRelation(String RELATION_DETAIL_ID, String RELATION_ID,
			int SET_YEAR, BigDecimal IS_DELETED) {
		this.RELATION_DETAIL_ID = RELATION_DETAIL_ID;
		this.RELATION_ID = RELATION_ID;
		this.SET_YEAR = SET_YEAR;
		this.IS_DELETED = IS_DELETED;
	}

	public String getRELATION_DETAIL_ID() {
		return this.RELATION_DETAIL_ID;
	}

	public void setRELATION_DETAIL_ID(String RELATION_DETAIL_ID) {
		this.RELATION_DETAIL_ID = RELATION_DETAIL_ID;
	}

	public String getRELATION_ID() {
		return this.RELATION_ID;
	}

	public void setRELATION_ID(String RELATION_ID) {
		this.RELATION_ID = RELATION_ID;
	}

	public String getPRI_ELE_VALUE() {
		return this.PRI_ELE_VALUE;
	}

	public void setPRI_ELE_VALUE(String PRI_ELE_VALUE) {
		this.PRI_ELE_VALUE = PRI_ELE_VALUE;
	}

	public String getSEC_ELE_VALUE() {
		return this.SEC_ELE_VALUE;
	}

	public void setSEC_ELE_VALUE(String SEC_ELE_VALUE) {
		this.SEC_ELE_VALUE = SEC_ELE_VALUE;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
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
		return new ToStringBuilder(this).append("RELATION_DETAIL_ID",
				getRELATION_DETAIL_ID()).toString();
	}

}
