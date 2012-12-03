package gov.nbcs.rp.queryreport.batchreport.ui;



import java.awt.BorderLayout;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.queryreport.batchreport.pub.BatchReportStub;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.UIControlFactory;
import com.foundercy.pf.control.UITools;
import com.foundercy.pf.util.XMLData;


public class BatchReportUI extends RpModulePanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// FTree treeEn = null;
	FTree treeRpt = null;
	FPanel pnlRpt = null;

	/**
	 * 功能模块中支持的报表ID，以逗号间隔
	 */
	private String reportIds = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.pf.framework.systemmanager.FModulePanel#initize()
	 */
	public void initize() {
		try {
			createToolBar();

			UIControlFactory factory = new UIControlFactory(
					"gov/rp/nbcs/queryreport/batchreport/ui/xml/BatchReportUI.xml");
			add((FPanel) factory.createControl());

			assignCtrlVar();

			// treeEn.setData(BatchReportStub.getMethod().getEnList());
			treeRpt.setData(BatchReportStub.getMethod()
					.getReportList(reportIds));
			pnlRpt.setLayout(new BorderLayout());

			bindCtrlEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void assignCtrlVar() {
		// treeEn = (FTree) UITools.getFirstControlById(this, "tree_en");
		treeRpt = (FTree) UITools.getFirstControlById(this, "tree_rpt");
		pnlRpt = (FPanel) UITools.getFirstControlById(this, "pnl_rpt");
	}

	ProgressBar pbr = null;

	private void bindCtrlEvent() {
		treeRpt.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				XMLData sel = treeRpt.getSelectedData();
				if (sel != null) {
					final String report_id = (String) sel.get("report_id");
					if (report_id != null) {
						pbr = ProgressBar.createRefreshing();
						pnlRpt.removeAll();
						new Thread() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see java.lang.Thread#run()
							 */
							public void run() {
								try {
									BrClientReport rptPanel = new BrClientReport();
									rptPanel.setReportId(report_id);
									rptPanel.initize();
									pnlRpt.add(rptPanel);
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									pbr.dispose();
								}
							}

						}.start();
						pbr.show();
						pnlRpt.validate();
					}
				}
			}
		});
		// treeEn.addTreeSelectionListener(new TreeSelectionListener() {
		//
		// public void valueChanged(TreeSelectionEvent e) {
		//
		// }
		// });

	}

	/**
	 * @return the reportIds
	 */
	public String getReportIds() {
		return this.reportIds;
	}

	/**
	 * @param reportIds
	 *            the reportIds to set
	 */
	public void setReportIds(String reportIds) {
		this.reportIds = reportIds;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
