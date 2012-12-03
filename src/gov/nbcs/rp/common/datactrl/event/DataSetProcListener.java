/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl.event;

import java.io.Serializable;

public interface DataSetProcListener extends Serializable {
    /**
     * 操作调用之前发生
     * @param event 
     */
    public void beforeProc(DataSetEvent event)
    throws Exception ;
    
    /**
     * 操作调用之后发生
     * @param event 
     */    
    public void afterProc(DataSetEvent event)
    throws Exception ;
}
