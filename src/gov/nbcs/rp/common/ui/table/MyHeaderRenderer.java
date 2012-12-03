/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
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
     * �Ƿ�ѡ��ͷCheckBox
     */
    private boolean checked;
    
    private String checkLabel;
    
    /**
     * �����Ƿ�ѡ��ͷCheckBox
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
            check.setSelected(checked);//��������˹�ѡ���ù�ѡ�˵�checkbox��Ϊ��Ⱦ�����򲻹�ѡ
            JPanel jp = new JPanel();
            jp.add(check);
            jp.setBorder(border);
            return jp;
        } else {
            JLabel label = new JLabel();//���û������checkbox����Ĭ���ı���ǩ��ʾ
            label.setText(Common.nonNullStr(value));
            label.setBorder(border);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVisible(false);
            return label;
        }
    }

}
