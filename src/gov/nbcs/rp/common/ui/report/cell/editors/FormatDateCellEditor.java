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

import java.text.SimpleDateFormat;

import com.fr.cell.editor.DateCellEditor;

public class FormatDateCellEditor extends DateCellEditor {
    private String format;

    public FormatDateCellEditor(String format) {
        super();

        this.format = format;
    }

    public Object getCellEditorValue() throws Exception {
        if (this.format != null) {
            Object obj = super.getCellEditorValue();
            SimpleDateFormat sformat = new SimpleDateFormat(format);
            return sformat.format(obj);
        }
        return super.getCellEditorValue();
    }
}
