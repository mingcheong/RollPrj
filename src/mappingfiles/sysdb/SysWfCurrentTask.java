package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfCurrentTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3630442965850261817L;

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

	/** full constructor */
	public SysWfCurrentTask(String TASK_ID, String WF_TABLE_NAME,
			String ENTITY_ID, String WF_ID, int CURRENT_NODE_ID,
			int PREVIOUS_NODE_ID, String TASK_STATUS, String CREATE_USER,
			String CREATE_DATE, String LATEST_OP_USER, String LATEST_OP_DATE) {
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
	}

	/** default constructor */
	public SysWfCurrentTask() {
	}

	/** minimal constructor */
	public SysWfCurrentTask(String TASK_ID, String WF_TABLE_NAME,
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

	public String toString() {
		return new ToStringBuilder(this).append("TASK_ID", getTASK_ID())
				.toString();
	}

}
