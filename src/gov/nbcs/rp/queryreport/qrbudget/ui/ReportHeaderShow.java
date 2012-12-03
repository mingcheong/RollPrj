package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

//import gov.nbcs.rp.basinfo.common.filtertable.FilterTableHeaderRender;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.qrbudget.action.SaveColWidthAction;
import gov.nbcs.rp.queryreport.qrbudget.action.SetWhereReadWrite;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.ColDispInf;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget.ToolBarPanel;

import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import gov.nbcs.rp.sys.sysiaestru.ui.SetActionStatus;
import com.foundercy.pf.control.table.ColumnGroup;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.report.cellElement.CellGUIAttr;

/**
 * ��ѯ���ͷ��ʾ
 * 
 * @author qzc
 * 
 */
public class ReportHeaderShow {
	private QrBudget qrBudget;

	public static String PAPER_SIZE = "PAPER_SIZE"; // ֽ�Ŵ�С

	public static String PRINT_ORIENT = "PRINT_ORIENT"; // ֽ�ŷ���

	public static String MARGIN_LEFT = "MARGIN_LEFT"; // ��߾�

	public static String MARGIN_RIGHT = "MARGIN_RIGHT"; // �ұ߾�

	public static String MARGIN_TOP = "MARGIN_TOP"; // �ϱ߾�

	public static String MARGIN_BOTTOM = "MARGIN_BOTTOM"; // �±߾�

	public ReportHeaderShow(QrBudget qrBudget) {
		this.qrBudget = qrBudget;

	}

	public void show(ReportInfo reportInfo) {
		try {
			Global.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (reportInfo == null)
				return;

			// ����ToolBar���״̬
			setTooBarState(reportInfo);

			// ��ʾ������Ϣ
			showHeader(reportInfo);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����ʾ�����ͷ�������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} finally {
			Global.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	/**
	 * ��ʾ��ͷ
	 * 
	 * @param dsRepset
	 * @throws Exception
	 */
	public void showHeader(ReportInfo reportInfo) throws Exception {

		if (reportInfo.getBussType() == QrBudget.TYPE_COVER) {// ����
			// ���ù̶���
			qrBudget.getReportUI().setFrozenRow(0);
			// ��ʾ����
			showCover(reportInfo);
			// ����ԭ�����п�
			SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
					.getReport(), reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {// �б���
			Report curReport = (Report) qrBudget.getReportUI().getReport();
			qrBudget
					.setCurReport((gov.nbcs.rp.common.ui.report.Report) ReportFactory
							.getRowSetHeaderByExists(curReport, reportInfo));
			qrBudget.getReportUI().setReport(curReport);

		} else if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// ��֧�ܱ��ʽ
			Report curReport = (Report) qrBudget.getReportUI().getReport();
	
		
			qrBudget.getReportUI().setReport(curReport);
		SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
					.getReport(), reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
				|| reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {// ���鱨��,���ⱨ��

			showNormalHeader(reportInfo.getReportID());

		}

		// �̶���
		int iFreezeCol = Integer.parseInt(qrBudget.getFpnlToolBar()
				.getJspFrozenColumn().getValue().toString());
		qrBudget.getReportUI().freezeColumn(iFreezeCol);

		qrBudget.getReportUI().updateUI();
		qrBudget.getReportUI().repaint();

		// if (reportInfo.getBussType() == QrBudget.TYPE_COVER
		// || reportInfo.getBussType() == QrBudget.TYPE_ROWSET
		// || reportInfo.getBussType() == QrBudget.TYPE_SZZB) {
		// qrBudget.getReportUI().freezeColumn(iFreezeCol);
		//
		// qrBudget.getReportUI().updateUI();
		// qrBudget.getReportUI().repaint();
		// } else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
		// || reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
		// qrBudget.getFtabReport().lockColumns(iFreezeCol);
		// }

	}

	/**
	 * ��ʾ����
	 */
	private void showCover(ReportInfo reportInfo) throws Exception {
		Report curReport = (Report) qrBudget.getReportUI().getReport();


	}

	public static void setCover(Report report, DataSet dsSzzb) throws Exception {
		int iColumn;
		int iColumnSpan;
		int iRow;
		int iRowSpan;
		Object oValue;
		Cell cellElement;
		CellGUIAttr cellGUIAttr = new CellGUIAttr();
		cellGUIAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);

		dsSzzb.beforeFirst();
	
	}

	/**
	 * ���ݱ�ͷDdataSet�õ������ѯ�ֶ����
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getFieldSelect(DataSet dsReportHeader) throws Exception {
		String sFieldSelect = "";
		dsReportHeader.beforeFirst();
		while (dsReportHeader.next()) {
			if (dsReportHeader.fieldByName(IQrBudget.IS_LEAF).getInteger() == 0)
				continue;
			if ("".equals(sFieldSelect))
				sFieldSelect = dsReportHeader
						.fieldByName(IQrBudget.FIELD_ENAME).getString();
			else
				sFieldSelect = sFieldSelect
						+ ","
						+ dsReportHeader.fieldByName(IQrBudget.FIELD_ENAME)
								.getString();
		}
		return sFieldSelect;
	}

	/**
	 * ��ʾ�����ͷ(���������ⱨ��
	 * 
	 */
	public static void showNormalHeader(DataSet dsReportHeader,
			QrBudget qrBudget) throws Exception {

		// �ӽڵ�,��ʾ��ͷ
		qrBudget.setDsReportHeader(dsReportHeader);

		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		qrBudget.setNodeHeader(hg.generate(qrBudget.getDsReportHeader(),
				IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
				BesAct.codeRule_Col, IQrBudget.FIELD_CODE));

		// �����ͷ���������
		TableHeader tableHeader = new TableHeader(qrBudget.getNodeHeader());
		tableHeader.setColor(UntPub.HEADER_COLOR);
		tableHeader.setFont(qrBudget.getFont());
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		int iEndRepeatRow = curReport.getBodyRowCount()
				+ curReport.getReportHeader().getRows();
		curReport.removeAllCellElements();
		for (int i = 0; i < iEndRepeatRow; i++)
			curReport.setRowHeight(i, 20);
		curReport.setReportHeader(tableHeader);

		// ���ñ����п����ݿ��б�����п��ֵ,������ʾ���ͺ���ʾ��ʽ
		List lstFields = tableHeader.getFields();

		for (int i = 0; i < lstFields.size(); i++) {
			if (!qrBudget.getDsReportHeader().locate(IQrBudget.FIELD_CODE,
					lstFields.get(i).toString())) {
				JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����ʾ�����ͷ�п�������!",
						"��ʾ", JOptionPane.ERROR_MESSAGE);
				break;
			}

			if (qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_DISPWIDTH).getValue() != null) {
				curReport.setColumnWidth(i, qrBudget.getDsReportHeader()
						.fieldByName(IQrBudget.FIELD_DISPWIDTH).getDouble());
			}
		}

		curReport.shrinkToFitRowHeight();
		int iSZZBTitleRows = curReport.getReportHeader().getRows();
		qrBudget.getReportUI().setFrozenRow(iSZZBTitleRows);

	}

	/**
	 * ��ʾ�����ͷ(���������ⱨ��
	 * 
	 */
	private void showNormalHeader(String sReportId) throws Exception {
		List lstCols = new ArrayList();// ��¼����Ϣ

		DataSet dsReportHeader = qrBudget.getQrBudgetServ().getReportHeader(
				sReportId, Global.loginYear);

		// ��ͷ���ݣ������ʽ���Դ�ͶԱȷ���ʱ�������ز�����Ϣ
		SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(sReportId);
		if (setWhereReadWrite.isReportExists()) {
			if (setWhereReadWrite.getPfsFname() != null)
				// �ı��ͷ�ʽ���Դ��ʾ
				dsReportHeader = ReportHeaderOpe.changeDataSource(
						dsReportHeader, setWhereReadWrite.getPfsFname());
		}

		// �ӽڵ�,��ʾ��ͷ
		qrBudget.setDsReportHeader(dsReportHeader);

		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		qrBudget.setNodeHeader(hg.generate(qrBudget.getDsReportHeader(),
				IQrBudget.FIELD_CODE, IQrBudget.FIELD_CNAME,
				BesAct.codeRule_Col, IQrBudget.FIELD_CODE));

		// �����ͷ���������
		TableHeader tableHeader = new TableHeader(qrBudget.getNodeHeader());
		tableHeader.setColor(UntPub.HEADER_COLOR);
		tableHeader.setFont(qrBudget.getFont());
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		int iEndRepeatRow = curReport.getBodyRowCount()
				+ curReport.getReportHeader().getRows();
		curReport.removeAllCellElements();
		for (int i = 0; i < iEndRepeatRow; i++)
			curReport.setRowHeight(i, 20);
		curReport.setReportHeader(tableHeader);

		// ���ñ����п����ݿ��б�����п��ֵ,������ʾ���ͺ���ʾ��ʽ
		List lstFields = tableHeader.getFields();

		// add by xxl �����������⣺��Щ������������ʱ�������ڵ����ʺϸ߶Ȼ���ֹ�����и�
		// ����������������ɵģ���������Ҫ��Ӵ����Ƚ����������ó�һ���ϴ�Ŀ�ȣ�����¼���������
		// �������ø߶Ⱥ��ٻָ���0�Ŀ�� 20090823

		int iFieldDispwidth[] = new int[lstFields.size()]; // ��ʾ�п�
		Map mapZeroCol = new HashMap();

		for (int i = 0; i < lstFields.size(); i++) {
			if (!qrBudget.getDsReportHeader().locate(IQrBudget.FIELD_CODE,
					lstFields.get(i).toString())) {
				JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����ʾ�����ͷ�п�������!",
						"��ʾ", JOptionPane.ERROR_MESSAGE);
				break;
			}
			// xxl���˸���------
			// if (qrBudget.getDsReportHeader().fieldByName(
			// IQrBudget.FIELD_DISPWIDTH).getValue() != null) {
			// curReport.setColumnWidth(i, qrBudget.getDsReportHeader()
			// .fieldByName(IQrBudget.FIELD_DISPWIDTH).getDouble());
			// }
			iFieldDispwidth[i] = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_DISPWIDTH).getInteger();
			// xxl---------------
			int colWidth = iFieldDispwidth[i];
			if (colWidth <= 3) {// XXL Ϊʲô������3
				// �أ����������˽��п����ó���1��2�ȣ�Ŀ��Ҳ�������أ�����ſ�������
				colWidth = 600;
				mapZeroCol.put(new Integer(i), null);
			}// -----------------
			curReport.setColumnWidth(i, colWidth);

			ColDispInf colDispInf = new ColDispInf();
			colDispInf.sFieldType = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_TYPE).getString();
			colDispInf.sFieldDisformat = qrBudget.getDsReportHeader()
					.fieldByName(IQrBudget.FIELD_DISFORMAT).getString();
			if ("����".equals(colDispInf.sFieldType)
					|| "������".equals(colDispInf.sFieldType)
					|| "����".equals(colDispInf.sFieldType)) {
				colDispInf.style = colDispInf.style
						.deriveFormat(new DecimalFormat(
								colDispInf.sFieldDisformat));
				colDispInf.style = colDispInf.style
						.deriveHorizontalAlignment(Constants.RIGHT);
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_WrapText);
			} else if ("������".equals(colDispInf.sFieldType)) {
				colDispInf.style = colDispInf.style
						.deriveFormat(new SimpleDateFormat(
								colDispInf.sFieldDisformat));
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_SingleLine);
			} else {
				colDispInf.style = colDispInf.style
						.deriveTextStyle(Style.TextStyle_SingleLine);
			}
			colDispInf.fieldEName = qrBudget.getDsReportHeader().fieldByName(
					IQrBudget.FIELD_ENAME).getString();

			colDispInf.style = colDispInf.style.deriveBorder(1, Color.BLACK, 1,
					Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);

			lstCols.add(colDispInf);
		}

		String sFieldSelect = getFieldSelect(qrBudget.getDsReportHeader());
		qrBudget.setSFieldSelect(sFieldSelect);
		qrBudget.setLstColInfo(lstCols);
		if ("".equals(sFieldSelect)) {
			JOptionPane.showMessageDialog(qrBudget, "δ��ñ�����Ϣ��", "��ʾ",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		curReport.shrinkToFitRowHeight();
		// �ٻָ�������
		if (!mapZeroCol.isEmpty()) {
			java.util.Iterator it = mapZeroCol.keySet().iterator();
			while (it.hasNext()) {
				curReport.setColumnWidth(((Integer) it.next()).intValue(), 0);
			}
		}
		int iSZZBTitleRows = curReport.getReportHeader().getRows();
		qrBudget.getReportUI().setFrozenRow(iSZZBTitleRows);

	}

	/**
	 * ����ToolBar��ť״̬
	 * 
	 * 
	 */
	private void setTooBarState(ReportInfo reportInfo) throws Exception {

		// ���ò�ѯ�汾�Ƿ���ʾ, ��������ʾ�汾��Ϣ,��λ�治��ʾ�汾��Ϣ
		// chcx add 2007��09��10,modify:zlx
		ToolBarPanel fpnlToolBar = qrBudget.getFpnlToolBar();
		if (qrBudget.getIUserType() == 1) {
			DataSet dsDataVer = QrBudgetI.getMethod().getVerInfoWithReport(
					reportInfo.getReportID());
			fpnlToolBar.lstVer.clear();
			fpnlToolBar.cbxDataVer.removeAllItems();
			fpnlToolBar.cbxDataVer.addItem("ʵʱ");
			fpnlToolBar.lstVer.add(new Integer(0));
			if (dsDataVer != null && !dsDataVer.isEmpty())
				dsDataVer.beforeFirst();
			while (dsDataVer.next()) {
				fpnlToolBar.lstVer.add(new Integer(dsDataVer.fieldByName(
						IQrBudget.VER_NO).getInteger()));
				fpnlToolBar.cbxDataVer.addItem(dsDataVer.fieldByName(
						IQrBudget.FULLNAME).getString());
			}
			fpnlToolBar.cbxDataVer.setSelectedIndex(0);
		}

		// �������������Ƿ���ʾ
		if (reportInfo.isHasBatch()) {
			fpnlToolBar.flblEmpty1.setVisible(true);
//			fpnlToolBar.flblDataType.setVisible(true);
//			fpnlToolBar.cbxDataType.setVisible(true);
		} else {
			fpnlToolBar.flblEmpty1.setVisible(false);
//			fpnlToolBar.flblDataType.setVisible(false);
//			fpnlToolBar.cbxDataType.setVisible(false);
		}

		// ����ÿ����ʾ������
		fpnlToolBar.flblRown.setVisible(false);
		fpnlToolBar.jspRow.setVisible(false);
		fpnlToolBar.flblRown1.setVisible(false);
		fpnlToolBar.flblEmpty2.setVisible(false);

		int iReprot_type = qrBudget.getFtreReportName().getDataSet()
				.fieldByName(IQrBudget.TYPE_FLAG).getInteger();
		if (iReprot_type == 2) {
			fpnlToolBar.flblEmpty2.setVisible(true);
			fpnlToolBar.flblRown.setVisible(true);
			fpnlToolBar.jspRow.setVisible(true);
			fpnlToolBar.flblRown1.setVisible(true);
		}
		// �任����Դ��ť�Ƿ����
		SetActionStatus setActionStatus = new SetActionStatus(qrBudget);
		if (QrBudget.TYPE_GROUP == reportInfo.getBussType()) {
			setActionStatus.setOneBtnState("����ѡ��", true);
		} else {
			setActionStatus.setOneBtnState("����ѡ��", false);
		}
	}

	/**
	 * �����б���һ��TABLE
	 * 
	 * @param lstCols
	 * @return
	 */
	private FTable createTable(DataSet dsHeader, String lvlField,
			String fieldName, String fieldTitle, String fieldLeaf,
			String fieldColwidth, String fieldFormat, SysCodeRule codeRole,
			FTable aTable) throws Exception {
		if (dsHeader == null || dsHeader.isEmpty())
			return null;
		if (aTable == null)
			aTable = new FTable();
		List lstBasCols = new ArrayList();

		XMLData xmlCols = new XMLData();
		XMLData xmlFieldToLvl = new XMLData();// �ֶ����Ծ�LVLCODE��ֻ��¼�ֶ�

		List lstLastCol = new ArrayList();// ������
		// ��ϸ��ӹ�ϵ
		dsHeader.beforeFirst();
		while (dsHeader.next()) {
			String sLeaf = dsHeader.fieldByName(fieldLeaf).getString();
			String sTitle = dsHeader.fieldByName(fieldTitle).getString();
			String sLvl = dsHeader.fieldByName(lvlField).getString();
			String sFieldName = dsHeader.fieldByName(fieldName).getString();
			if (sFieldName != null)
				sFieldName = sFieldName.toLowerCase();
			String sWidth = dsHeader.fieldByName(fieldColwidth).getString();
			String sFormat = dsHeader.fieldByName(fieldFormat).getString();
			boolean isTopLevel = (codeRole.previous(sLvl) == null);
			int width = 0;
			if (sWidth == null || "".equals(sWidth) || "0".equals(sWidth)) {
				width = 120;
			} else
				width = Integer.parseInt(sWidth);

			if (Common.estimate(sLeaf)) {
				// ��������
				FBaseTableColumn aCol = createTableCol(sFieldName, sTitle,
						sFormat, width);
//				aCol.setHeaderRenderer(new FilterTableHeaderRender(null, null,
//						null));
				if (isTopLevel)
					lstLastCol.add(aCol);
				else
					lstBasCols.add(aCol);

				xmlCols.put(sLvl, aCol);
				xmlFieldToLvl.put(sFieldName, sLvl);

			} else {
				// ��������
				ColumnGroup aGroup = new ColumnGroup();
				aGroup.setId(sLvl);
				aGroup.setTitle(sTitle);
//				aGroup.setHeaderRenderer(new FilterTableHeaderRender(null,
//						null, null));
				if (isTopLevel)
					lstLastCol.add(aGroup);
				else
					lstBasCols.add(aGroup);
				xmlCols.put(sLvl, aGroup);
			}
		}

		// 2.����
		int iCount = lstBasCols.size();
		for (int i = 0; i < iCount; i++) {
			Object obj = lstBasCols.get(i);
			if (obj instanceof FBaseTableColumn) {
				FBaseTableColumn aCol = (FBaseTableColumn) obj;
				String sParentID = codeRole.previous((String) xmlFieldToLvl
						.get(aCol.getId()));
				ColumnGroup parGroup = (ColumnGroup) xmlCols.get(sParentID);
				if (parGroup == null)
					throw new Exception("����ļ��ι�ϵû��������ȷ![" + aCol.getId() + "]");
				parGroup.addControl(aCol);
			} else {
				ColumnGroup aGroup = (ColumnGroup) obj;
				String sParentID = codeRole.previous(aGroup.getId());
				ColumnGroup parGroup = (ColumnGroup) xmlCols.get(sParentID);
				if (parGroup == null)
					throw new Exception("����ļ��ι�ϵû��������ȷ![" + aGroup.getId()
							+ "]");
				parGroup.addControl(aGroup);
			}
		}
		iCount = lstLastCol.size();
		for (int i = 0; i < iCount; i++) {
			Object obj = lstLastCol.get(i);
			if (obj instanceof ColumnGroup)
				aTable.addColumnGroup((ColumnGroup) obj);
			else
				aTable.addColumn((FBaseTableColumn) obj);
		}

		return aTable;
	}

	private FBaseTableColumn createTableCol(String fieldName, String title,
			String sFormat, int width) {
		FBaseTableColumn aCol = new FBaseTableColumn();
		aCol.setId(fieldName);
		aCol.setTitle(title);
		aCol.setWidth(width);
		if (sFormat != null && !sFormat.equals("")) {
			// ���Ӹ�ʽ����Ϣ
			aCol.setFormat(sFormat);
		}
		return aCol;
	}
}
