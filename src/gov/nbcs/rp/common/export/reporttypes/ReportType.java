/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
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
     * �򵥵���
     */
    public final static int Simp = 0;

    /**
     * ��ͷ���嶼Ϊsql���ĵ�����ʽ����������Ϣ��
     */
    public final static int Sql = 1;

    /**
     * ��ͷdataset������Ϊsql�ĵ�����ʽ������������Ϣ��
     */
    public final static int DsSqlWithoutSet = 3;

    /**
     * ��ͷΪdataset,����Ϊlist�ĵ�����ʽ������������Ϣ��
     */
    public final static int DsLstWithoutSet = 4;

    /**
     * ��ͷΪdataset,����Ϊlist�ĵ�����ʽ����������Ϣ��
     */
    public final static int DsLst = 4;

    /**
     * ���Ʒ���ĵ�������fb_u_szzb��ȡ��Ϣ��
     */
    public final static int LikeFace = 5;

    /**
     * ���ɱ�������
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
     * ���ó�ʼ����(sql)
     * 
     */
    public void setParam(String reportID, Node node, String sqlHeader,
            String sqlBody, String ename) {
        ((RESql) this).setParam(reportID, node, sqlHeader,sqlBody, ename);
    }

    /**
     * ���ò���(list)
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
     * ���ò���(���Ʒ���)
     * 
     * @param aReportID
     * @param dsData
     */
    public void setParam(String aReportID,DataSet dsData) {
        ((RELikeFace)this).setParam(aReportID,dsData);
    }
}
