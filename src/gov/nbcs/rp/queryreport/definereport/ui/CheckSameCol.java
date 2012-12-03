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
 * Title:����ͬ���ֵ�refIDУ��Ϊ��ͬ��Ŀ�ģ���֯��ʱ��ʱֻ��洢һ���ֶ�
 * </p>
 * <p>
 * Description:����ͬ���ֵ�refIDУ��Ϊ��ͬ��Ŀ�ģ���֯��ʱ��ʱֻ��洢һ���ֶ�
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

			// �ж��Ƿ��Ƿ�����
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null) {
					length = groupAble.length;
					for (int j = 0; j < length; j++) {
						ref = groupAble[j].getFormula().getFunctionRefArray();
						for (int k = 0; k < ref.length; k++) {
							// ����У��
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
	 * ִ�н���ͬ���ֵ�IDУ��Ϊ��ͬ(������)
	 * 
	 * @param ref�ֶ�
	 * @param list������List
	 * @param curCol��ǰ��
	 */
	private void checkSameColExecuteCacl(MyCalculateValueImpl calcValue,
			IMeasureAttr mea, List list, int curCol) {

		// �ж��Ƿ�����������������������У��,�������ַ��������оͳ���һ�е����
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
			// �ж��Ƿ��Ƿ�����
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

							// �ж��ǲ���ö��
							isEnum = DefinePub.judgetEnumWithColID(querySource,
									groupAble[j].getSourceID(), groupAble[j]
											.getSourceColID(), false);
							if (isEnum) {
								// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
								enumID = DefinePub.getEnumRefWithColID(
										querySource, refArray[k].getSourceID(),
										refArray[k].getSourceColID());
								if (!checkeIsSame(enumIDCode, enumID)) {
									continue;
								}
							} else {// ����ö��
								if (!checkeIsSame(mea, refArray[k])) {
									continue;
								}
							}
							// ����У��
							checkOutSameCol(calcValue, mea, refArray[k]
									.getRefID());
						}
					}
			} else if (value instanceof MyCalculateValueImpl) {
				// �жϼ������ǲ���������,��������������У��,�������ֽ�û�������ֶ����������ֶ�У���ͬһ�ֶ�
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
					// �ж��ǲ���ö��
					isEnum = DefinePub.judgetEnumWithColID(querySource,
							measureAttr[j].getSourceID(), measureAttr[j]
									.getSourceColID(), false);
					if (isEnum) {

						// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
						enumID = DefinePub.getEnumRefWithColID(querySource,
								measureAttr[j].getSourceID(), measureAttr[j]
										.getSourceColID());
						if (!checkeIsSame(enumIDCode, enumID)) {
							continue;
						}
					} else {// ����ö��
						if (!checkeIsSame(measureAttr[j], mea)) {
							continue;
						}
					}
					// ����У��
					checkOutSameCol(calcValue, mea, measureAttr[j]
							.getMeasureID());
				}
			}
		}
	}

	/**
	 * ִ�н���ͬ���ֵ�refIDУ��Ϊ��ͬ
	 * 
	 * @param ref�ֶ�
	 * @param list������List
	 * @param curCol��ǰ��
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
			// �ж��Ƿ��Ƿ�����
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				if (groupAble != null)
					for (int j = 0; j < groupAble.length; j++) {
						refArray = groupAble[j].getFormula()
								.getFunctionRefArray();
						for (int k = 0; k < refArray.length; k++) {
							if (ref.getRefID().equals(refArray[k].getRefID()))
								continue;

							// �ж��ǲ���ö��
							isEnum = DefinePub.judgetEnumWithColID(querySource,
									groupAble[j].getSourceID(), groupAble[j]
											.getSourceColID(), false);
							if (isEnum) {
								// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
								enumID = DefinePub.getEnumRefWithColID(
										querySource, refArray[k].getSourceID(),
										refArray[k].getSourceColID());
								if (!checkeIsSame(enumIDCode, enumID)) {
									continue;
								}
							} else {// ����ö��
								if (!checkeIsSame(ref, refArray[k])) {
									continue;
								}
							}
							// ����У��
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
					// �ж��ǲ���ö��
					isEnum = DefinePub.judgetEnumWithColID(querySource,
							measureAttr[j].getSourceID(), measureAttr[j]
									.getSourceColID(), false);
					if (isEnum) {

						// �ж��ǲ��Ƕ�Ӧͬһö�٣����Ӧͬһö�٣�����У��
						enumID = DefinePub.getEnumRefWithColID(querySource,
								measureAttr[j].getSourceID(), measureAttr[j]
										.getSourceColID());
						if (!checkeIsSame(enumIDCode, enumID)) {
							continue;
						}
					} else {// ����ö��
						if (!checkeIsSame(measureAttr[j], ref)) {
							continue;
						}
					}
					// ����У��
					checkOutSameCol(formula, ref, measureAttr[j].getMeasureID());
				}
			}
		}
	}

	/**
	 * �ж��Ƿ�ͬһö��
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
	 * �Ƚ��Ƿ�ͬһ����Դ��
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
	 * �Ƚ��Ƿ�ͬһ����Դ��
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
	 * �Ƚ��Ƿ�ͬһ����Դ��
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
	 * ��ͬ���ֿ�ʼУ��
	 * 
	 * @param refУ����ֶ�
	 * @param refSame���ҵ����ֶ�
	 */
	private void checkOutSameCol(IFormula formula, FunctionRef ref,
			String sIDSame) {
		String content = formula.getContent().replaceAll(ref.getRefID(),
				sIDSame);
		formula.setContent(content);
		ref.setRefID(sIDSame);
	}

	/**
	 * ��ͬ���ֿ�ʼУ��(�����У�
	 * 
	 * @param refУ����ֶ�
	 * @param refSame���ҵ����ֶ�
	 */
	private void checkOutSameCol(MyCalculateValueImpl calcValue,
			IMeasureAttr mea, String sIDSame) {
		String content = calcValue.getContent().replaceAll(mea.getMeasureID(),
				sIDSame);
		calcValue.setContent(content);
		mea.setMeasureID(sIDSame);
	}

	/**
	 * �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶΣ������У�
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

			// �ж��Ƿ��Ƿ�����
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
	 * �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�(�����У�
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

			// �ж��Ƿ��Ƿ�����
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
