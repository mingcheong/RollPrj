package gov.nbcs.rp.sys.sysrefcol.ibs;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;

/**

 */
public interface ISysRefCol {
	// �����б��
	String REFCOL_ID = "refcol_id";

	// ����������
	String REFCOL_NAME = "refcol_name";

	// ����������
	String REFCOL_KIND = "refcol_kind";

	// �������������
	String SQL_DET = "sql_det";

	// ���������Ora����
	String SQL_DET_O = "sql_det_o";

	// ѡȡ��ʽ
	String SELECT_KIND = "select_kind";

	// �����ֶ�
	String PRIMARY_FIELD = "primary_field";

	// �����ֶ�����
	String PRIMARY_TYPE = "primary_type";

	// ��ʾ��������ֶ�
	String CODE_FIELD = "code_field";

	// ��ʾ�����ֶ�
	String NAME_FIELD = "name_field";

	// ������ֶ�
	String LVL_FIELD = "lvl_field";

	// �������
	String LVL_STYLE = "lvl_style";

	// ����ʹ����
	String DATA_OWNER = "data_owner";

	// Ԥ�����
	String SET_YEAR = "set_year";

	// ����
	String RG_CODE = "rg_code";

	// ���汾
	String LAST_VER = "last_ver";

	/**
	 * ��ѯ�����м�¼
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColRecord(String setYear) throws Exception;

	/**
	 * ִ��sql���
	 * 
	 * @param sSql
	 * @throws Exception
	 */
	public void exeSql(String sSql) throws Exception;

	/**
	 * ִ��sql��䣬����DataSet
	 * 
	 * @param sSql
	 * @throws Exception
	 */
	public DataSet exeSqlDs(String sSql) throws Exception;

	/**
	 * 
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getDisplayFormat(String setYear) throws Exception;

	/**
	 * ȡ����������ϸ
	 * 
	 * @param refColId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getReservedField(String setYear) throws Exception;

	/**
	 * ȡ����������ϸ,����������refcode_id
	 * 
	 * @param refColId
	 * @param setYear
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColDetailWithRefColId(String RefCol_id, String setYear)
			throws Exception;

	/**
	 * ����SQL��ѯ��ȡ�ֶ���Ϣ
	 * 
	 * @throws Exception
	 */
	public TableColumnInfo[] getFieldInfo(String sSql) throws Exception;

	/**
	 * ����������
	 * 
	 * @param dsRefCol,������������Ϣ
	 * @param dsRefColDetail,��������ϸ����Ϣ
	 * @param dsRefColRelatable
	 *            �����й�������Ϣ
	 * @throws Exception
	 */
	public void saveRefCol(DataSet dsRefCol, DataSet dsRefColDetail,
			DataSet dsRefColRelatable) throws Exception;

	/**
	 * �õ�����Դ��
	 * 
	 * @param RefCol_Id
	 * @return
	 * @throws Exception
	 */
	public DataSet getRefColRelatable(String RefCol_Id, String setYear)
			throws Exception;

	/**
	 * ɾ��һ�������й�����Ϣ
	 * 
	 * @param dsRefCol
	 * @param sRefColId
	 * @throws Exception
	 */
	public void DeleteRefCol(String sRefColId, String setYear) throws Exception;

	/**
	 * ����������Ƿ�ʹ��
	 * 
	 * @return.true�� δ��ʹ�ã�false:�ѱ�ʹ��
	 * @throws Exception
	 */
	public InfoPackage checkRefColUsed(String sRefColId, String setYear)
			throws Exception;

	/**
	 * ���������д�Ƿ��ظ�
	 * 
	 * @param sName����
	 * @param sCode���룬����ʱ��null,�޸�ʱ�ǰ��¼��refcol_id
	 * @param setYear
	 * @return true:δ��ʹ��,false���ѱ�ʹ��
	 * @throws Exception
	 */
	public boolean checkRefNameUsed(String sName, String sCode, String setYear)
			throws Exception;

	public String replaceRefColFixFlag(String sSql) throws Exception;

}
