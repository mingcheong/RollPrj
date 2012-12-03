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
 * Title: 级次信息生成Node对象的类
 * </p>
 * <p>
 * Description: 由传入的带级次信息的数据生成树对象
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
     * 树结点的创建工具
     */
    private TreeFactory treeFactory = TreeFactory.getInstance();

    protected HierarchyListGenerator() {
    }

    private static HierarchyListGenerator inst;

    public static HierarchyListGenerator getInstance() {
        return inst == null ? inst = new HierarchyListGenerator() : inst;
    }

    /**
     * 由级次信息或者编码规则生成树
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
     *            结点缓冲
     * @param idName
     *            结点ID字段名
     * @param parentIdName
     *            父结点ID的字段名
     * @param codeRule
     *            描述树结点父子关系编码规则
     * @param sortKey
     *            排序关键字字段名
     * @return 返回生成树的根节点
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
     * 获取父结点ID,优先使用编码规则生成i
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
     * 由级次信息生成树
     */
    public Node generate(DataSet ds, String idName, String parentIdName,
            String sortKey) throws Exception {
        return this.generate(ds, idName, null, parentIdName, null, sortKey);
    }

    /**
     * 由编码规则生成树
     */
    public Node generate(DataSet ds, String idName, SysCodeRule codeRule,
            String sortKey) throws Exception {
        return this.generate(ds, idName, null, null, codeRule, sortKey);
    }

    /**
     * 由编码规则生成树，附加结点文本内容
     */
    public Node generate(DataSet ds, String idName, String textName,
            SysCodeRule codeRule, String sortKey) throws Exception {
        return this.generate(ds, idName, textName, null, codeRule, sortKey);
    }

    /**
     * 由级次信息生成树，附加结点文本内容
     */
    public Node generate(DataSet ds, String idName, String textName,
            String parentIdName, String sortKey) throws Exception {
        return this.generate(ds, idName, textName, parentIdName, null, sortKey);
    }

}
