package gov.nbcs.rp.generateBudget.ui;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.SearchPanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.generateBudget.action.GeBudgetAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;




public class GlSetDivToDiv extends FDialog
{

	private static final long serialVersionUID = 1L;

	private CustomTree treeDiv = null;
	public boolean isOK = false;
	private DataSet dsDiv;
	private List lBudgetData = null;



	public GlSetDivToDiv(List lBudgetData)
	{
		super(Global.mainFrame);
		initData();
		getContentPane().add(getBasePanel());
		this.lBudgetData = lBudgetData;

	}


	private FPanel getBasePanel()
	{
		FPanel pnlBase = new FPanel();
		RowPreferedLayout layBase = new RowPreferedLayout(1);
		pnlBase.setLayout(layBase);

		FButton btnOK = new FButton("", "确定");
		btnOK.setIcon("images/rp/save.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

		FButton btnCancel = new FButton("", "取消");
		btnCancel.setIcon("images/rp/close.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

		FToolBar toolbar = new FToolBar();
		FPanel pnlTop = new FPanel();

		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);

		RowPreferedLayout layTop = new RowPreferedLayout(1);
		pnlTop.setLayout(layTop);
		layTop.setRowHeight(50);
		pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));

		SearchPanel sp = new SearchPanel(treeDiv, "DIV_CODE", "DIV_NAME");
		pnlBase.addControl(pnlTop, new TableConstraints(2, 1, false, true));
		pnlBase.addControl(sp, new TableConstraints(1, 1, false, true));
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(treeDiv);
		pnlBase.addControl(pnlTree, new TableConstraints(10, 1, true, true));
		btnOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					isOK = true;
					String enToEn_id = "";
					String enToEn_code = "";
					String enToEn_name = "";
					MyTreeNode node = treeDiv.getSelectedNode();
					if (node == null)
					{
						JOptionPane.showMessageDialog(Global.mainFrame, "请选择关联单位！");
						return;
					}
					if (node.isLeaf() == false)
					{
						JOptionPane.showMessageDialog(Global.mainFrame, "请选择子级关联单位！");
						return;
					}
					if (node != null && node != treeDiv.getRoot())
					{
						PfTreeNode pNode = (PfTreeNode) node.getUserObject();
						enToEn_id = pNode.getValue();
						String sCode_Name = pNode.getShowContent();
						String[] sCode_Names = sCode_Name.split(" ");
						enToEn_code = sCode_Names[0];
						enToEn_name = sCode_Names[1];
					}
					GeBudgetAction.getMethod().setGLToDiv(lBudgetData, enToEn_id, enToEn_code, enToEn_name);
					JOptionPane.showMessageDialog(Global.mainFrame, "设置关联单位成功！");
					setVisible(false);
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "设置关联单位失败！");
				}
			}
		});
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					isOK = false;
					setVisible(false);
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.setSize(400, 600);
		pnlBase.setLeftInset(10);
		pnlBase.setRightInset(10);
		pnlBase.setBottomInset(10);
		this.getContentPane().add(pnlBase);
		this.dispose();
		this.setTitle("关联单位选择");
		this.setModal(true);
		return pnlBase;
	}


	private void initData()
	{
		try
		{
			this.dsDiv = GeBudgetAction.getMethod().getDivTreePop(Global.loginYear, 1);
			this.treeDiv = new CustomTree("单位信息表", this.dsDiv, "En_ID", "CODE_NAME", "PARENT_ID", null, "DIV_CODE", false);

			treeDiv.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						MyTreeNode node = treeDiv.getSelectedNode();
						if (node != null && node.isLeaf())
						{
							try
							{
								isOK = true;
								String enToEn_id = "";
								String enToEn_code = "";
								String enToEn_name = "";
								MyTreeNode node1 = treeDiv.getSelectedNode();
								if (node == null)
								{
									JOptionPane.showMessageDialog(Global.mainFrame, "请选择关联单位！");
									return;
								}
								if (node.isLeaf() == false)
								{
									JOptionPane.showMessageDialog(Global.mainFrame, "请选择子级关联单位！");
									return;
								}
								if (node1 != null && node1 != treeDiv.getRoot())
								{
									PfTreeNode pNode = (PfTreeNode) node.getUserObject();
									enToEn_id = pNode.getValue();
									String sCode_Name = pNode.getShowContent();
									String[] sCode_Names = sCode_Name.split(" ");
									enToEn_code = sCode_Names[0];
									enToEn_name = sCode_Names[1];
								}
								GeBudgetAction.getMethod().setGLToDiv(lBudgetData, enToEn_id, enToEn_code, enToEn_name);
								JOptionPane.showMessageDialog(Global.mainFrame, "设置关联单位成功！");
								setVisible(false);
							}
							catch (Exception e1)
							{
								JOptionPane.showMessageDialog(Global.mainFrame, "设置关联单位失败！");
							}
						}
					}
				}
			});
			// tree.setIsCheckBoxEnabled(true);
			// setTreeSelect();
		}
		catch (Exception ee)
		{}
	}
}
