package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysUsermanage implements Serializable {

	/** identifier field */
	private String USER_ID;

	/** persistent field */
	private String USER_CODE;

	/** nullable persistent field */
	private String USER_NAME;

	/** nullable persistent field */
	private String PASSWORD;

	/** persistent field */
	private String ORG_TYPE;

	/** nullable persistent field */
	private String ORG_CODE;

	/** persistent field */
	private int LEVEL_NUM;

	/** nullable persistent field */
	private Integer IS_LEAF;

	/** nullable persistent field */
	private Integer GENDER;

	/** nullable persistent field */
	private String TELEPHONE;

	/** nullable persistent field */
	private String MOBILE;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String HEADSHIP_CODE;

	/** nullable persistent field */
	private String BIRTHDAY;

	/** nullable persistent field */
	private String ADDRESS;

	/** nullable persistent field */
	private String EMAIL;

	/** persistent field */
	private int USER_TYPE;

	/** persistent field */
	private int IS_AUDIT;

	/** nullable persistent field */
	private String AUDIT_DATE;

	/** nullable persistent field */
	private String AUDIT_USER;

	/** nullable persistent field */
	private String NICKNAME;

	/** nullable persistent field */
	private String MB_ID;

	/** nullable persistent field */
	private String BELONG_ORG;

	/** nullable persistent field */
	private String BELONG_TYPE;

	/** nullable persistent field */
	private String LAST_VER;

	private Integer SECURITY_LEVEL;

	/** full constructor */
	public SysUsermanage(String USER_ID, String USER_CODE, String USER_NAME,
			String PASSWORD, String ORG_TYPE, String ORG_CODE, int LEVEL_NUM,
			Integer IS_LEAF, Integer GENDER, String TELEPHONE, String MOBILE,
			int ENABLED, String HEADSHIP_CODE, String BIRTHDAY, String ADDRESS,
			String EMAIL, int USER_TYPE, int IS_AUDIT, String AUDIT_DATE,
			String AUDIT_USER, String NICKNAME, String LAST_VER, String MB_ID,
			String BELONG_ORG, String BELONG_TYPE, Integer SECURITY_LEVEL) {
		this.USER_ID = USER_ID;
		this.USER_CODE = USER_CODE;
		this.USER_NAME = USER_NAME;
		this.PASSWORD = PASSWORD;
		this.ORG_TYPE = ORG_TYPE;
		this.ORG_CODE = ORG_CODE;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.GENDER = GENDER;
		this.TELEPHONE = TELEPHONE;
		this.MOBILE = MOBILE;
		this.ENABLED = ENABLED;
		this.HEADSHIP_CODE = HEADSHIP_CODE;
		this.BIRTHDAY = BIRTHDAY;
		this.ADDRESS = ADDRESS;
		this.EMAIL = EMAIL;
		this.USER_TYPE = USER_TYPE;
		this.IS_AUDIT = IS_AUDIT;
		this.AUDIT_DATE = AUDIT_DATE;
		this.AUDIT_USER = AUDIT_USER;
		this.NICKNAME = NICKNAME;
		this.LAST_VER = LAST_VER;
		this.MB_ID = MB_ID;
		this.BELONG_ORG = BELONG_ORG;
		this.BELONG_TYPE = BELONG_TYPE;
		this.SECURITY_LEVEL = SECURITY_LEVEL;
	}

	/** default constructor */
	public SysUsermanage() {
	}

	/** minimal constructor */
	public SysUsermanage(String USER_ID, String USER_CODE, String ORG_TYPE,
			int LEVEL_NUM, int ENABLED, int USER_TYPE, int IS_AUDIT,
			String BELONG_ORG, String BELONG_TYPE, Integer SECURITY_LEVEL) {
		this.USER_ID = USER_ID;
		this.USER_CODE = USER_CODE;
		this.ORG_TYPE = ORG_TYPE;
		this.LEVEL_NUM = LEVEL_NUM;
		this.ENABLED = ENABLED;
		this.USER_TYPE = USER_TYPE;
		this.IS_AUDIT = IS_AUDIT;
		this.BELONG_ORG = BELONG_ORG;
		this.BELONG_TYPE = BELONG_TYPE;
		this.SECURITY_LEVEL = SECURITY_LEVEL;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getUSER_CODE() {
		return this.USER_CODE;
	}

	public void setUSER_CODE(String USER_CODE) {
		this.USER_CODE = USER_CODE;
	}

	public String getUSER_NAME() {
		return this.USER_NAME;
	}

	public void setUSER_NAME(String USER_NAME) {
		this.USER_NAME = USER_NAME;
	}

	public String getPASSWORD() {
		return this.PASSWORD;
	}

	public void setPASSWORD(String PASSWORD) {
		this.PASSWORD = PASSWORD;
	}

	public String getORG_TYPE() {
		return this.ORG_TYPE;
	}

	public void setORG_TYPE(String ORG_TYPE) {
		this.ORG_TYPE = ORG_TYPE;
	}

	public String getORG_CODE() {
		return this.ORG_CODE;
	}

	public void setORG_CODE(String ORG_CODE) {
		this.ORG_CODE = ORG_CODE;
	}

	public int getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(int LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
	}

	public Integer getIS_LEAF() {
		return this.IS_LEAF;
	}

	public void setIS_LEAF(Integer IS_LEAF) {
		this.IS_LEAF = IS_LEAF;
	}

	public Integer getGENDER() {
		return this.GENDER;
	}

	public void setGENDER(Integer GENDER) {
		this.GENDER = GENDER;
	}

	public String getTELEPHONE() {
		return this.TELEPHONE;
	}

	public void setTELEPHONE(String TELEPHONE) {
		this.TELEPHONE = TELEPHONE;
	}

	public String getMOBILE() {
		return this.MOBILE;
	}

	public void setMOBILE(String MOBILE) {
		this.MOBILE = MOBILE;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getHEADSHIP_CODE() {
		return this.HEADSHIP_CODE;
	}

	public void setHEADSHIP_CODE(String HEADSHIP_CODE) {
		this.HEADSHIP_CODE = HEADSHIP_CODE;
	}

	public String getBIRTHDAY() {
		return this.BIRTHDAY;
	}

	public void setBIRTHDAY(String BIRTHDAY) {
		this.BIRTHDAY = BIRTHDAY;
	}

	public String getADDRESS() {
		return this.ADDRESS;
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS = ADDRESS;
	}

	public String getEMAIL() {
		return this.EMAIL;
	}

	public void setEMAIL(String EMAIL) {
		this.EMAIL = EMAIL;
	}

	public int getUSER_TYPE() {
		return this.USER_TYPE;
	}

	public void setUSER_TYPE(int USER_TYPE) {
		this.USER_TYPE = USER_TYPE;
	}

	public int getIS_AUDIT() {
		return this.IS_AUDIT;
	}

	public void setIS_AUDIT(int IS_AUDIT) {
		this.IS_AUDIT = IS_AUDIT;
	}

	public String getAUDIT_DATE() {
		return this.AUDIT_DATE;
	}

	public void setAUDIT_DATE(String AUDIT_DATE) {
		this.AUDIT_DATE = AUDIT_DATE;
	}

	public String getAUDIT_USER() {
		return this.AUDIT_USER;
	}

	public void setAUDIT_USER(String AUDIT_USER) {
		this.AUDIT_USER = AUDIT_USER;
	}

	public String getNICKNAME() {
		return this.NICKNAME;
	}

	public void setNICKNAME(String NICKNAME) {
		this.NICKNAME = NICKNAME;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String getMB_ID() {
		return this.MB_ID;
	}

	public void setMB_ID(String MB_ID) {
		this.MB_ID = MB_ID;
	}

	public String getBELONG_ORG() {
		return this.BELONG_ORG;
	}

	public void setBELONG_ORG(String BELONG_ORG) {
		this.BELONG_ORG = BELONG_ORG;
	}

	public String getBELONG_TYPE() {
		return this.BELONG_TYPE;
	}

	public void setBELONG_TYPE(String BELONG_TYPE) {
		this.BELONG_TYPE = BELONG_TYPE;
	}

	public String toString() {
		return new ToStringBuilder(this).append("USER_ID", getUSER_ID())
				.toString();
	}

	/**
	 * @return sECURITY_LEVEL
	 */
	public Integer getSECURITY_LEVEL() {
		return SECURITY_LEVEL;
	}

	/**
	 * @param security_level
	 *            Ҫ���õ� sECURITY_LEVEL
	 */
	public void setSECURITY_LEVEL(Integer security_level) {
		SECURITY_LEVEL = security_level;
	}

}
