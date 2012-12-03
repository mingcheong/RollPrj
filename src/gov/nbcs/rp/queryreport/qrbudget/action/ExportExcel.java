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
 * Title:�����������
 * </p>
 * <p>
 * Description:�����������
 * </p>
 * <p>

 * @version 6.2.40
 */
public class ExportExcel {

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

	private String sDivName;

	private List lstDept;

	private List lstDiv;

	private List lstTempTable = new ArrayList();

	private String sStatusWhere;

	private String sVerNo;

	private boolean isNotExportWhenEmpty;

	// ���ݿ�����
	private IQrBudget qrBudgetServ;

	// �û����ͣ�1��ҵ���ң���λ
	private int iUserType;

	private ExportToExcel eep;

	private String sFileType;

	private String sPath = "C:\\Documents and Settings\\Administrator\\My Documents\\";

	public ExportExcel(List lstDept, List lstDiv, String sStatusWhere,
			String sVerNo, String sFileType, boolean isNotExportWhenEmpty) {
		// ���ݿ�����
		qrBudgetServ = QrBudgetI.getMethod();
		this.lstDept = lstDept;
		this.lstDiv = lstDiv;
		this.sStatusWhere = sStatusWhere;
		this.sFileType = sFileType;
		this.sVerNo = sVerNo;
		this.isNotExportWhenEmpty = isNotExportWhenEmpty;

		// 1:ҵ����,������λ
		iUserType = UntPub.FIS_VIS.equals(GlobalEx.getBelongType()) ? 1 : 0;
	}

	public ExportExcel(String sDivName, List lstDept, List lstDiv,
			String sStatusWhere, String sVerNo, boolean isNotExportWhenEmpty) {
		this(lstDept, lstDiv, sStatusWhere, sVerNo, "0", isNotExportWhenEmpty);
		this.sDivName = sDivName;
	}

	/**
	 * ������ѯ��ÿ����λ����һ���ļ���
	 * 
	 * @param myTreeNode
	 *            ѡ�еĽڵ�
	 * @throws Exception
	 */
	public void doReportExcelWithDiv(CustomTree ftreReportNameChoice,
			String sDivCodeName) throws Exception {
		MyTreeNode[] reportTreeNode = ftreReportNameChoice
				.getSelectedNodes(true);

		// ѡ�нڵ���(Ŀ¼�ͷ����
		iConverCount = getConverCount(reportTreeNode, ftreReportNameChoice
				.getDataSet());
		// ȡ��ѡ�еĽڵ���
		int iTotalSelectCount = reportTreeNode.length;
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

		for (int i = 0; i < reportTreeNode.length; i++) {
			ftreReportNameChoice.getDataSet().gotoBookmark(
					reportTreeNode[i].getBookmark());
			reportInfo = new ReportInfo(ftreReportNameChoice.getDataSet());

			switch (reportInfo.getBussType()) {
			case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
				exportExcelConver(reportInfo, jConver, sDivName,
						ftreReportNameChoice.getDataSet());
				jConver++;
				break;
			}
//			case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�
//				exportExcelSzzb(reportInfo, jOther, sDivName);
//
//				DataSet dsSzzbDisp = SzzbDispByReport.getReportDsByValue("0",
//						reportInfo.getReportID(), sStatusWhere, lstDept,
//						lstDiv, "", Global.loginYear, reportInfo.getBussType(),
//						SzzbSetI.getMethod().isSZReportNeedConvert(
//								reportInfo.getReportID()));
//				// ���ñ�ʶ��Ϊsql��������
//				iReTypes[jOther] = ReportType.LikeFace;
//				ReportType ret = (ReportType) ReportType
//						.create(ReportType.LikeFace);
//				ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
//				oReTypes[jOther] = ret;
//				jOther++;
//				break;
//			}
			case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
				// ����node
				DataSet dsReportHeader = qrBudgetServ.getReportHeader(
						reportInfo.getReportID(), Global.loginYear);

				exportExcelRow(reportInfo, jOther, sDivName, dsReportHeader);
				List lstRowSearch = RowSetDispByReport.getSearchList(
						reportInfo, lstDept, lstDiv, "0", sStatusWhere,
						Global.loginmode);
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
				ReportType ret = ReportType.create(ReportType.Sql);
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
				ReportType ret = ReportType.create(ReportType.Sql);
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
		pp.setSAddress(sPath + sDivCodeName);

		eep = new ExportToExcel(iReTypes, oReTypes, pp, true,
				isNotExportWhenEmpty, lstTempTable);
		eep.setFaceTitles(sConvertTitle);

	}

	/**
	 * ������ѯ��(ÿ����������һ���ļ���
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

		// �ж�ѡ�е��Ƿ���Ŀ¼�����
		if (getConverCount(reportTreeNode, ftreReportNameChoice.getDataSet())) {
			iConverCount = iDivLen;
		} else {
			iConverCount = 0;
		}
		// ȡ��ѡ�еĽڵ���
		int iTotalSelectCount = iDivLen;
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
			case IDefineReport.REPORTTYPE_COVER: {// Ŀ¼�����
				exportExcelConver(reportInfo, jConver, sDepOrDivName, null);
				jConver++;
				break;
			}
			case IDefineReport.REPORTTYPE_SZZB: {// ��֧�ܱ�
				exportExcelSzzb(reportInfo, jOther, sDepOrDivName);

//				DataSet dsSzzbDisp = SzzbDispByReport.getReportDsByValue("0",
//						reportInfo.getReportID(), sStatusWhere, lstDeptTmp,
//						lstDivTmp, "", Global.loginYear, reportInfo
//								.getBussType(),
//						SzzbSetI.getMethod().isSZReportNeedConvert(
//								reportInfo.getReportID()));
				// ���ñ�ʶ��Ϊsql��������
//				iReTypes[jOther] = ReportType.LikeFace;
//				ReportType ret = (ReportType) ReportType
//						.create(ReportType.LikeFace);
//				ret.setParam(reportInfo.getReportID(), dsSzzbDisp);
//				oReTypes[jOther] = ret;
//
//				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_ROW: {// ��λ�ۺ������
				// ����node
				DataSet dsReportHeader = qrBudgetServ.getReportHeader(
						reportInfo.getReportID(), Global.loginYear);

				exportExcelRow(reportInfo, jOther, sDepOrDivName,
						dsReportHeader);
				List lstRowSearch = RowSetDispByReport.getSearchList(
						reportInfo, lstDeptTmp, lstDivTmp, "0", sStatusWhere,
						Global.loginmode);
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
						lstDeptTmp, lstDivTmp, sDepOrDivName);

				// ���ñ�ʶ��Ϊsql��������
				iReTypes[jOther] = ReportType.Sql;
				ReportType ret = ReportType.create(ReportType.Sql);
				ret.setParam(sReportArray[jOther], node[jOther],
						sSqlHeader[jOther], sSqlBody[jOther], ename);
				oReTypes[jOther] = ret;
				jOther++;
				break;
			}
			case IDefineReport.REPORTTYPE_OTHER: {// �������ⱨ������
				exportExcelGroupOther(reportInfo, jOther, sStatusWhere,
						lstDeptTmp, lstDivTmp, sDepOrDivName);

				// ���ñ�ʶ��Ϊsql��������
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
		// �õ����������ַ���
		String sReportName = ftreReportNameChoice.getDataSet().fieldByName(
				IQrBudget.REPORT_CNAME).getString();
		pp.setSAddress(sPath + sReportName);

		eep = new ExportToExcel(iReTypes, oReTypes, pp, false,
				isNotExportWhenEmpty, lstTempTable);
		eep.setFaceTitles(sConvertTitle);

	}

	/**
	 * ��������
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
//			// Ϊ������������,����������Ҫ��dataset
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
		DataSet dsReportHeader = qrBudgetServ.getReportHeader(sReportId,
				Global.loginYear);
		// ��ͷ���ݣ������ʽ���Դ�ͶԱȷ���ʱ�������ز�����Ϣ
		SetWhereReadWrite setWhereReadWrite = new SetWhereReadWrite(sReportId);
		if (setWhereReadWrite.isReportExists()) {
			if (setWhereReadWrite.getPfsFname() != null)
				// �ı��ͷ�ʽ���Դ��ʾ
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
		// ��ѯ�ֶ���ɵ��ַ���
		String sFieldSelect = getFieldSelect(dsReportHeader);
		// ��ͷsql
		sSqlHeader[jOther] = qrBudgetServ.getReportHeaderSql(sReportId,
				Global.loginYear);

		// ����Sql
		// -------------------------------------
		// ����ֳ������֣������GROUPREPORT��Ҫ�Բ�ͬ��ʽȡ��XXL

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
						"���ɱ�����Ϣ����������Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			sSqlBody[jOther] = otherSearchObj.getSSqlBody();
			lstMidView.add(otherSearchObj.getLstSqlMidView());
			lstDel.add(otherSearchObj.getLstDeleteRecord());
			lstTempTable.add(otherSearchObj.getLstTempTable());
		} else {// ����Ƿ���ı���
			// ����ID
			String reportId = reportInfo.getReportID();
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
				lstLevIsTotalField = ReportUnt.geLevIsTotalField(groupReport,
						querySource);
				// �жϱ����ļ������뱨���Ƿ�һ��(�����жϱ����Ƿ��޸Ĺ���
				isSame = setWhereReadWrite.checkFilePara(lstLevIsTotalField);
			}

			OriSearchObj oriSearchObj;
			if (isSame) {// һ��
				// ���ݱ��ز����ļ����ݣ�����lstLevIsTotalFieldֵ
				setWhereReadWrite.getLevIsTotalField(lstLevIsTotalField);
				GroupWhereSetPanel.changeLevValue(groupReport,
						lstLevIsTotalField, querySource);
				// �õ�֧���ʽ���Դֵ
				List lstFieldEname = setWhereReadWrite.getPfsCode();
				// �Ա�����
				ConditionObj conditionObj = setWhereReadWrite.getConditionObj();

				BuildSql buildSql = new BuildSql(groupReport);
				// �ı��ʽ���Դ
				buildSql.changetDataSourceField(lstFieldEname);
				DataSet dsHeader = qrBudgetServ.getReportHeader_A(reportInfo
						.getReportID());
				// ��֯sql
				List lstSqlLines = buildSql.getSqlLinesSql(dsHeader);
				// ��ѯ���
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
			List lstTmp = new ArrayList();
			lstTmp.add(oriSearchObj.getTempTable());
			lstTempTable.add(lstTmp);
		}

		// ������
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
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
		DataSet dsReportHeader = qrBudgetServ.getReportHeader(sReportId,
				Global.loginYear);
		node[jOther] = hg.generate(dsReportHeader, IQrBudget.FIELD_CODE,
				IQrBudget.FIELD_CNAME, BesAct.codeRule_Col,
				IQrBudget.FIELD_CODE);

		lstMidView.add(null);
		lstDel.add(null);
		lstTempTable.add(null);

		// ������
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
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
		lstTempTable.add(null);

		// ������
		if ("0".equals(sFileType)) {
			if (reportInfo.getTitle() != null)
				sTitle[jOther] = reportInfo.getTitle();
			else
				sTitle[jOther] = "";
		} else {
			sTitle[jOther] = sDivName;
		}
		// ��λ����
		sSubTitle.add(new String[] { sDivName, "" });
		// ����Id
		sReportArray[jOther] = reportInfo.getReportID();
		setColWidth(dsReportHeader, node[jOther]);
	}

	/**
	 * ѡ�нڵ���(Ŀ¼�ͷ����
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean getConverCount(MyTreeNode myTreeNode,
			DataSet dsReportNameChoice) throws Exception {
		// ѡ�нڵ���
		int typeFlag;

		dsReportNameChoice.gotoBookmark(myTreeNode.getBookmark());
		typeFlag = dsReportNameChoice.fieldByName(IQrBudget.TYPE_FLAG)
				.getInteger();
		if (typeFlag == QrBudget.TYPE_COVER)
			return true;
		return false;
	}

	/**
	 * ѡ�нڵ���(Ŀ¼�ͷ����
	 * 
	 * @return
	 * @throws Exception
	 */
	private int getConverCount(MyTreeNode[] myTreeNode,
			DataSet dsReportNameChoice) throws Exception {
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
			if (!dsReportHeader.locate(IQrBudget.FIELD_CODE, lstFields.get(k)
					.toString())) {
				JOptionPane
						.showMessageDialog(Global.mainFrame,
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

	private class Exportpp implements ExportBatchProp {

		private List beforeSql;

		private List afterSql;

		private DataSet[] dsCover;

		private String[] titles;

		private List tilteChild;

		// ·��
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
			return new String[] { "������", "����", "������" };
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

	public ExportToExcel getEep() {
		return eep;
	}

	public void setSPath(String path) {
		sPath = path;
	}

}
