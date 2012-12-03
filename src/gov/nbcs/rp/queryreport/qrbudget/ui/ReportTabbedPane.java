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
 * Title:切换页面
 * </p>
 * <p>
 * Description:切换页面
 * </p>

 */
public class ReportTabbedPane extends FTabbedPane {

	private static final long serialVersionUID = 1L;

	private List lstReport = null;

	private QrBudget qrBudget;

	// 保存当前选中的页面值
	private int oldSelectIndex = -1;

	private ReportHeaderShow reportHeaderShow;

	public ReportTabbedPane(QrBudget qrBudget) {
		this.qrBudget = qrBudget;
		// 切换页面
		this.addChangeListener(new ReportChangeListener());
		reportHeaderShow = new ReportHeaderShow(qrBudget);
	}

	/**
	 * 切换报表页面
	 */
	private class ReportChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent evt) {
			if (!(evt.getSource() instanceof FTabbedPane)) {
				return;
			}

			try {
				// 重新布置当前页面
				setLayoutReport();

				int index = ((FTabbedPane) evt.getSource()).getSelectedIndex();
				// 判断是否是增加的第一个页面,移除的是否是最后一个页面
				if (lstReport.size() == 1 || lstReport.size() == 0) {
					oldSelectIndex = ((FTabbedPane) evt.getSource())
							.getSelectedIndex();
				} else {
					// 保存原页页值
					if (oldSelectIndex != index && index != -1
							&& oldSelectIndex != -1) {
						// 保存当前文件
						ReportInfo reportInfo = (ReportInfo) lstReport
								.get(oldSelectIndex);

						// 分组报表和特殊报表保存
						if (reportInfo.getBussType() == QrBudget.TYPE_GROUP
								|| reportInfo.getBussType() == QrBudget.TYPE_NORMAL) {
							if (qrBudget.getLstResult() != null) {
								Map curPara = getPara(false);
								// 判断参数是否相同
								boolean isSame = ReadWriteFile.check(curPara,
										reportInfo.getParaMap());
								// 判断文件是否存在
								if (!isSame
										|| !ReadWriteFile
												.isExistFile(reportInfo
														.getReportID())) {
									// 保存提示信息
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

				// 显示新的页面值
				if (qrBudget.getUUID() != null) {
					if (index != -1) {
						ExeSearch exeSearch = new ExeSearch(qrBudget);
						exeSearch.doExeSearch(getPara(true), false);
					}
				} else {
					// 显示表头信息
					if (index != -1) {
						ReportInfo reportInfo = (ReportInfo) lstReport
								.get(index);
						reportHeaderShow.show(reportInfo);
					}
				}
				oldSelectIndex = index;

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(qrBudget, "切换报表发生错误，错误信息:"
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 重新布置当前页面
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
	 * 增加页面
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
	 * 删除页面
	 * 
	 * @param reportMap
	 * @throws Exception
	 */
	public void removeTitlePane(Map reportMap) throws Exception {
		int index = getIndex(lstReport, reportMap);
		if (index == -1) {
			return;
		}

		// 当前显示页面
		int curSelectIndex = this.getSelectedIndex();

		// 删除临时表
		ReportInfo reportInfo = (ReportInfo) lstReport.get(index);
		Object searchObj = reportInfo.getSearchObj();

		// 先界面移除
		this.remove(index);
		// 从列表中删除
		lstReport.remove(index);

		// 删除生成的文件有库中临时表
		if (searchObj instanceof OriSearchObj) {
			treatOriSearchObj((OriSearchObj) searchObj, reportInfo
					.getReportID());
		}

		// 判断移除页面是否是当前页面
		if (index == curSelectIndex) {
			// 重新布置当前页面
			setLayoutReport();
		}
		oldSelectIndex = this.getSelectedIndex();
	}

	/**
	 * 得到值在list中的位置
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
	 * 检查是否允许增加
	 * 
	 * @param reportId
	 * @return
	 */
	private String checkAdd(String reportId) {
		if (lstReport == null) {
			return "";
		}
		if (isExistReport(lstReport, reportId)) {
			return "此报表已勾选,不允行重复勾选！";
		}

		if (lstReport.size() > 10) {
			return "一次最多允许选择十张报表！";
		}
		return "";
	}

	/**
	 * 判断报表是否已被选中
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
	 * 得到参数值
	 * 
	 * @param curPage是否当前页，true:切换到的页：false:
	 *            原显示页
	 * @return
	 * @throws Exception
	 */
	public Map getPara(boolean curPage) throws Exception {
		Map mapPara = new HashMap();
		SearchPublic searchPublic = new SearchPublic(qrBudget);
		// 批次
		mapPara.put(IDefineReport.BATCH_NO, String.valueOf(searchPublic
				.getBatchNo()));
		// 类型
		mapPara.put(IDefineReport.DATA_TYPE, String.valueOf(searchPublic
				.getDataType()));
		// 版本号
		mapPara.put(IDefineReport.VER_NO, qrBudget.getFpnlToolBar().getVerNo());
		// 单位
		mapPara.put(IDefineReport.DIV_CODE, searchPublic.getDivWhere());
		// 报表ID
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
				new MessageBox("删除数据表失败!", e.getMessage(), MessageBox.ERROR,
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
	 * 清除查询体
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
