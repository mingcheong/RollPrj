/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.formula;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * The Class GroupRowFormula. 汇总计算中建议使用的一些常量
 * 
 * @author qj
 */
public interface RowFormula {

	/** The Constant TOTAL_SUM_ROW_FLAG_FIELD. 总计行标识. */
	public static final String TOTAL_SUM_ROW_FLAG_FIELD = "TOTAL_SUM_ROW_FLAG_FIELD";

	/** The Constant GROUP_SUM_ROW_FLAG_FIELD. 汇总行标识. */
	public static final String GROUP_SUM_ROW_FLAG_FIELD = "GROUP_SUM_ROW_FLAG_FIELD";
	
	/** The Constant GROUP_SUM_ROW_FLAG_FIELD. 汇总行标识，不参与单元格风格判断. */
	public static final String GROUP_SUM_ROW_FLAG_FIELD_NO_STYLE = "GROUP_SUM_ROW_FLAG_FIELD_NO_STYLE";

	/** The Constant GROUP_SUM_ROW_LEVEL_FIELD. 分组 level 字段 */
	public static final String GROUP_SUM_ROW_LEVEL_FIELD = "GROUP_SUM_ROW_LEVEL_FIELD";

	/** The Constant GROUP_SUM_ROW_GROUPIDX_FIELD. 分组group_index字段 */
	public static final String GROUP_SUM_ROW_GROUPIDX_FIELD = "GROUP_SUM_ROW_GROUPIDX_FIELD";

	/** The Constant ROW_IS_MID_FLAG. */
	public static final String ROW_IS_MID_FLAG = "ROW_IS_MID_FLAG";// 其中数行
	
	/** 所属汇总行的bookmark */
	public static final String GROUP_SUM_ROW_BOOKMARK_FIELD = "GROUP_SUM_ROW_BOOKMARK_FIELD";

	/**
	 * Calculate by row.
	 * 
	 * @param fields
	 *            the fields
	 * @param data
	 *            the data
	 * @param calFields
	 *            the cal fields
	 * @throws Exception
	 *             the exception
	 */
	public void calculateByRow(List fields, DataSet data, List calFields)
			throws Exception;
}
