package gov.nbcs.rp.input.action;

import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.HeaderUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.input.ui.ReportUIProp;
import gov.nbcs.rp.input.ui.TFixComboBoxCellEditor;
import gov.nbcs.rp.input.ui.TFixTextWithButtonCellEditor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.util.Global;
import com.fr.cell.CellSelection;
import com.fr.report.PaperSize;
import com.fr.report.ReportConstants;




public class PrjInputDTO
{

	private DataSet dsHeader;

	private DataSet dsBody;

	private ReportUI reportUI;

	private Report report;

	// ��Ԫ������
	private ReportUIProp pp;

	private boolean isOneYearPrj;

	private boolean isCurYearBegin;

	private String xmxh;

	private boolean isHavSetAcct;

	// ����������ʾ��ʽ
	private TFixComboBoxCellEditor cbYSJC;
	// ��ȡ���ܿ�Ŀ
	private TFixTextWithButtonCellEditor btnAcct;

	private TFixTextWithButtonCellEditor btnAcctJJ;

	/** �û�ѡ��Ĺ��ܿ�Ŀ */
	private DataSet dsAcctSel;

	private DataSet dsAcctJJ;

	PrjAuditDTO dt = new PrjAuditDTO();



	public PrjInputDTO()
	{
	}


	/**
	 * ��ȡ��ͷ���ݼ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getDsHeader() throws Exception
	{
		if (dsHeader == null)
		{
			dsHeader = DataSet.createClient();
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("SB_TYPE");
			dsHeader.fieldByName("field_cname").setValue("����");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0001");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("YSJC_MC");
			dsHeader.fieldByName("field_cname").setValue("Ԥ�㼶��");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0002");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("ACCT_NAME");
			dsHeader.fieldByName("field_cname").setValue("���ܿ�Ŀ");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0003");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("ACCT_NAME_JJ");
			dsHeader.fieldByName("field_cname").setValue("���ÿ�Ŀ");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0004");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("TOTAL_SUM");
			dsHeader.fieldByName("field_cname").setValue("�ϼ�");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("0005");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("CQ");
			dsHeader.fieldByName("field_cname").setValue("����");
			dsHeader.fieldByName("field_type").setValue("");
			dsHeader.fieldByName("lvl_id").setValue("0006");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F1");
			dsHeader.fieldByName("field_cname").setValue("С��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00060001");
			dsHeader.fieldByName("par_id").setValue("CQ");

			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F2");
			dsHeader.fieldByName("field_cname").setValue("һ��Ԥ��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00060002");
			dsHeader.fieldByName("par_id").setValue("CQ");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F3");
			dsHeader.fieldByName("field_cname").setValue("����Ԥ��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00060003");
			dsHeader.fieldByName("par_id").setValue("CQ");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F6");
			dsHeader.fieldByName("field_cname").setValue("����");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00060004");
			dsHeader.fieldByName("par_id").setValue("CQ");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("CP");
			dsHeader.fieldByName("field_cname").setValue("�ϼ�");
			dsHeader.fieldByName("field_type").setValue("");
			dsHeader.fieldByName("lvl_id").setValue("0009");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F7");
			dsHeader.fieldByName("field_cname").setValue("С��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00090001");
			dsHeader.fieldByName("par_id").setValue("CP");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F8");
			dsHeader.fieldByName("field_cname").setValue("һ��Ԥ��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00090002");
			dsHeader.fieldByName("par_id").setValue("CP");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F9");
			dsHeader.fieldByName("field_cname").setValue("����Ԥ��");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00090003");
			dsHeader.fieldByName("par_id").setValue("CP");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("F10");
			dsHeader.fieldByName("field_cname").setValue("����");
			dsHeader.fieldByName("field_type").setValue("������");
			dsHeader.fieldByName("lvl_id").setValue("00090004");
			dsHeader.fieldByName("par_id").setValue("CP");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("BZ");
			dsHeader.fieldByName("field_cname").setValue("��ע");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0010");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.append();
			dsHeader.fieldByName("field_ename").setValue("IS_IDX");
			dsHeader.fieldByName("field_cname").setValue("");
			dsHeader.fieldByName("field_type").setValue("�ַ���");
			dsHeader.fieldByName("lvl_id").setValue("0011");
			dsHeader.fieldByName("par_id").setValue("");
			dsHeader.applyUpdate();
		}
		return dsHeader;
	}


	public void setDsHeader(DataSet dsHeader)
	{
		this.dsHeader = dsHeader;
	}


	public DataSet getDsBody()
	{
		return dsBody;
	}


	public void setDsBody(DataSet dsBody)
	{
		this.dsBody = dsBody;
	}



	private int state = -1; // -1:��� 0������ 1���޸�



	public int getCurState()
	{
		return state;
	}


	public void setCurState(int state)
	{
		this.state = state;
	}


	/**
	 * ��ȡ�����׼ֵ��reportUI
	 * 
	 * @return
	 */
	public ReportUI getReportUI()
	{
		try
		{
			// ���reportUIΪ�ղŴ���
			if (reportUI == null)
			{
				reportUI = new ReportUI(getReport());
				// ����
				reportUI.getReport().getReportSettings().setPaperSize(new PaperSize(2000, 3000));
				// ȡ������������Ϣ
				reportUI.clearColumnHeaderContent();
				// ��������
				reportUI.setColumnEndless(false);
				reportUI.setRowEndless(false);
				// ����reportUI��صľ�������
				refreshReportUI();
				// ��reportUI�Ӽ����¼����۽�������
				reportUI.getGrid().addFocusListener(new TGridFocusListener());
				reportUI.getGrid().addKeyListener(new TKeyListener());
				reportUI.getGrid().addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						calData();
					}
				});
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee.getMessage());
		}
		return reportUI;
	}


	/**
	 * ��ȡreport
	 * 
	 * @return
	 */
	private Report getReport()
	{
		try
		{
			// ���Ϊ�ղŴ���
			if (report == null)
			{
				TableHeader tableHeader = null;
				DataSet dsValue = null; // �����׼ֵ
				report = new Report(tableHeader, dsValue, pp);
				// ��������
				report.setCellProperty(pp);
				// ���ñ���ɫ
				// report.setReadOnlyBackground(new Color(0xe8f2fe));
				report.maskZeroValue(true);
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee.getMessage());
		}
		return report;
	}


	/**
	 * ˢ����Ŀ��ϸ�� j
	 * 
	 * @throws Exception
	 */
	public void refreshReportUI() throws Exception
	{
		if (reportUI == null)
			return;
		dsHeader = this.getDsHeader();
		dsBody = this.getDsBody();
		// ����reportUI��Ӧ��������
		if (pp == null)
			pp = new ReportUIProp(this);
		// �ػ�reportUI
		reportUI.getReport().removeAllCellElements();
		reportUI.repaint();
		// ������ͷ
		TableHeader tableHeader = HeaderUtility.createHeader(getDsHeader(), "field_ename", "field_cname", "PAR_ID", "lvl_id");
		report.setReportHeader(tableHeader);
		// ���ò��ɱ༭��Ԫ����ɫ
		// report.setReadOnlyBackground(new Color(0xe8f2fe));
		// report.maskZeroValue(true);
		// ���ö�����
		reportUI.setFrozenRow(tableHeader.getRows());
		reportUI.getReport().setRowHeight(0, 30);
		reportUI.getReport().setRowHeight(1, 30);
		reportUI.getGrid().setEnterMoveDirection(ReportConstants.MOVE_DIRECTION_RIGHT);
		setColumnWidth();
		// ���ñ������ݼ�
		report.setBodyData(dsBody);
		// ����������
		report.setCellProperty(pp);
		// ˢ�±���
		report.refreshBody();
		report.getUI().validate();
		// reportUI.setReport(report);
		// ���ñ�ͷ��Ԫ���ʽ
		// ��������Ӧ�п�
		// report.autoAdjustColumnWidth();
		// �����п�
		// reportUI.getReport().setColumnWidth(0, 0);
		// reportUI.setFrozenColumn(5);
		// ˢ��
		reportUI.repaint();
		reportUI.updateUI();
	}


	/**
	 * �����п�
	 * 
	 */
	private void setColumnWidth()
	{
		reportUI.getReport().setColumnWidth(0, 50);
		reportUI.getReport().setColumnWidth(1, 40);
		reportUI.getReport().setColumnWidth(2, 140);
		reportUI.getReport().setColumnWidth(3, 140);
		reportUI.getReport().setColumnWidth(4, 55);
		reportUI.getReport().setColumnWidth(5, 55);
		reportUI.getReport().setColumnWidth(6, 55);
		reportUI.getReport().setColumnWidth(7, 55);
		reportUI.getReport().setColumnWidth(8, 55);
		reportUI.getReport().setColumnWidth(9, 55);
		reportUI.getReport().setColumnWidth(10, 55);
		reportUI.getReport().setColumnWidth(11, 55);
		reportUI.getReport().setColumnWidth(12, 55);
		reportUI.getReport().setColumnWidth(13, 200);
		reportUI.getReport().setColumnWidth(14, 0);
		// reportUI.getReport().setColumnWidth(14, 350);
	}



	/**
	 * report��λ����
	 */
	class TGridFocusListener implements FocusListener
	{
		Cell cell;

		Report report;



		public void focusGained(FocusEvent arg0)
		{
			// ����¼��״̬���޸ļ�¼
			report = (Report) reportUI.getWorkSheet();
			// ��ȡ�༭��Ԫ��
			cell = (Cell) report.getSelectedCell();
			if (getCurState() == 1)
				modData(reportUI, report, dsBody, cell);
		}


		public void focusLost(FocusEvent arg0)
		{
			report = (Report) reportUI.getWorkSheet();
			// ��ȡѡ��Ԫ��
			cell = (Cell) report.getSelectedCell();
		}
	}

	/**
	 * ����delete ����ļ���
	 */
	class TKeyListener implements KeyListener
	{
		public void keyReleased(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) // deleteɾ����
			{
				CellSelection cells = reportUI.getCellSelection();
				// ���ѡ������򲻺Ϸ����򷵻�
				if (cells.getRowSpan() > 1 || cells.getColumnSpan() > 1)
				{
					e.setKeyCode(0);
					return;
				}
				// ����¼��״̬���޸ļ�¼
				try
				{
					// ֻ�б༭״̬�£����ݲ���������
					if (getCurState() == 1)
					{
						Report report = (Report) reportUI.getWorkSheet();
						Cell cell = (Cell) report.getSelectedCell();
						cell.setValue("");
						// ������������
						modData(reportUI, report, dsBody, cell);
					}
				}
				catch (Exception ex)
				{
					ErrorInfo.showErrorDialog(ex.getMessage());
				}
			}
		}


		public void keyTyped(KeyEvent e)
		{
		}


		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_DELETE) // deleteɾ����
			{
				CellSelection cells = reportUI.getCellSelection();
				// ���ѡ������򲻺Ϸ����򷵻�
				if (cells.getRowSpan() > 1 || cells.getColumnSpan() > 1)
				{
					e.setKeyCode(0);
					return;
				}
				// ����¼��״̬���޸ļ�¼
				try
				{
					if (getCurState() == 1)
					{
						Report report = (Report) reportUI.getWorkSheet();
						Cell cell = (Cell) report.getSelectedCell();
						modData(reportUI, report, dsBody, cell);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}



	/**
	 * �޸ĵ�Ԫ������
	 * 
	 * @param reportUI
	 * @param report
	 * @param dsBody
	 * @param cell
	 */
	public void modData(ReportUI reportUI, Report report, DataSet dsBody, Cell cell)
	{
		try
		{
			if (this.getCurState() == -1)
				return;
			// ��ͷ�в�������
			if (cell.getRow() <= report.getReportHeader().getRows() - 1) { return; }
			// ����ǰ1�в�������
			if (cell.getColumn() < 1) { return; }
			InfoPackage infopackage = new InfoPackage();
			// �޸ĵ�Ԫ������
			infopackage = mod_colinfo(dsBody, cell);
			// if (isMoneyField(cell.getFieldName(), cell))
			calData();
			// ����޸Ĳ��ɹ����򷵻ص���ѡ��Ԫ��
			if (!infopackage.getSuccess())
			{
				CellSelection cellsel = new CellSelection(cell.getColumn(), cell.getRow(), cell.getColumn(), cell.getRow(), cell.getColumnSpan(), cell.getRowSpan());
				reportUI.setCellSelection(cellsel);
				reportUI.repaint();
				return;
			}
			// �޸ĳɹ���ˢ�½���
			reportUI.repaint();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * ���õ�Ԫ���޸ı�־
	 * 
	 * @param cell
	 * @return
	 */
	public InfoPackage mod_colinfo(DataSet dsBody, Cell cell)
	{
		InfoPackage infopackage = new InfoPackage();
		try
		{
			if (cell.getRow() <= report.getReportHeader().getRows() - 1) { return infopackage; }
			// ���ݼ�����༭״̬
			dsBody.edit();
			// ���ݵ�Ԫ��λ�����ݼ���Ӧ�ļ�¼
			if (!dsBody.gotoBookmark(cell.getBookmark()))
				return infopackage;
			Object cellValue = cell.getValue() == null ? "0" : cell.getValue();
			// �ѵ�Ԫ���޸ĵļ�¼���浽���ݼ���
			dsBody.fieldByName(cell.getFieldName()).setValue(new Float(Common.nonNullStr(cellValue)));
			// ���ø�����¼�༭״̬Ϊ�ѱ༭
			dsBody.fieldByName("MODFLAG").setValue("1");
		}
		catch (Exception ex)
		{
			infopackage.setsMessage(ex.getMessage());
			infopackage.setSuccess(false);
			return infopackage;
		}
		infopackage.setSuccess(true);
		return infopackage;
	}


	// private boolean isMoneyField(String field, Cell cell) {
	// String ib = field.substring(0, 1);
	// String ie = field.substring(1);
	// if ("F".equalsIgnoreCase(ib)) {
	// if (Common.isNumber(ie)) {
	// if (this.isOneYearPrj) {
	// if (cell.getRow() > 3 && cell.getColumn() > 4
	// && cell.getColumn() < 9) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// if (cell.getRow() > 3 && cell.getColumn() > 4
	// && cell.getColumn() < 9) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// } else {
	// return false;
	// }
	// } else {
	// return false;
	// }
	// }

	// public boolean isMoneyField(String field) {
	// if (!Common.isNullStr(field) && field.length() > 1) {
	// String ib = field.substring(0, 1);
	// String ie = field.substring(1);
	// if ("F".equalsIgnoreCase(ib)) {
	// if (Common.isNumber(ie)) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// return false;
	// }
	// } else
	// return false;
	// }

	public void calData()
	{
		// �кϼƼ���
		if (this.state == -1)
			return;
		String iv = "", ivv = "", iv1 = "";
		Cell cell = null;
		DataSet ds = ((Report) reportUI.getReport()).getBodyData();

		try
		{
			if (StringUtils.isNotEmpty(ds.fieldByName("is_idx").getString()))
				return;
		}
		catch (Exception e1)
		{
			return;
		}
		for (int col = 5; col < 13; col++)
		{
			// �ϼ��м���(��Ԥ�㡢����Ԥ�㡢�Ѱ�����)
			float value = 0;
			double value2 = 0;// �Ѱ�����
			for (int row = 5; row < reportUI.getReport().getRowCount(); row++)
			{
				iv = Common.nonNullStr(reportUI.getReport().getCellElement(col, row).getValue());
				if (Common.isNullStr(iv))
					continue;
				iv1 = Common.nonNullStr(reportUI.getReport().getCellElement(14, row).getValue());
				if (!Common.isNullStr(iv1))
					continue;
				value += Double.parseDouble(iv);
			}
			cell = (Cell) report.getCellElement(col, 4);

			try
			{
				ds.edit();
				if (ds.gotoBookmark(cell.getBookmark()))
				{
					BigDecimal bg = new BigDecimal(value);
					ds.fieldByName(cell.getFieldName()).setValue(bg);
					// reportUI.getReport().getCellElement(col, 4).setValue(
					// new Double(value));
				}
				// if (isOneYearPrj)
				// {
				ivv = Common.nonNullStr(reportUI.getReport().getCellElement(col, 3).getValue());

				if (Common.isNullStr(ivv))
					value2 = 0;
				else
					value2 = Double.parseDouble(ivv);

				cell = (Cell) report.getCellElement(col, 2);

				if (ds.gotoBookmark(cell.getBookmark()))
				{
					BigDecimal bg = new BigDecimal(value + value2);
					ds.fieldByName(cell.getFieldName()).setValue(bg);
					// reportUI.getReport().getCellElement(col, 2).setValue(
					// new Double(value));
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 2; i < reportUI.getReport().getRowCount(); i++)
		{
			// �ϼ��м��㣨�ϼ��С�С���У�
			float value1 = 0;
			float value2 = 0;
			for (int j = 6; j < 9; j++)
			{
				iv = Common.nonNullStr(reportUI.getReport().getCellElement(j, i).getValue());
				if (Common.isNullStr(iv))
					continue;
				value1 += Double.parseDouble(iv);
			}
			for (int j = 10; j < 13; j++)
			{
				iv = Common.nonNullStr(reportUI.getReport().getCellElement(j, i).getValue());
				if (Common.isNullStr(iv))
					continue;
				value2 += Double.parseDouble(iv);
			}
			try
			{

				//		

				cell = (Cell) report.getCellElement(5, i);

				if (ds.gotoBookmark(cell.getBookmark()))
				{
					BigDecimal bg = new BigDecimal(value1);
					ds.fieldByName(cell.getFieldName()).setValue(bg);
					// reportUI.getReport().getCellElement(5, i).setValue(
					// Common.nonNullStr(new Double(value1)));
				}
				cell = (Cell) report.getCellElement(9, i);

				if (ds.gotoBookmark(cell.getBookmark()))
				{
					BigDecimal bg = new BigDecimal(value2);
					ds.fieldByName(cell.getFieldName()).setValue(bg);
					// reportUI.getReport().getCellElement(9, i).setValue(
					// Common.nonNullStr(new Double(value2)));
				}

				cell = (Cell) report.getCellElement(4, i);

				if (ds.gotoBookmark(cell.getBookmark()))
				{
					BigDecimal bg = new BigDecimal(value1 + value2);
					ds.fieldByName(cell.getFieldName()).setValue(bg);
					// reportUI.getReport().getCellElement(4, i).setValue(
					// Common.nonNullStr(new Double(value1 + value2)));
				}
			}
			catch (Exception e)
			{
				ErrorInfo.showErrorDialog(e.getMessage());
			}
		}
	}


	public boolean isOneYearPrj()
	{
		return isOneYearPrj;
	}


	public void setOneYearPrj(boolean isOneYearPrj)
	{
		this.isOneYearPrj = isOneYearPrj;
	}


	/**
	 * ��ȡ����(��������)��ʾ���ͱ༭��
	 * 
	 * @return
	 * @throws Exception
	 */
	public TFixComboBoxCellEditor getYSJCFormat() throws Exception
	{
		if (cbYSJC == null)
		{
			DataSet ds = getdsYSJC();
			String[] listName = getLstDataType(ds);
			cbYSJC = new TFixComboBoxCellEditor(reportUI, listName, dsBody, ds, "YSJC_MC", "YSJC_MC");
		}
		return cbYSJC;
	}


	/**
	 * ��ȡ�������༭��
	 * 
	 * @return
	 * @throws Exception
	 */
	public TFixTextWithButtonCellEditor getAcctInfo() throws Exception
	{
		String initName = "ACCT_NAME";
		String initValue = "";
		if (dsBody != null && dsBody.getRecordCount() > 0)
		{
			initValue = dsBody.fieldByName("ACCT_NAME").getString();
		}
		btnAcct = new TFixTextWithButtonCellEditor(0, 200,
		// ------------DIALOG-----------------//
				"��ѡ���ܿ�Ŀ", // �Ի������
				"��Ŀ��Ϣ",// ���������
				this.getDsAcctSel(), "ACCT_NAME",// �����ID
				"ACCT_NAME", // �����NAME
				null, // ����㸸ID
				1,// ������Ƿ�ѡ
				null,// �����������
				// ------------FINEREPORT -----------------//
				1, dsBody,// fineReport��ǰ�е����ݼ�
				"ACCT_NAME",// fineReport��ǰ�е�ID
				"ACCT_NAME",// fineReport��ǰ�е�NAME
				initName,// �����ID
				initValue,// �����ID
				getReportUI(),// ��ˢ�µı��
				this);
		return btnAcct;
	}


	/**
	 * ��ȡ�������༭��
	 * 
	 * @return
	 * @throws Exception
	 */
	public TFixTextWithButtonCellEditor getAcctJJInfo() throws Exception
	{
		String initName = "ACCT_NAME_JJ";
		String initValue = "";
		if (dsBody != null && dsBody.getRecordCount() > 0)
		{
			initValue = dsBody.fieldByName("ACCT_NAME_JJ").getString();
		}
		btnAcctJJ = new TFixTextWithButtonCellEditor(1, 200,
		// ------------DIALOG-----------------//
				"��ѡ���ܿ�Ŀ", // �Ի������
				"��Ŀ��Ϣ",// ���������
				this.getDsAcctJJ(), "ACCT_NAME_JJ",// �����ID
				"ACCT_NAME_JJ", // �����NAME
				null, // ����㸸ID
				1,// ������Ƿ�ѡ
				null,// �����������
				// ------------FINEREPORT -----------------//
				1, dsBody,// fineReport��ǰ�е����ݼ�
				"ACCT_NAME_JJ",// fineReport��ǰ�е�ID
				"ACCT_NAME_JJ",// fineReport��ǰ�е�NAME
				initName,// �����ID
				initValue,// �����ID
				getReportUI(),// ��ˢ�µı��
				this);
		return btnAcctJJ;
	}


	/**
	 * ��ȡ��������ʾ���ݼ�
	 * 
	 * @return
	 * @throws Exception
	 */
	private DataSet getdsYSJC() throws Exception
	{
		String sql = "select chr_code as code,chr_name as name, chr_id as  ysjc_bm from dm_ysjc where set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion()
				+ " order by chr_code";
		return DBSqlExec.client().getDataSet(sql);
	}


	/**
	 * ��ȡ���ݼ��е��ֶ�ֵ�Ž�������
	 * 
	 * @param ds
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private String[] getLstDataType(DataSet ds) throws Exception
	{
		String names[] = new String[ds.getRecordCount()];
		int i = 0;
		ds.beforeFirst();
		while (ds.next())
		{
			String name = ds.fieldByName("NAME").getString();
			names[i++] = name;
		}
		return names;
	}


	public String getXmxh()
	{
		return xmxh;
	}


	public void setXmxh(String xmxh)
	{
		this.xmxh = xmxh;
	}


	public boolean isHavSetAcct()
	{
		return isHavSetAcct;
	}


	public void setHavSetAcct(boolean isHavSetAcct)
	{
		this.isHavSetAcct = isHavSetAcct;
	}


	public DataSet getDsAcctSel()
	{
		return dsAcctSel;
	}


	public void setDsAcctSel()
	{
		StringBuffer sql = new StringBuffer();
		if (isHavSetAcct)
		{
			// ����Ѿ������˶�Ӧ��ϵ����ֱ��ȥ��Ӧ��ϵ��Χ�µĿ�Ŀ
			sql.append("select a.SET_YEAR, BS_ID,ACCT_CODE,IS_LEAF, PARENT_ID,");
			sql.append(" '[' || ACCT_CODE || ']' || ACCT_NAME as acct_name");
			sql.append(" from vw_fb_acct_func a,rp_xmjl_km b");
			sql.append(" where a.BS_ID = b.kmdm");
			sql.append(" and a.set_Year = b.set_year");
			sql.append(" and b.xmxh = '" + xmxh + "'");
			sql.append(" and a.set_year = " + Global.loginYear);
			sql.append(" order by acct_code");
		}
		else
		{
			// ���û�����ö�Ӧ��ϵ�������ѡ�����п�Ŀ
			sql.append("select SET_YEAR,BS_ID,ACCT_CODE,");
			sql.append("'['||ACCT_CODE||']'|| ACCT_NAME as acct_name,");
			sql.append(" IS_LEAF,PARENT_ID from vw_fb_acct_func where set_year = " + Global.loginYear);
			sql.append(" order by acct_code");
		}
		try
		{
			dsAcctSel = DBSqlExec.client().getDataSet(sql.toString());
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e.getMessage());
		}
	}


	public void setDsAcctSel(String[] acctCodes)
	{
		if (acctCodes != null && acctCodes.length > 0)
		{
			String filter = getFilter("acct_code", acctCodes);
			StringBuffer sql = new StringBuffer();
			// ���û�����ö�Ӧ��ϵ�������ѡ�����п�Ŀ
			sql.append("select SET_YEAR,BS_ID,ACCT_CODE,");
			sql.append("'['||ACCT_CODE||']'|| ACCT_NAME as acct_name,");
			sql.append(" IS_LEAF,PARENT_ID from vw_fb_acct_func");
			sql.append(" where set_year = " + Global.loginYear + " and " + filter);
			sql.append(" order by acct_code");
			try
			{
				dsAcctSel = DBSqlExec.client().getDataSet(sql.toString());
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			return;
		}
	}


	private String getFilter(String tag, String[] values)
	{
		StringBuffer filter = new StringBuffer();
		if (!Common.isNullStr(tag) && values != null && values.length > 0)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (i == 0)
					filter.append(tag + " in ('" + values[i] + "'");
				else
					filter.append(",'" + values[i] + "'");
			}
			filter.append(")");
		}
		return filter.toString();

	}


	public DataSet getDsAcctJJ()
	{
		if (dsAcctJJ == null)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select SET_YEAR,BSI_ID,ACCT_CODE_JJ,");
			sql.append("'['||ACCT_CODE_JJ||']'|| ACCT_NAME_JJ as acct_name_jj,");
			sql.append(" IS_LEAF,PARENT_ID from VW_FB_ACCT_ECONOMY ");
			sql.append("order by acct_code_JJ");
			try
			{
				dsAcctJJ = DBSqlExec.client().getDataSet(sql.toString());
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dsAcctJJ;
	}


	public void setDsAcctJJ(DataSet dsAcctJJ)
	{
		this.dsAcctJJ = dsAcctJJ;
	}


	/**
	 * ��ȡ��Ŀ��ǰ���״̬
	 * 
	 * @param xmxh
	 * @return
	 */
	public int getAuditStepByPrjCode(String xmxh)
	{
		if (!GlobalEx.isFisVis())
			return 0;
		else
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select nvl(audit_state,0) From rp_audit_cur a ");
			sql.append(" where set_year = " + Global.loginYear);
			sql.append(" and rg_code = " + Global.getCurrRegion());
			sql.append(" and exists(");
			sql.append(" select 1 from rp_xmjl b");
			sql.append(" where a.prj_code = b.xmbm");
			sql.append(" and a.set_year = b.set_year");
			sql.append(" and a.rg_code = b.rg_code");
			sql.append(" and b.xmxh = '" + xmxh + "'");
			sql.append(")");
			try
			{
				return DBSqlExec.client().getIntValue(sql.toString());
			}
			catch (Exception ee)
			{
				return 0;
			}
		}
	}


	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @param xmxh
	 * @return
	 */
	public int getBatchNoByPrjCode(String xmxh)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select batch_no From rp_prjbatch a");
		sql.append(" where set_year = " + Global.loginYear);
		sql.append(" and rg_code = " + Global.getCurrRegion());
		sql.append(" and exists(");
		sql.append(" select 1 from rp_xmjl b");
		sql.append(" where a.prj_code = b.xmbm");
		sql.append(" and a.set_year = b.set_year");
		sql.append(" and a.rg_code = b.rg_code");
		sql.append(" and b.xmxh = '" + xmxh + "'");
		sql.append(")");
		try
		{
			return DBSqlExec.client().getIntValue(sql.toString());
		}
		catch (Exception ee)
		{
			return 0;
		}
	}


	/**
	 * ������Ŀ���룺��λ���+��Ŀ���ƴ��+��ˮ��(000+order)
	 * 
	 * @return
	 */
	public String getNewPrjCode(String div_code)
	{
		// String pinyin = "";
		// String[] firstChar = Pinyin.getFirstChineseChar(""
		// + xmfl, 4);
		// for (int i = 0; i < firstChar.length; i++) {
		// pinyin += firstChar[i];
		// }
		String order = "";
		try
		{
			order = String.valueOf(PrjInputStub.getMethod().getPrjCount(div_code) + 1);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "����ר�����ʧ�ܣ�");
			return "";
		}
		int orderLength = order.length();
		switch (orderLength)
		{
			case 1:
				order = "000" + order;
				break;
			case 2:
				order = "00" + order;
				break;
			case 3:
				order = "0" + order;
				break;
			default:
				break;
		}
		// String curYear = Global.loginYear;
		// String kind = "";
		// // ����ǲ����û������ʾC�������ʾD
		// if (GlobalEx.isFisVis()) {
		// kind = "C";
		// } else {
		// kind = "D";
		// }
		// return div_code + pinyin + curYear + kind + order;
		return div_code + order;
		// return div_code + curYear + kind + order;
	}


	public String getNewPrjCodeXmsh(String div_code, int i)
	{
		// String pinyin = "";
		// String[] firstChar = Pinyin.getFirstChineseChar(""
		// + xmfl, 4);
		// for (int i = 0; i < firstChar.length; i++) {
		// pinyin += firstChar[i];
		// }
		String order = "";
		try
		{
			order = String.valueOf(PrjInputStub.getMethod().getPrjCount(div_code) + 1);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "����ר�����ʧ�ܣ�");
			return "";
		}
		int orderLength = order.length();
		switch (orderLength)
		{
			case 1:
				order = "000" + order;
				break;
			case 2:
				order = "00" + order;
				break;
			case 3:
				order = "0" + order;
				break;

			default:
				break;
		}
		// String curYear = Global.loginYear;
		// String kind = "";
		// // ����ǲ����û������ʾC�������ʾD
		// if (GlobalEx.isFisVis()) {
		// kind = "C";
		// } else {
		// kind = "D";
		// }
		// return div_code + pinyin + curYear + kind + order;
		// return div_code + curYear + kind + order;
		return div_code + order;
	}


	public boolean isCurYearBegin()
	{
		return isCurYearBegin;
	}


	public void setCurYearBegin(boolean isCurYearBegin)
	{
		this.isCurYearBegin = isCurYearBegin;
	}

}
