/*
 * Created on 2005-12-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nbcs.rp.common;

import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.Record;
import gov.nbcs.rp.common.janino.JavaEvaluator;
import gov.nbcs.rp.common.js.JSEvaluator;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FormulaTool {
	public static boolean isNaN(Object val) {
		return "NaN".equalsIgnoreCase(Common.nonNullStr(val));
	}

	public static boolean isInfinity(Object val) {
		return "Infinity".equalsIgnoreCase(Common.nonNullStr(val));
	}

	// Ϊ�˱��ڲ�������2��hash������¼��������Ϣ
	private static Hashtable op_class = new Hashtable();// ��������Ϣ(�������ţ�������)
	static {
		op_class.put("+", "");
		op_class.put("-", "");
		op_class.put("*", "");
		op_class.put("/", "");
	}

	/**
	 *	����JavaScript�﷨���㹫ʽ
	 *
	 * @param formula
	 * @param scale
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValueFromRecord(String formula, int scale, Record r)
			throws Exception {
		return getValue(formula, r).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * ����JavaScript�﷨���㹫ʽ
	 *
	 * @param formula
	 * @param r
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValueFromRecord(String formula, Record r) throws Exception {
		formula = formula.replaceAll("\\s+", "");
		if (Common.isNullStr(formula)) {
			return new BigDecimal("0");
		} else if (Common.isNumber(formula)) {
			return new BigDecimal(formula);
		} else {
			// formula = formula.replaceAll("\\s+", "");
			if ((r != null) && !r.isEmpty()) {
				formula = toNumericFormula(formula, r);
			}

			// StringExpressionToDoubleValue dd = new
			// StringExpressionToDoubleValue(formula);
			// String evelResult = String.valueOf(dd.getExpressionResult());
			String evelResult = JSEvaluator.evaluateNumber(formula, r).toString();
			if (isInfinity(evelResult) || isNaN(evelResult)) {
				evelResult = "0";
			}
			BigDecimal result = new BigDecimal(evelResult);
			return result;
		}
	}

	/**
	 * ����JavaScript�﷨���㹫ʽ
	 *
	 * getValue�����Ǹ��ݸ�����ʽ�Ͷ�Ӧֵ���㹫ʽ��ֵ���������ֵ
	 */
	public static BigDecimal getValue(String formula, int scale, Map m)
			throws Exception {
		return getValue(formula, m).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * ����JavaScript�﷨���㹫ʽ
	 *
	 * @param formula
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValue(String formula, Map m) throws Exception {
		formula = formula.replaceAll("\\s+", "");
		if (Common.isNullStr(formula)) {
			return new BigDecimal("0");
		} else if (Common.isNumber(formula)) {
			return new BigDecimal(formula);
		} else {
			// formula = formula.replaceAll("\\s+", "");
			if ((m != null) && !m.isEmpty()) {
				formula = toNumericFormula(formula, m);
			}

			// StringExpressionToDoubleValue dd = new
			// StringExpressionToDoubleValue(formula);
			// String evelResult = String.valueOf(dd.getExpressionResult());
			String evelResult = JSEvaluator.evaluate(formula).toString();
			if (isInfinity(evelResult) || isNaN(evelResult)) {
				evelResult = "0";
			}
			BigDecimal result = new BigDecimal(evelResult);
			return result;
		}
	}

	/**
	 * ����Java�﷨���㹫ʽ
	 *
	 * @param formula
	 * @param scale
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValue2(String formula, int scale, Map m)
			throws Exception {
		return getValue2(formula, m).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * ����Java�﷨���㹫ʽ
	 *
	 * @param formula
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getValue2(String formula, Map m) throws Exception {
		formula = formula.replaceAll("\\s+", "");
		if (Common.isNullStr(formula)) {
			return new BigDecimal("0");
		} else if (Common.isNumber(formula)) {
			return new BigDecimal(formula);
		} else {
			// formula = formula.replaceAll("\\s+", "");
			if ((m != null) && !m.isEmpty()) {
				formula = toNumericFormula(formula, m);
			}
			// StringExpressionToDoubleValue dd = new
			// StringExpressionToDoubleValue(formula);
			// String evelResult = String.valueOf(dd.getExpressionResult());
			formula = formula.replaceAll(UntPub.GS_FORMULALEFT_RIGHT, "");
			BigDecimal result = (BigDecimal) JavaEvaluator.evaluateNumber(
					formula, m, BigDecimal.class);
			// if (isInfinity(evelResult) || isNaN(evelResult)) {
			// evelResult = "0";
			// }
			// BigDecimal result = new BigDecimal(evelResult);
			return result;
		}
	}
	
	/**
	 * To numeric formula. ��Ԫ�ع�ʽ����ȥ���ո�ת�������ֹ�ʽ���ղ�����0����������
	 * 
	 * @param formula
	 *            the formula
	 * @param record
	 *            the record
	 * @return the string
	 */
	private static String toNumericFormula(String formula, Record record) {
		String[] formulaValues = formula.split("\\+|-|\\*|/|\\(|\\)");
		for (int i = 0; i < formulaValues.length; i++) {
			if (!Common.isNullStr(formulaValues[i])) {
				if (!Common.isNumber(formulaValues[i])) {
					String key = formulaValues[i].replaceAll(
							UntPub.GS_FORMULALEFT_RIGHT, "");
					if(record.containsKey(key)) {
						Field field = (Field) record.get(key);
						Object val = null;
//						Class valClass = null;
						if(field != null) {
							val = field.getValue();
//							valClass = field.getValueType();
						}

						if ((val != null)
//								&& valClass != null
//								&& valClass.isAssignableFrom(Number.class)
								&& Common.isNumber(val.toString())) {
						} else {
							formula = replaceAllElement(formula,
									formulaValues[i], "0");
						}

					} else {
						formula = replaceAllElement(formula, formulaValues[i], "0");
					}
				}
			}
		}
		return formula.replaceAll(UntPub.GS_FORMULALEFT_RIGHT, "");
	}

	/**
	 * To numeric formula. ��Ԫ�ع�ʽת�������ֹ�ʽ
	 * 
	 * @param formula
	 *            the formula
	 * @param values
	 *            the values
	 * @return the string
	 */
	private static String toNumericFormula(String formula, Map values) {
		String[] formulaValues = formula.split("\\+|-|\\*|/|\\(|\\)");
		for (int i = 0; i < formulaValues.length; i++) {
			if (!Common.isNullStr(formulaValues[i])) {
				if (!Common.isNumber(formulaValues[i])) {
					Object val = values.get(formulaValues[i]);
					if (val == null) {
						val = values.get(formulaValues[i].replaceAll(
								UntPub.GS_FORMULALEFT_RIGHT, ""));
					}
					if (Common.nonNullStr(val).startsWith("-")) {
						val = "(" + val + ')';
					}
					if (Common.isNullStr(Common.nonNullStr(val))) {
						val = "0";
					}

					formula = replaceAllElement(formula, formulaValues[i], val
							.toString());
				}
			}
		}
		return formula;
	}
	
	/**
	 * �ú�������������⣺ ����String s="G1+G117"�����s=s.replaceAll("G1","A") ��ʱs=="A+A17"
	 */
	public static String replaceAllElement(String src, String regex, String rep) {
		int index = -1;
		while ((index = src.indexOf(regex, index)) >= 0) {
			String sub0 = "";
			if (index > 0) {
				sub0 = src.substring(0, index);
			}
			String sub1 = "";
			if (index + regex.length() < src.length()) {
				sub1 = src.substring(index + regex.length(), src.length());
			}
			if ((index < src.length() - 1) && (sub1.length() > 0)) {
				char c = sub1.charAt(0);
				if ((c != '(') && (c != ')')
						&& !op_class.containsKey(String.valueOf(c))) {
					index += regex.length();
					continue;
				}
			}
			index += rep.length();
			src = sub0 + rep + sub1;
		}
		return src;
	}

	public static void main(String args[]) throws Exception {
		Map map = new MyMap();
		map.put("A", new Integer(2));
		map.put("B", new Float(11f));
		map.put("C", new Integer(-11));
		BigDecimal value = FormulaTool.getValue("(A+B)/C", 2, map);
		System.out.println(value);
	}
}
