package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysStatus implements Serializable {

	/** identifier field */
	private String STATUS_ID;

	/** persistent field */
	private String STATUS_NAME;

	/** persistent field */
	private String STATUS_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysStatus(String STATUS_ID, String STATUS_NAME, String STATUS_CODE,
			String LAST_VER) {
		this.STATUS_ID = STATUS_ID;
		this.STATUS_NAME = STATUS_NAME;
		this.STATUS_CODE = STATUS_CODE;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysStatus() {
	}

	/** minimal constructor */
	public SysStatus(String STATUS_ID, String STATUS_NAME, String STATUS_CODE) {
		this.STATUS_ID = STATUS_ID;
		this.STATUS_NAME = STATUS_NAME;
		this.STATUS_CODE = STATUS_CODE;
	}

	public String getSTATUS_ID() {
		return this.STATUS_ID;
	}

	public void setSTATUS_ID(String STATUS_ID) {
		this.STATUS_ID = STATUS_ID;
	}

	public String getSTATUS_NAME() {
		return this.STATUS_NAME;
	}

	public void setSTATUS_NAME(String STATUS_NAME) {
		this.STATUS_NAME = STATUS_NAME;
	}

	public String getSTATUS_CODE() {
		return this.STATUS_CODE;
	}

	public void setSTATUS_CODE(String STATUS_CODE) {
		this.STATUS_CODE = STATUS_CODE;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("STATUS_ID", getSTATUS_ID())
				.toString();
	}

}
