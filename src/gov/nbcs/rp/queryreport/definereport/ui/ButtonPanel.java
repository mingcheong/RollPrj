/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.common.util.ReportTools;
import com.foundercy.pf.reportcy.summary.exception.SummaryException;
import com.foundercy.pf.reportcy.summary.iface.IDataSourceManager;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;


 
public class ButtonPanel extends FFlowLayoutPanel {

	private static final long serialVersionUID = 1L;

	// "完成"按钮
	private FButton fbtnDown = null;

	// "取消"按钮
	private FButton fbtnCancel = null;

	private ReportGuideUI reportGuideUI = null;

	// 定义数据库接口
	private IDefineReport definReportServ = null;

	public ButtonPanel(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.definReportServ = reportGuideUI.definReportServ;

		// 设置靠右显示
		this.setAlignment(FlowLayout.RIGHT);

		fbtnDown = new FButton("fbtnDown", "完 成");
		fbtnDown.addActionListener(new DownActionListener());
		fbtnCancel = new FButton("fbtnDown", "关 闭");
		fbtnCancel.addActionListener(new CancelActionListener());

		// 完成
		this.addControl(fbtnDown, new TableConstraints(1, 1, true, false));
		// 取消
		this.addControl(fbtnCancel, new TableConstraints(1, 1, true, false));
	}

	/**
	 * 完成操作
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 报表保存前检查
			String sErr = check();
			if (!Common.isNullStr(sErr)) {
				new MessageBox(reportGuideUI, sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			reportGuideUI.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				// 得到报表主表信息
				RepSetObject repSetObject = reportGuideUI.reportInfoSet
						.getReportBasicInfo();

				// 得到reportId
				String sReportId = repSetObject.getREPORT_ID();

				GroupReport groupReport = reportGuideUI.fpnlDefineReport.groupReport;
				// 文件名
				File filePath = new File(IDefineReport.PATH_ + sReportId
						+ ".xml");
				// 写文件
				ReportTools.writeReport(groupReport, filePath, false);

				ReportHeader reportHeader = new ReportHeader();
				// 得到表头DataSet
				DataSet dsHeader = reportHeader.getHeader(groupReport,
						sReportId);

				// 生成sql语句
				BuildSql buildSql = new BuildSql(groupReport);
				// 得到sqllines
				List lstSqlLines = buildSql.getSqlLinesSql(reportHeader
						.getEnameMap());

				List lstType = reportGuideUI.reportInfoSet.getType();
				// 保存报表
				saveFile(filePath, reportGuideUI.repSetObject, dsHeader,
						lstSqlLines, lstType);

				// 修改或增加节点时，刷新树节点
				reportGuideUI.defineReport.refreshNodeEdit(repSetObject,
						lstType, reportGuideUI.sReportId);

				// 报表类型设为修改
				reportGuideUI.sReportId = sReportId;

				new MessageBox(reportGuideUI, "保存成功!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			} catch (SummaryException e) {
				new MessageBox(reportGuideUI, "保存失败，错误信息：" + e.getMessage(),
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			} catch (Exception e) {
				new MessageBox(reportGuideUI, "保存失败，错误信息：" + e.getMessage(),
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			} finally {
				reportGuideUI.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		}
	}

	/**
	 * 关闭按钮点击按钮事件
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			reportGuideUI.dispose();

		}
	}

	/**
	 * 保存报表
	 * 
	 * @param aFile
	 * @param reportID
	 * @return
	 * @throws Exception
	 */
	private int saveFile(File aFile, RepSetObject repSetObject,
			DataSet dsHeader, List lstSqlLines, List lstType) throws Exception {
		FileInputStream input = new FileInputStream(aFile);

		byte[] obj = new byte[(int) aFile.length()];
		input.read(obj);
		input.close();
		return definReportServ.saveReportFile(obj, repSetObject,
				reportGuideUI.sReportId, dsHeader, lstSqlLines, lstType);

	}

	/**
	 * 报表保存前检查
	 * 
	 * @return
	 */
	private String check() {
		// 基础信息填写检查
		String sErr = reportGuideUI.reportInfoSet.check();
		if (!Common.isNullStr(sErr)) {
			return sErr;
		}

		IDataSourceManager dataSourceManager = reportGuideUI.querySource
				.getDataSourceManager();
		if (dataSourceManager == null
				|| dataSourceManager.getDataSourceArray() == null
				|| dataSourceManager.getDataSourceArray().length == 0) {
			return "未选择数据源,请选择数据！";
		}

		GroupReport groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
		int col = groupReport.getColumnCount();
		boolean flag = false;
		Object value;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value == null) {
				continue;
			}
			if (value instanceof MyGroupValueImpl) {
				flag = true;
				break;
			} else if (value instanceof MyCalculateValueImpl) {
				flag = true;
				break;
			} else {
				continue;
			}
		}
		if (!flag) {
			return "报表列需设置选数据源字段，请设置！";
		}

		// (表头与表体一一对应）不允许合并单元格，表头最后一行不允许合并单元格
		sErr = checkHeader(groupReport);
		if (!Common.isNullStr(sErr))
			return sErr;
		return "";
	}

	/**
	 * 表头与表体一一对应）不允许合并单元格，表头最后一行不允许合并单元格
	 * 
	 * @return
	 */
	private String checkHeader(GroupReport groupReport) {
		int[] row = DefinePub.getHeaderRow(groupReport);
		if (row == null)
			return "表头不存在，请修改！";
		int lastRow = row[row.length - 1];
		CellElement[] cells = groupReport.getCellElementRow(lastRow)
				.getCellElements();
		int length = cells.length;
		for (int i = 0; i < length; i++) {
			if (cells[i].getColumnSpan() > 1)
				return "表头最后一行不允许合并单元格,请修改！";
		}
		return "";
	}
}
