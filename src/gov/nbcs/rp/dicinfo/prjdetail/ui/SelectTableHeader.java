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
			//����report
			this.exi = ei;
			report = new Report();
			reportUI = new ReportUI(report);
			reportUI.setColumnHeaderVisible(false);
			reportUI.setRowHeaderVisible(false);
			
			//����Ԫ�ض���
			FToolBar toolbar = new FToolBar();
			FPanel pnlTop = new FPanel();
			FPanel pnlBase = new FPanel(); // �����
			FPanel pnlUnder = new FPanel(); // �����ȡreport�ı�ͷ��report�����

			FButton btnOpen = new FButton("", "��");
			btnOpen.setIcon("images/father-node.gif");
			btnOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
			FButton btnOK = new FButton("", "ȷ��");
			btnOK.setIcon("images/fbudget/save.gif");
			btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

			FButton btnCancel = new FButton("", "ȡ��");
			btnCancel.setIcon("images/fbudget/close.gif");
			btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnCancel.addActionListener( new CancelBtnActionListener());
			btnOpen.addActionListener( new OpenBtnActionListener());
			btnOK.addActionListener( new OKBtnActionListener());

			initReportUI();   //��ʱ������prjdetail����ѡ���excel��report
			
			//����
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
			
			// �����С����������
			this.setSize(700, 530);
			this.getContentPane().add(pnlBase);
			this.dispose();
			this.setTitle("��ȡ��ͷ");
			this.setModal(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��reportUI 
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
	 *open��ť�ļ����¼�����excel�ļ�
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
	 * ok��ť�ļ����¼�
	 * ��ȡ��ͷ��Ϣ��������prjdetail����
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
	 * cancel��ť�ļ����¼�
	 * �ر����
	 */
	private class CancelBtnActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CellSelection cs = reportUI.getCellSelection();
			node = ReportUtil.parseDocument(reportUI.getWorkSheet(), cs);
			setVisible(false);
			}
		}
	/**
	 * @param �����ļ�ѡ����
	 *
	 */
	private void CreateChooser() {
		// �����ļ�ѡ����
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		// �趨���õ��ļ��ĺ�׺��
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls") || f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "�����ļ�(*.xls)";
			}
		});
	}
	
	public Node getNode() throws Exception{
		return node;
	}
}
