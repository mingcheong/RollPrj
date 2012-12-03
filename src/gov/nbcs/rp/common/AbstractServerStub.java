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

import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * The class AbstractServerStub.
 * 
 * @author qj
 * @version 1.0, 2010-3-19
 */
public abstract class AbstractServerStub implements IServiceStub {

	/**
	 * Gets the method. ��ȡ�ͻ��˽ӿ�
	 * 
	 * @param serviceName
	 *            the service name
	 * @param confFilePath
	 *            the conf file path
	 * @return the method
	 */
	protected Object getMethod(String serviceName, String confFilePath) {
		return ServiceFactory.getServiceInterface(serviceName, confFilePath);
	}

	/**
	 * Gets the server method. ��ȡ����˽ӿ�
	 * 
	 * @param serviceName
	 *            the service name
	 * @return the server method
	 */
	protected Object getServerMethod(String serviceName) {
		return SessionUtil.getServerBean(serviceName);
	}

	/**
	 * Gets the server method nt. ��ȡ����˽ӿڣ�����������
	 * 
	 * @param serviceBoName
	 *            the service bo name
	 * @return the server method nt
	 */
	protected Object getServerMethodNT(String serviceBoName) {
		return SessionUtil.getServerBean(serviceBoName);
	}

}
