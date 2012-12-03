package gov.nbcs.rp.common.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;

/**
 * 带查找功能的面板
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-7-24
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class SearchPanel extends FPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fieldName;

	private String fieldID;

	private CustomTree tree;

	private FTextField tf;

	private CustomTable table;
	int type = 0;

	public SearchPanel(Object comp, String fieldID, String fieldName) {
		if (comp instanceof CustomTree) {
			this.tree = (CustomTree) comp;
		} else if (comp instanceof CustomTable) {
			this.table = (CustomTable) comp;
			type = 1;
		}

		this.fieldID = fieldID;
		this.fieldName = fieldName;

		RowPreferedLayout lay = new RowPreferedLayout(6);
		this.setLayout(lay);
		lay.setColumnWidth(80);
		tf = new FTextField("  模糊查找:");
		tf.setProportion(0.4f);
		tf.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		FButton btnSerch = new FButton("serch", "查找");
		FButton btnSN = new FButton("serchnext", "下一个");
		this.addControl(tf, new TableConstraints(1, 2, false, false));
		this.addControl(btnSerch, new TableConstraints(1, 1, false, false));
		this.addControl(btnSN, new TableConstraints(1, 1, false, false));
		this.addControl(new FLabel(), new TableConstraints(1, 1, false, true));
		btnSerch.addActionListener(new actionListener(0));
		btnSN.addActionListener(new actionListener(1));
	}

	private class actionListener implements ActionListener, Runnable {

		private int isBegin;

		public actionListener(int isBegin) {
			this.isBegin = isBegin;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// Thread aa = new Thread(this);
			// aa.start();
			if (type == 0) {
				serchByTree(isBegin);
			} else {
				serchByTable(isBegin);
			}
		}

		public void run() {
			// TODO Auto-generated method stub
			// ProgressBar pfShow = null;
			// pfShow = new ProgressBar(Global.mainFrame, "正在查找数据，请稍候······",
			// false);
			// try {
			// serchDivByDataSet(isBegin);
			// } catch (Exception ee) {
			// ee.printStackTrace();
			// } finally {
			// // pfShow.dispose();
			// }
		}

	}

	private void serchByTree(int isBegin) {
		try {
			if (tree == null) {
				return;
			}
			if (tree.getDataSet() == null) {
				return;
			}
			if (tree.getDataSet().isEmpty()) {
				return;
			}
			String fieldName = this.fieldName;
			String content = Common.nonNullStr(tf.getValue());
			if (Common.isNullStr(content)) {
				return;
			}
			if (Common.isNumber(content)) {
				fieldName = this.fieldID;
			}
			if (isBegin == 0) {
				tree.getDataSet().beforeFirst();
			} else {
				if (tree.getDataSet().bof() || tree.getDataSet().eof()) {
					tree.getDataSet().beforeFirst();
				}
			}
			String value = null;
			tree.getDataSet().maskDataChange(true);
			while (tree.getDataSet().next()) {
				value = tree.getDataSet().fieldByName(fieldName).getString();
				if (Common.isNullStr(tree.getDataSet().fieldByName(fieldID)
						.getString())) {
					return;
				}
				if (!Common.isNullStr(content) && !Common.isNullStr(value)
						&& (value.indexOf(content) >= 0)) {
					tree.expandTo(fieldID, tree.getDataSet().fieldByName(
							fieldID).getString());
					return;
				}
			}
			tree.getDataSet().maskDataChange(false);
			tree.repaint();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void serchByTable(int isBegin) {
		try {
			DataSet ds = table.getDataSet();
			ds.maskDataChange(true);
			JTable tb = new JTable();
			tb = table.getTable();
			String sValue = null;
			int idx = 0;
			String sFilterValue = (tf.getValue() == null) ? "" : tf.getValue()
					.toString().toUpperCase();
			if (Common.isNullStr(sFilterValue)) {
				return;
			}
			// 判断第一个字符是否为数字，如果为数字则根据数字来查找，否则根据名称来查找
			String sFieldName = null;
			String[] intarray = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
					"9" };
			Arrays.sort(intarray);
			String sFirstChar = sFilterValue.substring(0, 1);
			int ifdx = Arrays.binarySearch(intarray, sFirstChar);
			if (ifdx < 0) {
				sFieldName = fieldName;
			} else {
				sFieldName = fieldID;
			}

			if (Common.isNullStr(sFilterValue)) {
				return;
			}
			table.getTable().getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			table.getTable().getSelectionModel().clearSelection();
			if ((isBegin == 1) && (table.getDataSet() != null)
					&& !table.getDataSet().isEmpty()
					&& !table.getDataSet().bof() && !table.getDataSet().eof()) {
				while (ds.next()) {
					sValue = ds.fieldByName(sFieldName).getString().trim();
					boolean bTrue =  sValue.indexOf(sFilterValue) >= 0;
					if (bTrue) {
						idx = table.bookmarkToRow(ds.toogleBookmark());
						tb.getSelectionModel().setLeadSelectionIndex(idx);
						tb.scrollRectToVisible(tb.getCellRect(idx, 1, false));
						return;
					}
				}
			}
			// if (ds.bof() || ds.eof())
			ds.bof();
			ds.beforeFirst();
			while (ds.next()) {
				sValue = ds.fieldByName(sFieldName).getString().trim();
				boolean bTrue = sValue.indexOf(sFilterValue) >= 0;
				if (bTrue) {
					idx = table.bookmarkToRow(ds.toogleBookmark());
					tb.getSelectionModel().setLeadSelectionIndex(idx);
					tb.scrollRectToVisible(tb.getCellRect(idx, 1, false));
					return;
				}
			}
			// if (ds.bof() || ds.eof())
			// ds.beforeFirst();
			// while (ds.next()) {
			// sValue = ds.fieldByName(sFieldName).getString().trim();
			// int idex = sValue.indexOf(sFilterValue);
			// if (idex >= 0) {
			// idx = table.bookmarkToRow(ds.toogleBookmark());
			// tb.getSelectionModel().setLeadSelectionIndex(idx);
			// tb.scrollRectToVisible(tb.getCellRect(idx, 1, true));
			// return;
			// }
			// }
			ds.maskDataChange(false);
		} catch (Exception ek) {
			ek.printStackTrace();
		}
	}
}
