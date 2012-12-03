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
 * Title:����ťѡ����ı����cellEditor
 * </p>
 * <p>
 * Description:�㰴ť����������ѡ����壬ѡ�����ݲ��ѽ�����ص��ı�����
 * </p>
 * <p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author Ǯ�Գ�
 * @version 1.0
 */

public class TFixTextWithButtonCellEditor extends AbstractCellEditor {
	SelAcct treeDialog = null;

	SelAcctJJ treeDataSource = null;

	// ����ť���ı���
	TTextWithButton tTextWithButton;

	int length;// �༭������

	// ------------DIALOG-----------------//
	String dialogTitle; // �Ի������

	String rootName;// ���������

	DataSet dialogDS; // �����ݼ�

	String dialogFieldID;// �����ID

	String dialogFieldName; // �����NAME

	String parID; // ����㸸ID

	int isCheck;// ������Ƿ�ѡ

	SysCodeRule codeRule;// �����������

	// ------------FINEREPORT -----------------//
	int colType;// 0��ʾid��1��ʾname

	DataSet lineDS;// fineReport��ǰ�е����ݼ�

	String lineFieldID;// fineReport��ǰ�е�ID

	String lineFieldName;// fineReport��ǰ�е�NAME

	String initName;// ��ʼֵ�ֶ���

	String initValue;// ��ʼֵ

	ReportUI reportUI;// ��ˢ�µı��

	PrjInputDTO fd;

	int indexPanel; // ѡ�����

	/**
	 * @param length
	 *            �༭������
	 * @param dialogTitle�Ի������
	 * @param rootName���������
	 * @param dialogDS�����ݼ�
	 * @param dialogFieldID�����ID
	 * @param dialogFieldName�����NAME
	 * @param parID����㸸ID
	 * @param isCheck������Ƿ�ѡ
	 * @param codeRule�����������
	 * @param lineDS
	 *            fineReport��ǰ�е�����
	 * @param lineFieldID
	 *            fineReport��ǰ�е�ID
	 * @param fieldName
	 *            fineReport��ǰ�е�NAME
	 */
	public TFixTextWithButtonCellEditor(int indexPanel, // ѡ��ѡ���
			int length,// �༭������
			// ------------DIALOG-----------------//
			String dialogTitle, // �Ի������
			String rootName,// ���������
			DataSet dialogDS, // �����ݼ�
			String dialogFieldID,// �����ID
			String dialogFieldName, // �����NAME
			String parID, // ����㸸ID
			int isCheck,// ������Ƿ�ѡ
			SysCodeRule codeRule,// �����������
			// ------------FINEREPORT -----------------//
			int colType,// 0��ʾid��1��ʾname
			DataSet lineDS,// fineReport��ǰ�е����ݼ�
			String lineFieldID,// fineReport��ǰ�е�ID
			String lineFieldName,// fineReport��ǰ�е�NAME
			String initName,// ��ʼֵ�ֶ���
			String initValue1,// ��ʼֵ
			ReportUI reportUI,// ��ˢ�µı��
			final PrjInputDTO fd // ���ݲ�����
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
				String value = "��";
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
				tTextWithButton.getTextField().setText("��");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tTextWithButton;
	}

	/**
	 * �򿪶Ի���
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
				// ���ܿ�Ŀ
				tTextWithButton.getTextField().setText(value[1]);
				(((Report) fd.getReportUI().getReport()).getBodyData()).edit();
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("BS_ID").setValue(
								Common.nonNullStr(value[0]));
				(((Report) fd.getReportUI().getReport()).getBodyData())
						.fieldByName("ACCT_NAME").setValue(
								Common.nonNullStr(value[1]));
			} else if (indexPanel == 1 && treeDataSource.isOK) {
				// ���ÿ�Ŀ
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
	 * ��ť�¼�
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
	 * һ���ı����Ҳ��һ����ť
	 * 
	 */
	class TTextWithButton extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** ������� */
		private int length;

		/** �ı��� */
		private JTextField textField;

		/** ��ť */
		private JButton button;

		/**
		 * ���췽��
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
		 * ��ʼ��
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
		 * ��ȡ�ı���
		 */
		public JTextField getTextField() {
			return textField;
		}

		/**
		 * ��ȡ��ť
		 */
		public JButton getButton() {
			return button;
		}

		/**
		 * ��ʼ���ı���
		 */
		public void initTextField() {
			textField = new JTextField();
		}

		/**
		 * ��ʼ����ť
		 */
		public void initButton() {
			button = new JButton("..");
		}
	}

	public void refreshBean() throws Exception {
		this.fireEditingStopped();
	}
	/**
	 * �����¼�
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
