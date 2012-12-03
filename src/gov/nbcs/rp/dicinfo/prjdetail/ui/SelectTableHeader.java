/**
 * @author qzc
 * @version 1.0
 */
package gov.nbcs.rp.dicinfo.prjdetail.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.PaperSize;
import com.fr.report.WorkSheet;
import com.fr.report.io.ExcelImporter;

public class SelectTableHeader extends FDialog {
	private ReportUI reportUI;
	
	private Report report;

	private ExcelImporter exi;
	
	private JFileChooser fileChooser; 
	
	private Node node;
	
	public SelectTableHeader(ExcelImporter ei) {
		super(Global.mainFrame);
		try {
			//创建report
			this.exi = ei;
			report = new Report();
			reportUI = new ReportUI(report);
			reportUI.setColumnHeaderVisible(false);
			reportUI.setRowHeaderVisible(false);
			
			//界面元素定义
			FToolBar toolbar = new FToolBar();
			FPanel pnlTop = new FPanel();
			FPanel pnlBase = new FPanel(); // 主面版
			FPanel pnlUnder = new FPanel(); // 下面获取report的表头的report的面版

			FButton btnOpen = new FButton("", "打开");
			btnOpen.setIcon("images/father-node.gif");
			btnOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
			FButton btnOK = new FButton("", "确定");
			btnOK.setIcon("images/fbudget/save.gif");
			btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

			FButton btnCancel = new FButton("", "取消");
			btnCancel.setIcon("images/fbudget/close.gif");
			btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnCancel.addActionListener( new CancelBtnActionListener());
			btnOpen.addActionListener( new OpenBtnActionListener());
			btnOK.addActionListener( new OKBtnActionListener());

			initReportUI();   //此时创建在prjdetail界面选择的excel的report
			
			//布局
			RowPreferedLayout layTop = new RowPreferedLayout(1);
			pnlTop.setLayout( layTop );
			layTop.setRowHeight(50);
			pnlTop.addControl( toolbar , new TableConstraints(1,1,true,true));

			toolbar.addControl(btnOpen);
			toolbar.addControl(btnOK);
			toolbar.addControl(btnCancel);
			
			RowPreferedLayout layUnder = new RowPreferedLayout(1);
			pnlUnder.setLayout( layUnder );
			pnlUnder.add(reportUI , new TableConstraints(1,1,true,true));
			
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlBase.setLayout( layBase );
			pnlBase.addControl(pnlTop , new TableConstraints(2,1,false, true));
			pnlBase.addControl(pnlUnder , new TableConstraints(25,1,true, true));
			
			// 界面大小及基本设置
			this.setSize(700, 530);
			this.getContentPane().add(pnlBase);
			this.dispose();
			this.setTitle("获取表头");
			this.setModal(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化reportUI 
	 */
	private void initReportUI() throws Exception {
		reportUI.clearAll();
		reportUI.setWorkSheet((WorkSheet) exi.generateWorkBook().getReport(0));
		reportUI.getReport().shrinkToFitRowHeight();
		reportUI.getReport().getReportSettings().setPaperSize(
				new PaperSize(2000, 3000));
		reportUI.updateUI();
	}
	
	/**
	 *open按钮的监听事件，打开excel文件
	 */
	private class OpenBtnActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				CreateChooser();
				fileChooser.showOpenDialog(Global.mainFrame);
				exi = new ExcelImporter(fileChooser
						.getSelectedFile());
				initReportUI();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}
	/**
	 * ok按钮的监听事件
	 * 获取表头信息，并传给prjdetail界面
	 */
	private class OKBtnActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				CellSelection cs = reportUI.getCellSelection();
				node = ReportUtil.parseDocument(reportUI.getWorkSheet(), cs);
				setVisible(false);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			}
		}
	/**
	 * cancel按钮的监听事件
	 * 关闭面板
	 */
	private class CancelBtnActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CellSelection cs = reportUI.getCellSelection();
			node = ReportUtil.parseDocument(reportUI.getWorkSheet(), cs);
			setVisible(false);
			}
		}
	/**
	 * @param 创建文件选择器
	 *
	 */
	private void CreateChooser() {
		// 创建文件选择器
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls") || f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "所有文件(*.xls)";
			}
		});
	}
	
	public Node getNode() throws Exception{
		return node;
	}
}
