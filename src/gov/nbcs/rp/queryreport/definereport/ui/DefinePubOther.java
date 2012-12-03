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
 * Title:报表定义公共单元
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateDate 2009-4-20
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class DefinePubOther {

	// 分组汇总类型
	static final int LEVISTOTAL_TYPE = 1;

	// 分组类型
	static final int GROUP_TYPE = 2;

	// 明细类型
	static final int DETAIL_TYPE = 3;

	/**
	 * 得到操作区cell列表
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static List getCells(GroupReport groupReport)
			throws CloneNotSupportedException {
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
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
	 * 得到列值
	 * 
	 * @return
	 */
	public static List getCellsWithOutClone(GroupReport groupReport) {
		// 得到操作区行号
		int rowOpe = DefinePub.getOpeRow(groupReport);
		// 得到总列数
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
	 * 判断是否count列
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
	 * 增加逗号
	 * 
	 * @param sSql需增加的语句
	 */
	public static void addComma(StringBuffer sSql) {
		if (!Common.isNullStr(sSql.toString())) {
			sSql.append(",");
		}
	}

	/**
	 * 增加逗号
	 * 
	 * @param sSql需增加的语句
	 */
	public static String addComma(String sSql) {
		if (!Common.isNullStr(sSql)) {
			sSql = sSql + ",";
		}
		return sSql;
	}

	/**
	 * 增加字符串联接符（||）
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
	 * 增加字符串联接符（||）
	 * 
	 * @param sSql
	 */
	public static void addStrLink(StringBuffer sSql) {
		if (!Common.isNullStr(sSql.toString())) {
			sSql.append("||");
		}
	}

	/**
	 * 得到最大SummaryIndex值
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
	 * 是否明细列判断
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
	 * 是否分组汇总判断
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
	 * 是否分组列判断
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
	 * 是否明细列判断
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
	 * 是否分组汇总判断
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
	 * 是否分组列判断
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
	 * 判断是否业务处室汇总
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
	 * 判断是否业务处室汇总
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
	 * 得到分组汇总层次
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
	 * 得到分组汇总层次
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
	 * 得到GroupAble值,根据summaryIndex值,列值作为key,groupAble作为值
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
	 * 得到GroupAble值,根据summaryIndex值,列值作为key,groupAble作为值
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
	 * 得到groupAble类型
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
	 * 得到groupAble类型
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
	// * 加单引号
	// *
	// * @param sSql
	// * @return
	// */
	// public String addSingleQuotes(String sSql) {
	// return sSql = "'" + sSql + "'";
	// }
	//
	// /**
	// * 加单引号
	// *
	// * @param sSql
	// * @return
	// */
	// public void addSingleQuotes(StringBuffer sSql) {
	// sSql.insert(0, "'");
	// sSql.append("'");
	// }

	/**
	 * 判断是否都不向上汇总
	 * 
	 * @return
	 */
	public static boolean isAllNotSumUp(List lstCells) {
		Object value;
		MyCalculateValueImpl calculateValueImpl;

		// 得到四舍五入的标志
		for (int i = 0; i < lstCells.size(); i++) {
			// 得到单元格值
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			if (value instanceof MyCalculateValueImpl) {// 计算列
				calculateValueImpl = (MyCalculateValueImpl) value;
				String isSumUpValue = calculateValueImpl.getIsSumUp();

				// 是否向上汇总
				if (isSumUpValue == null
						|| IDefineReport.TRUE_NUM.equals(isSumUpValue)) {
					return false;
				}
			}
		}
		return true;
	}
}
