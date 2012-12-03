package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlBusvouControl implements Serializable {

	/** identifier field */
	private String VOU_CONTROL_ID;

	/** persistent field */
	private String VOU_TYPE_ID;

	/** persistent field */
	private String SUM_TYPE_ID;

	/** nullable persistent field */
	private Integer AVI_PERCENT;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private Integer CTRLLEVEL;

	/** nullable persistent field */
	private Integer IS_PRIMARYCONTROL;

	/** full constructor */
	public GlBusvouControl(String VOU_CONTROL_ID, String VOU_TYPE_ID,
			String SUM_TYPE_ID, Integer AVI_PERCENT, String LAST_VER,
			int SET_YEAR, Integer CTRLLEVEL, Integer IS_PRIMARYCONTROL) {
		this.VOU_CONTROL_ID = VOU_CONTROL_ID;
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.AVI_PERCENT = AVI_PERCENT;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
		this.CTRLLEVEL = CTRLLEVEL;
		this.IS_PRIMARYCONTROL = IS_PRIMARYCONTROL;
	}

	/** default constructor */
	public GlBusvouControl() {
	}

	/** minimal constructor */
	public GlBusvouControl(String VOU_CONTROL_ID, String VOU_TYPE_ID,
			String SUM_TYPE_ID, int SET_YEAR) {
		this.VOU_CONTROL_ID = VOU_CONTROL_ID;
		this.VOU_TYPE_ID = VOU_TYPE_ID;
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getVOU_CONTROL_ID() {
		return this.VOU_CONTROL_ID;
	}

	public void setVOU_CONTROL_ID(String VOU_CONTROL_ID) {
		this.VOU_CONTROL_ID = VOU_CONTROL_ID;
	}

	public String getVOU_TYPE_ID() {
		return this.VOU_TYPE_ID;
	}

	public void setVOU_TYPE_ID(String VOU_TYPE_ID) {
		this.VOU_TYPE_ID = VOU_TYPE_ID;
	}

	public String getSUM_TYPE_ID() {
		return this.SUM_TYPE_ID;
	}

	public void setSUM_TYPE_ID(String SUM_TYPE_ID) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
	}

	public Integer getAVI_PERCENT() {
		return this.AVI_PERCENT;
	}

	public void setAVI_PERCENT(Integer AVI_PERCENT) {
		this.AVI_PERCENT = AVI_PERCENT;
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

	public Integer getCTRLLEVEL() {
		return this.CTRLLEVEL;
	}

	public void setCTRLLEVEL(Integer CTRLLEVEL) {
		this.CTRLLEVEL = CTRLLEVEL;
	}

	public Integer getIS_PRIMARYCONTROL() {
		return this.IS_PRIMARYCONTROL;
	}

	public void setIS_PRIMARYCONTROL(Integer IS_PRIMARYCONTROL) {
		this.IS_PRIMARYCONTROL = IS_PRIMARYCONTROL;
	}

	public String toString() {
		return new ToStringBuilder(this).append("VOU_CONTROL_ID",
				getVOU_CONTROL_ID()).toString();
	}

}
