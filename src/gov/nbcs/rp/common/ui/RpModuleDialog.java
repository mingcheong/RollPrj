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

import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModuleDialog;
import com.foundercy.pf.util.Global;
import gov.nbcs.rp.common.action.ActionedUIEx;

import javax.swing.JFrame;


/**
 * The class FbModulePanel. 预算编审风格的UI基类 <br>	
 * 1、实现了doClose()方法 <br>
 * 2、占位实现ActionedUIEx接口中其他方法，子类根据需要覆盖。
 */
public abstract class RpModuleDialog extends FModuleDialog implements
		ActionedUIEx {

	/**
	 * Instantiates a new fb module dialog.
	 */
	public RpModuleDialog() {
		super();
	}

	/**
	 * The Constructor.
	 * 
	 * @param owner
	 *            the owner
	 * @param title
	 *            the title
	 * @param isModal
	 *            the value is true if it is modal
	 */
	public RpModuleDialog(JFrame owner, String title, boolean isModal) {
		super(owner, title, isModal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.CloseActionedUI#doClose()
	 */
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doAdd()
	 */
	public void doAdd() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doCancel()
	 */
	public void doCancel() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doDelete()
	 */
	public void doDelete() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doInsert()
	 */
	public void doInsert() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doModify()
	 */
	public void doModify() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.action.ActionedUI#doSave()
	 */
	public void doSave() {
		throw new UnsupportedOperationException("not implemented!");
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.action.ActionedUIEx#doExpExcel()
	 */
	public void doExpExcel() {
		throw new UnsupportedOperationException("not implemented!");		
	}

	/* (non-Javadoc)
	 * @see gov.nbcs.rp.common.action.ActionedUIEx#doPrint()
	 */
	public void doPrint() {
		throw new UnsupportedOperationException("not implemented!");		
	}
	
}
