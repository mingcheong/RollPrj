/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 查询table
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;

public class HeaderUtility {
    /**
     * 创建表头对象
     * @param headerData 表头数据   
     * @param idName 单元格的ID字段名，创建表头的时候应该传入取数据时用的字段的名字
     * @param textName 单元格显示文本的字段名
     * @param parentIdName 提取父结点ID的字段名
     * @param sortKey 用来排序的字段名
     * @return
     * @throws Exception
     */
    public static TableHeader createHeader(DataSet headerData, String idName,
            String textName, String parentIdName, String sortKey,int rowStart,int colStart)
            throws Exception {
        return new TableHeader(HierarchyListGenerator.getInstance().generate(
                headerData, idName, textName, parentIdName, sortKey),rowStart,colStart);
    }
    
    /**
     * 创建表头对象
     * @param headerData 表头数据   
     * @param idName 单元格的ID字段名，创建表头的时候应该传入取数据时用的字段的名字
     * @param textName 单元格显示文本的字段名
     * @param codeRule 提取父结点ID使用的编码规则
     * @param sortKey 用来排序的字段名
     * @return
     * @throws Exception
     */
    public static TableHeader createHeader(DataSet headerData, String idName,
            String textName, SysCodeRule codeRule, String sortKey,int rowStart,int colStart)
            throws Exception {
        return new TableHeader(HierarchyListGenerator.getInstance().generate(
                headerData, idName, textName, codeRule, sortKey),rowStart,colStart);
    }
    
    /**
     * 创建表头对象
     * @param headerData 表头数据   
     * @param idName 单元格的ID字段名，创建表头的时候应该传入取数据时用的字段的名字
     * @param textName 单元格显示文本的字段名
     * @param parentIdName 提取父结点ID的字段名
     * @param sortKey 用来排序的字段名
     * @return
     * @throws Exception
     */
    public static TableHeader createHeader(DataSet headerData, String idName,
            String textName, String parentIdName, String sortKey)
            throws Exception {
        return new TableHeader(HierarchyListGenerator.getInstance().generate(
                headerData, idName, textName, parentIdName, sortKey));
    }
    
    /**
     * 创建表头对象
     * @param headerData 表头数据   
     * @param idName 单元格的ID字段名，创建表头的时候应该传入取数据时用的字段的名字
     * @param textName 单元格显示文本的字段名
     * @param codeRule 提取父结点ID使用的编码规则
     * @param sortKey 用来排序的字段名
     * @return
     * @throws Exception
     */
    public static TableHeader createHeader(DataSet headerData, String idName,
            String textName, SysCodeRule codeRule, String sortKey)
            throws Exception {
        return new TableHeader(HierarchyListGenerator.getInstance().generate(
                headerData, idName, textName, codeRule, sortKey));
    }
}
