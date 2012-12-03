package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlCcidTran implements Serializable {

	/** identifier field */
	private String S_CCID;

	/** identifier field */
	private String T_CCID;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlCcidTran(String S_CCID, String T_CCID, String LAST_VER,
			int SET_YEAR) {
		this.S_CCID = S_CCID;
		this.T_CCID = T_CCID;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlCcidTran() {
	}

	/** minimal constructor */
	public GlCcidTran(String S_CCID, String T_CCID, int SET_YEAR) {
		this.S_CCID = S_CCID;
		this.T_CCID = T_CCID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getS_CCID() {
		return this.S_CCID;
	}

	public void setS_CCID(String S_CCID) {
		this.S_CCID = S_CCID;
	}

	public String getT_CCID() {
		return this.T_CCID;
	}

	public void setT_CCID(String T_CCID) {
		this.T_CCID = T_CCID;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String toString() {
		return new ToStringBuilder(this).append("S_CCID", getS_CCID()).append(
				"T_CCID", getT_CCID()).toString();
	}

}
