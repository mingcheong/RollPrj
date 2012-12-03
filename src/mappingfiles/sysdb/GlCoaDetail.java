package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlCoaDetail implements Serializable {

	/** identifier field */
	private String COA_DETAIL_ID;

	/** persistent field */
	private String COA_ID;

	/** persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private Long DISP_ORDER;

	/** nullable persistent field */
	private Integer LEVEL_NUM;

	/** nullable persistent field */
	private String LAST_VER;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public GlCoaDetail(String COA_DETAIL_ID, String COA_ID, String ELE_CODE,
			Long DISP_ORDER, Integer LEVEL_NUM, String LAST_VER, int SET_YEAR) {
		this.COA_DETAIL_ID = COA_DETAIL_ID;
		this.COA_ID = COA_ID;
		this.ELE_CODE = ELE_CODE;
		this.DISP_ORDER = DISP_ORDER;
		this.LEVEL_NUM = LEVEL_NUM;
		this.LAST_VER = LAST_VER;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public GlCoaDetail() {
	}

	/** minimal constructor */
	public GlCoaDetail(String COA_DETAIL_ID, String COA_ID, String ELE_CODE,
			int SET_YEAR) {
		this.COA_DETAIL_ID = COA_DETAIL_ID;
		this.COA_ID = COA_ID;
		this.ELE_CODE = ELE_CODE;
		this.SET_YEAR = SET_YEAR;
	}

	public String getCOA_DETAIL_ID() {
		return this.COA_DETAIL_ID;
	}

	public void setCOA_DETAIL_ID(String COA_DETAIL_ID) {
		this.COA_DETAIL_ID = COA_DETAIL_ID;
	}

	public String getCOA_ID() {
		return this.COA_ID;
	}

	public void setCOA_ID(String COA_ID) {
		this.COA_ID = COA_ID;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
	}

	public Long getDISP_ORDER() {
		return this.DISP_ORDER;
	}

	public void setDISP_ORDER(Long DISP_ORDER) {
		this.DISP_ORDER = DISP_ORDER;
	}

	public Integer getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(Integer LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
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
		return new ToStringBuilder(this).append("COA_DETAIL_ID",
				getCOA_DETAIL_ID()).toString();
	}

}
