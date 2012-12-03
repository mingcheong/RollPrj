package gov.nbcs.rp.common.export.action;

import jxl.format.Alignment;
import jxl.format.Colour;

/**
 * 属性定义
 * 
 * @author Administrator
 * 
 */
public class ExportPropType {

	// //////////////////////////////////////////////////////表相关配置///////////////////////////////////////////////////////

	// 配置表的表名
	public static final String sConfigTableName = "FB_S_PRINTINFO";

	// 配置表里的关键字段
	public static final String sKeyFiled = "REPORT_ID";

	// 配置表里的年度
	public static final String sYear = "SET_YEAR";

	// ////////////////////////////////////////////////////大标题相关配置///////////////////////////////////////////////////////

	// 大标题名称对应的字段值
	public static final String sTitleName = "REPORT_TITLE";

	// 大标题字体对应的字段值
	public static final String sTitleFont = "REPORT_TITLE_FONT";

	// 大标题字体大小对应的字段值
	public static final String sTitleFontSize = "REPORT_TITLE_FONT_SIZE";

	// 大标题对应的字体大小
	public static final int size_Title = 14;

	public static final int FontSize_Title = 14;

	public static final Alignment postion_Title = Alignment.CENTRE;

	// ////////////////////////////////////////////////////小标题相关配置///////////////////////////////////////////////////////

	// 小标题名称的对应的字段
	public static final String[] sTitle_ChildName = new String[] {
			"REPORT_TITLE_A1", "REPORT_TITLE_A2" };

	// 小标题字体依次对应的大小
	public static final int[] iTitle_ChildSize = new int[] { 20, 20 };

	// 小标题对应的行高
	public static final int[] iTitle_ChildRowHeight = new int[] { 10, 10 };

	// 小标题依次字体
	public static final String[] sTitle_ChildFont = new String[] { "", "" };

	// 小标题依次对应的位置
	public static final Alignment[] postion_ChildTitle = new Alignment[] {
			Alignment.RIGHT, Alignment.RIGHT };

	// 小标题的个数 (这个不用改动)
	public static final int num_ChildTitlesCount = sTitle_ChildName.length;

	// ////////////////////////////////////////////////////表体相关配置///////////////////////////////////////////////////////

	// 表体的表头字体
	public static final String font_BodyHeaderFont = "宋体";

	// 表体的表头的行高
	// public static final int height_BodyHeader = 20;

	// 表体的表头字体大小
	public static final int fontsize_BodyHeaderSize = 10;

	// 表体的表头背景色
	public static final jxl.format.Colour bg_BodyHeaderColor = jxl.format.Colour.WHITE;

	// ////////////////////////////////////////////////////封面相关配置///////////////////////////////////////////////////////
	// 封面的背景色
	public static final Colour bg_Face = Colour.WHITE;

	// 封面对齐字段的字段名称
	public static final String size_FieldName = "HOR_ALIGNMENT";

	// 各个封面相关的对齐方式的对应方式 现在的对应方式是： 0：中间对齐 1：左对齐 2：右对齐
	public static final Alignment[] size_Face = new Alignment[] {
			Alignment.CENTRE, Alignment.LEFT, Alignment.RIGHT };
    
    

}
