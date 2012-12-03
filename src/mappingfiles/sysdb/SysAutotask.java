package mappingfiles.sysdb;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class SysAutotask implements Serializable {

	/** identifier field */
	private int AUTOTASK_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String AUTOTASK_CODE;

	/** nullable persistent field */
	private String AUTOTASK_NAME;

	/** nullable persistent field */
	private String AUTOTASK_DESC;

	/** nullable persistent field */
	private Integer AUTOTASK_TYPE;

	/** nullable persistent field */
	private String START_DATE;

	/** nullable persistent field */
	private String END_DATE;

	/** nullable persistent field */
	private Integer TASKINTERVAL;

	/** nullable persistent field */
	private Integer RUN_TIMES;

	/** nullable persistent field */
	private Integer ENABLED;

	/** nullable persistent field */
	private String AUTOTASK_BEAN;

	/** nullable persistent field */
	private String AUTOTASK_PARAM;

	/** nullable persistent field */
	private String REMARK;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private Integer SCHEDULE_CRONTYPE;

	/** nullable persistent field */
	private Integer MONTH_OF_YEAR;

	/** nullable persistent field */
	private Integer DAY_OF_WEEK;

	/** nullable persistent field */
	private Integer DAY_OF_MONTH;

	/** nullable persistent field */
	private Integer HOUR_OF_DAY;

	/** nullable persistent field */
	private Integer MINUTE_OF_DAY;

	/** nullable persistent field */
	private String SYS_ID;

	/** full constructor */
	public SysAutotask(int AUTOTASK_ID, int SET_YEAR, String AUTOTASK_CODE,
			String AUTOTASK_NAME, String AUTOTASK_DESC, Integer AUTOTASK_TYPE,
			String START_DATE, String END_DATE, Integer TASKINTERVAL,
			Integer RUN_TIMES, Integer ENABLED, String AUTOTASK_BEAN,
			String AUTOTASK_PARAM, String REMARK, String CREATE_USER,
			String CREATE_DATE, String LAST_VER, Integer SCHEDULE_CRONTYPE,
			Integer MONTH_OF_YEAR, Integer DAY_OF_WEEK, Integer DAY_OF_MONTH,
			Integer HOUR_OF_DAY, Integer MINUTE_OF_DAY, String SYS_ID) {
		this.AUTOTASK_ID = AUTOTASK_ID;
		this.SET_YEAR = SET_YEAR;
		this.AUTOTASK_CODE = AUTOTASK_CODE;
		this.AUTOTASK_NAME = AUTOTASK_NAME;
		this.AUTOTASK_DESC = AUTOTASK_DESC;
		this.AUTOTASK_TYPE = AUTOTASK_TYPE;
		this.START_DATE = START_DATE;
		this.END_DATE = END_DATE;
		this.TASKINTERVAL = TASKINTERVAL;
		this.RUN_TIMES = RUN_TIMES;
		this.ENABLED = ENABLED;
		this.AUTOTASK_BEAN = AUTOTASK_BEAN;
		this.AUTOTASK_PARAM = AUTOTASK_PARAM;
		this.REMARK = REMARK;
		this.CREATE_USER = CREATE_USER;
		this.CREATE_DATE = CREATE_DATE;
		this.LAST_VER = LAST_VER;
		this.SCHEDULE_CRONTYPE = SCHEDULE_CRONTYPE;
		this.MONTH_OF_YEAR = MONTH_OF_YEAR;
		this.DAY_OF_WEEK = DAY_OF_WEEK;
		this.DAY_OF_MONTH = DAY_OF_MONTH;
		this.HOUR_OF_DAY = HOUR_OF_DAY;
		this.MINUTE_OF_DAY = MINUTE_OF_DAY;
		this.SYS_ID = SYS_ID;

	}

	/** default constructor */
	public SysAutotask() {
	}

	/** minimal constructor */
	public SysAutotask(int AUTOTASK_ID, int SET_YEAR) {
		this.AUTOTASK_ID = AUTOTASK_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public int getAUTOTASK_ID() {
		return AUTOTASK_ID;
	}

	public void setAUTOTASK_ID(int autotask_id) {
		AUTOTASK_ID = autotask_id;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int set_year) {
		SET_YEAR = set_year;
	}

	public String getAUTOTASK_PARAM() {
		return AUTOTASK_PARAM;
	}

	public void setAUTOTASK_PARAM(String autotask_param) {
		AUTOTASK_PARAM = autotask_param;
	}

	public Integer getAUTOTASK_TYPE() {
		return AUTOTASK_TYPE;
	}

	public void setAUTOTASK_TYPE(Integer autotask_type) {
		AUTOTASK_TYPE = autotask_type;
	}

	public String getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(String create_date) {
		CREATE_DATE = create_date;
	}

	public String getCREATE_USER() {
		return CREATE_USER;
	}

	public void setCREATE_USER(String create_user) {
		CREATE_USER = create_user;
	}

	public Integer getDAY_OF_MONTH() {
		return DAY_OF_MONTH;
	}

	public void setDAY_OF_MONTH(Integer day_of_month) {
		DAY_OF_MONTH = day_of_month;
	}

	public Integer getDAY_OF_WEEK() {
		return DAY_OF_WEEK;
	}

	public void setDAY_OF_WEEK(Integer day_of_week) {
		DAY_OF_WEEK = day_of_week;
	}

	public Integer getENABLED() {
		return ENABLED;
	}

	public void setENABLED(Integer enabled) {
		ENABLED = enabled;
	}

	public String getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(String end_date) {
		END_DATE = end_date;
	}

	public Integer getHOUR_OF_DAY() {
		return HOUR_OF_DAY;
	}

	public void setHOUR_OF_DAY(Integer hour_of_day) {
		HOUR_OF_DAY = hour_of_day;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public Integer getMINUTE_OF_DAY() {
		return MINUTE_OF_DAY;
	}

	public void setMINUTE_OF_DAY(Integer minute_of_day) {
		MINUTE_OF_DAY = minute_of_day;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String remark) {
		REMARK = remark;
	}

	public Integer getRUN_TIMES() {
		return RUN_TIMES;
	}

	public void setRUN_TIMES(Integer run_times) {
		RUN_TIMES = run_times;
	}

	public Integer getSCHEDULE_CRONTYPE() {
		return SCHEDULE_CRONTYPE;
	}

	public void setSCHEDULE_CRONTYPE(Integer schedule_crontype) {
		SCHEDULE_CRONTYPE = schedule_crontype;
	}

	public String getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(String start_date) {
		START_DATE = start_date;
	}

	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sys_id) {
		SYS_ID = sys_id;
	}

	public String getAUTOTASK_BEAN() {
		return AUTOTASK_BEAN;
	}

	public void setAUTOTASK_BEAN(String autotask_bean) {
		AUTOTASK_BEAN = autotask_bean;
	}

	public String getAUTOTASK_CODE() {
		return AUTOTASK_CODE;
	}

	public void setAUTOTASK_CODE(String autotask_code) {
		AUTOTASK_CODE = autotask_code;
	}

	public String getAUTOTASK_DESC() {
		return AUTOTASK_DESC;
	}

	public void setAUTOTASK_DESC(String autotask_desc) {
		AUTOTASK_DESC = autotask_desc;
	}

	public String getAUTOTASK_NAME() {
		return AUTOTASK_NAME;
	}

	public void setAUTOTASK_NAME(String autotask_name) {
		AUTOTASK_NAME = autotask_name;
	}

	public Integer getMONTH_OF_YEAR() {
		return MONTH_OF_YEAR;
	}

	public void setMONTH_OF_YEAR(Integer month_of_year) {
		MONTH_OF_YEAR = month_of_year;
	}

	public Integer getTASKINTERVAL() {
		return TASKINTERVAL;
	}

	public void setTASKINTERVAL(Integer taskinterval) {
		TASKINTERVAL = taskinterval;
	}

}