/**
 * 
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import gov.nbcs.rp.queryreport.definereport.ui.ReportPanel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.WorkBook;
import com.fr.report.WorkSheet;

/**
 * <p>
 * Title:��֧�ܱ�����
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateDate 2009-5-22
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class SzReportPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private JWorkBook jWorkBook;

	private WorkSheet workSheet;

	private FLabel flblOpenExcel; // ��Excel���ļ���·��

	private SzzbSet szzbSet;

	public SzReportPanel(SzzbSet szzbSet) {
		this.szzbSet = szzbSet;
		this.setLeftInset(10);
		this.setRightInset(10);
		this.setBottomInset(10);
		this.setTopInset(10);

		// ��ʼ��
		init();
	}

	/**
	 * ��ʼ��
	 * 
	 */
	private void init() {
		WorkBook workBook = new WorkBook();
		workSheet = new WorkSheet();

		workBook = new WorkBook();
		workSheet = new WorkSheet();
		workBook.addReport(workSheet);
		jWorkBook = new JWorkBook() {
			private static final long serialVersionUID = 1L;

			public JPopupMenu createColumnPopupMenu(MouseEvent arg0, int arg1) {
				return getColPopupMenu(jWorkBook, workSheet);
			}

			public JPopupMenu createRowPopupMenu(MouseEvent arg0, int arg1) {
				return getRowPopupMenu(jWorkBook, workSheet);
			}

		};
		jWorkBook.setWorkBook(workBook);
		jWorkBook.setRowEndless(true);
		jWorkBook.setColumnEndless(true);

		flblOpenExcel = new FLabel();
		flblOpenExcel.setForeground(Color.blue);

		FButton fbtnOpenExcel = new FButton("fbtnOpenExcel", "��Excelģ��");
		fbtnOpenExcel.addActionListener(new OpenExcelActionListener(jWorkBook,
				flblOpenExcel));

		JComponent reportToolBar = ReportPanel
				.createReportToolBarPanel(jWorkBook);

		RowPreferedLayout rLay = new RowPreferedLayout(3);
		rLay.setRowHeight(23);
		rLay.setRowGap(5);
		rLay.setColumnWidth(115);
		this.setLayout(rLay);
		this.add(reportToolBar, new TableConstraints(1, 3, false, true));
		this.add(jWorkBook, new TableConstraints(1, 3, true, true));
		this
				.addControl(fbtnOpenExcel, new TableConstraints(1, 1, false,
						false));
		this.addControl(flblOpenExcel, new TableConstraints(1, 1, false, true));

	}

	/**
	 * ���롢ɾ���в���
	 * 
	 * @return
	 */
	private JPopupMenu getRowPopupMenu(final JWorkBook jWorkBook,
			final WorkSheet workSheet) {
		JPopupMenu rowPopupMenu = new JPopupMenu();
		JMenuItem insertRowMenuItem = new JMenuItem("������");
		JMenuItem delRowMenuItem = new JMenuItem("ɾ����");
		rowPopupMenu.add(insertRowMenuItem);
		rowPopupMenu.add(delRowMenuItem);
		// ������
		insertRowMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CellSelection cs = jWorkBook.getCellSelection();
				int row = cs.getRow();
				try {
					szzbSet.cellPanel.checkInsertOrDelRowCol(row,
							CellColPanel.INSERT_FLAG, CellColPanel.ROW_FLAG);
					workSheet.insertRow(row);
					refreshInserOrDelRowCol();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(szzbSet, "�����з������󣬴�����Ϣ��"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		// ɾ����
		delRowMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CellSelection cs = jWorkBook.getCellSelection();
				int rowStart = cs.getRow();
				int rowEnd = rowStart + cs.getRowSpan();
				try {
					for (int i = rowEnd - 1; i >= rowStart; i--) {
						szzbSet.cellPanel.checkInsertOrDelRowCol(i,
								CellColPanel.DEL_FLAG, CellColPanel.ROW_FLAG);
						workSheet.removeRow(i);
					}
					refreshInserOrDelRowCol();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(szzbSet, "ɾ���з������󣬴�����Ϣ��"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		return rowPopupMenu;
	}

	/**
	 * ���롢ɾ���в���
	 * 
	 * @return
	 */
	private JPopupMenu getColPopupMenu(final JWorkBook jWorkBook,
			final WorkSheet workSheet) {
		JPopupMenu colPopupMenu = new JPopupMenu();
		JMenuItem insertcolMenuItem = new JMenuItem("������");
		JMenuItem delcolMenuItem = new JMenuItem("ɾ����");
		colPopupMenu.add(insertcolMenuItem);
		colPopupMenu.add(delcolMenuItem);
		// ������
		insertcolMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				CellSelection cs = jWorkBook.getCellSelection();
				int col = cs.getColumn();
				try {
					szzbSet.cellPanel.checkInsertOrDelRowCol(col,
							CellColPanel.INSERT_FLAG, CellColPanel.COL_FLAG);
					workSheet.insertColumn(col);
					refreshInserOrDelRowCol();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(szzbSet, "�����з������󣬴�����Ϣ��"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		// ɾ����
		delcolMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CellSelection cs = jWorkBook.getCellSelection();
				int colStart = cs.getColumn();
				int colEnd = colStart + cs.getColumnSpan();
				try {
					for (int i = colEnd - 1; i >= colStart; i--) {
						szzbSet.cellPanel.checkInsertOrDelRowCol(i,
								CellColPanel.DEL_FLAG, CellColPanel.COL_FLAG);
						workSheet.removeColumn(i);
					}
					refreshInserOrDelRowCol();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(szzbSet, "ɾ���з������󣬴�����Ϣ��"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		return colPopupMenu;
	}

	private void refreshInserOrDelRowCol() throws Exception {
		szzbSet.setChanged(true);
		szzbSet.setRowOrColChanged(true);
		jWorkBook.repaint();
		// ˢ��"��Ԫ����������"����
		szzbSet.cellPanel.dispData(szzbSet.getReportID(), false);
	}

	public FLabel getFlblOpenExcel() {
		return flblOpenExcel;
	}

	public WorkSheet getWorkSheet() {
		return workSheet;
	}

	public JWorkBook getJWorkBook() {
		return jWorkBook;
	}

}
