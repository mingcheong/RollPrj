/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.editors.spinner;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField.AbstractFormatter;

public class SpinnerValueGetter {
    public static Object getEditorValue(JSpinner spinner) throws Exception {
        JFormattedTextField text = ((JSpinner.DefaultEditor) spinner
                .getEditor()).getTextField();
        AbstractFormatter formatter = text.getFormatter();
        return formatter.stringToValue(text.getText());
    }
}
