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

public class InvalidParametersException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public InvalidParametersException(String opId) {
		super("无效的参数类 -- " + opId);
	}

}
