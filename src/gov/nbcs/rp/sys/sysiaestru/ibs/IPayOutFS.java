package gov.nbcs.rp.sys.sysiaestru.ibs;

public interface IPayOutFS {
	// 表名
	public String PFS_TABLE = "fb_iae_payout_fundsource";

	public String PFS_ROOT = "支出资金来源";

	public String PFS_CODE = "PFS_CODE";// 资金来源编码

	public String PFS_NAME = "PFS_NAME";// 资金来源名称

	public String PFS_FNAME = "PFS_FNAME";// 资金来源全名

	public String PFS_ENAME = "PFS_ENAME";// 资金来源英文名

	public String STD_TYPE_CODE = "STD_TYPE_CODE";// 标准支出类型编码

	public String DATA_SOURCE = "DATA_SOURCE";// 数据来源

	public String FORMULA_DET = "FORMULA_DET";// 计算公式内容

	public String CALC_PRI = "CALC_PRI";// 计算优先级

	public String SQL_DET = "SQL_DET";// 取数Sql

	public String SQL_DET_O = "SQL_DET_O";// 取数Sql的Oracle形式

	public String INCCOL_ENAME = "INCCOL_ENAME";// 对应收入栏目

	public String SUP_PRJ = "SUP_PRJ";// 是否支持项目

	public String CF_PFS_FLAG = "CF_PFS_FLAG";// 是否参与控制数分配

	public String END_FLAG = "END_FLAG";// 末级标志

	public String DISPLAY_FORMAT = "DISPLAY_FORMAT";// 显示格式

	public String EDIT_FORMAT = "EDIT_FORMAT";// 编辑格式

	public String F1 = "F1";

	public String F2 = "F2";

	public String N1 = "n1";

	public String N2 = "n2";

	public String C1 = "c1";

	public String C2 = "c2";

	public String D1 = "d1";

	public String LVL_ID = "lvl_id";// 层次码

	public String PAR_ID = "par_id";// 父层次码

	public String IN_COMMON_USE = "in_common_use";// 是否常用

	public String SET_YEAR = "set_year";// 年度

	public String RG_CODE = "RG_CODE";

	public String LAST_VER = "LAST_VER";

	public String IS_BALANCE = "is_balance";

}
