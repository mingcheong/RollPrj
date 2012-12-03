/*
 * @(#)IURDao.java	Mar 12, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
*/
package gov.nbcs.rp.sys.usertoreport.action;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

/**
 * IURDao.java
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
\
 * @version 1.0
 */
public interface IURDao {
	/**
	 * �������
	 * ���û����û�ѡ�еı����浽���ݿ���
	 * @throws Exception
	 */
	public void doSave() throws Exception;
	/**
	 * ���������û�����
	 * @return
	 * ����DataSet
	 * @throws Exception
	 */
	public DataSet loadAllUser() throws Exception;
	
	public List loadUserInfo() throws Exception;
	
	/**
	 * 
	 * @param userList
	 */
	public void setUserList(List userList);
	/**
	 * 
	 * @param selectNodes
	 */
	public void setSelectNodes(MyTreeNode[] selectNodes);
}
