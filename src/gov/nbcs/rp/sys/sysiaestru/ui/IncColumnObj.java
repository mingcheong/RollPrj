/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */

package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Date;

/**
 * <p>
 * Title:收入栏目对象
 * </p>
 * <p>
 * Description:收入栏目对象
 * </p>

 */
public class IncColumnObj {
	// 收入栏目编码
	String INCCOL_CODE;

	// 收入栏目中文名
	String INCCOL_NAME;

	// 收入栏目全名
	String INCCOL_FNAME;

	// 收入栏目对应字段
	String INCCOL_ENAME;

	// 数据来源
	int DATA_SOURCE;

	// 计算公式内容
	String FORMULA_DET;

	// 计算优先级
	int CALC_PRI;

	// 栏目是否需要纵向求和
	int SUM_FLAG;

	// 是否隐藏
	int IS_HIDE;

	// 末级标志
	int END_FLAG;

	// 显示格式
	String DISPLAY_FORMAT;

	// 编辑格式
	String EDIT_FORMAT;

	// 浮点型留用字段一
	Double F1;

	// 浮点型留用字段二
	Double F2;

	// 整型留用字段一
	int N1;

	// 整型留用字段二
	int N2;

	// 字符串留用字段一
	String C1;

	// 字符串留用字段二
	String C2;

	// 日期串留用字段
	Date D1;

	// 层次码
	String LVL_ID;

	// 父层次码
	String PAR_ID;

	// 预算年度
	int SET_YEAR;

	String RG_CODE;

	String LAST_VER;

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

	public int getCALC_PRI() {
		return CALC_PRI;
	}

	public void setCALC_PRI(int calc_pri) {
		CALC_PRI = calc_pri;
	}

	public Date getD1() {
		return D1;
	}

	public void setD1(Date d1) {
		D1 = d1;
	}

	public int getDATA_SOURCE() {
		return DATA_SOURCE;
	}

	public void setDATA_SOURCE(int data_source) {
		DATA_SOURCE = data_source;
	}

	public String getDISPLAY_FORMAT() {
		return DISPLAY_FORMAT;
	}

	public void setDISPLAY_FORMAT(String display_format) {
		DISPLAY_FORMAT = display_format;
	}

	public String getEDIT_FORMAT() {
		return EDIT_FORMAT;
	}

	public void setEDIT_FORMAT(String edit_format) {
		EDIT_FORMAT = edit_format;
	}

	public int getEND_FLAG() {
		return END_FLAG;
	}

	public void setEND_FLAG(int end_flag) {
		END_FLAG = end_flag;
	}

	public Double getF1() {
		return F1;
	}

	public void setF1(Double f1) {
		F1 = f1;
	}

	public Double getF2() {
		return F2;
	}

	public void setF2(Double f2) {
		F2 = f2;
	}

	public String getFORMULA_DET() {
		return FORMULA_DET;
	}

	public void setFORMULA_DET(String formula_det) {
		FORMULA_DET = formula_det;
	}

	public String getINCCOL_CODE() {
		return INCCOL_CODE;
	}

	public void setINCCOL_CODE(String inccol_code) {
		INCCOL_CODE = inccol_code;
	}

	public String getINCCOL_ENAME() {
		return INCCOL_ENAME;
	}

	public void setINCCOL_ENAME(String inccol_ename) {
		INCCOL_ENAME = inccol_ename;
	}

	public String getINCCOL_FNAME() {
		return INCCOL_FNAME;
	}

	public void setINCCOL_FNAME(String inccol_fname) {
		INCCOL_FNAME = inccol_fname;
	}

	public String getINCCOL_NAME() {
		return INCCOL_NAME;
	}

	public void setINCCOL_NAME(String inccol_name) {
		INCCOL_NAME = inccol_name;
	}

	public int getIS_HIDE() {
		return IS_HIDE;
	}

	public void setIS_HIDE(int is_hide) {
		IS_HIDE = is_hide;
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

	public int getN1() {
		return N1;
	}

	public void setN1(int n1) {
		N1 = n1;
	}

	public int getN2() {
		return N2;
	}

	public void setN2(int n2) {
		N2 = n2;
	}

	public String getPAR_ID() {
		return PAR_ID;
	}

	public void setPAR_ID(String par_id) {
		PAR_ID = par_id;
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

	public int getSUM_FLAG() {
		return SUM_FLAG;
	}

	public void setSUM_FLAG(int sum_flag) {
		SUM_FLAG = sum_flag;
	}

}
