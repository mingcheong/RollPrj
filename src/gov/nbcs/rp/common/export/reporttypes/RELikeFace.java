/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 报表类型 ：类似封面形式
 * 
 * 该类封装了类似封面形式的报表类型
 * 对应的表识符是：ReportType.LikeFace
 * 传入reportID,从表fb_u_qr_szzb中取各个单元格的信息
 * 同时根据reportID在打印设置功能模块里取相关的配置信息
 * 与ExportBatchProp结合使用
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import gov.nbcs.rp.common.datactrl.DataSet;

public class RELikeFace extends ReportType{

    // 报表编码
    public String sReportID;
    
    //设置数据集合
    public DataSet dsData;
    
    /**
     * 构造函数
     * @return
     */
    public static ReportType create() {
        RELikeFace rel = new RELikeFace();
        return rel;
    }
    
    /**
     * 设置参数
     * @param aReportID
     * @param dsData
     */
    public void setParam(String aReportID,DataSet dsData) {
        this.sReportID = aReportID;
        this.dsData = dsData;
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
     * 设置数据集
     * @param dsData
     */
    public void setDsData(DataSet dsData){
        this.dsData = dsData;
    }

    /**
     * 获取数据集
     * @return
     */
    public DataSet getDsData(){
        return this.dsData;
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
     * 获取该报表类型对应的标识符号
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.LikeFace;
    }
}
