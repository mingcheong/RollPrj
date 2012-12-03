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
 * @author ���ܱ�
 * 
 * @version ����ʱ�䣺2012-3-14 ����03:31:42
 * 
 * @Description ������
 */
public class PrjectManageBO extends AbstractBO
{
	IWorkFlowNew workFlow = null;


	public PrjectManageBO()
	{
		super();
	}


	/**
	 * ����������
	 * 
	 * @param list
	 *            ���������̵�����
	 * @param tablename
	 *            ҵ�����
	 * @param keyfield
	 *            ҵ�������
	 * @return �Ƿ�ִ�гɹ�
	 */
	public void inputFlow(String tablename, String keyfield, String billId, String moduleId) throws Exception
	{
		if (moduleId == null || "".equals(moduleId))
			moduleId = SessionUtil.getUserInfoContext().getModuleID();
		workFlow = (IWorkFlowNew) SessionUtil.getServerBean("sys.workFlowService");
		// ��������׼�����빤����������
		ArrayList listData = new ArrayList();
		// for (int i = 0; i < list.size(); i++) {
		//	
		// XMLData map = (XMLData) list.get(i);
		XMLData rowData = new XMLData();
		rowData.put("tablename", tablename);// ����
		rowData.put("tableid", keyfield);// ���ֶ���
		rowData.put("vou_id", billId);// ���ֶ���ֵ
		rowData.put("moduleid", moduleId);// ����ID

		FVoucherDTO dto = new FVoucherDTO();
		dto.copy(rowData);
		listData.add(dto);
		// }
		workFlow.doBatchAllProcessReturnObj("", "", "INPUT", "", "", "", tablename, tablename, listData, false, false, false);
	}


	/**
	 * �����̲���
	 * 
	 * @param list
	 *            ���������̵�����
	 * @param tablename
	 *            ����
	 * @param keyfield
	 *            ����
	 * @param type
	 *            ����
	 *            TONEXT,����һ��;TOPREVIOUS����һ��;TORECALLNEXT��������һ��;TORECALLPREVIOUS��������һ��
	 * @return �Ƿ��������
	 */
	public void goFlow(List list, String tablename, String keyfield, String billId, String type, String moduleId) throws Exception
	{
		workFlow = (IWorkFlowNew) SessionUtil.getServerBean("sys.workFlowService");
		ArrayList listData = new ArrayList(); // ��������׼�����빤����������
		String roleId = SessionUtil.getUserInfoContext().getRoleID();
		if (moduleId == null || "".equals(moduleId))
			moduleId = SessionUtil.getUserInfoContext().getModuleID();
		try
		{
			// for (int i = 0; i < list.size(); i++) {
			// XMLData map = (XMLData) list.get(i);
			XMLData rowData = new XMLData();
			rowData.put("tablename", tablename);// ����
			rowData.put("tableid", keyfield);// ���ֶ���
			rowData.put("vou_id", billId);// ���ֶ���ֵ
			rowData.put("moduleid", moduleId);// ����ID

			FVoucherDTO dto = new FVoucherDTO();
			dto.copy(rowData);
			listData.add(dto);
			// }
			if (type == "TONEXT" || type.equals("TONEXT"))
			{// ��һ��
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "NEXT", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TOPREVIOUS" || type.equals("TOPREVIOUS"))
			{// �˻�
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "BACK", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TORECALLNEXT" || type.equals("TORECALLNEXT"))
			{// �������
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "RECALL", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TORECALLPREVIOUS" || type.equals("TORECALLPREVIOUS"))
			{// �����˻�
				workFlow.doBatchAllProcessReturnObj(moduleId, roleId, "RECALL", "", "", "", tablename, tablename, listData, false, false, false);
			}
			else if (type == "TODELETE" || type.equals("TODELETE"))
			{// ɾ��
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
	 *            ����
	 * @param key_id
	 *            ����
	 * @param flowstatus
	 *            ״̬���
	 * @return ��������ѯ���
	 */
	// OperateType:001δȷ�ϡ�002��ȷ�ϡ�003���˻ء�004���˻ء�101�ѹ���102��ɾ����103������
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
			throw new AppException("", "���������ݴ���");
		}
	}


	/**
	 * �����ѯ��䡣���û�����ĳһ�����ܻ������������ʱ���������������������ȡ�����SQL��䡣
	 * 
	 * @param TableName
	 *            ------------������
	 * @param TableId
	 *            --------------������ID
	 * @param RoleId
	 *            ---------------��ɫID
	 * @param ModuleId
	 *            -------------����ID
	 * @param OperateType
	 *            ----------��������
	 * @param TableAlias
	 *            -----------����ı���
	 * @return SQL���
	 * @throws Exception
	 *             -----------������Ϣ
	 */
	public String getTasksBySql(String TableName, String TableId, String RoleId, String ModuleId, String OperateType, String TableAlias) throws Exception
	{

		String c = OperateType;
		String b = "";
		String return_sql = "";

		boolean is1stExistClauseFlag = true;// ��ʶ�Ƿ��ǵ�һ��"exist(.....)" ���.
		// �����,��ǰ�治��"or".
		// ѭ�����ö���״̬������
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

			// ���� " and () " ���
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
				throw new Exception("�Ҳ����ýڵ���Ϣ!");

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
		// δȷ��
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
	 * �õ��������������Ľڵ㼰״̬��Ϣ
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
