/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title ��Ŀ��ϸ����-�ӿ�
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */

package gov.nbcs.rp.dicinfo.prjdetail.ibs;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

public interface IPrjDetail {
    public String Table_SIMP_COLINFO = "FB_P_SIMP_COLINFO";
	public String Table_SIMP_MASTER = "FB_P_SIMP_MASTER";
	public String SET_YEAR = "SET_YEAR";  //
    public String DETAIL_ID = "DETAIL_CODE";
	public String DETAIL_CODE = "DETAIL_CODE";  //��Ŀ��ϸ���
	public String DETAIL_NAME = "DETAIL_NAME";         //��Ŀ��ϸ����
	public String PAR_ID = "PAR_ID";         //��Ŀ��ϸ���ڵ�
	public String LVL_ID = "LVL_ID";         //������
	public String DETAILTAB_CODE = "DETAILTAB_CODE"; //��ȡ�������ʱ detail��coderule�����Ӧ���ֶ�ֵ
	public String DETAILTABITEM_CODE = "DETAILTABITEM_CODE";  //��ȡ�������ʱ��col��coderule�����Ӧ���ֶ�ֵ

	public String FIELD_ID = "FIELD_ID";    //����Ϣ�����ֶε�id
	public String FIELD_CNAME = "FIELD_CNAME"; //����Ϣ�����ֶε�name
	public String FIELD_FNAME = "FIELD_FNAME"; //��ȫ�� 
	public String FIELD_ENAME = "FIELD_ENAME"; //Ӣ����
	public String DATA_TYPE = "DATA_TYPE";     //������
	public String FIELD_KIND = "FIELD_KIND";   //������Դ
	public String FORMULA_DET = "FORMULA_DET"; //���㹫ʽ
	public String CALL_PRI = "CALC_PRI";       //���ȼ�
	public String PICK_VALUES = "PICK_VALUES"; //ѡȡֵ�б�
	public String DISPLAY_FORMAT = "DISPLAY_FORMAT";  //��ʾ���
	public String EDIT_FORMAT = "EDIT_FORMAT"; //�༭���
	public String NOTNULL = "NOTNULL";         //���б�������
	public String STD_TYPE = "STD_TYPE";       //�б�׼����
	public String IS_SUMCOL = "IS_SUMCOL";     //�Ƿ��Ǻϼ���
	public String FIELD_INDEX = "FIELD_INDEX";
	public String PRIMARY_INDEX = "PRIMARY_INDEX";
	public String PRIMARY_PROPFIELD = "PRIMARY_PROPFIELD";
	public String FIELD_COLUMN_WIDTH = "FIELD_COLUMN_WIDTH"; //�п�
	public String IS_HIDECOL = "IS_HIDECOL";
	
	public String TABLENAME_DETAIL = "fb_p_detail_mx";
	public String DETAIL_TYPE = "DETAIL_TYPE";
	
	public String FB_P_SORT_TO_DETAIL = "FB_P_SORT_TO_DETAIL"; 
	
	public DataSet getDataset( String aTableName, String aFilter ) throws Exception;
	public DataSet getComboDPDataset() throws Exception;  //��ȡ��ʾ���
	public DataSet getComboREDataset() throws Exception;  //��ȡ�༭���
	public DataSet getComboSCDataset() throws Exception;  //��ȡ��׼��getComboSCDataset
	public String getFormatMaxCode(String aFieldName, String aTableName, String aFilter,  int aFormat) throws Exception;
	public SysCodeRule getCodeRule(String aFilterCode) throws Exception;
	public DataSet getColInfoAccordDetailCode (String aTableName, String aFilter) throws Exception;
	public void dsPost( DataSet dsDetail,DataSet dscol,DataSet dsSour) throws Exception ;
	public DataSet getENameDataSet() throws Exception ;
	public boolean checkDetailName( String code,String Name ) throws Exception;
	public DataSet getAcctData() throws Exception;
	public int getRecNum(String aTableName, String aFilter ) throws Exception;
	/**
	 * ��������Ϣ����
	 * 
	 * @throws Exception
	 */
	public void saveColData(DataSet dsDetail,DataSet ds) throws Exception ;
}

//	  CustomComboBox cbItemType = new CustomComboBox(dsCombo,"CODE","NAME" );
