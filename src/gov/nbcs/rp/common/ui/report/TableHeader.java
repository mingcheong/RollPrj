/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ���з���Ч���Ķ��ͷ
 * 
 * @description �����������ݽṹ���ĵ���㣬������Ӧ�Ľ���
 * 
 * @author Ǯ�Գ�
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
	 * ��ͷ��Ԫ��ķ��
	 */
	private Style cellStyle;

	public static Style PRINTING_STYLE;

	private int iTitleRows = 0;// XXL����¼��ͷ����
	
	private Node documentRoot;

	static {
		TableHeader.createDefaultHeaderPrintingStyle();
	}

	/**
	 * �ĵ��ṹ�������
	 */
	private int treeHeight;

	/**
	 * �ĵ��ṹ���Ŀ��
	 */
	private int treeWidth;

	/**
	 * ����ȡ���ݵ��ֶ�
	 */
	private List fields = new ArrayList();

	/**
	 * ��ͷ��Ԫ�񼯺�
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
	 * ͨ���ĵ��ṹ���ڵ�����ɱ�ͷ
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
	 * ͨ���ĵ��ṹ���ڵ�����ɱ�ͷ
	 * 
	 * @param documentRoot
	 */
	public TableHeader(Node documentRoot) {
		this(documentRoot, 0, 0);
	}

	/**
	 * add BY xxl�����ʾ������ı���
	 * 
	 * @param documentRoot
	 * @param title
	 *            ����
	 * @param isNeedYuan
	 *            �Ƿ���Ԫ
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
							FRFont.getInstance(new Font("����", Font.BOLD, 30)));
			cellTitle.setStyle(astyle);
			cellTitle.setCellGUIAttr(this.defaultCellAttr);
			cells.add(cellTitle);
			rowStart = 1;
			iTitleRows = 1;// ��ӱ�����
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
			iTitleRows = iTitleRows + 1;// ��ӱ�����
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
	 * ���ñ�ͷ��ɫ
	 */
	public void setColor(Color color) {
		cellStyle = cellStyle.deriveBackground(ColorBackground
				.getInstance(color));
	}

	/**
	 * ���ñ߿��ȡ���ɫ
	 */
	public void setBorder(int thickness, Color borderColor) {
		cellStyle = cellStyle.deriveBorder(thickness, borderColor, thickness,
				borderColor, thickness, borderColor, thickness, borderColor);
	}

	/**
	 * ���ñ�ͷ��Ԫ������
	 * 
	 * @param font
	 */
	public void setFont(Font font) {
		cellStyle = cellStyle.deriveFRFont(FRFont.getInstance(font));
	}

	/**
	 * ����Ĭ�ϵı�ͷ��Ԫ����
	 * 
	 * @return
	 */
	protected Style createDefaultHeaderStyle() { 
		return CellStyle.HEADER_DEFAULT;
	}

	/**
	 * ����Ĭ�ϵı�ͷ��Ԫ���ӡ���
	 * 
	 * @return
	 */
	protected static Style createDefaultHeaderPrintingStyle() {
		PRINTING_STYLE = CellStyleFactory.create(Constants.CENTER,
				CellStyle.BackgroundColors.HEADER_COLOR,Color.LIGHT_GRAY,
				new Font("����", Font.PLAIN, 12));
		return PRINTING_STYLE;
	}

	/**
	 * ��ȡ�ֶ����֣���ĩ����㣬ȡ��ʹ�ã�
	 * 
	 * @return
	 */
	public List getFields() throws Exception {
		return this.fields;
	}

	/**
	 * ��ȡ��ͷ��Ԫ���Cell��������
	 * 
	 * @return
	 */
	public List getHeaderCells() {
		return this.cells;
	}

	/**
	 * ���ݽ����Ϣ���ɵ�Ԫ��
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
	 * ��ȡ��ͷռ�õ���
	 * 
	 * @return
	 */
	public int getRows() {
		return getStartRow() + treeHeight;
	}

	/**
	 * ��ȡ��ͷռ�õ���
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
