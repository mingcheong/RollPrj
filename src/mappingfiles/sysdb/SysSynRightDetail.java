package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysSynRightDetail implements Serializable {

	/** identifier field */
	private String RIGHT_ID;

	/** nullable persistent field */
	private String RIGHT_DETAIL_ID;

	/** nullable persistent field */
	private String ELE_VALUE;

	/** nullable persistent field */
	private Integer SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysSynRightDetail(String RIGHT_ID, String RIGHT_DETAIL_ID,
			String ELE_VALUE, Integer SET_YEAR, String LAST_VER) {
		this.RIGHT_ID = RIGHT_ID;
		this.RIGHT_DETAIL_ID = RIGHT_DETAIL_ID;
		this.ELE_VALUE = ELE_VALUE;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysSynRightDetail() {
	}

	/** minimal constructor */
	public SysSynRightDetail(String RIGHT_ID) {
		this.RIGHT_ID = RIGHT_ID;
	}

	public String getRIGHT_ID() {
		return this.RIGHT_ID;
	}

	public void setRIGHT_ID(String RIGHT_ID) {
		this.RIGHT_ID = RIGHT_ID;
	}

	public String getRIGHT_DETAIL_ID() {
		return this.RIGHT_DETAIL_ID;
	}

	public void setRIGHT_DETAIL_ID(String RIGHT_DETAIL_ID) {
		this.RIGHT_DETAIL_ID = RIGHT_DETAIL_ID;
	}

	public String getELE_VALUE() {
		return this.ELE_VALUE;
	}

	public void setELE_VALUE(String ELE_VALUE) {
		this.ELE_VALUE = ELE_VALUE;
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String toString() {
		return new ToStringBuilder(this).append("RIGHT_ID", getRIGHT_ID())
				.toString();
	}

}
