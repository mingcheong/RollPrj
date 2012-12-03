/**
 * 单表导出辅助类
 */

package gov.nbcs.rp.common.export.action;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

public interface ExportProp {	

	/**
	 * 获取类型字段名
	 * 
	 * @return 类型字段名
	 */
	public String getFieldTName();

	/**
	 * 获取格式化字段名
	 * 
	 * @return 格式化字段名
	 */
	public String getFieldFName();

	/**
	 * 获取字段列宽字段名
	 * 
	 * @return 列宽字段名
	 */
	public String getFieldWName();

	/**
	 * 获取大标题
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 获取小标题
	 * 
	 * @return
	 */
	public String[] getTitle_Child();

	/**
	 * 数值型在表中的表现形式 如String[] number = new String[]{"整型","浮点型","货币型"}
	 * 
	 * @return
	 */
	public String[] getNumberViewInTable();

	/**
	 * 设置默认保存文件路径名，设置后则不管单表还是多表都不需要通过选择框选择，而是直接保存当前设置的路径文件
	 * 
	 * @return
	 */
	public String getDefaultAddres();
	
	/**
	 * 设置在执行查询导出语句之前要进行的操作 
	 * @param list_Before 导出之前要执行的语句
	 */
	public List getSqls_BeforeGetBody();

	/**
	 * 设置在执行查询导出语句之后要进行的操作 
	 * @param list_Before 导出之后要执行的语句
	 */
	public List getSqls_AfterGetBody();
    
    /**
     * 设置封面数据 
     * @return
     */
    public DataSet[] getDsFace();
    
    /**
     * 当表头不是从库里获取的时候，如果需要设置列宽则设置该地方
     * @return
     */
    public float[] getWidths();
    
    /**
     * 当表头不是从库里获取的时候，如果需要设置格式化则设置该地方
     * @return new String[]{"#","#,##0.00","#","###"}
     */
    public String[] getFormats();
    
    /**
     * 当表头不是从库里获取的时候，如果需要设置类型则设置该地方
     * @return
     */
    public String[] getTypes();
    
    
}
