/*
 * $Id: TFixTextWithButtonCellEditor.java,v 1.5 2008/06/18 02:09:45 qianzicheng Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */

package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.input.action.PrjInputDTO;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Tools;
import com.fr.cell.Grid;
import com.fr.cell.editor.AbstractCellEditor;
import com.fr.report.CellElement;

/**
 * <p>
 * Title:带按钮选择的文本框的cellEditor
 * </p>
 * <p>
 * Description:点按钮，弹出数据选择面板，选中数据并把结果返回到文本框中
 * </p>
 * <p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class TFixTextWithButtonCellEditor extends AbstractCellEditor {
	SelAcct treeDialog = null;

	SelAcctJJ treeDataSource = null;

	// 带按钮的文本框
	TTextWithButton tTextWithButton;

	int length;// 编辑器长度

	// ------------DIALOG-----------------//
	String dialogTitle; // 对话框标题

	String rootName;// 树结点名称

	DataSet dialogDS; // 树数据集

	String dialogFieldID;// 数结点ID

	String dialogFieldName; // 树结点NAME

	String parID; // 树结点父ID

	int isCheck;// 树结点是否复选

	SysCodeRule codeRule;// 树结点编码规则

	// ------------FINEREPORT -----------------//
	int colType;// 0表示id，1表示name

	DataSet lineDS;// fineReport当前行的数据集

	String lineFieldID;// fineReport当前行的ID

	String lineFieldName;// fineReport当前行的NAME

	String initName;// 初始值字段名

	String initValue;// 初始值

	ReportUI reportUI;// 待刷新的表格

	PrjInputDTO fd;

	int indexPanel; // 选择面板

	/**
	 * @param length
	 *            编辑器长度
	 * @param dialogTitle对话框标题
	 * @param rootName树结点名称
	 * @param dialogDS树数据集
	 * @param dialogFieldID数结点ID
	 * @param dialogFieldName树结点NAME
	 * @param parID树结点父ID
	 * @param isCheck树结点是否复选
	 * @param codeRule树结点编码规则
	 * @param lineDS
	 *            fineReport当前行的数据
	 * @param lineFieldID
	 *            fineReport当前行的ID
	 * @param fieldName
	 *            fineReport当前行的NAME
	 */
	public TFixTextWithButtonCellEditor(int indexPanel, // 选择选择框
			int length,// 编辑器长度
			// ------------DIALOG-----------------//
			String dialogTitle, // 对话框标题
			String rootName,// 树结点名称
			DataSet dialogDS, // 树数据集
			String dialogFieldID,// 数结点ID
			String dialogFieldName, // 树结点NAME
			String parID, // 树结点父ID
			int isCheck,// 树结点是否复选
			SysCodeRule codeRule,// 树结点编码规则
			// ------------FINEREPORT -----------------//
			int colType,// 0表示id，1表示name
			DataSet lineDS,// fineReport当前行的数据集
			String lineFieldID,// fineReport当前行的ID
			String lineFieldName,// fineReport当前行的NAME
			String initName,// 初始值字段名
			String initValue1,// 初始值
			ReportUI reportUI,// 待刷新的表格
			final PrjInputDTO fd // 数据操作类
	) {
		this.length = length;
		this.dialogTitle = dialogTitle;
		this.rootName = rootName;
		this.dialogDS = dialogDS;
		this.dialogFieldID = dialogFieldID;
		this.dialogFieldName = dialogFieldName;
		this.parID = parID;
		this.isCheck = isCheck;
		this.codeRule = codeRule;

		this.colType = colType;
		this.lineDS = lineDS;
		this.lineFieldID = lineFieldID;
		this.lineFieldName = lineFieldName;
		this.initName = initName;
		this.initValue = initValue1;
		this.reportUI = reportUI;
		this.fd = fd;
		this.indexPanel = indexPanel;
		tTextWithButton = new TTextWithButton(length);
		tTextWithButton.getTextField().setEditable(false);
		tTextWithButton.getButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (fd.getCurState() == -1) {
						TFixTextWithButtonCellEditor.this.fireEditingStopped();
					} else {
						openDialog(TFixTextWithButtonCellEditor.this, initValue);
					}
					
				} catch (Exception ee) {
					TFixTextWithButtonCellEditor.this.fireEditingStopped();
				}
			}
		});
	}

	public Object getCellEditorValue() {
		try {
			Cell cell = (Cell) ((Report) fd.getReportUI().getReport())
					.getSelectedCell();
			if (cell == null)
				return "";
			String bmk = cell.getBookmark();
			if ((((Report) fd.getReportUI().getReport()).getBodyData())
					.gotoBookmark(bmk)) {
				if (indexPanel == 0) {
					return (((Report) fd.getReportUI().getReport())
							.getBodyData()).fieldByName("ACCT_NAME")
							.getString();
				} else {
					return (((Report) fd.getReportUI().getReport())
							.getBodyData()).fieldByName("ACCT_NAME_JJ")
							.getString();
				}
			} else
				return "";
		} catch (Exception e) {
			return null;
		}
	}

	public Component getCellEditorComponent(Grid grid, CellElement cell) {
		try {
			if (cell == null)
				return tTextWithButton;
			String bmk = ((Cell) cell).getBookmark();
			if ((((Report) fd.getReportUI().getReport()).getBodyData())
					.gotoBookmark(bmk)) {
				String value = "无";
				if (indexPanel == 0) {
					value = (((Report) fd.getReportUI().getReport())
							.getBodyData()).fieldByName("ACCT_NAME")
							.getString();
				} else {
					value = (((Report) fd.getReportUI().getReport())
							.getBodyData()).fieldByName("ACCT_NAME_JJ")
							.getString();
				}
				tTextWithButton.getTextField().setText(value);
			} else {
				tTextWithButton.getTextField().setText("无");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tTextWithButton;
	}

	/**
	 * 打开对话框
	 */
	private void openDialog(TFixTextWithButtonCellEditor cellEditor,
			String oldValue) {
		try {
			Report report = (Report) reportUI.getWorkSheet();
			Cell cell = (Cell) report.getSelectedCell();
			if (cell.getRow() <= report.getReportHeader().getRows()-1) {
				return ;
			}
			if (fd.getCurState()==-1)
				return ;
			String bmk = cell.getBookmark();
			String[] value = null;
			(((Report) fd.getReportUI().getReport()).getBodyData())
					.gotoBookmark(bmk);
			if (indexPanel == 0) {
				if (treeDialog == null)
					treeDialog = new SelAcct(this,fd.getDsAcctSel(), (((Report) fd
							.getReportUI().getReport()).getBodyData())
							.fieldByName("acct_name").getString());
				Tools.centerWindow(treeDialog);
				treeDialog.setVisible(true);
				value = treeDialog.getReturnValue();
			} else if (indexPanel == 1) {
				if (treeDataSource == null)
					treeDataSource = new SelAcctJJ(this,fd.getDsAcctJJ(), oldValue);
				Tools.centerWindow(treeDataSource);
				treeDataSource.setVisible(true);
				value = treeDataSource.getReturnValue();
			}
			if (indexPanel == 0 && treeDialog.isOK) {
				// 功能科目
				tTextWithButton.getTextField().setText(value[1]);
				(((Report) fd.getReportUI().getReport()).getBodyData()).edit();
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("BS_ID").setValue(
								Common.nonNullStr(value[0]));
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("ACCT_NAME").setValue(
								Common.nonNullStr(value[1]));
			} else if (indexPanel == 1 && treeDataSource.isOK) {
				// 经济科目
				tTextWithButton.getTextField().setText(value[1]);
				(((Report) fd.getReportUI().getReport()).getBodyData()).edit();
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("BSI_ID").setValue(
								Common.nonNullStr(value[0]));
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("ACCT_NAME_JJ").setValue(
								Common.nonNullStr(value[1]));
//				List list = treeDataSource.getTreeSelect();
//				String[] abs = (String[]) list.get(0);
//				String[] ans = (String[]) list.get(1);
//				String sbType = (((Report) fd.getReportUI().getReport())
//						.getBodyData()).fieldByName("SB_TYPE").getString();
//				String sbCode = (((Report) fd.getReportUI().getReport())
//						.getBodyData()).fieldByName("SB_CODE").getString();
//				String acctName = (((Report) fd.getReportUI().getReport())
//						.getBodyData()).fieldByName("ACCT_NAME").getString();
//				String BS_ID = (((Report) fd.getReportUI().getReport())
//						.getBodyData()).fieldByName("BS_ID").getString();
//				String BSI_ID = (((Report) fd.getReportUI().getReport())
//						.getBodyData()).fieldByName("BSI_ID").getString();
//				Arrays.sort(abs);
//				List lsAdd = new ArrayList();
//				List lsAdd1 = new ArrayList();
//				(((Report) fd.getReportUI().getReport()).getBodyData())
//						.beforeFirst();
//				for (int i = 0; i < abs.length; i++) {
//					if (Common.isNullStr(abs[i]))
//						continue;
//					if (fd.getDsBody().locate("bsi_id", abs[i], "BS_ID", BS_ID))
//						continue;
//					lsAdd.add(abs[i]);
//					lsAdd1.add(ans[i]);
//				}
//				if (lsAdd != null && lsAdd.size() > 1) {
//					for (int i = 0; i < lsAdd.size(); i++) {
//						if (Common.isNullStr(BSI_ID)) {
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).gotoBookmark(bmk);
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).edit();
//						} else {
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).append();
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).fieldByName("xmxh")
//									.setValue(fd.getXmxh());
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).fieldByName("SB_TYPE")
//									.setValue(
//											String.valueOf((Integer
//													.parseInt(sbType) + 1)));
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).fieldByName("SB_CODE")
//									.setValue(
//											String.valueOf((Integer
//													.parseInt(sbCode) + 1)));
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).fieldByName("ACCT_NAME")
//									.setValue(acctName);
//							(((Report) fd.getReportUI().getReport())
//									.getBodyData()).fieldByName("BS_ID")
//									.setValue(BS_ID);
//						}
//						(((Report) fd.getReportUI().getReport()).getBodyData())
//								.fieldByName("BSI_ID").setValue(
//										Common.nonNullStr(lsAdd.get(i)));
//						(((Report) fd.getReportUI().getReport()).getBodyData())
//								.fieldByName("ACCT_NAME_JJ").setValue(
//										Common.nonNullStr(lsAdd1.get(i)));
//						tTextWithButton.getTextField().setText(
//								Common.nonNullStr(lsAdd1.get(i)));
//					}
//				} else {
//					if (lsAdd.isEmpty())
//						return;
//					if (!(((Report) fd.getReportUI().getReport()).getBodyData())
//							.gotoBookmark(bmk))
//						return;
//					(((Report) fd.getReportUI().getReport()).getBodyData())
//							.edit();
//					(((Report) fd.getReportUI().getReport()).getBodyData())
//							.fieldByName("BSI_ID").setValue(
//									Common.nonNullStr(lsAdd.get(0)));
//					String name = Common.nonNullStr(lsAdd1.get(0));
//					(((Report) fd.getReportUI().getReport()).getBodyData())
//							.fieldByName("ACCT_NAME_JJ").setValue(name);
//					tTextWithButton.getTextField().setText(name);
//				}
//				DataSet ds  = (((Report) fd.getReportUI().getReport()).getBodyData());
//				int k = 1;
//				String type = "";
//				ds.beforeFirst();
//				while(ds.next()){
//					type = ds.fieldByName("SB_CODE").getString();
//					if ("111".equals(type)
//							||"222".equals(type)
//							||"333".equals(type))
//						continue;
//					ds.fieldByName("SB_TYPE").setValue(String.valueOf(k++));
//					ds.fieldByName("SB_CODE").setValue("40000"+k);
//				}
//				fd.getReportUI().repaint();
//				(((Report) fd.getReportUI().getReport()).getBodyData())
//						.gotoBookmark(bmk);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * 按钮事件
	 * 
	 */
	class TActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				TFixTextWithButtonCellEditor.this.fireEditingStopped();
			} catch (Exception ee) {
				TFixTextWithButtonCellEditor.this.fireEditingStopped();
			}
		}
	}

	/**
	 * 
	 * 一个文本框右侧跟一个按钮
	 * 
	 */
	class TTextWithButton extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** 组件长度 */
		private int length;

		/** 文本框 */
		private JTextField textField;

		/** 按钮 */
		private JButton button;

		/**
		 * 构造方法
		 * 
		 * @param length
		 */
		public TTextWithButton(int length) {
			super();
			initialize();
			this.length = length;
			initialize();
		}

		/**
		 * 初始化
		 * 
		 */
		private void initialize() {
			RowPreferedLayout rowlayout = new RowPreferedLayout(2);
			rowlayout.setColumnWidth(length / 10);
			rowlayout.setColumnGap(0);
			this.setLayout(rowlayout);
			initTextField();
			initButton();
			this.add(getTextField(), new TableConstraints(9, 1, true, true));
			this.add(getButton(), new TableConstraints(1, 1, false, false));
		}

		/**
		 * 获取文本框
		 */
		public JTextField getTextField() {
			return textField;
		}

		/**
		 * 获取按钮
		 */
		public JButton getButton() {
			return button;
		}

		/**
		 * 初始化文本框
		 */
		public void initTextField() {
			textField = new JTextField();
		}

		/**
		 * 初始化按钮
		 */
		public void initButton() {
			button = new JButton("..");
		}
	}

	public void refreshBean() throws Exception {
		this.fireEditingStopped();
	}
	/**
	 * 焦点事件
	 */
	class TFocusListener implements FocusListener{

		public void focusGained(FocusEvent e) {}

		public void focusLost(FocusEvent e) {
//			openDialog(cellEditor);
			try {
				refreshBean();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
