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

/**
 * The Class FailedOperatorException. ������ִ��ʧ���쳣��
 */
public class FailedOperatorException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5065087344301900099L;

	/**
	 * Invalid operator exception.
	 * 
	 * @param opId
	 *            the op id
	 */
	public FailedOperatorException(String opId) {
		super("������ִ��ʧ�� -- " + opId);
	}

}
