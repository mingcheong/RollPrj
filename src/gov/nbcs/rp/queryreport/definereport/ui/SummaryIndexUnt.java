/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.List;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:summaryIndex公共单元
 * </p>
 * <p>
 * Description:summaryIndex公共单元
 * </p>

 */
public class SummaryIndexUnt {

	/**
	 * 校验原报表分组和明细值summaryIndex值
	 * 
	 * @param groupReport
	 */
	public static void checkGroupDetail(GroupReport groupReport) {
		// 得到操作区行号
		List list = DefinePubOther.getCellsWithOutClone(groupReport);

		// 得到最大maxSummaryIndex值
		int maxSummaryIndex = DefinePubOther.getMaxSummaryIndex(list);

		IGroupAble[] groupAbleArray;
		Object value;
		int length;
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i);
			// 判断是否是分组列
			if (value == null || !(value instanceof MyGroupValueImpl)) {
				continue;
			}

			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			if (groupAbleArray != null) {
				length = groupAbleArray.length;
				for (int j = 0; j < length; j++) {
					// 判断是否已设置
					if (!Common.isNullStr(groupAbleArray[j].getSummaryIndex())) {
						continue;
					}

					if (DefinePubOther.isDetail(groupAbleArray[j])) {// 明细信息
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(maxSummaryIndex + 2));

					} else if (DefinePubOther.isGroup(groupAbleArray[j])) {// 分组列
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(maxSummaryIndex + 1));
					}
				}
			}
		}
	}

	/**
	 * 得到向上汇总序号(最大值+1)
	 * 
	 * @param groupReport
	 * @return 向上汇总序号
	 */
	public static String getSummaryIndex(GroupReport groupReport) {
		// 得到操作区行头区的开始行
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int rowCur = rowIndex[rowIndex.length - 1];

		// 得到总列数
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		int maxSummaryIndex = 0;
		int curSummaryIndex = 0;
		// 循环处理，得到排序值
		for (int i = 0; i < colCount; i++) {
			// 得到单元格值
			if (groupReport.getCellElement(i, rowCur) == null)
				continue;
			object = groupReport.getCellElement(i, rowCur).getValue();
			// 判断是不是分组列,如是分组列判断是否向上汇总，得到最大排序号
			if (!(object instanceof MyGroupValueImpl)) {
				continue;
			}
			iGroupAbleArray = ((MyGroupValueImpl) object).getGroupAbleArray();
			arrayNum = iGroupAbleArray.length;
			// 循环取得单元格数组中上向汇总序号
			for (int j = 0; j < arrayNum; j++) {
				// 得到当前排序序号
				if (Common.isNullStr(iGroupAbleArray[j].getSummaryIndex())
						|| !Common.isInteger(iGroupAbleArray[j]
								.getSummaryIndex()))
					continue;
				curSummaryIndex = Integer.parseInt(iGroupAbleArray[j]
						.getSummaryIndex());
				// 与当前最大排序号进行比较
				if (maxSummaryIndex < curSummaryIndex) {
					maxSummaryIndex = curSummaryIndex;
				}

			}
		}
		// 返回当前最大排序号加1
		return String.valueOf(maxSummaryIndex + 1);
	}

	/**
	 * 得到分组summaryIndex
	 * 
	 * @param groupReport
	 * @return
	 */
	public static int getGroupSummaryIndex(List lstCells) {
		Object value;
		IGroupAble[] groupAbleArray;
		int maxSummaryIndex = -1;
		String curSummaryIndex;
		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int arrSize = groupAbleArray.length;
			for (int j = 0; j < arrSize; j++) {
				if (DefinePubOther.isGroup(groupAbleArray[j])) {
					// 判断是否分组汇总中的分组列（如单位性质）
					List lstGroupAble = DefinePubOther.getGroupAbleValuelst(
							lstCells, Integer.parseInt(groupAbleArray[j]
									.getSummaryIndex()));
					if (DefinePubOther.isLevIsTotal(lstGroupAble)) {
						continue;
					}

					curSummaryIndex = groupAbleArray[j].getSummaryIndex();
					if (Common.isNullStr(curSummaryIndex)
							|| !Common.isInteger(curSummaryIndex)) {
						continue;
					}
					if (maxSummaryIndex < Integer.parseInt(curSummaryIndex)) {
						maxSummaryIndex = Integer.parseInt(curSummaryIndex);
					}
				}
			}

		}
		return maxSummaryIndex;
	}

	/**
	 * 得到明细summaryIndex
	 * 
	 * @param groupReport
	 * @return
	 * @throws SummaryConverException
	 */
	public static int getDetailSummaryIndex(ReportQuerySource querySource,
			List lstCells) {
		Object value;
		IGroupAble[] groupAbleArray;
		int maxSummaryIndex = -1;
		String curSummaryIndex;

		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int arrSize = groupAbleArray.length;
			for (int j = 0; j < arrSize; j++) {
				// 判断是否是明细列且不是枚举名称列
				if (DefinePubOther.isDetail(groupAbleArray[j])
						&& !DefinePub.judgetEnumNameWithColID(querySource,
								groupAbleArray[j].getSourceID(),
								groupAbleArray[j].getSourceColID())) {
					curSummaryIndex = groupAbleArray[j].getSummaryIndex();
					if (Common.isNullStr(curSummaryIndex)
							|| !Common.isInteger(curSummaryIndex)) {
						continue;
					}
					if (maxSummaryIndex < Integer.parseInt(curSummaryIndex)) {
						maxSummaryIndex = Integer.parseInt(curSummaryIndex);
					}
				}
			}

		}
		return maxSummaryIndex;
	}

	/**
	 * 校验summaryIndex,拆分
	 * 
	 * @param querySource
	 * @param groupAble
	 * @param lstCells
	 * @param downMoveMode
	 *            true:修改的groupAble不动，同summaryIndex其他下移，false:修改的groupAble加1，同summaryIndex不变
	 */
	public static void checkSplit(ReportQuerySource querySource,
			IGroupAble groupAble, List lstCells, boolean downMoveMode) {
		Object value;
		IGroupAble[] groupAbleArray;
		int curSummaryIndex;
		int summaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
		String codeEnumID = DefinePub.getEnumIDWithColID(querySource, groupAble
				.getSourceID(), groupAble.getSourceColID());
		String enumID;
		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int arrSize = groupAbleArray.length;
			for (int j = 0; j < arrSize; j++) {
				curSummaryIndex = Integer.parseInt(groupAbleArray[j]
						.getSummaryIndex());
				if (curSummaryIndex > summaryIndex) {// 大于直接加1
					groupAbleArray[j].setSummaryIndex(String
							.valueOf(curSummaryIndex + 1));
				} else if (curSummaryIndex == summaryIndex) {
					enumID = DefinePub.getEnumIDWithColID(querySource,
							groupAbleArray[j].getSourceID(), groupAbleArray[j]
									.getSourceColID());
					if (enumID == null) {
						if (groupAble == groupAbleArray[j]) {
							if (!downMoveMode) {
								groupAbleArray[j].setSummaryIndex(String
										.valueOf(curSummaryIndex + 1));
							}
						} else {
							groupAbleArray[j].setSummaryIndex(String
									.valueOf(curSummaryIndex + 1));
						}
					} else {
						// 判断是否同一枚举
						if (downMoveMode) {
							if (enumID.equalsIgnoreCase(codeEnumID)) {
								continue;
							} else {
								groupAbleArray[j].setSummaryIndex(String
										.valueOf(curSummaryIndex + 1));
							}
						} else {
							if (enumID.equalsIgnoreCase(codeEnumID)) {
								groupAbleArray[j].setSummaryIndex(String
										.valueOf(curSummaryIndex + 1));
							} else {
								continue;
							}

						}
					}
				}
			}
		}
	}

	/**
	 * 设置groupAble设为summaryIndexSet
	 * 
	 * @param querySource
	 * @param groupAble
	 * @param lstCells
	 * @param summaryIndexSet
	 */
	public static void checkSummaryIndexSet(ReportQuerySource querySource,
			IGroupAble groupAble, List lstCells, int summaryIndexSet) {

		Object value;
		IGroupAble[] groupAbleArray;
		int curSummaryIndex;
		int summaryIndex = Integer.parseInt(groupAble.getSummaryIndex());
		groupAble.setSummaryIndex(String.valueOf(summaryIndexSet));
		String codeEnumID = DefinePub.getEnumIDWithColID(querySource, groupAble
				.getSourceID(), groupAble.getSourceColID());
		String enumID;
		int size = lstCells.size();
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (!(value instanceof MyGroupValueImpl)) {
				continue;
			}
			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int arrSize = groupAbleArray.length;
			for (int j = 0; j < arrSize; j++) {
				curSummaryIndex = Integer.parseInt(groupAbleArray[j]
						.getSummaryIndex());
				if (curSummaryIndex == summaryIndex) {// 等于时判断是否枚举名称
					enumID = DefinePub.getEnumIDWithColID(querySource,
							groupAbleArray[j].getSourceID(), groupAbleArray[j]
									.getSourceColID());
					if (enumID == null)
						continue;
					if (enumID.equalsIgnoreCase(codeEnumID)) {
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(summaryIndexSet));
					}
				}
			}
		}

	}

}
