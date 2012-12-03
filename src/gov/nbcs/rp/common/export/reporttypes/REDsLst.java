/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title �������� ����ͷΪDataSet,����ΪList����֯��ʽ
 * 
 * �����װ�������б���ʽ�ı�������
 * ��Ӧ�ı�ʶ���ǣ�ReportType.DsLst
 * ͬʱ����reportID�ڴ�ӡ���ù���ģ����ȡ��ص�������Ϣ
 * ��ExportBatchProp���ʹ��
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

    // ��ͷ��node
    private Node node;

    // ��ͷ�����ݼ�
    private DataSet dsHeader;

    // ��������ݼ�
    private List listBody;

    // �ؼ��ֶ�����ͨ��Ϊfield_ename)
    private String sFieldKeyName;

    // �������
    private String sReportID;

    /**
     * ���캯��
     * 
     * @return
     */
    public static ReportType create() {
        REDsLst red = new REDsLst();
        return red;
    }

    /**
     * ���ò���
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
     * ���ñ������
     * 
     * @param aReportID
     */
    public void setReportID(String aReportID) {
        this.sReportID = aReportID;
    }

    /**
     * ���ñ�ͷ����֯��ʽ
     * 
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * ���ñ�ͷ�����ݼ�
     * 
     * @param dsHeader
     */
    public void setDsHeader(DataSet dsHeader) {
        this.dsHeader = dsHeader;
    }

    /**
     * ���ñ�������ݼ�
     * 
     * @param listBody
     */
    public void setBodyData(List listBody) {
        this.listBody = listBody;
    }

    /**
     * ���ùؼ��ֶ�����Ԥ����ͨ��Ϊfield_ename��
     * 
     * @param aFieldKeyName
     */
    public void setFieldKeyName(String aFieldKeyName) {
        this.sFieldKeyName = aFieldKeyName;
    }

    /**
     * ��ȡ�ؼ��ֶ���
     * 
     * @return
     */
    public String getFieldKeyName() {
        return this.sFieldKeyName;
    }

    /**
     * ��ȡ��������
     * 
     * @return
     */
    public List getListBody() {
        return this.listBody;
    }

    /**
     * ��ȡ��ͷ����
     * 
     * @return
     */
    public DataSet getDsHeader() {
        return this.dsHeader;
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
     * ��ȡ��ͷ��֯��ʽ
     * 
     * @return
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * ��ȡ�������ͱ�ʶ��
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.DsLst;
    }
}
