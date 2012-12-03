package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysApp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String SYS_ID;

	/** nullable persistent field */
	private String SYS_NAME;

	/** nullable persistent field */
	private String SYS_DESC;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String VERSION;

	/** nullable persistent field */
	private String DEPLOY_IP;

	/** nullable persistent field */
	private String DEPLOY_PORT;

	/** nullable persistent field */
	private String DEPLOY_FOLDER;

	/** nullable persistent field */
	private String DOWNLOAD_SERVLET;

	/** nullable persistent field */
	private Integer IS_LOCAL;

	/** nullable persistent field */
	private String ORG_TYPE;

	/** nullable persistent field */
	private String WELCOME_IMG;

	/** nullable persistent field */
	private Integer IS_OFFLINE;

	/** nullable persistent field */
	private String APPLICATION_HANDLE;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private Integer IS_DSTORE;

	/** nullable persistent field */
	private String SYS_YEAR;

	private String JAR_NAMES;

	/** full constructor */
	public SysApp(String SYS_ID, String SYS_NAME, String SYS_DESC, int ENABLED,
			String VERSION, String DEPLOY_IP, String DEPLOY_PORT,
			String DEPLOY_FOLDER, String DOWNLOAD_SERVLET, Integer IS_LOCAL,
			String ORG_TYPE, String WELCOME_IMG, Integer IS_DSTORE,
			Integer IS_OFFLINE, String APPLICATION_HANDLE, String LAST_VER,
			String SYS_YEAR, String JAR_NAMES) {
		this.SYS_ID = SYS_ID;
		this.SYS_NAME = SYS_NAME;
		this.SYS_DESC = SYS_DESC;
		this.ENABLED = ENABLED;
		this.VERSION = VERSION;
		this.DEPLOY_IP = DEPLOY_IP;
		this.DEPLOY_PORT = DEPLOY_PORT;
		this.DEPLOY_FOLDER = DEPLOY_FOLDER;
		this.DOWNLOAD_SERVLET = DOWNLOAD_SERVLET;
		this.IS_LOCAL = IS_LOCAL;
		this.ORG_TYPE = ORG_TYPE;
		this.WELCOME_IMG = WELCOME_IMG;
		this.IS_OFFLINE = IS_OFFLINE;
		this.APPLICATION_HANDLE = APPLICATION_HANDLE;
		this.LAST_VER = LAST_VER;
		this.IS_DSTORE = IS_DSTORE;
		this.SYS_YEAR = SYS_YEAR;
		this.JAR_NAMES = JAR_NAMES;
	}

	/** default constructor */
	public SysApp() {
	}

	/** minimal constructor */
	public SysApp(String SYS_ID, int ENABLED) {
		this.SYS_ID = SYS_ID;
		this.ENABLED = ENABLED;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public String getSYS_NAME() {
		return this.SYS_NAME;
	}

	public void setSYS_NAME(String SYS_NAME) {
		this.SYS_NAME = SYS_NAME;
	}

	public String getSYS_DESC() {
		return this.SYS_DESC;
	}

	public void setSYS_DESC(String SYS_DESC) {
		this.SYS_DESC = SYS_DESC;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getVERSION() {
		return this.VERSION;
	}

	public void setVERSION(String VERSION) {
		this.VERSION = VERSION;
	}

	public String getDEPLOY_IP() {
		return this.DEPLOY_IP;
	}

	public void setDEPLOY_IP(String DEPLOY_IP) {
		this.DEPLOY_IP = DEPLOY_IP;
	}

	public String getDEPLOY_PORT() {
		return this.DEPLOY_PORT;
	}

	public void setDEPLOY_PORT(String DEPLOY_PORT) {
		this.DEPLOY_PORT = DEPLOY_PORT;
	}

	public String getDEPLOY_FOLDER() {
		return this.DEPLOY_FOLDER;
	}

	public void setDEPLOY_FOLDER(String DEPLOY_FOLDER) {
		this.DEPLOY_FOLDER = DEPLOY_FOLDER;
	}

	public String getDOWNLOAD_SERVLET() {
		return this.DOWNLOAD_SERVLET;
	}

	public void setDOWNLOAD_SERVLET(String DOWNLOAD_SERVLET) {
		this.DOWNLOAD_SERVLET = DOWNLOAD_SERVLET;
	}

	public Integer getIS_LOCAL() {
		return this.IS_LOCAL;
	}

	public void setIS_LOCAL(Integer IS_LOCAL) {
		this.IS_LOCAL = IS_LOCAL;
	}

	public String getORG_TYPE() {
		return this.ORG_TYPE;
	}

	public void setORG_TYPE(String ORG_TYPE) {
		this.ORG_TYPE = ORG_TYPE;
	}

	public String getWELCOME_IMG() {
		return this.WELCOME_IMG;
	}

	public void setWELCOME_IMG(String WELCOME_IMG) {
		this.WELCOME_IMG = WELCOME_IMG;
	}

	public Integer getIS_OFFLINE() {
		return this.IS_OFFLINE;
	}

	public void setIS_OFFLINE(Integer IS_OFFLINE) {
		this.IS_OFFLINE = IS_OFFLINE;
	}

	public String getAPPLICATION_HANDLE() {
		return this.APPLICATION_HANDLE;
	}

	public void setAPPLICATION_HANDLE(String APPLICATION_HANDLE) {
		this.APPLICATION_HANDLE = APPLICATION_HANDLE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SYS_ID", getSYS_ID())
				.toString();
	}

	public Integer getIS_DSTORE() {
		return this.IS_DSTORE;
	}

	public void setIS_DSTORE(Integer IS_DSTORE) {
		this.IS_DSTORE = IS_DSTORE;
	}

	public String getSYS_YEAR() {
		return SYS_YEAR;
	}

	public void setSYS_YEAR(String SYS_YEAR) {
		this.SYS_YEAR = SYS_YEAR;
	}

	/**
	 * @return the jAR_NAMES
	 */
	public String getJAR_NAMES() {
		return JAR_NAMES;
	}

	/**
	 * @param jar_names
	 *            the jAR_NAMES to set
	 */
	public void setJAR_NAMES(String jar_names) {
		JAR_NAMES = jar_names;
	}

}
