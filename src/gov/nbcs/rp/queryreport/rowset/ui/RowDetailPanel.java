/**
 * @# RowDetailPanel.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.LengthSupportTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:������һ���ֶε���ϸ��Ϣ,֧����ק
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 * @since java 1.4.2
 */
public class RowDetailPanel extends FPanel implements DropTargetListener,
		IRowSelectchangeListener, ITitleChangedListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4620543508134564845L;

	private boolean maskChange = false;

	private RowInfo aRow;

	private DefaultListModel listModel;

	// �ؼ�
	private FTextField txtTableName;// ����Դ��

	private FTextField txtItem;// ��Ŀ����

	private FRadioGroup rdoType;// ������ʽ

	private FPanel pnlRTop;

	private JList lstFields;

	private FPanel pnlRBottom;

	private ConsTable tblCons;

	private RowSetUI parent;

	private FTextArea txtSql;

	private FPanel pnlBtn;// Ĭ�ϲ���

	private static final String divcode = "divcode";

	private static final String year = "year";

	private static final String batch = "batch";

	private static final String type = "type";

	private static final String ver = "ver";

	private static final String hint = "hint";

	private static final String table = "table";

	public RowDetailPanel(RowSetUI parent) {
		this.parent = parent;
		init();
	}

	private void init() {
		txtTableName = new FTextField("����Դ:");
		txtTableName.setEditable(false);
		txtTableName.setTitleAdapting(true);
		rdoType = new FRadioGroup("", FRadioGroup.VERTICAL);
		rdoType.setTitleVisible(false);
		rdoType.setRefModel("" + RowInfo.MEASURE_COUNT + "#����+"
				+ RowInfo.MEASURE_SUM + "#���+" + RowInfo.MEASURE_SQL + "#��ѯ���");
		rdoType.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				if (rdoType.getValue() == null)
					return;
				if (!maskChange && aRow != null)

					aRow.setMeasureType(Integer.parseInt(rdoType.getValue()
							.toString()));
				boolean sqlVisible = Integer.parseInt(rdoType.getValue()
						.toString()) == RowInfo.MEASURE_SQL;
				txtSql.setVisible(sqlVisible);
				pnlBtn.setVisible(sqlVisible);
				pnlRTop.setVisible(Integer.parseInt(rdoType.getValue()
						.toString()) == RowInfo.MEASURE_SUM);
				pnlRBottom.setVisible(!sqlVisible);

			}

		});
		FPanel pnlRadio = new FPanel();
		pnlRadio.setLayout(new BorderLayout());
		pnlRadio.add(rdoType, BorderLayout.CENTER);

		pnlRadio.setTitle("�������");
		txtItem = new FTextField("��Ŀ��:");
		txtItem.setTitleAdapting(true);
		txtItem.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				if (maskChange || aRow == null)
					return;
				aRow
						.setItem(((LengthSupportTextField) (txtItem
								.getComponent(1))).getText());

			}

		});
		// -------------------------------------
		// ���ϵ�а�ť xxl 20080919
		FToolBar tpnlButton = new FToolBar();
		pnlBtn = new FPanel();
		pnlBtn.setLayout(new BorderLayout());
		pnlBtn.setTitle("��������");
		pnlBtn.add(tpnlButton, BorderLayout.CENTER);
		tpnlButton.setLayout(new RowPreferedLayout(3));
		FButton btnDiv = new FButton(divcode, "��λ");
		btnDiv.setBorderVisible(false);
		btnDiv.addActionListener(this);
		tpnlButton.add(btnDiv, new TableConstraints(1, 1, true, true));
		FButton btnYear = new FButton(year, "���");
		btnYear.addActionListener(this);
		tpnlButton.add(btnYear, new TableConstraints(1, 1, true, true));
		FButton btnBatchr = new FButton(batch, "����");
		btnBatchr.addActionListener(this);
		tpnlButton.add(btnBatchr, new TableConstraints(1, 1, true, true));
		FButton btnType = new FButton(type, "����");
		btnType.addActionListener(this);
		tpnlButton.add(btnType, new TableConstraints(1, 1, true, true));
		FButton btnVer = new FButton(ver, "�汾");
		btnVer.addActionListener(this);
		tpnlButton.add(btnVer, new TableConstraints(1, 1, true, true));
		FButton btnTable = new FButton(table, "���ݱ�");
		btnTable.addActionListener(this);
		tpnlButton.add(btnTable, new TableConstraints(1, 1, true, true));
		FButton btnReport = new FButton(hint, "����˵��");
		btnReport.addActionListener(this);
		tpnlButton.add(btnReport, new TableConstraints(1, 1, true, true));

		// ---------------------------------------
		txtSql = new FTextArea();
		txtSql.setTitleVisible(false);

		txtSql.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				if (maskChange || aRow == null)
					return;
				aRow.setSql((String) txtSql.getValue());

			}

		});
		FPanel pnlLeft = new FPanel();
		pnlLeft.setBorder(new EmptyBorder(new Insets(4, 4, 4, 4)));
		pnlLeft.setLayout(new RowPreferedLayout(15));
		pnlLeft.add(txtTableName, new TableConstraints(1, 12, false, true));
		pnlLeft.add(txtItem, new TableConstraints(1, 12, false, true));
		pnlLeft.add(pnlRadio, new TableConstraints(4, 5, false, true));
		pnlLeft.add(pnlBtn, new TableConstraints(4, 10, false, true));
		pnlLeft.add(txtSql, new TableConstraints(5, 15, true, true));

		// /===========================
		pnlRTop = new FPanel();
		pnlRTop.setTitle("�ֶ�");
		pnlRTop.setLayout(new RowPreferedLayout(5));
		listModel = new DefaultListModel();
		lstFields = new JList(listModel);
		lstFields.setBorder(new BevelBorder(BevelBorder.LOWERED));
		lstFields.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == 1) {
					deleteCurField();
				}
			}
		});
		FScrollPane spnlFields = new FScrollPane();
		spnlFields.getViewport().add(lstFields);
		new DropTarget(lstFields, this);
		FButton btnAdd = new FButton();
		btnAdd.setText("����");
		btnAdd.addActionListener(new btnAddFieldClick());
		FButton btnDelete = new FButton();
		btnDelete.setText("ɾ��");
		btnDelete.addActionListener(new btnDelFieldClick());
		Box aBox = Box.createVerticalBox();
		aBox.add(Box.createVerticalStrut(30));
		aBox.add(btnAdd);
		aBox.add(btnDelete);
		pnlRTop.add(spnlFields, new TableConstraints(6, 4, true, true));
		pnlRTop.add(aBox, new TableConstraints(6, 1, true, true));
		// �±�
		tblCons = new ConsTable();
		pnlRBottom = new FPanel();
		pnlRBottom.setLayout(new RowPreferedLayout(5));
		pnlRBottom.setTitle("����");

		btnAdd = new FButton();
		btnAdd.setText("����");
		btnAdd.addActionListener(new btnAddConClick());
		btnDelete = new FButton();
		btnDelete.setText("ɾ��");
		btnDelete.addActionListener(new btnDelConClick());
		aBox = Box.createVerticalBox();
		aBox.add(Box.createVerticalStrut(30));
		aBox.add(btnAdd);
		aBox.add(btnDelete);
		pnlRBottom.add(tblCons, new TableConstraints(6, 4, true, true));
		pnlRBottom.add(aBox, new TableConstraints(6, 1, true, true));
		this.setLayout(new RowPreferedLayout(5));
		this.add(pnlLeft, new TableConstraints(12, 2, true, true));
		this.add(pnlRTop, new TableConstraints(6, 3, true, true));
		this.add(pnlRBottom, new TableConstraints(6, 3, true, true));
		txtSql.setVisible(false);
		pnlBtn.setVisible(false);
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO �Զ����ɷ������

	}

	public void dragExit(DropTargetEvent dte) {
		// TODO �Զ����ɷ������

	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO �Զ����ɷ������

	}

	public void drop(DropTargetDropEvent dtde) {
		Transferable tr = dtde.getTransferable();
		DataFlavor[] flavors = tr.getTransferDataFlavors();

		if (flavors.length < 2
				|| !tr.isDataFlavorSupported(flavors[ObjectSelection.OBJECT])) {
			dtde.rejectDrop();
			return;
		}
		// �����SQL������򲻿��Խ���
		if (aRow.getMeasureType() != RowInfo.MEASURE_SUM) {
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
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO �Զ����ɷ������

	}

	class btnAddFieldClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (parent == null)
				return;
			XMLData aData = parent.getSelectField();
			if (aData == null) {
				// new MessageBox(parent.frame, "��ѡ��Ҫ��ӵ��ֶ�!",
				// MessageBox.MESSAGE,
				// MessageBox.BUTTON_OK).show();
				JOptionPane.showMessageDialog(RowDetailPanel.this.parent,
						"��ѡ��Ҫ��ӵ��ֶ�!");
				return;
			}

			appendField(aData);

		}
	}

	class btnDelFieldClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (lstFields.getSelectedValue() == null) {
				// new MessageBox(parent.frame, "��ѡ��Ҫɾ�����ֶ�!",
				// MessageBox.MESSAGE,
				// MessageBox.BUTTON_OK).show();
				JOptionPane.showMessageDialog(RowDetailPanel.this.parent,
						"��ѡ��Ҫɾ�����ֶ�!");
			}

			deleteCurField();

		}

	}

	class btnDelConClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			tblCons.del();

		}

	}

	class btnAddConClick implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (aRow == null) {
				// new MessageBox(parent.frame, "����ѡ��һ�У��ſ����������!",
				// MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				JOptionPane.showMessageDialog(RowDetailPanel.this.parent,
						"����ѡ��һ�У��ſ����������!");
				return;
			}

			if (Common.isNullStr(aRow.getTableName())) {
				// new MessageBox(parent.frame,"����Դ��δѡ�����������������!",
				// MessageBox.MESSAGE,
				// MessageBox.BUTTON_OK).show();
				JOptionPane.showMessageDialog(RowDetailPanel.this.parent,
						"����Դ��δѡ�����������������!");

				return;
			}

			tblCons.add();

		}

	}

	// ��ʾ��Ϣ
	public void setRowInfo(RowInfo aRow) {

		this.aRow = aRow;
		updateDisp();

	}

	private void updateDisp() {
		maskChange = true;
		if (aRow == null)
			return;
		this.txtItem.setValue(aRow.getItem());
		this.txtTableName.setValue(aRow.transTableToZH(parent.xmlTableSource));
		rdoType.setValue(String.valueOf(aRow.getMeasureType()));
		txtSql.setValue(aRow.getSql());
		dispFields();// /ˢ���ֶ�
		tblCons.setValue(aRow, (List) parent.xmlField.get(aRow.getSourceID()));// ˢ������

		maskChange = false;
	}

	// /ˢ���ֶ�
	private void dispFields() {
		listModel.removeAllElements();
		if (aRow == null || Common.isNullStr(aRow.getFields())
				|| parent == null || parent.xmlField == null)
			return;
		String arrField[] = aRow.getFields().split(";");
		List aData = (List) parent.xmlField.get(aRow.getSourceID());
		if (aData == null) {// ���û�д˱�ķ��룬��ֻ��Ϊ����
			aData = new ArrayList();

		}
		for (int i = 0; i < arrField.length; i++) {
			int index = ((List) aData.get(0)).indexOf(arrField[i]);
			if (index == -1)
				continue;
			listModel.addElement(new DsField(arrField[i],
					(String) ((List) aData.get(1)).get(index)));
		}

	}

	public void selectChange(RowInfo aRow, boolean isOnDrag) {
		setRowInfo(aRow);

	}

	// ���һ���ֶ�,ֵΪ������ѡ�е�
	public void appendField(XMLData aField) {
		if (aField == null || aRow == null)
			return;
		if (aRow.getMeasureType() != RowInfo.MEASURE_SUM) {
			return;
		}
		// �����ǰû��ȷ������Դ��
		if (Common.isNullStr(aRow.getTableName())) {
			aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
			aRow.setSourceID((String) aField.get(IDataDictBO.DICID));
		}
		String sOldTableID = aRow.getSourceID();
		String sNewTableID = (String) aField.get(IDataDictBO.DICID);

		if (IDataDictBO.MARK_TYPE.equals(aField.get(IDataDictBO.fieldMark)))
			return;
		if (!IDataDictBO.MARK_FIELD.equals(aField.get(IDataDictBO.fieldMark))) {
			// ������ϼ��Ľڵ㣬�򲻿������
			// �������������Դ��һ�£���Ҫɾ��ԭ��������Դ��������ֶ���Ϣ
			if (!sNewTableID.equals(sOldTableID)) {
				aRow.setSourceID(sNewTableID);
				aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
				aRow.setFields(null);
				dispFields();
			}
			return;
		}
		// ������б���Ҫ�Ա�һ��
		if (sNewTableID.equals(sOldTableID)) {// �����ȣ���������
			aRow.addOperField((String) aField.get(IDataDictBO.AFIELD_ENAME));
			dispFields();
		} else {// ������ȣ���Ҫˢ������Դ
			aRow.setSourceID(sNewTableID);
			aRow.setTableName((String) aField.get(IDataDictBO.TABLE_ENAME));
			aRow.setFields((String) aField.get(IDataDictBO.AFIELD_ENAME));
			dispFields();
		}

	}

	public void deleteCurField() {
		Object obj = lstFields.getSelectedValue();
		if (obj == null)
			return;
		int iIndex = lstFields.getSelectedIndex();
		aRow.deleteAField(((DsField) obj).getField());
		dispFields();

		int iCount = lstFields.getModel().getSize();
		if (iIndex < iCount)
			lstFields.setSelectedIndex(iIndex);
		else {
			if (iIndex == 0)
				return;
			else
				lstFields.setSelectedIndex(iIndex - 1);
		}

	}

	public void titleChanged(RowInfo aRow, boolean isItem) {
		if (!isItem)
			txtTableName.setValue(aRow.transTableToZH(parent.xmlTableSource));

	}

	// ��Ӧ��ť���� add XXL 20080919
	public void actionPerformed(ActionEvent e) {
		String btnID = ((FButton) e.getSource()).getId();
		String addStr = "";
		if (btnID.equals(divcode)) {
			addStr = RowInfo.PARAM_DIV.replaceAll("\\\\", "");
		} else if (btnID.equals(year)) {
			addStr = RowInfo.PARAM_SET_YEAR.replaceAll("\\\\", "");
		} else if (btnID.equals(batch)) {
			addStr = RowInfo.PARAM_BATCH.replaceAll("\\\\", "");
		} else if (btnID.equals(type)) {
			addStr = RowInfo.PARAM_DATA_TYPE.replaceAll("\\\\", "");
		} else if (btnID.equals(ver)) {
			addStr = RowInfo.PARAM_DATA_VER.replaceAll("\\\\", "");
		} else if (btnID.equals(table))
			addStr = RowInfo.PARAM_TABLE.replaceAll("\\\\", "");
		if (!"".equals(addStr)) {
			int iStart = ((JTextArea) (txtSql.getEditor())).getSelectionStart();
			int iEnd = ((JTextArea) (txtSql.getEditor())).getSelectionEnd();
			String value = ((JTextArea) (txtSql.getEditor())).getText();
			value = value.substring(0, iStart) + addStr + value.substring(iEnd);
			txtSql.setValue(value);
			((JTextArea) (txtSql.getEditor())).setSelectionEnd(iStart);
			// ��ý���
			txtSql.requestFocus();
			// ���ù����ʾλ��
			((JTextArea) (txtSql.getEditor())).setCaretPosition(iStart
					+ addStr.length());
			return;
		} else {
			HintDlg hintDlg = new HintDlg((JFrame) this.getParent().getParent()
					.getParent().getParent().getParent().getParent()
					.getParent().getParent().getParent());
			hintDlg.show();

		}

	}

	private class HintDlg extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3966857588609814254L;

		private final String strHint = "�����б�: $div_code ��λ���� \n\r   "
				+ " $set_year ��� \n\r    "
				+ "$batch ����  \n\r    "
				+ "$data_type  �������ͣ���λ���ݻ�������ݣ� \n\r   "
				+ " $data_ver ���ݰ汾(һ�㶼�ǵ�ǰ����)\n\r "
				+ "%table- �����û����Ϳɱ�����ݱ���(������Ϣ��)\n\r    "
				+ "����\n\r"
				+ " select sum(f1) "
				+ "\n\r from $table-fb_b_info \n\r where  $div_code  "
				+ " \n\r and set_year=$set_year \n\r and batch_no=$batch  \n\r and  data_type=$data_type   \n\r and ver_no= $data_ver \n\r"
				+ "ע��1.��λ���� $div_codeǰ��Ҫ��'div_code ='.������Ҫд."
				+ "\n\r    2.���ִ�Сд!";

		public HintDlg(JFrame frame) {
			super(frame, true);
			init();
		}

		private void init() {
			JTextArea txtInfo = new JTextArea();
			JScrollPane spnlPane = new JScrollPane();
			spnlPane.getViewport().add(txtInfo);
			txtInfo.setText(strHint);
			txtInfo.setEditable(false);

			JPanel pnlBtn = new JPanel();
			pnlBtn.setLayout(new FlowLayout(FlowLayout.CENTER));
			FButton btnOK = new FButton("ok", "ȷ��");
			btnOK.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					HintDlg.this.dispose();

				}

			});
			pnlBtn.add(btnOK);

			JPanel pnlBack = new FPanel();
			pnlBack.setBorder(new EmptyBorder(new Insets(10, 20, 30, 20)));
			pnlBack.setLayout(new BorderLayout());
			pnlBack.add(spnlPane, BorderLayout.CENTER);
			pnlBack.add(pnlBtn, BorderLayout.SOUTH);

			this.setTitle("������ʾ");
			this.getContentPane().add(pnlBack);
			this.setSize(400, 500);
			Dimension dSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation((dSize.width - this.getWidth()) / 2,
					(dSize.height - this.getHeight()) / 2);
		}

	}
}
