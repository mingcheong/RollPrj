package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRightGroupType implements Serializable {

	/** identifier field */
	private String RIGHT_GROUP_ID;

	/** identifier field */
	private String ELE_CODE;

	/** nullable persistent field */
	private Integer RIGHT_TYPE;

	/** nullable persistent field */
	private Integer SET_YEAR;

	/** full constructor */
	public SysRightGroupType(String RIGHT_GROUP_ID, String ELE_CODE,
			Integer RIGHT_TYPE, Integer SET_YEAR) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
		this.ELE_CODE = ELE_CODE;
		this.RIGHT_TYPE = RIGHT_TYPE;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public SysRightGroupType() {
	}

	/** minimal constructor */
	public SysRightGroupType(String RIGHT_GROUP_ID, String ELE_CODE) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
		this.ELE_CODE = ELE_CODE;
	}

	public String getRIGHT_GROUP_ID() {
		return this.RIGHT_GROUP_ID;
	}

	public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
	}

	public String getELE_CODE() {
		return this.ELE_CODE;
	}

	public void setELE_CODE(String ELE_CODE) {
		this.ELE_CODE = ELE_CODE;
	}

	public Integer getRIGHT_TYPE() {
		return this.RIGHT_TYPE;
	}

	public void setRIGHT_TYPE(Integer RIGHT_TYPE) {
		this.RIGHT_TYPE = RIGHT_TYPE;
	}

	public Integer getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(Integer SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String toString() {
		return new ToStringBuilder(this).append("RIGHT_GROUP_ID",
				getRIGHT_GROUP_ID()).append("ELE_CODE", getELE_CODE())
				.toString();
	}

}
