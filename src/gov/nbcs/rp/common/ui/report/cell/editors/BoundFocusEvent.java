/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.fr.cell.editor.CellEditor;

public class BoundFocusEvent {
    public void bind(final CellEditor editor,final Component comp) {
        comp.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e) {}

            public void focusLost(FocusEvent e) {
                editor.stopCellEditing();
            }            
        });
    }
}
