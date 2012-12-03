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

	// ѡ�нڵ���(Ŀ¼�ͷ����
	private int iConverCount;

	// ��������ȥ��Ŀ¼�ͷ��棩
	private int iOtherCount;

	// ��ͷSql
	private String sSqlHeader[];

	// ����Sql
	private String sSqlBody[];

	// ����Node
	private Node node[];

	private String sTitle[]; // ������

	private List sSubTitle;// С����

	private String sReportArray[];// reportid

	private List lstFieldDispwidth;// ��ʾ�п�

	private List lstFieldDisformat;// ��ʾ��ʽ

	private List lstMidView; // �м���ͼ���м���ʱ��

	private List lstDel;// ɾ���м���¼

	// ����Ŀ¼���dataSet
	private DataSet dsSzzb[];

	// ����Ŀ¼����
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
	 * ������Ի���
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
			this.setTitle("����ΪExcel��");
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget,
						"����Ԥ���ѯ����Excel�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		private void init() throws Exception {
			FLabel flblTitle = new FLabel();
			flblTitle.setTitle("����Ԥ���ѯ��");
			flblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			// ��ʾ��
			dsReportNameChoice = (DataSet) qrBudget.getFtreReportName()
					.getDataSet().clone();
			ftreReportNameChoice = new CustomTree("��ѯ��", dsReportNameChoice,
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

			// ��ťPanel
			FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
			choosePanel.setAlignment(FlowLayout.CENTER);
			FButton fbtnPrint = new FButton("fbtnPrint", "��ӡ...");
			fbtnPrint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					MyTreeNode[] myTreeNode = ftreReportNameChoice
							.getSelectedNodes(true);
					if (myTreeNode.length == 0
							|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportNameChoice
									.getRoot())) {
						JOptionPane.showMessageDialog(qrBudget, "��ѡ�񵼳���ѯ��",
								"��ʾ", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					MyPrintProgressBar myPrintProgressBar = new MyPrintProgressBar(
							myTreeNode);
					myPrintProgressBar.display();
				}
			});

			FButton fbtnExcel = new FButton("fbtnExcel", "����...");
			fbtnExcel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					MyTreeNode[] myTreeNode = ftreReportNameChoice
							.getSelectedNodes(true);
					if (myTreeNode.length == 0
							|| (myTreeNode.length == 1 && myTreeNode[0] == ftreReportNameChoice
									.getRoot())) {
						JOptionPane.showMessageDialog(qrBudget, "��ѡ�񵼳���ѯ��",
								"��ʾ", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					MyProgressBar myProgressBar = new MyProgressBar(myTreeNode);
					myProgressBar.display();
				}
			});

			FButton cancelButton = new FButton("cancelButton", "ȡ��");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ReportExcelChoice.this.dispose();
				}
			});
			choosePanel.addControl(fbtnExcel);
			// choosePanel.addControl(fbtnPrint); // delete by ymq :��ʱ��ӡ�������ε�
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
		 * ������ѯ��
		 * 
		 * @param myTreeNode
		 *            ѡ�еĽڵ�
		 */
		private void doReportExcel(MyTreeNode[] myTreeNode) {
			int bResult = ReportExcel.FALSE_OPTION;
			ExportToExcel eep = null;
			ProgressBar pf = new ProgressBar(Global.mainFrame,
					"���ڵ���Excel�����Ժ򡤡���������", false);
			try {
				// �õ���λ�����ַ���
				SearchPublic searchPublic = new SearchPublic(qrBudget);
				String sDivName = searchPublic.getDivNameValue();
				// �õ���ѯbatch��ѯ����
				InfoPackage infoPackage = searchPublic.getFilter();
				if (!infoPackage.getSuccess()) {
					JOptionPane.showMessageDialog(qrBudget,
							"����Ԥ���ѯ����Excel�������󣬴�����Ϣ:"
									+ infoPackage.getsMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String sStatusWhere = infoPackage.getsMessage();

				// �õ���λ��ѯ����
				List lstDept = new ArrayList();
				List lstDiv = new ArrayList();

				MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName()
						.getRoot().getUserObject();
				if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
					searchPublic.getDivWhere(lstDept, lstDiv, qrBudget
							.getIUserType());
				}

				// ѡ�нڵ���(Ŀ¼�ͷ����
				iConverCount = getConverCount(myTreeNode);
				// ȡ��ѡ�еĽڵ���
				int iTotalSelectCount = myTreeNode.length;
				// ��������ȥ��Ŀ¼�ͷ��棩
				iOtherCount = iTotalSelectCount - iConverCount;
				if (iConverCount != 0) {
					dsSzzb = new DataSet[iConverCount];
					// ����Ŀ¼����
					sConvertTitle = new String[iConverCount];
				}

				if (iOtherCount != 0) {
					node = new Node[iOtherCount]; // ����Node
					sSqlHeader = new String[iOtherCount]; // ��ͷSql
					sSqlBody = new String[iOtherCount]; // ����Sql
					sTitle = new String[iOtherCount]; // ������sTitle
					sSubTitle = new ArrayList();// С����
					sReportArray = new String[iOtherCount]; // reportid

					lstFieldDispwidth = new ArrayList(); // ��ʾ�п�
					lstFieldDisformat = new ArrayList(); // ��ʾ��ʽ

					lstMidView = new ArrayList(); // �м���ͼ���м���ʱ��
					lstDel = new ArrayList();// ɾ���м���¼
				}
				String ename = "FIELD_ENAME";

				int jConver = 0;
				int jOther = 0;

				// �������ͱ�ʶ��
				int[] iReTypes = new int[iOtherCount];
				// �������ͷ�װ��
				Object[] oReTypes = new Object[iOtherCount];
				ReportInfo reportInfo;

				for (int i = 0; i < myTreeNode.length; i++) {
					dsReportNameChoice
							.gotoBookmark(myTreeNode[i].getBookmark());
					reportInfo = new ReportInfo(dsReportNameChoice);

					switch (reportInfo.getBussType()) {
					case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
						exportExcelConver(reportInfo, jConver, sDivName);
						jConver++;
						break;
					}
					case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�
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
						// ���ñ�ʶ��Ϊsql��������
						iReTypes[jOther] = ReportType.LikeFace;
						ReportType ret = ReportType
								.create(ReportType.LikeFace);
//						ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
						oReTypes[jOther] = ret;

						jOther++;
						break;
					}
					case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
						// ����node
						DataSet dsReportHeader = qrBudget.getQrBudgetServ()
								.getReportHeader(reportInfo.getReportID(),
										Global.loginYear);

						exportExcelRow(reportInfo, jOther, sDivName,
								dsReportHeader);
						List lstRowSearch = RowSetDispByReport.getSearchList(
								reportInfo, lstDept, lstDiv, qrBudget
										.getFpnlToolBar().getVerNo(),
								sStatusWhere, Global.loginmode);
						// ���ñ�ʶ��Ϊsql��������
						iReTypes[jOther] = ReportType.DsLst;
						ReportType ret = ReportType
								.create(ReportType.DsLst);
						ret.setParam(reportInfo.getReportID(), node[jOther],
								dsReportHeader, lstRowSearch, ename);
						oReTypes[jOther] = ret;
						jOther++;
						break;
					}
					case IDefineReport.REPORTTYPE_GROUP: { // �����
						exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
								lstDept, lstDiv, sDivName);

						// ���ñ�ʶ��Ϊsql��������
						iReTypes[jOther] = ReportType.Sql;
						ReportType ret = ReportType
								.create(ReportType.Sql);
						ret.setParam(sReportArray[jOther], node[jOther],
								sSqlHeader[jOther], sSqlBody[jOther], ename);
						oReTypes[jOther] = ret;
						jOther++;
						break;
					}
					case IDefineReport.REPORTTYPE_OTHER: {// �������ⱨ������
						exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
								lstDept, lstDiv, sDivName);

						// ���ñ�ʶ��Ϊsql��������
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

				// ����ͬ������Ч�飬����������ظ�
				this.checkTitleRepeat(sTitle);
				if (iConverCount != 0) {
					this.checkTitleRepeat(sConvertTitle);
				}

				// ���������������
				Exportpp pp = new Exportpp();
				pp.setAfterSql(lstDel);
				pp.setBeforeSql(lstMidView);
				if (iConverCount != 0) {
					pp.setDsCover(dsSzzb);
				}
				pp.setTitles(sTitle);
				pp.setTilteChild(sSubTitle);

				eep = new ExportToExcel(iReTypes, oReTypes, pp);
				eep.setTitle("��ѯ��");
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
						"���\"����\"��ť�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				pf.dispose();
			}

			if (bResult == ReportExcel.CANCEL_OPTION)
				return;
			else if (bResult == ReportExcel.TRUE_OPTION)
				new MessageBox("�����ļ��ɹ���", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			else
				new MessageBox("�����ļ�ʧ�ܣ�", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
		}

		/**
		 * ����ͬ������Ч�飬����������ظ�
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
		 * ��������
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
//				// Ϊ������������,����������Ҫ��dataset
//				dsSzzb[jConver].edit();
//				dsSzzb[jConver].fieldByName(ISzzbSet.FIELD_VALUE).setValue(
//						sDivName);
//				dsSzzb[jConver].applyUpdate();
//			}
			sConvertTitle[jConver] = dsReportNameChoice.fieldByName(
					IQrBudget.TITLE).getString();
		}

		/**
		 * ����������
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
			// ����node
			DataSet dsReportHeader = qrBudget.getQrBudgetServ()
					.getReportHeader(sReportId, Global.loginYear);
			SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(
					reportInfo.getReportID());
			if (setWhereReadWrite.isReportExists()) {
				if (setWhereReadWrite.getPfsFname() != null)
					// �ı��ͷ�ʽ���Դ��ʾ
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
			// ��ѯ�ֶ���ɵ��ַ���
			String sFieldSelect = getFieldSelect(dsReportHeader);
			// ��ͷsql
			sSqlHeader[jOther] = qrBudget.getQrBudgetServ().getReportHeaderSql(
					sReportId, Global.loginYear);

			// ����Sql
			// -------------------------------------
			// ����ֳ������֣������GROUPREPORT��Ҫ�Բ�ͬ��ʽȡ��XXL

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
					JOptionPane.showMessageDialog(qrBudget, "���ɱ�����Ϣ����������Ϣ:"
							+ e.getMessage(), "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				sSqlBody[jOther] = otherSearchObj.getSSqlBody();
				lstMidView.add(otherSearchObj.getLstSqlMidView());
				lstDel.add(otherSearchObj.getLstDeleteRecord());
			} else {// ����Ƿ���ı���
				// ����ID
				String reportId = reportInfo.getReportID();
				// ������д����
				setWhereReadWrite = new SetWhereReadWrite(reportId);
				// �����ļ������뱨���Ƿ�һ��
				boolean isSame = false;
				List lstLevIsTotalField = null;
				GroupReport groupReport = null;
				ReportQuerySource querySource = null;
				// �жϱ��ز����ļ��Ƿ��б��������
				if (setWhereReadWrite.isReportExists()) {
					IDefineReport definReportServ = DefineReportI.getMethod();
					groupReport = ReportPanel.getGroupReport(reportId,
							definReportServ);
					querySource = (ReportQuerySource) ReportConver
							.getReportQuerySource(groupReport);
					// �õ�����lstLevIsTotalField
					lstLevIsTotalField = ReportUnt.geLevIsTotalField(
							groupReport, querySource);
					// �жϱ����ļ������뱨���Ƿ�һ��(�����жϱ����Ƿ��޸Ĺ���
					isSame = setWhereReadWrite
							.checkFilePara(lstLevIsTotalField);
				}
				OriSearchObj oriSearchObj;
				if (isSame) {// һ��
					// ��lstLevIsTotalFieldֵ����groupReport�ļ�
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
					GroupWhereSetPanel.changeLevValue(groupReport,
							lstLevIsTotalField, querySource);

					// ���ݱ��ز����ļ����ݣ�����lstLevIsTotalFieldֵ
					setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
					// �õ�֧���ʽ���Դֵ
					List lstFieldEname = setWhereReadWrite.getPfsCode();
					// �Ա�����
					ConditionObj conditionObj = setWhereReadWrite
							.getConditionObj();

					BuildSql buildSql = new BuildSql(groupReport);
					// �ı��ʽ���Դ
					buildSql.changetDataSourceField(lstFieldEname);
					DataSet dsHeader = qrBudget.getQrBudgetServ()
							.getReportHeader_A(reportInfo.getReportID());
					// ��֯sql
					List lstSqlLines = buildSql.getSqlLinesSql(dsHeader);
					// ��ѯ���
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
					new MessageBox("���ɱ�����Ϣ����!" + sErr, MessageBox.MESSAGE,
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

			// ������
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
			// ��λ����
			sSubTitle.add(new String[] { sDivName, reportInfo.getUnit() });
			// ����Id
			sReportArray[jOther] = reportInfo.getReportID();

			// ��ʾ�п�
			setColWidth(dsReportHeader, node[jOther]);

		}

		/**
		 * ��֧�ܱ�
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
			// ����node
			DataSet dsReportHeader = qrBudget.getQrBudgetServ()
					.getReportHeader(sReportId, Global.loginYear);
			node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
					IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
					IQrBudget.FIELD_CODE);

			lstMidView.add(null);
			lstDel.add(null);

			// ������
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
			// ��λ����
			sSubTitle.add(new String[] { sDivName, "" });
			// ����Id
			sReportArray[jOther] = reportInfo.getReportID();
			setColWidth(dsReportHeader, node[jOther]);
		}

		/**
		 * �б�
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

			// ������
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
			// ��λ����
			sSubTitle.add(new String[] { sDivName, "" });
			// ����Id
			sReportArray[jOther] = reportInfo.getReportID();
			setColWidth(dsReportHeader, node[jOther]);
		}

		/**
		 * �����п���Ϣ
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
							"����Ԥ���ѯ����ʾ�����ͷ�п�������!", "��ʾ",
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
		 * ѡ�нڵ���(Ŀ¼�ͷ����
		 * 
		 * @return
		 * @throws Exception
		 */
		private int getConverCount(MyTreeNode[] myTreeNode) throws Exception {
			// ѡ�нڵ���
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
					"���ڴ�ӡ�����Ժ򡤡���������", false);
			try {
				SearchPublic searchPublic = new SearchPublic(qrBudget);
				String sDivName = searchPublic.getDivNameValue();

				InfoPackage info;
				Report report;
				for (int i = 0; i < myTreeNode.length; i++) {
					dsReportNameChoice
							.gotoBookmark(myTreeNode[i].getBookmark());
					pf.setTitle("���ڴ�ӡ"
							+ dsReportNameChoice.fieldByName(
									IQrBudget.REPORT_CNAME).getString()
							+ "�����Ժ򡤡���������");

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
						"���\"��ӡ\"��ť�������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				if (pf != null)
					pf.dispose();
			}
		}
	}

	/**
	 * �õ�ENameֵ
	 * 
	 * @param dsReportHeader��ͷDataSet
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
			return new String[] { "������", "����", "������" };
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
	 * ��ӡ��Ҫ��report
	 * 
	 * @param reportInfo
	 * @return
	 */
	private Report getReport(ReportInfo reportInfo) {
		try {
			String sVerNo = qrBudget.getFpnlToolBar().getVerNo();
			if (reportInfo.getBussType() == QrBudget.TYPE_COVER) {// ����
				return this.getCover(reportInfo);
			} else if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// ��֧�ܱ�
				return null;
			} else if (reportInfo.getBussType() == QrBudget.TYPE_ROWSET) {// �б���
				return this.getRowSet(sVerNo, reportInfo);
			} else if (reportInfo.getBussType() == QrBudget.TYPE_GROUP) {// ���鱨��
				return this.getGroup(reportInfo);
			} else {// ��ͨ��ѯ��
				return this.getOtherReport(reportInfo);
			}
		} catch (Exception e) {
			new MessageBox("��ѯ����[" + reportInfo.getTitle() + "]����!", e
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
		// �ж��Ƿ�֧������
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		String sStatusWhere = "";
		if (reportInfo.isHasBatch()) {
			// �õ���ѯ����
			InfoPackage infoPackage = searchPublic.getFilter();
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ��ִ�в�ѯ�������󣬴�����Ϣ:"
						+ infoPackage.getsMessage(), "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return null;

			}
			sStatusWhere = infoPackage.getsMessage();
		}

		// �õ���λ
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		}

		DataSet dsReportHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());
		List lstCols = new ArrayList();// ��¼����Ϣ
		Report report = (gov.nbcs.rp.common.ui.report.Report) ReportFactory
				.getReportWithHead(null, dsReportHeader, true, lstCols,
						reportInfo);

		// ȡ�ò�ѯ�ı��ֶ�
		String sFieldSelect = getFieldSelect(dsReportHeader);

		List lstResult;
		try {
			lstResult = qrBudget.getQrBudgetServ().getData(
					qrBudget.getFpnlToolBar().getVerNo(),
					reportInfo.getReportID(), sStatusWhere, lstDept, lstDiv,
					sFieldSelect, Global.loginYear, qrBudget.getIUserType(),
					Global.loginmode, reportInfo.getBussType());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(qrBudget, "����Ԥ���ѯ��ִ�в�ѯ�������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		List lstList = (List) lstResult.get(0);

		// ȡ�ò�ѯ�ı��ֶ�
		report = ReportFactory.getOriReportWithData(lstList, report, lstCols);

		return report;

	}

	private Report getGroup(ReportInfo reportInfo) throws Exception {
		// �õ���λ
		List lstDept = new ArrayList();
		List lstDiv = new ArrayList();
		List lstDivName = new ArrayList();

		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			searchPublic.getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
		} else {
			// ���û��ѡ�е�λ��λ���������ĵ�һ���ڵ����
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

		// ����ѯ���ת����LIST
		List lstMid = new ArrayList();
		List lstFirst = new ArrayList();
		lstFirst.add(oriSearchObj.getSearchSql());
		lstMid.add(lstFirst);
		List lstLast = new ArrayList();
		lstLast.add(oriSearchObj.getDropTableSql());
		lstMid.add(lstLast);

		List lstCols = new ArrayList();

		// ȡ�ñ���Ϣ
		DataSet dsReportHeader = QrBudgetI.getMethod().getReportHeader(
				reportInfo.getReportID(), Global.getSetYear());

		Report report = (Report) ReportFactory.getReportWithHead(null,
				dsReportHeader, true, lstCols, reportInfo);

		// ȡ�ò�ѯ�ı��ֶ�
		report = ReportFactory.getOriReportWithData(lstData, report, lstCols);
		return report;
	}

	/**
	 * ��֧�ܱ�
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
