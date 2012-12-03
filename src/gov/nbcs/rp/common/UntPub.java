/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class UntPub. ��Ҫ���ڴ洢���ೣ��
 */
public class UntPub {
	// //ȫ�ֳ�������/////////////////////////
	/** The Constant GS_SYS_ID. ����Ԥ��ҵ����ϵͳ�ı�� */
	public static final String GS_SYS_ID = "101";

	/** The Constant GS_FORMULA_ENABLEINPUT. ͨ�ù�ʽ��ʶ-��¼�� */
	public static final String GS_FORMULA_ENABLEINPUT = "@00001";

	/** The Constant GS_FORMULA_DISABLEINPUT. ͨ�ù�ʽ��ʶ-��¼�� */
	public static final String GS_FORMULA_DISABLEINPUT = "@00002";

	/** The Constant GS_FORMULA_NULL. ͨ�ù�ʽ��ʶ-������¼����λ����¼ */
	public static final String GS_FORMULA_NULL = "@00003";

	/** The Constant GS_FORMULALEFT. ��ʽԪ����ָ�� */
	public static final String GS_FORMULALEFT = "{";

	/** The Constant GS_FORMULARIGHT. ��ʽԪ���ҷָ�� */
	public static final String GS_FORMULARIGHT = "}";

	/** The Constant GS_FORMULALEFT_RIGHT. ����Ԫ�ع�ʽ���壬��Ҫ��{} */
	public static final String GS_FORMULALEFT_RIGHT = "\\{|\\}";
	// ȥ����������ʽ�е�[],ԭ���������������ͻ by ymq 08.07.08
	// public static final String GS_FORMULALEFT_RIGHT = "\\{|\\}|\\[|\\]"; //
	// ����Ԫ�ع�ʽ���壬��Ҫ��{},[]������չ��

	/** The Constant NumberFieldNamePattern. */
	public static final Pattern NumberFieldNamePattern = Pattern
			.compile("([F|f|N|n]\\D+)");

	/** The Constant refElementPattern. */
	public static final Pattern refElementPattern = Pattern
			.compile("(\\{.+\\})|(\\[.+\\])|(.*[\\D]+.*)");

	/** The Constant formulaSplitter. */
	public static final Pattern formulaSplitter = Pattern
			.compile("\\+|-|\\*|/|\\(|\\)"); // �������������ʽ

	/** The Constant matp. */
	public static final Pattern matp = Pattern
			.compile("\\{[^\\{\\}[\u4e00-\u9fa5]]+\\}"); // ƥ��������ʽ

	/** The Constant PARTITION_TAG. */
	public static final String PARTITION_TAG = "."; // ȫ���ָ����

	// ������Դ
	/** The Constant DATA_SRC_PAYOUT. */
	public static final int DATA_SRC_PAYOUT = 0; // ֧��Ԥ��������Դ: ¼��

	/** The Constant DATA_CALC_PAYOUT. */
	public static final int DATA_CALC_PAYOUT = 50; // ֧��Ԥ��������Դ: �Զ�����

	/** The Constant DATA_SRC_PROJECT. */
	public static final int DATA_SRC_PROJECT = 150; // ֧��Ԥ��������Դ: ��Ŀ

	// ����֧��Ԥ������ģʽ��־
	/** The Constant BASE_PAYOUT_CALC. */
	public static final int BASE_PAYOUT_CALC = 10; // �Զ��������� ��Ӧ������Դ 50

	/** The Constant BASE_PAYOUT_INPUT. */
	public static final int BASE_PAYOUT_INPUT = 20; // �û�¼�� ��Ӧ������Դ 0

	/** The Constant PRJAUDIT_REPORTID. */
	public static final String PRJAUDIT_REPORTID = "P00001"; // ��Ŀ���� �� ��ͼ��һ��

	// ��������
	/** The Constant IR_REPORT_BASE. ������Ϣ¼��� */
	public static final int IR_REPORT_BASE = 00; // ������Ϣ¼��� ��00 0000��ʼ��00

	/** The Constant IR_REPORT_KIND_INPUT. �û��Զ���¼��� */
	public static final int IR_REPORT_KIND_INPUT = 10; // �û��Զ���¼��� 10 0000��ʼ��10

	/** The Constant IR_REPORT_KIND_DIC. �û��Զ����ֵ� */
	public static final int IR_REPORT_KIND_DIC = 20; // �û��Զ����ֵ� 20 0000��ʼ��20

	/** The Constant IR_REPORT_KIND_CUS. �̶����Զ���¼��� */
	public static final int IR_REPORT_KIND_CUS = 30; // �̶����Զ���¼��� 30
	// 0000��ʼ��30

	/** The Constant IR_REPORT_KIND_NULL. ���ñ�׼¼��� */
	public static final int IR_REPORT_KIND_NULL = 40; // ���ñ�׼¼��� 40 0000��ʼ��40

	/** The Constant IR_REPORT_QR. ��ѯ���ɱ� */
	public static final int IR_REPORT_QR = 50; // ��ѯ���ɱ� 50 0000�Ժ� ֧�ֲַ�

	// Ӧ�÷�Χ��ʶ
	/** The Constant DIV_USER. */
	public static final int DIV_USER = 0; // ��λʹ��

	/** The Constant FIS_USER. */
	public static final int FIS_USER = 1; // ����ʹ��

	/** The Constant ALL_USER. */
	public static final int ALL_USER = 2; // �����͵�λ��ͬʹ��

	// ��¼�û����ͱ�ʶ ���ֺ�ƽ̨���õ�����һ�� ��ӦBelongTYpe
	/** The Constant FIS_VIS. */
	public static final String FIS_VIS = "007"; // �����û�

	/** The Constant DIV_VIS. */
	public static final String DIV_VIS = "002"; // ��λ�û�

	// ��������
	/** The Constant DIV_DataType. */
	public static final int DIV_DataType = 0; // ԭʼ��

	/** The Constant FIS_DataType. */
	public static final int FIS_DataType = 1; // �����

	// ��׼�ʽ���Դ����
	/** The Constant GS_FSTOTALTYPE. */
	public static final String GS_FSTOTALTYPE = "001"; // �ʽ���Դ�ܺϼ����ͱ�ʶ

	/** The Constant GS_FSFISCLTYPE. */
	public static final String GS_FSFISCLTYPE = "002"; // �ʽ���Դ�����������ͱ�ʶ

	/** The Constant GS_SUBSIDYTYPE. */
	public static final String GS_SUBSIDYTYPE = "003"; // �ϼ��������ͱ�ʶ

	// ��׼֧���������
	/** The Constant GS_PAYOUTBAS. */
	public static final String GS_PAYOUTBAS = "001"; // ����֧��

	/** The Constant GS_PAYOUTPUB. */
	public static final String GS_PAYOUTPUB = "002"; // ����֧��

	/** The Constant GS_PAYOUTPRJ. */
	public static final String GS_PAYOUTPRJ = "003"; // ר��֧��

	/** The Constant HEADER_FONT. */
	public static final Font HEADER_FONT = new Font("����", Font.PLAIN, 12);

	/** The Constant HEADER_COLOR. */
	public static final Color HEADER_COLOR = new Color(250, 228, 184); // ��ͷ��ɫ

	// ///////////////////////////////////////////////////////////////////////////
	// //ȫ�ֱ������� //////////////////////////
	/** The lvl rule. */
	public static SysCodeRule lvlRule = SysCodeRule.createClient(new int[] { 4,
			8, 12, 16, 20, 24, 28, 32, 36, 40 }); // ��������

	/** The Constant codeRule. */
	public static final SysCodeRule codeRule = SysCodeRule
			.createClient(new int[] { 3, 6, 9, 12, 15, 18, 21, 24, 27, 30 }); // fb_b_set_colset�������

	// public static SysCodeRule REPORTRule = SysCodeRule.createClient(new int[]
	// {
	// 6, 10, 14, 18, 22, 26, 30 }); // ����������

	/** The Constant ZERO_DECIMAL. */
	public static final BigDecimal ZERO_DECIMAL = new BigDecimal(0.0);
	
	/** The Constant FIELD_DATA_TYPE_CHAR. */
	public static final String FIELD_DATA_TYPE_CHAR = "�ַ���";
	
	/** The Constant FIELD_DATA_TYPE_DATE. */
	public static final String FIELD_DATA_TYPE_DATE = "������";
	
	/** The Constant FIELD_DATA_TYPE_INT. */
	public static final String FIELD_DATA_TYPE_INT = "����";	
	
	/** The Constant FIELD_DATA_TYPE_FLOAT. */
	public static final String FIELD_DATA_TYPE_FLOAT = "������";	
	
	/** The Constant FIELD_EDIT_FORMAT_LONGCHAR. */
	public static final String FIELD_EDIT_FORMAT_LONGCHAR = "���ַ�";		
	
	/**
	 * The Class RefElementType. ����Ԫ������
	 */
	public static class RefElementType {

		/** The Constant retPubStandard. */
		public static final String retPubStandard = "01"; // ������׼

		/** The Constant retRationStandard. */
		public static final String retRationStandard = "02"; // �����׼

		/** The Constant retBaseData. */
		public static final String retBaseData = "03"; // ��������

		/** The Constant retPubFormula. */
		public static final String retPubFormula = "04"; // ��׼���ù�ʽ

		/** The Constant retSpecFormula. */
		public static final String retSpecFormula = "05"; // ���⹫�ù�ʽ
	}

	/**
	 * The Class AuditKind. �������
	 */
	public static class AuditKind {

		/** The Constant akBalanceRelation. */
		public static final int akBalanceRelation = 1; // ������ϵ���

		/** The Constant akDataNotNull. */
		public static final int akDataNotNull = 2; // �����������

		/** The Constant akDailyForPublicUse. */
		public static final int akDailyForPublicUse = 3;// �ճ��������

		/** The Constant akControlFigures. */
		public static final int akControlFigures = 4; // ���������
	}

	/**
	 * The Class InputTableName. ¼������ݿ����
	 */
	public static final class InputTableName {

		/** The Constant ITN_INCOMING_BUDGET. ����Ԥ��¼������ݿ���� */
		public static final String ITN_INCOMING_BUDGET = "FB_U_DIV_INCOMING_BUDGET";

		/** The Constant ITN_INCOMING. Ԥ�����ʽ�����Ԥ��¼������ݿ���� */
		public static final String ITN_INCOMING = "FB_U_DIV_INCOMING";

		/** The Constant IT_PAYOUT_BUDGET. ֧��Ԥ��¼������ݿ���� */
		public static final String ITN_PAYOUT_BUDGET = "VW_FB_U_PAYOUT_BUDGET";

		/** The Constant ITN_PAYOUT_GOV_PURCHASE. �����ɹ�Ԥ������ݿ���� */
		public static final String ITN_PAYOUT_GOV_PURCHASE = "FB_U_PAYOUT_GOV_PURCHASE";

	}

	/**
	 * The Class SupportType. ��FB_S_SUPPORT��SUPTYPE�е�ӳ��
	 */
	public static final class SupportType {

		/** The Constant CUR_FISC. ��ǰ�û��� ���磺ĳĳ������ */
		public static final String CUR_FISC = "CURFISC";

		/** The Constant BUDGET_YEAR. Ԥ����� */
		public static final String BUDGET_YEAR = "BUDGET_YEAR";

		/** The Constant GOV_LEVEL. �����ɹ��������� 1�����Ƶ���Ŀ��0�����Ƶ���Ŀ��ϸ�ľ��ÿ�Ŀ */
		public static final String GOV_LEVEL = "GOVLEVEL";

		/** The Constant BASE_JOIN. ������Ϣ���㿪�� 0 ��������㣬 1 ������� */
		public static final String BASE_JOIN = "BASEJOIN";

		/** The Constant BASE_PAYOUT. ����֧������Ʒ����֧����֧��Ԥ���ķ�ʽ��10�������Զ��20���ֹ�¼�� */
		public static final String BASE_PAYOUT = "BASEPAYOUT";

		/** The Constant PAYOUT_INNER_AUDIT ֧������������ˡ���Ĭ��0����������1��������. */
		public static final String PAYOUT_INNER_AUDIT = "PAYOUT_INNER_AUDIT";

		/** The Constant PAYOUT_LIMITATION_AUDIT ֧��������޶���ˡ���Ĭ��0����������1��������. */
		public static final String PAYOUT_LIMITATION_AUDIT = "PAYOUT_LIMITATION_AUDIT";

		/** The Constant DIV_SHOW_SH. ��λ�Ƿ���ʾ�������1����ʾ��0������ʾ */
		public static final String DIV_SHOW_SH = "DIVSHOWSH";

		/** The Constant EDIT_STRU_LOCK. 0��ʾ�ṹ���ÿ���1��ʾ�ṹ���ùء� */
		public static final String EDIT_STRU_LOCK = "EDITSTRULOCK";

		/** The Constant PRJ_ACCTJJ_LVL. ��Ŀ���о��ÿ�Ŀ֧�ֵļ��� */
		public static final String PRJ_ACCTJJ_LVL = "PRJ_ACCTJJ_LVL";

		/** The Constant SERVER_TYPE. ����������:weblogic,tomcat,�������߸���blob������������Ŀ������ */
		public static final String SERVER_TYPE = "SERVERTYPE";

		/** The Constant CF_CONTROL. 1�������õ�λ���ƣ�2�����ô��Һ͵�λ���ƣ�3�������ÿ������� */
		public static final String CF_CONTROL = "CFCONTROL";

		/** The Constant PRJ_RATING. ��Ŀ���ƶ�� */
		public static final String PRJ_RATING = "PRJRATING";

		/** The Constant USE_BSE_AUDIT. ������Ϣ����Ƿ����ü��㣬0 �����ã�1���� */
		public static final String USE_BSE_AUDIT = "USEBSEAUDIT";

		/** The Constant PRJ_SHOW_STD. ��Ŀ��׼Ŀ¼ 1�����ã�0�������� */
		public static final String PRJ_SHOW_STD = "PRJSHOWSTD";

		/** The Constant PAYOUT_2_INCOMINGBUDGET ֧��Ԥ�����������Ԥ���0�������ã�1������. */
		public static final String PAYOUT_2_INCOMINGBUDGET = "PAYOUT2INCOMINGBUDGET";

		/** The Constant SHOW_ACCT_JJ. ֧������Ŀ���ý��澭�ÿ�Ŀ��0:���У�1:��ʽ�Ե�λ���в����ڵ� */
		public static final String SHOW_ACCT_JJ = "SHOW_ACCT_JJ";

		/** The Constant PAYOUT_ITEM_EDIT_INI. ֧������Ŀ����ʱ�Ƿ����༭״̬��0��1�� */
		public static final String PAYOUT_ITEM_EDIT_INI = "PAYOUT_ITEM_EDIT_INI";

		/** The Constant PAYOUT_CLEAN_EMPTY_ROW. �Ƿ��ڱ���֧��Ԥ���ʱɾ���ϼ�ֵΪ0���С���0:��ɾ����1:ɾ�� */
		public static final String PAYOUT_CLEAN_EMPTY_ROW = "PAYOUT_CLEAN_EMPTY_ROW";

		/** The Constant QUOTA_SHOW. 0�����֡���ʾ�޶��ť��1���޶���ʾ������·� */
		public static final String QUOTA_SHOW = "QUOTA_SHOW";

		/**
		 * The Constant SHOW_ONLY_DAILY_PAYOUT.
		 * 0:֧������Ŀ���ý�����ʾ����֧����Ŀ��1ֻ��ʾ����֧���µ��ճ�����֧��
		 */
		public static final String SHOW_ONLY_DAILY_PAYOUT = "SHOW_ONLY_DAILY_PAYOUT";

		/**
		 * The Constant NEED_INSERT_DAILY_PAYOUT.
		 * 0:֧�������û��302��¼���Զ����룬1�Զ���������302��¼
		 */
		public static final String NEED_INSERT_DAILY_PAYOUT = "NEED_INSERT_DAILY_PAYOUT";

		/** The Constant AUTO_DISTR_PFS. 0:֧�����ʽ���䡱��ť���أ�1��ʾ */
		public static final String AUTO_DISTR_PFS = "AUTO_DISTR_PFS";

		/** The Constant CUR_STATE. ϵͳ��ǰ״̬���ɹ�����ص����� */
		public static final String CUR_STATE = "CUR_STATE";

		/** The Constant SHOW_TREE. 0�����ÿ�Ŀ���շ���Ŀ��1�������շ���Ŀ�Կ�Ŀ */
		public static final String SHOW_TREE = "SHOWTREE";

		/** The Constant SERIAL_NO. ����Ԥ��ϵͳ֧����֤�� */
		public static final String SERIAL_NO = "SERIALNO";
		
		/** The Constant PAYOUT_ITEM_NAME_INPUT. ��Ŀ֧���������¼���� */
		public static final String PAYOUT_ITEM_NAME_INPUT = "PAYOUT_ITEM_NAME_INPUT";
		
		/** ֧���������ĳ�ʼֵ��-1�����ƣ�0����Ϊ0. */
		public static final String PAYOUT_CTRLFUND_INIT_VALUE = "PAYOUT_CTRLFUND_INIT_VALUE";
		
		/** The Constant AUDIT_LOG_RECORD_SWITCH. */
		public static final String AUDIT_LOG_RECORD_SWITCH = "AUDIT_LOG_RECORD_SWITCH";

		/** The Constant HIGH_LIGHT_CALC_CELL. */
		public static final String HIGH_LIGHT_CALC_CELL = "HIGH_LIGHT_CALC_CELL";

		/** The Constant WORKFLOW_ENABLED. */
		public static final String WORKFLOW_ENABLED = "WORKFLOW_ENABLED";
	}
	
	/**
	 * The Class PayoutItemNameInputOption.
	 */
	public static final class PayoutItemNameInputOption {
		
		/** The Constant only_name. �޷���,�����ֶ�¼�� */
		public static final int ONLY_NAME = 0;
		
		/** The Constant select_kind_name. ����/���ƾ�ѡ��¼�� */
		public static final int SELECT_KIND_NAME = 1;
		
		/** The Constant input_name_only. ����ѡ��¼��,�����ֶ�¼�� */
		public static final int SELECT_KIND_ONLY = 2;	
	}
	
	/** The Constant STATUS_999: 001|004 ��ʼ״̬. */
	public static final String STATUS_999 = "{7B35E12E-875F-425F-BD19-AACE4403CFFD}";
	/** The Constant STATUS_000: ȫ��. */
	public static final String STATUS_000 = "{8A98726A-81D8-4A94-9893-CA54C448D68E}";
	/** The Constant STATUS_001: δȷ��. */
	public static final String STATUS_001 = "{26C5597A-02B0-4B0A-8E5E-C3F30612F202}";
	/** The Constant STATUS_002: ��ȷ��. */
	public static final String STATUS_002 = "{91FA49E4-9630-423B-8025-99CAD5ADEF50}";
	/** The Constant STATUS_003: ���˻�. */
	public static final String STATUS_003 = "{610449EB-78CC-4269-9B69-1F8FB8408CFF}";
	/** The Constant STATUS_004: ���˻�. */
	public static final String STATUS_004 = "{8FEDC3D1-3768-457A-9BF8-1C3319989893}";
	/** The Constant STATUS_005: ���޸�. */
	public static final String STATUS_005 = "{EF6AE69E-75A4-4FEE-8D7A-7FE86774AADC}";
	/** The Constant STATUS_008: ������. */
	public static final String STATUS_008 = "{47379426-2E9D-4F02-9FF4-235020098801}";
	/** The Constant STATUS_101: �ѹ���. */
	public static final String STATUS_101 = "{FD62AD5B-A22D-404A-90BB-337F667A4001}";
	/** The Constant STATUS_102: ��ɾ��. */
	public static final String STATUS_102 = "{3EF10CA3-A1AE-4DEC-BCAD-DB1C1D2D678F}";
	/** The Constant STATUS_103: ������. */
	public static final String STATUS_103 = "{08DB04AC-FE69-4C7C-84E9-568E7970A024}";

}
