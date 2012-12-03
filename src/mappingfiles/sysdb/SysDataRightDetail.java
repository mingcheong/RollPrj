package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysDataRightDetail implements Serializable {

	/** identifier field */
	private String RIGHT_DETAIL_ID;

	/** nullable persistent field */
	private String RIGHT_ID;

	/** nullable persistent field */
	private String ELEMENT_VALUE;

	/** persistent field */
	private int SET_YEAR;

	/** full constructor */
	public SysDataRightDetail(String RIGHT_DETAIL_ID, String RIGHT_ID,
			String ELEMENT_VALUE, int SET_YEAR) {
		this.RIGHT_DETAIL_ID = RIGHT_DETAIL_ID;
		this.RIGHT_ID = RIGHT_ID;
		this.ELEMENT_VALUE = ELEMENT_VALUE;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public SysDataRightDetail() {
	}

	/** minimal constructor */
	public SysDataRightDetail(String RIGHT_DETAIL_ID, int SET_YEAR) {
		this.RIGHT_DETAIL_ID = RIGHT_DETAIL_ID;
		this.SET_YEAR = SET_YEAR;
	}

	public String getRIGHT_DETAIL_ID() {
		return this.RIGHT_DETAIL_ID;
	}

	public void setRIGHT_DETAIL_ID(String RIGHT_DETAIL_ID) {
		this.RIGHT_DETAIL_ID = RIGHT_DETAIL_ID;
	}

	public String getRIGHT_ID() {
		return this.RIGHT_ID;
	}

	public void setRIGHT_ID(String RIGHT_ID) {
		this.RIGHT_ID = RIGHT_ID;
	}

	public String getELEMENT_VALUE() {
		return this.ELEMENT_VALUE;
	}

	public void setELEMENT_VALUE(String ELEMENT_VALUE) {
		this.ELEMENT_VALUE = ELEMENT_VALUE;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String toString() {
		return new ToStringBuilder(this).append("RIGHT_DETAIL_ID",
				getRIGHT_DETAIL_ID()).toString();
	}

}
