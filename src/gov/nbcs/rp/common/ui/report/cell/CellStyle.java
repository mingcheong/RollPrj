/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0 #001 移植到FineReport6.1.2版本
 */

package gov.nbcs.rp.common.ui.report.cell;

import java.awt.Color;

import org.springframework.beans.factory.BeanFactory;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.report.Report;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.fr.base.Constants;
import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.report.cellElement.CellGUIAttr;

/**
 * The Class CellStyle.
 */
public class CellStyle {

	/** The Constant DA_ATTENT. */
	public static final int DA_ATTENT = 0x0001;

	/** The Constant DA_HINT. */
	public static final int DA_HINT = 0x0002;

	/** The Constant DA_CALC. */
	public static final int DA_CALC = 0x0004;

	/**
	 * 背景色
	 */
	public static final class BackgroundColors {

		public static final Color HEADER_COLOR = new Color(250, 228, 184); // 表头颜色
		public static final Color EDITABLE_COLOR = Color.WHITE; // 表体中可编辑单元格颜色
		public static final Color READONLY_COLOR = new Color(235, 235, 235); // 表体中只读单元格颜色
		public static final Color SELECTED_COLOR = new Color(200, 200, 255); // 单元格被选中时的背景色

		public static final Color ODD_ROW_COLOR; // 奇数行背景色
		public static final Color EVEN_ROW_COLOR; // 偶数行背景色
		public static final Color ATTENT_COLOR; // 着重提示单元格背景颜色
		public static final Color CALC_COLOR; // 高亮显示计算单元格
		static {
			BeanFactory bf = BeanFactoryUtil
					.getBeanFactory(report_theme_xml_location);
			// 颜色值
			ODD_ROW_COLOR = (Color) bf.getBean("fb.cellstyle.color.ODD_ROW"); // 奇数行背景色
			EVEN_ROW_COLOR = (Color) bf.getBean("fb.cellstyle.color.EVEN_ROW"); // 偶数行背景色
			ATTENT_COLOR = (Color) bf.getBean("fb.cellstyle.color.ATTENT"); // 着重提示单元格背景颜色
			CALC_COLOR = (Color) bf.getBean("fb.cellstyle.color.CALC"); // 高亮显示计算单元格
		}
	}

	/**
	 * 边框色
	 */
	public static final class BorderColors {

		public static final Color LIGHT_GRAY_COLOR = Color.LIGHT_GRAY; // 边框颜色

	}

	/** The Constant report_theme_xml_location. */
	private final static String report_theme_xml_location = "gov/nbcs/rp/common/ui/report/report-theme.xml";

	/**
	 * 默认的表头单元格样式
	 */
	public static final Style HEADER_DEFAULT;

	/**
	 * 表头单元格样式，大字号
	 */
	public static final Style HEADER_BIG_FONT;

	/**
	 * 可编辑、非数字、非计算行
	 */
	public static final Style EDITABLE;

	/**
	 * 默认状态
	 */
	public static final Style NORMAL;

	/**
	 * 不可编辑、非数字、非计算行
	 */
	public static final Style READONLY;

	/**
	 * 不可编辑、非数字、计算行
	 */
	public static final Style READONLY_SUM;

	/**
	 * 不可编辑、正数、计算行
	 */
	public static final Style READONLY_POSITIVE_SUM;

	/**
	 * 不可编辑、负数、计算行
	 */
	public static final Style READONLY_NEGATIVE_SUM;

	/**
	 * 可编辑、数字、非计算行
	 */
	public static final Style EDITABLE_NUMBER;

	/**
	 * 不可编辑、数字、非计算行
	 */
	public static final Style READONLY_NUMBER;

	/**
	 * 默认的打印样式
	 */
	public static final Style PRINTING_STYLE;

	static {
		BeanFactory bf = BeanFactoryUtil
				.getBeanFactory(report_theme_xml_location);
		HEADER_DEFAULT = (Style) bf.getBean("fb.cellstyle.HEADER_DEFAULT");
		HEADER_BIG_FONT = (Style) bf.getBean("fb.cellstyle.HEADER_BIG_FONT");
		EDITABLE = (Style) bf.getBean("fb.cellstyle.EDITABLE");
		NORMAL = (Style) bf.getBean("fb.cellstyle.NORMAL");
		READONLY = (Style) bf.getBean("fb.cellstyle.READONLY");
		READONLY_SUM = (Style) bf.getBean("fb.cellstyle.READONLY_SUM");
		READONLY_POSITIVE_SUM = (Style) bf
				.getBean("fb.cellstyle.READONLY_POSITIVE_SUM");
		READONLY_NEGATIVE_SUM = (Style) bf
				.getBean("fb.cellstyle.READONLY_NEGATIVE_SUM");
		EDITABLE_NUMBER = (Style) bf.getBean("fb.cellstyle.EDITABLE_NUMBER");
		READONLY_NUMBER = (Style) bf.getBean("fb.cellstyle.READONLY_NUMBER");
		PRINTING_STYLE = (Style) bf.getBean("fb.cellstyle.PRINTING");
	}

	/**
	 * 获取Style
	 * 
	 * @param isEditable
	 *            是否可编辑状态
	 * @param isSumRow
	 *            是否计算行
	 * @param isNumber
	 *            是否数字
	 * @param isNegative
	 *            是否负数
	 * @return com.fr.base.Style
	 */
	public static Style getInstance(boolean isEditable, boolean isSumRow,
			boolean isNumber, boolean isNegative) {
		return getInstance(isEditable, isSumRow, isNumber, isNegative, false);
	}

	/**
	 * 获取Style.
	 * 
	 * @param isEditable
	 *            是否可编辑状态
	 * @param isSumRow
	 *            是否计算行
	 * @param isNumber
	 *            是否数字
	 * @param isNegative
	 *            是否负数
	 * @param isOdd
	 *            the value is true if it is odd
	 * @return com.fr.base.Style
	 */
	public static Style getInstance(boolean isEditable, boolean isSumRow,
			boolean isNumber, boolean isNegative, boolean isOdd) {
		Style style = null;
		if (isEditable) {
			if (isNumber) {
				style = EDITABLE_NUMBER;
			} else {
				style = EDITABLE;
			}
		} else {
			if (isSumRow) {
				if (isNumber) {
					if (isNegative) {
						style = READONLY_NEGATIVE_SUM;
					} else {
						style = READONLY_POSITIVE_SUM;
					}
				} else {
					style = READONLY_SUM;
				}
			} else {
				if (isNumber) {
					style = READONLY_NUMBER;
				} else {
					style = READONLY;
				}
			}
			if (isOdd) {
				style = style.deriveBackground(ColorBackground
						.getInstance(BackgroundColors.ODD_ROW_COLOR));
			}
		}
		return style;
	}

	/**
	 * Gets the instance of Style.
	 * 
	 * @param cell
	 *            the cell
	 * @return single instance of CellStyle
	 */
	public static Style getInstance(Cell cell) {
		Style style;
		if ((cell != null) && (cell.getReport() != null)) {
			Report report = cell.getReport();

			if (cell.getCellType() == Cell.BODY_CELL) {
				// 是否可编辑
				CellGUIAttr ga = cell.getCellGUIAttr();
				boolean isEditable = (ga != null)
						&& (ga.getEditableState() == Constants.EDITABLE_STATE_TRUE);
				// 是否计算行
				boolean isSumRow = (report != null)
						&& report.isSumRow(cell.getRow());
				// 是否数字
				Object value = cell.getValue();
				boolean isNumber = (value != null) && (value instanceof Number);
				boolean isNegative = false;
				if (isNumber) {
					isNegative = Common.nonNullStr(value).startsWith("-");
				}
				boolean isOdd = cell.getRow() % 2 == 1;
				style = getInstance(isEditable, isSumRow, isNumber, isNegative,
						isOdd);
				if ((cell.getDecorateAttr() & CellStyle.DA_ATTENT) == CellStyle.DA_ATTENT) {
					style = style
							.deriveBackground(ColorBackground
									.getInstance(CellStyle.BackgroundColors.ATTENT_COLOR));

				} else if ((cell.getDecorateAttr() & CellStyle.DA_CALC) == CellStyle.DA_CALC) {
					style = style
					.deriveBackground(ColorBackground
							.getInstance(CellStyle.BackgroundColors.CALC_COLOR));

				} 
				if ((cell.getDecorateAttr() & DA_HINT) == DA_HINT) {
					style = style.deriveFRFont(FRFont.getInstance(style
							.getFRFont().getName(), style.getFRFont()
							.getStyle(), style.getFRFont().getSize(),
							Color.BLUE));
				}
			} else {
				style = CellStyle.HEADER_DEFAULT;
			}

			// 打印时的样式
			if (report.isPrinting()) {
				style = style.deriveBackground(ColorBackground
						.getInstance(Color.WHITE));
			}
		} else {
			style = NORMAL;
		}
		return style;
	}

	/**
	 * Gets the intance readonly.
	 * 
	 * @param cell
	 *            the cell
	 * @return the intance editable
	 */
	public static Style getIntanceReadonly(Cell cell) {
		Style style;
		if ((cell != null) && (cell.getReport() != null)) {
			Report report = cell.getReport();

			if (cell.getCellType() == Cell.BODY_CELL) {
				// 是否可编辑
				boolean isEditable = cell.getEditableState() == Constants.EDITABLE_STATE_TRUE;
				// 是否计算行
				boolean isSumRow = (report != null)
						&& report.isSumRow(cell.getRow());
				// 是否数字
				Object value = cell.getValue();
				boolean isNumber = (value != null) && (value instanceof Number);
				boolean isNegative = false;
				if (isNumber) {
					isNegative = Common.nonNullStr(value).startsWith("-");
				}
				boolean isOdd = cell.getRow() % 2 == 1;
				style = getInstance(isEditable, isSumRow, isNumber, isNegative,
						isOdd);
				if ((cell.getDecorateAttr() & CellStyle.DA_ATTENT) == CellStyle.DA_ATTENT) {
					style = style
							.deriveBackground(ColorBackground
									.getInstance(CellStyle.BackgroundColors.ATTENT_COLOR));

				}
				if ((cell.getDecorateAttr() & DA_HINT) == DA_HINT) {
					style = style.deriveFRFont(FRFont.getInstance(style
							.getFRFont().getName(), style.getFRFont()
							.getStyle(), style.getFRFont().getSize(),
							Color.BLUE));
				}
			} else {
				style = CellStyle.HEADER_DEFAULT;
			}
			// 打印时的样式
			if (report.isPrinting()) {
				style = style.deriveBackground(ColorBackground
						.getInstance(Color.WHITE));
			}
		} else {
			style = NORMAL;
		}
		return style;
	}
}
