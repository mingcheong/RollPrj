/*
 * @(#)VariableConstraints.java	Feb 28, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
*/
package gov.nbcs.rp.standardkind.ui.common;
/**
 * VariableConstraints.java
 * <p>
 * Title: ����Լ��
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData: Feb 28, 2008
 * </p>
 * 
 * @author GeXinying
 * @version 1.0
 */
public interface VarCons {
	//���
	public final static int STATE_BRO = 0;
	//����
	public final static int STATE_ADD = 1;
	//����
	public final static int STATE_SAVE = 2;
	//�༭
	public final static int STATE_EDIT = 3;
	//ɾ��
	public final static int STATE_DELE = 4;
	//ȡ��
	public final static int STATE_CANL = 5;
	//ѡ��
	public final static int STATE_SELECT = 6;
	//ѡ�и�
	public final static int STATE_SELECT_PARENT = 7;
	
	//���õĲ����޸ĺ�ɾ��
	public final static int BUILD_IN_STATE = 1;
	
}
