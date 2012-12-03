package mappingfiles.sysdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author Hibernate CodeGenerator */
public class SysWfCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1819783727685938581L;

	private String CONDITION_ID;

	private String CONDITION_CODE;

	private String CONDITION_NAME;

	private String EXPRESSION;

	private String REMARK;

	private String CREATE_USER;

	private String CREATE_DATE;

	private String LATEST_OP_USER;

	private String LATEST_OP_DATE;

	private String LAST_VER;

	private String BSH_EXPRESSION;

	private String CONDITION_TYPE;

	private Set sysWfConditionLineses = new HashSet(0);

	private List sysWfConditionLineList = new ArrayList();

	public List getSysWfConditionLineList() {
		return sysWfConditionLineList;
	}

	public void setSysWfConditionLineList(List sysWfConditionLineList) {
		this.sysWfConditionLineList = sysWfConditionLineList;
	}

	// Constructors
	public SysWfCondition() {

	}

	public Set getSysWfConditionLineses() {
		return this.sysWfConditionLineses;
	}

	public void setSysWfConditionLineses(Set sysWfConditionLineses) {
		this.sysWfConditionLineses = sysWfConditionLineses;
	}

	public String getBSH_EXPRESSION() {
		return BSH_EXPRESSION;
	}

	public void setBSH_EXPRESSION(String bsh_expression) {
		BSH_EXPRESSION = bsh_expression;
	}

	public String getCONDITION_CODE() {
		return CONDITION_CODE;
	}

	public void setCONDITION_CODE(String condition_code) {
		CONDITION_CODE = condition_code;
	}

	public String getCONDITION_ID() {
		return CONDITION_ID;
	}

	public void setCONDITION_ID(String condition_id) {
		CONDITION_ID = condition_id;
	}

	public String getCONDITION_NAME() {
		return CONDITION_NAME;
	}

	public void setCONDITION_NAME(String condition_name) {
		CONDITION_NAME = condition_name;
	}

	public String getCONDITION_TYPE() {
		return CONDITION_TYPE;
	}

	public void setCONDITION_TYPE(String condition_type) {
		CONDITION_TYPE = condition_type;
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

	public String getEXPRESSION() {
		return EXPRESSION;
	}

	public void setEXPRESSION(String expression) {
		EXPRESSION = expression;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public String getLATEST_OP_DATE() {
		return LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String latest_op_date) {
		LATEST_OP_DATE = latest_op_date;
	}

	public String getLATEST_OP_USER() {
		return LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String latest_op_user) {
		LATEST_OP_USER = latest_op_user;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String remark) {
		REMARK = remark;
	}

}
