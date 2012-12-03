package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysRightGroupDetail implements Serializable {

	private static final long serialVersionUID = -7315711989174794482L;

	/** identifier field */
	private String RIGHT_GROUP_ID;

	/** identifier field */
	private String ELE_CODE;

	private String ELE_NAME;

	/** identifier field */
	private String ELE_VALUE;

	/** identifier field */
	private Integer SET_YEAR;

	/** full constructor */
	public SysRightGroupDetail(String RIGHT_GROUP_ID, String ELE_CODE,
			String ELE_VALUE, Integer SET_YEAR, String ELE_NAME) {
		this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
		this.ELE_CODE = ELE_CODE;
		this.ELE_NAME = ELE_NAME;
		this.ELE_VALUE = ELE_VALUE;
		this.SET_YEAR = SET_YEAR;
	}

	/** default constructor */
	public SysRightGroupDetail() {
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

	public String toString() {
		return new ToStringBuilder(this).append("RIGHT_GROUP_ID",
				getRIGHT_GROUP_ID()).append("ELE_CODE", getELE_CODE()).append(
				"ELE_VALUE", getELE_VALUE()).append("SET_YEAR", getSET_YEAR())
				.append("ELE_NAME", getELE_NAME()).toString();
	}

	public String getELE_NAME() {
		return ELE_NAME;
	}

	public void setELE_NAME(String ele_name) {
		ELE_NAME = ele_name;
	}

}
