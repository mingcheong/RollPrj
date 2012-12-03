/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 项目明细设置-接口
 * 
 * @author 钱自成
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
	public String DETAIL_CODE = "DETAIL_CODE";  //项目明细编号
	public String DETAIL_NAME = "DETAIL_NAME";         //项目明细名称
	public String PAR_ID = "PAR_ID";         //项目明细父节点
	public String LVL_ID = "LVL_ID";         //级次码
	public String DETAILTAB_CODE = "DETAILTAB_CODE"; //获取编码规则时 detail在coderule表里对应的字段值
	public String DETAILTABITEM_CODE = "DETAILTABITEM_CODE";  //获取编码规则时，col在coderule表里对应的字段值

	public String FIELD_ID = "FIELD_ID";    //列信息表列字段的id
	public String FIELD_CNAME = "FIELD_CNAME"; //列信息表列字段的name
	public String FIELD_FNAME = "FIELD_FNAME"; //列全名 
	public String FIELD_ENAME = "FIELD_ENAME"; //英文名
	public String DATA_TYPE = "DATA_TYPE";     //列类型
	public String FIELD_KIND = "FIELD_KIND";   //数据来源
	public String FORMULA_DET = "FORMULA_DET"; //计算公式
	public String CALL_PRI = "CALC_PRI";       //优先级
	public String PICK_VALUES = "PICK_VALUES"; //选取值列表
	public String DISPLAY_FORMAT = "DISPLAY_FORMAT";  //显示风格
	public String EDIT_FORMAT = "EDIT_FORMAT"; //编辑风格
	public String NOTNULL = "NOTNULL";         //该列必须输入
	public String STD_TYPE = "STD_TYPE";       //列标准类型
	public String IS_SUMCOL = "IS_SUMCOL";     //是否是合计列
	public String FIELD_INDEX = "FIELD_INDEX";
	public String PRIMARY_INDEX = "PRIMARY_INDEX";
	public String PRIMARY_PROPFIELD = "PRIMARY_PROPFIELD";
	public String FIELD_COLUMN_WIDTH = "FIELD_COLUMN_WIDTH"; //列宽
	public String IS_HIDECOL = "IS_HIDECOL";
	
	public String TABLENAME_DETAIL = "fb_p_detail_mx";
	public String DETAIL_TYPE = "DETAIL_TYPE";
	
	public String FB_P_SORT_TO_DETAIL = "FB_P_SORT_TO_DETAIL"; 
	
	public DataSet getDataset( String aTableName, String aFilter ) throws Exception;
	public DataSet getComboDPDataset() throws Exception;  //获取显示风格
	public DataSet getComboREDataset() throws Exception;  //获取编辑风格
	public DataSet getComboSCDataset() throws Exception;  //获取标准列getComboSCDataset
	public String getFormatMaxCode(String aFieldName, String aTableName, String aFilter,  int aFormat) throws Exception;
	public SysCodeRule getCodeRule(String aFilterCode) throws Exception;
	public DataSet getColInfoAccordDetailCode (String aTableName, String aFilter) throws Exception;
	public void dsPost( DataSet dsDetail,DataSet dscol,DataSet dsSour) throws Exception ;
	public DataSet getENameDataSet() throws Exception ;
	public boolean checkDetailName( String code,String Name ) throws Exception;
	public DataSet getAcctData() throws Exception;
	public int getRecNum(String aTableName, String aFilter ) throws Exception;
	/**
	 * 保存列信息数据
	 * 
	 * @throws Exception
	 */
	public void saveColData(DataSet dsDetail,DataSet ds) throws Exception ;
}

//	  CustomComboBox cbItemType = new CustomComboBox(dsCombo,"CODE","NAME" );
