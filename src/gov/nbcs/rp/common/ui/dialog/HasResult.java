/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.dialog;

public interface HasResult {
	public static final int UNKNOWN = 0;
	public static final int OK = 1;
	public static final int CANCEL = 2;

	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	public int getButtonResult();
	
	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	public Object getResult();
}
