package mappingfiles.sysdb;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * RpXmjl entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RpXmjl implements java.io.Serializable {

	// Fields

	private String xmxh;
	private Long setYear;
	private String xmbm;
	private String xmmc;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private String c6;
	private String c7;
	private String c8;
	private String c9;
	private String c10;
	private String enId;
	private String rgCode;
	private String lrrDm;
	private Date lrrq;
	private String xgrDm;
	private Date xgrq;
	private String wfztDm;
	private String divCode;
	private String divName;
	private String csSort;
	private String enSort;
	private String gkdwId;
	private String gkdwCode;
	private Set rpXmjlKms = new HashSet(0);
	private Set rpXmsbs = new HashSet(0);

	// Constructors

	/** default constructor */
	public RpXmjl() {
	}

	/** minimal constructor */
	public RpXmjl(String xmxh, Long setYear, String xmbm, String xmmc,
			String enId, String lrrDm, Date lrrq, String xgrDm, Date xgrq,
			String divCode) {
		this.xmxh = xmxh;
		this.setYear = setYear;
		this.xmbm = xmbm;
		this.xmmc = xmmc;
		this.enId = enId;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
		this.divCode = divCode;
	}

	/** full constructor */
	public RpXmjl(String xmxh, Long setYear, String xmbm, String xmmc,
			String c1, String c2, String c3, String c4, String c5, String c6,
			String c7, String c8, String c9, String c10, String enId,
			String rgCode, String lrrDm, Date lrrq, String xgrDm, Date xgrq,
			String wfztDm, String divCode, String divName, String csSort,
			String enSort, String gkdwId, String gkdwCode, Set rpXmjlKms,
			Set rpXmsbs) {
		this.xmxh = xmxh;
		this.setYear = setYear;
		this.xmbm = xmbm;
		this.xmmc = xmmc;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.c5 = c5;
		this.c6 = c6;
		this.c7 = c7;
		this.c8 = c8;
		this.c9 = c9;
		this.c10 = c10;
		this.enId = enId;
		this.rgCode = rgCode;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
		this.wfztDm = wfztDm;
		this.divCode = divCode;
		this.divName = divName;
		this.csSort = csSort;
		this.enSort = enSort;
		this.gkdwId = gkdwId;
		this.gkdwCode = gkdwCode;
		this.rpXmjlKms = rpXmjlKms;
		this.rpXmsbs = rpXmsbs;
	}

	// Property accessors

	public String getXmxh() {
		return this.xmxh;
	}

	public void setXmxh(String xmxh) {
		this.xmxh = xmxh;
	}

	public Long getSetYear() {
		return this.setYear;
	}

	public void setSetYear(Long setYear) {
		this.setYear = setYear;
	}

	public String getXmbm() {
		return this.xmbm;
	}

	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}

	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getC1() {
		return this.c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return this.c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getC3() {
		return this.c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getC4() {
		return this.c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}

	public String getC5() {
		return this.c5;
	}

	public void setC5(String c5) {
		this.c5 = c5;
	}

	public String getC6() {
		return this.c6;
	}

	public void setC6(String c6) {
		this.c6 = c6;
	}

	public String getC7() {
		return this.c7;
	}

	public void setC7(String c7) {
		this.c7 = c7;
	}

	public String getC8() {
		return this.c8;
	}

	public void setC8(String c8) {
		this.c8 = c8;
	}

	public String getC9() {
		return this.c9;
	}

	public void setC9(String c9) {
		this.c9 = c9;
	}

	public String getC10() {
		return this.c10;
	}

	public void setC10(String c10) {
		this.c10 = c10;
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

	public String getWfztDm() {
		return this.wfztDm;
	}

	public void setWfztDm(String wfztDm) {
		this.wfztDm = wfztDm;
	}

	public String getDivCode() {
		return this.divCode;
	}

	public void setDivCode(String divCode) {
		this.divCode = divCode;
	}

	public String getDivName() {
		return this.divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getCsSort() {
		return this.csSort;
	}

	public void setCsSort(String csSort) {
		this.csSort = csSort;
	}

	public String getEnSort() {
		return this.enSort;
	}

	public void setEnSort(String enSort) {
		this.enSort = enSort;
	}

	public String getGkdwId() {
		return this.gkdwId;
	}

	public void setGkdwId(String gkdwId) {
		this.gkdwId = gkdwId;
	}

	public String getGkdwCode() {
		return this.gkdwCode;
	}

	public void setGkdwCode(String gkdwCode) {
		this.gkdwCode = gkdwCode;
	}

	public Set getRpXmjlKms() {
		return this.rpXmjlKms;
	}

	public void setRpXmjlKms(Set rpXmjlKms) {
		this.rpXmjlKms = rpXmjlKms;
	}

	public Set getRpXmsbs() {
		return this.rpXmsbs;
	}

	public void setRpXmsbs(Set rpXmsbs) {
		this.rpXmsbs = rpXmsbs;
	}

}