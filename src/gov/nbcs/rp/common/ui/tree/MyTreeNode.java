/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;


public class MyTreeNode extends DefaultMutableTreeNode {
    String bookmark;
    String sortKeyValue;
    MyTreeNode() {
        this(null);
    }

    MyTreeNode(Object userObject) {
        this(userObject, true);
    }

    MyTreeNode(Object userObject, boolean allowChildren) {
        super(userObject, allowChildren);
    }
    
    public String getBookmark() {
        return bookmark;
    }
    
    public String sortKeyValue() {
        return sortKeyValue;
    }
}
