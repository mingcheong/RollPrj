package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusVoucherDetail implements Serializable {

	/** identifier field */
	private String LINE_ID;

	/** persistent field */
	private String VOU_DETAIL_ID;

	/** nullable persistent field */
	private String CCID;

	/** nullable persistent field */
	private String DS_ID;

	/** nullable persistent field */
	private String CS_ID;

	/** nullable persistent field */
	private String SUMMARY;

	/** nullable persistent field */
	private BigDecimal VOU_MONEY;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private Integer SET_MONTH;

	/** nullable persistent field */
	private Integer IS_BUSINCREASE;

	/** nullable persistent field */
	private BigDecimal FROMCTRLID;

	/** nullable persistent field */
	private BigDecimal TOCTRLID;

	/** nullable persistent field */
	private String REMARK;

	/** persistent field */
	private String HEAD_ID;

	/** full constructor */
	public GlBusVoucherDetail(String LINE_ID, String VOU_DETAIL_ID,
			String CCID, String DS_ID, String CS_ID, String SUMMARY,
			BigDecimal VOU_MONEY, String LAST_VER, int SET_YEAR,
			Integer SET_MONTH, Integer IS_BUSINCREASE, BigDecimal FROMCTRLID,
			BigDecimal TOCTRLID, String REMARK, String HEAD_ID) {
		this.LINE_ID = LINE_ID;
		this.VOU_DETAIL_ID = VOU_DETAIL_ID;
		this.CCID = CCID;
		this.DS_ID = DS_ID;
		this.CS_ID = CS_ID;
		this.SUMMARY = SUMMARY;
		this.VOU_MONEY = VOU_MONEY;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
		this.SET_MONTH = SET_MONTH;
		this.IS_BUSINCREASE = IS_BUSINCREASE;
		this.FROMCTRLID = FROMCTRLID;
		this.TOCTRLID = TOCTRLID;
		this.REMARK = REMARK;
		this.HEAD_ID = HEAD_ID;
	}

	/** default constructor */
	public GlBusVoucherDetail() {
	}

	/** minimal constructor */
	public GlBusVoucherDetail(String LINE_ID, String VOU_DETAIL_ID,
			int SET_YEAR, String HEAD_ID) {
		this.LINE_ID = LINE_ID;
		this.VOU_DETAIL_ID = VOU_DETAIL_ID;
		this.SET_YEAR = SET_YEAR;
		this.HEAD_ID = HEAD_ID;
	}

	public String getLINE_ID() {
		return this.LINE_ID;
	}

	public void setLINE_ID(String LINE_ID) {
		this.LINE_ID = LINE_ID;
	}

	public String getVOU_DETAIL_ID() {
		return this.VOU_DETAIL_ID;
	}

	public void setVOU_DETAIL_ID(String VOU_DETAIL_ID) {
		this.VOU_DETAIL_ID = VOU_DETAIL_ID;
	}

	public String getCCID() {
		return this.CCID;
	}

	public void setCCID(String CCID) {
		this.CCID = CCID;
	}

	public String getDS_ID() {
		return this.DS_ID;
	}

	public void setDS_ID(String DS_ID) {
		this.DS_ID = DS_ID;
	}

	public String getCS_ID() {
		return this.CS_ID;
	}

	public void setCS_ID(String CS_ID) {
		this.CS_ID = CS_ID;
	}

	public String getSUMMARY() {
		return this.SUMMARY;
	}

	public void setSUMMARY(String SUMMARY) {
		this.SUMMARY = SUMMARY;
	}

	public BigDecimal getVOU_MONEY() {
		return this.VOU_MONEY;
	}

	public void setVOU_MONEY(BigDecimal VOU_MONEY) {
		this.VOU_MONEY = VOU_MONEY;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public Integer getSET_MONTH() {
		return this.SET_MONTH;
	}

	public void setSET_MONTH(Integer SET_MONTH) {
		this.SET_MONTH = SET_MONTH;
	}

	public Integer getIS_BUSINCREASE() {
		return this.IS_BUSINCREASE;
	}

	public void setIS_BUSINCREASE(Integer IS_BUSINCREASE) {
		this.IS_BUSINCREASE = IS_BUSINCREASE;
	}

	public BigDecimal getFROMCTRLID() {
		return this.FROMCTRLID;
	}

	public void setFROMCTRLID(BigDecimal FROMCTRLID) {
		this.FROMCTRLID = FROMCTRLID;
	}

	public BigDecimal getTOCTRLID() {
		return this.TOCTRLID;
	}

	public void setTOCTRLID(BigDecimal TOCTRLID) {
		this.TOCTRLID = TOCTRLID;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String getHEAD_ID() {
		return this.HEAD_ID;
	}

	public void setHEAD_ID(String HEAD_ID) {
		this.HEAD_ID = HEAD_ID;
	}

	public String toString() {
		return new ToStringBuilder(this).append("LINE_ID", getLINE_ID())
				.toString();
	}

}
