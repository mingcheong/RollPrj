/**
 * @Copyright 浙江易桥版权所有
 * 
 * @ProjectName 宁海财政扶持项目系统
 * 
 * @aouthor 陈宪标
 * 
 * @version 1.0
 */
package gov.nbcs.rp;

import gov.nbcs.rp.common.SessionUtilEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.foundercy.pf.rule.interfaces.IDataRight;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;
import com.foundercy.pf.util.exception.FAppException;
import com.foundercy.pf.util.sessionmanager.SessionUtil;
import com.foundercy.pf.workflow.dto.FTaskItemDTO;
import com.foundercy.pf.workflow.interfaces.IMessageClient;




/**
 * @author xys
 * 
 * @version 创建时间：2012-3-23 上午09:29:38
 * 
 * @Description 待办事项
 */
public class Rp2BussinessMessage implements IMessageClient
{
	public GeneralDAO dao;



	public GeneralDAO getDao()
	{
		return dao;
	}


	public void setDao(GeneralDAO dao)
	{
		this.dao = dao;
	}


	/**
	 * @Description:获得待办事项
	 * @param userId
	 *            用户编号
	 * @param roleId
	 *            用户角色
	 * @throws Exception
	 */
	public List findTasks(String userId, String roleId) throws Exception
	{
		try
		{
			// 从工作流中取出事务提醒
			return this.getAllTasksStatInfo(userId, roleId);
		}
		catch (FAppException fae)
		{
			throw fae;
		}
		catch (Exception e)
		{
			throw new FAppException("WF-000001", e.getMessage());
		}
	}


	/**
	 * 全局的事务提醒。
	 * 
	 * @param UserId
	 *            -------------用户ID
	 * @param RoleId
	 *            -------------角色ID
	 * @throws Exception
	 *             ---------错误信息
	 */
	public List getAllTasksStatInfo(String UserId, String RoleId) throws FAppException
	{
		String switch_all_task = (String) SessionUtil.getParaMap().get("switch_all_task");
		List list = new ArrayList();
		if ("0".equals(switch_all_task))
		{
			return list;
		}
		else
		{
			return this.getAllModuleTasksStateInfo(UserId, RoleId);
		}
	}


	private List getAllModuleTasksStateInfo(String UserId, String RoleId) throws FAppException
	{
		List return_list = new ArrayList();
		IDataRight dataright = (IDataRight) SessionUtil.getServerBean("sys.dataRightService");
		String condition = " ";
		try
		{
			condition = dataright.getSqlBusiRightByUserNoCCID(UserId, "rp");
		}
		catch (Exception e)
		{
			throw new FAppException("WF-000001", e.getMessage());
		}
		StringBuffer return_sql = new StringBuffer();

		return_sql
				.append("SELECT MENU.MENU_ID,MENU.MENU_NAME,MENU.USER_SYS_ID ,COUNT(MENU.MENU_ID) AS NUM,MD.MODULE_NAME,MD.MODULE_ID ")
				.append(
						" FROM SYS_MENU MENU,SYS_ROLE_MENU RM,SYS_MENU_MODULE MM,SYS_WF_MODULE_NODE SWMN,SYS_WF_NODES N ,SYS_WF_CURRENT_TASKS T,SYS_MODULE MD, SYS_USER_ROLE_RULE   USER_ROLE,SYS_WF_ROLE_NODE RR")
				.append(" WHERE RM.MENU_ID = MENU.MENU_ID AND MENU.MENU_ID = MM.MENU_ID ").append(" AND MM.MODULE_ID = SWMN.MODULE_ID AND SWMN.NODE_ID = N.NODE_ID AND N.WF_ID = T.WF_ID ").append(
						" AND MM.MODULE_ID = MD.MODULE_ID ").append(" AND T.NEXT_NODE_ID = N.NODE_ID ").append(
						" AND (EXISTS(SELECT 1 FROM RP_SB_BILL RP WHERE RP.BILL_ID = T.ENTITY_ID AND SET_YEAR= " + SessionUtil.getLoginYear()).append(condition).append(")").append(
						" OR EXISTS(SELECT 1 FROM RP_TB_BILL RP WHERE RP.BILL_ID = T.ENTITY_ID AND SET_YEAR= " + SessionUtil.getLoginYear()).append(condition).append(")) ").append(
						" AND RM.ROLE_ID = ? AND USER_ROLE.ROLE_ID=RM.ROLE_ID AND RR.NODE_ID=N.NODE_ID  AND RR.ROLE_ID=RM.ROLE_ID AND USER_ROLE.USER_ID= ? AND RM.SET_YEAR= "
								+ SessionUtil.getLoginYear()).append(" GROUP BY MENU.MENU_ID,MENU.MENU_NAME,MENU.USER_SYS_ID,MD.MODULE_NAME,MD.MODULE_ID ");
		List result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, UserId });

		for (int i = 0, size = result.size(); i < size; i++)
		{

			Map data = (Map) result.get(i);
			FTaskItemDTO task = new FTaskItemDTO();
			task.setRole_id(RoleId);
			task.setMenu_id(data.get("menu_id").toString());
			task.setMenu_name(data.get("menu_name").toString());
			task.setSysapp(data.get("user_sys_id").toString());
			task.setModule_id(data.get("module_id").toString());
			task.setMsg_type_code("1");
			task.setMsg_type_name("日常事务");
			task.setMsg_type_name_local(data.get("module_name").toString());
			task.setTask_content("：" + data.get("num").toString() + "条 ");
			return_list.add(task);
		}

		if (SessionUtilEx.isFisVis())
		{
			List result1 = dao.findBySql("select a.chr_code,count(*) as num from RP_FJ_FILES a where a.dw_sure = 1 and a.cz_sure is null and a.set_year = ? group by a.chr_code order by a.chr_code", new Object[] { SessionUtil.getLoginYear() });
			if (result1 != null && !result1.isEmpty())
			{
				for (int i = 0; i < result1.size(); i++)
				{
					Map data = (Map) result1.get(i);
					FTaskItemDTO task = new FTaskItemDTO();
					task.setRole_id(RoleId);
					task.setMenu_id("900006001");
					task.setMenu_name(data.get("chr_code").toString());
					task.setSysapp("801");
					task.setModule_id("80100110"+i);
					task.setMsg_type_code("1");
					task.setMsg_type_name("预算表平台");
					task.setMsg_type_name_local(data.get("chr_code").toString());
					task.setTask_content("：" + data.get("num").toString() + "条 ");
					return_list.add(task);
				}
			}
		}
		return return_list;
	}
}
