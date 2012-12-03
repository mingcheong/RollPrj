package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.Report;

/**
 * ������֧�ܱ����ã�������
 * 
 * @author qzc XXL
 * 
 */
public class SzzbSet extends FModulePanel {
	/**
	 * 
	 */
	public JFrame frame;

	public static int lastValue_source = -1;// ��¼����ѡ��ı䶯

	public static int lastValue_Acc = -1;// ��¼����ѡ��ı䶯

	public static final int PAGE_BASE = 0;

	public static final int PAGE_COL = 1;

	public boolean maskPageChange = false;

	public boolean maskChange = false;// �༭�ؼ��ı䶯����

	public boolean isNeedFresh = false;// �ڶ���ҳ���Ƿ�Ҫˢ��

	private static final long serialVersionUID = -7862352940577468074L;

	public boolean isChanged = false;

	private boolean maskTypeChange = false;// �������ñ䶯��Ӧ�Ŀ��أ�add by xxl 20090923

	private boolean isRowOrColChanged = false;

	private XMLData xmlFormat;

	String reportID;

	String setYear = Global.getSetYear();

	CellColPanel cellPanel;

	FTabbedPane ftabPnlSet;

	DataSet dsHeader;// ��ͷDataSet

	DataSet dsRepset; // ����

	DataSet dsSzzb;// ����

	CellSetPanel pnlCellSet;

	DefineReport defineReport;

	JWorkBook jWorkBook;

	// ɨ�豨��
	SzReportPanel szReportPanel;

	// ������Ϣ
	BaseInfoTitledPanel ftitPnlBaseInfo;

	public SzzbSet() {
		super();
		init();
		// this.setReportID("5000040035");
	}

	public SzzbSet(String sReportID) {
		super();
		init();
		setReportID(sReportID);
	}

	public SzzbSet(String sReportID, String setYear) {
		super();
		this.setYear = setYear;
		init();
		setReportID(sReportID);
	}

	public SzzbSet(JFrame frame, String sReportID, String setYear,
			DefineReport defineReport) {
		super();
		this.setYear = setYear;
		this.frame = frame;
		this.defineReport = defineReport;
		init();
		setReportID(sReportID);

		// �ڴ���ӻָ���ʾ�Ĺ���
		initDispReport();
		// --------------------------

	}

	public void initize() {

	}

	public void init() {
		try {
			FSplitPane spnlBase = new FSplitPane();
			FSplitPane spnlCol = new FSplitPane();

			spnlCol.setOrientation(FSplitPane.VERTICAL_SPLIT);
			spnlBase.setOrientation(FSplitPane.VERTICAL_SPLIT);
			spnlBase.setResizeWeight(1);
			spnlCol.setResizeWeight(1);

			// ���汨����Ϣ
			szReportPanel = new SzReportPanel(this);
			this.jWorkBook = szReportPanel.getJWorkBook();
			spnlBase.addControl(szReportPanel);
			szReportPanel.setBorder(null);

			cellPanel = new CellColPanel(this);
			spnlCol.add(cellPanel, FSplitPane.TOP);

			// ����������Ϣ
			ftabPnlSet = new FTabbedPane();

			ftitPnlBaseInfo = new BaseInfoTitledPanel();
			pnlCellSet = new CellSetPanel(this);
			spnlCol.add(pnlCellSet, FSplitPane.BOTTOM);
			ftabPnlSet.addControl("������Ϣ����", spnlBase);
			ftabPnlSet.addControl("��Ԫ����������", spnlCol);
			ftabPnlSet.addChangeListener(new TabPnlSetChangeListener(this));

			spnlBase.addControl(ftitPnlBaseInfo);
			ftitPnlBaseInfo.setBorder(null);

			addSelectionListener(pnlCellSet);

			this.add(ftabPnlSet);
			getData();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��֧�ܱ���Ϣ������ʾ���淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setReportID(String reportID) {
		if (reportID != null && this.reportID != null
				&& reportID.equals(this.reportID))
			return;
		this.reportID = reportID;
		maskChange = true;
		refreshData();
		maskChange = false;
	}

	/**
	 * ������Ϣ����
	 * 
	 * @author qzc
	 * 
	 */
	public class BaseInfoTitledPanel extends FTitledPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1496501128963959223L;

		FTextField ftxtEditRepName;// ��������

		FTextField ftxtEditReportTitle;// ��������

		ReportInfoPanel reportTitlePanel; // �����

		CustomTree ftreeColSelect;// ����Ϣ

		FRadioGroup frdoFtype;// ��������

		FCheckBox fchkHideFlag;// ��������

		FComboBox fcbxSFormate;// ��ʾ���

		ReportTypeList reportTypeList;// �������

		JComboBox cbxCurrencyUnit;// ��������
		FCheckBox cbxConvert;//Ԫ����Ԫ��ת��

		// �û�����
		FRadioGroup reportUserTypeGrp;

		ReportInfoPanel reportColumnsPanel;// ��ͷ

		public BaseInfoTitledPanel() throws Exception {
			this.setLeftInset(5);
			this.setRightInset(5);
			RowPreferedLayout rLay = new RowPreferedLayout(7);
			// rLay.setColumnWidth(430);
			rLay.setRowHeight(24);
			rLay.setColumnGap(1);
			rLay.setRowGap(1);
			this.setLayout(rLay);
			// �����
			FPanel fpnlTitle = new FPanel();
			fpnlTitle.setTitle("�����");
			fpnlTitle.setLayout(new RowPreferedLayout(1));

			ftxtEditRepName = new FTextField("�������ƣ�");
			ftxtEditRepName.addValueChangeListener(new valueChange());
			ftxtEditRepName.setProportion(0.16f);
			ftxtEditReportTitle = new FTextField("�������ݣ�");
			ftxtEditReportTitle.setEditable(false);
			ftxtEditReportTitle.setProportion(0.16f);
			reportTitlePanel = new ReportInfoPanel("��ȡ�����");
			reportTitlePanel.fbtnGetInfo
					.addActionListener(new GetTitleActionListener(SzzbSet.this,
							reportTitlePanel));
			fpnlTitle.addControl(ftxtEditRepName, new TableConstraints(1, 1,
					false, true));
			fpnlTitle.addControl(ftxtEditReportTitle, new TableConstraints(1,
					1, false, true));
			fpnlTitle.addControl(reportTitlePanel, new TableConstraints(1, 1,
					true, true));
			// ��ͷ
			FPanel fpnlReportColumns = new FPanel();
			fpnlReportColumns.setTitle("��ͷ");
			fpnlReportColumns.setLayout(new RowPreferedLayout(1));
			reportColumnsPanel = new ReportInfoPanel("��ȡ��ͷ��");
			reportColumnsPanel.fbtnGetInfo
					.addActionListener(new GetHeaderActionListener(
							SzzbSet.this, reportColumnsPanel));
			fpnlReportColumns.addControl(reportColumnsPanel,
					new TableConstraints(1, 1, true, true));

			// ��������Ϣ
			FPanel fpnlSetColInfo = new FPanel();
			fpnlSetColInfo.setTitle("��������Ϣ");
			RowPreferedLayout rLaySetColInfo = new RowPreferedLayout(2);
			rLaySetColInfo.setRowGap(5);
			rLaySetColInfo.setColumnGap(5);
			fpnlSetColInfo.setLayout(rLaySetColInfo);
			ftreeColSelect = new CustomTree("����Ϣ", null, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_FNAME, "", null);
			ftreeColSelect
					.addTreeSelectionListener(new ColSelectTreeSelectionListener(
							SzzbSet.this));
			FScrollPane fspnlColSelect = new FScrollPane(ftreeColSelect);

			FPanel fpnlFtype = new FPanel(); // ��������
			fpnlFtype.setLayout(new RowPreferedLayout(1));
			fpnlFtype.setTitle("��������");
			frdoFtype = new FRadioGroup("", FRadioGroup.HORIZON);
			frdoFtype.setRefModel("����#" + ISzzbSet.FORMAT_INT + "+������#"
					+ ISzzbSet.FORMAT_FLOAT + "+�޸�ʽ#" + ISzzbSet.FORMAT_STRING
					+ "");
			frdoFtype.setTitleVisible(false);
			frdoFtype.addValueChangeListener(new FtypeValueChangeListener());

			fpnlFtype.addControl(frdoFtype, new TableConstraints(1, 1, true,
					true));

			fchkHideFlag = new FCheckBox("��������");
			fchkHideFlag.setTitlePosition("RIGHT");
			fchkHideFlag.setVisible(false);
			fchkHideFlag
					.addValueChangeListener(new HideFlagValueChangeListener());

			fcbxSFormate = new FComboBox();
			fcbxSFormate.setTitle("��ʾ��ʽ��");
			fcbxSFormate.setProportion(0.35f);
			fcbxSFormate.setValue("");
			fcbxSFormate
					.addValueChangeListener(new FormateValueChangeListener());

			fpnlSetColInfo.addControl(fspnlColSelect, new TableConstraints(5,
					1, false, true));
			fpnlSetColInfo.addControl(fpnlFtype, new TableConstraints(2, 1,
					false, true));
			fpnlSetColInfo.addControl(fchkHideFlag, new TableConstraints(1, 1,
					false, true));
			fpnlSetColInfo.addControl(fcbxSFormate, new TableConstraints(1, 1,
					false, true));

			// ������
			FPanel fpnlreportContent = new FPanel();
			fpnlreportContent.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

			// ���������Ϣ
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("�������ͣ�");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			FLabel flblCurrencyUnit = new FLabel();
			flblCurrencyUnit.setTitle("�������ͣ�");
			cbxCurrencyUnit = new JComboBox(new String[] { "", "Ԫ", "��Ԫ" });
			cbxCurrencyUnit.addActionListener(new valueChange());

			cbxConvert = new FCheckBox();
			cbxConvert.setTitle("Ԫת������Ԫ");
			cbxConvert.addValueChangeListener(new valueChange());
			cbxConvert.setPreferredSize(new Dimension(240, 20));

			FLabel flblEmpty = new FLabel();
			flblEmpty.setText("                      ");
			// ��ʱ����
			// FButton fbtnSaveDetail = new FButton("fbtnSaveDetail", "��������");

			// fbtnSaveDetail.addActionListener(new SaveReportActionListener(
			// SzzbSet.this));

			// ���屨���û�����
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#����λʹ�� +1#������ʹ��+2#������λ��ͬʹ��");
			reportUserTypeGrp.setTitleVisible(false);
			reportUserTypeGrp.setValue("2");
			// ���屨���û��������
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("�û�����");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// �����û����͵�ѡ����뱨���û��������
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			fpnlreportContent.addControl(flblCurrencyUnit);
			fpnlreportContent.add(cbxCurrencyUnit);

			fpnlreportContent.addControl(cbxConvert);

			// fpnlreportContent.addControl(fbtnSaveDetail);

			this.addControl(fpnlTitle, new TableConstraints(4, 3, false, true));
			this.addControl(fpnlReportType, new TableConstraints(6, 1, false,
					true));
			this.addControl(fpnlSetColInfo, new TableConstraints(6, 3, false,
					true));
			this.addControl(fpnlReportColumns, new TableConstraints(2, 3,
					false, true));
			this.addControl(reportUserTypePnl, new TableConstraints(2, 3,
					false, true));
			this.addControl(fpnlreportContent, new TableConstraints(2, 4,
					false, true));
		}

		/**
		 * ������������
		 * 
		 * @author qzc
		 * 
		 */
		private class FtypeValueChangeListener implements ValueChangeListener {

			public void valueChanged(ValueChangeEvent arg0) {

				if (maskTypeChange)// ���������������
					return;
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				String sValue = (String) arg0.getNewValue();
				// add by XXL TODO
				// ����Ҫ�����ʽ ������ѡ����
				Vector sFormat = (Vector) xmlFormat.get(frdoFtype.getRefModel()
						.getNameByValue(arg0.getNewValue()));

				maskTypeChange = true;
				fcbxSFormate.setRefModel(sFormat);
				fcbxSFormate.setValue("");
				maskTypeChange = false;

				try {
					dsHeader.edit();
					dsHeader.fieldByName(IQrBudget.FIELD_TYPE).setValue(sValue);
					dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT)
							.setValue("");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"��֧�ܱ���Ϣ�����������ͷ������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		/**
		 * ��������Ϣ�Ƿ�����
		 * 
		
		 * 
		 */
		private class HideFlagValueChangeListener implements
				ValueChangeListener {
			public void valueChanged(ValueChangeEvent arg0) {
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				try {
					dsHeader.edit();
					if ("true".equals(fchkHideFlag.getValue().toString())) {
						dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
								.setValue("��");
					} else {
						dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
								.setValue("��");
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"��֧�ܱ���Ϣ��������Ϣ���ط������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		private class FormateValueChangeListener implements ValueChangeListener {

			public void valueChanged(ValueChangeEvent arg0) {
				if (maskTypeChange)
					return;
				if (dsHeader == null)
					return;
				if (dsHeader.bof() || dsHeader.eof())
					return;
				try {
					dsHeader.edit();
					dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT).setValue(
							fcbxSFormate.getValue().toString());
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(SzzbSet.this,
							"��֧�ܱ���Ϣ��������Ϣ��ʾ��ʽ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void showRepsetInfo() throws Exception {

		if (!dsRepset.isEmpty()) {
			// ��ʾ��ͷ��Ϣ
			ftitPnlBaseInfo.ftxtEditRepName.setValue(dsRepset.fieldByName(
					IQrBudget.REPORT_CNAME).getValue());
			ftitPnlBaseInfo.ftxtEditReportTitle.setValue(dsRepset.fieldByName(
					IQrBudget.TITLE).getValue());

			ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleArea
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_AREA)
							.getValue());
			ftitPnlBaseInfo.reportTitlePanel.ftxtEditTitleFont
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_FONT)
							.getValue());
			ftitPnlBaseInfo.reportTitlePanel.ftxtEditRepTitleFontSize
					.setValue(dsRepset.fieldByName(IQrBudget.TITLE_FONTSIZE)
							.getValue());

			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_AREA)
							.getValue());
			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditTitleFont
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_FONT)
							.getValue());
			ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleFontSize
					.setValue(dsRepset.fieldByName(IQrBudget.COLUMN_FONTSIZE)
							.getValue());
			ftitPnlBaseInfo.reportTypeList.setSelected(dsRepset.fieldByName(
					IQrBudget.REPORT_ID).getString());

//			 if (!Common.isNullStr(dsRepset.fieldByName(IQrBudget.LVL_ID)
//			 .getString()))// �������ʱ��û�б�����
//			 cbxReportType.setCode(dsRepset.fieldByName(IQrBudget.LVL_ID)
//			 .getString().substring(0, 6));

			ftitPnlBaseInfo.cbxCurrencyUnit.setSelectedItem(dsRepset
					.fieldByName(IQrBudget.CURRENCYUNIT).getValue());
			String isConvert = dsRepset.fieldByName(ISzzbSet.CONVERT_FIELD)
					.getString();
			if ("1".equals(isConvert)) {
				ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(true));
			} else
				ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(false));
		}

	}

	/**
	 * ��ʾ����Ϣ
	 * 
	 * @param szzbSet
	 * @throws Exception
	 */
	public void showColInfo() throws Exception {
		// ��ʾ����Ϣ
		// dsHeader.cancel();
		ftitPnlBaseInfo.ftreeColSelect.setDataSet(dsHeader);
		ftitPnlBaseInfo.ftreeColSelect.reset();
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void refreshData() {
		try {
			getData();
			showRepsetInfo();
			showColInfo();
			cellPanel.dispData(this.reportID);
			isChanged = false;
			isNeedFresh = false;
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}

	private void getData() throws Exception {

		dsRepset = SzzbSetI.getMethod().getRepset(setYear, 1, reportID);

		// ��ʾ��ͷ
		dsHeader = SzzbSetI.getMethod().getReportHeader(reportID, setYear);
		// showColInfo();
		// ��ʾ������Ϣ
		// setReportBodyInfo(sReportId, jWorkBook);
		// add by xxl ���ڶ�̬��ʾ��ʽ����Ϣ 20090922
		xmlFormat = SzzbSetI.getMethod().getFormatList();
	}

	public void refreshColInfo() {

	}

	public void saveBaseInfo() {
		new SaveReportActionListener(this).actionPerformed(null);

	}

	public void saveColInfo() {
		String sErr = cellPanel.saveCols();
		if (!Common.isNullStr(sErr)) {
			// new MessageBox(sErr, MessageBox.MESSAGE, MessageBox.BUTTON_OK)
			// .show();
			JOptionPane.showMessageDialog(SzzbSet.this, sErr, "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// new MessageBox("����ɹ�!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
		// .show();
		JOptionPane.showMessageDialog(SzzbSet.this, "����ɹ�!", "��ʾ",
				JOptionPane.INFORMATION_MESSAGE);

		isChanged = false;

	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public void setActivePage(int iPage) {
		this.ftabPnlSet.setSelectedIndex(iPage);
	}

	public int getActivePage() {
		return this.ftabPnlSet.getSelectedIndex();
	}

	public String getReportID() {
		return reportID;
	}

	public void addSelectionListener(ISelectionListener l) {
		cellPanel.addSelectLisener(l);
	}

	class valueChange implements ValueChangeListener, ActionListener {

		public void valueChanged(ValueChangeEvent arg0) {
			if (maskChange)
				return;
			isChanged = true;

		}

		public void actionPerformed(ActionEvent e) {
			if (ftitPnlBaseInfo.cbxCurrencyUnit == e.getSource()) {//����ǵ�λѡ��
				if (ftitPnlBaseInfo.cbxCurrencyUnit.isEnabled()) {//ֻ���ڿɱ༭��״̬�²���Ч
					if ("��Ԫ".equals(ftitPnlBaseInfo.cbxCurrencyUnit
							.getSelectedItem()))
						ftitPnlBaseInfo.cbxConvert.setEnabled(true);
					else {
						ftitPnlBaseInfo.cbxConvert.setEnabled(false);
						ftitPnlBaseInfo.cbxConvert.setValue(new Boolean(false));
					}
				}

			}
			if (maskChange)
				return;
			isChanged = true;

		}
	}

	public void setReportIDForAdd(String sReportID) {
		try {

			this.reportID = sReportID;
			// ���±���
			if (dsRepset != null && !dsRepset.isEmpty()) {
				dsRepset.beforeFirst();
				dsRepset.next();
				dsRepset.edit();
				dsRepset.fieldByName(IQrBudget.REPORT_ID).setValue(sReportID);
			}

			// ���±�ͷ
			if (dsHeader != null) {
				String sBook = dsHeader.toogleBookmark();
				dsHeader.beforeFirst();
				dsHeader.next();
				dsHeader.edit();
				while (!dsHeader.eof()) {
					dsHeader.fieldByName(IQrBudget.REPORT_ID).setValue(
							sReportID);
					dsHeader.next();
				}
				dsHeader.gotoBookmark(sBook);
				// ����CELL
				cellPanel.setNewReportID(sReportID);
			}
		} catch (Exception e) {
			// if (this.frame != null)
			// new MessageBox(this.frame, "�������ݳ���!", e.getMessage(),
			// MessageBox.ERROR, MessageBox.BUTTON_OK).show();
			JOptionPane.showMessageDialog(SzzbSet.this, "�������ݳ���!"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			//			
			// else
			// new MessageBox("�������ݳ���!", e.getMessage(), MessageBox.ERROR,
			// MessageBox.BUTTON_OK).show();
			// JOptionPane.showMessageDialog(SzzbSet.this,
			// "�������ݳ���!"+e.getMessage(), "��ʾ",
			// JOptionPane.ERROR_MESSAGE);
		}

	}

	// ��ʼ����ʾ����
	private void initDispReport() {
		if (Common.isNullStr(reportID)) {
			szReportPanel.getFlblOpenExcel().setTitle("�ޱ�����Ϣ");
		} else {
			try {
				List lstConsCell = SzzbSetI.getMethod().getSzzbCons(
						this.reportID, Global.getSetYear());
				dispConsCells(lstConsCell, szReportPanel.getWorkSheet());
				szReportPanel.getFlblOpenExcel().setTitle("�ָ��ı���");
			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame, "��ʼ������ʧ��!"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void dispConsCells(List lstConsCell, Report report) {

		if (lstConsCell == null || lstConsCell.size() == 0)
			return;
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;

		// int colStart = thDetail.getColumns();
		Object oValue;
		Cell cellElement;
		XMLData aData;
		int iCount = lstConsCell.size();
		CellSelection cells = ReportUtil
				.translateToNumber(this.ftitPnlBaseInfo.reportColumnsPanel.ftxtEditRepTitleArea
						.getValue().toString());
		int titleStart = cells.getRow();
		for (int i = 0; i < iCount; i++) {
			aData = (XMLData) lstConsCell.get(i);
			iColumn = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMN));
			iColumnSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_COLUMNSPAN));
			iRow = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROW));
			iRowSpan = Integer.parseInt(Common.getAStringField(aData,
					ISzzbSet.FIELD_ROWSPAN));
			oValue = Common.getAStringField(aData, ISzzbSet.FIELD_VALUE);

			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// ���ʽ
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.HOR_ALIGNMENT))).deriveVerticalAlignment(
					Integer.parseInt(Common.getAStringField(aData,
							ISzzbSet.VER_ALIGNMENT)));
			FRFont aFont = FRFont.getInstance(Common.getAStringField(aData,
					ISzzbSet.FIELD_FONT), Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_FONTSTYPE)), Integer
					.parseInt(Common.getAStringField(aData,
							ISzzbSet.FIELD_FONTSIZE)));
			style = style.deriveFRFont(aFont);

			if (iRow >= titleStart) {
				style = style.deriveBorder(1, Color.BLACK, 1, Color.BLACK, 1,
						Color.BLACK, 1, Color.BLACK);
			}

			cellElement.setStyle(style);
			report.setColumnWidth(iColumn, Integer.parseInt(Common
					.getAStringField(aData, ISzzbSet.FIELD_COLWIDTH)));
			report.setRowHeight(iRow, Integer.parseInt(Common.getAStringField(
					aData, ISzzbSet.FIELD_ROWHEIGHT)));
			report.addCellElement(cellElement, false);
		}
	}

	// ��ӱ������������,�������԰Ѱ�ť�����ˣ��ò���һЩ
	public void saveInfos() {
		if (ftabPnlSet.getSelectedIndex() == 0) {// ���ѡ���ǵĵ�һ����ҳ
			new SaveReportActionListener(this).actionPerformed(null);
		} else {
			saveColInfo();
		}

	}

	public boolean isMaskTypeChange() {
		return maskTypeChange;
	}

	public void setMaskTypeChange(boolean maskTypeChange) {
		this.maskTypeChange = maskTypeChange;
	}

	public XMLData getXmlFormat() {
		return xmlFormat;
	}

	public boolean isRowOrColChanged() {
		return isRowOrColChanged;
	}

	public void setRowOrColChanged(boolean isRowOrColChanged) {
		this.isRowOrColChanged = isRowOrColChanged;
	}

}
