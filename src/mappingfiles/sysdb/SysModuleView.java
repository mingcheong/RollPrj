package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysModuleView implements Serializable {

	/** identifier field */
	private String MODULE_ID;

	/** identifier field */
	private String UI_ID;

	/** identifier field */
	private Integer DISP_ORDER;

	/** identifier field */
	private Integer SET_YEAR;

	/** nullable persistent field */
	private String REMARK;

	/** full constructor */
	public SysModuleView(String MODULE_ID, String UI_ID, Integer DISP_ORDER,
			Integer SET_YEAR, String REMARK) {
		this.MODULE_ID = MODULE_ID;
		this.UI_ID = UI_ID;
		this.DISP_ORDER = DISP_ORDER;
		this.SET_YEAR = SET_YEAR;
		this.REMARK = REMARK;
	}

	/** default constructor */
	public SysModuleView() {
	}

	/** minimal constructor */
	public SysModuleView(String MODULE_ID, String UI_ID, Integer DISP_ORDER,
			Integer SET_YEAR) {
		this.MODULE_ID = MODULE_ID;
		this.UI_ID = UI_ID;
		this.DISP_ORDER = DISP_ORDER;
		this.SET_YEAR = SET_YEAR;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getUI_ID() {
		return this.UI_ID;
	}

	public void setUI_ID(String UI_ID) {
		this.UI_ID = UI_ID;
	}

	public Integer getDISP_ORDER() {
		return this.DISP_ORDER;
	}

	public void setDISP_ORDER(Integer DISP_ORDER) {
		this.DISP_ORDER = DISP_ORDER;
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MODULE_ID", getMODULE_ID())
				.append("UI_ID", getUI_ID()).append("DISP_ORDER",
						getDISP_ORDER()).append("SET_YEAR", getSET_YEAR())
				.toString();
	}

}
