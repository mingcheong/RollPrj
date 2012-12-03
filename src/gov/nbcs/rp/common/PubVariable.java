package gov.nbcs.rp.common;
/**
 * @author л��ݥ
 *
 * @version ����ʱ�䣺Apr 10, 20121:51:54 PM
 *
 * @Description
 */

public interface PubVariable {

	//=========��Ŀ¼������ǩ����=========//
	//���Ұ�ťͼƬ·��
	String IMG_PATH_QUERY = "images/fbudget/query.gif" ;
	
	//ר�������Ϣ����Ԥ��
	String TAB_TITLE_BASIC = "ר�������Ϣ����Ԥ��" ;
	
	//Ԥ����ϸ����Ԥ��
	String SUB_TAB_TITLE_BUDGET = "Ԥ����ϸ����Ԥ��" ;
	
	//ר����Ҫ���ݣ��걨���ɼ���������֤
	String SUB_TAB_TITLE_INFO = "ר�����ݼ��" ;
	
	//����������
	String SUB_TAB_TITLE_OPINION = "����������" ;
	    
	//��Ŀ����
	String TAB_TITLE_DOC = "ר���" ;
	
	//ר���б�
	String TAB_TITLE_LIST = "ר���б�" ;
	
	//�����ɹ�
	String TAB_TITLE_GOV = "�����ɹ�Ԥ���" ;
	
	//=========������=========//
	//����Ԥ�㼰��Ԥ��
	String PANEL_TITLE_FOUNDS = "ר����ϸԤ�㣨���ʽ���Դ��" ;
	
	//ר����ϸԤ�㣨��ר�����
	String PANEL_TITLE_ITEM = "ר����ϸԤ�㣨��������ϸ��" ;
	
	//�����б�
	String PANEL_TITLE_DOC_LIST = "�����б�" ;
	
	//�������
	String PANEL_TITLE_DOC_INFO = "�������" ;
	
	//=========�����ڵ����=========//
	String TREE_ROOT_NAME_DIV = "��λ�б�" ;
	String TREE_ROOT_NAME_PRJ = "ר���б�" ;
	
	//=========��Ŀ������Ϣ��ǩ=========//
	String LABEL_BASE_INFO_CATEGORY = "ר�����" ;
	String LABEL_BASE_INFO_PRJ_CODE = "ר����룺" ;
	String LABEL_BASE_INFO_SORT_CODE = "��ţ�" ;
	String LABEL_BASE_INFO_PRJ_NAME = "ר�����ƣ�" ;
	String LABEL_BASE_INFO_PRJ_NAME_MX = "ר����ϸ���ƣ�" ;
	String LABEL_BASE_INFO_SUBJECT = "���ܿ�Ŀ��" ;
	String LABEL_BASE_INFO_STANDARD_CATALOG = "��׼Ŀ¼��" ;
	String LABEL_BASE_INFO_IS_GOV_PROC = "���������ɹ�" ;
	String LABEL_BASE_INFO_START_YEAR = "��ʼ��ݣ�" ;
	String LABEL_BASE_INFO_END_YEAR = "������ݣ�" ;
	String LABEL_BASE_INFO_PRJ_STATE = "��Ŀ״̬��" ;
	
	//=========��ť����=========//
	String BUTTON_LABEL_MODIFY_CAPITAL_SOURCE = "ר���ʽ�����" ;
	String BUTTON_LABEL_MODIFY_SUBJECT = "�޸Ĺ��ܿ�Ŀ" ;
	String BUTTON_LABEL_MODIFY_ECONOMY_SUBJECT = "�޸ľ��ÿ�Ŀ" ;
	String BUTTON_LABEL_MODIFY_DELETE_DETAIL_ROW = "ɾ����ϸ��¼" ;
	
	String BUTTON_LABEL_MODIFY_PRJ_GOV = "�ɹ�Ŀ¼" ;
	String BUTTON_LABEL_SAVE_PRJ_GOV = "��  ��" ;
	String BUTTON_LABEL_EDIT_PRJ_GOV = "��   ��" ;
	String BUTTON_LABEL_CANCLE_PRJ_GOV = "ȡ   ��" ;
	
	String BUTTON_LABEL_ADD = "����" ;
	String BUTTON_LABEL_MODIFY = "�޸�" ;
	String BUTTON_LABEL_DELETE = "ɾ��" ;
	String BUTTON_LABEL_SAVE = "����" ;
	String BUTTON_LABEL_CANCEL = "ȡ��" ;
	String BUTTON_LABEL_VIEW = "�鿴" ;
	String BUTTON_LABEL_SAVE_AS = "���" ;
	String BUTTON_LABEL_PFS_SET = "ר���ʽ�����" ;
	String BUTTON_LABEL_PRJ_PRINT = "��Ŀ�걨��" ;
	
	String LABEL_DOC_INFO_SORT_NUM = "��ţ�" ;
	String LABEL_DOC_INFO_NAME = "���ƣ�" ;
	String LABEL_DOC_INFO_FILE = "�ļ���" ;
	
	//������ı�ǩ��ɫ
	String LABEL_BASE_INFO_REQUIRE_COLOR = "#ff0000" ;
	//������С����
	long DOC_MAX_LENGTH = 5*1024*1024 ;
	//������Ϣ�ؼ������ı���������ȣ��ĸ߶�
	int BASE_INFO_COMPONENT_HIGHT = 23 ;
	
	//=========������sql�ֶ����Ӧ=========//
	String CHR_ID = "chr_id" ;
	String CHR_CODE = "chr_code" ;
	String CHR_NAME = "chr_name" ;
	String CHR_IS_LEAF = "chr_is_leaf" ;
	String PARENT_ID = "parent_id" ;
	
	String CODE_SYBLE_BEGIN = "[" ;
	String CODE_SYBLE_END = "]" ;

	//����Ŀ�����зǿձ�־
	int ITEM_COL_NOT_NULL = 1 ;
	
	String FIELD_CODE = "field_code" ;
	String FIELD_ENAME = "field_ename" ;
	String FIELD_CNAME = "field_cname" ;
	String FIELD_FNAME = "field_fname" ;
	String NOT_NULL = "notnull" ;
	//ROW_ID
	String ROW_ID = "row_id" ;
	
	//Ԥ�����
	String SET_YEAR = "set_year" ;
	
	//�������
	String BATCH_NO = "batch_no" ;
	
	//��������
	String DATA_TYPE = "data_type" ;
	
	//��λ��ˮ��
	String EN_ID = "en_id" ;
	
	//��λ����
	String DIV_CODE = "div_code" ;
	
	//��λ����
	String DIV_NAME = "div_name" ;
	
	//��λ����
	String DIV_KIND = "div_kind" ;
	
	//��λ�Ƿ�֧�ֶ��Ŀ
	String ACCT_MUT = "acct_mut" ;
	//��������
	String RG_CODE = "rg_code" ;
	
	//�ϴ��޸�ʱ��
	String LAST_VER = "last_ver" ;
	
	//��Ŀ������
	String PRJSORT_CODE = "prjsort_code" ;
	
	//��Ŀ�������
	String PRJSORT_NAME = "prjsort_name" ;
	
	//��Ŀ����
	String PRJ_CODE = "prj_code" ;
	
	//���
	String SORT_DIV = "sort_div" ;
	
	//��Ŀ����
	String PRJ_NAME = "prj_name" ;
	
	//���ÿ�Ŀ��ˮ��
	String BSI_ID = "bsi_id" ;
	
	//���ÿ�Ŀ����
	String ACCT_CODE_JJ = "acct_code_jj" ;
	
	//���ÿ�Ŀ����
	String ACCT_NAME_JJ = "acct_name_jj" ;
	
	//���ÿ�Ŀȫ��
	String ECON_FULL_NAME = "econ_full_name" ;
	
	//���ܿ�Ŀ��ˮ��
	String BS_ID = "bs_id" ;
	
	//���ܿ�Ŀ����
	String ACCT_CODE = "acct_code" ;
	
	//���ܿ�Ŀ����
	String ACCT_NAME = "acct_name" ;
	
	//���ܿ�Ŀȫ��
	String ACCT_FULL_NAME = "acct_full_name" ;
	
	//��׼Ŀ¼����
	String PRJSTD_CODE = "prjstd_code" ;
	
	//��׼Ŀ¼����
	String PRJSTD_NAME = "prjstd_name" ;
	
	//�Ƿ������ɹ�
	String STOCK_FLAG = "stock_flag" ;
	
	//��ʼ���
	String START_DATE = "start_date" ;
	
	//�������
	String END_DATE = "end_date" ;
	
	//ר��״̬����
	String PRJ_STATUS = "prj_status" ;
	
	//ר��״̬����
	String PRJ_STATUS_NAME = "prj_status_name" ;
	
	//��Ŀ��Ҫ���ݣ��걨���ɼ���������֤
	String PRJ_CONTENT = "prj_content" ;
	
	//����������
	String CL3 = "cl3" ;
	
	//��Ŀ��ϸ���̶�
	String DETAIL_DEGREE = "detail_degree" ;
	//��Ŀ���õ�¼������
	String INPUT_SET_ID = "input_set_id" ;
	
	//��Ŀ�ĵ�����
	String AFFIX_TITLE = "affix_title" ;
	
	//��Ŀ�ĵ�����
	String AFFIX_TYPE = "affix_type" ;
	
	//��Ŀ�ĵ�ȫ����
	String AFFIX_FILE = "affix_file" ;
	
	//��Ŀ�ĵ����
	String AFFIX_SORT = "sort_id" ;
	
	//��Ŀ�ĵ�·��
	String AFFIX_FILE_PATH = "path" ;
	
	//��Ŀ�ĵ��Ƿ�ԭʼ���ݿ��¼
	String AFFIX_FILE_DATABASE = "affix_file_database" ;
	
	//��Ŀ�ĵ�����״̬
	String AFFIX_OPER_STATE = "oper_state" ;
	
	//��Ŀ�ĵ�����ʱ��
	String AFFIX_UPDATE = "upddate" ;
	
	//���ÿ�Ŀ��Χ
	String ECONOMY_SCOPE = "jjtype" ;
	
	//�����ͱ���
	String DETAIL_TYPE = "detail_type" ;
	
	//����������
	String DETAIL_TYPE_NAME = "detail_type_name" ;
	
	//������Դ
	String DATA_SOURCE = "data_source" ;
	
	//�Ƿ�ĩ����־
	String END_FLAG = "end_flag" ;
	
	//
	String DATA_SRC = "data_src" ;
	
	//
	String DATA_ATTR = "data_attr";
	
	//
	String PRJ_TYPE = "prj_type" ;
	
	//��Ϊ"�ܼ�"���ʽ���Դ
	String TOTAL_PRICE_ENAME = "total_prices" ;
	
	//��Ŀ¼����Ʊ��ֶ�
	//��Ŀ״ֵ̬
	String PRJ_STATE_VALUE = "state_value";
	
	//��Ŀ�ɷ�ɾ��
	String PRJ_CAN_DELETE = "can_delete";
	
	//��Ŀ�ɷ�༭
	String PRJ_CAN_EDIT = "can_edit" ;
	
	//��ֹ�༭���ʽ���Դ���ϣ�Ӣ�����ԡ�,���ָ�
	String NOT_PFS = "not_pfs" ;
	
	//��Ŀ���ƿɷ�༭:0���ɣ�1����
	String PRJ_NAME_EDITABLE = "not_prj_name";
	
	//��Ŀ��Ŀ�ɷ�༭�����ܿ�Ŀ�;��ÿ�Ŀ��:0���ɣ�1����
	String NOT_ACCT = "not_acct";
	
	//������Ϣ������Զ��������ֹ�༭�ļ���
	String NOT_OTHER = "not_other";
	
	
	//C15��Ϊfb_p_detail_mx�������ֶ�
	public static String PRJ_DETAIL_MX_ORDER = "C15";
	
	//ѡ��
	int PRJ_CONTROL_SELECTED = 0 ;
	//δѡ��
	int PRJ_CONTROL_UN_SELECTED = 1 ;
	
	//======Ϊ��ģ��prjreturn������,���Ᵽ�����ֶ�,�����Ժ�ɾ��: ��ʼ===//
	/**Ԥ�㴦������*/
	String YUSUANCHUSHYJ = "C11";
	/**ҵ����������*/
	String YWCSSHYJ = "C12";
	/**��λ������*/
	String DWSHYJ = "C13";
	/**������*/
	String PIFUZHE = "N2" ;
	String READY_PROJECT = "00";// Ԥ����Ŀ
	String IN_PROJECT = "01";// ����Ԥ��
	String WAIT_PROJECT = "02";// Ԥѡ����Ŀ
	String OUT_PROJECT = "03";// ��̭��Ŀ
	String FOR_AUDIT_PROJECT = "04";// ������Ŀ
	String FOR_CHOOSE_PROJECT = "05";// ��ѡ��Ŀ
	String CHOOSED_PROJECT = "06";// �Ѷ���Ŀ
	//======Ϊ��ģ��prjreturn������,���Ᵽ�����ֶ�: ����===//
	
	//=========����״̬=========//
	//���
	int VIEW = -1;
	//����
	int NEW = 0;
	//�޸�
	int EDIT = 1;
	//ɾ��
	int DELETE = 2;
	//����
	int SAVE = 3;
	//ȡ��
	int CANCLE = 4;
	//��ӡ
	int PRINT = 5;
	//��������
	int NEW_SAVE = 6;
	//�޸ı���
	int EDIT_SAVE = 7;
	
	//=========�û�����=========//
	//��λ�û�
	int DIVISION_USER = 0;
	//�����û�
	int FINANCE_USER = 1 ;
	
	//=========��Ŀ��ϸ���̶�=========//
	//������ϸ
	int NO_DETAIL = 1 ;
	
	//ֻ��д�����ÿ�Ŀ����Ŀ�������ݵ���Ŀ��ϸԤ��
	int XMFY_DETAIL = 2 ;
	
	//ֻ��д�����ÿ�Ŀ���ʽ���Դ����Ŀ��ϸԤ��
	int ZJLY_DETAIL = 3 ;
	
	//���߶���
	int TOTAL_DETAIL = 4 ;
	
	
	//=========��Ŀ���ÿ�Ŀ��Χ=========//
	//����Ŀ��ϸ֧��
	int PRJ_ACCT_JJ = 1 ;
	
	//���о��ÿ�Ŀ
	int TOTAL_ACCT_JJ = 2 ;
	
	//=========����������=========//
	//��Ԥ��
	int TOTAL_BUDGET_TYPE = 1 ;
	
	//����Ԥ��
	int CURRENT_BUDGET_TYPE = 2 ;
	
	//����Ԥ����ϸ
	int CURRENT_BUDGET_DETAIL_TYPE = 11 ;
	
    //����������������ʡ����
	int CZHY_DETAIL_TYPE = 3 ;
	
	//=========��������=========//
	//���־���
	int BUMBER_SCALE = 2 ;
	
	String LVL_ID = "LVL_ID";
	String PAR_ID = "PAR_ID";
	String PFS_NAME = "PFS_NAME";
	String PFS_ENAME = "PFS_ENAME";
	String PFS_FNAME = "PFS_FNAME" ;
	String DISPLAY_FORMAT = "DISPLAY_FORMAT" ;
	String FIELD_ID = "FIELD_ID" ;
	
	//=========��ϸ�����Ͷ���=========//
	//��Ŀ��Ԥ��
	String LINE_MAST_BUDGET_NAME = "��Ŀ��Ԥ��" ;
	int LINE_MAST_BUDGET = 1 ;
	
	//����Ԥ��
	String LINE_CUR_BUDGET_NAME = "����Ԥ��" ;
	int LINE_CUR_BUDGET = 2 ;
	
	//����Ԥ����ϸ
	int LINE_CUR_BUDGET_DETAIL = 11 ;
	
	// �����ʲ�
	String TAB_TITLE_ASSET = "�ʲ�����Ԥ���";

	// �ʲ�����������
	String TAB_TITLE_ASSETSTOCK = "�ʲ����������";
	
    //�����ʲ�(�Ͼ�����Ŀ��
	String C7 = "c7";
}

