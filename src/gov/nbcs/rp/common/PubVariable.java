package gov.nbcs.rp.common;
/**
 * @author 谢昀荪
 *
 * @version 创建时间：Apr 10, 20121:51:54 PM
 *
 * @Description
 */

public interface PubVariable {

	//=========项目录入界面标签名称=========//
	//查找按钮图片路径
	String IMG_PATH_QUERY = "images/fbudget/query.gif" ;
	
	//专项基本信息及总预算
	String TAB_TITLE_BASIC = "专项基本信息及总预算" ;
	
	//预算明细及总预算
	String SUB_TAB_TITLE_BUDGET = "预算明细及总预算" ;
	
	//专项主要内容，申报理由及可行性论证
	String SUB_TAB_TITLE_INFO = "专项内容简介" ;
	
	//财政审核意见
	String SUB_TAB_TITLE_OPINION = "财政审核意见" ;
	    
	//项目附件
	String TAB_TITLE_DOC = "专项附件" ;
	
	//专项列表
	String TAB_TITLE_LIST = "专项列表" ;
	
	//政府采购
	String TAB_TITLE_GOV = "政府采购预算表" ;
	
	//=========面板标题=========//
	//本年预算及总预算
	String PANEL_TITLE_FOUNDS = "专项明细预算（按资金来源）" ;
	
	//专项明细预算（按专项类别）
	String PANEL_TITLE_ITEM = "专项明细预算（按费用明细）" ;
	
	//附件列表
	String PANEL_TITLE_DOC_LIST = "附件列表" ;
	
	//附件简介
	String PANEL_TITLE_DOC_INFO = "附件简介" ;
	
	//=========树根节点标题=========//
	String TREE_ROOT_NAME_DIV = "单位列表" ;
	String TREE_ROOT_NAME_PRJ = "专项列表" ;
	
	//=========项目基本信息标签=========//
	String LABEL_BASE_INFO_CATEGORY = "专项类别：" ;
	String LABEL_BASE_INFO_PRJ_CODE = "专项编码：" ;
	String LABEL_BASE_INFO_SORT_CODE = "序号：" ;
	String LABEL_BASE_INFO_PRJ_NAME = "专项名称：" ;
	String LABEL_BASE_INFO_PRJ_NAME_MX = "专项明细名称：" ;
	String LABEL_BASE_INFO_SUBJECT = "功能科目：" ;
	String LABEL_BASE_INFO_STANDARD_CATALOG = "标准目录：" ;
	String LABEL_BASE_INFO_IS_GOV_PROC = "含有政府采购" ;
	String LABEL_BASE_INFO_START_YEAR = "起始年份：" ;
	String LABEL_BASE_INFO_END_YEAR = "结束年份：" ;
	String LABEL_BASE_INFO_PRJ_STATE = "项目状态：" ;
	
	//=========按钮文字=========//
	String BUTTON_LABEL_MODIFY_CAPITAL_SOURCE = "专项资金设置" ;
	String BUTTON_LABEL_MODIFY_SUBJECT = "修改功能科目" ;
	String BUTTON_LABEL_MODIFY_ECONOMY_SUBJECT = "修改经济科目" ;
	String BUTTON_LABEL_MODIFY_DELETE_DETAIL_ROW = "删除明细记录" ;
	
	String BUTTON_LABEL_MODIFY_PRJ_GOV = "采购目录" ;
	String BUTTON_LABEL_SAVE_PRJ_GOV = "保  存" ;
	String BUTTON_LABEL_EDIT_PRJ_GOV = "修   改" ;
	String BUTTON_LABEL_CANCLE_PRJ_GOV = "取   消" ;
	
	String BUTTON_LABEL_ADD = "增加" ;
	String BUTTON_LABEL_MODIFY = "修改" ;
	String BUTTON_LABEL_DELETE = "删除" ;
	String BUTTON_LABEL_SAVE = "保存" ;
	String BUTTON_LABEL_CANCEL = "取消" ;
	String BUTTON_LABEL_VIEW = "查看" ;
	String BUTTON_LABEL_SAVE_AS = "另存" ;
	String BUTTON_LABEL_PFS_SET = "专项资金设置" ;
	String BUTTON_LABEL_PRJ_PRINT = "项目申报书" ;
	
	String LABEL_DOC_INFO_SORT_NUM = "序号：" ;
	String LABEL_DOC_INFO_NAME = "名称：" ;
	String LABEL_DOC_INFO_FILE = "文件：" ;
	
	//必填项的标签颜色
	String LABEL_BASE_INFO_REQUIRE_COLOR = "#ff0000" ;
	//附件大小上限
	long DOC_MAX_LENGTH = 5*1024*1024 ;
	//基本信息控件（如文本框，下拉框等）的高度
	int BASE_INFO_COMPONENT_HIGHT = 23 ;
	
	//=========与服务段sql字段相对应=========//
	String CHR_ID = "chr_id" ;
	String CHR_CODE = "chr_code" ;
	String CHR_NAME = "chr_name" ;
	String CHR_IS_LEAF = "chr_is_leaf" ;
	String PARENT_ID = "parent_id" ;
	
	String CODE_SYBLE_BEGIN = "[" ;
	String CODE_SYBLE_END = "]" ;

	//分项目费用列非空标志
	int ITEM_COL_NOT_NULL = 1 ;
	
	String FIELD_CODE = "field_code" ;
	String FIELD_ENAME = "field_ename" ;
	String FIELD_CNAME = "field_cname" ;
	String FIELD_FNAME = "field_fname" ;
	String NOT_NULL = "notnull" ;
	//ROW_ID
	String ROW_ID = "row_id" ;
	
	//预算年度
	String SET_YEAR = "set_year" ;
	
	//审核批次
	String BATCH_NO = "batch_no" ;
	
	//数据类型
	String DATA_TYPE = "data_type" ;
	
	//单位流水号
	String EN_ID = "en_id" ;
	
	//单位编码
	String DIV_CODE = "div_code" ;
	
	//单位名称
	String DIV_NAME = "div_name" ;
	
	//单位类型
	String DIV_KIND = "div_kind" ;
	
	//单位是否支持多科目
	String ACCT_MUT = "acct_mut" ;
	//区划编码
	String RG_CODE = "rg_code" ;
	
	//上次修改时间
	String LAST_VER = "last_ver" ;
	
	//项目类别编码
	String PRJSORT_CODE = "prjsort_code" ;
	
	//项目类别名称
	String PRJSORT_NAME = "prjsort_name" ;
	
	//项目编码
	String PRJ_CODE = "prj_code" ;
	
	//序号
	String SORT_DIV = "sort_div" ;
	
	//项目名称
	String PRJ_NAME = "prj_name" ;
	
	//经济科目流水号
	String BSI_ID = "bsi_id" ;
	
	//经济科目编码
	String ACCT_CODE_JJ = "acct_code_jj" ;
	
	//经济科目名称
	String ACCT_NAME_JJ = "acct_name_jj" ;
	
	//经济科目全称
	String ECON_FULL_NAME = "econ_full_name" ;
	
	//功能科目流水号
	String BS_ID = "bs_id" ;
	
	//功能科目编码
	String ACCT_CODE = "acct_code" ;
	
	//功能科目名称
	String ACCT_NAME = "acct_name" ;
	
	//功能科目全称
	String ACCT_FULL_NAME = "acct_full_name" ;
	
	//标准目录编码
	String PRJSTD_CODE = "prjstd_code" ;
	
	//标准目录名称
	String PRJSTD_NAME = "prjstd_name" ;
	
	//是否政府采购
	String STOCK_FLAG = "stock_flag" ;
	
	//起始年份
	String START_DATE = "start_date" ;
	
	//结束年份
	String END_DATE = "end_date" ;
	
	//专项状态编码
	String PRJ_STATUS = "prj_status" ;
	
	//专项状态名称
	String PRJ_STATUS_NAME = "prj_status_name" ;
	
	//项目主要内容，申报理由及可行性论证
	String PRJ_CONTENT = "prj_content" ;
	
	//财政审核意见
	String CL3 = "cl3" ;
	
	//项目明细化程度
	String DETAIL_DEGREE = "detail_degree" ;
	//项目设置的录入属性
	String INPUT_SET_ID = "input_set_id" ;
	
	//项目文档名称
	String AFFIX_TITLE = "affix_title" ;
	
	//项目文档类型
	String AFFIX_TYPE = "affix_type" ;
	
	//项目文档全名称
	String AFFIX_FILE = "affix_file" ;
	
	//项目文档序号
	String AFFIX_SORT = "sort_id" ;
	
	//项目文档路径
	String AFFIX_FILE_PATH = "path" ;
	
	//项目文档是否原始数据库记录
	String AFFIX_FILE_DATABASE = "affix_file_database" ;
	
	//项目文档操作状态
	String AFFIX_OPER_STATE = "oper_state" ;
	
	//项目文档更新时间
	String AFFIX_UPDATE = "upddate" ;
	
	//经济科目范围
	String ECONOMY_SCOPE = "jjtype" ;
	
	//行类型编码
	String DETAIL_TYPE = "detail_type" ;
	
	//行类型名称
	String DETAIL_TYPE_NAME = "detail_type_name" ;
	
	//数据来源
	String DATA_SOURCE = "data_source" ;
	
	//是否末级标志
	String END_FLAG = "end_flag" ;
	
	//
	String DATA_SRC = "data_src" ;
	
	//
	String DATA_ATTR = "data_attr";
	
	//
	String PRJ_TYPE = "prj_type" ;
	
	//作为"总计"的资金来源
	String TOTAL_PRICE_ENAME = "total_prices" ;
	
	//项目录入控制表字段
	//项目状态值
	String PRJ_STATE_VALUE = "state_value";
	
	//项目可否删除
	String PRJ_CAN_DELETE = "can_delete";
	
	//项目可否编辑
	String PRJ_CAN_EDIT = "can_edit" ;
	
	//禁止编辑的资金来源集合，英文名以“,”分隔
	String NOT_PFS = "not_pfs" ;
	
	//项目名称可否编辑:0不可，1可以
	String PRJ_NAME_EDITABLE = "not_prj_name";
	
	//项目科目可否编辑（功能科目和经济科目）:0不可，1可以
	String NOT_ACCT = "not_acct";
	
	//基本信息面板上自定义组件禁止编辑的集合
	String NOT_OTHER = "not_other";
	
	
	//C15作为fb_p_detail_mx的排序字段
	public static String PRJ_DETAIL_MX_ORDER = "C15";
	
	//选择
	int PRJ_CONTROL_SELECTED = 0 ;
	//未选择
	int PRJ_CONTROL_UN_SELECTED = 1 ;
	
	//======为了模块prjreturn的依赖,特意保留的字段,建议以后删除: 开始===//
	/**预算处审核意见*/
	String YUSUANCHUSHYJ = "C11";
	/**业务处室审核意见*/
	String YWCSSHYJ = "C12";
	/**单位审核意见*/
	String DWSHYJ = "C13";
	/**批复者*/
	String PIFUZHE = "N2" ;
	String READY_PROJECT = "00";// 预定项目
	String IN_PROJECT = "01";// 纳入预算
	String WAIT_PROJECT = "02";// 预选定项目
	String OUT_PROJECT = "03";// 淘汰项目
	String FOR_AUDIT_PROJECT = "04";// 待审项目
	String FOR_CHOOSE_PROJECT = "05";// 备选项目
	String CHOOSED_PROJECT = "06";// 已定项目
	//======为了模块prjreturn的依赖,特意保留的字段: 结束===//
	
	//=========操作状态=========//
	//浏览
	int VIEW = -1;
	//新增
	int NEW = 0;
	//修改
	int EDIT = 1;
	//删除
	int DELETE = 2;
	//保存
	int SAVE = 3;
	//取消
	int CANCLE = 4;
	//打印
	int PRINT = 5;
	//新增保存
	int NEW_SAVE = 6;
	//修改保存
	int EDIT_SAVE = 7;
	
	//=========用户类型=========//
	//单位用户
	int DIVISION_USER = 0;
	//财政用户
	int FINANCE_USER = 1 ;
	
	//=========项目明细化程度=========//
	//不填明细
	int NO_DETAIL = 1 ;
	
	//只填写按经济科目分项目费用内容的项目明细预算
	int XMFY_DETAIL = 2 ;
	
	//只填写按经济科目分资金来源的项目明细预算
	int ZJLY_DETAIL = 3 ;
	
	//两者都填
	int TOTAL_DETAIL = 4 ;
	
	
	//=========项目经济科目范围=========//
	//按项目明细支出
	int PRJ_ACCT_JJ = 1 ;
	
	//所有经济科目
	int TOTAL_ACCT_JJ = 2 ;
	
	//=========表格的行类型=========//
	//总预算
	int TOTAL_BUDGET_TYPE = 1 ;
	
	//本年预算
	int CURRENT_BUDGET_TYPE = 2 ;
	
	//本年预算明细
	int CURRENT_BUDGET_DETAIL_TYPE = 11 ;
	
    //财政建议数（江苏省厅）
	int CZHY_DETAIL_TYPE = 3 ;
	
	//=========其他定义=========//
	//数字精度
	int BUMBER_SCALE = 2 ;
	
	String LVL_ID = "LVL_ID";
	String PAR_ID = "PAR_ID";
	String PFS_NAME = "PFS_NAME";
	String PFS_ENAME = "PFS_ENAME";
	String PFS_FNAME = "PFS_FNAME" ;
	String DISPLAY_FORMAT = "DISPLAY_FORMAT" ;
	String FIELD_ID = "FIELD_ID" ;
	
	//=========明细行类型定义=========//
	//项目总预算
	String LINE_MAST_BUDGET_NAME = "项目总预算" ;
	int LINE_MAST_BUDGET = 1 ;
	
	//本年预算
	String LINE_CUR_BUDGET_NAME = "本年预算" ;
	int LINE_CUR_BUDGET = 2 ;
	
	//本年预算明细
	int LINE_CUR_BUDGET_DETAIL = 11 ;
	
	// 新增资产
	String TAB_TITLE_ASSET = "资产配置预算表";

	// 资产存量情量表
	String TAB_TITLE_ASSETSTOCK = "资产存量情况表";
	
    //新增资产(南京市项目）
	String C7 = "c7";
}

