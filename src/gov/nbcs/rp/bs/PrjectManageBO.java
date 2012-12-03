package gov.nbcs.rp.bs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.foundercy.pf.rule.FVoucherDTO;
import com.foundercy.pf.rule.interfaces.IDataRight;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.exception.AppException;
import com.foundercy.pf.util.sessionmanager.SessionUtil;
import com.foundercy.pf.workflow.interfaces.IWorkFlowNew;




/**
 * @author 陈宪标
 * 
 * @version 创建时间：2012-3-14 下午03:31:42
 * 
 * @Description 工作流
 */
public class PrjectManageBO extends AbstractBO
{
	IWorkFlowNew workFlow = null;


	public PrjectManageBO()
	{
		super();
	}


	/**
	 * 启动工作流
	 * 
	 * @param list
	 *            待进入流程的数据
	 * @param tablename
	 *            业务表名
	 * @param keyfield
	 *            业务表主键
	 * @return 是否执行成功
	 */
	public void inputFlow(String tablename, String keyfield, String billId, String moduleId) throws Exception
	{
		if (moduleId == null || "".equals(moduleId))
			moduleId = SessionUtil.getUserInfoContext().getModuleID();
		workFlow = (IWorkFlowNew) SessionUtil.getServerBean("sys.workFlowService");
		// 用来保存准备进入工作流的数据
		ArrayList listData = new ArrayList();
		// for (int i = 0; i < list.size(); i++) {
		//	
		// XMLData map = (XMLData) list.get(i);
		XMLData rowData = new XMLData();
		rowData.put("tablename", tablename);// 表名
		rowData.put("tableid", keyfield);// 表字段名
		rowData.put("vou_id", billId);// 表字段名值
		rowData.put("moduleid", moduleId);// 功能ID

		FVoucherDTO dto = new FVoucherDTO();
		dto.copy(rowData);
		listData.add(dto);
		// }
		workFlow.doBatchAllProcessReturnObj("", "", "INPUT", "", "", "", tablename, tablename, listData, false, false, false);
	}


	/**
	 * 走流程操作
	 * 
	 * @param list
	 *            进入入流程的数据
	 * @param tablename
	 *            表名
	 * @param keyfield
	 *            主键
	 * @param type
	 *            类型
	 *            TONEXT,到下一步;TOPREVIOUS到上一步;TORECALLNEXT撤消到下一步;TORECALLPREVIOUS撤消到上一步
	 * @return 是否进入流程
	 */
	public void goFlow(List list, String tablename, String keyfield, String billId, String type, String moduleId) throws Exception
	{
		workFlow = (IWorkFlowNew) SessionUtil.getServerBean("sys.workFlowService");
		ArrayList listData = new ArrayList(); // 用来保存准备进入工作流的数据
		String roleId = SessionUtil.getUserInfoContext().getRoleID();
		if (moduleId == null || "".equals(moduleId))
			moduleId = SessionUtil.getUserInfoContext().getModuleID();
		try
		{
			// for (int i = 0; i < list.size(); i++) {
			// XMLData map = (XMLData) list.get(i);
			XMLData rowData = new XMLData();
			rowData.put("tablename", tablename);// 表名
			rowData.put("tableid", keyfield);// 表字段名
			rowData.put("vou_id", billId);// 表字段名值
			rowData.put("moduleid", moduleId);// 功能ID

			FVoucherDTO dto = new FVoucherDTO();
			dto.copy(rowData);
			listData.add(dto);
			// }
			if (type == "TONEXT" || type.equals("TONEXT"))
			{// 下一步
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "NEXT", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TOPREVIOUS" || type.equals("TOPREVIOUS"))
			{// 退回
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "BACK", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TORECALLNEXT" || type.equals("TORECALLNEXT"))
			{// 撤销审核
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "RECALL", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TORECALLPREVIOUS" || type.equals("TORECALLPREVIOUS"))
			{// 撤销退回
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "RECALL", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TODELETE" || type.equals("TODELETE"))
			{// 删除
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "DELETE", "", "", "", tablename, tablename, listData, false, false, false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}


	/**
	 * @param tablename
	 *            表名
	 * @param key_id
	 *            主键
	 * @param flowstatus
	 *            状态编号
	 * @return 工作流查询语句
	 */
	// OperateType:001未确认、002已确认、003已退回、004被退回、101已挂起、102已删除、103已作废
	public String getFlowCondition(String tablename, String key_id, String flowstatus, String moduleId) throws Exception
	{
		IDataRight dataright = (IDataRight) SessionUtil.getServerBean("sys.dataRightService");
		workFlow = (IWorkFlowNew) SessionUtil.getServerBean("sys.workFlowService");
		String flowcondition = "";
		try
		{
			String userId = SessionUtil.getUserInfoContext().getUserID();
			String roleId = SessionUtil.getUserInfoContext().getRoleID();
			if (moduleId == null || "".equals(moduleId))
				moduleId = SessionUtil.getUserInfoContext().getModuleID();
			flowcondition = dataright.getSqlBusiRightByUserNoCCID(userId, tablename) + getTasksBySql(tablename, key_id, roleId, moduleId, flowstatus, tablename);
			return flowcondition;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new AppException("", "工作流数据错误");
		}
	}


	/**
	 * 任务查询语句。当用户启动某一个功能或进行事务提醒时，工作流组件返回用于提取任务的SQL语句。
	 * 
	 * @param TableName
	 *            ------------表名称
	 * @param TableId
	 *            --------------表主键ID
	 * @param RoleId
	 *            ---------------角色ID
	 * @param ModuleId
	 *            -------------功能ID
	 * @param OperateType
	 *            ----------操作类型
	 * @param TableAlias
	 *            -----------主表的别名
	 * @return SQL语句
	 * @throws Exception
	 *             -----------错误信息
	 */
	public String getTasksBySql(String TableName, String TableId, String RoleId, String ModuleId, String OperateType, String TableAlias) throws Exception
	{

		String c = OperateType;
		String b = "";
		String return_sql = "";

		boolean is1stExistClauseFlag = true;// 标识是否是第一个"exist(.....)" 语句.
		// 如果是,则前面不加"or".
		// 循环调用多种状态的数据
		try
		{
			while (c.length() > 0)
			{
				if (c.indexOf("|") >= 0)
				{
					b = c.substring(0, c.indexOf("|"));
					c = c.substring(c.indexOf("|") + 1, c.length());
				}
				else
				{
					b = c;
					if (b.trim().length() > 0)
					{
						return_sql = return_sql + getTasksBySqlBySingleType(TableName, TableId, RoleId, ModuleId, b.trim(), TableAlias, is1stExistClauseFlag);
					}
					break;
				}
				if (b.trim().length() > 0)
				{
					return_sql = return_sql + getTasksBySqlBySingleType(TableName, TableId, RoleId, ModuleId, b.trim(), TableAlias, is1stExistClauseFlag);
				}
				is1stExistClauseFlag = false;
			}

			// 屏蔽 " and () " 情况
			if (return_sql.trim().equals("")) { return " and 1 = 0 "; }

			return " and (" + return_sql + ")";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}


	private String getTasksBySqlBySingleType(String TableName, String TableId, String RoleId, String ModuleId, String OperateType, String TableAlias, boolean is1stExistClauseFlag) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		String wf_id = "";
		String node_id = "0";
		String gather_flag = "";
		List result = null;
		try
		{
			sql.append("SELECT D.WF_ID,D.NODE_ID,D.GATHER_FLAG,D.WF_TABLE_NAME,E.ID_COLUMN_NAME ").append("FROM SYS_WF_NODES").append(Tools.addDbLink()).append(" D,SYS_WF_MODULE_NODE").append(
					Tools.addDbLink()).append(" B,SYS_WF_ROLE_NODE").append(Tools.addDbLink()).append(" C,SYS_TABLEMANAGER").append(Tools.addDbLink()).append(" E").append(
					" WHERE  D.WF_TABLE_NAME=E.TABLE_CODE AND D.NODE_ID=B.NODE_ID AND D.NODE_ID=C.NODE_ID AND B.MODULE_ID=? ").append(" AND C.ROLE_ID=? ");
			result = dao.findBySql(sql.toString(), new Object[] { ModuleId, RoleId });

			if (result.size() == 0)
				throw new Exception("找不到该节点信息!");

			String return_sql = "";
			String return_return_sql = "";
			for (int i = 0; i < result.size(); i++)
			{
				if (TableName == null || TableName.equals(""))
				{
					TableName = ((Map) result.get(i)).get("wf_table_name").toString();
				}
				Map rs = (Map) result.get(i);
				wf_id = (String) rs.get("wf_id");
				node_id = (String) rs.get("node_id");
				gather_flag = (String) rs.get("gather_flag");
				return_sql = (i == 0 && is1stExistClauseFlag ? " " : " or ") + getTasksBySqlByNode(wf_id, node_id, TableName, TableId, OperateType, TableAlias, gather_flag);
				return_return_sql = return_return_sql + return_sql;
			}
			return return_return_sql;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}


	private String getTasksBySqlByNode(String wf_id, String node_id, String TableName, String TableId, String OperateType, String TableAlias, String gather_flag) throws Exception
	{
		StringBuffer return_sql = new StringBuffer();
		// 未确认
		try
		{
			if (OperateType.equals("001") || OperateType.equals("004"))
			{
				return_sql.append("  EXISTS (SELECT 1 ").append(" FROM SYS_WF_CURRENT_ITEM").append(Tools.addDbLink()).append(" b").append(
						" WHERE B.STATUS_CODE= '" + OperateType + "' AND B.ENTITY_ID=" + TableAlias + "." + TableId).append("  AND B.NODE_ID = '" + node_id + "')");
			}
			else
			{
				return_sql.append("  EXISTS (SELECT 1 ").append(" FROM SYS_WF_COMPLETE_ITEM ").append(Tools.addDbLink()).append(" b").append(
						" WHERE B.STATUS_CODE= '" + OperateType + "' AND B.ENTITY_ID=" + TableAlias + "." + TableId).append("  AND B.NODE_ID = '" + node_id + "')");
			}
			return return_sql.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}


	/*
	 * 得到传入数据所处的节点及状态信息
	 */
	public List getTasksNode(String bill_id, String tableName)
	{
		List TaskID = null;
		try
		{
//			TaskID = workFlow.getCurrNodeInfoDTO(tableName, bill_id);
			String sql="select c.node_name from sys_wf_current_item a, rp_tb_bill b,sys_wf_nodes c " +
					"where a.entity_id=b.bill_id  and a.node_id=c.node_id and entity_id='"+bill_id+"'";
			TaskID=dao.findBySql(sql);
			if(TaskID.size()==0)
				return null;
			}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return TaskID;
	}
}
