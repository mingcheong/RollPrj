package mappingfiles.sysdb;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EleEnterprise implements Serializable {

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

	/** nullable persistent field */
	private String RG_CODE;

	/** nullable persistent field */
	private String MAIN_CODE;

	/** nullable persistent field */
	private String UNION_CODE;

	/** nullable persistent field */
	private String EN_PROPERTY;

	/** nullable persistent field */
	private String SORT_PROPERTY;

	/** nullable persistent field */
	private String EN_CHARGE;

	/** nullable persistent field */
	private String FINANCE_CHARGE;

	/** nullable persistent field */
	private String CLERK;

	/** nullable persistent field */
	private String DISTRICT_NUMBER;

	/** nullable persistent field */
	private String TELEPHONE;

	/** nullable persistent field */
	private String EXTENSION_NUMBER;

	/** nullable persistent field */
	private String ADDRESS;

	/** nullable persistent field */
	private String MANAGE_LEVEL;

	/** nullable persistent field */
	private String RELATION;

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
	private String CHR_CODE6;

	/** nullable persistent field */
	private String CHR_CODE7;

	/** nullable persistent field */
	private String CHR_CODE8;

	/** nullable persistent field */
	private String CHR_CODE9;

	/** nullable persistent field */
	private String CHR_ID6;

	/** nullable persistent field */
	private String CHR_ID7;

	/** nullable persistent field */
	private String CHR_ID8;

	/** nullable persistent field */
	private String CHR_ID9;

	/** full constructor */
	public EleEnterprise(String CHR_ID, int SET_YEAR, String CHR_CODE,
			String DISP_CODE, String CHR_NAME, int LEVEL_NUM, int IS_LEAF,
			int ENABLED, String CREATE_DATE, String CREATE_USER,
			String LATEST_OP_DATE, BigDecimal IS_DELETED,
			String LATEST_OP_USER, String LAST_VER, String CHR_CODE1,
			String CHR_CODE2, String CHR_CODE3, String CHR_CODE4,
			String CHR_CODE5, String RG_CODE, String MAIN_CODE,
			String UNION_CODE, String EN_PROPERTY, String SORT_PROPERTY,
			String EN_CHARGE, String FINANCE_CHARGE, String CLERK,
			String DISTRICT_NUMBER, String TELEPHONE, String EXTENSION_NUMBER,
			String ADDRESS, String MANAGE_LEVEL, String RELATION,
			String PARENT_ID, String CHR_ID1, String CHR_ID2, String CHR_ID3,
			String CHR_ID4, String CHR_ID5, String CHR_CODE6, String CHR_CODE7,
			String CHR_CODE8, String CHR_CODE9, String CHR_ID6, String CHR_ID7,
			String CHR_ID8, String CHR_ID9) {
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
		this.MAIN_CODE = MAIN_CODE;
		this.UNION_CODE = UNION_CODE;
		this.EN_PROPERTY = EN_PROPERTY;
		this.SORT_PROPERTY = SORT_PROPERTY;
		this.EN_CHARGE = EN_CHARGE;
		this.FINANCE_CHARGE = FINANCE_CHARGE;
		this.CLERK = CLERK;
		this.DISTRICT_NUMBER = DISTRICT_NUMBER;
		this.TELEPHONE = TELEPHONE;
		this.EXTENSION_NUMBER = EXTENSION_NUMBER;
		this.ADDRESS = ADDRESS;
		this.MANAGE_LEVEL = MANAGE_LEVEL;
		this.RELATION = RELATION;
		this.PARENT_ID = PARENT_ID;
		this.CHR_ID1 = CHR_ID1;
		this.CHR_ID2 = CHR_ID2;
		this.CHR_ID3 = CHR_ID3;
		this.CHR_ID4 = CHR_ID4;
		this.CHR_ID5 = CHR_ID5;
		this.CHR_CODE6 = CHR_CODE6;
		this.CHR_CODE7 = CHR_CODE7;
		this.CHR_CODE8 = CHR_CODE8;
		this.CHR_CODE9 = CHR_CODE9;
		this.CHR_ID6 = CHR_ID6;
		this.CHR_ID7 = CHR_ID7;
		this.CHR_ID8 = CHR_ID8;
		this.CHR_ID9 = CHR_ID9;
	}

	/** default constructor */
	public EleEnterprise() {
	}

	/** minimal constructor */
	public EleEnterprise(String CHR_ID, int SET_YEAR, String DISP_CODE,
			String CHR_NAME, int LEVEL_NUM, int IS_LEAF, int ENABLED,
			String LATEST_OP_DATE, BigDecimal IS_DELETED) {
		this.CHR_ID = CHR_ID;
		this.SET_YEAR = SET_YEAR;
		this.DISP_CODE = DISP_CODE;
		this.CHR_NAME = CHR_NAME;
		this.LEVEL_NUM = LEVEL_NUM;
		this.IS_LEAF = IS_LEAF;
		this.ENABLED = ENABLED;
		this.LATEST_OP_DATE = LATEST_OP_DATE;
		this.IS_DELETED = IS_DELETED;
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

	public String getMAIN_CODE() {
		return this.MAIN_CODE;
	}

	public void setMAIN_CODE(String MAIN_CODE) {
		this.MAIN_CODE = MAIN_CODE;
	}

	public String getUNION_CODE() {
		return this.UNION_CODE;
	}

	public void setUNION_CODE(String UNION_CODE) {
		this.UNION_CODE = UNION_CODE;
	}

	public String getEN_PROPERTY() {
		return this.EN_PROPERTY;
	}

	public void setEN_PROPERTY(String EN_PROPERTY) {
		this.EN_PROPERTY = EN_PROPERTY;
	}

	public String getSORT_PROPERTY() {
		return this.SORT_PROPERTY;
	}

	public void setSORT_PROPERTY(String SORT_PROPERTY) {
		this.SORT_PROPERTY = SORT_PROPERTY;
	}

	public String getEN_CHARGE() {
		return this.EN_CHARGE;
	}

	public void setEN_CHARGE(String EN_CHARGE) {
		this.EN_CHARGE = EN_CHARGE;
	}

	public String getFINANCE_CHARGE() {
		return this.FINANCE_CHARGE;
	}

	public void setFINANCE_CHARGE(String FINANCE_CHARGE) {
		this.FINANCE_CHARGE = FINANCE_CHARGE;
	}

	public String getCLERK() {
		return this.CLERK;
	}

	public void setCLERK(String CLERK) {
		this.CLERK = CLERK;
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

	public String getMANAGE_LEVEL() {
		return this.MANAGE_LEVEL;
	}

	public void setMANAGE_LEVEL(String MANAGE_LEVEL) {
		this.MANAGE_LEVEL = MANAGE_LEVEL;
	}

	public String getRELATION() {
		return this.RELATION;
	}

	public void setRELATION(String RELATION) {
		this.RELATION = RELATION;
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

	public String getCHR_CODE6() {
		return this.CHR_CODE6;
	}

	public void setCHR_CODE6(String CHR_CODE6) {
		this.CHR_CODE6 = CHR_CODE6;
	}

	public String getCHR_CODE7() {
		return this.CHR_CODE7;
	}

	public void setCHR_CODE7(String CHR_CODE7) {
		this.CHR_CODE7 = CHR_CODE7;
	}

	public String getCHR_CODE8() {
		return this.CHR_CODE8;
	}

	public void setCHR_CODE8(String CHR_CODE8) {
		this.CHR_CODE8 = CHR_CODE8;
	}

	public String getCHR_CODE9() {
		return this.CHR_CODE9;
	}

	public void setCHR_CODE9(String CHR_CODE9) {
		this.CHR_CODE9 = CHR_CODE9;
	}

	public String getCHR_ID6() {
		return this.CHR_ID6;
	}

	public void setCHR_ID6(String CHR_ID6) {
		this.CHR_ID6 = CHR_ID6;
	}

	public String getCHR_ID7() {
		return this.CHR_ID7;
	}

	public void setCHR_ID7(String CHR_ID7) {
		this.CHR_ID7 = CHR_ID7;
	}

	public String getCHR_ID8() {
		return this.CHR_ID8;
	}

	public void setCHR_ID8(String CHR_ID8) {
		this.CHR_ID8 = CHR_ID8;
	}

	public String getCHR_ID9() {
		return this.CHR_ID9;
	}

	public void setCHR_ID9(String CHR_ID9) {
		this.CHR_ID9 = CHR_ID9;
	}

	public String toString() {
		return new ToStringBuilder(this).append("CHR_ID", getCHR_ID())
				.toString();
	}

}
