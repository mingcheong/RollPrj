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

public class InvalidParametersException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public InvalidParametersException(String opId) {
		super("��Ч�Ĳ����� -- " + opId);
	}

}
