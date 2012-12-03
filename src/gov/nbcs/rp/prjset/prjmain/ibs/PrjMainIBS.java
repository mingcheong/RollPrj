/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.ibs;

import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;

/**
 * <p>
 * Title:项目申报界面设置对应的业务处理接口
 * </p>
 * <p>
 * Description:为项目申报界面设置相关功能提供业务处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author 孙瑞敏
 * @version 1.0
 */
public interface PrjMainIBS {

	/** 模块进行添加操作 */
	public static final int OPERATION_ADD = 1;

	/** 模块进行修改操作 */
	public static final int OPERATION_MOD = 2;

	/** 模块的状态-正常状态 */
	public static final int NORMAL_STATUS = 1;

	/** 模块的状态-添加状态 */
	public static final int ADD_STATUS = 2;

	/** 模块的状态-修改状态 */
	public static final int MOD_STATUS = 3;

	/** 模块的状态-删除状态 */
	public static final int DEL_STATUS = 4;

	/**
	 * 调用Class ： PrjMainUI 
	 * 得到显示树形结构的DataSet
	 * 
	 * @return 树形结构的DataSet
	 */
	public DataSet getInputSetTreeData();

	/**
	 * 调用Class ： PrjMainListener , PrjMain_DetailUI
	 * 根据主设置表编号得到相应的明细数据
	 * 
	 * @param input_set_id
	 *            主设置表编号
	 * @return 明细数据组成的List
	 */

	public List getSetDetailValues(String input_set_id);

	/**
	 * 调用Class ： PrjMainListener 
	 * 判断是否能删除设置信息
	 * 
	 * @param inputID
	 *            设置信息编号
	 * @return 提示信息
	 */
	public String canDeleteInputSet(String inputID);

	/**
	 * 调用Class ： PrjMainListener 
	 * 删除设置信息，并返回值
	 * 
	 * @param inputID
	 *            设置信息编号
	 * @return 0 成功 -1 失败
	 */
	public int deleteInputSet(String inputID);

	/**
	 * 调用Class ： PrjMain_DetailListener 
	 * 插入设置信息
	 * 
	 * @param xmlData
	 *            数据的封装
	 * @return 0 正常 1 异常
	 */
	public int insertPrjSetting(XMLData xmlData);

	/**
	 * 调用Class ： PrjMain_DetailListener 
	 * 修改设置信息
	 * 
	 * @param xmlData
	 *            数据的封装
	 * @return 0 正常 1 异常
	 */
	public int updatePrjSetting(XMLData xmlData);

	/**
	 * 调用Class ： PrjMain_DetailSelectUI 
	 * 返回所有控件信息
	 * 
	 * @return 控件信息
	 */
	public List getAllComponetsList();

	/**
	 * 调用Class ： PrjMain_DetailSelectUI 
	 * 返回所有控件明细数据
	 * 
	 * @return 控件明细数据
	 */
	public List getAllComBoxValueList();

	/**
	 * 调用Class ： PrjMain_SelectModUI 
	 * 获得控件树数据集
	 * 
	 * @return 控件树数据集
	 */
	public DataSet getComponentsTreeData();

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 获取下拉框控件对应明细数据
	 * 
	 * @param comp_id
	 *            控件编号
	 * @return 下拉框控件对应明细数据
	 */
	public List getComboxValues(String comp_id);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 判断是否能够删除控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 判断后的提示信息，为空则代表允许删除
	 */
	public String componentCanDelete(XMLData xmldata);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 删除相关控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 0 删除成功 -1 删除失败
	 */
	public int deleteComponent(XMLData xmldata);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 判断是否能够添加控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 判断后的提示信息，为空则代表允许添加
	 */
	public String componentCanInsert(XMLData xmldata);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 添加相关控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 0 添加成功 -1 添加失败
	 */
	public int insertComponent(XMLData xmldata, List tableData);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 判断是否能够修改控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 判断后的提示信息，为空则代表允许修改
	 */
	public String componentCanUpdate(XMLData xmldata);

	/**
	 * 调用Class ： PrjMain_SelectModListener 
	 * 修改相关控件信息
	 * 
	 * @param xmldata
	 *            封装控件信息的XMLData
	 * @return 0 修改成功 -1 修改失败
	 */
	public int updateComponent(XMLData xmldata, List tableData);
	/**
	 * 已经被用的字段(C字段)
	 * @return
	 * @throws Exception
	 */
	public List getFieldHadUsed() throws Exception;
	/**
	 * 得到一张表中字段信息
	 * @param talbeName
	 * @return
	 * @throws Exception
	 */
	public List getFieldNameByTable(String tableName) throws Exception;
}
