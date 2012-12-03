package mappingfiles.sysdb;

import java.util.Date;




/**
 * RpDwkzs entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RpDwkzs implements java.io.Serializable
{

	// Fields

	private String dwkzsid;
	private Long setYear;
	private String enId;
	private Double f1;
	private Double f2;
	private Double f3;
	private Double f4;
	private Double f5;
	private Double f6;
	private Double f7;
	private Double f8;
	private String rgCode;
	private String lrrDm;
	private Date lrrq;
	private String xgrDm;
	private Date xgrq;
	private String bz;
	private Long enable;
	private String enCode;



	// Constructors

	/** default constructor */
	public RpDwkzs()
	{
	}


	/** minimal constructor */
	public RpDwkzs(String dwkzsid, Long setYear, String enId, String rgCode, String lrrDm, Date lrrq, String xgrDm, Date xgrq)
	{
		this.dwkzsid = dwkzsid;
		this.setYear = setYear;
		this.enId = enId;
		this.rgCode = rgCode;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
	}


	/** full constructor */
	public RpDwkzs(String dwkzsid, Long setYear, String enId, Double f1, Double f2, Double f3, Double f4, Double f5, Double f6, Double f7, Double f8, String rgCode, String lrrDm, Date lrrq,
			String xgrDm, Date xgrq, Long enable, String bz, String enCode)
	{
		this.dwkzsid = dwkzsid;
		this.setYear = setYear;
		this.enId = enId;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
		this.f6 = f6;
		this.f7 = f7;
		this.f8 = f8;
		this.rgCode = rgCode;
		this.lrrDm = lrrDm;
		this.lrrq = lrrq;
		this.xgrDm = xgrDm;
		this.xgrq = xgrq;
		this.enable = enable;
		this.bz = bz;
		this.enCode = enCode;
	}


	// Property accessors

	public String getDwkzsid()
	{
		return this.dwkzsid;
	}


	public void setDwkzsid(String dwkzsid)
	{
		this.dwkzsid = dwkzsid;
	}


	public Long getSetYear()
	{
		return this.setYear;
	}


	public void setSetYear(Long setYear)
	{
		this.setYear = setYear;
	}


	public String getEnId()
	{
		return this.enId;
	}


	public void setEnId(String enId)
	{
		this.enId = enId;
	}


	public Double getF1()
	{
		return this.f1;
	}


	public void setF1(Double f1)
	{
		this.f1 = f1;
	}


	public Double getF2()
	{
		return this.f2;
	}


	public void setF2(Double f2)
	{
		this.f2 = f2;
	}


	public Double getF3()
	{
		return this.f3;
	}


	public void setF3(Double f3)
	{
		this.f3 = f3;
	}


	public Double getF4()
	{
		return this.f4;
	}


	public void setF4(Double f4)
	{
		this.f4 = f4;
	}


	public Double getF5()
	{
		return this.f5;
	}


	public void setF5(Double f5)
	{
		this.f5 = f5;
	}


	public Double getF6()
	{
		return this.f6;
	}


	public void setF6(Double f6)
	{
		this.f6 = f6;
	}


	public Double getF7()
	{
		return this.f7;
	}


	public void setF7(Double f7)
	{
		this.f7 = f7;
	}


	public Double getF8()
	{
		return this.f8;
	}


	public void setF8(Double f8)
	{
		this.f8 = f8;
	}


	public String getRgCode()
	{
		return this.rgCode;
	}


	public void setRgCode(String rgCode)
	{
		this.rgCode = rgCode;
	}


	public String getLrrDm()
	{
		return this.lrrDm;
	}


	public void setLrrDm(String lrrDm)
	{
		this.lrrDm = lrrDm;
	}


	public Date getLrrq()
	{
		return this.lrrq;
	}


	public void setLrrq(Date lrrq)
	{
		this.lrrq = lrrq;
	}


	public String getXgrDm()
	{
		return this.xgrDm;
	}


	public void setXgrDm(String xgrDm)
	{
		this.xgrDm = xgrDm;
	}


	public Date getXgrq()
	{
		return this.xgrq;
	}


	public void setXgrq(Date xgrq)
	{
		this.xgrq = xgrq;
	}


	public String getBz()
	{
		return this.bz;
	}


	public void setBz(String bz)
	{
		this.bz = bz;
	}


	public String getEnCode()
	{
		return this.enCode;
	}


	public void setEnCode(String enCode)
	{
		this.enCode = enCode;
	}


	public Long getEnable()
	{
		return enable;
	}


	public void setEnable(Long enable)
	{
		this.enable = enable;
	}

}
