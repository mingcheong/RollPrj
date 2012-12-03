/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.List;

import com.foundercy.pf.reportcy.summary.iface.cell.IFormula;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;

/**
 * <p>
 * Title:将相同列字的refID校验为相同，目的：组织临时表时只需存储一个字段
 * </p>
 * <p>
 * Description:将相同列字的refID校验为相同，目的：组织临时表时只需存储一个字段
 * </p>
 * <p>

 */
public class CheckSameCol {


	private ReportQuerySource querySource;

	public CheckSameCol( ReportQuerySource querySource) {
		this.querySource = querySource;

	}

	public void check(List list) {

		IGroupAble[] groupAble;
		Object value;
		FunctionRef[] ref;
		IMeasureAttr[] measureAttr;
		int length;
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null) {
					length = groupAble.length;
					for (int j = 0; j < length; j++) {
						ref = groupAble[j].getFormula().getFunctionRefArray();
						for (int k = 0; k < ref.length; k++) {
							// 进行校验
							checkSameColExecute(groupAble[j].getFormula(),
									ref[k], list, i);
						}
					}
				}
			} else if (value instanceof MyCalculateValueImpl) {
				measureAttr = ((MyCalculateValueImpl) value).getMeasureArray();
				if (measureAttr == null)
					continue;
				length = measureAttr.length;
				for (int j = 0; j < length; j++) {
					checkSameColExecuteCacl((MyCalculateValueImpl) value,
							measureAttr[j], list, i);
				}
			}
		}

	}

	/**
	 * 执行将相同列字的ID校验为相同(计算列)
	 * 
	 * @param ref字段
	 * @param list所有列List
	 * @param curCol当前列
	 */
	private void checkSameColExecuteCacl(MyCalculateValueImpl calcValue,
			IMeasureAttr mea, List list, int curCol) {

		// 判断是否有条件，如有条件不进行校验,否则会出现分栏计算列就出现一列的情况
		IStatisticCaliber[] statisticCaliber = calcValue
				.getStatisticCaliberArray();
		if (statisticCaliber != null && statisticCaliber.length > 0) {
			return;
		}

		Object value;
		IGroupAble[] groupAble;
		boolean isEnum;
		RefEnumSource enumID;
		FunctionRef[] refArray;
		IMeasureAttr[] measureAttr;
		int length;

		RefEnumSource enumIDCode = DefinePub.getEnumRefWithColID(querySource,
				mea.getSourceID(), mea.getSourceColID());

		for (int i = 0; i <= curCol; i++) {
			value = list.get(i);
			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						refArray = groupAble[j].getFormula()
								.getFunctionRefArray();
						for (int k = 0; k < refArray.length; k++) {
							if (mea.getMeasureID().equals(
									refArray[k].getRefID()))
								continue;

							// 判断是不是枚举
							isEnum = DefinePub.judgetEnumWithColID(querySource,
									groupAble[j].getSourceID(), groupAble[j]
											.getSourceColID(), false);
							if (isEnum) {
								// 判断是不是对应同一枚举，如对应同一枚举，进行校验
								enumID = DefinePub.getEnumRefWithColID(
										querySource, refArray[k].getSourceID(),
										refArray[k].getSourceColID());
								if (!checkeIsSame(enumIDCode, enumID)) {
									continue;
								}
							} else {// 不是枚举
								if (!checkeIsSame(mea, refArray[k])) {
									continue;
								}
							}
							// 进行校验
							checkOutSameCol(calcValue, mea, refArray[k]
									.getRefID());
						}
					}
			} else if (value instanceof MyCalculateValueImpl) {
				// 判断计算列是不是有条件,如有条件不进行校验,否则会出现将没有条件字段与有条件字段校验成同一字段
				statisticCaliber = ((MyCalculateValueImpl) value)
						.getStatisticCaliberArray();
				if (statisticCaliber != null && statisticCaliber.length > 0) {
					return;
				}

				measureAttr = ((MyCalculateValueImpl) value).getMeasureArray();
				length = measureAttr.length;
				for (int j = 0; j < length; j++) {
					if (mea.getMeasureID()
							.equals(measureAttr[j].getMeasureID()))
						continue;
					// 判断是不是枚举
					isEnum = DefinePub.judgetEnumWithColID(querySource,
							measureAttr[j].getSourceID(), measureAttr[j]
									.getSourceColID(), false);
					if (isEnum) {

						// 判断是不是对应同一枚举，如对应同一枚举，进行校验
						enumID = DefinePub.getEnumRefWithColID(querySource,
								measureAttr[j].getSourceID(), measureAttr[j]
										.getSourceColID());
						if (!checkeIsSame(enumIDCode, enumID)) {
							continue;
						}
					} else {// 不是枚举
						if (!checkeIsSame(measureAttr[j], mea)) {
							continue;
						}
					}
					// 进行校验
					checkOutSameCol(calcValue, mea, measureAttr[j]
							.getMeasureID());
				}
			}
		}
	}

	/**
	 * 执行将相同列字的refID校验为相同
	 * 
	 * @param ref字段
	 * @param list所有列List
	 * @param curCol当前列
	 */
	private void checkSameColExecute(IFormula formula, FunctionRef ref,
			List list, int curCol) {
		Object value;
		IGroupAble[] groupAble;
		boolean isEnum;
		RefEnumSource enumID;
		FunctionRef[] refArray;
		IMeasureAttr[] measureAttr;
		int length;

		RefEnumSource enumIDCode = DefinePub.getEnumRefWithColID(querySource,
				ref.getSourceID(), ref.getSourceColID());

		for (int i = 0; i <= curCol; i++) {
			value = list.get(i);
			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						refArray = groupAble[j].getFormula()
								.getFunctionRefArray();
						for (int k = 0; k < refArray.length; k++) {
							if (ref.getRefID().equals(refArray[k].getRefID()))
								continue;

							// 判断是不是枚举
							isEnum = DefinePub.judgetEnumWithColID(querySource,
									groupAble[j].getSourceID(), groupAble[j]
											.getSourceColID(), false);
							if (isEnum) {
								// 判断是不是对应同一枚举，如对应同一枚举，进行校验
								enumID = DefinePub.getEnumRefWithColID(
										querySource, refArray[k].getSourceID(),
										refArray[k].getSourceColID());
								if (!checkeIsSame(enumIDCode, enumID)) {
									continue;
								}
							} else {// 不是枚举
								if (!checkeIsSame(ref, refArray[k])) {
									continue;
								}
							}
							// 进行校验
							checkOutSameCol(formula, ref, refArray[k]
									.getRefID());
						}
					}
			} else if (value instanceof MyCalculateValueImpl) {
				measureAttr = ((MyCalculateValueImpl) value).getMeasureArray();
				length = measureAttr.length;
				for (int j = 0; j < length; j++) {
					if (ref.getRefID().equals(measureAttr[j].getMeasureID()))
						continue;
					// 判断是不是枚举
					isEnum = DefinePub.judgetEnumWithColID(querySource,
							measureAttr[j].getSourceID(), measureAttr[j]
									.getSourceColID(), false);
					if (isEnum) {

						// 判断是不是对应同一枚举，如对应同一枚举，进行校验
						enumID = DefinePub.getEnumRefWithColID(querySource,
								measureAttr[j].getSourceID(), measureAttr[j]
										.getSourceColID());
						if (!checkeIsSame(enumIDCode, enumID)) {
							continue;
						}
					} else {// 不是枚举
						if (!checkeIsSame(measureAttr[j], ref)) {
							continue;
						}
					}
					// 进行校验
					checkOutSameCol(formula, ref, measureAttr[j].getMeasureID());
				}
			}
		}
	}

	/**
	 * 判断是否同一枚举
	 * 
	 * @param enumID
	 * @param enumIDComp
	 * @return
	 */
	private boolean checkeIsSame(RefEnumSource enumID, RefEnumSource enumIDComp) {
		if (enumID == null || enumIDComp == null)
			return false;

		if (enumIDComp.getEnumID().equals(enumID.getEnumID())
				&& enumIDComp.getRefEnumColCode().equals(
						enumID.getRefEnumColCode())) {
			return true;
		}
		return false;
	}

	/**
	 * 比较是否同一数据源列
	 * 
	 * @param ref
	 * @param refComp
	 * @return
	 */
	private boolean checkeIsSame(FunctionRef ref, FunctionRef refComp) {
		if (ref.getSourceID().equals(refComp.getSourceID())
				&& ref.getSourceColID().equals(refComp.getSourceColID())) {
			return true;
		}
		return false;
	}

	/**
	 * 比较是否同一数据源列
	 * 
	 * @param ref
	 * @param refComp
	 * @return
	 */
	private boolean checkeIsSame(IMeasureAttr mea, FunctionRef ref) {
		if (ref.getSourceID().equals(mea.getSourceID())
				&& ref.getSourceColID().equals(mea.getSourceColID())) {
			return true;
		}
		return false;
	}

	/**
	 * 比较是否同一数据源列
	 * 
	 * @param ref
	 * @param refComp
	 * @return
	 */
	private boolean checkeIsSame(IMeasureAttr mea, IMeasureAttr meaComp) {
		if (mea.getSourceID().equals(meaComp.getSourceID())
				&& mea.getSourceColID().equals(meaComp.getSourceColID())) {
			return true;
		}
		return false;
	}

	/**
	 * 相同列字开始校验
	 * 
	 * @param ref校验的字段
	 * @param refSame查找到的字段
	 */
	private void checkOutSameCol(IFormula formula, FunctionRef ref,
			String sIDSame) {
		String content = formula.getContent().replaceAll(ref.getRefID(),
				sIDSame);
		formula.setContent(content);
		ref.setRefID(sIDSame);
	}

	/**
	 * 相同列字开始校验(计算列）
	 * 
	 * @param ref校验的字段
	 * @param refSame查找到的字段
	 */
	private void checkOutSameCol(MyCalculateValueImpl calcValue,
			IMeasureAttr mea, String sIDSame) {
		String content = calcValue.getContent().replaceAll(mea.getMeasureID(),
				sIDSame);
		calcValue.setContent(content);
		mea.setMeasureID(sIDSame);
	}

	/**
	 * 判断在当前列前是否存在相同字段（分组列）
	 * 
	 * @param ref
	 * @param list
	 * @param curCol
	 * @return
	 */
	public static boolean isExistSameColGroup(FunctionRef ref, List list, int curCol) {
		IGroupAble[] groupAble;
		Object value;
		FunctionRef[] refArray;
		IMeasureAttr[] measureAttr;
		int length;
		for (int i = 0; i <= curCol; i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						refArray = groupAble[j].getFormula()
								.getFunctionRefArray();
						for (int k = 0; k < refArray.length; k++) {
							if (ref.equals(refArray[k]))
								return false;
							if (ref.getRefID().equals(refArray[k].getRefID()))
								return true;
						}
					}
			} else if (value instanceof MyCalculateValueImpl) {
				measureAttr = ((MyCalculateValueImpl) value).getMeasureArray();
				length = measureAttr.length;
				for (int j = 0; j < length; j++) {
					if (ref.getRefID().equals(measureAttr[j].getMeasureID()))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断在当前列前是否存在相同字段(计算列）
	 * 
	 * @param ref
	 * @param list
	 * @param curCol
	 * @return
	 */
	public static boolean isExistSameColCacl(IMeasureAttr measValue, List list,
			int curCol) {
		IGroupAble[] groupAble;
		Object value;
		FunctionRef[] refArray;
		IMeasureAttr[] measureAttr;
		int length;
		for (int i = 0; i <= curCol; i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						refArray = groupAble[j].getFormula()
								.getFunctionRefArray();
						for (int k = 0; k < refArray.length; k++) {

							if (measValue.getMeasureID().equals(
									refArray[k].getRefID()))
								return true;
						}
					}
			} else if (value instanceof MyCalculateValueImpl) {
				measureAttr = ((MyCalculateValueImpl) value).getMeasureArray();
				length = measureAttr.length;
				for (int j = 0; j < length; j++) {
					if (measValue.equals(measureAttr[j]))
						return false;
					if (measValue.getMeasureID().equals(
							measureAttr[j].getMeasureID()))
						return true;
				}
			}
		}
		return false;
	}

}
