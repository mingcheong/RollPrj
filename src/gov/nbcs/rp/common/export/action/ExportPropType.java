package gov.nbcs.rp.common.export.action;

import jxl.format.Alignment;
import jxl.format.Colour;

/**
 * ���Զ���
 * 
 * @author Administrator
 * 
 */
public class ExportPropType {

	// //////////////////////////////////////////////////////���������///////////////////////////////////////////////////////

	// ���ñ�ı���
	public static final String sConfigTableName = "FB_S_PRINTINFO";

	// ���ñ���Ĺؼ��ֶ�
	public static final String sKeyFiled = "REPORT_ID";

	// ���ñ�������
	public static final String sYear = "SET_YEAR";

	// ////////////////////////////////////////////////////������������///////////////////////////////////////////////////////

	// ��������ƶ�Ӧ���ֶ�ֵ
	public static final String sTitleName = "REPORT_TITLE";

	// ����������Ӧ���ֶ�ֵ
	public static final String sTitleFont = "REPORT_TITLE_FONT";

	// ����������С��Ӧ���ֶ�ֵ
	public static final String sTitleFontSize = "REPORT_TITLE_FONT_SIZE";

	// ������Ӧ�������С
	public static final int size_Title = 14;

	public static final int FontSize_Title = 14;

	public static final Alignment postion_Title = Alignment.CENTRE;

	// ////////////////////////////////////////////////////С�����������///////////////////////////////////////////////////////

	// С�������ƵĶ�Ӧ���ֶ�
	public static final String[] sTitle_ChildName = new String[] {
			"REPORT_TITLE_A1", "REPORT_TITLE_A2" };

	// С�����������ζ�Ӧ�Ĵ�С
	public static final int[] iTitle_ChildSize = new int[] { 20, 20 };

	// С�����Ӧ���и�
	public static final int[] iTitle_ChildRowHeight = new int[] { 10, 10 };

	// С������������
	public static final String[] sTitle_ChildFont = new String[] { "", "" };

	// С�������ζ�Ӧ��λ��
	public static final Alignment[] postion_ChildTitle = new Alignment[] {
			Alignment.RIGHT, Alignment.RIGHT };

	// С����ĸ��� (������øĶ�)
	public static final int num_ChildTitlesCount = sTitle_ChildName.length;

	// ////////////////////////////////////////////////////�����������///////////////////////////////////////////////////////

	// ����ı�ͷ����
	public static final String font_BodyHeaderFont = "����";

	// ����ı�ͷ���и�
	// public static final int height_BodyHeader = 20;

	// ����ı�ͷ�����С
	public static final int fontsize_BodyHeaderSize = 10;

	// ����ı�ͷ����ɫ
	public static final jxl.format.Colour bg_BodyHeaderColor = jxl.format.Colour.WHITE;

	// ////////////////////////////////////////////////////�����������///////////////////////////////////////////////////////
	// ����ı���ɫ
	public static final Colour bg_Face = Colour.WHITE;

	// ��������ֶε��ֶ�����
	public static final String size_FieldName = "HOR_ALIGNMENT";

	// ����������صĶ��뷽ʽ�Ķ�Ӧ��ʽ ���ڵĶ�Ӧ��ʽ�ǣ� 0���м���� 1������� 2���Ҷ���
	public static final Alignment[] size_Face = new Alignment[] {
			Alignment.CENTRE, Alignment.LEFT, Alignment.RIGHT };
    
    

}
