package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysLoginfo implements Serializable {

	/** identifier field */
	private String LOG_ID;

	/** nullable persistent field */
	private String USER_ID;

	private String USER_NAME;

	/** nullable persistent field */
	private String USER_IP;

	/** nullable persistent field */
	private String LOG_TYPE;

	/** nullable persistent field */
	private Integer LOG_LEVEL;

	/** nullable persistent field */
	private String MENU_ID;

	/** nullable persistent field */
	private String MODULE_ID;

	/** nullable persistent field */
	private String ACTION_TYPE_CODE;

	/** nullable persistent field */
	private String ACTION_NAME;

	/** nullable persistent field */
	private String SYS_ID;

	/** nullable persistent field */
	private String INSPECT_INFO;

	/** nullable persistent field */
	private String REMARK;

	/** nullable persistent field */
	private String WF_NAME;

	/** nullable persistent field */
	private String CUR_NODE_NAME;

	/** nullable persistent field */
	private BigDecimal MONEY;

	/** nullable persistent field */
	private String VOU_ID;

	/** nullable persistent field */
	private String OPER_TIME;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysLoginfo(String LOG_ID, String USER_ID, String USER_IP,
			String LOG_TYPE, Integer LOG_LEVEL, String MENU_ID,
			String MODULE_ID, String ACTION_TYPE_CODE, String ACTION_NAME,
			String SYS_ID, String INSPECT_INFO, String REMARK, String WF_NAME,
			String CUR_NODE_NAME, BigDecimal MONEY, String VOU_ID,
			String OPER_TIME, String LAST_VER) {
		this.LOG_ID = LOG_ID;
		this.USER_ID = USER_ID;
		this.USER_IP = USER_IP;
		this.LOG_TYPE = LOG_TYPE;
		this.LOG_LEVEL = LOG_LEVEL;
		this.MENU_ID = MENU_ID;
		this.MODULE_ID = MODULE_ID;
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
		this.ACTION_NAME = ACTION_NAME;
		this.SYS_ID = SYS_ID;
		this.INSPECT_INFO = INSPECT_INFO;
		this.REMARK = REMARK;
		this.WF_NAME = WF_NAME;
		this.CUR_NODE_NAME = CUR_NODE_NAME;
		this.MONEY = MONEY;
		this.VOU_ID = VOU_ID;
		this.OPER_TIME = OPER_TIME;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysLoginfo() {
	}

	/** minimal constructor */
	public SysLoginfo(String LOG_ID) {
		this.LOG_ID = LOG_ID;
	}

	public String getLOG_ID() {
		return this.LOG_ID;
	}

	public void setLOG_ID(String LOG_ID) {
		this.LOG_ID = LOG_ID;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getUSER_IP() {
		return this.USER_IP;
	}

	public void setUSER_IP(String USER_IP) {
		this.USER_IP = USER_IP;
	}

	public String getLOG_TYPE() {
		return this.LOG_TYPE;
	}

	public void setLOG_TYPE(String LOG_TYPE) {
		this.LOG_TYPE = LOG_TYPE;
	}

	public Integer getLOG_LEVEL() {
		return this.LOG_LEVEL;
	}

	public void setLOG_LEVEL(Integer LOG_LEVEL) {
		this.LOG_LEVEL = LOG_LEVEL;
	}

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String MENU_ID) {
		this.MENU_ID = MENU_ID;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getACTION_TYPE_CODE() {
		return this.ACTION_TYPE_CODE;
	}

	public void setACTION_TYPE_CODE(String ACTION_TYPE_CODE) {
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
	}

	public String getACTION_NAME() {
		return this.ACTION_NAME;
	}

	public void setACTION_NAME(String ACTION_NAME) {
		this.ACTION_NAME = ACTION_NAME;
	}

	public String getSYS_ID() {
		return this.SYS_ID;
	}

	public void setSYS_ID(String SYS_ID) {
		this.SYS_ID = SYS_ID;
	}

	public String getINSPECT_INFO() {
		return this.INSPECT_INFO;
	}

	public void setINSPECT_INFO(String INSPECT_INFO) {
		this.INSPECT_INFO = INSPECT_INFO;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String getWF_NAME() {
		return this.WF_NAME;
	}

	public void setWF_NAME(String WF_NAME) {
		this.WF_NAME = WF_NAME;
	}

	public String getCUR_NODE_NAME() {
		return this.CUR_NODE_NAME;
	}

	public void setCUR_NODE_NAME(String CUR_NODE_NAME) {
		this.CUR_NODE_NAME = CUR_NODE_NAME;
	}

	public BigDecimal getMONEY() {
		return this.MONEY;
	}

	public void setMONEY(BigDecimal MONEY) {
		this.MONEY = MONEY;
	}

	public String getVOU_ID() {
		return this.VOU_ID;
	}

	public void setVOU_ID(String VOU_ID) {
		this.VOU_ID = VOU_ID;
	}

	public String getOPER_TIME() {
		return this.OPER_TIME;
	}

	public void setOPER_TIME(String OPER_TIME) {
		this.OPER_TIME = OPER_TIME;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("LOG_ID", getLOG_ID())
				.toString();
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String user_name) {
		USER_NAME = user_name;
	}

}
