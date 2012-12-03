/**
 * Copyright zjyq 版权所有
 * 
 * 部门预算编审系统
 * 
 * @title 
 * 
 * @author qzcun
 * 
 * @version 1.0
 */
package gov.nbcs.rp.queryreport.batchreport.action;

import java.awt.event.ActionEvent;

import gov.nbcs.rp.queryreport.batchreport.ui.BatchPrintDialog;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.util.Tools;

/**
 * The Class BatchPrint.
 * 
 * @author Administrator
 */
public class BatchPrint extends CommonAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.foundercy.pf.framework.systemmanager.CommonAction#actionPerformed
	 * (java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		BatchPrintDialog dlg = new BatchPrintDialog();
		dlg.setTitle("选择需要打印的行");
		Tools.centerWindow(dlg);
		dlg.setVisible(true);
	}

}
