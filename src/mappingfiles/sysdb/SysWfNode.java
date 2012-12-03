package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 840037435982020340L;

	/** identifier field */
	private String NODE_ID;

	/** persistent field */
	private String WF_ID;

	/** nullable persistent field */
	private String NODE_CODE;

	/** nullable persistent field */
	private String NODE_NAME;

	/** nullable persistent field */
	private String NODE_TYPE;

	/** nullable persistent field */
	private String REMARK;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String WF_TABLE_NAME;

	/** persistent field */
	private int GATHER_FLAG;

	/** persistent field */
	private String ID_COLUMN_NAME;

	private String OUTER_TABLE_NAME;
	private String OUTER_COLUMN_NAME;

	private String RELATION_COLUMN_NAME;

	private int SEND_MSG_FLAG;

	private int SEND_MSG_TIME;

	private int SEND_MSG_TIME_UNIT;

	private int AUTO_AUDIT_FLAG;

	private int AUTO_AUDIT_TIME;

	private int AUTO_AUDIT_TIME_UNIT;

	public String getOUTER_COLUMN_NAME() {
		return OUTER_COLUMN_NAME;
	}

	public void setOUTER_COLUMN_NAME(String outer_column_name) {
		OUTER_COLUMN_NAME = outer_column_name;
	}

	public String getOUTER_TABLE_NAME() {
		return OUTER_TABLE_NAME;
	}

	public void setOUTER_TABLE_NAME(String outer_table_name) {
		OUTER_TABLE_NAME = outer_table_name;
	}

	public String getRELATION_COLUMN_NAME() {
		return RELATION_COLUMN_NAME;
	}

	public void setRELATION_COLUMN_NAME(String relation_column_name) {
		RELATION_COLUMN_NAME = relation_column_name;
	}

	/** full constructor */
	public SysWfNode(String NODE_ID, String WF_ID, String NODE_CODE,
			String NODE_NAME, String NODE_TYPE, String REMARK,
			String CREATE_USER, String CREATE_DATE, String LATEST_OP_USER,
			String LATEST_OP_DATE, String WF_TABLE_NAME, int GATHER_FLAG,
			String ID_COLUMN_NAME, String OUTER_TABLE_NAME,
			String OUTER_COLUMN_NAME, String RELATION_COLUMN_NAME) {
		this.NODE_ID = NODE_ID;
		this.WF_ID = WF_ID;
		this.NODE_CODE = NODE_CODE;
		this.NODE_NAME = NODE_NAME;
		this.NODE_TYPE = NODE_TYPE;
		this.REMARK = REMARK;
		this.CREATE_USER = CREATE_USER;
		this.CREATE_DATE = CREATE_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.WF_TABLE_NAME = WF_TABLE_NAME;
		this.GATHER_FLAG = GATHER_FLAG;
		this.ID_COLUMN_NAME = ID_COLUMN_NAME;
		this.OUTER_TABLE_NAME = OUTER_TABLE_NAME;
		this.OUTER_COLUMN_NAME = OUTER_COLUMN_NAME;
		this.RELATION_COLUMN_NAME = RELATION_COLUMN_NAME;
	}

	/** default constructor */
	public SysWfNode() {
	}

	/** minimal constructor */
	public SysWfNode(String NODE_ID, String WF_ID, int GATHER_FLAG,
			int TOLLY_FLAG) {
		this.NODE_ID = NODE_ID;
		this.WF_ID = WF_ID;
		this.GATHER_FLAG = GATHER_FLAG;
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getNODE_CODE() {
		return this.NODE_CODE;
	}

	public void setNODE_CODE(String NODE_CODE) {
		this.NODE_CODE = NODE_CODE;
	}

	public String getNODE_NAME() {
		return this.NODE_NAME;
	}

	public void setNODE_NAME(String NODE_NAME) {
		this.NODE_NAME = NODE_NAME;
	}

	public String getNODE_TYPE() {
		return this.NODE_TYPE;
	}

	public void setNODE_TYPE(String NODE_TYPE) {
		this.NODE_TYPE = NODE_TYPE;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String getCREATE_USER() {
		return this.CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public String getCREATE_DATE() {
		return this.CREATE_DATE;
	}

	public void setCREATE_DATE(String CREATE_DATE) {
		this.CREATE_DATE = CREATE_DATE;
	}

	public String getLATEST_OP_USER() {
		return this.LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String LATEST_OP_USER) {
		this.LATEST_OP_USER = LATEST_OP_USER;
	}

	public String getLATEST_OP_DATE() {
		return this.LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String LATEST_OP_DATE) {
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public String getWF_TABLE_NAME() {
		return this.WF_TABLE_NAME;
	}

	public void setWF_TABLE_NAME(String WF_TABLE_NAME) {
		this.WF_TABLE_NAME = WF_TABLE_NAME;
	}

	public int getGATHER_FLAG() {
		return this.GATHER_FLAG;
	}

	public void setGATHER_FLAG(int GATHER_FLAG) {
		this.GATHER_FLAG = GATHER_FLAG;
	}

	public String toString() {
		return new ToStringBuilder(this).append("NODE_ID", getNODE_ID())
				.toString();
	}

	public String getID_COLUMN_NAME() {
		return ID_COLUMN_NAME;
	}

	public void setID_COLUMN_NAME(String id_column_name) {
		ID_COLUMN_NAME = id_column_name;
	}

	public int getSEND_MSG_FLAG() {
		return SEND_MSG_FLAG;
	}

	public void setSEND_MSG_FLAG(int send_msg_flag) {
		SEND_MSG_FLAG = send_msg_flag;
	}

	public int getSEND_MSG_TIME() {
		return SEND_MSG_TIME;
	}

	public void setSEND_MSG_TIME(int send_msg_time) {
		SEND_MSG_TIME = send_msg_time;
	}

	public int getAUTO_AUDIT_FLAG() {
		return AUTO_AUDIT_FLAG;
	}

	public void setAUTO_AUDIT_FLAG(int auto_audit_flag) {
		AUTO_AUDIT_FLAG = auto_audit_flag;
	}

	public int getAUTO_AUDIT_TIME() {
		return AUTO_AUDIT_TIME;
	}

	public void setAUTO_AUDIT_TIME(int auto_audit_time) {
		AUTO_AUDIT_TIME = auto_audit_time;
	}

	/**
	 * @return Returns the sEND_MSG_TIME_UNIT.
	 */
	public int getSEND_MSG_TIME_UNIT() {
		return SEND_MSG_TIME_UNIT;
	}

	/**
	 * @param send_msg_time_unit
	 *            The sEND_MSG_TIME_UNIT to set.
	 */
	public void setSEND_MSG_TIME_UNIT(int send_msg_time_unit) {
		SEND_MSG_TIME_UNIT = send_msg_time_unit;
	}

	/**
	 * @return Returns the aUTO_AUDIT_TIME_UNIT.
	 */
	public int getAUTO_AUDIT_TIME_UNIT() {
		return AUTO_AUDIT_TIME_UNIT;
	}

	/**
	 * @param auto_audit_time_unit
	 *            The aUTO_AUDIT_TIME_UNIT to set.
	 */
	public void setAUTO_AUDIT_TIME_UNIT(int auto_audit_time_unit) {
		AUTO_AUDIT_TIME_UNIT = auto_audit_time_unit;
	}

}
