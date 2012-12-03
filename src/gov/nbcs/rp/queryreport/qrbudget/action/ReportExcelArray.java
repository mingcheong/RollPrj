package gov.nbcs.rp.queryreport.qrbudget.action;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.export.action.ExportBatchProp;
import gov.nbcs.rp.common.export.action.ExportToExcel;
import gov.nbcs.rp.common.export.reporttypes.ReportType;
import gov.nbcs.rp.common.print.PrintUtility;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.progress.ProgressBar;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
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
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportFactory;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportHeaderOpe;
import gov.nbcs.rp.queryreport.qrbudget.ui.ReportInfo;
import gov.nbcs.rp.queryreport.rowset.ui.RowSetDispByReport;

import gov.nbcs.rp.sys.besqryreport.action.BesAct;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.CommonAction;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.fr.report.GroupReport;

public class ReportExcelArray extends CommonAction {

	private static final long serialVersionUID = 1L;

	private QrBudget qrBudget;

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

	public void actionPerformed(ActionEvent arg0) {
		this.qrBudget = (QrBudget) this.getModulePanel();
		FModulePanel modulePanel = this.getModulePanel();
		if (modulePanel instanceof QrBudget) {
			QrBudget qrBudget = (QrBudget) modulePanel;
			ReportExcelChoice reportExcelChoice = new ReportExcelChoice(
					qrBudget);
			Tools.centerWindow(reportExcelChoice);
			reportExcelChoice.setVisible(true);
			reportExcelChoice.dispose();
		}
	}

	/**
	 * 多表导出对话框
	 * 
	 * @author qzc
	 * 
	 */
	private class ReportExcelChoice extends FDialog {

		private static final long serialVersionUID = 1L;

		private CustomTree ftreReportNameChoice;

		private DataSet dsReportNameChoice;

		public ReportExcelChoice(QrBudget qrBudget) {
			super(Global.mainFrame);
			this.setSize(400, 360);
			this.setResizable(false);
			this.dispose();
			this.setModal(true);
			this.setTitle("导出为Excel表");
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget,
						"部门预算查询表导出Excel发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		private void init() throws Exception {
			FLabel flblTitle = new FLabel();
			flblTitle.setTitle("部门预算查询表");
			flblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			// 显示树
			dsReportNameChoice = (DataSet) qrBudget.getFtreReportName()
					.getDataSet().clone();
			ftreReportNameChoice = new CustomTree("查询表", dsReportNameChoice,
					IDefineReport.SHOW_LVL, IQrBudget.REPORT_CNAME,
					IDefineReport.PAR_ID, null, IQrBudget.LVL_ID, true);
			ftreReportNameChoice.setIsCheckBoxEnabled(true);
			FScrollPane fScroll = new FScrollPane(ftreReportNameChoice);
			FPanel fPanelReportCol = new FPanel();
			fPanelReportCol.setTopInset(5);
			fPanelReportCol.setLeftInset(5);
			fPanelReportCol.setRightInset(5);
			fPanelReportCol.setLayout(new RowPreferedLayout(1));
			fPanelReportCol.addControl(fScroll);

			// 按钮Panel
			FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
			choosePanel.setAlignment(FlowLayout.CENTER);
			FButton fbtnPrint = new FButton("fbtnPrint", "打印...");
			fbtnPrint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					MyTreeNode[] myTreeNode = ftreReportNameChoice
							.getSelectedNodes(true);
					if (myTreeNode.length == 0
							|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportNameChoice
									.getRoot())) {
						JOptionPane.showMessageDialog(qrBudget, "请选择导出查询表！",
								"提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					MyPrintProgressBar myPrintProgressBar = new MyPrintProgressBar(
							myTreeNode);
					myPrintProgressBar.display();
				}
			});

			FButton fbtnExcel = new FButton("fbtnExcel", "导出...");
			fbtnExcel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					MyTreeNode[] myTreeNode = ftreReportNameChoice
							.getSelectedNodes(true);
					if (myTreeNode.length == 0
							|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportNameChoice
									.getRoot())) {
						JOptionPane.showMessageDialog(qrBudget, "请选择导出查询表！",
								"提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					MyProgressBar myProgressBar = new MyProgressBar(myTreeNode);
					myProgressBar.display();
				}
			});

			FButton cancelButton = new FButton("cancelButton", "取消");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ReportExcelChoice.this.dispose();
				}
			});
			choosePanel.addControl(fbtnExcel);
			// choosePanel.addControl(fbtnPrint); // delete by ymq :暂时打印功能屏蔽掉
			choosePanel.addControl(cancelButton);

			RowPreferedLayout rLay = new RowPreferedLayout(1);
			rLay.setRowHeight(15);
			this.getContentPane().setLayout(rLay);
			this.getContentPane().add(flblTitle,
					new TableConstraints(1, 1, false, true));
			this.getContentPane().add(fPanelReportCol,
					new TableConstraints(1, 1, true, true));
			this.getContentPane().add(choosePanel,
					new TableConstraints(2, 1, false, true));
		}

		/**
		 * 导出查询表
		 * 
		 * @param myTreeNode
		 *            选中的节点
		 */
		private void doReportExcel(MyTreeNode[] myTreeNode) {
			int bResult = ReportExcel.FALSE_OPTION;
			ExportToExcel eep = null;
			ProgressBar pf = new ProgressBar(Global.mainFrame,
					"正在导出Excel，请稍候······", false);
			try {
				// 得到单位名称字符串
				SearchPublic searchPublic = new SearchPublic(qrBudget);
				String sDivName = searchPublic.getDivNameValue();
				// 得到查询batch查询条件
				InfoPackage infoPackage = searchPublic.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(qrBudget,
							"部门预算查询表导出Excel发生错误，错误信息:"
									+ infoPackage.getsMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sStatusWhere = infoPackage.getsMessage();

				// 得到单位查询条件
				List lstDept = new ArrayList();
				List lstDiv = new ArrayList();

				MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName()
						.getRoot().getUserObject();
				if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
					searchPublic.getDivWhere(lstDept, lstDiv, qrBudget
							.getIUserType());
				}

				// 选中节点数(目录和封面表）
				iConverCount = getConverCount(myTreeNode);
				// 取得选中的节点数
				int iTotalSelectCount = myTreeNode.length;
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

				for (int i = 0; i < myTreeNode.length; i++) {
					dsReportNameChoice
							.gotoBookmark(myTreeNode[i].getBookmark());
					reportInfo = new ReportInfo(dsReportNameChoice);

					switch (reportInfo.getBussType()) {
					case IDefineReport.REPORTTYPE_COVER: {// 目录或封面
						exportExcelConver(reportInfo, jConver, sDivName);
						jConver++;
						break;
					}
					case IDefineReport.REPORTTYPE_SZZB: {// 收支总表
						exportExcelSzzb(reportInfo, jOther, sDivName);

//						DataSet dsSzzbDisp = SzzbDispByReport
//								.getReportDsByValue(qrBudget.getFpnlToolBar()
//										.getVerNo(), reportInfo.getReportID(),
//										sStatusWhere, lstDept, lstDiv, qrBudget
//												.getSFieldSelect(),
//										Global.loginYear, reportInfo
//												.getBussType(), SzzbSetI
//												.getMethod()
//												.isSZReportNeedConvert(
//														reportInfo
//																.getReportID()));
						// 设置标识符为sql导出类型
						iReTypes[jOther] = ReportType.LikeFace;
						ReportType ret = ReportType
								.create(ReportType.LikeFace);
//						ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
						oReTypes[jOther] = ret;

						jOther++;
						break;
					}
					case IDefineReport.REPORTTYPE_ROW: {// 单位综合情况表
						// 报表node
						DataSet dsReportHeader = qrBudget.getQrBudgetServ()
								.getReportHeader(reportInfo.getReportID(),
										Global.loginYear);

						exportExcelRow(reportInfo, jOther, sDivName,
								dsReportHeader);
						List lstRowSearch = RowSetDispByReport.getSearchList(
								reportInfo, lstDept, lstDiv, qrBudget
										.getFpnlToolBar().getVerNo(),
								sStatusWhere, Global.loginmode);
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
						ReportType ret = ReportType
								.create(ReportType.Sql);
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
						ReportType ret = ReportType
								.create(ReportType.Sql);
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

				eep = new ExportToExcel(iReTypes, oReTypes, pp);
				eep.setTitle("查询表");
				eep.setFaceTitles(sConvertTitle);
				if (eep.doExport()) {
					bResult = ReportExcel.TRUE_OPTION;
				} else {
					if (eep.getIsCancel()) {
						bResult = ReportExcel.CANCEL_OPTION;
					} else {
						bResult = ReportExcel.FALSE_OPTION;
					}
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(ReportExcelChoice.this,
						"点击\"导出\"按钮发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				pf.dispose();
			}

			if (bResult == ReportExcel.CANCEL_OPTION)
				return;
			else if (bResult == ReportExcel.TRUE_OPTION)
				new MessageBox("导出文件成功！", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			else
				new MessageBox("导出文件失败！", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
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

		/**
		 * 导出封面
		 * 
		 * @throws Exception
		 * 
		 */
		private void exportExcelConver(ReportInfo reportInfo, int jConver,
				String sDivName) throws Exception {
			String sReportId = reportInfo.getReportID();
//			dsSzzb[jConver] = SzzbSetI.getMethod().getSzzb(sReportId,
//					Global.loginYear);
//			dsSzzb[jConver].beforeFirst();
//			while (dsSzzb[jConver].next()) {
//				if (dsSzzb[jConver].fieldByName(ISzzbSet.FIELD_PARA).getValue() == null)
//					continue;
//				if (!"DIV_NAME".equals(dsSzzb[jConver].fieldByName(
//						ISzzbSet.FIELD_PARA).getString())) {
//					continue;
//				}
//				// 为导出报表设置,导出报表需要传dataset
//				dsSzzb[jConver].edit();
//				dsSzzb[jConver].fieldByName(ISzzbSet.FIELD_VALUE).setValue(
//						sDivName);
//				dsSzzb[jConver].applyUpdate();
//			}
			sConvertTitle[jConver] = dsReportNameChoice.fieldByName(
					IQrBudget.TITLE).getString();
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
			DataSet dsReportHeader = qrBudget.getQrBudgetServ()
					.getReportHeader(sReportId, Global.loginYear);
			SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(
					reportInfo.getReportID());
			if (setWhereReadWrite.isReportExists()) {
				if (setWhereReadWrite.getPfsFname() != null)
					// 改变表头资金来源显示
					dsReportHeader = ReportHeaderOpe.changeDataSource(
							dsReportHeader, setWhereReadWrite.getPfsFname());
				ConditionObj conditionObj = setWhereReadWrite.getConditionObj();
				if (conditionObj != null) {
//					DataSet dsDataType = PubInterfaceStub.getMethod()
//					.getOptDataTypeAllList();
					DataSet dsDataType = null;
					if (dsDataType.locate("BatchNo", conditionObj
							.getOneBatchNo(), "DataType", conditionObj
							.getOneDataType())) {
						dsReportHeader = ReportHeaderOpe.compareReport(
								dsReportHeader, conditionObj.getOneYear()
										+ dsDataType.fieldByName("name")
												.getString(),
								ReportHeader.COMPARE_1);
					}
					if (dsDataType.locate("BatchNo", conditionObj
							.getTwoBatchNo(), "DataType", conditionObj
							.getTwoDataType())) {
						dsReportHeader = ReportHeaderOpe.compareReport(
								dsReportHeader, conditionObj.getTwoYear()
										+ dsDataType.fieldByName("name")
												.getString(),
								ReportHeader.COMPARE_2);
					}

				}
			}
			if (reportInfo.getBussType() == QrBudget.TYPE_GROUP) {
			}

			node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
					IQrBudget.FIELD_CODE);
			// 查询字段组成的字符串
			String sFieldSelect = getFieldSelect(dsReportHeader);
			// 表头sql
			sSqlHeader[jOther] = qrBudget.getQrBudgetServ().getReportHeaderSql(
					sReportId, Global.loginYear);

			// 表体Sql
			// -------------------------------------
			// 这里分成两部分，如果是GROUPREPORT则要以不同方式取得XXL

			if (reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
				OtherSearchObj otherSearchObj = null;
				try {
					if (reportInfo.isHasBatch()) {
						otherSearchObj = qrBudget.getQrBudgetServ()
								.getOtherSearchObj(
										qrBudget.getFpnlToolBar().getVerNo(),
										sReportId, sStatusWhere, lstDept,
										lstDiv, sFieldSelect, Global.loginYear,
										qrBudget.getIUserType(),
										Global.loginmode);
					} else {
						otherSearchObj = qrBudget.getQrBudgetServ()
								.getOtherSearchObj(
										qrBudget.getFpnlToolBar().getVerNo(),
										sReportId, "", lstDept, lstDiv,
										sFieldSelect, Global.loginYear,
										qrBudget.getIUserType(),
										Global.loginmode);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(qrBudget, "生成报表信息出错，错误信息:"
							+ e.getMessage(), "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				sSqlBody[jOther] = otherSearchObj.getSSqlBody();
				lstMidView.add(otherSearchObj.getLstSqlMidView());
				lstDel.add(otherSearchObj.getLstDeleteRecord());
			} else {// 如果是分组的报表
				// 报表ID
				String reportId = reportInfo.getReportID();
				// 参数读写条件
				setWhereReadWrite = new SetWhereReadWrite(reportId);
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
					lstLevIsTotalField = ReportUnt.geLevIsTotalField(
							groupReport, querySource);
					// 判断本地文件参数与报表是否一致(用于判断报表是否修改过）
					isSame = setWhereReadWrite
							.checkFilePara(lstLevIsTotalField);
				}
				OriSearchObj oriSearchObj;
				if (isSame) {// 一致
					// 将lstLevIsTotalField值更新groupReport文件
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
					GroupWhereSetPanel.changeLevValue(groupReport,
							lstLevIsTotalField, querySource);

					// 根据本地参数文件内容，更改lstLevIsTotalField值
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
					// 得到支出资金来源值
					List lstFieldEname = setWhereReadWrite.getPfsCode();
					// 对比条件
					ConditionObj conditionObj = setWhereReadWrite
							.getConditionObj();

					BuildSql buildSql = new BuildSql(groupReport);
					// 改变资金来源
					buildSql.changetDataSourceField(lstFieldEname);
					DataSet dsHeader = qrBudget.getQrBudgetServ()
							.getReportHeader_A(reportInfo.getReportID());
					// 组织sql
					List lstSqlLines = buildSql.getSqlLinesSql(dsHeader);
					// 查询语句
					oriSearchObj = qrBudget.getQrBudgetServ()
							.getSearchObjWhere(
									qrBudget.getFpnlToolBar().getVerNo(),
									sStatusWhere, lstDept, lstDiv,
									Global.loginYear, qrBudget.getIUserType(),
									Global.loginmode, reportInfo.getBussType(),
									reportInfo, lstSqlLines, conditionObj);

				} else {
					oriSearchObj = qrBudget.getQrBudgetServ().getSearchObj(
							qrBudget.getFpnlToolBar().getVerNo(), sStatusWhere,
							lstDept, lstDiv, Global.loginYear,
							qrBudget.getIUserType(), Global.loginmode,
							reportInfo.getBussType(), reportInfo,
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

			}

			// 主标题
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
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
			DataSet dsReportHeader = qrBudget.getQrBudgetServ()
					.getReportHeader(sReportId, Global.loginYear);
			node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
					IQrBudget.FIELD_CODE);

			lstMidView.add(null);
			lstDel.add(null);

			// 主标题
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
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

			// 主标题
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
			// 单位参数
			sSubTitle.add(new String[] { sDivName, "" });
			// 报表Id
			sReportArray[jOther] = reportInfo.getReportID();
			setColWidth(dsReportHeader, node[jOther]);
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
				if (!dsReportHeader.locate(IQrBudget.FIELD_CODE, lstFields.get(
						k).toString())) {
					JOptionPane.showMessageDialog(qrBudget,
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

		/**
		 * 选中节点数(目录和封面表）
		 * 
		 * @return
		 * @throws Exception
		 */
		private int getConverCount(MyTreeNode[] myTreeNode) throws Exception {
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

		private class MyProgressBar implements Runnable {
			MyTreeNode[] myTreeNode;

			public MyProgressBar(MyTreeNode[] myTreeNode) {
				this.myTreeNode = myTreeNode;
			}

			public void display() {
				Thread myThread = new Thread(this);
				myThread.start();
			}

			public void run() {
				doReportExcel(myTreeNode);

			}
		}

		private class MyPrintProgressBar implements Runnable {
			MyTreeNode[] myTreeNode;

			public MyPrintProgressBar(MyTreeNode[] myTreeNode) {
				this.myTreeNode = myTreeNode;
			}

			public void display() {
				Thread myThread = new Thread(this);
				myThread.start();
			}

			public void run() {
				doPrint(myTreeNode);
			}
		}

		private void doPrint(MyTreeNode[] myTreeNode) {
			ProgressBar pf = new ProgressBar(Global.mainFrame,
					"正在打印，请稍候······", false);
			try {
				SearchPublic searchPublic = new SearchPublic(qrBudget);
				String sDivName = searchPublic.getDivNameValue();

				InfoPackage info;
				Report report;
				for (int i = 0; i < myTreeNode.length; i++) {
					dsReportNameChoice
							.gotoBookmark(myTreeNode[i].getBookmark());
					pf.setTitle("正在打印"
							+ dsReportNameChoice.fieldByName(
									IQrBudget.REPORT_CNAME).getString()
							+ "，请稍候······");

					report = getReport(new ReportInfo(dsReportNameChoice));

					// String filePath = "c:\\"
					// + dsReportNameChoice.fieldByName(
					// IQrBudget.REPORT_ID).getString() + ".xls";
					// FileOutputStream outputStream = new FileOutputStream(
					// new File(filePath));
					// ExcelExporter excelExporter = new ExcelExporter(
					// outputStream);
					// excelExporter.exportReport(report);

					info = PrintUtility.print(report, dsReportNameChoice
							.fieldByName(IQrBudget.REPORT_ID).getString(),
							Global.loginYear, false, new String[] { sDivName,
									"" });

					if (info.getSuccess() == false)
						JOptionPane.showMessageDialog(Global.mainFrame, info
								.getsMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ReportExcelChoice.this,
						"点击\"打印\"按钮发生错误，错误信息:" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (pf != null)
					pf.dispose();
			}
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

	private class Exportpp implements ExportBatchProp {

		private List beforeSql;

		private List afterSql;

		private DataSet[] dsCover;

		private String[] titles;

		private List tilteChild;

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

			return null;
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

	}

	/**
	 * 打印需要的report
	 * 
	 * @param reportInfo
	 * @return
	 */
	private Report getReport(ReportInfo reportInfo) {
		try {
			String sVerNo = qrBudget.getFpnlToolBar().getVerNo();
			if (reportInfo.getBussType() == QrBudget.TYPE_COVER) {// 封面
				return this.getCover(reportInfo);
			} else if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// 收支总表
				return null;
			} else if (reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {// 行报表
				return this.getRowSet(sVerNo, reportInfo);
			} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP) {// 分组报表
				return this.getGroup(reportInfo);
			} else {// 普通查询表
				return this.getOtherReport(reportInfo);
			}
		} catch (Exception e) {
			new MessageBox("查询报表[" + reportInfo.getTitle() + "]出错!", e
					.getMessage(), MessageBox.ERROR, MessageBox.BUTTON_OK)
					.show();
			return null;
		}
	}

	private Report getCover(ReportInfo reportInfo) throws Exception {
		Report report = new gov.nbcs.rp.common.ui.report.Report();

//		DataSet dsSzzb = SzzbSetI.getMethod().getSzzb(reportInfo.getReportID(),
//				Global.loginYear);
//		ReportHeaderShow.setCover(report, dsSzzb);
//		ExeSearch.setConver(report, reportInfo, dsSzzb, qrBudget);
//		return report;
		return null;
	}

	private Report getOtherReport(ReportInfo reportInfo) throws Exception {
		// 判断是否支持批次
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		String sStatusWhere = "";
		if (reportInfo.isHasBatch()) {
			// 得到查询条件
			InfoPackage infoPackage = searchPublic.getFilter();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
						+ infoPackage.getsMessage(), "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return null;

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

		DataSet dsReportHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());
		List lstCols = new ArrayList();// 记录列信息
		Report report = (gov.nbcs.rp.common.ui.report.Report) ReportFactory
				.getReportWithHead(null, dsReportHeader, true, lstCols,
						reportInfo);

		// 取得查询的表字段
		String sFieldSelect = getFieldSelect(dsReportHeader);

		List lstResult;
		try {
			lstResult = qrBudget.getQrBudgetServ().getData(
					qrBudget.getFpnlToolBar().getVerNo(),
					reportInfo.getReportID(), sStatusWhere, lstDept, lstDiv,
					sFieldSelect, Global.loginYear, qrBudget.getIUserType(),
					Global.loginmode, reportInfo.getBussType());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(qrBudget, "部门预算查询表执行查询发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		List lstList = (List) lstResult.get(0);

		// 取得查询的表字段
		report = ReportFactory.getOriReportWithData(lstList, report, lstCols);

		return report;

	}

	private Report getGroup(ReportInfo reportInfo) throws Exception {
		// 得到单位
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		List lstDivName = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
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

		IQrBudget qrBudgetBO = (IQrBudget) BOCache.getBO("fb.qrBudgetService");

		List lstResult = qrBudgetBO.getOriData(qrBudget.getFpnlToolBar()
				.getVerNo(), String.valueOf(iBatchNO), lstDept, lstDiv,
				Global.loginYear, qrBudget.getIUserType(), Global.loginmode,
				reportInfo.getBussType(), reportInfo, new ConditionObj());
		if (lstResult == null || lstResult.size() < 2)
			return null;
		List lstData = (List) lstResult.get(0);
		OriSearchObj oriSearchObj = (OriSearchObj) lstResult.get(1);

		// 将查询语句转换成LIST
		List lstMid = new ArrayList();
		List lstFirst = new ArrayList();
		lstFirst.add(oriSearchObj.getSearchSql());
		lstMid.add(lstFirst);
		List lstLast = new ArrayList();
		lstLast.add(oriSearchObj.getDropTableSql());
		lstMid.add(lstLast);

		List lstCols = new ArrayList();

		// 取得表信息
		DataSet dsReportHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());

		Report report = (Report) ReportFactory.getReportWithHead(null,
				dsReportHeader, true, lstCols, reportInfo);

		// 取得查询的表字段
		report = ReportFactory.getOriReportWithData(lstData, report, lstCols);
		return report;
	}

	/**
	 * 收支总表
	 * 
	 * @param reportInfo
	 * @return
	 * @throws Exception
	 */

	private Report getRowSet(String sVerNo, ReportInfo reportInfo)
			throws Exception {
		Report report = new gov.nbcs.rp.common.ui.report.Report();
		report = (gov.nbcs.rp.common.ui.report.Report) ReportFactory
				.getRowSetHeaderByExists(report, reportInfo);
		report = ExeSearch.getRowSetReort(sVerNo, reportInfo, report, qrBudget);
		return report;

	}
}
