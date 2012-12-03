/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ����Ԥ����ϵͳ
 * 
 * @title �������� ����ͷ������Ļ�ȡ��ͨ��sql���
 * 
 * �����װ������07���ѯ������ʽ�ı�������
 * ��Ӧ�ı�ʶ���ǣ�ReportType.Sql
 * ͬʱ����reportID�ڴ�ӡ���ù���ģ����ȡ��ص�������Ϣ
 * ��ExportBatchProp���ʹ��
 * 
 * @author qzc
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.export.reporttypes;

import gov.nbcs.rp.common.tree.Node;

public class RESql extends ReportType{

    // �������
    private String sReportID;

    // ��ͷ
    private Node node;

    // ��ͷsql
    private String sSqlTitle;

    // ����sql
    private String sSqlBody;

    // ����
    private String sFieldKeyName;

    /**
     * ���췽��
     * 
     */
    public static ReportType create() {
        RESql res = new RESql();
        return res;
    }

    /**
     * ���ó�ʼ����
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
     * ���ñ������
     * 
     * @param aReportID
     */
    public void setReportID(String aReportID) {
        this.sReportID = aReportID;
    }

    /**
     * ���ñ�ͷ
     * 
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * ���ñ�ͷsql
     * 
     * @param sql
     */
    public void setSqlTitle(String sql) {
        this.sSqlTitle = sql;
    }

    /**
     * ���ñ���sql
     * 
     * @param sql
     */
    public void setSqlBody(String sql) {
        this.sSqlBody = sql;
    }

    /**
     * ���ùؼ��ֶΣ�һ��Ϊfield_ename��
     * 
     * @param aKeyName
     */
    public void setFieldKeyName(String aKeyName) {
        this.sFieldKeyName = aKeyName;
    }

    /**
     * ��ȡ��ͷsql
     * 
     * @return
     */
    public String getSqlTitle() {
        return this.sSqlTitle;
    }

    /**
     * ��ȡ����sql
     * 
     * @return
     */
    public String getSqlBody() {
        return this.sSqlBody;
    }

    /**
     * ��ȡ�ؼ��ֶ�
     * 
     * @return
     */
    public String getFieldKeyName() {
        return this.sFieldKeyName;
    }

    /**
     * ��ȡ��ͷ
     * 
     * @return
     */
    public Node getNode() {
        return this.node;
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
     * ��ȡ�������ͱ�ʶ��
     * 
     * @return
     */
    public int getReportType() {
        return ReportType.Sql;
    }

}
