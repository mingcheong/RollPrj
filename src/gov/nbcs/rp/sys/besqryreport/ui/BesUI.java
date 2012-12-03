/**
 * Copyright�㽭����
 * 

 * 
 * @title ���Ʋ�ѯ�������
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.sys.besqryreport.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.report.HeaderUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.WorkSheet;
import com.fr.report.io.ExcelImporter;

public class BesUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private FTabbedPane tabPane;

	// ���������б��Ԫ�ض���
	private ReportUI reportUIB;

	private Report reportB;

	private FTextField tfTitle;

	private JFileChooser fileChooser;

	private FTextField tfFilePath;

	private FTextField tfFileName;

	private FTextField tfSource;

	private FTextField tfMid1;

	private FTextField tfMid2;

	// �������
	private ReportTypeList reportTypeList;

	private FComboBox cbMoneyType;

	private FRadioGroup rgUserType;

	private BesAct BAct;

	// private IBesQryReport itserv;

	private DataSet dsSource; // ����� dataset

	private DataSet dsSql; // sqllines��dataset

	private String sReportID;

	private String sTypeNO;

	private DataSet dsReportType; // �������

	private String[] bmk;

	// ������Ԫ�ض���
	private ReportUI reportUIC;

	private Report reportC;

	private TableHeader TTableHeaderC; // �ڶ��������reportUI�ı�ͷ

	private Node nodeC; // �ڶ�������reportUI��node

	private DataSet dsTitle; // �ڶ�������ı�ͷ�� dataset

	private CustomTree table; // ������Ϣ��tree

	private DataSet dsInfo; // col��tree��dataset

	private FTextField tfColName; // �е��������༭��

	private CustomComboBox tfColFormat; // ��ʽ��

	private FCheckBox ckHide; // �Ƿ� ����

	private CustomComboBox cbDataType; // ��������

	private DataSet dsFormat; // ��ʾ��ʽ��dataset

	// ����״̬����
	private boolean isChange = true;

	private int iState = 0;

	private final static int state_Browse = 0;

	private final static int state_Modify = 1;

	// ����Ԫ��
	private FPanel pnlBAttr;

	private FPanel pnlCAttr;

	// ���ݶ���
	private IBesQryReport itserv; // �ӿ�

	private DefineReport defineReport;

	public BesUI(String sReportID, DefineReport defineReport) {
		this.sReportID = sReportID;
		this.defineReport = defineReport;
		// ��ʼ������
		initize();
	}

	/**
	 * ��ʼ������
	 */
	private void initize() {
		try {
			this.setSize(900, 650);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setTitle("���ⱨ�����");

			BAct = new BesAct();
			itserv = BesStub.getMethod();
			tabPane = new FTabbedPane();
			tabPane.add(getBPanel(), "���������������");
			tabPane.add(getCPanel(), "��������������");

			// this.createToolBar();
			tabPane.addChangeListener(new SelNextPaneListener());
			// getReportID();
			FPanelBtnButtom fpnlButton = new FPanelBtnButtom();

			FPanel fpnlMain = new FPanel();
			fpnlMain.setLayout(new RowPreferedLayout(1));
			fpnlMain
					.addControl(tabPane, new TableConstraints(1, 1, true, true));
			fpnlMain.addControl(fpnlButton, new TableConstraints(2, 1, false,
					true));
			this.getContentPane().add(fpnlMain);
			addUserTypeSelListener();
			iState = state_Browse;
			setButtonState(state_Browse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ȡ�����������
	private FPanel getBPanel() throws Exception {
		if (pnlBAttr == null) {
			FPanel pnlBlank = new FPanel();
			pnlBAttr = new FPanel();
			dsSource = DataSet.create();
			dsSql = DataSet.create();
			dsReportType = BAct.getReportTypeData();
			dsReportType.beforeFirst();
			dsReportType.next();
			FPanel pnlTop = new FPanel();
			// pnlTop.setTitle( "top" );
			RowPreferedLayout layTop = new RowPreferedLayout(1);
			pnlTop.setLayout(layTop);
			reportB = new Report();
			reportUIB = new ReportUI(reportB);
			pnlTop.add(reportUIB, new TableConstraints(13, 1, true, true));
			reportUIB.setBorder(new LineBorder(Color.BLACK));
			/*
			 * �°벿�ֲ���
			 */
			FPanel pnlUnder = new FPanel();
			// pnlUnder.setTitle( "under" );
			RowPreferedLayout layUnder = new RowPreferedLayout(10);
			layUnder.setColumnGap(1);
			layUnder.setColumnWidth(100);
			pnlUnder.setLayout(layUnder);
			FLabel lbFilePath = new FLabel();
			lbFilePath.setText("   ��ǰ�ļ�:");
			FButton btnOpen = new FButton("btn01", "��");
			tfFilePath = new FTextField("");
			tfFilePath.setProportion(0.01f);
			tfFilePath.setEditable(false);

			// ����ϸ��Ϣ
			FPanel pnlContent = new FPanel();
			pnlContent.setTitle("�������Ϣ");
			RowPreferedLayout layContent = new RowPreferedLayout(10);
			layContent.setColumnGap(1);
			layContent.setColumnWidth(90);
			layContent.setRowGap(15);
			pnlContent.setLayout(layContent);
			tfFileName = new FTextField("  ��������:");
			tfFileName.setProportion(0.1f);

			// ���������Ϣ
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("�������ͣ�");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			cbMoneyType = new FComboBox("  ��������:");
			cbMoneyType.setRefModel("1#Ԫ+2#��Ԫ");
			cbMoneyType.setProportion(0.33f);
			rgUserType = new FRadioGroup("   �û�����:", 0);
			rgUserType.setRefModel("0#��λʹ��+1#����ʹ��+2#��λ������ͬʹ��");
			rgUserType.setProportion(0.2f);
			rgUserType.setValue("2");

			FLabel lbDataSource = new FLabel();
			lbDataSource.setText("  ������Դ");
			FButton btnSource = new FButton("btn02", "ѡ��");
			tfSource = new FTextField();
			tfSource.setProportion(0.001f);
			tfSource.setEditable(false);

			FLabel lbDataMid1 = new FLabel();
			lbDataMid1.setText("  �м���ͼ");
			FButton btnMid1 = new FButton("btn03", "ѡ��");
			tfMid1 = new FTextField();
			tfMid1.setProportion(0.001f);
			tfMid1.setEditable(false);

			FLabel lbDataMid2 = new FLabel();
			lbDataMid2.setText("  �м���ͼ");
			FButton btnMid2 = new FButton("btn04", "ѡ��");
			tfMid2 = new FTextField();
			tfMid2.setProportion(0.001f);
			tfMid2.setEditable(false);

			pnlContent.addControl(tfFileName, new TableConstraints(1, 10,
					false, false));
			pnlContent.addControl(cbMoneyType, new TableConstraints(1, 3,
					false, false));
			pnlContent.addControl(rgUserType, new TableConstraints(1, 7, false,
					false));

			pnlContent.addControl(lbDataSource, new TableConstraints(1, 1,
					false, false));
			pnlContent.addControl(btnSource, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfSource, new TableConstraints(1, 4, false,
					false));
			// �������
			pnlContent.addControl(fpnlReportType, new TableConstraints(4, 4,
					false, false));

			pnlContent.addControl(lbDataMid1, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(btnMid1, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfMid1, new TableConstraints(1, 4, false,
					false));

			pnlContent.addControl(lbDataMid2, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(btnMid2, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfMid2, new TableConstraints(1, 4, false,
					false));

			pnlUnder.addControl(lbFilePath, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(btnOpen, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(tfFilePath, new TableConstraints(1, 7, false,
					false));

			FButton btnGetHeader = new FButton("btnGet", "��ȡ��ͷ");
			FButton btnGetTitle = new FButton("btnGetTitle", "��ȡ����");
			tfTitle = new FTextField("   ������Ϣ");
			tfTitle.setProportion(0.11f);
			tfTitle.setEditable(false);

			pnlUnder.addControl(btnGetTitle, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(tfTitle, new TableConstraints(1, 9, false,
					false));
			pnlUnder.addControl(btnGetHeader, new TableConstraints(1, 1, false,
					false));
			pnlUnder.addControl(pnlContent, new TableConstraints(12, 10, true,
					true));
			/*
			 * ���岼��
			 */
			RowPreferedLayout layBase = new RowPreferedLayout(3);
			pnlBAttr.setLayout(layBase);
			layBase.setRowGap(5);
			FLabel lbTitle = new FLabel();
			lbTitle.setText("          ԭ   ʼ   ��   ��");
			lbTitle.setFontSize(20);
			lbTitle.setFont(new Font("����", 15, 15));
			pnlBAttr.addControl(pnlBlank, new TableConstraints(1, 1, false,
					true));
			pnlBAttr.addControl(lbTitle,
					new TableConstraints(1, 1, false, true));
			pnlBAttr.addControl(pnlBlank, new TableConstraints(1, 1, false,
					true));
			pnlBAttr.addControl(pnlTop,
					new TableConstraints(13, 3, false, true));
			pnlBAttr.addControl(pnlUnder, new TableConstraints(13, 3, true,
					true));
			this.getContentPane().add(pnlBAttr);
			// this.createToolBar();
			btnOpen.addMouseListener(new ImportRep());

			// /////////////////////////////////////////////////////////��ť�����¼�/////////////////////////////////////////////////////////////////////////////

			// Դ����Ϣ����ļ����¼�
			btnSource.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String sTableName = (tfSource.getValue() == null) ? ""
							: tfSource.getValue().toString();
					SelectSource ss = new SelectSource(BesUI.this, sTableName);
					Tools.centerWindow(ss);
					ss.setVisible(true);
					String sTName = (ss.getValue() == null) ? "" : ss
							.getValue().toString();
					tfSource.setValue(sTName);
					isChange = true;
					try {
						setSourceTitle(tfTitle.getValue());
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			});
			// �м��1����ļ����¼�
			btnMid1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SelectSource ss = new SelectSource(BesUI.this, (tfMid1
							.getValue() == null) ? "" : tfMid1.getValue()
							.toString());
					Tools.centerWindow(ss);
					ss.setVisible(true);
					tfMid1.setValue(ss.getValue());
				}
			});
			// �м�������ļ����¼�
			btnMid2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SelectSource ss = new SelectSource(BesUI.this, (tfMid2
							.getValue() == null) ? "" : tfMid2.getValue()
							.toString());
					Tools.centerWindow(ss);
					ss.setVisible(true);
					tfMid2.setValue(ss.getValue());
				}
			});
			btnGetHeader.addMouseListener(new SetTableHeader());
			btnGetTitle.addMouseListener(new SetTitleListener());

		}
		return pnlBAttr;
	}

	// ��ȡ���������
	private FPanel getCPanel() throws Exception {
		if (pnlCAttr == null) {
			pnlCAttr = new FPanel();
			BAct = new BesAct();
			reportC = new Report();
			reportUIC = new ReportUI(reportC);
			reportUIC.setEditable(false);

			// ���岼��
			FPanel pnlBlank = new FPanel();
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlCAttr.setLayout(layBase);
			reportUIC.setBorder(new LineBorder(Color.BLACK));

			// ������沼��
			FPanel pnlTop = new FPanel();
			RowPreferedLayout layTop = new RowPreferedLayout(8);
			pnlTop.setLayout(layTop);
			pnlTop.add(reportUIC, new TableConstraints(10, 8, false, false));

			// ������沼��
			FPanel pnlUnder = new FPanel();
			RowPreferedLayout layUnder = new RowPreferedLayout(8);
			pnlUnder.setLayout(layUnder);

			FPanel pnlContent = new FPanel();
			pnlContent.setTitle("��������");
			RowPreferedLayout layContent = new RowPreferedLayout(4);
			layContent.setRowGap(20);
			pnlContent.setLayout(layContent);
			FPanel pnlInfo = new FPanel();
			RowPreferedLayout layInfo = new RowPreferedLayout(4);
			pnlInfo.setLayout(layInfo);
			layInfo.setColumnWidth(155);
			pnlInfo.setTitle("ѡ���Ӧ����");

			// �������Ե����
			cbDataType = new CustomComboBox(BAct.getDataTypeData(),
					IBesQryReport.DataType_ID, IBesQryReport.DataType_Name);
			cbDataType.setTitle("  ������");
			cbDataType.setProportion(0.2f);
			ckHide = new FCheckBox("  �����Ƿ�����");
			ckHide.setProportion(0.7f);
			tfColName = new FTextField("  ������: ");
			tfColName.setProportion(0.13f);
			tfColName.setEditable(false);
			String sFilter = "typeid = 'DQRDISPLAY' and "
					+ IBesQryReport.SET_YEAR + "=" + Global.loginYear;
			dsFormat = itserv.getDataSet(IBesQryReport.PUBCODETABLE, sFilter);
			tfColFormat = new CustomComboBox(dsFormat, "NAME", "NAME");
			tfColFormat.setTitle("��ʾ��ʽ");
			tfColFormat.setProportion(0.2f);

			// ��ť���
			FPanel pnlButton = new FPanel();
			RowPreferedLayout layButton = new RowPreferedLayout(4);
			layButton.setColumnWidth(100);
			pnlButton.setLayout(layButton);
			FButton btnSet = new FButton("", "����");
			FButton btnCancelSet = new FButton("", "ȡ������");
			pnlButton.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));
			pnlButton.addControl(btnSet, new TableConstraints(1, 1, false,
					false));
			pnlButton.addControl(btnCancelSet, new TableConstraints(1, 1,
					false, false));
			pnlButton.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));

			// ����Ϣ
			pnlContent.addControl(cbDataType, new TableConstraints(1, 2, false,
					false));
			pnlContent.addControl(ckHide, new TableConstraints(1, 1, false,
					false));
			pnlContent.addControl(tfColName, new TableConstraints(1, 3, false,
					false));
			pnlContent.addControl(tfColFormat, new TableConstraints(1, 2,
					false, false));
			pnlContent.addControl(pnlButton, new TableConstraints(1, 4, false,
					false));

			// ѡ���Ӧ���е����
			// һ��customtable,��һ��Դ��
			dsInfo = DataSet.create();
			table = new CustomTree("����Ϣ", dsInfo, "COLUMN_ID", "COLUMN_NAME",
					null, (SysCodeRule)null);
			FScrollPane spTable = new FScrollPane();
			spTable.addControl(table);// , new
			// TableConstraints(4,4,true,true));
			pnlInfo.addControl(spTable, new TableConstraints(13, 3, false,
					false));
			pnlUnder.addControl(pnlContent, new TableConstraints(14, 4, false,
					false));
			pnlUnder.addControl(pnlInfo, new TableConstraints(14, 3, false,
					false));

			// �������
			FPanel pnlTitle = new FPanel();
			RowPreferedLayout layTitle = new RowPreferedLayout(9);
			pnlTitle.setLayout(layTitle);
			FLabel lbTitle = new FLabel();
			lbTitle.setText("  ��  ��  ��  ��  ��  ��");
			lbTitle.setFontSize(20);
			lbTitle.setFont(new Font("����", 15, 15));
			pnlTitle.addControl(pnlBlank, new TableConstraints(1, 3, false,
					false));
			pnlTitle.addControl(lbTitle, new TableConstraints(1, 3, false,
					false));
			pnlTitle.addControl(pnlBlank, new TableConstraints(1, 3, false,
					false));

			pnlCAttr.addControl(pnlTitle, new TableConstraints(1, 1, false,
					true));
			pnlCAttr.addControl(pnlTop,
					new TableConstraints(11, 1, false, true));
			// pnlCAttr.addControl( pnlButton, new TableConstraints(
			// 11,1,false,true));
			pnlCAttr.addControl(pnlUnder, new TableConstraints(15, 1, false,
					true));
			pnlCAttr.setTopInset(10);
			reportUIC.getGrid().addMouseListener(new ReportClickListener());
			ckHide.addValueChangeListener(new HideChangeListener());
			cbDataType.addValueChangeListener(new ColTypeChangeLisentener());
			tfColFormat.addValueChangeListener(new FormatChangeLisentener());
			btnSet.addActionListener(new SetListener());
		}
		return pnlCAttr;
	}

	// /////////////////////////////////////////////////////////�������/////////////////////////////////////////////////////////////////////////////////

	/**
	 * �����ļ�ѡ����
	 * 
	 */
	private void CreateChooser() {
		// �����ļ�ѡ����
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		// �趨���õ��ļ��ĺ�׺��
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls".toLowerCase())
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "�����ļ�(*.xls)";
			}
		});
	}

	/**
	 * ���뱨��ť�Ĳ���
	 */
	public class ImportRep extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				CreateChooser();
				int returnval = fileChooser.showOpenDialog(BesUI.this);
				File file = fileChooser.getSelectedFile();
				if (returnval == JFileChooser.APPROVE_OPTION) {
					ExcelImporter ei = new ExcelImporter(file);
					reportUIB = BAct.initReportUI(ei, reportUIB);
					reportUIB.repaint();
					tfFilePath.setValue(file.getPath());
					iState = state_Modify;
					sReportID = getReportID();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * ѡȡ��ͷ
	 */
	private class SetTableHeader extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				if (reportUIB == null)
					return;
				CellSelection cs = reportUIB.getCellSelection();
				if (cs == null)
					return;
				nodeC = ReportUtil.parseDocument(reportUIB.getWorkSheet(), cs);
				TTableHeaderC = new TableHeader(nodeC);
				TTableHeaderC.setFont(new Font("����", Font.PLAIN, 12));
				TTableHeaderC.setColor(new Color(250, 228, 184));
				// ��ʼ���ڶ�������reportUI
				BAct.createReport(TTableHeaderC, reportUIC, reportC);
				dsTitle = DataSet.create();
				dsSource.beforeFirst();
				dsSource.next();
				BAct.FillIntoDataSet(dsTitle, nodeC, reportUIC,
						BesAct.codeRule_Col, sReportID);
				TableHeader tableHeader = HeaderUtility.createHeader(dsTitle,
						IBesQryReport.FIELD_CODE, IBesQryReport.FIELD_CNAME,
						BesAct.codeRule_Col, null);
				BAct.createReport(tableHeader, reportUIC, reportC);
				List listFields = TTableHeaderC.getFields();
				DataSet dsBody = DataSet.create();
				initBodyData(dsBody, listFields, "");
				initBodyData(dsBody, listFields, "������");
				reportC.setBodyData(dsBody);
				reportC.refreshBody();
				reportUIC.repaint();
				table.addMouseListener(new DataSoureChangeListener(dsInfo,
						IBesQryReport.FIELD_ENAME, table, tfColName));
				for (int i = 0; i < TTableHeaderC.getColumns(); i++) {
					reportC.setColumnWidth(i, 100);
				}
				isChange = true;
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * ѡȡ����
	 */
	private class SetTitleListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				if (reportUIB == null)
					return;
				CellSelection cs = reportUIB.getCellSelection();
				int iRow = cs.getRow();
				int iCol = cs.getColumn();
				WorkSheet css = reportUIB.getWorkSheet();
				CellElement cell = css.getCellElement(iCol, iRow);
				if (cell == null)
					return;
				Object oValue = cell.getValue();
				tfTitle.setValue(oValue);
				setSourceTitle(oValue);
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * ����������Ϣ���ͷ�ñ���
	 * 
	 * @param aTitle
	 * @throws Exception
	 */
	private void setSourceTitle(Object aTitle) throws Exception {
		if (dsSource != null && !dsSource.isEmpty()) {
			dsSource.edit();
			dsSource.beforeFirst();
			dsSource.next();
			dsSource.fieldByName(IBesQryReport.TITLE).setValue(aTitle);
		}
	}

	// ///////////////////////////////////////////////////////////����///////////////////////////////////////////////////////////////////////////

	/**
	 * ���������һ��
	 */
	private class AddActionListenernext implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			try {
				int idex = tabPane.getSelectedIndex();
				if (idex != 0)
					return;
				InfoPackage info = gotoNext();
				if (info.getSuccess() == true) {
					iState = state_Modify;
					setButtonState(iState);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private InfoPackage checkIsNext() throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(String.valueOf(tfFileName.getValue()))) {
			info.setSuccess(false);
			info.setsMessage("���������");
			return info;
		}
		if (Common.isNullStr(String.valueOf(tfFilePath.getValue()))) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "��ѡ��EXCEL�ļ�");
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfSource.getValue()))) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "��ѡ��Դ����Ϣ");
			return info;
		}

		if (nodeC == null) {
			info.setSuccess(false);
			JOptionPane.showMessageDialog(pnlBAttr, "��ѡ���ͷ");
			return info;
		}
		return info;
	}

	/**
	 * ������һ�����潫���еĲ���
	 * 
	 */
	private InfoPackage gotoNext() {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try {
			info = checkIsNext();
			if (info.getSuccess() == false) {
				JOptionPane.showMessageDialog(tabPane, info.getsMessage());
				tabPane.setSelectedIndex(0);
				return info;
			}
			tabPane.setSelectedIndex(1);
			if (!isChange)
				return info;
			;
			bmk = BAct.initAttrData(dsSource, dsSql);
			saveSourceInfo();
			saveSqlInfo();
			BAct.changeParentID(dsSource, sReportID);
			setColPanel();
			List listFields = TTableHeaderC.getFields();
			DataSet dsBody = DataSet.create();
			initBodyData(dsBody, listFields, "");
			initBodyData(dsBody, listFields, "������");
			reportC.setBodyData(dsBody);
			reportC.refreshBody();
			reportUIC.repaint();
			isChange = false;
			return info;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return info;
	}

	/**
	 * ������һ������
	 * 
	 * @author Administrator
	 * 
	 */
	private class SelNextPaneListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			InfoPackage info = new InfoPackage();
			info.setSuccess(true);
			int idex = tabPane.getSelectedIndex();
			if (idex == 1) {
				info = gotoNext();
				if (info.getSuccess() == false) {
					JOptionPane.showMessageDialog(tabPane, info.getsMessage());
					return;
				}
				isChange = false;
			}
		}
	}

	// public void doCancel() {
	// // ȡ��
	// try {
	// Component aSel = tabPane.getSelectedComponent();
	// if (JOptionPane.showConfirmDialog(aSel, "�Ƿ�ȷ��Ҫȫ���������ã�", "��ʾ",
	// JOptionPane.OK_CANCEL_OPTION) == 0) {
	// if (dsTitle != null)
	// dsTitle.cancel();
	// if (dsSource != null)
	// dsSource.cancel();
	// if (dsInfo != null) {
	// dsInfo.cancel();
	// table.setDataSet(dsInfo);
	// table.reset();
	// }
	// if (dsSql != null)
	// dsSql.cancel();
	// tfSource.setValue("");
	// tfMid1.setValue("");
	// tfMid2.setValue("");
	// tfFileName.setValue("");
	// tabPane.setSelectedIndex(0);
	// iState = state_Browse;
	// setButtonState(iState);
	// getReportID();
	// isChange = true;
	// }
	// } catch (Exception ee) {
	// ee.printStackTrace();
	// }
	// }

	/**
	 * ����
	 * 
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			// ����
			try {
				InfoPackage infoReturn = new InfoPackage();
				infoReturn = checkInfo();
				if (infoReturn.getSuccess() == false) {
					JOptionPane.showMessageDialog(tabPane, infoReturn
							.getsMessage());
					return;
				}

				if (dsTitle == null || dsSql == null || dsSource == null) {
					JOptionPane.showMessageDialog(tabPane, "��Ϣ������");
					return;
				}
				if (dsTitle.isEmpty() || dsSql.isEmpty() || dsSource.isEmpty()) {
					JOptionPane.showMessageDialog(tabPane, "��Ϣ������");
					return;
				}
				if (!dsTitle.isEmpty()) {
					InfoPackage info = checkIsNullCol(dsTitle);
					if (info.getSuccess() == false) {
						JOptionPane.showMessageDialog(pnlCAttr, info
								.getsMessage());
						tabPane.setSelectedIndex(1);
						return;
					}
				}
				List listSql = new LinkedList();
				int iRecNum = 0;
				listSql.add(iRecNum, BAct.getBSql(dsSource));
				iRecNum++;
				sReportID = getReportID();
				dsSource.edit();
				dsSource.beforeFirst();
				while (dsSource.next()) {
					dsSource.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
				}
				dsTitle.maskDataChange(true);
				dsTitle.edit();
				dsTitle.beforeFirst();
				while (dsTitle.next()) {
					dsTitle.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
					listSql.add(iRecNum, BAct.getCSql(dsTitle));
					iRecNum++;
				}
				dsTitle.maskDataChange(false);

				List lstSqlLines = new ArrayList();
				dsSql.maskDataChange(true);
				dsSql.edit();
				dsSql.beforeFirst();
				int iLevel = 1;
				while (dsSql.next()) {
					dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(
							sReportID);
					dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue(
							String.valueOf(iLevel));
					lstSqlLines.add(BAct.getSSql(dsSql));
					iRecNum++;
					iLevel++;
				}

				List lstType = reportTypeList.getSelectData();
				itserv.execSql(listSql, lstSqlLines, sReportID, lstType);
				// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
				dsSource.beforeFirst();
				dsSource.next();
				dsSource.fieldByName(IBesQryReport.IS_END).setValue("1");
				defineReport.refreshNodeEdit(dsSource, lstType, sReportID);

				iState = state_Browse;
				JOptionPane.showMessageDialog(tabPane, "���Ʊ���ɹ�");
				BesUI.this.dispose();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	// ///////////////////////////////////////////////////////////���ܺ���///////////////////////////////////////////////////////////////////////////

	/**
	 * �ж��Ƿ����еĽڵ㶼������field_ename(���ڵ����)
	 * 
	 * @param dsPost
	 * @return
	 * @throws Exception
	 */
	private InfoPackage checkIsNullCol(DataSet dsPost) throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		// boolean IsParNull =
		DataSet ds = (DataSet) dsPost.clone();
		String[] pars = getArrayPars(dsPost);
		pars = BesAct.getNotNullArray(pars);
		boolean arrayIsNull = BesAct.checkArrayIsNull(pars);
		// int iparNum = pars.length;
		// int iparNewNum = 0;
		// for ( int i = 0 ; i < iparNum ; i++ ){
		//			
		// }
		if (pars.length != 0 && arrayIsNull)
			Arrays.sort(pars);
		ds.beforeFirst();
		while (ds.next()) {
			String sNodeID = ds.fieldByName("nodeID").getString();
			if (pars.length != 0 && arrayIsNull
					&& Arrays.binarySearch(pars, sNodeID) >= 0)
				continue;
			String sEName = ds.fieldByName(IBesQryReport.FIELD_ENAME)
					.getString();
			String sCName = ds.fieldByName(IBesQryReport.FIELD_CNAME)
					.getString();
			if (Common.isNullStr(sEName)) {
				info.setSuccess(false);
				info.setsMessage("[" + sCName + "] �Ķ�Ӧ��û������");
				return info;
			}
		}
		return info;
	}

	/**
	 * ��ȡ��ͷ���ݼ������и��ڵ������
	 * 
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	private String[] getArrayPars(DataSet ds) throws Exception {
		String[] pars = new String[ds.getRecordCount()];
		int iParCirNum = 0;
		ds.beforeFirst();
		while (ds.next()) {
			String sParID = ds.fieldByName("PARID").getString();
			if (Common.isNullStr(sParID))
				continue;
			pars[iParCirNum] = sParID;
			iParCirNum++;
		}
		return pars;
	}

	/**
	 * ���������Ϣ
	 * 
	 * @throws Exception
	 */
	private void setColPanel() throws Exception {
		// tableHeader = BesAct.getHeader();
		dsInfo = DataSet.create();
		dsInfo = itserv.getFieldInfo(dsSource.fieldByName(
				IBesQryReport.OBJECT_NAMES).getString());
		table.setDataSet(dsInfo);
		table.reset();
	}

	/**
	 * ����������Ϣ��repset��
	 */
	private void saveSourceInfo() throws Exception {
		if (dsSource == null)
			return;
		dsSource.edit();
		dsSource.gotoBookmark(bmk[0]);
		dsSource.fieldByName(IBesQryReport.TITLE).setValue(tfTitle.getValue());
		dsSource.fieldByName(IBesQryReport.REPORT_CNAME).setValue(
				tfFileName.getValue());
		dsSource.fieldByName(IBesQryReport.OBJECT_NAMES).setValue(
				tfSource.getValue());
		String sMT = cbMoneyType.getValue().toString();
		String sMTValue = null;
		if ("1".equals(sMT))
			sMTValue = "Ԫ";
		else
			sMTValue = "��Ԫ";
		dsSource.fieldByName(IBesQryReport.CURRENCYUNIT).setValue(sMTValue);

		// �����û�����
		dsSource.fieldByName(IBesQryReport.DATA_USER).setValue(
				rgUserType.getValue());
		dsSource.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		dsSource.fieldByName(IBesQryReport.LVL_ID).setValue(sReportID);
		sTypeNO = dsSource.fieldByName(IBesQryReport.TYPENO).getString();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void saveSqlInfo() throws Exception {
		if (dsSql == null)
			return;

		dsSql.edit();
		dsSql.gotoBookmark(bmk[1]);
		dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(tfSource.getValue());
		// dsSql.fieldByName( IBesQryReport.SQLLINES ).setValue(
		// BesAct.getSql(tfSource.getValue().toString()) );
		dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		if (!Common.isNullStr(Common.nonNullStr(tfMid1.getValue()))) {
			dsSql.append();
			dsSql.fieldByName(IBesQryReport.SET_YEAR)
					.setValue(Global.loginYear);
			// dsSql.fieldByName( IBesQryReport.REPORT_ID ).setValue( sReportID
			// );
			dsSql.fieldByName(IBesQryReport.SQLTYPE).setValue("MIDQUERY");
			dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue("2");
			dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(
					tfMid1.getValue());
			// String sType = BAct.getTalbeType( tfMid1.getValue().toString());
			// if ( "VIEW".equals(sType))
			// dsSql.fieldByName(IBesQryReport.SQLLINES).setValue(
			// BesAct.getSql(tfMid1.getValue().toString()));
			dsSql.fieldByName(IBesQryReport.TYPENO).setValue(sTypeNO);
			dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
		}
		if (!Common.isNullStr(Common.nonNullStr(tfMid2.getValue()))) {
			dsSql.append();
			dsSql.fieldByName(IBesQryReport.SET_YEAR)
					.setValue(Global.loginYear);
			// dsSql.fieldByName( IBesQryReport.REPORT_ID ).setValue( sReportID
			// );
			dsSql.fieldByName(IBesQryReport.SQLTYPE).setValue("MIDQUERY");
			dsSql.fieldByName(IBesQryReport.VIEWLEVEL).setValue("3");
			dsSql.fieldByName(IBesQryReport.VIEWNAME).setValue(
					tfMid2.getValue());
			dsSql.fieldByName(IBesQryReport.REPORT_ID).setValue(sReportID);
			// String sType = BAct.getTalbeType( tfMid2.getValue().toString());
			// if ( "VIEW".equals(sType))
			// dsSql.fieldByName(IBesQryReport.SQLLINES).setValue(
			// BesAct.getSql(tfMid2.getValue().toString()));
			dsSql.fieldByName(IBesQryReport.TYPENO).setValue(sTypeNO);
		}
	}

	/**
	 * ����dataset��ʼ������(�����Խ���ı���)
	 */
	private DataSet initBodyData(DataSet ds, List list, String fieldValue)
			throws Exception {
		ds.append();
		for (int i = 0; i < list.size(); i++) {
			String sFieldName = null;
			if (list.get(i) == null)
				sFieldName = "";
			else
				sFieldName = list.get(i).toString();
			ds.fieldByName(sFieldName).setValue(fieldValue);
		}
		return ds;
	}

	/**
	 * ��������Ϣ����Դ�����ݷ����ı�ʱ�ļ����¼�
	 * 
	 * @param ds
	 *            ����Ϣ��dataset
	 * 
	 * @param tree
	 *            Դ���dataset
	 * 
	 * @param tf
	 *            Ҫ����textfield
	 * 
	 * @param fieldname
	 *            ���ڵĶ�λ��col���ֶ���
	 */
	private class DataSoureChangeListener extends MouseAdapter {
		FTextField tf;

		CustomTree tree;

		DataSet ds;

		String fieldname;

		public DataSoureChangeListener(DataSet ds, String fieldname,
				CustomTree tree, FTextField tf) {
			this.tf = tf;
			this.tree = tree;
			this.ds = ds;
			this.fieldname = fieldname;
		}

		public void mouseClicked(MouseEvent e) {
			try {
				MyTreeNode node = (MyTreeNode) tree.getSelectedNode();
				if (node == null || node == tree.getRoot())
					return;
				PfTreeNode nodeNow = (PfTreeNode) node.getUserObject();
				String name = nodeNow.getShowContent();
				tf.setValue(name);
				if (e.getClickCount() == 2) {
					// ���������뵼colinfo��dataset��
					setColEName();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * ��������ϢӢ����
	 */
	private void setColEName() throws Exception {
		// ���ñ���ʾ��Ϣ
		Cell cell = (Cell) reportUIC.getReportContent().getSelectedCell();
		int iCol = cell.getColumn();
		int iRows = TTableHeaderC.getRows();
		Cell cellEName = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows);
		cellEName.setValue(dsInfo.fieldByName(IBesQryReport.Column_NAME)
				.getString());
		reportUIC.repaint();
		// �༭���ݼ��������
		Cell cellRoot = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows - 1);
		String bmk = cellRoot.getBookmark();
		dsTitle.gotoBookmark(bmk);
		dsTitle.edit();
		dsTitle.fieldByName(IBesQryReport.FIELD_ENAME).setValue(
				dsInfo.fieldByName("COLUMN_NAME").getString());
	}

	/**
	 * reportUI���������¼�
	 */
	private class ReportClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				Cell cell = (Cell) reportUIC.getReportContent() // ��ǰѡ�еĵ�Ԫ��
						.getSelectedCell();
				String bmkNow = null;
				int iCol = cell.getColumn();
				int iRows = TTableHeaderC.getRows();
				Object oEName = reportUIC.getReportContent().getCellElement(
						iCol, iRows).getValue();
				String sEName = ((oEName == null) ? "" : oEName.toString());
				if (!Common.isNullStr(sEName)) {
					table.expandTo("COLUMN_NAME", sEName);
				}
				Cell cellRoot = (Cell) reportUIC.getReportContent()
						.getCellElement(iCol, iRows - 1);
				bmkNow = cellRoot.getBookmark();
				dsTitle.gotoBookmark(bmkNow);
				setPanelInfo(iCol);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * �����Ƿ������еļ����¼�
	 */
	private class HideChangeListener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent e) {
			try {
				setColHide();
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * �и�ʽ����Ϣ�����ı�ʱ�ļ����¼�
	 */
	private class FormatChangeLisentener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent arg0) {
			try {
				Object oValue = tfColFormat.getValue();
				String sValue = (oValue == null) ? "" : oValue.toString();
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT).setValue(
						sValue);
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * ���ð�ť�ļ����¼�
	 */
	private class SetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// ������ʾ��Ϣ
				MyTreeNode nodeTree = (MyTreeNode) table.getSelectedNode();
				if (dsInfo == null || dsInfo.isEmpty() || dsInfo.bof()
						|| dsInfo.eof() || nodeTree == null
						|| nodeTree == table.getRoot()) {
					JOptionPane.showMessageDialog(pnlCAttr, "��ѡ��Ҫ���õ�����Ϣ");
					return;
				}
				setColEName();
				setColHide();
				int iRows = BesAct.tableHeader.getRows();
				Cell cell = (Cell) reportUIC.getReportContent()
						.getSelectedCell();
				int iCol = cell.getColumn();
				Cell cellRoot = (Cell) reportUIC.getReportContent()
						.getCellElement(iCol, iRows - 1);
				Double dWidth = new Double(reportUIC.getReportContent()
						.getColumnWidth(iCol));
				String sCName = cellRoot.getValue().toString();
				String bmk = cell.getBookmark();
				dsTitle.gotoBookmark(bmk);
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_DISPWIDTH).setValue(
						dWidth);
				dsTitle.fieldByName(IBesQryReport.FIELD_CNAME).setValue(sCName);
				dsTitle.fieldByName(IBesQryReport.IS_HIDECOL).setValue(
						("true".equals(ckHide.getValue().toString())) ? "��"
								: "��");
				dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT).setValue(
						Common.nonNullStr(tfColFormat.getValue()));
				dsTitle.fieldByName(IBesQryReport.FIELD_TYPE).setValue(
						cbDataType.getValue().toString());
				setPanelInfo(reportUIC.getReportContent().getSelectedCell()
						.getColumn());
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * �����͸ı�ʱ�����¼�
	 */
	private class ColTypeChangeLisentener implements ValueChangeListener {
		public void valueChanged(ValueChangeEvent e) {
			try {
				Object oValue = cbDataType.getValue();
				dsTitle.edit();
				dsTitle.fieldByName(IBesQryReport.FIELD_TYPE).setValue(oValue);
				if (oValue == null)
					return;
				String sValue = oValue.toString();
				if (sValue.equals("������")) {
					String sFilter = "CVALUE=='��С������ֵ'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("����")) {
					String sFilter = "CVALUE=='����'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("�ַ���")) {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				} else if (sValue.equals("������")) {
					String sFilter = "CVALUE=='����'";
					DataSet ds = DataSetUtil.filterBy(dsFormat, sFilter);
					tfColFormat.setDataSet(ds);
					tfColFormat.reset();
				} else if (sValue.equals("����")) {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				} else {
					tfColFormat.setDataSet(dsFormat);
					tfColFormat.reset();
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}
	}

	/**
	 * �ѱ��ϵ���Ϣ��ʾ�������
	 * 
	 * @param iCol
	 */
	private void setPanelInfo(int iCol) throws Exception {
		int iRows = TTableHeaderC.getRows();
		Cell cellEName = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows);
		tfColName.setValue(cellEName.getValue());
		Cell cellHide = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows + 1);
		Object hideValue = cellHide.getValue();
		String sHide = null;
		if (hideValue != null)
			sHide = cellHide.getValue().toString();
		if ("������".equals(sHide))
			ckHide.setValue("false");
		else if ("����".equals(sHide))
			ckHide.setValue("true");
		else
			ckHide.setValue("false");

		if (dsTitle == null || dsTitle.isEmpty())
			return;
		tfColFormat.setValue(dsTitle.fieldByName(IBesQryReport.FIELD_DISFORMAT)
				.getString());
		String sDataType = dsTitle.fieldByName(IBesQryReport.FIELD_TYPE)
				.getString();
		if (!Common.isNullStr(sDataType))
			cbDataType.setValue(dsTitle.fieldByName(IBesQryReport.FIELD_TYPE)
					.getString());
		String sFormatValue = dsTitle
				.fieldByName(IBesQryReport.FIELD_DISFORMAT).getString();
		if (!Common.isNullStr(sFormatValue))
			tfColFormat.setValue(sFormatValue);
	}

	/**
	 * ��������Ϣ�Ƿ�����
	 */
	private void setColHide() throws Exception {
		String sHideValue = ("true".equals(ckHide.getValue().toString())) ? "��"
				: "��";
		String sHideCellValue = ("true".equals(ckHide.getValue().toString())) ? "����"
				: "������";
		Cell cell = (Cell) reportUIC.getReportContent().getSelectedCell();
		int iCol = cell.getColumn();
		int iRows = TTableHeaderC.getRows();
		Cell cellHide = (Cell) reportUIC.getReportContent().getCellElement(
				iCol, iRows + 1);
		// reportUIC.repaint();
		cellHide.setValue(sHideCellValue);
		reportUIC.repaint();
		// �༭���ݼ��е�����
		if (dsTitle == null || dsTitle.isEmpty())
			return;
		dsTitle.edit();
		dsTitle.fieldByName(IBesQryReport.IS_HIDECOL).setValue(sHideValue);
	}

	/**
	 * �û����ͷ����ı�ʱ�ļ����¼�
	 */
	private void addUserTypeSelListener() throws Exception {
		JRadioButton[] radios = rgUserType.getRadios();
		int iRadioNum = radios.length;
		for (int i = 0; i < iRadioNum; i++) {
			radios[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						getReportID();
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * ��ñ������
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getReportID() throws Exception {
		return "";
//		return BAct.getReportID();
	}

	private InfoPackage checkInfo() throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(String.valueOf(tfFileName.getValue()))) {
			info.setsMessage("���������");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfFilePath.getValue()))) {
			info.setsMessage("��ѡ��EXCEL�ļ�");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (Common.isNullStr(String.valueOf(tfSource.getValue()))) {
			info.setsMessage("��ѡ��Դ����Ϣ");
			tabPane.setSelectedIndex(0);
			return info;
		}
		if (nodeC == null) {
			info.setsMessage("��ѡ���ͷ");
			tabPane.setSelectedIndex(0);
			return info;
		}

		if (reportTypeList.getSelectData().size() == 0) {
			info.setsMessage("��ѡ�񱨱�����!");
			tabPane.setSelectedIndex(0);
			return info;
		}

		// ����Ƿ��Ѿ�����
		String sFilter = IBesQryReport.SET_YEAR + "=" + Global.loginYear;
		DataSet dsExistSource = itserv.getDataSet(IBesQryReport.TABLENAME_MAIN,
				sFilter);
		dsExistSource.beforeFirst();
		while (dsExistSource.next()) {
			String sEReportID = dsExistSource.fieldByName(
					IBesQryReport.REPORT_ID).getString();
			if (Common.isEqual(sReportID, sEReportID)) {
				info.setSuccess(false);
				info.setsMessage("�����Ѿ��Ѿ�������ͬ��ŵı���");
				return info;
			}
		}
		return info;
	}

	/**
	 * ���ð�ť״̬
	 */
	private void setButtonState(int aState) throws Exception {
		List controls = null;
		// this.getToolbarPanel().getSubControls();
		boolean isEditEnable;
		boolean isSaveEnable;
		isEditEnable = true;
		isSaveEnable = true;
		if (aState == state_Browse) {
			isEditEnable = true;
			isSaveEnable = false;
		} else {
			isEditEnable = false;
			isSaveEnable = true;
		}
		// if (IWf.getCanEditStru() == false) {
		// isEditEnable = false;
		// isSaveEnable = false;
		// }
		// for (int i = 0; i < controls.size(); i++) {
		// FButton btnGet = (FButton) controls.get(i);
		// // if ("��һ��".equals(btnGet.getText())) {
		// // btnGet.setEnabled(isSaveEnable);
		// // }
		// // if ("��һ��".equals(btnGet.getText())) {
		// // btnGet.setEnabled(isEditEnable);
		// // }
		// if ("�ر�".equals(btnGet.getText())) {
		// btnGet.setEnabled(isEditEnable);
		// }
		// if ("����".equals(btnGet.getText())) {
		// btnGet.setEnabled(isSaveEnable);
		// }
		// if ("ȡ��".equals(btnGet.getText())) {
		// btnGet.setEnabled(isSaveEnable);
		// }
		// }
	}

	/**
	 * ��һ��
	 * 
	 */
	private class AddActionListenerpre implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			int idex = tabPane.getSelectedIndex();
			if (idex != 0)
				tabPane.setSelectedIndex(0);

		}
	}

	/**
	 * �ر�
	 * 
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			BesUI.this.dispose();
		}
	}

	/**
	 * ��ť���
	 */
	private class FPanelBtnButtom extends FPanel {
		private FButton fbtnPrev;

		private FButton fbtnNext;

		private FButton fbtnDown;

		private FButton fbtnCancel;

		private static final long serialVersionUID = 1L;

		public FPanelBtnButtom() {
			this.setLeftInset(10);
			this.setRightInset(10);
			this.setLayout(new RowPreferedLayout(2));

			fbtnPrev = new FButton("fbtnPrev", "��һ��");
			fbtnPrev.addActionListener(new AddActionListenerpre());
			fbtnNext = new FButton("fbtnNext", "��һ��");
			fbtnNext.addActionListener(new AddActionListenernext());
			FFlowLayoutPanel fpnlPreNext = new FFlowLayoutPanel();
			// ���ÿ�����ʾ
			fpnlPreNext.setAlignment(FlowLayout.RIGHT);
			// ��һ��
			fpnlPreNext.addControl(fbtnPrev, new TableConstraints(1, 1, false,
					false));
			// ��һ��
			fpnlPreNext.addControl(fbtnNext, new TableConstraints(1, 1, false,
					false));

			fbtnDown = new FButton("fbtnDown", "�� ��");
			fbtnDown.addActionListener(new DownActionListener());
			fbtnCancel = new FButton("fbtnCancel", "�� ��");
			fbtnCancel.addActionListener(new CancelActionListener());

			FFlowLayoutPanel fpnlDown = new FFlowLayoutPanel();
			fpnlDown.setAlignment(FlowLayout.RIGHT);
			// ���
			fpnlDown.addControl(fbtnDown, new TableConstraints(1, 1, false,
					false));
			// ȡ��
			fpnlDown.addControl(fbtnCancel, new TableConstraints(1, 1, false,
					false));

			this
					.addControl(fpnlPreNext, new TableConstraints(1, 1, true,
							true));
			this.addControl(fpnlDown, new TableConstraints(1, 1, true, true));

		}
	}

}
