package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysSynRightType implements Serializable {

	/** identifier field */
	private String RIGHT_ID;

	/** nullable persistent field */
	private String ROLE_ID;

	/** nullable persistent field */
	private String ELE_CODE;

	/** nullable persistent field */
	private Integer RIGHT_TYPE;

	/** nullable persistent field */
	private Integer SET_YEAR;

	/** nullable persistent field */
	private String LAST_VER;

	/** full constructor */
	public SysSynRightType(String RIGHT_ID, String ROLE_ID, String ELE_CODE,
			Integer RIGHT_TYPE, Integer SET_YEAR, String LAST_VER) {
		this.RIGHT_ID = RIGHT_ID;
		this.ROLE_ID = ROLE_ID;
		this.ELE_CODE = ELE_CODE;
		this.RIGHT_TYPE = RIGHT_TYPE;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	/** default constructor */
	public SysSynRightType() {
	}

	/** minimal constructor */
	public SysSynRightType(String RIGHT_ID) {
		this.RIGHT_ID = RIGHT_ID;
	}

	public String getRIGHT_ID() {
		return this.RIGHT_ID;
	}

	public void setRIGHT_ID(String RIGHT_ID) {
		this.RIGHT_ID = RIGHT_ID;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
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
