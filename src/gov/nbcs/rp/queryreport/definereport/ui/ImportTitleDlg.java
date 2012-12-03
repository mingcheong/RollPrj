/**
 * @# ImportTitleDlg.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.ui.actions.format.addrow.AddHeaderAreaRowAction;
import com.foundercy.pf.reportcy.summary.ui.actions.format.addrow.AddTitleAreaRowAction;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.foundercy.pf.util.XMLData;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.WorkBook;
import com.fr.report.WorkSheet;
import com.fr.report.io.ExcelImporter;

/**
 * ����˵��:��ȡ��ͷ
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-4-1
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class ImportTitleDlg extends JDialog {

	/**
	 * 
	 */
	JWorkBook jWorkBook;

	FLabel flblOpenExcel;

	GroupReport groupReport;

	SummaryReportPane operReort;

	JFrame parent;

	private static final long serialVersionUID = -6567941741487175012L;

	public ImportTitleDlg(JFrame parent, SummaryReportPane report) {
		super(parent, true);
		this.parent = parent;
		this.operReort = report;
		this.groupReport = report.getGroupReport();
		init();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void init() {
		FPanel fpnlReport = new FPanel();
		this.getContentPane().add(fpnlReport);
		fpnlReport.setLeftInset(10);
		fpnlReport.setRightInset(10);
		fpnlReport.setBottomInset(10);
		fpnlReport.setTopInset(10);

		WorkBook workBook = new WorkBook();
		WorkSheet workSheet = new WorkSheet();
		//
		// cellPanel = new CellColPanel(this);
		// spnlCol.add(cellPanel, FSplitPane.TOP);

		workBook = new WorkBook();
		workSheet = new WorkSheet();
		workBook.addReport(workSheet);
		jWorkBook = new JWorkBook();
		jWorkBook.setWorkBook(workBook);

		flblOpenExcel = new FLabel();
		flblOpenExcel.setForeground(Color.blue);

		FButton fbtnOpenExcel = new FButton("fbtnOpenExcel", "��Excelģ��");
		fbtnOpenExcel.addActionListener(new OpenExcelActionListener(jWorkBook,
				flblOpenExcel));

		FButton btnGetTitle = new FButton("title", "��ȡ����");
		btnGetTitle.addActionListener(new GetTitleAction());

		FButton btnGetHead = new FButton("head", "��ȡ��ͷ");
		btnGetHead.addActionListener(new GetHeadAction());

		FButton btnOK = new FButton("CLOSE", "�ر�");
		btnOK.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ImportTitleDlg.this.dispose();

			}

		});

		RowPreferedLayout rLay = new RowPreferedLayout(21);
		rLay.setRowHeight(23);
		rLay.setRowGap(5);
		// rLay.setColumnWidth(115);
		fpnlReport.setLayout(rLay);
		fpnlReport.add(jWorkBook, new TableConstraints(1, 21, true, true));
		fpnlReport.addControl(fbtnOpenExcel, new TableConstraints(1, 5, false,
				true));
		fpnlReport.addControl(btnGetTitle, new TableConstraints(1, 5, false,
				true));
		fpnlReport.addControl(btnGetHead, new TableConstraints(1, 5, false,
				true));

		fpnlReport.addControl(btnOK, new TableConstraints(1, 5, false, true));
		fpnlReport.addControl(flblOpenExcel, new TableConstraints(1, 21, false,
				true));
		this.setTitle("�����ͷ");
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width - 20,
				Toolkit.getDefaultToolkit().getScreenSize().height - 50);
		this.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - this
						.getWidth()) / 2, (Toolkit.getDefaultToolkit()
						.getScreenSize().height - this.getHeight()) / 2);

	}

	class OpenExcelActionListener implements ActionListener {

		JWorkBook jWorkBook;

		FLabel flblOpenExcel;

		public OpenExcelActionListener(JWorkBook jWorkBook, FLabel flblOpenExcel) {
			this.jWorkBook = jWorkBook;
			this.flblOpenExcel = flblOpenExcel;
		}

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			// �趨���õ��ļ��ĺ�׺��
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					if (f.getName().toLowerCase().endsWith(".xls")
							|| f.isDirectory()) {
						return true;
					}
					return false;
				}

				public String getDescription() {
					return "�����ļ�(*.xls)";
				}
			});
			fileChooser.showOpenDialog(parent);
			if (fileChooser.getSelectedFile() == null)
				return;
			try {

				ExcelImporter ei = new ExcelImporter(fileChooser
						.getSelectedFile());
				WorkBook wb = ei.generateWorkBook();
				jWorkBook.setWorkBook(wb);
				jWorkBook.updateUI();
				jWorkBook.repaint();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ImportTitleDlg.this,
						" ���ļ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ImportTitleDlg.this,
						" ��Excelģ�巢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			String filepath = fileChooser.getSelectedFile().getPath();
			flblOpenExcel.setTitle(filepath);
		}
	}

	class GetTitleAction implements ActionListener {

		public void actionPerformed(ActionEvent e2) {
			int iIndex = jWorkBook.getWorkBook().getSelectedIndex();
			com.fr.report.Report report = jWorkBook.getWorkBook().getReport(
					iIndex);
			CellSelection cs = jWorkBook.getCellSelection();
			if (cs == null) {
				new MessageBox(ImportTitleDlg.this, "��ѡ��һ������!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				return;
			}
			int iRowBegin = cs.getRow();
			int iColBegin = cs.getColumn();
			int iRowSpan = cs.getRowSpan();
			int iColSpan = cs.getColumnSpan();
			List cells = new ArrayList();
			CellElement aCell;
			XMLData xmlSaved = new XMLData();// ��¼��ȡ�õĸ���
			// ȡ��ѡȡ������
			// ȡ���и��п�
			double rowHeight[] = new double[iRowSpan];
			double colWidth[] = new double[iColSpan];
			for (int i = 0; i < iRowSpan; i++) {
				rowHeight[i] = report.getRowHeight(i + iRowBegin);// �и�
				for (int j = 0; j < iColSpan; j++) {
					if (i == 0)
						colWidth[j] = report.getColumnWidth(j + iColBegin);// �п�
					aCell = report.getCellElement(iColBegin + j, iRowBegin + i);
					if (aCell == null)
						continue;
					if (hasSave(xmlSaved, aCell))
						continue;
					if (aCell == null)
						continue;
					CellElement curCell = deepClone(aCell);
					// curCell.setRow(i);
					// curCell.setColumn(j);
					curCell = curCell.deriveCellElement(j, i);
					xmlSaved
							.put(aCell.getRow() + ":" + aCell.getColumn(), null);
					cells.add(curCell);

				}
			}
			// ���ѡ�������
			int rowIndex[] = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_TITLE, groupReport);
			// ��ɾ��
			if (rowIndex != null)
				for (int i = 0; i < rowIndex.length; i++) {
					groupReport.removeRow(0);
				}
			// �����
			for (int i = 0; i < iRowSpan; i++) {
				new AddTitleAreaRowAction(operReort, 1).actionPerformed(null);
				// .addReportHeaderRow();
			}
			// �����
			while (iColSpan - groupReport.getColumnCount() > 0) {
				groupReport.addColumnAfter(groupReport.getColumnCount());
			}
			// ɾ����
			while (groupReport.getColumnCount() - iColSpan > 0) {
				groupReport.removeColumn(groupReport.getColumnCount() - 1);
			}
			int iCount = cells.size();
			for (int i = 0; i < iCount; i++) {
				groupReport.addCellElement((CellElement) cells.get(i));
			}
			// �����и��п�
			for (int i = 0; i < rowHeight.length; i++)
				groupReport.setRowHeight(i, rowHeight[i]);
			for (int i = 0; i < colWidth.length; i++)
				groupReport.setColumnWidth(i, colWidth[i]);
			new MessageBox(ImportTitleDlg.this, "��ȡ����ɹ�!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
		}
	}

	class GetHeadAction implements ActionListener {

		public void actionPerformed(ActionEvent e2) {
			int iIndex = jWorkBook.getWorkBook().getSelectedIndex();
			com.fr.report.Report report = jWorkBook.getWorkBook().getReport(
					iIndex);
			CellSelection cs = jWorkBook.getCellSelection();
			if (cs == null) {
				new MessageBox(ImportTitleDlg.this, "��ѡ��һ������!",
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				return;
			}

			// ���ѡ�������
			int rowIndex[] = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_TITLE, groupReport);

			int iRowTitle = (rowIndex == null ? 0 : rowIndex.length);

			int iRowBegin = cs.getRow();
			int iColBegin = cs.getColumn();
			int iRowSpan = cs.getRowSpan();
			int iColSpan = cs.getColumnSpan();
			List cells = new ArrayList();
			CellElement aCell;
			XMLData xmlSaved = new XMLData();// ��¼��ȡ�õĸ���
			double rowHeight[] = new double[iRowSpan];
			double colWidth[] = new double[iColSpan];
			// ȡ��ѡȡ������
			for (int i = 0; i < iRowSpan; i++) {
				rowHeight[i] = report.getRowHeight(iRowBegin + i);
				for (int j = 0; j < iColSpan; j++) {
					if (i == 0)
						colWidth[j] = report.getColumnWidth(j + iColBegin);// �п�
					aCell = report.getCellElement(iColBegin + j, iRowBegin + i);
					if (aCell == null)
						continue;
					if (hasSave(xmlSaved, aCell))
						continue;
					if (aCell == null)
						continue;

					CellElement curCell = deepClone(aCell);
					// curCell.setRow(i + iRowTitle);
					// curCell.setColumn(j);
					curCell = curCell.deriveCellElement(j, i + iRowTitle);
					xmlSaved
							.put(aCell.getRow() + ":" + aCell.getColumn(), null);
					cells.add(curCell);

				}
			}
			int headIndex[] = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_HEADER, groupReport);
			// ��ɾ��
			if (headIndex != null)
				for (int i = 0; i < headIndex.length; i++) {
					groupReport.removeRow(headIndex[0]);
				}
			// �����
			for (int i = 0; i < iRowSpan; i++) {
				new AddHeaderAreaRowAction(operReort, 1).actionPerformed(null);
			}

			// �����
			while (iColSpan - groupReport.getColumnCount() > 0) {
				groupReport.addColumnAfter(groupReport.getColumnCount() - 1);
			}

			int iCount = cells.size();
			for (int i = 0; i < iCount; i++) {
				groupReport.addCellElement((CellElement) cells.get(i));
			}
			// �����и��п�
			for (int i = 0; i < rowHeight.length; i++)
				groupReport.setRowHeight(i + iRowTitle, rowHeight[i]);
			for (int i = 0; i < colWidth.length; i++)
				groupReport.setColumnWidth(i, colWidth[i]);
			new MessageBox(ImportTitleDlg.this, "��ȡ��ͷ�ɹ�!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();

		}
	}

	private CellElement deepClone(CellElement aCell) {
		/*
		 * CellElement resultCell = new CellElement();
		 * resultCell.setStyle(aCell.getStyle());
		 * resultCell.setValue(aCell.getValue());
		 * resultCell.setRow(aCell.getRow());
		 * resultCell.setColumn(aCell.getColumn());
		 * resultCell.setRowSpan(aCell.getRowSpan());
		 * resultCell.setColumnSpan(aCell.getColumnSpan()); //
		 * resultCell.setMap(aCell.getMap()); return resultCell;
		 */
		try {
			return (CellElement) aCell.clone();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean hasSave(XMLData aData, CellElement aCell) {
		return aData.containsKey(aCell.getRow() + ":" + aCell.getColumn());

	}
}
