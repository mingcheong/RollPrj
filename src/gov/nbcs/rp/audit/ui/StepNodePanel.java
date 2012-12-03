/**
 * @# StepNodePanel.java    <文件名>
 */
package gov.nbcs.rp.audit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.framework.systemmanager.ImagePanel;

public class StepNodePanel extends FPanel {
	private static final long serialVersionUID = 7003201219813257456L;

	private static final String IMG_ARROW = "images/rp/a6.gif";

	static final Font selectFont = new Font("宋体", Font.BOLD, 14);//选中字体

	static final Font unSelectFont = new Font("宋体", Font.PLAIN, 12);//非选中字体

	static final Color clrSelect = new Color(200, 255, 243);

	static final Color clrUnSelect = Color.LIGHT_GRAY;

	static final Color clrCur = Color.red;

	private boolean isCanSelected;//是否可以选择

	private boolean isLastNode;//是不是最后一个节点

	private boolean isCurNode;//是不是当前的节点

	private boolean isSelected;//是不是被选中了

	private ImagePanel nextArrow = new ImagePanel(IMG_ARROW);

	private JTextArea txtTitle;

	private JRadioButton btnSelected;
	
	private String nodeInfo ;
	
	private int step;

	public StepNodePanel(String text, boolean isCanSelected,
			boolean isLastNode, boolean isCurNode,int step) {
		this.step = step;
		this.nodeInfo = text;
		this.isCanSelected = isCanSelected;
		this.isLastNode = isLastNode;
		this.isCurNode = isCurNode;
		init();
	}

	public String getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public boolean isCanSelected() {
		return isCanSelected;
	}

	public void setCanSelected(boolean isCanSelected) {
		this.isCanSelected = isCanSelected;
	}

	public boolean isLastNode() {
		return isLastNode;
	}

	public void setLastNode(boolean isLastNode) {
		this.isLastNode = isLastNode;
	}

	public boolean isSelected() {
		return (isSelected && isCanSelected && !isCurNode);
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if (isCurNode) {
			isSelected = false;
			return;
		}
		if (!isCanSelected) {
			isSelected = false;
			return;
		}
		if (isSelected) {
			txtTitle.setFont(selectFont);
			txtTitle.setBackground(clrSelect);
			btnSelected.setSelected(true);
		} else {
			txtTitle.setFont(unSelectFont);
			txtTitle.setBackground(clrUnSelect);
			btnSelected.setSelected(false);
		}

	}

	public ImagePanel getNextArrow() {
		return nextArrow;
	}

	public void setNextArrow(ImagePanel nextArrow) {
		this.nextArrow = nextArrow;
	}

	private void init() {
		txtTitle = new JTextArea();

		//		txtTitle.setEnabled(false);
		txtTitle.setForeground(Color.black);
		txtTitle.setBackground(clrUnSelect);
		txtTitle.setFont(unSelectFont);
		txtTitle.setEditable(false);
		txtTitle.setSelectionColor(Color.white);
		if (isCurNode) {
			txtTitle.setFont(selectFont);
			txtTitle.setForeground(clrCur);
			isCanSelected = false;

		}

		txtTitle.setLineWrap(true);
		txtTitle.setText(this.nodeInfo);
		txtTitle.setBorder(null);
		txtTitle.setBorder(new BevelBorder(BevelBorder.LOWERED));

		txtTitle.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!isCanSelected) {//如果不可以选择,则退出
					setSelected(false);
					return;
				}
				setSelected(true);
//				listener.hasSelected(StepNodePanel.this);
			}

		});

		FPanel pnlTop = new FPanel();
		pnlTop.setLayout(new BorderLayout());
		if (isCanSelected) {
			btnSelected = new JRadioButton();
			btnSelected.setSelected(false);
			btnSelected.setBackground(Color.white);
			btnSelected.setBorder(new BevelBorder(BevelBorder.LOWERED));
			btnSelected.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (!isCanSelected) {//如果不可以选择,则退出
						setSelected(false);
						return;
					}
					setSelected(true);
//					listener.hasSelected(StepNodePanel.this);
				}

			});

			pnlTop.add(btnSelected, BorderLayout.NORTH);
		} else {
			JLabel lbl = new FLabel();
			lbl.setPreferredSize(new Dimension(20, 20));
			pnlTop.add(lbl, BorderLayout.NORTH);
		}

		this.setLayout(new BorderLayout());
		pnlTop.add(txtTitle, BorderLayout.CENTER);
		this.add(pnlTop, BorderLayout.CENTER);
		int width = 100;
		if (!isLastNode) {
			nextArrow = new ImagePanel(IMG_ARROW);
			FPanel pnlImage = new FPanel();
			pnlImage.setLayout(new BorderLayout());
			JLabel lbl1 = new JLabel();
			lbl1.setPreferredSize(new Dimension(20, 15));
			JLabel lbl2 = new JLabel();
			lbl2.setPreferredSize(new Dimension(20, 10));
			pnlImage.add(lbl1, BorderLayout.NORTH);
			pnlImage.add(nextArrow, BorderLayout.CENTER);
			pnlImage.add(lbl2, BorderLayout.SOUTH);
			this.add(pnlImage, BorderLayout.EAST);
			nextArrow.setPreferredSize(new Dimension(40, 20));
			width = 140;
		}

		this.setPreferredSize(new Dimension(width, 60));
		//		this.setBorder(new BevelBorder(BevelBorder.LOWERED));

	}
	
	/**
	 * 获取步骤信息
	 * @return
	 */
	public int getStepInfo(){
		return this.step;
	}
}
