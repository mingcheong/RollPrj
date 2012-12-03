/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell;

import com.fr.base.Style;

/**
 * The Interface PropertyProviderEx. PropertyProvider的扩展类
 */
public interface PropertyProviderEx extends PropertyProvider {

	/**
	 * 获取指定单元格的样式
	 *
	 * @param cell
	 *            the cell
	 *
	 * @return the cell style
	 */
	public Style getCellStyle(Cell cell);
}
