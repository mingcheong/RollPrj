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
	 * 保存操作
	 * 将用户和用户选中的报表保存到数据库中
	 * @throws Exception
	 */
	public void doSave() throws Exception;
	/**
	 * 加载所有用户数据
	 * @return
	 * 返回DataSet
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
