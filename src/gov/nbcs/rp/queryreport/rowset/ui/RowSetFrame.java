/**
 * @# RowSetFrame.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class RowSetFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6460781435279134010L;

	private RowSetUI rowSetUI;

	private String reportID;

	private String setYear;

	private DefineReport defineReport;

	public RowSetFrame(String sReportID, String setYear,
			DefineReport defineReport) {
		super("单位综合情况表设计");
		this.reportID = sReportID;
		this.setYear = setYear;
		this.defineReport = defineReport;
		initUI();

	}

	private void initUI() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit
				.getDefaultToolkit().getScreenSize().height - 30);
		rowSetUI = new RowSetUI(reportID, setYear, defineReport);

		FPanel pnlButton = new FPanel();
		pnlButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		FButton btnClose = new FButton("CLOSE", "关闭");
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// if (canClose()) {
				// rowSetUI.setChanged(false);
				RowSetFrame.this.dispose();
				// }

			}

		});
		pnlButton.add(btnClose);
		FPanel pnlBack = new FPanel();
		pnlBack.setLayout(new BorderLayout());
		pnlBack.add(rowSetUI, BorderLayout.CENTER);
		rowSetUI.frame = this;
		pnlBack.add(pnlButton, BorderLayout.SOUTH);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.getContentPane().add(pnlBack);
	}

}
