/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.EventObject;

import javax.swing.JFrame;

import com.foundercy.pf.reportcy.design.actions.UndoableAction;
import com.foundercy.pf.reportcy.design.gui2.core.DesignUtils;
import com.foundercy.pf.util.Tools;
import com.fr.cell.ReportPane;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>

 */
public class SetSubFieldAction extends UndoableAction {

	private static final long serialVersionUID = 1L;

	private JFrame jframe = null;

	public SetSubFieldAction(ReportPane reportPane, JFrame jframe,
			boolean bEnable) {
		// this.setEnabled(bEnable);��������������ı䣬���ɵ���
		this.setName("��������");
		this.setSmallIcon(DesignUtils.getIcon("images/fbudget/set.gif"));
		this.setReportPane(reportPane);

		this.jframe = jframe;

	}

	public boolean executeAction(EventObject evt, ReportPane reportPane) {

		SubFieldSetDialog subFieldSetDialog = new SubFieldSetDialog(
				(ReportGuideUI) jframe);
		Tools.centerWindow(subFieldSetDialog);
		subFieldSetDialog.setVisible(true);
		return false;
	}

}
