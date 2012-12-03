package mappingfiles.sysdb;

/**
 * RpXmsbLy entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RpXmsbLy implements java.io.Serializable {

	// Fields

	private String lyId;
	private Long setYear;
	private String rgCode;
	private String xmxh;
	private String ly;

	// Constructors

	/** default constructor */
	public RpXmsbLy() {
	}

	/** minimal constructor */
	public RpXmsbLy(String lyId, Long setYear, String rgCode) {
		this.lyId = lyId;
		this.setYear = setYear;
		this.rgCode = rgCode;
	}

	/** full constructor */
	public RpXmsbLy(String lyId, Long setYear, String rgCode, String xmxh,
			String ly) {
		this.lyId = lyId;
		this.setYear = setYear;
		this.rgCode = rgCode;
		this.xmxh = xmxh;
		this.ly = ly;
	}

	// Property accessors

	public String getLyId() {
		return this.lyId;
	}

	public void setLyId(String lyId) {
		this.lyId = lyId;
	}

	public Long getSetYear() {
		return this.setYear;
	}

	public void setSetYear(Long setYear) {
		this.setYear = setYear;
	}

	public String getRgCode() {
		return this.rgCode;
	}

	public void setRgCode(String rgCode) {
		this.rgCode = rgCode;
	}

	public String getXmxh() {
		return this.xmxh;
	}

	public void setXmxh(String xmxh) {
		this.xmxh = xmxh;
	}

	public String getLy() {
		return this.ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

}