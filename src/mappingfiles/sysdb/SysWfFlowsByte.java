package mappingfiles.sysdb;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysWfFlowsByte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2842260118738187866L;

	/** identifier field */
	private String WF_ID;

	/** nullable persistent field */
	private Blob WF_BYTE;

	/** full constructor */
	public SysWfFlowsByte(String WF_ID, Blob WF_BYTE) {
		this.WF_ID = WF_ID;
		this.WF_BYTE = WF_BYTE;
	}

	/** default constructor */
	public SysWfFlowsByte() {
	}

	/** minimal constructor */
	public SysWfFlowsByte(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public String getWF_ID() {
		return this.WF_ID;
	}

	public void setWF_ID(String WF_ID) {
		this.WF_ID = WF_ID;
	}

	public Blob getWF_BYTE() {
		return this.WF_BYTE;
	}

	public void setWF_BYTE(Blob WF_BYTE) {
		this.WF_BYTE = WF_BYTE;
	}

	public String toString() {
		return new ToStringBuilder(this).append("WF_ID", getWF_ID()).toString();
	}

}
