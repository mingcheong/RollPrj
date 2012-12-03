/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report.cell;

import java.awt.Color;
import java.awt.Font;

import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;

public class CellStyleFactory {

	/**
	 *
	 * @param horizontalAlignment
	 * @param verticalAlignment
	 * @param bgColor
	 * @param borderColor
	 * @param frFont
	 * @param textStyle
	 * @return
	 */
	public static Style create(byte horizontalAlignment,
			byte verticalAlignment, Color bgColor, Color borderColor,
			FRFont frFont, byte textStyle) {
		return create(bgColor, borderColor, null, textStyle)
				.deriveHorizontalAlignment(horizontalAlignment)
				.deriveVerticalAlignment(verticalAlignment)
				.deriveFRFont(frFont);
	}

	/**
	 *
	 * @param alignment
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @param textStyle
	 * @return
	 */
	public static Style create(byte alignment, Color bgColor,
			Color borderColor, Font font, byte textStyle) {
		return create(alignment, alignment, bgColor, borderColor, font,
				textStyle);
	}

	/**
	 *
	 * @param horizontalAlignment
	 * @param verticalAlignment
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @param textStyle
	 * @return
	 */
	public static Style create(byte horizontalAlignment,
			byte verticalAlignment, Color bgColor, Color borderColor,
			Font font, byte textStyle) {
		return create(bgColor, borderColor, font, textStyle)
				.deriveHorizontalAlignment(horizontalAlignment)
				.deriveVerticalAlignment(verticalAlignment);
	}

	/**
	 *
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @param textStyle
	 * @return
	 */
	public static Style create(Color bgColor, Color borderColor, Font font,
			byte textStyle) {
		return create(bgColor, borderColor, font).deriveTextStyle(textStyle);
	}

	/**
	 *
	 * @param alignment
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @return
	 */
	public static Style create(byte alignment, Color bgColor,
			Color borderColor, Font font) {
		return create(alignment, alignment, bgColor, borderColor, font);
	}

	/**
	 *
	 * @param horizontalAlignment
	 * @param verticalAlignment
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @return
	 */
	public static Style create(byte horizontalAlignment,
			byte verticalAlignment, Color bgColor, Color borderColor, Font font) {
		Style style = create(bgColor, borderColor, font)
				.deriveHorizontalAlignment(horizontalAlignment)
				.deriveVerticalAlignment(verticalAlignment);
		return style;
	}

	/**
	 *
	 * @param bgColor
	 * @param borderColor
	 * @param font
	 * @return
	 */
	public static Style create(Color bgColor, Color borderColor, Font font) {
		Style style = Style.getInstance();
		if (bgColor != null) {
			style = style
					.deriveBackground(ColorBackground.getInstance(bgColor));
		}
		if (font != null) {
			style = style.deriveFRFont(FRFont.getInstance(font));
		}
		if (borderColor != null) {
			style = style.deriveBorder(1, borderColor, 1, borderColor, 1,
					borderColor, 1, borderColor);
		}
		return style;
	}
}
