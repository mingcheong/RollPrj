/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 代码输出入框，只是能输入数字
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report.cell.inputui;

import java.awt.Frame;

import com.foundercy.pf.control.FDialog;
import com.fr.cell.Grid;
import com.fr.report.CellElement;

/**
 * The class InputUI.
 * 
 * @author qj
 * @version 1.0, Mar 16, 2009
 * @since 6.3.50
 */
public abstract class AbstractInputDialog extends FDialog {
	
	/** The grid. */
	private Grid grid;
	
	/** The cell. */
	private CellElement cell;

	/**
	 * Instantiates a new abstract input dialog.
	 * 
	 * @param owner
	 */
	protected AbstractInputDialog(Frame owner) {
		super(owner);
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public AbstractInputDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}

	/**
	 * 辅助录入窗体的返回值接口，该接口实现需要返回的内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Object getValue() throws Exception;

	/**
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * @param grid the grid to set
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * @return the cell
	 */
	public CellElement getCell() {
		return cell;
	}

	/**
	 * @param cell the cell to set
	 */
	public void setCell(CellElement cell) {
		this.cell = cell;
	}

}
