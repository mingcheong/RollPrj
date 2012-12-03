/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl.event;

import java.io.Serializable;

public interface DataChangeListener extends Serializable {
    /**
     * 当前数据项变化时候触发
     * @param event
     */
    public void onDataChange(DataChangeEvent event) 
    throws Exception ;
}
