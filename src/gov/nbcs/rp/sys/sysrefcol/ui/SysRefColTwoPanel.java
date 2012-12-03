package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.PaperSize;

/**
 * ����������������(�ڶ���)
 * 

 * 
 */
public class SysRefColTwoPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private int iOperationType;

	// �����ͷ��Ϣ(�ֶ������ֶ�����)
	TableColumnInfo tableColumnInfo[];

	private DataSet dsRefCol;

	private DataSet dsRefColDetail;

	// ��ŵ�һ����д��sql��ѯ�������ݼ�
	private DataSet dsQuery;

	private Report reportQuery;

	private ReportUI reportUI;

	// ��ǰ����Ϣ
	private FPanel fpnlFieldInfo;

	// ��������
	private FCheckBox fchkFieldHide;

	// ��Ӣ����
	private FTextField ftxtfFieldEName;

	// ��������
	private FTextField ftxtfFieldCName;

	// ��������
	private FRadioGroup frdoFieldType;

	// ��Ӧ������
	private TableComboBox fcbxReservedField;

	// ��ʾ���
	private FComboBox fcbxDisplayFormat;

	// ������
	private FRadioGroup frdoFieldFlag;

	private FPanel fpnlFieldCodeTypes;

	// ������ʾ���
	private SysRefCodeRule fpnlCodeRule;

	// ��ʾĩ�����
	private FCheckBox fchkShowLastLevel;

	private final ISysRefCol sysRefColServ = SysRefColI
			.getMethod();

	private Vector vectorInc = new Vector();

	private Vector vectorNumeric = new Vector();

	private Vector vectorDate = new Vector();

	private Object[][] objectInc;

	private Object[][] objectNumeric;

	private Object[][] objectVarchar;

	private Object[][] objectDate;

	private final String header[] = { "������", "�ֶ���" };

	private final String INC = "inc";

	private final String NUMERIC = "numeric";

	private final String VARCHAR = "varchar";

	private final String DATE = "date";

	final private String FIELDCODE = "FIELDCODE";

	final private String FIELDNAME = "FIELDNAME";

	final private String LVL_ID = "LVL_ID";

	private int iCurCol;

	private int iCurRow;

	private String sCurEname;

	public SysRefColTwoPanel() {
		try {
			this.setTopInset(3);
			this.setBottomInset(3);
			this.setLeftInset(3);
			this.setRightInset(3);
			this.setLayout(new RowPreferedLayout(1));
			reportQuery = new Report();

			reportUI = new ReportUI(reportQuery);
			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			reportUI.getGrid().addMouseListener(new ReportUIMouseListener());
			reportUI.getGrid().addKeyListener(new ReportUIKeyListener());

			reportUI.getGridColumn().addMouseListener(
					new ReportUIMouseListener());
			reportUI.getGridColumn().addKeyListener(new ReportUIKeyListener());

			// ��ǰ����ϢPanel
			fpnlFieldInfo = new FPanel();
			fpnlFieldInfo.setLayout(new RowPreferedLayout(6));
			fpnlFieldInfo.setTitle("��ǰ����Ϣ");
			{
				fchkFieldHide = new FCheckBox("��������");
				fchkFieldHide.setTitlePosition("RIGHT");
				fchkFieldHide
						.addValueChangeListener(new FieldHideValueChangeListener());
				ftxtfFieldEName = new FTextField("��Ӣ������");
				ftxtfFieldEName.setProportion(0.3f);
				ftxtfFieldCName = new FTextField("����������");
				ftxtfFieldCName.setProportion(0.24f);
				((JTextField) ftxtfFieldCName.getEditor()).getDocument()
						.addDocumentListener(new CNameDocumentListener());

				// ��Ӧ������
				FPanel fpnlReservedField = new FPanel();
				fpnlReservedField.setLayout(new RowPreferedLayout(4));
				FLabel flblReservedField = new FLabel();
				flblReservedField.setTitle("��Ӧ�����У�");
				fcbxReservedField = new TableComboBox();
				fpnlReservedField.addControl(flblReservedField,
						new TableConstraints(1, 1, true, true));
				fpnlReservedField.add(fcbxReservedField, new TableConstraints(
						1, 3, true, true));

				fcbxDisplayFormat = new FComboBox("��ʾ���");
				fcbxDisplayFormat.setProportion(0.26f);

				// ��������Panel
				FPanel fpnlFieldType = new FPanel();
				fpnlFieldType.setLayout(new RowPreferedLayout(1));
				fpnlFieldType.setTitle("��������");
				frdoFieldType = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoFieldType.setTitleVisible(false);
				frdoFieldType.setRefModel(INC + "#����     +" + NUMERIC
						+ "#������     +" + VARCHAR + "#�ַ���     +" + DATE
						+ "#������     ");
				frdoFieldType
						.addValueChangeListener(new FieldTypeValueChangeListener());
				fpnlFieldType.addControl(frdoFieldType, new TableConstraints(1,
						1, true, true));

				// ������ Panel
				FPanel fpnlFieldFlag = new FPanel();
				fpnlFieldFlag.setLayout(new RowPreferedLayout(1));
				fpnlFieldFlag.setTitle("������");
				frdoFieldFlag = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoFieldFlag
						.setRefModel("1#������      +2#������      +3#������      ");
				frdoFieldFlag.setTitleVisible(false);
				frdoFieldFlag
						.addValueChangeListener(new FieldFlagValueChangeListener());

				fpnlFieldFlag.addControl(frdoFieldFlag, new TableConstraints(1,
						1, true, true));

				// ������
				fpnlFieldCodeTypes = new FPanel();
				RowPreferedLayout rLayoutCodeType = new RowPreferedLayout(1);
				rLayoutCodeType.setColumnGap(1);
				fpnlFieldCodeTypes.setLayout(rLayoutCodeType);
				fpnlFieldCodeTypes.setTitle(" ������ ");
				fpnlCodeRule = new SysRefCodeRule();
				fchkShowLastLevel = new FCheckBox("��ĩ��������Ϊ������");
				fchkShowLastLevel.setTitlePosition("RIGHT");
				fpnlFieldCodeTypes.addControl(fpnlCodeRule,
						new TableConstraints(2, 1, false, true));
				// ��ĩ��������Ϊ������
				fpnlFieldCodeTypes.addControl(fchkShowLastLevel,
						new TableConstraints(1, 1, false, true));

				// ��������
				fpnlFieldInfo.addControl(fchkFieldHide, new TableConstraints(1,
						1, false, true));
				// ��Ӣ����
				fpnlFieldInfo.addControl(ftxtfFieldEName, new TableConstraints(
						1, 2, false, true));
				// ������ Panel
				fpnlFieldInfo.addControl(fpnlFieldFlag, new TableConstraints(2,
						3, false, true));
				// ��������
				fpnlFieldInfo.addControl(ftxtfFieldCName, new TableConstraints(
						1, 3, false, true));

				// ��������Panel
				fpnlFieldInfo.addControl(fpnlFieldType, new TableConstraints(2,
						3, false, true));
				// ������
				fpnlFieldInfo.addControl(fpnlFieldCodeTypes,
						new TableConstraints(4, 3, false, true));

				// ��Ӧ������
				fpnlFieldInfo.add(fpnlReservedField, new TableConstraints(1, 3,
						false, true));

				// ��ʾ���
				fpnlFieldInfo.addControl(fcbxDisplayFormat,
						new TableConstraints(1, 3, false, true));

			}
			// �ϲ���Table
			this.add(reportUI, new TableConstraints(1, 1, true, true));
			// ��ǰ����ϢPanel
			this.addControl(fpnlFieldInfo, new TableConstraints(7, 1, false,
					true));

			((JCheckBox) fchkFieldHide.getEditor()).setSelected(true);

			displayFormat();
			dispalyReserved();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��������������Ϣ�����ʼ���������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	private class FieldHideValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (Common.estimate(fchkFieldHide.getValue())) {
				setAllControlEnabledFalse(true);
			} else {
				setAllControlEnabledTrue();
			}

		}
	}

	/**
	 * �������пؼ�����
	 * 
	 */
	private void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(fpnlFieldInfo, true);
		ftxtfFieldEName.setEditable(false);
		fcbxReservedField.setEnabled(true);
		if (frdoFieldFlag.getValue() == null)
			frdoFieldFlag.setValue("3");
		switch (Integer.parseInt(frdoFieldFlag.getValue().toString())) {
		case 1:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, true);
			fpnlCodeRule.getJspChoiceLen().setEnabled(true);
			frdoFieldType.setEditable(false);
			break;
		case 2:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
			fpnlCodeRule.getJspChoiceLen().setEnabled(false);
			frdoFieldType.setEditable(false);
			break;
		case 3:
			Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
			fpnlCodeRule.getJspChoiceLen().setEnabled(false);
			frdoFieldType.setEditable(true);
			break;
		}
		if (frdoFieldType.getValue() == null)
			frdoFieldType.setValue(VARCHAR);
		if (INC.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		} else if (NUMERIC.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		} else if (VARCHAR.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(false);
		} else if (DATE.equals(frdoFieldType.getValue().toString())) {
			fcbxDisplayFormat.setEnabled(true);
		}
		((SysRefCodeRule) fpnlCodeRule).getFtxtCodeRule().setEditable(false);
		ftxtfFieldEName.setValue(sCurEname);
	}

	/**
	 * �������пؼ�������
	 * 
	 * @param bShowFieldHide,����"��������"�ؼ��Ƿ���Ա༭��true:�ɱ༭��
	 *            false�����ɱ༭
	 */
	private void setAllControlEnabledFalse(boolean bShowFieldHide) {
		Common.changeChildControlsEditMode(fpnlFieldInfo, false);
		fpnlCodeRule.getJspChoiceLen().setEnabled(false);
		fcbxReservedField.setEnabled(false);
		if (bShowFieldHide)
			fchkFieldHide.setEditable(true);
		else
			fchkFieldHide.setEditable(false);
	}

	private class FieldTypeValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (frdoFieldType.getValue() == null) {
				frdoFieldType.setValue(VARCHAR);
			}
			if (INC.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorInc);
				fcbxReservedField.setModel(objectInc, header);
			} else if (NUMERIC.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorNumeric);
				fcbxReservedField.setModel(objectNumeric, header);
			} else if (VARCHAR.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(false);
				fcbxReservedField.setModel(objectVarchar, header);
			} else if (DATE.equals(frdoFieldType.getValue().toString())) {
				fcbxDisplayFormat.setEnabled(true);
				fcbxDisplayFormat.setRefModel(vectorDate);
				fcbxReservedField.setModel(objectDate, header);
			}
			fcbxDisplayFormat.setValue("");
			fcbxReservedField.setValue("");
		}
	}

	private class FieldFlagValueChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			if (frdoFieldFlag.getValue() == null)
				frdoFieldFlag.setValue("3");
			switch (Integer.parseInt(frdoFieldFlag.getValue().toString())) {
			case 1:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, true);
				fpnlCodeRule.getJspChoiceLen().setEnabled(true);
				frdoFieldType.setEditable(false);
				frdoFieldType.setValue(VARCHAR);
				((SysRefCodeRule) fpnlCodeRule).getFtxtCodeRule().setEditable(
						false);
				break;
			case 2:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
				fpnlCodeRule.getJspChoiceLen().setEnabled(false);
				fpnlCodeRule.getFtxtCodeRule().setValue("");
				frdoFieldType.setEditable(false);
				frdoFieldType.setValue(VARCHAR);
				((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
				break;
			case 3:
				Common.changeChildControlsEditMode(fpnlFieldCodeTypes, false);
				fpnlCodeRule.getJspChoiceLen().setEnabled(false);
				fpnlCodeRule.getFtxtCodeRule().setValue("");
				frdoFieldType.setEditable(true);
				((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
				break;
			}
		}
	}

	/**
	 * ��ʾ��ʽ
	 * 
	 * @throws Exception
	 */
	private void displayFormat() throws Exception {
		DataSet getDisplayFormat = sysRefColServ
				.getDisplayFormat(Global.loginYear);
		vectorInc.addElement("");
		vectorNumeric.addElement("");
		vectorDate.addElement("");
		getDisplayFormat.beforeFirst();
		while (getDisplayFormat.next()) {
			String sCVuale = getDisplayFormat.fieldByName("CValue").getString();
			String sName = getDisplayFormat.fieldByName("Name").getString();
			if ("������ֵ".equals(sCVuale)) {
				vectorInc.addElement(sName);
			}
			if ("��С������ֵ".equals(sCVuale)) {
				vectorNumeric.addElement(sName);
			}
			if ("����".equals(sCVuale)) {
				vectorDate.addElement(sName);
			}
		}
	}

	private void dispalyReserved() throws Exception {
		DataSet getDisplayFormat = sysRefColServ
				.getReservedField(Global.loginYear);
		DataSet getDisplayFormatTemp;
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='" + INC
				+ "'");
		objectInc = setObjectValue(getDisplayFormatTemp, objectInc);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='"
				+ NUMERIC + "'");
		objectNumeric = setObjectValue(getDisplayFormatTemp, objectNumeric);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='"
				+ VARCHAR + "'");
		objectVarchar = setObjectValue(getDisplayFormatTemp, objectVarchar);
		getDisplayFormatTemp = DataSetUtil.filterBy(getDisplayFormat, "DATA_TYPE=='" + DATE
				+ "'");
		objectDate = setObjectValue(getDisplayFormatTemp, objectDate);

	}

	private Object[][] setObjectValue(DataSet dataSet, Object[][] object)
			throws Exception {
		object = new Object[dataSet.getRecordCount() + 1][2];
		object[0][0] = "";
		object[0][1] = "";
		int i = 1;
		dataSet.beforeFirst();
		while (dataSet.next()) {
			object[i][0] = dataSet.fieldByName("FIELD_CNAME").getString();
			object[i][1] = dataSet.fieldByName("FIELD_ENAME").getString();
			i++;
		}
		return object;
	}

	/**
	 * �����ͷDataSet
	 * 
	 * @return
	 * @throws Exception
	 */
	private DataSet getHeaderDataSet() throws Exception {
		DataSet ds = DataSet.create();
		String sFieldName;
		String sSqlDet = dsRefCol.fieldByName("SQL_DET").getString();
		sSqlDet = SysRefColPub.ReplaceRefColFixFlag(sSqlDet.toUpperCase());
		tableColumnInfo = sysRefColServ.getFieldInfo(sSqlDet);
		for (int i = 0; i < tableColumnInfo.length; i++) {
			sFieldName = tableColumnInfo[i].getColumnName().toUpperCase();
			ds.append();
			ds.fieldByName(FIELDCODE).setValue(sFieldName);
			ds.fieldByName(FIELDNAME).setValue(sFieldName);
			ds.fieldByName(LVL_ID).setValue(
					Common.getStrID(new BigDecimal(i + 1), 4));
		}
		// �ж��ֶ��Ƿ����ı䣬ɾ����ϸ��͹���Դ�����Ѳ����ڵļ�¼��
		List lstDelBookmark = new ArrayList();
		String sBookmark = dsRefColDetail.toogleBookmark();
		dsRefColDetail.beforeFirst();
		while (dsRefColDetail.next()) {
			//modify by qzc 2009.6.5
			String sEname = dsRefColDetail.fieldByName("FIELD_ENAME")
					.getString().toUpperCase();
			if (!ds.locate(FIELDCODE, sEname)) {
				JOptionPane.showMessageDialog(this, "��ʧ�ֶ�:"
						+ dsRefColDetail.fieldByName("field_cname").getString()
						+ "(" + sEname + ")!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				lstDelBookmark.add(dsRefColDetail.toogleBookmark());
			} else {
				ds.fieldByName(FIELDNAME).setValue(
						dsRefColDetail.fieldByName("FIELD_CNAME").getString());
			}
		}
		for (int i = 0; i < lstDelBookmark.size(); i++) {
			dsRefColDetail.gotoBookmark(lstDelBookmark.get(i).toString());
			dsRefColDetail.delete();
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		return ds;
	}

	/**
	 * �����һ��ҳ��д��sql����ѯ���ļ�¼
	 * 
	 * @param dsQuery
	 */
	public void setDsQuery(DataSet dsQuery) {
		this.dsQuery = dsQuery;
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 * @throws Exception
	 */
	private void setReport() throws Exception {
		DataSet dsHeader = getHeaderDataSet();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsHeader, FIELDCODE, FIELDNAME, "", LVL_ID);
		TableHeader tableHeader = new TableHeader(node);
		tableHeader.setFont(this.getFont());
		reportQuery.removeAllCellElements();
		reportQuery.setReportHeader(tableHeader);
		reportQuery.setBodyData(dsQuery);
		reportQuery.refreshBody();
		reportUI.repaint();
		// ���ͷ�п�
		reportQuery.getReportHeader().setColor(UntPub.HEADER_COLOR);
		for (int i = 0; i < reportQuery.getReportHeader().getColumns(); i++) {
			reportQuery.setColumnWidth(i, 100);
		}
	}

	/**
	 * �任��,�ж�����Ϣ��д����ȷ������ȷ������任�У���ȷ�������ɾ������Ϣ����ʾ��ѡ���е���Ϣ
	 * 
	 * @throws Exception
	 * 
	 */
	private void changeCol() throws Exception {
		// �ж�ѡ�е��Ƿ���ͬһ��
		if (iCurCol == reportQuery.getSelectedCell().getColumn())
			return;
		InfoPackage infoPackage = checkFillInfo();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(SysRefColTwoPanel.this, infoPackage
					.getsMessage(), "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			CellSelection cs = new CellSelection(iCurCol, iCurRow);
			reportUI.setCellSelection(cs);
			return;
		}
		// ���浱ǰ��Ԫ����Ϣ
		operateRefColDetailToDs();
		// ������ѡ��ĵ�Ԫ����Ϣ
		saveCurCellInfo();
		showDetailInfo(sCurEname);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class ReportUIMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				changeCol();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColTwoPanel.this,
						"��ʾ���������ݷ������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private class ReportUIKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			try {
				changeCol();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefColTwoPanel.this,
						"��ʾ���������ݷ������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	};

	/**
	 * ��ʾ��¼��Ϣ,����Dataset����
	 * 
	 * @throws Exception
	 * 
	 */
	public void showInfo(DataSet dsRefCol, DataSet dsRefColDetail,
			int iOperationType) throws Exception {
		this.iOperationType = iOperationType;
		this.dsRefCol = dsRefCol;
		this.dsRefColDetail = dsRefColDetail;
		// ��ʾ������Ϣ
		setReport();
		// ���浱ǰ��Ԫ����Ϣ
		saveCurCellInfo();
		// ��ʾ��ǰѡ���е���ϸ��Ϣ
		showDetailInfo(sCurEname);
	}

	/**
	 * ����ename��ֵ��λdsRefColDetail(��������ϸ��Ϣ��)��ʾ��ǰ��¼��Ϣ
	 * 
	 * @param sFieldEname
	 * @throws Exception
	 * @return,true:��λ����¼��false,��ϸ��¼������
	 */
	private void showDetailInfo(String sFieldEname) throws Exception {
		if (!dsRefColDetail.locate("FIELD_ENAME", sFieldEname)) {
			// ��ϸ��¼������,���ø�������Ϊtrue,�����ʾ��Ϣ
			((JCheckBox) fchkFieldHide.getEditor()).setSelected(true);
			// �����ʾ����Ϣ
			ftxtfFieldEName.setValue("");
			// ��������
			ftxtfFieldCName.setValue("");
			// ��������
			frdoFieldType.setValue("");
			// ��Ӧ������
			fcbxReservedField.setValue("");
			// ��ʾ���
			fcbxDisplayFormat.setValue("");
			// ������
			frdoFieldFlag.setValue("");
			// ������ʾ���
			fpnlCodeRule.getFtxtCodeRule().setValue("");
			// ��ʾĩ�����
			((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(false);
			// ��ʾ���пؼ�������
			setAllControlEnabledFalse(true);
		} else {
			// ��ϸ��¼����,��ʾ��ϸ��¼
			// ��������
			((JCheckBox) fchkFieldHide.getEditor()).setSelected(false);
			// ��Ӣ����
			ftxtfFieldEName.setValue(dsRefColDetail.fieldByName("FIELD_ENAME")
					.getString());
			// ��������
			ftxtfFieldCName.setValue(dsRefColDetail.fieldByName("FIELD_CNAME")
					.getString());
			// ��������
			frdoFieldType.setValue(dsRefColDetail.fieldByName("FIELD_TYPE")
					.getString());
			// ��Ӧ������
			fcbxReservedField.setValue(dsRefColDetail.fieldByName(
					"RESERVED_NAME").getString());
			// ��ʾ���
			fcbxDisplayFormat.setValue(dsRefColDetail.fieldByName(
					"DISPLAY_FORMAT").getString());
			// ������
			frdoFieldFlag.setValue(dsRefColDetail.fieldByName("FIELD_FLAG")
					.getString());
			// ������ʾ���
			fpnlCodeRule.getFtxtCodeRule().setValue(
					dsRefColDetail.fieldByName("FIELD_CODE_STYLE").getString());
			// ��ʾĩ�����
			((JCheckBox) fchkShowLastLevel.getEditor()).setSelected(Common
					.estimate(new Integer(dsRefColDetail.fieldByName(
							"Show_Last_Level").getInteger())));
			// ��ʾ���пؼ�����
			if (iOperationType != SysRefColOperation.rcoView)
				setAllControlEnabledTrue();
		}
		if (iOperationType == SysRefColOperation.rcoView)
			setAllControlEnabledFalse(false);
	}

	/**
	 * ���һ�°�ťʱ�ж��Ƿ���Ϣ��д����
	 * 
	 * @throws Exception
	 * 
	 */
	public InfoPackage nextOperate() throws Exception {
		InfoPackage infoPackage = checkFillInfo();
		// ���浱ǰ��Ԫ����Ϣ
		if (!infoPackage.getSuccess()) {
			return infoPackage;
		}
		operateRefColDetailToDs();
		infoPackage = checkFillCodeAndName();
		if (!infoPackage.getSuccess()) {
			return infoPackage;
		}
		return infoPackage;
	}

	/**
	 * �ж���д����Ϣ�Ƿ���������ȷ
	 * 
	 */
	private InfoPackage checkFillInfo() {
		InfoPackage infoPackage = new InfoPackage();
		if (Common.estimate(fchkFieldHide.getValue())) {
			infoPackage.setSuccess(true);
			return infoPackage;
		}
		if ("".equals(ftxtfFieldCName.getValue().toString())) {
			ftxtfFieldCName.setFocus();
			infoPackage.setsMessage("��������������");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if ("1".equals(frdoFieldFlag.getValue().toString())
				&& "".equals(fpnlCodeRule.getFtxtCodeRule().getValue()
						.toString())) {
			fpnlCodeRule.getFtxtCodeRule().setFocus();
			infoPackage.setsMessage("�����ô�����");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * ������ϸ��Ϣ������dataset
	 * 
	 * @throws Exception
	 * 
	 */
	private void operateRefColDetailToDs() throws Exception {
		if (Common.estimate(fchkFieldHide.getValue())) {
			if (dsRefColDetail.locate("field_ename", sCurEname))
				dsRefColDetail.delete();
			// ���ñ�ͷ��ʾ����ϢΪenameֵ
			reportQuery.setCellValue(iCurCol, 0, sCurEname);
		} else {
			if (dsRefColDetail.locate("field_ename", sCurEname)) {
				dsRefColDetail.edit();
				saveRefColDetailtoDs("mod");
			} else {
				dsRefColDetail.append();
				saveRefColDetailtoDs("add");
			}
		}
	}

	private void saveRefColDetailtoDs(String sType) throws Exception {
		if (sType == "add") {
			String sRefColId = dsRefCol.fieldByName("refcol_id").getValue()
					.toString();
			dsRefColDetail.fieldByName("refcol_id").setValue(sRefColId);
			dsRefColDetail.fieldByName("field_id").setValue(
					sRefColId + getMaxCode());
		}
		dsRefColDetail.fieldByName("field_cname").setValue(
				ftxtfFieldCName.getValue().toString());
		dsRefColDetail.fieldByName("field_ename").setValue(
				ftxtfFieldEName.getValue().toString());
		if (fcbxReservedField.getValue() == null)
			dsRefColDetail.fieldByName("reserved_name").setValue("");
		else
			dsRefColDetail.fieldByName("reserved_name").setValue(
					fcbxReservedField.getValue().toString());

		dsRefColDetail.fieldByName("field_flag").setValue(
				frdoFieldFlag.getValue().toString());
		dsRefColDetail.fieldByName("field_type").setValue(
				frdoFieldType.getValue());
		dsRefColDetail.fieldByName("field_width").setValue(
				new Double(reportQuery.getColumnWidth(iCurCol)));
		//modify by qzc 2009.6.5
		dsRefColDetail.fieldByName("display_format").setValue(
				Common.nonNullStr(fcbxDisplayFormat.getValue()));
		if ("1".equals(frdoFieldFlag.getValue().toString())) {
			dsRefColDetail.fieldByName("field_code_style").setValue(
					fpnlCodeRule.getFtxtCodeRule().getValue());
		} else {
			dsRefColDetail.fieldByName("field_code_style").setValue("");
		}
		dsRefColDetail.fieldByName("show_last_level")
				.setValue(
						Boolean.getBoolean(fchkShowLastLevel.getValue()
								.toString()) ? "1" : "0");

		dsRefColDetail.fieldByName("set_year").setValue(Global.loginYear);
		dsRefColDetail.fieldByName(ISysRefCol.RG_CODE).setValue(
				Global.getCurrRegion());
	}

	private String getMaxCode() throws Exception {
		int iMax = 0;
		String sBookmark = dsRefColDetail.toogleBookmark();
		dsRefColDetail.beforeFirst();
		while (dsRefColDetail.next()) {
			if (dsRefColDetail.fieldByName("field_id").getValue() != null) {
				int iMaxTmp = Integer.parseInt(dsRefColDetail.fieldByName(
						"field_id").getString().substring(4));
				if (iMax < iMaxTmp)
					iMax = iMaxTmp;
			}
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		iMax++;
		return Common.getStrID(new BigDecimal(iMax), 3);
	}

	/**
	 * ���浱ǰѡ�е�Ԫ����Ϣ
	 * 
	 */
	private void saveCurCellInfo() {
		iCurCol = reportQuery.getSelectedCell().getColumn();
		iCurRow = reportQuery.getSelectedCell().getRow();
		sCurEname = tableColumnInfo[iCurCol].getColumnName().toString();
	}

	private class CNameDocumentListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			if (!"".equals(ftxtfFieldCName.getValue().toString().trim())) {
				reportQuery
						.setCellValue(iCurCol, 0, ftxtfFieldCName.getValue());
			} else {
				reportQuery.setCellValue(iCurCol, 0, sCurEname);
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

	public TableColumnInfo[] getTableColumnInfo() {
		return tableColumnInfo;
	}

	/**
	 * �ж��Ƿ����ô����к�������
	 * 
	 * @return
	 * @throws Exception
	 */
	private InfoPackage checkFillCodeAndName() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		String sBookmark = dsRefColDetail.toogleBookmark();
		if (!dsRefColDetail.locate("field_flag", "1")) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����ô����У�");
			return infoPackage;
		}
		if (!dsRefColDetail.locate("field_flag", "2")) {
			infoPackage.setSuccess(false);
			infoPackage.setsMessage("�����������У�");
			return infoPackage;
		}
		dsRefColDetail.gotoBookmark(sBookmark);
		infoPackage.setSuccess(true);
		return infoPackage;
	}
}
