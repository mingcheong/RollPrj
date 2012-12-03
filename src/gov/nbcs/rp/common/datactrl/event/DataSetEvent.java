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

import gov.nbcs.rp.common.datactrl.DataSet;

import java.io.Serializable;


public class DataSetEvent implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * event��������DataSet����
     */
    private DataSet ds;
    
    Object sender;
    
    int previousState;
    
    /**
     * ʹ��DataSet���������Event
     * @param ds DataSet����
     * @param source �¼�������
     */
    public DataSetEvent(DataSet ds) {
        this.ds = ds;
    }
    
    /**
     * ���event��Ӧ��DataSet����
     * @return DataSet����
     */
    public DataSet getDataSet() {
        return ds;
    }
    
    /**
     * ��ȡ�¼��÷����ߣ�һ���Ǵ����ÿؼ��ȵ�
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
