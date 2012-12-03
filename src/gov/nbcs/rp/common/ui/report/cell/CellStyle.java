/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0 #001 ��ֲ��FineReport6.1.2�汾
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
	 * ����ɫ
	 */
	public static final class BackgroundColors {

		public static final Color HEADER_COLOR = new Color(250, 228, 184); // ��ͷ��ɫ
		public static final Color EDITABLE_COLOR = Color.WHITE; // �����пɱ༭��Ԫ����ɫ
		public static final Color READONLY_COLOR = new Color(235, 235, 235); // ������ֻ����Ԫ����ɫ
		public static final Color SELECTED_COLOR = new Color(200, 200, 255); // ��Ԫ��ѡ��ʱ�ı���ɫ

		public static final Color ODD_ROW_COLOR; // �����б���ɫ
		public static final Color EVEN_ROW_COLOR; // ż���б���ɫ
		public static final Color ATTENT_COLOR; // ������ʾ��Ԫ�񱳾���ɫ
		public static final Color CALC_COLOR; // ������ʾ���㵥Ԫ��
		static {
			BeanFactory bf = BeanFactoryUtil
					.getBeanFactory(report_theme_xml_location);
			// ��ɫֵ
			ODD_ROW_COLOR = (Color) bf.getBean("fb.cellstyle.color.ODD_ROW"); // �����б���ɫ
			EVEN_ROW_COLOR = (Color) bf.getBean("fb.cellstyle.color.EVEN_ROW"); // ż���б���ɫ
			ATTENT_COLOR = (Color) bf.getBean("fb.cellstyle.color.ATTENT"); // ������ʾ��Ԫ�񱳾���ɫ
			CALC_COLOR = (Color) bf.getBean("fb.cellstyle.color.CALC"); // ������ʾ���㵥Ԫ��
		}
	}

	/**
	 * �߿�ɫ
	 */
	public static final class BorderColors {

		public static final Color LIGHT_GRAY_COLOR = Color.LIGHT_GRAY; // �߿���ɫ

	}

	/** The Constant report_theme_xml_location. */
	private final static String report_theme_xml_location = "gov/nbcs/rp/common/ui/report/report-theme.xml";

	/**
	 * Ĭ�ϵı�ͷ��Ԫ����ʽ
	 */
	public static final Style HEADER_DEFAULT;

	/**
	 * ��ͷ��Ԫ����ʽ�����ֺ�
	 */
	public static final Style HEADER_BIG_FONT;

	/**
	 * �ɱ༭�������֡��Ǽ�����
	 */
	public static final Style EDITABLE;

	/**
	 * Ĭ��״̬
	 */
	public static final Style NORMAL;

	/**
	 * ���ɱ༭�������֡��Ǽ�����
	 */
	public static final Style READONLY;

	/**
	 * ���ɱ༭�������֡�������
	 */
	public static final Style READONLY_SUM;

	/**
	 * ���ɱ༭��������������
	 */
	public static final Style READONLY_POSITIVE_SUM;

	/**
	 * ���ɱ༭��������������
	 */
	public static final Style READONLY_NEGATIVE_SUM;

	/**
	 * �ɱ༭�����֡��Ǽ�����
	 */
	public static final Style EDITABLE_NUMBER;

	/**
	 * ���ɱ༭�����֡��Ǽ�����
	 */
	public static final Style READONLY_NUMBER;

	/**
	 * Ĭ�ϵĴ�ӡ��ʽ
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
	 * ��ȡStyle
	 * 
	 * @param isEditable
	 *            �Ƿ�ɱ༭״̬
	 * @param isSumRow
	 *            �Ƿ������
	 * @param isNumber
	 *            �Ƿ�����
	 * @param isNegative
	 *            �Ƿ���
	 * @return com.fr.base.Style
	 */
	public static Style getInstance(boolean isEditable, boolean isSumRow,
			boolean isNumber, boolean isNegative) {
		return getInstance(isEditable, isSumRow, isNumber, isNegative, false);
	}

	/**
	 * ��ȡStyle.
	 * 
	 * @param isEditable
	 *            �Ƿ�ɱ༭״̬
	 * @param isSumRow
	 *            �Ƿ������
	 * @param isNumber
	 *            �Ƿ�����
	 * @param isNegative
	 *            �Ƿ���
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
				// �Ƿ�ɱ༭
				CellGUIAttr ga = cell.getCellGUIAttr();
				boolean isEditable = (ga != null)
						&& (ga.getEditableState() == Constants.EDITABLE_STATE_TRUE);
				// �Ƿ������
				boolean isSumRow = (report != null)
						&& report.isSumRow(cell.getRow());
				// �Ƿ�����
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

			// ��ӡʱ����ʽ
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
				// �Ƿ�ɱ༭
				boolean isEditable = cell.getEditableState() == Constants.EDITABLE_STATE_TRUE;
				// �Ƿ������
				boolean isSumRow = (report != null)
						&& report.isSumRow(cell.getRow());
				// �Ƿ�����
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
			// ��ӡʱ����ʽ
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
