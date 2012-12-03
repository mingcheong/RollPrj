package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.export.action.ExportProp;
import gov.nbcs.rp.common.export.action.ExportToExcel;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.ColDispInf;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OtherSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetDispByReport;

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * ����ΪExcel,ֻ���Ե�һ�ű���
 * 

 * 
 */
public class ReportExcel extends CommonAction {

	private static final long serialVersionUID = 1L;

	QrBudget qrBudget;

	private Object searchObj;

	private ReportInfo reportInfo;

	private DataSet dsFace; // ����Ŀ¼, ��֧�ܱ�

	public static int CANCEL_OPTION = 0;

	public static int TRUE_OPTION = 1;

	public static int FALSE_OPTION = 2;

	public void actionPerformed(ActionEvent arg0) {

		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			qrBudget = (QrBudget) modulePanel;
			if (!qrBudget.checkSelectedNode(qrBudget.getFtreReportName()))
				return;

			MyProgressBar myProgressBar = new MyProgressBar();
			myProgressBar.display();
		}
	}

	private class MyProgressBar implements Runnable {

		public void display() {
			Thread myThread = new Thread(this);
			myThread.start();
		}

		public void run() {

			doExport();

		}
	}

	private void doExport() {

		int bResult = FALSE_OPTION;
		int index = qrBudget.getTabPaneReport().getSelectedIndex();
		reportInfo = (ReportInfo) qrBudget.getTabPaneReport().getLstReport()
				.get(index);
		searchObj = reportInfo.getSearchObj();

		ProgressBar pf = new ProgressBar(Global.mainFrame,
				"���ڵ���Excel�����Ժ򡤡���������", false);
		try {
			switch (reportInfo.getBussType()) {
			case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
				bResult = exportExcelConver();
				break;
			}
			case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�
				bResult = exportExcelSzzb();
				break;
			}
			case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
				bResult = exportExcelRow();
				break;
			}
			case IDefineReport.REPORTTYPE_GROUP: { // �����
				bResult = exportExcelGroupOther();
				break;
			}
			case IDefineReport.REPORTTYPE_OTHER: {// �������ⱨ������
				bResult = exportExcelGroupOther();
				break;
			}
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		} finally {
			pf.dispose();
		}

		if (bResult == CANCEL_OPTION)
			return;
		else if (bResult == TRUE_OPTION)
			new MessageBox("�����ļ��ɹ���", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("�����ļ�ʧ�ܣ�", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * ��������Ŀ¼
	 * 
	 * @return
	 * @throws Exception
	 */
	private int exportExcelConver() throws Exception {
		String sReportId = reportInfo.getReportID();
		Object oTitle = reportInfo.getTitle(); // ������
		dsFace = qrBudget.getDsSzzb();

		Exportpp pp = new Exportpp();
		ExportToExcel eep = new ExportToExcel(sReportId, null, null, null,
				null, pp);

		if (eep.exportConvert(Common.nonNullStr(oTitle))) {
			return TRUE_OPTION;
		} else {
			if (eep.getIsCancel()) {
				return CANCEL_OPTION;
			}
			return FALSE_OPTION;
		}
	}

	/**
	 * ������֧�ܱ�
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelSzzb() throws Exception {
		String sReportId = reportInfo.getReportID();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// �õ���ѯbatch��ѯ����
		InfoPackage infoPackage = searchPublic.getFilter();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
					+ infoPackage.getsMessage(), "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return FALSE_OPTION;
		}
		String sStatusWhere = infoPackage.getsMessage();

		// �õ���λ��ѯ����
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		}

//		dsFace = SzzbDispByReport.getReportDsByValue(qrBudget.getFpnlToolBar()
//				.getVerNo(), reportInfo.getReportID(), sStatusWhere, lstDept,
//				lstDiv, qrBudget.getSFieldSelect(), Global.loginYear,
//				reportInfo.getBussType(), SzzbSetI
//				.getMethod().isSZReportNeedConvert(
//						reportInfo.getReportID()));

		Exportpp pp = new Exportpp();
		ExportToExcel eep = new ExportToExcel(sReportId, pp);

		if (eep.doExport()) {
			return TRUE_OPTION;
		} else {
			if (eep.getIsCancel()) {
				return CANCEL_OPTION;
			}
			return FALSE_OPTION;
		}
	}

	/**
	 * �����б�
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelRow() throws Exception {
		String sReportId = reportInfo.getReportID();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// �õ���ѯbatch��ѯ����
		InfoPackage infoPackage = searchPublic.getFilter();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
					+ infoPackage.getsMessage(), "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return CANCEL_OPTION;
		}
		String sStatusWhere = infoPackage.getsMessage();

		// �õ���λ��ѯ����
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		// add by ymq 20101102  ����û�г�ʼ���������µ����Ͳ�ѯ�Ľ������ݲ�һ�¡�	
		searchPublic.getDivWhere(lstDept, lstDiv, GlobalEx.isFisVis()?1:0);
		
		List lstRowSearch = RowSetDispByReport.getSearchList(reportInfo,
				lstDept, lstDiv, qrBudget.getFpnlToolBar().getVerNo(),
				sStatusWhere, Global.loginmode);
		// ����Node
		Node node = ((Report) qrBudget.getReportUI().getReport())
				.getReportHeader().getDocumentRoot();
		// ��ͷDataSet
		DataSet dsHeader = qrBudget.getQrBudgetServ().getReportHeader(
				sReportId, Global.loginYear);

		Exportpp pp = new Exportpp();
		String ename = "FIELD_ENAME";
		ExportToExcel eep = new ExportToExcel(node, dsHeader, lstRowSearch,
				ename, pp, sReportId);
		if (eep.doExport()) {
			return TRUE_OPTION;
		} else {
			if (eep.getIsCancel()) {
				return CANCEL_OPTION;
			}
			return FALSE_OPTION;
		}
	}

	/**
	 * �������鱨������ⱨ��
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelGroupOther() throws Exception {
		String sReportId = reportInfo.getReportID();
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Node node = curReport.getReportHeader().getDocumentRoot(); // ����Node

		String sSqlHeader = qrBudget.getQrBudgetServ().getReportHeaderSql(
				sReportId, Global.loginYear); // ��ͷSql

		// ����Sql
		String sSqlBody = null;
		if (searchObj instanceof OriSearchObj) {
			sSqlBody = ((OriSearchObj) searchObj).getSearchSql();
		} else if (searchObj instanceof OtherSearchObj) {
			sSqlBody = ((OtherSearchObj) searchObj).getSSqlBody();
		}

		// �п�
		List lstFields = curReport.getReportHeader().getFields();
		int iFieldDispwidth[] = new int[lstFields.size()];
		for (int i = 0; i < lstFields.size(); i++) {
			iFieldDispwidth[i] = (int) curReport.getColumnWidth(i);
		}
		// ��ʾ��ʽ
		String sFieldDisformat[] = new String[lstFields.size()];
		for (int i = 0; i < lstFields.size(); i++) {
			sFieldDisformat[i] = ((ColDispInf) qrBudget.getLstColInfo().get(i)).sFieldDisformat;
		}

		String ename = "FIELD_ENAME";
		Exportpp pp = new Exportpp();
		ExportToExcel eep = new ExportToExcel(sReportId, node, sSqlHeader,
				sSqlBody, ename, pp);
		// ����excel�����������
		if (Common.estimate(reportInfo.getChangeSource()))
			if (!Common.isNullStr(reportInfo.getAddOnsTitle())) {
				eep.setTitle(reportInfo.getTitle() + "-"
						+ reportInfo.getAddOnsTitle());
			}

		if (eep.doExport()) {
			return TRUE_OPTION;
		} else {
			if (eep.getIsCancel()) {
				return CANCEL_OPTION;
			}
			return FALSE_OPTION;
		}
	}

	private class Exportpp implements ExportProp {

		public String getFieldTName() {
			return "FIELD_TYPE";
		}

		public String getFieldFName() {
			return "FIELD_DISFORMAT";
		}

		public String getFieldWName() {
			return "FIELD_DISPWIDTH";
		}

		public String getSqlHeader() {
			return null;
		}

		public String getSqlBody() {
			return null;
		}

		public DataSet getDsHeader() {
			return null;
		}

		public DataSet getDsBody() {
			return null;
		}

		public String getTitle() {
			return reportInfo.getTitle();
		}

		public String[] getTitle_Child() {
			SearchPublic searchPublic = new SearchPublic(qrBudget);
			String sDivName = null;
			try {
				sDivName = searchPublic.getDivNameValue();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "��ȡ��λ���Ʒ������󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
			return new String[] { sDivName, "��λ:" + reportInfo.getUnit() };
		}

		public String[] getNumberViewInTable() {
			return new String[] { "������", "����", "����", "������", "������" };
		}

		public String getDefaultAddres() {
			return null;
		}

		public List getSqls_BeforeGetBody() {
			if (searchObj instanceof OriSearchObj) {
				List lstSql = new ArrayList();
				// lstSql.add();
				// List lstSql = ((OriSearchObj) searchObj).getLstInsertSql();
				// lstSql.add(0, ((OriSearchObj)
				// searchObj).getCreateTableSql());
				lstSql.add(((OriSearchObj) searchObj).getCreateTableSql());
				lstSql.addAll(((OriSearchObj) searchObj).getLstInsertSql());
				// lstSql.add(((OriSearchObj) searchObj).getSearchSql());
				return lstSql;
			} else if (searchObj instanceof OtherSearchObj) {
				return ((OtherSearchObj) searchObj).getLstSqlMidView();
			} else {
				return null;
			}
		}

		public List getSqls_AfterGetBody() {
			if (searchObj instanceof OriSearchObj) {
				List lstSql = new ArrayList();
				lstSql.add(((OriSearchObj) searchObj).getDropTableSql());
				return lstSql;
			} else if (searchObj instanceof OtherSearchObj) {
				if (((OtherSearchObj) searchObj).getLstDeleteRecord() == null
						|| ((OtherSearchObj) searchObj).getLstDeleteRecord()
								.size() == 0)
					return null;
				return ((OtherSearchObj) searchObj).getLstDeleteRecord();

			} else {
				return null;
			}

		}

		public DataSet[] getDsFace() {
			return new DataSet[] { dsFace };
		}

		public float[] getWidths() {
			return null;
		}

		public String[] getFormats() {
			// ��֧�ܱ�
			if (reportInfo.getBussType() == IDefineReport.REPORTTYPE_SZZB) {
				try {
					// ����DataSet
					DataSet dsHeader = qrBudget.getQrBudgetServ()
							.getReportHeader(reportInfo.getReportID(),
									Global.loginYear);
					if (dsHeader == null)
						return null;
					int iCount = dsHeader.getRecordCount();
					if (iCount == 0)
						return null;
					String[] types = new String[iCount];
					int i = 0;
					dsHeader.beforeFirst();
					while (dsHeader.next()) {
						types[i] = dsHeader.fieldByName(
								IQrBudget.FIELD_DISFORMAT).getString();
						i++;
					}
					return types;
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(qrBudget, "��ȡ�����ͷ������󣬴�����Ϣ:"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
			}
			return null;
		}

		public String[] getTypes() {
			// ��֧�ܱ�
			if (reportInfo.getBussType() == IDefineReport.REPORTTYPE_SZZB) {
				try {
					// ����DataSet
					DataSet dsHeader = qrBudget.getQrBudgetServ()
							.getReportHeader(reportInfo.getReportID(),
									Global.loginYear);
					if (dsHeader == null)
						return null;
					int iCount = dsHeader.getRecordCount();
					if (iCount == 0)
						return null;
					String[] types = new String[iCount];
					int i = 0;
					dsHeader.beforeFirst();
					while (dsHeader.next()) {
						types[i] = dsHeader.fieldByName(IQrBudget.FIELD_TYPE)
								.getString();
						i++;
					}
					return types;
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(qrBudget, "��ȡ�����ͷ������󣬴�����Ϣ:"
							+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
			}
			return null;
		}
	}

}
