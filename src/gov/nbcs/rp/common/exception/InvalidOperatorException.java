/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.exception;

public class InvalidOperatorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid operator exception.
	 * 
	 * @param opId
	 *            the op id
	 */
	public InvalidOperatorException(String opId) {
		super("��Ч�Ĳ����� -- " + opId);
	}

}
