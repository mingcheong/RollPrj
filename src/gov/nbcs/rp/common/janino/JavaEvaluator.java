/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.janino;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.Record;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.janino.ExpressionEvaluator;


public class JavaEvaluator {

	/**
	 * Evaluate.
	 *
	 * @param formula
	 *            the formula
	 * @param record
	 *            the record
	 * @param returnType
	 *            the return type
	 *
	 * @return the object
	 *
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
	public static Object evaluate(String formula, Record record,
			Class returnType) throws InvocationTargetException {
		String[] parameterNames = new String[record.size() - 1];
		Class[] parameterTypes = new Class[record.size() - 1];
		Object[] parameterValues = new Object[record.size() - 1];
//		String[] parameterNames = new String[record.size()];
//		Class[] parameterTypes = new Class[record.size()];
//		Object[] parameterValues = new Object[record.size()];
		int idx = 0;
		for (Iterator iter = record.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (!DataSet.RECORD_STATE_ID.equals(key)) {
				Field f = (Field) record.get(key);
				parameterNames[idx] = f.getName();
				parameterTypes[idx] = f.getValueType();
				parameterValues[idx] = f.getValue();
				// System.out.println(idx + "-" + parameterNames[idx] + "-" +
				// parameterTypes[idx] + "-" + parameterValues[idx]);
				idx++;
			}
		}
		ExpressionEvaluator ee = ExpEvalFactory.getInstance(formula,
				returnType, parameterNames, parameterTypes);

		return ee.evaluate(parameterValues);
	}

	/**
	 * Evaluate.
	 *
	 * @param formula
	 *            the formula
	 * @param values
	 *            the values
	 * @param returnType
	 *            the return type
	 *
	 * @return the object
	 *
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
	public static Object evaluate(String formula, Map values, Class returnType)
			throws InvocationTargetException {
		String[] parameterNames = new String[values.size()];
		Class[] parameterTypes = new Class[values.size()];
		Object[] parameterValues = new Object[values.size()];
		int idx = 0;
		for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object f = values.get(key);
			parameterNames[idx] = key;
			parameterTypes[idx] = f.getClass();
			parameterValues[idx] = f;
			// System.out.println(idx + "-" + parameterNames[idx] + "-" +
			// parameterTypes[idx] + "-" + parameterValues[idx]);
			idx++;
		}
		ExpressionEvaluator ee = ExpEvalFactory.getInstance(formula,
				returnType, parameterNames, parameterTypes);

		return ee.evaluate(parameterValues);
	}

	public static Object evaluateNumber(String formula, Map values, Class returnType)
			throws InvocationTargetException {
		String[] parameterNames = new String[values.size()];
		Class[] parameterTypes = new Class[values.size()];
		Object[] parameterValues = new Object[values.size()];
		int idx = 0;
		for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Object f = values.get(key);
			parameterNames[idx] = key;
			parameterTypes[idx] = f == null ? Integer.class : f.getClass();
			parameterValues[idx] = f == null ? new Integer(0) : f;
			// System.out.println(idx + "-" + parameterNames[idx] + "-" +
			// parameterTypes[idx] + "-" + parameterValues[idx]);
			idx++;
		}
		ExpressionEvaluator ee = ExpEvalFactory.getInstance(formula,
				returnType, parameterNames, parameterTypes);

		return ee.evaluate(parameterValues);
	}

}
