/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.tree;

import com.foundercy.pf.control.PfTreeNode;

import java.util.Arrays;
import java.util.Enumeration;


public class MyPfNode extends PfTreeNode {
    public static final int SELECT = 0;

    public static final int UNSELECT = 1;

    public static final int PARTSELECT = 2;

    private int select_stat = 1;

    private MyTreeNode node;

    private CustomTree tree;

    public CustomTree getTree() {
        return tree;
    }

    public void setTree(CustomTree tree) {
        this.tree = tree;
    }

    public MyTreeNode getNode() {
        return node;
    }

    public void setNode(MyTreeNode node) {
        this.node = node;
    }

    MyPfNode() {
    }

    public boolean getIsSelect() {
        return (select_stat == SELECT) || (select_stat == PARTSELECT);
    }

    public void setIsSelect(boolean selected) {
        MyPfNode pNode = (MyPfNode) node.getUserObject();
        int stat = SELECT;
        if (selected) {
            stat = SELECT;
        } else {
            stat = UNSELECT;
        }
        pNode.setSelectStat(stat);
        if (tree.isHasRelation()) {
            setChildrenSelect(node, stat);
            setParentSelect((MyTreeNode) node.getParent());
        }
    }

    protected void setChildrenSelect(MyTreeNode node, int stat) {
        Enumeration _enum = node.breadthFirstEnumeration();
        while (_enum.hasMoreElements()) {
            MyTreeNode n = (MyTreeNode) _enum.nextElement();
            MyPfNode pNode = (MyPfNode) n.getUserObject();
            pNode.setSelectStat(stat);
        }
    }

    protected void setParentSelect(MyTreeNode parent) {
        if (parent != null) {
            MyPfNode pNode = (MyPfNode) parent.getUserObject();
            int i = 0;
            int buffer[] = new int[3];
            Arrays.fill(buffer, 0);
            for (; i < parent.getChildCount(); i++) {
                MyTreeNode child = (MyTreeNode) parent.getChildAt(i);
                MyPfNode pChild = (MyPfNode) child.getUserObject();
                buffer[pChild.getSelectStat()]++;
            }
            if (buffer[UNSELECT] == i) {
                pNode.setSelectStat(UNSELECT);
            } else if (buffer[SELECT] != i) {
                pNode.setSelectStat(PARTSELECT);
            } else {
                pNode.setSelectStat(SELECT);
            }
            setParentSelect((MyTreeNode) parent.getParent());
        }
    }
    
    

    public boolean getIsLeaf() {
        return node.isLeaf();
    }

    public int getSelectStat() {
        return select_stat;
    }

    public void setSelectStat(int stat) {
        this.select_stat = stat;
    }
}
