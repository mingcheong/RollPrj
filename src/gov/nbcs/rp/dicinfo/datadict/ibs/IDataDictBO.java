/**
 * Classname IDataDictBO
 *
 * Version 6.2.40
 *
 * Copyright �㽭���� ��Ȩ����
 */

package gov.nbcs.rp.dicinfo.datadict.ibs;

import java.util.List;

import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:�ֵ�����������ӿ�
 * </p>
 * <p>
 
 */

public interface IDataDictBO {

	public final int LOGIN_ONLINE = 0;// ����

	public final int LOGIN_OFFLINE = 1;// ����

	public final String ISCLASS = "isclass";// �Ƿ�������ֶ�

	public final String TABLECNAME = "tablecname";

	public final String TABLEENAME = "tableename";

	public static final int TABLE_MODEL = 1;// ѡ�����ݱ����ͼ

	public static final int DEFINE_MODEL = 2;// �Ѷ����ѡ��

	public static String DIC_FIELD_TABLENAME = "fb_dict_info_detail";

	public static String TABLE_ENAME = "table_ename";

	public static String AFIELD_ENAME = "afield_ename";

	public static String REFCOL_ID = "refcol_id";

	public static String REFCOL_NAME = "refcol_name";

	public static String CON_FIELDCNAME = "con_fieldcname";

	public static String CON_FIELDENAME = "con_fieldename";

	public static String AFIELD_CNAME = "afield_cname";

	public static String AFIELD_TYPE = "afield_type";

	public static String AIS_VISIBLE = "ais_visible";

	public static String AFIELD_SORT = "afield_sort";

	public static String MAIN_FIELD_TABLEENAME = "tableename";

	public static String MAIN_FIELD_TABLEENAME_DIV = "tableename_div";

	public static String MAIN_FIELD_ISBATCHNO = "isbatchno";

	public static String MAIN_FIELD_SUP_VER = "sup_ver";

	public static String DICID = "dicid";

	public static String AMEMO = "amemo";

	public static String fieldMark = "isfield";// ��������Դ���е���������ֶ�

	public static String MARK_FIELD = "1";// �ֶ�

	public static String MARK_TYPE = "-1";// ����

	public static String MARK_TABLE = "0";// ��

	// ��ݱ��:���ֱ����ȥ��
	public static String YEAR_FLAG = "year_flag";

	// �Աȷ������滻ʱ��Ӧ���Dicid
	public static String COMPARE_DICID = "compare_dicid";

	// �Ƿ�ϵͳ��������Դ1:���� 0������,��������Դ����ɾ��
	public static String ISDEFAULT = "isdefault";

	// ��Ŀ��ϸ��Ϣ
	public static String PRJ_PFS = "PRJPFS";

	// ��Ŀ������ϸ��Ϣ
	public static String PRJ_MX = "PRJMX";

	// ��Ŀ���
	public static String PRJ = "PRJ";

	// ������
	public static String PAYOUT_RAE = "PAYOUTRAE";

	// ��Ŀ����ǰ׺
	public static String PRJ_BASE_PREFIX = "base_";

	public static String PAYOUT_KIND_LVL = "payout_kind_lvl";

	/**
	 * ��ѯ������
	 * 
	 * @return
	 */
	public List queryTableTypeList();

	/**
	 * 
	 * �����ֵ�����
	 * 
	 * @param tableInfoData
	 */
	public int addDictType(String typeCode, String ypeName, String memo);

	/**
	 * 
	 * �����ֵ�����
	 * 
	 * @param tableInfoData
	 */
	public int updateDictType(String typeCode, String ypeName, String memo);

	/**
	 * 
	 * ɾ���ֵ�����
	 * 
	 * @param tableInfoData
	 */
	public int deleteDictType(String dictCode);

	/**
	 * ɾ���ֵ����Ϣ
	 * 
	 * @return ����
	 */
	public int deleteTableInfo(String dicID);

	/**
	 * �жϱ�����͵ı���Ƿ����
	 * 
	 * @param typeCodeObje
	 * @return
	 */
	public boolean isCodeExist(String typeCodeObje);

	/**
	 * ͬ��������
	 * 
	 * @param tableName
	 *            ����
	 * @return �Ƿ�ͬ���ɹ�
	 */
	public boolean sysRelTable(String tableName, String setYear);

	// 0------------------------------------------------
	/**
	 * ȡ��ö��Դ����ѡ����ģ���ִ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRefDataString(String setYear) throws Exception;

	/**
	 * ȡ�ñ�����ѡ��ģ��
	 * 
	 * @return
	 */
	public String getTableTypeRefString();

	/**
	 * ȡ�������ֵ��е���ϸ�ֶ���Ϣ
	 * 
	 * @param sTableID���Ӣ����
	 * @return
	 */
	public List getDicField(String sTableID, String setYear);

	/**
	 * ȡ�����еĲο���ѡ����
	 * 
	 * @return
	 */
	public String getAllRefFields(String setYear);

	/**
	 * ȡ�����е�֧������Դ�ı�
	 * 
	 * @param logMode
	 * @return
	 */
	public List getTableList(int logMode);

	/**
	 * �������ݣ���Ϊ��������Ӻ�ɾ���ֶΣ�����ֻ�Ǹ���ĳЩ�ֶ�
	 * 
	 * @param xmlMain
	 *            ������Ϣ
	 * @param lstFields
	 *            ��ϸ����Ϣ
	 * @return
	 * @throws Exception
	 */
	public String saveDs(XMLData xmlMain, List lstFields) throws Exception;

	/**
	 * ȡ��ö��Դ���ֶ�ѡ��ģ������,���ص���������Ϊ��key:REFCOL_ID,value:ѡ����String
	 * 
	 * @return
	 */
	public XMLData getRefDataFieldSelection(String setYear);

	/**
	 * ȡ�����ݿ���ж���ı����ͼ
	 * 
	 * @param isALl
	 * @return
	 */
	public List getTableInfo(int iModel, boolean isAll, String setYear);

	/**
	 * ��ӿ���еı�
	 */
	public String makeNewTable(String sTableName, String type, String tabProp,
			String setYear) throws Exception;

	/**
	 * ���Ԥ����ı�
	 * 
	 * @param tableName
	 *            ����
	 * @param type
	 *            �������
	 * @return
	 * @throws Exception
	 */
	public String makeNewTableByDefine(String tableName, String type,
			String tabProp, String report_id, String setYear) throws Exception;

	/**
	 * ��������ı�
	 * 
	 * @param lstSelectѡ��ı��б�
	 * @param isDefine�ǲ���Ԥ�����
	 * @throws Exception
	 */
	public void makeListTable(List lstSelect, boolean isDefine, String tabProp,
			String setYear) throws Exception;

	/**
	 * �����������˳��
	 * 
	 * @param xmlFirst
	 * @param xmlSecond
	 * @return
	 */
	public boolean changeOrder(XMLData xmlFirst, XMLData xmlSecond,
			String setYear);

	/**
	 * ȡ������Դ�������ã���ָ�������ķ�ʽ����������ʾӢ���ֶ� ���ݷ�Ϊ������һ���Ƿ��࣬���������ݱ������������ֶ���
	 * 
	 * @return
	 */
	public List getDataSourceForTree(String setYear);

	/**
	 * ȡ���ֵ���������Ϣ
	 * 
	 * @param tableID
	 *            �����ֵ���е�ID
	 * @return
	 */
	public XMLData getTableMainInfo(String tableID, String setYear);

	// ��ѯһ������Դ���ֶ�������Ϣ�����ڻ���
	public XMLData getFieldAndType(String dicID, String setYear);

	/**
	 * �ж�����Դ���������Ƿ���ڡ�
	 * 
	 * @param cname
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistTableCname(String cname, String reportId)
			throws Exception;

}
