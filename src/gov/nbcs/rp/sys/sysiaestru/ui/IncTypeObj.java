/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Date;

/**
 * <p>
 * Title:收入项目类别对象
 * </p>
 * <p>
 * Description:收入项目类别对象
 * </p>
 * <p>

 */
public class IncTypeObj {
	// 编号
	public String inctype_code;

	// 名称
	public String inctype_name;

	// 性质
	public String std_type_code;

	public int end_flag;

	// 0:数据录入，1：从非税收入表取数，规则在FB_IAE_INCTYPE_TO_INCOLUMN 表中
	public int is_inc;

	// 是否其中数
	public int is_sum;

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

	public String rg_code;

	public String last_ver;

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

	public String getInctype_code() {
		return inctype_code;
	}

	public void setInctype_code(String inctype_code) {
		this.inctype_code = inctype_code;
	}

	public String getInctype_name() {
		return inctype_name;
	}

	public void setInctype_name(String inctype_name) {
		this.inctype_name = inctype_name;
	}

	public String getLast_ver() {
		return last_ver;
	}

	public void setLast_ver(String last_ver) {
		this.last_ver = last_ver;
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

	public String getRg_code() {
		return rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
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

	public int getIs_inc() {
		return is_inc;
	}

	public void setIs_inc(int is_inc) {
		this.is_inc = is_inc;
	}

	public int getIs_sum() {
		return is_sum;
	}

	public void setIs_sum(int is_sum) {
		this.is_sum = is_sum;
	}
}
