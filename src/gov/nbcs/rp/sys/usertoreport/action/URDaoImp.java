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
	//����ӿ�
	private IUserToReport service;
	//
	private MyTreeNode[] selectNodes;
	//
	private List userList;
	
	
	public URDaoImp(IUserToReport iU2Report) {
		this.service = iU2Report;
	}
	public void doSave() throws Exception {
		//����ID
		String reportId = "";
		String reportType="";
		// ���µ�sql���
		List sqlInsert = new ArrayList();
		StringBuffer sql ;//= new StringBuffer();
		/**
		 * �����û��ͱ����Ӧ�Ĺ�ϵ
		 */
		Iterator iter = userList.iterator();
//		String[] key = { "USER_ID", "REPORT_ID", "SET_YEAR" };
//		curUserReportDS.setPrimarykey(key);
		String user_id = "";
		while (iter.hasNext()) {			
			user_id = (String)iter.next();
			//����ѡ�еĽ��
			for (int i = 0; i < selectNodes.length; i++) {
				if (!selectNodes[i].isLeaf())
				{
					continue;
				}
				sql = new StringBuffer();
				/*
				 * ��ǰ�ĵõ�report_ID�ķ���������(��λ����),��locate(,) ����������ļ�.�������ظ�
				 * reportId��ֵ��һ�����ֵ��reportType+reportId
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
	 * ���������û�
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
