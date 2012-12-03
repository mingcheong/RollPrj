/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.List;
import java.util.Map;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:summaryIndex设置单元
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */
public class SummaryIndexSet {

	/**
	 * 改变类型时，改变summaryIndex值
	 * 
	 * @param groupReport
	 * @param groupAble
	 * @param oldType
	 * @param newType
	 * @throws SummaryConverException
	 */
	public static void setSummaryIndexChangeType(ReportQuerySource querySource,
			GroupReport groupReport, IGroupAble groupAble, int oldType,
			int newType) {
		List lstCells = DefinePubOther.getCellsWithOutClone(groupReport);
		// 判断同一个summaryIndex是否需要拆分
		if (!isNeedSplit(querySource, groupAble, lstCells)) {
			return;
		}

		// A 分组汇总 B分组 C明细,共6种情况 B-A,C-A,C-B,A-B,A-C,B-C,后三种类型不处理
		if ((oldType == DefinePubOther.GROUP_TYPE || oldType == DefinePubOther.DETAIL_TYPE)
				&& newType == DefinePubOther.LEVISTOTAL_TYPE) {// B->A,C->A
			SummaryIndexUnt.checkSplit(querySource, groupAble, lstCells, true);
		} else if (oldType == DefinePubOther.DETAIL_TYPE
				&& newType == DefinePubOther.GROUP_TYPE) {// C->B
			// 得到分组summaryIndex
			int groupSummaryIndex = SummaryIndexUnt
					.getGroupSummaryIndex(lstCells);
			if (groupSummaryIndex == -1) {
				SummaryIndexUnt.checkSplit(querySource, groupAble, lstCells,
						true);
			} else {
				SummaryIndexUnt.checkSummaryIndexSet(querySource, groupAble,
						lstCells, groupSummaryIndex);
			}
		} else if (oldType == DefinePubOther.GROUP_TYPE
				&& newType == DefinePubOther.DETAIL_TYPE) {// B->C
			// 得到明细summaryIndex
			int detailSummaryIndex = SummaryIndexUnt.getDetailSummaryIndex(
					querySource, lstCells);
			if (detailSummaryIndex == -1) {
				SummaryIndexUnt.checkSplit(querySource, groupAble, lstCells,
						false);
			} else {
				SummaryIndexUnt.checkSummaryIndexSet(querySource, groupAble,
						lstCells, detailSummaryIndex);
			}
		}
	}

	/**
	 * 合并处理
	 * 
	 * @param lstData
	 * @return
	 */
	public static String setSummaryIndexUnite(List lstData) {
		List lstGroupAble = (List) ((Map) lstData.get(0))
				.get(IDefineReport.COL_ID);
		int groupAbleType = DefinePubOther.getGroupAbleType(lstGroupAble);
		IGroupAble[] groupAbleArr = DefinePubOther.convertType(lstGroupAble);

		if (groupAbleType == DefinePubOther.LEVISTOTAL_TYPE) { // 分组汇总列
			if (DefinePubOther.checkIsTotal(groupAbleArr)) {
				checkUniteGroupAble(lstData);
			} else {
				return "没有末级汇总，不可以合并！";
			}
		} else {
			checkUniteGroupAble(lstData);
		}
		return "";
	}

	/**
	 * 校验合并的summaryIndex值
	 * 
	 * @param lstData
	 */
	private static void checkUniteGroupAble(List lstData) {
		List lstGroupAbleTmp;
		IGroupAble groupAbleTmp;
		List lstGroupAble = (List) ((Map) lstData.get(0))
				.get(IDefineReport.COL_ID);
		IGroupAble groupAble = (IGroupAble) lstGroupAble.get(0);
		int size = lstData.size();
		for (int i = 1; i < size; i++) {
			lstGroupAbleTmp = (List) ((Map) lstData.get(i))
					.get(IDefineReport.COL_ID);
			int listSize = lstGroupAbleTmp.size();
			for (int j = 0; j < listSize; j++) {
				groupAbleTmp = (IGroupAble) lstGroupAbleTmp.get(j);
				// 末级汇总
				groupAbleTmp.setIsTotal("");
				groupAbleTmp.setSummaryIndex(groupAble.getSummaryIndex());
				groupAbleTmp.setIsMbSummary(0);
				groupAbleTmp.setIsMax(groupAble.getIsMax());
				groupAbleTmp.setLevel("");
			}
		}
	}

	/**
	 * 判断是否需要拆分
	 * 
	 * @param summaryIndex
	 * @return
	 * @throws SummaryConverException
	 */
	private static boolean isNeedSplit(ReportQuerySource querySource,
			IGroupAble groupAble, List lstCells) {
		String enumID;
		String codeEnumID = DefinePub.getEnumIDWithColID(querySource, groupAble
				.getSourceID(), groupAble.getSourceColID());
		IGroupAble groupAbleTmp;
		int count = 0;

		Map groupAbleMap = DefinePubOther.getGroupAbleValueMap(lstCells,
				Integer.parseInt(groupAble.getSummaryIndex()));
		Object[] values = groupAbleMap.values().toArray();
		int len = values.length;
		for (int i = 0; i < len; i++) {
			groupAbleTmp = (IGroupAble) values[i];
			if (groupAbleTmp == groupAble)
				continue;
			enumID = DefinePub.getEnumIDWithColID(querySource, groupAbleTmp
					.getSourceID(), groupAbleTmp.getSourceColID());
			if (enumID == null) {
				count++;
				continue;
			}
			if (!enumID.equalsIgnoreCase(codeEnumID)) {
				count++;
			}
		}
		if (count >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 拆分处理
	 * 
	 * @param lstData
	 * @return
	 * @throws SummaryConverException
	 */
	public static void setSummaryIndexSplit(ReportQuerySource querySource,
			List lstGroupAble, List lstCells) {
		IGroupAble groupAble;
		String sSummaryIndex = ((IGroupAble) lstGroupAble.get(0))
				.getSummaryIndex();
		int curSummaryIndex = Integer.parseInt(sSummaryIndex);

		int size = lstGroupAble.size();
		for (int i = 0; i < size; i++) {
			groupAble = (IGroupAble) lstGroupAble.get(i);
			// 已设置过的summaryIndex值的不重复校验
			if (Integer.parseInt(groupAble.getSummaryIndex()) < curSummaryIndex) {
				continue;
			}

			if (isNeedSplit(querySource, groupAble, lstCells)) {
				SummaryIndexUnt.checkSplit(querySource, groupAble, lstCells,
						true);
				curSummaryIndex++;
			}
		}
	}

}
