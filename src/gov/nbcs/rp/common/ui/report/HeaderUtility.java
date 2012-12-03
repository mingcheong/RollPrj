/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report;

import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;

public class HeaderUtility {
    /**
     * ������ͷ����
     * @param headerData ��ͷ����   
     * @param idName ��Ԫ���ID�ֶ�����������ͷ��ʱ��Ӧ�ô���ȡ����ʱ�õ��ֶε�����
     * @param textName ��Ԫ����ʾ�ı����ֶ���
     * @param parentIdName ��ȡ�����ID���ֶ���
     * @param sortKey ����������ֶ���
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
     * ������ͷ����
     * @param headerData ��ͷ����   
     * @param idName ��Ԫ���ID�ֶ�����������ͷ��ʱ��Ӧ�ô���ȡ����ʱ�õ��ֶε�����
     * @param textName ��Ԫ����ʾ�ı����ֶ���
     * @param codeRule ��ȡ�����IDʹ�õı������
     * @param sortKey ����������ֶ���
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
     * ������ͷ����
     * @param headerData ��ͷ����   
     * @param idName ��Ԫ���ID�ֶ�����������ͷ��ʱ��Ӧ�ô���ȡ����ʱ�õ��ֶε�����
     * @param textName ��Ԫ����ʾ�ı����ֶ���
     * @param parentIdName ��ȡ�����ID���ֶ���
     * @param sortKey ����������ֶ���
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
     * ������ͷ����
     * @param headerData ��ͷ����   
     * @param idName ��Ԫ���ID�ֶ�����������ͷ��ʱ��Ӧ�ô���ȡ����ʱ�õ��ֶε�����
     * @param textName ��Ԫ����ʾ�ı����ֶ���
     * @param codeRule ��ȡ�����IDʹ�õı������
     * @param sortKey ����������ֶ���
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
