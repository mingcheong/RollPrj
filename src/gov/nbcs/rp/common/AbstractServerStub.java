/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
	 * Gets the method. 获取客户端接口
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
	 * Gets the server method. 获取服务端接口
	 * 
	 * @param serviceName
	 *            the service name
	 * @return the server method
	 */
	protected Object getServerMethod(String serviceName) {
		return SessionUtil.getServerBean(serviceName);
	}

	/**
	 * Gets the server method nt. 获取服务端接口（不含事务处理）
	 * 
	 * @param serviceBoName
	 *            the service bo name
	 * @return the server method nt
	 */
	protected Object getServerMethodNT(String serviceBoName) {
		return SessionUtil.getServerBean(serviceBoName);
	}

}
