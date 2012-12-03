package gov.nbcs.rp.basinfo.common;

import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.control.table.FTableColumnItem;
import com.foundercy.pf.control.table.FTableModel;
import com.foundercy.pf.util.XMLData;

public class BaseUtil {

	public static String getAStringField(List lstData, String sField) {
	
		if ((lstData == null) || (lstData.size() == 0) || (sField == null)
				|| sField.equals(""))
			return null;
		Map aData = (Map) lstData.get(0);
		return getAStringField(aData, sField);

	}


	
	public static String getFieldQuoted(String field) {
		if (field == null || field.equals(""))
			return "null";
		return "'" + field + "'";
	}
	

	public static String getAStringField(Map aData, String sField) {
		if (aData == null)
			return null;
		if (aData.get(sField) == null)
			return null;
		return aData.get(sField).toString();
	}
	public static boolean signFirstBySpecMutiFields(FTree aTree,
			String[] fields, String[] values, DefaultMutableTreeNode aNode) {
		if (fields == null || values == null)
			return false;
		if (aNode == null)
			aNode = aTree.getRoot();
		// 先判断自身
		if ((PfTreeNode) aNode.getUserObject() != null) {
			XMLData aData = (XMLData) ((PfTreeNode) aNode.getUserObject())
					.getCustomerObject();
			if (aData != null) {
				int i = 0;
				for (; i < fields.length; i++) {
					Object obj = aData.get(fields[i]);
					if (obj == null)
						break;
					if (!obj.toString().equals(values[i]))
						break;
				}
				if (i == fields.length) {// 如果都相等
					if (aTree.getIsCheck()) {
						((PfTreeNode) aNode.getUserObject()).setIsSelect(true);
						aTree.setSelectionPath(new TreePath(aNode.getPath()));

					} else {
						aTree.setSelectionPath(new TreePath(aNode.getPath()));
						aTree.expandPath(new TreePath(
								((DefaultMutableTreeNode) aNode.getParent())
										.getPath()));
					}
					return true;
				}

			}
		}
		// 如果没找到则向下
		int iChildCount = aNode.getChildCount();
		if (iChildCount < 1)
			return false;
		for (int i = 0; i < iChildCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) aNode
					.getChildAt(i);
			if (signFirstBySpecMutiFields(aTree, fields, values, childNode))
				return true;
		}

		return false;

	}
	public static boolean setTableColEditable(FTable fTable, String colId,
			boolean isEditable) {
		if (fTable == null || colId == null || colId.equals(""))
			return false;
		List lstCol = ((FTableModel) fTable.getModel()).getColumnList();
		int iCount = lstCol.size();
		for (int i = 0; i < iCount; i++) {
			if (((FTableColumnItem) lstCol.get(i)).getId().equals(colId)) {
				((FTableColumnItem) lstCol.get(i)).setEditable(isEditable);
				return true;
			}
		}
		return false;
	}
	
	public static String getSelectFieldLeafOnTree(CustomTree aTree,
			String fieldName) throws Exception {
		if (Common.isNullStr(fieldName))
			throw new Exception("没有指定获取的字段名");
		StringBuffer sbField = new StringBuffer();
		MyTreeNode[] nodes = aTree.getSelectedNodes(true);
		DataSet dsData = aTree.getDataSet();
		if (dsData == null || nodes == null)
			return "";
		dsData.maskDataChange(true);
		int iCount = nodes.length;
		MyTreeNode aNode;

		for (int i = 0; i < iCount; i++) {
			aNode = nodes[i];
			dsData.gotoBookmark(aNode.getBookmark());
			sbField.append(dsData.fieldByName(fieldName).getString()).append(
					";");
		}
		return sbField.substring(0, sbField.length() - 1);

	}


}
