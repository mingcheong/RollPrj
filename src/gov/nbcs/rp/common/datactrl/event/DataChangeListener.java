/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ��
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl.event;

import java.io.Serializable;

public interface DataChangeListener extends Serializable {
    /**
     * ��ǰ������仯ʱ�򴥷�
     * @param event
     */
    public void onDataChange(DataChangeEvent event) 
    throws Exception ;
}
