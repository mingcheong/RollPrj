/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title �������� �����Ʒ�����ʽ
 * 
 * �����װ�����Ʒ�����ʽ�ı�������
 * ��Ӧ�ı�ʶ���ǣ�ReportType.LikeFace
 * ����reportID,�ӱ�fb_u_qr_szzb��ȡ������Ԫ�����Ϣ
 * ͬʱ����reportID�ڴ�ӡ���ù���ģ����ȡ��ص�������Ϣ
 * ��ExportBatchProp���ʹ��
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import gov.nbcs.rp.common.datactrl.DataSet;

public class RELikeFace extends ReportType{

    // �������
    public String sReportID;
    
    //�������ݼ���
    public DataSet dsData;
    
    /**
     * ���캯��
     * @return
     */
    public static ReportType create() {
        RELikeFace rel = new RELikeFace();
        return rel;
    }
    
    /**
     * ���ò���
     * @param aReportID
     * @param dsData
     */
    public void setParam(String aReportID,DataSet dsData) {
        this.sReportID = aReportID;
        this.dsData = dsData;
    }

    /**
     * ���ñ������
     * 
     * @param aReportID
     */
    public void setReportID(String aReportID) {
        this.sReportID = aReportID;
    }
    
    /**
     * �������ݼ�
     * @param dsData
     */
    public void setDsData(DataSet dsData){
        this.dsData = dsData;
    }

    /**
     * ��ȡ���ݼ�
     * @return
     */
    public DataSet getDsData(){
        return this.dsData;
    }
    /**
     * ��ȡ�������
     * 
     * @return
     */
    public String getReportID() {
        return this.sReportID;
    }

    /**
     * ��ȡ�ñ������Ͷ�Ӧ�ı�ʶ����
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.LikeFace;
    }
}
