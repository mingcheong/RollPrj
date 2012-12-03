/**
 * @# RowTable.java    <�ļ���>
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
 * ����˵��:�ɽ����϶���TABLE,������ʾ���е����б�
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
		// TODO �Զ����ɷ������
		isOnDrag = true;
	}

	public void dragExit(DropTargetEvent dte) {
		// TODO �Զ����ɷ������
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
			isOnDrag = false;
		} catch (IOException e) {
			// TODO �Զ����� catch ��
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

		aCol.setTitle("��Ŀ��");

		aCol.setId(RowInfo.FIELD_ITEM);
		aCol.setIdentifier(RowInfo.FIELD_ITEM);
		aCol.setWidth(200);
		aCol.setEditable(false);
		aCol.setSortable(false);
		this.addColumn(aCol);
		aCol = new FBaseTableColumn();
		aCol.setTitle("������Ϣ");
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
	 * ��ʼ�����ݣ��ڲ�ѯ�����б���Ҫ���һ�����У�������ʾ�����е�ֵ
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

	// ���һ���ֶ�,ֵΪ������ѡ�е�
	// ע������ rowDetailPanel�еĺ�����Щ�ظ���������û�������䷽��
	public void appendField(XMLData aField) {
		RowInfo aRow = parent.getcurRow();
		if (aField == null || aRow == null)
			return;

		// �����ǰû��ȷ������Դ��
		if (Common.isNullStr(aRow.getTableName())) {
			aRow.setSourceID((String) aField.get(IDataDictBO.DICID));
			aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));

		}

		String sOldTableID = aRow.getSourceID();
		String sNewTableID = (String) aField.get(IDataDictBO.DICID);
		// ���ѡ������࣬������

		if (IDataDictBO.MARK_TYPE.equals(aField.get(IDataDictBO.fieldMark))) {
			return;
		}
		// ���ѡ����Ǳ�
		if (IDataDictBO.MARK_TABLE.equals(aField.get(IDataDictBO.fieldMark))) {
			// ������ϼ��Ľڵ㣬��ֻ���ñ�
			if (!sOldTableID.equals(sNewTableID)) {// �������򲻱䶯��
				// ������ȣ���Ҫˢ������Դ
				aRow.setSourceID(sNewTableID);
				aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
				aRow.setFields(null);
			}
			parent.fireSelectChange();
			return;
		}
		// ������б���Ҫ�Ա�һ��
		// ���ѡ������ֶ�
		if (sOldTableID.equals(sNewTableID)) {// �����ȣ���������
			aRow.addOperField((String) aField.get(IDataDictBO.AFIELD_ENAME));

		} else {// ������ȣ���Ҫˢ������Դ
			// modified by xxl ��������˳��ԭ����˳��᲻������ʾ�䶯
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
