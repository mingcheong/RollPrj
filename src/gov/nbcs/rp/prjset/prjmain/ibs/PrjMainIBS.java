/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.ibs;

import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;

/**
 * <p>
 * Title:��Ŀ�걨�������ö�Ӧ��ҵ����ӿ�
 * </p>
 * <p>
 * Description:Ϊ��Ŀ�걨����������ع����ṩҵ����
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
public interface PrjMainIBS {

	/** ģ�������Ӳ��� */
	public static final int OPERATION_ADD = 1;

	/** ģ������޸Ĳ��� */
	public static final int OPERATION_MOD = 2;

	/** ģ���״̬-����״̬ */
	public static final int NORMAL_STATUS = 1;

	/** ģ���״̬-���״̬ */
	public static final int ADD_STATUS = 2;

	/** ģ���״̬-�޸�״̬ */
	public static final int MOD_STATUS = 3;

	/** ģ���״̬-ɾ��״̬ */
	public static final int DEL_STATUS = 4;

	/**
	 * ����Class �� PrjMainUI 
	 * �õ���ʾ���νṹ��DataSet
	 * 
	 * @return ���νṹ��DataSet
	 */
	public DataSet getInputSetTreeData();

	/**
	 * ����Class �� PrjMainListener , PrjMain_DetailUI
	 * ���������ñ��ŵõ���Ӧ����ϸ����
	 * 
	 * @param input_set_id
	 *            �����ñ���
	 * @return ��ϸ������ɵ�List
	 */

	public List getSetDetailValues(String input_set_id);

	/**
	 * ����Class �� PrjMainListener 
	 * �ж��Ƿ���ɾ��������Ϣ
	 * 
	 * @param inputID
	 *            ������Ϣ���
	 * @return ��ʾ��Ϣ
	 */
	public String canDeleteInputSet(String inputID);

	/**
	 * ����Class �� PrjMainListener 
	 * ɾ��������Ϣ��������ֵ
	 * 
	 * @param inputID
	 *            ������Ϣ���
	 * @return 0 �ɹ� -1 ʧ��
	 */
	public int deleteInputSet(String inputID);

	/**
	 * ����Class �� PrjMain_DetailListener 
	 * ����������Ϣ
	 * 
	 * @param xmlData
	 *            ���ݵķ�װ
	 * @return 0 ���� 1 �쳣
	 */
	public int insertPrjSetting(XMLData xmlData);

	/**
	 * ����Class �� PrjMain_DetailListener 
	 * �޸�������Ϣ
	 * 
	 * @param xmlData
	 *            ���ݵķ�װ
	 * @return 0 ���� 1 �쳣
	 */
	public int updatePrjSetting(XMLData xmlData);

	/**
	 * ����Class �� PrjMain_DetailSelectUI 
	 * �������пؼ���Ϣ
	 * 
	 * @return �ؼ���Ϣ
	 */
	public List getAllComponetsList();

	/**
	 * ����Class �� PrjMain_DetailSelectUI 
	 * �������пؼ���ϸ����
	 * 
	 * @return �ؼ���ϸ����
	 */
	public List getAllComBoxValueList();

	/**
	 * ����Class �� PrjMain_SelectModUI 
	 * ��ÿؼ������ݼ�
	 * 
	 * @return �ؼ������ݼ�
	 */
	public DataSet getComponentsTreeData();

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * ��ȡ������ؼ���Ӧ��ϸ����
	 * 
	 * @param comp_id
	 *            �ؼ����
	 * @return ������ؼ���Ӧ��ϸ����
	 */
	public List getComboxValues(String comp_id);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * �ж��Ƿ��ܹ�ɾ���ؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ�����������ɾ��
	 */
	public String componentCanDelete(XMLData xmldata);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * ɾ����ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 ɾ���ɹ� -1 ɾ��ʧ��
	 */
	public int deleteComponent(XMLData xmldata);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * �ж��Ƿ��ܹ���ӿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ��������������
	 */
	public String componentCanInsert(XMLData xmldata);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * �����ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 ��ӳɹ� -1 ���ʧ��
	 */
	public int insertComponent(XMLData xmldata, List tableData);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * �ж��Ƿ��ܹ��޸Ŀؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return �жϺ����ʾ��Ϣ��Ϊ������������޸�
	 */
	public String componentCanUpdate(XMLData xmldata);

	/**
	 * ����Class �� PrjMain_SelectModListener 
	 * �޸���ؿؼ���Ϣ
	 * 
	 * @param xmldata
	 *            ��װ�ؼ���Ϣ��XMLData
	 * @return 0 �޸ĳɹ� -1 �޸�ʧ��
	 */
	public int updateComponent(XMLData xmldata, List tableData);
	/**
	 * �Ѿ����õ��ֶ�(C�ֶ�)
	 * @return
	 * @throws Exception
	 */
	public List getFieldHadUsed() throws Exception;
	/**
	 * �õ�һ�ű����ֶ���Ϣ
	 * @param talbeName
	 * @return
	 * @throws Exception
	 */
	public List getFieldNameByTable(String tableName) throws Exception;
}
