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

	// "���"��ť
	private FButton fbtnDown = null;

	// "ȡ��"��ť
	private FButton fbtnCancel = null;

	private ReportGuideUI reportGuideUI = null;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	public ButtonPanel(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.definReportServ = reportGuideUI.definReportServ;

		// ���ÿ�����ʾ
		this.setAlignment(FlowLayout.RIGHT);

		fbtnDown = new FButton("fbtnDown", "�� ��");
		fbtnDown.addActionListener(new DownActionListener());
		fbtnCancel = new FButton("fbtnDown", "�� ��");
		fbtnCancel.addActionListener(new CancelActionListener());

		// ���
		this.addControl(fbtnDown, new TableConstraints(1, 1, true, false));
		// ȡ��
		this.addControl(fbtnCancel, new TableConstraints(1, 1, true, false));
	}

	/**
	 * ��ɲ���
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// ������ǰ���
			String sErr = check();
			if (!Common.isNullStr(sErr)) {
				new MessageBox(reportGuideUI, sErr, MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}

			reportGuideUI.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				// �õ�����������Ϣ
				RepSetObject repSetObject = reportGuideUI.reportInfoSet
						.getReportBasicInfo();

				// �õ�reportId
				String sReportId = repSetObject.getREPORT_ID();

				GroupReport groupReport = reportGuideUI.fpnlDefineReport.groupReport;
				// �ļ���
				File filePath = new File(IDefineReport.PATH_ + sReportId
						+ ".xml");
				// д�ļ�
				ReportTools.writeReport(groupReport, filePath, false);

				ReportHeader reportHeader = new ReportHeader();
				// �õ���ͷDataSet
				DataSet dsHeader = reportHeader.getHeader(groupReport,
						sReportId);

				// ����sql���
				BuildSql buildSql = new BuildSql(groupReport);
				// �õ�sqllines
				List lstSqlLines = buildSql.getSqlLinesSql(reportHeader
						.getEnameMap());

				List lstType = reportGuideUI.reportInfoSet.getType();
				// ���汨��
				saveFile(filePath, reportGuideUI.repSetObject, dsHeader,
						lstSqlLines, lstType);

				// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
				reportGuideUI.defineReport.refreshNodeEdit(repSetObject,
						lstType, reportGuideUI.sReportId);

				// ����������Ϊ�޸�
				reportGuideUI.sReportId = sReportId;

				new MessageBox(reportGuideUI, "����ɹ�!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
			} catch (SummaryException e) {
				new MessageBox(reportGuideUI, "����ʧ�ܣ�������Ϣ��" + e.getMessage(),
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			} catch (Exception e) {
				new MessageBox(reportGuideUI, "����ʧ�ܣ�������Ϣ��" + e.getMessage(),
						MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			} finally {
				reportGuideUI.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		}
	}

	/**
	 * �رհ�ť�����ť�¼�
	 */
	private class CancelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			reportGuideUI.dispose();

		}
	}

	/**
	 * ���汨��
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
	 * ������ǰ���
	 * 
	 * @return
	 */
	private String check() {
		// ������Ϣ��д���
		String sErr = reportGuideUI.reportInfoSet.check();
		if (!Common.isNullStr(sErr)) {
			return sErr;
		}

		IDataSourceManager dataSourceManager = reportGuideUI.querySource
				.getDataSourceManager();
		if (dataSourceManager == null
				|| dataSourceManager.getDataSourceArray() == null
				|| dataSourceManager.getDataSourceArray().length == 0) {
			return "δѡ������Դ,��ѡ�����ݣ�";
		}

		GroupReport groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �õ�������
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
			return "������������ѡ����Դ�ֶΣ������ã�";
		}

		// (��ͷ�����һһ��Ӧ��������ϲ���Ԫ�񣬱�ͷ���һ�в�����ϲ���Ԫ��
		sErr = checkHeader(groupReport);
		if (!Common.isNullStr(sErr))
			return sErr;
		return "";
	}

	/**
	 * ��ͷ�����һһ��Ӧ��������ϲ���Ԫ�񣬱�ͷ���һ�в�����ϲ���Ԫ��
	 * 
	 * @return
	 */
	private String checkHeader(GroupReport groupReport) {
		int[] row = DefinePub.getHeaderRow(groupReport);
		if (row == null)
			return "��ͷ�����ڣ����޸ģ�";
		int lastRow = row[row.length - 1];
		CellElement[] cells = groupReport.getCellElementRow(lastRow)
				.getCellElements();
		int length = cells.length;
		for (int i = 0; i < length; i++) {
			if (cells[i].getColumnSpan() > 1)
				return "��ͷ���һ�в�����ϲ���Ԫ��,���޸ģ�";
		}
		return "";
	}
}
