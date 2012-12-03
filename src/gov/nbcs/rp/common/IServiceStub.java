/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

/**
 * The class IServiceStub.
 * 
 * @author qj
 * @version 1.0, 2010-3-19
 */
public interface IServiceStub {

	/**
	 * Gets the method. ��ȡ�ͻ��˽ӿ�
	 * 
	 * @return the method
	 */
	public Object getMethod();

	/**
	 * Gets the server method. ��ȡ����˽ӿ�
	 * 
	 * @return the server method
	 */
	public Object getServerMethod();

	/**
	 * Gets the server method nt. ��ȡ����˽ӿڣ�����������
	 * 
	 * @return the server method nt
	 */
	public Object getServerMethodNT();
}
