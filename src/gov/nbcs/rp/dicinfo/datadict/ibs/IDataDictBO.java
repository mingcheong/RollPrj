/**
 * Classname IDataDictBO
 *
 * Version 6.2.40
 *
 * Copyright 浙江易桥 版权所有
 */

package gov.nbcs.rp.dicinfo.datadict.ibs;

import java.util.List;

import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:字典管理操作服务接口
 * </p>
 * <p>
 
 */

public interface IDataDictBO {

	public final int LOGIN_ONLINE = 0;// 在线

	public final int LOGIN_OFFLINE = 1;// 离线

	public final String ISCLASS = "isclass";// 是否是类的字段

	public final String TABLECNAME = "tablecname";

	public final String TABLEENAME = "tableename";

	public static final int TABLE_MODEL = 1;// 选择数据表或视图

	public static final int DEFINE_MODEL = 2;// 已定义的选择

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

	public static String fieldMark = "isfield";// 用于数据源树中的类别区分字段

	public static String MARK_FIELD = "1";// 字段

	public static String MARK_TYPE = "-1";// 大类

	public static String MARK_TABLE = "0";// 表

	// 年份标记:表现本年或去年
	public static String YEAR_FLAG = "year_flag";

	// 对比分析表，替换时对应表的Dicid
	public static String COMPARE_DICID = "compare_dicid";

	// 是否系统内置数据源1:内置 0非内置,内置数据源不可删除
	public static String ISDEFAULT = "isdefault";

	// 项目明细信息
	public static String PRJ_PFS = "PRJPFS";

	// 项目费用明细信息
	public static String PRJ_MX = "PRJMX";

	// 项目标记
	public static String PRJ = "PRJ";

	// 控制数
	public static String PAYOUT_RAE = "PAYOUTRAE";

	// 项目主表前缀
	public static String PRJ_BASE_PREFIX = "base_";

	public static String PAYOUT_KIND_LVL = "payout_kind_lvl";

	/**
	 * 查询表类型
	 * 
	 * @return
	 */
	public List queryTableTypeList();

	/**
	 * 
	 * 保存字典类型
	 * 
	 * @param tableInfoData
	 */
	public int addDictType(String typeCode, String ypeName, String memo);

	/**
	 * 
	 * 更新字典类型
	 * 
	 * @param tableInfoData
	 */
	public int updateDictType(String typeCode, String ypeName, String memo);

	/**
	 * 
	 * 删除字典类型
	 * 
	 * @param tableInfoData
	 */
	public int deleteDictType(String dictCode);

	/**
	 * 删除字典表信息
	 * 
	 * @return 表名
	 */
	public int deleteTableInfo(String dicID);

	/**
	 * 判断表格类型的编号是否存在
	 * 
	 * @param typeCodeObje
	 * @return
	 */
	public boolean isCodeExist(String typeCodeObje);

	/**
	 * 同步关联表
	 * 
	 * @param tableName
	 *            表名
	 * @return 是否同步成功
	 */
	public boolean sysRelTable(String tableName, String setYear);

	// 0------------------------------------------------
	/**
	 * 取得枚举源的主选择项模型字串
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRefDataString(String setYear) throws Exception;

	/**
	 * 取得表类型选择模型
	 * 
	 * @return
	 */
	public String getTableTypeRefString();

	/**
	 * 取得数据字典中的明细字段信息
	 * 
	 * @param sTableID表的英文名
	 * @return
	 */
	public List getDicField(String sTableID, String setYear);

	/**
	 * 取得所有的参考列选择项
	 * 
	 * @return
	 */
	public String getAllRefFields(String setYear);

	/**
	 * 取得所有的支持数据源的表
	 * 
	 * @param logMode
	 * @return
	 */
	public List getTableList(int logMode);

	/**
	 * 保存数据，因为不可以添加和删除字段，所以只是更新某些字段
	 * 
	 * @param xmlMain
	 *            主表信息
	 * @param lstFields
	 *            明细表信息
	 * @return
	 * @throws Exception
	 */
	public String saveDs(XMLData xmlMain, List lstFields) throws Exception;

	/**
	 * 取得枚举源的字段选择模型数据,返回的数据类型为：key:REFCOL_ID,value:选择项String
	 * 
	 * @return
	 */
	public XMLData getRefDataFieldSelection(String setYear);

	/**
	 * 取得数据库表中定义的表和视图
	 * 
	 * @param isALl
	 * @return
	 */
	public List getTableInfo(int iModel, boolean isAll, String setYear);

	/**
	 * 添加库的中的表
	 */
	public String makeNewTable(String sTableName, String type, String tabProp,
			String setYear) throws Exception;

	/**
	 * 添加预定义的表
	 * 
	 * @param tableName
	 *            表名
	 * @param type
	 *            表的类名
	 * @return
	 * @throws Exception
	 */
	public String makeNewTableByDefine(String tableName, String type,
			String tabProp, String report_id, String setYear) throws Exception;

	/**
	 * 添加批量的表
	 * 
	 * @param lstSelect选择的表列表
	 * @param isDefine是不是预定义的
	 * @throws Exception
	 */
	public void makeListTable(List lstSelect, boolean isDefine, String tabProp,
			String setYear) throws Exception;

	/**
	 * 交换两个表的顺序
	 * 
	 * @param xmlFirst
	 * @param xmlSecond
	 * @return
	 */
	public boolean changeOrder(XMLData xmlFirst, XMLData xmlSecond,
			String setYear);

	/**
	 * 取得数据源，建树用，以指定父结点的方式建树。将显示英文字段 数据分为三级，一级是分类，二级是数据表名，三级是字段名
	 * 
	 * @return
	 */
	public List getDataSourceForTree(String setYear);

	/**
	 * 取得字典表的主表信息
	 * 
	 * @param tableID
	 *            表在字典表中的ID
	 * @return
	 */
	public XMLData getTableMainInfo(String tableID, String setYear);

	// 查询一个数据源的字段类型信息，用于缓存
	public XMLData getFieldAndType(String dicID, String setYear);

	/**
	 * 判断数据源中文名称是否存在。
	 * 
	 * @param cname
	 * @param reportId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistTableCname(String cname, String reportId)
			throws Exception;

}
