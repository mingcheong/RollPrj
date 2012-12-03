/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.report.cell.CellStyle;
import com.fr.cell.CellSelection;
import com.fr.cell.GridCRRenderer;
import com.fr.cell.JWorkSheet;
import com.fr.report.CellElement;

/**
 * The Class ReportUI.
 */
public class ReportUI extends JWorkSheet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// /** The report. */
	// Report report;

	/** The selected cells. */
	private CellElement selectedCells[];

	/** The row select. */
	private boolean rowSelect = true;

	/**
	 * 判断是否支持行选.
	 * 
	 * @return true, if checks if is row select
	 */
	public boolean isRowSelect() {
		return rowSelect;
	}

	/**
	 * 打开/关闭行选.
	 * 
	 * @param rowSelect
	 *            the row select
	 */
	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
	}

	/**
	 * Instantiates a new report ui.
	 * 
	 * @param report
	 *            the report
	 */
	public ReportUI(Report report) {
		super(report, new ReportGrid());
		// this.report = report;
		report.reportUI = this;
		getGrid().addMouseListener(new ReportMouseAdapter());
		getGrid().addKeyListener(new ReportKeyAdapter());
		getGrid().setCellDragable(false);
		getGrid().setSelectedBackground(CellStyle.BackgroundColors.SELECTED_COLOR);
		this.setRowEndless(false);
		this.setColumnEndless(false);

		/*
		 * 禁止用del键删除一个或多个格子 杨学峰 26072007
		 */
		this.getGrid().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_DELETE)) {
					CellSelection cells = ReportUI.this.getCellSelection();
					if ((cells.getRowSpan() >= 1) || (cells.getColumnSpan() >= 1)) {
						e.setKeyCode(0);
					}
				}
			}
		});
	}

	/**
	 * Clear the column header content.
	 */
	public void clearColumnHeaderContent() {
		this.getGridColumn().setGridCRRender(new EmptyGridCRRender());
	}

	/**
	 * Clear the row header content.
	 */
	public void clearRowHeaderContent() {
		this.getGridRow().setGridCRRender(new EmptyGridCRRender());
	}

	/**
	 * Gets the report content.
	 * 
	 * @return the report content
	 */
	public Report getReportContent() {
		// return this.report;
		return (Report) getReport();
	}

	/**
	 * Sets the report content.
	 * 
	 * @param report
	 *            the new report content
	 */
	public void setReportContent(Report report) {
		if (report != null) {
			report.reportUI = this;
		}
		selectedCells = null;
		super.setReport(report);
		// ((ReportGrid) getGrid()).setReport(report);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fr.cell.ReportPane#setReport(com.fr.report.Report)
	 */
	public void setReport(Report report) {
		setReportContent(report);
	}

	/**
	 * 产生选中一行的各种效果.
	 * 
	 * @param rowIndex
	 *            行号
	 */
	public void doRowSelected(int rowIndex) {
		Report report = getReportContent();
		if ((report != null) && (report.getRowCount() > 0)) {
			// 判断，并设置行号在可选范围之内
			TableHeader header = report.getReportHeader();
			int min = header == null ? 0 : header.getRows();
			if (rowIndex < min) {
				rowIndex = min;
			}
			if (rowIndex > report.getRowCount() - 1) {
				rowIndex = report.getRowCount() - 1;
			}
			// 只操作表体
			if (((header == null) && (rowIndex >= 0))
					|| ((header != null) && (rowIndex >= header.getRows()))) {
				// CellElement cell = report.getSelectedCell();
				// if (cell != null) {
				if (isRowSelect()) {
					this.getCellSelection().setRow(rowIndex);
					CellElement cells[] = report.getRow(rowIndex);
					if (cells != selectedCells) {
						setRowSelected(selectedCells, false);
						selectedCells = cells;
						setRowSelected(cells, true);
						getGrid().repaint();
					}
				} else {
					getGrid().startEditing();
				}

				// 重新定位游标
				try {
					// by ymq 7.15 为查询而修改
					if (report.getBodyData() != null) {
						report.getBodyData().gotoBookmark(
								report.rowToBookmark(rowIndex));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// }
			}
		}
	}

	/**
	 * 用指定的颜色值渲染单元格背景.
	 * 
	 * @param cells
	 *            the cells
	 * @param color
	 *            the color
	 */
	protected void setRowSelected(CellElement cells[], boolean rowSelected) {
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				// XXL，添加一些判断，以便手工添加的也可以
				if (cells[i] == null) {
					return;
				}
				if (!(cells[i] instanceof Cell)) {
					return;
				}
				((Cell) cells[i]).setRowSelected(rowSelected);
			}
		}
	}

	private final class EmptyGridCRRender implements GridCRRenderer,
			Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		public Object getDisplay(int arg0) {
			return "";
		}
	}

	/**
	 * 鼠标事件的监听，点击后如果表格不处于可编辑状态 会产生一系列的行选择效果.
	 */
	class ReportMouseAdapter extends MouseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			doRowSelected(getCellSelection().getRow());
		}
	}

	/**
	 * The Class ReportKeyAdapter.
	 */
	class ReportKeyAdapter extends KeyAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent e) {
			if (getCellSelection() != null) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					doRowSelected(getCellSelection().getRow());
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Freeze cell. 冻结指定单元格
	 * 
	 * @param frozenColumn
	 *            the frozen column
	 * @param frozenRow
	 *            the frozen row
	 */
	public void freezeCell(int frozenColumn, int frozenRow) {
		if (frozenColumn < 0) {
			frozenColumn = 0;
		}
		if (frozenRow < 0) {
			frozenRow = 0;
		}
		if ((frozenColumn == 0) && (frozenRow == 0)) {
		} else {
			getGrid().setVerticalBeinValue(0);
			getGrid().setHorizontalBeginValue(0);
		}
		setFrozenColumn(frozenColumn);
		setFrozenRow(frozenRow);
		int horizontalX = getGrid().getHorizontalBeginValue();
		int verticalY = getGrid().getVerticalBeginValue();
		getHorizontalScrollBar().setValue(horizontalX);
		getVerticalScrollBar().setValue(verticalY);

	}

	/**
	 * Freeze column. 冻结指定列
	 * 
	 * @param frozenColumn
	 *            the frozen column
	 */
	public void freezeColumn(int frozenColumn) {
		freezeCell(frozenColumn, getFrozenRow());
	}

	/**
	 * Freeze row. 冻结指定行
	 * 
	 * @param frozenRow
	 *            the frozen row
	 */
	public void freezeRow(int frozenRow) {
		freezeCell(getFrozenColumn(), frozenRow);
	}

	/**
	 * 刷新页面显示，避免本不在视线内的列被显示后的空白.
	 */
	public void repaintView() {
		getVerticalScrollBar().setValue(getVerticalScrollBar().getValue());
		getHorizontalScrollBar().setValue(getHorizontalScrollBar().getValue());
	}

	/**
	 * 刷新页面显示，避免本不在视线内的列被显示后的空白 横纵向滚动条都置0.
	 */
	public void resetView() {
		getVerticalScrollBar().setValue(0);
		getHorizontalScrollBar().setValue(0);
	}	
}
