/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl.event;

import java.io.Serializable;

public interface StateChangeListener extends Serializable {
    public void onStateChange(DataSetEvent event)
    throws Exception ;
}
