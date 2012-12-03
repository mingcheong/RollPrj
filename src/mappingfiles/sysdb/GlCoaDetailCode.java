package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlCoaDetailCode implements Serializable {

	/** identifier field */
	private String COA_CODE_ID;

	/** persistent field */
	private String COA_DETAIL_ID;

	/** nullable persistent field */
	private String LEVEL_CODE;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlCoaDetailCode(String COA_CODE_ID, String COA_DETAIL_ID,
			String LEVEL_CODE, String LAST_VER, int SET_YEAR) {
		this.COA_CODE_ID = COA_CODE_ID;
		this.COA_DETAIL_ID = COA_DETAIL_ID;
		this.LEVEL_CODE = LEVEL_CODE;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlCoaDetailCode() {
	}

	/** minimal constructor */
	public GlCoaDetailCode(String COA_CODE_ID, String COA_DETAIL_ID,
			int SET_YEAR) {
		this.COA_CODE_ID = COA_CODE_ID;
		this.COA_DETAIL_ID = COA_DETAIL_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getCOA_CODE_ID() {
		return this.COA_CODE_ID;
	}

	public void setCOA_CODE_ID(String COA_CODE_ID) {
		this.COA_CODE_ID = COA_CODE_ID;
	}

	public String getCOA_DETAIL_ID() {
		return this.COA_DETAIL_ID;
	}

	public void setCOA_DETAIL_ID(String COA_DETAIL_ID) {
		this.COA_DETAIL_ID = COA_DETAIL_ID;
	}

	public String getLEVEL_CODE() {
		return this.LEVEL_CODE;
	}

	public void setLEVEL_CODE(String LEVEL_CODE) {
		this.LEVEL_CODE = LEVEL_CODE;
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
		return new ToStringBuilder(this)
				.append("COA_CODE_ID", getCOA_CODE_ID()).toString();
	}

}
