/**
 * @# ISelectionChangeListener.java    <文件名>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import com.fr.cell.CellSelection;
import com.fr.report.CellElement;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * 版权所有：浙江易桥
 * <P>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <P>
 * 侵权者将受到法律追究。
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-19
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public interface ISelectionListener {

	public void selectionChangeed(CellSelection cs, CellElement cell,
			CellFieldInfo cf);

}
