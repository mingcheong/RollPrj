/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 带有分组效果的多表头
 * 
 * @description 根据树型数据结构的文档结点，生成相应的界面
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.tree.Node; 
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.report.cell.CellStyle;
import gov.nbcs.rp.common.ui.report.cell.CellStyleFactory;
import com.fr.base.Constants;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.report.cellElement.CellGUIAttr;

public class TableHeader implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * 表头单元格的风格
	 */
	private Style cellStyle;

	public static Style PRINTING_STYLE;

	private int iTitleRows = 0;// XXL，记录表头行数
	
	private Node documentRoot;

	static {
		TableHeader.createDefaultHeaderPrintingStyle();
	}

	/**
	 * 文档结构树的深度
	 */
	private int treeHeight;

	/**
	 * 文档结构树的宽度
	 */
	private int treeWidth;

	/**
	 * 表体取数据的字段
	 */
	private List fields = new ArrayList();

	/**
	 * 表头单元格集合
	 */
	private List cells = new ArrayList();

	private CellGUIAttr defaultCellAttr = new CellGUIAttr();

	private int startRow;

	private int startCol;

	public int getStartCol() {
		return startCol;
	}

	protected void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	protected void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * 通过文档结构根节点解析成表头
	 * 
	 * @param documentRoot
	 */
	public TableHeader(Node documentRoot, int rowStart, int colStart) {
		this.cellStyle = createDefaultHeaderStyle();
		this.documentRoot=documentRoot;
		treeHeight = documentRoot.getHeight();
		treeWidth = documentRoot.getWidth();
		this.setStartCol(colStart);
		this.setStartRow(rowStart);
		for (int i = 0; i < documentRoot.getChildrenCount(); i++) {
			colStart += parseNode(documentRoot.getChildAt(i), rowStart,
					colStart);
		}
		defaultCellAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);
	}

	/**
	 * 通过文档结构根节点解析成表头
	 * 
	 * @param documentRoot
	 */
	public TableHeader(Node documentRoot) {
		this(documentRoot, 0, 0);
	}

	/**
	 * add BY xxl添加显示带标题的报表
	 * 
	 * @param documentRoot
	 * @param title
	 *            标题
	 * @param isNeedYuan
	 *            是否有元
	 */
	public TableHeader(Node documentRoot, String title, String seTitle) {

		int rowStart = 0;
		int colStart = 0;
		this.documentRoot=documentRoot;
		treeHeight = documentRoot.getHeight();
		treeWidth = documentRoot.getWidth();
		if ((title != null) && !title.equals("")) {
			Cell cellTitle = new Cell(0, 0, treeWidth, 1, title);

			Style astyle = createDefaultHeaderStyle().deriveBackground(
					ColorBackground.getInstance(UntPub.HEADER_COLOR))
					.deriveFRFont(
							FRFont.getInstance(new Font("宋体", Font.BOLD, 30)));
			cellTitle.setStyle(astyle);
			cellTitle.setCellGUIAttr(this.defaultCellAttr);
			cells.add(cellTitle);
			rowStart = 1;
			iTitleRows = 1;// 添加标题行
		}
		if ((seTitle != null) && !seTitle.equals("")) {
			Cell cellYuan = new Cell(0, rowStart, treeWidth, 1, seTitle);
			Style astyle = createDefaultHeaderStyle()
					.deriveHorizontalAlignment(Constants.RIGHT)
					.deriveBackground(
							ColorBackground.getInstance(UntPub.HEADER_COLOR));
			cellYuan.setStyle(astyle);
			cellYuan.setCellGUIAttr(this.defaultCellAttr);
			cells.add(cellYuan);
			rowStart = rowStart + 1;
			iTitleRows = iTitleRows + 1;// 添加标题行
		}

		this.cellStyle = createDefaultHeaderStyle();

		this.setStartCol(colStart);
		this.setStartRow(rowStart);
		// treeHeight=rowStart+treeHeight;
		for (int i = 0; i < documentRoot.getChildrenCount(); i++) {
			colStart += parseNode(documentRoot.getChildAt(i), rowStart,
					colStart);
		}
		defaultCellAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);

	}

	/**
	 * 设置表头颜色
	 */
	public void setColor(Color color) {
		cellStyle = cellStyle.deriveBackground(ColorBackground
				.getInstance(color));
	}

	/**
	 * 设置边框宽度、颜色
	 */
	public void setBorder(int thickness, Color borderColor) {
		cellStyle = cellStyle.deriveBorder(thickness, borderColor, thickness,
				borderColor, thickness, borderColor, thickness, borderColor);
	}

	/**
	 * 设置表头单元格字体
	 * 
	 * @param font
	 */
	public void setFont(Font font) {
		cellStyle = cellStyle.deriveFRFont(FRFont.getInstance(font));
	}

	/**
	 * 创建默认的表头单元格风格
	 * 
	 * @return
	 */
	protected Style createDefaultHeaderStyle() { 
		return CellStyle.HEADER_DEFAULT;
	}

	/**
	 * 创建默认的表头单元格打印风格
	 * 
	 * @return
	 */
	protected static Style createDefaultHeaderPrintingStyle() {
		PRINTING_STYLE = CellStyleFactory.create(Constants.CENTER,
				CellStyle.BackgroundColors.HEADER_COLOR,Color.LIGHT_GRAY,
				new Font("宋体", Font.PLAIN, 12));
		return PRINTING_STYLE;
	}

	/**
	 * 获取字段名字（最末级结点，取数使用）
	 * 
	 * @return
	 */
	public List getFields() throws Exception {
		return this.fields;
	}

	/**
	 * 获取表头单元格的Cell对象数组
	 * 
	 * @return
	 */
	public List getHeaderCells() {
		return this.cells;
	}

	/**
	 * 根据结点信息生成单元格
	 */
	protected int parseNode(Node node, int rowStart, int colStart) {
		int rowSpan = 1;
		int colSpan = 1;
		switch (node.getNodeType()) {
		case Node.BRANCH:
			colSpan = node.getWidth();
			break;
		case Node.LEAF:
			rowSpan = treeHeight - node.getLevel() + 1;
			fields.add(node.getIdentifier());
			break;
		}
		createCell(cells, rowStart, colStart, rowSpan, colSpan, node.getText(),
				Common.nonNullStr(node.getValue()));
		for (int i = 0; i < node.getChildrenCount(); i++) {
			colStart += parseNode(node.getChildAt(i), rowStart + rowSpan,
					colStart);
		}
		return colSpan;
	}

	protected void createCell(Collection cellsBuffer, int rowStart,
			int colStart, int rowSpan, int colSpan, String text, String bookmark) {
		Cell cell = new Cell(colStart, rowStart, colSpan, rowSpan, text);
		cell.setCellType(Cell.HEADER_CELL);
		cell.setBookmark(bookmark);
		cell.setStyle(cellStyle);
		cell.setCellGUIAttr(this.defaultCellAttr);
		cellsBuffer.add(cell);
	}

	/**
	 * 获取表头占用的行
	 * 
	 * @return
	 */
	public int getRows() {
		return getStartRow() + treeHeight;
	}

	/**
	 * 获取表头占用的列
	 */
	public int getColumns() {
		return treeWidth;
	}

	public int getITitleRows() {
		return iTitleRows;
	}

	public void setITitleRows(int titleRows) {
		iTitleRows = titleRows;
	}

	public Node getDocumentRoot() {
		return documentRoot;
	}
}
