package mappingfiles.sysdb;

import java.util.Date;

/**
 * RpXmsb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RpXmsb implements java.io.Serializable {

	// Fields

	private String xmsbid;
	private RpXmjl rpXmjl;
	private Long setYear;
	private String ysjcDm;
	private String bsId;
	private String bsiId;
	private Double f1;
	private Double f2;
	private Double f3;
	private Double f4;
	private Double f5;
	private Double f6;
	private Double f7;
	private Double f8;
	private String enId;
	private String rgCode;
	private String sbType;
	private String wsztDm;
	private String lrrDm;
	private Date lrrq;
	private String xgrDm;
	private Date xgrq;
	private String bz;
	private String sbCode;
	private Double totalSum;

	// Constructors

	/** default constructor */
	public RpXmsb() {
	}

	/** minimal constructor */
	public RpXmsb(String xmsbid, RpXmjl rpXmjl, Long setYear, String enId,
			String rgCode, String lrrDm, Date lrrq, String xgrDm, Date xgrq) {
		this.xmsbid = xmsbid;
		this.rpXmjl = rpXmjl;
		this.setYear = setYear;
		this.enId = enId;
		this.rgCode = rgCode;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
	}

	/** full constructor */
	public RpXmsb(String xmsbid, RpXmjl rpXmjl, Long setYear, String ysjcDm,
			String bsId, String bsiId, Double f1, Double f2, Double f3,
			Double f4, Double f5, Double f6, Double f7, Double f8, String enId,
			String rgCode, String sbType, String wsztDm, String lrrDm,
			Date lrrq, String xgrDm, Date xgrq, String bz, String sbCode,
			Double totalSum) {
		this.xmsbid = xmsbid;
		this.rpXmjl = rpXmjl;
		this.setYear = setYear;
		this.ysjcDm = ysjcDm;
		this.bsId = bsId;
		this.bsiId = bsiId;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
		this.f6 = f6;
		this.f7 = f7;
		this.f8 = f8;
		this.enId = enId;
		this.rgCode = rgCode;
		this.sbType = sbType;
		this.wsztDm = wsztDm;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
		this.bz = bz;
		this.sbCode = sbCode;
		this.totalSum = totalSum;
	}

	// Property accessors

	public String getXmsbid() {
		return this.xmsbid;
	}

	public void setXmsbid(String xmsbid) {
		this.xmsbid = xmsbid;
	}

	public RpXmjl getRpXmjl() {
		return this.rpXmjl;
	}

	public void setRpXmjl(RpXmjl rpXmjl) {
		this.rpXmjl = rpXmjl;
	}

	public Long getSetYear() {
		return this.setYear;
	}

	public void setSetYear(Long setYear) {
		this.setYear = setYear;
	}

	public String getYsjcDm() {
		return this.ysjcDm;
	}

	public void setYsjcDm(String ysjcDm) {
		this.ysjcDm = ysjcDm;
	}

	public String getBsId() {
		return this.bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}

	public String getBsiId() {
		return this.bsiId;
	}

	public void setBsiId(String bsiId) {
		this.bsiId = bsiId;
	}

	public Double getF1() {
		return this.f1;
	}

	public void setF1(Double f1) {
		this.f1 = f1;
	}

	public Double getF2() {
		return this.f2;
	}

	public void setF2(Double f2) {
		this.f2 = f2;
	}

	public Double getF3() {
		return this.f3;
	}

	public void setF3(Double f3) {
		this.f3 = f3;
	}

	public Double getF4() {
		return this.f4;
	}

	public void setF4(Double f4) {
		this.f4 = f4;
	}

	public Double getF5() {
		return this.f5;
	}

	public void setF5(Double f5) {
		this.f5 = f5;
	}

	public Double getF6() {
		return this.f6;
	}

	public void setF6(Double f6) {
		this.f6 = f6;
	}

	public Double getF7() {
		return this.f7;
	}

	public void setF7(Double f7) {
		this.f7 = f7;
	}

	public Double getF8() {
		return this.f8;
	}

	public void setF8(Double f8) {
		this.f8 = f8;
	}

	public String getEnId() {
		return this.enId;
	}

	public void setEnId(String enId) {
		this.enId = enId;
	}

	public String getRgCode() {
		return this.rgCode;
	}

	public void setRgCode(String rgCode) {
		this.rgCode = rgCode;
	}

	public String getSbType() {
		return this.sbType;
	}

	public void setSbType(String sbType) {
		this.sbType = sbType;
	}

	public String getWsztDm() {
		return this.wsztDm;
	}

	public void setWsztDm(String wsztDm) {
		this.wsztDm = wsztDm;
	}

	public String getLrrDm() {
		return this.lrrDm;
	}

	public void setLrrDm(String lrrDm) {
		this.lrrDm = lrrDm;
	}

	public Date getLrrq() {
		return this.lrrq;
	}

	public void setLrrq(Date lrrq) {
		this.lrrq = lrrq;
	}

	public String getXgrDm() {
		return this.xgrDm;
	}

	public void setXgrDm(String xgrDm) {
		this.xgrDm = xgrDm;
	}

	public Date getXgrq() {
		return this.xgrq;
	}

	public void setXgrq(Date xgrq) {
		this.xgrq = xgrq;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSbCode() {
		return this.sbCode;
	}

	public void setSbCode(String sbCode) {
		this.sbCode = sbCode;
	}

	public Double getTotalSum() {
		return this.totalSum;
	}

	public void setTotalSum(Double totalSum) {
		this.totalSum = totalSum;
	}

}