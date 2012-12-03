/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title  Ĭ��ʵ�ֵ���FineReport�п�����ʾ��CellEditor
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import javax.swing.JCheckBox;

import gov.nbcs.rp.common.Common;

public class DefaultCheckBoxEditor extends JCheckBox {
    /**
     * ����ѡ/��ѡ�󣬶�CellEditorȡֵʱ���غ���ֵ�����磺true->"��"��false->"��"
     */
    private Object checkedValue;
    private Object uncheckedValue;
    
    public DefaultCheckBoxEditor(Object checkedValue,Object uncheckedValue,String title) {
        super(title);
        this.checkedValue = checkedValue;
        this.uncheckedValue = uncheckedValue;
    }
    
    public DefaultCheckBoxEditor(Object checkedValue,Object uncheckedValue) {
        super();
        this.checkedValue = checkedValue;
        this.uncheckedValue = uncheckedValue;
    }


    public Object getValue() {
        return this.isSelected()?checkedValue:uncheckedValue;
    }

    public void setValue(Object value) {
        boolean isSelected = Common.estimate(value);
        this.getModel().setSelected(isSelected);
    }

}
