/**
 * @(#)AbstractActionOp.java
 * @title - 滚动项目库
 * @copyright 浙江易桥 版权所有
 */
package gov.nbcs.rp.common.action;

import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;

/**
 * The Class AbstractActionOp.
 * 
 * @author qzc(钱自成)
 * @version 1.0, May 19, 2011
 * @since rp 1.0.00
 */
public abstract class AbstractActionOp implements ActionedUIEx {

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
