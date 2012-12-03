package gov.nbcs.rp.sys.sysiaestru.ibs;

/**
 * 收入栏目表结构
 * 
 * @author qzc
 * 
 */
public interface IIncColumn {
	//表名
	public String INCCOL_TABLE = "fb_iae_inccolumn";

	public String INCCOL_ROOT = "收入栏目";

	// 收入栏目编码
	public String INCCOL_CODE = "INCCOL_CODE";

	// 收入栏目中文名
	public String INCCOL_NAME = "INCCOL_NAME";

	// 收入栏目全名
	public String INCCOL_FNAME = "INCCOL_FNAME";

	// 收入栏目全名
	public String INCCOL_ENAME = "INCCOL_ENAME";

	// 数据来源
	public String DATA_SOURCE = "DATA_SOURCE";

	// 计算公式内容
	public String FORMULA_DET = "FORMULA_DET";

	// 计算优先级
	public String CALC_PRI = "CALC_PRI";

	// 栏目是否需要纵向求和
	public String SUM_FLAG = "SUM_FLAG";

	// 是否隐藏
	public String IS_HIDE = "IS_HIDE";

	// 末级标志
	public String END_FLAG = "END_FLAG";

	// 显示格式
	public String DISPLAY_FORMAT = "DISPLAY_FORMAT";

	// 编辑格式
	public String EDIT_FORMAT = "EDIT_FORMAT";

	// 浮点型留用字段一
	public String F1 = "F1";

	// 浮点型留用字段二
	public String F2 = "F2";

	// 整型留用字段一
	public String N1 = "N1";

	// 整型留用字段二
	public String N2 = "N2";

	// 字符串留用字段一
	public String C1 = "C1";

	// 字符串留用字段二
	public String C2 = "C2";

	// 日期串留用字段
	public String D1 = "D1";

	// 层次码
	public String LVL_ID = "LVL_ID";

	// 父层次码
	public String PAR_ID = "PAR_ID";

	// 预算年度
	public String SET_YEAR = "SET_YEAR";

	public String RG_CODE = "RG_CODE";

	public String LAST_VER = "LAST_VER";

}
