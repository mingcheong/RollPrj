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
 * 导出为Excel,只可以导一张报表
 * 

 * 
 */
public class ReportExcel extends CommonAction {

	private static final long serialVersionUID = 1L;

	QrBudget qrBudget;

	private Object searchObj;

	private ReportInfo reportInfo;

	private DataSet dsFace; // 封面目录, 收支总表

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
				"正在导出Excel，请稍候······", false);
		try {
			switch (reportInfo.getBussType()) {
			case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
				bResult = exportExcelConver();
				break;
			}
			case IDefineReport.REPORTTYPE_SZZB: {// 收支总表
				bResult = exportExcelSzzb();
				break;
			}
			case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
				bResult = exportExcelRow();
				break;
			}
			case IDefineReport.REPORTTYPE_GROUP: { // 分组表
				bResult = exportExcelGroupOther();
				break;
			}
			case IDefineReport.REPORTTYPE_OTHER: {// 其他特殊报表设置
				bResult = exportExcelGroupOther();
				break;
			}
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表导出Excel发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		} finally {
			pf.dispose();
		}

		if (bResult == CANCEL_OPTION)
			return;
		else if (bResult == TRUE_OPTION)
			new MessageBox("导出文件成功！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
		else
			new MessageBox("导出文件失败！", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
	}

	/**
	 * 导出封面目录
	 * 
	 * @return
	 * @throws Exception
	 */
	private int exportExcelConver() throws Exception {
		String sReportId = reportInfo.getReportID();
		Object oTitle = reportInfo.getTitle(); // 主标题
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
	 * 导出收支总表
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelSzzb() throws Exception {
		String sReportId = reportInfo.getReportID();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// 得到查询batch查询条件
		InfoPackage infoPackage = searchPublic.getFilter();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表导出Excel发生错误，错误信息:"
					+ infoPackage.getsMessage(), "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return FALSE_OPTION;
		}
		String sStatusWhere = infoPackage.getsMessage();

		// 得到单位查询条件
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
	 * 导出行表
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelRow() throws Exception {
		String sReportId = reportInfo.getReportID();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// 得到查询batch查询条件
		InfoPackage infoPackage = searchPublic.getFilter();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表导出Excel发生错误，错误信息:"
					+ infoPackage.getsMessage(), "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return CANCEL_OPTION;
		}
		String sStatusWhere = infoPackage.getsMessage();

		// 得到单位查询条件
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		// add by ymq 20101102  由于没有初始化参数导致导出和查询的界面数据不一致。	
		searchPublic.getDivWhere(lstDept, lstDiv, GlobalEx.isFisVis()?1:0);
		
		List lstRowSearch = RowSetDispByReport.getSearchList(reportInfo,
				lstDept, lstDiv, qrBudget.getFpnlToolBar().getVerNo(),
				sStatusWhere, Global.loginmode);
		// 报表Node
		Node node = ((Report) qrBudget.getReportUI().getReport())
				.getReportHeader().getDocumentRoot();
		// 报头DataSet
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
	 * 导出分组报表和特殊报表
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws Exception
	 */
	private int exportExcelGroupOther() throws Exception {
		String sReportId = reportInfo.getReportID();
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Node node = curReport.getReportHeader().getDocumentRoot(); // 报表Node

		String sSqlHeader = qrBudget.getQrBudgetServ().getReportHeaderSql(
				sReportId, Global.loginYear); // 表头Sql

		// 表体Sql
		String sSqlBody = null;
		if (searchObj instanceof OriSearchObj) {
			sSqlBody = ((OriSearchObj) searchObj).getSearchSql();
		} else if (searchObj instanceof OtherSearchObj) {
			sSqlBody = ((OtherSearchObj) searchObj).getSSqlBody();
		}

		// 列宽
		List lstFields = curReport.getReportHeader().getFields();
		int iFieldDispwidth[] = new int[lstFields.size()];
		for (int i = 0; i < lstFields.size(); i++) {
			iFieldDispwidth[i] = (int) curReport.getColumnWidth(i);
		}
		// 显示格式
		String sFieldDisformat[] = new String[lstFields.size()];
		for (int i = 0; i < lstFields.size(); i++) {
			sFieldDisformat[i] = ((ColDispInf) qrBudget.getLstColInfo().get(i)).sFieldDisformat;
		}

		String ename = "FIELD_ENAME";
		Exportpp pp = new Exportpp();
		ExportToExcel eep = new ExportToExcel(sReportId, node, sSqlHeader,
				sSqlBody, ename, pp);
		// 导出excel报表标题名称
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
				JOptionPane.showMessageDialog(qrBudget, "获取单位名称发生错误，错误信息:"
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
			return new String[] { sDivName, "单位:" + reportInfo.getUnit() };
		}

		public String[] getNumberViewInTable() {
			return new String[] { "浮点型", "整型", "整数", "货币型", "浮点数" };
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
			// 收支总表
			if (reportInfo.getBussType() == IDefineReport.REPORTTYPE_SZZB) {
				try {
					// 报表DataSet
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
					JOptionPane.showMessageDialog(qrBudget, "获取列类型发生错误，错误信息:"
							+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
			return null;
		}

		public String[] getTypes() {
			// 收支总表
			if (reportInfo.getBussType() == IDefineReport.REPORTTYPE_SZZB) {
				try {
					// 报表DataSet
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
					JOptionPane.showMessageDialog(qrBudget, "获取列类型发生错误，错误信息:"
							+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
			return null;
		}
	}

}
