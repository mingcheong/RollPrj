package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysNavigate implements Serializable {

	/** identifier field */
	private String NAVIGATE_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String NAVIGATE_CODE;

	/** nullable persistent field */
	private String NAVIGATE_NAME;

	/** persistent field */
	private int LEVEL_NUM;

	/** persistent field */
	private BigDecimal IS_LEAF;

	/** nullable persistent field */
	private Long DISP_ORDER;

	/** nullable persistent field */
	private String NAVIGATE_ICON;

	/** nullable persistent field */
	private String ROLE_ID;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private String MENU_ID;

	/** nullable persistent field */
	private String PARENT_NAVIGATE_ID;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysNavigate(String NAVIGATE_ID, int SET_YEAR, String NAVIGATE_CODE,
			String NAVIGATE_NAME, int LEVEL_NUM, BigDecimal IS_LEAF,
			Long DISP_ORDER, String NAVIGATE_ICON, String ROLE_ID, String TIPS,
			String MENU_ID, String PARENT_NAVIGATE_ID, String LAST_VER) {
		this.NAVIGATE_ID = NAVIGATE_ID;
		this.SET_YEAR = SET_YEAR;
		this.NAVIGATE_CODE = NAVIGATE_CODE;
		this.NAVIGATE_NAME = NAVIGATE_NAME;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.DISP_ORDER = DISP_ORDER;
		this.NAVIGATE_ICON = NAVIGATE_ICON;
		this.ROLE_ID = ROLE_ID;
		this.TIPS = TIPS;
		this.MENU_ID = MENU_ID;
		this.PARENT_NAVIGATE_ID = PARENT_NAVIGATE_ID;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysNavigate() {
	}

	/** minimal constructor */
	public SysNavigate(String NAVIGATE_ID, int SET_YEAR, int LEVEL_NUM,
			BigDecimal IS_LEAF) {
		this.NAVIGATE_ID = NAVIGATE_ID;
		this.SET_YEAR = SET_YEAR;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
	}

	public String getNAVIGATE_ID() {
		return this.NAVIGATE_ID;
	}

	public void setNAVIGATE_ID(String NAVIGATE_ID) {
		this.NAVIGATE_ID = NAVIGATE_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getNAVIGATE_CODE() {
		return this.NAVIGATE_CODE;
	}

	public void setNAVIGATE_CODE(String NAVIGATE_CODE) {
		this.NAVIGATE_CODE = NAVIGATE_CODE;
	}

	public String getNAVIGATE_NAME() {
		return this.NAVIGATE_NAME;
	}

	public void setNAVIGATE_NAME(String NAVIGATE_NAME) {
		this.NAVIGATE_NAME = NAVIGATE_NAME;
	}

	public int getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(int LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
	}

	public BigDecimal getIS_LEAF() {
		return this.IS_LEAF;
	}

	public void setIS_LEAF(BigDecimal IS_LEAF) {
		this.IS_LEAF = IS_LEAF;
	}

	public Long getDISP_ORDER() {
		return this.DISP_ORDER;
	}

	public void setDISP_ORDER(Long DISP_ORDER) {
		this.DISP_ORDER = DISP_ORDER;
	}

	public String getNAVIGATE_ICON() {
		return this.NAVIGATE_ICON;
	}

	public void setNAVIGATE_ICON(String NAVIGATE_ICON) {
		this.NAVIGATE_ICON = NAVIGATE_ICON;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String MENU_ID) {
		this.MENU_ID = MENU_ID;
	}

	public String getPARENT_NAVIGATE_ID() {
		return this.PARENT_NAVIGATE_ID;
	}

	public void setPARENT_NAVIGATE_ID(String PARENT_NAVIGATE_ID) {
		this.PARENT_NAVIGATE_ID = PARENT_NAVIGATE_ID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("NAVIGATE_ID", getNAVIGATE_ID()).toString();
	}

}
