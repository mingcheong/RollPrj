/**
 * ��ӡ��������
 * 
 * qzc
 */
package gov.nbcs.rp.common.print;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.report.Report;
import com.fr.base.Constants;
import com.fr.base.FRFont;
import com.fr.report.Margin;
import com.fr.report.ReportHF;
import com.fr.report.ReportSettings;
import com.fr.view.PreviewFrame;

public class PrintUtility {
	// /////////////////////////////���ݶ���

	private static String[] params_Child;

	/**
	 * ��ӡ������
	 * 
	 * @param report
	 *            ����
	 * @param reportId
	 *            ����ID
	 * @param setYear
	 *            ���
	 * @param isShowDialog
	 *            �Ƿ���ʾ��ӡ�Ի���
	 */
	public static InfoPackage print(Report report, String reportId,
			String setYear, boolean isShowDiaLog) throws Exception {
		DataSet dsPrint = getPrintData(reportId, setYear);
		ReportSettings reportSettings = getReportSettings(report);
		InfoPackage info = checkBeforePrint(report, dsPrint);
		if (info.getSuccess() == false)
			return info;
		setPrintSettings(report, dsPrint, reportSettings);
		report.print(false);
		return info;
	}

	/**
	 * ��ӡԤ��
	 * 
	 * @param report
	 * @param reportId
	 * @throws Exception
	 */
	public static InfoPackage printPreview(Report report, String reportId,
			String setYear, String[] param_Child) throws Exception {
		params_Child = param_Child;
		DataSet dsPrint = getPrintData(reportId, setYear);
		ReportSettings reportSettings = getReportSettings(report);
		InfoPackage info = checkBeforePrint(report, dsPrint);
		if (info.getSuccess() == false)
			return info;
		report.setPrinting(true);
		setPrintSettings(report, dsPrint, reportSettings);
		PreviewFrame previewFrame = new PreviewFrame();
		print1(previewFrame, report, reportId, setYear, param_Child);
		previewFrame.print(report);
		previewFrame.setVisible(true);
		report.setPrinting(false);
		return info;
	}

	/**
	 * ��ӡԤ��
	 * 
	 * @param report
	 * @param reportId
	 * @throws Exception
	 */
	public static InfoPackage printPreview(JDialog dialog, Report report,
			String reportId, String setYear, String[] param_Child)
			throws Exception {
		params_Child = param_Child;
		DataSet dsPrint = getPrintData(reportId, setYear);
		ReportSettings reportSettings = getReportSettings(report);
		InfoPackage info = checkBeforePrint(report, dsPrint);
		if (info.getSuccess() == false)
			return info;
		report.setPrinting(true);
		setPrintSettings(report, dsPrint, reportSettings);
		PreviewDialog previewFrame = null;
		if (dialog != null)
			previewFrame = new PreviewDialog(report, reportId, setYear,
					param_Child, dialog);
		else
			previewFrame = new PreviewDialog(report, reportId, setYear,
					param_Child);
		previewFrame.print(report);
		previewFrame.setVisible(true);
		report.setPrinting(false);
		return info;
	}

	private static void print1(PreviewFrame previewFrame, final Report report,
			final String reportId, final String setYear,
			final String[] param_Child) {
		Component[] comps = previewFrame.getComponents();
		JButton btn = null;
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof JPanel) {
				btn = getButton("��ӡ(T)...", (JPanel) comps[i]);
				break;
			}
		}
		ActionListener[] al = btn.getActionListeners();
		for (int i = 0; i < al.length; i++) {
			btn.removeActionListener(al[i]);
		}
		MouseListener[] ml = btn.getMouseListeners();
		for (int i = 0; i < ml.length; i++) {
			btn.removeMouseListener(ml[i]);
		}
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					print(report, reportId, setYear, false, param_Child);
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		});
	}

	private static JButton getButton(String title, JPanel pnl) {
		int iCount = pnl.getComponentCount();
		for (int i = 0; i < iCount; i++) {
			if (pnl.getComponent(i) instanceof JButton) {
				if (((JButton) pnl.getComponent(i)).getText().equals(title))
					return (JButton) pnl.getComponent(i);

			}
			if (pnl.getComponent(i) instanceof JPanel) {
				JButton btn = getButton(title, (JPanel) pnl.getComponent(i));
				if (btn != null)
					return btn;
			}
		}
		return null;
	}

	/**
	 * ��ӡ������
	 * 
	 * @param report
	 *            ����
	 * @param reportId
	 *            ����ID
	 * @param setYear
	 *            ���
	 * @param isShowDialog
	 *            �Ƿ���ʾ��ӡ�Ի���
	 * @param titles_Child
	 *            ����
	 */
	public static InfoPackage print(Report report, String reportId,
			String setYear, boolean isShowDiaLog, String[] param_Child)
			throws Exception {
		if (isShowDiaLog) {
			return printPreview(report, reportId, setYear, param_Child);
		}
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		params_Child = param_Child;
		DataSet dsPrint = getPrintData(reportId, setYear);
		ReportSettings reportSettings = getReportSettings(report);
		// InfoPackage info = checkBeforePrint(report, dsPrint);
		if (dsPrint == null || dsPrint.isEmpty()) {
			report.print(false);
			return info;
		}
		if (report == null) {
			info.setSuccess(false);
			info.setsMessage("��ӡ����Ϊ��");
			return info;
		}
		// if (info.getSuccess() == false)
		// return info
		setPrintSettings(report, dsPrint, reportSettings);
		report.print(false);
		return info;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param report
	 * @return
	 * @throws Exception
	 */
	private static ReportSettings getReportSettings(Report report)
			throws Exception {
		return report.getReportSettings();
	}

	/**
	 * �ӱ����ȡ��ӡ����
	 * 
	 * @param reportId
	 * @param reportType
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	private static DataSet getPrintData(String reportId, String setYear)
			throws Exception {
		// String sql = "select report_id from fb_s_printinfo where REPORT_ID='"
		// + reportId + "' and " + "set_year=" + setYear;
		StringBuffer sqls = new StringBuffer();
		sqls.append("select ");
		sqls.append("REPORT_TITLE,");
		sqls.append("REPORT_TITLE_A1,");
		sqls.append("REPORT_TITLE_A2,");
		sqls.append("REPORT_TITLE_FONT,");
		sqls.append("REPORT_TITLE_FONT_SIZE,");
		sqls.append("PRINT_SCALE,");
		sqls.append("PAPER_SIZE,");
		sqls.append("PRINT_ORIENT,");
		sqls.append("MARGIN_LEFT,");
		sqls.append("MARGIN_RIGHT,");
		sqls.append("MARGIN_TOP,");
		sqls.append("MARGIN_BOTTOM,");
		sqls.append("TOP_TITLE_STARTROW,");
		sqls.append("TOP_TITLE_ENDROW,");
		sqls.append("LEFT_TITLE_STARTCOL,");
		sqls.append("LEFT_TITLE_ENDCOL,");
		sqls.append("REPORT_TITLE_HEIGHT,");
		sqls.append("REPORT_TITLE_ORIENT,");
		sqls.append("REPORT_TITLE_A1_HEIGHT,");
		sqls.append("REPORT_TITLE_A2_HEIGHT,");
		sqls.append("REPORT_TITLE_A1_FONT_SIZE,");
		sqls.append("REPORT_TITLE_A2_FONT_SIZE,");
		sqls.append("REPORT_TITLE_A1_ORIENT,");
		sqls.append("REPORT_TITLE_A2_ORIENT,");
		sqls.append("REPORT_TITLE_A1_FONT,");
		sqls.append("REPORT_TITLE_A2_FONT,");
		sqls.append("REPORT_TITLE_A1_USE,");
		sqls.append("REPORT_TITLE_A2_USE,");
		sqls.append("REPORT_BODY_FONT,");
		sqls.append("REPORT_BODY_FONT_SIZE,");
		sqls.append("REPORT_BODY_HEIGHT,");
		sqls.append("REPORT_AUTONEXT,");
		sqls.append("REPORT_PARAM_A1_ORIENT,");
		sqls.append("REPORT_PARAM_A2_ORIENT,");
		sqls.append("REPORT_PARAM_A1_POSTION,");
		sqls.append("REPORT_PARAM_A2_POSTION");
		sqls.append(" from fb_s_printinfo where report_id = '");
		sqls.append(reportId + "' and set_year = ");
		sqls.append(setYear);
		DataSet ds = PrintStub.getMethod().getDataSet(sqls.toString());
		return ds;
	}

	/**
	 * ����ӡ֮ǰ�Ƿ��Ѿ�׼����
	 * 
	 * @param report
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	private static InfoPackage checkBeforePrint(Report report, DataSet dsPrint)
			throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (dsPrint == null || dsPrint.isEmpty()) {
			info.setSuccess(false);
			info.setsMessage("û�ҵ��ñ���ص�����");
		} else {
			dsPrint.beforeFirst();
			dsPrint.next();
		}
		if (report == null) {
			info.setSuccess(false);
			info.setsMessage("û�ҵ�Ҫ��ӡ�ı���");
		}
		return info;
	}

	/**
	 * ���ô�ӡ����
	 * 
	 * @param report
	 * @param dsPrint
	 * @throws Exception
	 */
	private static void setPrintSettings(Report report, DataSet dsPrint,
			ReportSettings reportSettings) throws Exception {
		setPropertyDependDB(report, dsPrint, reportSettings);
		setPropertyAuto(report, reportSettings);
		setOnPage(reportSettings, dsPrint);
		report.setPrinting(true);
	}

	/**
	 * Ĭ�����õĴ�ӡ����
	 * 
	 * @param report
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setPropertyAuto(Report report,
			ReportSettings reportSettings) throws Exception {
		// ��ӡ˳��
		// reportSettings.setPageOrder(Constants.TOP_TO_BOTTOM);
		reportSettings.setPageOrder(Constants.LEFT_TO_RIGHT);
		// �Ƿ�����Ӧ
		reportSettings.setShrinkRowHeightToFitContent(true);
		// ������ʼ��ӡҳ
		// reportSettings.setFirstPageNumber(0);
		// ���÷ֱ���
		// reportSettings.setResolution(((Number) value);
		// �����Ƿ��ӡ����ɫ
		// reportSettings.setPrintBackground(false);
		//
	}

	/**
	 * ���ö��뷽ʽ
	 * 
	 * @param reportSettings
	 * @param dsPrint
	 * @throws Exception
	 */
	private static void setOnPage(ReportSettings reportSettings, DataSet dsPrint)
			throws Exception {
		// //���뷽ʽ
		boolean ish = PrintConVert.getOnPaper(dsPrint); // �Ƿ�ˮƽ
		if (ish)
			reportSettings.setHorizontalCenterOnPage(ish);
		else
			reportSettings.setHorizontalCenterOnPage(!ish);

	}

	/**
	 * ���ñ�������
	 * 
	 * @param report
	 * @throws Exception
	 */
	// private static void setBodyFont(Report report) throws Exception{
	// // report.PRINTING_STYLE.setFRFont(new
	// FRFont("����",FRFont.PLAIN,12,Color.BLACK)); //���ñ�������
	// }
	/**
	 * �ӿ���ȡ�������ô�ӡ����
	 * 
	 * @param report
	 * @param dsPrint
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setPropertyDependDB(Report report, DataSet dsPrint,
			ReportSettings reportSettings) throws Exception {
		setTitle(report, reportSettings, dsPrint);
		setPaperOrient(reportSettings, dsPrint);
		setScale(report, reportSettings);
		setPaperSize(dsPrint, reportSettings);
		setMagin(reportSettings, dsPrint);
		setRepeatCR(report, dsPrint);
	}

	private static String getBlankString(Report report) {
		double rwidth = 0;
		for (int kk = 0; kk < report.getColumnCount(); kk++) {
			rwidth = rwidth + report.getColumnWidth(kk);
		}
		String bblank = " ";
		String bblank1 = " ";
		for (int kk = 0; kk < rwidth / 10; kk++) {
			bblank = bblank + bblank1;
		}
		if (rwidth > 450 && rwidth < 500){
			bblank = bblank + "                                          ";
		}
		if (rwidth > 500 && rwidth < 550)
			bblank = bblank + "                              ";
		if (rwidth > 550 && rwidth <575)
			bblank = bblank + "                     ";
		if (rwidth > 575 && rwidth < 600)
			bblank = bblank + "                 ";
		if (rwidth > 650 && rwidth < 670)
			bblank = bblank + "   ";
		if (rwidth > 670 && rwidth < 700)
			bblank = bblank + "                        ";
		// if (rwidth>700 && rwidth<800){
		// for (int kk = 0 ; kk < (rwidth-700)/14;kk++)
		// bblank = bblank+" ";
		// }
		if (rwidth > 700 && rwidth < 730)
			bblank = "                                                       ";
		if (rwidth > 730 && rwidth < 750)
			bblank = "                                                   ";
		if (rwidth > 800 && rwidth<880)
			bblank = "                                   ";
		if (rwidth > 880)
			bblank = "                ";
		return bblank;
	}

	/**
	 * ���ñ���
	 * 
	 * @param reportSettings
	 * @param dsPrint
	 * @throws Exception
	 */
	private static void setTitle(Report report, ReportSettings reportSettings,
			DataSet dsPrint) throws Exception {
		if (params_Child == null || params_Child.length == 0)
			return;
		setHeaderFooterHeight(reportSettings);
		ReportHF header = new ReportHF();
		// ������ʽ
		FRFont hfont = FRFont.getInstance(PrintConVert.getTitleFont(dsPrint),
				FRFont.BOLD, PrintConVert.getTitleSize(dsPrint) + 2,
				Color.BLACK);
		// С����1��ʽ
		FRFont hfontTitle1 = FRFont.getInstance(PrintConVert
				.getChildTitle1Font(dsPrint) + 2, FRFont.PLAIN, PrintConVert
				.getChildTitle1FontSize(dsPrint), Color.BLACK);
		// С����2��ʽ
		FRFont hfontTitle2 = FRFont.getInstance(PrintConVert
				.getChildTitle1Font(dsPrint) + 2, FRFont.PLAIN, PrintConVert
				.getChildTitle2FontSize(dsPrint), Color.BLACK);
		// header.addCenterText(PrintConVert.getTitleName(dsPrint), hfont);
		addTitle(PrintConVert.getTitleName(dsPrint), header, PrintConVert
				.getTitleOrient(dsPrint), hfont);
		// addTitle("\r\n\r\n\r\n", header, 0, hfontTitle1);
		// addTitle("\r\n\r\n\r\n", header, 1, hfontTitle1);
		// addTitle("\r\n\r\n\r\n", header, 2, hfontTitle1);
		// �Ƿ����ñ���1
		int isT1Use = PrintConVert.getChildTitle1InUse(dsPrint);
		// �Ƿ����ñ���2
		int isT2Use = PrintConVert.getChildTitle2InUse(dsPrint);
		// ����1����
		// String title_Child1 = PrintConVert.getTitleA1(dsPrint);
		DataSet dsChildTitle1 = PrintStub
				.getMethod()
				.getDataSet(
						"select * from fb_s_pubcode where typeid like 'PRINTTITLE' and code ='01'");
		DataSet dsChildTitle2 = PrintStub
				.getMethod()
				.getDataSet(
						"select * from fb_s_pubcode where typeid like 'PRINTTITLE' and code ='02'");
		String title_Child1 = null;
		String title_Child2 = null;
		// ����1���뷽ʽ
		int iT1Orient = 0;// PrintConVert.getChildTitle1Orient(dsPrint);
		// ����2���뷽ʽ
		int iT2Orient = 0;// PrintConVert.getChildTitle2Orient(dsPrint);
		String bblank = getBlankString(report);
		if (dsChildTitle1 != null && !dsChildTitle1.isEmpty()) {
			title_Child1 = bblank
					+ dsChildTitle1.fieldByName("NAME").getString();
			iT1Orient = dsChildTitle1.fieldByName("N1").getInteger();
		}
		if (dsChildTitle2 != null && !dsChildTitle2.isEmpty()) {
			title_Child2 = dsChildTitle2.fieldByName("NAME").getString() + bblank;
			iT2Orient = dsChildTitle2.fieldByName("N1").getInteger();
		}
		// String title_Child2 = dsChildTitle.fieldByName("").getString();
		// ����2����
		// String title_Child2 = PrintConVert.getTitleA2(dsPrint);
		// ����1����
		String param_Child1 = params_Child[0];
		// ����2����
		String param_Child2 = params_Child[1];
		// ����1���뷽ʽ
		int iP1Orient = iT1Orient;// PrintConVert.getChildPamam1Orient(dsPrint);
		// ����2���뷽ʽ
		int iP2Orient = iT2Orient;// PrintConVert.getChildPamam2Orient(dsPrint);
		// ����1�ҿ�����
		int iP1Postion = 1;// PrintConVert.getChildPamam1Postion(dsPrint);
		// ����2�ҿ�����
		int iP2Postion = 2;// PrintConVert.getChildPamam2Postion(dsPrint);
		addTitle(" ", header, iT1Orient, hfontTitle1);
		addTitle(" ", header, iT1Orient, hfontTitle1);
		addTitle(" ", header, iT1Orient, hfontTitle1);
		addTitle(" ", header, iT2Orient, hfontTitle2);
		addTitle(" ", header, iT2Orient, hfontTitle2);
		addTitle(" ", header, iT2Orient, hfontTitle2);
		if (1 == isT1Use && 0 == isT2Use) {
			// ֻ���ñ���1
			if (1 == iP1Postion && 1 != iP2Postion) {
				// �������1�ҿ�����1,����2���ҿ�
				if (iT1Orient == iP1Orient) {
					// �������1�ͱ���1��һ��
					addTitle(title_Child1 + param_Child1, header, iT1Orient,
							hfontTitle1);
				} else {
					// �������1�ͱ���1����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
				}
			} else if (1 != iP1Postion && 1 == iP2Postion) {
				// �������2�ҿ�����1������1���ҿ�
				if (iT2Orient == iP1Orient) {
					// �������2�ͱ���1��һ��
					addTitle(title_Child1 + param_Child2, header, iT1Orient,
							hfontTitle1);
				} else {
					// �������2�ͱ���1����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				}
			} else if (1 == iP1Postion && 1 == iP2Postion) {
				// �������1��2���ҿ�����1
				if (iP1Orient == iT1Orient && iP2Orient != iT1Orient) {
					// �������1�ͱ���1��һ�𣬲���2����
					addTitle(title_Child1 + param_Child1, header, iT1Orient,
							hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				} else if (iP2Orient == iT1Orient && iP1Orient != iT1Orient) {
					// �������2�ͱ���1��һ�𣬲���1����
					addTitle(title_Child1 + param_Child2, header, iT1Orient,
							hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
				} else if (iP2Orient == iT1Orient && iP1Orient == iT1Orient) {
					// �������1,����2���ͱ���1��һ��
					addTitle(title_Child1 + param_Child1 + param_Child2,
							header, iT1Orient, hfontTitle1);
				} else if (iP2Orient != iT1Orient && iP1Orient != iT1Orient
						&& iP1Orient == iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2��һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1 + param_Child2, header, iP2Orient,
							hfontTitle1);
				} else if (iP2Orient != iT1Orient && iP1Orient != iT1Orient
						&& iP1Orient != iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2Ҳ����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				}
			} else {
				// �������1��2�����ҿ�����1
				addTitle(title_Child1, header, iT1Orient, hfontTitle1);
			}
		} else if (0 == isT1Use && 1 == isT2Use) {
			// ֻ���ñ���2
			if (2 == iP1Postion && 2 != iP2Postion) {
				// �������1�ҿ�����2,����2���ҿ�
				if (iT2Orient == iP1Orient) {
					// �������1�ͱ���1��һ��
					addTitle(title_Child2 + param_Child1, header, iT2Orient,
							hfontTitle2);
				} else {
					// �������1�ͱ���1����һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
				}
			} else if (2 != iP1Postion && 2 == iP2Postion) {
				// �������2�ҿ�����2������1���ҿ�
				if (iT2Orient == iP1Orient) {
					// �������1�ͱ���2��һ��
					addTitle(title_Child2 + param_Child2, header, iT2Orient,
							hfontTitle2);
				} else {
					// �������2�ͱ���2����һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
				}
			} else if (2 == iP1Postion && 2 == iP2Postion) {
				// �������1��2���ҿ�����2
				if (iP1Orient == iT2Orient && iP2Orient != iT2Orient) {
					// �������1�ͱ���2��һ�𣬲���2����
					addTitle(title_Child2 + param_Child1, header, iT2Orient,
							hfontTitle2);
					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
				} else if (iP2Orient == iT2Orient && iP1Orient != iT2Orient) {
					// �������2�ͱ���2��һ�𣬲���1����
					addTitle(title_Child2 + param_Child2, header, iT2Orient,
							hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
				} else if (iP2Orient == iT2Orient && iP1Orient == iT2Orient) {
					// �������1,����2���ͱ���2��һ��
					addTitle(title_Child2 + param_Child1 + param_Child2,
							header, iT2Orient, hfontTitle2);
				} else if (iP2Orient != iT2Orient && iP1Orient != iT2Orient
						&& iP1Orient == iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2��һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1 + param_Child2, header, iP2Orient,
							hfontTitle2);
				} else if (iP2Orient != iT2Orient && iP1Orient != iT2Orient
						&& iP1Orient != iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2Ҳ����һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
				}
			} else {
				// �������1��2�����ҿ�����2
				addTitle(title_Child2, header, iT2Orient, hfontTitle2);
			}
		} else if (1 == isT1Use && 1 == isT2Use) {
			// ������
			if (1 == iP1Postion && 1 != iP2Postion) {
				// �������1�ҿ�����1,����2���ҿ�
				if (iT1Orient == iP1Orient) {
					// �������1�ͱ���1��һ��
					addTitle(title_Child1 + param_Child1, header, iT1Orient,
							hfontTitle1);
				} else {
					// �������1�ͱ���1����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
				}
			} else if (1 != iP1Postion && 1 == iP2Postion) {
				// �������2�ҿ�����1������1���ҿ�
				if (iT2Orient == iP1Orient) {
					// �������2�ͱ���1��һ��
					addTitle(title_Child1 + param_Child2, header, iT1Orient,
							hfontTitle1);
				} else {
					// �������2�ͱ���1����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				}
			} else if (1 == iP1Postion && 1 == iP2Postion) {
				// �������1��2���ҿ�����1
				if (iP1Orient == iT1Orient && iP2Orient != iT1Orient) {
					// �������1�ͱ���1��һ�𣬲���2����
					addTitle(title_Child1 + param_Child1, header, iT1Orient,
							hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				} else if (iP2Orient == iT1Orient && iP1Orient != iT1Orient) {
					// �������2�ͱ���1��һ�𣬲���1����
					addTitle(title_Child1 + param_Child2, header, iT1Orient,
							hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
				} else if (iP2Orient == iT1Orient && iP1Orient == iT1Orient) {
					// �������1,����2���ͱ���1��һ��
					addTitle(title_Child1 + param_Child1 + param_Child2,
							header, iT1Orient, hfontTitle1);
				} else if (iP2Orient != iT1Orient && iP1Orient != iT1Orient
						&& iP1Orient == iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2��һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1 + param_Child2, header, iP2Orient,
							hfontTitle1);
				} else if (iP2Orient != iT1Orient && iP1Orient != iT1Orient
						&& iP1Orient != iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2Ҳ����һ��
					addTitle(title_Child1, header, iT1Orient, hfontTitle1);
					addTitle(param_Child1, header, iP1Orient, hfontTitle1);
					addTitle(param_Child2, header, iP2Orient, hfontTitle1);
				}
			} else {
				// �������1��2�����ҿ�����1
				addTitle(title_Child1, header, iT1Orient, hfontTitle1);
			}
			if (2 == iP1Postion && 2 != iP2Postion) {
				// �������1�ҿ�����2,����2���ҿ�
				if (iT2Orient == iP1Orient) {
					// �������1�ͱ���1��һ��
					addTitle(title_Child2 + param_Child1, header, iT2Orient,
							hfontTitle2);
				} else {
					// �������1�ͱ���1����һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
				}
			} else if (2 != iP1Postion && 2 == iP2Postion) {
				// �������2�ҿ�����2������1���ҿ�
//				if (iT2Orient == iP1Orient) {
					// �������1�ͱ���2��һ��
					addTitle(param_Child2+title_Child2, header, iT2Orient,
							hfontTitle2);
//				}
//				else {
//					 �������2�ͱ���2����һ��
//					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
//					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
//				}
			} else if (2 == iP1Postion && 2 == iP2Postion) {
				// �������1��2���ҿ�����2
				if (iP1Orient == iT2Orient && iP2Orient != iT2Orient) {
					// �������1�ͱ���2��һ�𣬲���2����
					addTitle(title_Child2 + param_Child1, header, iT2Orient,
							hfontTitle2);
					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
				} else if (iP2Orient == iT2Orient && iP1Orient != iT2Orient) {
					// �������2�ͱ���2��һ�𣬲���1����
					addTitle(title_Child2 + param_Child2, header, iT2Orient,
							hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
				} else if (iP2Orient == iT2Orient && iP1Orient == iT2Orient) {
					// �������1,����2���ͱ���2��һ��
					addTitle(title_Child2 + param_Child1 + param_Child2,
							header, iT2Orient, hfontTitle2);
				} else if (iP2Orient != iT2Orient && iP1Orient != iT2Orient
						&& iP1Orient == iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2��һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1 + param_Child2, header, iP2Orient,
							hfontTitle2);
				} else if (iP2Orient != iT2Orient && iP1Orient != iT2Orient
						&& iP1Orient != iP2Orient) {
					// �������1������2�����ͱ���1��һ�𣬲���1�Ͳ���2Ҳ����һ��
					addTitle(title_Child2, header, iT2Orient, hfontTitle2);
					addTitle(param_Child1, header, iP1Orient, hfontTitle2);
					addTitle(param_Child2, header, iP2Orient, hfontTitle2);
				}
			} else {
				// �������1��2�����ҿ�����2
				addTitle(title_Child2, header, iT2Orient, hfontTitle2);
			}
		}

		// if (Common.isEqual("1", sto)) { // ���Ϊ������ʾ
		// header.addLeftNewLine(10);
		// header
		// .addLeftText(title_Child1 + "\r\n" + title_Child2,
		// hfontTitle);
		//
		// } else if (Common.isEqual("2", sto)) { // ����
		// header.addCenterNewLine(10);
		// header.addCenterText(title_Child1 + "\r\n" + title_Child2,
		// hfontTitle);
		// } else { // ���һ�Ϊ��
		// header.addRightNewLine(10);
		// header.addRightText(title_Child1 + "\r\n" + title_Child2,
		// hfontTitle);
		// }
		// report.setHeader(header);
		report.setHeader(0, header);
		setFooter(report, hfontTitle2);
	}

	/**
	 * �ӱ���
	 * 
	 * @param text
	 *            ����
	 * @param header
	 *            ��ͷλ��
	 * @param sto
	 *            ���뷽ʽ
	 * @param hfont
	 *            �����ʽ
	 * @throws Exception
	 */
	private static void addTitle(String text, ReportHF header, int sto,
			FRFont hfont) throws Exception {
		if (0 == sto) { // ���Ϊ������ʾ
			header.addLeftNewLine(10);
			header.addLeftText("\r\n" + "\r\n" + "\r\n" + "          " + text,
					hfont);
		} else if (1 == sto) { // ����
			header.addCenterNewLine(10);
			header.addCenterText("\r\n" + "\r\n" + "\r\n" + text, hfont);
		} else { // ���һ�Ϊ�� //����
			header.addRightNewLine(10);
			header.addRightText("\r\n" + "\r\n" + "\r\n" + " " + text
					+ "           ", hfont);
		}
	}

	/**
	 * ����ҳüҳ�ŵĸ߶�
	 * 
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setHeaderFooterHeight(ReportSettings reportSettings)
			throws Exception {
		reportSettings.setHeaderHeight(20 / 25.4);
		reportSettings.setFooterHeight(10 / 25.4);
	}

	/**
	 * ����ҳ��
	 * 
	 * @param report
	 * @param hfont
	 * @throws Exception
	 */
	private static void setFooter(Report report, FRFont hfont) throws Exception {
		ReportHF footer = new ReportHF();
		footer.addCenterPageNumber(hfont);
		report.setFooter(0,footer);

	}

	/**
	 * �������ű���
	 * 
	 * @param report
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setScale(Report report, ReportSettings reportSettings)
			throws Exception {
		// reportSettings.setResolution(50);
	}

	/**
	 * ����ֽ�Ŵ�С
	 * 
	 * @param dsPrint
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setPaperSize(DataSet dsPrint,
			ReportSettings reportSettings) throws Exception {
		reportSettings.setPaperSize(PrintConVert.getPaperSize(dsPrint));
	}

	/**
	 * ����ֽ�Ŵ�ӡ����
	 * 
	 * @param reportSettings
	 * @param dsPrint
	 * @throws Exception
	 */
	private static void setPaperOrient(ReportSettings reportSettings,
			DataSet dsPrint) throws Exception {
		reportSettings.setOrientation(PrintConVert.getOrient(dsPrint));
	}

	/**
	 * ���ñ߾�
	 * 
	 * @param reportSettings
	 * @throws Exception
	 */
	private static void setMagin(ReportSettings reportSettings, DataSet dsPrint)
			throws Exception {
		Margin margin = reportSettings.getMargin();
		margin.setTop(PrintConVert.getMarginTop(dsPrint));
		margin.setLeft(PrintConVert.getMargnLeft(dsPrint));
		margin.setBottom(PrintConVert.getMarginBottom(dsPrint));
		margin.setRight(PrintConVert.getMargnRight(dsPrint));
	}

	/**
	 * ���ù̶���,�̶���
	 * 
	 * @param report
	 * @param dsPrint
	 * @throws Exception
	 */
	private static void setRepeatCR(Report report, DataSet dsPrint)
			throws Exception {
		// report.setStartRepeatColumn(PrintConVert.getStartRepeatCol(dsPrint));
		// report.setEndRepeatColumn(PrintConVert.getEndRepeatCol(dsPrint));
		// report.setStartRepeatRow(PrintConVert.getStartRepeatRow(dsPrint));
		// report.setEndRepeatRow(PrintConVert.getEndRepeatRow(dsPrint));
		int rows = PrintConVert.getStartRepeatCol(dsPrint);

		int rowe = PrintConVert.getEndRepeatRow(dsPrint);
		int cols = PrintConVert.getStartRepeatCol(dsPrint);
		int cole = PrintConVert.getEndRepeatCol(dsPrint);
		// for (int i = rows; i < rowe; i++) {
		// for (int j = cols; j < cole; j++) {
		// CellElement cell = report.getCellElement(i, j);
		// cell.setRepage(true);
		// }
		// }
		report.setPageRowHeaderFrom(rows);
		report.setPageRowHeaderTo(rowe);
		report.setPageColumnHeaderFrom(cols);
		report.setPageColumnHeaderTo(cole);
	}
}