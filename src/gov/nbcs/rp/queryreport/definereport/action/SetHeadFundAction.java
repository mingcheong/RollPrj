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

 * Copyright: Copyright (c) 2011 �㽭����
 * </p>
 * <p>
 * Company: �㽭����
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
		// this.setEnabled(bEnable);��������������ı䣬���ɵ���
		this.setName("��ͷ��Ϣ����");
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
