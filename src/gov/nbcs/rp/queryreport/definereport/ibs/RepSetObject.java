/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ibs;


public class RepSetObject {
	// 预算年度
	private int SET_YEAR;

	// 业务编号
	private int TYPE_NO;

	// 报表ID 一报表编号规则生成
	private String REPORT_ID;

	// 报表类型
	private int REPORT_TYPE = 50;

	// 报表名称
	private String REPORT_CNAME;

	// 标题
	private String TITLE;

	// 标题区域
	private String TITLE_AREA;

	private String TITLE_FONT;

	private int TITLE_FONTSIZE;

	private String COLUMN_AREA;

	private String COLUMN_FONT;

	private int COLUMN_FONTSIZE;

	// 货币类型
	private String CURRENCYUNIT;

	// 定制/定义
	private String REPORT_SOURCE;

	// 是否通过校验
	private String IS_PASSVERIFY;

	// 是否启用
	private String IS_ACTIVE = "是";

	// 0:单位使用;1:财政使用;2:单位财政共同使用
	private int DATA_USER;

	// 源表名称
	private String OBJECT_NAMES;

	// 源表中文名称
	private String OBJECT_ENAMES;

	// 是否支持批次
	private String IS_HASBATCH;

	// 是否支持多选
	private String IS_MULTICOND;

	// 是否末级
	private int IS_END;

	// 排序码，预留，保存时和ID一致
	private String LVL_ID;

	// 区划
	private String RG_CODE;

	// 最后版本
	private String LAST_VER;

	// 报表类型 0：普通报表，1：收支总表 ，2：行报表,3：封面，
	private int TYPE_FLAG;

	// 是否四舍五入
	private int IS_MONEYOP;

	// 是否跨年份
	private int IS_MULYEAR;

	// 是否跨区域
	private int IS_MULRGION;

	private String SHOW_LVL;

	private String PAR_ID;

	// 支出更换支出资金来源

	private String FUNDSOURCE_FLAG;

	private String COMPARE_FLAG;

	private String C1;

	private String C2;

	public String getCOLUMN_AREA() {
		return COLUMN_AREA;
	}

	public void setCOLUMN_AREA(String column_area) {
		COLUMN_AREA = column_area;
	}

	public String getCOLUMN_FONT() {
		return COLUMN_FONT;
	}

	public void setCOLUMN_FONT(String column_font) {
		COLUMN_FONT = column_font;
	}

	public int getCOLUMN_FONTSIZE() {
		return COLUMN_FONTSIZE;
	}

	public void setCOLUMN_FONTSIZE(int column_fontsize) {
		COLUMN_FONTSIZE = column_fontsize;
	}

	public String getCURRENCYUNIT() {
		return CURRENCYUNIT;
	}

	public void setCURRENCYUNIT(String currencyunit) {
		CURRENCYUNIT = currencyunit;
	}

	public int getDATA_USER() {
		return DATA_USER;
	}

	public void setDATA_USER(int data_user) {
		DATA_USER = data_user;
	}

	public String getIS_ACTIVE() {
		return IS_ACTIVE;
	}

	public void setIS_ACTIVE(String is_active) {
		IS_ACTIVE = is_active;
	}

	public int getIS_END() {
		return IS_END;
	}

	public void setIS_END(int is_end) {
		IS_END = is_end;
	}

	public String getIS_HASBATCH() {
		return IS_HASBATCH;
	}

	public void setIS_HASBATCH(String is_hasbatch) {
		IS_HASBATCH = is_hasbatch;
	}

	public int getIS_MONEYOP() {
		return IS_MONEYOP;
	}

	public void setIS_MONEYOP(int is_moneyop) {
		IS_MONEYOP = is_moneyop;
	}

	public int getIS_MULRGION() {
		return IS_MULRGION;
	}

	public void setIS_MULRGION(int is_mulrgion) {
		IS_MULRGION = is_mulrgion;
	}

	public String getIS_MULTICOND() {
		return IS_MULTICOND;
	}

	public void setIS_MULTICOND(String is_multicond) {
		IS_MULTICOND = is_multicond;
	}

	public int getIS_MULYEAR() {
		return IS_MULYEAR;
	}

	public void setIS_MULYEAR(int is_mulyear) {
		IS_MULYEAR = is_mulyear;
	}

	public String getIS_PASSVERIFY() {
		return IS_PASSVERIFY;
	}

	public void setIS_PASSVERIFY(String is_passverify) {
		IS_PASSVERIFY = is_passverify;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public String getLVL_ID() {
		return LVL_ID;
	}

	public void setLVL_ID(String lvl_id) {
		LVL_ID = lvl_id;
	}

	public String getOBJECT_ENAMES() {
		return OBJECT_ENAMES;
	}

	public void setOBJECT_ENAMES(String object_enames) {
		OBJECT_ENAMES = object_enames;
	}

	public String getOBJECT_NAMES() {
		return OBJECT_NAMES;
	}

	public void setOBJECT_NAMES(String object_names) {
		OBJECT_NAMES = object_names;
	}

	public String getREPORT_CNAME() {
		return REPORT_CNAME;
	}

	public void setREPORT_CNAME(String report_cname) {
		REPORT_CNAME = report_cname;
	}

	public String getREPORT_ID() {
		return REPORT_ID;
	}

	public void setREPORT_ID(String report_id) {
		REPORT_ID = report_id;
	}

	public String getREPORT_SOURCE() {
		return REPORT_SOURCE;
	}

	public void setREPORT_SOURCE(String report_source) {
		REPORT_SOURCE = report_source;
	}

	public int getREPORT_TYPE() {
		return REPORT_TYPE;
	}

	public void setREPORT_TYPE(int report_type) {
		REPORT_TYPE = report_type;
	}

	public String getRG_CODE() {
		return RG_CODE;
	}

	public void setRG_CODE(String rg_code) {
		RG_CODE = rg_code;
	}

	public int getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(int set_year) {
		SET_YEAR = set_year;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String title) {
		TITLE = title;
	}

	public String getTITLE_AREA() {
		return TITLE_AREA;
	}

	public void setTITLE_AREA(String title_area) {
		TITLE_AREA = title_area;
	}

	public String getTITLE_FONT() {
		return TITLE_FONT;
	}

	public void setTITLE_FONT(String title_font) {
		TITLE_FONT = title_font;
	}

	public int getTITLE_FONTSIZE() {
		return TITLE_FONTSIZE;
	}

	public void setTITLE_FONTSIZE(int title_fontsize) {
		TITLE_FONTSIZE = title_fontsize;
	}

	public int getTYPE_FLAG() {
		return TYPE_FLAG;
	}

	public void setTYPE_FLAG(int type_flag) {
		TYPE_FLAG = type_flag;
	}

	public int getTYPE_NO() {
		return TYPE_NO;
	}

	public void setTYPE_NO(int type_no) {
		TYPE_NO = type_no;
	}

	public String getSHOW_LVL() {
		return SHOW_LVL;
	}

	public void setSHOW_LVL(String show_lvl) {
		SHOW_LVL = show_lvl;
	}

	public String getPAR_ID() {
		return PAR_ID;
	}

	public void setPAR_ID(String par_id) {
		PAR_ID = par_id;
	}

	public String getC1() {
		return C1;
	}

	public void setC1(String c1) {
		C1 = c1;
	}

	public String getC2() {
		return C2;
	}

	public void setC2(String c2) {
		C2 = c2;
	}

	public String getFUNDSOURCE_FLAG() {
		return FUNDSOURCE_FLAG;
	}

	public void setFUNDSOURCE_FLAG(String fundsource_flag) {
		FUNDSOURCE_FLAG = fundsource_flag;
	}

	public String getCOMPARE_FLAG() {
		return COMPARE_FLAG;
	}

	public void setCOMPARE_FLAG(String compare_flag) {
		COMPARE_FLAG = compare_flag;
	}

}
