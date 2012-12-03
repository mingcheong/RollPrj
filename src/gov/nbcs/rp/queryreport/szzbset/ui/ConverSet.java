package gov.nbcs.rp.queryreport.szzbset.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ReportUtil;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.ReportTypeList;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.szzbset.ibs.ISzzbSet;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.cell.JWorkBook;
import com.fr.report.CellElement;
import com.fr.report.WorkBook;
import com.fr.report.WorkSheet;

/**
 * ��ѯ�����������
 * 
 * @author qzc
 * 
 */
public class ConverSet extends JFrame {

	private static final long serialVersionUID = 1L;

	// ��Excel���ļ���·��
	private FLabel flblOpenExcel;

	private JWorkBook jWorkBook;

	private FTabbedPane ftabPnlSet;

	// ������
	private FTextField ftxtTitle;

	// �����û�����
	private FRadioGroup reportUserTypeGrp = null;

	// ��Ԫ�����
	private FComboBox fcbxFieldPara;

	// �������
	private ReportTypeList reportTypeList;

	// ռ������
	private FTextField ftxtConverArea;

	// �Ƿ�����
	private FCheckBox fchkIsActice;

	private DataSet dsSzzb = null;

	private int iTypeNo = 1;

	private int KEY_UP = 38;

	private int KEY_DOWN = 40;

	private int KEY_LEFT = 37;

	private int KEY_RIGHT = 39;

	private DefineReport defineReport;

	private RepSetObject repSetObject;

	private boolean clearFlag = false;

	private String sReportID = null;

	public ConverSet(RepSetObject repSetObject, DefineReport defineReport) {
		this.setSize(900, 650);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		if (repSetObject == null) {
			this.setTitle("���������Ŀ¼");
		} else {
			this.setTitle("�޸ķ����Ŀ¼");
		}
		if (repSetObject != null) {
			this.sReportID = repSetObject.getREPORT_ID();
		}

		this.repSetObject = repSetObject;
		this.defineReport = defineReport;

		if (this.repSetObject == null) {
			this.repSetObject = new RepSetObject();
		}

		init();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void init() {
		try {
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(FSplitPane.VERTICAL_SPLIT);
			fSplitPane.setDividerLocation(Toolkit.getDefaultToolkit()
					.getScreenSize().height - 310);
			this.getContentPane().add(fSplitPane);

			// ���汨����Ϣ
			FPanel fpnlReport = new FPanel();
			fSplitPane.addControl(fpnlReport);
			fpnlReport.setLeftInset(10);
			fpnlReport.setRightInset(10);
			fpnlReport.setBottomInset(10);
			fpnlReport.setTopInset(10);

			WorkBook workBook = new WorkBook() {
				private static final long serialVersionUID = 1L;

			};
			WorkSheet workSheet = new WorkSheet() {

				private static final long serialVersionUID = 1L;

			};
			workBook.addReport(workSheet);
			jWorkBook = new JWorkBook() {
				private static final long serialVersionUID = 1L;

			};

			jWorkBook.setWorkBook(workBook);
			jWorkBook.getGrid().addMouseListener(new reportMouseListener());
			jWorkBook.getGrid().addKeyListener(new reportKeyListener());

			flblOpenExcel = new FLabel();
			flblOpenExcel.setForeground(Color.blue);
			FButton fbtnOpenExcel = new FButton("fbtnOpenExcel", "��Excelģ��");
			fbtnOpenExcel.addActionListener(new OpenExcelActionListener(
					jWorkBook, flblOpenExcel));

			RowPreferedLayout rLay = new RowPreferedLayout(3);
			rLay.setRowHeight(23);
			rLay.setRowGap(5);
			rLay.setColumnWidth(115);
			fpnlReport.setLayout(rLay);
			fpnlReport.add(jWorkBook, new TableConstraints(1, 3, true, true));
			fpnlReport.addControl(fbtnOpenExcel, new TableConstraints(1, 1,
					false, false));
			fpnlReport.addControl(flblOpenExcel, new TableConstraints(1, 1,
					false, true));

			// ����������Ϣ
			ftabPnlSet = new FTabbedPane();
			FTitledPanel ftitPnlConverInfo = new ConverInfoTitledPanel();
			ftabPnlSet.addControl("��Ϣ����", ftitPnlConverInfo);
			ftabPnlSet.setBorder(null);

			// ���尴ť���
			ButtonPanel buttonPanel = new ButtonPanel();

			// �����·����
			FPanel fpnlBottom = new FPanel();
			RowPreferedLayout rLayBtn = new RowPreferedLayout(1);
			rLayBtn.setRowHeight(30);
			fpnlBottom.setLayout(rLayBtn);
			fpnlBottom.addControl(ftabPnlSet, new TableConstraints(1, 1, true,
					true));
			fpnlBottom.addControl(buttonPanel, new TableConstraints(1, 1,
					false, true));
			fSplitPane.addControl(fpnlBottom);

			// ��ʾ������Ϣ
			if (!Common.isNullStr(sReportID)) {
				showBaseInfo();
				// ��ʾ������Ϣ
				dsSzzb = SzzbSetI.getMethod().getSzzb(sReportID,
						Global.loginYear);
				setConverInfo(jWorkBook, dsSzzb);
			} else {
				dsSzzb = DataSet.create();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�����Ŀ¼������ʾ���淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ������Ϣ����
	 * 
	 */
	private class ConverInfoTitledPanel extends FTitledPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ConverInfoTitledPanel() throws Exception {
			this.setLeftInset(5);
			this.setRightInset(5);
			this.setTopInset(5);
			RowPreferedLayout rLay = new RowPreferedLayout(2);
			rLay.setColumnWidth(370);
			rLay.setRowGap(8);
			this.setLayout(rLay);

			ftxtTitle = new FTextField("�������ƣ�");

			// ���������Ϣ
			FPanel fpnlReportType = new FPanel();
			fpnlReportType.setTitle("�������ͣ�");
			reportTypeList = new ReportTypeList();
			fpnlReportType.setLayout(new RowPreferedLayout(1));
			fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
					true));

			// �û�����
			reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
			reportUserTypeGrp.setRefModel("0#����λʹ�� +1#������ʹ��+2#������λ��ͬʹ��");
			reportUserTypeGrp.setValue("2");
			reportUserTypeGrp.setTitleVisible(false);
			// ���屨���û��������
			FPanel reportUserTypePnl = new FPanel();
			reportUserTypePnl.setTitle("�û�����");
			reportUserTypePnl.setLayout(new RowPreferedLayout(1));
			// �����û����͵�ѡ����뱨���û��������
			reportUserTypePnl.addControl(reportUserTypeGrp,
					new TableConstraints(1, 1, true, true));

			FPanel fpnlConverArea = new FPanel();
			fpnlConverArea.setTitle("�����Ŀ¼����");
			RowPreferedLayout rConverAreaLay = new RowPreferedLayout(2);
			rConverAreaLay.setColumnWidth(90);
			fpnlConverArea.setLayout(rConverAreaLay);
			ftxtConverArea = new FTextField("ռ������");
			ftxtConverArea.setEditable(false);
			FButton fbtnConverArea = new FButton("fbtnConverArea", "�������");
			fbtnConverArea.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int i = jWorkBook.getCellSelection().getColumn();
					int j = jWorkBook.getCellSelection().getRow();
					int ispan = jWorkBook.getCellSelection().getColumnSpan();
					int jspan = jWorkBook.getCellSelection().getRowSpan();
					String sColumnName = ReportUtil.translateToColumnName(i)
							+ (j + 1) + ":"
							+ ReportUtil.translateToColumnName(i + ispan - 1)
							+ (j + jspan);
					ftxtConverArea.setValue(sColumnName);

					// �õ�fb_u_qr_szzb����Ϣ
					try {
						// if (dsSzzb == null) {
						// dsSzzb = DataSet.create();
						// } else {
						// dsSzzb.clearAll();
						// }
						dsSzzb = DataSet.create();
						dsSzzb.edit();
						getSzzbInfo(dsSzzb);
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ConverSet.this,
								"������������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			});
			fpnlConverArea.addControl(ftxtConverArea, new TableConstraints(1,
					1, false, true));
			fpnlConverArea.addControl(fbtnConverArea, new TableConstraints(1,
					1, false, false));

			FPanel fpnlFieldPara = new FPanel();
			fpnlFieldPara.setTitle("���õ�Ԫ�����");
			RowPreferedLayout rFieldParaLay = new RowPreferedLayout(2);
			rFieldParaLay.setColumnWidth(90);
			fpnlFieldPara.setLayout(rFieldParaLay);
			fcbxFieldPara = new FComboBox("��Ԫ�������");
			fcbxFieldPara.setRefModel("# +DIV_NAME#Ԥ�㵥λ");
			FButton fbtnFieldArea = new FButton("fbtnFieldArea", "���ò���");
			fbtnFieldArea.addActionListener(new FieldAreaActionListener());
			fpnlFieldPara.addControl(fcbxFieldPara, new TableConstraints(1, 1,
					false, true));
			fpnlFieldPara.addControl(fbtnFieldArea, new TableConstraints(1, 1,
					false, false));

			// �Ƿ�����
			fchkIsActice = new FCheckBox("�Ƿ�����");
			fchkIsActice.setTitlePosition("RIGHT");
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);

			// ��������
			this
					.addControl(ftxtTitle, new TableConstraints(1, 1, false,
							false));
			// �������
			this.addControl(fpnlReportType, new TableConstraints(5, 1, false,
					false));
			// ���÷�������
			this.addControl(fpnlConverArea, new TableConstraints(2, 1, false,
					false));
			// �û�����
			this.addControl(reportUserTypePnl, new TableConstraints(2, 1,
					false, false));
			// ��������
			this.addControl(fpnlFieldPara, new TableConstraints(2, 1, false,
					false));
			// �Ƿ�����
			this.addControl(fchkIsActice, new TableConstraints(1, 1, false,
					false));
		}
	}

	/**
	 * ��ť���
	 */
	private class ButtonPanel extends FFlowLayoutPanel {
		private static final long serialVersionUID = 1L;

		// "���"��ť
		private FButton fbtnDown = null;

		// "ȡ��"��ť
		private FButton fbtnCancel = null;

		public ButtonPanel() {
			init();
		}

		private void init() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);

			fbtnDown = new FButton("fbtnDown", "�� ��");
			fbtnDown.addActionListener(new SaveConverActionListener());
			fbtnCancel = new FButton("fbtnDown", "�� ��");
			fbtnCancel.addActionListener(new CancelActionListener());

			// ���
			this.addControl(fbtnDown, new TableConstraints(1, 1, true, false));
			// ȡ��
			this
					.addControl(fbtnCancel, new TableConstraints(1, 1, true,
							false));
		}

	}

	/**
	 * ��ʾ������Ϣ
	 */
	private void showBaseInfo() throws Exception {
		ftxtTitle.setValue(repSetObject.getREPORT_CNAME());
		// ���屨�����
		reportTypeList.setSelected(repSetObject.getREPORT_ID());
		ftxtConverArea.setValue(repSetObject.getCOLUMN_AREA());

		reportUserTypeGrp.setValue(String.valueOf(repSetObject.getDATA_USER()));
		((JCheckBox) fchkIsActice.getEditor()).setSelected(Common
				.estimate(repSetObject.getIS_ACTIVE()));
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 */
	private void setConverInfo(JWorkBook jWorkBook, DataSet dsSzzb)
			throws Exception {
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;
		Object oValue;
		Cell cellElement;

		dsSzzb.beforeFirst();
		while (dsSzzb.next()) {
			iColumn = dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN).getInteger();
			iColumnSpan = dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMNSPAN)
					.getInteger();
			iRow = dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).getInteger();
			iRowSpan = dsSzzb.fieldByName(ISzzbSet.FIELD_ROWSPAN).getInteger();

			oValue = dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).getValue();
			cellElement = new Cell(iColumn, iRow, iColumnSpan, iRowSpan, oValue);
			// ���ʽ
			Style style = Style.getInstance();
			style = style.deriveHorizontalAlignment(dsSzzb.fieldByName(
					ISzzbSet.HOR_ALIGNMENT).getInteger());
			style = style.deriveVerticalAlignment(dsSzzb.fieldByName(
					ISzzbSet.VER_ALIGNMENT).getInteger());
			FRFont aFont = FRFont.getInstance(dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONT).getString(), dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONTSTYPE).getInteger(), dsSzzb.fieldByName(
					ISzzbSet.FIELD_FONTSIZE).getInteger());
			style = style.deriveFRFont(aFont);
			cellElement.setStyle(style);
			jWorkBook.getReport().setColumnWidth(iColumn,
					dsSzzb.fieldByName(ISzzbSet.FIELD_COLWIDTH).getInteger());
			jWorkBook.getReport().setRowHeight(iRow,
					dsSzzb.fieldByName(ISzzbSet.FIELD_ROWHEIGHT).getInteger());
			jWorkBook.getReport().addCellElement(cellElement, false);
		}
	}

	/**
	 * �������
	 */
	public class SaveConverActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (Common.isNullStr(ftxtTitle.getValue().toString().trim())) {
				JOptionPane.showMessageDialog(ConverSet.this, "����д��������!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				ftxtTitle.setFocus();
				return;
			}
			if ("".equals(ftxtConverArea.getValue())) {
				JOptionPane.showMessageDialog(ConverSet.this, "��ѡ��ռ������!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			List lstType = reportTypeList.getSelectData();
			if (lstType.size() == 0) {
				JOptionPane.showMessageDialog(ConverSet.this, "	��ѡ�񱨱�����!",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try {
				// ���������Ϣ
				getRepsetInfo();

				// ��fb_u_qr_szzb���report_id
				dsSzzb.edit();
				dsSzzb.beforeFirst();
				while (dsSzzb.next()) {
					dsSzzb.fieldByName(ISzzbSet.REPORT_ID).setValue(
							repSetObject.getREPORT_ID());
				}

				// ���汨����Ϣ
				if (clearFlag) {
					SzzbSetI.getMethod().saveConver(repSetObject, dsSzzb,
							repSetObject.getREPORT_ID(),
							reportTypeList.getSelectData());
				} else {
					SzzbSetI.getMethod().saveConver(repSetObject, dsSzzb, null,
							lstType);
				}

				dsSzzb.applyUpdate();
				clearFlag = false;

				// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
				defineReport.refreshNodeEdit(repSetObject, lstType, sReportID);

				// ����������Ϊ�޸�
				sReportID = repSetObject.getREPORT_ID();

				JOptionPane.showMessageDialog(ConverSet.this, "����ɹ���", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this, "����ʧ��,������Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR);
			}
		}
	}

	/**
	 * �رհ�ť�����ť�¼�
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			ConverSet.this.dispose();

		}
	}

	/**
	 * �õ�repset��Ϣ
	 */
	private void getRepsetInfo() throws Exception {

		if (Common.isNullStr(sReportID)) {
			repSetObject.setSET_YEAR(Integer.parseInt(Global.loginYear));
			repSetObject.setTYPE_NO(iTypeNo);
			repSetObject.setREPORT_SOURCE("����");
			repSetObject.setIS_PASSVERIFY("��");
			repSetObject.setIS_HASBATCH("��");
			repSetObject.setIS_MULTICOND("��");
			repSetObject.setIS_END(1);
			repSetObject.setTYPE_FLAG(IDefineReport.REPORTTYPE_COVER);
			repSetObject.setRG_CODE(Global.getCurrRegion());
			// ����������
			repSetObject.setLVL_ID(SzzbSetI.getMethod().getMaxCode(
					IQrBudget.LVL_ID));
			// ����ID
			repSetObject.setREPORT_ID(SzzbSetI.getMethod().getMaxCode(
					IQrBudget.REPORT_ID));
		}
		// ��������
		repSetObject.setREPORT_CNAME(ftxtTitle.getValue().toString());
		// ռ������
		repSetObject.setCOLUMN_AREA(ftxtConverArea.getValue().toString());
		// �û�����
		repSetObject.setDATA_USER(Integer.parseInt(reportUserTypeGrp.getValue()
				.toString()));
		// �Ƿ�����
		repSetObject.setIS_ACTIVE(("false".equals(fchkIsActice.getValue()
				.toString()) ? "��" : "��"));
	}

	/**
	 * �õ���������Ϣ
	 * 
	 */
	private void getSzzbInfo(DataSet dsSzzb) throws Exception {
		clearFlag = true;
		XMLData aData = new XMLData();// XXL �洢�ѷ��뵥Ԫ�񣬱����ظ�����
		// �ظ�����ʱ�ڸ�ԭչʾʱ���ᴴ�������ͬ�ĵ�Ԫ��
		CellSelection cells = ReportUtil.translateToNumber(ftxtConverArea
				.getValue().toString());
		CellElement cell;
		for (int i = cells.getColumn(); i < cells.getColumn()
				+ cells.getColumnSpan(); i++) {
			for (int j = cells.getRow(); j < cells.getRow()
					+ cells.getRowSpan(); j++) {
				cell = jWorkBook.getReport().getCellElement(i, j);
				if (cell == null)
					continue;
				this.addSzzb(cell, aData);
			}
		}
	}

	/**
	 * ���ӵ�Ԫ����Ϣ
	 * 
	 * @param cell
	 * @param aData
	 * @throws Exception
	 */
	private void addSzzb(CellElement cell, XMLData aData) throws Exception {
		int iCol = cell.getColumn();
		int iRow = cell.getRow();
		if (aData.containsKey(iCol + "-" + iRow))
			return;
		aData.put(iCol + "-" + iRow, null);
		this.addSzzb(cell);
	}

	private void addSzzb(CellElement cell) throws Exception {
		int iCol = cell.getColumn();
		int iRow = cell.getRow();
		dsSzzb.append();
		dsSzzb.fieldByName(ISzzbSet.SET_YEAR).setValue(Global.loginYear);
		dsSzzb.fieldByName(ISzzbSet.TYPE_NO).setValue(new Integer(iTypeNo));

		dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN).setValue(new Integer(iCol));
		dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMNSPAN).setValue(
				new Integer(cell.getColumnSpan()));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).setValue(new Integer(iRow));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROWSPAN).setValue(
				new Integer(cell.getRowSpan()));
		dsSzzb.fieldByName(ISzzbSet.HEADERBODY_FLAG).setValue(new Integer(2));

		Object oValue = jWorkBook.getReport().getCellValue(cell.getColumn(),
				cell.getRow());
		dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).setValue(oValue);

		dsSzzb.fieldByName(ISzzbSet.FIELD_COLWIDTH).setValue(
				new Double(jWorkBook.getReport().getColumnWidth(iCol)));
		dsSzzb.fieldByName(ISzzbSet.FIELD_ROWHEIGHT).setValue(
				new Double(jWorkBook.getReport().getRowHeight(iRow)));

		dsSzzb.fieldByName(ISzzbSet.FIELD_FONT).setValue(
				cell.getStyle().getFRFont().getFontName());
		dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSIZE).setValue(
				new Integer(cell.getStyle().getFRFont().getSize()));
		dsSzzb.fieldByName(ISzzbSet.FIELD_FONTSTYPE).setValue(
				new Integer(cell.getStyle().getFRFont().getStyle()));
		dsSzzb.fieldByName(ISzzbSet.HOR_ALIGNMENT).setValue(
				new Integer(cell.getStyle().getHorizontalAlignment()));
		dsSzzb.fieldByName(ISzzbSet.VER_ALIGNMENT).setValue(
				new Integer(cell.getStyle().getVerticalAlignment()));
		dsSzzb.fieldByName(IQrBudget.RG_CODE).setValue(Global.getCurrRegion());
	}

	/**
	 * ���ò���
	 * 
	 */
	private class FieldAreaActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if ("".equals(ftxtConverArea.getValue())) {
				JOptionPane.showMessageDialog(ConverSet.this, "��ѡ��ռ������!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (jWorkBook.getCellSelection() == null) {
				JOptionPane.showMessageDialog(ConverSet.this, "��ѡ��һ����Ԫ��",
						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			try {
				CellSelection cell = ReportUtil
						.translateToNumber(ftxtConverArea.getValue().toString());

				int iCol = jWorkBook.getCellSelection().getColumn();
				int iRow = jWorkBook.getCellSelection().getRow();
				int iColSpan = jWorkBook.getCellSelection().getColumnSpan();
				int iRowSpan = jWorkBook.getCellSelection().getRowSpan();
				if ((iCol > (cell.getColumn() + cell.getColumnSpan()))
						|| (iRow > (cell.getRow() + cell.getRowSpan()))) {
					JOptionPane.showMessageDialog(ConverSet.this, "������������",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (locateByFilter(dsSzzb, iCol, iRow)) {
						dsSzzb.edit();
						dsSzzb.fieldByName(ISzzbSet.FIELD_PARA).setValue(
								fcbxFieldPara.getValue());
					} else {
						CellElement cellEle = new CellElement(iCol, iRow,
								iColSpan, iRowSpan, "");
						jWorkBook.getReport().addCellElement(cellEle);
						addSzzb(cellEle);
						dsSzzb.fieldByName(ISzzbSet.FIELD_PARA).setValue(
								fcbxFieldPara.getValue());
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this, "���ò�������,������Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR);
			}
		}
	}

	private boolean locateByFilter(DataSet ds, int iCol, int iRow)
			throws Exception {
		if (ds == null)
			return false;
		ds.beforeFirst();
		while (ds.next()) {
			if (ds.fieldByName(ISzzbSet.FIELD_COLUMN).getInteger() == iCol
					&& ds.fieldByName(ISzzbSet.FIELD_ROW).getInteger() == iRow) {
				return true;
			}
		}
		return false;
	}

	private class reportMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int iCol = jWorkBook.getCellSelection().getColumn();
			int iRow = jWorkBook.getCellSelection().getRow();
			try {
				showFieldArea(iCol, iRow);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this,
						"��ʾ��Ԫ������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

	private class reportKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int iCol = jWorkBook.getCellSelection().getColumn();
			int iRow = jWorkBook.getCellSelection().getRow();
			if (e.getKeyCode() == KEY_UP && iRow != 0) {
				iRow = iRow - 1;
			}
			if (e.getKeyCode() == KEY_DOWN) {
				iRow = iRow + 1;
			}
			if (e.getKeyCode() == KEY_LEFT && iCol != 0) {
				iCol = iCol - 1;
			}
			if (e.getKeyCode() == KEY_RIGHT) {
				iCol = iCol + 1;
			}
			try {
				showFieldArea(iCol, iRow);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ConverSet.this,
						"��ʾ��Ԫ������������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * ��ʾ��Ԫ�����s
	 * 
	 */
	private void showFieldArea(int iCol, int iRow) throws Exception {
		if (locateByFilter(dsSzzb, iCol, iRow)) {
			fcbxFieldPara.setValue(dsSzzb.fieldByName(ISzzbSet.FIELD_PARA)
					.getValue());
		} else {
			fcbxFieldPara.setValue("");
		}
	}

}
