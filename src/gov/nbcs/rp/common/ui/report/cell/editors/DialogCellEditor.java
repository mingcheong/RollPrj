/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import gov.nbcs.rp.common.ui.report.cell.inputui.AbstractInputDialog;
import com.foundercy.pf.util.Tools;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

public class DialogCellEditor extends AbstractCellEditor {

	AbstractInputDialog dialog;

//    private ActionListener actionListener;

    public DialogCellEditor(AbstractInputDialog dialog) {
        this.dialog = dialog;
    }

//    public FDialog getDialog() {
//        return this.dialog;
//    }

//    public DialogCellEditor setSize(int width, int height) {
//        this.dialog.setSize(width, height);
//        return this;
//    }

    public Object getCellEditorValue() throws Exception {
        return dialog.getValue();
    }

//    public void setActionListener(ActionListener actionListener) {
//        this.actionListener = actionListener;
//    }

    public Component getCellEditorComponent(Grid grid, final CellElement cell) {
    	// 采用已有方法实现对话框居中 by qinj at Mar 16, 2009
    	// int left = (grid.getWidth() - dialog.getWidth()) / 2;
		// int top = (grid.getHeight() - dialog.getHeight()) / 2;
		// Point gridLocation = grid.getLocationOnScreen();
		// left += gridLocation.x;
		// top += gridLocation.y;
		// dialog.setLocation(left, top);
    	dialog.setGrid(grid);
    	dialog.setCell(cell);
        Tools.centerWindow(dialog);
        dialog.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				stopCellEditing();
//		        if (actionListener != null) {
//		            ActionEvent event = new ActionEvent(cell, 0, cell.getName());
//		            actionListener.actionPerformed(event);
//		        }
			}
        	
        });
        return dialog;
    }
}
