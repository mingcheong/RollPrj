package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Types;
import java.text.Format;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.cell.editor.CellEditor;
import com.fr.report.PaperSize;

/**
 * ����������������(������)
 *

 */
public class SysRefColThreePanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private int iOperationType;

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	private DataSet dsRefColRelatable;

	private TableColumnInfo tableColumnInfo[];

	private ReportUI reportUI;

	private Report reportPriInfo;

	private FTextArea ftxtaSql;

	private DataSet dsBody = DataSet.create();

	FPanel fpnlFieldProperty;

	// Ӣ����
	private FTextField ftxtfFieldEName;

	// ��������
	private FTextField ftxtfFieldFName;

	// �����
	private JSpinner jspFieldGroup;

	// ����Դ���ֶ�
	private FTextField ftxtfRelaField;

	// �����ֶ�
	private FCheckBox fchkPrimaryField;

	// ������ʾ���ֶ�
	private FCheckBox fchkCodeField;

	// ������ʾ���ֶ�
	private FCheckBox fchkNameField;

	// �༭�����
	private FCheckBox fchkbxLvlField;

	private SysRefCodeRule sysRefCodeRule;

	private final String CHECK_FLAG = "��";

	private int iCurRow; // ���浱ǰ��ʾ���к�

	public SysRefColThreePanel() {
		try {
			this.setTopInset(3);
			this.setBottomInset(3);
			this.setLeftInset(3);
			this.setRightInset(3);
			RowPreferedLayout rLayout = new RowPreferedLayout(10);
			rLayout.setColumnGap(1);
			this.setLayout(rLayout);
			// �����ϰ벿�ֵı�
			TableHeader tableHeader = setReportHeader();
			CellPropertyProvider cellPropertyProvider = new CellPropertyProvider();
			reportPriInfo = new Report(tableHeader, dsBody,
					cellPropertyProvider);
			reportPriInfo.shrinkToFitRowHeight();

			reportUI = new ReportUI(reportPriInfo);
			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			// û����ѡЧ��
			reportUI.setRowSelect(false);
			// ���ù̶��к���
			reportUI.setFrozenRow(tableHeader.getRows());
			reportUI.setFrozenColumn(2);
			reportUI.getGrid().addMouseListener(new ReportUIMouseListener());
			reportUI.getGrid().addKeyListener(new ReportUIKeyListener());

			// �°벿��,�ֶ�����FPanel
			fpnlFieldProperty = new FPanel();
			fpnlFieldProperty.setLayout(new RowPreferedLayout(8));
			fpnlFieldProperty.setTitle("�ֶ�����");
			{
				// �Ų�����ϢFPanel
				FPanel fpnlField = new FPanel();
				fpnlField.setLayout(new RowPreferedLayout(6));
				ftxtfFieldEName = new FTextField("Ӣ������");
				ftxtfFieldEName.setProportion(0.175f);
				ftxtfFieldEName.setEditable(false);
				ftxtfFieldFName = new FTextField("����������");
				ftxtfFieldFName.setProportion(0.175f);
				ftxtfFieldFName.setEditable(false);
				// �����Label
				FLabel flblFieldGroup = new FLabel();
				flblFieldGroup.setText("����ţ�");
				// �����JSpinner
				SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
				jspFieldGroup = new JSpinner(spinnerModel);
				jspFieldGroup.addChangeListener(new FieldGroupChangeListener());

				ftxtfRelaField = new FTextField("����Դ���ֶΣ�");
				ftxtfRelaField.setProportion(0.34f);
				((JTextField) ftxtfRelaField.getEditor()).getDocument()
						.addDocumentListener(new RelaFieldDocumentListener());

				// Ӣ����
				fpnlField.addControl(ftxtfFieldEName, new TableConstraints(1,
						6, true, true));
				// ȫ��
				fpnlField.addControl(ftxtfFieldFName, new TableConstraints(1,
						6, true, true));
				// �����Label
				fpnlField.addControl(flblFieldGroup, new TableConstraints(1, 1,
						true, true));
				// �����JSpinner
				fpnlField.add(jspFieldGroup);// , new TableConstraints(1, 1,
				// true, true));
				fpnlField.addControl(ftxtfRelaField, new TableConstraints(1, 4,
						true, true));

				// ��������FCheckBox
				fchkPrimaryField = new FCheckBox("�����ֶ�");
				fchkPrimaryField.setTitlePosition("RIGHT");
				fchkPrimaryField
						.addValueChangeListener(new PrimaryFieldValueChangeListener());
				fchkCodeField = new FCheckBox("������ʾ���ֶ�");
				fchkCodeField.setTitlePosition("RIGHT");
				fchkCodeField
						.addValueChangeListener(new CodeFieldValueChangeListener());
				fchkNameField = new FCheckBox("������ʾ���ֶ�");
				fchkNameField.setTitlePosition("RIGHT");
				fchkNameField
						.addValueChangeListener(new NameFieldValueChangeListener());

				// �����FPanel
				FPanel fpnlLvlStyle = new FPanel();
				RowPreferedLayout rLay = new RowPreferedLayout(1);
				rLay.setRowGap(1);
				fpnlLvlStyle.setLayout(rLay);
				fpnlLvlStyle.setTitle("�����");
				fchkbxLvlField = new FCheckBox("�༭�����");
				fchkbxLvlField.setTitlePosition("RIGHT");
				fchkbxLvlField
						.addValueChangeListener(new LvlFieldValueChangeListener());
				sysRefCodeRule = new SysRefCodeRule();

				((JTextField) sysRefCodeRule.getFtxtCodeRule().getEditor())
						.getDocument().addDocumentListener(
								new CodeRuleDocumentListener());

				fpnlLvlStyle.addControl(fchkbxLvlField, new TableConstraints(1,
						1, false, true));
				fpnlLvlStyle.addControl(sysRefCodeRule, new TableConstraints(1,
						1, true, true));

				// �Ų�����ϢfpnlField
				fpnlFieldProperty.addControl(fpnlField, new TableConstraints(3,
						6, false, true));
				// ����FCheckBox
				fpnlFieldProperty.addControl(fchkPrimaryField,
						new TableConstraints(1, 2, false, true));
				fpnlFieldProperty.addControl(fchkCodeField,
						new TableConstraints(1, 2, false, true));
				fpnlFieldProperty.addControl(fchkNameField,
						new TableConstraints(1, 2, false, true));
				// �����FPanel
				fpnlFieldProperty.addControl(fpnlLvlStyle,
						new TableConstraints(4, 8, false, true));
			}

			// �ο���ѯ���
			FPanel fpnlSql = new FPanel();
			fpnlSql.setLayout(new RowPreferedLayout(1));
			fpnlSql.setTitle("�ο���ѯ���");
			ftxtaSql = new FTextArea();
			ftxtaSql.setTitleVisible(false);
			ftxtaSql.setEditable(false);
			ftxtaSql.setBorder(null);
			fpnlSql
					.addControl(ftxtaSql,
							new TableConstraints(1, 1, true, true));

			this.add(reportUI, new TableConstraints(1, 10, true, true));
			this.addControl(fpnlFieldProperty, new TableConstraints(8, 7,
					false, true));
			this.addControl(fpnlSql, new TableConstraints(8, 3, false, true));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��������������Ϣ�����ʼ���������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �����ͷDataSet
	 *
	 * @return
	 * @throws Exception
	 */
	private DataSet getHeaderDataSet() throws Exception {
		DataSet ds = DataSet.create();
		ds.append();
		ds.fieldByName("code").setValue("01");
		ds.fieldByName("name").setValue("��");
		ds.fieldByName("par_id").setValue("");
		ds.append();
		ds.fieldByName("code").setValue("0101");
		ds.fieldByName("name").setValue("Ӣ����");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("0102");
		ds.fieldByName("name").setValue("��������");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("0103");
		ds.fieldByName("name").setValue("�����");
		ds.fieldByName("par_id").setValue("01");
		ds.append();
		ds.fieldByName("code").setValue("02");
		ds.fieldByName("name").setValue("�����б�ʶ");
		ds.fieldByName("par_id").setValue("");
		ds.append();
		ds.fieldByName("code").setValue("0201");
		ds.fieldByName("name").setValue("����");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0202");
		ds.fieldByName("name").setValue("������ʾ��");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0203");
		ds.fieldByName("name").setValue("������ʾ��");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("0204");
		ds.fieldByName("name").setValue("�����");
		ds.fieldByName("par_id").setValue("02");
		ds.append();
		ds.fieldByName("code").setValue("020401");
		ds.fieldByName("name").setValue("��ʶ");
		ds.fieldByName("par_id").setValue("0204");
		ds.append();
		ds.fieldByName("code").setValue("020402");
		ds.fieldByName("name").setValue("���");
		ds.fieldByName("par_id").setValue("0204");
		ds.append();
		ds.fieldByName("code").setValue("03");
		ds.fieldByName("name").setValue("����Դ���ֶ�");
		ds.fieldByName("par_id").setValue("");
		ds.applyUpdate();
		return ds;
	}

	/**
	 * ��ʾ������Ϣ
	 *
	 * @throws Exception
	 */
	private TableHeader setReportHeader() throws Exception {
		DataSet dsHeader = getHeaderDataSet();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsHeader, "code", "name", "par_id", "code");
		TableHeader tableHeader = new TableHeader(node);
		tableHeader.setFont(this.getFont());
		return tableHeader;
	}

	private void setReportShowStyle() {
		// ���ͷ�п�
		reportPriInfo.getReportHeader().setColor(UntPub.HEADER_COLOR);
		// �����п�
		reportPriInfo.setColumnWidth(0, 110);
		reportPriInfo.setColumnWidth(1, 110);
		reportPriInfo.setColumnWidth(2, 50);
		reportPriInfo.setColumnWidth(3, 40);
		reportPriInfo.setColumnWidth(4, 40);
		reportPriInfo.setColumnWidth(5, 40);
		reportPriInfo.setColumnWidth(6, 40);
		reportPriInfo.setColumnWidth(7, 110);
		reportPriInfo.setColumnWidth(8, 200);
	}

	/**
	 * ��ʾ��¼��Ϣ,����Dataset����
	 *
	 * @throws Exception
	 *
	 */
	public void showInfo(DataSet dsRefCol, DataSet dsRefColDetail,
			DataSet dsRefColRelatable, int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		this.dsRefCol = dsRefCol;
		this.dsRefColDetail = dsRefColDetail;
		this.dsRefColRelatable = dsRefColRelatable;
		ftxtaSql.setValue(dsRefCol.fieldByName("SQL_DET").getString());
		setReport();
		setReportShowStyle();
		gotoFirstRecord();
	}

	/**
	 * ����report
	 *
	 * @throws Exception
	 *
	 */
	private void setReport() throws Exception {
		dsBody.clearAll();
		for (int i = 0; i < tableColumnInfo.length; i++) {
			String sEname = tableColumnInfo[i].getColumnName().toString();
			dsBody.append();
			dsBody.fieldByName("0101").setValue(sEname);
			if (dsRefColDetail.locate("field_ename", sEname)) {
				String sFieldId = dsRefColDetail.fieldByName("field_id")
						.getString();
				dsBody.fieldByName("0102").setValue(
						dsRefColDetail.fieldByName("field_cname").getString());
				if (dsRefColDetail.fieldByName("field_group").getValue() == null)
					dsBody.fieldByName("0103").setValue("");
				else
					dsBody.fieldByName("0103").setValue(
							dsRefColDetail.fieldByName("field_group")
									.getString());

				// �������ʾ��
				if (dsRefCol.fieldByName("CODE_FIELD").getValue() != null
						&& dsRefCol.fieldByName("CODE_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("0202").setValue(CHECK_FLAG);
				} else {
					dsBody.fieldByName("0202").setValue("");
				}
				// ��������ʾ��
				if (dsRefCol.fieldByName("NAME_FIELD") != null
						&& dsRefCol.fieldByName("NAME_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("0203").setValue(CHECK_FLAG);
				} else {
					dsBody.fieldByName("0203").setValue("");
				}
				// ������
				if (dsRefCol.fieldByName("LVL_FIELD").getValue() != null
						&& dsRefCol.fieldByName("LVL_FIELD").getString()
								.equals(sEname)) {
					dsBody.fieldByName("020401").setValue(CHECK_FLAG);
					dsBody.fieldByName("020402").setValue(
							dsRefCol.fieldByName("LVL_STYLE").getString());
				} else {
					dsBody.fieldByName("020401").setValue("");
					dsBody.fieldByName("020402").setValue("");
				}
				if (dsRefColRelatable.locate("field_id", sFieldId)) {
					String sRelaField = dsRefColRelatable.fieldByName(
							"RELATION_TABLE").getString()
							+ "."
							+ dsRefColRelatable.fieldByName("RELATION_FIELD")
									.getString();
					dsBody.fieldByName("03").setValue(sRelaField);
				} else {
					dsBody.fieldByName("03").setValue("");
				}
				dsBody.fieldByName("flag").setValue("1");
				dsBody.fieldByName("field_id").setValue(sFieldId);
			} else {
				dsBody.fieldByName("0102").setValue("");
				dsBody.fieldByName("0103").setValue("");
				dsBody.fieldByName("flag").setValue("0");
			}
			// ������
			if (dsRefCol.fieldByName("PRIMARY_FIELD").getString()
					.equals(sEname)) {
				dsBody.fieldByName("0201").setValue(CHECK_FLAG);
			} else {
				dsBody.fieldByName("0201").setValue("");
			}

		}
		reportUI.repaint();

	}

	private void showDetailInfo() throws Exception {
		int iRow = reportPriInfo.getSelectedRow();
		if (iRow < reportPriInfo.getReportHeader().getRows())
			return;
		iCurRow = iRow;
		// Ӣ����
		ftxtfFieldEName.setValue(reportPriInfo.getCellValue(0, iRow));
		// �����ֶ�
		if (CHECK_FLAG.equals(reportPriInfo.getCellValue(3, iRow)))
			((JCheckBox) fchkPrimaryField.getEditor()).setSelected(true);
		else
			((JCheckBox) fchkPrimaryField.getEditor()).setSelected(false);
		if ("0".equals(dsBody.fieldByName("flag").getString())) {
			// ��������
			ftxtfFieldFName.setValue("");
			// �����
			jspFieldGroup.setValue(new Integer(0));
			// ����Դ���ֶ�
			ftxtfRelaField.setValue("");
			// ������ʾ���ֶ�
			((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
			// ������ʾ���ֶ�
			((JCheckBox) fchkNameField.getEditor()).setSelected(false);
			// �༭�����
			((JCheckBox) fchkbxLvlField.getEditor()).setSelected(false);
			sysRefCodeRule.getFtxtCodeRule().setValue("");
			setAllControlEnabledFalse(true);
		} else {
			// ��������
			ftxtfFieldFName.setValue(reportPriInfo.getCellValue(1, iRow));
			// �����
			Object sFieldGroup = reportPriInfo.getCellValue(2, iRow);
			if ("".equals(sFieldGroup) || sFieldGroup == null)
				jspFieldGroup.setValue(new Integer(0));
			else
				jspFieldGroup.setValue(new Integer(sFieldGroup.toString()));
			// ����Դ���ֶ�
			ftxtfRelaField.setValue(reportPriInfo.getCellValue(8, iRow));
			// ������ʾ���ֶ�
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(4, iRow)))
				((JCheckBox) fchkCodeField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
			// ������ʾ���ֶ�
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(5, iRow)))
				((JCheckBox) fchkNameField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkNameField.getEditor()).setSelected(false);
			// �༭�����
			if (CHECK_FLAG.equals(reportPriInfo.getCellValue(6, iRow)))
				((JCheckBox) fchkbxLvlField.getEditor()).setSelected(true);
			else
				((JCheckBox) fchkbxLvlField.getEditor()).setSelected(false);
			sysRefCodeRule.getFtxtCodeRule().setValue(
					reportPriInfo.getCellValue(7, iRow));
			setAllControlEnabledTrue();
		}
		if (iOperationType == SysRefColOperation.rcoView)
			setAllControlEnabledFalse(false);
	}

	/**
	 * �������пؼ�������
	 *
	 * @param bShowPrimaryField:true:�ɱ༭,false
	 *            :���ɱ༭
	 */
	private void setAllControlEnabledFalse(boolean bShowPrimaryField) {
		Common.changeChildControlsEditMode(fpnlFieldProperty, false);
		jspFieldGroup.setEnabled(false);
		sysRefCodeRule.getJspChoiceLen().setEnabled(false);
		if (bShowPrimaryField)
			fchkPrimaryField.setEditable(true);
		else
			fchkPrimaryField.setEditable(false);
	}

	/**
	 * �������пؼ�����
	 *
	 */
	private void setAllControlEnabledTrue() {
		// �����
		jspFieldGroup.setEnabled(true);
		// ����Դ���ֶ�
		ftxtfRelaField.setEditable(true);
		// �����ֶ�
		fchkPrimaryField.setEditable(true);
		// ������ʾ���ֶ�
		fchkCodeField.setEditable(true);
		// ������ʾ���ֶ�
		fchkNameField.setEditable(true);
		// �༭�����
		fchkbxLvlField.setEditable(true);
		setCodeRuleState();
	}

	private void setCodeRuleState() {
		if (Common.estimate(fchkbxLvlField.getValue())) {
			Common.changeChildControlsEditMode(sysRefCodeRule, true);
			sysRefCodeRule.getJspChoiceLen().setEnabled(true);
			((SysRefCodeRule) sysRefCodeRule).getFtxtCodeRule().setEditable(
					false);
		} else {
			Common.changeChildControlsEditMode(sysRefCodeRule, false);
			sysRefCodeRule.getJspChoiceLen().setEnabled(false);
			((SysRefCodeRule) sysRefCodeRule).getFtxtCodeRule().setEditable(
					false);
		}
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	private class ReportUIMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				showDetailInfo();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"��ʾ��������������Ϣ�������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private class ReportUIKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			try {
				showDetailInfo();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"��ʾ��������������Ϣ�������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	};

	private void gotoFirstRecord() throws Exception {
		reportUI.setCellSelection(new CellSelection(0, reportPriInfo
				.getReportHeader().getRows()));
		dsBody.beforeFirst();
		if (!dsBody.isEmpty())
			dsBody.next();
		showDetailInfo();

	}

	private class RelaFieldDocumentListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			try {
				dsBody.fieldByName("03").setValue(ftxtfRelaField.getValue());
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"�༭����Դ���ֶη������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);

		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}
	}

	/**
	 * �����ֶ�
	 *
	 * @author Administrator
	 *
	 */
	private class PrimaryFieldValueChangeListener implements
			ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkPrimaryField.getValue())) {
					clearColShow("0201");
					dsBody.fieldByName("0201").setValue(CHECK_FLAG);
					reportPriInfo.setCellValue(3, iCurRow, CHECK_FLAG);
				} else {
					dsBody.fieldByName("0201").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"ѡ�������ֶη������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}
	}

	/**
	 *
	 *
	 * ������ʾ���ֶ�
	 *
	 * @author Administrator
	 */
	private class CodeFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkCodeField.getValue())) {
					clearColShow("0202");
					dsBody.fieldByName("0202").setValue(CHECK_FLAG);
					((JCheckBox) fchkNameField.getEditor()).setSelected(false);
				} else {
					dsBody.fieldByName("0202").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"ѡ�������ʾ�з������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
		}
	}

	/**
	 *
	 * ������ʾ���ֶ�
	 *
	 * @author Administrator
	 *
	 */
	private class NameFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkNameField.getValue())) {
					clearColShow("0203");
					dsBody.fieldByName("0203").setValue(CHECK_FLAG);
					((JCheckBox) fchkCodeField.getEditor()).setSelected(false);
				} else {
					dsBody.fieldByName("0203").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"ѡ��������ʾ���ֶη������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}
	}

	/**
	 * ���һ�е���ʾ��Ϣ
	 *
	 * @param iCol
	 * @throws Exception
	 */
	private void clearColShow(String sFieldName) throws Exception {
		String sBookmark = dsBody.toogleBookmark();
		dsBody.beforeFirst();
		while (dsBody.next()) {
			dsBody.fieldByName(sFieldName).setValue("");
		}
		dsBody.gotoBookmark(sBookmark);
	}

	private class CodeRuleDocumentListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			try {
				dsBody.fieldByName("020402").setValue(
						sysRefCodeRule.getFtxtCodeRule().getValue());
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"�༭����뷢�����󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);

		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);
		}
	}

	/**
	 * �༭�����
	 *
	 * @author Administrator
	 *
	 */
	private class LvlFieldValueChangeListener implements ValueChangeListener {

		public void valueChanged(ValueChangeEvent arg0) {
			try {
				if (Common.estimate(fchkbxLvlField.getValue())) {
					String sLvlCode = dsBody.fieldByName("020402").getString();
					clearColShow("020401");
					clearColShow("020402");
					dsBody.fieldByName("020401").setValue(CHECK_FLAG);
					dsBody.fieldByName("020402").setValue(sLvlCode);
				} else {
					dsBody.fieldByName("020401").setValue("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"ѡ�����뷢�����󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

			reportUI.repaint();
			setCodeRuleState();
		}
	}

	/**
	 * �����д����Ϣ
	 *
	 * @throws Exception
	 *
	 */
	private InfoPackage CheckFillInfo() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sBookmark = dsBody.toogleBookmark();
		// ��鱻����Ϊ������ʶ���ֶΣ�����Ҫ����������
		if (dsBody.locate("020401", CHECK_FLAG)) {
			if (dsBody.fieldByName("020402").getValue() == null
					|| "".equals(dsBody.fieldByName("020402").getString())) {
				dsBody.gotoBookmark(sBookmark);
				sysRefCodeRule.getFtxtCodeRule().setFocus();
				infoPackage.setsMessage("������������");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
		}
		String sErrorInfo = "";
		// ����ָ������ֵ
		if (!dsBody.locate("0201", CHECK_FLAG)) {
			sErrorInfo = "�����ֶ�";
		}
		// ������ѡȡ��ʽΪ��ʱ����Ҫȷ����ʾ�����С���ʾ�����кͲ�����ʶ
		if (dsRefCol.fieldByName("SELECT_KIND").getInteger() == 1) {
			if (!dsBody.locate("0202", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "������ʾ��";
			}
			if (!dsBody.locate("0203", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "������ʾ��";
			}
			if (!dsBody.locate("020401", CHECK_FLAG)) {
				if (!"".equals(sErrorInfo))
					sErrorInfo = sErrorInfo + ",";
				sErrorInfo = sErrorInfo + "������ʶ";
			}
		}
		if (!"".equals(sErrorInfo)) {
			sErrorInfo = "��ָ����" + sErrorInfo;
			dsBody.gotoBookmark(sBookmark);
			infoPackage.setsMessage(sErrorInfo);
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		// �жϹ���Դ���ֶ��Ƿ���д,�Ƿ���д��ȷ
		dsBody.beforeFirst();
		while (dsBody.next()) {
			if (dsBody.fieldByName("flag").getInteger() == 0)
				continue;
			if (dsBody.fieldByName("03").getValue() == null
					|| "".equals(dsBody.fieldByName("03").getString())) {
				dsBody.gotoBookmark(sBookmark);
				infoPackage.setsMessage("��������Ч�Ĺ���Դ���ֶ���Ϣ��");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
			String sRelaField = dsBody.fieldByName("03").getString();
			String[] sRelaFieldArray = sRelaField.split("\\.");
			if (sRelaFieldArray.length != 2) {
				dsBody.gotoBookmark(sBookmark);
				infoPackage.setsMessage("��������Ч�Ĺ���Դ���ֶ���Ϣ,��ʽ:����.�ֶ�����");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
		}
		dsBody.gotoBookmark(sBookmark);
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	private void saveInfo() throws Exception {
		String sBookmark = dsBody.toogleBookmark();
		dsRefColRelatable.clearAll();
		dsBody.beforeFirst();
		while (dsBody.next()) {
			saveRefCol();// ����������Ϣ
			if (dsBody.fieldByName("flag").getInteger() == 0)
				continue;
			saveDetail();// ������ϸ��
			saveRelatable();// �������Դ��
		}
		dsBody.gotoBookmark(sBookmark);
	}

	/**
	 * ����������Ϣ
	 *
	 * @throws Exception
	 *
	 */
	private void saveRefCol() throws Exception {
		String sEname = dsBody.fieldByName("0101").getString();
		if (CHECK_FLAG.equals(dsBody.fieldByName("0201").getString())) {
			dsRefCol.fieldByName("PRIMARY_FIELD").setValue(sEname);
			int iFieldType = getFieldTypeWithFieldName(sEname);
			if (Types.CHAR == iFieldType || Types.VARCHAR == iFieldType)
				dsRefCol.fieldByName("PRIMARY_TYPE").setValue("1");
			else
				dsRefCol.fieldByName("PRIMARY_TYPE").setValue("0");
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("0202").getString())) {
			dsRefCol.fieldByName("CODE_FIELD").setValue(sEname);
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("0203").getString())) {
			dsRefCol.fieldByName("NAME_FIELD").setValue(sEname);
		}
		if (CHECK_FLAG.equals(dsBody.fieldByName("020401").getString())) {
			dsRefCol.fieldByName("LVL_FIELD").setValue(sEname);
		}
		if (!"".equals(dsBody.fieldByName("020402").getString())) {
			dsRefCol.fieldByName("LVL_STYLE").setValue(
					dsBody.fieldByName("020402").getString());
		}
	}

	private int getFieldTypeWithFieldName(String sFieldName) {
		if (sFieldName == null || "".equals(sFieldName))
			return -1;
		for (int i = 0; i < tableColumnInfo.length; i++) {
			if (sFieldName.equals(tableColumnInfo[i].getColumnName())) {
				return tableColumnInfo[i].getColumnType();
			}
		}
		return -1;
	}

	/**
	 * ������ϸ����Ϣ
	 *
	 * @throws Exception
	 *
	 */
	private void saveDetail() throws Exception {
		if (dsRefColDetail.locate("FIELD_ID", dsBody.fieldByName("FIELD_ID")
				.getString())) {
			if (!"".equals(dsBody.fieldByName("0103").getString())) {
				dsRefColDetail.fieldByName("FIELD_GROUP").setValue(
						dsBody.fieldByName("0103").getString());
			}
		}
	}

	private void saveRelatable() throws Exception {
		dsRefColRelatable.append();
		dsRefColRelatable.fieldByName("REFCOL_ID").setValue(
				dsRefCol.fieldByName("refcol_id").getString());
		dsRefColRelatable.fieldByName("FIELD_ID").setValue(
				dsBody.fieldByName("field_id").getString());
		String sReLation = dsBody.fieldByName("03").getString();
		if (!"".equals(sReLation)) {
			String[] sRelationArray = sReLation.split("\\.");
			dsRefColRelatable.fieldByName("RELATION_TABLE").setValue(
					sRelationArray[0]);
			dsRefColRelatable.fieldByName("RELATION_FIELD").setValue(
					sRelationArray[1]);
		}
		dsRefColRelatable.fieldByName("SET_YEAR").setValue(Global.loginYear);
		dsRefColRelatable.fieldByName(ISysRefCol.RG_CODE).setValue(
				Global.getCurrRegion());
	}

	public InfoPackage nextOperate() throws Exception {
		InfoPackage infoPackage = CheckFillInfo();
		if (!infoPackage.getSuccess())
			return infoPackage;
		saveInfo();
		return infoPackage;
	}

	private class FieldGroupChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			try {
				if ("0".equals(jspFieldGroup.getValue().toString())) {
					dsBody.fieldByName("0103").setValue("");

				} else {
					dsBody.fieldByName("0103").setValue(
							jspFieldGroup.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColThreePanel.this,
						"�༭����ŷ������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			reportUI.repaint();
		}

	}

	public void setTableColumnInfo(TableColumnInfo[] tableColumnInfo) {
		this.tableColumnInfo = tableColumnInfo;
	}

	private class CellPropertyProvider implements PropertyProvider {

		public boolean isEditable(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return false;
		}

		public CellEditor getEditor(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return null;
		}

		public double getColumnWidth(Object fieldId) {
			// TODO Auto-generated method stub
			return 0;
		}

		public Format getFormat(String bookmark, Object fieldId) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getFieldName(Object fieldId) {
			return fieldId.toString();
		}

		public Object getFieldId(String fieldName) {
			return fieldName;
		}
	}

}
