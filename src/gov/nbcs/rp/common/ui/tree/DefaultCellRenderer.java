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

import gov.nbcs.rp.common.Common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;


public class DefaultCellRenderer implements TreeCellRenderer {
    protected Color defaultBackColor = new Color(200, 200, 225);
    
    static Image leafImg = null;

    static Image branchImg = null;

    static Image branchImg_opened = null;

    static {

        try {
            leafImg = ImageIO.read(DefaultCellRenderer.class.getClassLoader()
                    .getResourceAsStream("images/rp/tree/son-node.gif"));

            branchImg = ImageIO.read(DefaultCellRenderer.class.getClassLoader()
                    .getResourceAsStream(
                            "images/rp/tree/father-node-unopen.gif"));

            branchImg_opened = ImageIO.read(DefaultCellRenderer.class
                    .getClassLoader().getResourceAsStream(
                            "images/rp/tree/father-node.gif"));
        } catch (Exception ex) {

        }
    }

    private class RendererComponent extends JPanel {
        JPanel icon = new JPanel() {
            public void paint(Graphics g) {
                Image img = null;
                switch (stat) {
                case PARENT:
                    img = branchImg;
                    break;
                case PARENT_OPEN:
                    img = branchImg_opened;
                    break;
                case CHILD:
                    img = leafImg;
                    break;
                }
                if (img != null) {
                    Rectangle rect = g.getClipBounds();
                    int height = img.getHeight(this);
                    g.drawImage(img, 0, rect.height / 2 - height / 2, this);
                }
            }
        };

        JLabel label = new JLabel();

        int stat;

        static final int PARENT = 0;

        static final int PARENT_OPEN = 1;

        static final int CHILD = 2;

        public RendererComponent() {
            super(new BorderLayout()); 
            add(icon, BorderLayout.WEST);
            icon.setPreferredSize(new Dimension(leafImg.getWidth(this), leafImg
                    .getHeight(this)));
            add(label, BorderLayout.EAST);
        }
    }

    RendererComponent comp = new RendererComponent();

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        MyTreeNode node = (MyTreeNode) value;
        if (node.isLeaf()) {
            comp.stat = RendererComponent.CHILD;
        } else {
            comp.stat = tree.isExpanded(row) ? RendererComponent.PARENT_OPEN
                    : RendererComponent.PARENT;
        }
        comp.label.setText(Common.nonNullStr(value));
        if (selected) {
            comp.setBackground(defaultBackColor);
        } else {
            comp.setBackground(tree.getBackground());
        }
        comp.icon.setBackground(tree.getBackground());
        comp.label.setBackground(tree.getBackground());
        return comp;
    }
}
