/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 报表类型 ：表头为DataSet,表体为List的组织形式
 * 
 * 该类封装了类似行表形式的报表类型
 * 对应的表识符是：ReportType.DsLst
 * 同时根据reportID在打印设置功能模块里取相关的配置信息
 * 与ExportBatchProp结合使用
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import java.util.List;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.Node;

public class REDsLst extends ReportType{

    // 表头的node
    private Node node;

    // 表头的数据集
    private DataSet dsHeader;

    // 表体的数据集
    private List listBody;

    // 关键字段名（通常为field_ename)
    private String sFieldKeyName;

    // 报表编码
    private String sReportID;

    /**
     * 构造函数
     * 
     * @return
     */
    public static ReportType create() {
        REDsLst red = new REDsLst();
        return red;
    }

    /**
     * 设置参数
     * @param reportID
     * @param node
     * @param dsHeader
     * @param listBody
     * @param sFieldKeyName
     */
    public void setParam(String reportID, Node node, DataSet dsHeader,
            List listBody, String sFieldKeyName) {
        this.sFieldKeyName = sFieldKeyName;
        this.sReportID = reportID;
        this.node = node;
        this.dsHeader = dsHeader;
        this.listBody = listBody;
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
     * 设置表头的组织形式
     * 
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * 设置表头的数据集
     * 
     * @param dsHeader
     */
    public void setDsHeader(DataSet dsHeader) {
        this.dsHeader = dsHeader;
    }

    /**
     * 设置表体的数据集
     * 
     * @param listBody
     */
    public void setBodyData(List listBody) {
        this.listBody = listBody;
    }

    /**
     * 设置关键字段名（预算中通常为field_ename）
     * 
     * @param aFieldKeyName
     */
    public void setFieldKeyName(String aFieldKeyName) {
        this.sFieldKeyName = aFieldKeyName;
    }

    /**
     * 获取关键字段名
     * 
     * @return
     */
    public String getFieldKeyName() {
        return this.sFieldKeyName;
    }

    /**
     * 获取表体数据
     * 
     * @return
     */
    public List getListBody() {
        return this.listBody;
    }

    /**
     * 获取表头数据
     * 
     * @return
     */
    public DataSet getDsHeader() {
        return this.dsHeader;
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
     * 获取表头组织形式
     * 
     * @return
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * 获取报表类型标识符
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.DsLst;
    }
}
