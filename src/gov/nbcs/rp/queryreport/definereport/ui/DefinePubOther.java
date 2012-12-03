/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.besqryreport.ibs.IBesQryReport;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:�����幫����Ԫ
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateDate 2009-4-20
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class DefinePubOther {

	// �����������
	static final int LEVISTOTAL_TYPE = 1;

	// ��������
	static final int GROUP_TYPE = 2;

	// ��ϸ����
	static final int DETAIL_TYPE = 3;

	/**
	 * �õ�������cell�б�
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static List getCells(GroupReport groupReport)
			throws CloneNotSupportedException {
		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �õ�������
		int col = groupReport.getColumnCount();
		List lstReulst = new ArrayList();
		Object value;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value == null) {
				lstReulst.add(null);
				continue;
			}

			if (value instanceof MyGroupValueImpl) {
				lstReulst.add(((MyGroupValueImpl) value).clone());
			} else if (value instanceof MyCalculateValueImpl) {
				lstReulst.add(((MyCalculateValueImpl) value).clone());
			} else {
				lstReulst.add(null);
			}
		}

		return lstReulst;
	}

	/**
	 * �õ���ֵ
	 * 
	 * @return
	 */
	public static List getCellsWithOutClone(GroupReport groupReport) {
		// �õ��������к�
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// �õ�������
		int col = groupReport.getColumnCount();
		List lstReulst = new ArrayList();
		Object value;
		for (int i = 0; i < col; i++) {
			value = groupReport.getCellValue(i, rowOpe);
			if (value == null) {
				lstReulst.add(null);
				continue;
			}

			if (value instanceof MyGroupValueImpl) {
				lstReulst.add(value);
			} else if (value instanceof MyCalculateValueImpl) {
				lstReulst.add(value);
			} else {
				lstReulst.add(null);
			}
		}

		return lstReulst;
	}

	/**
	 * �ж��Ƿ�count��
	 * 
	 * @param measureAttr
	 * @return
	 */
	public static boolean isCountField(IMeasureAttr measureAttr) {
		if (IDefineReport.CHAR_Val.equalsIgnoreCase(measureAttr.getColType())) {
			return true;
		}
		return false;

	}

	public static Map getBuildSqlMap(String sqlTream, String veiwName,
			String sqlType) {
		Map mapResult = new HashMap();
		mapResult.put(IBesQryReport.SQLTREAM, sqlTream);
		mapResult.put(IBesQryReport.VIEWNAME, veiwName);
		mapResult.put(IBesQryReport.SQLTYPE, sqlType);
		return mapResult;
	}

	/**
	 * ���Ӷ���
	 * 
	 * @param sSql�����ӵ����
	 */
	public static void addComma(StringBuffer sSql) {
		if (!Common.isNullStr(sSql.toString())) {
			sSql.append(",");
		}
	}

	/**
	 * ���Ӷ���
	 * 
	 * @param sSql�����ӵ����
	 */
	public static String addComma(String sSql) {
		if (!Common.isNullStr(sSql)) {
			sSql = sSql + ",";
		}
		return sSql;
	}

	/**
	 * �����ַ������ӷ���||��
	 * 
	 * @param sSql
	 * @return
	 */
	public static String addStrLink(String sSql) {
		if (!Common.isNullStr(sSql)) {
			sSql = sSql + "||";
		}
		return sSql;
	}

	/**
	 * �����ַ������ӷ���||��
	 * 
	 * @param sSql
	 */
	public static void addStrLink(StringBuffer sSql) {
		if (!Common.isNullStr(sSql.toString())) {
			sSql.append("||");
		}
	}

	/**
	 * �õ����SummaryIndexֵ
	 * 
	 * @return
	 */
	public static int getMaxSummaryIndex(List lstCells) {
		Object value;
		IGroupAble[] groupAbleArray;
		String summaryIndex;
		int curSummaryIndex;
		int maxSummaryIndex = 0;

		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int groupArrayLen = groupAbleArray.length;
			for (int j = 0; j < groupArrayLen; j++) {
				summaryIndex = groupAbleArray[j].getSummaryIndex();
				if (!Common.isNullStr(groupAbleArray[j].getSummaryIndex())
						&& Common.isInteger(summaryIndex)) {
					curSummaryIndex = Integer.parseInt(summaryIndex);
					if (curSummaryIndex > maxSummaryIndex) {
						maxSummaryIndex = curSummaryIndex;
					}
				}
			}
		}
		return maxSummaryIndex;
	}

	/**
	 * �Ƿ���ϸ���ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isDetail(IGroupAble groupAble) {
		if (groupAble.getIsMax() == 1)
			return true;
		return false;
	}

	/**
	 * �Ƿ��������ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isLevIsTotal(IGroupAble groupAble) {
		if (groupAble.getIsMax() == 0
				&& (groupAble.getIsMbSummary() == 1
						|| !Common.isNullStr(groupAble.getLevel()) || Common
						.estimate(groupAble.getIsTotal()))) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ�������ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isGroup(IGroupAble groupAble) {
		if (groupAble.getIsMax() == 0
				&& (groupAble.getIsMbSummary() != 1
						&& Common.isNullStr(groupAble.getLevel()) && !Common
						.estimate(groupAble.getIsTotal()))) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ���ϸ���ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isDetail(List lstGroupAble) {
		IGroupAble groupAble;
		int size = lstGroupAble.size();
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) lstGroupAble.get(i);
			if (groupAble.getIsMax() == 1)
				return true;
		}
		return false;
	}

	/**
	 * �Ƿ��������ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isLevIsTotal(List lstGroupAble) {
		IGroupAble groupAble;
		int size = lstGroupAble.size();
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) lstGroupAble.get(i);
			if (groupAble.getIsMax() == 0
					&& (groupAble.getIsMbSummary() == 1
							|| !Common.isNullStr(groupAble.getLevel()) || Common
							.estimate(groupAble.getIsTotal()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �Ƿ�������ж�
	 * 
	 * @param groupAble
	 * @return
	 */
	public static boolean isGroup(List lstGroupAble) {
		IGroupAble groupAble;
		int size = lstGroupAble.size();
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) lstGroupAble.get(i);
			if (groupAble.getIsMax() == 0
					&& (groupAble.getIsMbSummary() != 1
							&& Common.isNullStr(groupAble.getLevel()) && !Common
							.estimate(groupAble.getIsTotal()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж��Ƿ�ҵ���һ���
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	public static boolean chekcIsMbSummary(Map groupAbleValueMap) {
		Object[] groupAbleArray = groupAbleValueMap.values().toArray();
		int size = groupAbleArray.length;
		for (int i = 0; i < size; i++) {
			if (((IGroupAble) groupAbleArray[i]).getIsMbSummary() == 1) {
				return true;
			}
		}
		return false;

	}

	/**
	 * �ж��Ƿ�ҵ���һ���
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	public static boolean chekcIsMbSummary(IGroupAble[] groupAbleArray) {
		int size = groupAbleArray.length;
		for (int i = 0; i < size; i++) {
			if ((groupAbleArray[i]).getIsMbSummary() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �õ�������ܲ��
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	public static String getLevel(IGroupAble[] groupAbleArray) {
		String level;
		int size = groupAbleArray.length;
		for (int i = 0; i < size; i++) {
			level = (groupAbleArray[i]).getLevel();
			if (!Common.isNullStr(level)) {
				return level;
			}
		}
		return "";
	}

	/**
	 * �õ�������ܲ��
	 * 
	 * @param groupAbleValueMap
	 * @return
	 */
	public static boolean checkIsTotal(IGroupAble[] groupAbleArray) {
		String isTotal;
		int size = groupAbleArray.length;
		for (int i = 0; i < size; i++) {
			isTotal = groupAbleArray[i].getIsTotal();
			if (!Common.isNullStr(isTotal) && Common.estimate(isTotal)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �õ�GroupAbleֵ,����summaryIndexֵ,��ֵ��Ϊkey,groupAble��Ϊֵ
	 * 
	 * @param summaryIndex
	 * @return
	 */
	public static Map getGroupAbleValueMap(List lstCells, int summaryIndex) {
		Object value;
		IGroupAble[] groupAbleArray;
		String sSummaryIndex;
		Map groupAbleValueMap = null;

		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int groupAbleLen = groupAbleArray.length;
			for (int j = 0; j < groupAbleLen; j++) {
				sSummaryIndex = groupAbleArray[j].getSummaryIndex();
				if (Common.isNullStr(sSummaryIndex)
						|| !Common.isInteger(sSummaryIndex)) {
					continue;
				}
				if (summaryIndex == Integer.parseInt(sSummaryIndex)) {
					if (groupAbleValueMap == null)
						groupAbleValueMap = new HashMap();
					groupAbleValueMap.put(String.valueOf(i), groupAbleArray[j]);
					break;
				}
			}
		}
		return groupAbleValueMap;
	}

	/**
	 * �õ�GroupAbleֵ,����summaryIndexֵ,��ֵ��Ϊkey,groupAble��Ϊֵ
	 * 
	 * @param summaryIndex
	 * @return
	 */
	public static List getGroupAbleValuelst(List lstCells, int summaryIndex) {
		Object value;
		IGroupAble[] groupAbleArray;
		String sSummaryIndex;
		List lstGroupAbleValue = null;

		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int groupAbleLen = groupAbleArray.length;
			for (int j = 0; j < groupAbleLen; j++) {
				sSummaryIndex = groupAbleArray[j].getSummaryIndex();
				if (Common.isNullStr(sSummaryIndex)
						|| !Common.isInteger(sSummaryIndex)) {
					continue;
				}
				if (summaryIndex == Integer.parseInt(sSummaryIndex)) {
					if (lstGroupAbleValue == null)
						lstGroupAbleValue = new ArrayList();
					lstGroupAbleValue.add(groupAbleArray[j]);
					break;
				}
			}
		}
		return lstGroupAbleValue;
	}

	/**
	 * �õ�groupAble����
	 * 
	 * @param groupAble
	 * @return
	 */
	public static int getGroupAbleType(IGroupAble groupAble) {
		if (isLevIsTotal(groupAble)) {
			return LEVISTOTAL_TYPE;
		} else if (isGroup(groupAble)) {
			return GROUP_TYPE;
		} else if (isDetail(groupAble)) {
			return DETAIL_TYPE;
		}
		return -1;
	}

	/**
	 * �õ�groupAble����
	 * 
	 * @param groupAble
	 * @return
	 */
	public static int getGroupAbleType(List lstGroupAble) {
		if (isLevIsTotal(lstGroupAble)) {
			return LEVISTOTAL_TYPE;
		} else if (isGroup(lstGroupAble)) {
			return GROUP_TYPE;
		} else if (isDetail(lstGroupAble)) {
			return DETAIL_TYPE;
		}
		return -1;
	}

	public static IGroupAble[] convertType(List lstGroupAble) {
		int len = lstGroupAble.size();
		if (len == 0)
			return null;

		IGroupAble[] groupAbleResult = new IGroupAble[len];
		for (int i = 0; i < len; i++) {
			groupAbleResult[i] = (IGroupAble) lstGroupAble.get(i);
		}

		return groupAbleResult;
	}

	public static List convertType(Object[] values) {
		List lstValues = null;
		int len = values.length;
		if (len == 0)
			return null;

		for (int i = 0; i < len; i++) {
			if (lstValues == null)
				lstValues = new ArrayList();
			lstValues.add(values[i]);
		}

		return lstValues;
	}

	// /**
	// * �ӵ�����
	// *
	// * @param sSql
	// * @return
	// */
	// public String addSingleQuotes(String sSql) {
	// return sSql = "'" + sSql + "'";
	// }
	//
	// /**
	// * �ӵ�����
	// *
	// * @param sSql
	// * @return
	// */
	// public void addSingleQuotes(StringBuffer sSql) {
	// sSql.insert(0, "'");
	// sSql.append("'");
	// }

	/**
	 * �ж��Ƿ񶼲����ϻ���
	 * 
	 * @return
	 */
	public static boolean isAllNotSumUp(List lstCells) {
		Object value;
		MyCalculateValueImpl calculateValueImpl;

		// �õ���������ı�־
		for (int i = 0; i < lstCells.size(); i++) {
			// �õ���Ԫ��ֵ
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {// ������
				calculateValueImpl = (MyCalculateValueImpl) value;
				String isSumUpValue = calculateValueImpl.getIsSumUp();

				// �Ƿ����ϻ���
				if (isSumUpValue == null
						|| IDefineReport.TRUE_NUM.equals(isSumUpValue)) {
					return false;
				}
			}
		}
		return true;
	}
}
