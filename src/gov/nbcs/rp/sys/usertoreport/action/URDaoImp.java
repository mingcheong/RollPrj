/*
 * @(#)URDaoImp.java	Mar 12, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
*/
package gov.nbcs.rp.sys.usertoreport.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.util.Global;

/**
 * URDaoImp.java
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 * @version 1.0
 */
public class URDaoImp implements IURDao {
	//服务接口
	private IUserToReport service;
	//
	private MyTreeNode[] selectNodes;
	//
	private List userList;
	
	
	public URDaoImp(IUserToReport iU2Report) {
		this.service = iU2Report;
	}
	public void doSave() throws Exception {
		//报表ID
		String reportId = "";
		String reportType="";
		// 更新的sql语句
		List sqlInsert = new ArrayList();
		StringBuffer sql ;//= new StringBuffer();
		/**
		 * 保存用户和报表对应的关系
		 */
		Iterator iter = userList.iterator();
//		String[] key = { "USER_ID", "REPORT_ID", "SET_YEAR" };
//		curUserReportDS.setPrimarykey(key);
		String user_id = "";
		while (iter.hasNext()) {			
			user_id = (String)iter.next();
			//遍历选中的结点
			for (int i = 0; i < selectNodes.length; i++) {
				if (!selectNodes[i].isLeaf())
				{
					continue;
				}
				sql = new StringBuffer();
				/*
				 * 以前的得到report_ID的方法有问题(定位问题),用locate(,) 现在用排序的键.不会有重复
				 * reportId的值是一个组合值，reportType+reportId
				 */
				reportId = ((PfTreeNode)selectNodes[i].getUserObject()).getValue();
				reportType = ((PfTreeNode)((MyTreeNode)selectNodes[i].getParent()).getUserObject()).getValue();
                if (Common.isNullStr(reportType))
                    continue;
				int index = reportId.indexOf(reportType); 
				if(index > -1) {
					reportId = reportId.substring(reportType.length(),reportId.length());
				}
				sql.append("insert into fb_u_usertoreport (LAST_VER,RG_CODE,USER_ID,REPORT_ID,SET_YEAR,REPORT_TYPE)")
				   .append("values('','"+Global.getCurrRegion()+"','").append(user_id)
				   .append("','").append(reportId).append("','").append(Global.loginYear).append("','")
				   .append(reportType).append("')");
				sqlInsert.add(sql.toString());
			}
		}
		service.updataUser2Report(userList,sqlInsert);
	}
	/**
	 * 加载所有用户
	 */
	public DataSet loadAllUser() throws Exception {
		// TODO Auto-generated method stub
		return service.getAllUser();
	}

	public List loadUserInfo() throws Exception {
		// TODO Auto-generated method stub
		return service.loadAllUser();
	}
	public MyTreeNode[] getSelectNodes() {
		return selectNodes;
	}
	public void setSelectNodes(MyTreeNode[] selectNodes) {
		this.selectNodes = selectNodes;
	}
	
	public List getUserList() {
		return userList;
	}
	public void setUserList(List userList) {
		this.userList = userList;
	}
}
