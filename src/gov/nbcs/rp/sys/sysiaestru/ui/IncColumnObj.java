/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */

package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Date;

/**
 * <p>
 * Title:������Ŀ����
 * </p>
 * <p>
 * Description:������Ŀ����
 * </p>

 */
public class IncColumnObj {
	// ������Ŀ����
	String INCCOL_CODE;

	// ������Ŀ������
	String INCCOL_NAME;

	// ������Ŀȫ��
	String INCCOL_FNAME;

	// ������Ŀ��Ӧ�ֶ�
	String INCCOL_ENAME;

	// ������Դ
	int DATA_SOURCE;

	// ���㹫ʽ����
	String FORMULA_DET;

	// �������ȼ�
	int CALC_PRI;

	// ��Ŀ�Ƿ���Ҫ�������
	int SUM_FLAG;

	// �Ƿ�����
	int IS_HIDE;

	// ĩ����־
	int END_FLAG;

	// ��ʾ��ʽ
	String DISPLAY_FORMAT;

	// �༭��ʽ
	String EDIT_FORMAT;

	// �����������ֶ�һ
	Double F1;

	// �����������ֶζ�
	Double F2;

	// ���������ֶ�һ
	int N1;

	// ���������ֶζ�
	int N2;

	// �ַ��������ֶ�һ
	String C1;

	// �ַ��������ֶζ�
	String C2;

	// ���ڴ������ֶ�
	Date D1;

	// �����
	String LVL_ID;

	// �������
	String PAR_ID;

	// Ԥ�����
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
