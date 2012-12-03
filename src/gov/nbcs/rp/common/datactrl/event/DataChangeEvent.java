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

public class DataChangeEvent extends DataSetEvent {  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int FIELD_MODIRED = 0x00000001;
    public static final int CURSOR_MOVED = 0x00000002;
    public static final int DATA_FILLED = 0x00000008;
    public static final int No_CHANGE = 0x00000010;
    public static final int CHANGE_CANCELED = 0x00000020;
    public static final int CHANGE_APPLIED = 0x00000040;
    
    /**
     * ʱ������
     */
    private int type;
    
    /**
     * ���type��FIELD_MODIRED�����Ի�ȡ�޸ĵ��ֶ�
     */
    private Object source;
    
    public DataChangeEvent(DataSet ds,Object source,int type) {
        super(ds);
        this.source = source;
        this.type = type;
    }
    
    public Object getSource() {
        return source;
    }
    
    public int type() {
        return type;
    }
}
