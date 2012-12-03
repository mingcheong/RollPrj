/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.tree;

import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

class CancelActionListener implements DataSetProcListener {
    CustomTree tree;
    CancelActionListener(CustomTree tree) {
        this.tree = tree;
    }
    
    public void beforeProc(DataSetEvent event)
    throws Exception  {
    }

    /**
     * DataSet����Cancel�����������
     */
    public void afterProc(DataSetEvent event)
    throws Exception {
        tree.maskValueChange(true);
        tree.reset();
        tree.maskValueChange(false);
    }
}
