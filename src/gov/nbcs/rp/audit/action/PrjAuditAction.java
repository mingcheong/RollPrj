/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title ��Ŀ��Ч-������
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.audit.action;

import gov.nbcs.rp.audit.ibs.IPrjAudit;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.CellStyle;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.util.Global;
import com.fr.base.Style;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;

public class PrjAuditAction {

	/** ������ */

	private PrjAuditDTO pd;

	private DataSet dsPrjTreeHeader;

	private DataSet dsPrjTableHeader;

	private DataSet dsPrjStandHeader;

	private DataSet dsPrjAudit;

	private DataSet dsPrj;

	private DataSet dsPType;

	/** ���캯�� */
	public PrjAuditAction(PrjAuditDTO pd) {
		this.pd = pd;
	}

	/**
	 * ��ȡ��λ��Ϣ���ݼ�
	 * 
	 * @return
	 */
//	public DataSet getDivData() {
//		DataSet dsDiv = null;
//		try {
//			dsDiv = PubInterfaceStub.getMethod()
//					.getDivDataPop(Global.loginYear);
//			pd.setDivData(dsDiv);
//			dsDiv.addDataChangeListener(new DataChangeListener() {
//
//				private static final long serialVersionUID = 1L;
//
//				public void onDataChange(DataChangeEvent event)
//						throws Exception {
//					refreshAllPrjData();
//				}
//			});
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "��ȡ��λ��Ϣ����");
//		}
//		return dsDiv;
//	}

//	public void refreshAllPrjData() throws Exception {
//
//		if (pd.getDivData() != null && !pd.getDivData().isEmpty()
//				&& !pd.getDivData().bof() && !pd.getDivData().eof()
//				&& pd.getLReport() != null) {
//			String divCode = pd.getDivData()
//					.fieldByName(IPubInterface.DIV_CODE).getString();
//			pd.setDivCode(divCode);
//			DataSet dsBody = getMainPrjData(divCode);
//			if (dsBody.isEmpty()) {
//				if (!GlobalEx.isFisVis())
//					JOptionPane.showMessageDialog(Global.mainFrame,
//							"����λ����Ҫ��д��Ч��������");
//			}
//			dsPrj = getPrjTableDataFilter(dsBody);
//			((Report) pd.getLReportUI().getReport()).setBodyData(dsPrj);
//			String userCode = "";
//			if (!GlobalEx.isFisVis())
//				userCode = pd.getUserCode();
//			DataSet dsDetail = PrjAuditStub.getMethod().getPrjData(userCode,
//					divCode, "", Global.loginYear, null,
//					String.valueOf(pd.getBathcNo()),
//					GlobalEx.isFisVis() ? "1" : "0");
//			DataSet dsDetailNow = getPrjTableDataFilter(dsDetail);
//			pd.setPrjData(dsPrj);
//			pd.setPrjCode(null);
//			pd.setPrjTreeData(dsDetailNow);
////			refreshReportUI(0, dsPrj);
////			refreshReportUI(1, dsDetailNow);
////			pd.getPrjGeneralPanel().tableAffix.setDataSet(DataSet
////					.createClient());
////			pd.getPrjGeneralPanel().setTableProp();
//			setPraiseInfo(true);
//		}
//
//	}

	/**
	 * ��ȡ��Ŀ��Ϣ���ݼ�
	 * 
	 * @return
	 */
	public DataSet getMainPrjData(String aDivCode) {
		DataSet dsPrj = null;
		try {
			String userCode = "";
			if (!GlobalEx.isFisVis()) {
				userCode = pd.getUserCode();
			}
			dsPrj = PrjAuditStub.getMethod().getMainPrjData(userCode, aDivCode,
					Global.loginYear, null, null,
					String.valueOf(pd.getBathcNo()),
					GlobalEx.isFisVis() ? "1" : "0");
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "��ȡ����Ŀ��Ϣ����");
		}
		return dsPrj;
	}

	/**
	 * ��ȡ������Ŀ���ı�ͷ���ݼ�
	 * 
	 * @return
	 */
	public DataSet getPrjTreeHeaderData() throws Exception {
		if (dsPrjTreeHeader == null) {
			dsPrjTreeHeader = DataSet.createClient();
			dsPrjTreeHeader.append();
			dsPrjTreeHeader.fieldByName("code").setValue("prj_code");
			dsPrjTreeHeader.fieldByName("name").setValue("prj_name");
			dsPrjTreeHeader.applyUpdate();
		}
		return dsPrjTreeHeader;
	}

	/**
	 * ��ȡ������Ŀ��ı�ͷ���ݼ�
	 * 
	 * @return
	 */
//	public DataSet getPrjTableHeaderData() throws Exception {
//		if (dsPrjTableHeader == null) {
//			dsPrjTableHeader = DataSet.createClient();
//			// dsPrjTableHeader.append();
//			// dsPrjTableHeader.fieldByName("code").setValue("prj_code");
//			// dsPrjTableHeader.fieldByName("name").setValue("prj_name");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.DIV_CODE);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��λ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0001");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.DIV_NAME);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��λ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0002");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.PRJ_CODE);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0003");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.PRJ_NAME);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0004");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.MainTable.GeneralInfo.IsInputPrjAudit);
//			dsPrjTableHeader.fieldByName("NAME").setValue("�Ƿ������Ч����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0005");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.PRJACCORD_NAME);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��Ŀ��������");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0006");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.PRJ_CONTENT);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0007");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.PRJSORT_NAME);
//			dsPrjTableHeader.fieldByName("NAME").setValue("��Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0008");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.ACCT_CODE);
//			dsPrjTableHeader.fieldByName("NAME").setValue("���ܿ�Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0009");
//			dsPrjTableHeader.append();
//			dsPrjTableHeader.fieldByName("CODE").setValue(
//					IPrjAudit.PrjTable.ACCT_NAME);
//			dsPrjTableHeader.fieldByName("NAME").setValue("���ܿ�Ŀ����");
//			dsPrjTableHeader.fieldByName("LVLID").setValue("0010");
//			dsPrjTableHeader = getFundsTableData(dsPrjTableHeader);
//			dsPrjTableHeader.applyUpdate();
//		}
//		return dsPrjTableHeader;
//	}

	/**
	 * ��ȡ��Ŀ��Ч���۵ı�ͷ���ݼ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjStandHeaderData() throws Exception {
		if (this.dsPrjStandHeader == null) {
			dsPrjStandHeader = DataSet.createClient();
			dsPrjStandHeader.append();
			dsPrjStandHeader.fieldByName("code").setValue(
					IPrjAudit.DetailInfo.Index.SingleIndex);
			dsPrjStandHeader.fieldByName("name").setValue("2������ָ��");
			dsPrjStandHeader.fieldByName("LVLID").setValue("0003");
			dsPrjStandHeader.append();
			dsPrjStandHeader.fieldByName("code").setValue(
					IPrjAudit.DetailInfo.Index.PubIndex);
			dsPrjStandHeader.fieldByName("name").setValue("1������ָ��");
			dsPrjStandHeader.fieldByName("LVLID").setValue("0002");
			dsPrjStandHeader.append();
			dsPrjStandHeader.fieldByName("code").setValue(
					IPrjAudit.DetailInfo.Index.DETAILNAME);
			dsPrjStandHeader.fieldByName("name").setValue("��Ч������Ҫָ��");
			dsPrjStandHeader.fieldByName("LVLID").setValue("0001");
			dsPrjStandHeader.applyUpdate();
		}
		return dsPrjStandHeader;
	}

	/**
	 * ��ȡ��Ŀ��Ч���۵ı�ͷ���ݼ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjStandBodyData(String parCode, String setYear)
			throws Exception {
		DataSet ds = PrjAuditStub.getMethod().getPriaseStandData(parCode,
				Global.loginYear, String.valueOf(pd.getBathcNo()),
				GlobalEx.isFisVis() ? "1" : "0");
		if (ds.isEmpty()) {
			getPrjStandDataWhenNull(ds, pd.getPrjCode(), Global.loginYear);
		}
		pd.setStandardData(ds);
		return ds;
	}

	/**
	 * ����ȡ����ָ��Ϊ��ʱ���ص����ݼ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataSet getPrjStandDataWhenNull(DataSet ds, String parCode,
			String setYear) throws Exception {
		String[] sortnames = IPrjAudit.DetailInfo.Index.SortNames;
		for (int i = 0; i < sortnames.length; i++) {
			// ���Ӵ���
			addPrjStandData(ds, sortnames[i], "000" + i, parCode, setYear, true);
			// �����������������������N����ϸ
			addPrjStandData(ds, String.valueOf(i), "000" + i, parCode, setYear,
					false);
		}
		return ds;
	}

	/**
	 * ����һ����¼
	 * 
	 * @param sortCode
	 * @throws Exception
	 */
	private void addPrjStandData(DataSet ds, String name, String sortCode,
			String parCode, String setYear, boolean isSort) throws Exception {
		int count = 1;
		if (!isSort) {
			count = IPrjAudit.IndexRowCount;
		}
		for (int i = 0; i < count; i++) {
			ds.append();
			if (isSort) {
				ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILCODE).setValue(
						sortCode);
				ds.fieldByName(IPrjAudit.DetailInfo.Index.ENDFLAG).setValue("0");
				ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME).setValue(
						name);
			} else {
				ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILCODE).setValue(
						sortCode + "000" + i);
				ds.fieldByName(IPrjAudit.DetailInfo.Index.ENDFLAG).setValue("1");
				switch (Integer.parseInt(name)) {
				case 0:
					switch (i) {
					case 0:
						ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
								.setValue("1��Ԥ��ִ�����ָ��");
						break;
					case 1:
						ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
								.setValue("2������������ָ��");
						break;
					default:
						break;
					}
					break;
				case 1:
					switch (i) {
					case 0:
						ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
								.setValue("1�����Ч��ָ��");
						break;
					case 1:
						ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
								.setValue("2������Ч��ָ��");
						break;
					case 2:
						ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
								.setValue("3����̬����Ч��ָ��");
						break;
					default:
						break;
					}
					break;
				case 2:
					ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
							.setValue("");
					break;
				case 3:
					ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
							.setValue("");
					break;
				default:
					ds.fieldByName(IPrjAudit.DetailInfo.Index.DETAILNAME)
							.setValue("");
					break;
				}
			}

			ds.fieldByName(IPrjAudit.DetailInfo.Index.SortCode).setValue(
					sortCode);
			ds.fieldByName(IPrjAudit.DetailInfo.Index.PARCODE).setValue(parCode);
			ds.fieldByName(IPrjAudit.DetailInfo.Index.SETYEAR).setValue(setYear);
			ds.fieldByName(IPrjAudit.PrjTable.DIV_CODE)
					.setValue(pd.getDivCode());
			ds.fieldByName("BATCH_NO")
					.setValue(String.valueOf(pd.getBathcNo()));
			ds.fieldByName("DATA_TYPE").setValue(
					GlobalEx.isFisVis() ? "1" : "0");
		}
	}

	/**
	 * ���ʽ���Դ�ŵ���ͷ����
	 * 
	 * @param dsHeader
	 * @return
	 * @throws Exception
	 */
	private DataSet getFundsTableData(DataSet dsHeader) throws Exception {
		DataSet dsFunds = PrjAuditStub.getMethod().getFundsData();
		if ((dsFunds != null) && !dsFunds.isEmpty()) {
			dsFunds.beforeFirst();
			while (dsFunds.next()) {
				dsPrjTableHeader.append();
				if ("1".equals(dsFunds.fieldByName("END_FLAG").getString())) {
					dsPrjTableHeader.fieldByName("CODE").setValue(
							dsFunds.fieldByName("PFS_ENAME").getString()
									.toUpperCase());
				}
				String pfsCode = dsFunds.fieldByName("LVL_ID").getString();
				pfsCode = "1" + pfsCode.substring(1, pfsCode.length());
				dsPrjTableHeader.fieldByName("LVLID").setValue(pfsCode);
				dsPrjTableHeader.fieldByName("NAME").setValue(
						dsFunds.fieldByName("PFS_NAME").getString());
			}
		}
		return dsHeader;
	}

	/**
	 * ˢ����Ŀ���� i: 0:��Ŀ���� 1����Ŀ�б� 2:ָ��
	 * 
	 * @throws Exception
	 */
//	public void refreshReportUI(int i, DataSet dsBody) throws Exception {
//		// ����reportUI��Ӧ��������
//		DataSet dsHeader = null;
//		TableHeader tableHeader = null;
//		ReportUI reportUI = null;
//		Report report = null;
//		PropertyProvider pp = null;
//		if (i == 0) {
//			dsHeader = getPrjTreeHeaderData();
//			tableHeader = HeaderUtility.createHeader(dsHeader, "CODE", "NAME",
//					"", "CODE");
//			reportUI = pd.getLReportUI();
//			report = pd.getLReport();
//			pp = new PrjAuditLeftPrjProp(dsHeader, dsBody);
//			pd.setLReportPP(pp);
//			if (pd.getisInit() && reportUI != null
//					&& pd.getPrjTreeData() != null
//					&& !pd.getPrjTreeData().isEmpty()) {
//				// ֻ�ڳ�ʼ����ʱ�o���ݼ��Ӽ����¼�
//				pd.getLReportUI().getGrid().addMouseListener(
//						new PrjTreeClickListener());
//			}
//		} else if (i == 1) {
//			dsHeader = getPrjTableHeaderData();
//			tableHeader = HeaderUtility.createHeader(dsHeader, "LVLID", "NAME",
//					UntPub.lvlRule, "LVLID");
//			reportUI = pd.getReportUI();
//			report = pd.getReport();
//			pp = new PrjAuditPrjProp(pd, 0, dsHeader, dsBody);
//			pd.setReportPP(pp);
//			if (pd.getisInit() && reportUI != null
//					&& pd.getPrjTreeData() != null
//					&& !pd.getPrjTreeData().isEmpty()) {
//				pd.getReportUI().getGrid().addMouseListener(
//						new PrjTableClickListener());
//			}
//
//		} else {
//			// ָ��
//			dsHeader = this.getPrjStandHeaderData();
//			tableHeader = HeaderUtility.createHeader(dsHeader, "LVLID", "NAME",
//					UntPub.lvlRule, "LVLID");
//			reportUI = pd.getStdReportUI();
//			report = pd.getStdReport();
//			dsBody = getPrjStandBodyData(pd.getPrjCode(), Global.loginYear);
//			pp = new PrjAuditPrjProp(pd, 1, dsHeader, dsBody);
//			pd.setStdReportPP(pp);
//			// ������ϸ
//			DataSet dsAffix = PrjAuditStub.getMethod().getAffixInfo(
//					pd.getDivCode(), pd.getPrjCode(), Global.loginYear,
//					String.valueOf(pd.getBathcNo()),
//					GlobalEx.isFisVis() ? "1" : "0");
//			pd.setAffixData(dsAffix);
//			pd.getPrjGeneralPanel().tableAffix.setDataSet(pd.getAffixData());
//			pd.getPrjGeneralPanel().setTableProp();
//		}
//		refresh(reportUI, report, pp, tableHeader, dsHeader, dsBody);
//		setReportUICell(reportUI, report, i);
//	}

	private void setReportUICell(ReportUI reportUI, Report report, int i) {
		if (reportUI != null) {
			if (i == 0) {
				// �����п�
				reportUI.getReport().setColumnWidth(0, 270);
				// �����и�
				report.setRowHeight(0, 0);
				reportUI.setRowHeaderVisible(false);
				reportUI.setColumnHeaderVisible(false);
			} else if (i == 1) {
				reportUI.getReport().setColumnWidth(0, 100);
				reportUI.getReport().setColumnWidth(1, 200);
				reportUI.getReport().setColumnWidth(2, 120);
				reportUI.getReport().setColumnWidth(3, 200);
				report.setRowHeight(0, 30);
				report.setRowHeight(1, 30);
			} else {
				report.setRowHeight(0, 30);
				reportUI.getReport().setColumnWidth(0, 250);
				reportUI.getReport().setColumnWidth(1, 250);
				reportUI.getReport().setColumnWidth(2, 250);
			}
		}
	}

	/**
	 * ��Ŀ�����������¼�
	 * 
	 * @author qzc
	 * 
	 */
//	private class PrjTreeClickListener extends MouseAdapter {
//		public void mouseClicked(MouseEvent e) {
//			try {
//				if (pd.getPrjData() != null && !pd.getPrjData().isEmpty()
//						&& !pd.getPrjData().bof() && !pd.getPrjData().eof()) {
//					if (pd.getisEdit()) {
//						// // ����Ǳ༭״̬������ʾ�Ƿ񱣴�����
//						pd.getMainUI().setButtonEditState(true);
//						pd.getMainUI().doCancel();
//					}
//					pd.setPrjCode(pd.getPrjData().fieldByName(
//							IPrjAudit.PrjTable.PRJ_CODE).getString());
//					pd.setDivCode(pd.getPrjData().fieldByName("DIV_CODE")
//							.getString());
//					if (pd.getPrjData().locate(IPrjAudit.PrjTable.PRJ_CODE,
//							pd.getPrjCode())) {
//						refreshReportUI(2, getPrjStandBodyData(pd.getPrjCode(),
//								Global.loginYear));
//					}
//					setRowToSelectPrj(0, pd.getLReportUI(), pd.getReportUI());
//					setPraiseInfo(false);
//					pd.getMainUI().setButtonEditState(pd.getisEdit());
//				} else {
//				}
//			} catch (Exception ee) {
//				ErrorInfo.showErrorDialog(ee, "��ȡ��ϸ��Ŀ��Ϣ����");
//			}
//		}
//	}

	/**
	 * ��Ŀ�б��������¼�
	 * 
	 * @author qzc
	 * 
	 */
//	private class PrjTableClickListener extends MouseAdapter {
//
//		public void mouseClicked(MouseEvent e) {
//			try {
//				pd.setisInit(false);
//				if (pd.getPrjData() != null && !pd.getPrjTreeData().isEmpty()
//						&& !pd.getPrjTreeData().bof()
//						&& !pd.getPrjTreeData().eof()) {
//					if (pd.getisEdit()) {
//						// ����Ǳ༭״̬������ʾ�Ƿ񱣴�����
//						pd.getMainUI().setButtonEditState(true);
//						pd.getMainUI().doCancel();
//					}
//					pd.setPrjCode(pd.getPrjTreeData().fieldByName(
//							IPrjAudit.PrjTable.PRJ_CODE).getString());
//					pd.setDivCode(pd.getPrjTreeData().fieldByName("DIV_CODE")
//							.getString());
//					refreshReportUI(2, getPrjStandBodyData(pd.getPrjCode(),
//							Global.loginYear));
//					if (pd.getPrjAuditData() != null
//							&& !pd.getPrjAuditData().isEmpty()
//							&& pd.getPrjAuditData().locate(
//									IPrjAudit.PrjTable.PRJ_CODE,
//									pd.getPrjTreeData().fieldByName(
//											IPrjAudit.PrjTable.PRJ_CODE)
//											.getString())) {
//
//					}
//					setRowToSelectPrj(1, pd.getReportUI(), pd.getLReportUI());
//					setPraiseInfo(false);
//					pd.getMainUI().setButtonEditState(pd.getisEdit());
//				} else {
//					return;
//				}
//			} catch (Exception ee) {
//				ErrorInfo.showErrorDialog(ee, "��ȡ��Ŀ�������");
//			}
//		}
//	}

//	private DataSet getPrjTableDataFilter(DataSet ds) throws Exception {
//		String filter = "";
//		String filter1 = pd.getUserWid();
//		switch (pd.getPrjTablePanel().cbQueryType.getSelectedIndex()) {
//		case 0:
//			// ȫ��
//			return ds;
//		case 1:
//			// δ���
//			filter = (Common.isNullStr(filter1) ? "" : (filter1 + "&&"))
//					+ IPrjAudit.AuditNewInfo.AuditCurPrjState.WID + "<=0";
//			break;
//		case 2:
//			// �����
//			filter = (Common.isNullStr(filter1) ? "" : (filter1 + "&&"))
//					+ IPrjAudit.AuditNewInfo.AuditCurPrjState.WID + ">0";
//			break;
//		case 3:
//			// ���˻�
//			filter = (Common.isNullStr(filter1) ? "" : (filter1 + "&&"))
//					+ IPrjAudit.AuditNewInfo.AuditCurPrjState.ISBACK + "==1"
//					+ "&&" + IPrjAudit.AuditNewInfo.AuditCurPrjState.WID + "==0";
//			break;
//		case 4:
//			// ���˻�
//			filter = (Common.isNullStr(filter1) ? "" : (filter1 + "&&"))
//					+ IPrjAudit.AuditNewInfo.AuditCurPrjState.WID + "=="
//					+ (pd.getMaxStep() + 1);
//			break;
//		default:
//			break;
//		}
//		return (DataSet) (DataSetUtil.filterBy(ds, filter));
//	}

	/**
	 * ��Ŀ������Ŀ�䶯ʱ����Ŀ�б�Ҫѡ���� i: 0:��Ŀ������ 1����Ŀ�б���
	 * 
	 */
	private void setRowToSelectPrj(int i, ReportUI reportUI1, ReportUI reportUI2) {
		if (reportUI1 == null) {
			return;
		}
		if (reportUI2 == null) {
			return;
		}
		CellSelection cells = reportUI1.getCellSelection();
		CellSelection cells1 = new CellSelection();
		int row = cells.getRow();
		if (cells == null) {
			return;
		}
		if (row < 1) {
			return;
		}
		if (i == 0) {
			cells1.setRow(row + 1);
		} else {
			cells1.setRow(row - 1);
		}
		cells1.setColumnSpan(reportUI2.getReport().getColumnCount());
		reportUI2.setCellSelection(cells1);
		reportUI2.repaint();
		reportUI2.updateUI();
	}

	/**
	 * ˢ�±���
	 * 
	 * @param reportUI
	 * @param report
	 * @param pp
	 * @param divCode
	 * @param tableHeader
	 * @param dsHeader
	 * @param dsBody
	 * @throws Exception
	 */
	public void refresh(ReportUI reportUI, Report report, PropertyProvider pp,
			TableHeader tableHeader, DataSet dsHeader, DataSet dsBody)
			throws Exception {
		// �ػ�reportUI
		if (reportUI == null) {
			return;
		}
		reportUI.getReport().removeAllCellElements();
		reportUI.repaint();
		report.setReportHeader(tableHeader);
		report.maskZeroValue(true);
		// ���ñ������ݼ�
		report.setBodyData(dsBody);
		// ����������
		report.setCellProperty(pp);
		// ˢ�±���
		report.refreshBody();
		report.getUI().validate();
		reportUI.setReport(report);
		Style style = CellStyle.HEADER_DEFAULT;
		int i = dsHeader.getRecordCount();
		Iterator cells = report.cellIterator();
		while (cells.hasNext()) {
			i--;
			if (i <= 0) {
				break;
			}
			CellElement cellElement = (CellElement) cells.next();
			cellElement.setStyle(style);
		}
		// ��������Ӧ�п�
		report.autoAdjustColumnWidth();
		reportUI.setFrozenColumn(0);
		reportUI.repaint();
		reportUI.updateUI();
	}

	/**
	 * д����
	 * 
	 */
//	public void doInputData() {
//		try {
//			if (!pd.getPrjAuditData().locate(
//					IPrjAudit.MainTable.GeneralInfo.PrjCode, pd.getPrjCode()))
//				pd.getPrjAuditData().append();
//			else
//				pd.getPrjAuditData().edit();
//			((Report) (pd.getStdReportUI().getReport())).getBodyData().edit();
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "�������ʧ��");
//			return;
//		}
//		pd.setisEdit(true);
//		setPanelView();
//	}

	/**
	 * ��������
	 * 
	 */
//	public boolean doSaveData() {
//		try {
//			String bmk = pd.getPrjData().toggleBookmark(0);
//			if (!pd.getPrjAuditData().isEmpty() && !pd.getPrjAuditData().bof()
//					&& !pd.getPrjAuditData().eof()) {
//				// ������Ϣ
//				if (pd.getPrjAuditData().getState() == 6) {
//					pd.getPrjAuditData().fieldByName(
//							IPrjAudit.MainTable.GeneralInfo.ROW_ID).setValue(
//							UUID.randomUUID().toString());
//					pd.getPrjAuditData().fieldByName(
//							IPrjAudit.MainTable.GeneralInfo.PrjCode).setValue(
//							pd.getPrjCode());
//					pd.getPrjAuditData().fieldByName(IPrjAudit.PrjTable.DIV_CODE)
//							.setValue(pd.getDivCode());
//					pd.getPrjAuditData().fieldByName("RG_CODE").setValue(
//							Global.getCurrRegion());
//					pd.getPrjAuditData().fieldByName("SET_YEAR").setValue(
//							Global.loginYear);
//					pd.getPrjAuditData().fieldByName("BATCH_NO").setValue(
//							String.valueOf(pd.getBathcNo()));
//					pd.getPrjAuditData().fieldByName("DATA_TYPE").setValue(
//							GlobalEx.isFisVis() ? "1" : "0");
//				}
//				pd.getPrjGeneralPanel().setDataToDataSet();
//
//				String v0 = pd.getPrjAuditData().fieldByName(
//						IPrjAudit.MainTable.GeneralInfo.TotleMoney).getString();
//				String v1 = pd.getPrjAuditData().fieldByName(
//						IPrjAudit.MainTable.GeneralInfo.FinanceMoney)
//						.getString();
//				String v2 = pd.getPrjAuditData().fieldByName(
//						IPrjAudit.MainTable.GeneralInfo.DivColMoney).getString();
//				String v3 = pd.getPrjAuditData().fieldByName(
//						IPrjAudit.MainTable.GeneralInfo.OtherMoney).getString();
//				v0 = Common.isNullStr(v0) ? "0" : v0;
//				v1 = Common.isNullStr(v1) ? "0" : v1;
//				v2 = Common.isNullStr(v2) ? "0" : v2;
//				v3 = Common.isNullStr(v3) ? "0" : v3;
//				float f0 = Float.parseFloat(v0);
//				float f1 = Float.parseFloat(v1);
//				float f2 = Float.parseFloat(v2);
//				float f3 = Float.parseFloat(v3);
//				if (f0 < f1 + f2 + f3) {
//					JOptionPane.showMessageDialog(Global.mainFrame,
//							"��Ŀ�ſ����ʽ��ܽ�������ϸ���ϼ�");
//					return false;
//				}
//				pd.getPrjAuditTargetPanel().setDataToDataSet();
//				pd.getPrjAuditSpeedPanel().setDataToDataSet();
//				// ��ϸ����Ϣ
//				((Report) pd.getStdReportUI().getReport()).getBodyData()
//						.beforeFirst();
//				while (((Report) pd.getStdReportUI().getReport()).getBodyData()
//						.next()) {
//					((Report) pd.getStdReportUI().getReport()).getBodyData()
//							.fieldByName("SET_YEAR").setValue(Global.loginYear);
//					((Report) pd.getStdReportUI().getReport()).getBodyData()
//							.fieldByName("RG_CODE").setValue(
//									Global.getCurrRegion());
//				}
//				InfoPackage info = null;
//				// saveAuditInfo(pd.getPrjAuditData());
//				info = PrjAuditStub.getMethod().saveData(
//						pd.getPrjAuditData(),
//						((Report) pd.getStdReportUI().getReport())
//								.getBodyData());
//				if (!info.getSuccess()) {
//					return false;
//				}
//				pd.getPrjAuditData().applyUpdate();
//				((Report) pd.getStdReportUI().getReport()).getBodyData()
//						.applyUpdate();
//			}
//			pd.setisEdit(false);
//			setPanelView();
//			DataSet dsView = ((Report) pd.getReportUI().getReport())
//					.getBodyData();
//			if (dsView != null && !dsView.isEmpty() && !dsView.bof()
//					&& !dsView.eof()) {
//				((Report) pd.getReportUI().getReport()).getBodyData().edit();
//				((Report) pd.getReportUI().getReport()).getBodyData()
//						.fieldByName("ISINPUTPrjAudit").setValue("��");
//			}
//			((Report) pd.getReportUI().getReport()).getBodyData().applyUpdate();
//			this.refreshReportUI(1, ((Report) pd.getReportUI().getReport())
//					.getBodyData());
//			pd.getPrjData().gotoBookmark(bmk);
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "�������ݳ���");
//			return false;
//		}
//		return true;
//	}

	/**
	 * ȡ������
	 * 
	 */
//	public void doCancelData() {
//		try {
//			this.setPraiseInfo(false);
//			pd.getPrjAuditData().applyUpdate();
//			((Report) pd.getStdReportUI().getReport()).getBodyData()
//					.applyUpdate();
//			pd.getPrjGeneralPanel().doCloseFile();
//			pd.setisEdit(false);
//			setPanelView();
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "ȡ������������");
//		}
//	}

	/**
	 * �������״̬
	 * 
	 */
//	public void setPanelView() {
//		pd.getPrjGeneralPanel().setPanelEnable(pd.getisEdit());
//		pd.getPrjAuditTargetPanel().setPanelEnable(pd.getisEdit());
//		pd.getPrjAuditStandardPanel().setPanelEnable(pd.getisEdit());
//		pd.getPrjAuditSpeedPanel().setPanelEnable(pd.getisEdit());
//
//	}

	/**
	 * ������Ŀ�仯�����ü�Ч��Ϣ
	 * 
	 */
//	public void setPraiseInfo(boolean isClearAll) {
//		try {
//			if (Common.isNullStr(pd.getPrjCode()))
//				isClearAll = true;
//			dsPrjAudit = PrjAuditStub.getMethod().getPrjAuditMainData(
//					pd.getPrjCode(), Global.loginYear,
//					String.valueOf(pd.getBathcNo()),
//					GlobalEx.isFisVis() ? "1" : "0");
//			pd.setPrjAuditData(dsPrjAudit);
//			if (dsPrjAudit.isEmpty())
//				isClearAll = true;
//			if (isClearAll) {
//				pd.getPrjInputMainPanel().setInfoNull();
//				pd.getPrjGeneralPanel().setInfoNull();
//				pd.getPrjAuditTargetPanel().setInfoNull();
//				pd.getPrjAuditSpeedPanel().setInfoNull();
//				return;
//			}
//			StringBuffer sql = new StringBuffer();
//			sql
//					.append("select nullif(b.wname,'Ԥ�㵥λ')||'  '||case when a.wisend=1 ");
//			sql.append(" then '������' else 'δ���' end wname from ");
//			sql.append(" fb_p_appr_audit_curstate a,fb_p_appr_audit_wstep b");
//			sql.append(" where a.wid = b.wid");
//			sql.append(" and a.prj_code = '" + pd.getPrjCode() + "'");
//			String sAuditState = DBSqlExec.client().getStringValue(
//					sql.toString());
//			dsPrjAudit.beforeFirst();
//			dsPrjAudit.next();
//			pd.getPrjInputMainPanel().setInfo();
//			// ������Ŀ����Ϣ
//			if (dsPrjAudit != null && !dsPrjAudit.isEmpty() && !dsPrjAudit.bof()
//					&& !dsPrjAudit.eof()) {
//				// ��Ŀ�ſ���Ϣ
//				pd.getPrjGeneralPanel().setInfo(dsPrjAudit);
//				pd.getPrjAuditTargetPanel().setInfo(dsPrjAudit);
//				pd.getPrjAuditSpeedPanel().setInfo(dsPrjAudit);
//				DataSet dsD = null;
//				if (pd.getReportUI() != null
//						&& pd.getReportUI().getReport() != null)
//					dsD = ((Report) (pd.getReportUI().getReport()))
//							.getBodyData();
//				if (dsPrj != null && !dsPrj.isEmpty() && !dsPrj.bof()
//						&& !dsPrj.eof()) {
//					if (pd.getPrjTablePanel() != null) {
//						String bmk11 = dsPrj.toogleBookmark();
//						dsPrj.locate("PRJ_CODE", pd.getPrjCode());
//						String isback = dsPrj.fieldByName("isback").getString();
//						// String tag = "";
//						// if (Common.isEqual("1", isback))
//						// tag = "   ���˻�";
//						// if (Common.isNullStr(dsPrj.fieldByName("wname")
//						// .getString()))
//						// pd.getPrjTablePanel().labelStep.setText("Ԥ�㵥λ"
//						// + tag);
//						// else
//						// pd.getPrjTablePanel().labelStep.setText(dsPrj
//						// .fieldByName("wname").getString()
//						// + tag);
//						pd.getPrjTablePanel().labelStep.setText(sAuditState);
//						if (Common.isEqual("1".trim(), isback.trim()))
//							pd.getPrjTablePanel().labelStep
//									.setForeground(Color.red);
//						else
//							pd.getPrjTablePanel().labelStep
//									.setForeground(Color.BLACK);
//						dsPrj.gotoBookmark(bmk11);
//					}
//				} else if ((dsD != null && !dsD.isEmpty() && !dsD.bof() && !dsD
//						.eof())) {
//					if (pd.getPrjTablePanel() != null) {
//						String bmk12 = dsD.toogleBookmark();
//						dsD.locate("PRJ_CODE", pd.getPrjCode());
//						String isback = dsD.fieldByName("isback").getString();
//						String tag = "";
//						if (Common.isEqual("1", isback))
//							tag = "   ���˻�";
//						if (Common.isNullStr(dsD.fieldByName("wname")
//								.getString()))
//							pd.getPrjTablePanel().labelStep.setText("Ԥ�㵥λ"
//									+ tag);
//						else
//							pd.getPrjTablePanel().labelStep.setText(dsD
//									.fieldByName("wname").getString()
//									+ tag);
//						if (Common.isEqual("1".trim(), isback.trim()))
//							pd.getPrjTablePanel().labelStep
//									.setForeground(Color.red);
//						else
//							pd.getPrjTablePanel().labelStep
//									.setForeground(Color.BLACK);
//						dsD.gotoBookmark(bmk12);
//					}
//				} else {
//					if (pd.getPrjTablePanel() != null) {
//						pd.getPrjTablePanel().labelStep.setText("Ԥ�㵥λ δ���");
//						pd.getPrjTablePanel().labelStep
//								.setForeground(Color.BLACK);
//					}
//				}
//			} else {
//				pd.getPrjGeneralPanel().setInfoNull();
//				pd.getPrjAuditTargetPanel().setInfoNull();
//				pd.getPrjAuditSpeedPanel().setInfoNull();
//				pd.getPrjTablePanel().labelStep.setText("Ԥ�㵥λ δ���");
//				pd.getPrjTablePanel().labelStep.setForeground(Color.BLACK);
//			}
//		} catch (Exception ee) {
//			ErrorInfo.showErrorDialog(ee, "������Ŀ��Ч�걨��Ϣʧ��");
//		}
//	}

	public String[] getBMKs(int[] rows, CustomTable table) {
		String[] bmks = new String[rows.length];
		for (int i = 0; i < rows.length; i++) {
			bmks[i] = table.rowToBookmark(rows[i]);
		}
		return bmks;
	}
	
	//��Ŀ���Ϊ
	public boolean saveAffixFileForXm(String tableName, DataSet dsAffix, File outFile) {
		// ����ԭ�ļ������
		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		try {
			if (outFile.exists()) {
				outFile.delete();
			}
			fileOut = new FileOutputStream(outFile);
			bufOut = new BufferedOutputStream(fileOut);
			// ������ȡblob��ת��Ϊ�ֽ�,���������
//			int count = 1;
			boolean isSuccessful = true;
//			while (true) {
//				byte[] buf = PrjAuditStub.getMethod().getDocBlob(
//						tableName,
//						count++,
//						dsAffix.fieldByName("row_id")
//						.getString(), Global.loginYear);
				byte[] buf = PrjAuditStub.getMethod().getFileFind(tableName, dsAffix.fieldByName("row_id").getString(),  Global.loginYear);
//				if (buf == null) {
//					isSuccessful = false;
//					break;
//				} else {
					bufOut.write(buf);
//					if (buf.length < 1024 * 350) {
//						break;
//					}
//					if (buf.length <= 0) {
//						isSuccessful = false;
//						break;
//					}
//				}
//			}
			/* ���� */
			bufOut.flush();
			/* �ͷ�����Դ */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists()) {
				outFile.delete();
			}
			if (outFile.length() == 0) {
				isSuccessful = false;
				outFile.delete();
			}
			return isSuccessful;
		} catch (Exception e) {
			return false;
		} finally {
			/* �ر��� */
			try {
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				fileOut = null;
				bufOut = null;
			} catch (Exception eek) {
				return false;
			}
		}
	}

	/**
	 * ���渽��
	 * 
	 * @param outFile
	 * @return
	 */
	public boolean saveAffixFile(String tableName, DataSet dsAffix, File outFile) {
		// ����ԭ�ļ������
		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		try {
			if (outFile.exists()) {
				outFile.delete();
			}
			fileOut = new FileOutputStream(outFile);
			bufOut = new BufferedOutputStream(fileOut);
			// ������ȡblob��ת��Ϊ�ֽ�,���������
			int count = 1;
			boolean isSuccessful = true;
			while (true) {
				byte[] buf = PrjAuditStub.getMethod().getDocBlob(
						tableName,
						count++,
						dsAffix.fieldByName(IPrjAudit.MainTable.Affix.ROW_ID)
								.getString(), Global.loginYear);
				if (buf == null) {
					isSuccessful = false;
					break;
				} else {
					bufOut.write(buf);
					if (buf.length < 1024 * 350) {
						break;
					}
					if (buf.length <= 0) {
						isSuccessful = false;
						break;
					}
				}
			}
			/* ���� */
			bufOut.flush();
			/* �ͷ�����Դ */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists()) {
				outFile.delete();
			}
			if (outFile.length() == 0) {
				isSuccessful = false;
				outFile.delete();
			}
			return isSuccessful;
		} catch (Exception e) {
			return false;
		} finally {
			/* �ر��� */
			try {
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				fileOut = null;
				bufOut = null;
			} catch (Exception eek) {
				return false;
			}
		}
	}

	// class ItemShowHistoryPerform implements ActionListener {
	//
	// ReportUI reportUI;
	//
	// public ItemShowHistoryPerform(ReportUI reportUI) {
	// this.reportUI = reportUI;
	// }
	//
	// public void actionPerformed(ActionEvent e) {
	// try {
	// DataSet ds = ((Report) reportUI.getReport()).getBodyData();
	// String divCode = null;
	// String prjCode = null;
	// if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof()) {
	// divCode = ds.fieldByName(IPrjAudit.AuditInfo.DIV_CODE)
	// .getString();
	// prjCode = ds.fieldByName(IPrjAudit.AuditInfo.PRJ_CODE)
	// .getString();
	// }
	// ShowAuditHistoryPanel aa = new ShowAuditHistoryPanel(pd,
	// new String[] { divCode }, new String[] { prjCode });
	// aa.doQuery();
	// Tools.centerWindow(aa);
	// aa.setVisible(true);
	// } catch (Exception ee) {
	// ErrorInfo.showErrorDialog(ee, "���ʧ��");
	// }
	// }
	// }

	// private JPopupMenu getJpopMenu(ReportUI reportUI) {
	// JPopupMenu jp = new JPopupMenu();
	// JMenuItem menuShowHistory = new JMenuItem("�鿴��ʷ������¼");
	// menuShowHistory.addActionListener(new ItemShowHistoryPerform(reportUI));
	// menuShowHistory.setEnabled(true);
	// jp.add(menuShowHistory);
	// jp.addSeparator();
	// return jp;
	// }

	/**
	 * ��ȡ�����Ϣ
	 * 
	 * @param beanfactory
	 * @param bName
	 * @param aPrjCode
	 * @return
	 * @throws Exception
	 */
//	public String getAuditSql(BeanFactory beanfactory, String bName,
//			String aPrjCode) throws Exception {
////		String bCode = Common.nonNullStr(PrjAuditDefineLoader
////				.readPrjAuditContent(beanfactory, "prjaudit-branchid", bName));
//		StringBuffer sql = new StringBuffer();
//		sql
//				.append("SELECT a.User_Code,a.user_name,c.chr_code AS mcode,c.chr_name AS mname,d.audit_context,d.last_ver");
//		sql
//				.append(" FROM sys_usermanage a,sys_user_org b,ele_manage_branch c,fb_p_appr_audit_history d");
//		sql.append(" WHERE a.user_id = b.User_Id ");
//		sql.append("  AND a.USER_NAME = d.audit_user");
//		sql.append("  AND b.org_id = c.chr_id");
////		if ("zg".equals(bName)) {
////			String jxCode = Common
////					.nonNullStr(PrjAuditDefineLoader.readPrjAuditContent(
////							beanfactory, "prjaudit-branchid", "jx"));
////			String ysCode = Common
////					.nonNullStr(PrjAuditDefineLoader.readPrjAuditContent(
////							beanfactory, "prjaudit-branchid", "ys"));
////			sql.append("  AND c.chr_code not in ('" + jxCode + "','" + ysCode
////					+ "')");
////		} else
//			sql.append("  AND c.chr_code = '" + bCode + "'");
//		sql.append("  AND D.PRJ_CODE = '" + aPrjCode + "'");
//		sql.append("  AND d.data_type = 1");
//		return sql.toString();
//	}

	// /**
	// * �ж���Ŀ�Ƿ���Ա༭ ���裺 1.�Ȼ�ȡ��ǰ��Ŀ�����״̬ 2.��ǰ���״̬������ⲿ����δ��������
	// * a.��ѯ��ǰ��Ŀ�ڲ����״̬���̣������ڲ����״̬
	// *
	// * @param prjCOde
	// * @return
	// */
	// public boolean getIsCanOptByAuditStep(String prjCOde) {
	// String userCode = Global.user_code;
	//
	// return true;
	// }

	public String[] getSelElement(CustomTree tree, boolean isSel) {
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // ���ڵ�
		Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
		int i = 0;
		String[] strs = null;
		if (isSel) {
			strs = new String[tree.getSelectedNodes(true).length];
		}else{
			strs = new String[tree.getNodeCount(true, false)-tree.getSelectedNodes(true).length];
		}
		while (enumeration.hasMoreElements()) { // ��ʼ����
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (isSel) {
				if (pNode.getIsSelect()) {
					// ����ýڵ�Ϊѡ��״̬�����������list��
					strs[i] = (pNode.getValue());
					i++;
				}
			} else {
				if (!pNode.getIsSelect()) {
					// ����ýڵ�Ϊѡ��״̬�����������list��
					strs[i] = (pNode.getValue());
					i++;
				}
			}
		}
		return strs;
	}

	public String[] getSelElementContent(CustomTree tree) {
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // ���ڵ�
		Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
		int i = 0;
		String[] strs = new String[tree.getSelectedNodes(true).length];
		while (enumeration.hasMoreElements()) { // ��ʼ����
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (pNode.getIsSelect()) {
				// ����ýڵ�Ϊѡ��״̬�����������list��
				strs[i] = (pNode.getShowContent()).substring(pNode
						.getShowContent().indexOf("]") + 1);
				i++;
			}
		}
		return strs;
	}

	public void setSelElement(CustomTree tree, String str) {
		if (Common.isNullStr(str)) {
			return;
		}
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // ���ڵ�
		Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
		if (Common.isNullStr(str)) {
			while (enumeration.hasMoreElements()) { // ��ʼ����
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (!node.isLeaf()) {
					continue;
				}
				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
				pNode.setIsSelect(false); // ���øýڵ�Ϊδѡȡ״̬
			}
			return;
		}
		String[] ele = str.split(";\n");
		Arrays.sort(ele);
		while (enumeration.hasMoreElements()) { // ��ʼ����
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (Arrays.binarySearch(ele, pNode.getValue()) >= 0) {
				pNode.setIsSelect(true);
			} else {
				pNode.setIsSelect(false);
			}
		}
	}

	public void setSelElement(CustomTree tree, String[] str) {
		if (str == null) {
			return;
		}
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // ���ڵ�
		Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
		if (str.length == 0) {
			while (enumeration.hasMoreElements()) { // ��ʼ����
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (!node.isLeaf()) {
					continue;
				}
				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
				pNode.setIsSelect(false); // ���øýڵ�Ϊδѡȡ״̬
			}
			return;
		}
		Arrays.sort(str);
		while (enumeration.hasMoreElements()) { // ��ʼ����
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode == null) {
				continue;
			}
			if (Arrays.binarySearch(str, pNode.getValue()) >= 0) {
				pNode.setIsSelect(true);
			} else {
				pNode.setIsSelect(false);
			}
		}
	}

	/**
	 * ��ȡ��ǰ��Ŀ����Ŀ�����Ϣ״̬
	 * 
	 * @param prjCodes
	 * @return map key:prj_code value:wid
	 * @throws Exception
	 */
	public Map getPrjCurWID(String[] prjCodes) throws Exception {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from "
				+ IPrjAudit.AuditNewInfo.AuditCurPrjState.TableName);
		sql.append(" where " + IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE);
		sql.append(" in (" + getSplitString(prjCodes, ",") + ")");
		DataSet ds = DBSqlExec.client().getDataSet(sql.toString());
		sql.append(" where " + IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE);
		if ((ds != null) && !ds.isEmpty()) {
			ds.beforeFirst();
			while (ds.next()) {
				map
						.put(
								ds
										.fieldByName(
												IPrjAudit.AuditNewInfo.AuditCurPrjState.PRJ_CODE)
										.getString(),
								ds
										.fieldByName(
												IPrjAudit.AuditNewInfo.AuditCurPrjState.WID)
										.getString());
			}
			return map;
		} else {
			return null;
		}
	}

	public String getSplitString(String[] str, String splitSign) {
		StringBuffer sc = new StringBuffer();
		if ((str != null) && (str.length != 0)) {
			for (int i = 0; i < str.length; i++) {
				if (i == 0) {
					sc.append("'" + str[i] + "'");
				} else {
					sc.append(splitSign + str[i]);
				}
			}
		} else {
			return "";
		}
		return sc.toString();
	}

	/**
	 * ������Ŀ���½�˻�ȡ��������Ϣ
	 * 
	 * @param prjCode
	 * @param wid
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public String getAuditContext(List prjCodes, String userCode)
			throws Exception {
		StringBuffer sql = new StringBuffer();
	    StringBuffer value = new StringBuffer();
	    if (prjCodes==null) {
			return "";
		}
	    for(int i=0;i<prjCodes.size();i++){
	    	sql.append(" select audit_text From "+IPrjAudit.PrjAuditTable.TABLENAME_AUDIT_CUR);
	    	sql.append(" where 1=1 "+getAuditContextFilter(prjCodes.get(i),userCode));
	    	value.append(DBSqlExec.client().getStringValue(sql.toString()));
	    	value.append("\n\n");
	    }
		return value.toString();
	}
	
	private String getAuditContextFilter(Object prjCodes,String userCode){
		StringBuffer filter = new StringBuffer();
		filter.append(" and prj_code = '"+prjCodes+"'");
		filter.append(" and set_year = "+Global.loginYear);
		filter.append(" and rg_code = "+Global.getCurrRegion());
		filter.append(" and audit_userid='"+userCode+"'");
		return filter.toString();
	}

	public DataSet getPriaseTypeData() throws Exception {
		if (dsPType == null) {
			dsPType = PrjAuditStub.getMethod().getPriaseTypeData(
					Global.loginYear);
		} else {
			return dsPType;
		}
		return dsPType;
	}
}
