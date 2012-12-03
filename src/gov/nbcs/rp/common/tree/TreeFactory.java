package gov.nbcs.rp.common.tree;

import java.util.Map;

/**
 * <p>
 * Title: ���������Ĺ�����
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: �㽭����
 * </p>
 * 
 * @author �ֶ˲�������
 * @version 1.0
 */
public class TreeFactory {
    /**
     * ���ȫ�־��
     */
    private static TreeFactory instance;

    protected TreeFactory() {
    }

    /**
     * ���TreeFactory��ʵ��
     * 
     * @return ʵ�����
     */
    public static TreeFactory getInstance() {
        return instance == null ? instance = new TreeFactory() : instance;
    }

    /**
     * �����Լ��ϴ���һ�������
     * 
     * @param attributes
     *            ���Լ�
     * @return �����
     */
    public Node createTreeNode(Map attributes) {
        return createTreeNode(null, attributes);
    }

    /**
     * �ý��ֵ�����Լ��ϴ���һ�������
     * 
     * @param value
     *            ���ֵ
     * @param attributes
     *            ���Լ�
     * @return �����
     */
    public Node createTreeNode(Object value, Map attributes) {
        return new TreeNode(null, null, null, value, attributes);
    }

    /**
     * �ý��ֵ�����Լ��ϴ���һ�������
     */
    public Node createTreeNode(Object id, Object sortValue, Object value,
            Map attributes) {
        return new TreeNode(id, null, sortValue, value, attributes);
    }

    /**
     * �ý��ֵ�����Լ��ϡ��ı����ݴ���һ�������
     */
    public static Node createTreeNode(Object id, String text, Object sortValue,
            Object value, Map attributes) {
        return new TreeNode(id, text, sortValue, value, attributes);
    }
}