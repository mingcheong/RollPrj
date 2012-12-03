/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.exception;

/**
 * The Class FailedOperatorException. 操作子执行失败异常类
 */
public class FailedOperatorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5065087344301900099L;

	/**
	 * Invalid operator exception.
	 * 
	 * @param opId
	 *            the op id
	 */
	public FailedOperatorException(String opId) {
		super("操作类执行失败 -- " + opId);
	}

}
