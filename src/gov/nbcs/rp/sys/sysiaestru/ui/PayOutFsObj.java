package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Date;

/**
 * <p>
 * Title:支出资金来源对象
 * </p>
 * <p>
 * Description:支出资金来源对象
 * </p>

 */

public class PayOutFsObj {
	// 资金来源编码
	public String PFS_CODE;

	// 资金来源名称
	public String PFS_NAME;

	// 资金来源全名
	public String pfs_fname;

	// 资金来源英文名
	public String pfs_ename;

	// 标准支出类型编码
	public String std_type_code;

	// 数据来源
	public int data_source;

	// 计算公式内容
	public String formula_det;

	// 计算优先级
	public int calc_pri;

	// 取数Sql
	public String sql_det;

	// 取数Sql的Oracle形式
	public String sql_det_o;

	// 对应收入栏目
	public String inccol_ename;

	// 是否支持项目
	public int sup_prj;

	// 是否参与控制数分配
	public int cf_pfs_flag;

	// 末级标志
	public int end_flag;

	// 显示格式
	public String display_format;

	// 编辑格式
	public String edit_format;

	public double f1;

	public double f2;

	public int n1;

	public int n2;

	public String c1;

	public String c2;

	public Date d1;

	// 层次码
	public String lvl_id;

	// 父层次码
	public String par_id;

	// 是否常用
	public int in_common_use;

	// 年度
	public int set_year;

	public String RG_CODE;

	public String LAST_VER;

	// 是否收支平衡
	public int is_balance;

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

	public int getCalc_pri() {
		return calc_pri;
	}

	public void setCalc_pri(int calc_pri) {
		this.calc_pri = calc_pri;
	}

	public int getCf_pfs_flag() {
		return cf_pfs_flag;
	}

	public void setCf_pfs_flag(int cf_pfs_flag) {
		this.cf_pfs_flag = cf_pfs_flag;
	}

	public Date getD1() {
		return d1;
	}

	public void setD1(Date d1) {
		this.d1 = d1;
	}

	public int getData_source() {
		return data_source;
	}

	public void setData_source(int data_source) {
		this.data_source = data_source;
	}

	public String getDisplay_format() {
		return display_format;
	}

	public void setDisplay_format(String display_format) {
		this.display_format = display_format;
	}

	public String getEdit_format() {
		return edit_format;
	}

	public void setEdit_format(String edit_format) {
		this.edit_format = edit_format;
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

	public String getFormula_det() {
		return formula_det;
	}

	public void setFormula_det(String formula_det) {
		this.formula_det = formula_det;
	}

	public int getIn_common_use() {
		return in_common_use;
	}

	public void setIn_common_use(int in_common_use) {
		this.in_common_use = in_common_use;
	}

	public String getInccol_ename() {
		return inccol_ename;
	}

	public void setInccol_ename(String inccol_ename) {
		this.inccol_ename = inccol_ename;
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

	public String getPFS_CODE() {
		return PFS_CODE;
	}

	public void setPFS_CODE(String pfs_code) {
		PFS_CODE = pfs_code;
	}

	public String getPfs_ename() {
		return pfs_ename;
	}

	public void setPfs_ename(String pfs_ename) {
		this.pfs_ename = pfs_ename;
	}

	public String getPfs_fname() {
		return pfs_fname;
	}

	public void setPfs_fname(String pfs_fname) {
		this.pfs_fname = pfs_fname;
	}

	public String getPFS_NAME() {
		return PFS_NAME;
	}

	public void setPFS_NAME(String pfs_name) {
		PFS_NAME = pfs_name;
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

	public String getSql_det() {
		return sql_det;
	}

	public void setSql_det(String sql_det) {
		this.sql_det = sql_det;
	}

	public String getSql_det_o() {
		return sql_det_o;
	}

	public void setSql_det_o(String sql_det_o) {
		this.sql_det_o = sql_det_o;
	}

	public String getStd_type_code() {
		return std_type_code;
	}

	public void setStd_type_code(String std_type_code) {
		this.std_type_code = std_type_code;
	}

	public int getSup_prj() {
		return sup_prj;
	}

	public void setSup_prj(int sup_prj) {
		this.sup_prj = sup_prj;
	}

	public int getIs_balance() {
		return is_balance;
	}

	public void setIs_balance(int is_balance) {
		this.is_balance = is_balance;
	}

}
