/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 报表类型 ：表头，表体的获取都通过sql语句
 * 
 * 该类封装了类似07年查询报表形式的报表类型
 * 对应的表识符是：ReportType.Sql
 * 同时根据reportID在打印设置功能模块里取相关的配置信息
 * 与ExportBatchProp结合使用
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import gov.nbcs.rp.common.tree.Node;

public class RESql extends ReportType{

    // 报表编码
    private String sReportID;

    // 表头
    private Node node;

    // 表头sql
    private String sSqlTitle;

    // 表体sql
    private String sSqlBody;

    // 主键
    private String sFieldKeyName;

    /**
     * 构造方法
     * 
     */
    public static ReportType create() {
        RESql res = new RESql();
        return res;
    }

    /**
     * 设置初始参数
     * 
     */
    public void setParam(String reportID, Node node, String sqlHeader,
            String sqlBody, String ename) {
        this.sReportID = reportID;
        this.node = node;
        this.sSqlTitle = sqlHeader;
        this.sSqlBody = sqlBody;
        this.sFieldKeyName = ename;
    }

    /**
     * 设置报表编码
     * 
     * @param aReportID
     */
    public void setReportID(String aReportID) {
        this.sReportID = aReportID;
    }

    /**
     * 设置表头
     * 
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * 设置表头sql
     * 
     * @param sql
     */
    public void setSqlTitle(String sql) {
        this.sSqlTitle = sql;
    }

    /**
     * 设置标题sql
     * 
     * @param sql
     */
    public void setSqlBody(String sql) {
        this.sSqlBody = sql;
    }

    /**
     * 设置关键字段（一般为field_ename）
     * 
     * @param aKeyName
     */
    public void setFieldKeyName(String aKeyName) {
        this.sFieldKeyName = aKeyName;
    }

    /**
     * 获取表头sql
     * 
     * @return
     */
    public String getSqlTitle() {
        return this.sSqlTitle;
    }

    /**
     * 获取表体sql
     * 
     * @return
     */
    public String getSqlBody() {
        return this.sSqlBody;
    }

    /**
     * 获取关键字段
     * 
     * @return
     */
    public String getFieldKeyName() {
        return this.sFieldKeyName;
    }

    /**
     * 获取表头
     * 
     * @return
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * 获取报表编码
     * 
     * @return
     */
    public String getReportID() {
        return this.sReportID;
    }

    /**
     * 获取报表类型标识符
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.Sql;
    }

}
