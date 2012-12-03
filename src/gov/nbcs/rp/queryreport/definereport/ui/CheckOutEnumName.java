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
 * Title:校验名称列，循环处理，判断是否是编码列，如是编码列，循环查询名称列，找到名称列进行校验
 * </p>
 * <p>
 * Description:校验名称列，循环处理，判断是否是编码列，如是编码列，循环查询名称列，找到名称列进行校验
 * </p>

 */
public class CheckOutEnumName {

	private ReportQuerySource querySource;

	public CheckOutEnumName(ReportQuerySource querySource) {
		this.querySource = querySource;

	}

	/**
	 * 校验名称列，循环处理，判断是否是编码列，如是编码列，循环查询名称列，找到名称列进行校验
	 * 
	 * @param list
	 */
	public void check(List list) {
		this.checkPub(list, false);
	}

	/**
	 * 校验执行
	 * 
	 * @param groupAbleCode编码列
	 * @param list
	 *            所有列
	 * @param curCol
	 *            当前列
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
			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						// 判断是不是枚举名称列
						isEnum = DefinePub.judgetEnumWithColID(querySource,
								groupAble[j].getSourceID(), groupAble[j]
										.getSourceColID(), false);
						if (!isEnum)
							continue;

						// 判断是不是对应同一枚举，如对应同一枚举，进行校验
						enumID = DefinePub.getEnumIDWithColID(querySource,
								groupAble[j].getSourceID(), groupAble[j]
										.getSourceColID());
						if (!enumIDCode.equals(enumID)) {
							continue;
						}
						// 进行校验
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
	 * 开始校验
	 * 
	 * @param groupAbleCode编码
	 * @param groupAbleName名称
	 */
	private void checkOutOpe(IGroupAble groupAbleCode, IGroupAble groupAbleName) {
		// 末级汇总
		groupAbleName.setIsTotal(groupAbleCode.getIsTotal());
		// 排序
		// groupAbleName.setOrderIndex(groupAbleCode.getOrderIndex());
		groupAbleName.setSummaryIndex(groupAbleCode.getSummaryIndex());
		groupAbleName.setOrderType(groupAbleCode.getOrderType());
		groupAbleName.setIsMbSummary(groupAbleCode.getIsMbSummary());
		groupAbleName.setIsMax(groupAbleCode.getIsMax());
		groupAbleName.setLevel(groupAbleCode.getLevel());

	}

	/**
	 * 校验su
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
		// 名称列校验,校验与编码列一致
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						// 判断是不是枚举主键
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
	 * 检验summaryIndex
	 * 
	 * @param groupReport
	 */
	public void checkEnumSummaryIndex(GroupReport groupReport) {
		List list = DefinePubOther.getCellsWithOutClone(groupReport);

		this.checkPub(list, true);

	}

}
