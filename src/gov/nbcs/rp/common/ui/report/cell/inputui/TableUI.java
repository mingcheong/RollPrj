/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report.cell.inputui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * 
 * 
 * @author ��˫��
 * 
 */

public class TableUI extends AbstractInputDialog {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// û��check��
	public static final int iNoCheck = 0;

	// ��check��
	public static final int iChcek = 1;

	private Object ob;

	// ����ֵ���ж��Ƿ���check��
	public int iCount = -1;

	// ��ȡ�����i
	private int Flag;

	// ��ȡ���������ֶ�
	private String fieldName;

	private DataSet ds;

	// ��Ŀ���������
	FTextField prjTextField;

	// ��
	CustomTable table;

	String[] aFieldName;

	/*
	 * ����˵����aFieldText,aFieldName,dataset ����customtable�Ĳ��� i check�������0 û�У�1 �У�
	 * fieldEName ��ȡ�����ֶΣ�obj ��ȡ����ֵ�Ŀؼ�
	 */
	public TableUI(String[] aFieldText, String[] aFieldName, DataSet dataSet,
			int i, String fieldEName, Object obj) throws Exception {
		super(Global.mainFrame);
		this.ds = dataSet;
		this.Flag = i;
		this.aFieldName = aFieldName;
		ob = obj;
		this.fieldName = fieldEName;
		ds.beforeFirst();
		ds.next();

		this.setSize(500, 600);
		this.setResizable(false);

		this.dispose();
		this.setTitle("¼����ѡ��");
		this.setModal(true);

		// �������Ϸ���panel����Ϊ������panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(10);
		// ��Ŀ���������
		prjTextField = new FTextField();
		prjTextField.setTitle("�������ѯ����:");
		prjTextField.setProportion(0.25f);

		// �������Ұ�ť
		FFlowLayoutPanel findPanel = new FFlowLayoutPanel();
		findPanel.setAlignment(FlowLayout.CENTER);
		// ���Ұ�ť
		FButton findButton = new FButton();
		findButton.setText("����");
		findButton.addActionListener(new searchAction(this));

		FButton findNextButton = new FButton();
		findNextButton.setText("������һ��");
		findPanel.addControl(findButton);
		findPanel.addControl(findNextButton);
		findNextButton.addActionListener(new searchNextAction(this));

		// ��
		// �ֱ���ʾʱ����Check��
		FPanel tablePanel = new FPanel();
		if (Flag == TableUI.iChcek) {
			table = new CustomTable(aFieldText, aFieldName, ds, true,
					new String[] { "" });
		} else {
			table = new CustomTable(aFieldText, aFieldName, ds, false,
					new String[] { "" });
		}
		table.reset();
		RowPreferedLayout tableLayout = new RowPreferedLayout(1);
		tablePanel.setLayout(tableLayout);
		tablePanel.addControl(table, new TableConstraints(1, 1, true, true));

		// ˫������У���������Ҫ��ֵ������ͬ��ȷ������ť
		table.getTable().addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					iCount = 1;
					try {
						setVisible(false);
						if ((ob != null) && (!Common.isNullStr(fieldName))) {
							resultObject(ob);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

		});

		// "ȷ��"��"ȡ��"��ť
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		// ȷ����ť
		FButton okButton = new FButton();
		okButton.setText("ȷ��");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iCount = 1;
				try {
					setVisible(false);
					if ((ob != null) && (!Common.isNullStr(fieldName))) {
						resultObject(ob);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		FButton cancelButton = new FButton();
		cancelButton.setText("ȡ��");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iCount = 0;
				setVisible(false);
			}
		});
		choosePanel.addControl(okButton);
		choosePanel.addControl(cancelButton);

		// ��Ԫ�ض����뵽mainPanel��
		mainPanel.addControl(prjTextField, new TableConstraints(1, 1, false,
				true));
		mainPanel
				.addControl(findPanel, new TableConstraints(2, 1, false, true));
		mainPanel.add(tablePanel, new TableConstraints(8, 1, true, true));
		mainPanel.addControl(choosePanel, new TableConstraints(2, 1, false,
				true));
		this.getContentPane().add(mainPanel);
	}

	// ��ȡ����ֵ
	public Object getValue() throws Exception {
		if (iCount == 1) {
			if ((!ds.bof()) && (!ds.eof())) {
				if (Flag == iNoCheck) {
					return ds.fieldByName(fieldName).getString();
				} else {
					String string = new String();
					int[] row = table.getTable().getSelectedRows();
					for (int i = 0; i < row.length; i++) {
						String bookmark = table.rowToBookmark(row[i]);
						ds.gotoBookmark(bookmark);
						if (i != row.length - 1) {
							string += ds.fieldByName(fieldName).getString()
									+ ";";
						} else {
							string += ds.fieldByName(fieldName).getString();
						}
					}
					return string;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	// ƥ��ؼ�,Ϊ�ؼ���ֵ
	public void resultObject(Object ob) throws Exception {
		if (ob instanceof FTextField) {
			((FTextField) ob).setValue(getValue());
		}
		if (ob instanceof FTextArea) {
			((FTextArea) ob).setValue(getValue());
		}
		if (ob instanceof FList) {
			DefaultListModel listModel = (DefaultListModel) ((FList) ob)
					.getModel();
			((FList) ob).setModel(listModel);
			listModel.addElement(getValue());
		}
		if (ob instanceof JTextField) {
			if (getValue() != null) {
				((JTextField) ob).setText(getValue().toString());
			}
		}
	}

	public void setUiTitle(String title) {
		this.setTitle(title);
	}

	public FTextField getPrjTextField() {
		return prjTextField;
	}

	// [����]��ť�����¼�
	public class searchAction implements ActionListener {
		TableUI tableUI;

		public searchAction(TableUI tableUI) {
			this.tableUI = tableUI;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				boolean isSel = false;
				String content = Common.nonNullStr(prjTextField.getValue());
				ds.beforeFirst();
				while (ds.next()) {
					String fieldEName = aFieldName[1];
					String fieldValue = Common.nonNullStr(ds.fieldByName(
							fieldEName).getValue());
					if (fieldValue.indexOf(content) >= 0) {
						isSel = true;
						int index = table.bookmarkToRow(ds.toogleBookmark());
						table.getTable().getSelectionModel().setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						table.getTable().getSelectionModel().clearSelection();
						table.getTable().getSelectionModel()
								.setLeadSelectionIndex(index);
						break;
					}
				}
				if (!isSel) {
					JOptionPane.showMessageDialog(tableUI, "û���ҵ�ƥ������ݣ�");
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * ������һ������
	 * @author ��˫��
	 *
	 */
	public class searchNextAction implements ActionListener {
		TableUI tableUI;

		public searchNextAction(TableUI tableUI) {
			this.tableUI = tableUI;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				boolean isSel=false;
				String content = Common.nonNullStr(prjTextField.getValue());
				while (ds.next()) {
					String fieldEName = aFieldName[1];
					String fieldValue = Common.nonNullStr(ds.fieldByName(
							fieldEName).getValue());
					if (fieldValue.indexOf(content) >= 0) {
						isSel=true;
						int index = table.bookmarkToRow(ds.toogleBookmark());
						table.getTable().getSelectionModel().setSelectionMode(
								ListSelectionModel.SINGLE_SELECTION);
						table.getTable().getSelectionModel().clearSelection();
						table.getTable().getSelectionModel()
								.setLeadSelectionIndex(index);
						break;
					}
				}
				if(!isSel) {
					JOptionPane.showMessageDialog(tableUI, "û���ҵ�ƥ������ݣ�");
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
