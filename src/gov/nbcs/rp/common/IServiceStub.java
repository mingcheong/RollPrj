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

/**
 * The class IServiceStub.
 * 
 * @author qj
 * @version 1.0, 2010-3-19
 */
public interface IServiceStub {

	/**
	 * Gets the method. 获取客户端接口
	 * 
	 * @return the method
	 */
	public Object getMethod();

	/**
	 * Gets the server method. 获取服务端接口
	 * 
	 * @return the server method
	 */
	public Object getServerMethod();

	/**
	 * Gets the server method nt. 获取服务端接口（不含事务处理）
	 * 
	 * @return the server method nt
	 */
	public Object getServerMethodNT();
}
