/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title 
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import java.util.List;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.Node;

public class ReportType {

    /**
     * 简单导出
     */
    public final static int Simp = 0;

    /**
     * 表头表体都为sql语句的导出形式（带配置信息）
     */
    public final static int Sql = 1;

    /**
     * 表头dataset，表体为sql的导出形式（不带配置信息）
     */
    public final static int DsSqlWithoutSet = 3;

    /**
     * 表头为dataset,表体为list的导出形式（不带配置信息）
     */
    public final static int DsLstWithoutSet = 4;

    /**
     * 表头为dataset,表体为list的导出形式（带配置信息）
     */
    public final static int DsLst = 4;

    /**
     * 类似封面的导出（在fb_u_szzb里取信息）
     */
    public final static int LikeFace = 5;

    /**
     * 生成报表类型
     * 
     * @param reptype
     * @return
     */
    public static ReportType create(int reptype) {
        switch (reptype) {
        case Simp:
            return null;
        case Sql:
            RESql res = (RESql) RESql.create();
            return res;
        case DsSqlWithoutSet:
            return null;
        case DsLst:
            REDsLst red = (REDsLst) REDsLst.create();
            return red;
        case LikeFace:
            RELikeFace ref = (RELikeFace) RELikeFace.create();
            return ref;
        default:
            return null;
        }
    }

    /**
     * 设置初始参数(sql)
     * 
     */
    public void setParam(String reportID, Node node, String sqlHeader,
            String sqlBody, String ename) {
        ((RESql) this).setParam(reportID, node, sqlHeader,sqlBody, ename);
    }

    /**
     * 设置参数(list)
     * 
     * @param reportID
     * @param node
     * @param dsHeader
     * @param listBody
     * @param sFieldKeyName
     */
    public void setParam(String reportID, Node node, DataSet dsHeader,
            List listBody, String sFieldKeyName) {
        ((REDsLst) this).setParam(reportID, node, dsHeader, listBody,
                sFieldKeyName);
    }

    /**
     * 设置参数(类似封面)
     * 
     * @param aReportID
     * @param dsData
     */
    public void setParam(String aReportID,DataSet dsData) {
        ((RELikeFace)this).setParam(aReportID,dsData);
    }
}
