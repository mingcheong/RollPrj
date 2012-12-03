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

import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

import org.springframework.beans.factory.BeanFactory;


/**
 * A factory for creating Service objects.
 */
public class ServiceFactory {

	/** �ӿڶ��弯�����ļ�. */
	private static final String SERVICE_CONF_URL = "rp-local.xml";

	/**
	 * ���ݵ�¼״̬�жϣ���ȡָ���ӿڵ����߽ӿڻ�Զ�̽ӿڣ����ߣ�
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @return the service interface
	 * @deprecated ������� getServiceInterface(String, String)
	 */
	public static Object getServiceInterface(String interfaceId) {
		return getServiceInterface(interfaceId, null, true);
	}

	/**
	 * Gets the service interface.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param resourceUrl
	 *            the resource url
	 * @return the service interface
	 */
	public static Object getServiceInterface(String interfaceId,
			String resourceUrl) {
		return getServiceInterface(interfaceId, resourceUrl, true);
	}

	/**
	 * Gets the service interface.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param isClient
	 *            the value is true if it is client
	 * @return the service interface
	 * @deprecated ������� getServiceInterface(String, String, boolean)
	 */
	public static Object getServiceInterface(String interfaceId,
			boolean isClient) {
		return getServiceInterface(interfaceId, null, isClient);
	}

	/**
	 * Gets the service interface.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param resourceUrl
	 *            the resource url
	 * @param isClient
	 *            the value is true if it is client
	 * @return the service interface
	 */
	public static Object getServiceInterface(String interfaceId,
			String resourceUrl, boolean isClient) {
		if (Global.loginmode == 1) { // offline
			return SessionUtil.getOffServerBean(interfaceId);
		} else {
			if (isClient) {
				if (resourceUrl != null) {
					return getRemoteServiceInterface(interfaceId, resourceUrl);
				} else {
					return getRemoteServiceInterface(interfaceId);
				}
			} else {
				return SessionUtil.getServerBean(interfaceId);
			}
		}
	}

	/**
	 * ��ȡָ����Դ�ļ��ж����ָ���ӿڵ�Զ�̽ӿ�.
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @param resourceUrl
	 *            the resource url
	 * @return the remote service interface
	 */
	public static Object getRemoteServiceInterface(String interfaceId,
			String resourceUrl) {
		BeanFactory bf = BeanFactoryUtil.getBeanFactory(resourceUrl);
		return bf.getBean(interfaceId);
	}

	/**
	 * ��ȡָ���ӿڵ�Զ�̽ӿ�
	 * 
	 * @param interfaceId
	 *            the interface id
	 * @return the remote service interface
	 * @deprecated getRemoteServiceInterface(String, String)
	 */
	public static Object getRemoteServiceInterface(String interfaceId) {
		return getRemoteServiceInterface(interfaceId, SERVICE_CONF_URL);
	}
}
