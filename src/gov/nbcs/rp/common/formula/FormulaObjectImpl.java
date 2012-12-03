/**
 * Copyright ���շ�����Ԫ ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @author ������
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.formula;

/**
 * The Class FormulaObject. FormulaObject��Ĭ��ʵ��
 */
public class FormulaObjectImpl implements FormulaObject, Comparable {
	/**
	 * ����ֶ�
	 */
	private String resultField;

	/**
	 * ���ʽ
	 */
	private String expression;

	/**
	 * ���ʽ�������ȼ�
	 */
	private int priority;
	
	/** ��������С��λ��. */
	private int decimalScale = -9;

	/**
	 * Instantiates a new formula object.
	 * 
	 * @param resultField
	 *            the result field ����ֶ�
	 * @param expression
	 *            the expression ���ʽ
	 * @param priority
	 *            the priority ���ʽ�������ȼ�
	 */
	public FormulaObjectImpl(String resultField, String expression, int priority) {
		super();
		this.resultField = resultField;
		this.expression = expression;
		this.priority = priority;
	}
	
	public FormulaObjectImpl(String resultField, String expression, int priority, int scale) {
		super();
		this.resultField = resultField;
		this.expression = expression;
		this.priority = priority;
		this.decimalScale = scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.fiscalbudget.common.formula.IFormulaObject#getResultField()
	 */
	public String getResultField() {
		return resultField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.fiscalbudget.common.formula.IFormulaObject#getExpression()
	 */
	public String getExpression() {
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.fiscalbudget.common.formula.IFormulaObject#getPriority()
	 */
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see com.foundercy.fiscalbudget.common.formula.FormulaObject#getDecimalScale()
	 */
	public int getDecimalScale() {
		return decimalScale;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object o) {
		return new Integer(priority).compareTo(new Integer(((FormulaObject) o)
				.getPriority()));
	}
}
