/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title Ĭ��ʵ�ֵ�������CellEditor��������ʾ��
 * <code>gov.nbcs.rp.common.ui.report.Report</code>������
 * 
 * @author Ǯ�Գ�
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
     * ʹ��һ���Ӧ��ID���ı�������������
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
