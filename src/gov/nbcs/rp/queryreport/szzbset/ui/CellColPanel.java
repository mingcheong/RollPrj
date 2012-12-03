/**
 * @# CellColPanel.java    <文件名>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkSheet;
import com.fr.report.CellElement;
import com.fr.report.WorkSheet;

/**
 * 功能说明:上方展示设置单元格的面板。这里设置的用红色显示出
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * 版权所有：浙江易桥
 * <P>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <P>
 * 侵权者将受到法律追究。
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
 * DATE: 2011-3-18
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class CellColPanel extends FPanel {
	private String reportID;

	private List lstConsCell;

	private SzzbSet parent;

	private XMLData xmlForCell = new XMLData();

	private List lstListener = new ArrayList();

	private int titleStart;

	private int bodyStart;

	private CellFieldInfo globalCellField = null;

	private WorkSheet report;

	private JWorkSheet reportUI;

	static final int INSERT_FLAG = 1;

	static final int DEL_FLAG = 2;

	static final int ROW_FLAG = 1;

	static final int COL_FLAG = 2;

	public CellColPanel(SzzbSet parent) throws Exception {
		this.parent = parent;

		this.reportID = parent.getReportID();
		if (reportID == null)
			reportID = "";
		init();
		reportUI.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("cellSelection")) {
					int iCount = lstListener.size();
					CellElement curCell = getEditCell();
					CellFieldInfo aField = null;
					if (curCell == null || curCell.getRow() < bodyStart)
						aField = null;
					else {
						// 如果新的格子中没有公式，但有文字则说明是固定的格
						// 如果新的格子中没有公式，也没有文字则要新加一个公式格

						CellFieldInfo aTempField = (CellFieldInfo) xmlForCell
								.get("" + curCell.getRow() + ":"
										+ curCell.getColumn());
						if (aTempField == null && curCell.getValue() == null) {
							aField = new CellFieldInfo(reportUI);
							aField.setCellCol(curCell.getColumn());
							aField.setCellRow(curCell.getRow());
							aField.setReportID(reportID);
							aField.setSourceType(CellFieldInfo.SOURCETYPE_NULL);
							xmlForCell.put(curCell.getRow() + ":"
									+ curCell.getColumn(), aField);
						} else if (aTempField != null) {
							aField = aTempField;
						}
					}
					for (int i = 0; i < iCount; i++) {

						((ISelectionListener) (lstListener.get(i)))
								.selectionChangeed((CellSelection) evt
										.getNewValue(), curCell, aField);

					}
				}
			}

		});
		reportUI.getGrid().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_DELETE)) {
					CellSelection cells = reportUI.getCellSelection();
					if (cells.getRowSpan() >= 1 || cells.getColumnSpan() >= 1) {
						e.setKeyCode(0);
					}
				} else
				// 复制
				if ((e.getKeyCode() == KeyEvent.VK_C && e.isControlDown())) {
					CellElement curCell = getEditCell();
					if (curCell == null)
						return;
					CellFieldInfo aTempField = (CellFieldInfo) xmlForCell
							.get("" + curCell.getRow() + ":"
									+ curCell.getColumn());
					if (aTempField == null && curCell.getValue() != null)// 固定格
						return;
					if (curCell == null || curCell.getRow() < bodyStart)// 超出表体
						return;
					globalCellField = (CellFieldInfo) aTempField.clone();
					e.setKeyCode(0);

				} else // 粘贴
				if ((e.getKeyCode() == KeyEvent.VK_V && e.isControlDown())) {

					if (globalCellField == null)
						return;
					CellElement curCell = getEditCell();
					CellFieldInfo aTempField = (CellFieldInfo) xmlForCell
							.get("" + curCell.getRow() + ":"
									+ curCell.getColumn());
					if (aTempField == null && curCell.getValue() != null)// 固定格
						return;
					if (curCell == null || curCell.getRow() < bodyStart)// 超出表体
						return;

					CellFieldInfo tempCell = (CellFieldInfo) globalCellField
							.clone();
					tempCell.setCellRow(curCell.getRow());
					tempCell.setCellCol(curCell.getColumn());
					xmlForCell.put("" + curCell.getRow() + ":"
							+ curCell.getColumn(), tempCell);
					curCell.setValue(tempCell.getTitle());
					reportUI.repaint();

					int iCount = lstListener.size();
					for (int i = 0; i < iCount; i++) {// 事件执行

						((ISelectionListener) (lstListener.get(i)))
								.selectionChangeed(reportUI.getCellSelection(),
										curCell, tempCell);

					}
					e.setKeyCode(0);
				}
			}
		});
		// 添加复制功能

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8841691294182600772L;

	// 初始化界面
	private void init() throws Exception {
		report = new WorkSheet();
		reportUI = new JWorkSheet(report);
		reportUI.setEditable(false);
		reportUI.setColumnEndless(false);
		reportUI.setRowEndless(false);
		this.setLayout(new BorderLayout());
		this.add(reportUI, BorderLayout.CENTER);

	}

	public void dispData(String sReportID, boolean isGetData) throws Exception {
		this.reportID = sReportID;
		if (reportID == null)
			return;
		report.removeAllCellElements();
		reportUI.setReport(report);
		reportUI.updateUI();
		if (isGetData) {
			getData();
		}
		dispConsCells();

		dispRorCells();
		reportUI.repaint();
		parent.isNeedFresh = false;
	}

	public void dispData(String sReportID) throws Exception {
		this.dispData(sReportID, true);
	}

	/**
	 * 取得各种数据
	 * 
	 */
	private void getData() throws Exception {

		lstConsCell = SzzbSetI.getMethod().getSzzbCons(this.reportID,
				Global.getSetYear());
		List lstFor = SzzbSetI.getMethod().getSzzbFor(reportID,
				Global.getSetYear());
		if (lstFor == null || lstFor.size() == 0)
			return;
		xmlForCell.clear();
		int iCount = lstFor.size();
		XMLData aData;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstFor.get(i);
			CellFieldInfo aField = new CellFieldInfo(aData);
			xmlForCell.put(
					"" + aField.getCellRow() + ":" + aField.getCellCol(),
					aField);
		}
		// 取得表头的区域信息

	}

	public void dispConsCells() {

		if (lstConsCell == null || lstConsCell.size() == 0)
			return;
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;

		// int colStart = thDetail.getColumns();
		Object oValue;
		Cell cellElement;
		XMLData aData;
		int iCount = lstConsCell.size();
		CellSelection cells = ReportUtil
				.translateToNumber(parent.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString());
		titleStart = cells.getRow();
		bodyStart = titleStart + cells.getRowSpan();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstConsCell.get(i);
			iColumn = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMN));
			iColumnSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMNSPAN));
			iRow = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROW));
			iRowSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROWSPAN));
			oValue = Common.getAStringField(aData, ISzzbSet.FIELD_VALUE);

			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// 设格式
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.HOR_ALIGNMENT))).deriveVerticalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.VER_ALIGNMENT)));
			FRFont aFont = FRFont.getInstance(Common.getAStringField(aData,
					ISzzbSet.FIELD_FONT), Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_FONTSTYPE)), Integer
					.parseInt(Common.getAStringField(aData,
							ISzzbSet.FIELD_FONTSIZE)));
			style = style.deriveFRFont(aFont);

			if (iRow >= titleStart) {
				style = style.deriveBorder(1, Color.BLACK, 1, Color.BLACK, 1,
						Color.BLACK, 1, Color.BLACK);
			}

			cellElement.setStyle(style);
			report.setColumnWidth(iColumn, Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_COLWIDTH)));
			report.setRowHeight(iRow, Integer.parseInt(Common.getAStringField(
					aData, ISzzbSet.FIELD_ROWHEIGHT)));
			report.addCellElement(cellElement, false);
		}
	}

	public void dispRorCells() {
		if (xmlForCell.size() < 1)
			return;
		Iterator it = xmlForCell.values().iterator();
		int col, row;
		String sTitle;
		while (it.hasNext()) {
			CellFieldInfo aCell = (CellFieldInfo) it.next();
			col = aCell.getCellCol();
			row = aCell.getCellRow();
			sTitle = aCell.getTitle();
			CellElement eleCell = report.getCellElement(col, row);
			if (eleCell == null) {
				eleCell = new CellElement(col, row);
				report.addCellElement(eleCell);
			}
			Style redStyle = eleCell.getStyle();
			FRFont aFont = redStyle.getFRFont();
			aFont = aFont.applyForeground(Color.RED);
			redStyle = redStyle.deriveFRFont(aFont);
			eleCell.setStyle(redStyle);
			eleCell.setValue(sTitle);
			aCell.setParent(reportUI);

		}

	}

	public CellElement getEditCell() {
		CellSelection cs = reportUI.getCellSelection();
		if (cs == null)
			return null;
		return report.getCellElement(cs.getEditColumn(), cs.getEditRow());

	}

	public boolean addSelectLisener(ISelectionListener l) {
		if (l == null)
			return false;
		return this.lstListener.add(l);

	}

	public boolean removeSelectLisener(ISelectionListener l) {
		return this.lstListener.remove(l);
	}

	// 用于中途设置报表ID
	public void setNewReportID(String ID) {
		CellFieldInfo aCellInfo = null;
		if (xmlForCell.size() > 0) {
			Iterator it = xmlForCell.values().iterator();
			while (it.hasNext()) {
				aCellInfo = (CellFieldInfo) it.next();
				aCellInfo.setReportID(ID);
			}
		}
	}

	public String saveCols() {
		List lstSql = new ArrayList();
		String sSql;
		CellFieldInfo aCellInfo = null;
		if (xmlForCell.size() > 0) {
			Iterator it = xmlForCell.values().iterator();
			while (it.hasNext()) {
				try {
					aCellInfo = ((CellFieldInfo) it.next());
					sSql = aCellInfo.getInsertSql();
					if (Common.isNullStr(sSql))
						continue;
					lstSql.add(sSql);
				} catch (Exception e) {
					// this.reportUI.setCellSelection(new
					// CellSelection(aCellInfo
					// .getCellCol(), aCellInfo.getCellRow(), 1, 1));
					e.printStackTrace();
					return e.getMessage();

				}
			}

		}
		try {
			SzzbSetI.getMethod().saveFillCols(lstSql, reportID);
		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		}
		return "";

	}

	/**
	 * 插入或删除行时，校验行号、公式、表头和标题区域 add by zlx
	 * 
	 * @param rowOrCol
	 *            行号或列号值
	 * @param insertOrDelFlag
	 *            插入或删除
	 * @param rowOrColFlag
	 *            行或列
	 * @throws Exception
	 */
	public void checkInsertOrDelRowCol(int rowOrCol, int insertOrDelFlag,
			int rowOrColFlag) throws Exception {
		int cellRowOrCol;
		XMLData xmlForCellTmp = new XMLData();
		// 校验xmlForCell行号或列号（fb_u_qr_szzbcell)
		CellFieldInfo aCellInfo = null;
		if (xmlForCell.size() > 0) { // xmlForCell的key=行+列
			Iterator it = xmlForCell.values().iterator();
			while (it.hasNext()) {
				aCellInfo = ((CellFieldInfo) it.next());
				if (aCellInfo.getSourceType() == CellFieldInfo.SOURCETYPE_FOR) {// 公式
					String formula = aCellInfo.getFormula();
					// 校验公式
					formula = checkFormula(formula, rowOrCol, insertOrDelFlag,
							rowOrColFlag);
					aCellInfo.setFormula(formula);
				}
			}

			it = xmlForCell.values().iterator();
			while (it.hasNext()) {
				aCellInfo = ((CellFieldInfo) it.next());
				if (rowOrColFlag == ROW_FLAG) {// 校验行号
					cellRowOrCol = aCellInfo.getCellRow();
					if (cellRowOrCol >= rowOrCol) {
						if (insertOrDelFlag == INSERT_FLAG) {// 插入
							cellRowOrCol++;
							aCellInfo.setCellRow(cellRowOrCol);
							xmlForCellTmp.put("" + aCellInfo.getCellRow() + ":"
									+ aCellInfo.getCellCol(), aCellInfo);
						} else {// 删除
							// cellRowOrCol == rowOrCol不加入xmlForCellTmp,相当于删除
							if (cellRowOrCol > rowOrCol) {
								cellRowOrCol--;
								aCellInfo.setCellRow(cellRowOrCol);
								xmlForCellTmp.put("" + aCellInfo.getCellRow()
										+ ":" + aCellInfo.getCellCol(),
										aCellInfo);
							}
						}
					} else {
						xmlForCellTmp.put("" + aCellInfo.getCellRow() + ":"
								+ aCellInfo.getCellCol(), aCellInfo);
					}
				} else {// 校验列号
					cellRowOrCol = aCellInfo.getCellCol();
					if (cellRowOrCol >= rowOrCol) {
						if (insertOrDelFlag == INSERT_FLAG) {// 插入
							cellRowOrCol++;
							aCellInfo.setCellCol(cellRowOrCol);
							xmlForCellTmp.put("" + aCellInfo.getCellRow() + ":"
									+ aCellInfo.getCellCol(), aCellInfo);
						} else {// 删除
							// cellRowOrCol == rowOrCol不加入xmlForCellTmp,相当于删除
							if (cellRowOrCol > rowOrCol) {
								cellRowOrCol--;
								aCellInfo.setCellCol(cellRowOrCol);
								xmlForCellTmp.put("" + aCellInfo.getCellRow()
										+ ":" + aCellInfo.getCellCol(),
										aCellInfo);
							}
						}
					} else {
						xmlForCellTmp.put("" + aCellInfo.getCellRow() + ":"
								+ aCellInfo.getCellCol(), aCellInfo);
					}
				}

			}
			xmlForCell = xmlForCellTmp;
		}

		// 校验标题和表头获得区域
		checkTitleAndHeaderArea(rowOrCol, insertOrDelFlag, rowOrColFlag);

		// 校验lstConsCell行号（fb_u_qr_szzb)
		if (lstConsCell == null)
			return;
		XMLData aData;
		int iCount = lstConsCell.size();
		// 保存需删除记录的位置
		List lstSaveDelPos = null;
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstConsCell.get(i);
			if (rowOrColFlag == ROW_FLAG) {// 校验行号
				cellRowOrCol = Integer.parseInt(Common.getAStringField(aData,
						ISzzbSet.FIELD_ROW));
				if (cellRowOrCol >= rowOrCol) {// 行号+1
					if (insertOrDelFlag == INSERT_FLAG) {
						cellRowOrCol++;
						aData.put(ISzzbSet.FIELD_ROW, String
								.valueOf(cellRowOrCol));
					} else {
						if (cellRowOrCol > rowOrCol) {
							cellRowOrCol--;
							aData.put(ISzzbSet.FIELD_ROW, String
									.valueOf(cellRowOrCol));
						} else {// cellRowOrCol = rowOrCol
							if (lstSaveDelPos == null)
								lstSaveDelPos = new ArrayList();
							lstSaveDelPos.add(String.valueOf(i));
						}
					}
				}
			}
		}
		// 删除记录
		if (lstSaveDelPos != null) {
			int size = lstSaveDelPos.size();
			for (int i = size - 1; i >= 0; i--) {
				lstConsCell.remove(lstSaveDelPos.get(i));
			}
		}
	}

	/**
	 * 校验公式(增加或删除行时）
	 * 
	 * @param formula
	 *            公式(例{B11}+{B12})
	 * @param rowOrCol
	 *            行号或列号
	 * @param insertOrDelFlag
	 *            插入或删除
	 * @param rowOrColFlag
	 *            行或列
	 * @return
	 */
	private String checkFormula(String formula, int rowOrCol,
			int insertOrDelFlag, int rowOrColFlag) {
		int pos = 0;
		int indexStart = formula.indexOf("{", pos);
		int indexEnd;
		while (indexStart != -1) {
			indexEnd = formula.indexOf("}", pos);
			String cellValue = formula.substring(indexStart + 1, indexEnd);
			String newCellValue = getOneCellValue(rowOrCol, insertOrDelFlag,
					cellValue, rowOrColFlag);

			// 替换
			if (!cellValue.equals(newCellValue))
				formula = formula.substring(0, indexStart + 1) + newCellValue
						+ formula.substring(indexEnd);
			// 查询开始位置
			int cellRow = Integer.parseInt(Common.trimToNumber(cellValue)) - 1;
			int newCellRow = Integer
					.parseInt(Common.trimToNumber(newCellValue));
			pos = indexEnd + 1 + String.valueOf(newCellRow).length()
					- String.valueOf(cellRow).length();
			indexStart = formula.indexOf("{", pos);
		}
		return formula;
	}

	/**
	 * 得到一个单位元格(B12)插入或删除行新位置值
	 * 
	 * @param rowOrCol
	 *            行号或列号值
	 * @param insertOrDelFlag
	 *            插入或删除
	 * @param cellValue
	 * @param rowOrColFlag
	 * @return
	 */
	private String getOneCellValue(int rowOrCol, int insertOrDelFlag,
			String cellValue, int rowOrColFlag) {
		int cellRow = Integer.parseInt(Common.trimToNumber(cellValue));
		int cellCol = getColValue(cellValue);
		if ((rowOrColFlag == ROW_FLAG && (cellRow-1) < rowOrCol)
				|| (rowOrColFlag == COL_FLAG && cellCol < rowOrCol)) { // 增删行或列车员
			return cellValue;
		}

		if (rowOrColFlag == ROW_FLAG) {// 增删行
			if (insertOrDelFlag == INSERT_FLAG) {
				cellRow++;
			} else {
				cellRow--;
			}
		} else {// 增删列
			if (insertOrDelFlag == INSERT_FLAG) {
				cellCol++;
			} else {
				cellCol--;
			}
		}
		String colName = ReportUtil.translateToColumnName(cellCol);
		return colName + cellRow;
	}

	/**
	 * 得到列号
	 * 
	 * @param cellValue单元格值(例B16)
	 * @return
	 */
	private int getColValue(String cellValue) {
		byte[] bytes = new byte[2];
		bytes = Common.trimNumberToStr(cellValue).getBytes();
		int col = 0, j = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			j++;
			col = col + 25 * (j - 1) * (bytes[i] - 65 + 1)
					+ (bytes[i] - 65 + 1);
		}
		col = col - 1;
		return col;
	}

	/**
	 * 校验标题和表头选择区域
	 * 
	 * @param rowOrCol
	 *            行号或列号值
	 * @param insertOrDelFlag
	 *            插入或删除
	 */
	private void checkTitleAndHeaderArea(int rowOrCol, int insertOrDelFlag,
			int rowOrColFlag) {
		// 标题
		String titleArea = parent.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
				.getValue().toString();
		parent.ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
				.setValue(chekcOneAreaRow(rowOrCol, insertOrDelFlag, titleArea,
						rowOrColFlag));

		// 表头
		String headerArea = parent.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
				.getValue().toString();
		parent.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
				.setValue(chekcOneAreaRow(rowOrCol, insertOrDelFlag,
						headerArea, rowOrColFlag));
	}

	/**
	 * 校验某一选择区域(A1:C12)
	 * 
	 * @param rowOrCol
	 *            行号或列号值
	 * @param insertOrDelFlag
	 *            插入或删除
	 * @param areaValue
	 *            区域值
	 * @param rowOrColFlag
	 *            行或列
	 * @return
	 */
	private String chekcOneAreaRow(int rowOrCol, int insertOrDelFlag,
			String areaValue, int rowOrColFlag) {
		String[] areaArr = areaValue.split(":");
		String areaStart = getOneCellValue(rowOrCol, insertOrDelFlag,
				areaArr[0], rowOrColFlag);
		String areaEnd = getOneCellValue(rowOrCol, insertOrDelFlag, areaArr[1],
				rowOrColFlag);
		return areaStart + ":" + areaEnd;
	}
}
