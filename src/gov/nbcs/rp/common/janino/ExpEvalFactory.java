/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.janino;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.janino.ExpressionEvaluator;

/**
 * A factory for creating ExpressionEvaluator objects.
 */
public class ExpEvalFactory {

	/** The exp eval factory. */
	private static ExpEvalFactory expEvalFactory = new ExpEvalFactory();

	/**
	 * Gets the single instance of ExpEvalFactory.
	 *
	 *	��ȫ�ֵĹ�ʽ���ڻ�ȡһ��ʵ��
	 *
	 * @param expression
	 *            the expression
	 * @param expressionType
	 *            the expression type
	 * @param parameterNames
	 *            the parameter names
	 * @param parameterTypes
	 *            the parameter types
	 *
	 * @return single instance of ExpEvalFactory
	 */
	public static ExpressionEvaluator getInstance(String expression,
			Class expressionType, String[] parameterNames,
			Class[] parameterTypes) {
		return expEvalFactory.oneInstance(expression, expressionType,
				parameterNames, parameterTypes);
	}

	/** The self exp eval pool. ʵ���Ĺ�ʽ�� */
	private Map selfExpEvalPool = new HashMap();

	/**
	 * Instantiates a new exp eval factory.
	 */
	public ExpEvalFactory() {
	}

	/**
	 * One instance.
	 *
	 * @param expression
	 *            the expression
	 * @param expressionType
	 *            the expression type
	 * @param parameterNames
	 *            the parameter names
	 * @param parameterTypes
	 *            the parameter types
	 *
	 * @return the expression evaluator
	 */
	public synchronized ExpressionEvaluator oneInstance(String expression,
			Class expressionType, String[] parameterNames,
			Class[] parameterTypes) {
		StringBuffer sbKey = new StringBuffer().append(expression).append("#")
				.append(expressionType.getName());
		// for (int i = 0; i < parameterNames.length; i++) {
		// sbKey.append("#N").append(parameterNames[i]);
		// }
		// for (int i = 0; i < parameterTypes.length; i++) {
		// if(parameterTypes[i] != null)
		// sbKey.append("#T").append(parameterTypes[i].getName());
		// }
		String key = sbKey.toString();

		if (selfExpEvalPool.containsKey(key)) {
			return (ExpressionEvaluator) selfExpEvalPool.get(key);
		} else {
			try {
				ExpressionEvaluator expressionevaluator = new ExpressionEvaluator(
						expression, expressionType, parameterNames,
						parameterTypes);
				// qinj ������������ʱ���
				if (selfExpEvalPool.size() > 8192) {
					selfExpEvalPool.clear();
				}
				selfExpEvalPool.put(key, expressionevaluator);
				return expressionevaluator;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
