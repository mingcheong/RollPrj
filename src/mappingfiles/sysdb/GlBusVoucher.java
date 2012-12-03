package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusVoucher implements Serializable {

	/** identifier field */
	private String HEAD_ID;

	/** persistent field */
	private String VOU_ID;

	/** persistent field */
	private String VOU_TYPE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private int SET_MONTH;

	/** nullable persistent field */
	private String BILL_DATE;

	/** nullable persistent field */
	private Integer IS_END;

	/** nullable persistent field */
	private Long FLOW_ID;

	/** nullable persistent field */
	private Integer IS_VALID;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String VOU_NO;

	/** nullable persistent field */
	private String BILLTYPE_ID;

	/** nullable persistent field */
	private Integer IS_BUSINCREASE;

	/** nullable persistent field */
	private String ST_ID;

	/** full constructor */
	public GlBusVoucher(String HEAD_ID, String VOU_ID, String VOU_TYPE_ID,
			int SET_YEAR, int SET_MONTH, String BILL_DATE, Integer IS_END,
			Long FLOW_ID, Integer IS_VALID, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			String LAST_VER, String VOU_NO, String BILLTYPE_ID,
			Integer IS_BUSINCREASE, String ST_ID) {
		this.HEAD_ID = HEAD_ID;
		this.VOU_ID = VOU_ID;
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.SET_YEAR = SET_YEAR;
		this.SET_MONTH = SET_MONTH;
		this.BILL_DATE = BILL_DATE;
		this.IS_END = IS_END;
		this.FLOW_ID = FLOW_ID;
		this.IS_VALID = IS_VALID;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LAST_VER = LAST_VER;
		this.VOU_NO = VOU_NO;
		this.BILLTYPE_ID = BILLTYPE_ID;
		this.IS_BUSINCREASE = IS_BUSINCREASE;
		this.ST_ID = ST_ID;
	}

	/** default constructor */
	public GlBusVoucher() {
	}

	/** minimal constructor */
	public GlBusVoucher(String HEAD_ID, String VOU_ID, String VOU_TYPE_ID,
			int SET_YEAR, int SET_MONTH, String LATEST_OP_DATE) {
		this.HEAD_ID = HEAD_ID;
		this.VOU_ID = VOU_ID;
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.SET_YEAR = SET_YEAR;
		this.SET_MONTH = SET_MONTH;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public String getHEAD_ID() {
		return this.HEAD_ID;
	}

	public void setHEAD_ID(String HEAD_ID) {
		this.HEAD_ID = HEAD_ID;
	}

	public String getVOU_ID() {
		return this.VOU_ID;
	}

	public void setVOU_ID(String VOU_ID) {
		this.VOU_ID = VOU_ID;
	}

	public String getVOU_TYPE_ID() {
		return this.VOU_TYPE_ID;
	}

	public void setVOU_TYPE_ID(String VOU_TYPE_ID) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
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

	public String getBILL_DATE() {
		return this.BILL_DATE;
	}

	public void setBILL_DATE(String BILL_DATE) {
		this.BILL_DATE = BILL_DATE;
	}

	public Integer getIS_END() {
		return this.IS_END;
	}

	public void setIS_END(Integer IS_END) {
		this.IS_END = IS_END;
	}

	public Long getFLOW_ID() {
		return this.FLOW_ID;
	}

	public void setFLOW_ID(Long FLOW_ID) {
		this.FLOW_ID = FLOW_ID;
	}

	public Integer getIS_VALID() {
		return this.IS_VALID;
	}

	public void setIS_VALID(Integer IS_VALID) {
		this.IS_VALID = IS_VALID;
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

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String getVOU_NO() {
		return this.VOU_NO;
	}

	public void setVOU_NO(String VOU_NO) {
		this.VOU_NO = VOU_NO;
	}

	public String getBILLTYPE_ID() {
		return this.BILLTYPE_ID;
	}

	public void setBILLTYPE_ID(String BILLTYPE_ID) {
		this.BILLTYPE_ID = BILLTYPE_ID;
	}

	public Integer getIS_BUSINCREASE() {
		return this.IS_BUSINCREASE;
	}

	public void setIS_BUSINCREASE(Integer IS_BUSINCREASE) {
		this.IS_BUSINCREASE = IS_BUSINCREASE;
	}

	public String getST_ID() {
		return this.ST_ID;
	}

	public void setST_ID(String ST_ID) {
		this.ST_ID = ST_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("HEAD_ID", getHEAD_ID())
				.toString();
	}

}
