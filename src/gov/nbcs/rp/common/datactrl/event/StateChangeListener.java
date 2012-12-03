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

public interface StateChangeListener extends Serializable {
    public void onStateChange(DataSetEvent event)
    throws Exception ;
}
