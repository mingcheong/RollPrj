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
 * Title:summaryIndex������Ԫ
 * </p>
 * <p>
 * Description:summaryIndex������Ԫ
 * </p>

 */
public class SummaryIndexUnt {

	/**
	 * У��ԭ����������ϸֵsummaryIndexֵ
	 * 
	 * @param groupReport
	 */
	public static void checkGroupDetail(GroupReport groupReport) {
		// �õ��������к�
		List list = DefinePubOther.getCellsWithOutClone(groupReport);

		// �õ����maxSummaryIndexֵ
		int maxSummaryIndex = DefinePubOther.getMaxSummaryIndex(list);

		IGroupAble[] groupAbleArray;
		Object value;
		int length;
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i);
			// �ж��Ƿ��Ƿ�����
			if (value == null || !(value instanceof MyGroupValueImpl)) {
				continue;
			}

			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			if (groupAbleArray != null) {
				length = groupAbleArray.length;
				for (int j = 0; j < length; j++) {
					// �ж��Ƿ�������
					if (!Common.isNullStr(groupAbleArray[j].getSummaryIndex())) {
						continue;
					}

					if (DefinePubOther.isDetail(groupAbleArray[j])) {// ��ϸ��Ϣ
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(maxSummaryIndex + 2));

					} else if (DefinePubOther.isGroup(groupAbleArray[j])) {// ������
						groupAbleArray[j].setSummaryIndex(String
								.valueOf(maxSummaryIndex + 1));
					}
				}
			}
		}
	}

	/**
	 * �õ����ϻ������(���ֵ+1)
	 * 
	 * @param groupReport
	 * @return ���ϻ������
	 */
	public static String getSummaryIndex(GroupReport groupReport) {
		// �õ���������ͷ���Ŀ�ʼ��
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int rowCur = rowIndex[rowIndex.length - 1];

		// �õ�������
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		int maxSummaryIndex = 0;
		int curSummaryIndex = 0;
		// ѭ�������õ�����ֵ
		for (int i = 0; i < colCount; i++) {
			// �õ���Ԫ��ֵ
			if (groupReport.getCellElement(i, rowCur) == null)
				continue;
			object = groupReport.getCellElement(i, rowCur).getValue();
			// �ж��ǲ��Ƿ�����,���Ƿ������ж��Ƿ����ϻ��ܣ��õ���������
			if (!(object instanceof MyGroupValueImpl)) {
				continue;
			}
			iGroupAbleArray = ((MyGroupValueImpl) object).getGroupAbleArray();
			arrayNum = iGroupAbleArray.length;
			// ѭ��ȡ�õ�Ԫ������������������
			for (int j = 0; j < arrayNum; j++) {
				// �õ���ǰ�������
				if (Common.isNullStr(iGroupAbleArray[j].getSummaryIndex())
						|| !Common.isInteger(iGroupAbleArray[j]
								.getSummaryIndex()))
					continue;
				curSummaryIndex = Integer.parseInt(iGroupAbleArray[j]
						.getSummaryIndex());
				// �뵱ǰ�������Ž��бȽ�
				if (maxSummaryIndex < curSummaryIndex) {
					maxSummaryIndex = curSummaryIndex;
				}

			}
		}
		// ���ص�ǰ�������ż�1
		return String.valueOf(maxSummaryIndex + 1);
	}

	/**
	 * �õ�����summaryIndex
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
					// �ж��Ƿ��������еķ����У��絥λ���ʣ�
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
	 * �õ���ϸsummaryIndex
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
				// �ж��Ƿ�����ϸ���Ҳ���ö��������
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
	 * У��summaryIndex,���
	 * 
	 * @param querySource
	 * @param groupAble
	 * @param lstCells
	 * @param downMoveMode
	 *            true:�޸ĵ�groupAble������ͬsummaryIndex�������ƣ�false:�޸ĵ�groupAble��1��ͬsummaryIndex����
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
				if (curSummaryIndex > summaryIndex) {// ����ֱ�Ӽ�1
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
						// �ж��Ƿ�ͬһö��
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
	 * ����groupAble��ΪsummaryIndexSet
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
				if (curSummaryIndex == summaryIndex) {// ����ʱ�ж��Ƿ�ö������
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
