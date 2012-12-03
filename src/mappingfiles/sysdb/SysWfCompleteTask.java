package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfCompleteTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2593967611072177591L;

	/** identifier field */
	private String TASK_ID;

	/** persistent field */
	private String WF_TABLE_NAME;

	/** persistent field */
	private String ENTITY_ID;

	/** persistent field */
	private String WF_ID;

	/** persistent field */
	private int CURRENT_NODE_ID;

	/** persistent field */
	private int PREVIOUS_NODE_ID;

	/** persistent field */
	private String TASK_STATUS;

	/** nullable persistent field */
	private String CREATE_USER;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LATEST_OP_DATE;

	/** nullable persistent field */
	private String OPERATION_NAME;

	/** nullable persistent field */
	private String OPERATION_DATE;

	/** nullable persistent field */
	private String OPERATION_REMARK;

	/** nullable persistent field */
	private BigDecimal INIT_MONEY;

	/** nullable persistent field */
	private BigDecimal RESULT_MONEY;

	/** nullable persistent field */
	private String REMARK;

	/** full constructor */
	public SysWfCompleteTask(String TASK_ID, String WF_TABLE_NAME,
			String ENTITY_ID, String WF_ID, int CURRENT_NODE_ID,
			int PREVIOUS_NODE_ID, String TASK_STATUS, String CREATE_USER,
			String CREATE_DATE, String LATEST_OP_USER, String LATEST_OP_DATE,
			String OPERATION_NAME, String OPERATION_DATE,
			String OPERATION_REMARK, BigDecimal INIT_MONEY,
			BigDecimal RESULT_MONEY, String REMARK) {
		this.TASK_ID = TASK_ID;
		this.WF_TABLE_NAME = WF_TABLE_NAME;
		this.ENTITY_ID = ENTITY_ID;
		this.WF_ID = WF_ID;
		this.CURRENT_NODE_ID = CURRENT_NODE_ID;
		this.PREVIOUS_NODE_ID = PREVIOUS_NODE_ID;
		this.TASK_STATUS = TASK_STATUS;
		this.CREATE_USER = CREATE_USER;
		this.CREATE_DATE = CREATE_DATE;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.OPERATION_NAME = OPERATION_NAME;
		this.OPERATION_DATE = OPERATION_DATE;
		this.OPERATION_REMARK = OPERATION_REMARK;
		this.INIT_MONEY = INIT_MONEY;
		this.RESULT_MONEY = RESULT_MONEY;
		this.REMARK = REMARK;
	}

	/** default constructor */
	public SysWfCompleteTask() {
	}

	/** minimal constructor */
	public SysWfCompleteTask(String TASK_ID, String WF_TABLE_NAME,
			String ENTITY_ID, String WF_ID, int CURRENT_NODE_ID,
			int PREVIOUS_NODE_ID, String TASK_STATUS) {
		this.TASK_ID = TASK_ID;
		this.WF_TABLE_NAME = WF_TABLE_NAME;
		this.ENTITY_ID = ENTITY_ID;
		this.WF_ID = WF_ID;
		this.CURRENT_NODE_ID = CURRENT_NODE_ID;
		this.PREVIOUS_NODE_ID = PREVIOUS_NODE_ID;
		this.TASK_STATUS = TASK_STATUS;
	}

	public String getTASK_ID() {
		return this.TASK_ID;
	}

	public void setTASK_ID(String TASK_ID) {
		this.TASK_ID = TASK_ID;
	}

	public String getWF_TABLE_NAME() {
		return this.WF_TABLE_NAME;
	}

	public void setWF_TABLE_NAME(String WF_TABLE_NAME) {
		this.WF_TABLE_NAME = WF_TABLE_NAME;
	}

	public String getENTITY_ID() {
		return this.ENTITY_ID;
	}

	public void setENTITY_ID(String ENTITY_ID) {
		this.ENTITY_ID = ENTITY_ID;
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public int getCURRENT_NODE_ID() {
		return this.CURRENT_NODE_ID;
	}

	public void setCURRENT_NODE_ID(int CURRENT_NODE_ID) {
		this.CURRENT_NODE_ID = CURRENT_NODE_ID;
	}

	public int getPREVIOUS_NODE_ID() {
		return this.PREVIOUS_NODE_ID;
	}

	public void setPREVIOUS_NODE_ID(int PREVIOUS_NODE_ID) {
		this.PREVIOUS_NODE_ID = PREVIOUS_NODE_ID;
	}

	public String getTASK_STATUS() {
		return this.TASK_STATUS;
	}

	public void setTASK_STATUS(String TASK_STATUS) {
		this.TASK_STATUS = TASK_STATUS;
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

	public String getOPERATION_NAME() {
		return this.OPERATION_NAME;
	}

	public void setOPERATION_NAME(String OPERATION_NAME) {
		this.OPERATION_NAME = OPERATION_NAME;
	}

	public String getOPERATION_DATE() {
		return this.OPERATION_DATE;
	}

	public void setOPERATION_DATE(String OPERATION_DATE) {
		this.OPERATION_DATE = OPERATION_DATE;
	}

	public String getOPERATION_REMARK() {
		return this.OPERATION_REMARK;
	}

	public void setOPERATION_REMARK(String OPERATION_REMARK) {
		this.OPERATION_REMARK = OPERATION_REMARK;
	}

	public BigDecimal getINIT_MONEY() {
		return this.INIT_MONEY;
	}

	public void setINIT_MONEY(BigDecimal INIT_MONEY) {
		this.INIT_MONEY = INIT_MONEY;
	}

	public BigDecimal getRESULT_MONEY() {
		return this.RESULT_MONEY;
	}

	public void setRESULT_MONEY(BigDecimal RESULT_MONEY) {
		this.RESULT_MONEY = RESULT_MONEY;
	}

	public String getREMARK() {
		return this.REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String toString() {
		return new ToStringBuilder(this).append("TASK_ID", getTASK_ID())
				.toString();
	}

}
