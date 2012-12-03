/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui;

import com.foundercy.pf.util.XMLData;

public interface XmlControlContainerTemplate {

	/**
	 * Assign ctrl var.
	 */
	public abstract void assignCtrlVar();

	/**
	 * Bind ctrl event.
	 */
	public abstract void bindCtrlEvent();

	/**
	 * Gets the ctrl value.
	 * 
	 * @return the ctrl value
	 */
	public abstract XMLData getCtrlValue();

	/**
	 * Sets ctrl value.
	 * 
	 * @param xmlData
	 *            the xml data
	 */
	public abstract void setCtrlValue(XMLData xmlData);

	/**
	 * Resets ctrl value.
	 */
	public abstract void resetCtrlValue();

}