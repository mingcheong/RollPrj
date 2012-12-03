/**
 * 打印属性数据的定义
 * 
 * qzc
 */
package gov.nbcs.rp.common.print;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import com.fr.report.PaperSize;
import com.fr.report.ReportConstants;

public class PrintConVert {
	// /////////////////// 字段名定义/////////////////////////
	private static String REPORT_TITLE = "REPORT_TITLE"; // 标题

	private static String REPORT_TITLE_FONT = "REPORT_TITLE_FONT"; // 标题字体

	private static String REPORT_TITLE_FONT_SIZE = "REPORT_TITLE_FONT_SIZE"; // 标题字体

	private static String REPORT_TITLE_A1 = "REPORT_TITLE_A1"; // 子标题1

	private static String REPORT_TITLE_A2 = "REPORT_TITLE_A2"; // 子标题2

	private static String PRINT_SCALE = "PRINT_SCALE"; // 缩放比例

	private static String PAPER_SIZE = "PAPER_SIZE"; // 纸张大小

	private static String PRINT_ORIENT = "PRINT_ORIENT"; // 纸张方向

	private static String MARGIN_LEFT = "MARGIN_LEFT"; // 左边距

	private static String MARGIN_RIGHT = "MARGIN_RIGHT"; // 右边距

	private static String MARGIN_TOP = "MARGIN_TOP"; // 上边距

	private static String MARGIN_BOTTOM = "MARGIN_BOTTOM"; // 下边距

	private static String TOP_TITLE_STARTROW = "TOP_TITLE_STARTROW"; // 固定行-起始

	private static String TOP_TITLE_ENDROW = "TOP_TITLE_ENDROW"; // 固定行-结束

	private static String LEFT_TITLE_STARTCOL = "LEFT_TITLE_STARTCOL";// 固定列-起始

	private static String LEFT_TITLE_ENDCOL = "LEFT_TITLE_ENDCOL"; // 固定列-结束

	/**
	 * 获取标题
	 * 
	 * @param title
	 * @return
	 */
	public static String getTitleName(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE).getString();
	}

	/**
	 * 获取标题字体
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleFont(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_FONT).getString();
	}

	/**
	 * 获取标题字体大小
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getTitleSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_FONT_SIZE).getInteger();
	}

	/**
	 * 获取标题对齐方式
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getTitleOrient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_ORIENT").getInteger();
	}

	/**
	 * 获取子标题1
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleA1(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_A1).getString();
	}

	/**
	 * 获取子标题2
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getTitleA2(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(REPORT_TITLE_A2).getString();
	}

	/**
	 * 获取缩放比例
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getScale(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(PRINT_SCALE).getString();
	}

	/**
	 * 获取纸张大小
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
	 * 获取打印方向 0:横向 1:纵向
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
	 * 获取对齐方式 true:水平对齐 false:垂直对齐
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
	 * 获取子标题1对齐方式
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
	 * 获取子标题2对齐方式
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
	 * 获取参数1对齐方式
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam1Orient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A1_ORIENT").getInteger();
	}

	/**
	 * 获取参数2对齐方式
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam2Orient(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A2_ORIENT").getInteger();
	}

	/**
	 * 获取参数1位置
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam1Postion(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A1_POSTION").getInteger();
	}

	/**
	 * 获取参数2位置
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildPamam2Postion(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_PARAM_A2_POSTION").getInteger();
	}

	/**
	 * 获取是否启用标题1参数
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
	 * 获取是否启用标题1参数
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
	 * 获取标题1字体
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getChildTitle1Font(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A1_FONT").getString();
	}

	/**
	 * 获取标题2字体
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static String getChildTitle2Font(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A2_FONT").getString();
	}

	/**
	 * 获取标题1字体大小
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle1FontSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A1_FONT_SIZE").getInteger();
	}

	/**
	 * 获取标题2字体大小
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getChildTitle2FontSize(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName("REPORT_TITLE_A2_FONT_SIZE").getInteger();
	}

	/**
	 * 获取左边距
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMargnLeft(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_LEFT).getInteger() / 25.4;
	}

	/**
	 * 获取右边距
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMargnRight(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_RIGHT).getInteger() / 25.4;
	}

	/**
	 * 获取上边距
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMarginTop(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_TOP).getInteger() / 25.4;
	}

	/**
	 * 获取下边距
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static double getMarginBottom(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(MARGIN_BOTTOM).getInteger() / 25.4;
	}

	/**
	 * 获取固定行的起始行
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getStartRepeatRow(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(TOP_TITLE_STARTROW).getInteger();
	}

	/**
	 * 获取固定行的结束行
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getEndRepeatRow(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(TOP_TITLE_ENDROW).getInteger();
	}

	/**
	 * 获取固定列的开始列
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getStartRepeatCol(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(LEFT_TITLE_STARTCOL).getInteger();
	}

	/**
	 * 获取固定列的结束列
	 * 
	 * @param dsPrint
	 * @return
	 * @throws Exception
	 */
	public static int getEndRepeatCol(DataSet dsPrint) throws Exception {
		return dsPrint.fieldByName(LEFT_TITLE_ENDCOL).getInteger();
	}
}
