/**
 * Copyright 江苏方正春元 版权所有
 * 
 * 部门预算子系统
 * 
 * @author 汪佩雄
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.formula;

/**
 * The Class FormulaObject. FormulaObject的默认实现
 */
public class FormulaObjectImpl implements FormulaObject, Comparable {
	/**
	 * 结果字段
	 */
	private String resultField;

	/**
	 * 表达式
	 */
	private String expression;

	/**
	 * 表达式计算优先级
	 */
	private int priority;
	
	/** 计算结果的小数位数. */
	private int decimalScale = -9;

	/**
	 * Instantiates a new formula object.
	 * 
	 * @param resultField
	 *            the result field 结果字段
	 * @param expression
	 *            the expression 表达式
	 * @param priority
	 *            the priority 表达式计算优先级
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
