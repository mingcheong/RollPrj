/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.logHandler;

import java.lang.reflect.Method;

/**
 * The Interface ILogAdvice.
 * 
 * @author qzc)
 * @version 1.0, Apr 28, 2009
 * @since fb6.3.70
 */
public interface ILogAdvice {

	/**
	 * Runs before the.
	 * 
	 * @param method
	 *            the method
	 * @param aobj
	 *            the aobj
	 * @param obj
	 *            the obj
	 * @throws Throwable
	 *             the throwable
	 */
	public void before(Method method, Object aobj[], Object obj)
			throws Throwable;

	/**
	 * Runs after the returning.
	 * 
	 * @param rt
	 *            the rt
	 * @param method
	 *            the method
	 * @param aobj
	 *            the aobj
	 * @param obj
	 *            the obj
	 * @throws Throwable
	 *             the throwable
	 */
	public void afterReturning(Object rt, Method method, Object aobj[],
			Object obj) throws Throwable;

	/**
	 * Runs after the throwing.
	 * 
	 * @param ex
	 *            the ex
	 * @param method
	 *            the method
	 * @param aobj
	 *            the aobj
	 * @param obj
	 *            the obj
	 */
	public void afterThrowing(Throwable ex, Method method, Object aobj[],
			Object obj);
}
