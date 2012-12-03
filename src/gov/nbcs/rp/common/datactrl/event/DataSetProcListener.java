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

public interface DataSetProcListener extends Serializable {
    /**
     * ��������֮ǰ����
     * @param event 
     */
    public void beforeProc(DataSetEvent event)
    throws Exception ;
    
    /**
     * ��������֮����
     * @param event 
     */    
    public void afterProc(DataSetEvent event)
    throws Exception ;
}
