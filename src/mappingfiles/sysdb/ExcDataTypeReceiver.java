package mappingfiles.sysdb;

import java.io.Serializable;

public class ExcDataTypeReceiver implements Serializable {
	private String DATA_TYPE_CODE;
	private String RECEIVER_SYSTEM_CODE;
	private Integer ENABLED;
	private int SET_YEAR;
	private String LAST_VER;

	public ExcDataTypeReceiver() {
	}

	public ExcDataTypeReceiver(String DATA_TYPE_CODE,
			String RECEIVER_SYSTEM_CODE, Integer ENABLED, int SET_YEAR,
			String LAST_VER) {
		this.DATA_TYPE_CODE = DATA_TYPE_CODE;
		this.RECEIVER_SYSTEM_CODE = RECEIVER_SYSTEM_CODE;
		this.ENABLED = ENABLED;
		this.SET_YEAR = SET_YEAR;
		this.LAST_VER = LAST_VER;
	}

	public ExcDataTypeReceiver(String DATA_TYPE_CODE,
			String RECEIVER_SYSTEM_CODE) {
		this.DATA_TYPE_CODE = DATA_TYPE_CODE;
		this.RECEIVER_SYSTEM_CODE = RECEIVER_SYSTEM_CODE;
	}

	public String getDATA_TYPE_CODE() {
		return DATA_TYPE_CODE;
	}

	public void setDATA_TYPE_CODE(String data_type_code) {
		DATA_TYPE_CODE = data_type_code;
	}

	public Integer getENABLED() {
		return ENABLED;
	}

	public void setENABLED(Integer enabled) {
		ENABLED = enabled;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public String getRECEIVER_SYSTEM_CODE() {
		return RECEIVER_SYSTEM_CODE;
	}

	public void setRECEIVER_SYSTEM_CODE(String receiver_system_code) {
		RECEIVER_SYSTEM_CODE = receiver_system_code;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int set_year) {
		SET_YEAR = set_year;
	}
}
