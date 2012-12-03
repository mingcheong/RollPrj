/**
 * @# RowTable.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.List;

import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:可接受拖动的TABLE,用于显示所有的行列表
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class RowTable extends FTable implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8096795850632687619L;

	private RowPanel parent;

	public static final String FIELD_DISP = "TITLE";

	private boolean isSupportDrop = true;

	private boolean isOnDrag = false;

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO 自动生成方法存根
		isOnDrag = true;
	}

	public void dragExit(DropTargetEvent dte) {
		// TODO 自动生成方法存根
		isOnDrag = false;

	}

	public void dragOver(DropTargetDragEvent dtde) {
		doMounseClick(dtde.getLocation());
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
		isOnDrag = true;

	}

	public void drop(DropTargetDropEvent dtde) {
		Transferable tr = dtde.getTransferable();
		DataFlavor[] flavors = tr.getTransferDataFlavors();

		if (flavors.length < 2
				|| !tr.isDataFlavorSupported(flavors[ObjectSelection.OBJECT])) {
			dtde.rejectDrop();
			isOnDrag = false;
			return;
		}
		if (parent.getcurRow().getMeasureType() != RowInfo.MEASURE_SUM
				&& parent.getcurRow().getMeasureType() != RowInfo.MEASURE_COUNT) {
			dtde.rejectDrop();
			return;
		}
		try {
			Object userObj = tr
					.getTransferData(flavors[ObjectSelection.OBJECT]);
			if (!(userObj instanceof XMLData)) {
				dtde.rejectDrop();
				return;
			}
			appendField((XMLData) userObj);
			dtde.dropComplete(true);

		} catch (UnsupportedFlavorException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			isOnDrag = false;
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			isOnDrag = false;
		}
		isOnDrag = false;

	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		isOnDrag = false;

	}

	public RowTable(RowPanel parent) {
		this.parent = parent;
		init();
	}

	private void init() {
		this.setRowSelectionAllowed(true);
		this.getRightActiveTable().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		initCols();
	}

	private void initCols() {
		FBaseTableColumn aCol = new FBaseTableColumn();

		aCol.setTitle("项目名");

		aCol.setId(RowInfo.FIELD_ITEM);
		aCol.setIdentifier(RowInfo.FIELD_ITEM);
		aCol.setWidth(200);
		aCol.setEditable(false);
		aCol.setSortable(false);
		this.addColumn(aCol);
		aCol = new FBaseTableColumn();
		aCol.setTitle("设置信息");
		aCol.setId(FIELD_DISP);
		aCol.setIdentifier(FIELD_DISP);
		aCol.setWidth(500);
		aCol.setEditable(false);
		aCol.setSortable(false);
		aCol.setForeground("#FF0000");
		this.addColumn(aCol);
		new DropTarget(this.getRightActiveTable(), this);
		// new DropTarget(this, this);
		initData();
	}

	/**
	 * 初始化数据，在查询出的列表中要添加一个新列，用于显示设置列的值
	 * 
	 */
	public void initData() {
		List lstData = parent.getLstXMLRows();
		if (lstData != null && lstData.size() > 0) {
			int iCount = lstData.size();
			for (int i = 0; i < iCount; i++) {
				XMLData aData = (XMLData) lstData.get(i);
				String itemID = aData.get(RowInfo.FIELD_ITEM_ID).toString();
				Object obj = parent.getXmlRows().get(itemID);
				if (obj == null)
					return;
				aData.put(FIELD_DISP, ((RowInfo) obj).getTitle(parent
						.getXmlTableSource(), (List) parent.getXmlFields().get(
						((RowInfo) obj).getSourceID())));
			}
		}
		this.setData(lstData);
		this.updateUI();

	}

	public void doMounseClick(Point pt) {
		int iPos = this.getRightActiveTable().rowAtPoint(pt);
		if (iPos != -1 && iPos <= this.getRowCount())
			this.setRowSelectionInterval(iPos, iPos);

	}

	public boolean isSupportDrop() {
		return isSupportDrop;
	}

	public void setSupportDrop(boolean isSupportDrop) {
		this.isSupportDrop = isSupportDrop;
	}

	// 添加一个字段,值为在树上选中的
	// 注这里与 rowDetailPanel中的函数有些重复，但这里没法调用其方法
	public void appendField(XMLData aField) {
		RowInfo aRow = parent.getcurRow();
		if (aField == null || aRow == null)
			return;

		// 如果先前没有确定数据源表
		if (Common.isNullStr(aRow.getTableName())) {
			aRow.setSourceID((String) aField.get(IDataDictBO.DICID));
			aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));

		}

		String sOldTableID = aRow.getSourceID();
		String sNewTableID = (String) aField.get(IDataDictBO.DICID);
		// 如果选择的是类，则跳出

		if (IDataDictBO.MARK_TYPE.equals(aField.get(IDataDictBO.fieldMark))) {
			return;
		}
		// 如果选择的是表
		if (IDataDictBO.MARK_TABLE.equals(aField.get(IDataDictBO.fieldMark))) {
			// 如果是上级的节点，则只设置表
			if (!sOldTableID.equals(sNewTableID)) {// 如果相等则不变动，
				// 如果不等，则要刷新数据源
				aRow.setSourceID(sNewTableID);
				aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
				aRow.setFields(null);
			}
			parent.fireSelectChange();
			return;
		}
		// 如果已有表，则要对比一下
		// 如果选择的是字段
		if (sOldTableID.equals(sNewTableID)) {// 如果相等，则继续添加
			aRow.addOperField((String) aField.get(IDataDictBO.AFIELD_ENAME));

		} else {// 如果不等，则要刷新数据源
			// modified by xxl 调整设置顺序，原来的顺序会不触发显示变动
			aRow.setSourceID(sNewTableID);
			aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
			aRow.setFields((String) aField.get(IDataDictBO.AFIELD_ENAME));

		}
		parent.fireSelectChange();

	}

	public boolean isOnDrag() {
		return isOnDrag;
	}

	public void setSelectRow(int i) {
		if (this.getData() == null)
			return;
		if (i < 0 || i >= this.getData().size())
			return;
		this.getRightActiveTable().setRowSelectionInterval(i, i);
	}
}
