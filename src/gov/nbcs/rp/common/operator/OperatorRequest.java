/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.operator;

/**
 * The Class OperatorRequest.
 * 
 * @author qzc)
 * @version 1.0, Mar 18, 2009
 * @since fb6.2.81
 */
public class OperatorRequest {

	/** The params. */
	private String paramsStr;

	/**
	 * Instantiates a new cache parameters.
	 * 
	 * @param paramsStr
	 *            the params str
	 */
	public OperatorRequest(String paramsStr) {
		super();
		this.paramsStr = paramsStr;
	}

	/**
	 * Gets the parameter.
	 * 
	 * @param key
	 *            the key
	 * @return the parameter
	 */
	public String getParameter(String key) {
		if ((paramsStr != null) && (paramsStr.length() > 0)) {
			if (paramsStr.indexOf(key + "=") == 0) {
				int endIndex = paramsStr.indexOf("&");
				if (endIndex < 0) {
					endIndex = paramsStr.length();
				}
				return paramsStr.substring(key.length() + 1, endIndex);
			}

			int begin = paramsStr.indexOf("&" + key + "=");
			if (begin >= 0) {
				int beginIndex = begin + key.length() + 2;
				int endIndex = paramsStr.indexOf("&", beginIndex);
				if (endIndex < 0) {
					endIndex = paramsStr.length();
				}
				return paramsStr.substring(beginIndex, endIndex);
			}

		}
		return null;
	}

	// /**
	// * Gets the parameters.
	// *
	// * @param key
	// * the key
	// * @return the parameters
	// */
	// public String[] getParameters(String key) {
	// // TODO
	// return new String[0];
	// }

	/**
	 * 获取 params str.
	 * 
	 * @return the paramsStr
	 */
	public String getParamsStr() {
		return paramsStr;
	}

	/**
	 * 设置 params str.
	 * 
	 * @param paramsStr
	 *            the paramsStr to set
	 */
	public void setParamsStr(String paramsStr) {
		this.paramsStr = paramsStr;
	}
}
