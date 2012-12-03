package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysElement implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private int SET_YEAR;

	/** persistent field */
	private String ELE_SOURCE;

	/** nullable persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private String ELE_NAME;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private Integer DISPMODE;

	/** nullable persistent field */
	private Integer REF_MODE;

	/** nullable persistent field */
	private Integer IS_RIGHTFILTER;

	/** nullable persistent field */
	private Integer MAX_LEVEL;

	/** nullable persistent field */
	private String CODE_RULE;

	/** nullable persistent field */
	private String LEVEL_NAME;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** persistent field */
	private BigDecimal IS_DELETED;

	/** persistent field */
	private BigDecimal IS_NOLEVEL;

	/** nullable persistent field */
	private Integer IS_LOCAL;

	/** nullable persistent field */
	private Integer IS_SYSTEM;

	/** nullable persistent field */
	private Integer ELE_TYPE;

	/** nullable persistent field */
	private Integer IS_VIEW;

	/** nullable persistent field */
	private String CZGB_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private Integer DISP_ORDER;

	private String SYS_ID;

	/** full constructor */
	public SysElement(String CHR_ID, int SET_YEAR, String ELE_SOURCE,
			String ELE_CODE, String ELE_NAME, int ENABLED, Integer DISPMODE,
			Integer REF_MODE, Integer IS_RIGHTFILTER, Integer MAX_LEVEL,
			String CODE_RULE, String LEVEL_NAME, String CREATE_DATE,
			String CREATE_USER, String LATEST_OP_DATE, String LATEST_OP_USER,
			BigDecimal IS_DELETED, BigDecimal IS_NOLEVEL, Integer IS_LOCAL,
			Integer IS_SYSTEM, Integer ELE_TYPE, Integer IS_VIEW,
			String CZGB_CODE, String LAST_VER, Integer DISP_ORDER, String SYS_ID) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.ELE_SOURCE = ELE_SOURCE;
		this.ELE_CODE = ELE_CODE;
		this.ELE_NAME = ELE_NAME;
		this.ENABLED = ENABLED;
		this.DISPMODE = DISPMODE;
		this.REF_MODE = REF_MODE;
		this.IS_RIGHTFILTER = IS_RIGHTFILTER;
		this.MAX_LEVEL = MAX_LEVEL;
		this.CODE_RULE = CODE_RULE;
		this.LEVEL_NAME = LEVEL_NAME;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.IS_DELETED = IS_DELETED;
		this.IS_NOLEVEL = IS_NOLEVEL;
		this.IS_LOCAL = IS_LOCAL;
		this.IS_SYSTEM = IS_SYSTEM;
		this.ELE_TYPE = ELE_TYPE;
		this.IS_VIEW = IS_VIEW;
		this.CZGB_CODE = CZGB_CODE;
		this.LAST_VER = LAST_VER;
		this.DISP_ORDER = DISP_ORDER;
		this.SYS_ID = SYS_ID;
	}

	/** default constructor */
	public SysElement() {
	}

	/** minimal constructor */
	public SysElement(String CHR_ID, int SET_YEAR, String ELE_SOURCE,
			int ENABLED, String LATEST_OP_DATE, BigDecimal IS_DELETED,
			BigDecimal IS_NOLEVEL, String SYS_ID) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.ELE_SOURCE = ELE_SOURCE;
		this.ENABLED = ENABLED;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
		this.IS_NOLEVEL = IS_NOLEVEL;
		this.SYS_ID = SYS_ID;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getELE_SOURCE() {
		return this.ELE_SOURCE;
	}

	public void setELE_SOURCE(String ELE_SOURCE) {
		this.ELE_SOURCE = ELE_SOURCE;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
	}

	public String getELE_NAME() {
		return this.ELE_NAME;
	}

	public void setELE_NAME(String ELE_NAME) {
		this.ELE_NAME = ELE_NAME;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public Integer getDISPMODE() {
		return this.DISPMODE;
	}

	public void setDISPMODE(Integer DISPMODE) {
		this.DISPMODE = DISPMODE;
	}

	public Integer getREF_MODE() {
		return this.REF_MODE;
	}

	public void setREF_MODE(Integer REF_MODE) {
		this.REF_MODE = REF_MODE;
	}

	public Integer getIS_RIGHTFILTER() {
		return this.IS_RIGHTFILTER;
	}

	public void setIS_RIGHTFILTER(Integer IS_RIGHTFILTER) {
		this.IS_RIGHTFILTER = IS_RIGHTFILTER;
	}

	public Integer getMAX_LEVEL() {
		return this.MAX_LEVEL;
	}

	public void setMAX_LEVEL(Integer MAX_LEVEL) {
		this.MAX_LEVEL = MAX_LEVEL;
	}

	public String getCODE_RULE() {
		return this.CODE_RULE;
	}

	public void setCODE_RULE(String CODE_RULE) {
		this.CODE_RULE = CODE_RULE;
	}

	public String getLEVEL_NAME() {
		return this.LEVEL_NAME;
	}

	public void setLEVEL_NAME(String LEVEL_NAME) {
		this.LEVEL_NAME = LEVEL_NAME;
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

	public BigDecimal getIS_DELETED() {
		return this.IS_DELETED;
	}

	public void setIS_DELETED(BigDecimal IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
	}

	public BigDecimal getIS_NOLEVEL() {
		return this.IS_NOLEVEL;
	}

	public void setIS_NOLEVEL(BigDecimal IS_NOLEVEL) {
		this.IS_NOLEVEL = IS_NOLEVEL;
	}

	public Integer getIS_LOCAL() {
		return this.IS_LOCAL;
	}

	public void setIS_LOCAL(Integer IS_LOCAL) {
		this.IS_LOCAL = IS_LOCAL;
	}

	public Integer getIS_SYSTEM() {
		return this.IS_SYSTEM;
	}

	public void setIS_SYSTEM(Integer IS_SYSTEM) {
		this.IS_SYSTEM = IS_SYSTEM;
	}

	public Integer getELE_TYPE() {
		return this.ELE_TYPE;
	}

	public void setELE_TYPE(Integer ELE_TYPE) {
		this.ELE_TYPE = ELE_TYPE;
	}

	public Integer getIS_VIEW() {
		return this.IS_VIEW;
	}

	public void setIS_VIEW(Integer IS_VIEW) {
		this.IS_VIEW = IS_VIEW;
	}

	public String getCZGB_CODE() {
		return this.CZGB_CODE;
	}

	public void setCZGB_CODE(String CZGB_CODE) {
		this.CZGB_CODE = CZGB_CODE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public Integer getDISP_ORDER() {
		return this.DISP_ORDER;
	}

	public void setDISP_ORDER(Integer DISP_ORDER) {
		this.DISP_ORDER = DISP_ORDER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("CHR_ID", getCHR_ID())
				.toString();
	}

	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

}
