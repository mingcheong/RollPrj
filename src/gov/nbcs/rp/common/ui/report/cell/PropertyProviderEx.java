/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell;

import com.fr.base.Style;

/**
 * The Interface PropertyProviderEx. PropertyProvider����չ��
 */
public interface PropertyProviderEx extends PropertyProvider {

	/**
	 * ��ȡָ����Ԫ�����ʽ
	 *
	 * @param cell
	 *            the cell
	 *
	 * @return the cell style
	 */
	public Style getCellStyle(Cell cell);
}
