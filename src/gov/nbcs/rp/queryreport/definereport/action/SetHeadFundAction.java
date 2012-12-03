/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.action;

import java.util.EventObject;

import javax.swing.JFrame;

import gov.nbcs.rp.queryreport.definereport.ui.FundSourceSetDialog;
import com.foundercy.pf.reportcy.design.actions.UndoableAction;
import com.foundercy.pf.reportcy.design.gui2.core.DesignUtils;
import com.foundercy.pf.util.Tools;
import com.fr.cell.ReportPane;

/**

 * Copyright: Copyright (c) 2011 浙江易桥
 * </p>
 * <p>
 * Company: 浙江易桥
 * </p>
 * <p>
 * CreateDate 20011-7-22
 * </p>
 * 
 * @author wuyal

 * @version 1.0
 */


public class SetHeadFundAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	private JFrame jframe = null;

	public SetHeadFundAction(ReportPane reportPane, JFrame jframe,
			boolean bEnable) {
		// this.setEnabled(bEnable);报表组件包发生改变，不可调用
		this.setName("表头信息设置");
		this.setSmallIcon(DesignUtils.getIcon("images/fbudget/set.gif"));
		this.setReportPane(reportPane);

		this.jframe = jframe;

	}

	public boolean executeAction(EventObject evt, ReportPane reportPane) {

		FundSourceSetDialog headFundSetDialog = new FundSourceSetDialog(
				jframe);
		Tools.centerWindow(headFundSetDialog);
		headFundSetDialog.setVisible(true);
		return false;
	}

}
