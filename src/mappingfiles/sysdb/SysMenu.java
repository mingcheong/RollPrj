package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysMenu implements Serializable {

	/** identifier field */
	private String MENU_ID;

	/** persistent field */
	private String MENU_CODE;

	/** nullable persistent field */
	private String MENU_NAME;

	/** nullable persistent field */
	private String ICON;

	/** persistent field */
	private BigDecimal ENABLED;

	/** persistent field */
	private int LEVEL_NUM;

	/** persistent field */
	private BigDecimal IS_LEAF;

	/** nullable persistent field */
	private String TIPS;

	/** nullable persistent field */
	private Long DISP_ORDER;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String USER_SYS_ID;

	/** full constructor */
	public SysMenu(String MENU_ID, String MENU_CODE, String MENU_NAME,
			String ICON, BigDecimal ENABLED, int LEVEL_NUM, BigDecimal IS_LEAF,
			String TIPS, Long DISP_ORDER, String LAST_VER, String USER_SYS_ID) {
		this.MENU_ID = MENU_ID;
		this.MENU_CODE = MENU_CODE;
		this.MENU_NAME = MENU_NAME;
		this.ICON = ICON;
		this.ENABLED = ENABLED;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.TIPS = TIPS;
		this.DISP_ORDER = DISP_ORDER;
		this.LAST_VER = LAST_VER;
		this.USER_SYS_ID = USER_SYS_ID;
	}

	/** default constructor */
	public SysMenu() {
	}

	/** minimal constructor */
	public SysMenu(String MENU_ID, String MENU_CODE, BigDecimal ENABLED,
			int LEVEL_NUM, BigDecimal IS_LEAF) {
		this.MENU_ID = MENU_ID;
		this.MENU_CODE = MENU_CODE;
		this.ENABLED = ENABLED;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
	}

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String MENU_ID) {
		this.MENU_ID = MENU_ID;
	}

	public String getMENU_CODE() {
		return this.MENU_CODE;
	}

	public void setMENU_CODE(String MENU_CODE) {
		this.MENU_CODE = MENU_CODE;
	}

	public String getMENU_NAME() {
		return this.MENU_NAME;
	}

	public void setMENU_NAME(String MENU_NAME) {
		this.MENU_NAME = MENU_NAME;
	}

	public String getICON() {
		return this.ICON;
	}

	public void setICON(String ICON) {
		this.ICON = ICON;
	}

	public BigDecimal getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(BigDecimal ENABLED) {
		this.ENABLED = ENABLED;
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

	public String getTIPS() {
		return this.TIPS;
	}

	public void setTIPS(String TIPS) {
		this.TIPS = TIPS;
	}

	public Long getDISP_ORDER() {
		return this.DISP_ORDER;
	}

	public void setDISP_ORDER(Long DISP_ORDER) {
		this.DISP_ORDER = DISP_ORDER;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MENU_ID", getMENU_ID())
				.toString();
	}

	/**
	 * @return the uSER_SYS_ID
	 */
	public String getUSER_SYS_ID() {
		return USER_SYS_ID;
	}

	/**
	 * @param user_sys_id
	 *            the uSER_SYS_ID to set
	 */
	public void setUSER_SYS_ID(String user_sys_id) {
		USER_SYS_ID = user_sys_id;
	}

}
