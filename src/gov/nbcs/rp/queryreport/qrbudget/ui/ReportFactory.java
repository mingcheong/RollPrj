/**
 * @# ReportFactory.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.DefinePub;
import gov.nbcs.rp.queryreport.qrbudget.bs.KitPub;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.ColDispInf;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.DivObject;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetDisp;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetDispByReport;

import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.common.constants.ReportTypeConstants;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.common.ifun.IReportBasicAttr;
import com.foundercy.pf.reportcy.common.ifun.ui.IFReportDisplay;
import com.foundercy.pf.reportcy.common.util.Log;
import com.foundercy.pf.reportcy.common.util.ReportParameterTools;
import com.foundercy.pf.reportcy.common.util.ReportTools;
import com.foundercy.pf.reportcy.common.util.StringEx;
import com.foundercy.pf.reportcy.design.drag.FormulaM;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.IDataSourceManager;
import com.foundercy.pf.reportcy.summary.iface.IReportQuerySource;
import com.foundercy.pf.reportcy.summary.iface.ISourceCol;
import com.foundercy.pf.reportcy.summary.iface.IToSource;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSourceManager;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportBasicAttr;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportUiAttr;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.PaperSize;
import com.fr.report.Report;
import com.fr.report.cellElement.CellGUIAttr;

/**
 * ����˵��:������Ĳ����㼯��һ�������� �������жϱ��������
 * <P>
 * һ��Ҫ���ñ�������ԣ�Ҫ�������reportCore��ʼ��ʧ�� ������IReportBasicAttr attr =
 * ReportParameterTools.getReportBasicAttr(report, reportInfo.getReportID());
 * Ҫ����REPORT_ID,�����ƽ̨δʵ�ֵĲ�ѯ�еı���Ҫ���������������Ϊ
 * ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT
 * ��Ҫ��SQL������ѯ����ʱ��IReportBasicAttr attr = ReportParameterTools
 * .getReportBasicAttr(qrBudget.getReport(), qrBudget
 * .getCurReportInfo().getReportID()); if (attr == null) attr = new
 * SummaryReportBasicAttr(); // ��̬���� String reportType =
 * ReportTypeConstants.REPORT_TYPE_7_DYNAMIC_REPORT; //
 * ���reportID�ᱻ��Ϊ���ؼ�������������в�����
 * attr.setReportID(qrBudget.getCurReportInfo().getReportID());
 * attr.setReportType(reportType);
 * attr.setExecuteType(ExecuteTypeConstants.EXEC_BY_0_SQL);�����������ѯExecuteTypeConstants.EXEC_BY_1_ENGINE
 * ��Ϊ���汨���б������֧�ܱ��ѯ�ķ�ʽ���Լ�ʵ�֣� �ǽ����в�ѯ���Ľ����Ϊ��ͷ��ʾ�� ���ԾͲ���Ҫָ����ѯ�ķ�ʽ Copyright
 * <P>
 * ҳ�����õı��ؼ�¼��Ĭ�ϴ򿪵ģ���ֻҪ���������ڵ�ҳ�����ã��ͻᱣ���ڱ����ļ��У�
 * ���п�ߵ�������Ϣ��Ҫ�ֶ��������ʱ�Ż��¼�ڱ��أ����ֶ�ɾ������ʱ������һͬɾ����ӡ������Ϣ Copyright All rights
 * reserved.
 * <P>
 
 */
public class ReportFactory {

	/**
	 * ȡ�÷��� ����
	 */

	public static final String fileName = "C:\\temp.xml";

	public static XMLData xmlDSource = new XMLData();// ���汨����Ҷ�Ӧ����

	public static String getDivSourceName(String sPartSourceName) {
		if (xmlDSource == null || xmlDSource.isEmpty())
			xmlDSource = ((IRowSet) BOCache.getBO("fb.rowSetService"))
					.getDataSourceTables(Global.getSetYear());
		return (String) xmlDSource.get(sPartSourceName);

	}

	public static Report getCover(String sReportId) throws Exception {

	
		
		return null;
	}

	/**
	 * ���Դ���ʽ����
	 * 
	 * @param aReport
	 *            ����һ���ڲ�ѯǰ�ʹ��ڣ�ֱ�ӿ��ԷŹ�����
	 * @param reportInfo
	 *            ������Ϣ
	 * @param lstCols
	 *            ������ʾ�е���Ϣ
	 * @param lstData
	 *            ��ѯ��������
	 * @return
	 * @throws Exception
	 */
	public static Report getReportWithData(Report aReport,
			ReportInfo reportInfo, List lstCols, List lstData) throws Exception {
		if (aReport == null) {
			aReport = new gov.nbcs.rp.common.ui.report.Report();
		}
		if (aReport == null) {
			lstCols = new ArrayList();

			aReport = getReportWithHead(aReport, reportInfo, true, lstCols);
		}

		TableHeader tableHeader = ((gov.nbcs.rp.common.ui.report.Report) aReport)
				.getReportHeader();
		aReport.removeAllCellElements();

		((gov.nbcs.rp.common.ui.report.Report) aReport)
				.setReportHeader(tableHeader);

		if (lstData == null)
			return aReport;

		List lstColInfo = lstCols; // �õ�����Ϣ�������ͣ�����ʾ��ʽ)

		int iHeaderRows = ((gov.nbcs.rp.common.ui.report.Report) aReport)
				.getReportHeader().getRows();
		CellGUIAttr cellGUIAttr = new CellGUIAttr();
		cellGUIAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);

		// ������ʾ��ʽ
		String sFieldType;
		Style dispStyle;

		Cell cellElement;
		Object sValue;
		for (int i = 0; i < lstData.size(); i++) {
			for (int j = 0; j < ((ArrayList) lstData.get(i)).size(); j++) {
				cellElement = new Cell(j, i + iHeaderRows);
				cellElement.setCellGUIAttr(cellGUIAttr);

				sFieldType = ((ColDispInf) lstColInfo.get(j)).sFieldType;
				dispStyle = ((ColDispInf) lstColInfo.get(j)).style;

				if (((ArrayList) lstData.get(i)).get(j) != null)
					sValue = ((ArrayList) lstData.get(i)).get(j);
				else
					sValue = "";

				if ("������".equals(sFieldType)) {
					cellElement.setStyle(dispStyle);
					cellElement.getStyle().deriveHorizontalAlignment(
							Constants.RIGHT);
					if ("0.00".equals(sValue) || "0".equals(sValue)
							|| "0.000000".equals(sValue))
						cellElement.setValue("");
					else
						cellElement.setValue(sValue);
				} else if ("".equals(sFieldType) || "�ַ���".equals(sFieldType)) {
					cellElement.setValue(sValue);
				} else if ("����".equals(sFieldType) || "����".equals(sFieldType)) {
					cellElement.setStyle(dispStyle);
					cellElement.getStyle().deriveHorizontalAlignment(
							Constants.RIGHT);
					if ("0".equals(sValue) || "0.00".equals(sValue)
							|| "0.000000".equals(sValue))
						cellElement.setValue("");
					else
						cellElement.setValue(sValue);
				} else if ("������".equals(sFieldType)) {
					cellElement.setStyle(dispStyle);
					cellElement.setValue(sValue);
				}
				cellElement.setStyle(dispStyle);
				aReport.addCellElement(cellElement, false);

			}
		}
		if (lstData.size() > 10000) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"��¼����һ�������뵼��excel�ٲ鿴��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return aReport;

	}

	public static Report getReportWithHead(Report aReport,
			ReportInfo reportInfo, boolean isShowTitle, List lstCols) {
		try {
			aReport = new gov.nbcs.rp.common.ui.report.Report();

			IQrBudget qrBudgetServ = QrBudgetI.getMethod();
			// aReport.removeAllCellElements();
			// ��ѯ��ͷ
			DataSet dsHeader = qrBudgetServ.getReportHeader(reportInfo
					.getReportID(), Global.loginYear);

			aReport = getReportWithHead(aReport, dsHeader, isShowTitle,
					lstCols, reportInfo);
		} catch (Exception e) {
			return null;
		}
		return aReport;
	}

	public static Report getReportWithHead(Report aReport, DataSet dsHeader,
			boolean isShowTitle, List lstCols, ReportInfo reportInfo) {
		try {
			HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
			Node nodeHeader = hg.generate(dsHeader, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
					IQrBudget.FIELD_CODE);
			// �����ͷ���������
			if (aReport == null
					|| !(aReport instanceof gov.nbcs.rp.common.ui.report.Report)) {
				aReport = new gov.nbcs.rp.common.ui.report.Report();
			}
			TableHeader tableHeader;
			int iEndRepeatRow = ((gov.nbcs.rp.common.ui.report.Report) aReport)
					.getBodyRowCount()
					+ Math
							.abs(((gov.nbcs.rp.common.ui.report.Report) aReport)
									.getReportHeader().getRows());

			aReport.removeAllCellElements();
			for (int i = 0; i < iEndRepeatRow; i++)
				aReport.setRowHeight(i, 20);

			if (!isShowTitle) {
				tableHeader = new TableHeader(nodeHeader);
			} else {
				String sCur = reportInfo.getUnit();
				if (sCur != null && !sCur.equals(""))
					sCur = "��λ:" + sCur;
				// tableHeader = new TableHeader(nodeHeader,
				// reportInfo.getTitle(), sCur);
				tableHeader = new TableHeader(nodeHeader);
			}
			((gov.nbcs.rp.common.ui.report.Report) aReport)
					.setReportHeader(tableHeader);

			// ���ñ����п����ݿ��б�����п��ֵ,������ʾ���ͺ���ʾ��ʽ
			List lstFields = tableHeader.getFields();
			if (lstCols == null)
				lstCols = new ArrayList();
			lstCols.clear();
			for (int i = 0; i < lstFields.size(); i++) {
				if (!dsHeader.locate(IQrBudget.FIELD_CODE, lstFields.get(i)
						.toString())) {
					JOptionPane.showMessageDialog(Global.mainFrame,
							"����Ԥ���ѯ����ʾ�����ͷ�п�������!", "��ʾ",
							JOptionPane.ERROR_MESSAGE);
					break;
				}

				if (dsHeader.fieldByName(IQrBudget.FIELD_DISPWIDTH).getValue() != null) {
					aReport.setColumnWidth(i, dsHeader.fieldByName(
							IQrBudget.FIELD_DISPWIDTH).getDouble());
				}
				dsHeader.fieldByName(IQrBudget.FIELD_DISPWIDTH).getInteger();

				ColDispInf colDispInf = new ColDispInf();
				colDispInf.sFieldType = dsHeader.fieldByName(
						IQrBudget.FIELD_TYPE).getString();
				colDispInf.sFieldDisformat = dsHeader.fieldByName(
						IQrBudget.FIELD_DISFORMAT).getString();
				if ("����".equals(colDispInf.sFieldType)
						|| "������".equals(colDispInf.sFieldType)
						|| "����".equals(colDispInf.sFieldType)) {
					colDispInf.style.deriveFormat(new DecimalFormat(
							colDispInf.sFieldDisformat));
				} else if ("������".equals(colDispInf.sFieldType)) {
					colDispInf.style.deriveFormat(new SimpleDateFormat(
							colDispInf.sFieldDisformat));
				}
				colDispInf.fieldEName = dsHeader.fieldByName(
						IQrBudget.FIELD_ENAME).getString();
				colDispInf.style = colDispInf.style.deriveBorderBottom(1,
						Color.BLACK).deriveBorderLeft(1, Color.BLACK)
						.deriveBorderRight(1, Color.BLACK).deriveBorderTop(1,
								Color.BLACK);

				// colDispInf.style.deriveBorderBottom(1, Color.BLACK);
				// colDispInf.style.setBorderBottomColor(Color.BLACK);
				// colDispInf.style.deriveBorderLeft(1, Color.BLACK);
				// colDispInf.style.setBorderLeftColor(Color.BLACK);
				// colDispInf.style.deriveBorderRight(1, Color.BLACK);
				// colDispInf.style.setBorderRightColor(Color.BLACK);
				// colDispInf.style.deriveBorderTop(1, Color.BLACK);
				// colDispInf.style.setBorderTopColor(Color.BLACK);
				lstCols.add(colDispInf);
			}

			aReport.shrinkToFitRowHeight();
		} catch (Exception e) {
			return null;
		}
		return aReport;
	}

	public static Report getConverWithData(Report report, String sDiv,
			ReportInfo reportInfo) throws Exception {
		// ���﷢��һ�����⣬��ֱ���޸�GROUPREPORT��CELLʱ��ʾ����ȷ��
		// �������е�CELL�����
		if (report == null)
			report = ReportFactory.getCover(reportInfo.getReportID());

	
		// report = ReportFactory.convertToGroupReport(
		// (gov.nbcs.rp.common.ui.report.Report) report,
		// reportInfo, null);
		// setReportSize(report, reportInfo.getReportID(), Global.getSetYear());
		// // ���ñ���ID���Ա����ɱ��ؼ���
		// IReportBasicAttr attr =
		// ReportParameterTools.getReportBasicAttr(report,
		// reportInfo.getReportID());
		// attr.setReportID(reportInfo.getReportID());

		// setGroupReport((GroupReport) report);

		return report;
	}

	public static String getFieldSelect(DataSet dsHead) throws Exception {
		DataSet dsReportHeader = dsHead;
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

	

	// ����һ���µķ��鱨��
	public static com.fr.report.Report getGroupReport(String setYear,
			String sReportId) throws Exception {
		// ȡ�ñ�����Ϣ
		File file = getFileFromDB(setYear, sReportId);
		if (file == null)
			return null;
		// ���ɱ������
		Report aReport = ReportTools.readReportByPath(file);
		// ���ñ����ͷ����
		// int iCount = aReport.getCellElementCount();
		// for (int i = 0; i < iCount; i++) {
		// aReport.getCellElement(i).getStyle().setBackground(
		// new ColorBackground(UntPub.HEADER_COLOR));
		//
		// }
		Iterator it = aReport.cellIterator();
		ColorBackground color = ColorBackground
				.getInstance(UntPub.HEADER_COLOR);
		while (it.hasNext()) {
			((CellElement) it.next()).getStyle().deriveBackground(color);
		}
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_HEADER, (GroupReport) aReport);
		int colCount = aReport.getLastColumn();
		// ���ñ�ͷ���߿�

		Style st = Style.getInstance();
		st = st.deriveHorizontalAlignment(Constants.CENTER).deriveBorder(1,
				Color.BLACK, 1, Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);
		for (int i = 0; i < rowIndex.length; i++) {
			for (int j = 0; j <= colCount; j++) {
				CellElement aCell = aReport.getCellElement(j, rowIndex[i]);
				if (aCell == null)
					continue;
				aCell.setStyle(st);

			}
		}
		rowIndex = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, (GroupReport) aReport);
		// ȡ����ϸ�������,Ĭ����ֻ��һ��

		if (rowIndex != null) {
			ColorBackground colorWhite = ColorBackground
					.getInstance(Color.WHITE);
			st = Style.getInstance();
			st = st.deriveBackground(colorWhite).deriveBorder(1, Color.BLACK,
					1, Color.BLACK, 1, Color.BLACK, 1, Color.BLACK);
			for (int i = 0; i <= colCount; i++) {
				CellElement aCell = aReport.getCellElement(i, rowIndex[0]);
				if (aCell == null)
					continue;
				aCell.setStyle(st);
			}
		}

		return aReport;

	}

	/**
	 * ���ñ������ز�������ѯ
	 * 
	 * @param aReport
	 * @param sVerNo
	 * @param sStatusWhere
	 * @param lstDept
	 * @param lstDiv
	 * @param getSFieldSelect
	 * @param loginYear
	 * @param getIUserType
	 * @param loginmode
	 * @Param reportControl,���鱨�������
	 * @return
	 */
	public static boolean setGroupReport(GroupReport aReport, String sVerNo,
			int batchNo, int dataType, List lstDept, List lstDiv,
			List lstDivName, String getSFieldSelect, int getIUserType,
			IFReportDisplay reportControl, boolean isZHC) throws Exception {

		ReportQuerySource querySource = (ReportQuerySource) ReportConver
				.getReportQuerySource(aReport);
		if (getIUserType == QrBudget.USER_DW) {// ��λ�û�
			// ����������е�����Դ����

			IDataSourceManager dsManager = querySource.getDataSourceManager();
			IDataSource[] arrDs = dsManager.getDataSourceArray();
			int iCount = arrDs.length;
			for (int i = 0; i < iCount; i++) {
				arrDs[i].setSource(getDivSourceName(arrDs[i].getSource()));
			}
		}
		// ���ñ���ĸ�����
		IParameter[] arrPar = querySource.getParameterArray();
		if (arrPar == null || arrPar.length == 0)
			return true;
		int iCount = arrPar.length;
		for (int i = 0; i < iCount; i++) {
			String sParName = arrPar[i].getChName();
			if (IDefineReport.DIV_CODE.equals(sParName)) {// ��λ���
				// ��ȡ�ñ���
				String aliaName = getDivAliaName(querySource, arrPar[i]);
				if (aliaName == null)
					return false;
				try {
					arrPar[i].getToSourceArray()[0].setAddSQL(getDivWhereSql(
							lstDept, lstDiv, Global.getSetYear(), aliaName,
							isZHC));
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else if (IDefineReport.BATCH_NO.equals(sParName)) {// ����
				arrPar[i].setValue(String.valueOf(batchNo));
			} else if (IDefineReport.DATA_TYPE.equals(sParName)) {// ��������
				arrPar[i].setValue(String.valueOf(dataType));
			} else if (IDefineReport.VER_NO.equals(sParName)) {// ���ݰ汾
				arrPar[i].setValue(String.valueOf(sVerNo));
			} else if (IDefineReport.DIVNAME_PARA.equals(sParName)) {// ��λ����
				arrPar[i].setValue(getDivsByList(lstDivName));
			}
		}
		// ִ�в�ѯ
		reportControl.doQuery();
		return true;
	}

	public static File getFileFromDB(String setYear, String reportID) {

		byte[] obj = null;
		try {
			obj = ((IRowSet) BOCache.getBO("fb.rowSetService")).getOBByID(
					Global.getSetYear(), reportID);
		} catch (Exception e1) {

			e1.printStackTrace();
			new MessageBox("��ѯ����!", e1.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return null;
		}
		File file = new File(fileName);
		file.deleteOnExit();

		FileOutputStream out = null;
		;
		try {
			out = new FileOutputStream(file);

			out.write(obj);
		} catch (Exception e1) {
			new MessageBox("ȡ���ļ�����!", e1.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e1.printStackTrace();
			file = null;
			try {
				out.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return null;
		}
		return file;

	}

	/**
	 * �õ���ѯ���� ע:alaisName�Ǳ������ֶ���һ��Ĵ��룬�磺budget.div_code
	 */
	public static String getDivWhereSql(List lstDept, List lstDiv,
			String setYear, String alaisName, boolean isZHC) throws Exception {
		String sFilter = "";
		// �õ�ҵ���Ҳ�ѯ����
		String sDeptWhere = getDeptFilter(lstDept, setYear, alaisName);
		// �õ���λ��ѯ����
		String sDivWhere = getDivFilter(lstDiv, alaisName);

		if ("".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDivWhere + ")";
		} else if (!"".equals(sDeptWhere) && "".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + ")";
		} else if (!"".equals(sDeptWhere) && !"".equals(sDivWhere)) {
			sFilter = sFilter + " and (" + sDeptWhere + " or" + sDivWhere + ")";
		}

		// modify by zlx 20080406
		// // �ܲ�ѯ����
		// if (!PubInterfaceStub.getMethod().isYSC()) {
		// if (isZHC)
		// sFilter = sFilter + getUserToDiv_ZHC(setYear, alaisName);
		// else
		// sFilter = sFilter + getUserToDiv(setYear, alaisName);
		// }

		// �ܲ�ѯ����
		// sFilter = sFilter + getUserToDiv(setYear, alaisName);
		return sFilter;
	}

	/**
	 * �õ�ҵ���Ҳ�ѯ����
	 * 
	 * @param lstDept
	 * @param setYear
	 * @return
	 */
	private static String getDeptFilter(List lstDept, String setYear,
			String alaisName) {
		// ҵ����
		String sDeptWhere = "";
		String sDept;
		for (int i = 0; i < lstDept.size(); i++) {
			sDept = "'" + lstDept.get(i).toString() + "'";
			if ("".equals(sDeptWhere))
				sDeptWhere = sDept;
			else
				sDeptWhere = sDeptWhere + "," + sDept;
		}
		if (lstDept.size() > 0) {
			sDeptWhere = "  "
					+ alaisName
					+ " in (select div_code from fb_u_deptodiv where dep_id in("
					+ sDeptWhere + ") and set_year = " + setYear + ")";
		}
		return sDeptWhere;
	}

	/**
	 * �õ���λ��ѯ����
	 * 
	 * @param lstDiv
	 * @return
	 */
	private static String getDivFilter(List lstDiv, String alaisName) {
		String sDivWhereisLeaf = "";
		String sDivWhereNoLeaf = "";
		DivObject divObject;
		for (int i = 0; i < lstDiv.size(); i++) {
			divObject = ((IQrBudget.DivObject) lstDiv.get(i));
			if (divObject.isLeaf) { // Ҷ�ڵ�
				if ("".equals(sDivWhereisLeaf)) {
					sDivWhereisLeaf = "'" + divObject.sDivCode + "'";
				} else {
					sDivWhereisLeaf = sDivWhereisLeaf + ",'"
							+ divObject.sDivCode + "'";
				}
			} else { // ����Ҷ�ڵ�
				if ("".equals(sDivWhereNoLeaf)) {
					sDivWhereNoLeaf = "  " + alaisName + " like '"
							+ divObject.sDivCode + "%'";
				} else {
					sDivWhereNoLeaf = sDivWhereNoLeaf + " or " + alaisName
							+ " like '" + divObject.sDivCode + "%'";
				}
			}
		}
		if (!"".equals(sDivWhereisLeaf)) {
			sDivWhereisLeaf = "  " + alaisName + " in (" + sDivWhereisLeaf
					+ ")";
		}

		String sFilter = "";
		if ("".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereNoLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && "".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf;
		} else if (!"".equals(sDivWhereisLeaf) && !"".equals(sDivWhereNoLeaf)) {
			sFilter = sDivWhereisLeaf + " or " + sDivWhereNoLeaf;
		}
		return sFilter;
	}

	// private static String getUserToDiv_ZHC(String setYear, String alaisName)
	// throws Exception {
	// String sFilterWhere = " and " + alaisName + " in "
	// + KitPub.vw_fb_filterDiv_ZHC(setYear, "chr_code");
	// return sFilterWhere;
	// }

	private static String getUserToDiv(String setYear, String alaisName)
			throws Exception {
		String sFilterWhere = " and " + alaisName + " in "
				+ KitPub.vw_fb_filterDiv(setYear, "chr_code");
		return sFilterWhere;
	}

	private static ISourceCol getColByID(String colID, ISourceCol[] cols) {
		int iCount = cols.length;
		for (int i = 0; i < iCount; i++) {
			if (cols[i].getSourceColID().equalsIgnoreCase(colID))
				return cols[i];

		}
		return null;
	}

	private static String getDivAliaName(ReportQuerySource aReport,
			IParameter divParam) throws Exception {
		try {
			String aliaName;
			IToSource[] arrToSource = divParam.getToSourceArray();
			if (arrToSource == null || arrToSource.length < 1)
				return null;
			String sSourceID = arrToSource[0].getSourceID();// Ĭ ��ֻ���ǵ�һ������Դ
			// �ҳ�����Դ��ö��Դ�����ӹ�ϵ
			IDataSourceManager dsManager = aReport.getDataSourceManager();
			IDataSource ds = dsManager.getDataSourceByID(sSourceID);// ����Դ������

			ISourceCol[] cols = ds.getColArray();
			if (cols == null || cols.length == 0) {// ���û�к�ö��Դ�Ĺ�ϵ����ֻ���ص�ǰ����Դ�ı���
				return ds.getSourceAlais();
			}
			// �ҵ�����Դ���Ӧ����
			ISourceCol aCol = getColByID(arrToSource[0].getSourceColID(), cols);
			if (aCol == null) {// ���û�к�ö��Դ�Ĺ�ϵ����ֻ���ص�ǰ����Դ�ı���
				return ds.getSourceAlais();
			}

			RefEnumSource es = aCol.getRefEnumSource();// ȡ�ô˴��е�ö����Ϣ
			String emumID = es.getEnumID();
			String emumType = es.getJoinType();// ȡ��ö������
			// ����������ӣ���ֻ�õ�����Դ�������Ϣ
			if (emumType.equals(IDefineReport.LEFT_JOIN)) {
				aliaName = ds.getSourceAlais() + "."
						+ arrToSource[0].getSourceColID();
			} else {// ����������ӣ����õ���ö�ٵ���Ϣ
				IEnumSourceManager arrEmManager = aReport
						.getEnumSourceManager();
				IEnumSource enumSource = arrEmManager.getEnumSourceByID(emumID);
				aliaName = enumSource.getEnumSourceAlais() + ","
						+ es.getRefEnumColCode();

			}
			return aliaName;
		} catch (Exception e) {
			throw new Exception("���ò���ʱ����" + e.getMessage());
		}
	}

	/**
	 * ��һ����REPORTת����GROUPREPORT;��Ϊ��չ��ʱ���Ƚ�������ԭʼ�ķ�ʽ�����˱��� ��Ҫ����ת����ƽ̨չʾ����ĸ�ʽ��
	 * 
	 * @param fbReport
	 * @param reportInfo
	 * @param lstFields
	 * @return
	 * @throws Exception
	 */
	public static GroupReport convertToGroupReport(
			gov.nbcs.rp.common.ui.report.Report fbReport,
			ReportInfo reportInfo, List lstFields) throws Exception {
		GroupReport aReport = null;
		if (reportInfo.getBussType() == QrBudget.TYPE_NORMAL)
			aReport = getGroupReportByHeader(fbReport, lstFields);
		else if (reportInfo.getBussType() == QrBudget.TYPE_COVER
				|| reportInfo.getBussType() == QrBudget.TYPE_SZZB
				|| reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {
			aReport = getReportByCover(fbReport);
		}
		setGroupReportAttr(reportInfo, aReport);
		return aReport;
	}

	/**
	 * ������ͷ��ƽ̨δʵ�ֱ���ת���ɷ��鱨��
	 * 
	 * @param headerReport
	 * @param lstFields
	 * @return
	 * @throws Exception
	 */
	private static GroupReport getGroupReportByHeader(
			gov.nbcs.rp.common.ui.report.Report headerReport,
			List lstFields) throws Exception {

		// ȡ�ñ�ͷ���򣬲�����GROUPREPROT��

		int iRows = headerReport.getReportHeader().getRows();
		int iCols = headerReport.getLastColumn();
		GroupReport groupReport = new GroupReport();
		setGroupReport(groupReport);
		// ���屨����
		// ��ø�Ϊ���ֶ����
		groupReport.removeAllCellElements();
		// �����ӵ�valueΪFormula������ע��༭��
		// // �����ӵ�valueΪFormula������ע��༭��
		// ѭ��ȡ��
		// �����ñ���

		int iTitle = headerReport.getReportHeader().getITitleRows();
		if (iTitle > 0) {

			// new AddTitleAreaRowAction(designGroupReportPane, iTitle)
			// .actionPerformed(null);
			for (int i = 0; i < iTitle; i++)
				groupReport.addReportHeaderRow();

			for (int i = 0; i < iRows; i++) {
				groupReport.setRowHeight(i, headerReport.getRowHeight(i));
				for (int j = 0; j <= iCols; j++) {
					CellElement aCell = headerReport.getCellElement(j, i);
					if (aCell == null)
						continue;
					groupReport.addCellElement(deepClone(aCell));
					groupReport.setColumnWidth(j, headerReport
							.getColumnWidth(j));
				}
			}
		}

		// ��ͷ��
		// new AddHeaderAreaRowAction(designGroupReportPane, iRows - iTitle)
		// .actionPerformed(null);
		for (int i = iTitle; i < iRows; i++)
			groupReport.addPageHeaderRow();
		ColorBackground color = ColorBackground
				.getInstance(UntPub.HEADER_COLOR);
		for (int i = iTitle; i < iRows; i++) {
			for (int j = 0; j <= iCols; j++) {
				CellElement aCell = headerReport.getCellElement(j, i);
				if (aCell == null)
					continue;
				aCell.getStyle().deriveBackground(color);
				groupReport.addCellElement(deepClone(aCell));

			}
		}
		groupReport.shrinkToFitRowHeight();

		// ���ñ��������ݿ����ֶεĶ�Ӧ
		// new AddOperationAreaRowAction(designGroupReportPane)
		// .actionPerformed(null);

		int detailRowIndex = 0;

		int detailRowIndexArray[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		if (detailRowIndexArray == null || detailRowIndexArray.length <= 0) {
			groupReport.addDetailRow();
			detailRowIndexArray = CreateGroupReport.getRowIndexs(
					RowConstants.UIAREA_OPERATION, groupReport);
			detailRowIndex = detailRowIndexArray[0];

		} else {
			detailRowIndex = detailRowIndexArray[0];
		}

		if (lstFields == null
				|| (lstFields.size() < headerReport.getReportHeader()
						.getColumns()))
			throw new Exception("�ֶ����в�ƥ��!");

		CellElement aCell = null;
		int iCount = lstFields.size();
		for (int i = 0; i < iCount; i++) {
			aCell = new CellElement(i, detailRowIndex);
			FieldInfo field = (FieldInfo) lstFields.get(i);
			FormulaM formula = new FormulaM(StringEx.sNull(field
					.getFieldEName().toUpperCase()));

			aCell.setValue(formula);
			DefinePub pub = new DefinePub();
			String type = pub.getFieldTypeWithCh(field.getFieldType());
			if (IDefineReport.CHAR_Val.equals(type)) {
				aCell.getStyle().deriveHorizontalAlignment(Constants.LEFT);
			} else {
				aCell.getStyle().deriveHorizontalAlignment(Constants.RIGHT);
				if (IDefineReport.CURRENCY_TYPE.equals(field.getFieldType())) {// ����
					String format = field.getFieldFormat();
					if (Common.isNullStr(format))
						format = "#,##0.00";
					aCell.getStyle().deriveFormat(new DecimalFormat(format));
				}
			}
			// aCell.getStyle().setBorderBottom(1);
			// aCell.getStyle().setBorderBottomColor(Color.BLACK);
			// aCell.getStyle().setBorderLeft(1);
			// aCell.getStyle().setBorderLeftColor(Color.BLACK);
			// aCell.getStyle().setBorderRight(1);
			// aCell.getStyle().setBorderRightColor(Color.BLACK);
			// aCell.getStyle().setBorderTop(1);
			// aCell.getStyle().setBorderTopColor(Color.BLACK);

			aCell.getStyle().deriveBorderBottom(1, Color.BLACK);
			aCell.getStyle().deriveBorderLeft(1, Color.BLACK);
			aCell.getStyle().deriveBorderRight(1, Color.BLACK);
			aCell.getStyle().deriveBorderTop(1, Color.BLACK);

			// ReportTools.writeReport(groupReport, new File("c://text.cpt"),
			// false);

			groupReport.addCellElement(aCell);

		}
		// ���ñ�����Ϣ

		return groupReport;

	}

	/**
	 * ���÷��鱨�������
	 * 
	 * @param groupReport
	 */
	public static void setGroupReport(GroupReport groupReport) {

		{

			// �������Զ���
			SummaryReportUiAttr attr = new SummaryReportUiAttr();
			attr.setColumnTypeArray(new int[] { 0, 0, 0, 1, 1, 1 });
			attr.setColumnCount(10);
			attr.setTitleAreaRowCount(2);
			attr.setHeaderAreaRowCount(1);
			// // ����������

			CreateGroupReport cg = new CreateGroupReport();
			int columnCount = attr.getColumnCount();
			try {
				// cg.makeReportNoGroupColumn(groupReport);
				int rowIndex = 0;

				/* titleArea */
				rowIndex += cg.addEmptyReportHeader(groupReport, rowIndex, attr
						.getTitleAreaRowCount(), columnCount);

				/* headerArea ͷ���� */
				rowIndex += cg.addEmptyPageHeader(groupReport, rowIndex, attr
						.getHeaderAreaRowCount(), columnCount);

				/* OperationArea������ */
				rowIndex += cg.addEmptyDetail(groupReport, rowIndex, attr
						.getOperationAreaRowCount(), columnCount);

			} catch (Exception ex) {
				Log.logger.error("", ex);
			}
			// groupReport
			IReportQuerySource reportQuerySource = new ReportQuerySource();

			// reportQuerySource.setDataSourceManager();
			// reportQuerySource.setEnumSourceManager();
			// reportQuerySource.setParameterArray();
			reportQuerySource.setReportBasicAttr(new SummaryReportBasicAttr());
			groupReport.setXMLable(reportQuerySource);
			reportQuerySource.getReportBasicAttr().setReportType(
					ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT);

		}

	}

	/**
	 * ��ȿ�¡����Ԫ
	 * 
	 * @param aCell
	 * @return
	 */
	private static CellElement deepClone(CellElement aCell) {
		// CellElement resultCell = new CellElement();aCell.clone();
		// resultCell.setStyle(aCell.getStyle());
		// resultCell.setValue(aCell.getValue());
		// resultCell.deriveCellElement(column, row)(aCell.getRow());
		// resultCell.setColumn(aCell.getColumn());
		// resultCell.setRowSpan(aCell.getRowSpan());
		// resultCell.setColumnSpan(aCell.getColumnSpan());
		// ���ֶ��޸�
		// Style style = aCell.getStyle();
		// FRFont frFont = style.getFRFont();
		// Font aFont = new Font("", frFont.getStyle(), frFont.getSize());
		// style.setFRFont(new FRFont(aFont));
		// resultCell.setStyle(style);
		try {
			return (CellElement) aCell.clone();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �������SZZB�����ʱ�����������������һ���յ�SQL�� �����ǽ����е����ݵ�����ͷ
	 * 
	 * @param coverReport
	 * @return
	 */
	private static GroupReport getReportByCover(Report coverReport)
			throws Exception {
		// ��������Ʒ���ģ���ѱ���ȫ��������ͷ
		int iRows = coverReport.getLastRow();
		int iCols = coverReport.getLastColumn();
		GroupReport groupReport = new GroupReport();
		// ���屨����
		setGroupReport(groupReport);
		groupReport.removeAllCellElements();
		// new AddHeaderAreaRowAction(designGroupReportPane, iRows)
		// .actionPerformed(null);
		for (int i = 0; i <= iRows; i++)
			groupReport.addPageHeaderRow();

		for (int i = 0; i <= iCols; i++) {
			groupReport.setRowHeight(i, coverReport.getRowHeight(i));
			for (int j = 0; j <= iRows; j++) {
				try {
					CellElement aCell = coverReport.getCellElement(i, j);
					if (aCell == null)
						continue;
					groupReport.addCellElement(deepClone(aCell));
				} catch (Exception e) {
					continue;
				}
			}
		}
		for (int j = 0; j < iCols; j++) {
			groupReport.setColumnWidth(j, coverReport.getColumnWidth(j));
		}
		groupReport.shrinkToFitRowHeight();
		return groupReport;

	}

	public static PaperSize getPaperSize(String size) {

		if (Common.isEqual("A1", size))
			return (PaperSize) PaperSize.PAPERSIZE_A1;
		else if (Common.isEqual("A2", size))
			return (PaperSize) PaperSize.PAPERSIZE_A2;
		else if (Common.isEqual("A3", size))
			return (PaperSize) PaperSize.PAPERSIZE_A3;
		else if (Common.isEqual("A4", size))
			return (PaperSize) PaperSize.PAPERSIZE_A4;
		else if (Common.isEqual("A5", size))
			return (PaperSize) PaperSize.PAPERSIZE_A5;
		else if (Common.isEqual("A6", size))
			return (PaperSize) PaperSize.PAPERSIZE_A6;
		else if (Common.isEqual("A7", size))
			return (PaperSize) PaperSize.PAPERSIZE_A7;
		else if (Common.isEqual("A8", size))
			return (PaperSize) PaperSize.PAPERSIZE_A8;
		else if (Common.isEqual("B1", size))
			return (PaperSize) PaperSize.PAPERSIZE_B1;
		else if (Common.isEqual("B2", size))
			return (PaperSize) PaperSize.PAPERSIZE_B2;
		else if (Common.isEqual("B3", size))
			return (PaperSize) PaperSize.PAPERSIZE_B3;
		else if (Common.isEqual("B4", size))
			return (PaperSize) PaperSize.PAPERSIZE_B4;
		else if (Common.isEqual("B5", size))
			return (PaperSize) PaperSize.PAPERSIZE_B5;
		else
			return (PaperSize) PaperSize.PAPERSIZE_A3;
	}

	// ȡ�����ñ����С
	private static void setReportSize(Report report, String reportID,
			String setYear) {
		// XMLData xmlInfo = QrBudgetI.getMethod().getSettingsInfo(reportID,
		// setYear);
		// if (xmlInfo == null)
		// return;
		// ReportSettings setting = report.getReportSettings();
		// setting.setPaperSize(getPaperSize((String) xmlInfo
		// .get(ReportNameTreeSelectionListener.PAPER_SIZE)));
		//
		// setting.setMargin(new Margin(getaDouble(xmlInfo,
		// ReportNameTreeSelectionListener.MARGIN_TOP), getaDouble(
		// xmlInfo, ReportNameTreeSelectionListener.MARGIN_LEFT),
		// getaDouble(xmlInfo,
		// ReportNameTreeSelectionListener.MARGIN_BOTTOM),
		// getaDouble(xmlInfo,
		// ReportNameTreeSelectionListener.MARGIN_RIGHT)));
		// if ("0".equals((String) xmlInfo
		// .get(ReportNameTreeSelectionListener.PRINT_ORIENT))) {
		// setting.setOrientation(ReportConstants.LANDSCAPE);
		// } else
		// setting.setOrientation(ReportConstants.PORTRAIT);
	}

	private static double getaDouble(XMLData aData, String field) {
		Object obj = aData.get(field);
		if (obj == null)
			return 0;
		try {
			return Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	// ���÷��鱨�������,����ҳ��Ĵ�С�Ǵ����ݿ���ȡ�ò����õģ������REPORT��IDһ��Ҫ����
	public static void setGroupReportAttr(ReportInfo reportInfo,
			GroupReport report) {
		IReportBasicAttr attr = ReportParameterTools.getReportBasicAttr(report,
				reportInfo.getReportID());
		// ��̬����
		String reportType = ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT;

		// ���reportID�ᱻ��Ϊ���ؼ�������������в�����
		attr.setReportID(reportInfo.getReportID());
		attr.setReportName(reportInfo.getName());
		attr.setReportType(reportType);
		setReportSize(report, reportInfo.getReportID(), Global.getSetYear());
	}

	private static String getDivsByList(List lstDivs) {
		if (lstDivs == null || lstDivs.size() == 0)
			return "";
		int iCount = lstDivs.size() - 1;
		if (iCount == 0)
			return (String) lstDivs.get(0);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iCount; i++) {
			sb.append(lstDivs.get(i).toString()).append("��");
		}
		return sb.substring(0, sb.length() - 1) + " �� "
				+ (String) lstDivs.get(iCount);

	}

	public static Report getRowSetHeader(ReportInfo reportInfo) {

		try {
			return new RowSetDisp(reportInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȡ���б���ֻ�б���,û���ݣ��ɴ��뱨��ʵ�����Ϳɲ����ٴ�����ʵ��
	 * 
	 * @param report
	 * @param reportInfo
	 * @param rows
	 * @return
	 */
	public static Report getRowSetHeaderByExists(Report report,
			ReportInfo reportInfo) {
		try {
			RowSetDispByReport rowSetDsp = new RowSetDispByReport(report,
					reportInfo);
			return rowSetDsp.getReportWithHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȡ���б���������ݣ��ṩREPORT��ֱ���ڱ������޸�
	 * 
	 * @param report
	 * @param reportInfo
	 * @param rows
	 * @param lstDept
	 * @param lstDiv
	 * @param sVerNo
	 * @param batchFilter
	 * @param iLoginmode
	 * @return
	 * @throws Exception
	 */
	public static Report getRowSetWithDataByExists(Report report,
			ReportInfo reportInfo, List lstDept, List lstDiv, String sVerNo,
			String batchFilter, int iLoginmode) throws Exception {

		RowSetDispByReport rowSetDsp = new RowSetDispByReport(report,
				reportInfo);
		return rowSetDsp.getSearchReport(lstDept, lstDiv, sVerNo, batchFilter,
				iLoginmode);

	}

	public static Report getRowSetWithData(ReportInfo reportInfo, List lstDept,
			List lstDiv, String sVerNo, String batchFilter, int iLoginmode)
			throws Exception {
		RowSetDisp rowSetDisp = (RowSetDisp) getRowSetHeader(reportInfo);
		rowSetDisp.doSearch(lstDept, lstDiv, sVerNo, batchFilter, iLoginmode);
		return rowSetDisp;

	}

	public static List getRowSetListData(ReportInfo reportInfo, List lstDept,
			List lstDiv, String sVerNo, String batchFilter, int iLoginmode)
			throws Exception {
		return RowSetDispByReport.getSearchList(reportInfo, lstDept, lstDiv,
				sVerNo, batchFilter, iLoginmode);
	}

	// ԭʼ��ѯ����ʾ

	/**
	 * ��ʾ��ѯ��Ϣ
	 * 
	 * @param sReportId
	 * @throws Exception
	 */
	public static gov.nbcs.rp.common.ui.report.Report getOriReportWithData(
			List lstList,
			gov.nbcs.rp.common.ui.report.Report report,
			List lstColInfo) throws Exception {
		TableHeader tableHeader = report.getReportHeader();
		// �����п�
		int cols = tableHeader.getColumns();
		double[] colArray = new double[cols];
		for (int i = 0; i < cols; i++) {
			colArray[i] = report.getColumnWidth(i);
		}
		report.removeAllCellElements();
		report.setReportHeader(tableHeader);
		//add by xxl �ͳ�ʼ��ʱһ�����Ƚ�Ϊ��ı��У�����һ����ȣ����仯�߶Ⱥ��ַ���
		Map mapZeroCol = new HashMap();
		// �����п�
		for (int i = 0; i < cols; i++) {
			double colWidth = colArray[i];
			if (colWidth <= 3) {
				colWidth = 500;
				mapZeroCol.put(new Integer(i), null);
			}

			report.setColumnWidth(i, colWidth);
		}//-------------------------------------------------------------

		if (lstList == null)
			return report;

		int iHeaderRows = report.getReportHeader().getRows();
		// CellGUIAttr cellGUIAttr = new CellGUIAttr();
		// cellGUIAttr.setEditableState(Constants.EDITABLE_STATE_FALSE);

		// ������ʾ��ʽ
		String sFieldType;
		Style dispStyle;
		String sFieldEname;

		Cell cellElement;
		Object sValue;
		XMLData aData = null;
		int iRowCount = lstList.size();
		if (iRowCount > 10000) {
			iRowCount = 10000;
		}
		int iColCount = lstColInfo.size();
		for (int i = 0; i < iRowCount; i++) {
			aData = (XMLData) lstList.get(i);

			for (int j = 0; j < iColCount; j++) {
				cellElement = new Cell(j, i + iHeaderRows);
				// cellElement.setCellGUIAttr(cellGUIAttr);

				sFieldType = ((ColDispInf) lstColInfo.get(j)).sFieldType;
				dispStyle = ((ColDispInf) lstColInfo.get(j)).style;
				sFieldEname = ((ColDispInf) lstColInfo.get(j)).fieldEName
						.toLowerCase();

				// if (((ArrayList) lstList.get(i)).get(j) != null)
				sValue = aData.get(sFieldEname);
				if ("null".equals(sValue))
					sValue = "";
				if ("������".equals(sFieldType)) {
					// cellElement.setStyle(dispStyle);
					// cellElement.getStyle().deriveHorizontalAlignment(
					// Constants.RIGHT);
					try {
						double dd = Double.parseDouble(sValue.toString());
						if (dd == 0)
							cellElement.setValue("");
						else
							cellElement.setValue(sValue);
					} catch (Exception e) {
						cellElement.setValue("");
					}

				} else if ("".equals(sFieldType) || "�ַ���".equals(sFieldType)) {
					cellElement.setValue(sValue);
				} else if ("����".equals(sFieldType) || "����".equals(sFieldType)) {
					// cellElement.setStyle(dispStyle);
					// cellElement.getStyle().deriveHorizontalAlignment(
					// Constants.RIGHT);
					try {
						double dd = Double.parseDouble(sValue.toString());
						if (dd == 0)
							cellElement.setValue("");
						else
							cellElement.setValue(sValue);
					} catch (Exception e) {
						cellElement.setValue("");
					}
				} else if ("������".equals(sFieldType)) {
					// cellElement.setStyle(dispStyle);
					cellElement.setValue(sValue);
				}
				cellElement.setStyle(dispStyle);
				report.addCellElement(cellElement, false);

			}
		}

		report.shrinkToFitRowHeight();
		//XXL �ָ����20090823
		if (!mapZeroCol.isEmpty()) {
			Iterator it = mapZeroCol.keySet().iterator();
			while (it.hasNext()) {
				report.setColumnWidth(((Integer) it.next()).intValue(), 0);
			}
		}
		//-----------------------
		if (iRowCount > 10000) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"��¼����һ�������뵼��excel�ٲ鿴��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return report;
	}
}
