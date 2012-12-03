package gov.nbcs.rp.queryreport.qrbudget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.BuildSql;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReportI;
import gov.nbcs.rp.queryreport.definereport.ui.ReportPanel;
import gov.nbcs.rp.queryreport.qrbudget.common.ReportUnt;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.GroupWhereSetPanel;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportFactory;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;


import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.fr.report.GroupReport;

public class ExeSearch {
	QrBudget qrBudget;

	private SearchPublic searchPublic;

	private Map mapCurPara;

	public ExeSearch(QrBudget qrBudget) {
		this.qrBudget = qrBudget;
		searchPublic = new SearchPublic(qrBudget);

	}

	/**
	 * 查询
	 * 
	 * @param mapPara
	 * @param treatSearchObj
	 *            清除页面保存信息
	 */
	public void doExeSearch(Map mapPara, final boolean treatSearchObj) {

		this.mapCurPara = mapPara;

		final ProgressBar pro = ProgressBar
				.createRefreshing("正在查询数据，请稍候......");
		new Thread() {

			public void run() {
				try {
					if (treatSearchObj) {
						// 清除查询体
						qrBudget.getTabPaneReport().treatSearchObj();
					}

					String sVerNo = qrBudget.getFpnlToolBar().getVerNo();
					// 查询数据,显示数据
					if (!getData(sVerNo))
						return;
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(Global.mainFrame,
							"发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (pro != null)
						pro.dispose();
				}
			}
		}.start();
		if (pro != null)
			pro.show();

	}

	/**
	 * 条件设置查询
	 * 
	 * @param sFieldEname
	 */
	public void doExeSearch(final List lstFieldEname,
			final GroupReport groupReport, final ConditionObj conditionObj,
			Map mapPara) {
		this.mapCurPara = mapPara;
		final ProgressBar pro = ProgressBar
				.createRefreshing("正在查询数据，请稍候......");
		new Thread() {
			public void run() {
				try {
					String sVerNo = qrBudget.getFpnlToolBar().getVerNo();
					// 查询数据,显示数据
					if (!getDataWhere(sVerNo, lstFieldEname, groupReport,
							conditionObj))
						return;

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(Global.mainFrame,
							"发生错误，错误信息：" + e.getMessage(), "提示",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (pro != null)
						pro.dispose();
				}
			}
		}.start();
		if (pro != null)
			pro.show();
	}

	/**
	 * 得到查询结果
	 * 
	 * @throws Exception
	 * 
	 */
	public boolean getData(String sVerNo) throws Exception {
		int index = qrBudget.getTabPaneReport().getSelectedIndex();
		ReportInfo reportInfo = (ReportInfo) qrBudget.getTabPaneReport()
				.getLstReport().get(index);

		if (reportInfo.getBussType() == QrBudget.TYPE_COVER) {// 封面
			showConver(reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// 收支总表
			showSzzb(reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP) {// 分组报表
			// 报表ID
			String reportId = reportInfo.getReportID();
			// 参数读写条件
			SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(
					reportId);
			// 本地文件参数与报表是否一致
			boolean isSame = false;
			List lstLevIsTotalField = null;
			GroupReport groupReport = null;
			ReportQuerySource querySource = null;
			// 判断本地参数文件是否有本报表参数
			if (setWhereReadWrite.isReportExists()) {
				IDefineReport definReportServ = DefineReportI.getMethod();
				groupReport = ReportPanel.getGroupReport(reportId,
						definReportServ);
				querySource = (ReportQuerySource) ReportConver
						.getReportQuerySource(groupReport);
				// 得到报表lstLevIsTotalField
				lstLevIsTotalField = ReportUnt.geLevIsTotalField(groupReport,
						querySource);
				// 判断本地文件参数与报表是否一致(用于判断报表是否修改过）
				isSame = setWhereReadWrite.checkFilePara(lstLevIsTotalField);
			}
			if (isSame) {// 一致
				// 将lstLevIsTotalField值更新groupReport文件
				setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
				GroupWhereSetPanel.changeLevValue(groupReport,
						lstLevIsTotalField, querySource);
				// 改变查询条件，显示分组报表
				showGroupWhere(sVerNo, reportInfo, setWhereReadWrite
						.getPfsCode(), groupReport, setWhereReadWrite
						.getConditionObj());
			} else {
				showGroup(sVerNo, reportInfo);
			}
		} else if (reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {// 行报表
			showRowSet(sVerNo, reportInfo);
		} else {// 其他查询表
			showOtherReport(sVerNo, reportInfo);
		}
		// qrBudget.getReportUI().repaint();
		qrBudget.getReportUI().setEditable(false);
		return true;
	}

	/**
	 * 设置查询条件，得到查询结果
	 * 
	 * @param sVerNo查询版本号
	 * @param reportInfo选中报表对象
	 * @param sFieldEname选中的支出资金来源组织的字符串
	 * @return
	 * @throws Exception
	 */
	public boolean getDataWhere(String sVerNo, List lstFieldEname,
			GroupReport groupReport, ConditionObj conditionObj)
			throws Exception {
		int index = qrBudget.getTabPaneReport().getSelectedIndex();
		ReportInfo reportInfo = (ReportInfo) qrBudget.getTabPaneReport()
				.getLstReport().get(index);

		if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// 收支总表
			// showSzzb(reportInfo);
		} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP) {// 分组报表
			this.showGroupWhere(sVerNo, reportInfo, lstFieldEname, groupReport,
					conditionObj);
		}

		qrBudget.getReportUI().repaint();
		qrBudget.getReportUI().setEditable(false);
		return true;
	}

	/**
	 * 显示封面
	 * 
	 */
	private void showConver(ReportInfo reportInfo) throws Exception {
		setConver((Report) qrBudget.getReportUI().getReport(), reportInfo,
				qrBudget.getDsSzzb(), qrBudget);
		qrBudget.getReportUI().repaint();
	}

	public static void setConver(
			gov.nbcs.rp.common.ui.report.Report report,
			ReportInfo reportInfo, DataSet dsSzzb, QrBudget qrBudget)
			throws Exception {
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		String sDivName = searchPublic.getDivNameValue();
		dsSzzb.beforeFirst();
//		while (dsSzzb.next()) {
//			if (dsSzzb.fieldByName(ISzzbSet.FIELD_PARA).getValue() == null)
//				continue;
//			if (!"DIV_NAME".equals(dsSzzb.fieldByName(ISzzbSet.FIELD_PARA)
//					.getString()))
//				continue;
//			int iColumn = dsSzzb.fieldByName(ISzzbSet.FIELD_COLUMN)
//					.getInteger();
//			int iRow = dsSzzb.fieldByName(ISzzbSet.FIELD_ROW).getInteger();
//			report.getCellElement(iColumn, iRow).setValue(sDivName);
//			// 为导出报表设置,导出报表需要传dataset
//			dsSzzb.edit();
//			dsSzzb.fieldByName(ISzzbSet.FIELD_VALUE).setValue(sDivName);
//			dsSzzb.applyUpdate();
//		}
		// 保存原报表列宽
		SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
				.getReport(), reportInfo);
	}

	/**
	 * 显示普通报表
	 */
	private boolean showOtherReport(String sVerNo, ReportInfo reportInfo)
			throws Exception {

		// 判断是否支持批次
		String sStatusWhere = "";
		if (reportInfo.isHasBatch()) {
			// 得到查询条件
			InfoPackage infoPackage = searchPublic.getFilter();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
						+ infoPackage.getsMessage(), "提示",
						JOptionPane.INFORMATION_MESSAGE);
				qrBudget.getFtreReportName().setEnabled(true);
				return false;
			}
			sStatusWhere = infoPackage.getsMessage();
		}

		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		}

		Map filePara = reportInfo.getParaMap();
		List lstData = null;
		if (ReadWriteFile.check(mapCurPara, filePara)) {
			lstData = ReadWriteFile.readFile(filePara);
		}

		if (lstData == null) {
			List lstResult;
			try {
				lstResult = qrBudget.getQrBudgetServ().getData(sVerNo,
						reportInfo.getReportID(), sStatusWhere, lstDept,
						lstDiv, qrBudget.getSFieldSelect(), Global.loginYear,
						qrBudget.getIUserType(), Global.loginmode,
						reportInfo.getBussType());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
						+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
				qrBudget.getFtreReportName().setEnabled(true);
				return false;
			}

			lstData = (List) lstResult.get(0);

			reportInfo.setSearchObj(lstResult.get(1));
			reportInfo.setParaMap(mapCurPara);
		} else {
			qrBudget.getFlblInfo().setText(ReadWriteFile.getInfo(filePara));
		}

		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Report report = ReportFactory.getOriReportWithData(lstData,
				curReport,
				qrBudget.getLstColInfo());
		qrBudget
				.setCurReport(report);

		qrBudget.getReportUI().setFrozenRow(
				curReport.getReportHeader().getRows());

		return true;

	}

	private void showSzzb(ReportInfo reportInfo) throws Exception {

		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// 判断是否支持批次
		String sStatusWhere = "";
		if (reportInfo.isHasBatch()) {
			// 得到查询条件
			InfoPackage infoPackage = searchPublic.getFilter();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
						+ infoPackage.getsMessage(), "提示",
						JOptionPane.INFORMATION_MESSAGE);
				qrBudget.getFtreReportName().setEnabled(true);
				return;
			}
			sStatusWhere = infoPackage.getsMessage();
		}

		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		}
//		SzzbDispByReport dispReport = ReportFactory
//				.getSzzbDispWithHeadByExists(
//						qrBudget.getReportUI().getReport(), reportInfo
//								.getReportID());
//
//		Report report = (Report) dispReport.getSearchReport(qrBudget
//				.getFpnlToolBar().getVerNo(), reportInfo.getReportID(),
//				sStatusWhere, lstDept, lstDiv, qrBudget.getSFieldSelect(),
//				Global.loginYear, reportInfo.getBussType(), SzzbSetI
//						.getMethod().isSZReportNeedConvert(
//								reportInfo.getReportID()));

//		qrBudget
//				.setCurReport((gov.nbcs.rp.common.ui.report.Report) report);
//		qrBudget.getReportUI().setFrozenRow(
//				dispReport.getBodyStart() - dispReport.getTitleStart());

		// 保存原报表列宽
		SaveColWidthAction.saveOldColWidth((Report) qrBudget.getReportUI()
				.getReport(), reportInfo);

	}

	private void showRowSet(String sVerNo, ReportInfo reportInfo)
			throws Exception {
		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Report report = getRowSetReort(sVerNo, reportInfo, curReport, qrBudget);
		qrBudget
				.setCurReport(report);

		qrBudget.getReportUI().setFrozenRow(
				curReport.getReportHeader().getRows());

	}

	public static Report getRowSetReort(String sVerNo, ReportInfo reportInfo,
			Report curReport, QrBudget qrBudget) throws Exception {
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		List lstDivName = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		} else {
			// 如果没有选中单位则单位名称由树的第一级节点组成

			searchPublic.getDivNameWhere(lstDivName, qrBudget.getIUserType());

		}
		int iBatchNO = searchPublic.getBatchNo();
		if (iBatchNO < 0)
			return null;
		int iDataType = searchPublic.getDataType();
		if (iDataType < 0)
			return null;
		InfoPackage info = searchPublic.getFilter();
		if (!info.getSuccess()) {
			JOptionPane
					.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
							+ info.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		String batchFilter = info.getsMessage();

		Report report = (Report) ReportFactory.getRowSetWithDataByExists(
				curReport, reportInfo, lstDept, lstDiv, sVerNo, batchFilter,
				Global.loginmode);
		return report;
	}

	/**
	 * 显示分组报表
	 * 
	 * @param sVerNo
	 * @param reportInfo
	 * @return
	 * @throws Exception
	 */
	private boolean showGroup(String sVerNo, ReportInfo reportInfo)
			throws Exception {
		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		List lstDivName = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		} else {
			// 如果没有选中单位则单位名称由树的第一级节点组成
			searchPublic.getDivNameWhere(lstDivName, qrBudget.getIUserType());

		}

		IQrBudget qrBudgetBO = (IQrBudget) BOCache.getBO("rp.qrBudgetService");
		List lstData = null;
		Map filePara = reportInfo.getParaMap();
		if (ReadWriteFile.check(mapCurPara, filePara)) {
			lstData = ReadWriteFile.readFile(filePara);
		}

		if (lstData == null) {
			InfoPackage infoPackage = searchPublic.getFilter();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(qrBudget,
						"部门预算查询表导出Excel发生错误，错误信息:" + infoPackage.getsMessage(),
						"提示", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			List lstResult = qrBudgetBO.getOriData(sVerNo, infoPackage
					.getsMessage(), lstDept, lstDiv, Global.loginYear, qrBudget
					.getIUserType(), Global.loginmode,
					reportInfo.getBussType(), reportInfo, new ConditionObj());

			if (lstResult == null || lstResult.size() < 2)
				return false;
			lstData = (List) lstResult.get(0);
			qrBudget.setLstResult(lstData);
			OriSearchObj oriSearchObj = (OriSearchObj) lstResult.get(1);
			reportInfo.setParaMap(mapCurPara);
			reportInfo.setSearchObj(oriSearchObj);
			qrBudget.getFlblInfo().setText("");
		} else {
			qrBudget.getFlblInfo().setText(ReadWriteFile.getInfo(filePara));
		}

		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Report report = ReportFactory.getOriReportWithData(lstData, curReport,
				qrBudget.getLstColInfo());
		qrBudget
				.setCurReport(report);
		qrBudget.getReportUI().setFrozenRow(
				curReport.getReportHeader().getRows());
		return true;

	}

	/**
	 * 改变查询条件，显示分组报表
	 * 
	 * @param sVerNo
	 * @param reportInfo
	 * @return
	 * @throws Exception
	 */
	private boolean showGroupWhere(String sVerNo, ReportInfo reportInfo,
			List lstFieldEname, GroupReport groupReport,
			ConditionObj conditionObj) throws Exception {
		IDefineReport definReportServ = DefineReportI.getMethod();
		if (groupReport == null)
			groupReport = ReportPanel.getGroupReport(reportInfo.getReportID(),
					definReportServ);

		List lstSqlLines;
		BuildSql buildSql = new BuildSql(groupReport);
		try {
			// 改变资金来源
			buildSql.changetDataSourceField(lstFieldEname);
			DataSet dsHeader = qrBudget.getQrBudgetServ().getReportHeader_A(
					reportInfo.getReportID());
			lstSqlLines = buildSql.getSqlLinesSql(dsHeader);
		} catch (Exception e) {
			e.printStackTrace();
			new MessageBox("更换数据源发生错误，请与管理员联系!", e.getMessage(),
					MessageBox.ERROR, MessageBox.BUTTON_OK).show();
			return false;
		}

		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		List lstDivName = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		} else {
			// 如果没有选中单位则单位名称由树的第一级节点组成
			searchPublic.getDivNameWhere(lstDivName, qrBudget.getIUserType());

		}

		IQrBudget qrBudgetBO = (IQrBudget) BOCache.getBO("rp.qrBudgetService");

		InfoPackage infoPackage = searchPublic.getFilter();
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表导出Excel发生错误，错误信息:"
					+ infoPackage.getsMessage(), "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		String sBatchNoFilter = infoPackage.getsMessage();

		List lstResult = qrBudgetBO.getOriDataWhere(sVerNo, sBatchNoFilter,
				lstDept, lstDiv, Global.loginYear, qrBudget.getIUserType(),
				Global.loginmode, reportInfo.getBussType(), reportInfo,
				lstSqlLines, conditionObj);

		if (lstResult == null || lstResult.size() < 2)
			return false;
		List lstData = (List) lstResult.get(0);
		qrBudget.setLstResult(lstData);
		OriSearchObj oriSearchObj = (OriSearchObj) lstResult.get(1);
		reportInfo.setParaMap(mapCurPara);
		reportInfo.setSearchObj(oriSearchObj);

		Report curReport = (Report) qrBudget.getReportUI().getReport();
		Report report = ReportFactory.getOriReportWithData(lstData, curReport,
				qrBudget.getLstColInfo());
		qrBudget
				.setCurReport(report);
		qrBudget.getReportUI().setFrozenRow(
				curReport.getReportHeader().getRows());
		// 删除原文件
		ReadWriteFile.deleteFile(reportInfo.getReportID());
		return true;

	}

}
