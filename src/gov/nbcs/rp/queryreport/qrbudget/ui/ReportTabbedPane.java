/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.action.ExeSearch;
import gov.nbcs.rp.queryreport.qrbudget.action.ReadWriteFile;
import gov.nbcs.rp.queryreport.qrbudget.action.SearchPublic;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:�л�ҳ��
 * </p>
 * <p>
 * Description:�л�ҳ��
 * </p>

 */
public class ReportTabbedPane extends FTabbedPane {

	private static final long serialVersionUID = 1L;

	private List lstReport = null;

	private QrBudget qrBudget;

	// ���浱ǰѡ�е�ҳ��ֵ
	private int oldSelectIndex = -1;

	private ReportHeaderShow reportHeaderShow;

	public ReportTabbedPane(QrBudget qrBudget) {
		this.qrBudget = qrBudget;
		// �л�ҳ��
		this.addChangeListener(new ReportChangeListener());
		reportHeaderShow = new ReportHeaderShow(qrBudget);
	}

	/**
	 * �л�����ҳ��
	 */
	private class ReportChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent evt) {
			if (!(evt.getSource() instanceof FTabbedPane)) {
				return;
			}

			try {
				// ���²��õ�ǰҳ��
				setLayoutReport();

				int index = ((FTabbedPane) evt.getSource()).getSelectedIndex();
				// �ж��Ƿ������ӵĵ�һ��ҳ��,�Ƴ����Ƿ������һ��ҳ��
				if (lstReport.size() == 1 || lstReport.size() == 0) {
					oldSelectIndex = ((FTabbedPane) evt.getSource())
							.getSelectedIndex();
				} else {
					// ����ԭҳҳֵ
					if (oldSelectIndex != index && index != -1
							&& oldSelectIndex != -1) {
						// ���浱ǰ�ļ�
						ReportInfo reportInfo = (ReportInfo) lstReport
								.get(oldSelectIndex);

						// ���鱨������ⱨ����
						if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
								|| reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
							if (qrBudget.getLstResult() != null) {
								Map curPara = getPara(false);
								// �жϲ����Ƿ���ͬ
								boolean isSame = ReadWriteFile.check(curPara,
										reportInfo.getParaMap());
								// �ж��ļ��Ƿ����
								if (!isSame
										|| !ReadWriteFile
												.isExistFile(reportInfo
														.getReportID())) {
									// ������ʾ��Ϣ
									curPara.put(IDefineReport.SHOW_INFO,
											qrBudget.getFlblInfo().getText());
									ReadWriteFile.saveFile(qrBudget
											.getLstResult(), qrBudget
											.getDsReportHeader(), curPara);
								}
							}
						}
					}
				}

				// ��ʾ�µ�ҳ��ֵ
				if (qrBudget.getUUID() != null) {
					if (index != -1) {
						ExeSearch exeSearch = new ExeSearch(qrBudget);
						exeSearch.doExeSearch(getPara(true), false);
					}
				} else {
					// ��ʾ��ͷ��Ϣ
					if (index != -1) {
						ReportInfo reportInfo = (ReportInfo) lstReport
								.get(index);
						reportHeaderShow.show(reportInfo);
					}
				}
				oldSelectIndex = index;

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "�л����������󣬴�����Ϣ:"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * ���²��õ�ǰҳ��
	 * 
	 * @throws Exception
	 */
	private void setLayoutReport() throws Exception {
		int index = this.getSelectedIndex();
		if (index == -1)
			return;
		ReportInfo reportInfo = (ReportInfo) lstReport.get(index);
		Component curComponent = this.getComponent(index);
		if (curComponent instanceof FTitledPanel) {
			((FTitledPanel) curComponent).setLayout(new RowPreferedLayout(1));

			// int bussType = reportInfo.getBussType();
			// if (bussType == QrBudget.TYPE_COVER
			// || bussType == QrBudget.TYPE_SZZB
			// || bussType == QrBudget.TYPE_ROWSET) {
			// if (qrBudget.getReportUI() == null) {
			// ReportUI reportUI = new ReportUI(new Report());
			// reportUI.getReport().getReportSettings().setPaperSize(
			// new PaperSize(2000, 3000));
			// reportUI.setColumnEndless(false);
			// reportUI.setRowEndless(false);
			// reportUI.clearColumnHeaderContent();
			// reportUI.clearRowHeaderContent();
			//
			// qrBudget.setReportUI(reportUI);
			// }
			// ((FTitledPanel) curComponent).add(qrBudget.getReportUI(),
			// new TableConstraints(1, 1, true));
			// reportHeaderShow.show(reportInfo);
			// } else {
			// reportHeaderShow.show(reportInfo);
			// ((FTitledPanel) curComponent).add(qrBudget.getFtabReport(),
			// new TableConstraints(1, 1, true));
			// }

			((FTitledPanel) curComponent).add(qrBudget.getReportUI(),
					new TableConstraints(1, 1, true));
			reportHeaderShow.show(reportInfo);
		}
	}

	/**
	 * ����ҳ��
	 * 
	 * @param reportMap
	 * @throws Exception
	 */
	public boolean addTitlePane(Map reportMap) throws Exception {
		String sReportID = reportMap.get(IQrBudget.REPORT_ID).toString();
		String sErr = checkAdd(sReportID);
		if (!Common.isNullStr(sErr)) {
			new MessageBox(Global.mainFrame, sErr, MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}
		if (lstReport == null) {
			lstReport = new ArrayList();
		}
		lstReport.add(new ReportInfo(reportMap));
		String sReportName = reportMap.get(IQrBudget.REPORT_CNAME).toString();
		this.addControl(sReportName, new FTitledPanel());
		return true;
	}

	/**
	 * ɾ��ҳ��
	 * 
	 * @param reportMap
	 * @throws Exception
	 */
	public void removeTitlePane(Map reportMap) throws Exception {
		int index = getIndex(lstReport, reportMap);
		if (index == -1) {
			return;
		}

		// ��ǰ��ʾҳ��
		int curSelectIndex = this.getSelectedIndex();

		// ɾ����ʱ��
		ReportInfo reportInfo = (ReportInfo) lstReport.get(index);
		Object searchObj = reportInfo.getSearchObj();

		// �Ƚ����Ƴ�
		this.remove(index);
		// ���б���ɾ��
		lstReport.remove(index);

		// ɾ�����ɵ��ļ��п�����ʱ��
		if (searchObj instanceof OriSearchObj) {
			treatOriSearchObj((OriSearchObj) searchObj, reportInfo
					.getReportID());
		}

		// �ж��Ƴ�ҳ���Ƿ��ǵ�ǰҳ��
		if (index == curSelectIndex) {
			// ���²��õ�ǰҳ��
			setLayoutReport();
		}
		oldSelectIndex = this.getSelectedIndex();
	}

	/**
	 * �õ�ֵ��list�е�λ��
	 * 
	 * @param list
	 * @param value
	 * @return
	 */
	private int getIndex(List list, Object value) {
		if (list == null)
			return -1;
		int size = list.size();
		String reportID = ((Map) value).get(IQrBudget.REPORT_ID).toString();
		String reportIDTmp;
		for (int i = 0; i < size; i++) {
			reportIDTmp = ((ReportInfo) list.get(i)).getReportID();
			if (reportID.equals(reportIDTmp))
				return i;
		}
		return -1;
	}

	/**
	 * ����Ƿ���������
	 * 
	 * @param reportId
	 * @return
	 */
	private String checkAdd(String reportId) {
		if (lstReport == null) {
			return "";
		}
		if (isExistReport(lstReport, reportId)) {
			return "�˱����ѹ�ѡ,�������ظ���ѡ��";
		}

		if (lstReport.size() > 10) {
			return "һ���������ѡ��ʮ�ű���";
		}
		return "";
	}

	/**
	 * �жϱ����Ƿ��ѱ�ѡ��
	 * 
	 * @param lstReport
	 * @param reportID
	 * @return
	 */
	private boolean isExistReport(List lstReport, String reportID) {
		int size = lstReport.size();
		ReportInfo reportInfo;
		for (int i = 0; i < size; i++) {
			reportInfo = (ReportInfo) lstReport.get(i);
			if (reportInfo.getReportID().equals(reportID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �õ�����ֵ
	 * 
	 * @param curPage�Ƿ�ǰҳ��true:�л�����ҳ��false:
	 *            ԭ��ʾҳ
	 * @return
	 * @throws Exception
	 */
	public Map getPara(boolean curPage) throws Exception {
		Map mapPara = new HashMap();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// ����
		mapPara.put(IDefineReport.BATCH_NO, String.valueOf(searchPublic
				.getBatchNo()));
		// ����
		mapPara.put(IDefineReport.DATA_TYPE, String.valueOf(searchPublic
				.getDataType()));
		// �汾��
		mapPara.put(IDefineReport.VER_NO, qrBudget.getFpnlToolBar().getVerNo());
		// ��λ
		mapPara.put(IDefineReport.DIV_CODE, searchPublic.getDivWhere());
		// ����ID
		if (curPage) {
			if (this.getSelectedIndex() != -1) {
				mapPara.put(IQrBudget.REPORT_ID, ((ReportInfo) lstReport
						.get(this.getSelectedIndex())).getReportID());
			}
		} else {
			mapPara.put(IQrBudget.REPORT_ID, ((ReportInfo) lstReport
					.get(oldSelectIndex)).getReportID());
		}

		mapPara.put(IQrBudget.UUID, qrBudget.getUUID());
		return mapPara;
	}

	public List getLstReport() {
		return lstReport;
	}

	public void treatOriSearchObj(OriSearchObj oriSearchObj, String sReportId) {
		if (oriSearchObj != null) {
			String sErr = "";
			try {
				ReadWriteFile.deleteFile(sReportId);
			} catch (Exception e) {
				new MessageBox("ɾ�����ݱ�ʧ��!", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				oriSearchObj = null;
				return;
			}
			if (!sErr.equals(""))
				new MessageBox(sErr, MessageBox.MESSAGE, MessageBox.BUTTON_OK)
						.show();
			oriSearchObj = null;
		}
	}

	/**
	 * �����ѯ��
	 * 
	 * @throws Exception
	 * 
	 */
	public void treatSearchObj() throws Exception {
		if (lstReport == null) {
			return;
		}
		int size = lstReport.size();
		ReportInfo reportInfo;
		for (int i = 0; i < size; i++) {
			reportInfo = (ReportInfo) lstReport.get(i);
			ReadWriteFile.deleteFile(reportInfo.getReportID());
			reportInfo.setSearchObj(null);
			reportInfo.setParaMap(null);
		}
	}

}
