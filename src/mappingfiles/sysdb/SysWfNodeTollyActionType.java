package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfNodeTollyActionType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5920759326250923107L;

	/** identifier field */
	private String NODE_ID;

	/** identifier field */
	private String ACTION_TYPE_CODE;

	/** persistent field */
	private int TOLLY_FLAG;

	/** full constructor */
	public SysWfNodeTollyActionType(String NODE_ID, String ACTION_TYPE_CODE,
			int TOLLY_FLAG) {
		this.NODE_ID = NODE_ID;
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
		this.TOLLY_FLAG = TOLLY_FLAG;
	}

	/** default constructor */
	public SysWfNodeTollyActionType() {
	}

	public String getNODE_ID() {
		return this.NODE_ID;
	}

	public void setNODE_ID(String NODE_ID) {
		this.NODE_ID = NODE_ID;
	}

	public String getACTION_TYPE_CODE() {
		return this.ACTION_TYPE_CODE;
	}

	public void setACTION_TYPE_CODE(String ACTION_TYPE_CODE) {
		this.ACTION_TYPE_CODE = ACTION_TYPE_CODE;
	}

	public int getTOLLY_FLAG() {
		return this.TOLLY_FLAG;
	}

	public void setTOLLY_FLAG(int TOLLY_FLAG) {
		this.TOLLY_FLAG = TOLLY_FLAG;
	}

	public String toString() {
		return new ToStringBuilder(this).append("NODE_ID", getNODE_ID())
				.append("ACTION_TYPE_CODE", getACTION_TYPE_CODE()).toString();
	}

}
