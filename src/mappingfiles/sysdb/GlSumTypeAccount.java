package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlSumTypeAccount implements Serializable {

	/** identifier field */
	private String SUM_ACCOUNT_ID;

	/** persistent field */
	private String SUM_TYPE_ID;

	/** persistent field */
	private String AS_ID;

	/** persistent field */
	private int SUM_SYMBOL;

	/** persistent field */
	private int VALUE_TYPE;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlSumTypeAccount(String SUM_ACCOUNT_ID, String SUM_TYPE_ID,
			String AS_ID, int SUM_SYMBOL, int VALUE_TYPE, int SET_YEAR) {
		this.SUM_ACCOUNT_ID = SUM_ACCOUNT_ID;
		this.SUM_TYPE_ID = SUM_TYPE_ID;
		this.AS_ID = AS_ID;
		this.SUM_SYMBOL = SUM_SYMBOL;
		this.VALUE_TYPE = VALUE_TYPE;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlSumTypeAccount() {
	}

	public String getSUM_ACCOUNT_ID() {
		return this.SUM_ACCOUNT_ID;
	}

	public void setSUM_ACCOUNT_ID(String SUM_ACCOUNT_ID) {
		this.SUM_ACCOUNT_ID = SUM_ACCOUNT_ID;
	}

	public String getSUM_TYPE_ID() {
		return this.SUM_TYPE_ID;
	}

	public void setSUM_TYPE_ID(String SUM_TYPE_ID) {
		this.SUM_TYPE_ID = SUM_TYPE_ID;
	}

	public String getAS_ID() {
		return this.AS_ID;
	}

	public void setAS_ID(String AS_ID) {
		this.AS_ID = AS_ID;
	}

	public int getSUM_SYMBOL() {
		return this.SUM_SYMBOL;
	}

	public void setSUM_SYMBOL(int SUM_SYMBOL) {
		this.SUM_SYMBOL = SUM_SYMBOL;
	}

	public int getVALUE_TYPE() {
		return this.VALUE_TYPE;
	}

	public void setVALUE_TYPE(int VALUE_TYPE) {
		this.VALUE_TYPE = VALUE_TYPE;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String toString() {
		return new ToStringBuilder(this).append("SUM_ACCOUNT_ID",
				getSUM_ACCOUNT_ID()).toString();
	}

}
