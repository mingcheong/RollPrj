/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 默认实现的下拉框CellEditor，可以显示在
 * <code>gov.nbcs.rp.common.ui.report.Report</code>对象中
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.list.CustomComboBox;

public class DefaultComboBoxEditor extends CustomComboBox {
    private static final String DefaultComboBoxEditor_ID_NAME = "DefaultComboBoxEditor_ID_NAME";
    private static final String DefaultComboBoxEditor_TEXT_NAME = "DefaultComboBoxEditor_TEXT_NAME";
    
    public DefaultComboBoxEditor(DataSet ds, String idName, String textName)
            throws Exception {
        super(ds, idName, textName);
    }

    public DefaultComboBoxEditor() throws Exception {
        this(null, null, null);
    }
    
    /**
     * 使用一组对应的ID、文本来创建下拉框
     * @param values
     * @param texts
     */
    public DefaultComboBoxEditor(Object[] values,Object [] texts)
    throws Exception {
        DataSet ds = DataSet.createClient();
        if(values.length!=texts.length) {
            throw new IllegalArgumentException("The size of array 'values' must must be the same as 'texts'!");
        }
        for(int i=0;i<values.length;i++) {
            ds.append();
            ds.fieldByName(DefaultComboBoxEditor_ID_NAME).setValue(values[i]);
            ds.fieldByName(DefaultComboBoxEditor_TEXT_NAME).setValue(texts[i]);
        }
        this.setIdName(DefaultComboBoxEditor_ID_NAME);
        this.setTextName(DefaultComboBoxEditor_TEXT_NAME);
        this.setDataSet(ds);
        this.reset();
    }
}
