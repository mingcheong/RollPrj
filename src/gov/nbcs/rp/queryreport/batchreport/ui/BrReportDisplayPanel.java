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
package gov.nbcs.rp.queryreport.batchreport.ui;

import java.awt.Component;

import javax.swing.JPanel;

import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.reportcy.common.ifun.operator.IClientReportOperation;
import com.foundercy.pf.reportcy.report.op.ReportCore;
import com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel;

/**
 * @author Administrator
 * 
 */
public class BrReportDisplayPanel extends ReportDisplayPanel {

	public BrReportDisplayPanel() {
		super();
	}

	public BrReportDisplayPanel(IClientReportOperation reportCore) {
		super(reportCore);
	}

	public BrReportDisplayPanel(ReportCore reportCore) {
		super(reportCore);
	}

	public BrReportDisplayPanel(String reportId) {
		super(reportId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel#initize(java.lang
	 * .String)
	 */
	public void initize(String style) {
		super.initize(style);
//		FTreeAssistInput c = null;
//		int i = 0;
//		while (true) {
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			c = getFTreeAssistInput(previewPanel);
//			if (c != null || i++ > 5)
//				break;
//		}
//		if (c != null)
//			c.setVisible(false);
	}

	private FTreeAssistInput getFTreeAssistInput(JPanel parent) {
		for (int i = 0; i < parent.getComponentCount(); i++) {
			FTreeAssistInput c = null;
			Component c1 = parent.getComponent(i);
			// System.out.println(c1);
			if (c1 instanceof FTreeAssistInput) {
				FTreeAssistInput c0 = (FTreeAssistInput) c1;
				if ("div_code".equalsIgnoreCase(c0.getId())) {
					c = c0;
				}
			} else if (c1 instanceof JPanel) {
				c = getFTreeAssistInput((JPanel) c1);
			}
			if (c != null)
				return c;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel#getQueryPanel()
	 */
	public JPanel getQueryPanel() {
		return super.getQueryPanel();
		// return new JPanel();
	}

}
