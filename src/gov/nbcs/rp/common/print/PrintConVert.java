/**
 * ��ӡ�������ݵĶ���
 * 
 * qzc
 */
package gov.nbcs.rp.common.print;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import com.fr.report.PaperSize;
import com.fr.report.ReportConstants;

public class PrintConVert {
	// /////////////////// �ֶ�������/////////////////////////
	private static String REPORT_TITLE = "REPORT_TITLE"; // ����

	private static String REPORT_TITLE_FONT = "REPORT_TITLE_FONT"; // ��������

	private static String REPORT_TITLE_FONT_SIZE = "REPORT_TITLE_FONT_SIZE"; // ��������

	private static String REPORT_TITLE_A1 = "REPORT_TITLE_A1"; // �ӱ���1

	private static String REPORT_TITLE_A2 = "REPORT_TITLE_A2"; // �ӱ���2

	private static String PRINT_SCALE = "PRINT_SCALE"; // ���ű���

	private static String PAPER_SIZE = "PAPER_SIZE"; // ֽ�Ŵ�С

	private static String PRINT_ORIENT = "PRINT_ORIENT"; // ֽ�ŷ���

	private static String MARGIN_LEFT = "MARGIN_LEFT"; // ��߾�

	private static String MARGIN_RIGHT = "MARGIN_RIGHT"; // �ұ߾�

	private static String MARGIN_TOP = "MARGIN_TOP"; // �ϱ߾�

	private static String MARGIN_BOTTOM = "MARGIN_BOTTOM"; // �±߾�

	private static String TOP_TITLE_STARTROW = "TOP_TITLE_STARTROW"; // �̶���-��ʼ

	private static String TOP_TITLE_ENDROW = "TOP_TITLE_ENDROW"; // �̶���-����

	private static String LEFT_TITLE_STARTCOL = "LEFT_TITLE_STARTCOL";// �̶���-��ʼ

	private static String LEFT_TITLE_ENDCOL = "LEFT_TITLE_ENDCOL"; // �̶���-����

	/**
	 * ��ȡ����
	 * 
	 * @param title
	 * @return
	 */
	public static String getTitleName(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE).getString();
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleFont(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_FONT).getString();
	}

	/**
	 * ��ȡ���������С
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getTitleSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_FONT_SIZE).getInteger();
	}

	/**
	 * ��ȡ������뷽ʽ
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getTitleOrient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_ORIENT").getInteger();
	}

	/**
	 * ��ȡ�ӱ���1
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleA1(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_A1).getString();
	}

	/**
	 * ��ȡ�ӱ���2
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleA2(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_A2).getString();
	}

	/**
	 * ��ȡ���ű���
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getScale(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(PRINT_SCALE).getString();
	}

	/**
	 * ��ȡֽ�Ŵ�С
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static PaperSize getPaperSize(DataSet dsPrint) throws Exception {
		String size = dsPrint.fieldByName(PAPER_SIZE).getString();
		if (Common.isEqual("A1", size))
			return (PaperSize) PaperSize.PAPERSIZE_A1;
		else if (Common.isEqual("A2", size))
			return (PaperSize) PaperSize.PAPERSIZE_A2;
		else if (Common.isEqual("A3", size))
			return (PaperSize) PaperSize.PAPERSIZE_A3;
		else if (Common.isEqual("A4", size))
			return (PaperSize) PaperSize.PAPERSIZE_A4;
		else if (Common.isEqual("A5", size))
			return (PaperSize) PaperSize.PAPERSIZE_A5;
		else if (Common.isEqual("A6", size))
			return (PaperSize) PaperSize.PAPERSIZE_A6;
		else if (Common.isEqual("A7", size))
			return (PaperSize) PaperSize.PAPERSIZE_A7;
		else if (Common.isEqual("A8", size))
			return (PaperSize) PaperSize.PAPERSIZE_A8;
		else if (Common.isEqual("B1", size))
			return (PaperSize) PaperSize.PAPERSIZE_B1;
		else if (Common.isEqual("B2", size))
			return (PaperSize) PaperSize.PAPERSIZE_B2;
		else if (Common.isEqual("B3", size))
			return (PaperSize) PaperSize.PAPERSIZE_B3;
		else if (Common.isEqual("B4", size))
			return (PaperSize) PaperSize.PAPERSIZE_B4;
		else if (Common.isEqual("B5", size))
			return (PaperSize) PaperSize.PAPERSIZE_B5;
		else
			return (PaperSize) PaperSize.PAPERSIZE_A4;
	}

	/**
	 * ��ȡ��ӡ���� 0:���� 1:����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getOrient(DataSet dsPrint) throws Exception {
		String sPace = dsPrint.fieldByName(PRINT_ORIENT).getString();
		if (Common.isEqual(sPace, "0"))
			return ReportConstants.LANDSCAPE;
		else
			return ReportConstants.PORTRAIT;
	}

	/**
	 * ��ȡ���뷽ʽ true:ˮƽ���� false:��ֱ����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static boolean getOnPaper(DataSet dsPrint) throws Exception {
		String sOnPaper = dsPrint.fieldByName("N1").getString();
		if (Common.isEqual(sOnPaper, "0"))
			return true;
		else
			return false;
	}

	/**
	 * ��ȡ�ӱ���1���뷽ʽ
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle1Orient(DataSet dsPrint) throws Exception {
		if (dsPrint.fieldByName("REPORT_TITLE_A1_ORIENT").getValue() == null)
			return 0;
		return dsPrint.fieldByName("REPORT_TITLE_A1_ORIENT").getInteger();
	}

	/**
	 * ��ȡ�ӱ���2���뷽ʽ
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle2Orient(DataSet dsPrint) throws Exception {
		if (dsPrint.fieldByName("REPORT_TITLE_A2_ORIENT").getValue() == null)
			return 0;
		return dsPrint.fieldByName("REPORT_TITLE_A2_ORIENT").getInteger();
	}

	/**
	 * ��ȡ����1���뷽ʽ
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam1Orient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A1_ORIENT").getInteger();
	}

	/**
	 * ��ȡ����2���뷽ʽ
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam2Orient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A2_ORIENT").getInteger();
	}

	/**
	 * ��ȡ����1λ��
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam1Postion(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A1_POSTION").getInteger();
	}

	/**
	 * ��ȡ����2λ��
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam2Postion(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A2_POSTION").getInteger();
	}

	/**
	 * ��ȡ�Ƿ����ñ���1����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle1InUse(DataSet dsPrint) throws Exception {
		if (dsPrint.fieldByName("REPORT_TITLE_A1_USE").getValue() == null)
			return 0;
		return dsPrint.fieldByName("REPORT_TITLE_A1_USE").getInteger();
	}

	/**
	 * ��ȡ�Ƿ����ñ���1����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle2InUse(DataSet dsPrint) throws Exception {
		if (dsPrint.fieldByName("REPORT_TITLE_A2_USE").getValue() == null)
			return 0;
		return dsPrint.fieldByName("REPORT_TITLE_A2_USE").getInteger();
	}

	/**
	 * ��ȡ����1����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getChildTitle1Font(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A1_FONT").getString();
	}

	/**
	 * ��ȡ����2����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getChildTitle2Font(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A2_FONT").getString();
	}

	/**
	 * ��ȡ����1�����С
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle1FontSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A1_FONT_SIZE").getInteger();
	}

	/**
	 * ��ȡ����2�����С
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle2FontSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A2_FONT_SIZE").getInteger();
	}

	/**
	 * ��ȡ��߾�
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMargnLeft(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_LEFT).getInteger() / 25.4;
	}

	/**
	 * ��ȡ�ұ߾�
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMargnRight(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_RIGHT).getInteger() / 25.4;
	}

	/**
	 * ��ȡ�ϱ߾�
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMarginTop(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_TOP).getInteger() / 25.4;
	}

	/**
	 * ��ȡ�±߾�
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMarginBottom(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_BOTTOM).getInteger() / 25.4;
	}

	/**
	 * ��ȡ�̶��е���ʼ��
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getStartRepeatRow(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(TOP_TITLE_STARTROW).getInteger();
	}

	/**
	 * ��ȡ�̶��еĽ�����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getEndRepeatRow(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(TOP_TITLE_ENDROW).getInteger();
	}

	/**
	 * ��ȡ�̶��еĿ�ʼ��
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getStartRepeatCol(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(LEFT_TITLE_STARTCOL).getInteger();
	}

	/**
	 * ��ȡ�̶��еĽ�����
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getEndRepeatCol(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(LEFT_TITLE_ENDCOL).getInteger();
	}
}
