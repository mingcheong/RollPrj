/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

public interface StatefulData {
    int DS_BROWSE = 0x00000001;   
    int DS_EDIT = 0x00000002;
    int DS_INSERT = 0x00000004;
    
    
    int FLOW_CONFIRM_OR_UNTREAD_DONE = 13; //����¼���ʱ���ư�ť����  ��ʾ��������ȷ�ϣ����˻�״̬
    int FLOW_UNTREAD = 5; //��ʾ�������˻�״̬
    int FLOW_NOTENABLE= 9;//��ʾ��ǰ�û�������������������״̬������
    int FLOW_ISNOTDIV= 8;//��ǰ�� ��������ҵ�û�
    public int getState();
}
