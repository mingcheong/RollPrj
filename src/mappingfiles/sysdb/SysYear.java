package mappingfiles.sysdb;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class SysYear implements Serializable {
	private static final long serialVersionUID = -4994565074410422326L;

	/** identifier field */
	private Integer SET_YEAR;

	/** nullable persistent field */
	private String YEAR_NAME;

	/** persistent field */
	private int ENABLED;

	/** persistent field */
	private int INIT_FLAG;

	/** nullable persistent field */
	private String START_DATE;

	/** nullable persistent field */
	private String END_DATE;

	/** nullable persistent field */
	private String LAST_VER;

	private String YEAR_STATUS;

	/** full constructor */
	public SysYear(Integer SET_YEAR, String YEAR_NAME, int ENABLED,
			int INIT_FLAG, String START_DATE, String END_DATE, String LAST_VER,
			String YEAR_STATUS) {
		this.SET_YEAR = SET_YEAR;
		this.YEAR_NAME = YEAR_NAME;
		this.ENABLED = ENABLED;
		this.INIT_FLAG = INIT_FLAG;
		this.START_DATE = START_DATE;
		this.END_DATE = END_DATE;
		this.LAST_VER = LAST_VER;
		this.YEAR_STATUS = YEAR_STATUS;
	}

	/** default constructor */
	public SysYear() {
	}

	/** minimal constructor */
	public SysYear(Integer SET_YEAR, int ENABLED, int INIT_FLAG) {
		this.SET_YEAR = SET_YEAR;
		this.ENABLED = ENABLED;
		this.INIT_FLAG = INIT_FLAG;
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getYEAR_NAME() {
		return this.YEAR_NAME;
	}

	public void setYEAR_NAME(String YEAR_NAME) {
		this.YEAR_NAME = YEAR_NAME;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public int getINIT_FLAG() {
		return this.INIT_FLAG;
	}

	public void setINIT_FLAG(int INIT_FLAG) {
		this.INIT_FLAG = INIT_FLAG;
	}

	public String getSTART_DATE() {
		return this.START_DATE;
	}

	public void setSTART_DATE(String START_DATE) {
		this.START_DATE = START_DATE;
	}

	public String getEND_DATE() {
		return this.END_DATE;
	}

	public void setEND_DATE(String END_DATE) {
		this.END_DATE = END_DATE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return YEAR_NAME;
	}

	/**
	 * @return yEAR_STATUS
	 */
	public String getYEAR_STATUS() {
		return YEAR_STATUS;
	}

	/**
	 * @param year_status
	 *            Ҫ���õ� yEAR_STATUS
	 */
	public void setYEAR_STATUS(String year_status) {
		YEAR_STATUS = year_status;
	}

}
