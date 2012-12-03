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


public class CheckBoxCellRenderer implements TreeCellRenderer {
    protected Color defaultBackColor = new Color(200, 200, 225);
    
    public static class RendererComponent extends JPanel {
        
        MyCheck check = new MyCheck();

        JLabel label = new JLabel();
        
        JLabel space = new JLabel();

        public RendererComponent() {
            super(new BorderLayout());
            add(check, BorderLayout.WEST);
            
            space.setPreferredSize(new Dimension(5, label.getHeight()));
            add(space, BorderLayout.CENTER);
            add(label, BorderLayout.EAST);
        }
    }

    private static class MyCheck extends JPanel {
        boolean isSelected;

        static Image selectedImg;

        static Image unSelectedImg;

        static Image partSelectedImg;
        
        int stat;
        
        static {
            try {
                selectedImg = ImageIO.read(MyCheck.class.getClassLoader()
                        .getResourceAsStream("images/rp/tree/select.png"));
                unSelectedImg = ImageIO
                        .read(MyCheck.class.getClassLoader()
                                .getResourceAsStream(
                                        "images/rp/tree/unselect.png"));
                partSelectedImg = ImageIO.read(MyCheck.class.getClassLoader()
                        .getResourceAsStream(
                                "images/rp/tree/partselect.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public MyCheck() {
            this.setPreferredSize(new Dimension(unSelectedImg.getWidth(null), unSelectedImg
                    .getHeight(null)));
        }

        public void setSelected(int selectStat) {
            stat = selectStat;
        }

        public void paint(Graphics g) {
            Image img = null;
            switch (stat) {
            case MyPfNode.SELECT:
                img = selectedImg;
                break;
            case MyPfNode.UNSELECT:
                img = unSelectedImg;
                break;
            case MyPfNode.PARTSELECT:
                img = partSelectedImg;
                break;
            }
            Rectangle rect = g.getClipBounds();
            int height = img.getHeight(this);
            g.drawImage(img, 0, rect.height / 2 - height / 2, this);
        }
    }
    
    RendererComponent comp = new RendererComponent();

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        comp.label.setText(Common.nonNullStr(value));
        if (selected) {
            comp.setBackground(defaultBackColor);
        } else {
            comp.setBackground(tree.getBackground());
        }
        MyTreeNode node = (MyTreeNode)value;
        comp.check.setSelected(((MyPfNode)node.getUserObject()).getSelectStat());
        comp.check.setBackground(tree.getBackground());
        comp.label.setBackground(tree.getBackground());
        comp.space.setBackground(tree.getBackground());
        return comp;
    }
}
