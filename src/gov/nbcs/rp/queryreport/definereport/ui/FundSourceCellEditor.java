/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.foundercy.pf.util.Tools;
import com.fr.cell.Grid;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateDate 2011-9-4
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class FundSourceCellEditor extends com.fr.cell.editor.AbstractCellEditor {

	private FundSourceSetDialog fundSourceSetDialog = null;

	public FundSourceCellEditor() {
		super();
	}

	public Object getCellEditorValue() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Component getCellEditorComponent(Grid grid, CellElement cellElement) {
		Object value = cellElement.getValue();
		if (value == null || !(value instanceof MyCalculateValueImpl)) {
			value = new MyCalculateValueImpl("");
		}

		Window window = SwingUtilities.getWindowAncestor(grid);
		if (window instanceof Frame) {
			this.fundSourceSetDialog = new FundSourceSetDialog((JFrame) window);
		} else {
			this.fundSourceSetDialog = new FundSourceSetDialog((Dialog) window);
		}
		Tools.centerWindow(fundSourceSetDialog);
		fundSourceSetDialog.populate(cellElement, value);

		return this.fundSourceSetDialog;
	}
}
