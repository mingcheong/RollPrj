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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.fr.base.FRFont;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;

/**
 * The Class CellStyleFactoryBean.
 * 
 * @author qzc)
 * @version 1.0, Apr 14, 2009
 * @since fb6.2.81
 */
public class CellStyleFactoryBean implements FactoryBean, InitializingBean,
		DisposableBean {

	/** The style. */
	private Style style = null;

	/** The horizontal alignment. */
	private byte horizontalAlignment;

	/** The vertical alignment. */
	private byte verticalAlignment;

	/** The background color. */
	private Color backgroundColor;

	/** The border color. */
	private Color borderColor;

	/** The foreground color. */
	private Color foregroundColor;

	/** The font. */
	private Font font;

	/** The text style. */
	private byte textStyle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return style == null ? Style.class : style.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		style = Style.getInstance().deriveVerticalAlignment(verticalAlignment)
				.deriveHorizontalAlignment(horizontalAlignment);
		if (backgroundColor != null) {
			style = style.deriveBackground(ColorBackground
					.getInstance(backgroundColor));
		}
		if (font != null) {
			FRFont frfont;
			if (foregroundColor != null) {
				frfont = FRFont.getInstance(font.getName(), font.getStyle(),
						font.getSize(), foregroundColor);
			} else {
				frfont = FRFont.getInstance(font);
			}
			style = style.deriveFRFont(frfont);
		}
		if (borderColor != null) {
			style = style.deriveBorder(1, borderColor, 1, borderColor, 1,
					borderColor, 1, borderColor);
		}
		style = style.deriveTextStyle(textStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @param horizontalAlignment
	 *            the horizontalAlignment to set
	 */
	public void setHorizontalAlignment(byte horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * @param verticalAlignment
	 *            the verticalAlignment to set
	 */
	public void setVerticalAlignment(byte verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * 设置 background color.
	 * 
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * 设置 border color.
	 * 
	 * @param borderColor
	 *            the borderColor to set
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * 设置 foreground color.
	 * 
	 * @param foregroundColor
	 *            the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * 设置 font.
	 * 
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * 设置 text style.
	 * 
	 * @param textStyle
	 *            the textStyle to set
	 */
	public void setTextStyle(byte textStyle) {
		this.textStyle = textStyle;
	}

}
