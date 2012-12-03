package mappingfiles.sysdb;

/**
 * RpXmjlKm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RpXmjlKm implements java.io.Serializable {

	// Fields

	private String xhid;
	private RpXmjl rpXmjl;
	private Long setYear;
	private String kmdm;

	// Constructors

	/** default constructor */
	public RpXmjlKm() {
	}

	/** full constructor */
	public RpXmjlKm(String xhid, RpXmjl rpXmjl, Long setYear, String kmdm) {
		this.xhid = xhid;
		this.rpXmjl = rpXmjl;
		this.setYear = setYear;
		this.kmdm = kmdm;
	}

	// Property accessors

	public String getXhid() {
		return this.xhid;
	}

	public void setXhid(String xhid) {
		this.xhid = xhid;
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

	public String getKmdm() {
		return this.kmdm;
	}

	public void setKmdm(String kmdm) {
		this.kmdm = kmdm;
	}

}