/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title  默认实现的在FineReport中可以显示的CellEditor
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import javax.swing.JCheckBox;

import gov.nbcs.rp.common.Common;

public class DefaultCheckBoxEditor extends JCheckBox {
    /**
     * 当勾选/反选后，对CellEditor取值时返回何种值，例如：true->"是"，false->"否"
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
