/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
 * The Class UntPub. 主要用于存储各类常量
 */
public class UntPub {
	// //全局常量定义/////////////////////////
	/** The Constant GS_SYS_ID. 部门预算业务子系统的编号 */
	public static final String GS_SYS_ID = "101";

	/** The Constant GS_FORMULA_ENABLEINPUT. 通用公式标识-可录入 */
	public static final String GS_FORMULA_ENABLEINPUT = "@00001";

	/** The Constant GS_FORMULA_DISABLEINPUT. 通用公式标识-不录入 */
	public static final String GS_FORMULA_DISABLEINPUT = "@00002";

	/** The Constant GS_FORMULA_NULL. 通用公式标识-财政可录，单位不可录 */
	public static final String GS_FORMULA_NULL = "@00003";

	/** The Constant GS_FORMULALEFT. 公式元素左分割符 */
	public static final String GS_FORMULALEFT = "{";

	/** The Constant GS_FORMULARIGHT. 公式元素右分割符 */
	public static final String GS_FORMULARIGHT = "}";

	/** The Constant GS_FORMULALEFT_RIGHT. 引用元素公式定义，组要用{} */
	public static final String GS_FORMULALEFT_RIGHT = "\\{|\\}";
	// 去掉了正则表达式中的[],原因是与计算条件冲突 by ymq 08.07.08
	// public static final String GS_FORMULALEFT_RIGHT = "\\{|\\}|\\[|\\]"; //
	// 引用元素公式定义，组要用{},[]留用扩展。

	/** The Constant NumberFieldNamePattern. */
	public static final Pattern NumberFieldNamePattern = Pattern
			.compile("([F|f|N|n]\\D+)");

	/** The Constant refElementPattern. */
	public static final Pattern refElementPattern = Pattern
			.compile("(\\{.+\\})|(\\[.+\\])|(.*[\\D]+.*)");

	/** The Constant formulaSplitter. */
	public static final Pattern formulaSplitter = Pattern
			.compile("\\+|-|\\*|/|\\(|\\)"); // 计算符号正则表达式

	/** The Constant matp. */
	public static final Pattern matp = Pattern
			.compile("\\{[^\\{\\}[\u4e00-\u9fa5]]+\\}"); // 匹配正则表达式

	/** The Constant PARTITION_TAG. */
	public static final String PARTITION_TAG = "."; // 全名分割符号

	// 数据来源
	/** The Constant DATA_SRC_PAYOUT. */
	public static final int DATA_SRC_PAYOUT = 0; // 支出预算数据来源: 录入

	/** The Constant DATA_CALC_PAYOUT. */
	public static final int DATA_CALC_PAYOUT = 50; // 支出预算数据来源: 自动计算

	/** The Constant DATA_SRC_PROJECT. */
	public static final int DATA_SRC_PROJECT = 150; // 支出预算数据来源: 项目

	// 基本支出预算生存模式标志
	/** The Constant BASE_PAYOUT_CALC. */
	public static final int BASE_PAYOUT_CALC = 10; // 自动计算填入 对应数据来源 50

	/** The Constant BASE_PAYOUT_INPUT. */
	public static final int BASE_PAYOUT_INPUT = 20; // 用户录入 对应数据来源 0

	/** The Constant PRJAUDIT_REPORTID. */
	public static final String PRJAUDIT_REPORTID = "P00001"; // 项目编码 和 视图中一致

	// 报表类型
	/** The Constant IR_REPORT_BASE. 基础信息录入表 */
	public static final int IR_REPORT_BASE = 00; // 基础信息录入表 从00 0000开始到00

	/** The Constant IR_REPORT_KIND_INPUT. 用户自定义录入表 */
	public static final int IR_REPORT_KIND_INPUT = 10; // 用户自定义录入表 10 0000开始到10

	/** The Constant IR_REPORT_KIND_DIC. 用户自定义字典 */
	public static final int IR_REPORT_KIND_DIC = 20; // 用户自定义字典 20 0000开始到20

	/** The Constant IR_REPORT_KIND_CUS. 固定行自定义录入表 */
	public static final int IR_REPORT_KIND_CUS = 30; // 固定行自定义录入表 30
	// 0000开始到30

	/** The Constant IR_REPORT_KIND_NULL. 内置标准录入表 */
	public static final int IR_REPORT_KIND_NULL = 40; // 内置标准录入表 40 0000开始到40

	/** The Constant IR_REPORT_QR. 查询生成表 */
	public static final int IR_REPORT_QR = 50; // 查询生成表 50 0000以后 支持分层

	// 应用范围标识
	/** The Constant DIV_USER. */
	public static final int DIV_USER = 0; // 单位使用

	/** The Constant FIS_USER. */
	public static final int FIS_USER = 1; // 财政使用

	/** The Constant ALL_USER. */
	public static final int ALL_USER = 2; // 财政和单位共同使用

	// 登录用户类型标识 保持和平台配置的类型一致 对应BelongTYpe
	/** The Constant FIS_VIS. */
	public static final String FIS_VIS = "007"; // 财政用户

	/** The Constant DIV_VIS. */
	public static final String DIV_VIS = "002"; // 单位用户

	// 数据类型
	/** The Constant DIV_DataType. */
	public static final int DIV_DataType = 0; // 原始数

	/** The Constant FIS_DataType. */
	public static final int FIS_DataType = 1; // 审核数

	// 标准资金来源类型
	/** The Constant GS_FSTOTALTYPE. */
	public static final String GS_FSTOTALTYPE = "001"; // 资金来源总合计类型标识

	/** The Constant GS_FSFISCLTYPE. */
	public static final String GS_FSFISCLTYPE = "002"; // 资金来源财政补助类型标识

	/** The Constant GS_SUBSIDYTYPE. */
	public static final String GS_SUBSIDYTYPE = "003"; // 上级补助类型标识

	// 标准支出类别类型
	/** The Constant GS_PAYOUTBAS. */
	public static final String GS_PAYOUTBAS = "001"; // 基本支出

	/** The Constant GS_PAYOUTPUB. */
	public static final String GS_PAYOUTPUB = "002"; // 公用支出

	/** The Constant GS_PAYOUTPRJ. */
	public static final String GS_PAYOUTPRJ = "003"; // 专项支出

	/** The Constant HEADER_FONT. */
	public static final Font HEADER_FONT = new Font("宋体", Font.PLAIN, 12);

	/** The Constant HEADER_COLOR. */
	public static final Color HEADER_COLOR = new Color(250, 228, 184); // 表头颜色

	// ///////////////////////////////////////////////////////////////////////////
	// //全局变量定义 //////////////////////////
	/** The lvl rule. */
	public static SysCodeRule lvlRule = SysCodeRule.createClient(new int[] { 4,
			8, 12, 16, 20, 24, 28, 32, 36, 40 }); // 层次码规则

	/** The Constant codeRule. */
	public static final SysCodeRule codeRule = SysCodeRule
			.createClient(new int[] { 3, 6, 9, 12, 15, 18, 21, 24, 27, 30 }); // fb_b_set_colset编码规则

	// public static SysCodeRule REPORTRule = SysCodeRule.createClient(new int[]
	// {
	// 6, 10, 14, 18, 22, 26, 30 }); // 报表编码规则

	/** The Constant ZERO_DECIMAL. */
	public static final BigDecimal ZERO_DECIMAL = new BigDecimal(0.0);
	
	/** The Constant FIELD_DATA_TYPE_CHAR. */
	public static final String FIELD_DATA_TYPE_CHAR = "字符型";
	
	/** The Constant FIELD_DATA_TYPE_DATE. */
	public static final String FIELD_DATA_TYPE_DATE = "日期型";
	
	/** The Constant FIELD_DATA_TYPE_INT. */
	public static final String FIELD_DATA_TYPE_INT = "整型";	
	
	/** The Constant FIELD_DATA_TYPE_FLOAT. */
	public static final String FIELD_DATA_TYPE_FLOAT = "浮点型";	
	
	/** The Constant FIELD_EDIT_FORMAT_LONGCHAR. */
	public static final String FIELD_EDIT_FORMAT_LONGCHAR = "长字符";		
	
	/**
	 * The Class RefElementType. 引用元素类型
	 */
	public static class RefElementType {

		/** The Constant retPubStandard. */
		public static final String retPubStandard = "01"; // 公共标准

		/** The Constant retRationStandard. */
		public static final String retRationStandard = "02"; // 定额标准

		/** The Constant retBaseData. */
		public static final String retBaseData = "03"; // 基础数据

		/** The Constant retPubFormula. */
		public static final String retPubFormula = "04"; // 标准公用公式

		/** The Constant retSpecFormula. */
		public static final String retSpecFormula = "05"; // 特殊公用公式
	}

	/**
	 * The Class AuditKind. 审核类型
	 */
	public static class AuditKind {

		/** The Constant akBalanceRelation. */
		public static final int akBalanceRelation = 1; // 钩稽关系审核

		/** The Constant akDataNotNull. */
		public static final int akDataNotNull = 2; // 必输内容审核

		/** The Constant akDailyForPublicUse. */
		public static final int akDailyForPublicUse = 3;// 日常公用审核

		/** The Constant akControlFigures. */
		public static final int akControlFigures = 4; // 控制数审核
	}

	/**
	 * The Class InputTableName. 录入表数据库表名
	 */
	public static final class InputTableName {

		/** The Constant ITN_INCOMING_BUDGET. 收入预算录入表数据库表名 */
		public static final String ITN_INCOMING_BUDGET = "FB_U_DIV_INCOMING_BUDGET";

		/** The Constant ITN_INCOMING. 预算外资金收入预算录入表数据库表名 */
		public static final String ITN_INCOMING = "FB_U_DIV_INCOMING";

		/** The Constant IT_PAYOUT_BUDGET. 支出预算录入表数据库表名 */
		public static final String ITN_PAYOUT_BUDGET = "VW_FB_U_PAYOUT_BUDGET";

		/** The Constant ITN_PAYOUT_GOV_PURCHASE. 政府采购预算表数据库表名 */
		public static final String ITN_PAYOUT_GOV_PURCHASE = "FB_U_PAYOUT_GOV_PURCHASE";

	}

	/**
	 * The Class SupportType. 表FB_S_SUPPORT中SUPTYPE列的映射
	 */
	public static final class SupportType {

		/** The Constant CUR_FISC. 当前用户： 比如：某某财政局 */
		public static final String CUR_FISC = "CURFISC";

		/** The Constant BUDGET_YEAR. 预算年度 */
		public static final String BUDGET_YEAR = "BUDGET_YEAR";

		/** The Constant GOV_LEVEL. 政府采购控制粒度 1：控制到项目，0：控制到项目明细的经济科目 */
		public static final String GOV_LEVEL = "GOVLEVEL";

		/** The Constant BASE_JOIN. 基础信息计算开关 0 不分组计算， 1 分组计算 */
		public static final String BASE_JOIN = "BASEJOIN";

		/** The Constant BASE_PAYOUT. 基本支出和商品服务支出进支出预算表的方式：10，计算自动填，20：手工录入 */
		public static final String BASE_PAYOUT = "BASEPAYOUT";

		/** The Constant PAYOUT_INNER_AUDIT 支出表计算表内审核——默认0：不启动；1：启动；. */
		public static final String PAYOUT_INNER_AUDIT = "PAYOUT_INNER_AUDIT";

		/** The Constant PAYOUT_LIMITATION_AUDIT 支出表计算限额审核——默认0：不启动；1：启动；. */
		public static final String PAYOUT_LIMITATION_AUDIT = "PAYOUT_LIMITATION_AUDIT";

		/** The Constant DIV_SHOW_SH. 单位是否显示审核数，1：显示，0：不显示 */
		public static final String DIV_SHOW_SH = "DIVSHOWSH";

		/** The Constant EDIT_STRU_LOCK. 0表示结构配置开，1表示结构配置关。 */
		public static final String EDIT_STRU_LOCK = "EDITSTRULOCK";

		/** The Constant PRJ_ACCTJJ_LVL. 项目库中经济科目支持的级次 */
		public static final String PRJ_ACCTJJ_LVL = "PRJ_ACCTJJ_LVL";

		/** The Constant SERVER_TYPE. 服务器类型:weblogic,tomcat,处理在线更新blob内容有区别，项目附件用 */
		public static final String SERVER_TYPE = "SERVERTYPE";

		/** The Constant CF_CONTROL. 1：仅启用单位控制，2：启用处室和单位控制，3：不启用控制数。 */
		public static final String CF_CONTROL = "CFCONTROL";

		/** The Constant PRJ_RATING. 项目控制额度 */
		public static final String PRJ_RATING = "PRJRATING";

		/** The Constant USE_BSE_AUDIT. 基础信息审核是否启用计算，0 不启用，1启用 */
		public static final String USE_BSE_AUDIT = "USEBSEAUDIT";

		/** The Constant PRJ_SHOW_STD. 项目标准目录 1：启用，0：不启用 */
		public static final String PRJ_SHOW_STD = "PRJSHOWSTD";

		/** The Constant PAYOUT_2_INCOMINGBUDGET 支出预算表填入收入预算表；0：不启用；1：启用. */
		public static final String PAYOUT_2_INCOMINGBUDGET = "PAYOUT2INCOMINGBUDGET";

		/** The Constant SHOW_ACCT_JJ. 支出表项目设置界面经济科目：0:所有，1:公式对单位表中不存在的 */
		public static final String SHOW_ACCT_JJ = "SHOW_ACCT_JJ";

		/** The Constant PAYOUT_ITEM_EDIT_INI. 支出表项目设置时是否进入编辑状态：0否，1是 */
		public static final String PAYOUT_ITEM_EDIT_INI = "PAYOUT_ITEM_EDIT_INI";

		/** The Constant PAYOUT_CLEAN_EMPTY_ROW. 是否在保存支出预算表时删除合计值为0的行——0:不删除；1:删除 */
		public static final String PAYOUT_CLEAN_EMPTY_ROW = "PAYOUT_CLEAN_EMPTY_ROW";

		/** The Constant QUOTA_SHOW. 0：出现“显示限额”按钮，1：限额显示在面板下方 */
		public static final String QUOTA_SHOW = "QUOTA_SHOW";

		/**
		 * The Constant SHOW_ONLY_DAILY_PAYOUT.
		 * 0:支出表项目设置界面显示所有支出项目，1只显示基本支出下的日常公用支出
		 */
		public static final String SHOW_ONLY_DAILY_PAYOUT = "SHOW_ONLY_DAILY_PAYOUT";

		/**
		 * The Constant NEED_INSERT_DAILY_PAYOUT.
		 * 0:支出表如果没有302记录则不自动插入，1自动插入所有302记录
		 */
		public static final String NEED_INSERT_DAILY_PAYOUT = "NEED_INSERT_DAILY_PAYOUT";

		/** The Constant AUTO_DISTR_PFS. 0:支出表“资金分配”按钮隐藏，1显示 */
		public static final String AUTO_DISTR_PFS = "AUTO_DISTR_PFS";

		/** The Constant CUR_STATE. 系统当前状态，可关联相关的批次 */
		public static final String CUR_STATE = "CUR_STATE";

		/** The Constant SHOW_TREE. 0：启用科目对收费项目，1：启用收费项目对科目 */
		public static final String SHOW_TREE = "SHOWTREE";

		/** The Constant SERIAL_NO. 部门预算系统支持验证码 */
		public static final String SERIAL_NO = "SERIALNO";
		
		/** The Constant PAYOUT_ITEM_NAME_INPUT. 项目支出类别及名称录入风格 */
		public static final String PAYOUT_ITEM_NAME_INPUT = "PAYOUT_ITEM_NAME_INPUT";
		
		/** 支出控制数的初始值：-1不控制，0控制为0. */
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
		
		/** The Constant only_name. 无分类,名称手动录入 */
		public static final int ONLY_NAME = 0;
		
		/** The Constant select_kind_name. 分类/名称均选择录入 */
		public static final int SELECT_KIND_NAME = 1;
		
		/** The Constant input_name_only. 分类选择录入,名称手动录入 */
		public static final int SELECT_KIND_ONLY = 2;	
	}
	
	/** The Constant STATUS_999: 001|004 初始状态. */
	public static final String STATUS_999 = "{7B35E12E-875F-425F-BD19-AACE4403CFFD}";
	/** The Constant STATUS_000: 全部. */
	public static final String STATUS_000 = "{8A98726A-81D8-4A94-9893-CA54C448D68E}";
	/** The Constant STATUS_001: 未确认. */
	public static final String STATUS_001 = "{26C5597A-02B0-4B0A-8E5E-C3F30612F202}";
	/** The Constant STATUS_002: 已确认. */
	public static final String STATUS_002 = "{91FA49E4-9630-423B-8025-99CAD5ADEF50}";
	/** The Constant STATUS_003: 已退回. */
	public static final String STATUS_003 = "{610449EB-78CC-4269-9B69-1F8FB8408CFF}";
	/** The Constant STATUS_004: 被退回. */
	public static final String STATUS_004 = "{8FEDC3D1-3768-457A-9BF8-1C3319989893}";
	/** The Constant STATUS_005: 已修改. */
	public static final String STATUS_005 = "{EF6AE69E-75A4-4FEE-8D7A-7FE86774AADC}";
	/** The Constant STATUS_008: 曾经办. */
	public static final String STATUS_008 = "{47379426-2E9D-4F02-9FF4-235020098801}";
	/** The Constant STATUS_101: 已挂起. */
	public static final String STATUS_101 = "{FD62AD5B-A22D-404A-90BB-337F667A4001}";
	/** The Constant STATUS_102: 已删除. */
	public static final String STATUS_102 = "{3EF10CA3-A1AE-4DEC-BCAD-DB1C1D2D678F}";
	/** The Constant STATUS_103: 已作废. */
	public static final String STATUS_103 = "{08DB04AC-FE69-4C7C-84E9-568E7970A024}";

}
