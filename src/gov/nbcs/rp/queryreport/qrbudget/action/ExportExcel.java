/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.action;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.export.action.ExportBatchProp;
import gov.nbcs.rp.common.export.action.ExportToExcel;
import gov.nbcs.rp.common.export.reporttypes.ReportType;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ui.BuildSql;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReportI;
import gov.nbcs.rp.queryreport.definereport.ui.ReportHeader;
import gov.nbcs.rp.queryreport.definereport.ui.ReportPanel;
import gov.nbcs.rp.queryreport.qrbudget.common.ReportUnt;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.ConditionObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.GroupWhereSetPanel;
import gov.nbcs.rp.queryreport.qrbudget.ui.OriSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.OtherSearchObj;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudgetI;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportHeaderOpe;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetDispByReport;

import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:多表导出公共类
 * </p>
 * <p>
 * Description:多表导出公共类
 * </p>
 * <p>

 * @version 6.2.40
 */
public class ExportExcel {

	// 选中节点数(目录和封面表）
	private int iConverCount;

	// 其他报表（去除目录和封面）
	private int iOtherCount;

	// 表头Sql
	private String sSqlHeader[];

	// 表体Sql
	private String sSqlBody[];

	// 报表Node
	private Node node[];

	private String sTitle[]; // 主标题

	private List sSubTitle;// 小标题

	private String sReportArray[];// reportid

	private List lstFieldDispwidth;// 显示列宽

	private List lstFieldDisformat;// 显示格式

	private List lstMidView; // 中间视图或中间临时表

	private List lstDel;// 删除中间表记录

	// 封面目录存放dataSet
	private DataSet dsSzzb[];

	// 定义目录封面
	private String sConvertTitle[] = null;

	private String sDivName;

	private List lstDept;

	private List lstDiv;

	private List lstTempTable = new ArrayList();

	private String sStatusWhere;

	private String sVerNo;

	private boolean isNotExportWhenEmpty;

	// 数据库联接
	private IQrBudget qrBudgetServ;

	// 用户类型：1：业务处室，单位
	private int iUserType;

	private ExportToExcel eep;

	private String sFileType;

	private String sPath = "C:\\Documents and Settings\\Administrator\\My Documents\\";

	public ExportExcel(List lstDept, List lstDiv, String sStatusWhere,
			String sVerNo, String sFileType, boolean isNotExportWhenEmpty) {
		// 数据库联接
		qrBudgetServ = QrBudgetI.getMethod();
		this.lstDept = lstDept;
		this.lstDiv = lstDiv;
		this.sStatusWhere = sStatusWhere;
		this.sFileType = sFileType;
		this.sVerNo = sVerNo;
		this.isNotExportWhenEmpty = isNotExportWhenEmpty;

		// 1:业务处室,其他单位
		iUserType = UntPub.FIS_VIS.equals(GlobalEx.getBelongType()) ? 1 : 0;
	}

	public ExportExcel(String sDivName, List lstDept, List lstDiv,
			String sStatusWhere, String sVerNo, boolean isNotExportWhenEmpty) {
		this(lstDept, lstDiv, sStatusWhere, sVerNo, "0", isNotExportWhenEmpty);
		this.sDivName = sDivName;
	}

	/**
	 * 导出查询表（每个单位生成一个文件）
	 * 
	 * @param myTreeNode
	 *            选中的节点
	 * @throws Exception
	 */
	public void doReportExcelWithDiv(CustomTree ftreReportNameChoice,
			String sDivCodeName) throws Exception {
		MyTreeNode[] reportTreeNode = ftreReportNameChoice
				.getSelectedNodes(true);

		// 选中节点数(目录和封面表）
		iConverCount = getConverCount(reportTreeNode, ftreReportNameChoice
				.getDataSet());
		// 取得选中的节点数
		int iTotalSelectCount = reportTreeNode.length;
		// 其他报表（去除目录和封面）
		iOtherCount = iTotalSelectCount - iConverCount;
		if (iConverCount != 0) {
			dsSzzb = new DataSet[iConverCount];
			// 封面目录标题
			sConvertTitle = new String[iConverCount];
		}

		if (iOtherCount != 0) {
			node = new Node[iOtherCount]; // 报表Node
			sSqlHeader = new String[iOtherCount]; // 表头Sql
			sSqlBody = new String[iOtherCount]; // 表体Sql
			sTitle = new String[iOtherCount]; // 主标题sTitle
			sSubTitle = new ArrayList();// 小标题
			sReportArray = new String[iOtherCount]; // reportid

			lstFieldDispwidth = new ArrayList(); // 显示列宽
			lstFieldDisformat = new ArrayList(); // 显示格式

			lstMidView = new ArrayList(); // 中间视图或中间临时表
			lstDel = new ArrayList();// 删除中间表记录
		}
		String ename = "FIELD_ENAME";

		int jConver = 0;
		int jOther = 0;

		// 报表类型标识符
		int[] iReTypes = new int[iOtherCount];
		// 报表类型封装类
		Object[] oReTypes = new Object[iOtherCount];
		ReportInfo reportInfo;

		for (int i = 0; i < reportTreeNode.length; i++) {
			ftreReportNameChoice.getDataSet().gotoBookmark(
					reportTreeNode[i].getBookmark());
			reportInfo = new ReportInfo(ftreReportNameChoice.getDataSet());

			switch (reportInfo.getBussType()) {
			case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
				exportExcelConver(reportInfo, jConver, sDivName,
						ftreReportNameChoice.getDataSet());
				jConver++;
				break;
			}
//			case IDefineReport.REPORTTYPE_SZZB: {// 收支总表
//				exportExcelSzzb(reportInfo, jOther, sDivName);
//
//				DataSet dsSzzbDisp = SzzbDispByReport.getReportDsByValue("0",
//						reportInfo.getReportID(), sStatusWhere, lstDept,
//						lstDiv, "", Global.loginYear, reportInfo.getBussType(),
//						SzzbSetI.getMethod().isSZReportNeedConvert(
//								reportInfo.getReportID()));
//				// 设置标识符为sql导出类型
//				iReTypes[jOther] = ReportType.LikeFace;
//				ReportType ret = (ReportType) ReportType
//						.create(ReportType.LikeFace);
//				ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
//				oReTypes[jOther] = ret;
//				jOther++;
//				break;
//			}
			case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
				// 报表node
				DataSet dsReportHeader = qrBudgetServ.getReportHeader(
						reportInfo.getReportID(), Global.loginYear);

				exportExcelRow(reportInfo, jOther, sDivName, dsReportHeader);
				List lstRowSearch = RowSetDispByReport.getSearchList(
						reportInfo, lstDept, lstDiv, "0", sStatusWhere,
						Global.loginmode);
				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.DsLst;
				ReportType ret = ReportType
						.create(ReportType.DsLst);
				ret.setParam(reportInfo.getReportID(), node[jOther],
						dsReportHeader, lstRowSearch, ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_GROUP: { // 分组表
				exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
						lstDept, lstDiv, sDivName);

				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.Sql;
				ReportType ret = ReportType.create(ReportType.Sql);
				ret.setParam(sReportArray[jOther], node[jOther],
						sSqlHeader[jOther], sSqlBody[jOther], ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_OTHER: {// 其他特殊报表设置
				exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
						lstDept, lstDiv, sDivName);

				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.Sql;
				ReportType ret = ReportType.create(ReportType.Sql);
				ret.setParam(sReportArray[jOther], node[jOther],
						sSqlHeader[jOther], sSqlBody[jOther], ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			}
		}

		// 标题同名进行效验，不允许标题重复
		this.checkTitleRepeat(sTitle);
		if (iConverCount != 0) {
			this.checkTitleRepeat(sConvertTitle);
		}

		// 创建多表导出辅助类
		Exportpp pp = new Exportpp();
		pp.setAfterSql(lstDel);
		pp.setBeforeSql(lstMidView);
		if (iConverCount != 0) {
			pp.setDsCover(dsSzzb);
		}
		pp.setTitles(sTitle);
		pp.setTilteChild(sSubTitle);
		pp.setSAddress(sPath + sDivCodeName);

		eep = new ExportToExcel(iReTypes, oReTypes, pp, true,
				isNotExportWhenEmpty, lstTempTable);
		eep.setFaceTitles(sConvertTitle);

	}

	/**
	 * 导出查询表(每个报表生成一个文件）
	 * 
	 * @param ftreReportNameChoice
	 * @param reportTreeNode
	 * @param sExportType
	 * @throws Exception
	 */
	public void doReportExcelWithFile(CustomTree ftreReportNameChoice,
			CustomTree ftreDivName, MyTreeNode reportTreeNode,
			MyTreeNode[] divNode, String sExportType) throws Exception {
		int iDivLen = divNode.length;

		// 判断选中的是否是目录或封面
		if (getConverCount(reportTreeNode, ftreReportNameChoice.getDataSet())) {
			iConverCount = iDivLen;
		} else {
			iConverCount = 0;
		}
		// 取得选中的节点数
		int iTotalSelectCount = iDivLen;
		// 其他报表（去除目录和封面）
		iOtherCount = iTotalSelectCount - iConverCount;
		if (iConverCount != 0) {
			dsSzzb = new DataSet[iConverCount];
			// 封面目录标题
			sConvertTitle = new String[iConverCount];
		}

		if (iOtherCount != 0) {
			node = new Node[iOtherCount]; // 报表Node
			sSqlHeader = new String[iOtherCount]; // 表头Sql
			sSqlBody = new String[iOtherCount]; // 表体Sql
			sTitle = new String[iOtherCount]; // 主标题sTitle
			sSubTitle = new ArrayList();// 小标题
			sReportArray = new String[iOtherCount]; // reportid

			lstFieldDispwidth = new ArrayList(); // 显示列宽
			lstFieldDisformat = new ArrayList(); // 显示格式

			lstMidView = new ArrayList(); // 中间视图或中间临时表
			lstDel = new ArrayList();// 删除中间表记录
		}
		String ename = "FIELD_ENAME";

		int jConver = 0;
		int jOther = 0;

		// 报表类型标识符
		int[] iReTypes = new int[iOtherCount];
		// 报表类型封装类
		Object[] oReTypes = new Object[iOtherCount];
		ReportInfo reportInfo = new ReportInfo(ftreReportNameChoice
				.getDataSet());

		for (int i = 0; i < iTotalSelectCount; i++) {
			List lstDeptTmp = new ArrayList();
			List lstDivTmp = new ArrayList();

			ftreDivName.getDataSet().gotoBookmark(divNode[i].getBookmark());
			String sDepOrDivName = ftreDivName.getDataSet().fieldByName(
					IPubInterface.DIV_NAME).getString();
			if ("0".equals(sExportType)) {
				lstDeptTmp.clear();
				lstDeptTmp.add(lstDept.get(i));
			} else {
				lstDivTmp.clear();
				lstDivTmp.add(lstDiv.get(i));
			}

			switch (reportInfo.getBussType()) {
			case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
				exportExcelConver(reportInfo, jConver, sDepOrDivName, null);
				jConver++;
				break;
			}
			case IDefineReport.REPORTTYPE_SZZB: {// 收支总表
				exportExcelSzzb(reportInfo, jOther, sDepOrDivName);

//				DataSet dsSzzbDisp = SzzbDispByReport.getReportDsByValue("0",
//						reportInfo.getReportID(), sStatusWhere, lstDeptTmp,
//						lstDivTmp, "", Global.loginYear, reportInfo
//								.getBussType(),
//						SzzbSetI.getMethod().isSZReportNeedConvert(
//								reportInfo.getReportID()));
				// 设置标识符为sql导出类型
//				iReTypes[jOther] = ReportType.LikeFace;
//				ReportType ret = (ReportType) ReportType
//						.create(ReportType.LikeFace);
//				ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
//				oReTypes[jOther] = ret;
//
//				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
				// 报表node
				DataSet dsReportHeader = qrBudgetServ.getReportHeader(
						reportInfo.getReportID(), Global.loginYear);

				exportExcelRow(reportInfo, jOther, sDepOrDivName,
						dsReportHeader);
				List lstRowSearch = RowSetDispByReport.getSearchList(
						reportInfo, lstDeptTmp, lstDivTmp, "0", sStatusWhere,
						Global.loginmode);
				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.DsLst;
				ReportType ret = ReportType
						.create(ReportType.DsLst);
				ret.setParam(reportInfo.getReportID(), node[jOther],
						dsReportHeader, lstRowSearch, ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_GROUP: { // 分组表
				exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
						lstDeptTmp, lstDivTmp, sDepOrDivName);

				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.Sql;
				ReportType ret = ReportType.create(ReportType.Sql);
				ret.setParam(sReportArray[jOther], node[jOther],
						sSqlHeader[jOther], sSqlBody[jOther], ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_OTHER: {// 其他特殊报表设置
				exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
						lstDeptTmp, lstDivTmp, sDepOrDivName);

				// 设置标识符为sql导出类型
				iReTypes[jOther] = ReportType.Sql;
				ReportType ret = ReportType.create(ReportType.Sql);
				ret.setParam(sReportArray[jOther], node[jOther],
						sSqlHeader[jOther], sSqlBody[jOther], ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			}
		}

		// 标题同名进行效验，不允许标题重复
		this.checkTitleRepeat(sTitle);
		if (iConverCount != 0) {
			this.checkTitleRepeat(sConvertTitle);
		}

		// 创建多表导出辅助类
		Exportpp pp = new Exportpp();
		pp.setAfterSql(lstDel);
		pp.setBeforeSql(lstMidView);
		if (iConverCount != 0) {
			pp.setDsCover(dsSzzb);
		}
		pp.setTitles(sTitle);
		pp.setTilteChild(sSubTitle);
		// 得到报表名称字符串
		String sReportName = ftreReportNameChoice.getDataSet().fieldByName(
				IQrBudget.REPORT_CNAME).getString();
		pp.setSAddress(sPath + sReportName);

		eep = new ExportToExcel(iReTypes, oReTypes, pp, false,
				isNotExportWhenEmpty, lstTempTable);
		eep.setFaceTitles(sConvertTitle);

	}

	/**
	 * 导出封面
	 * 
	 * @throws Exception
	 * 
	 */
	private void exportExcelConver(ReportInfo reportInfo, int jConver,
			String sDivName, DataSet dsReportNameChoice) throws Exception {
		String sReportId = reportInfo.getReportID();
//		dsSzzb[jConver] = SzzbSetI.getMethod().getSzzb(sReportId,
//				Global.loginYear);
//		if (dsSzzb[jConver].locate(ISzzbSet.FIELD_PARA, "DIV_NAME")) {
//			// 为导出报表设置,导出报表需要传dataset
//			dsSzzb[jConver].edit();
//			dsSzzb[jConver].fieldByName(ISzzbSet.FIELD_VALUE)
//					.setValue(sDivName);
//			dsSzzb[jConver].applyUpdate();
//		}
		if ("0".equals(sFileType)) {
			sConvertTitle[jConver] = dsReportNameChoice.fieldByName(
					IQrBudget.TITLE).getString();
		} else {
			sConvertTitle[jConver] = sDivName;
		}
	}

	/**
	 * 分组和特殊表
	 * 
	 * @param reportInfo
	 * @param iOther
	 * @throws Exception
	 */
	private void exportExcelGroupOther(ReportInfo reportInfo, int jOther,
			String sStatusWhere, List lstDept, List lstDiv, String sDivName)
			throws Exception {
		String sReportId = reportInfo.getReportID();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		// 报表node
		DataSet dsReportHeader = qrBudgetServ.getReportHeader(sReportId,
				Global.loginYear);
		// 表头内容，更改资金来源和对比分析时，读本地参数信息
		SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(sReportId);
		if (setWhereReadWrite.isReportExists()) {
			if (setWhereReadWrite.getPfsFname() != null)
				// 改变表头资金来源显示
				dsReportHeader = ReportHeaderOpe.changeDataSource(
						dsReportHeader, setWhereReadWrite.getPfsFname());
			ConditionObj conditionObj = setWhereReadWrite.getConditionObj();
//			if (conditionObj != null) {
//				DataSet dsDataType = PubInterfaceStub.getMethod()
//				.getOptDataTypeAllList();
			if (conditionObj != null) {
				DataSet dsDataType =null; 
				if (dsDataType.locate("BatchNo", conditionObj.getOneBatchNo(),
						"DataType", conditionObj.getOneDataType())) {
					dsReportHeader = ReportHeaderOpe.compareReport(
							dsReportHeader, conditionObj.getOneYear()
									+ dsDataType.fieldByName("name")
											.getString(),
							ReportHeader.COMPARE_1);
				}
				if (dsDataType.locate("BatchNo", conditionObj.getTwoBatchNo(),
						"DataType", conditionObj.getTwoDataType())) {
					dsReportHeader = ReportHeaderOpe.compareReport(
							dsReportHeader, conditionObj.getTwoYear()
									+ dsDataType.fieldByName("name")
											.getString(),
							ReportHeader.COMPARE_2);
				}

			}
		}

		node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
				IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
				IQrBudget.FIELD_CODE);
		// 查询字段组成的字符串
		String sFieldSelect = getFieldSelect(dsReportHeader);
		// 表头sql
		sSqlHeader[jOther] = qrBudgetServ.getReportHeaderSql(sReportId,
				Global.loginYear);

		// 表体Sql
		// -------------------------------------
		// 这里分成两部分，如果是GROUPREPORT则要以不同方式取得XXL

		if (reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
			OtherSearchObj otherSearchObj = null;
			try {
				if (reportInfo.isHasBatch()) {
					otherSearchObj = qrBudgetServ.getOtherSearchObj(sVerNo,
							sReportId, sStatusWhere, lstDept, lstDiv,
							sFieldSelect, Global.loginYear, iUserType,
							Global.loginmode);
				} else {
					otherSearchObj = qrBudgetServ.getOtherSearchObj(sVerNo,
							sReportId, "", lstDept, lstDiv, sFieldSelect,
							Global.loginYear, iUserType, Global.loginmode);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"生成报表信息出错，错误信息:" + e.getMessage(), "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			sSqlBody[jOther] = otherSearchObj.getSSqlBody();
			lstMidView.add(otherSearchObj.getLstSqlMidView());
			lstDel.add(otherSearchObj.getLstDeleteRecord());
			lstTempTable.add(otherSearchObj.getLstTempTable());
		} else {// 如果是分组的报表
			// 报表ID
			String reportId = reportInfo.getReportID();
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

			OriSearchObj oriSearchObj;
			if (isSame) {// 一致
				// 根据本地参数文件内容，更改lstLevIsTotalField值
				setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
				GroupWhereSetPanel.changeLevValue(groupReport,
						lstLevIsTotalField, querySource);
				// 得到支出资金来源值
				List lstFieldEname = setWhereReadWrite.getPfsCode();
				// 对比条件
				ConditionObj conditionObj = setWhereReadWrite.getConditionObj();

				BuildSql buildSql = new BuildSql(groupReport);
				// 改变资金来源
				buildSql.changetDataSourceField(lstFieldEname);
				DataSet dsHeader = qrBudgetServ.getReportHeader_A(reportInfo
						.getReportID());
				// 组织sql
				List lstSqlLines = buildSql.getSqlLinesSql(dsHeader);
				// 查询语句
				oriSearchObj = qrBudgetServ.getSearchObjWhere(sVerNo,
						sStatusWhere, lstDept, lstDiv, Global.loginYear,
						reportInfo.getBussType(), Global.loginmode, reportInfo
								.getBussType(), reportInfo, lstSqlLines,
						conditionObj);
			} else {
				oriSearchObj = qrBudgetServ.getSearchObj(sVerNo, sStatusWhere,
						lstDept, lstDiv, Global.loginYear, iUserType,
						Global.loginmode, reportInfo.getBussType(), reportInfo,
						new ConditionObj());
			}
			String sErr = oriSearchObj.check();
			if (!sErr.equals("")) {
				new MessageBox("生成报表信息出错!" + sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			sSqlBody[jOther] = oriSearchObj.getSearchSql();
			List lstMid = new ArrayList();
			lstMid.add(oriSearchObj.getCreateTableSql());
			lstMid.addAll(oriSearchObj.getLstInsertSql());
			lstMidView.add(lstMid);
			List lstDrop = new ArrayList();
			lstDrop.add(oriSearchObj.getDropTableSql());
			lstDel.add(lstDrop);
			List lstTmp = new ArrayList();
			lstTmp.add(oriSearchObj.getTempTable());
			lstTempTable.add(lstTmp);
		}

		// 主标题
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
		// 单位参数
		sSubTitle.add(new String[] { sDivName, reportInfo.getUnit() });
		// 报表Id
		sReportArray[jOther] = reportInfo.getReportID();

		// 显示列宽
		setColWidth(dsReportHeader, node[jOther]);

	}

	/**
	 * 收支总表
	 * 
	 * @param reportInfo
	 * @param jOther
	 * @param sDivName
	 * @throws Exception
	 */
	private void exportExcelSzzb(ReportInfo reportInfo, int jOther,
			String sDivName) throws Exception {
		String sReportId = reportInfo.getReportID();
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		// 报表node
		DataSet dsReportHeader = qrBudgetServ.getReportHeader(sReportId,
				Global.loginYear);
		node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
				IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
				IQrBudget.FIELD_CODE);

		lstMidView.add(null);
		lstDel.add(null);
		lstTempTable.add(null);

		// 主标题
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
		// 单位参数
		sSubTitle.add(new String[] { sDivName, "" });
		// 报表Id
		sReportArray[jOther] = reportInfo.getReportID();
		setColWidth(dsReportHeader, node[jOther]);
	}

	/**
	 * 行表
	 * 
	 * @param reportInfo
	 * @param jOther
	 * @param sDivName
	 * @throws Exception
	 */
	private void exportExcelRow(ReportInfo reportInfo, int jOther,
			String sDivName, DataSet dsReportHeader) throws Exception {

		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
				IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
				IQrBudget.FIELD_CODE);

		lstMidView.add(null);
		lstDel.add(null);
		lstTempTable.add(null);

		// 主标题
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
		// 单位参数
		sSubTitle.add(new String[] { sDivName, "" });
		// 报表Id
		sReportArray[jOther] = reportInfo.getReportID();
		setColWidth(dsReportHeader, node[jOther]);
	}

	/**
	 * 选中节点数(目录和封面表）
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean getConverCount(MyTreeNode myTreeNode,
			DataSet dsReportNameChoice) throws Exception {
		// 选中节点数
		int typeFlag;

		dsReportNameChoice.gotoBookmark(myTreeNode.getBookmark());
		typeFlag = dsReportNameChoice.fieldByName(IQrBudget.TYPE_FLAG)
				.getInteger();
		if (typeFlag == QrBudget.TYPE_COVER)
			return true;
		return false;
	}

	/**
	 * 选中节点数(目录和封面表）
	 * 
	 * @return
	 * @throws Exception
	 */
	private int getConverCount(MyTreeNode[] myTreeNode,
			DataSet dsReportNameChoice) throws Exception {
		// 选中节点数
		int nodeCount = 0;
		int typeFlag;

		for (int i = 0; i < myTreeNode.length; i++) {
			dsReportNameChoice.gotoBookmark(myTreeNode[i].getBookmark());
			typeFlag = dsReportNameChoice.fieldByName(IQrBudget.TYPE_FLAG)
					.getInteger();
			if (typeFlag == QrBudget.TYPE_COVER)
				nodeCount++;
		}
		return nodeCount;
	}

	/**
	 * 设置列宽信息
	 * 
	 * @param dsReportHeader
	 * @param node
	 * @throws Exception
	 */
	private void setColWidth(DataSet dsReportHeader, Node node)
			throws Exception {
		TableHeader tableHeader = new TableHeader(node);
		List lstFields = tableHeader.getFields();
		int iFieldDispwidth[] = new int[lstFields.size()];
		String sFieldDisformat[] = new String[lstFields.size()];
		for (int k = 0; k < lstFields.size(); k++) {
			if (!dsReportHeader.locate(IQrBudget.FIELD_CODE, lstFields.get(k)
					.toString())) {
				JOptionPane
						.showMessageDialog(Global.mainFrame,
								"部门预算查询表显示报表表头列宽发生错误!", "提示",
								JOptionPane.ERROR_MESSAGE);
				break;
			}
			iFieldDispwidth[k] = dsReportHeader.fieldByName(
					IQrBudget.FIELD_DISPWIDTH).getInteger();
			if (dsReportHeader.fieldByName(IQrBudget.FIELD_DISFORMAT)
					.getValue() != null) {
				sFieldDisformat[k] = dsReportHeader.fieldByName(
						IQrBudget.FIELD_DISFORMAT).getString();
			} else {
				sFieldDisformat[k] = "";
			}
		}
		lstFieldDispwidth.add(iFieldDispwidth);
		lstFieldDisformat.add(sFieldDisformat);
	}

	private class Exportpp implements ExportBatchProp {

		private List beforeSql;

		private List afterSql;

		private DataSet[] dsCover;

		private String[] titles;

		private List tilteChild;

		// 路径
		private String sAddress;

		public String[] getFieldTName() {

			String[] fieldTypes = new String[iOtherCount];
			for (int i = 0; i < fieldTypes.length; i++) {
				fieldTypes[i] = "FIELD_TYPE";
			}
			return fieldTypes;
		}

		public String[] getFieldFName() {
			String[] fieldTypes = new String[iOtherCount];
			for (int i = 0; i < fieldTypes.length; i++) {
				fieldTypes[i] = "field_disformat";
			}
			return fieldTypes;
		}

		public String[] getFieldWName() {
			String[] fieldTypes = new String[iOtherCount];
			for (int i = 0; i < fieldTypes.length; i++) {
				fieldTypes[i] = "field_dispwidth";
			}
			return fieldTypes;
		}

		public String[] getTitles() {
			return titles;
		}

		public List getTitle_Childs() {

			return tilteChild;
		}

		public String[] getNumberViewInTable() {
			return new String[] { "浮点型", "整型", "货币型" };
		}

		public String getDefaultAddres() {

			return sAddress;
		}

		public List getSqls_BeforeGetBody() {

			return beforeSql;
		}

		public List getSqls_AfterGetBody() {

			return afterSql;
		}

		public DataSet[] getFaceData() {
			return dsCover;
		}

		public List getWidths() {

			return null;
		}

		public List getFormats() {

			return null;
		}

		public List getTypes() {

			return null;
		}

		public void setAfterSql(List afterSql) {
			this.afterSql = afterSql;
		}

		public void setBeforeSql(List beforeSql) {
			this.beforeSql = beforeSql;
		}

		public void setDsCover(DataSet[] dsCover) {
			this.dsCover = dsCover;
		}

		public void setTilteChild(List tilteChild) {
			this.tilteChild = tilteChild;
		}

		public void setTitles(String[] titles) {
			this.titles = titles;
		}

		public String getSAddress() {
			return sAddress;
		}

		public void setSAddress(String address) {
			sAddress = address;
		}

	}

	/**
	 * 得到EName值
	 * 
	 * @param dsReportHeader表头DataSet
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
	 * 标题同名进行效验，不允许标题重复
	 * 
	 * @param sTitle
	 */
	private void checkTitleRepeat(String[] sTitle) {
		if (sTitle == null || sTitle.length == 0)
			return;
		int size = sTitle.length;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				int postfix = 1;
				if (sTitle[i].equals(sTitle[j])) {
					sTitle[j] = sTitle[j] + "_" + postfix;
					postfix++;
				}
			}
		}
	}

	public ExportToExcel getEep() {
		return eep;
	}

	public void setSPath(String path) {
		sPath = path;
	}

}
