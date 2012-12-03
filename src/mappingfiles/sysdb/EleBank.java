package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EleBank implements Serializable {

	/** identifier field */
	private String CHR_ID;

	/** persistent field */
	private int SET_YEAR;

	/** nullable persistent field */
	private String CHR_CODE;

	/** persistent field */
	private String DISP_CODE;

	/** persistent field */
	private String CHR_NAME;

	/** persistent field */
	private int LEVEL_NUM;

	/** persistent field */
	private int IS_LEAF;

	/** persistent field */
	private int ENABLED;

	/** nullable persistent field */
	private String CREATE_DATE;

	/** nullable persistent field */
	private String CREATE_USER;

	/** persistent field */
	private String LATEST_OP_DATE;

	/** persistent field */
	private BigDecimal IS_DELETED;

	/** nullable persistent field */
	private String LATEST_OP_USER;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String CHR_CODE1;

	/** nullable persistent field */
	private String CHR_CODE2;

	/** nullable persistent field */
	private String CHR_CODE3;

	/** nullable persistent field */
	private String CHR_CODE4;

	/** nullable persistent field */
	private String CHR_CODE5;

	/** persistent field */
	private String RG_CODE;

	/** nullable persistent field */
	private String BANK_CHARGE;

	/** nullable persistent field */
	private String FINANCE_CHARGE;

	/** nullable persistent field */
	private String DISTRICT_NUMBER;

	/** nullable persistent field */
	private String TELEPHONE;

	/** nullable persistent field */
	private String EXTENSION_NUMBER;

	/** nullable persistent field */
	private String ADDRESS;

	/** nullable persistent field */
	private String PARENT_ID;

	/** nullable persistent field */
	private String CHR_ID1;

	/** nullable persistent field */
	private String CHR_ID2;

	/** nullable persistent field */
	private String CHR_ID3;

	/** nullable persistent field */
	private String CHR_ID4;

	/** nullable persistent field */
	private String CHR_ID5;

	/** nullable persistent field */
	private Integer AGENTFLAG;

	/** nullable persistent field */
	private Integer CLEARFLAG;

	/** nullable persistent field */
	private Integer INCOMEFLAG;

	/** full constructor */
	public EleBank(String CHR_ID, int SET_YEAR, String CHR_CODE,
			String DISP_CODE, String CHR_NAME, int LEVEL_NUM, int IS_LEAF,
			int ENABLED, String CREATE_DATE, String CREATE_USER,
			String LATEST_OP_DATE, BigDecimal IS_DELETED,
			String LATEST_OP_USER, String LAST_VER, String CHR_CODE1,
			String CHR_CODE2, String CHR_CODE3, String CHR_CODE4,
			String CHR_CODE5, String RG_CODE, String BANK_CHARGE,
			String FINANCE_CHARGE, String DISTRICT_NUMBER, String TELEPHONE,
			String EXTENSION_NUMBER, String ADDRESS, String PARENT_ID,
			String CHR_ID1, String CHR_ID2, String CHR_ID3, String CHR_ID4,
			String CHR_ID5, Integer AGENTFLAG, Integer CLEARFLAG,
			Integer INCOMEFLAG) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.CHR_CODE = CHR_CODE;
		this.DISP_CODE = DISP_CODE;
		this.CHR_NAME = CHR_NAME;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.ENABLED = ENABLED;
		this.CREATE_DATE = CREATE_DATE;
		this.CREATE_USER = CREATE_USER;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
		this.LATEST_OP_USER = LATEST_OP_USER;
		this.LAST_VER = LAST_VER;
		this.CHR_CODE1 = CHR_CODE1;
		this.CHR_CODE2 = CHR_CODE2;
		this.CHR_CODE3 = CHR_CODE3;
		this.CHR_CODE4 = CHR_CODE4;
		this.CHR_CODE5 = CHR_CODE5;
		this.RG_CODE = RG_CODE;
		this.BANK_CHARGE = BANK_CHARGE;
		this.FINANCE_CHARGE = FINANCE_CHARGE;
		this.DISTRICT_NUMBER = DISTRICT_NUMBER;
		this.TELEPHONE = TELEPHONE;
		this.EXTENSION_NUMBER = EXTENSION_NUMBER;
		this.ADDRESS = ADDRESS;
		this.PARENT_ID = PARENT_ID;
		this.CHR_ID1 = CHR_ID1;
		this.CHR_ID2 = CHR_ID2;
		this.CHR_ID3 = CHR_ID3;
		this.CHR_ID4 = CHR_ID4;
		this.CHR_ID5 = CHR_ID5;
		this.AGENTFLAG = AGENTFLAG;
		this.CLEARFLAG = CLEARFLAG;
		this.INCOMEFLAG = INCOMEFLAG;
	}

	/** default constructor */
	public EleBank() {
	}

	/** minimal constructor */
	public EleBank(String CHR_ID, int SET_YEAR, String DISP_CODE,
			String CHR_NAME, int LEVEL_NUM, int IS_LEAF, int ENABLED,
			String LATEST_OP_DATE, BigDecimal IS_DELETED, String RG_CODE) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.DISP_CODE = DISP_CODE;
		this.CHR_NAME = CHR_NAME;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.ENABLED = ENABLED;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
		this.RG_CODE = RG_CODE;
	}

	public String getCHR_ID() {
		return this.CHR_ID;
	}

	public void setCHR_ID(String CHR_ID) {
		this.CHR_ID = CHR_ID;
	}

	public int getSET_YEAR() {
		return this.SET_YEAR;
	}

	public void setSET_YEAR(int SET_YEAR) {
		this.SET_YEAR = SET_YEAR;
	}

	public String getCHR_CODE() {
		return this.CHR_CODE;
	}

	public void setCHR_CODE(String CHR_CODE) {
		this.CHR_CODE = CHR_CODE;
	}

	public String getDISP_CODE() {
		return this.DISP_CODE;
	}

	public void setDISP_CODE(String DISP_CODE) {
		this.DISP_CODE = DISP_CODE;
	}

	public String getCHR_NAME() {
		return this.CHR_NAME;
	}

	public void setCHR_NAME(String CHR_NAME) {
		this.CHR_NAME = CHR_NAME;
	}

	public int getLEVEL_NUM() {
		return this.LEVEL_NUM;
	}

	public void setLEVEL_NUM(int LEVEL_NUM) {
		this.LEVEL_NUM = LEVEL_NUM;
	}

	public int getIS_LEAF() {
		return this.IS_LEAF;
	}

	public void setIS_LEAF(int IS_LEAF) {
		this.IS_LEAF = IS_LEAF;
	}

	public int getENABLED() {
		return this.ENABLED;
	}

	public void setENABLED(int ENABLED) {
		this.ENABLED = ENABLED;
	}

	public String getCREATE_DATE() {
		return this.CREATE_DATE;
	}

	public void setCREATE_DATE(String CREATE_DATE) {
		this.CREATE_DATE = CREATE_DATE;
	}

	public String getCREATE_USER() {
		return this.CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public String getLATEST_OP_DATE() {
		return this.LATEST_OP_DATE;
	}

	public void setLATEST_OP_DATE(String LATEST_OP_DATE) {
		this.LATEST_OP_DATE = LATEST_OP_DATE;
	}

	public BigDecimal getIS_DELETED() {
		return this.IS_DELETED;
	}

	public void setIS_DELETED(BigDecimal IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
	}

	public String getLATEST_OP_USER() {
		return this.LATEST_OP_USER;
	}

	public void setLATEST_OP_USER(String LATEST_OP_USER) {
		this.LATEST_OP_USER = LATEST_OP_USER;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String getCHR_CODE1() {
		return this.CHR_CODE1;
	}

	public void setCHR_CODE1(String CHR_CODE1) {
		this.CHR_CODE1 = CHR_CODE1;
	}

	public String getCHR_CODE2() {
		return this.CHR_CODE2;
	}

	public void setCHR_CODE2(String CHR_CODE2) {
		this.CHR_CODE2 = CHR_CODE2;
	}

	public String getCHR_CODE3() {
		return this.CHR_CODE3;
	}

	public void setCHR_CODE3(String CHR_CODE3) {
		this.CHR_CODE3 = CHR_CODE3;
	}

	public String getCHR_CODE4() {
		return this.CHR_CODE4;
	}

	public void setCHR_CODE4(String CHR_CODE4) {
		this.CHR_CODE4 = CHR_CODE4;
	}

	public String getCHR_CODE5() {
		return this.CHR_CODE5;
	}

	public void setCHR_CODE5(String CHR_CODE5) {
		this.CHR_CODE5 = CHR_CODE5;
	}

	public String getRG_CODE() {
		return this.RG_CODE;
	}

	public void setRG_CODE(String RG_CODE) {
		this.RG_CODE = RG_CODE;
	}

	public String getBANK_CHARGE() {
		return this.BANK_CHARGE;
	}

	public void setBANK_CHARGE(String BANK_CHARGE) {
		this.BANK_CHARGE = BANK_CHARGE;
	}

	public String getFINANCE_CHARGE() {
		return this.FINANCE_CHARGE;
	}

	public void setFINANCE_CHARGE(String FINANCE_CHARGE) {
		this.FINANCE_CHARGE = FINANCE_CHARGE;
	}

	public String getDISTRICT_NUMBER() {
		return this.DISTRICT_NUMBER;
	}

	public void setDISTRICT_NUMBER(String DISTRICT_NUMBER) {
		this.DISTRICT_NUMBER = DISTRICT_NUMBER;
	}

	public String getTELEPHONE() {
		return this.TELEPHONE;
	}

	public void setTELEPHONE(String TELEPHONE) {
		this.TELEPHONE = TELEPHONE;
	}

	public String getEXTENSION_NUMBER() {
		return this.EXTENSION_NUMBER;
	}

	public void setEXTENSION_NUMBER(String EXTENSION_NUMBER) {
		this.EXTENSION_NUMBER = EXTENSION_NUMBER;
	}

	public String getADDRESS() {
		return this.ADDRESS;
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS = ADDRESS;
	}

	public String getPARENT_ID() {
		return this.PARENT_ID;
	}

	public void setPARENT_ID(String PARENT_ID) {
		this.PARENT_ID = PARENT_ID;
	}

	public String getCHR_ID1() {
		return this.CHR_ID1;
	}

	public void setCHR_ID1(String CHR_ID1) {
		this.CHR_ID1 = CHR_ID1;
	}

	public String getCHR_ID2() {
		return this.CHR_ID2;
	}

	public void setCHR_ID2(String CHR_ID2) {
		this.CHR_ID2 = CHR_ID2;
	}

	public String getCHR_ID3() {
		return this.CHR_ID3;
	}

	public void setCHR_ID3(String CHR_ID3) {
		this.CHR_ID3 = CHR_ID3;
	}

	public String getCHR_ID4() {
		return this.CHR_ID4;
	}

	public void setCHR_ID4(String CHR_ID4) {
		this.CHR_ID4 = CHR_ID4;
	}

	public String getCHR_ID5() {
		return this.CHR_ID5;
	}

	public void setCHR_ID5(String CHR_ID5) {
		this.CHR_ID5 = CHR_ID5;
	}

	public Integer getAGENTFLAG() {
		return this.AGENTFLAG;
	}

	public void setAGENTFLAG(Integer AGENTFLAG) {
		this.AGENTFLAG = AGENTFLAG;
	}

	public Integer getCLEARFLAG() {
		return this.CLEARFLAG;
	}

	public void setCLEARFLAG(Integer CLEARFLAG) {
		this.CLEARFLAG = CLEARFLAG;
	}

	public Integer getINCOMEFLAG() {
		return this.INCOMEFLAG;
	}

	public void setINCOMEFLAG(Integer INCOMEFLAG) {
		this.INCOMEFLAG = INCOMEFLAG;
	}

	public String toString() {
		return new ToStringBuilder(this).append("CHR_ID", getCHR_ID())
				.toString();
	}

}
