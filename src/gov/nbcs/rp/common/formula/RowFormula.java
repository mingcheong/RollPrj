/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.formula;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

/**
 * The Class GroupRowFormula. ���ܼ����н���ʹ�õ�һЩ����
 * 
 * @author qj
 */
public interface RowFormula {

	/** The Constant TOTAL_SUM_ROW_FLAG_FIELD. �ܼ��б�ʶ. */
	public static final String TOTAL_SUM_ROW_FLAG_FIELD = "TOTAL_SUM_ROW_FLAG_FIELD";

	/** The Constant GROUP_SUM_ROW_FLAG_FIELD. �����б�ʶ. */
	public static final String GROUP_SUM_ROW_FLAG_FIELD = "GROUP_SUM_ROW_FLAG_FIELD";
	
	/** The Constant GROUP_SUM_ROW_FLAG_FIELD. �����б�ʶ�������뵥Ԫ�����ж�. */
	public static final String GROUP_SUM_ROW_FLAG_FIELD_NO_STYLE = "GROUP_SUM_ROW_FLAG_FIELD_NO_STYLE";

	/** The Constant GROUP_SUM_ROW_LEVEL_FIELD. ���� level �ֶ� */
	public static final String GROUP_SUM_ROW_LEVEL_FIELD = "GROUP_SUM_ROW_LEVEL_FIELD";

	/** The Constant GROUP_SUM_ROW_GROUPIDX_FIELD. ����group_index�ֶ� */
	public static final String GROUP_SUM_ROW_GROUPIDX_FIELD = "GROUP_SUM_ROW_GROUPIDX_FIELD";

	/** The Constant ROW_IS_MID_FLAG. */
	public static final String ROW_IS_MID_FLAG = "ROW_IS_MID_FLAG";// ��������
	
	/** ���������е�bookmark */
	public static final String GROUP_SUM_ROW_BOOKMARK_FIELD = "GROUP_SUM_ROW_BOOKMARK_FIELD";

	/**
	 * Calculate by row.
	 * 
	 * @param fields
	 *            the fields
	 * @param data
	 *            the data
	 * @param calFields
	 *            the cal fields
	 * @throws Exception
	 *             the exception
	 */
	public void calculateByRow(List fields, DataSet data, List calFields)
			throws Exception;
}
