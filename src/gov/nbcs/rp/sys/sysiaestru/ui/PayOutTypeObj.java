/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Date;

/**
 * <p>
 * Title:支出项目类别别对象
 * </p>
 * <p>
 * Description:支出项目类别对象
 * </p>
 * <p>

 */
public class PayOutTypeObj {
	public String payout_kind_code; // 编码

	public String payout_kind_name;// 名称

	public String std_type_code;// 性质

	public int end_flag;

	public double f1;

	public double f2;

	public int n1;

	public int n2;

	public String c1;

	public String c2;

	public Date d1;

	public String lvl_id;

	public String par_id;

	public int set_year;

	public String RG_CODE;

	public String LAST_VER;

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public Date getD1() {
		return d1;
	}

	public void setD1(Date d1) {
		this.d1 = d1;
	}

	public int getEnd_flag() {
		return end_flag;
	}

	public void setEnd_flag(int end_flag) {
		this.end_flag = end_flag;
	}

	public double getF1() {
		return f1;
	}

	public void setF1(double f1) {
		this.f1 = f1;
	}

	public double getF2() {
		return f2;
	}

	public void setF2(double f2) {
		this.f2 = f2;
	}

	public String getLAST_VER() {
		return LAST_VER;
	}

	public void setLAST_VER(String last_ver) {
		LAST_VER = last_ver;
	}

	public String getLvl_id() {
		return lvl_id;
	}

	public void setLvl_id(String lvl_id) {
		this.lvl_id = lvl_id;
	}

	public int getN1() {
		return n1;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public int getN2() {
		return n2;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public String getPar_id() {
		return par_id;
	}

	public void setPar_id(String par_id) {
		this.par_id = par_id;
	}

	public String getPayout_kind_code() {
		return payout_kind_code;
	}

	public void setPayout_kind_code(String payout_kind_code) {
		this.payout_kind_code = payout_kind_code;
	}

	public String getPayout_kind_name() {
		return payout_kind_name;
	}

	public void setPayout_kind_name(String payout_kind_name) {
		this.payout_kind_name = payout_kind_name;
	}

	public String getRG_CODE() {
		return RG_CODE;
	}

	public void setRG_CODE(String rg_code) {
		RG_CODE = rg_code;
	}

	public int getSet_year() {
		return set_year;
	}

	public void setSet_year(int set_year) {
		this.set_year = set_year;
	}

	public String getStd_type_code() {
		return std_type_code;
	}

	public void setStd_type_code(String std_type_code) {
		this.std_type_code = std_type_code;
	}
}
