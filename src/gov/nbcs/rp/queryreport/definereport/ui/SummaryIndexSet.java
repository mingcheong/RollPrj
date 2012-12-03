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
 * Title:summaryIndex���õ�Ԫ
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */
public class SummaryIndexSet {

	/**
	 * �ı�����ʱ���ı�summaryIndexֵ
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
		// �ж�ͬһ��summaryIndex�Ƿ���Ҫ���
		if (!isNeedSplit(querySource, groupAble, lstCells)) {
			return;
		}

		// A ������� B���� C��ϸ,��6����� B-A,C-A,C-B,A-B,A-C,B-C,���������Ͳ�����
		if ((oldType == DefinePubOther.GROUP_TYPE || oldType == DefinePubOther.DETAIL_TYPE)
				&& newType == DefinePubOther.LEVISTOTAL_TYPE) {// B->A,C->A
			SummaryIndexUnt.checkSplit(querySource, groupAble, lstCells, true);
		} else if (oldType == DefinePubOther.DETAIL_TYPE
				&& newType == DefinePubOther.GROUP_TYPE) {// C->B
			// �õ�����summaryIndex
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
			// �õ���ϸsummaryIndex
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
	 * �ϲ�����
	 * 
	 * @param lstData
	 * @return
	 */
	public static String setSummaryIndexUnite(List lstData) {
		List lstGroupAble = (List) ((Map) lstData.get(0))
				.get(IDefineReport.COL_ID);
		int groupAbleType = DefinePubOther.getGroupAbleType(lstGroupAble);
		IGroupAble[] groupAbleArr = DefinePubOther.convertType(lstGroupAble);

		if (groupAbleType == DefinePubOther.LEVISTOTAL_TYPE) { // ���������
			if (DefinePubOther.checkIsTotal(groupAbleArr)) {
				checkUniteGroupAble(lstData);
			} else {
				return "û��ĩ�����ܣ������Ժϲ���";
			}
		} else {
			checkUniteGroupAble(lstData);
		}
		return "";
	}

	/**
	 * У��ϲ���summaryIndexֵ
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
				// ĩ������
				groupAbleTmp.setIsTotal("");
				groupAbleTmp.setSummaryIndex(groupAble.getSummaryIndex());
				groupAbleTmp.setIsMbSummary(0);
				groupAbleTmp.setIsMax(groupAble.getIsMax());
				groupAbleTmp.setLevel("");
			}
		}
	}

	/**
	 * �ж��Ƿ���Ҫ���
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
	 * ��ִ���
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
			// �����ù���summaryIndexֵ�Ĳ��ظ�У��
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
