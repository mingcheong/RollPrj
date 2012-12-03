package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysBilltype implements Serializable {

	/** identifier field */
	private String BILLTYPE_ID;

	/** persistent field */
	private String BILLTYPE_CODE;

	/** persistent field */
	private String BILLTYPE_NAME;

	/** nullable persistent field */
	private Integer IS_BUSINCREASE;

	/** nullable persistent field */
	private String BUSVOU_TYPE_ID;

	/** nullable persistent field */
	private String COA_ID;

	/** nullable persistent field */
	private String BILLNORULE_ID;

	/** nullable persistent field */
	private String TABLE_NAME;

	/** nullable persistent field */
	private Integer BILLTYPE_CLASS;

	/** nullable persistent field */
	private Integer ENABLED;

	/** nullable persistent field */
	private Integer IS_NEEDCHECKNOBUDGET;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private Integer SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String NOBUDGETBUSVOU_TYPE_ID;

	private String SYS_ID;

	private String FIELD_NAME;

	private String UI_ID;

	private String FROM_BILLTYPE_ID;

	private String FROM_UI_ID;

	private String TO_BILLTYPE_ID;

	private String TO_UI_ID;

	private String VOU_CONTROL_ID;

	/** full constructor */
	public SysBilltype(String BILLTYPE_ID, String BILLTYPE_CODE,
			String BILLTYPE_NAME, Integer IS_BUSINCREASE,
			String BUSVOU_TYPE_ID, Integer IS_NEEDCHECKNOBUDGET,
			String NOBUDGETBUSVOU_TYPE_ID, String COA_ID, String BILLNORULE_ID,
			String TABLE_NAME, Integer BILLTYPE_CLASS, Integer ENABLED,
			String LATEST_OP_DATE, String LATEST_OP_USER, Integer SET_YEAR,
			String LAST_VER, String FIELD_NAME, String UI_ID,
			String FROM_BILLTYPE_ID, String FROM_UI_ID, String TO_BILLTYPE_ID,
			String TO_UI_ID, String VOU_CONTROL_ID) {
		this.BILLTYPE_ID = BILLTYPE_ID;
		this.BILLTYPE_CODE = BILLTYPE_CODE;
		this.BILLTYPE_NAME = BILLTYPE_NAME;
		this.IS_BUSINCREASE = IS_BUSINCREASE;
		this.BUSVOU_TYPE_ID = BUSVOU_TYPE_ID;
		this.NOBUDGETBUSVOU_TYPE_ID = NOBUDGETBUSVOU_TYPE_ID;
		this.COA_ID = COA_ID;
		this.BILLNORULE_ID = BILLNORULE_ID;
		this.TABLE_NAME = TABLE_NAME;
		this.BILLTYPE_CLASS = BILLTYPE_CLASS;
		this.IS_NEEDCHECKNOBUDGET = IS_NEEDCHECKNOBUDGET;
		this.ENABLED = ENABLED;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
		this.FIELD_NAME = FIELD_NAME;
		this.UI_ID = UI_ID;
		this.FROM_BILLTYPE_ID = FROM_BILLTYPE_ID;
		this.FROM_UI_ID = FROM_UI_ID;
		this.TO_BILLTYPE_ID = TO_BILLTYPE_ID;
		this.TO_UI_ID = TO_UI_ID;
		this.VOU_CONTROL_ID = VOU_CONTROL_ID;
	}

	/** default constructor */
	public SysBilltype() {
	}

	/** minimal constructor */
	public SysBilltype(String BILLTYPE_ID, String BILLTYPE_CODE,
			String BILLTYPE_NAME) {
		this.BILLTYPE_ID = BILLTYPE_ID;
		this.BILLTYPE_CODE = BILLTYPE_CODE;
		this.BILLTYPE_NAME = BILLTYPE_NAME;
	}

	public String getBILLTYPE_ID() {
		return this.BILLTYPE_ID;
	}

	public void setBILLTYPE_ID(String BILLTYPE_ID) {
		this.BILLTYPE_ID = BILLTYPE_ID;
	}

	public String getBILLTYPE_CODE() {
		return this.BILLTYPE_CODE;
	}

	public void setBILLTYPE_CODE(String BILLTYPE_CODE) {
		this.BILLTYPE_CODE = BILLTYPE_CODE;
	}

	public String getBILLTYPE_NAME() {
		return this.BILLTYPE_NAME;
	}

	public void setBILLTYPE_NAME(String BILLTYPE_NAME) {
		this.BILLTYPE_NAME = BILLTYPE_NAME;
	}

	public Integer getIS_BUSINCREASE() {
		return this.IS_BUSINCREASE;
	}

	public void setIS_BUSINCREASE(Integer IS_BUSINCREASE) {
		this.IS_BUSINCREASE = IS_BUSINCREASE;
	}

	public String getBUSVOU_TYPE_ID() {
		return this.BUSVOU_TYPE_ID;
	}

	public void setBUSVOU_TYPE_ID(String BUSVOU_TYPE_ID) {
		this.BUSVOU_TYPE_ID = BUSVOU_TYPE_ID;
	}

	public String getCOA_ID() {
		return this.COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
	}

	public String getBILLNORULE_ID() {
		return this.BILLNORULE_ID;
	}

	public void setBILLNORULE_ID(String BILLNORULE_ID) {
		this.BILLNORULE_ID = BILLNORULE_ID;
	}

	public String getTABLE_NAME() {
		return this.TABLE_NAME;
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME = TABLE_NAME;
	}

	public Integer getBILLTYPE_CLASS() {
		return this.BILLTYPE_CLASS;
	}

	public void setBILLTYPE_CLASS(Integer BILLTYPE_CLASS) {
		this.BILLTYPE_CLASS = BILLTYPE_CLASS;
	}

	public Integer getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(Integer ENABLED) {
		this.ENABLED = ENABLED;
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

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("BILLTYPE_ID", getBILLTYPE_ID()).toString();
	}

	public String getNOBUDGETBUSVOU_TYPE_ID() {
		return NOBUDGETBUSVOU_TYPE_ID;
	}

	public void setNOBUDGETBUSVOU_TYPE_ID(String nobudgetbusvou_type_id) {
		NOBUDGETBUSVOU_TYPE_ID = nobudgetbusvou_type_id;
	}

	public Integer getIS_NEEDCHECKNOBUDGET() {
		return IS_NEEDCHECKNOBUDGET;
	}

	public void setIS_NEEDCHECKNOBUDGET(Integer is_needchecknobudget) {
		IS_NEEDCHECKNOBUDGET = is_needchecknobudget;
	}

	/**
	 * @return fIELD_NAME
	 */
	public String getFIELD_NAME() {
		return FIELD_NAME;
	}

	/**
	 * @param field_name
	 *            Ҫ���õ� fIELD_NAME
	 */
	public void setFIELD_NAME(String field_name) {
		FIELD_NAME = field_name;
	}

	/**
	 * @return fROM_BILLTYPE_ID
	 */
	public String getFROM_BILLTYPE_ID() {
		return FROM_BILLTYPE_ID;
	}

	/**
	 * @param from_billtype_id
	 *            Ҫ���õ� fROM_BILLTYPE_ID
	 */
	public void setFROM_BILLTYPE_ID(String from_billtype_id) {
		FROM_BILLTYPE_ID = from_billtype_id;
	}

	/**
	 * @return fROM_UI_ID
	 */
	public String getFROM_UI_ID() {
		return FROM_UI_ID;
	}

	/**
	 * @param from_ui_id
	 *            Ҫ���õ� fROM_UI_ID
	 */
	public void setFROM_UI_ID(String from_ui_id) {
		FROM_UI_ID = from_ui_id;
	}

	/**
	 * @return tO_BILLTYPE_ID
	 */
	public String getTO_BILLTYPE_ID() {
		return TO_BILLTYPE_ID;
	}

	/**
	 * @param to_billtype_id
	 *            Ҫ���õ� tO_BILLTYPE_ID
	 */
	public void setTO_BILLTYPE_ID(String to_billtype_id) {
		TO_BILLTYPE_ID = to_billtype_id;
	}

	/**
	 * @return tO_UI_ID
	 */
	public String getTO_UI_ID() {
		return TO_UI_ID;
	}

	/**
	 * @param to_ui_id
	 *            Ҫ���õ� tO_UI_ID
	 */
	public void setTO_UI_ID(String to_ui_id) {
		TO_UI_ID = to_ui_id;
	}

	/**
	 * @return uI_ID
	 */
	public String getUI_ID() {
		return UI_ID;
	}

	/**
	 * @param ui_id
	 *            Ҫ���õ� uI_ID
	 */
	public void setUI_ID(String ui_id) {
		UI_ID = ui_id;
	}

	/**
	 * @return vOU_CONTROL_ID
	 */
	public String getVOU_CONTROL_ID() {
		return VOU_CONTROL_ID;
	}

	/**
	 * @param vou_control_id
	 *            Ҫ���õ� vOU_CONTROL_ID
	 */
	public void setVOU_CONTROL_ID(String vou_control_id) {
		VOU_CONTROL_ID = vou_control_id;
	}

	/**
	 * @return sYS_ID
	 */
	public String getSYS_ID() {
		return SYS_ID;
	}

	/**
	 * @param sys_id
	 *            Ҫ���õ� sYS_ID
	 */
	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

}
