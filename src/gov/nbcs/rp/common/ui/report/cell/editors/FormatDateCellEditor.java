/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title
 * 
 * @author Ǯ�Գ�
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
