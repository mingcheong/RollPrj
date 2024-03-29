package mappingfiles.sysdb;

/**
 * SysOffLineVersionManager generated by MyEclipse Persistence Tools
 */

public class SysOffLineVersionManager implements java.io.Serializable {

	// Fields

	private String VERSION_ID;
	private String SYS_ID;
	private String VERSION_CODE;
	private String VERSION_NAME;
	private String VERSION_URL;
	private Long LAST_VER_NUM;

	// Constructors

	/** default constructor */
	public SysOffLineVersionManager() {
	}

	/** minimal constructor */
	public SysOffLineVersionManager(String VERSION_ID, String SYS_ID,
			String VERSION_NAME, String VERSION_URL) {
		this.VERSION_ID = VERSION_ID;
		this.SYS_ID = SYS_ID;
		this.VERSION_NAME = VERSION_NAME;
		this.VERSION_URL = VERSION_URL;
	}

	/** full constructor */
	public SysOffLineVersionManager(String VERSION_ID, String SYS_ID,
			String VERSION_CODE, String VERSION_NAME, String VERSION_URL,
			Long LAST_VER_NUM) {
		this.VERSION_ID = VERSION_ID;
		this.SYS_ID = SYS_ID;
		this.VERSION_CODE = VERSION_CODE;
		this.VERSION_NAME = VERSION_NAME;
		this.VERSION_URL = VERSION_URL;
		this.LAST_VER_NUM = LAST_VER_NUM;
	}

	// Property accessors

	public String getVERSION_ID() {
		return this.VERSION_ID;
	}

	public void setVERSION_ID(String VERSION_ID) {
		this.VERSION_ID = VERSION_ID;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public String getVERSION_CODE() {
		return this.VERSION_CODE;
	}

	public void setVERSION_CODE(String VERSION_CODE) {
		this.VERSION_CODE = VERSION_CODE;
	}

	public String getVERSION_NAME() {
		return this.VERSION_NAME;
	}

	public void setVERSION_NAME(String VERSION_NAME) {
		this.VERSION_NAME = VERSION_NAME;
	}

	public String getVERSION_URL() {
		return this.VERSION_URL;
	}

	public void setVERSION_URL(String VERSION_URL) {
		this.VERSION_URL = VERSION_URL;
	}

	public Long getLAST_VER_NUM() {
		return this.LAST_VER_NUM;
	}

	public void setLAST_VER_NUM(Long LAST_VER_NUM) {
		this.LAST_VER_NUM = LAST_VER_NUM;
	}

}