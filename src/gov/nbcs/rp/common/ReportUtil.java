package gov.nbcs.rp.common;

import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.tree.TreeFactory;

import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.WorkSheet;

/**
 * <p>
 * Title: Common
 * </p>
 * <p>
 * Description: 常用的FineReport工具类，包含一些通用的算法
 * </p>
 * @version 1.0
 */
public class ReportUtil {
	public static String translateToColumnName(int columnIndex) {
		String columnName = "";
		byte[] bytes = new byte[1];
		if (columnIndex > 25) {
			bytes[0] = (byte) (columnIndex / 26 - 1 + 65);
			columnName = new String(bytes);
		}
		bytes[0] = (byte) ((columnIndex % 26) + 65);
		columnName = columnName + new String(bytes);

		return columnName;
	}

	// 单元格所在区域转换为起始行、起始列、结束行、结束列
	public static CellSelection translateToNumber(String columnName) {
		int j = 0, col = 0, row = 0, colSpan = 0, rowSpan = 0;
		if ("".equals(columnName)) {
			return null;
		}
		String[] area = new String[2];
		area = columnName.split(":");
		row = Integer.parseInt(Common.trimToNumber(area[0])) - 1;
		rowSpan = Integer.parseInt(Common.trimToNumber(area[1])) - row;
		byte[] bytes = new byte[2];
		bytes = Common.trimNumberToStr(area[0]).getBytes();
		j = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			j++;
			col = col + 25 * (j - 1)*(bytes[i] - 65+1) + (bytes[i] - 65 + 1);
		}
		col = col - 1;
		bytes = Common.trimNumberToStr(area[1]).getBytes();
		j = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			j++;
			colSpan = colSpan + 25 * (j - 1)*(bytes[i] - 65+1) + (bytes[i] - 65 + 1);
		}
		colSpan = colSpan - col;
		CellSelection cells = new CellSelection(col, row, col, row, colSpan,
				rowSpan);
		return cells;
	}

	/**
	 * 解析FineReport文档结构为树型数据结构
	 * 
	 * @param workSheet
	 * @param cs
	 * @return
	 */
	public static Node parseDocument(WorkSheet content, CellSelection cs) {
		Node root = TreeFactory.getInstance().createTreeNode(null);
		for (int i = cs.getColumn(); i < cs.getColumn() + cs.getColumnSpan();) {
			CellElement cell = content.getCellElement(i, cs.getRow());
			if (cell != null) {
				i += cell.getColumnSpan();
				Node node = TreeFactory.createTreeNode(
						UUID.randomUUID().toString(),
						Common.nonNullStr(cell.getValue()), null, cell, null);
				root.append(node);
				genSubCells(node, content, cs.getRow() + cs.getRowSpan());
			} else {
				i++;
			}
		}
		return root;
	}

	/**
	 * 解析FineReport文档结构为树型数据结构,add by zlx 2011-4-15
	 * 
	 * @param workSheet
	 * @param cs
	 * @return
	 */
	public static Node parseDocument(GroupReport content, CellSelection cs) {
		Node root = TreeFactory.getInstance().createTreeNode(null);
		for (int i = cs.getColumn(); i < cs.getColumn() + cs.getColumnSpan();) {
			CellElement cell = content.getCellElement(i, cs.getRow());
			if (cell == null) {
				cell = new CellElement(i, cs.getRow());
			}
			if (cell != null) {
				i += cell.getColumnSpan();
				Node node = TreeFactory.createTreeNode(
						UUID.randomUUID().toString(),
						Common.nonNullStr(cell.getValue()), null, cell, null);
				root.append(node);
				genSubCells(node, content, cs.getRow() + cs.getRowSpan());
			} else {
				i++;
			}
		}
		return root;
	}

	/**
	 * 获取单元格的直接下级单元格
	 * 
	 * @param branch
	 *            树枝结点
	 * @param content
	 *            报表内容
	 * @return
	 */
	protected static void genSubCells(Node branch, WorkSheet content,
			int rowSpan) {
		CellElement branchCell = (CellElement) branch.getValue();
		int row = branchCell.getRow() + branchCell.getRowSpan();// 紧贴branchCell的下层单元格的起始行号
		int column = branchCell.getColumn();
		if (row < rowSpan) {
			for (int i = column; i < column + branchCell.getColumnSpan();) {
				CellElement cell = content.getCellElement(i, row);
				if (cell != null) {
					i += cell.getColumnSpan();
					Node node = TreeFactory.createTreeNode(
							UUID.randomUUID().toString(),
							Common.nonNullStr(cell.getValue()), null, cell,
							null);
					branch.append(node);
					genSubCells(node, content, rowSpan);
				} else {
					i++;
				}
			}
		}
	}

	/**
	 * 
	 * 获取单元格的直接下级单元格,add by zlx 2011-4-15
	 * 
	 * @param branch
	 *            树枝结点
	 * @param content
	 *            报表内容
	 * @return
	 */
	protected static void genSubCells(Node branch, GroupReport content,
			int rowSpan) {
		CellElement branchCell = (CellElement) branch.getValue();
		int row = branchCell.getRow() + branchCell.getRowSpan();// 紧贴branchCell的下层单元格的起始行号
		int column = branchCell.getColumn();
		if (row < rowSpan) {
			for (int i = column; i < column + branchCell.getColumnSpan();) {
				CellElement cell = content.getCellElement(i, row);
				if (cell != null) {
					i += cell.getColumnSpan();
					Node node = TreeFactory.createTreeNode(
							UUID.randomUUID().toString(),
							Common.nonNullStr(cell.getValue()), null, cell,
							null);
					branch.append(node);
					genSubCells(node, content, rowSpan);
				} else {
					i++;
				}
			}
		}
	}
}
