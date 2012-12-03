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
 * The Interface FormulaObject.
 */
public interface FormulaObject
{

	/**
	 * Gets the result field.
	 * 
	 * @return the result field
	 */
	public String getResultField();


	/**
	 * Gets the expression.
	 * 
	 * @return the expression
	 */
	public String getExpression();


	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public int getPriority();


	/**
	 * Gets the decimal scale.
	 * 
	 * @return the decimal scale
	 */
	public int getDecimalScale();

}
