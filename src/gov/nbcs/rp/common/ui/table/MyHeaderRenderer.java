/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.table;

import gov.nbcs.rp.common.Common;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class MyHeaderRenderer implements TableCellRenderer {
    /**
     * 是否勾选表头CheckBox
     */
    private boolean checked;
    
    private String checkLabel;
    
    /**
     * 设置是否勾选表头CheckBox
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public boolean isChecked() {
        return checked;
    }

    public String getCheckLabel() {
		return checkLabel;
	}

	public void setCheckLabel(String checkLabel) {
		this.checkLabel = checkLabel;
	}

	/*
     *  (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        TableColumn col = table.getColumnModel().getColumn(column);
        Border border = new EtchedBorder(EtchedBorder.LOWERED);
        if (((MyTable) table).hasCheck && (col.getModelIndex() == 0)) {
            JCheckBox check = checkLabel==null?new JCheckBox():new JCheckBox(checkLabel);
            check.setMargin(new Insets(0, 0, 0, 0));
            check.setSelected(checked);//如果设置了勾选就用勾选了的checkbox作为渲染，反则不勾选
            JPanel jp = new JPanel();
            jp.add(check);
            jp.setBorder(border);
            return jp;
        } else {
            JLabel label = new JLabel();//如果没有设置checkbox，用默认文本标签显示
            label.setText(Common.nonNullStr(value));
            label.setBorder(border);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVisible(false);
            return label;
        }
    }

}
