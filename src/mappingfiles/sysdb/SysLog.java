package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysLog implements Serializable {

	/** identifier field */
	private String LOG_TYPE;

	/** nullable persistent field */
	private String LOG_TYPENAME;

	/** nullable persistent field */
	private Integer IS_DEFALUTLOG;

	/** nullable persistent field */
	private String SEARCHLOGCLASS;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysLog(String LOG_TYPE, String LOG_TYPENAME, Integer IS_DEFALUTLOG,
			String SEARCHLOGCLASS, String LAST_VER) {
		this.LOG_TYPE = LOG_TYPE;
		this.LOG_TYPENAME = LOG_TYPENAME;
		this.IS_DEFALUTLOG = IS_DEFALUTLOG;
		this.SEARCHLOGCLASS = SEARCHLOGCLASS;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysLog() {
	}

	/** minimal constructor */
	public SysLog(String LOG_TYPE) {
		this.LOG_TYPE = LOG_TYPE;
	}

	public String getLOG_TYPE() {
		return this.LOG_TYPE;
	}

	public void setLOG_TYPE(String LOG_TYPE) {
		this.LOG_TYPE = LOG_TYPE;
	}

	public String getLOG_TYPENAME() {
		return this.LOG_TYPENAME;
	}

	public void setLOG_TYPENAME(String LOG_TYPENAME) {
		this.LOG_TYPENAME = LOG_TYPENAME;
	}

	public Integer getIS_DEFALUTLOG() {
		return this.IS_DEFALUTLOG;
	}

	public void setIS_DEFALUTLOG(Integer IS_DEFALUTLOG) {
		this.IS_DEFALUTLOG = IS_DEFALUTLOG;
	}

	public String getSEARCHLOGCLASS() {
		return this.SEARCHLOGCLASS;
	}

	public void setSEARCHLOGCLASS(String SEARCHLOGCLASS) {
		this.SEARCHLOGCLASS = SEARCHLOGCLASS;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("LOG_TYPE", getLOG_TYPE())
				.toString();
	}

}
