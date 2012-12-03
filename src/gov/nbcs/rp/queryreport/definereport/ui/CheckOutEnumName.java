/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.List;

import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:У�������У�ѭ�������ж��Ƿ��Ǳ����У����Ǳ����У�ѭ����ѯ�����У��ҵ������н���У��
 * </p>
 * <p>
 * Description:У�������У�ѭ�������ж��Ƿ��Ǳ����У����Ǳ����У�ѭ����ѯ�����У��ҵ������н���У��
 * </p>

 */
public class CheckOutEnumName {

	private ReportQuerySource querySource;

	public CheckOutEnumName(ReportQuerySource querySource) {
		this.querySource = querySource;

	}

	/**
	 * У�������У�ѭ�������ж��Ƿ��Ǳ����У����Ǳ����У�ѭ����ѯ�����У��ҵ������н���У��
	 * 
	 * @param list
	 */
	public void check(List list) {
		this.checkPub(list, false);
	}

	/**
	 * У��ִ��
	 * 
	 * @param groupAbleCode������
	 * @param list
	 *            ������
	 * @param curCol
	 *            ��ǰ��
	 */
	private void checkOutExecute(IGroupAble groupAbleCode, List list,
			int curCol, boolean isSummaryIndex) {
		Object value;
		IGroupAble[] groupAble;
		boolean isEnum;
		String enumID;

		String enumIDCode = DefinePub.getEnumIDWithColID(querySource,
				groupAbleCode.getSourceID(), groupAbleCode.getSourceColID());
		for (int i = 0; i < list.size(); i++) {
			if (i == curCol)
				continue;
			value = list.get(i);
			// �ж��Ƿ��Ƿ�����
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						// �ж��ǲ���ö��������
						isEnum = DefinePub.judgetEnumWithColID(querySource,
								groupAble[j].getSourceID(), groupAble[j]
										.getSourceColID(), false);
						if (!isEnum)
							continue;

						// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
						enumID = DefinePub.getEnumIDWithColID(querySource,
								groupAble[j].getSourceID(), groupAble[j]
										.getSourceColID());
						if (!enumIDCode.equals(enumID)) {
							continue;
						}
						// ����У��
						if (isSummaryIndex) {
							checkOutOpeSummaryIndex(groupAbleCode, groupAble[j]);
						} else {
							checkOutOpe(groupAbleCode, groupAble[j]);
						}
					}
			}
		}
	}

	/**
	 * ��ʼУ��
	 * 
	 * @param groupAbleCode����
	 * @param groupAbleName����
	 */
	private void checkOutOpe(IGroupAble groupAbleCode, IGroupAble groupAbleName) {
		// ĩ������
		groupAbleName.setIsTotal(groupAbleCode.getIsTotal());
		// ����
		// groupAbleName.setOrderIndex(groupAbleCode.getOrderIndex());
		groupAbleName.setSummaryIndex(groupAbleCode.getSummaryIndex());
		groupAbleName.setOrderType(groupAbleCode.getOrderType());
		groupAbleName.setIsMbSummary(groupAbleCode.getIsMbSummary());
		groupAbleName.setIsMax(groupAbleCode.getIsMax());
		groupAbleName.setLevel(groupAbleCode.getLevel());

	}

	/**
	 * У��su
	 * @param groupAbleCode
	 * @param groupAbleName
	 */
	private void checkOutOpeSummaryIndex(IGroupAble groupAbleCode,
			IGroupAble groupAbleName) {
		groupAbleName.setSummaryIndex(groupAbleCode.getSummaryIndex());
	}

	private void checkPub(List list, boolean isSummaryIndex) {
		IGroupAble[] groupAble;
		Object value;
		boolean isEnumPri;
		// ������У��,У���������һ��
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// �ж��Ƿ��Ƿ�����
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						// �ж��ǲ���ö������
						isEnumPri = DefinePub.judgetEnumWithColID(querySource,
								groupAble[j].getSourceID(), groupAble[j]
										.getSourceColID(), true);
						if (!isEnumPri)
							continue;

						checkOutExecute(groupAble[j], list, i, isSummaryIndex);
					}
			}
		}

	}

	/**
	 * ����summaryIndex
	 * 
	 * @param groupReport
	 */
	public void checkEnumSummaryIndex(GroupReport groupReport) {
		List list = DefinePubOther.getCellsWithOutClone(groupReport);

		this.checkPub(list, true);

	}

}
