package mappingfiles.sysdb;

import java.io.Serializable;

public class ExcDataType implements Serializable {

	private static final long serialVersionUID = 3034121458183650944L;
	private String DATA_TYPE_CODE;
	private String DATA_TYPE_NAME;
	private String SENDER_SYSTEM_CODE;
	private int SET_YEAR;
	private String LAST_VER;
	private Integer ENABLED;
	private String COMPRESSCODE;
	private String ENCRPTCODE;
	private int EXCHANGE_TYPE;

	public ExcDataType(String DATA_TYPE_CODE, String DATA_TYPE_NAME,
			String SENDER_SYSTEM_CODE, Integer ENABLED) {
		this.DATA_TYPE_CODE = DATA_TYPE_CODE;
		this.DATA_TYPE_NAME = DATA_TYPE_NAME;
		this.SENDER_SYSTEM_CODE = SENDER_SYSTEM_CODE;
		this.ENABLED = ENABLED;
	}

	public ExcDataType() {
	}

	public ExcDataType(String DATA_TYPE_CODE, Integer ENABLED) {
		this.DATA_TYPE_CODE = DATA_TYPE_CODE;
		this.ENABLED = ENABLED;
	}

	public String getDATA_TYPE_CODE() {
		return DATA_TYPE_CODE;
	}

	public void setDATA_TYPE_CODE(String data_type_code) {
		DATA_TYPE_CODE = data_type_code;
	}

	public String getDATA_TYPE_NAME() {
		return DATA_TYPE_NAME;
	}

	public void setDATA_TYPE_NAME(String data_type_name) {
		DATA_TYPE_NAME = data_type_name;
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

	public String getSENDER_SYSTEM_CODE() {
		return SENDER_SYSTEM_CODE;
	}

	public void setSENDER_SYSTEM_CODE(String sender_system_code) {
		SENDER_SYSTEM_CODE = sender_system_code;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int set_year) {
		SET_YEAR = set_year;
	}

	public String getCOMPRESSCODE() {
		return COMPRESSCODE;
	}

	public void setCOMPRESSCODE(String compresscode) {
		COMPRESSCODE = compresscode;
	}

	public String getENCRPTCODE() {
		return ENCRPTCODE;
	}

	public void setENCRPTCODE(String encrptcode) {
		ENCRPTCODE = encrptcode;
	}

	public int getEXCHANGE_TYPE() {
		return EXCHANGE_TYPE;
	}

	public void setEXCHANGE_TYPE(int exchange_type) {
		EXCHANGE_TYPE = exchange_type;
	}

}
