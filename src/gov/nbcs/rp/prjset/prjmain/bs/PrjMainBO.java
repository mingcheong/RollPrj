/*
 * $Id: PrjMainBO.java,v 1.2 2010/06/08 12:33:42 xiexianglong Exp $
 *
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.prjset.prjmain.bs;

import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import com.foundercy.pf.util.sessionmanager.SessionUtil;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.Query;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title:��Ŀ�걨�������ö�Ӧ��ҵ������
 * </p>
 * <p>
 * Description:�ԡ���Ŀ�걨�������ö�Ӧ��ҵ����ӿڡ���ʵ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class PrjMainBO implements PrjMainIBS {
	// ���ݿ⴦��ӿڣ���ʱ����
//	private GeneralDAO dao;
//
//	public GeneralDAO getDao() {
//		return dao;
//	}
//
//	public void setDao(GeneralDAO dao) {
//		this.dao = dao;
//	}

	/**
	 * ����Class �� PrjMainUI �õ���ʾ���νṹ��DataSet
	 * 
	 * @return ���νṹ��DataSet
	 */
	public DataSet getInputSetTreeData() {
		DataSet dsData = null;
		try {
			StringBuffer sbSQL = new StringBuffer();
			// ��ѯ�������������Ϣ��Ϊ���Ժ���ƽ̨���ݣ�������chr_id,chr_code,chr_name,parent_id,level_num,is_leaf�ֶ�
			sbSQL.append(" select input_set_id chr_id,input_set_id chr_code,  ");
			sbSQL.append("        input_set_name chr_name,null parent_id, ");
			sbSQL.append("        1 level_num,1 is_leaf,  ");// �����ֶΣ����ڽ���
			sbSQL.append("        input_set_id, input_set_name, input_set_shortname, ");
			sbSQL.append("        input_set_longname, is_default ");
			sbSQL.append("   from fb_p_input_set ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return dsData;
	}

	/**
	 * ����Class �� PrjMainListener , PrjMain_DetailUI ���������ñ��ŵõ���Ӧ����ϸ����
	 * 
	 * @param input_set_id
	 *            �����ñ���
	 * @return ��ϸ������ɵ�List
	 */
	public List getSetDetailValues(String input_set_id) {
		List list = new Vector();
		try {
			DataSet dsData = null;
			StringBuffer sbSQL = new StringBuffer();
			// ���ݱ�Ų�ѯ���ñ���ϸ��¼����Ҫ�����ؼ���õ��ؼ���Ϣ
			sbSQL.append(" select cp.comp_id,comp_name,cp.comp_ename,cp.field_type, ");
			sbSQL.append("        decode(cp.field_type,1,'¼���',4,'������') field_type_name, ");
			sbSQL.append("        sd.is_must,decode(sd.is_must,0,'�Ǳ���','����') is_must_name, ");
			sbSQL.append("        cp.set_year,cp.rg_code, ");
			sbSQL.append("        decode(cp.is_inner,0,'��','��') is_inner ");
			sbSQL.append("   from fb_p_input_setdetail sd,fb_p_input_components cp ");
			sbSQL.append("  where sd.input_comp_id=cp.comp_id ");
			sbSQL.append("    and sd.input_set_id=" + input_set_id);
			sbSQL.append(" order by cp.comp_id ");
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			// �Բ�ѯ������д�������list��ԭ��ѯ����޷�ֱ��ʹ�ã�
			if ( dsData.getDataCache() != null ) {
				for ( int i = 0 ; i < dsData.getDataCache().size() ; i++ ) {
					XMLData xmlData = new XMLData();
					xmlData.put("comp_id", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_id")).getValue());
					xmlData.put("comp_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_name")).getValue());
					xmlData.put("comp_ename", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_ename")).getValue());
					xmlData.put("field_type", ((Field) ((Map) dsData.getDataCache().get(i)).get("field_type")).getValue());
					xmlData.put("field_type_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("field_type_name")).getValue());
					xmlData.put("is_must", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_must")).getValue());
					xmlData.put("is_must_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_must_name")).getValue());
//					xmlData.put("is_comm", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_comm")).getValue());
					xmlData.put("set_year", ((Field) ((Map) dsData.getDataCache().get(i)).get("set_year")).getValue());
					xmlData.put("rg_code", ((Field) ((Map) dsData.getDataCache().get(i)).get("rg_code")).getValue());
//					xmlData.put("comp_min_len", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_min_len")).getValue());
//					xmlData.put("comp_max_len", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_max_len")).getValue());
//					xmlData.put("comp_min_value", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_min_value")).getValue());
//					xmlData.put("comp_max_value", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_max_value")).getValue());
					
					xmlData.put("is_inner", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_inner")).getValue());
					
//					xmlData.put("data_format", ((Field) ((Map) dsData.getDataCache().get(i)).get("data_format")).getValue());
					list.add(xmlData);
				}
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * ����Class �� PrjMainListener �ж��Ƿ���ɾ��������Ϣ
	 * 
	 * @param inputID
	 *            ������Ϣ���
	 * @return ��ʾ��Ϣ
	 */
	public String canDeleteInputSet(String inputID) {
		try {
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�Ƿ���ʹ�ñ��ؼ���������ϸ����
			sbSQL.append(" select count(1) recordcount ");
			sbSQL.append("   from fb_e_prjsort ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_set_id=" + inputID);
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			if ( dsData.getRecordCount() != 1 ) {
				return "�Բ��𣬸������Ѿ�������Ŀ����¼�У�";
			}
			dsData.beforeFirst();
			while ( dsData.next() ) {
				if ( dsData.fieldByName("recordcount").getInteger() != 0 ) {
					return "�Բ��𣬸������Ѿ�������Ŀ����¼�У�";
				}
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return "��ѯ���ִ���";
		}
		return "";
	}

	/**
	 * ����Class �� PrjMainListener ɾ��������Ϣ��������ֵ
	 * 
	 * @param inputID
	 *            ������Ϣ���
	 * @return 0 �ɹ� -1 ʧ��
	 */
	public int deleteInputSet(String inputID) {
		int input_set_id = inputID == null ? 0 : Integer.parseInt(inputID);
		try {
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			//ɾ�������ñ�
			sbSQL.setLength(0);
			sbSQL.append(" delete fb_p_input_set ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_set_id=" + input_set_id);
			listSQL.add(sbSQL.toString());
			//ɾ����ϸ���ñ�
			sbSQL.setLength(0);
			sbSQL.append(" delete fb_p_input_setdetail ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_set_id=" + input_set_id);
			listSQL.add(sbSQL.toString());
			QueryStub.getQueryTool().executeBatch(listSQL);
			return 0;
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return -1;
		}
	}

	/**
	 * ����Class �� PrjMain_DetailListener ����������Ϣ
	 * 
	 * @param xmlData
	 *            ���ݵķ�װ
	 * @return 0 ���� 1 �쳣
	 */
	public int insertPrjSetting(XMLData xmlData) {
		// ��ȡ��������
		String input_set_id = (String) xmlData.get("input_set_id");
		String input_set_name = (String) xmlData.get("input_set_name");
		String input_set_shortname = (String) xmlData.get("input_set_shortname");
		String input_set_longname = (String) xmlData.get("input_set_longname");
		String is_default = ((Boolean) xmlData.get("is_default")).booleanValue() ? "1"
				: "0";
		// ��ϸ���ݷ�װ��list����
		List detail_data = (List) xmlData.get("detail_data");
		try {
			// ��װSQL
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			// ��ѯ����ȡ���������
			sbSQL.append(" select nvl(max(input_set_id),0)+1 as MAXID ");
			sbSQL.append("   from fb_p_input_set ");
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			if ( dsData.getRecordCount() != 1 ) {
				return -1;
			}
			dsData.beforeFirst();
			while ( dsData.next() ) {
				input_set_id = dsData.fieldByName("MAXID").getString();
			}
			// ���ѡ��"Ĭ��"��Ҫ�Ƚ���������Ϊ"��Ĭ��"
			if ( "1".equals(is_default) ) {
				sbSQL.setLength(0);
				sbSQL.append(" update fb_p_input_set set is_default = 0 ");
				sbSQL.append("  where set_year="
						+ SessionUtil.getUserInfoContext().getSetYear());
				listSQL.add(sbSQL.toString());
			}
			// �����������
			sbSQL.setLength(0);
			sbSQL.append(" insert into fb_p_input_set ");
			sbSQL.append("   (input_set_id, input_set_name, ");
			sbSQL.append("    input_set_shortname, input_set_longname, ");
			sbSQL.append("    is_default, set_year, rg_code) ");
			sbSQL.append(" values ");
			sbSQL.append("   ('" + input_set_id + "', '" + input_set_name
					+ "', ");
			sbSQL.append("    '" + input_set_shortname + "', '"
					+ input_set_longname + "', ");
			sbSQL.append("    '" + is_default + "', ");
			sbSQL.append("    '"
					+ SessionUtil.getUserInfoContext().getSetYear() + "', ");
			sbSQL.append("    '"
					+ SessionUtil.getUserInfoContext().getCurrRegion() + "') ");
			listSQL.add(sbSQL.toString());
			// ��ɾ����ϸ������
			sbSQL.setLength(0);
			sbSQL.append(" delete fb_p_input_setdetail ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_set_id=" + input_set_id);
			listSQL.add(sbSQL.toString());
			if ( detail_data != null ) {
				// ���������ϸ����
				for ( int i = 0 ; i < detail_data.size() ; i++ ) {
					Map map = (Map) detail_data.get(i);
					sbSQL.setLength(0);
					sbSQL.append(" insert into fb_p_input_setdetail ");
					sbSQL.append("   (input_set_id, input_set_detail_id, input_comp_id, ");
					sbSQL.append("    set_year, rg_code, is_must) ");
					sbSQL.append(" values ");
					sbSQL.append("   ('" + input_set_id + "', '" + i + "','" + map.get("comp_id") + "', ");
					sbSQL.append("    '" + SessionUtil.getUserInfoContext().getSetYear() + "', ");
					sbSQL.append("    '" + SessionUtil.getUserInfoContext().getCurrRegion() + "', ");
					sbSQL.append("    '" + map.get("is_must") + "') ");
					listSQL.add(sbSQL.toString());
				}
			}
			// ����ִ������sql
			QueryStub.getQueryTool().executeBatch(listSQL);
			return Integer.parseInt(input_set_id);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return -1;
		}
	}

	/**
	 * ����Class �� PrjMain_DetailListener �޸�������Ϣ
	 * 
	 * @param xmlData
	 *            ���ݵķ�װ
	 * @return 0 ���� 1 �쳣
	 */
	public int updatePrjSetting(XMLData xmlData) {
		// ��ȡ��������
		String input_set_id = (String) xmlData.get("input_set_id");
		String input_set_name = (String) xmlData.get("input_set_name");
		String input_set_shortname = (String) xmlData.get("input_set_shortname");
		String input_set_longname = (String) xmlData.get("input_set_longname");
		String is_default = ((Boolean) xmlData.get("is_default")).booleanValue() ? "1"
				: "0";
		// ��ϸ���ݷ�װ��list����
		List detail_data = (List) xmlData.get("detail_data");
		try {
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			// ���ѡ��"Ĭ��"��Ҫ�Ƚ���������Ϊ"��Ĭ��"
			if ( "1".equals(is_default) ) {
				sbSQL.setLength(0);
				sbSQL.append(" update fb_p_input_set set is_default = 0 ");
				sbSQL.append("  where set_year="
						+ SessionUtil.getUserInfoContext().getSetYear());
				listSQL.add(sbSQL.toString());
			}
			// ������������
			sbSQL.setLength(0);
			sbSQL.append(" update fb_p_input_set ");
			sbSQL.append("    set input_set_name = '" + input_set_name + "', ");
			sbSQL.append("        input_set_shortname = '"
					+ input_set_shortname + "', ");
			sbSQL.append("        input_set_longname = '" + input_set_longname
					+ "', ");
			sbSQL.append("        is_default = '" + is_default + "' ");
			sbSQL.append("  where input_set_id = '" + input_set_id + "' ");
			sbSQL.append("    and set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			listSQL.add(sbSQL.toString());
			// ��ɾ����ϸ������
			sbSQL.setLength(0);
			sbSQL.append(" delete fb_p_input_setdetail ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_set_id=" + input_set_id);
			listSQL.add(sbSQL.toString());
			if ( detail_data != null ) {
				// ���������ϸ����
				for ( int i = 0 ; i < detail_data.size() ; i++ ) {
					Map map = (Map) detail_data.get(i);
					sbSQL.setLength(0);
					sbSQL.append(" insert into fb_p_input_setdetail ");
					sbSQL.append("   (input_set_id, input_set_detail_id, input_comp_id, ");
					sbSQL.append("    set_year, rg_code, is_must) ");
					sbSQL.append(" values ");
					sbSQL.append("   ('" + input_set_id + "', '" + i + "','" + map.get("comp_id") + "', ");
					sbSQL.append("    '" + SessionUtil.getUserInfoContext().getSetYear() + "', ");
					sbSQL.append("    '" + SessionUtil.getUserInfoContext().getCurrRegion() + "', ");
					sbSQL.append("    '" + map.get("is_must") + "') ");
					listSQL.add(sbSQL.toString());
				}
			}
			QueryStub.getQueryTool().executeBatch(listSQL);
			return Integer.parseInt(input_set_id);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return -1;
		}
	}

	/**
	 * ����Class �� PrjMain_DetailSelectUI �������пؼ���Ϣ
	 * 
	 * @return �ؼ���Ϣ
	 */
	public List getAllComponetsList() {
		List list = new Vector();
		try {
			DataSet dsData = null;
			StringBuffer sbSQL = new StringBuffer();
			sbSQL.append(" select cp.comp_id,comp_name,cp.comp_ename,cp.field_type, ");
			sbSQL.append("        decode(cp.field_type,1,'¼���',4,'������') field_type_name, ");
			sbSQL.append("        0 is_must,decode(0,0,'�Ǳ���','����') is_must_name, ");
			sbSQL.append("        cp.set_year,cp.rg_code, ");
			sbSQL.append("        cp.is_inner");
			sbSQL.append("   from fb_p_input_components cp ");
			sbSQL.append("  where cp.set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append(" order by cp.comp_id ");
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			// �Բ�ѯ������д�������list��ԭ��ѯ����޷�ֱ��ʹ�ã�
			if ( dsData.getDataCache() != null ) {
				for ( int i = 0 ; i < dsData.getDataCache().size() ; i++ ) {
					XMLData xmlData = new XMLData();
					xmlData.put("comp_id", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_id")).getValue());
					xmlData.put("comp_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_name")).getValue());
					xmlData.put("comp_ename", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_ename")).getValue());
					xmlData.put("field_type", ((Field) ((Map) dsData.getDataCache().get(i)).get("field_type")).getValue());
					xmlData.put("field_type_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("field_type_name")).getValue());
					xmlData.put("is_must", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_must")).getValue());
					xmlData.put("is_must_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_must_name")).getValue());
//					xmlData.put("is_comm", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_comm")).getValue());
					xmlData.put("set_year", ((Field) ((Map) dsData.getDataCache().get(i)).get("set_year")).getValue());
					xmlData.put("rg_code", ((Field) ((Map) dsData.getDataCache().get(i)).get("rg_code")).getValue());
//					xmlData.put("comp_min_len", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_min_len")).getValue());
//					xmlData.put("comp_max_len", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_max_len")).getValue());
//					xmlData.put("comp_min_value", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_min_value")).getValue());
//					xmlData.put("comp_max_value", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_max_value")).getValue());
					xmlData.put("is_inner", ((Field) ((Map) dsData.getDataCache().get(i)).get("is_inner")).getValue());
//					xmlData.put("data_format", ((Field) ((Map) dsData.getDataCache().get(i)).get("data_format")).getValue());
					list.add(xmlData);
				}
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * ����Class �� PrjMain_DetailSelectUI �������пؼ���ϸ����
	 * 
	 * @return �ؼ���ϸ����
	 */
	public List getAllComBoxValueList() {
		List list = new Vector();
		try {
			DataSet dsData = null;
			StringBuffer sbSQL = new StringBuffer();
			sbSQL.append(" select comp_id, item_code, item_name ");
			sbSQL.append("   from fb_p_input_combox_values cp ");
			sbSQL.append("  where cp.set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append(" order by comp_id, item_code ");
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			// �Բ�ѯ������д�������list��ԭ��ѯ����޷�ֱ��ʹ�ã�
			if ( dsData.getDataCache() != null ) {
				for ( int i = 0 ; i < dsData.getDataCache().size() ; i++ ) {
					XMLData xmlData = new XMLData();
					xmlData.put("comp_id", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_id")).getValue());
					xmlData.put("item_code", ((Field) ((Map) dsData.getDataCache().get(i)).get("item_code")).getValue());
					xmlData.put("item_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("item_name")).getValue());
					list.add(xmlData);
				}
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * ����Class �� PrjMain_SelectModUI ��ÿؼ������ݼ�
	 * 
	 * @return �ؼ������ݼ�
	 */
	public DataSet getComponentsTreeData() {
		DataSet dsData = null;
		try {
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�ؼ�����������
			sbSQL.append("  select comp_id as chr_id,comp_id as chr_code,  ");
			sbSQL.append("        comp_name as chr_name,null as parent_id, ");
			sbSQL.append("        1 as level_num,1 as is_leaf,  ");// �����ֶΣ����ڽ���
			sbSQL.append("        comp_id, comp_name, comp_ename, field_type,  ");
			sbSQL.append("        set_year, rg_code, last_ver, ");
			sbSQL.append("         is_inner  ");
			sbSQL.append("   from fb_p_input_components  ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("  order by comp_id ");
			String sSQL = sbSQL.toString();
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sSQL, dsData);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return null;
		}
		return dsData;
	}

	/**
	 * ����Class �� PrjMain_SelectModListener ��ȡ������ؼ���Ӧ��ϸ����
	 * 
	 * @param comp_id
	 *            �ؼ����
	 * @return ������ؼ���Ӧ��ϸ����
	 */
	public List getComboxValues(String comp_id) {
		List list = new Vector();
		try {
			DataSet dsData = null;
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�ؼ���Ӧ����ϸ����
			sbSQL.append(" select comp_id, item_code, item_name, set_year, rg_code, last_ver  ");
			sbSQL.append("   from fb_p_input_combox_values ");
			sbSQL.append("  where comp_id=" + comp_id);
			sbSQL.append(" order by item_code ");
			dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			// �Բ�ѯ������д�������list��ԭ��ѯ����޷�ֱ��ʹ�ã�
			if ( dsData.getDataCache() != null ) {
				for ( int i = 0 ; i < dsData.getDataCache().size() ; i++ ) {
					XMLData xmlData = new XMLData();
					xmlData.put("comp_id", ((Field) ((Map) dsData.getDataCache().get(i)).get("comp_id")).getValue());
					xmlData.put("item_code", ((Field) ((Map) dsData.getDataCache().get(i)).get("item_code")).getValue());
					xmlData.put("item_name", ((Field) ((Map) dsData.getDataCache().get(i)).get("item_name")).getValue());
					list.add(xmlData);
				}
			}
		} catch ( Exception e ) {
		}
		return list;
	}

	/**
	 * ����Class �� PrjMain_SelectModListener �ж��Ƿ��ܹ�ɾ���ؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ�����������ɾ��
	 */
	public String componentCanDelete(XMLData xmldata) {
		String comp_id = (String) xmldata.get("comp_id");
		try {
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�Ƿ���ʹ�ñ��ؼ���������ϸ����
			sbSQL.append(" select count(1) recordcount ");
			sbSQL.append("   from fb_p_input_setdetail ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and input_comp_id=" + comp_id);
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sbSQL.toString(), dsData);
			if ( dsData.getRecordCount() != 1 ) {
				return "�Բ��𣬸��ֶ��Ѿ��������ü�¼�У�";
			}
			dsData.beforeFirst();
			while ( dsData.next() ) {
				if ( dsData.fieldByName("recordcount").getInteger() != 0 ) {
					return "�Բ��𣬸��ֶ��Ѿ��������ü�¼�У�";
				}
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return "��ѯ���ִ���";
		}
		return "";
	}

	/**
	 * ����Class �� PrjMain_SelectModListener ɾ����ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 ɾ���ɹ� -1 ɾ��ʧ��
	 */
	public int deleteComponent(XMLData xmldata) {
		String comp_id = (String) xmldata.get("comp_id");
		try {
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			sbSQL.setLength(0);
			//ɾ���ؼ���
			sbSQL.append(" delete from fb_p_input_components ");
			sbSQL.append("  where comp_id = '" + comp_id + "' ");
			listSQL.add(sbSQL.toString());
			sbSQL.setLength(0);
			//ɾ���ؼ���ϸ���ݱ�
			sbSQL.append(" delete from fb_p_input_combox_values ");
			sbSQL.append("  where comp_id = '" + comp_id + "' ");
			listSQL.add(sbSQL.toString());
			QueryStub.getQueryTool().executeBatch(listSQL);
			return 0;
		} catch ( Exception e ) {
			Log.error(e.getMessage());
			return -1;
		}
	}

	/**
	 * ����Class �� PrjMain_SelectModListener �ж��Ƿ��ܹ���ӿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ��������������
	 */
	public String componentCanInsert(XMLData xmldata) {
		String comp_ename = (String) xmldata.get("comp_ename");
		try {
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�Ƿ���ʹ�ñ��ֶεĿؼ����������ֶ��ظ�
			sbSQL.append(" select 1 ");
			sbSQL.append("   from fb_p_input_components ");
			sbSQL.append("  where set_year="
					+ SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and comp_ename='" + comp_ename + "'");
			String sSQL = sbSQL.toString();
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sSQL, dsData);
			if ( !dsData.isEmpty() ) {
				return "�Բ��𣬸��ֶ��Ѿ�ʹ�ã�";
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return "";
	}

	/**
	 * ����Class �� PrjMain_SelectModListener �����ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 ��ӳɹ� -1 ���ʧ��
	 */
	public int insertComponent(XMLData xmldata, List tableData) {
		// String comp_id = (String) xmldata.get("comp_id");
		String comp_name = (String) xmldata.get("comp_name");
		String comp_ename = (String) xmldata.get("comp_ename");
		String field_type = (String) xmldata.get("field_type");
//		String is_comm = ((Boolean) xmldata.get("is_comm")).booleanValue() ? "1"
//				: "0";
//		Integer comp_min_len = (Integer) xmldata.get("comp_min_len");
//		Integer comp_max_len = (Integer) xmldata.get("comp_max_len");
//		String comp_min_value = (String) xmldata.get("comp_min_value");
//		String comp_max_value = (String) xmldata.get("comp_max_value");
		//�ֹ�����ľ� �������õ�
		String is_inner = "0";
//		String data_format = (String) xmldata.get("data_format");
		try {
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			String maxid = null;
			String sSQL = null;
			//��ѯ���ؼ����
			sbSQL.append(" select nvl(max(comp_id),0)+1 MAXID ");
			sbSQL.append("   from fb_p_input_components ");
			sSQL = sbSQL.toString();
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sSQL, dsData);
			if ( dsData.getRecordCount() != 1 ) {
				return -1;
			}
			dsData.beforeFirst();
			while ( dsData.next() ) {
				maxid = dsData.fieldByName("MAXID").getString();
			}
			//����ؼ�����
			sbSQL.setLength(0);
			sbSQL.append(" insert into fb_p_input_components ");
			sbSQL.append(" ( ");
			sbSQL.append("   comp_id, comp_name, comp_ename, ");
			sbSQL.append("   field_type,  ");
			sbSQL.append("   set_year, rg_code, last_ver, ");
			sbSQL.append("   is_inner");
			sbSQL.append(" ) ");
			sbSQL.append(" values ");
			sbSQL.append(" ( ");
			sbSQL.append("   '" + maxid + "',");
			sbSQL.append("   '" + comp_name + "', '" + comp_ename + "', ");
			sbSQL.append("   '" + field_type + "',");
			sbSQL.append("   '" + SessionUtil.getUserInfoContext().getSetYear()
					+ "', '" + SessionUtil.getUserInfoContext().getCurrRegion()
					+ "','',");
//			sbSQL.append("   '" + comp_min_len + "', '" + comp_max_len + "', ");
//			sbSQL.append("   '" + comp_min_value + "', '" + comp_max_value
//					+ "', ");
			sbSQL.append("   '" + is_inner + "'");
			sbSQL.append(" )");
			sSQL = sbSQL.toString();
			listSQL.add(sSQL);
			sbSQL.setLength(0);
			//ɾ���ؼ���ϸ����
			sbSQL.append(" delete from fb_p_input_combox_values ");
			sbSQL.append(" where comp_id=" + maxid);
			sSQL = sbSQL.toString();
			listSQL.add(sSQL);
			//"4"��ָ������
			if ( "4".equals(field_type) ) {
				if ( tableData != null ) {
					//ѭ�������ϸ����
					for ( int i = 0 ; i < tableData.size() ; i++ ) {
						Map map = (Map) tableData.get(i);
						sbSQL.setLength(0);
						sbSQL.append(" insert into fb_p_input_combox_values ");
						sbSQL.append("   (comp_id, item_code, item_name,  ");
						sbSQL.append("    set_year, rg_code) ");
						sbSQL.append(" values ");
						sbSQL.append("   ('" + maxid + "', '"
								+ map.get("item_code") + "', '"
								+ map.get("item_name") + "', ");
						sbSQL.append("    '"
								+ SessionUtil.getUserInfoContext().getSetYear()
								+ "',");
						sbSQL.append("    '"
								+ SessionUtil.getUserInfoContext().getCurrRegion()
								+ "') ");
						sSQL = sbSQL.toString();
						listSQL.add(sSQL);
					}
				}
			}
			QueryStub.getQueryTool().executeBatch(listSQL);
			return Integer.parseInt(maxid);
		} catch ( Exception e ) {
			Log.error("��̨���������ݿ�����쳣�������Ǳ�Ľṹ������ "+e.getMessage());			
			return -1;
		}
	}

	/**
	 * ����Class �� PrjMain_SelectModListener �ж��Ƿ��ܹ��޸Ŀؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ������������޸�
	 */
	public String componentCanUpdate(XMLData xmldata) {
		String comp_id = (String) xmldata.get("comp_id");
		String comp_ename = (String) xmldata.get("comp_ename");
		try {
			StringBuffer sbSQL = new StringBuffer();
			//��ѯ�Ƿ�����ͬ���ֶ�
			sbSQL.append(" select 1 ");
			sbSQL.append("   from fb_p_input_components ");
			sbSQL.append("  where set_year=" + SessionUtil.getUserInfoContext().getSetYear());
			sbSQL.append("    and comp_ename='" + comp_ename + "'");
			sbSQL.append("    and comp_id<>" + comp_id);
			String sSQL = sbSQL.toString();
			DataSet dsData = DataSet.create();
			DBSqlExec.getDataSet(sSQL, dsData);
			if ( !dsData.isEmpty() ) {
				return "�Բ��𣬸��ֶ��Ѿ�ʹ�ã�";
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		return "";
	}

	/**
	 * ����Class �� PrjMain_SelectModListener �޸���ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 �޸ĳɹ� -1 �޸�ʧ��
	 */
	public int updateComponent(XMLData xmldata, List tableData) {

		//
		// System.out.println(tableData);
		String comp_id = (String) xmldata.get("comp_id");
		String comp_name = (String) xmldata.get("comp_name");
		String comp_ename = (String) xmldata.get("comp_ename");
		String field_type = (String) xmldata.get("field_type");
		// String is_comm = ((Boolean) xmldata.get("is_comm")).booleanValue() ?
		// "1"
		// : "0";
		// Integer comp_min_len = (Integer) xmldata.get("comp_min_len");
		// Integer comp_max_len = (Integer) xmldata.get("comp_max_len");
		// String comp_min_value = (String) xmldata.get("comp_min_value");
		// String comp_max_value = (String) xmldata.get("comp_max_value");
		// String is_inner = ((Boolean) xmldata.get("is_hide")).booleanValue() ?
		// "1"
		// : "0";
		// String data_format = (String) xmldata.get("data_format");
		try {
			List listSQL = new Vector();
			StringBuffer sbSQL = new StringBuffer();
			// ���¿ؼ���
			sbSQL.append(" update fb_p_input_components ");
			sbSQL.append("    set comp_name = '" + comp_name + "', ");
			sbSQL.append("        comp_ename = '" + comp_ename + "', ");
			sbSQL.append("        field_type = '" + field_type + "'");
			// sbSQL.append(" is_comm = '" + is_comm + "', ");
			// sbSQL.append(" comp_min_len = '" + comp_min_len + "', ");
			// sbSQL.append(" comp_max_len = '" + comp_max_len + "', ");
			// sbSQL.append(" comp_min_value = '" + comp_min_value + "', ");
			// sbSQL.append(" comp_max_value = '" + comp_max_value + "', ");
			// sbSQL.append(" is_hide = '" + is_hide + "'");
			// sbSQL.append(" data_format = '" + data_format + "' ");
			sbSQL.append("  where comp_id = '" + comp_id + "' ");
			String sSQL = sbSQL.toString();
			listSQL.add(sSQL);
			sbSQL.setLength(0);
			// ɾ���ؼ���ϸ����
			sbSQL.append(" delete from fb_p_input_combox_values ");
			sbSQL.append(" where comp_id=" + comp_id);
			sSQL = sbSQL.toString();
			listSQL.add(sSQL);
			// "4"��ָ������
			if ("4".equals(field_type) && (tableData != null)) {
				// ѭ�������ϸ����
				for (int i = 0; i < tableData.size(); i++) {
					Map map = (Map) tableData.get(i);
					sbSQL.setLength(0);
					sbSQL.append(" insert into fb_p_input_combox_values ");
					sbSQL.append("   (comp_id, item_code, item_name,  ");
					sbSQL.append("    set_year, rg_code) ");
					sbSQL.append(" values ");
					sbSQL.append("   ('" + comp_id + "', '"
							+ map.get("item_code") + "', '"
							+ map.get("item_name") + "', ");
					sbSQL.append("    '"
							+ SessionUtil.getUserInfoContext().getSetYear()
							+ "',");
					sbSQL.append("    '"
							+ SessionUtil.getUserInfoContext().getCurrRegion()
							+ "') ");
					sSQL = sbSQL.toString();
					listSQL.add(sSQL);
				}
			}
			QueryStub.getQueryTool().executeBatch(listSQL);
			return Integer.parseInt(comp_id);
		} catch (Exception e) {
			Log.error(e.getMessage());
			return -1;
		}
	}
	/**
	 * �Ѿ����õ��ֶ�(C�ֶ�)
	 * @return
	 * @throws Exception
	 */
	public List getFieldHadUsed() throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("select comp_ename from fb_p_input_components where set_year=?");
		List list = QueryStub.getQueryTool().executeQuery2(query.toString(),new Object[]{SessionUtil.getUserInfoContext().getSetYear()});
		List result = new ArrayList();
		for(int i=0;i<list.size();i++) {
			result.add(((Map)list.get(i)).get("comp_ename"));
		}
		return result;
	}
	/**
	 * �õ�һ�ű����ֶ���Ϣ
	 * @param talbeName
	 * @return
	 * @throws Exception
	 */
	public List getFieldNameByTable(String tableName) throws Exception {
		Query query = QueryStub.getQueryTool();
		Set res = query.getColumnInfo(tableName);
		Pattern p = Pattern.compile("^[Cc][1-9]+");
		Matcher m;
		Iterator itr = res.iterator();
		String con = "";
		List result = new ArrayList();
		while(itr.hasNext()) {
			con = (String)itr.next();
			m = p.matcher(con);
			if(m.find()) {
				result.add(con);
			}
		}
		return result;
	}
}
