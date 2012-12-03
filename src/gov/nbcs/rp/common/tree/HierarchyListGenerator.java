/*
 * Created on 2005-12-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nbcs.rp.common.tree;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * <p>
 * Title: ������Ϣ����Node�������
 * </p>
 * <p>
 * Description: �ɴ���Ĵ�������Ϣ����������������
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: zjyq
 * </p>
 * 
 * @author qzc
 * @version 1.0
 */
public class HierarchyListGenerator {
    /**
     * �����Ĵ�������
     */
    private TreeFactory treeFactory = TreeFactory.getInstance();

    protected HierarchyListGenerator() {
    }

    private static HierarchyListGenerator inst;

    public static HierarchyListGenerator getInstance() {
        return inst == null ? inst = new HierarchyListGenerator() : inst;
    }

    /**
     * �ɼ�����Ϣ���߱������������
     */
    protected Node generate(DataSet ds, String idName, String textName,
            String parentIdName, SysCodeRule codeRule, String sortKey)
            throws Exception {

        Map nodeCache = new HashMap();
        ds.maskDataChange(true);
        ds.beforeFirst();
        while (ds.next()) {
            String id = ds.fieldByName(idName).getString();
            String bookmark = ds.toogleBookmark();
            String sortKeyValue = (sortKey != null) && ds.containsField(sortKey) ? ds
                    .fieldByName(sortKey).getString()
                    : id;
            String text = (textName != null) && ds.containsField(textName) ? ds
                    .fieldByName(textName).getString() : null;
            Node node = TreeFactory.createTreeNode(id, text, sortKeyValue,
                    bookmark, null);
            nodeCache.put(id, node);
        }
        ds.maskDataChange(false);
        return parseTree(ds, nodeCache, idName, parentIdName, codeRule);
    }

    /**
     * 
     * @param nodeCache
     *            ��㻺��
     * @param idName
     *            ���ID�ֶ���
     * @param parentIdName
     *            �����ID���ֶ���
     * @param codeRule
     *            ��������㸸�ӹ�ϵ�������
     * @param sortKey
     *            ����ؼ����ֶ���
     * @return �����������ĸ��ڵ�
     */
    protected Node parseTree(DataSet ds, Map nodeCache, String idName,
            String parentIdName, SysCodeRule codeRule) throws Exception {
        Node root = treeFactory.createTreeNode(null);
        for (Iterator it = nodeCache.values().iterator(); it.hasNext();) {
            Node node = (Node) it.next();
            String bookmark = (String) node.getValue();
            ds.gotoBookmark(bookmark);
            String id = ds.fieldByName(idName).getString();
            String parentId = this.getParentId(ds, id, parentIdName, codeRule);
            Node parent = null;
            if (Common.isNullStr(parentId)) {
                parent = root;
            } else {
                parent = (Node) nodeCache.get(parentId);
            }
            if (parent != null) {
                addChildNode(parent, node);
            }
        }
        return root;
    }

    protected void addChildNode(Node parent, Node newChild) {
        for (int i = 0; i < parent.getChildrenCount(); i++) {
            Node child = parent.getChildAt(i);
            String childSortVal = Common.nonNullStr(child.getSortByValue());
            String newChildSortValue = Common.nonNullStr(newChild
                    .getSortByValue());
            if (childSortVal.compareTo(newChildSortValue) > 0) {
                parent.insert(newChild, i);
                return;
            }
        }
        parent.append(newChild);
    }

    /**
     * ��ȡ�����ID,����ʹ�ñ����������i
     * 
     * @return
     */
    protected String getParentId(DataSet ds, String id, String parentIdName,
            SysCodeRule codeRule) throws Exception {
        if (codeRule != null) {
			return codeRule.previous(id);
		} else {
			return ds.fieldByName(parentIdName).getString();
		}
    }

    /**
     * �ɼ�����Ϣ������
     */
    public Node generate(DataSet ds, String idName, String parentIdName,
            String sortKey) throws Exception {
        return this.generate(ds, idName, null, parentIdName, null, sortKey);
    }

    /**
     * �ɱ������������
     */
    public Node generate(DataSet ds, String idName, SysCodeRule codeRule,
            String sortKey) throws Exception {
        return this.generate(ds, idName, null, null, codeRule, sortKey);
    }

    /**
     * �ɱ�����������������ӽ���ı�����
     */
    public Node generate(DataSet ds, String idName, String textName,
            SysCodeRule codeRule, String sortKey) throws Exception {
        return this.generate(ds, idName, textName, null, codeRule, sortKey);
    }

    /**
     * �ɼ�����Ϣ�����������ӽ���ı�����
     */
    public Node generate(DataSet ds, String idName, String textName,
            String parentIdName, String sortKey) throws Exception {
        return this.generate(ds, idName, textName, parentIdName, null, sortKey);
    }

}
