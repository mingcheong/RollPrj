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

import gov.nbcs.rp.common.datactrl.DataSet;

import java.io.Serializable;


public class DataSetEvent implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * event所关联的DataSet对象
     */
    private DataSet ds;
    
    Object sender;
    
    int previousState;
    
    /**
     * 使用DataSet对象构造关联Event
     * @param ds DataSet对象
     * @param source 事件触发者
     */
    public DataSetEvent(DataSet ds) {
        this.ds = ds;
    }
    
    /**
     * 获得event对应的DataSet对象
     * @return DataSet对象
     */
    public DataSet getDataSet() {
        return ds;
    }
    
    /**
     * 获取事件得发送者，一般是触发得控件等等
     * @return
     */
    public Object getSender() {
        return sender;
    }
    
    public void setSender(Object sender) {
        this.sender = sender;
    }
    
    public int getPreviousState() {
        return this.previousState;
    }
}
