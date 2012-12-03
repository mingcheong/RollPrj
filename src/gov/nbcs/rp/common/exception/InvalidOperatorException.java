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

public class InvalidOperatorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid operator exception.
	 * 
	 * @param opId
	 *            the op id
	 */
	public InvalidOperatorException(String opId) {
		super("无效的操作类 -- " + opId);
	}

}
